import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;

public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 private ConnectionInitializer connectionInitializer;
 private Connection connection;
 private Lock lock;
 private EventPublisher eventPublisher;

 @Before
 public void setUp() {
 connectionInitializer = mock(ConnectionInitializer.class);
 connection = mock(Connection.class);
 lock = new ReentrantLock();
 eventPublisher = mock(EventPublisher.class);
 dataSource = new SingleConnectionDataSource(false);
 dataSource.addInitializer(connectionInitializer);
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
 public void testGetConnectionWithInvalidCredential() {
 assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPassword"));
 }

 @Test
 public void testGetConnectionWithNullCredential() {
 assertThrows(SQLException.class, () -> dataSource.getConnection(null, null));
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 verify(connection).close();

 dataSource = new SingleConnectionDataSource(false);
 dataSource.setConnection(connection);
 dataSource.setLock(lock);
 dataSource.resetConnection();
 verify(connection).close();
 }

 @Test
 public void testAddInitializer() {
 dataSource.addInitializer(connectionInitializer);
 assertTrue(dataSource.getInitializerList().contains(connectionInitializer));
 }

 @Test
 public void testClose() {
 dataSource.close();
 verify(connection).close();
 }

 @Test
 public void testGetConnectionProxy() {
 Connection connectionProxy = dataSource.getConnectionProxy(connection);
 assertTrue(connectionProxy instanceof Connection);
 }

 @Test
 public void testPrepareConnection() throws SQLException {
 Boolean autoCommit = true;
 dataSource.setAutoCommit(autoCommit);
 dataSource.prepareConnection(connection);
 assertTrue(connection.getAutoCommit() == autoCommit);
 }

 @Test
 public void testTryLock() {
 assertTrue(dataSource.tryLock());
 }

 @Test
 public void testTryLockWithInterruptedException() {
 assertThrows(RuntimeException.class, () -> dataSource.tryLock());
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 Connection result = dataSource.innerCreateConnection();
 assertTrue(result == connection);
 }

 @Test
 public void testInitConnection() throws SQLException {
 List<ConnectionInitializer> initializerList = new LinkedList<>();
 initializerList.add(connectionInitializer);
 dataSource.setInitializerList(initializerList);
 dataSource.initConnection(connection);
 ArgumentCaptor<Connection> argument = ArgumentCaptor.forClass(Connection.class);
 verify(connectionInitializer).init(argument.capture());
 assertTrue(argument.getValue() == connection);
 }

 @Test
 public void testOnConnectionCreated() {
 dataSource.onConnectionCreated(connection);
 verify(connection).isValid(Mockito.anyInt());
 }

 @Test
 public void testOnConnectionReset() {
 dataSource.onConnectionReset(connection);
 verify(eventPublisher).publishEvent(Mockito.any(ConnectionResetEvent.class));
 }

 @Test
 public void testCloseIgnoreInvocationHandler() {
 InvocationHandler handler = dataSource.new CloseIgnoreInvocationHandler(connection, lock);
 Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
 new Class[] {Connection.class}, handler);

 assertTrue(handler.invoke(proxy, equalsMethod(proxy, connection)));
 assertTrue(handler.invoke(proxy, hashCodeMethod(proxy)));
 assertTrue(handler.invoke(proxy, unwrapMethod(proxy, connection.getClass())));
 assertTrue(handler.invoke(proxy, isWrapperForMethod(proxy, connection.getClass())));

 // Unlock the lock after calling close
 handler.invoke(proxy, closeMethod());
 handler.invoke(proxy, isClosedMethod());

 // Exception should not be thrown when invoking methods after closing
 handler.invoke(proxy, createMethod());
 handler.invoke(proxy, prepareStatementMethod());

 assertFalse((Boolean) handler.invoke(proxy, isClosedMethod()));
 }

 @Test
 public void testCloseIgnoreInvocationHandlerWithInterruptedException()
 throws Throwable {
 InvocationHandler handler = dataSource.new CloseIgnoreInvocationHandler(connection, lock);
 Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
 new Class[] {Connection.class}, handler);

 handler.invoke(proxy, tryLockMethod());
 assertThrows(ConflictException.class, () -> handler.invoke(proxy, tryLockMethod()));
 }

 private Method equalsMethod(Object proxy, Connection target) {
 Method method;
 try {
 method = Connection.class.getDeclaredMethod("equals", Object.class);
 } catch (NoSuchMethodException | SecurityException e) {
 throw new RuntimeException(e);
 }
 method.setAccessible(true);
 return method;
 }

 private Method hashCodeMethod(Object proxy) {
 Method method;
 try {
 method = Connection.class.getDeclaredMethod("hashCode");
 } catch (NoSuchMethodException | SecurityException e) {
 throw new RuntimeException(e);
 }
 method.setAccessible(true);
 return method;
 }

 private Method unwrapMethod(Object proxy, Class<?> clazz) {
 Method method;
 try {
 method = Connection.class.getDeclaredMethod("unwrap", Class.class);
 } catch (NoSuchMethodException | SecurityException e) {
 throw new RuntimeException(e);
 }
 method.setAccessible(true);
 return method;
 }

 private Method isWrapperForMethod(Object proxy, Class<?> clazz) {
 Method method;
 try {
 method = Connection.class.getDeclaredMethod("isWrapperFor", Class.class);
 } catch (NoSuchMethodException | SecurityException e) {
 throw new RuntimeException(e);
 }
 method.setAccessible(true);
 return method;
 }

 private Method closeMethod() {
 Method method;
 try {
 method = Connection.class.getDeclaredMethod("close");
 } catch (NoSuchMethodException | SecurityException e) {
 throw new RuntimeException(e);
 }
 method.setAccessible(true);
 return method;
 }

 private Method isClosedMethod() {
 Method method;
 try {
 method = Connection.class.getDeclaredMethod("isClosed");
 } catch (NoSuchMethodException | SecurityException e) {
 throw new RuntimeException(e);
 }
 method.setAccessible(true);
 return method;
 }

 private Method createMethod() {
 Method method;
 try {
 method = Connection.class.getDeclaredMethod("createStatement");
 } catch (NoSuchMethodException | SecurityException e) {
 throw new RuntimeException(e);
 }
 method.setAccessible(true);
 return method;
 }

 private Method prepareStatementMethod() {
 Method method;
 try {
 method = Connection.class.getDeclaredMethod("prepareStatement", String.class);
 } catch (NoSuchMethodException | SecurityException e) {
 throw new RuntimeException(e);
 }
 method.setAccessible(true);
 return method;
 }

 private Method tryLockMethod() {
 Method method;
 try {
 method = Lock.class.getDeclaredMethod("tryLock", long.class, TimeUnit.class);
 } catch (NoSuchMethodException | SecurityException e) {
 throw new RuntimeException(e);
 }
 method.setAccessible(true);
 return method;
 }
}