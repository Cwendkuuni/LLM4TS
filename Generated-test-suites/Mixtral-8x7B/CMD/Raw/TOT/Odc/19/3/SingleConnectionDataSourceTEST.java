import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(true);
        dataSource.addInitializer(new ConnectionInitializerImpl());
    }

    @After
    public void tearDown() throws Exception {
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(1));

        // Test auto-reconnect
        dataSource.resetConnection();
        connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(1));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.setUsername("invalid");
        dataSource.setPassword("invalid");
        dataSource.getConnection("username", "password");
    }

    @Test
    public void testAddInitializer() throws SQLException {
        dataSource.addInitializer(new ConnectionInitializerImpl());
        Connection connection = dataSource.getConnection();

        // Verify initializer is called
        verify(connection).setAutoCommit(false);
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection1 = dataSource.getConnection();
        Connection connection2 = dataSource.getConnection();

        // Make sure same connection is returned
        assertEquals(connection1, connection2);

        dataSource.resetConnection();

        // Make sure connection is reset
        Connection connection3 = dataSource.getConnection();
        assertNotEquals(connection1, connection3);
        assertTrue(connection3.isValid(1));
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();

        // Verify connection is closed
        assertTrue(connection.isClosed());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = dataSource.getConnection();

        // Verify proxy is created and lock is obtained
        assertTrue(connection instanceof CloseIgnoreInvocationHandler.ConnectionProxy);
        assertTrue(((CloseIgnoreInvocationHandler.ConnectionProxy) connection).getLock().isHeldByCurrentThread());

        // Verify unlocking the lock
        ((CloseIgnoreInvocationHandler.ConnectionProxy) connection).getLock().unlock();
        assertFalse(((CloseIgnoreInvocationHandler.ConnectionProxy) connection).getLock().isHeldByCurrentThread());

        // Verify close is suppressed
        ((CloseIgnoreInvocationHandler.ConnectionProxy) connection).invokeClose();
        assertTrue(connection.isValid(1));
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithLockedConnection() throws SQLException {
        dataSource.getConnection();

        // Try to get a second connection without unlocking the lock
        dataSource.getConnection();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithTimeout() throws SQLException {
        // Set timeout to 1ms
        dataSource.setTimeOutMillis(1);

        // Wait for 2ms then try to get the connection
        Thread.sleep(2);
        dataSource.getConnection();
    }
}