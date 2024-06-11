import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connection;
 private EventPublisher eventPublisher;
 private ExecutorService executorService;

 @Before
 public void setup() throws SQLException {
 dataSource = new SingleConnectionDataSource();
 connection = DriverManager.getConnection("jdbc:h2:mem:test", "user", "password");
 eventPublisher = mock(EventPublisher.class);
 dataSource.setEventPublisher(eventPublisher);
 executorService = Executors.newFixedThreadPool(10);
 }

 @After
 public void teardown() throws SQLException {
 if (dataSource != null) {
 dataSource.close();
 }
 if (connection != null) {
 connection.close();
 }
 if (executorService != null) {
 executorService.shutdownNow();
 try {
 executorService.awaitTermination(5, TimeUnit.SECONDS);
 } catch (InterruptedException e) {
 Thread.currentThread().interrupt();
 }
 }
 }

 @Test
 public void testGetConnection() throws SQLException {
 dataSource.setConnection(connection);
 assertNotNull(dataSource.getConnection());
 }

 @Test
 public void testGetConnectionWithAutoReconnect() throws SQLException {
 dataSource = new SingleConnectionDataSource(true);
 dataSource.setConnection(connection);
 connection.close();
 assertNotNull(dataSource.getConnection());
 }

 @Test
 public void testGetConnectionWithInvalidUsernameAndPassword() {
 assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPassword"));
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.setConnection(connection);
 dataSource.resetConnection();
 verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
 }

 @Test
 public void testAddInitializer() {
 InvocationHandler handler = mock(InvocationHandler.class);
 ConnectionInitializer initializer = (ConnectionInitializer) Proxy.newProxyInstance(
 ConnectionInitializer.class.getClassLoader(),
 new Class[] {ConnectionInitializer.class},
 handler);
 dataSource.addInitializer(initializer);
 verify(handler, times(1)).invoke(eq(initializer), any(Connection.class));
 }

 @Test
 public void testGetConnectionProxy() {
 assertNotNull(dataSource.getConnectionProxy(connection));
 }

 @Test
 public void testConnectionProxyClose() throws SQLException {
 dataSource.setConnection(connection);
 dataSource.getConnectionProxy(connection).close();
 assertFalse(connection.isClosed());
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 when(connection.isValid(any(int.class))).thenReturn(false);
 dataSource.setConnection(connection);
 dataSource.innerCreateConnection();
 verify(connection, times(1)).close();
 }

 @Test
 public void testInitConnection() throws SQLException {
 InvocationHandler handler = mock(InvocationHandler.class);
 ConnectionInitializer initializer = (ConnectionInitializer) Proxy.newProxyInstance(
 ConnectionInitializer.class.getClassLoader(),
 new Class[] {ConnectionInitializer.class},
 handler);
 dataSource.addInitializer(initializer);
 connection.setAutoCommit(false);
 dataSource.initConnection(connection);
 verify(handler, times(1)).init(eq(connection));
 }

 @Test
 public void testOnConnectionReset() throws SQLException {
 dataSource.setConnection(connection);
 dataSource.onConnectionReset(connection);
 verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
 }

 @Test
 public void testClose() throws SQLException {
 dataSource.setConnection(connection);
 dataSource.close();
 verify(connection, times(1)).close();
 }

 @Test
 public void testPrepareConnection() throws SQLException {
 Boolean autoCommit = true;
 dataSource.setAutoCommit(autoCommit);
 dataSource.prepareConnection(connection);
 assertTrue(connection.getAutoCommit() == autoCommit);
 }

 @Test
 public void testPrepareConnectionWithNullAutoCommit() throws SQLException {
 dataSource.prepareConnection(connection);
 assertTrue(connection.getAutoCommit());
 }

 @Test
 public void testTryLock() throws InterruptedException {
 dataSource.setTimeOutMillis(1000);
 assertTrue(dataSource.tryLock());
 dataSource.setTimeOutMillis(0);
 assertFalse(dataSource.tryLock());
 }

 @Test
 public void testGetConnectionWithLock() throws SQLException, InterruptedException {
 dataSource.setTimeOutMillis(1000);
 dataSource.getConnectionProxy(connection);
 dataSource.setTimeOutMillis(0);
 assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));
 }

 @Test
 public void testGetConnectionWithLockInMultipleThreads()
 throws SQLException, InterruptedException {
 dataSource.setTimeOutMillis(1000);
 Runnable task = () -> {
 try {
 dataSource.getConnectionProxy(connection);
 } catch (ConflictException ignored) {
 }
 };
 executorService.submit(task);
 executorService.submit(task);
 dataSource.setTimeOutMillis(0);
 executorService.submit(task);
 executorService.shutdown();
 }

 @Test
 public void testGetConnectionWithLockEdgeCase() throws SQLException, InterruptedException {
 dataSource.setTimeOutMillis(0);
 assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));
 }

 @Test
 public void testGetConnectionWithLockError() throws SQLException, InterruptedException {
 dataSource.setTimeOutMillis(1000);
 dataSource.getConnectionProxy(connection);
 dataSource.setConnection(null);
 assertThrows(SQLException.class, () -> dataSource.getConnectionProxy(connection));
 }
}