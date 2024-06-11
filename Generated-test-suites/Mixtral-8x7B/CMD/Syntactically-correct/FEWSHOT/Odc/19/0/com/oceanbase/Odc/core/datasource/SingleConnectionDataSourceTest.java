import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

@Slf4j
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    private ConnectionInitializer connectionInitializer;

    private ConnectionInitializer connectionInitializer2;

    private ConnectionInitializer connectionInitializer3;

    private EventPublisher eventPublisher;

    private Connection connection;

    private ExecutorService executorService;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        dataSource = new SingleConnectionDataSource();

        connectionInitializer = new ConnectionInitializerImpl();

        connectionInitializer2 = new ConnectionInitializerImpl();

        connectionInitializer3 = new ConnectionInitializerImpl();

        eventPublisher = mock(EventPublisher.class);

        dataSource.addInitializer(connectionInitializer);
        dataSource.addInitializer(connectionInitializer2);
        dataSource.addInitializer(connectionInitializer3);

        connection = DriverManager.getConnection("jdbc:h2:mem:test");

        executorService = Executors.newSingleThreadExecutor();
    }

    @After
    public void teardown() throws Exception {
        executorService.shutdownNow();
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void testInitConnection() throws Exception {
        dataSource.initConnection(connection);

        verify(connectionInitializer, times(1)).init(connection);
        verify(connectionInitializer2, times(1)).init(connection);
        verify(connectionInitializer3, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionCreated() throws Exception {
        dataSource.onConnectionCreated(connection);

        assertTrue(dataSource.getConnection() == connection);
    }

    @Test
    public void testGetConnection() throws Exception {
        assertTrue(dataSource.getConnection() == connection);
    }

    @Test
    public void testGetConnection_Reconnect() throws Exception {
        dataSource.connection = null;
        dataSource.getConnection();

        verify(connectionInitializer, times(1)).init(connection);
        verify(connectionInitializer2, times(1)).init(connection);
        verify(connectionInitializer3, times(1)).init(connection);
    }

    @Test
    public void testGetConnection_WrongCredentials() throws Exception {
        dataSource.username = "wrong";

        try {
            dataSource.getConnection("", "");
            fail("Expected SQLException");
        } catch (SQLException e) {
            // expected
        }
    }

    @Test
    public void testResetConnection() throws Exception {
        dataSource.resetConnection();

        assertTrue(dataSource.getConnection() == connection);
        assertTrue(dataSource.getConnectionProxy(connection) == connection);
    }

    @Test
    public void testResetConnection_NoInitializers() throws Exception {
        dataSource.initializerList.clear();

        dataSource.resetConnection();

        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() throws Exception {
        dataSource.addInitializer(connectionInitializer);

        assertEquals(Arrays.asList(connectionInitializer, connectionInitializer2,connectionInitializer3), dataSource.getInitializerList());
    }

    @Test
    public void testGetInitializerList() throws Exception {
        assertEquals(Arrays.asList(connectionInitializer, connectionInitializer2,connectionInitializer3), dataSource.getInitializerList());
    }

    @Test
    public void testClose() throws Exception {
        dataSource.close();

        assertTrue(dataSource.getConnection() == null);
    }

    @Test
    public void testClose_NoConnection() throws Exception {
        dataSource.connection = null;

        dataSource.close();
    }

    @Test
    public void testPrepareConnection() throws Exception {
        dataSource.prepareConnection(connection);

        assertEquals(dataSource.getAutoCommit(), connection.getAutoCommit());
    }

    @Test
    public void testTryLock() throws Exception {
        assertTrue(dataSource.tryLock());

        assertFalse(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() throws Exception {
        InvocationHandler handler = dataSource.getConnectionProxy(connection);

        Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
            new Class[] {Connection.class}, handler);

        assertEquals(proxyConnection, dataSource.getConnectionProxy(connection));
    }

    @Test
    public void testOnConnectionReset() throws Exception {
        dataSource.onConnectionReset(connection);

        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws Exception {
        dataSource.innerCreateConnection();

        verify(connectionInitializer, times(1)).init(connection);
        verify(connectionInitializer2, times(1)).init(connection);
        verify(connectionInitializer3, times(1)).init(connection);

        assertTrue(dataSource.getConnection() == connection);
    }

    @Test(expected = IllegalStateException.class)
    public void testInnerCreateConnection_AlreadyConnected() throws Exception {
        dataSource.innerCreateConnection();
        dataSource.innerCreateConnection();
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws Exception {
        InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, new ReentrantLock());

        Method closeMethod = Connection.class.getDeclaredMethod("close");
        handler.invoke(null, connection, closeMethod, new Object[0]);

        assertFalse(connection.isClosed());
    }

    @Test
    public void testConcurrentGetConnection() throws Exception {
        Runnable task = () -> {
            try {
                Connection connection = dataSource.getConnection();
                connection.close();
            } catch (SQLException e) {
                fail(e.getMessage());
            }
        };

        executorService.execute(task);
        executorService.execute(task);

        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }

    @Test
    public void testConcurrentGetConnection_Reconnect() throws Exception {
        dataSource.connection = null;

        Runnable task = () -> {
            try {
                Connection connection = dataSource.getConnection();
                connection.close();
            } catch (SQLException e) {
                fail(e.getMessage());
            }
        };

        executorService.execute(task);
        executorService.execute(task);

        executorService.awaitTermination(1, TimeUnit.SECONDS);

        verify(connectionInitializer, times(2)).init(connection);
        verify(connectionInitializer2, times(2)).init(connection);
        verify(connectionInitializer3, times(2)).init(connection);
    }

    @Test(expected = ConflictException.class)
    public void testConcurrentGetConnection_Occupied() throws Exception {
        dataSource.lock = new ReentrantLock();

        Runnable task = () -> {
            dataSource.getConnectionProxy(connection);
        };

        executorService.execute(task);
        executorService.execute(task);

        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }
}