import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private ConnectionInitializer connectionInitializer;
 private DataSource mockDataSource;
 private ExecutorService executorService;

 @Before
 public void setUp() throws Exception {
 connectionInitializer = new ConnectionInitializerImpl();
 mockDataSource = mock(DataSource.class);
 dataSource = new SingleConnectionDataSource();
 dataSource.setDataSource(mockDataSource);
 dataSource.addInitializer(connectionInitializer);
 executorService = Executors.newFixedThreadPool(10);
 }

 @After
 public void tearDown() throws Exception {
 if (dataSource != null) {
 dataSource.close();
 }
 if (executorService != null) {
 executorService.shutdown();
 }
 }

 @Test
 public void testGetConnection() throws SQLException {
 when(mockDataSource.getConnection()).thenReturn(mock(Connection.class));
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 verify(mockDataSource, times(1)).getConnection();
 }

 @Test
 public void testGetConnectionWithUsernameAndPassword() throws SQLException {
 when(mockDataSource.getConnection(Mockito.anyString(), Mockito.anyString()))
 .thenThrow(new SQLException("Invalid username or password"));
 assertThrows(SQLException.class, () -> dataSource.getConnection("testUser", "testPassword"));
 }

 @Test
 public void testResetConnection() throws SQLException {
 when(mockDataSource.getConnection()).thenReturn(mock(Connection.class));
 dataSource.getConnection();
 dataSource.resetConnection();
 verify(mockDataSource, times(2)).getConnection();
 }

 @Test
 public void testAddInitializer() {
 dataSource.addInitializer(connectionInitializer);
 assertEquals(1, dataSource.getInitializerList().size());
 }

 @Test
 public void testClose() throws SQLException {
 when(mockDataSource.getConnection()).thenReturn(mock(Connection.class));
 dataSource.getConnection();
 dataSource.close();
 verify(mockDataSource, times(1)).getConnection();
 }

 @Test
 public void testGetConnectionProxy() throws SQLException {
 when(mockDataSource.getConnection()).thenReturn(mock(Connection.class));
 Connection connection = dataSource.getConnectionProxy(dataSource.getConnection());
 assertNotNull(connection);
 }

 @Test
 public void testPrepareConnection() throws SQLException {
 dataSource.setAutoCommit(true);
 Connection connection = mock(Connection.class);
 dataSource.prepareConnection(connection);
 assertTrue(connection.getAutoCommit());
 }

 @Test
 public void testTryLock() throws InterruptedException {
 dataSource.lock = new ReentrantLock();
 assertTrue(dataSource.tryLock());
 }

 @Test
 public void testTryLockInterruptedException() throws InterruptedException {
 dataSource.lock = new ReentrantLock();
 Thread.currentThread().interrupt();
 assertFalse(dataSource.tryLock());
 }

 @Test
 public void testOnConnectionReset() throws SQLException, ExecutionException, InterruptedException {
 EventPublisher eventPublisher = mock(EventPublisher.class);
 dataSource.setEventPublisher(eventPublisher);
 when(mockDataSource.getConnection()).thenReturn(mock(Connection.class));
 Future<?> future = executorService.submit(() -> {
 try {
 dataSource.getConnection();
 } catch (SQLException e) {
 // Ignore
 }
 });
 future.get();
 verify(eventPublisher, times(1)).publishEvent(Mockito.any());
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 when(mockDataSource.getConnection()).thenReturn(mock(Connection.class));
 dataSource.innerCreateConnection();
 verify(mockDataSource, times(1)).getConnection();
 }

 @Test
 public void testInnerCreateConnectionInterruptedException()
 throws SQLException, ExecutionException, InterruptedException {
 when(mockDataSource.getConnection()).thenThrow(new InterruptedException());
 Future<?> future = executorService.submit(() -> {
 try {
 dataSource.innerCreateConnection();
 } catch (SQLException e) {
 // Ignore
 }
 });
 future.get();
 }

 @Test
 public void testInitConnection() throws SQLException {
 Connection connection = mock(Connection.class);
 dataSource.initConnection(connection);
 verify(connectionInitializer, times(1)).init(connection);
 }

 @Test
 public void testOnConnectionCreated() {
 dataSource.onConnectionCreated(mock(Connection.class));
 }

 @Test
 public void testGetConnectionThrowsConflictException() {
 when(mockDataSource.getConnection()).thenReturn(mock(Connection.class));
 dataSource.getConnection();
 assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(dataSource.getConnection()));
 }
}