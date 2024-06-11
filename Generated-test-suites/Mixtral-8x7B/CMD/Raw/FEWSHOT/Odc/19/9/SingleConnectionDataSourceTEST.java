import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private ConnectionInitializer connectionInitializer;
 private EventPublisher eventPublisher;
 private Connection connection;
 private Lock lock;

 @Before
 public void setUp() {
 connectionInitializer = mock(ConnectionInitializer.class);
 eventPublisher = mock(EventPublisher.class);
 connection = mock(Connection.class);
 lock = new ReentrantLock();
 }

 @Test
 public void testConstructor() {
 dataSource = new SingleConnectionDataSource();
 assertNotNull(dataSource.getConnection());
 assertFalse(dataSource.isAutoReconnect());
 assertNull(dataSource.getEventPublisher());
 assertNull(dataSource.getConnectionProxy(connection));
 assertNull(dataSource.getLock());

 dataSource = new SingleConnectionDataSource(true);
 assertTrue(dataSource.isAutoReconnect());
 }

 @Test
 public void testGetConnection() throws SQLException {
 dataSource = new SingleConnectionDataSource();

 // Test when connection is null
 when(connection.isClosed()).thenReturn(true);
 when(connection.isValid(Mockito.anyInt())).thenReturn(false);

 dataSource.setConnection(connection);

 assertNotNull(dataSource.getConnection());
 verify(connection, times(1)).close();

 // Test when connection is not null and not closed
 when(connection.isClosed()).thenReturn(false);
 when(connection.isValid(Mockito.anyInt())).thenReturn(true);

 dataSource.setConnection(connection);

 assertNotNull(dataSource.getConnection());
 verify(connection, times(0)).close();
 }

 @Test
 public void testGetConnectionWithUsernameAndPassword() throws SQLException {
 dataSource = new SingleConnectionDataSource();

 assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUsername", "invalidPassword"));
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource = new SingleConnectionDataSource();
 dataSource.setConnection(connection);

 dataSource.resetConnection();

 verify(connection, times(1)).close();
 }

 @Test
 public void testAddInitializer() {
 dataSource = new SingleConnectionDataSource();

 dataSource.addInitializer(connectionInitializer);
 List<ConnectionInitializer> initializerList = dataSource.getInitializerList();
 assertTrue(initializerList.contains(connectionInitializer));
 }

 @Test
 public void testClose() {
 dataSource = new SingleConnectionDataSource();
 dataSource.setConnection(connection);

 dataSource.close();

 verify(connection, times(1)).close();
 }

 @Test
 public void testPrepareConnection() throws SQLException {
 dataSource = new SingleConnectionDataSource();
 dataSource.setAutoCommit(true);
 Connection connection = mock(Connection.class);
 when(connection.getAutoCommit()).thenReturn(false);
 dataSource.prepareConnection(connection);
 assertTrue(connection.getAutoCommit());
 }

 @Test
 public void testTryLock() throws InterruptedException {
 dataSource = new SingleConnectionDataSource();
 dataSource.setTimeOutMillis(100);
 assertTrue(dataSource.tryLock());
 }

 @Test
 public void testGetConnectionProxy() {
 dataSource = new SingleConnectionDataSource();
 dataSource.setLock(lock);

 assertNotNull(dataSource.getConnectionProxy(connection));
 }

 @Test
 public void testInitConnection() throws SQLException {
 dataSource = new SingleConnectionDataSource();
 dataSource.setInitializerList(Collections.singletonList(connectionInitializer));
 dataSource.setConnection(connection);

 dataSource.initConnection(connection);

 verify(connectionInitializer, times(1)).init(connection);
 }

 @Test
 public void testOnConnectionCreated() {
 dataSource = new SingleConnectionDataSource();
 dataSource.setConnection(connection);

 dataSource.onConnectionCreated(connection);

 verify(connection, times(1)).isValid(Mockito.anyInt());
 }

 @Test
 public void testOnConnectionReset() throws SQLException {
 dataSource = new SingleConnectionDataSource();
 dataSource.setConnection(connection);
 dataSource.setEventPublisher(eventPublisher);

 dataSource.onConnectionReset(connection);

 ArgumentCaptor<ConnectionResetEvent> argument = ArgumentCaptor.forClass(ConnectionResetEvent.class);
 verify(eventPublisher, times(1)).publishEvent(argument.capture());
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 dataSource = new SingleConnectionDataSource();
 dataSource.setConnection(null);
 dataSource.setLock(lock);

 dataSource.innerCreateConnection();

 verify(connection, times(1)).close();
 verify(lock, times(1)).unlock();
 }

 @Test
 public void testCloseIgnoreInvocationHandler() {
 InvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, lock);
 Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
 new Class[] {Connection.class}, handler);

 ((Connection) proxy).close();

 verify(lock, times(1)).unlock();
 }
}