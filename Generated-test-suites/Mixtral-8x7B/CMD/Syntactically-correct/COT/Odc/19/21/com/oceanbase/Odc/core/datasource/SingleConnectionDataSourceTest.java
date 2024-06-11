import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private Connection connection;
private Lock lock;
private EventPublisher eventPublisher;
private ConnectionInitializer connectionInitializer;

@Before
public void setup() {
this.dataSource = new SingleConnectionDataSource();
this.connection = mock(Connection.class);
this.lock = new ReentrantLock();
this.eventPublisher = mock(EventPublisher.class);
this.connectionInitializer = mock(ConnectionInitializer.class);

dataSource.setConnection(connection);
dataSource.setLock(lock);
dataSource.setEventPublisher(eventPublisher);
dataSource.addInitializer(connectionInitializer);
}

@After
public void teardown() {
if (dataSource != null) {
dataSource.close();
}
}

@Test
public void testGetConnection() throws SQLException {
Connection result = dataSource.getConnection();
Assert.assertEquals(connection, result);
}

@Test
public void testGetConnectionWithInvalidUsernameAndPassword() {
Assert.assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPassword"));
verify(connection, never()).close();
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();
verify(connection, times(1)).close();
}

@Test
public void testAddInitializer() {
List<ConnectionInitializer> initializers = new ArrayList<>();
initializers.add(connectionInitializer);

dataSource.addInitializer(connectionInitializer);
Assert.assertEquals(initializers, dataSource.getInitializerList());
}

@Test
public void testGetConnectionProxy() {
Connection result = dataSource.getConnectionProxy(connection);
Assert.assertNotSame(connection, result);
}

@Test
public void testPrepareConnection() throws SQLException {
Boolean autoCommit = true;
dataSource.setAutoCommit(autoCommit);

dataSource.prepareConnection(connection);
verify(connection, times(1)).setAutoCommit(autoCommit);
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithLockTimeout()
throws SQLException, ConflictException {
try (MockedStatic<TimeUnit> timeUnitMock = Mockito.mockStatic(TimeUnit.class)) {
timeUnitMock.when(() -> TimeUnit.MILLISECONDS.toNanos(anyLong())).thenReturn(1L);
dataSource.getConnectionProxy(connection);
}
}

@Test
public void testInitConnection() throws SQLException {
dataSource.initConnection(connection);
verify(connectionInitializer, times(1)).init(connection);
}

@Test
public void testOnConnectionCreated() {
dataSource.onConnectionCreated(connection);
verify(connection, times(1)).isValid(dataSource.getLoginTimeout());
}

@Test
public void testOnConnectionReset() throws SQLException {
dataSource.onConnectionReset(connection);
verify(eventPublisher, times(1)).publishEvent(any());
}

@Test
public void testInnerCreateConnection() throws SQLException {
Connection result = dataSource.innerCreateConnection();
Assert.assertSame(connection, result);
}

private class TestInvocationHandler implements InvocationHandler {

private final Connection target;
private final Lock lock;

public TestInvocationHandler(Connection target, Lock lock) {
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
}
try {
return method.invoke(this.target, args);
} catch (InvocationTargetException ex) {
throw ex.getTargetException();
}
}
}
}