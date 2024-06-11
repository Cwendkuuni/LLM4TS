package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private Lock lock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataSource = new SingleConnectionDataSource(true);
        connection = mock(Connection.class);
        lock = new ReentrantLock();
        dataSource.setConnection(connection);
        dataSource.setLock(lock);
    }

    @After
    public void tearDown() throws SQLException {
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(true);
        Connection result = dataSource.getConnection();
        assertTrue(result == connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        SQLException sqlException = new SQLException("Invalid username or password");
        when(connection.getConnection(anyString(), anyString())).thenThrow(sqlException);
        try {
            dataSource.getConnection("invalidUser", "invalidPassword");
            fail("Expected SQLException to be thrown");
        } catch (SQLException e) {
            assertEquals(sqlException.getMessage(), e.getMessage());
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        verify(connection).close();
        verify(dataSource).innerCreateConnection();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithLockConflict() throws SQLException {
        dataSource.getConnectionProxy(connection);
        dataSource.getConnectionProxy(connection);
    }

    @Test
    public void testGetConnectionProxy() throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        InvocationHandler handler = dataSource.getConnectionProxy(connection);
        Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[] {Connection.class},
                handler);

        Method closeMethod = proxyConnection.getClass().getDeclaredMethod("close");
        closeMethod.setAccessible(true);
        closeMethod.invoke(proxyConnection);

        verify(connection).close();
    }

    @Test
    public void testCloseIgnoreInvocationHandlerCloseMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
        Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[] {Connection.class},
                handler);

        Method closeMethod = proxyConnection.getClass().getDeclaredMethod("close");
        closeMethod.setAccessible(true);
        closeMethod.invoke(proxyConnection);

        verify(connection, never()).close();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }
}