import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.ODCException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer initializer;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        initializer = new ConnectionInitializerImpl();
        dataSource = new SingleConnectionDataSource("jdbc:mysql://localhost:3306/testdb", "root", "password", true);
        dataSource.addInitializer(initializer);

        eventPublisher = mock(EventPublisher.class);
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
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = dataSource.getConnection("testUser", "testPassword");
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        dataSource.getConnection("invalidUser", "invalidPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.getConnection();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() throws SQLException {
        dataSource.getConnection();
        assertNotNull(initializer.getConnection());
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.getConnection();
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(dataSource.getAutoCommit());
    }

    @Test
    public void testGetConnectionProxy() {
        Connection connection = Mockito.mock(Connection.class);
        Lock lock = Mockito.mock(Lock.class);

        Connection proxyConnection = dataSource.getConnectionProxy(connection);

        assertNotNull(proxyConnection);
        assertTrue(proxyConnection instanceof Proxy);

        InvocationHandler handler = Proxy.getInvocationHandler(proxyConnection);
        assertTrue(handler instanceof CloseIgnoreInvocationHandler);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithLockedConnection() throws ConflictException {
        Connection connection = Mockito.mock(Connection.class);
        Lock lock = new ReentrantLock();
        when(connection.isClosed()).thenReturn(false);

        dataSource.getConnectionProxy(connection);

        try {
            dataSource.getConnectionProxy(connection);
        } finally {
            lock.lock();
            dataSource.getConnectionProxy(connection);
            lock.unlock();
        }
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        dataSource.onConnectionReset(connection);

        verify(eventPublisher, times(1)).publishEvent(Mockito.any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        dataSource.initConnection(connection);

        verify(connection, times(1)).createStatement();
        verify(connection, times(1)).setAutoCommit(dataSource.getAutoCommit());
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        dataSource.onConnectionCreated(connection);

        verify(connection, times(1)).setAutoCommit(dataSource.getAutoCommit());
    }

    @Test
    public void testCloseIgnoreInvocationHandlerCloseMethod() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        Lock lock = new ReentrantLock();

        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
        handler.invoke(connection, Method.class, new Object[0]);

        verify(connection, times(1)).close();
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testCloseIgnoreInvocationHandlerIsClosedMethod() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        Lock lock = new ReentrantLock();

        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
        handler.invoke(connection, Method.class, new Object[0]);

        verify(connection, times(1)).isClosed();
    }

    @Test
    public void testExecuteQuery() throws SQLException {
        String query = "SELECT 1 + 1";
        String expectedResult = "2";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            resultSet.next();
            assertEquals(expectedResult, resultSet.getString(1));
        }
    }

    @Test
    public void testExecuteUpdate() throws SQLException {
        String update = "CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY)";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            int rowsAffected = statement.executeUpdate(update);
            assertEquals(0, rowsAffected);
        }
    }

    @Test
    public void testConcurrentGetConnection() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Connection> connections = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                try {
                    connections.add(dataSource.getConnection());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(1, connections.size());
    }

    @Test(expected = ConflictException.class)
    public void testConcurrentGetConnectionWithLock() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Connection> connections = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                try {
                    connections.add(dataSource.getConnectionProxy(dataSource.connection));
                } catch (ODCException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        dataSource.getConnectionProxy(dataSource.connection);
    }
}