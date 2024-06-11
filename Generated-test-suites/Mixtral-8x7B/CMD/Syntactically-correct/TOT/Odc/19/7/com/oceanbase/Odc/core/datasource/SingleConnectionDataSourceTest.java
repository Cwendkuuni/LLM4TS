import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Mock
    private EventPublisher eventPublisher;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        dataSource = new SingleConnectionDataSource();
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
        assertTrue(connection.isValid(1));
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");

        Connection connection = dataSource.getConnection("testUser", "testPassword");
        assertNotNull(connection);
        assertTrue(connection.isValid(1));
    }

    @Test
    public void testGetConnectionWithInvalidUsernameAndPassword() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.setConnection(DriverManager.getConnection("jdbc:h2:mem:test"));

        dataSource.resetConnection();

        assertFalse(dataSource.getConnection().isClosed());
    }

    @Test
    public void testAddInitializer() throws SQLException {
        dataSource.addInitializer(connection -> {
            connection.setAutoCommit(false);
        });

        dataSource.getConnection();

        verify(eventPublisher).publishEvent(any());
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.setConnection(DriverManager.getConnection("jdbc:h2:mem:test"));
        dataSource.getConnection();

        dataSource.close();

        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testProxyConnectionCloseMethod() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:test");
        InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, new Object());
        Connection proxy = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[] {Connection.class}, handler);

        assertEquals(connection, proxy.unwrap(Connection.class));
        assertFalse(proxy.isClosed());
    }

    @Test
    public void testProxyConnectionIsWrapperForMethod() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:test");
        InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, new Object());
        Connection proxy = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[] {Connection.class}, handler);

        assertTrue(proxy.isWrapperFor(Connection.class));
    }

    @Test
    public void testLockConnection() throws SQLException, InterruptedException {
        dataSource.getConnection();
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Runnable runnable1 = () -> {
            Connection connection = dataSource.getConnection();
            try {
                assertTrue(connection.isClosed());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };

        Runnable runnable2 = () -> {
            Connection connection = dataSource.getConnection();
            try {
                assertTrue(connection.isClosed());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };

        executorService.submit(runnable1);
        executorService.submit(runnable2);

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }

    @Test
    public void testLockTimeoutConnection() {
        assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(DriverManager.getConnection("jdbc:h2:mem:test")));
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:test");
        dataSource.setLock(new ReentrantLock());
        Connection proxyConnection = dataSource.getConnectionProxy(connection);
        assertNotSame(connection, proxyConnection);
    }
}