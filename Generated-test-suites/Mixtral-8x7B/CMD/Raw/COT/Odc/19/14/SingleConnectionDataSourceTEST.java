import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private ConnectionInitializer connectionInitializer;
private Connection connection;
private Lock lock;

@Before
public void setUp() {
dataSource = new SingleConnectionDataSource();
connectionInitializer = mock(ConnectionInitializer.class);
dataSource.addInitializer(connectionInitializer);
connection = mock(Connection.class);
lock = new ReentrantLock();
}

@Test
public void testGetConnection() throws SQLException {
when(connection.isClosed()).thenReturn(false);
dataSource.connection = connection;
Connection result = dataSource.getConnection();
assertEquals(connection, result);
}

@Test
public void testGetConnectionWhenConnectionIsNull() throws SQLException {
Connection result = dataSource.getConnection();
assertNotNull(result);
}

@Test
public void testGetConnectionWhenConnectionIsClosed() throws SQLException {
when(connection.isClosed()).thenReturn(true);
dataSource.connection = connection;
assertThrows(SQLException.class, () -> dataSource.getConnection());
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
String username = "testUser";
String password = "testPassword";
when(connection.isClosed()).thenReturn(false);
when(connection.getMetaData().getUserName()).thenReturn(username);
when(connection.getMetaData().getPassword()).thenReturn(password);
dataSource.connection = connection;
Connection result = dataSource.getConnection(username, password);
assertEquals(connection, result);
}

@Test
public void testGetConnectionWithInvalidUsernameOrPassword() throws SQLException {
String invalidUsername = "invalidUser";
String invalidPassword = "invalidPassword";
when(connection.isClosed()).thenReturn(false);
when(connection.getMetaData().getUserName()).thenReturn("testUser");
when(connection.getMetaData().getPassword()).thenReturn("testPassword");
dataSource.connection = connection;
assertThrows(SQLException.class, () -> dataSource.getConnection(invalidUsername, invalidPassword));
}

@Test
public void testResetConnection() throws SQLException {
dataSource.connection = connection;
dataSource.resetConnection();
verify(connection, times(1)).close();
}

@Test
public void testAddInitializer() {
List<ConnectionInitializer> initializers = new LinkedList<>();
SingleConnectionDataSource dataSourceSpy = spy(dataSource);
initializers.add(connectionInitializer);
dataSourceSpy.addInitializer(connectionInitializer);
assertEquals(initializers, dataSourceSpy.getInitializerList());
}

@Test
public void testClose() {
dataSource.connection = connection;
dataSource.close();
verify(connection, times(1)).close();
}

@Test
public void testPrepareConnection() throws SQLException {
Boolean autoCommit = true;
dataSource.setAutoCommit(autoCommit);
Connection connectionSpy = spy(connection);
dataSource.prepareConnection(connectionSpy);
verify(connectionSpy, times(1)).setAutoCommit(autoCommit);
}

@Test
public void testTryLock() {
Lock lockSpy = spy(lock);
when(lockSpy.tryLock(anyLong(), any(TimeUnit.class))).thenReturn(true);
assertTrue(dataSource.tryLock(lockSpy));
}

@Test
public void testTryLockWithInterruptedException() {
Lock lockSpy = spy(lock);
when(lockSpy.tryLock(anyLong(), any(TimeUnit.class))).thenThrow(new InterruptedException());
assertFalse(dataSource.tryLock(lockSpy));
}

@Test
public void testGetConnectionProxy() {
Connection connectionProxy = dataSource.getConnectionProxy(connection);
assertTrue(Proxy.isProxyClass(connectionProxy.getClass()));
}

@Test
public void testOnConnectionCreated() throws SQLException {
dataSource.onConnectionCreated(connection);
verifyNoMoreInteractions(connection);
}

@Test
public void testOnConnectionReset() {
EventPublisher eventPublisher = mock(EventPublisher.class);
dataSource.setEventPublisher(eventPublisher);
dataSource.onConnectionReset(connection);
verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
}

@Test
public void testInnerCreateConnection() throws SQLException {
Connection result = dataSource.innerCreateConnection();
verify(connection, times(1)).close();
}

@Test
public void testInnerCreateConnectionWithAutoReconnectTrue() throws SQLException {
dataSource = new SingleConnectionDataSource(true);
Connection result = dataSource.innerCreateConnection();
assertNotNull(result);
}

@Test
public void testInnerCreateConnectionWithAutoReconnectFalse() {
dataSource = new SingleConnectionDataSource(false);
assertThrows(IllegalStateException.class, dataSource::innerCreateConnection);
}

@Test
public void testCloseIgnoreInvocationHandlerCloseMethod() {
CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
handler.invoke(connection, null, null, new Object[] {});
verify(connection, times(1)).close();
lock.unlock();
}

@Test
public void testCloseIgnoreInvocationHandlerIsClosedMethod() {
CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
assertTrue((Boolean) handler.invoke(connection, null, null, new Object[] { "isClosed" }));
}

@Test
public void testCloseIgnoreInvocationHandlerOtherMethods() throws Throwable {
Connection connectionSpy = spy(connection);
CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connectionSpy, lock);
Method method = Connection.class.getDeclaredMethod("isValid", int.class);
handler.invoke(connectionSpy, null, method, new Object[] { 5000 });
verify(connectionSpy, times(1)).isValid(5000);
}

@Test
public void testCloseIgnoreInvocationHandlerWrapperForMethod() {
Class<?> clazz = Proxy.getProxyClass(Connection.class.getClassLoader(), new Class[] { Connection.class });
Connection connectionProxy = (Connection) clazz.getConstructor(Connection.class).newInstance(connection);
assertTrue(Connection.class.isInstance(connectionProxy));
assertTrue(connectionProxy.isWrapperFor(Connection.class));
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithLockConflict() throws ConflictException {
Lock lockSpy = spy(lock);
when(lockSpy.tryLock(dataSource.timeOutMillis, TimeUnit.MILLISECONDS)).thenReturn(false);
dataSource.getConnectionProxy(connection);
}
}