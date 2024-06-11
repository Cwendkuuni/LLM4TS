import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
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
private DataSource testDataSource;
private Connection connection;

@Before
public void setUp() throws SQLException {
dataSource = new SingleConnectionDataSource();
testDataSource = dataSource;
connection = dataSource.getConnection();
}

@After
public void tearDown() throws SQLException {
if (connection != null) {
connection.close();
}
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertTrue(connection.isValid(0));
}

@Test
public void testGetConnectionWithUsernameAndPassword() {
assertThrows(SQLException.class, () -> dataSource.getConnection("wrong_username", "wrong_password"));
}

@Test
public void testAddInitializer() throws SQLException {
ConnectionInitializerImpl initializer = new ConnectionInitializerImpl();
dataSource.addInitializer(initializer);
dataSource.initConnection(connection);
assertTrue(initializer.isInvoked());
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();
assertTrue(dataSource.getConnection().isClosed());
}

@Test
public void testAutoCommit() throws SQLException {
dataSource.setAutoCommit(false);
Connection connection = dataSource.getConnection();
assertFalse(connection.getAutoCommit());
}

@Test
public void testEventPublisher() throws SQLException, ConflictException {
// Given
EventPublisher publisher = new EventPublisher();
dataSource.setEventPublisher(publisher);

// When
dataSource.resetConnection();

// Then
assertTrue(publisher.getEvents().size() > 0);
}

@Test
public void testClose() throws SQLException {
dataSource.close();
assertThrows(IllegalStateException.class, () -> dataSource.getConnection());
}

@Test
public void testMultipleThreads() throws SQLException, InterruptedException {
ExecutorService executor = Executors.newFixedThreadPool(10);
List<Connection> connections = new LinkedList<>();

for (int i = 0; i < 10; i++) {
connections.add(dataSource.getConnection());
}

executor.submit(() -> {
for (Connection connection : connections) {
try {
connection.close();
} catch (SQLException e) {
// Ignore
}
}
});

executor.shutdown();
executor.awaitTermination(5, TimeUnit.SECONDS);

for (Connection connection : connections) {
assertTrue(connection.isClosed());
}
}

// Additional test cases for exception handling scenarios

@Test
public void testGetConnectionWithNullDataSource() {
assertThrows(IllegalArgumentException.class, () -> Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class},
new CloseIgnoreInvocationHandler(null, new ReentrantLock())));
}

@Test
public void testCloseIgnoreInvocationHandlerWithNullConnection() {
assertThrows(NullPointerException.class, () -> new CloseIgnoreInvocationHandler(null, new ReentrantLock()));
}

@Test
public void testInnerCreateConnectionWithNullUsername() {
assertThrows(NullPointerException.class, () -> dataSource.innerCreateConnection());
}

@Test
public void testInnerCreateConnectionWithNullPassword() {
assertThrows(NullPointerException.class, () -> dataSource.innerCreateConnection("username", null));
}

@Test
public void testOnConnectionResetWithNullConnection() {
assertThrows(NullPointerException.class, () -> dataSource.onConnectionReset(null));
}

@Test
public void testNewConnectionFromDriverWithNullDriver() {
assertThrows(NullPointerException.class, () -> DriverManager.getConnection("jdbc:mysql://localhost:3306/test", null, null));
}

@Test
public void testNewConnectionFromDriverWithNullUrl() {
assertThrows(NullPointerException.class, () -> DriverManager.getConnection(null, "username", "password"));
}

@Test
public void testNewConnectionFromDriverWithNullUsername() {
assertThrows(NullPointerException.class, () -> DriverManager.getConnection("jdbc:mysql://localhost:3306/test", null, "password"));
}

@Test
public void testNewConnectionFromDriverWithNullPassword() {
assertThrows(NullPointerException.class, () -> DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "username", null));
}

@Test
public void testGetConnectionWithNullDataSourceAndUsernamePassword() {
assertThrows(IllegalArgumentException.class, () -> dataSource.getConnection(null, null));
}

@Test
public void testGetConnectionWithEmptyUsernamePassword() {
assertThrows(SQLException.class, () -> dataSource.getConnection("", ""));
}

@Test
public void testPrepareConnectionWithNullConnection() {
assertThrows(NullPointerException.class, () -> dataSource.prepareConnection(null));
}

@Test
public void testTryLockWithNegativeTimeOutMillis() {
assertThrows(IllegalArgumentException.class, () -> dataSource.tryLock(-1));
}

@Test
public void testGetConnectionProxyWithNullConnection() {
assertThrows(NullPointerException.class, () -> dataSource.getConnectionProxy(null));
}

@Test
public void testOnConnectionCreatedWithNullConnection() {
assertThrows(NullPointerException.class, () -> dataSource.onConnectionCreated(null));
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithLockInterruptedException() throws Throwable, ConflictException {
InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, new ReentrantLock());
Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class},
handler);

dataSource.getConnectionProxy(proxyConnection);
}

}