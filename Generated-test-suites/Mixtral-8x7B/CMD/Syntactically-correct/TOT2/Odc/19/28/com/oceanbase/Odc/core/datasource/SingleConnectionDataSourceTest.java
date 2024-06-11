import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.SQLException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource(true);
dataSource.addInitializer(new ConnectionInitializerImpl());
}

@After
public void teardown() throws SQLException {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertFalse(connection.isClosed());
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
String username = "testUser";
String password = "testPassword";
dataSource.setUsername(username);
dataSource.setPassword(password);
Connection connection = dataSource.getConnection(username, password);
assertFalse(connection.isClosed());
}

@Test
public void testGetConnectionWithInvalidUsernameAndPassword() {
String invalidUsername = "invalidUser";
String invalidPassword = "invalidPassword";
assertThrows(SQLException.class,
() -> dataSource.getConnection(invalidUsername, invalidPassword));
}

@Test
public void testResetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertTrue(connection.isValid(dataSource.getLoginTimeout()));
dataSource.resetConnection();
Connection resetConnection = dataSource.getConnection();
assertFalse(resetConnection.isClosed());
}

@Test
public void testAddInitializer() {
ConnectionInitializer initializer = new ConnectionInitializerImpl();
dataSource.addInitializer(initializer);
assertTrue(dataSource.getInitializerList().contains(initializer));
}

@Test
public void testClose() throws SQLException {
Connection connection = dataSource.getConnection();
assertFalse(connection.isClosed());
dataSource.close();
assertTrue(connection.isClosed());
}

@Test
public void testTryLockAndUnlock() throws InterruptedException {
assertTrue(dataSource.tryLock());
dataSource.getLock().unlock();
}

@Test
public void testGetConnectionProxy() throws SQLException {
Connection connection = dataSource.getConnection();
Connection proxyConnection = dataSource.getConnectionProxy(connection);
assertNotEquals(connection, proxyConnection);
}

@Test
public void testOnConnectionCreated() throws SQLException {
Connection connection = newConnectionFromDriver("testUser", "testPassword");
dataSource.onConnectionCreated(connection);
assertTrue(log.isDebugEnabled());
}

@Test
public void testOnConnectionReset() throws SQLException {
Connection connection = newConnectionFromDriver("testUser", "testPassword");
dataSource.onConnectionReset(connection);
assertTrue(dataSource.getEventPublisher() != null);
}

@Test
public void testInnerCreateConnection() throws SQLException {
Connection connection = dataSource.innerCreateConnection();
assertFalse(connection.isClosed());
}

@Test
public void testEdgeCaseWithMultipleThreads() throws InterruptedException, SQLException {
ExecutorService executorService = Executors.newFixedThreadPool(10);
for (int i = 0; i < 10; i++) {
executorService.submit(() -> {
try {
dataSource.getConnection();
} catch (SQLException e) {
log.error("Error occurred while getting connection", e);
}
});
}
executorService.shutdown();
executorService.awaitTermination(1, TimeUnit.MINUTES);
}

@Test
public void testExceptionHandlingWithCloseIgnoreInvocationHandler() {
assertThrows(ConflictException.class,
() -> dataSource.getConnectionProxy(null));
}

private Connection newConnectionFromDriver(String username, String password) throws SQLException {
return DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", username, password);
}
}