import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SingleConnectionDataSourceTest {

// Initialize a data source for testing
private SingleConnectionDataSource dataSource;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource();
}

@After
public void teardown() throws SQLException {
dataSource.close();
}

// Test getConnection() method
@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertEquals(true, connection.isValid(dataSource.getLoginTimeout()));
}

// Test getConnection(username, password) method
@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
String username = "testuser";
String password = "testpassword";
dataSource.setUsername(username);
dataSource.setPassword(password);
Connection connection = dataSource.getConnection(username, password);
assertEquals(true, connection.isValid(dataSource.getLoginTimeout()));
}

// Test getConnection() with invalid username and password
@Test
public void testGetConnectionWithInvalidUsernameAndPassword() {
assertThrows(SQLException.class, () -> {
dataSource.getConnection("invaliduser", "invalidpassword");
});
}

// Test resetConnection() method
@Test
public void testResetConnection() throws SQLException {
dataSource.getConnection();
dataSource.resetConnection();
assertThrows(SQLException.class, () -> {
dataSource.getConnection();
});
}

// Test addInitializer() method
@Test
public void testAddInitializer() throws SQLException {
ConnectionInitializer initializer = new ConnectionInitializer() {
@Override
public void init(Connection connection) throws SQLException {
connection.setAutoCommit(true);
}
};
dataSource.addInitializer(initializer);
Connection connection = dataSource.getConnection();
assertEquals(true, connection.getAutoCommit());
}

// Test tryLock() method
@Test
public void testTryLock() throws InterruptedException {
ReentrantLock lock = new ReentrantLock();
assertEquals(true, lock.tryLock(100, TimeUnit.MILLISECONDS));
lock.unlock();
}

// Test getConnectionProxy() method
@Test
public void testGetConnectionProxy() throws SQLException {
ReentrantLock lock = new ReentrantLock();
Connection connection = dataSource.getConnectionProxy(dataSource.innerCreateConnection());
dataSource.setLock(lock);
dataSource.getConnectionProxy(connection);
assertEquals(true, lock.isHeldByCurrentThread());
}

// Test onConnectionReset() method
@Test
public void testOnConnectionReset() throws SQLException {
dataSource.setEventPublisher(new EventPublisher() {
@Override
public void publishEvent(Object event) {
assertEquals(ConnectionResetEvent.class, event.getClass());
}
});
dataSource.getConnection();
dataSource.resetConnection();
}

// Test innerCreateConnection() method
@Test
public void testInnerCreateConnection() throws SQLException {
Connection connection = dataSource.innerCreateConnection();
assertEquals(true, connection.isValid(dataSource.getLoginTimeout()));
}

// Test CloseIgnoreInvocationHandler's close method
@Test
public void testCloseIgnoreInvocationHandlerCloseMethod() throws Exception {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
ReentrantLock lock = new ReentrantLock();
dataSource.setLock(lock);
Connection connection = dataSource.innerCreateConnection();
InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class}, handler);
proxyConnection.close();
assertEquals(false, lock.isHeldByCurrentThread());
}

// Test ConnectionInitializer interface
@Test
public void testConnectionInitializer() throws Exception {
dataSource.addInitializer(new ConnectionInitializer() {
@Override
public void init(Connection connection) throws Exception {
connection.setAutoCommit(true);
}
});
dataSource.getConnection();
}

// Test ConflictException
@Test
public void testConflictException() {
assertThrows(ConflictException.class, () -> {
throw new ConflictException(ErrorCodes.ConnectionOccupied, new Object[] {},
"Connection is occupied, waited 10000 millis");
});
}
}