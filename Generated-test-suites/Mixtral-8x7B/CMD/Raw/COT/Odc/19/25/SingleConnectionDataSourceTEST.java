import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer connectionInitializer;
    private ExecutorService executorService;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();

        connectionInitializer = new ConnectionInitializerImpl();
        dataSource.addInitializer(connectionInitializer);

        executorService = Executors.newFixedThreadPool(10);
    }

    @After
    public void tearDown() throws Exception {
        executorService.shutdown();
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        String username = "testUser";
        String password = "testPassword";
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        Connection connection = dataSource.getConnection(username, password);
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithInvalidUsernameAndPassword() {
        String invalidUsername = "invalidUser";
        String invalidPassword = "invalidPassword";

        assertThrows(SQLException.class, () -> dataSource.getConnection(invalidUsername, invalidPassword));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.getConnection();

        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);

        dataSource.resetConnection();
        verify(connection).close();
    }

    @Test
    public void testAddInitializer() {
        int initializerListSizeBefore = dataSource.getInitializerList().size();
        dataSource.addInitializer(connectionInitializer);
        int initializerListSizeAfter = dataSource.getInitializerList().size();

        assertEquals(initializerListSizeBefore + 1, initializerListSizeAfter);
    }

    @Test
    public void testClose() {
        dataSource.getConnection();

        dataSource.close();
        assertThrows(IllegalStateException.class, dataSource::getConnection);
    }

    @Test
    public void testAutoCommit() throws SQLException {
        dataSource.setAutoCommit(true);

        Connection connection = dataSource.getConnection();
        assertEquals(true, connection.getAutoCommit());
    }

    @Test
    public void testIsAutoCommit() {
        dataSource.setAutoCommit(false);

        assertEquals(false, dataSource.isAutoCommit());
    }

    @Test
    public void testTryLock() throws InterruptedException {
        dataSource.setTimeOutMillis(100);

        assertTrue(dataSource.tryLock());
        assertFalse(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connection = mock(Connection.class);

        Object proxy = dataSource.getConnectionProxy(connection);

        assertTrue(Proxy.isProxyClass(proxy.getClass()));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        dataSource.innerCreateConnection();

        verify(dataSource, times(1)).newConnectionFromDriver(dataSource.getUsername(),
                dataSource.getPassword());
    }

    @Test
    public void testConnectionResetEvent() throws SQLException {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);

        dataSource.getConnection();
        verify(eventPublisher, times(0)).publishEvent(any(ConnectionResetEvent.class));

        dataSource.resetConnection();
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnectionWithMultipleThreads() throws SQLException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                try {
                    dataSource.getConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testConflictException() {
        dataSource.setTimeOutMillis(100);
        assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(mock(Connection.class)));
    }
}