import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.ODCException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingleConnectionDataSourceTest {

@Mock
private EventPublisher eventPublisher;

private SingleConnectionDataSource dataSource;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dataSource = new SingleConnectionDataSource(true);
}

@After
public void tearDown() throws SQLException {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void testGetConnectionWhenConnectionIsClosed() throws SQLException {
Connection connection = dataSource.getConnection();
connection.close();
dataSource.getConnection();
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
String username = "testUser";
String password = "testPassword";
dataSource.setUsername(username);
dataSource.setPassword(password);
Connection connection = dataSource.getConnection(username, password);
assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
String username = "testUser";
String password = "testPassword";
String invalidUsername = "invalidUser";
String invalidPassword = "invalidPassword";
dataSource.setUsername(username);
dataSource.setPassword(password);
dataSource.getConnection(invalidUsername, invalidPassword);
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();
Connection connection = dataSource.getConnection();
assertNotNull(connection);
}

@Test
public void testAddInitializer() {
ConnectionInitializer initializer1 = Mockito.mock(ConnectionInitializer.class);
ConnectionInitializer initializer2 = Mockito.mock(ConnectionInitializer.class);
dataSource.addInitializer(initializer1);
dataSource.addInitializer(initializer2);
List<ConnectionInitializer> initializers = new ArrayList<>(dataSource.getInitializerList());
assertEquals(2, initializers.size());
assertEquals(initializer1, initializers.get(0));
assertEquals(initializer2, initializers.get(1));
}

@Test
public void testClose() throws SQLException {
Connection connection = dataSource.getConnection();
dataSource.close();
assertNull(dataSource.getConnection());
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxy() {
Lock lock = new ReentrantLock();
dataSource.getConnectionProxy(Mockito.mock(Connection.class));
assertFalse(lock.tryLock());
}

@Test
public void testPrepareConnection() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
dataSource.prepareConnection(connection);
Mockito.verify(connection).setAutoCommit(dataSource.getAutoCommit());
}

@Test
public void testInitConnection() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
dataSource.initConnection(connection);
List<ConnectionInitializer> initializers = new ArrayList<>(dataSource.getInitializerList());
for (ConnectionInitializer initializer : initializers) {
initializer.init(connection);
}
}

@Test
public void testOnConnectionCreated() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
dataSource.onConnectionCreated(connection);
}

@Test
public void testOnConnectionReset() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
dataSource.onConnectionReset(connection);
}

@Test(expected = IllegalStateException.class)
public void testInnerCreateConnectionWhenConnectionIsNotNull() throws SQLException {
dataSource.innerCreateConnection();
}

private static Connection newConnectionFromDriver(String username, String password) throws ODCException {
try {
return DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", username, password);
} catch (SQLException e) {
throw new ODCException(e);
}}

private static class TestConnectionInitializer implements ConnectionInitializer {

@Override
public void init(Connection connection) throws Exception {
// Do nothing
}
}

private static class TestInvocationHandler implements InvocationHandler {

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
} else {
throw new RuntimeException("Unexpected method call: " + method.getName());
}
return method.invoke(this.target, args);
}
}
}