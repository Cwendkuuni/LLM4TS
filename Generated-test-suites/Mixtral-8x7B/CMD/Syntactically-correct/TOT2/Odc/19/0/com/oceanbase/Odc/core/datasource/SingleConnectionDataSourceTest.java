import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private ConnectionInitializer connectionInitializer;
private Lock lock;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource();
connectionInitializer = new ConnectionInitializerImpl();
lock = new ReentrantLock();

dataSource.addInitializer(connectionInitializer);
dataSource.setEventPublisher(event -> {});
dataSource.setAutoCommit(true);
dataSource.setTimeOutMillis(1000);
}

@After
public void teardown() throws SQLException {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertEquals(Connection.class, connection.getClass());
assertEquals(dataSource.getUsername(), connection.getMetaData().getUserName());
}

@Test
public void testGetConnectionWithUsernameAndPassword() {
assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUsername", "invalidPassword"));
}

@Test
public void testGetConnectionWithProxy() throws SQLException {
Connection connection = dataSource.getConnection();
Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class},
new CloseIgnoreInvocationHandler(connection, lock));

assertEquals(proxyConnection, dataSource.getConnectionProxy(connection));
}

@Test
public void testResetConnection() throws SQLException {
Connection initialConnection = dataSource.getConnection();
assertFalse(initialConnection.isClosed());

dataSource.resetConnection();
Connection newConnection = dataSource.getConnection();
assertTrue(initialConnection.isClosed());
assertEquals(newConnection, dataSource.getConnection());
}

@Test
public void testAddInitializer() {
dataSource.addInitializer(connectionInitializer);
assertEquals(1, dataSource.getInitializerList().size());
}

@Test
public void testClose() throws SQLException {
Connection connection = dataSource.getConnection();
assertFalse(connection.isClosed());

dataSource.close();
assertTrue(connection.isClosed());
}

@Test
public void testTryLock() {
assertFalse(dataSource.tryLock());
}

@Test
public void testTryLockWithTimeOut() throws InterruptedException {
assertTrue(dataSource.tryLock());
assertFalse(dataSource.tryLock());
}

@Test
public void testConnectionTimeout() {
assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(createConnectionWithLock()));
}

private Connection createConnectionWithLock() throws SQLException {
Connection connection = newConnectionFromDriver(dataSource.getUsername(), dataSource.getPassword());
connection.setLock(lock);
return connection;
}

private Connection newConnectionFromDriver(String username, String password) throws SQLException {
// Create a mock Connection object
// ...
return null;
}

private static class CloseIgnoreInvocationHandler implements InvocationHandler {
private final Connection target;
private final Lock lock;

public CloseIgnoreInvocationHandler(Connection target, Lock lock) {
this.target = target;
this.lock = lock;
}

@Override
@SuppressWarnings("all")
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
// Implement the logic to ignore the close method
}
}

private static class ConnectionInitializerImpl implements ConnectionInitializer {
@Override
public void init(Connection connection) throws Exception {
// Implement the logic to initialize the connection
}
}
}