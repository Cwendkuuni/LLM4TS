package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        dataSource = new SingleConnectionDataSource(true);
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");
        dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testDB");
        dataSource.addInitializer(connection -> {
            // Initialize connection if needed
        });
    }

    @After
    public void tearDown() throws SQLException {
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertEquals(dataSource.getUsername(), connection.getMetaData().getUserName());

        Connection closedConnection = spy(connection);
        when(closedConnection.isClosed()).thenReturn(true);
        when(closedConnection.isValid(anyInt())).thenReturn(false);
        when(dataSource.newConnectionFromDriver(anyString(), anyString())).thenReturn(closedConnection);
        dataSource.setConnection(closedConnection);

        Connection resetConnection = dataSource.getConnection();
        assertNotNull(resetConnection);
        assertEquals(closedConnection, resetConnection);
        verify(closedConnection, times(1)).close();
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() throws SQLException {
        dataSource.setUsername("invalidUser");
        dataSource.setPassword("invalidPassword");

        try {
            dataSource.getConnection();
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);

        doNothing().when(dataSource).close();
        dataSource.resetConnection();

        verify(dataSource, times(1)).close();
        verify(connection, never()).close();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() throws SQLException {
        List<ConnectionInitializer> initializers = new CopyOnWriteArrayList<>();
        initializers.add(connection -> {
            // Initialize connection 1
        });
        initializers.add(connection -> {
            // Initialize connection 2
        });
        dataSource.setInitializerList(initializers);

        Connection connection = dataSource.getConnection();
        assertNotNull(connection);

        verify(connection, times(1)).createStatement();
        verify(connection, times(1)).commit();
        verify(connection, times(1)).close();
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);

        doNothing().when(dataSource).close();
        dataSource.close();

        verify(connection, times(1)).close();
        verify(dataSource, times(1)).close();
        assertEquals(null, dataSource.getConnection());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);

        Boolean autoCommit = true;
        dataSource.setAutoCommit(autoCommit);

        verify(connection, times(1)).setAutoCommit(autoCommit);
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);

        Lock lock = new ReentrantLock();
        Connection proxyConnection = dataSource.getConnectionProxy(connection);

        assertNotNull(proxyConnection);
        assertTrue(proxyConnection instanceof Connection);
        assertNotSame(connection, proxyConnection);

        assertFalse(lock.tryLock());
        proxyConnection.createStatement();
        assertTrue(lock.tryLock());
        proxyConnection.close();
        lock.unlock();
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        dataSource.setConnection(null);

        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);

        verify(dataSource, times(1)).newConnectionFromDriver(anyString(), anyString());
        verify(dataSource, times(1)).onConnectionCreated(connection);
        verify(dataSource, times(1)).prepareConnection(connection);
    }

    @Test
    public void testMultiThreadedAccess() throws SQLException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Connection> connections = Collections.synchronizedList(new LinkedList<>());

        for (int i = 0; i < 100; i++) {
            executor.execute(() -> {
                try {
                    connections.add(dataSource.getConnection());
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    log.error("InterruptedException occurred: ", e);
                } catch (SQLException e) {
                    log.error("SQLException occurred: ", e);
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(1, connections.size());

        Connection connection = connections.get(0);
        for (Connection c : connections) {
            assertEquals(connection.hashCode(), c.hashCode());
        }
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        InvocationHandler handler = new CloseIgnoreInvocationHandler(null, null);

        assertFalse((Boolean) invokeMethod(handler, "close"));
        assertNull((Connection) invokeMethod(handler, "unwrap", Connection.class));
        assertFalse((Boolean) invokeMethod(handler, "isWrapperFor", Connection.class));
        assertNull(invokeMethod(handler, "isClosed"));
    }

    private Object invokeMethod(InvocationHandler handler, String methodName, Class<?>... parameterTypes) {
        Method method;
        try {
            method = Connection.class.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return handler.invoke(null, method, null);
    }

    @Test
    public void testConflictException() {
        try {
            when(eventPublisher.publishEvent(any())).thenThrow(new RuntimeException());
            dataSource.setEventPublisher(eventPublisher);
            dataSource.getConnectionProxy(DriverManager.getConnection("jdbc:h2:mem:test", "sa", ""));
            fail("Expected ConflictException");
        } catch (SQLException e) {
            log.error("SQLException occurred: ", e);
            verify(eventPublisher, times(1)).publishEvent(ArgumentMatchers.any());
            assertTrue(e.getMessage().contains("Connection is occupied"));
            assertTrue(e.getMessage().contains("ConnectionOccupied"));
            assertTrue(e.getMessage().contains("waited"));
            assertTrue(e.getMessage().contains("millis"));
        }
    }
}