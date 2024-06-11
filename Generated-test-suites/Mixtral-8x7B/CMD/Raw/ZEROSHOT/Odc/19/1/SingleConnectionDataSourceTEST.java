package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    private SingleConnectionDataSource dataSource;
    private Lock lock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataSource = new SingleConnectionDataSource(true);
        lock = new ReentrantLock();
        dataSource.setLock(lock);
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
        verify(eventPublisher, never()).publishEvent(null);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        when(dataSource.getUsername()).thenReturn("testUser");
        when(dataSource.getPassword()).thenReturn("testPassword");

        SQLException sqlException = assertThrows(SQLException.class, () ->
                dataSource.getConnection("invalidUser", "invalidPassword"));
        assertEquals(ErrorCodes.InvalidUsernameOrPassword, sqlException.getErrorCode());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = dataSource.getConnectionProxy(newConnectionFromDriver("testUser", "testPassword"));
        assertNotNull(connection);
        assertTrue(connection instanceof ConnectionProxy);

        // Verify the lock is acquired
        assertTrue(lock.isHeldByCurrentThread());

        // Verify the close() method is ignored
        connection.close();
        assertFalse(lock.isHeldByCurrentThread());
        verify(eventPublisher, never()).publishEvent(null);
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.getConnection();
        verify(eventPublisher, never()).publishEvent(null);

        dataSource.resetConnection();
        verify(eventPublisher, times(1)).publishEvent(null);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = connection -> {};
        dataSource.addInitializer(initializer);
        List<ConnectionInitializer> initializers = dataSource.getInitializerList();
        assertEquals(1, initializers.size());
        assertTrue(initializers.contains(initializer));
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.getConnection();
        dataSource.close();
        assertThrows(SQLException.class, dataSource::getConnection);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = newConnectionFromDriver("testUser", "testPassword");
        dataSource.prepareConnection(connection);
        assertEquals(dataSource.getAutoCommit(), connection.getAutoCommit());
    }

    @Test
    public void testTryLock() {
        when(lock.tryLock(1000, TimeUnit.MILLISECONDS)).thenReturn(true);
        assertTrue(dataSource.tryLock());

        when(lock.tryLock(1000, TimeUnit.MILLISECONDS)).thenReturn(false).thenReturn(true);
        assertFalse(dataSource.tryLock());
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testOnConnectionReset() {
        dataSource.onConnectionReset(newConnectionFromDriver("testUser", "testPassword"));
        verify(eventPublisher, times(1)).publishEvent(null);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
        verify(eventPublisher, times(1)).publishEvent(null);
    }

    private Connection newConnectionFromDriver(String username, String password) {
        try {
            Class<?> driverClass = Class.forName("com.mysql.jdbc.Driver");
            return (Connection) driverClass.getMethod("connect", String.class, String.class)
                    .invoke(null, "jdbc:mysql://localhost:3306/testdb?autoReconnect=true", username + ":" + password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    class ConnectionProxy implements Connection {

        private final Connection target;
        private final Lock lock;

        public ConnectionProxy(Connection target, Lock lock) {
            this.target = target;
            this.lock = lock;
        }

        @Override
        public <T> T unwrap(Class<T> iface) throws SQLException {
            if (iface.isInstance(this.target)) {
                return iface.cast(this.target);
            }
            throw new SQLException("Unwrapping not supported");
        }

        @Override
        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            return iface.isInstance(this.target);
        }

        // Implement the rest of the Connection methods using the target Connection
        // and synchronizing on the lock
    }

    interface ConnectionInitializer {
        void init(Connection connection) throws Exception;
    }
}