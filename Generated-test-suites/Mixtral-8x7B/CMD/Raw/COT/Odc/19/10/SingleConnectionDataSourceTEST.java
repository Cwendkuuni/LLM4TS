import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

    private SingleConnectionDataSource dataSource;
    private DataSource testDataSource;
    private ConnectionInitializer connectionInitializer;
    private ConnectionInitializer connectionInitializer2;
    private Connection connection;
    private Lock lock;

    @Before
    public void setUp() throws Exception {
        dataSource = new SingleConnectionDataSource();
        testDataSource = mock(DataSource.class);
        connectionInitializer = new ConnectionInitializerImpl();
        connectionInitializer2 = new ConnectionInitializerImpl();
        dataSource.addInitializer(connectionInitializer);
        dataSource.addInitializer(connectionInitializer2);
        connection = mock(Connection.class);
        lock = new ReentrantLock();
        Field field = dataSource.getClass().getDeclaredField("lock");
        field.setAccessible(true);
        field.set(dataSource, lock);
    }

    @After
    public void tearDown() throws Exception {
        dataSource.close();
    }

    @Test
    public void testGetConnectionWithDefaults() throws Exception {
        when(testDataSource.getConnection()).thenReturn(connection);
        Field field = dataSource.getClass().getDeclaredField("dataSource");
        field.setAccessible(true);
        field.set(dataSource, testDataSource);
        Connection result = dataSource.getConnection();
        assertEquals(connection, result);
        verify(testDataSource, times(1)).getConnection();
    }

    @Test
    public void testGetConnectionWithUsernameAndPassword() throws Exception {
        when(testDataSource.getConnection("testUser", "testPassword")).thenReturn(connection);
        Field field = dataSource.getClass().getDeclaredField("dataSource");
        field.setAccessible(true);
        field.set(dataSource, testDataSource);
        Connection result = dataSource.getConnection("testUser", "testPassword");
        assertEquals(connection, result);
        verify(testDataSource, times(1)).getConnection("testUser", "testPassword");
    }

    @Test(expected = SQLException.class)
    public void testGetConnectionWithInvalidUsernameAndPassword() throws Exception {
        when(testDataSource.getConnection("testUser", "testPassword")).thenThrow(new SQLException());
        Field field = dataSource.getClass().getDeclaredField("dataSource");
        field.setAccessible(true);
        field.set(dataSource, testDataSource);
        dataSource.getConnection("testUser", "testPassword");
    }

    @Test
    public void testResetConnection() throws Exception {
        dataSource.connection = connection;
        dataSource.resetConnection();
        assertNull(dataSource.connection);
        assertNull(dataSource.lock);
        verify(connection, times(1)).close();
    }

    @Test
    public void testAddInitializer() throws Exception {
        int initializerCountBefore = dataSource.initializerList.size();
        dataSource.addInitializer(connectionInitializer);
        int initializerCountAfter = dataSource.initializerList.size();
        assertEquals(initializerCountBefore + 1, initializerCountAfter);
    }

    @Test
    public void testClose() throws Exception {
        dataSource.connection = connection;
        dataSource.close();
        verify(connection, times(1)).close();
    }

    @Test
    public void testPrepareConnection() throws Exception {
        dataSource.prepareConnection(connection);
        verify(connection, times(1)).setAutoCommit(dataSource.autoCommit);
    }

    @Test
    public void testTryLock() throws Exception {
        assertTrue(dataSource.tryLock());
    }

    @Test
    public void testGetConnectionProxy() throws Exception {
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(anyInt())).thenReturn(false);
        Connection result = dataSource.getConnectionProxy(connection);
        assertNotNull(result);
        assertFalse(result.isClosed());
        verify(testDataSource, times(1)).getConnection();
    }

    @Test(expected = ConflictException.class)
    public void testGetConnectionProxyWithConnectionOccupied() throws Exception {
        when(connection.isClosed()).thenReturn(true);
        when(connection.isValid(anyInt())).thenReturn(false);
        SingleConnectionDataSource dataSource2 = new SingleConnectionDataSource();
        Field field = dataSource2.getClass().getDeclaredField("lock");
        field.setAccessible(true);
        field.set(dataSource2, lock);
        dataSource2.getConnectionProxy(connection);
    }

    @Test
    public void testInitConnection() throws Exception {
        dataSource.initConnection(connection);
        verify(connectionInitializer, times(1)).init(connection);
        verify(connectionInitializer2, times(1)).init(connection);
    }

    @Test
    public void testOnConnectionCreated() throws Exception {
        dataSource.onConnectionCreated(connection);
        assertTrue(log.isDebugEnabled());
    }

    @Test
    public void testOnConnectionReset() throws Exception {
        EventPublisher eventPublisher = mock(EventPublisher.class);
        dataSource.eventPublisher = eventPublisher;
        dataSource.onConnectionReset(connection);
        verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
    }

    @Test
    public void testInnerCreateConnection() throws Exception {
        when(dataSource.newConnectionFromDriver(anyString(), anyString())).thenReturn(connection);
        Connection result = dataSource.innerCreateConnection();
        assertEquals(connection, result);
        verify(connectionInitializer, times(1)).init(connection);
        verify(connectionInitializer2, times(1)).init(connection);
        verify(dataSource, times(1)).onConnectionCreated(connection);
    }

    @Test(expected = IllegalStateException.class)
    public void testInnerCreateConnectionWithExistingConnection() throws Exception {
        dataSource.connection = connection;
        dataSource.innerCreateConnection();
    }

    @Test
    public void testCloseIgnoreInvocationHandler() {
        CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
        assertTrue(handler.invoke(null, getClass("equals"), new Object[] {connection}).equals(true));
        assertTrue(handler.invoke(null, getClass("hashCode"), null).equals(connection.hashCode()));
        assertNull(handler.invoke(null, getClass("unwrap"), new Class[] {Connection.class},
                new Object[] {Connection.class}));
        assertTrue(handler.invoke(null, getClass("isWrapperFor"), new Class[] {Connection.class},
                new Object[] {Connection.class}).equals(true));
        handler.invoke(null, getClass("close"), null, null);
        assertNull(handler.invoke(null, getClass("isClosed"), null, null));
    }

    private Method getClass(String methodName) {
        try {
            return Class.class.getDeclaredMethod(methodName, String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}