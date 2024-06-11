import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerAdapter;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer connectionInitializer;
    private ExecutorService executorService;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        connectionInitializer = new ConnectionInitializerAdapter() {
            @Override
            public void init(Connection connection) throws Exception {
                // Do nothing
            }
        };
        dataSource.addInitializer(connectionInitializer);
        executorService = Executors.newSingleThreadExecutor();
    }

    @After
    public void tearDown() throws Exception {
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection1 = dataSource.getConnection();
        Connection connection2 = dataSource.getConnection();
        assertEquals(connection1, connection2);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        String username = "testUser";
        String password = "testPassword";
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        assertEquals(username, dataSource.getUsername());
        assertEquals(password, dataSource.getPassword());
        assertThrows(SQLException.class, () -> dataSource.getConnection("wrongUser", "wrongPassword"));
        Connection connection = dataSource.getConnection();
        assertEquals(username, connection.getMetaData().getUserName());
    }

    @Test
    public void testGetConnectionWithAutoCommit() throws SQLException {
        dataSource.setAutoCommit(false);
        assertFalse(dataSource.getConnection().getAutoCommit());
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.getConnection().getAutoCommit());
    }

    @Test
    public void testGetConnectionWithInitializers() throws Exception {
        List<ConnectionInitializer> initializers = new LinkedList<>();
        initializers.add(connectionInitializer);
        initializers.add(new ConnectionInitializerAdapter() {
            @Override
            public void init(Connection connection) throws Exception {
                connection.createStatement().execute("CREATE TABLE test_table (id INT)");
            }
        });

        dataSource = new SingleConnectionDataSource(true, initializers);
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");

        Connection connection = dataSource.getConnection();
        assertFalse(connection.isClosed());

        // Assert that the table was created
        assertFalse(connection.createStatement().execute("SELECT 1 FROM test_table WHERE id = 1"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.getConnection().createStatement().execute("CREATE TABLE test_table (id INT)");
        dataSource.resetConnection();
        assertThrows(SQLException.class, () -> dataSource.getConnection().createStatement().execute("SELECT 1 FROM test_table WHERE id = 1"));
    }

    @Test
    public void testAddInitializer() throws Exception {
        List<ConnectionInitializer> initializers = new LinkedList<>();
        initializers.add(connectionInitializer);
        initializers.add(new ConnectionInitializerAdapter() {
            @Override
            public void init(Connection connection) throws Exception {
                connection.createStatement().execute("CREATE TABLE test_table (id INT)");
            }
        });

        dataSource = new SingleConnectionDataSource(true, initializers);
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");

        Connection connection = dataSource.getConnection();
        assertFalse(connection.isClosed());

        // Assert that the table was created
        assertFalse(connection.createStatement().execute("SELECT 1 FROM test_table WHERE id = 1"));

        // Add another initializer
        ConnectionInitializer anotherInitializer = new ConnectionInitializerAdapter() {
            @Override
            public void init(Connection connection) throws Exception {
                connection.createStatement().execute("INSERT INTO test_table (id) VALUES (1)");
            }
        };
        dataSource.addInitializer(anotherInitializer);

        // Reset the connection to apply the new initializer
        dataSource.resetConnection();

        // Assert that the table contains a row
        assertTrue(connection.createStatement().execute("SELECT 1 FROM test_table WHERE id = 1"));
    }

    @Test
    public void testConnectionProxy() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Connection connection = Mockito.mock(Connection.class);
        InvocationHandler invocationHandler = dataSource.getConnectionProxy(connection);

        // Call the proxy's hashCode method
        Method hashCodeMethod = Object.class.getMethod("hashCode");
        int hashCode = (int) invocationHandler.invoke(invocationHandler, connection, hashCodeMethod, new Object[0]);
        assertEquals(connection.hashCode(), hashCode);

        // Call the proxy's equals method
        Method equalsMethod = Object.class.getMethod("equals", Object.class);
        assertTrue(invocationHandler.invoke(invocationHandler, connection, equalsMethod, new Object[]{connection}));
        assertFalse(invocationHandler.invoke(invocationHandler, connection, equalsMethod, new Object[]{Mockito.mock(Connection.class)}));

        // Call the proxy's unwrap method
        Method unwrapMethod = Class.class.getMethod("unwrap", Class.class);
        assertEquals(connection, invocationHandler.invoke(invocationHandler, connection, unwrapMethod, new Class[]{Connection.class}));

        // Call the proxy's isWrapperFor method
        Method isWrapperForMethod = Class.class.getMethod("isWrapperFor", Class.class);
        assertTrue((Boolean) invocationHandler.invoke(invocationHandler, connection, isWrapperForMethod, new Class[]{Connection.class}));
        assertFalse((Boolean) invocationHandler.invoke(invocationHandler, connection, isWrapperForMethod, new Class[]{Integer.class}));

        // Call the proxy's close method
        Method closeMethod = Connection.class.getMethod("close");
        invocationHandler.invoke(invocationHandler, connection, closeMethod, new Object[0]);
        verify(connection, times(1)).close();

        // Call the proxy's isClosed method
        Method isClosedMethod = Connection.class.getMethod("isClosed");
        assertTrue((Boolean) invocationHandler.invoke(invocationHandler, connection, isClosedMethod, new Object[0]));
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertThrows(SQLException.class, () -> connection.createStatement());
    }

    @Test
    public void testAutoReconnect() throws SQLException, InterruptedException {
        dataSource.setAutoReconnect(true);

        // Get the initial connection
        Connection connection1 = dataSource.getConnection();

        // Close the connection
        connection1.close();

        // Wait for the connection to be reconnected
        Thread.sleep(100);

        // Get the new connection
        Connection connection2 = dataSource.getConnection();

        // Assert that the connections are not the same
        assertNotEquals(connection1, connection2);
    }

    @Test
    public void testTryLock() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertTrue(dataSource.tryLock());

        // Release the lock
        dataSource.getConnectionProxy(connection).close();

        // Try to lock again
        assertFalse(dataSource.tryLock());
    }

    @Test
    public void testConflictException() {
        SingleConnectionDataSource conflictDataSource = new SingleConnectionDataSource(true, 10);
        conflictDataSource.addInitializer(connectionInitializer);

        // Simulate multiple threads trying to get the connection
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                try {
                    conflictDataSource.getConnection();
                } catch (SQLException e) {
                    assertTrue(e instanceof ConflictException);
                }
            });
        }

        // Wait for all threads to complete
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }
}