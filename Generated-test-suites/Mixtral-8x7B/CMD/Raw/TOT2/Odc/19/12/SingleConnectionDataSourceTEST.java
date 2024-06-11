import com.oceanbase.odc.core.datasource.event.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.SQLException;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private Connection connection;
private Lock lock;
private EventPublisher eventPublisher;

@Before
public void setUp() throws Exception {
dataSource = new SingleConnectionDataSource();
connection = DriverManager.getConnection("jdbc:mock:test", "username", "password");
lock = new ReentrantLock();
eventPublisher = Mockito.mock(EventPublisher.class);
dataSource.setEventPublisher(eventPublisher);
}

@After
public void tearDown() throws Exception {
if (connection != null) {
connection.close();
}
}

@Test
public void testGetConnection() throws SQLException {
Connection result = dataSource.getConnection();
Assert.assertSame(getConnectionProxy(connection, lock), result);
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
Connection result = dataSource.getConnection("username", "password");
Assert.assertSame(getConnectionProxy(connection, lock), result);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
dataSource.getConnection("wrongUsername", "wrongPassword");
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();
ArgumentCaptor<ConnectionResetEvent> argumentCaptor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
Mockito.verify(eventPublisher).publishEvent(argumentCaptor.capture());
}

@Test
public void testAddInitializer() throws Exception {
ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
dataSource.addInitializer(initializer);
// Mocking the init method
Mockito.doNothing().when(initializer).init(connection);
dataSource.innerCreateConnection();
Mockito.verify(initializer).init(connection);
}

@Test(expected = SQLException.class)
public void testInitConnectionWithException() throws SQLException {
ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
Mockito.doThrow(new Exception()).when(initializer).init(connection);
dataSource.addInitializer(initializer);
dataSource.innerCreateConnection();
}

@Test
public void testClose() throws Exception {
dataSource.innerCreateConnection();
dataSource.close();
Assert.assertNull(dataSource.connection);
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithOccupiedConnection() throws Exception {
dataSource.innerCreateConnection();
dataSource.getConnectionProxy(connection);
dataSource.getConnectionProxy(connection);
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithTimeout() throws Exception {
dataSource.innerCreateConnection();
StopWatch stopWatch = new StopWatch();
stopWatch.start();
while (stopWatch.getTime() < 100) {
}
dataSource.getConnectionProxy(connection);
}

@Test
public void testGetConnectionProxy() throws Exception {
dataSource.innerCreateConnection();
Assert.assertSame(getConnectionProxy(connection, lock), dataSource.getConnectionProxy(connection));
}

private Connection getConnectionProxy(Connection connection, Lock lock) {
return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class},
new CloseIgnoreInvocationHandler(connection, lock));
}

private static class CloseIgnoreInvocationHandler implements InvocationHandler {
private final Connection target;
private final Lock lock;

public CloseIgnoreInvocationHandler(Connection target, Lock lock) {
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