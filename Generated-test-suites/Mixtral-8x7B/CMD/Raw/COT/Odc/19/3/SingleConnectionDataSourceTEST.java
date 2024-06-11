import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private Lock lock;
    private DataSource mockDataSource;
    private EventPublisher mockEventPublisher;

    @Before
    public void setUp() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        connection = mock(Connection.class);
        lock = new ReentrantLock();
        mockDataSource = mock(DataSource.class);
        mockEventPublisher = mock(EventPublisher.class);
        dataSource.setDataSource(mockDataSource);
        dataSource.setEventPublisher(mockEventPublisher);
    }

    @After
    public void tearDown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(connection);

        Connection result = dataSource.getConnection();

        assertEquals(connection, result);
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        when(mockDataSource.getConnection(any(), any())).thenReturn(connection);

        Connection result = dataSource.getConnection("user", "password");

        assertEquals(connection, result);
        verify(mockDataSource, times(1)).getConnection("user", "password");
    }

    @Test
    public void testGetConnectionWithInvalidUsernameOrPassword() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPassword"));
        verify(mockDataSource, times(0)).getConnection("invalidUser", "invalidPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.setConnection(connection);

        dataSource.resetConnection();

        verify(connection, times(1)).close();
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);

        dataSource.addInitializer(initializer);

        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose() {
        dataSource.setConnection(connection);

        dataSource.close();

        verify(connection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Boolean autoCommit = true;
        dataSource.setAutoCommit(autoCommit);

        dataSource.prepareConnection(connection);

        verify(connection, times(1)).setAutoCommit(eq(autoCommit));
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        dataSource.setConnection(connection);
        dataSource.setLock(lock);

        Connection result = dataSource.getConnectionProxy(connection);

        assertTrue(result instanceof Proxy);
        InvocationHandler handler = Proxy.getInvocationHandler(result);
        assertTrue(handler instanceof CloseIgnoreInvocationHandler);
        assertEquals(connection, ((CloseIgnoreInvocationHandler) handler).getTarget());
        assertEquals(lock, ((CloseIgnoreInvocationHandler) handler).getLock());
    }

    @Test
    public void testGetConnectionProxyThrowsConflictException() {
        dataSource.setConnection(connection);
        dataSource.setLock(lock);
        dataSource.setTimeOutMillis(-1);

        assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(connection);

        Connection result = dataSource.innerCreateConnection();

        assertEquals(connection, result);
        verify(mockDataSource, times(1)).getConnection();
        verify(connection, times(1)).setAutoCommit(any());
        verify(connection, times(1)).isValid(anyInt());
        verify(connection, times(1)).isClosed();
        verify(connection, times(1)).close();
    }

    @Test
    public void testInnerCreateConnectionWithInitializers() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(connection);
        List<ConnectionInitializer> initializers = new LinkedList<>();
        initializers.add(mock(ConnectionInitializer.class));
        dataSource.setInitializerList(initializers);

        dataSource.innerCreateConnection();

        verify(initializers.get(0), times(1)).init(connection);
    }

    @Test
    public void testOnConnectionReset() {
        dataSource.setEventPublisher(mockEventPublisher);
        dataSource.setConnection(connection);

        dataSource.onConnectionReset(connection);

        verify(mockEventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testTryLockWhenLockIsAvailable() throws InterruptedException {
        dataSource.setLock(lock);

        boolean result = dataSource.tryLock();

        assertTrue(result);
    }

    @Test
    public void testTryLockWhenLockIsNotAvailable() throws InterruptedException {
        dataSource.setLock(lock);
        lock.lock();

        boolean result = dataSource.tryLock();

        assertFalse(result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCloseIgnoreInvocationHandler() {
        InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);

        assertEquals(connection, handler.invoke(null, any(), any()));
        assertEquals(0, handler.invoke(null, eq("close"), any()));
        assertEquals(false, handler.invoke(null, eq("isClosed"), any()));
    }

    private interface ConnectionInitializer {
        void init(Connection connection) throws Exception;
    }

    private class ConnectionResetEvent {
        private Connection connection;

        public ConnectionResetEvent(Connection connection) {
            this.connection = connection;
        }

        public Connection getConnection() {
            return connection;
        }
    }
}