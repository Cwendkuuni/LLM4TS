package com.oceanbase.odc.core.datasource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

    @Mock
    private DataSource mockDataSource;
    @Mock
    private Connection mockConnection;
    @Mock
    private EventPublisher mockEventPublisher;
    @Mock
    private ConnectionInitializer mockConnectionInitializer;
    private AutoCloseable closeable;
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        dataSource = new SingleConnectionDataSource();
        dataSource.setDataSource(mockDataSource);
        dataSource.addInitializer(mockConnectionInitializer);
    }

    @After
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(mockConnection);

        Connection result = dataSource.getConnection();

        assertTrue(result instanceof Proxy);
        assertSame(mockConnection, result);
        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnectionInitializer, times(1)).init(mockConnection);
    }

    @Test
    public void testGetConnectionWithUserNameAndPassword() throws SQLException {
        String user = "testUser";
        String password = "testPassword";
        when(mockDataSource.getConnection(user, password)).thenReturn(mockConnection);

        Connection result = dataSource.getConnection(user, password);

        assertTrue(result instanceof Proxy);
        assertSame(mockConnection, result);
        verify(mockDataSource, times(1)).getConnection(user, password);
        verify(mockConnectionInitializer, never()).init(any());
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUserNameAndPassword() throws SQLException {
        String user = "invalidUser";
        String password = "invalidPassword";

        dataSource.getConnection(user, password);

        verify(mockDataSource, never()).getConnection(user, password);
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.setConnection(mockConnection);
        dataSource.resetConnection();

        verify(mockConnection, times(1)).close();
        verify(mockConnectionInitializer, times(1)).init(mockConnection);
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        dataSource.setConnection(mockConnection);
        Proxy proxy = (Proxy) dataSource.getConnectionProxy(mockConnection);
        assertNotNull(proxy);

        InvocationHandler handler = Proxy.getInvocationHandler(proxy);
        assertTrue(handler instanceof CloseIgnoreInvocationHandler);
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.setConnection(mockConnection);
        dataSource.close();

        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testCreateConnection() throws SQLException {
        dataSource.createConnection();

        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnectionInitializer, times(1)).init(mockConnection);
    }

    @Test
    public void testCreateConnectionWithInitializer() throws SQLException {
        dataSource.addInitializer(mockConnectionInitializer);
        dataSource.createConnection();

        verify(mockDataSource, times(1)).getConnection();
        verify(mockConnectionInitializer, times(1)).init(mockConnection);
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithLockTimeout() throws SQLException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                dataSource.getConnectionProxy(mockConnection);
            } catch (SQLException e) {
                fail(e.getMessage());
            }
        });

        TimeUnit.MILLISECONDS.sleep(dataSource.getTimeOutMillis() + 100);

        dataSource.getConnectionProxy(mockConnection);
    }

    @Test
    public void testEventPublisher() throws SQLException {
        dataSource.setEventPublisher(mockEventPublisher);
        dataSource.createConnection();

        verify(mockEventPublisher, times(1)).publishEvent(any());
    }
}