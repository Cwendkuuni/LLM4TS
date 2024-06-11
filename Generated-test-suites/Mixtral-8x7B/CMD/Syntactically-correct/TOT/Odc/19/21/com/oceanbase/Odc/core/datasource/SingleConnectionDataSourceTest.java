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
import java.util.Arrays;
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
    private ExecutorService executor;

    @Before
    public void setUp() {
        executor = Executors.newFixedThreadPool(10);
        connectionInitializer = new ConnectionInitializerImpl();
        dataSource = new SingleConnectionDataSource(true);
        dataSource.addInitializer(connectionInitializer);
    }

    @After
    public void tearDown() throws Exception {
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(3));
    }

    @Test
    public void testGetConnectionWithInvalidUsernameAndPassword() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalid_username", "invalid_password"));
    }

    @Test
    public void testGetConnectionWithNullUsernameAndPassword() {
        assertThrows(SQLException.class, () -> dataSource.getConnection(null, null));
    }

    @Test
    public void testGetConnectionWithSameUsernameAndPassword() throws SQLException {
        Connection connection1 = dataSource.getConnection("test", "test");
        Connection connection2 = dataSource.getConnection("test", "test");
        assertEquals(connection1, connection2);
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.getConnection();
        verify(connectionInitializer, times(1)).init(any(Connection.class));
        dataSource.resetConnection();
        verify(connectionInitializer, times(2)).init(any(Connection.class));
    }

    @Test
    public void testAutoCommit() throws SQLException {
        dataSource.setAutoCommit(true);
        Connection connection = dataSource.getConnection();
        assertEquals(true, connection.getAutoCommit());

        dataSource.setAutoCommit(false);
        connection = dataSource.getConnection();
        assertEquals(false, connection.getAutoCommit());
    }

    @Test
    public void testConnectionProxy() throws SQLException {
        Connection connection = dataSource.getConnection();
        InvocationHandler handler = Proxy.getInvocationHandler(connection);
        assertTrue(handler instanceof CloseIgnoreInvocationHandler);
    }

    @Test
    public void testLockWithMultipleThreads() throws InterruptedException, SQLException {
        List<Connection> connections = Arrays.asList(dataSource.getConnection(), dataSource.getConnection(),
                dataSource.getConnection());
        for (Connection connection : connections) {
            executor.submit(() -> {
                try {
                    Connection connectionProxy = getConnectionProxy(connection);
                    connectionProxy.createStatement().executeQuery("SELECT 1");
                } catch (SQLException e) {
                    //Ignore
                }
            });
        }
        executor.awaitTermination(1, TimeUnit.SECONDS);
    }

    private Connection getConnectionProxy(Connection connection) {
        SingleConnectionDataSource proxyDataSource = Mockito.spy(dataSource);
        Mockito.when(proxyDataSource.getConnectionProxy(connection)).thenReturn(connection);
        return proxyDataSource.getConnectionProxy(connection);
    }

    @Test
    public void testLockTimeout() {
        dataSource.setTimeOutMillis(1);
        assertThrows(ConflictException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testLockInterrupted() throws InterruptedException {
        Thread.currentThread().interrupt();
        assertThrows(InterruptedException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertFalse(connection.isValid(3));
    }

    @Test
    public void testCloseIgnore() throws SQLException {
        Connection connection = dataSource.getConnection();
        InvocationHandler handler = Proxy.getInvocationHandler(connection);
        ((CloseIgnoreInvocationHandler) handler).invoke(connection, Connection.class.getMethod("close"), null);
        assertTrue(connection.isValid(3));
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithLockOccupied() throws SQLException {
        dataSource.getConnectionProxy(dataSource.getConnection());
        dataSource.getConnectionProxy(dataSource.getConnection());
    }
}