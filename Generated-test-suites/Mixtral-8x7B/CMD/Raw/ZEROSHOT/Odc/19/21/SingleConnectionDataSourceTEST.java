package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.event.TestConnectionResetEvent;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.ODCException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private static final String TEST_URL = "jdbc:test:test";
    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPassword";

    @Mock
    private EventPublisher eventPublisher;

    private SingleConnectionDataSource dataSource;
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dataSource = new SingleConnectionDataSource(TEST_URL, TEST_USERNAME, TEST_PASSWORD, eventPublisher);
        connection = dataSource.getConnection();
    }

    @After
    public void tearDown() throws Exception {
        dataSource.close();
    }

    @Test
    public void testConstructor() {
        assertNotNull(dataSource.getUrl());
        assertNotNull(dataSource.getUsername());
        assertNotNull(dataSource.getPassword());
        assertTrue(dataSource.getAutoReconnect());
        assertNull(dataSource.getConnection());
        assertNull(dataSource.getLock());
        assertTrue(dataSource.getInitializerList().isEmpty());
    }

    @Test
    public void testConstructorWithAutoReconnectFalse() {
        dataSource = new SingleConnectionDataSource(TEST_URL, TEST_USERNAME, TEST_PASSWORD, false, eventPublisher);
        assertFalse(dataSource.getAutoReconnect());
    }

    @Test
    public void testGetConnection() throws Exception {
        assertSame(connection, dataSource.getConnection());
        verify(dataSource, never()).innerCreateConnection();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidCredentials() throws Exception {
        dataSource = new SingleConnectionDataSource("jdbc:test:test", "invalidUser", "invalidPassword", eventPublisher);
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithProxy() throws Exception {
        assertFalse(connection.isClosed());
        assertNotNull(connection.getClass().getDeclaredMethod("getProxyConnection"));
        connection.getClass().getDeclaredMethod("getProxyConnection").setAccessible(true);
        Connection proxyConnection = (Connection) connection.getClass().getDeclaredMethod("getProxyConnection").invoke(connection);
        assertNotNull(proxyConnection);
        assertTrue(proxyConnection instanceof CloseIgnoreInvocationHandler);

        Method closeMethod = proxyConnection.getClass().getSuperclass().getDeclaredMethod("close");
        closeMethod.setAccessible(true);
        closeMethod.invoke(proxyConnection);
        verify(dataSource.getLock(), times(1)).unlock();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionWithProxyAndLockTimeout() throws Exception {
        ReentrantLock lock = new ReentrantLock();
        dataSource.setLock(lock);

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                dataSource.getConnection();
            } catch (SQLException e) {
                fail();
            }
        });

        assertFalse(lock.tryLock(100, TimeUnit.MILLISECONDS));
        dataSource.getConnection();
    }

    @Test
    public void testResetConnection() throws Exception {
        dataSource.resetConnection();
        verify(dataSource, times(1)).innerCreateConnection();
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(conn -> {
            // Do nothing
        });
        assertTrue(dataSource.getInitializerList().size() == 1);
    }

    @Test
    public void testClose() throws Exception {
        dataSource.close();
        verify(connection, times(1)).close();
        verify(dataSource, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws Exception {
        dataSource.setAutoCommit(false);
        Connection preparedConnection = mock(Connection.class);
        when(preparedConnection.getAutoCommit()).thenReturn(true);

        dataSource.prepareConnection(preparedConnection);
        verify(preparedConnection, times(1)).setAutoCommit(false);
    }

    @Test
    public void testInitConnection() throws Exception {
        dataSource.initConnection(connection);
        verify(connection, times(1)).setAutoCommit(false);
        verify(eventPublisher, times(1)).publishEvent(any(TestConnectionResetEvent.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testInnerCreateConnectionWithConnection() throws Exception {
        dataSource.setConnection(connection);
        dataSource.innerCreateConnection();
    }

    @Test
    public void testInnerCreateConnection() throws Exception {
        dataSource.innerCreateConnection();
        assertNotNull(dataSource.getConnection());
        assertNotNull(dataSource.getLock());
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, new ReentrantLock());
        assertNull(Proxy.getInvocationHandler(handler.invoke(null, "close", null)));
    }

    @Test
    public void testNewConnectionFromDriver() throws Exception {
        dataSource = new SingleConnectionDataSource(TEST_URL, TEST_USERNAME, TEST_PASSWORD, eventPublisher);
        assertNotNull(dataSource.newConnectionFromDriver(TEST_USERNAME, TEST_PASSWORD));
    }
}