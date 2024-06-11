import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerTestImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource mockDataSource;
private ConnectionInitializer initializer;
private ExecutorService executorService;

@Before
public void setup() {
initializer = new ConnectionInitializerTestImpl();
dataSource = new SingleConnectionDataSource();
dataSource.addInitializer(initializer);

mockDataSource = Mockito.mock(DataSource.class);
dataSource.setDelegate(mockDataSource);

executorService = Executors.newFixedThreadPool(10);
}

@Test
public void testGetConnection_shouldReturnConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void testGetConnection_shouldThrowExceptionWhenDataSourceIsNull() throws SQLException {
dataSource.setDelegate(null);
dataSource.getConnection();
}

@Test
public void testGetConnection_shouldReturnProxyConnection() throws SQLException {
Connection connection = dataSource.getConnection();
Connection proxyConnection = dataSource.getConnectionProxy(connection);
assertTrue(proxyConnection instanceof ConnectionProxy);
}

@Test
public void testResetConnection_shouldResetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
dataSource.resetConnection();
Connection resetConnection = dataSource.getConnection();
assertNotSame(connection, resetConnection);
}

@Test
public void testAddInitializer_shouldAddInitializer() {
dataSource.addInitializer(initializer);
assertTrue(dataSource.getInitializerList().contains(initializer));
}

@Test
public void testClose_shouldCloseConnection() throws SQLException {
Connection connection = dataSource.getConnection();
dataSource.close();
verify(mockDataSource).close();
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxy_shouldThrowExceptionWhenConnectionIsOccupied() throws SQLException {
Connection connection = dataSource.getConnection();
dataSource.getConnectionProxy(connection);
dataSource.getConnectionProxy(connection);
}

@Test
public void testGetConnectionProxy_shouldReturnSameProxyConnection() throws SQLException {
Connection connection = dataSource.getConnection();
Connection proxyConnection1 = dataSource.getConnectionProxy(connection);
Connection proxyConnection2 = dataSource.getConnectionProxy(connection);
assertEquals(proxyConnection1, proxyConnection2);
}

@Test
public void testGetConnectionProxy_shouldUnlockLockWhenCloseIsCalled() {
Connection connection = dataSource.getConnection();
dataSource.getConnectionProxy(connection);
ConnectionProxy proxyConnection = (ConnectionProxy) dataSource.getConnectionProxy(connection);
proxyConnection.close();
verify(dataSource.getLock()).unlock();
}

@Test
public void testGetConnectionProxy_shouldReturnUnclosedConnection() throws SQLException {
Connection connection = dataSource.getConnection();
ConnectionProxy proxyConnection = (ConnectionProxy) dataSource.getConnectionProxy(connection);
assertFalse(proxyConnection.isClosed());
}

@Test
public void testGetConnectionProxy_shouldCallInitConnection() throws SQLException {
Connection connection = dataSource.getConnection();
ConnectionProxy proxyConnection = (ConnectionProxy) dataSource.getConnectionProxy(connection);
verify(initializer).init(connection);
}

@Test
public void testGetConnectionProxy_shouldCallOnConnectionReset() throws SQLException {
Connection connection = dataSource.getConnection();
ConnectionProxy proxyConnection = (ConnectionProxy) dataSource.getConnectionProxy(connection);
dataSource.onConnectionReset(connection);
verify(initializer).onConnectionReset(connection);
}

@Test
public void testPrepareConnection_shouldSetAutoCommit() throws SQLException {
Connection connection = Mockito.mock(Connection.class);
when(connection.getAutoCommit()).thenReturn(true);
dataSource.prepareConnection(connection);
verify(connection).setAutoCommit(false);
}

@Test
public void testInnerCreateConnection_shouldCreateConnection() throws SQLException {
Connection connection = dataSource.innerCreateConnection();
assertNotNull(connection);
}

@Test
public void testInnerCreateConnection_shouldCallOnConnectionCreated() throws SQLException {
Connection connection = dataSource.innerCreateConnection();
verify(initializer).onConnectionCreated(connection);
}

@Test
public void testTryLock_shouldLockWhenTimeoutIsZero() {
Lock lock = new ReentrantLock();
assertTrue(dataSource.tryLock(0, TimeUnit.MILLISECONDS));
}

@Test
public void testTryLock_shouldReturnFalseWhenTimeoutIsReached() throws InterruptedException {
Lock lock = new ReentrantLock();
assertFalse(dataSource.tryLock(1, TimeUnit.MILLISECONDS));
}

@Test
public void testMultiThreadedAccess_shouldAllowMultipleThreadsToAccessConnection() throws InterruptedException, SQLException {
List<Connection> connections = new LinkedList<>();
for (int i = 0; i < 10; i++) {
connections.add(dataSource.getConnection());
}
for (Connection connection : connections) {
executorService.submit(() -> {
try {
connection.createStatement().executeQuery("SELECT 1");
} catch (SQLException e) {
fail("Failed to execute query: " + e.getMessage());
}
});
}
executorService.shutdown();
executorService.awaitTermination(1, TimeUnit.SECONDS);
}

}