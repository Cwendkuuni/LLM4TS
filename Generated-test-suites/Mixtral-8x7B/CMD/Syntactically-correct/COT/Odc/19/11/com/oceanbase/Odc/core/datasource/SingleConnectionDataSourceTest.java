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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private ConnectionInitializer connectionInitializer;
private EventPublisher eventPublisher;
private Connection connection;
private ExecutorService executorService;

@Before
public void setUp() throws Exception {
dataSource = new SingleConnectionDataSource();
dataSource.setUsername("testUser");
dataSource.setPassword("testPass");
dataSource.setDriverClassName("org.h2.Driver");
dataSource.setUrl("jdbc:h2:mem:test");

connectionInitializer = new ConnectionInitializerImpl();
dataSource.addInitializer(connectionInitializer);

eventPublisher = Mockito.mock(EventPublisher.class);
dataSource.setEventPublisher(eventPublisher);

connection = dataSource.innerCreateConnection();
dataSource.initConnection(connection);

executorService = Executors.newFixedThreadPool(10);
}

@After
public void tearDown() throws Exception {
dataSource.close();
executorService.shutdown();
executorService.awaitTermination(1, TimeUnit.SECONDS);
}

@Test
public void testGetConnection() throws SQLException {
Connection result = dataSource.getConnection();
assertNotNull(result);
assertEquals(connection, result);
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPass"));
Connection result = dataSource.getConnection("testUser", "testPass");
assertNotNull(result);
assertEquals(connection, result);
}

@Test
public void testResetConnection() throws SQLException {
Connection resetConnection = Mockito.spy(Connection.class);
dataSource.setConnection(resetConnection);

dataSource.resetConnection();

verify(resetConnection, times(1)).close();
assertNull(dataSource.getConnection());
}

@Test
public void testAddInitializer() {
dataSource.addInitializer(connectionInitializer);
List<ConnectionInitializer> initializerList = dataSource.getInitializerList();
assertEquals(1, initializerList.size());
assertEquals(connectionInitializer, initializerList.get(0));
}

@Test
public void testGetConnectionProxy() throws SQLException {
Connection proxyConnection = dataSource.getConnectionProxy(connection);
assertNotNull(proxyConnection);
assertNotSame(proxyConnection, connection);
}

@Test
public void testInnerCreateConnection() throws Exception {
Connection newConnection = dataSource.innerCreateConnection();
assertNotNull(newConnection);
assertNotSame(newConnection, connection);
}

@Test
public void testCloseIgnoreInvocationHandler() {
InvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, new ReentrantLock());
Object proxy = Proxy.newProxyInstance(connection.getClass().getClassLoader(),
new Class[] {Connection.class}, handler);

Method equalsMethod = proxy.getClass().getMethod("equals", Object.class);
Method hashCodeMethod = proxy.getClass().getMethod("hashCode");
Method unwrapMethod = proxy.getClass().getMethod("unwrap", Class.class);
Method isWrapperForMethod = proxy.getClass().getMethod("isWrapperFor", Class.class);
Method closeMethod = proxy.getClass().getMethod("close");
Method isClosedMethod = proxy.getClass().getMethod("isClosed");

Object result1 = equalsMethod.invoke(proxy, proxy);
Object result2 = hashCodeMethod.invoke(proxy);
Object result3 = unwrapMethod.invoke(proxy, connection.getClass());
Object result4 = isWrapperForMethod.invoke(proxy, connection.getClass());
Object result5 = closeMethod.invoke(proxy);
Object result6 = isClosedMethod.invoke(proxy);

assertEquals(true, result1);
assertNotNull(result2);
assertEquals(connection, result3);
assertEquals(true, result4);
assertNull(result5);
assertEquals(false, result6);
}

@Test
public void testTryLock() throws InterruptedException {
ReentrantLock lock = new ReentrantLock();
assertFalse(lock.tryLock(1, TimeUnit.MILLISECONDS));
Thread.sleep(10);
assertTrue(lock.tryLock());
}

@Test
public void testPrepareConnection() throws SQLException {
Connection preparedConnection = Mockito.spy(connection);
dataSource.prepareConnection(preparedConnection);
verify(preparedConnection, times(1)).setAutoCommit(dataSource.getAutoCommit());
}

@Test
public void testOnConnectionCreated() {
dataSource.onConnectionCreated(connection);
verifyNoMoreInteractions(eventPublisher);
}

@Test
public void testOnConnectionReset() throws Exception {
ConnectionResetEvent event = Mockito.mock(ConnectionResetEvent.class);
when(eventPublisher.publishEvent(event)).thenReturn(null);
dataSource.onConnectionReset(connection);
verify(eventPublisher, times(1)).publishEvent(event);
}

@Test
public void testMultipleGetConnectionConcurrently() throws Exception {
List<Connection> connections = new LinkedList<>();
for (int i = 0; i < 100; i++) {
connections.add(dataSource.getConnection());
}

for (Connection connection : connections) {
dataSource.returnConnection(connection);
}
}

@Test
public void testGetConnectionProxyWithLock() throws SQLException {
Connection proxyConnection1 = dataSource.getConnectionProxy(connection);
Connection proxyConnection2 = dataSource.getConnectionProxy(connection);

assertSame(proxyConnection1, proxyConnection2);
}

@Test
public void testGetConnectionProxyWithLockAndInterruptedException() {
dataSource.setTimeOutMillis(1);
assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));
}

@Test
public void testGetConnectionWithNullUsernameAndPassword() throws SQLException {
Connection result = dataSource.getConnection(null, null);
assertNotNull(result);
assertEquals(connection, result);
}

@Test
public void testGetConnectionWithEmptyUsernameAndPassword() throws SQLException {
Connection result = dataSource.getConnection("", "");
assertNotNull(result);
assertEquals(connection, result);
}
}