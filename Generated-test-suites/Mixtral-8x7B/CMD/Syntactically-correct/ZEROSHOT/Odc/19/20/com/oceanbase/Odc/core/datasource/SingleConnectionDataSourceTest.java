package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private EventPublisher eventPublisher;
    private ExecutorService executorService;

    @Before
    public void setUp() throws SQLException {
        dataSource = new SingleConnectionDataSource();
        connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
        eventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.setConnection(connection);
        dataSource.addInitializer(connection1 -> {
            connection1.createStatement().execute("SET SCHEMA PUBLIC");
        });
        executorService = Executors.newSingleThreadExecutor();
    }

    @After
    public void tearDown() throws InterruptedException {
        executorService.shutdownNow();
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection result = dataSource.getConnection();
        assertEquals(connection, result);
        verify(connection).setAutoCommit(dataSource.isAutoCommit());
        verifyZeroInteractions(eventPublisher);
    }

    @Test
    public void testGetConnectionWithAutoReconnect() throws SQLException {
        dataSource = new SingleConnectionDataSource(true);
        connection.close();
        Connection result = dataSource.getConnection();
        assertEquals(connection, result);
        verify(connection).setAutoCommit(dataSource.isAutoCommit());
        verifyZeroInteractions(eventPublisher);
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithAutoReconnectDisabled() throws SQLException {
        dataSource = new SingleConnectionDataSource(false);
        connection.close();
        dataSource.getConnection();
    }

    @Test
    public void testGetConnectionWithWrongCredentials() throws SQLException {
        dataSource.setUsername("wrongUser");
        dataSource.setPassword("wrongPassword");
        try {
            dataSource.getConnection();
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertEquals("Invalid username or password", e.getMessage());
        }
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection result = dataSource.getConnection();
        assertEquals(connection, result);
        dataSource.resetConnection();
        result = dataSource.getConnection();
        assertEquals(connection, result);
    }

    @Test
    public void testAddInitializer() throws SQLException {
        ArgumentCaptor<Connection> captor = ArgumentCaptor.forClass(Connection.class);
        dataSource.addInitializer(connection1 -> {
            connection1.createStatement().execute("SET SCHEMA PUBLIC");
        });
        dataSource.getConnection();
        verify(connection).createStatement();
        verify(connection).setAutoCommit(dataSource.isAutoCommit());
        verify(connection).createStatement();
        verify(connection).setSchema("PUBLIC");
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.close();
        assertTrue(dataSource.isClosed());
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        dataSource.innerCreateConnection();
        verify(connection).setAutoCommit(dataSource.isAutoCommit());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection result = dataSource.getConnectionProxy(connection);
        assertNotNull(result);
        assertNotSame(connection, result);
        assertEquals(connection, ((InvocationHandler) Proxy.getInvocationHandler(result)).
                invoke(result, Proxy.getProxyClass().getMethods()[1], new Object[]{"h2"}));
    }

    @Test
    public void testCloseIgnoreInvocationHandler() throws SQLException {
        Connection proxyConnection = dataSource.getConnectionProxy(connection);
        Connection result = ((CloseIgnoreInvocationHandler)
                Proxy.getInvocationHandler(proxyConnection)).invoke(proxyConnection,
                Proxy.getProxyClass().getMethods()[13], null);
        assertNull(result);
    }
}