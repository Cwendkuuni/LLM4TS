
package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private Lock lock;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        connection = DriverManager.getConnection("jdbc:h2:mem:test", "test", "test");
        lock = new ReentrantLock();
        eventPublisher = Mockito.mock(EventPublisher.class);

        dataSource.setConnection(connection);
        dataSource.setLock(lock);
        dataSource.setEventPublisher(eventPublisher);
    }

    @After
    public void tearDown() throws SQLException {
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertSame(connection, dataSource.getConnection());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalid", "password"));
    }

    @Test
    public void testGetConnectionWithNullUsernameAndPassword() throws SQLException {
        assertThrows(SQLException.class, () -> dataSource.getConnection(null, null));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testResetConnectionWithAutoReconnectFalse() {
        dataSource = new SingleConnectionDataSource(false);
        assertThrows(SQLException.class, () -> dataSource.resetConnection());
    }

    @Test
    public void testAddInitializer() {
        InvocationHandler handler = new ConnectionInitializerHandler();
        ConnectionInitializer initializer = (ConnectionInitializer) Proxy.newProxyInstance(
                ConnectionInitializer.class.getClassLoader(),
                new Class[]{ConnectionInitializer.class}, handler);

        dataSource.addInitializer(initializer);
        assertEquals(1, dataSource.getInitializerList().size());

        dataSource.addInitializer(initializer);
        assertEquals(2, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.close();
        assertNull(dataSource.getConnection());
        assertFalse(lock.isHeldByCurrentThread());

        dataSource = new SingleConnectionDataSource();
        dataSource.close();
        assertTrue(dataSource.getConnection() == null && lock.isHeldByCurrentThread());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        boolean isAutoCommit = connection.getAutoCommit();
        dataSource.setAutoCommit(false);

        dataSource.prepareConnection(connection);
        assertFalse(connection.getAutoCommit());

        dataSource.prepareConnection(connection);
        assertFalse(connection.getAutoCommit());

        dataSource.setAutoCommit(isAutoCommit);
        dataSource.prepareConnection(connection);
        assertTrue(connection.getAutoCommit());
    }

    @Test
    public void testTryLock() {
        assertFalse(dataSource.tryLock());

        dataSource.setTimeOutMillis(1);
        assertTrue(dataSource.tryLock());
        dataSource.lock.unlock();
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        InvocationHandler handler = new ConnectionInitializerHandler();
        ConnectionInitializer initializer = (ConnectionInitializer) Proxy.newProxyInstance(
                ConnectionInitializer.class.getClassLoader(),
                new Class[]{ConnectionInitializer.class}, handler);
        dataSource.addInitializer(initializer);

        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertNotNull(connectionProxy);
        assertTrue(connectionProxy instanceof Connection);
        verify(initializer, times(1)).init(connection);

        dataSource.close();
        assertNull(dataSource.getConnectionProxy(connection));
        verify(initializer, times(1)).close();
    }

    @Test
    public void testInitConnection() throws SQLException {
        dataSource.initConnection(connection);
        verify(eventPublisher, never()).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testOnConnectionCreated() {
        dataSource.onConnectionCreated(connection);
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionCreatedEvent.class));
    }

    @Test
    public void testOnConnectionReset() {
        dataSource.onConnectionReset(connection);
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        when(eventPublisher.publishEvent(any(ConnectionCreatedEvent.class))).thenReturn(true);
        assertNotNull(dataSource.innerCreateConnection());
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionCreatedEvent.class));
    }

    @Test
    public void testInnerCreateConnectionWithException() throws SQLException {
        when(eventPublisher.publishEvent(any(ConnectionCreatedEvent.class))).thenThrow(new RuntimeException());
        assertThrows(SQLException.class, () -> dataSource.innerCreateConnection());
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionCreatedEvent.class));
    }

    @Test
    public void testCloseIgnoreInvocationHandlerEquals() {
        Connection proxyConnection1 = dataSource.getConnectionProxy(connection);
        Connection proxyConnection2 = dataSource.getConnectionProxy(connection);
        assertTrue(proxyConnection1.equals(proxyConnection2));
        assertTrue(proxyConnection2.equals(proxyConnection1));
    }

    @Test
    public void testCloseIgnoreInvocationHandlerHashCode() {
        Connection proxyConnection1 = dataSource.getConnectionProxy(connection);
        Connection proxyConnection2 = dataSource.getConnectionProxy(connection);
        assertEquals(proxyConnection1.hashCode(), proxyConnection2.hashCode());
    }

    @Test
    public void testCloseIgnoreInvocationHandlerUnwrap() {
        assertTrue(dataSource.getConnectionProxy(connection)
                .unwrap(Connection.class).equals(dataSource.getConnectionProxy(connection)));
    }

    @Test
    public void testCloseIgnoreInvocationHandlerIsWrapperFor() {
        assertTrue(dataSource.getConnectionProxy(connection)
                .isWrapperFor(Connection.class));
    }

    @Test
    public void testCloseIgnoreInvocationHandlerClose() {
        dataSource.getConnectionProxy(connection);
        dataSource.lock.unlock();
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionClosedEvent.class));
    }

    @Test
    public void testCloseIgnoreInvocationHandlerIsClosed() {
        assertTrue(dataSource.getConnectionProxy(connection).isClosed());
    }

    static class ConnectionInitializerHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("init".equals(method.getName())) {
                connection.setAutoCommit(false);
                return null;
            } else if ("close".equals(method.getName())) {
                return null;
            }
            throw new UnsupportedOperationException("Method " + method.getName() + " is not supported");
        }
    }

    static class ConnectionCreatedEvent {

        private Connection connection;

        public ConnectionCreatedEvent(Connection connection) {
            this.connection = connection;
        }

        public Connection getConnection() {
            return connection;
        }
    }

    static class ConnectionResetEvent {

        private Connection connection;

        public ConnectionResetEvent(Connection connection) {
            this.connection = connection;
        }

        public Connection getConnection() {
            return connection;
        }
    }

    static class ConnectionClosedEvent {

        private Connection connection;

        public ConnectionClosedEvent(Connection connection) {
            this.connection = connection;
        }

        public Connection getConnection() {
            return connection;
        }
    }
}
