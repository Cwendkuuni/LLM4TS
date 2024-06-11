import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.locks.Lock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;

@Before
public void setUp() {
dataSource = new SingleConnectionDataSource();
}

@After
public void tearDown() throws SQLException {
if (dataSource != null) {
dataSource.close();
}
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertFalse(connection.isClosed());
dataSource.close();
assertThrows(SQLException.class, () -> connection.createStatement());
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
String invalidUsername = "invalid";
String invalidPassword = "invalid";
assertThrows(SQLException.class, () -> dataSource.getConnection(invalidUsername, invalidPassword));
}

@Test
public void testResetConnection() throws SQLException {
dataSource.getConnection();
dataSource.resetConnection();
assertThrows(SQLException.class, dataSource::getConnection);
}

@Test
public void testAddInitializer() throws SQLException {
ConnectionInitializer initializer = new ConnectionInitializer() {
@Override
public void init(Connection connection) throws SQLException {
connection.setAutoCommit(false);
}
};
dataSource.addInitializer(initializer);
dataSource.getConnection();
Connection connection = dataSource.getConnection();
assertTrue(connection.getAutoCommit() == false);
}

@Test
public void testGetConnectionProxy() throws SQLException {
Lock lock = new ReentrantLock();
Connection connection = dataSource.getConnectionProxy(dataSource.innerCreateConnection());
assertTrue(lock.tryLock(Duration.ofMillis(1000)));
dataSource.close();
lock.unlock();
}

@Test
public void testClose() throws SQLException {
dataSource.getConnection();
dataSource.close();
assertThrows(SQLException.class, dataSource::getConnection);
}

@Test
public void testTryLock() throws InterruptedException {
Lock lock = new ReentrantLock();
assertTrue(lock.tryLock());
lock.unlock();
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithLockConflict()
throws SQLException, InterruptedException {
Lock lock = new ReentrantLock();
Connection connection1 = dataSource.getConnectionProxy(dataSource.innerCreateConnection());
Connection connection2 = dataSource.getConnectionProxy(dataSource.innerCreateConnection());
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithTimeout()
throws SQLException, InterruptedException {
Lock lock = new ReentrantLock();
Thread.sleep(1000);
Connection connection = dataSource.getConnectionProxy(dataSource.innerCreateConnection());
}
}