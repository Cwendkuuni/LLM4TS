import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.TestConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    private SingleConnectionDataSource dataSource;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource();
        dataSource.addInitializer(new TestConnectionInitializer());
        dataSource.setEventPublisher(eventPublisher);

        connection = dataSource.getConnection();
    }

    @After
    public void tearDown() throws Exception {
        if (connection != null) {
            connection.close();
        }
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        DataSourceTestUtil.checkConnection(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        SingleConnectionDataSource dataSourceWithCredentials = new SingleConnectionDataSource("testUser", "testPassword");
        Connection connection = dataSourceWithCredentials.getConnection("testUser", "testPassword");
        DataSourceTestUtil.checkConnection(connection);
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();

        assertThrows(ConflictException.class, () -> dataSource.getConnection());

        // Wait a bit for the lock to be released
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        dataSource.getConnection();
        verify(eventPublisher, times(1)).publishEvent(any());
    }

    @Test
    public void testAutoCommit() throws SQLException {
        Boolean initialAutoCommit = connection.getAutoCommit();
        dataSource.setAutoCommit(true);
        assertEquals(true, connection.getAutoCommit());

        dataSource.setAutoCommit(null);
        assertEquals(initialAutoCommit, connection.getAutoCommit());

        dataSource.setAutoCommit(false);
        assertEquals(false, connection.getAutoCommit());
    }

    @Test
    public void testInitializers() throws Exception {
        TestConnectionInitializer initializer1 = new TestConnectionInitializer();
        initializer1.setName("initializer1");
        initializer1.setInvoked(false);

        TestConnectionInitializer initializer2 = new TestConnectionInitializer();
        initializer2.setName("initializer2");
        initializer2.setInvoked(false);

        dataSource.addInitializer(initializer1);
        dataSource.addInitializer(initializer2);

        connection = dataSource.getConnection();

        verify(initializer1, times(1)).init(connection);
        verify(initializer2, times(1)).init(connection);
    }

    @Test
    public void testClose() throws Exception {
        assertFalse(connection.isClosed());
        dataSource.close();
        assertTrue(connection.isClosed());
    }

    @Test
    public void testTryLock() throws SQLException, InterruptedException {
        assertTrue(dataSource.tryLock());
        dataSource.getConnection();

        assertFalse(dataSource.tryLock());
        assertThrows(ConflictException.class, () -> dataSource.getConnection());

        // Wait a bit for the lock to be released
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        assertTrue(dataSource.tryLock());
        dataSource.getConnection();
    }

}

class TestConnectionInitializer implements ConnectionInitializer {

    private String name;
    private boolean invoked;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInvoked() {
        return invoked;
    }

    public void setInvoked(boolean invoked) {
        this.invoked = invoked;
    }

    @Override
    public void init(Connection connection) throws Exception {
        invoked = true;
    }
}

class DataSourceTestUtil {

    public static void checkConnection(Connection connection) throws SQLException {
        assertFalse(connection.isClosed());
        connection.createStatement().close();
    }
}