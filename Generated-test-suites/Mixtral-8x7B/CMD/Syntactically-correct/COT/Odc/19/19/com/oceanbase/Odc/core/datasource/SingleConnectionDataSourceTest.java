import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource testDataSource;
private ConnectionInitializer initializer;
private EventPublisher eventPublisher;
private ReentrantLock lock;
private Connection connection;

@Before
public void setup() throws SQLException {
dataSource = new SingleConnectionDataSource();
testDataSource = mock(DataSource.class);
initializer = mock(ConnectionInitializer.class);
eventPublisher = mock(EventPublisher.class);
lock = new ReentrantLock();
connection = mock(Connection.class);

when(testDataSource.getConnection()).thenReturn(connection);

dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
dataSource.setUrl("jdbc:mysql://localhost:3306/mydb");
dataSource.setUsername("root");
dataSource.setPassword("password");
dataSource.setAutoReconnect(true);
dataSource.setEventPublisher(eventPublisher);
dataSource.addInitializer(initializer);
}

@After
public void tearDown() throws SQLException {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection result = dataSource.getConnection();
assertEquals(connection, result);
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPass"));
}

@Test
public void testGetConnectionWithAutoReconnect() throws SQLException {
dataSource.setAutoReconnect(false);
assertThrows(SQLException.class, () -> dataSource.getConnection());
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();
verify(connection, times(1)).close();
verify(eventPublisher, times(1)).publishEvent(Mockito.any(ConnectionResetEvent.class));
}

@Test
public void testAddInitializer() {
dataSource.addInitializer(initializer);
assertEquals(1, dataSource.getInitializerList().size());
}

@Test
public void testClose() throws SQLException {
dataSource.close();
assertFalse(dataSource.getConnectionProxy(connection).isValid(1));
}

@Test
public void testGetConnectionProxy() throws SQLException {
Connection result = dataSource.getConnectionProxy(connection);
assertTrue(result instanceof Proxy);
}

@Test
public void testPrepareConnection() throws SQLException {
when(connection.getAutoCommit()).thenReturn(true);
dataSource.prepareConnection(connection);
verify(connection, times(1)).setAutoCommit(dataSource.getAutoCommit());
}

@Test
public void testTryLock() throws InterruptedException {
assertTrue(dataSource.tryLock());
}

@Test
public void testInitConnection() throws SQLException {
dataSource.initConnection(connection);
verify(initializer, times(1)).init(connection);
}

@Test
public void testOnConnectionCreated() {
dataSource.onConnectionCreated(connection);
}

@Test
public void testOnConnectionReset() {
dataSource.onConnectionReset(connection);
verify(eventPublisher, times(1)).publishEvent(Mockito.any(ConnectionResetEvent.class));
}

@Test
public void testInnerCreateConnection() throws SQLException {
Connection result = dataSource.innerCreateConnection();
assertEquals(connection, result);
}

private static class TestConnectionInitializer implements ConnectionInitializer {

@Override
public void init(Connection connection) throws Exception {
// Do nothing
}
}

private static class CloseIgnoreInvocationHandlerTest implements InvocationHandler {

private final Connection target;
private final Lock lock;

public CloseIgnoreInvocationHandlerTest(Connection target, Lock lock) {
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