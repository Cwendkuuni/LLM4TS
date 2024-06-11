import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource defaultDataSource;
private ConnectionInitializer initializer;

@Before
public void setup() {
defaultDataSource = new BaseClassBasedDataSource();
initializer = new ConnectionInitializerImpl();
dataSource = new SingleConnectionDataSource(defaultDataSource, initializer);
}

@After
public void teardown() throws SQLException {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
assertEquals(defaultDataSource.getUsername(), connection.getMetaData().getUserName());
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
Connection connection = dataSource.getConnection(defaultDataSource.getUsername(),
defaultDataSource.getPassword());
assertNotNull(connection);
assertEquals(defaultDataSource.getUsername(), connection.getMetaData().getUserName());
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
dataSource.getConnection("invalidUser", "invalidPassword");
}

@Test
public void testResetConnection() throws SQLException {
dataSource.getConnection();
dataSource.resetConnection();
assertNotNull(dataSource.getConnection());
}

@Test
public void testAddInitializer() {
dataSource.addInitializer(initializer);
assertTrue(dataSource.getInitializerList().contains(initializer));
}

@Test
public void testGetConnectionProxy() {
ReentrantLock lock = new ReentrantLock();
Connection connection = dataSource.getConnectionProxy(lock);
assertNotNull(connection);
}

@Test
public void testGetConnectionProxyWithLockConflict() {
ReentrantLock lock = new ReentrantLock();
dataSource.getConnectionProxy(lock);
assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(lock));
}

@Test
public void testInitConnection() throws SQLException {
dataSource.initConnection(newConnectionFromDriver("user", "password"));
}

@Test
public void testOnConnectionCreated() {
dataSource.onConnectionCreated(newConnectionFromDriver("user", "password"));
}

@Test
public void testOnConnectionReset() {
dataSource.onConnectionReset(newConnectionFromDriver("user", "password"));
}

@Test
public void testClose() throws SQLException {
dataSource.getConnection();
dataSource.close();
assertThrows(SQLException.class, dataSource::getConnection);
}

@Test
public void testPrepareConnection() throws SQLException {
Connection connection = newConnectionFromDriver("user", "password");
dataSource.prepareConnection(connection);
assertEquals(dataSource.getAutoCommit(), connection.getAutoCommit());
}

@Test
public void testTryLock() throws InterruptedException {
ReentrantLock lock = new ReentrantLock();
assertTrue(lock.tryLock(100, TimeUnit.MILLISECONDS));
assertFalse(lock.tryLock(1, TimeUnit.MILLISECONDS));
}

}

class CloseIgnoreInvocationHandlerTest {

@Test
public void testInvoke() throws Throwable {
Connection connection = newConnectionFromDriver("user", "password");
ReentrantLock lock = new ReentrantLock();
CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class}, handler);

assertEquals(proxy, Proxy.getInvocationHandler(proxy));
assertEquals(0, ((Connection) proxy).hashCode());
assertTrue(((Connection) proxy).equals(proxy));
assertFalse(((Connection) proxy).isWrapperFor(Object.class));
assertFalse(((Connection) proxy).isWrapperFor(Connection.class));
assertFalse(((Connection) proxy).isClosed());

handler.invoke(proxy, Methods.unwrap.getMethod, new Class[] {Class.class}, new Object[] {Connection.class});
assertEquals(proxy, Methods.unwrap.invoke(connection, new Object[] {Connection.class}));

handler.invoke(proxy, Methods.isWrapperFor.getMethod, new Class[] {Class.class},
new Object[] {Connection.class});
assertEquals(true, Methods.isWrapperFor.invoke(connection, new Object[] {Connection.class}));

handler.invoke(proxy, Methods.close.getMethod, new Class[0], new Object[0]);
lock.unlock();

assertThrows(IllegalStateException.class, () -> handler.invoke(proxy, Methods.close.getMethod,
new Class[0], new Object[0]));

}

}