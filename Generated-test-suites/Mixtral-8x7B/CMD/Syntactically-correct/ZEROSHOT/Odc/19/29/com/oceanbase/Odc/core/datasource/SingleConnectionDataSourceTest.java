package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.SQLException;
import com.oceanbase.odc.core.datasource.connection.ConnectionInitializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private ConnectionInitializer connectionInitializer;

    @InjectMocks
    private SingleConnectionDataSource singleConnectionDataSource;

    private Connection connection;

    @Before
    public void setUp() throws SQLException {
        singleConnectionDataSource = new SingleConnectionDataSource();
        singleConnectionDataSource.setUsername("test_user");
        singleConnectionDataSource.setPassword("test_password");
        singleConnectionDataSource.setDriverClassName("org.h2.Driver");
        singleConnectionDataSource.setUrl("jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1");

        connection = DriverManager.getConnection(
                "jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1", "test_user", "test_password");

        singleConnectionDataSource.initConnection(connection);
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);

        Connection resultConnection = singleConnectionDataSource.getConnection();

        assertTrue(resultConnection.isClosed());
        assertEquals(connection, resultConnection);
        verify(singleConnectionDataSource, times(1)).innerCreateConnection();
    }

    @Test
    public void testGetConnectionWithAutoReconnect() throws SQLException {
        SingleConnectionDataSource singleConnectionDataSourceWithAutoReconnect =
                new SingleConnectionDataSource(true);
        singleConnectionDataSourceWithAutoReconnect.setUsername("test_user");
        singleConnectionDataSourceWithAutoReconnect.setPassword("test_password");
        singleConnectionDataSourceWithAutoReconnect.setDriverClassName("org.h2.Driver");
        singleConnectionDataSourceWithAutoReconnect.setUrl("jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1");

        when(singleConnectionDataSourceWithAutoReconnect.innerCreateConnection()).thenReturn(connection);

        Connection resultConnection = singleConnectionDataSourceWithAutoReconnect.getConnection();

        assertFalse(resultConnection.isClosed());
        assertEquals(connection, resultConnection);
        verify(singleConnectionDataSourceWithAutoReconnect, times(1)).innerCreateConnection();

        resultConnection.close();

        resultConnection = singleConnectionDataSourceWithAutoReconnect.getConnection();

        assertFalse(resultConnection.isClosed());
        assertEquals(connection, resultConnection);
        verify(singleConnectionDataSourceWithAutoReconnect, times(2)).innerCreateConnection();
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() {
        SingleConnectionDataSource singleConnectionDataSource =
                new SingleConnectionDataSource();
        singleConnectionDataSource.setUsername("invalid_user");
        singleConnectionDataSource.setPassword("invalid_password");
        singleConnectionDataSource.setDriverClassName("org.h2.Driver");
        singleConnectionDataSource.setUrl("jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1");

        try {
            singleConnectionDataSource.getConnection();
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        singleConnectionDataSource.setAutoCommit(true);
        singleConnectionDataSource.resetConnection();

        assertTrue(connection.isClosed());
        assertTrue(singleConnectionDataSource.isAutoCommit());

        when(singleConnectionDataSource.innerCreateConnection()).thenReturn(connection);

        Connection resultConnection = singleConnectionDataSource.getConnection();

        assertFalse(resultConnection.isClosed());
        assertEquals(connection, resultConnection);
        verify(singleConnectionDataSource, times(1)).innerCreateConnection();
    }

    @Test
    public void testAddInitializer() {
        singleConnectionDataSource.addInitializer(connectionInitializer);

        List<ConnectionInitializer> resultInitializers = singleConnectionDataSource.getInitializerList();

        assertEquals(1, resultInitializers.size());
        assertTrue(resultInitializers.contains(connectionInitializer));
    }

    @Test
    public void testClose() throws SQLException {
        singleConnectionDataSource.setConnection(connection);

        singleConnectionDataSource.close();

        verify(connection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        singleConnectionDataSource.setAutoCommit(true);
        assertFalse(connection.getAutoCommit());

        singleConnectionDataSource.prepareConnection(connection);

        assertTrue(connection.getAutoCommit());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        ReentrantLock lock = new ReentrantLock();
        ConnectionProxy connectionProxy = singleConnectionDataSource.getConnectionProxy(connection);

        assertNotSame(connection, connectionProxy);
        assertTrue(connectionProxy instanceof ConnectionProxy);
        assertTrue(connectionProxy.getConnection().isClosed());
        assertFalse(lock.isHeldByCurrentThread());

        connectionProxy.close();

        assertFalse(connectionProxy.getConnection().isClosed());
        assertTrue(lock.isHeldByCurrentThread());
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);

        singleConnectionDataSource.initConnection(connection);

        verify(connection, times(1)).createStatement();
    }

    @Test
    public void testOnConnectionCreated() {
        singleConnectionDataSource.onConnectionCreated(connection);

        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testOnConnectionReset() {
        singleConnectionDataSource.onConnectionReset(connection);

        verifyNoMoreInteractions(connection);
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        when(singleConnectionDataSource.newConnectionFromDriver(
                ArgumentMatchers.eq("test_user"), ArgumentMatchers.eq("test_password")))
                .thenReturn(connection);

        Connection resultConnection = singleConnectionDataSource.innerCreateConnection();

        assertTrue(resultConnection.isClosed());
        assertEquals(connection, resultConnection);
        verify(singleConnectionDataSource, times(1)).newConnectionFromDriver(
                ArgumentMatchers.eq("test_user"), ArgumentMatchers.eq("test_password"));
        verify(singleConnectionDataSource, times(1)).initConnection(connection);
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        InvocationHandler invocationHandler =
                new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, new ReentrantLock());
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[] {Connection.class}, invocationHandler);

        assertSame(invocationHandler, Proxy.getInvocationHandler(proxy));
        assertTrue(proxy instanceof Connection);
        assertTrue(((Connection) proxy).isClosed());
        assertEquals(connection, ((Connection) proxy));

        Method closeMethod = null;
        try {
            closeMethod = Connection.class.getDeclaredMethod("close");
        } catch (NoSuchMethodException e) {
            fail("Failed to get 'close' method from Connection class");
        }
        closeMethod.setAccessible(true);

        try {
            closeMethod.invoke(proxy);
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Failed to invoke 'close' method on Connection proxy");
        }

        assertFalse(((Connection) proxy).isClosed());
    }
}