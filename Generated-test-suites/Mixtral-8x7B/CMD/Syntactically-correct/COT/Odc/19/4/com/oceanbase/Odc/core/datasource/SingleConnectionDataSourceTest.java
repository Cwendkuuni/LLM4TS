import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer connectionInitializer;
    private DataSource testDataSource;

    @Before
    public void setup() {
        dataSource = new SingleConnectionDataSource();
        connectionInitializer = (Connection connection) -> {
            // Do nothing
        };
        dataSource.addInitializer(connectionInitializer);
    }

    @After
    public void tearDown() throws SQLException {
        if (testDataSource != null) {
            testDataSource.close();
        }
    }

    @Test
    public void testGetConnection() throws SQLException {
        testDataSource = dataSource;
        Connection connection = testDataSource.getConnection();
        Assert.assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnection_whenConnectionIsClosed() throws SQLException {
        testDataSource = dataSource;
        Connection connection = testDataSource.getConnection();
        connection.close();
        testDataSource.getConnection();
    }

    @Test
    public void testGetConnection_whenAutoReconnectIsTrue() throws SQLException {
        dataSource.setAutoReconnect(true);
        testDataSource = dataSource;
        Connection connection = testDataSource.getConnection();
        connection.close();
        Connection newConnection = testDataSource.getConnection();
        Assert.assertNotNull(newConnection);
    }

    @Test
    public void testGetConnection_whenPasswordsDoNotMatch() throws SQLException {
        dataSource.setPassword("wrongPassword");
        testDataSource = dataSource;
        Assert.assertThrows(SQLException.class, () -> testDataSource.getConnection("username", "password"));
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxy_whenConnectionIsOccupied() {
        dataSource.setTimeOutMillis(100);
        dataSource.getConnectionProxy(new ReentrantLock());
        Assert.assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(new ReentrantLock()));
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(connectionInitializer);
        List<ConnectionInitializer> initializers = dataSource.getInitializerList();
        Assert.assertEquals(1, initializers.size());
    }

    @Test
    public void testClose() throws SQLException {
        testDataSource = dataSource;
        Connection connection = testDataSource.getConnection();
        connection.close();
        testDataSource.close();
        Assert.assertThrows(IllegalStateException.class, () -> testDataSource.getConnection());
    }

    @Test
    public void testInitConnection() throws SQLException {
        dataSource.setConnection(new ReentrantLock());
        dataSource.initConnection(new ReentrantLock());
        Assert.assertNotNull(dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        dataSource.setAutoCommit(false);
        Connection connection = new ReentrantLock();
        dataSource.prepareConnection(connection);
        Assert.assertFalse(connection.getAutoCommit());
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.setConnection(new ReentrantLock());
        dataSource.resetConnection();
        Assert.assertNull(dataSource.getConnection());
    }

    @Test
    public void testTryLock() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Assert.assertTrue(dataSource.tryLock(1000, TimeUnit.MILLISECONDS));
    }
}