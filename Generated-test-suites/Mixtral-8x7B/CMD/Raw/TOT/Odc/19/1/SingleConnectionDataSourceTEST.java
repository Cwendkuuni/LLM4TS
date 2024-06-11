import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleConnectionDataSourceTest.class);

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        // Add a ConnectionInitializer to test initConnection method
        dataSource.addInitializer(new ConnectionInitializerImpl());
    }

    @After
    public void tearDown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    // Test getConnection() method
    @Test
    public void testGetConnection() throws SQLException {
        // Test when connection is null
        assertNull(dataSource.getConnection());

        // Test valid connection
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);

        // Test with autoReconnect = true
        SingleConnectionDataSource dataSourceWithAutoReconnect = new SingleConnectionDataSource(true);
        dataSourceWithAutoReconnect.innerCreateConnection();

        try {
            // Test with invalid username or password
            dataSource.getConnection("invalidUsername", "invalidPassword");
            fail();
        } catch (SQLException e) {
            LOGGER.info("Expected SQLException thrown for invalid username or password");
        }
    }

    // Test getConnection(String username, String password) method
    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        // Test with valid username and password
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(dataSource.getConnection(dataSource.getUsername(), dataSource.getPassword()));

        // Test with invalid username or password
        try {
            dataSource.getConnection("invalidUsername", "invalidPassword");
            fail();
        } catch (SQLException e) {
            LOGGER.info("Expected SQLException thrown for invalid username or password");
        }
    }

    // Test resetConnection() method
    @Test
    public void testResetConnection() throws SQLException {
        // Test when connection is null
        dataSource.resetConnection();

        // Test with valid connection
        Connection connection = dataSource.innerCreateConnection();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());

        // Test with autoReconnect = true and closed connection
        SingleConnectionDataSource dataSourceWithAutoReconnect = new SingleConnectionDataSource(true);
        dataSourceWithAutoReconnect.innerCreateConnection();
        dataSourceWithAutoReconnect.connection.close();
        dataSourceWithAutoReconnect.resetConnection();
        assertNotNull(dataSourceWithAutoReconnect.getConnection());
    }

    // Test addInitializer() method
    @Test
    public void testAddInitializer() {
        // Test adding a ConnectionInitializer
        int initializerCountBefore = dataSource.getInitializerList().size();
        ConnectionInitializer initializer = new ConnectionInitializerImpl();
        dataSource.addInitializer(initializer);
        int initializerCountAfter = dataSource.getInitializerList().size();

        assertTrue(initializerCountBefore + 1 == initializerCountAfter);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    // Test innerCreateConnection() method
    @Test
    public void testInnerCreateConnection() throws SQLException {
        // Test when connection is null
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
        assertNotNull(dataSource.getConnection());

        // Test when connection is not null
        Connection connection1 = dataSource.innerCreateConnection();
        assertSame(connection, connection1);
    }

    // Test getConnectionProxy() method
    @Test
    public void testGetConnectionProxy() throws SQLException {
        // Test tryLock()
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.innerCreateConnection();
        assertFalse(dataSource.getConnectionProxy(dataSource.connection).isClosed());

        // Test when connection is closed
        dataSource.connection.close();
        assertTrue(dataSource.getConnectionProxy(dataSource.connection).isClosed());
    }

    // Test prepareConnection() method
    @Test
    public void testPrepareConnection() throws SQLException {
        // Test when autoCommit is null
        dataSource.setAutoCommit(null);
        dataSource.innerCreateConnection();
        assertTrue(dataSource.connection.getAutoCommit());

        // Test when autoCommit is true
        dataSource.setAutoCommit(true);
        dataSource.innerCreateConnection();
        assertTrue(dataSource.connection.getAutoCommit());

        // Test when autoCommit is false
        dataSource.setAutoCommit(false);
        dataSource.innerCreateConnection();
        assertFalse(dataSource.connection.getAutoCommit());
    }

    // Test onConnectionCreated() method
    @Test
    public void testOnConnectionCreated() throws SQLException {
        // Test when connection is created
        dataSource.innerCreateConnection();
        LOGGER.info(dataSource.onConnectionCreated(dataSource.connection).toString());
    }

    // Test tryLock()
    @Test
    public void testTryLock() throws ConflictException, SQLException, InterruptedException {
        // Test when connection is available
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.innerCreateConnection();
        assertTrue(dataSource.tryLock());
        assertFalse(dataSource.tryLock());

        // Test when connection is occupied
        Thread.sleep(dataSource.timeOutMillis + 1000);
        assertFalse(dataSource.tryLock());
    }
}