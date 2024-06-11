import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.datasource.event.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.exception.ConflictException;
import com.oceanbase.odc.core.datasource.initializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.shared.BaseClassBasedDataSource;
import com.oceanbase.odc.core.datasource.shared.DataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import javax.sql.PooledConnection;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@Slf4j
public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;

 @Mock
 private ConnectionInitializer connectionInitializer;

 @Mock
 private EventPublisher eventPublisher;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataSource = new SingleConnectionDataSource();
 }

 @After
 public void teardown() {
 if (dataSource != null) {
 dataSource.close();
 }
 }

 @Test
 public void testGetConnection() throws SQLException {
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 }

 @Test
 public void testGetConnectionWithUsernameAndPassword() throws SQLException {
 dataSource.setUsername("testUser");
 dataSource.setPassword("testPassword");
 Connection connection = dataSource.getConnection("testUser", "testPassword");
 assertNotNull(connection);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
 dataSource.setUsername("testUser");
 dataSource.setPassword("testPassword");
 dataSource.getConnection("invalidUser", "invalidPassword");
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 }

 @Test
 public void testAddInitializer() {
 dataSource.addInitializer(connectionInitializer);
 assertTrue(dataSource.getInitializerList().contains(connectionInitializer));
 }

 @Test
 public void testClose() {
 dataSource.close();
 }

 @Test
 public void testGetAutoCommit() {
 dataSource.setAutoCommit(true);
 assertTrue(dataSource.getAutoCommit());
 }

 @Test
 public void testIsAutoReconnect() {
 assertFalse(dataSource.isAutoReconnect());
 dataSource = new SingleConnectionDataSource(true);
 assertTrue(dataSource.isAutoReconnect());
 }

 @Test
 public void testGetEventPublisher() {
 assertNull(dataSource.getEventPublisher());
 }

 @Test
 public void testGetConnectionProxy() {
 Connection connection = mock(Connection.class);
 dataSource.setLock(new ReentrantLock());
 Connection proxyConnection = dataSource.getConnectionProxy(connection);
 assertNotNull(proxyConnection);
 }

 @Test
 public void testPrepareConnection() throws SQLException {
 Connection connection = mock(Connection.class);
 dataSource.prepareConnection(connection);
 }

 @Test
 public void testTryLockSuccess() {
 Lock lock = new ReentrantLock();
 assertTrue(dataSource.tryLock(lock));
 }

 @Test
 public void testTryLockFailure() {
 Lock lock = new ReentrantLock();
 dataSource.setLock(lock);
 assertFalse(dataSource.tryLock(0, TimeUnit.MILLISECONDS));
 }

 @Test
 public void testGetConnectionProxyWithLock() throws SQLException, ConflictException {
 Connection connection = mock(Connection.class);
 dataSource.setLock(new ReentrantLock());
 dataSource.getConnectionProxy(connection);
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionProxyWithLockFailure()
 throws SQLException, ConflictException {
 Connection connection = mock(Connection.class);
 dataSource.setLock(new ReentrantLock());
 dataSource.getConnectionProxy(connection);
 dataSource.getConnectionProxy(connection);
 }

 @Test
 public void testInitConnection() throws SQLException {
 Connection connection = mock(Connection.class);
 dataSource.initConnection(connection);
 }

 @Test
 public void testOnConnectionCreated() {
 Connection connection = mock(Connection.class);
 dataSource.onConnectionCreated(connection);
 }

 @Test
 public void testOnConnectionReset() {
 Connection connection = mock(Connection.class);
 dataSource.setEventPublisher(eventPublisher);
 dataSource.onConnectionReset(connection);
 ArgumentCaptor<ConnectionResetEvent> argument = ArgumentCaptor.forClass(ConnectionResetEvent.class);
 verify(eventPublisher).publishEvent(argument.capture());
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 Connection connection = dataSource.innerCreateConnection();
 assertNotNull(connection);
 }

 private static class CloseIgnoreInvocationHandlerMock implements InvocationHandler {
 private final Connection target;
 private final Lock lock;

 public CloseIgnoreInvocationHandlerMock(Connection target, Lock lock) {
 this.target = target;
 this.lock = lock;
 }

 @Override
 @SuppressWarnings("all")
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 if ("equals".equals(method.getName())) {
 return (proxy == args[0]);
 } else if ("hashCode".equals(method.getName())) {
 return System.identityHashCode(proxy);
 } else if ("unwrap".equals(method.getName())) {
 if (((Class<?>) args[0]).isInstance(proxy)) {
 return proxy;
 }
 } else if ("isWrapperFor".equals(method.getName())) {
 if (((Class<?>) args[0]).isInstance(proxy)) {
 return true;
 }
 } else if ("close".equals(method.getName())) {
 lock.unlock();
 return null;
 } else if ("isClosed".equals(method.getName())) {
 return false;
 }
 try {
 return method.invoke(this.target, args);
 } catch (InvocationTargetException ex) {
 throw ex.getTargetException();
 }
 }
 }

}