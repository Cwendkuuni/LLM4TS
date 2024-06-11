import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connection.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connection.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.event.EventPublisher;
import com.oceanbase.odc.core.shared.event.EventPublisherImpl;
import com.oceanbase.odc.test.helper.DriverLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private DataSource dataSourceWithoutAutoReconnect;
    private EventPublisher eventPublisher;
    private ConnectionInitializer connectionInitializer;
    private Lock lock;

    @Before
    public void setUp() throws Exception {
        DriverLoader.loadDriver();
        eventPublisher = new EventPublisherImpl();
        connectionInitializer = new ConnectionInitializerImpl();
        lock = new ReentrantLock();

        dataSource = new SingleConnectionDataSource();
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dataSource.setAutoCommit(true);
        dataSource.setAutoReconnect(true);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.addInitializer(connectionInitializer);

        dataSourceWithoutAutoReconnect = new SingleConnectionDataSource();
        dataSourceWithoutAutoReconnect.setUsername("testUser");
        dataSourceWithoutAutoReconnect.setPassword("testPassword");
        dataSourceWithoutAutoReconnect.setDriverClassName("org.h2.Driver");
        dataSourceWithoutAutoReconnect.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dataSourceWithoutAutoReconnect.setAutoCommit(true);
        dataSourceWithoutAutoReconnect.setAutoReconnect(false);
    }

    @After
    public void tearDown() throws Exception {
        if (dataSource != null) {
            dataSource.close();
        }
        if (dataSourceWithoutAutoReconnect != null) {
            dataSourceWithoutAutoReconnect.close();
        }
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isClosed());

        connection = dataSource.getConnection();
        assertFalse(connection.isClosed());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        dataSource.setUsername("invalidUser");
        dataSource.setPassword("invalidPassword");
        dataSource.getConnection("testUser", "testPassword");
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = dataSource.getConnectionProxy(dataSource.getConnection());
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithConnectionOccupied() throws SQLException {
        dataSource.getConnectionProxy(dataSource.getConnection());
        dataSource.getConnectionProxy(dataSource.getConnection());
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.getConnection();
        dataSource.resetConnection();
        assertTrue(dataSource.getConnection().isClosed());
    }

    @Test
    public void testAddInitializer() throws SQLException {
        dataSource.getConnection();
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                connection.setAutoCommit(false);
            }
        });
        assertFalse(dataSource.getConnection().getAutoCommit());
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.getConnection();
        dataSource.close();
        assertTrue(dataSource.getConnection().isClosed());
    }

    @Test
    public void testGetConnectionWithAutoReconnectFalse() throws SQLException {
        dataSourceWithoutAutoReconnect.getConnection();
        dataSourceWithoutAutoReconnect.getConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithAutoReconnectFalseAndClosedConnection() throws SQLException {
        dataSourceWithoutAutoReconnect.getConnection();
        dataSourceWithoutAutoReconnect
                .getConnection()
                .close();
        dataSourceWithoutAutoReconnect.getConnection();
    }
}