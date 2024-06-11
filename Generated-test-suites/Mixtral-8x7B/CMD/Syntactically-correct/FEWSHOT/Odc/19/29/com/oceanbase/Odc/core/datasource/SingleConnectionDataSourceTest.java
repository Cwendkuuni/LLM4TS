package com.oceanbase.odc.core.datasource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.SQLException;

@SuppressWarnings("unchecked")
public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private Connection connection;
 private Lock lock;
 private EventPublisher eventPublisher;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 connection = mock(Connection.class);
 lock = new ReentrantLock();
 eventPublisher = mock(EventPublisher.class);

 dataSource = new SingleConnectionDataSource(false);
 dataSource.setConnection(connection);
 dataSource.setLock(lock);
 dataSource.setEventPublisher(eventPublisher);
 }

 @Test
 public void testGetConnection() throws SQLException {
 Connection result = dataSource.getConnection();

 assertTrue(result == connection);
 }

 @Test
 public void testGetConnectionWithInvalidUsernameAndPassword() {
 assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPass"));
 }

 @Test
 public void testGetConnectionProxy() {
 Connection proxy = dataSource.getConnectionProxy(connection);

 assertTrue(proxy instanceof Connection);
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();

 verify(connection, times(1)).close();

 when(connection.isClosed()).thenReturn(true);

 dataSource.resetConnection();

 verify(connection, times(2)).close();
 }

 @Test
 public void testAddInitializer() {
 ConnectionInitializer initializer1 = mock(ConnectionInitializer.class);
 ConnectionInitializer initializer2 = mock(ConnectionInitializer.class);

 dataSource.addInitializer(initializer1);
 dataSource.addInitializer(initializer2);

 List<ConnectionInitializer> initializerList = dataSource.getInitializerList();

 assertTrue(initializerList.contains(initializer1));
 assertTrue(initializerList.contains(initializer2));
 }

 @Test
 public void testClose() {
 dataSource.close();

 verify(connection, times(1)).close();
 }

 @Test
 public void testPrepareConnection() throws SQLException {
 when(connection.getAutoCommit()).thenReturn(false);

 dataSource.prepareConnection(connection);

 verify(connection, times(1)).setAutoCommit(true);
 }

 @Test
 public void testTryLock() {
 when(lock.tryLock(dataSource.timeOutMillis, TimeUnit.MILLISECONDS)).thenReturn(true);

 assertTrue(dataSource.tryLock());
 }

 @Test
 public void testTryLockInterrupted() {
 when(lock.tryLock(dataSource.timeOutMillis, TimeUnit.MILLISECONDS)).thenThrow(
 InterruptedException.class);

 assertFalse(dataSource.tryLock());
 }

 @Test
 public void testPublishConnectionResetEvent() {
 dataSource.publishConnectionResetEvent(connection);

 verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 Connection result = dataSource.innerCreateConnection();

 verify(connection, times(1)).close();

 dataSource.setConnection(null);
 dataSource.setLock(null);

 result = dataSource.innerCreateConnection();

 verify(connection, times(1)).close();
 }

 @Test
 public void testCloseIgnoreInvocationHandler() {
 InvocationHandler handler = dataSource.new CloseIgnoreInvocationHandler(connection, lock);

 Class<?>[] interfaces = new Class[] {Connection.class};
 Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), interfaces, handler);

 Method closeMethod = Arrays.stream(interfaces[0].getDeclaredMethods())
 .filter(m -> m.getName().equals("close"))
 .findFirst()
 .orElse(null);

 closeMethod.invoke(proxy);

 verify(lock, times(1)).unlock();
 }

 @Test
 public void testCloseIgnoreInvocationHandlerIsClosed() {
 InvocationHandler handler = dataSource.new CloseIgnoreInvocationHandler(connection, lock);

 Class<?>[] interfaces = new Class[] {Connection.class};
 Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), interfaces, handler);

 Method isClosedMethod = Arrays.stream(interfaces[0].getDeclaredMethods())
 .filter(m -> m.getName().equals("isClosed"))
 .findFirst()
 .orElse(null);

 Object result = isClosedMethod.invoke(proxy);

 assertTrue(result instanceof Boolean);
 assertTrue((Boolean) result);
 }
}

class ConnectionInitializerMock implements ConnectionInitializer {

 @Override
 public void init(Connection connection) throws Exception {
 // Intentionally left blank
 }
}

class ConnectionResetEventMock implements ConnectionResetEvent {

 private static final long serialVersionUID = 1L;

 public ConnectionResetEventMock(Connection connection) {
 // Intentionally left blank
 }
}