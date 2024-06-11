import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;

public class SingleConnectionDataSourceTest {

 private static final String VALID_USERNAME = "username";
 private static final String VALID_PASSWORD = "password";
 private static final String INVALID_USERNAME = "invalid_username";
 private static final String INVALID_PASSWORD = "invalid_password";

 @Mock
 private ConnectionInitializer connectionInitializer;

 private SingleConnectionDataSource dataSource;
 private Connection connection;
 private Lock lock;
 private EventPublisher eventPublisher;

 @Before
 public void setUp() {
 MockitoAnnotations.openMocks(this);
 dataSource = new SingleConnectionDataSource();
 connection = mock(Connection.class);
 lock = new ReentrantLock();
 eventPublisher = mock(EventPublisher.class);
 dataSource.setConnectionInitializerList(Collections.singletonList(connectionInitializer));
 dataSource.setEventPublisher(eventPublisher);
 }

 @After
 public void tearDown() {
 dataSource.close();
 }

 @Test
 public void testGetConnectionWithValidCredentials() throws SQLException {
 Connection result = dataSource.getConnection(VALID_USERNAME, VALID_PASSWORD);
 verify(connectionInitializer).init(connection);
 assertTrue(result.isClosed());
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidCredentials() throws SQLException {
 dataSource.getConnection(INVALID_USERNAME, INVALID_PASSWORD);
 }

 @Test
 public void testGetConnectionProxy() {
 Connection result = dataSource.getConnectionProxy(connection);
 assertTrue(result instanceof Proxy);
 }

 @Test
 public void testTryLock() throws InterruptedException {
 assertTrue(dataSource.tryLock());
 assertFalse(dataSource.tryLock());
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 verify(connectionInitializer).init(connection);
 }

 @Test
 public void testAddInitializer() {
 dataSource.addInitializer(connectionInitializer);
 assertEquals(1, dataSource.getConnectionInitializerList().size());
 }

 @Test
 public void testClose() {
 dataSource.close();
 verify(connection).close();
 }

 @Test
 public void testPrepareConnection() throws SQLException {
 Boolean autoCommit = Boolean.TRUE;
 dataSource.setAutoCommit(autoCommit);
 Connection connection = mock(Connection.class);
 dataSource.prepareConnection(connection);
 verify(connection).setAutoCommit(autoCommit);
 }

 @Test
 public void testGetConnectionOccupied() {
 final ArgumentCaptor<Throwable> argumentCaptor = ArgumentCaptor.forClass(Throwable.class);
 dataSource.getConnectionProxy(connection);
 dataSource.getConnectionProxy(connection);
 verify(eventPublisher).publishEvent(argumentCaptor.capture());
 Throwable throwable = argumentCaptor.getValue();
 assertTrue(throwable instanceof ConflictException);
 assertEquals(ErrorCodes.ConnectionOccupied, ((ConflictException) throwable).getErrorCode());
 }

 @Test
 public void testInitConnection() throws SQLException {
 dataSource.initConnection(connection);
 verify(connectionInitializer).init(connection);
 }

 @Test
 public void testOnConnectionCreated() throws SQLException {
 dataSource.onConnectionCreated(connection);
 verifyNoMoreInteractions(connectionInitializer);
 }

 @Test
 public void testOnConnectionReset() {
 dataSource.onConnectionReset(connection);
 verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 dataSource.innerCreateConnection();
 verify(connectionInitializer).init(connection);
 }

 @Test
 public void testCloseIgnoreInvocationHandler() {
 InvocationHandler invocationHandler = new CloseIgnoreInvocationHandler(connection, lock);
 Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class },
 invocationHandler);
 assertFalse(((Connection) proxy).isClosed());
 Method closeMethod = Arrays.stream(Connection.class.getDeclaredMethods())
 .filter(method -> method.getName().equals("close"))
 .findFirst()
 .orElseThrow(() -> new RuntimeException("Failed to find close method"));
 closeMethod.invoke(proxy);
 verify(connection).close();
 assertTrue(lock.isHeldByCurrentThread());
 }

 @Test
 public void testCloseIgnoreInvocationHandlerWithIsClosed() {
 InvocationHandler invocationHandler = new CloseIgnoreInvocationHandler(connection, lock);
 Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class },
 invocationHandler);
 assertFalse(((Connection) proxy).isClosed());
 Method isClosedMethod = Arrays.stream(Connection.class.getDeclaredMethods())
 .filter(method -> method.getName().equals("isClosed"))
 .findFirst()
 .orElseThrow(() -> new RuntimeException("Failed to find isClosed method"));
 assertFalse((Boolean) isClosedMethod.invoke(proxy));
 }

 @Test
 public void testCloseIgnoreInvocationHandlerWithEquals() {
 InvocationHandler invocationHandler = new CloseIgnoreInvocationHandler(connection, lock);
 Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class },
 invocationHandler);
 assertTrue(proxy.equals(proxy));
 }

 @Test
 public void testCloseIgnoreInvocationHandlerWithHashCode() {
 InvocationHandler invocationHandler = new CloseIgnoreInvocationHandler(connection, lock);
 Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class },
 invocationHandler);
 assertEquals(System.identityHashCode(proxy), proxy.hashCode());
 }

 @Test
 public void testCloseIgnoreInvocationHandlerWithUnwrap() {
 InvocationHandler invocationHandler = new CloseIgnoreInvocationHandler(connection, lock);
 Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class },
 invocationHandler);
 assertTrue(Proxy.isProxyClass(proxy.getClass()));
 assertTrue(((Class<?>) Proxy.getProxyClass(proxy.getClass().getClassLoader(), Connection.class))
 .isInstance(proxy));
 }

 @Test
 public void testCloseIgnoreInvocationHandlerWithIsWrapperFor() {
 InvocationHandler invocationHandler = new CloseIgnoreInvocationHandler(connection, lock);
 Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class },
 invocationHandler);
 assertTrue(Proxy.isProxyClass(proxy.getClass()));
 assertTrue(((Class<?>) Proxy.getProxyClass(proxy.getClass().getClassLoader(), Connection.class))
 .isInstance(proxy));
 }

 @Test(expected = IllegalStateException.class)
 public void testInnerCreateConnectionWithConnectionNotClose() throws SQLException {
 dataSource.innerCreateConnection();
 dataSource.innerCreateConnection();
 }
}