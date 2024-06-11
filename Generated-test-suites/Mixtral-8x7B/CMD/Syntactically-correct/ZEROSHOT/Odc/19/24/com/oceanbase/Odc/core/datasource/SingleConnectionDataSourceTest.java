package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Slf4j
public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;

    @Mock
    private EventPublisher eventPublisher;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        dataSource = new SingleConnectionDataSource();
        dataSource.setEventPublisher(eventPublisher);
    }

    @After
    public void tearDown() throws SQLException {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        Connection connection = dataSource.getConnection("username", "password");
        assertNotNull(connection);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        dataSource.getConnection("invalidUsername", "invalidPassword");
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.getConnection();
        dataSource.close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = dataSource.getConnection();
        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        assertTrue(connectionProxy instanceof Connection);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithLockedConnection() throws SQLException {
        dataSource.getConnection();
        dataSource.getConnectionProxy(dataSource.getConnection());
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        InvocationHandler handler = new CloseIgnoreInvocationHandler(mock(Connection.class), mock(Lock.class));
        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
                new Class[] {Connection.class},
                handler);
        assertNull(((Connection) proxy).close());
    }

    @Test
    public void testGetConnectionWithDriver() throws SQLException {
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertTrue(connection.isClosed());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidDriver() throws SQLException {
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        dataSource.setDriverClassName("invalid.driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/testdb";
        dataSource.setUrl(url);
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/invalid";
        dataSource.setUrl(url);
        dataSource.setUsername("username");
        dataSource.setPassword("password");
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionProxyWithLockedConnectionWhenReset() throws SQLException {
        dataSource.getConnection();
        dataSource.resetConnection();
        dataSource.getConnectionProxy(dataSource.getConnection());
    }

    @Test(expected = IllegalStateException.class)
    public void testInnerCreateConnectionWithExistingConnection() throws SQLException {
        dataSource.getConnection();
        dataSource.innerCreateConnection();
    }

    @Test
    public void testOnConnectionCreated() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.onConnectionCreated(connection);
        verify(connection, times(1)).isClosed();
    }

    @Test
    public void testOnConnectionReset() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.onConnectionReset(connection);
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(dataSource.getAutoCommit());
    }

    @Test
    public void testTryLock() {
        Lock lock = new ReentrantLock();
        assertTrue(dataSource.tryLock());
        assertFalse(dataSource.tryLock());
    }
}