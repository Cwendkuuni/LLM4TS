import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private Connection connection;
private ExecutorService executorService;

@Before
public void setup() throws SQLException {
dataSource = new SingleConnectionDataSource();
connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
dataSource.setConnection(connection);
dataSource.addInitializer(new ConnectionInitializer() {
@Override
public void init(Connection connection) throws SQLException {
// Do nothing
}
});
executorService = Executors.newFixedThreadPool(10);
}

@After
public void teardown() throws SQLException {
dataSource.close();
executorService.shutdown();
try {
executorService.awaitTermination(1, TimeUnit.SECONDS);
} catch (InterruptedException e) {
e.printStackTrace();
}
connection.close();
}

@Test
public void testGetConnection_ValidConnection() throws SQLException {
Connection result = dataSource.getConnection();
assertTrue(result.isValid(5));
}

@Test
public void testGetConnection_WithUsernameAndPassword_ThrowsException() {
assertThrows(SQLException.class, () -> dataSource.getConnection("invalid", "credentials"));
}

@Test
public void testGetConnection_NullConnection_CreatesNewConnection() throws SQLException {
dataSource.setConnection(null);
Connection result = dataSource.getConnection();
assertTrue(result.isValid(5));
}

@Test
public void testClose_ClosesConnection() throws SQLException {
dataSource.close();
assertEquals(connection.getState(), Connection.TRANSIENT);
}

@Test
public void testResetConnection_ResetsConnection() throws SQLException {
dataSource.resetConnection();
assertTrue(dataSource.getConnection().isClosed());
}

@Test
public void testResetConnection_WithEventPublisher_PublishesEvent() {
EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
dataSource.setEventPublisher(eventPublisher);
dataSource.resetConnection();
Mockito.verify(eventPublisher).publishEvent(Mockito.any(ConnectionResetEvent.class));
}

@Test
public void testAddInitializer_InitializerIsCalled() throws SQLException {
ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
dataSource.addInitializer(initializer);
dataSource.getConnection();
Mockito.verify(initializer).init(connection);
}

@Test
public void testLock_AcquiresLock() {
InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, dataSource.getLock());
Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class}, handler);
assertFalse(proxyConnection.isClosed());
}

@Test
public void testLock_TimeOut_ThrowsException() {
InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, dataSource.getLock());
Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class}, handler);
assertThrows(ConflictException.class, () -> getConnectionWithLock(proxyConnection, 1));
}

private Connection getConnectionWithLock(Connection connection, long timeOutMillis) {
try {
return dataSource.getConnectionProxy(connection);
} catch (ConflictException e) {
throw e;
} catch (Exception e) {
throw new RuntimeException(e);
}
}

@Test
public void testConnectionProxy_CloseMethod_DoesNothing() throws SQLException {
InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, dataSource.getLock());
Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class}, handler);
proxyConnection.close();
assertFalse(proxyConnection.isClosed());
}

@Test
public void testAutoCommit_SetToTrue_ConnectionIsAutoCommit() throws SQLException {
dataSource.setAutoCommit(true);
Connection connection = dataSource.getConnection();
assertTrue(connection.getAutoCommit());
}

@Test
public void testAutoCommit_SetToFalse_ConnectionIsNotAutoCommit() throws SQLException {
dataSource.setAutoCommit(false);
Connection connection = dataSource.getConnection();
assertFalse(connection.getAutoCommit());
}

@Test
public void testAutoCommit_Changed_ProxyConnectionReflectsChangesInTargetConnection() throws SQLException {
dataSource.setAutoCommit(true);
Connection connection = dataSource.getConnection();
Connection proxyConnection = dataSource.getConnectionProxy(connection);
assertFalse(proxyConnection.getAutoCommit());
connection.setAutoCommit(true);
assertTrue(proxyConnection.getAutoCommit());
}

@Test
public void testAutoReconnect_SetToTrue_ReconnectsOnClosedConnection() throws SQLException {
dataSource.setAutoReconnect(true);
Connection connection = dataSource.getConnection();
connection.close();
assertTrue(dataSource.getConnection().isValid(5));
}

@Test
public void testAutoReconnect_SetToFalse_DoesNotReconnectOnClosedConnection() throws SQLException {
dataSource.setAutoReconnect(false);
Connection connection = dataSource.getConnection();
connection.close();
assertTrue(connection.isClosed());
}

@Test
public void testPrepareConnection_SetsAutoCommit() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
dataSource.prepareConnection(connection);
assertTrue(connection.getAutoCommit());
}

@Test
public void testPrepareConnection_DoesNotChangeAutoCommitWhenNull() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
dataSource.setAutoCommit(null);
dataSource.prepareConnection(connection);
assertTrue(connection.getAutoCommit());
}

@Test
public void testInnerCreateConnection_CreatesConnection() throws SQLException {
dataSource.innerCreateConnection();
assertTrue(dataSource.getConnection().isValid(5));
}

@Test
public void testInnerCreateConnection_ThrowsExceptionOnIllegalState() {
assertThrows(IllegalStateException.class, () -> dataSource.innerCreateConnection());
}

@Test
public void testCreateConnectionProxy_AcquiresLock() throws SQLException {
InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, dataSource.getLock());
Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class}, handler);
dataSource.getConnectionProxy(proxyConnection);
assertTrue(dataSource.getLock().isHeldByCurrentThread());
}

@Test
public void testCreateConnectionProxy_ReleasesLock() throws SQLException {
InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, dataSource.getLock());
Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class}, handler);
dataSource.getConnectionProxy(proxyConnection);
dataSource.getConnectionProxy(proxyConnection);
assertFalse(dataSource.getLock().isHeldByCurrentThread());
}

@Test
public void testOnConnectionReset_PublishesEvent() throws SQLException {
EventPublisher eventPublisher = Mockito.mock(EventPublisher.class);
dataSource.setEventPublisher(eventPublisher);
dataSource.resetConnection();
Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any(ConnectionResetEvent.class));
}

@Test
public void testOnConnectionReset_DoesNotPublishEventOnNullEventPublisher() throws SQLException {
dataSource.setEventPublisher(null);
dataSource.resetConnection();
// No exception should be thrown
}

@Test
public void testOnConnectionCreated_LogsMessage() throws SQLException {
dataSource.onConnectionCreated(connection);
assertTrue(log.isDebugEnabled());
}

@Test
public void testNewConnectionFromDriver_CreatesConnection() throws SQLException {
String url = "jdbc:h2:mem:test";
String username = "sa";
String password = "";
Connection result = dataSource.newConnectionFromDriver(username, password, url);
assertTrue(result.isValid(5));
}

private class TestConnectionInitializer implements ConnectionInitializer {
@Override
public void init(Connection connection) throws SQLException {
// Do nothing
}
}

}