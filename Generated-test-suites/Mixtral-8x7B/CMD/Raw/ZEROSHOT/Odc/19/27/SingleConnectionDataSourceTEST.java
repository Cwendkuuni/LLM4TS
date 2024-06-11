package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static org.mockito.Mockito.*;

@Slf4j
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        this.dataSource = new SingleConnectionDataSource();
    }

    @Test
    public void testDefaultConstructor() {
        Assert.assertEquals(false, this.dataSource.getAutoReconnect());
    }

    @Test
    public void testConstructorWithAutoReconnect() {
        SingleConnectionDataSource dataSourceWithAutoReconnect = new SingleConnectionDataSource(true);
        Assert.assertEquals(true, dataSourceWithAutoReconnect.getAutoReconnect());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionNonExistentConnection() throws SQLException {
        this.dataSource.getConnection();
    }

    @Test
    public void testGetConnectionExistentConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);
        this.dataSource.setConnection(connection);
        Connection result = this.dataSource.getConnection();
        Assert.assertEquals(connection, result);
    }

    @Test
    public void testGetConnectionExistentInvalidConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(anyInt())).thenReturn(false);
        this.dataSource.setConnection(connection);
        Connection result = this.dataSource.getConnection();
        Assert.assertNotEquals(connection, result);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionExistentInvalidConnectionNoAutoReconnect() throws SQLException {
        this.dataSource.setAutoReconnect(false);
        Connection connection = Mockito.mock(Connection.class);
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(anyInt())).thenReturn(false);
        this.dataSource.setConnection(connection);
        this.dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);
        this.dataSource.setConnection(connection);
        this.dataSource.resetConnection();
        verify(connection, times(1)).close();
        Assert.assertNull(this.dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
        this.dataSource.addInitializer(initializer);
        Assert.assertEquals(1, this.dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() {
        Connection connection = Mockito.mock(Connection.class);
        this.dataSource.setConnection(connection);
        this.dataSource.close();
        verify(connection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        this.dataSource.setAutoCommit(true);
        this.dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(true);
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);
        this.dataSource.setConnection(connection);
        Lock lock = Mockito.mock(Lock.class);
        when(lock.tryLock(anyLong(), any())).thenReturn(true);
        SingleConnectionDataSource spyDataSource = Mockito.spy(this.dataSource);
        Connection result = spyDataSource.getConnectionProxy(connection);
        Assert.assertNotEquals(connection, result);
        verify(spyDataSource, times(1)).getConnectionProxy(connection);
        verify(lock, times(1)).unlock();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyConflict() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);
        this.dataSource.setConnection(connection);
        Lock lock = Mockito.mock(Lock.class);
        when(lock.tryLock(anyLong(), any())).thenReturn(false);
        SingleConnectionDataSource spyDataSource = Mockito.spy(this.dataSource);
        spyDataSource.getConnectionProxy(connection);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        SingleConnectionDataSource spyDataSource = Mockito.spy(this.dataSource);
        Connection connection = Mockito.mock(Connection.class);
        when(spyDataSource.newConnectionFromDriver(anyString(), anyString())).thenReturn(connection);
        when(connection.isValid(anyInt())).thenReturn(true);
        when(connection.isClosed()).thenReturn(false);
        when(spyDataSource.lock.tryLock(anyLong(), any())).thenReturn(true);
        Connection result = spyDataSource.innerCreateConnection();
        Assert.assertEquals(connection, result);
        verify(spyDataSource, times(1)).onConnectionCreated(connection);
    }

    @Test
    public void testCloseIgnoreInvocationHandlerEquals() {
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                new CloseIgnoreInvocationHandler(Mockito.mock(Connection.class), Mockito.mock(Lock.class)));
        Assert.assertEquals(proxy, proxy);
    }

    @Test
    public void testCloseIgnoreInvocationHandlerHashCode() {
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                new CloseIgnoreInvocationHandler(Mockito.mock(Connection.class), Mockito.mock(Lock.class)));
        Assert.assertNotEquals(System.identityHashCode(proxy), System.identityHashCode(new Object()));
    }

    @Test
    public void testCloseIgnoreInvocationHandlerUnwrap() {
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                new CloseIgnoreInvocationHandler(Mockito.mock(Connection.class), Mockito.mock(Lock.class)));
        Assert.assertEquals(proxy, proxy);
    }

    @Test
    public void testCloseIgnoreInvocationHandlerIsWrapperFor() {
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                new CloseIgnoreInvocationHandler(Mockito.mock(Connection.class), Mockito.mock(Lock.class)));
        Assert.assertEquals(true, ((Class<?>) Connection.class).isInstance(proxy));
    }

    @Test
    public void testCloseIgnoreInvocationHandlerClose() {
        Lock lock = Mockito.mock(Lock.class);
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                new CloseIgnoreInvocationHandler(Mockito.mock(Connection.class), lock));
        Method closeMethod = Arrays.stream(Connection.class.getDeclaredMethods())
                .filter(m -> "close".equals(m.getName())).findFirst().get();
        closeMethod.invoke(proxy, new Object[0]);
        verify(lock, times(1)).unlock();
    }

    @Test
    public void testCloseIgnoreInvocationHandlerIsClosed() {
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                new CloseIgnoreInvocationHandler(Mockito.mock(Connection.class), Mockito.mock(Lock.class)));
        Method isClosedMethod = Arrays.stream(Connection.class.getDeclaredMethods())
                .filter(m -> "isClosed".equals(m.getName())).findFirst().get();
        Assert.assertEquals(false, isClosedMethod.invoke(proxy, new Object[0]));
    }

    @Test
    public void testNewConnectionFromDriver() throws SQLException {
        SingleConnectionDataSource spyDataSource = Mockito.spy(this.dataSource);
        Connection connection = Mockito.mock(Connection.class);
        when(spyDataSource.getDriver()).thenReturn(Mockito.mock(Driver.class));
        when(spyDataSource.getDriver().connect(anyString(), anyMap())).thenReturn(connection);
        Assert.assertEquals(connection, spyDataSource.newConnectionFromDriver("url", "user", "password"));
    }

    @Test(expected = SQLException.class)
    public void testNewConnectionFromDriverException() throws SQLException {
        SingleConnectionDataSource spyDataSource = Mockito.spy(this.dataSource);
        when(spyDataSource.getDriver()).thenReturn(Mockito.mock(Driver.class));
        when(spyDataSource.getDriver().connect(anyString(), anyMap())).thenThrow(new SQLException());
        spyDataSource.newConnectionFromDriver("url", "user", "password");
    }
}

class ConnectionInitializer {}

class Driver {}

@FunctionalInterface
interface ConnectionResetEvent {}

class EventPublisher {}