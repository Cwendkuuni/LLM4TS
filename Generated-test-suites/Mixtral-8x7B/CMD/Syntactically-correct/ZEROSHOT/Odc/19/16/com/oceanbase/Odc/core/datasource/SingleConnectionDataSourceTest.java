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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Slf4j
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private ConnectionInitializer initializer1;
    private ConnectionInitializer initializer2;
    private Connection connection;
    private Lock lock;
    private EventPublisher eventPublisher;

    @Before
    public void setUp() throws SQLException {
        initializer1 = mock(ConnectionInitializer.class);
        initializer2 = mock(ConnectionInitializer.class);
        connection = mock(Connection.class);
        lock = new ReentrantLock();
        eventPublisher = mock(EventPublisher.class);

        dataSource = new SingleConnectionDataSource(true);
        dataSource.addInitializer(initializer1);
        dataSource.addInitializer(initializer2);
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
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);

        Connection result = dataSource.getConnection();

        assertEquals(connection, result);
        verify(connection, never()).close();
        verify(initializer1, times(1)).init(connection);
        verify(initializer2, times(1)).init(connection);
        verify(eventPublisher, never()).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnectionWithClosedConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(true);

        assertThrows(SQLException.class, () -> dataSource.getConnection());
        verify(connection, times(1)).close();
        verify(initializer1, never()).init(connection);
        verify(initializer2, never()).init(connection);
        verify(eventPublisher, never()).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testGetConnectionWithInvalidConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(false);

        assertThrows(SQLException.class, () -> dataSource.getConnection());
        verify(connection, times(1)).close();
        verify(initializer1, never()).init(connection);
        verify(initializer2, never()).init(connection);
        verify(eventPublisher, never()).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);

        dataSource.resetConnection();

        verify(connection, times(1)).close();
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
        verify(initializer1, times(1)).init(connection);
        verify(initializer2, times(1)).init(connection);
    }

    @Test
    public void testGetConnectionProxy() {
        assertNotSame(connection, dataSource.getConnectionProxy(connection));
        verify(lock, times(1)).tryLock(anyLong(), any(TimeUnit.class));
        verify(lock, times(1)).unlock();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer3 = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer3);
        verify(initializer3, times(1)).init(connection);
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.close();
        verify(connection, times(1)).close();
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        when(connection.isClosed()).thenReturn(false);
        when(connection.isValid(anyInt())).thenReturn(true);

        Connection result = dataSource.innerCreateConnection();

        assertEquals(connection, result);
        verify(initializer1, times(1)).init(connection);
        verify(initializer2, times(1)).init(connection);
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        InvocationHandler handler = dataSource.new CloseIgnoreInvocationHandler(connection, lock);
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[] {Connection.class}, handler);

        assertNotNull(proxy);
        try {
            Method closeMethod = Connection.class.getDeclaredMethod("close");
            closeMethod.invoke(proxy);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            fail("Failed to invoke close method on proxy: " + e.getMessage());
        }
        verify(lock, times(1)).unlock();
    }
}