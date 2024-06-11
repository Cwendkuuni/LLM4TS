import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;

@Before
public void setUp() {
dataSource = new SingleConnectionDataSource(true);
dataSource.addInitializer(new ConnectionInitializerImpl());
}

@After
public void tearDown() throws SQLException {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertTrue(connection.isValid(dataSource.getLoginTimeout()));
}

@Test
public void testGetConnectionWithWrongUsernameAndPassword() {
assertThrows(SQLException.class, () -> dataSource.getConnection("wrongUser", "wrongPassword"));
}

@Test
public void testResetConnection() throws SQLException {
Connection originalConnection = dataSource.getConnection();
dataSource.resetConnection();
Connection newConnection = dataSource.getConnection();
assertFalse(originalConnection.equals(newConnection));
}

@Test
public void testGetConnectionProxy() throws SQLException {
Connection originalConnection = dataSource.getConnection();
ReentrantLock lock = new ReentrantLock();
Connection connectionProxy = dataSource.getConnectionProxy(originalConnection, lock);
assertEquals(originalConnection, connectionProxy);
lock.unlock();
}

@Test
public void testGetConnectionProxyWithLockInterruption() throws SQLException, InterruptedException {
Connection originalConnection = dataSource.getConnection();
ReentrantLock lock = new ReentrantLock();
Thread testThread = new Thread(() -> {
try {
dataSource.getConnectionProxy(originalConnection, lock);
} catch (ConflictException e) {
// Expected behavior, no action needed
}
});
testThread.start();
Thread.sleep(100);
lock.lock();
testThread.join();
}

@Test
public void testPrepareConnection() throws SQLException {
Connection connection = dataSource.newConnectionFromDriver("testUser", "testPassword");
dataSource.prepareConnection(connection);
assertEquals(dataSource.getAutoCommit(), connection.getAutoCommit());
}

@Test
public void testInitConnection() throws SQLException {
Connection connection = dataSource.newConnectionFromDriver("testUser", "testPassword");
dataSource.initConnection(connection);
// Custom logic for testing ConnectionInitializer implementation
}

@Test
public void testOnConnectionCreated() throws SQLException {
Connection connection = dataSource.newConnectionFromDriver("testUser", "testPassword");
dataSource.onConnectionCreated(connection);
// Check for debug log if enabled
}

@Test
public void testOnConnectionReset() throws SQLException {
Connection connection = dataSource.newConnectionFromDriver("testUser", "testPassword");
dataSource.onConnectionReset(connection);
// Check for event publication
}

@Test
public void testInnerCreateConnection() throws SQLException {
Connection connection = dataSource.innerCreateConnection();
assertTrue(connection.isValid(dataSource.getLoginTimeout()));
}

}