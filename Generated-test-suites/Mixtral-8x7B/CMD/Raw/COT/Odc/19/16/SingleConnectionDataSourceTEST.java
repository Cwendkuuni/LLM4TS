import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private ConnectionInitializer connectionInitializer;
private DataSource mockDataSource;
private EventPublisher mockEventPublisher;
private ExecutorService executorService;

@Before
public void setUp() throws Exception {
dataSource = new SingleConnectionDataSource();
connectionInitializer = new ConnectionInitializerImpl();
dataSource.addInitializer(connectionInitializer);
mockDataSource = mock(DataSource.class);
mockEventPublisher = mock(EventPublisher.class);
dataSource.setEventPublisher(mockEventPublisher);
executorService = Executors.newFixedThreadPool(10);
}

@After
public void tearDown() throws Exception {
dataSource.close();
executorService.shutdown();
try {
executorService.awaitTermination(1, TimeUnit.SECONDS);
} catch (InterruptedException e) {
Thread.currentThread().interrupt();
}
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
assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPassword"));
}

@Test
public void testResetConnection() throws SQLException {
dataSource.getConnection();
dataSource.resetConnection();
assertTrue(dataSource.getConnection().isClosed());
}

@Test
public void testAddInitializer() {
List<ConnectionInitializer> initializersBefore = dataSource.getInitializerList();
ConnectionInitializer initializerToAdd = Mockito.mock(ConnectionInitializer.class);
dataSource.addInitializer(initializerToAdd);
List<ConnectionInitializer> initializersAfter = dataSource.getInitializerList();
assertTrue(initializersAfter.contains(initializerToAdd));
assertTrue(initializersAfter.size() > initializersBefore.size());
}

@Test
public void testClose() throws SQLException {
Connection connection = dataSource.getConnection();
dataSource.close();
assertTrue(connection.isClosed());
}

@Test
public void testPrepareConnection() throws SQLException {
Boolean autoCommit = true;
dataSource.setAutoCommit(autoCommit);
Connection connection = Mockito.mock(Connection.class);
when(connection.getAutoCommit()).thenReturn(false);
dataSource.prepareConnection(connection);
verify(connection, times(1)).setAutoCommit(autoCommit);
}

@Test
public void testTryLock() throws InterruptedException {
assertTrue(dataSource.tryLock());
assertFalse(dataSource.tryLock());
}

@Test
public void testGetConnectionProxy() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
when(connection.isClosed()).thenReturn(true);
InvocationHandler handler = dataSource.getConnectionProxy(connection);
Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class}, handler);
assertTrue(proxyConnection.isClosed());
}

@Test
public void testInitConnection() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
dataSource.initConnection(connection);
verify(connection, times(1)).createStatement();
}

@Test
public void testOnConnectionCreated() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
dataSource.onConnectionCreated(connection);
verify(connection, times(1)).createStatement();
}

@Test
public void testOnConnectionReset() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
dataSource.onConnectionReset(connection);
verify(mockEventPublisher, times(1)).publishEvent(Mockito.any(ConnectionResetEvent.class));
}

@Test
public void testInnerCreateConnection() throws SQLException {
Connection connection = dataSource.innerCreateConnection();
assertFalse(connection.isClosed());
}

@Test
public void testConstructorWithAutoReconnect() {
SingleConnectionDataSource dataSourceWithAutoReconnect = new SingleConnectionDataSource(true);
assertTrue(dataSourceWithAutoReconnect.isAutoReconnect());
}

@Test
public void testConstructorWithoutAutoReconnect() {
SingleConnectionDataSource dataSourceWithoutAutoReconnect = new SingleConnectionDataSource();
assertFalse(dataSourceWithoutAutoReconnect.isAutoReconnect());
}

@Test
public void testGetConnectionWithProxy() throws SQLException {
Connection connection = dataSource.getConnection();
InvocationHandler handler = Proxy.getInvocationHandler(connection);
assertTrue(handler instanceof CloseIgnoreInvocationHandler);
CloseIgnoreInvocationHandler closeIgnoreHandler = (CloseIgnoreInvocationHandler) handler;
assertEquals(dataSource.getConnection(), closeIgnoreHandler.target);
assertEquals(dataSource.getLock(), closeIgnoreHandler.lock);
}

@Test
public void testMultipleThreadsAccessingConnection() throws InterruptedException, SQLException {
List<Connection> connections = new LinkedList<>();
for (int i = 0; i < 10; i++) {
executorService.submit(() -> {
try {
connections.add(dataSource.getConnection());
} catch (SQLException e) {
e.printStackTrace();
}
});
}
executorService.shutdown();
executorService.awaitTermination(3, TimeUnit.SECONDS);
assertEquals(1, connections.size());
}

@Test
public void testLockInterruption() throws InterruptedException {
Thread lockThread = new Thread(() -> dataSource.tryLock());
lockThread.start();
Thread.sleep(100);
lockThread.interrupt();
lockThread.join();
assertFalse(lockThread.isInterrupted());
assertFalse(dataSource.tryLock());
}

@Test
public void testConflictExceptionWithConnectionOccupied() {
assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(Mockito.mock(Connection.class)));
}

@Test
public void testConflictExceptionWithConnectionOccupiedMessage() {
String expectedMessage = "Connection is occupied, waited 10 millis";
assertThrows(ConflictException.class, expectedMessage, () -> dataSource.getConnectionProxy(Mockito.mock(Connection.class)));
}

@Test
public void testGetUsername() {
String expectedUsername = "testUser";
dataSource.setUsername(expectedUsername);
assertEquals(expectedUsername, dataSource.getUsername());
}

@Test
public void testGetPassword() {
String expectedPassword = "testPassword";
dataSource.setPassword(expectedPassword);
assertEquals(expectedPassword, dataSource.getPassword());
}

@Test
public void testGetTimeOutMillis() {
long expectedTimeOutMillis = 5000;
dataSource.setTimeOutMillis(expectedTimeOutMillis);
assertEquals(expectedTimeOutMillis, dataSource.getTimeOutMillis());
}
}