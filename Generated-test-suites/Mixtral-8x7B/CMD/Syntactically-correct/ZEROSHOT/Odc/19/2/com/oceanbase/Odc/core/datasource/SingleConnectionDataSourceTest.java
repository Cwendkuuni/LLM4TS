package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private static final Logger logger = LoggerFactory.getLogger(SingleConnectionDataSourceTest.class);

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("testuser");
        dataSource.setPassword("testpassword");
    }

    @Test
    public void testConstructor() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
        assertNotNull(dataSource);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isValid(dataSource.getLoginTimeout()));
        connection.close();
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        Connection connection = dataSource.getConnection("testuser", "testpassword");
        assertNotNull(connection);
        assertTrue(connection.isValid(dataSource.getLoginTimeout()));
        connection.close();
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        dataSource.getConnection("invaliduser", "invalidpassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.close();
        dataSource.resetConnection();

        Connection newConnection = dataSource.getConnection();
        assertNotNull(newConnection);
        assertTrue(newConnection.isValid(dataSource.getLoginTimeout()));
        newConnection.close();
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();
        assertNull(dataSource.getConnection());
        assertTrue(connection.isClosed());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        DataSource dataSourceSpy = spy(dataSource);
        Connection connection = mock(Connection.class);
        doReturn(true).when(connection).isClosed();

        dataSourceSpy.prepareConnection(connection);

        verify(connection, never()).close();

        dataSourceSpy.setAutoCommit(true);
        doReturn(false).when(connection).isClosed();

        dataSourceSpy.prepareConnection(connection);

        verify(connection, times(1)).setAutoCommit(true);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithConnectionOccupied() {
        dataSource.setTimeOutMillis(100);
        dataSource.getConnectionProxy(mock(Connection.class));
        dataSource.getConnectionProxy(mock(Connection.class));
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = dataSource.getConnection();
        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertNotNull(connectionProxy);
        assertNotSame(connection, connectionProxy);
        assertTrue(connectionProxy instanceof Proxy);
        assertEquals(connection, ((Connection) Proxy.getInvocationHandler(connectionProxy)).getConnection());
    }

    @Test
    public void testInitConnection() throws SQLException {
        dataSource.setInitializerList(Collections.singletonList(mock(ConnectionInitializer.class)));
        Connection connection = mock(Connection.class);
        dataSource.initConnection(connection);
        verify(connection, times(1)).createStatement();
    }

    @Test
    public void testOnConnectionCreated() {
        dataSource.onConnectionCreated(mock(Connection.class));
        logger.debug("onConnectionCreated called");
    }

    @Test
    public void testOnConnectionReset() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
        Connection connection = mock(Connection.class);
        dataSource.onConnectionReset(connection);
        verify(eventPublisher, times(1)).publishEvent(Mockito.any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        InvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(mock(Connection.class),
                mock(Lock.class));
        assertFalse((Boolean) handler.invoke(null, getClosestMethod("isClosed"), null));
        handler.invoke(null, getClosestMethod("close"), null);
        assertTrue((Boolean) handler.invoke(null, getClosestMethod("isClosed"), null));
    }

    private Method getClosestMethod(String name) {
        try {
            return Connection.class.getMethod(name);
        } catch (NoSuchMethodException e) {
            fail("No such method: " + name);
            return null;
        }
    }

}