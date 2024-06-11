import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Mock
    private EventPublisher eventPublisher;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.addInitializer(connection -> {
            // Perform some initialization logic
        });
        dataSource.setEventPublisher(eventPublisher);
    }

    @After
    public void tearDown() throws SQLException {
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernamePassword() throws SQLException {
        when(dataSource.getUsername()).thenReturn("test_user");
        when(dataSource.getPassword()).thenReturn("test_password");

        Connection connection = dataSource.getConnection("test_user", "test_password");
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernamePassword() throws SQLException {
        when(dataSource.getUsername()).thenReturn("invalid_user");
        when(dataSource.getPassword()).thenReturn("invalid_password");

        dataSource.getConnection("test_user", "test_password");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();

        // The connection should now be closed
        try {
            connection.createStatement();
            fail("Expected SQLException");
        } catch (SQLException e) {
            // Expected
        }
    }

    @Test
    public void testAddInitializer() throws SQLException {
        ConnectionInitializer initializer = connection -> {
            // Perform some initialization logic
        };

        dataSource.addInitializer(initializer);

        dataSource.getConnection();

        verify(initializer).init(any(Connection.class));
    }

    @Test
    public void testAutoCommit() throws SQLException {
        dataSource.setAutoCommit(true);
        Connection connection = dataSource.getConnection();
        assertTrue(connection.getAutoCommit());

        dataSource.setAutoCommit(false);
        connection = dataSource.getConnection();
        assertFalse(connection.getAutoCommit());
    }

    @Test
    public void testLockTimeout() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Runnable runnable = () -> {
            try {
                dataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        executor.submit(runnable);

        try {
            dataSource.getConnection();
            fail("Expected ConflictException");
        } catch (ConflictException e) {
            // Expected
        }

        executor.shutdownNow();
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws Exception {
        InvocationHandler handler = Proxy.getInvocationHandler(dataSource.getConnectionProxy(dataSource.getConnection()));
        assertTrue(handler instanceof CloseIgnoreInvocationHandler);

        Method closeMethod = handler.getClass().getDeclaredMethod("close");
        closeMethod.setAccessible(true);
        closeMethod.invoke(handler);

        verify(dataSource.getConnection()).close();
    }

    @Test
    public void testConnectionResetEvent() throws Exception {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();

        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = dataSource.getConnectionProxy(dataSource.getConnection());
        assertSame(connection, dataSource.getConnection());
        assertTrue(connection instanceof Proxy);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        when(dataSource.getConnection().getAutoCommit()).thenReturn(false);
        dataSource.prepareConnection(dataSource.getConnection());

        assertTrue(dataSource.getConnection().getAutoCommit());
    }

    @Test
    public void testInnerCreateConnection() throws Exception {
        dataSource.innerCreateConnection();

        verify(dataSource.getConnection(), times(1)).close();
        assertNotNull(dataSource.getConnection());
    }

    @Test(expected = IllegalStateException.class)
    public void testInnerCreateConnectionWhenConnectionIsNotNull() throws Exception {
        dataSource.setConnection(mock(Connection.class));
        dataSource.innerCreateConnection();
    }

    @Test
    public void testOnConnectionReset() throws Exception {
        dataSource.setEventPublisher(eventPublisher);
        dataSource.onConnectionReset(dataSource.getConnection());

        verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testOnConnectionCreated() throws Exception {
        dataSource.onConnectionCreated(dataSource.getConnection());

        verifyNoMoreInteractions(dataSource.getConnection());
    }

}