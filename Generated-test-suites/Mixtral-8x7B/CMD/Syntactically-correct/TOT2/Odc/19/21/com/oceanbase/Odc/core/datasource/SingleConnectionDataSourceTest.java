import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.TestConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private Connection connection;
private ReentrantLock lock;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource();
connection = mock(Connection.class);
lock = new ReentrantLock();
}

@After
public void teardown() {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
dataSource.setConnection(connection);
when(connection.isClosed()).thenReturn(false);
Connection result = dataSource.getConnection();
Assert.assertEquals(connection, result);
}

@Test(expected = SQLException.class)
public void testGetConnectionWhenConnectionIsNull() throws SQLException {
dataSource.getConnection();
}

@Test(expected = SQLException.class)
public void testGetConnectionWhenConnectionIsClosed() throws SQLException {
dataSource.setConnection(connection);
when(connection.isClosed()).thenReturn(true);
dataSource.getConnection();
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
dataSource.setConnection(connection);
when(connection.isClosed()).thenReturn(false);
Assert.assertEquals(connection, dataSource.getConnection("testUser", "testPassword"));
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
dataSource.setConnection(connection);
when(connection.isClosed()).thenReturn(false);
dataSource.getConnection("invalidUser", "invalidPassword");
}

@Test
public void testResetConnection() throws SQLException {
dataSource.setConnection(connection);
dataSource.resetConnection();
Assert.assertNull(dataSource.getConnection());
}

@Test
public void testAddInitializer() {
TestConnectionInitializer initializer = new TestConnectionInitializer();
dataSource.addInitializer(initializer);
Assert.assertTrue(dataSource.getInitializerList().contains(initializer));
}

@Test
public void testClose() {
dataSource.close();
verify(connection).close();
}

@Test
public void testGetAutoCommit() {
dataSource.setAutoCommit(true);
Assert.assertEquals(true, dataSource.getAutoCommit());
}

@Test
public void testSetAutoCommit() {
dataSource.setAutoCommit(false);
Assert.assertEquals(false, dataSource.getAutoCommit());
}

@Test
public void testGetConnectionProxy() {
dataSource.setConnection(connection);
dataSource.setLock(lock);
Assert.assertNotSame(connection, dataSource.getConnectionProxy(connection));
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithConflict() throws SQLException {
dataSource.setConnection(connection);
dataSource.setLock(lock);
dataSource.getConnectionProxy(connection);
dataSource.getConnectionProxy(connection);
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithTimeout() throws SQLException, InterruptedException {
dataSource.setConnection(connection);
dataSource.setLock(lock);
Thread.sleep(dataSource.getTimeOutMillis() + 100);
dataSource.getConnectionProxy(connection);
}

@Test
public void testGetTimeOutMillis() {
dataSource.setTimeOutMillis(5000);
Assert.assertEquals(5000, dataSource.getTimeOutMillis());
}

@Test
public void testGetEventPublisher() {
Assert.assertNull(dataSource.getEventPublisher());
}

@Test
public void testSetEventPublisher() {
EventPublisher eventPublisher = mock(EventPublisher.class);
dataSource.setEventPublisher(eventPublisher);
Assert.assertEquals(eventPublisher, dataSource.getEventPublisher());
}
}