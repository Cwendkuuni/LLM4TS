import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer initializer;
    private ExecutorService executorService;

    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource();
        initializer = new ConnectionInitializerImpl();
        dataSource.addInitializer(initializer);
        executorService = Executors.newFixedThreadPool(10);
    }

    @After
    public void tearDown() throws Exception {
        dataSource.close();
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());

        Connection proxyConnection = dataSource.getConnectionProxy(connection);
        assertNotNull(proxyConnection);
        assertFalse(proxyConnection.isClosed());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        String username = "testUser";
        String password = "testPassword";

        dataSource.setUsername(username);
        dataSource.setPassword(password);

        Connection connection = dataSource.getConnection(username, password);
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        String username = "invalidUser";
        String password = "invalidPassword";

        dataSource.getConnection(username, password);
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();

        Connection newConnection = dataSource.getConnection();
        assertNotNull(newConnection);
        assertFalse(newConnection.isClosed());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithLockedConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.getConnectionProxy(connection);
        dataSource.getConnectionProxy(connection);
    }

    @Test
    public void testAddInitializer() throws Exception {
        dataSource.addInitializer(initializer);

        verify(initializer, times(1)).init(any(Connection.class));
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();

        assertTrue(connection.isClosed());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        dataSource.setAutoCommit(true);
        Connection connection = mock(Connection.class);

        dataSource.prepareConnection(connection);

        verify(connection, times(1)).setAutoCommit(true);
    }

    @Test
    public void testConcurrentConnectionGet() throws Exception {
        Runnable runnable = () -> {
            try {
                dataSource.getConnection();
            } catch (SQLException e) {
                fail();
            }
        };

        for (int i = 0; i < 100; i++) {
            executorService.submit(runnable);
        }

        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    public void testTryLock() throws Exception {
        assertTrue(dataSource.tryLock());
        assertFalse(dataSource.tryLock());
    }

    @Test
    public void testInnerCreateConnection() throws Exception {
        Connection connection = dataSource.innerCreateConnection();

        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }
}