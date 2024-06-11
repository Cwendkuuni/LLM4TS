import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.TestConnectionInitializer;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private Connection connection;
    private DataSource mockDataSource;
    private EventPublisher mockEventPublisher;
    private ExecutorService executorService;

    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource();
        connection = mock(Connection.class);
        mockDataSource = mock(DataSource.class);
        mockEventPublisher = mock(EventPublisher.class);
        dataSource.setEventPublisher(mockEventPublisher);
        executorService = Executors.newFixedThreadPool(10);
    }

    @After
    public void tearDown() throws Exception {
        if (dataSource != null) {
            dataSource.close();
        }
        executorService.shutdownNow();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
    }

    @Test
    public void testGetConnection_WithoutInitialization() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(connection);
        dataSource.setDataSource(mockDataSource);

        Connection result = dataSource.getConnection();

        assertNotNull(result);
        verify(connection, times(1)).setAutoCommit(false);
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void testGetConnection_WithInitialization() throws Exception {
        TestConnectionInitializer initializer = new TestConnectionInitializer();
        dataSource.addInitializer(initializer);

        Connection result = dataSource.getConnection();

        assertNotNull(result);
        assertTrue(initializer.isInitialized());
        verify(connection, times(1)).setAutoCommit(false);
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void testGetConnection_WithWrongCredentials() {
        when(mockDataSource.getConnection(anyString(), anyString())).thenThrow(SQLException.class);
        dataSource.setDataSource(mockDataSource);

        assertThrows(SQLException.class, dataSource::getConnection);
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalid", "invalid"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(connection);
        dataSource.setDataSource(mockDataSource);
        dataSource.getConnection();

        dataSource.resetConnection();

        verify(mockDataSource, times(2)).getConnection();
        verify(connection, times(1)).close();
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        when(mockDataSource.getConnection()).thenReturn(connection);
        dataSource.setDataSource(mockDataSource);

        Connection result = dataSource.getConnectionProxy(connection);

        assertNotNull(result);
        assertTrue(result instanceof SingleConnectionDataSource.CloseIgnoreInvocationHandler);
        verify(connection, times(1)).setAutoCommit(false);
    }

    @Test
    public void testClose() throws Exception {
        when(mockDataSource.getConnection()).thenReturn(connection);
        dataSource.setDataSource(mockDataSource);

        dataSource.close();

        verify(connection, times(1)).close();
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void testPrepareConnection() throws Exception {
        final boolean[] called = {false};

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                called[0] = true;
                Method method = invocationOnMock.getMethod();
                assertEquals("setAutoCommit", method.getName());
                return null;
            }
        }).when(connection).setAutoCommit(anyBoolean());

        dataSource.prepareConnection(connection);

        assertTrue(called[0]);
    }

    @Test
    public void testTryLock_Acquired() throws Exception {
        dataSource.lock = new ReentrantLock();

        boolean result = dataSource.tryLock();

        assertTrue(result);
        assertTrue(dataSource.lock.isHeldByCurrentThread());
    }

    @Test
    public void testTryLock_TimedOut() throws Exception {
        dataSource.lock = new ReentrantLock();
        dataSource.timeOutMillis = 1;

        boolean result = dataSource.tryLock();

        assertFalse(result);
        assertFalse(dataSource.lock.isHeldByCurrentThread());
    }

    @Test
    public void testInnerCreateConnection() throws Exception {
        when(mockDataSource.getConnection()).thenReturn(connection);

        Connection result = dataSource.innerCreateConnection();

        assertNotNull(result);
        verify(connection, times(1)).setAutoCommit(false);
        verify(mockDataSource, times(1)).getConnection();
    }

    @Test
    public void testConnectionResetEvent_Published() throws Exception {
        dataSource.setEventPublisher(mockEventPublisher);

        dataSource.resetConnection();

        ArgumentCaptor<ConnectionResetEvent> argumentCaptor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        verify(mockEventPublisher, times(1)).publishEvent(argumentCaptor.capture());
        assertEquals(connection, argumentCaptor.getValue().getConnection());
    }

    @Test
    public void testConnectionResetEvent_NotPublished() throws Exception {
        dataSource.setEventPublisher(null);

        dataSource.resetConnection();

        verify(mockEventPublisher, times(0)).publishEvent(any());
    }

    @Test
    public void testMultipleThreads() throws Exception {
        when(mockDataSource.getConnection()).thenReturn(connection);
        dataSource.setDataSource(mockDataSource);

        List<Runnable> tasks = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            tasks.add(() -> {
                try {
                    dataSource.getConnection();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.invokeAll(tasks);

        verify(connection, times(100)).setAutoCommit(false);
        verify(mockDataSource, times(100)).getConnection();
    }

    @Test
    public void testGetConnectionProxy_Close() throws Exception {
        when(mockDataSource.getConnection()).thenReturn(connection);
        dataSource.setDataSource(mockDataSource);

        Connection connectionProxy = dataSource.getConnectionProxy(connection);
        connectionProxy.close();

        verify(connection, times(1)).close();
    }

    @Test
    public void testGetConnection_Interrupted() throws Exception {
        when(mockDataSource.getConnection()).thenReturn(connection);
        dataSource.setDataSource(mockDataSource);

        Thread.currentThread().interrupt();

        assertThrows(ConflictException.class, dataSource::getConnection);
    }

    @Test
    public void testCloseIgnoreInvocationHandler_Equals() throws Exception {
        SingleConnectionDataSource.CloseIgnoreInvocationHandler handler =
                new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, dataSource.lock);

        assertTrue(handler.equals(connection));
    }

    @Test
    public void testCloseIgnoreInvocationHandler_HashCode() throws Exception {
        SingleConnectionDataSource.CloseIgnoreInvocationHandler handler =
                new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, dataSource.lock);

        assertEquals(System.identityHashCode(connection), handler.hashCode());
    }
}