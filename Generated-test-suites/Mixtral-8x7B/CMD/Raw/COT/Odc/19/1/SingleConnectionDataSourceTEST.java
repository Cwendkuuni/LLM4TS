import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer initializer;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private Connection connection;
    @Mock
    private Lock lock;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        initializer = new ConnectionInitializerImpl();
        dataSource.addInitializer(initializer);
        dataSource.setEventPublisher(eventPublisher);
    }

    @After
    public void tearDown() throws SQLException {
        dataSource.close();
    }

    @Test
    public void testGetConnection_InitializesConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(any())).thenReturn(false);

        Connection result = dataSource.getConnection();

        verify(connection, times(1)).isValid(dataSource.getLoginTimeout());
        verify(connection, times(1)).close();
        verify(eventPublisher, times(1)).publishEvent(any());
        assertTrue(result instanceof Proxy);
    }

    @Test
    public void testGetConnection_WithAutoReconnectFalse_ThrowsSQLException() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(any())).thenReturn(false);
        dataSource.setAutoReconnect(false);

        assertThrows(SQLException.class, () -> dataSource.getConnection());

        verify(connection, times(1)).isValid(dataSource.getLoginTimeout());
        verify(connection, never()).close();
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    public void testGetConnection_WithInitializers() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(any())).thenReturn(false);

        dataSource.addInitializer(initializer);

        Connection result = dataSource.getConnection();

        verify(initializer, times(1)).init(connection);
        verify(connection, times(1)).isValid(dataSource.getLoginTimeout());
        verify(connection, times(1)).close();
        verify(eventPublisher, times(1)).publishEvent(any());
        assertTrue(result instanceof Proxy);
    }

    @Test
    public void testGetConnection_WithUsernameAndPassword_ThrowsSQLException() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(any())).thenReturn(false);

        assertThrows(SQLException.class, () -> dataSource.getConnection("user", "password"));

        verify(connection, times(1)).isValid(dataSource.getLoginTimeout());
        verify(connection, never()).close();
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.connection = connection;
        dataSource.lock = lock;

        dataSource.resetConnection();

        verify(connection, times(1)).close();
        verify(lock, times(1)).unlock();
        assertFalse(dataSource.lock.isHeldByCurrentThread());
    }

    @Test
    public void testClose() throws SQLException {
        dataSource = spy(dataSource);
        dataSource.connection = connection;

        doNothing().when(dataSource).close();

        dataSource.close();

        verify(dataSource, times(1)).close();
        verify(connection, times(1)).close();
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection resultConnection = dataSource.innerCreateConnection();

        assertEquals(resultConnection, dataSource.connection);
        assertEquals(lock, dataSource.lock);
        verify(connection, times(1)).isValid(dataSource.getLoginTimeout());
    }

    @Test
    public void testGetConnectionProxy() {
        Connection proxy = dataSource.getConnectionProxy(connection);

        assertTrue(proxy instanceof Proxy);
    }

    @Test
    public void testTryLock() throws InterruptedException {
        dataSource.timeOutMillis = 100;
        dataSource.lock = lock;

        verify(lock, times(1)).tryLock(eq(100), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    public void testTryLock_InterruptedException() throws InterruptedException {
        dataSource.timeOutMillis = 100;
        dataSource.lock = lock;

        doThrow(new InterruptedException()).when(lock).tryLock(eq(100), eq(TimeUnit.MILLISECONDS));

        assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));
    }

    @Test
    public void testGetConnection_WithInitializerException() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(any())).thenReturn(false);
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws SQLException {
                throw new SQLException("Initializer exception");
            }
        });

        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testGetConnection_WithInitializerThrowingException() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(any())).thenReturn(false);
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) throws Exception {
                throw new Exception("Initializer exception");
            }
        });

        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testGetConnection_WithInitializerThrowingRuntimeException() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(any())).thenReturn(false);
        dataSource.addInitializer(new ConnectionInitializer() {
            @Override
            public void init(Connection connection) {
                throw new RuntimeException("Initializer exception");
            }
        });

        assertThrows(SQLException.class, () -> dataSource.getConnection());
    }

    @Test
    public void testGetConnectionProxy_WithLockUnlocked() throws Exception {
        dataSource.lock = lock;
        doNothing().when(lock).unlock();
        dataSource.connection = connection;

        Connection proxy = dataSource.getConnectionProxy(connection);

        lock.unlock();
        verify(lock, times(1)).unlock();
        verify(connection, times(1)).isValid(dataSource.getLoginTimeout());
        assertTrue(proxy instanceof Proxy);
    }

    @Test
    public void testGetConnectionProxy_WithLockLocked() throws Exception {
        dataSource.lock = lock;
        Connection proxy = dataSource.getConnectionProxy(connection);

        verify(lock, times(1)).tryLock(eq(10000), eq(TimeUnit.MILLISECONDS));
        verify(connection, times(1)).isValid(dataSource.getLoginTimeout());
        assertTrue(proxy instanceof Proxy);
    }

    @Test
    public void testGetConnectionProxy_WithLockInterrupted() throws Exception {
        dataSource.lock = lock;
        doThrow(new InterruptedException()).when(lock).tryLock(eq(10000), eq(TimeUnit.MILLISECONDS));

        assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));

        verify(lock, times(1)).tryLock(eq(10000), eq(TimeUnit.MILLISECONDS));
        verify(connection, never()).isValid(any());
    }
}