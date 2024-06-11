import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;

@SuppressWarnings("rawtypes")
public class SingleConnectionDataSourceTest {

 private SingleConnectionDataSource dataSource;
 @Mock
 private ConnectionInitializer connectionInitializer;
 @Mock
 private EventPublisher eventPublisher;
 @Mock
 private Connection connection;
 private Lock lock;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataSource = new SingleConnectionDataSource(true);
 dataSource.addInitializer(connectionInitializer);
 dataSource.setEventPublisher(eventPublisher);
 lock = new ReentrantLock();
 when(connection.isClosed()).thenReturn(false);
 }

 @After
 public void tearDown() {
 dataSource.close();
 }

 @Test
 public void testGetConnection() throws SQLException {
 // Test when connection is null
 dataSource.setConnection(null);
 dataSource.getConnection();
 verify(connectionInitializer, times(1)).init(ArgumentMatchers.any(Connection.class));
 verify(connection, times(1)).isValid(dataSource.getLoginTimeout());

 // Test when connection is not null and closed
 when(connection.isClosed()).thenReturn(true);
 dataSource.getConnection();
 verify(connection, times(1)).close();

 // Test when connection is not null and valid
 dataSource.setConnection(connection);
 dataSource.getConnection();
 verify(connection, times(1)).isValid(dataSource.getLoginTimeout());
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionProxy() throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
 dataSource.setConnection(connection);
 Method method = Connection.class.getDeclaredMethod("close");
 method.setAccessible(true);
 method.invoke(connection);
 dataSource.getConnectionProxy(connection);
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.setConnection(connection);
 dataSource.resetConnection();
 verify(connection, times(1)).close();
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 dataSource.innerCreateConnection();
 verify(connectionInitializer, times(1)).init(ArgumentMatchers.any(Connection.class));
 }

 @Test
 public void testCloseIgnoreInvocationHandler() {
 InvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, lock);
 Method equalsMethod = Object.class.getMethod("equals", Object.class);
 Method hashCodeMethod = Object.class.getMethod("hashCode");
 Method unwrapMethod = Connection.class.getMethod("unwrap", Class.class);
 Method isWrapperForMethod = Connection.class.getMethod("isWrapperFor", Class.class);
 Method closeMethod = Connection.class.getMethod("close");
 Method isClosedMethod = Connection.class.getMethod("isClosed");

 assertEquals(handler.invoke(null, null, new Object[] { null }), true);
 assertEquals(handler.invoke(null, null, new Object[] { new Object() }), false);
 assertEquals(handler.invoke(null, null, new Object[] { connection }), true);
 assertEquals(handler.invoke(null, hashCodeMethod, null), System.identityHashCode(null));
 assertEquals(handler.invoke(null, unwrapMethod, new Object[] { Connection.class }), connection);
 assertEquals(handler.invoke(null, isWrapperForMethod, new Object[] { Connection.class }), true);
 assertNull(handler.invoke(null, closeMethod, null));
 assertEquals(handler.invoke(null, isClosedMethod, null), false);
 }

 @Test
 public void testLock() throws InterruptedException {
 assertTrue(dataSource.tryLock());
 assertFalse(dataSource.tryLock());
 }

 @Test
 public void testPublishConnectionResetEvent() throws Exception {
 dataSource.onConnectionReset(connection);
 verify(eventPublisher, times(1)).publishEvent(ArgumentMatchers.any(ConnectionResetEvent.class));
 }

}