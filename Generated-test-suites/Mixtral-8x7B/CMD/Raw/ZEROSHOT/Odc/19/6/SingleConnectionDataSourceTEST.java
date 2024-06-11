package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

    private static final String VALID_USERNAME = "valid_user";
    private static final String VALID_PASSWORD = "valid_password";

    private SingleConnectionDataSource dataSource;

    @Mock
    private DataSource mockDataSource;

    @Mock
    private Connection mockConnection;

    @Mock
    private EventPublisher mockEventPublisher;

    @Mock
    private InvocationHandler mockInvocationHandler;

    private ConnectionInitializer connectionInitializer;

    private ScheduledExecutorService scheduledExecutor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        connectionInitializer = (Connection connection) -> {
            // Do nothing
        };

        dataSource = new SingleConnectionDataSource(false, mockEventPublisher);
        dataSource.setUsername(VALID_USERNAME);
        dataSource.setPassword(VALID_PASSWORD);
        dataSource.addInitializer(connectionInitializer);

        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    @After
    public void tearDown() {
        scheduledExecutor.shutdownNow();
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        dataSource.setDataSource(mockDataSource);

        Connection connection = dataSource.getConnection();

        assertNotNull(connection);
        assertSame(mockConnection, connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        when(mockDataSource.getConnection(VALID_USERNAME, VALID_PASSWORD)).thenReturn(mockConnection);
        dataSource.setDataSource(mockDataSource);

        Connection connection = dataSource.getConnection(VALID_USERNAME, VALID_PASSWORD);

        assertNotNull(connection);
        assertSame(mockConnection, connection);
    }

    @Test
    public void testGetConnectionWithInvalidUsernameAndPassword() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalid_user", "invalid_password"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        dataSource.setDataSource(mockDataSource);

        dataSource.getConnection();
        dataSource.resetConnection();

        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(connectionInitializer);

        assertEquals(1, dataSource.getInitializerList().size());
        assertTrue(dataSource.getInitializerList().contains(connectionInitializer));
    }

    @Test
    public void testClose() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        dataSource.setDataSource(mockDataSource);

        dataSource.getConnection();
        dataSource.close();

        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        dataSource.setDataSource(mockDataSource);

        Connection createdConnection = dataSource.innerCreateConnection();

        assertNotNull(createdConnection);
        assertSame(mockConnection, createdConnection);
        assertNotNull(dataSource.getLock());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        dataSource.setDataSource(mockDataSource);

        Connection connectionProxy = dataSource.getConnectionProxy(mockConnection);

        assertNotNull(connectionProxy);
        assertNotSame(mockConnection, connectionProxy);
        assertFalse(connectionProxy.isClosed());
    }

    @Test
    public void testGetConnectionProxyWithLockAcquired() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        dataSource.setDataSource(mockDataSource);

        dataSource.getConnectionProxy(mockConnection);
        assertNotNull(dataSource.getLock());

        ArgumentCaptor<Long> timeCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<TimeUnit> unitCaptor = ArgumentCaptor.forClass(TimeUnit.class);

        verify(dataSource.getLock(), times(1)).tryLock(timeCaptor.capture(), unitCaptor.capture());

        long timeout = timeCaptor.getValue();
        TimeUnit timeUnit = unitCaptor.getValue();

        ConcurrentMap<Object, Boolean> lockMap = new ConcurrentHashMap<>();
        lockMap.put(dataSource.getLock(), true);

        try {
            scheduledExecutor.schedule(() -> lockMap.put(dataSource.getLock(), false), timeout, timeUnit);
        } catch (Throwable ignored) {
            // ignored
        }

        Connection connectionProxy = dataSource.getConnectionProxy(mockConnection);

        assertNotNull(connectionProxy);
        assertNotSame(mockConnection, connectionProxy);
        assertFalse(connectionProxy.isClosed());

        verify(dataSource.getLock(), times(1)).unlock();
    }

    @Test
    public void testGetConnectionProxyWithLockFailed() {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        dataSource.setDataSource(mockDataSource);

        assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(mockConnection));
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        dataSource.setAutoCommit(true);

        Connection connection = Mockito.mock(Connection.class);
        when(connection.getAutoCommit()).thenReturn(false);

        dataSource.prepareConnection(connection);

        verify(connection, times(1)).setAutoCommit(true);
    }

    @Test
    public void testPrepareConnectionWithAutoCommitNull() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);

        dataSource.prepareConnection(connection);

        verify(connection, never()).setAutoCommit(anyBoolean());
    }

    @Test
    public void testInitConnection() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        dataSource.setDataSource(mockDataSource);

        dataSource.initConnection(mockConnection);

        verify(mockConnection, times(1)).createStatement();
        verify(mockConnection, times(1)).close();
        verify(mockEventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        dataSource.setDataSource(mockDataSource);

        dataSource.onConnectionCreated(mockConnection);

        verify(mockEventPublisher, times(1)).publishEvent(any(ConnectionCreatedEvent.class));
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
        dataSource.setDataSource(mockDataSource);

        dataSource.onConnectionReset(mockConnection);

        verify(mockEventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testNewConnectionFromDriver() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        Class<?> driverClass = Class.forName("java.sql.DriverManager", true, classLoader);
        Method getConnectionMethod = driverClass.getMethod("getConnection", String.class, String.class);

        Objects.requireNonNull(getConnectionMethod);

        Connection connection = (Connection) getConnectionMethod.invoke(null, "jdbc:test:url", "username");

        assertNotNull(connection);
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws Exception {
        InvocationHandler handler = new CloseIgnoreInvocationHandler(mockConnection, dataSource.getLock());
        Connection proxy = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[]{Connection.class}, handler);

        Method closeMethod = Connection.class.getMethod("close");

        Objects.requireNonNull(closeMethod);

        closeMethod.invoke(proxy);

        verify(mockConnection, never()).close();
        assertFalse(proxy.isClosed());

        Method isClosedMethod = Connection.class.getMethod("isClosed");

        Objects.requireNonNull(isClosedMethod);

        assertFalse((boolean) isClosedMethod.invoke(proxy));
    }

    @Test
    public void testCloseIgnoreInvocationHandlerWithLockReleased() throws Exception {
        InvocationHandler handler = new CloseIgnoreInvocationHandler(mockConnection, dataSource.getLock());
        Connection proxy = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[]{Connection.class}, handler);

        Method closeMethod = Connection.class.getMethod("close");

        Objects.requireNonNull(closeMethod);

        closeMethod.invoke(proxy);

        verify(mockConnection, never()).close();
        assertFalse(proxy.isClosed());

        Method isClosedMethod = Connection.class.getMethod("isClosed");

        Objects.requireNonNull(isClosedMethod);

        assertFalse((boolean) isClosedMethod.invoke(proxy));

        dataSource.getLock().unlock();

        closeMethod.invoke(proxy);

        verify(mockConnection, times(1)).close();
        assertTrue(proxy.isClosed());

        isClosedMethod.invoke(proxy);

        assertTrue((boolean) isClosedMethod.invoke(proxy));
    }

    @Test
    public void testCloseIgnoreInvocationHandlerWithLockNotAcquired() throws Exception {
        InvocationHandler handler = new CloseIgnoreInvocationHandler(mockConnection, null);
        Connection proxy = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[]{Connection.class}, handler);

        Method closeMethod = Connection.class.getMethod("close");

        Objects.requireNonNull(closeMethod);

        closeMethod.invoke(proxy);

        verify(mockConnection, times(1)).close();
        assertTrue(proxy.isClosed());
    }
}