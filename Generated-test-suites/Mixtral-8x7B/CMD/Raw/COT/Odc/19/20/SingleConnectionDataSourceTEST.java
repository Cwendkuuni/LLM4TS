import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

    private static final String VALID_USERNAME = "validUsername";
    private static final String VALID_PASSWORD = "validPassword";

    @Mock
    private ConnectionInitializer connectionInitializerMock;

    @Mock
    private EventPublisher eventPublisherMock;

    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        dataSource = new SingleConnectionDataSource(true);
        dataSource.addInitializer(connectionInitializerMock);
        dataSource.setEventPublisher(eventPublisherMock);
    }

    @After
    public void tearDown() throws SQLException {
        dataSource.close();
    }

    @Test
    public void testGetConnectionWithValidCredentials() throws SQLException {
        Connection connection = dataSource.getConnection(VALID_USERNAME, VALID_PASSWORD);
        assertConnection(connection);
    }

    @Test
    public void testGetConnectionWithoutCredentials() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertConnection(connection);
    }

    @Test
    public void testGetConnectionWithInvalidCredentials() {
        assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUsername", "invalidPassword"));
    }

    @Test
    public void testResetConnection() throws SQLException {
        Connection initialConnection = dataSource.getConnection();
        dataSource.resetConnection();

        Connection newConnection = dataSource.getConnection();
        assertConnection(newConnection);
        assertFalse(initialConnection.equals(newConnection));
    }

    @Test
    public void testAddInitializer() {
        dataSource.addInitializer(connectionInitializerMock);
        assertEquals(1, dataSource.getInitializerList().size());
    }

    @Test
    public void testClose() throws SQLException {
        Connection connection = dataSource.getConnection();
        dataSource.close();

        assertNull(dataSource.getConnection());
        assertFalse(connection.isClosed());
    }

    @Test
    public void testGetConnectionProxy() throws SQLException {
        Connection connection = dataSource.getConnection();
        assertConnection(connection);

        ArgumentCaptor<InvocationHandler> invocationHandlerArgumentCaptor = ArgumentCaptor.forClass(InvocationHandler.class);
        verify(connection).getClass().getMethod("getClassLoader").invoke(connection);
        verify(connection).getClass().getInterfaces();
        verify(connection).getClass().getClassLoader();
        verify(connection).getClass().getInterfaces();

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethods();
        verify(connection).getClass().getDeclaredMethods();
        verify(connection).getClass().getInterfaces();

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("isWrapperFor", Class.class);
        verify(connection).getClass().getMethod("unwrap", Class.class);
        verify(connection).getClass().getMethod("isClosed");

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("createStatement");
        verify(connection).getClass().getMethod("prepareStatement", String.class);
        verify(connection).getClass().getMethod("prepareStatement", String.class, int[].class);
        verify(connection).getClass().getMethod("prepareCall", String.class);

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("setAutoCommit", boolean.class);
        verify(connection).getClass().getMethod("getAutoCommit");
        verify(connection).getClass().getMethod("commit");
        verify(connection).getClass().getMethod("rollback");
        verify(connection).getClass().getMethod("close");

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("getCatalog");
        verify(connection).getClass().getMethod("getSchema");

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("createStruct", String.class, Object[].class);
        verify(connection).getClass().getMethod("createArrayOf", Class.class, Object[].class);
        verify(connection).getClass().getMethod("getMetaData");

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("nativeSQL", String.class);

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("prepareStatement", String.class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareStatement", String.class, int[].class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareStatement", String.class, int.class);
        verify(connection).getClass().getMethod("prepareStatement", String.class, int[].class);

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("setObject", int.class, Object.class, int.class);
        verify(connection).getClass().getMethod("setObject", String.class, Object.class, int.class);

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("getWarnings");
        verify(connection).getClass().getMethod("clearWarnings");

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("getTransactionIsolation");
        verify(connection).getClass().getMethod("setTransactionIsolation", int.class);

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("setTypeMap", java.util.Map.class);

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("setHoldability", int.class);

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("getHoldability");

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("getSchema", String.class);

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("createStatement", int.class, int.class);

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("createStatement", int.class);

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("createStatement");

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("prepareCall", String.class, int.class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareCall", String.class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareCall", String.class, int[].class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareCall", String.class, int[].class, int.class);
        verify(connection).getClass().getMethod("prepareCall", String.class, int.class, int.class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareCall", String.class, int.class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareCall", String.class, int[].class, int.class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareCall", String.class, int[].class, int.class, int.class);

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("prepareStatement", String.class, int.class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareStatement", String.class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareStatement", String.class, int[].class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareStatement", String.class, int[].class, int.class);
        verify(connection).getClass().getMethod("prepareStatement", String.class, int.class, int.class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareStatement", String.class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareStatement", String.class, int[].class, int.class, int.class);
        verify(connection).getClass().getMethod("prepareStatement", String.class, int[].class, int.class);

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("setAutoCommit", boolean.class);
        verify(connection, never()).setAutoCommit(any(Boolean.class));

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("getAutoCommit");
        verify(connection, never()).getAutoCommit();

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("commit");
        verify(connection, never()).commit();

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("rollback");
        verify(connection, never()).rollback();

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection).getClass().getMethod("close");
        verify(connection, times(1)).close();

        Mockito.verifyNoMoreInteractions(connection);

        verify(connection, invocationHandlerArgumentCaptor.capture()).getClass();
        assertEquals(CloseIgnoreInvocationHandler.class, invocationHandlerArgumentCaptor.getValue().getClass());

        CloseIgnoreInvocationHandler closeIgnoreInvocationHandler = (CloseIgnoreInvocationHandler) invocationHandlerArgumentCaptor.getValue();
        assertEquals(connection, closeIgnoreInvocationHandler.getTarget());
        assertEquals(dataSource.lock, closeIgnoreInvocationHandler.getLock());

        Mockito.verifyNoMoreInteractions(connection);
    }

    @Test
    public void testTimeout() throws SQLException {
        dataSource.setTimeOutMillis(100);

        assertThrows(ConflictException.class, () -> getConnectionProxy(dataSource.innerCreateConnection()));
    }

    @Test
    public void testPublishConnectionResetEvent() {
        dataSource.onConnectionReset(mock(Connection.class));

        verify(eventPublisherMock).publishEvent(any(ConnectionResetEvent.class));
    }

    private void assertConnection(Connection connection) throws SQLException {
        assertEquals(VALID_USERNAME, connection.getMetaData().getUserName());
        assertEquals(VALID_PASSWORD, connection.getMetaData().getPassword());
    }

    private Connection getConnectionProxy(Connection connection) throws SQLException {
        return dataSource.getConnectionProxy(connection);
    }
}

class ConnectionInitializerMock implements SingleConnectionDataSource.ConnectionInitializer {

    @Override
    public void init(Connection connection) throws Exception {
    }
}