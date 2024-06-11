import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource dataSourceMock;
private Connection connectionMock;
private ConnectionInitializer connectionInitializerMock;
private InvocationHandler invocationHandlerMock;
private Lock lock;

@Before
public void setUp() {
dataSourceMock = mock(DataSource.class);
connectionMock = mock(Connection.class);
connectionInitializerMock = mock(ConnectionInitializer.class);
invocationHandlerMock = mock(InvocationHandler.class);
lock = new ReentrantLock();

when(dataSourceMock.getConnection()).thenReturn(connectionMock);
dataSource = new SingleConnectionDataSource(dataSourceMock, connectionInitializerMock);
}

@Test
public void testGetConnection() throws SQLException {
dataSource.setAutoCommit(true);
dataSource.setTimeOutMillis(5000);

Connection connection = dataSource.getConnection();
assertTrue(connection.isClosed());

verify(dataSourceMock, times(1)).getConnection();
verify(connectionMock, times(1)).isClosed();
}

@Test
public void testGetConnectionWithInvalidUsernameOrPassword() {
assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPassword"));
}

@Test
public void testResetConnection() throws SQLException {
dataSource.setAutoCommit(true);
dataSource.setTimeOutMillis(5000);
dataSource.getConnection();

verify(connectionMock, times(1)).close();
verify(dataSourceMock, times(1)).getConnection();

Connection connection = dataSource.getConnection();
assertFalse(connection.isClosed());
}

@Test
public void testAddInitializer() {
List<ConnectionInitializer> initializers = new LinkedList<>();
initializers.add(connectionInitializerMock);

dataSource.addInitializer(connectionInitializerMock);
assertEquals(initializers, dataSource.getInitializerList());
}

@Test
public void testClose() throws SQLException {
dataSource.setAutoCommit(true);
dataSource.setTimeOutMillis(5000);
dataSource.getConnection();

dataSource.close();
verify(connectionMock, times(1)).close();
}

@Test
public void testPrepareConnection() throws SQLException {
Boolean autoCommit = true;
when(connectionMock.getAutoCommit()).thenReturn(false);

dataSource.prepareConnection(connectionMock);
verify(connectionMock, times(1)).setAutoCommit(autoCommit);
}

@Test
public void testTryLock() throws InterruptedException {
lock.lock();
assertTrue(dataSource.tryLock());
assertFalse(dataSource.tryLock());
lock.unlock();
}

@Test
public void testGetConnectionProxy() {
InvocationHandler invocationHandler = Proxy.getInvocationHandler(dataSource.getConnectionProxy(connectionMock));
assertEquals(invocationHandlerMock, invocationHandler);
}

@Test
public void testOnConnectionReset() {
dataSource.setEventPublisher(mock(EventPublisher.class));
dataSource.onConnectionReset(connectionMock);
verify(dataSource.getEventPublisher(), times(1)).publishEvent(any(SingleConnectionDataSource.ConnectionResetEvent.class));
}

@Test
public void testInnerCreateConnection() throws SQLException {
Connection connection = dataSource.innerCreateConnection();
verify(dataSourceMock, times(1)).getConnection();
verify(connection, times(1)).isClosed();
verify(connectionMock, times(1)).close();
verify(connectionInitializerMock, times(1)).init(connection);
}

private static class CloseIgnoreInvocationHandlerMock implements InvocationHandler {

private final Connection target;
private final Lock lock;

public CloseIgnoreInvocationHandlerMock(Connection target, Lock lock) {
this.target = target;
this.lock = lock;
}

@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
if ("equals".equals(method.getName())) {
return (proxy == args[0]);
} else if ("hashCode".equals(method.getName())) {
return System.identityHashCode(proxy);
} else if ("unwrap".equals(method.getName())) {
if (((Class<?>) args[0]).isInstance(proxy)) {
return proxy;
}
} else if ("isWrapperFor".equals(method.getName())) {
if (((Class<?>) args[0]).isInstance(proxy)) {
return true;
}
} else if ("close".equals(method.getName())) {
lock.unlock();
return null;
} else if ("isClosed".equals(method.getName())) {
return false;
} else if ("toString".equals(method.getName())) {
return target.toString();
}
return method.invoke(target, args);
}
}
}