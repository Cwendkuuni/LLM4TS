import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connection.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connection.ConnectionInitializerImpl;
import com.oceanbase.odc.core.datasource.connection.ConnectionResetEvent;
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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SingleConnectionDataSourceTest {

    @Mock
    private EventPublisher eventPublisher;

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataSource = new SingleConnectionDataSource(true);
        dataSource.setEventPublisher(eventPublisher);
        dataSource.addInitializer(new ConnectionInitializerImpl());
    }

    @After
    public void tearDown() throws SQLException {
        dataSource.close();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection1 = dataSource.getConnection();
        assertNotNull(connection1);
        assertTrue(connection1.isClosed());

        Connection connection2 = dataSource.getConnection();
        assertNotNull(connection2);
        assertFalse(connection2.isClosed());
        assertEquals(connection1.hashCode(), connection2.hashCode());
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws SQLException {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalid", "invalid"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        dataSource.getConnection();
        dataSource.resetConnection();
        assertNull(dataSource.getConnection());
    }

    @Test
    public void testAddInitializer() {
        List<ConnectionInitializer> initializers = dataSource.getInitializerList();
        assertEquals(1, initializers.size());

        ConnectionInitializer initializer2 = new ConnectionInitializerImpl();
        dataSource.addInitializer(initializer2);
        initializers = dataSource.getInitializerList();
        assertEquals(2, initializers.size());
        assertTrue(initializers.contains(initializer2));
    }

    @Test
    public void testClose() throws SQLException {
        dataSource.getConnection();
        dataSource.close();
        assertThrows(SQLException.class, dataSource::getConnection);
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);
        Lock lock = Mockito.mock(Lock.class);

        Mockito.when(lock.tryLock(any(long.class), any(TimeUnit.class))).thenReturn(true);

        Connection proxy = dataSource.getConnectionProxy(connection);
        assertNotNull(proxy);

        Method closeMethod = Proxy.getInvocationHandler(proxy).getClass().getDeclaredMethod("close");
        closeMethod.setAccessible(true);
        closeMethod.invoke(Proxy.getInvocationHandler(proxy));

        verify(lock, times(1)).unlock();
    }

    @Test
    public void testGetConnectionProxyWithLockTimeout() {
        Connection connection = Mockito.mock(Connection.class);
        Lock lock = Mockito.mock(Lock.class);

        Mockito.when(lock.tryLock(any(long.class), any(TimeUnit.class))).thenReturn(false);

        assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));
    }

    @Test
    public void testInnerCreateConnection() throws SQLException {
        Connection connection = dataSource.innerCreateConnection();
        assertNotNull(connection);
        assertTrue(connection.isClosed());
    }

    @Test
    public void testPrepareConnection() throws SQLException {
        Connection connection = Mockito.mock(Connection.class);

        dataSource.prepareConnection(connection);

        verify(connection, times(1)).setAutoCommit(dataSource.getAutoCommit());
    }

    @Test
    public void testOnConnectionReset() {
        Connection connection = Mockito.mock(Connection.class);
        dataSource.setConnection(connection);

        dataSource.onConnectionReset(connection);

        ArgumentCaptor<ConnectionResetEvent> captor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
        verify(eventPublisher, times(1)).publishEvent(captor.capture());
        ConnectionResetEvent event = captor.getValue();
        assertEquals(connection, event.getConnection());
    }

    @Test
    public void testNewConnectionFromDriver() throws SQLException {
        DataSource dataSourceMock = Mockito.mock(DataSource.class);
        Method newConnectionMethod = Mockito.spy(DataSource.class.getDeclaredMethod("getConnection"));
        Mockito.when(dataSourceMock.getConnection()).thenReturn(newConnectionMethod);

        Connection connection = SingleConnectionDataSource.newConnectionFromDriver(dataSourceMock, "user", "password");

        Mockito.verify(newConnectionMethod, times(1)).invoke(dataSourceMock);
        assertEquals("user", connection.getMetaData().getUserName());
        assertEquals("password", connection.getMetaData().getPassword());
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        Connection connection = Mockito.mock(Connection.class);
        Lock lock = new ReentrantLock();
        InvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, lock);

        Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class },
                handler);

        assertEquals(connection, ((Connection) proxy).unwrap(Connection.class));
        assertTrue(((Connection) proxy).isWrapperFor(Connection.class));

        Method closeMethod = Proxy.getInvocationHandler(proxy).getClass().getDeclaredMethod("close");
        closeMethod.setAccessible(true);
        closeMethod.invoke(Proxy.getInvocationHandler(proxy));
        assertFalse(lock.isHeldByCurrentThread());

        assertThrows(SQLException.class, ((Connection) proxy)::close);
        assertThrows(SQLException.class, ((Connection) proxy)::isClosed);

        Mockito.verify(handler, times(1)).invoke(connection, Mockito.any(), Mockito.any(), Mockito.any());
    }
}