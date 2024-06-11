import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectionInitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.SQLException;
import com.oceanbase.odc.core.shared.listener.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.listener.EventPublisherImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private DataSource dataSourceWithoutAutoReconnect;
    private ConnectionInitializer connectionInitializer;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource();
        dataSourceWithoutAutoReconnect = new SingleConnectionDataSource(false);

        connectionInitializer = new ConnectionInitializerImpl();

        eventPublisher = new EventPublisherImpl();
        dataSource.setEventPublisher(eventPublisher);
    }

    @After
    public void tearDown() throws Exception {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetConnectionMethods() throws SQLException {
        // Test getConnection() method
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);

        // Test getConnection(username, password) method
        SQLException exception = assertThrows(SQLException.class, () ->
                dataSource.getConnection("invalid_username", "invalid_password"));
        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    public void testResetConnectionMethod() throws SQLException {
        // Test resetConnection() method
        Connection originalConnection = dataSource.getConnection();
        dataSource.resetConnection();
        Connection newConnection = dataSource.getConnection();
        assertNotSame(originalConnection, newConnection);
    }

    @Test
    public void testAddInitializerMethod() throws Exception {
        // Test addInitializer() method
        dataSource.addInitializer(connectionInitializer);
        dataSource.initConnection(DriverManager.getConnection("jdbc:any-database", "username", "password"));
        assertTrue(connectionInitializer.isInvoked());
    }

    @Test
    public void testCloseMethod() throws SQLException {
        // Test close() method
        dataSource.getConnection();
        dataSource.close();
        assertThrows(SQLException.class, dataSource::getConnection);
    }

    @Test
    public void testAutoCommitProperty() throws SQLException {
        // Test AutoCommit property
        assertFalse(dataSource.getConnection().getAutoCommit());
        dataSource.setAutoCommit(true);
        assertTrue(dataSource.getConnection().getAutoCommit());
    }

    @Test
    public void testAutoReconnectProperty() throws SQLException {
        // Test AutoReconnect property
        dataSourceWithoutAutoReconnect.getConnection();
        assertThrows(SQLException.class, dataSourceWithoutAutoReconnect::resetConnection);
    }

    @Test
    public void testLockingMechanism() throws Exception {
        // Test Locking Mechanism
        Connection connection = dataSource.getConnection();
        ReentrantLock lock = (ReentrantLock) Proxy.getInvocationHandler(connection).
                invoke(connection, "getLock", new Object[0]);
        assertFalse(lock.isHeldByCurrentThread());

        dataSource.getConnectionProxy(connection);
        assertTrue(lock.isHeldByCurrentThread());

        lock.unlock();
    }

    @Test
    public void testConnectionResetEvent() throws Exception {
        // Test ConnectionResetEvent
        dataSource.addInitializer(new ConnectionInitializerImpl() {
            @Override
            public void init(Connection connection) throws Exception {
                connection.createStatement().execute("SELECT 1");
            }
        });

        dataSource.getConnection();
        dataSource.resetConnection();

        ConnectionResetEvent event = (ConnectionResetEvent) eventPublisher.getLastEvent();
        assertNotNull(event);
        assertEquals(dataSource.getConnection(), event.getConnection());
    }

    @Test
    public void testConflictException() {
        // Test ConflictException
        assertThrows(ConflictException.class, () -> {
            List<Connection> connections = new LinkedList<>();
            for (int i = 0; i < 10; i++) {
                connections.add(dataSource.getConnection());
            }
        });
    }
}