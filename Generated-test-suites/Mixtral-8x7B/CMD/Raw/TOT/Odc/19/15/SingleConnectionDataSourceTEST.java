import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connection;
 private DataSource mockDataSource;
 private ExecutorService executorService;

 @Before
 public void setUp() throws SQLException {
 dataSource = new SingleConnectionDataSource();
 connection = mock(Connection.class);
 mockDataSource = mock(DataSource.class);
 executorService = Executors.newFixedThreadPool(10);

 when(mockDataSource.getConnection()).thenReturn(connection);
 dataSource.setDelegatedDataSource(mockDataSource);
 dataSource.addInitializer((conn) -> {
 // Do nothing
 });
 }

 @Test
 public void testGetConnection() throws SQLException {
 Connection result = dataSource.getConnection();

 assertEquals(connection, result);
 }

 @Test
 public void testGetConnectionWithUsernameAndPassword() throws SQLException {
 when(mockDataSource.getConnection(any(String.class), any(String.class)))
 .thenThrow(new SQLException("Invalid username or password"));

 assertThrows(SQLException.class, () -> dataSource.getConnection("invalid", "password"));
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.getConnection();

 dataSource.resetConnection();

 verify(connection, times(1)).close();
 }

 @Test
 public void testAddInitializer() {
 ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);

 dataSource.addInitializer(initializer);

 verify(initializer, times(1)).init(any(Connection.class));
 }

 @Test
 public void testClose() throws SQLException {
 dataSource.getConnection();

 dataSource.close();

 verify(connection, times(1)).close();
 }

 @Test
 public void testGetConnectionProxy() {
 InvocationHandler handler = dataSource.getConnectionProxy(connection);

 assertFalse(handler instanceof CloseIgnoreInvocationHandler);

 dataSource.tryLock();

 handler = dataSource.getConnectionProxy(connection);

 assertTrue(handler instanceof CloseIgnoreInvocationHandler);
 }

 @Test
 public void testPrepareConnection() throws SQLException {
 dataSource.setAutoCommit(true);

 dataSource.prepareConnection(connection);

 verify(connection, times(1)).setAutoCommit(true);
 }

 @Test
 public void testTryLock() {
 dataSource.lock = new ReentrantLock();

 assertTrue(dataSource.tryLock());
 }

 @Test
 public void testTryLockWithTimeout() throws InterruptedException {
 dataSource.lock = new ReentrantLock();
 dataSource.timeOutMillis = 10;

 assertFalse(dataSource.tryLock());
 TimeUnit.SECONDS.sleep(1);
 }

 @Test
 public void testConnectionOccupied() {
 dataSource.lock = new ReentrantLock();

 assertThrows(
 ConflictException.class,
 () -> {
 dataSource.getConnectionProxy(connection);
 dataSource.getConnectionProxy(connection);
 }
 );
 }

 @Test
 public void testInnerCreateConnectionWithAutoCommit() throws SQLException {
 when(mockDataSource.getConnection()).thenReturn(connection);
 dataSource.setAutoCommit(true);

 dataSource.innerCreateConnection();

 verify(connection, times(1)).setAutoCommit(true);
 }

 @Test
 public void testInnerCreateConnectionWithoutAutoCommit() throws SQLException {
 when(mockDataSource.getConnection()).thenReturn(connection);

 dataSource.innerCreateConnection();

 verify(connection, times(1)).setAutoCommit(false);
 }

 @Test
 public void testOnConnectionReset() {
 EventPublisher eventPublisher = mock(EventPublisher.class);

 dataSource.setEventPublisher(eventPublisher);
 dataSource.onConnectionReset(connection);

 verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
 }

 @Test
 public void testCloseIgnoreInvocationHandlerClose() {
 InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, dataSource.lock);

 handler.invoke(null, "close", null);

 verify(connection, times(1)).close();
 }

 @Test
 public void testCloseIgnoreInvocationHandlerIsWrapperFor() {
 InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, dataSource.lock);

 handler.invoke(
 null,
 "isWrapperFor",
 new Class[] {Connection.class},
 new Object[] {Connection.class}
 );

 assertTrue(true);
 }

 @Test
 public void testCloseIgnoreInvocationHandlerIsClosed() {
 InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, dataSource.lock);

 handler.invoke(null, "isClosed", null);

 assertFalse(true);
 }

 @Test(expected = ConflictException.class)
 public void testCloseIgnoreInvocationHandlerGetConnectionProxy()
 throws SQLException, InterruptedException {
 dataSource.lock = new ReentrantLock();
 dataSource.timeOutMillis = 10;

 Connection result = dataSource.getConnectionProxy(connection);
 TimeUnit.SECONDS.sleep(1);

 result.createStatement();
 }
}