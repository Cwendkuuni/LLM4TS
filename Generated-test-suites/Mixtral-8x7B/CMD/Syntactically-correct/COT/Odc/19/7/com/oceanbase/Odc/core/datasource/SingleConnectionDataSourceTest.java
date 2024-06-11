import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.event.ConnectionEventPublisher;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource testDataSource;
private ConnectionInitializer connectionInitializer;
private ConnectionResetEvent connectionResetEvent;
private ConnectionResetEvent receivedConnectionResetEvent;
private ExecutorService executorService;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource();
testDataSource = new TestDataSource();
connectionInitializer = (Connection connection) -> {
// Do nothing
};
connectionResetEvent = new ConnectionResetEvent(testDataSource.getConnection());
executorService = Executors.newSingleThreadExecutor();
}

@After
public void teardown() throws Exception {
if (dataSource != null) {
dataSource.close();
}
if (testDataSource != null) {
testDataSource.close();
}
if (executorService != null) {
executorService.shutdown();
executorService.awaitTermination(1, TimeUnit.SECONDS);
}
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
Assert.assertNotNull(connection);
Assert.assertEquals(dataSource.getConnection(), connection);
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
dataSource.setUsername("testUser");
dataSource.setPassword("testPassword");
Connection connection = dataSource.getConnection("testUser", "testPassword");
Assert.assertNotNull(connection);
Assert.assertEquals(dataSource.getConnection(), connection);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
dataSource.setUsername("testUser");
dataSource.setPassword("testPassword");
dataSource.getConnection("invalidUser", "invalidPassword");
}

@Test(expected = SQLException.class)
public void testGetConnectionWithNullConnection() throws SQLException {
dataSource.setConnection(null);
dataSource.getConnection();
}

@Test(expected = SQLException.class)
public void testGetConnectionWithClosedConnection() throws SQLException {
dataSource.getConnection();
dataSource.close();
dataSource.getConnection();
}

@Test
public void testResetConnection() throws SQLException {
dataSource.getConnection();
dataSource.resetConnection();
Assert.assertNull(dataSource.getConnection());
}

@Test
public void testAddInitializer() throws SQLException {
ConnectionInitializer initializer1 = (connection) -> {
// Do nothing
};
ConnectionInitializer initializer2 = (connection) -> {
// Do nothing
};
dataSource.addInitializer(initializer1);
dataSource.addInitializer(initializer2);
Assert.assertEquals(2, dataSource.getInitializerList().size());
}

@Test
public void testAddInitializerWithNullInitializer() {
Assert.assertThrows(NullPointerException.class, () -> dataSource.addInitializer(null));
}

@Test
public void testClose() throws SQLException {
dataSource.getConnection();
dataSource.close();
Assert.assertNull(dataSource.getConnection());
}

@Test
public void testInitConnection() throws SQLException {
Connection connection = newConnectionFromDriver("testUser", "testPassword");
dataSource.initConnection(connection);
Assert.assertEquals(connection, dataSource.getConnection());
}

@Test
public void testOnConnectionCreated() throws SQLException {
dataSource.setConnection(newConnectionFromDriver("testUser", "testPassword"));
dataSource.onConnectionCreated(dataSource.getConnection());
Assert.assertEquals("Connection is created", dataSource.getConnectionEventPublisher().getEvents().get(0).getMessage());
}

@Test
public void testOnConnectionReset() throws SQLException {
dataSource.setConnection(newConnectionFromDriver("testUser", "testPassword"));
dataSource.setEventPublisher(new ConnectionEventPublisher());
dataSource.onConnectionReset(dataSource.getConnection());
receivedConnectionResetEvent = dataSource.getConnectionEventPublisher().getEvents().get(0);
Assert.assertEquals(connectionResetEvent, receivedConnectionResetEvent);
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithLockedConnection() throws SQLException, InterruptedException {
Connection connection = dataSource.getConnection();
ExecutorService executorService = Executors.newSingleThreadExecutor();
executorService.execute(() -> {
try {
dataSource.getConnectionProxy(connection);
} catch (ConflictException e) {
// Expected
}
});
executorService.shutdown();
executorService.awaitTermination(1, TimeUnit.SECONDS);
}

private Connection newConnectionFromDriver(String username, String password) throws SQLException {
return DriverManager.getConnection("jdbc:h2:mem:test", username, password);
}

private static class TestDataSource extends SingleConnectionDataSource {
@Override
protected Connection newConnectionFromDriver(String username, String password) throws SQLException {
return DriverManager.getConnection("jdbc:h2:mem:test", username, password);
}
}

private static class TestInvocationHandler implements InvocationHandler {
private final Connection connection;
private final Lock lock;

public TestInvocationHandler(Connection connection, Lock lock) {
this.connection = connection;
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
return method.invoke(this.connection, args);
}
}
}