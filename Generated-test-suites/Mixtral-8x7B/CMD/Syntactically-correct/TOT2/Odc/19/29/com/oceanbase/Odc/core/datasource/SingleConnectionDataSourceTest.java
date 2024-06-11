import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connection.ConnectionInitializer;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 @Mock
 private ConnectionInitializer connectionInitializer;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private Connection connection;
 @Mock
 private Lock lock;
 private ArgumentCaptor<ConnectionResetEvent> connectionResetEventArgCaptor;

 @Before
 public void setUp() {
 MockitoAnnotations.openMocks(this);
 dataSource = new SingleConnectionDataSource(true);
 dataSource.addInitializer(connectionInitializer);
 dataSource.setEventPublisher(eventPublisher);
 connectionResetEventArgCaptor = ArgumentCaptor.forClass(ConnectionResetEvent.class);
 }

 @After
 public void tearDown() throws Exception {
 dataSource.close();
 }

 @Test
 public void testGetConnection() throws SQLException {
 Connection connection = dataSource.getConnection();
 verify(this.connection, times(1)).isClosed();
 verify(this.connection, times(1)).isValid(any());
 }

 @Test
 public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
 assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUsername", "invalidPassword"));
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 verify(connectionInitializer, times(1)).init(connection);
 }

 @Test
 public void testGetConnectionProxy() {
 Object proxy = dataSource.getConnectionProxy(connection);
 assertTrue(Proxy.isProxyClass(proxy.getClass()));
 }

 @Test
 public void testClose() {
 dataSource.close();
 assertFalse(dataSource.getConnection().isClosed());
 }

 @Test
 public void testTryLock() {
 Lock lock = new ReentrantLock();
 assertTrue(lock.tryLock());
 }

 @Test
 public void testGetConnectionWithLockConflict() {
 assertThrows(ConflictException.class, () -> dataSource.getConnection());
 }

 @Test
 public void testPrepareConnection() throws SQLException {
 dataSource.prepareConnection(connection);
 assertEquals(dataSource.getAutoCommit(), connection.getAutoCommit());
 }

 @Test
 public void testOnConnectionReset() {
 dataSource.onConnectionReset(connection);
 verify(eventPublisher, times(1)).publishEvent(connectionResetEventArgCaptor.capture());
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 dataSource.innerCreateConnection();
 verify(connectionInitializer, times(1)).init(connection);
 }

 @Test
 public void testCloseIgnoreInvocationHandlerCloseMethod() {
 InvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
 handler.invoke(connection, any(), any(), any());
 verify(lock, times(1)).unlock();
 }
}