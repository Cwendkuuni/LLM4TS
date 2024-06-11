import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.event.ConnectionEventPublisher;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer connectionInitializer;
    private ConnectionEventPublisher eventPublisher;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        connectionInitializer = new ConnectionInitializerImpl();
        eventPublisher = new ConnectionEventPublisher();
        dataSource.addInitializer(connectionInitializer);
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
        assertTrue(connection.isValid(10));
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        dataSource.setUsername("test");
        dataSource.setPassword("test");
        Connection connection = dataSource.getConnection("test", "test");
        assertNotNull(connection);
        assertTrue(connection.isValid(10));
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        dataSource.setUsername("test");
        dataSource.setPassword("test");
        dataSource.getConnection("invalid", "invalid");
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithNullUsernameAndPassword() throws SQLException {
        dataSource.getConnection(null, null);
    }

    @Test
    public void testAutoCommit() throws SQLException {
        dataSource.setAutoCommit(true);
        Connection connection = dataSource.getConnection();
        assertTrue(connection.getAutoCommit());
    }

    @Test
    public void testAutoReconnect() throws SQLException {
        SingleConnectionDataSource dataSourceWithReconnect = new SingleConnectionDataSource(true);
        dataSourceWithReconnect.setAutoCommit(false);
        Connection connection = dataSourceWithReconnect.getConnection();
        assertFalse(connection.getAutoCommit());
        dataSourceWithReconnect.resetConnection();
        connection = dataSourceWithReconnect.getConnection();
        assertFalse(connection.getAutoCommit());
    }

    @Test
    public void testTimeOutMillis() throws SQLException {
        dataSource.setTimeOutMillis(5000);
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(5));
    }

    @Test
    public void testOnConnectionResetEvent() throws Exception {
        eventPublisher.registerListener(event -> {
            if (event instanceof ConnectionResetEvent) {
                ConnectionResetEvent resetEvent = (ConnectionResetEvent) event;
                assertNotNull(resetEvent.getConnection());
            }
        });
        dataSource.getConnection();
        dataSource.resetConnection();
        eventPublisher.waitForNextEvent(10, TimeUnit.SECONDS);
    }

    @Test
    public void testConnectionInitializer() throws SQLException {
        dataSource.addInitializer(connectionInitializer);
        Connection connection = dataSource.getConnection();
        assertTrue(connectionInitializer.isCalled());
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertThrows(SQLException.class, connection::close);
    }
}