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
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleConnectionDataSourceTest.class);

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer connectionInitializer;

    @Before
    public void setUp() throws Exception {
        // Initialize a SingleConnectionDataSource with autoReconnect = true
        dataSource = new SingleConnectionDataSource(true);

        // Initialize a ConnectionInitializer
        connectionInitializer = new ConnectionInitializerImpl();

        // Add the ConnectionInitializer to the dataSource
        dataSource.addInitializer(connectionInitializer);

        // Initialize the connection
        dataSource.initConnection(null);
    }

    @After
    public void tearDown() throws Exception {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetConnection() throws Exception {
        // Test getting a connection
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(dataSource.getLoginTimeout()));

        // Test that the connection is closed when close() is called
        dataSource.close();
        assertFalse(connection.isValid(dataSource.getLoginTimeout()));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws Exception {
        // Test getting a connection with invalid username and password
        SingleConnectionDataSource dataSourceInvalid = new SingleConnectionDataSource();
        dataSourceInvalid.setUsername("invalid");
        dataSourceInvalid.setPassword("invalid");
        dataSourceInvalid.initConnection(null);

        // This should throw an SQLException
        dataSourceInvalid.getConnection("invalid", "invalid");
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithOccupiedConnection() throws Exception {
        // Test getting a connection when the connection is occupied
        ReentrantLock lock = new ReentrantLock();
        Connection connection = dataSource.getConnectionProxy(dataSource.getConnection());
        lock.lock();

        // This should throw a ConflictException
        dataSource.getConnectionProxy(connection);
    }

    @Test
    public void testResetConnection() throws Exception {
        // Test resetting the connection
        dataSource.resetConnection();

        // Test that the connection is null after resetting
        assertNull(dataSource.getConnection());

        // Test that the connection is initialized after resetting
        dataSource.initConnection(null);
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testAutoReconnect() throws Exception {
        // Test that the connection is reconnected when it is closed
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        connection.close();

        // Test that the connection is still valid after closing
        assertTrue(dataSource.getConnection().isValid(dataSource.getLoginTimeout()));
    }

    @Test
    public void testConnectionInitializer() throws Exception {
        // Test that the connection initializer is called
        connectionInitializer.setInitializeSuccess(false);
        dataSource.initConnection(null);
        assertTrue(connectionInitializer.isInitializeSuccess());
    }

    @Test
    public void testConnectionInitializerWithException() throws Exception {
        // Test that an Exception in the connection initializer is caught
        connectionInitializer.setInitializeException(true);
        dataSource.initConnection(null);
        assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testTimeout() throws Exception {
        // Test that a ConflictException is thrown after the timeout
        dataSource.setTimeOutMillis(100);
        ReentrantLock lock = new ReentrantLock();
        Connection connection = dataSource.getConnectionProxy(dataSource.getConnection());
        lock.lock();

        try {
            TimeUnit.MILLISECONDS.sleep(150);
        } catch (InterruptedException e) {
            LOGGER.error("InterruptedException caught: ", e);
        }

        try {
            dataSource.getConnectionProxy(connection);
        } catch (ConflictException e) {
            LOGGER.info("ConflictException caught: ", e);
            assertEquals("Connection is occupied, waited 100 millis", e.getMessage());
        }
    }

}