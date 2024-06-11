import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource testDataSource;
private ConnectionInitializer initializer;
private Connection connection;
private Lock lock;

@Before
public void setUp() throws Exception {
dataSource = new SingleConnectionDataSource();
initializer = mock(ConnectionInitializer.class);
dataSource.addInitializer(initializer);
lock = new ReentrantLock();
connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
doNothing().when(initializer).init(connection);
doNothing().when(initializer).close();
}

@After
public void tearDown() throws Exception {
if (connection != null) {
connection.close();
}
if (dataSource != null) {
dataSource.close();
}
}

@Test
public void testConstructor() {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
assertNotNull(dataSource);
}

@Test
public void testConstructorWithAutoReconnect() {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource(true);
assertTrue(dataSource.isAutoReconnect());
}

@Test
public void testGetConnection() throws SQLException {
when(initializer.init(connection)).thenReturn(true);
dataSource.setConnection(connection);
Connection result = dataSource.getConnection();
assertSame(connection, result);
verify(initializer, times(1)).init(connection);
}

@Test
public void testGetConnectionWithInvalidUsernameAndPassword() {
dataSource.setUsername("invalid_user");
dataSource.setPassword("invalid_password");
try {
dataSource.getConnection("test_user", "test_password");
fail("Expected SQLException");
} catch (SQLException e) {
assertEquals("Invalid username or password", e.getMessage());
}
}

@Test
public void testGetConnectionWithNullUsernameAndPassword() {
dataSource.setUsername(null);
dataSource.setPassword(null);
try {
dataSource.getConnection(null, null);
fail("Expected SQLException");
} catch (SQLException e) {
assertEquals("Invalid username or password", e.getMessage());
}
}

@Test
public void testResetConnection() throws SQLException {
dataSource.setConnection(connection);
dataSource.resetConnection();
verify(initializer, times(1)).close();
}

@Test
public void testAddInitializer() {
dataSource.addInitializer(initializer);
List<ConnectionInitializer> initializers = dataSource.getInitializerList();
assertTrue(initializers.contains(initializer));
}

@Test
public void testClose() throws SQLException {
dataSource.setConnection(connection);
dataSource.close();
verify(initializer, times(1)).close();
}

@Test
public void testInnerCreateConnection() throws SQLException {
dataSource.setUsername("test_user");
dataSource.setPassword("test_password");
Connection result = dataSource.innerCreateConnection();
assertNotNull(result);
verify(initializer, times(1)).init(result);
}

@Test(expected = IllegalStateException.class)
public void testInnerCreateConnectionWithExistingConnection() throws SQLException {
dataSource.setConnection(connection);
dataSource.innerCreateConnection();
}

@Test
public void testGetConnectionProxy() throws SQLException {
dataSource.setConnection(connection);
dataSource.setLock(lock);
Connection result = dataSource.getConnectionProxy(connection);
assertTrue(Proxy.isProxyClass(result.getClass()));
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithLockTimeout() throws SQLException {
dataSource.setConnection(connection);
dataSource.setLock(lock);
when(lock.tryLock(1, TimeUnit.MILLISECONDS)).thenReturn(false);
dataSource.getConnectionProxy(connection);
}

@Test
public void testTryLock() {
lock.lock();
assertTrue(dataSource.tryLock());
lock.unlock();
assertFalse(dataSource.tryLock());
}

@Test
public void testOnConnectionReset() {
EventPublisher eventPublisher = mock(EventPublisher.class);
dataSource.setEventPublisher(eventPublisher);
dataSource.setConnection(connection);
dataSource.onConnectionReset(connection);
verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
}

@Test
public void testPrepareConnection() throws SQLException {
dataSource.setAutoCommit(true);
Connection connection = mock(Connection.class);
when(connection.getAutoCommit()).thenReturn(false);
dataSource.prepareConnection(connection);
verify(connection, times(1)).setAutoCommit(true);
}
}