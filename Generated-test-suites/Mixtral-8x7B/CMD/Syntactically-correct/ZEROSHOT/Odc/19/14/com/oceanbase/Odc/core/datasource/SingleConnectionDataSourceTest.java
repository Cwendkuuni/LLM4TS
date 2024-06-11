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
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SingleConnectionDataSourceTest {

    private static final Logger log = LoggerFactory.getLogger(SingleConnectionDataSourceTest.class);

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private Lock lock;

    @Before
    public void setUp() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        connection = mock(Connection.class);
        lock = new ReentrantLock();
        dataSource.setConnection(connection);
        dataSource.setLock(lock);
    }

    @Test
    public void testGetConnection() throws SQLException {
        assertTrue(dataSource.getConnection() == connection);
    }

    @Test
    public void testGetConnectionAutoReconnect() throws SQLException {
        dataSource.setAutoReconnect(true);
        dataSource.setConnection(null);
        assertTrue(dataSource.getConnection() == connection);
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        assertEquals(dataSource.getConnection(), dataSource.getConnection("username", "password"));
    }

    @Test
    public void testGetConnectionWithInvalidUsernameAndPassword() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalid", "password"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.resetConnection();
        verify(connection).close();
        assertTrue(dataSource.getConnection() == connection);
    }

    @Test
    public void testAddInitializer() {
        ConnectionInitializer initializer = mock(ConnectionInitializer.class);
        dataSource.addInitializer(initializer);
        assertTrue(dataSource.getInitializerList().contains(initializer));
    }

    @Test(timeout = 1100)
    public void testClose() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dataSource.close();
        });

        assertThrows(IllegalStateException.class, () -> dataSource.getConnection());
        executor.shutdown();
        assertTrue(executor.isTerminated());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        boolean autoCommit = dataSource.getAutoCommit();
        dataSource.setAutoCommit(true);
        dataSource.prepareConnection(connection);
        assertEquals(dataSource.getConnection().getAutoCommit(), true);
        dataSource.setAutoCommit(autoCommit);
    }

    @Test(timeout = 1100)
    public void testGetConnectionProxy() throws SQLException {
        assertTrue(dataSource.getConnectionProxy(connection) == connection);
    }

    @Test
    public void testGetConnectionProxyOccupied() {
        Connection secondConnection = mock(Connection.class);
        dataSource.setConnection(secondConnection);
        InvocationHandler invocationHandler = dataSource.getConnectionProxy(connection);
        assertTrue(invocationHandler.invoke(null, "equals", new Object[]{secondConnection}).equals(true));
        assertTrue(invocationHandler.invoke(null, "hashCode").equals(System.identityHashCode(secondConnection)));
        assertTrue(invocationHandler.invoke(null, "isWrapperFor", new Object[]{Connection.class}).equals(true));
        assertThrows(ConflictException.class, () -> invocationHandler.invoke(null, "unwrap",
                new Class[]{Connection.class}));
        assertThrows(ConflictException.class, () -> invocationHandler.invoke(null, "unwrap",
                new Class[]{DataSource.class}));
        assertThrows(ConflictException.class, () -> invocationHandler.invoke(null, "close"));
        assertFalse(invocationHandler.invoke(null, "isClosed").equals(true));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        dataSource.setConnection(null);
        assertTrue(dataSource.innerCreateConnection() == connection);
    }

    @Test
    public void testNewConnectionFromDriver() throws SQLException {
        // Implement your custom test for newConnectionFromDriver
    }

    @Test
    public void testInitConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        dataSource.setConnection(connection);
        dataSource.initConnection(connection);
        verify(connection).createStatement();
    }

    @Test
    public void testOnConnectionCreated() {
        dataSource.onConnectionCreated(connection);
        if (log.isDebugEnabled()) {
            verify(log).debug("Connection is created");
        }
    }

    @Test
    public void testOnConnectionReset() {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.onConnectionReset(connection);
        verify(eventPublisher).publishEvent(Mockito.any(ConnectionResetEvent.class));
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        CloseIgnoreInvocationHandler invocationHandler =
                new CloseIgnoreInvocationHandler(connection, lock);
        assertTrue(invocationHandler.invoke(null, "equals", new Object[]{connection}).equals(true));
        assertTrue(invocationHandler.invoke(null, "hashCode").equals(System
                .identityHashCode(connection)));
        assertTrue(invocationHandler.invoke(null, "unwrap", new Object[]{Connection.class}).equals(connection));
        assertTrue(invocationHandler.invoke(null, "isWrapperFor", new Object[]{Connection.class}).equals(true));
        assertThrows(RuntimeException.class, () -> invocationHandler.invoke(null, "close", null));
        assertFalse(invocationHandler.invoke(null, "isClosed").equals(true));
    }
}

class ConnectionInitializer {

    public void init(Connection connection) throws Exception {
    }
}

class ConnectionResetEvent {

    private Connection connection;

    public ConnectionResetEvent(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}