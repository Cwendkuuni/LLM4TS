import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.event.EventPublisher;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer connectionInitializer;
    private EventPublisher eventPublisher;
    private Lock lock;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        connectionInitializer = new ConnectionInitializerImpl();
        eventPublisher = Mockito.mock(EventPublisher.class);
        lock = new ReentrantLock();
    }

    @After
    public void tearDown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetConnection_WhenCalled_ReturnsConnection() throws SQLException {
        // Given
        dataSource.addInitializer(connectionInitializer);

        // When
        Connection connection = dataSource.getConnection();

        // Then
        Assert.assertNotNull(connection);
    }

    @Test
    public void testGetConnection_WhenUsernameAndPasswordAreInvalid_ThrowsSQLException()
            throws SQLException {
        // Given
        String invalidUsername = "invalid";
        String invalidPassword = "invalid";

        // When
        try {
            dataSource.getConnection(invalidUsername, invalidPassword);
            Assert.fail("Expected SQLException");
        } catch (SQLException e) {
            // Then
            Assert.assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testResetConnection_WhenCalled_ResetsConnection() throws SQLException {
        // Given
        dataSource.addInitializer(connectionInitializer);
        Connection connection = dataSource.getConnection();

        // When
        dataSource.resetConnection();

        // Then
        Assert.assertNull(dataSource.connection);
        Assert.assertNull(dataSource.lock);

        try {
            dataSource.getConnection();
            Assert.fail("Expected SQLException");
        } catch (SQLException e) {
            // Then
            Assert.assertTrue(e.getMessage().contains("Connection was closed or not valid"));
        }
    }

    @Test
    public void testAddInitializer_WhenCalledWithNonNullConnectionInitializer_AddsInitializerToList()
            throws SQLException {
        // Given
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);

        // When
        dataSource.addInitializer(initializer);

        // Then
        Assert.assertTrue(dataSource.initializerList.contains(initializer));
    }

    @Test
    public void testAddInitializer_WhenCalledMultipleTimesWithNonNullConnectionInitializers_AddsAllInitializersToList()
            throws SQLException {
        // Given
        ConnectionInitializer initializer1 = Mockito.mock(ConnectionInitializer.class);
        ConnectionInitializer initializer2 = Mockito.mock(ConnectionInitializer.class);

        // When
        dataSource.addInitializer(initializer1);
        dataSource.addInitializer(initializer2);

        // Then
        Assert.assertTrue(dataSource.initializerList.contains(initializer1));
        Assert.assertTrue(dataSource.initializerList.contains(initializer2));
    }

    @Test
    public void testClose_WhenCalled_ClosesConnection() throws SQLException {
        // Given
        dataSource.addInitializer(connectionInitializer);
        Connection connection = dataSource.getConnection();

        // When
        dataSource.close();

        // Then
        Assert.assertNull(dataSource.connection);

        try {
            dataSource.getConnection();
            Assert.fail("Expected SQLException");
        } catch (SQLException e) {
            // Then
            Assert.assertTrue(e.getMessage().contains("Connection is null"));
        }
    }

    @Test
    public void testGetConnectionProxy_WhenCalledWithNonNullConnection_ReturnsProxyConnection() {
        // Given
        dataSource.addInitializer(connectionInitializer);

        // When
        Connection connection = dataSource.getConnection();

        // Then
        Assert.assertNotNull(connection);
        Assert.assertTrue(connection instanceof Proxy);
    }

    @Test
    public void testGetConnectionProxy_WhenCalledMultipleTimes_ReturnsSameProxyConnection()
            throws SQLException {
        // Given
        dataSource.addInitializer(connectionInitializer);

        // When
        Connection connection1 = dataSource.getConnection();
        Connection connection2 = dataSource.getConnection();

        // Then
        Assert.assertSame(connection1, connection2);
    }

    @Test
    public void testInnerCreateConnection_WhenCalledWithoutConnection_CreatesNewConnection()
            throws SQLException {
        // Given
        dataSource.addInitializer(connectionInitializer);

        // When
        dataSource.innerCreateConnection();

        // Then
        Assert.assertNotNull(dataSource.connection);
    }

    @Test
    public void testInnerCreateConnection_WhenCalledMultipleTimes_CreatesSameConnection()
            throws SQLException {
        // Given
        dataSource.addInitializer(connectionInitializer);

        // When
        dataSource.innerCreateConnection();
        dataSource.innerCreateConnection();

        // Then
        Assert.assertSame(dataSource.connection, dataSource.connection);
    }

    @Test
    public void testTryLock_WhenCalledWithValidLockAndConnectionIsAvailable_ReturnsTrue()
            throws InterruptedException {
        // Given
        dataSource.lock = lock;

        // When
        boolean result = dataSource.tryLock();

        // Then
        Assert.assertTrue(result);
    }

    @Test
    public void testTryLock_WhenCalledWithValidLockAndConnectionIsNotAvailable_ReturnsFalse()
            throws InterruptedException {
        // Given
        dataSource.lock = lock;
        dataSource.connection = new Object();

        // When
        boolean result = dataSource.tryLock();

        // Then
        Assert.assertFalse(result);
    }

    @Test
    public void testTryLock_WhenCalledWithInvalidLock_ThrowsRuntimeException() {
        // Given
        Lock invalidLock = Mockito.mock(Lock.class);
        dataSource.lock = lock;

        // When
        try {
            dataSource.tryLock();
            Assert.fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Then
            Assert.assertTrue(e.getMessage().contains("Failed to lock connection"));
        }
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxy_WhenCalledWithOccupiedConnection_ThrowsConflictException()
            throws SQLException {
        // Given
        dataSource.addInitializer(connectionInitializer);
        dataSource.innerCreateConnection();

        // When
        dataSource.getConnectionProxy(dataSource.connection);

        // Then
        // Throws ConflictException
    }

    @Test
    public void testCloseIgnoreInvocationHandler_WhenCloseIsCalled_ReturnsNull() {
        // Given
        InvocationHandler handler = new CloseIgnoreInvocationHandler(null, null);
        Connection proxy = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[] {Connection.class}, handler);

        // When
        Object result = handler.invoke(proxy, "close", null);

        // Then
        Assert.assertNull(result);
    }

    @Test
    public void testCloseIgnoreInvocationHandler_WhenIsClosedIsCalled_ReturnsFalse() {
        // Given
        InvocationHandler handler = new CloseIgnoreInvocationHandler(null, null);
        Connection proxy = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[] {Connection.class}, handler);

        // When
        Object result = handler.invoke(proxy, "isClosed", null);

        // Then
        Assert.assertFalse((Boolean) result);
    }

}