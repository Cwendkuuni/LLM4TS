package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private static final String TEST_URL = "jdbc:test:test";
    private static final String TEST_USERNAME = "test_username";
    private static final String TEST_PASSWORD = "test_password";

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private Lock lock;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() throws SQLException {
        dataSource = new SingleConnectionDataSource(true);
        dataSource.setUsername(TEST_USERNAME);
        dataSource.setPassword(TEST_PASSWORD);
        dataSource.setUrl(TEST_URL);
        dataSource.setTimeOutMillis(10000);

        connection = Mockito.mock(Connection.class);
        lock = new ReentrantLock();
        eventPublisher = Mockito.mock(EventPublisher.class);

        dataSource.addInitializer((Connection conn) -> {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @After
    public void tearDown() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(dataSource.innerCreateConnection()).thenReturn(connection);

        Connection result = dataSource.getConnection();

        verify(connection, times(1)).isValid(anyLong());
        assertSame(connection, result);
    }

    @Test
    public void testGetConnection_InvalidConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(anyLong())).thenReturn(false);
        when(dataSource.innerCreateConnection()).thenReturn(connection);

        Connection result = dataSource.getConnection();

        verify(dataSource, times(1)).innerCreateConnection();
        assertSame(connection, result);
    }

    @Test
    public void testGetConnection_AutoReconnectFalse() throws SQLException {
        dataSource.setAutoReconnect(false);
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(anyLong())).thenReturn(false);
        when(dataSource.innerCreateConnection()).thenThrow(new SQLException("Connection was closed or not valid"));

        assertThrows(SQLException.class, () -> dataSource.getConnection());

        verify(dataSource, times(1)).innerCreateConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.connection = connection;
        dataSource.lock = lock;
        dataSource.resetConnection();

        verify(connection, times(1)).close();
        verify(dataSource, times(1)).innerCreateConnection();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);

        assertTrue(dataSource.initializerList.contains(initializer));
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.connection = connection;

        dataSource.close();

        verify(connection, times(1)).close();
        assertNull(dataSource.connection);
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        dataSource.prepareConnection(connection);

        verify(connection, times(1)).setAutoCommit(dataSource.isAutoCommit());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        dataSource.connection = connection;
        dataSource.lock = lock;

        Connection result = dataSource.getConnectionProxy(connection);

        assertTrue(result instanceof Proxy);
        assertSame(connection, ((Connection) result).unwrap(Connection.class));
    }

    @Test
    public void testGetConnectionProxy_Locked() {
        dataSource.connection = connection;
        dataSource.lock = lock;
        lock.lock();

        assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));

        lock.unlock();
    }

    @Test
    public void testInitConnection() throws SQLException {
        dataSource.connection = connection;

        dataSource.initConnection(connection);

        verify(connection, times(1)).setAutoCommit(dataSource.isAutoCommit());
        verify(dataSource, times(1)).onConnectionCreated(connection);
    }

    @Test
    public void testOnConnectionReset() {
        dataSource.eventPublisher = eventPublisher;
        Connection connection = Mockito.mock(Connection.class);
        dataSource.onConnectionReset(connection);

        ArgumentCaptor<ConnectionResetEvent> captor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        verify(eventPublisher, times(1)).publishEvent(captor.capture());
        assertEquals(connection, captor.getValue().getConnection());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        when(DriverManager.getConnection(anyString(), anyString(), anyString())).thenReturn(connection);

        Connection result = dataSource.innerCreateConnection();

        verify(DriverManager, times(1)).getConnection(dataSource.getUrl(), TEST_USERNAME, TEST_PASSWORD);
        verify(connection, times(1)).isValid(dataSource.getLoginTimeout());
        assertSame(connection, result);
    }

    @Test
    public void testCloseIgnoreInvocationHandler_Close() {
        InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[]{Connection.class}, handler);

        Method closeMethod = Proxy.getInvocationHandler(proxy).getClass().getDeclaredMethod("close");
        closeMethod.setAccessible(true);
        closeMethod.invoke(handler);

        verify(lock, times(1)).unlock();
    }

    @Test
    public void testCloseIgnoreInvocationHandler_IsClosed() {
        InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[]{Connection.class}, handler);

        Method isClosedMethod = Proxy.getInvocationHandler(proxy).getClass().getDeclaredMethod("isClosed");
        isClosedMethod.setAccessible(true);
        Boolean result = (Boolean) isClosedMethod.invoke(handler);

        assertFalse(result);
    }
}