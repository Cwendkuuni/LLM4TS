import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
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
import java.util.Collections;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer connectionInitializer;
    private Connection connection;
    private Lock lock;

    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource();
        connectionInitializer = new ConnectionInitializerImpl();
        connection = mock(Connection.class);
        lock = new ReentrantLock();
    }

    @After
    public void tearDown() throws Exception {
        dataSource.close();
    }

    @Test
    public void testGetConnectionWithDefaultValues() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(0)).thenReturn(true);

        DataSource spyDataSource = spy(dataSource);
        doReturn(connection).when(spyDataSource).innerCreateConnection();

        Connection result = spyDataSource.getConnection();

        verify(spyDataSource, times(1)).innerCreateConnection();
        verify(spyDataSource, times(1)).getConnectionProxy(connection);
        assertEquals(connection, result);
    }

    @Test
    public void testGetConnectionWithAutoCommitTrue() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(0)).thenReturn(true);

        dataSource.setAutoCommit(true);

        DataSource spyDataSource = spy(dataSource);
        doReturn(connection).when(spyDataSource).innerCreateConnection();

        Connection result = spyDataSource.getConnection();

        verify(spyDataSource, times(1)).innerCreateConnection();
        verify(spyDataSource, times(1)).getConnectionProxy(connection);
        assertEquals(connection, result);
        assertTrue(result.getAutoCommit());
    }

    @Test
    public void testGetConnectionWithAutoCommitFalse() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(0)).thenReturn(true);

        dataSource.setAutoCommit(false);

        DataSource spyDataSource = spy(dataSource);
        doReturn(connection).when(spyDataSource).innerCreateConnection();

        Connection result = spyDataSource.getConnection();

        verify(spyDataSource, times(1)).innerCreateConnection();
        verify(spyDataSource, times(1)).getConnectionProxy(connection);
        assertEquals(connection, result);
        assertFalse(result.getAutoCommit());
    }

    @Test
    public void testGetConnectionWithClosedConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(true);

        DataSource spyDataSource = spy(dataSource);
        doReturn(connection).when(spyDataSource).innerCreateConnection();

        assertThrows(SQLException.class, () -> spyDataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithResetConnection() throws SQLException {
        dataSource.addInitializer(connectionInitializer);

        when(connection.isClosed()).thenReturn(true);

        DataSource spyDataSource = spy(dataSource);
        doReturn(connection).doReturn(connection)
                .when(spyDataSource).innerCreateConnection();

        Connection result = spyDataSource.getConnection();

        verify(spyDataSource, times(2)).innerCreateConnection();
        verify(spyDataSource, times(1)).prepareConnection(connection);
        verify(spyDataSource, times(1)).onConnectionReset(connection);
        assertEquals(connection, result);
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.addInitializer(connectionInitializer);

        dataSource.resetConnection();

        verify(dataSource, times(1)).innerCreateConnection();
        verify(dataSource, times(1)).prepareConnection(connection);
        verify(dataSource, times(1)).onConnectionReset(connection);
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(connectionInitializer);

        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws Exception {
        InvocationHandler handler =
                Proxy.getInvocationHandler(dataSource);
        Field lockField = handler.getClass().getDeclaredField("lock");
        lockField.setAccessible(true);

        dataSource.close();

        Lock lock = (Lock) lockField.get(handler);
        assertFalse(lock.isHeldByCurrentThread());
    }

    @Test
    public void testGetConnectionProxy() throws Exception {
        Lock lock = mock(Lock.class);

        ConnectionProxy connectionProxy = new ConnectionProxy(connection, lock);

        assertEquals(connection, connectionProxy.getConnection());
        assertFalse(connectionProxy.isClosed());

        connectionProxy.close();

        assertTrue(connectionProxy.isClosed());
    }

    @Test
    public void testGetConnectionProxyWithCloseIgnored() throws SQLException {
        Lock lock = mock(Lock.class);
        ConnectionProxy connectionProxy = new ConnectionProxy(connection, lock);

        Connection result =
                connectionProxy.invoke(connectionProxy, method, new Object[0]);

        verify(lock, times(1)).unlock();
        assertEquals(null, result);
    }

    @Test
    public void testGetConnectionProxyWithConflictException() throws SQLException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Lock lock = mock(Lock.class);
        ConnectionProxy connectionProxy = new ConnectionProxy(connection, lock);

        doThrow(new ConflictException(ErrorCodes.ConnectionOccupied, new Object[] {},
                "Connection is occupied, waited 10 millis")).when(lock)
                .tryLock(10, TimeUnit.MILLISECONDS);

        assertThrows(ConflictException.class, () -> connectionProxy.invoke(connectionProxy,
                method, new Object[0]));
    }

    @Test
    public void testInnerCreateConnectionWithAutoReconnectFalse() throws
            SQLException {
        dataSource.setAutoReconnect(false);

        when(connection.isClosed()).thenReturn(true);

        DataSource spyDataSource = spy(dataSource);
        doReturn(connection).when(spyDataSource).newConnectionFromDriver(anyString(),
                anyString());

        assertThrows(SQLException.class, () -> spyDataSource.innerCreateConnection());
    }

    @Test
    public void testInnerCreateConnectionWithAutoReconnectTrue() throws SQLException {
        dataSource.setAutoReconnect(true);

        when(connection.isClosed()).thenReturn(true);

        DataSource spyDataSource = spy(dataSource);
        doReturn(connection).when(spyDataSource).newConnectionFromDriver(anyString(),
                anyString());

        Connection result = spyDataSource.innerCreateConnection();

        verify(spyDataSource, times(1)).newConnectionFromDriver(anyString(), anyString());
        verify(spyDataSource, times(1)).onConnectionCreated(connection);
        verify(spyDataSource, times(1)).prepareConnection(connection);
        assertEquals(connection, result);
    }

    @Test
    public void testNewConnectionFromDriverWithEmptyCredentials() throws
            SQLException {
        assertThrows(SQLException.class, () -> dataSource.newConnectionFromDriver("", ""));
    }

    private static class ConnectionProxy extends
            Proxy.ProxyHandler<Connection> {

        private final Lock lock;

        public ConnectionProxy(Connection target, Lock lock) {
            super(target);
            this.lock = lock;
        }

        @Override
        protected Connection getTarget() {
            return super.getTarget();
        }

        @Override
        public boolean isClosed() {
            // Implement your custom closed logic here
            return false;
        }

        @Override
        public Connection getConnection() throws SQLException {
            if (lock.tryLock(1, TimeUnit.SECONDS)) {
                try {
                    return super.getConnection();
                } finally {
                    lock.unlock();
                }
            } else {
                throw new ConflictException(ErrorCodes.ConnectionOccupied,
                        new Object[0],
                        "Connection is occupied, waited 10 millis");
            }
        }
    }
}