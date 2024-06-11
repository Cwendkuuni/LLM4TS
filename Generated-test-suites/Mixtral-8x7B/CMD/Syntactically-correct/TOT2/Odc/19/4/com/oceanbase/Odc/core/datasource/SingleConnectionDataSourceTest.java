import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private ConnectionInitializer connectionInitializer;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource();
connectionInitializer = new ConnectionInitializerImpl();
dataSource.addInitializer(connectionInitializer);
}

@After
public void teardown() throws SQLException {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
assertEquals(connectionInitializer.getConnection(), connection);
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
String username = "testUser";
String password = "testPassword";
dataSource.setUsername(username);
dataSource.setPassword(password);
Connection connection = dataSource.getConnection(username, password);
assertNotNull(connection);
assertEquals(connectionInitializer.getConnection(), connection);
}

@Test
public void testGetConnectionWithInvalidUsernameAndPassword() {
String username = "invalidUser";
String password = "invalidPassword";
Exception exception = assertThrows(SQLException.class, () -> {
dataSource.getConnection(username, password);
});
String expectedMessage = "Invalid username or password";
assertEquals(expectedMessage, exception.getMessage());
}

@Test
public void testResetConnection() throws SQLException {
Connection initialConnection = dataSource.getConnection();
dataSource.resetConnection();
Connection newConnection = dataSource.getConnection();
assertFalse(initialConnection.isClosed());
assertTrue(newConnection.isValid(dataSource.getLoginTimeout()));
}

@Test
public void testClose() throws SQLException {
Connection connection = dataSource.getConnection();
dataSource.close();
assertThrows(SQLException.class, () -> {
connection.createStatement();
});
}

@Test
public void testAutoCommit() throws SQLException {
dataSource.setAutoCommit(true);
Connection connection = dataSource.getConnection();
assertTrue(connection.getAutoCommit());

dataSource.setAutoCommit(false);
connection = dataSource.getConnection();
assertFalse(connection.getAutoCommit());
}

@Test
public void testEventPublisher() {
EventPublisher eventPublisher = new EventPublisher();
dataSource.setEventPublisher(eventPublisher);
dataSource.getConnection();
assertTrue(eventPublisher.getEvents().size() > 0);
}

@Test
public void testLock() throws InterruptedException {
ReentrantLock lock = new ReentrantLock();
dataSource.setLock(lock);
Connection connection = dataSource.getConnection();
CloseIgnoreInvocationHandler invocationHandler = (CloseIgnoreInvocationHandler) Proxy.getInvocationHandler(connection);
invocationHandler.invoke(connection, lock.getClass().getMethod("unlock"));

// Test if the lock is unlocked
assertTrue(lock.isHeldByCurrentThread());

// Test timeout
assertFalse(dataSource.getConnectionProxy(connection).isValid(0));
}
}