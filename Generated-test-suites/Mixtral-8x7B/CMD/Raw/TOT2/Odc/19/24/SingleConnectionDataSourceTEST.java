import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SingleConnectionDataSourceTest {

// Initialize the test data source
private SingleConnectionDataSource dataSource;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource();
}

@After
public void teardown() {
try {
dataSource.close();
} catch (SQLException e) {
// Ignore
}
}

// Test case for getConnection() method
@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertEquals(true, connection.isClosed() == false);
}

// Test case for getConnection(username, password) method
@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
String username = "testUser";
String password = "testPassword";
dataSource.setUsername(username);
dataSource.setPassword(password);
Connection connection = dataSource.getConnection(username, password);
assertEquals(true, connection.isClosed() == false);
}

// Test case for resetConnection() method
@Test
public void testResetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
dataSource.resetConnection();
assertThrows(SQLException.class, () -> connection.createStatement().executeQuery("SELECT 1"));
}

// Test case for addInitializer() method
@Test
public void testAddInitializer() throws Exception {
ConnectionInitializer initializer = new ConnectionInitializer() {
@Override
public void init(Connection connection) throws Exception {
connection.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS test_table (id INT)");
}
};
dataSource.addInitializer(initializer);
dataSource.getConnection().createStatement().executeQuery("SELECT 1 FROM test_table");
}

// Test case for close() method
@Test
public void testClose() throws SQLException {
Connection connection = dataSource.getConnection();
dataSource.close();
assertThrows(SQLException.class, () -> connection.createStatement().executeQuery("SELECT 1"));
}

// Test case for getConnectionProxy() method
@Test
public void testGetConnectionProxy() {
ReentrantLock lock = new ReentrantLock();
SingleConnectionDataSource dataSourceWithLock = new SingleConnectionDataSource(false);
dataSourceWithLock.setLock(lock);
Connection connection = dataSourceWithLock.getConnectionProxy(dataSourceWithLock.innerCreateConnection());
assertEquals(true, connection instanceof SingleConnectionDataSource.CloseIgnoreInvocationHandler);
}

// Test case for innerCreateConnection() method
@Test
public void testInnerCreateConnection() throws Exception {
Connection connection = dataSource.innerCreateConnection();
assertEquals(true, connection.isClosed() == false);
}

// Test case for CloseIgnoreInvocationHandler class
@Test
public void testCloseIgnoreInvocationHandler() {
SingleConnectionDataSource.CloseIgnoreInvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(null, null);
assertEquals(false, handler.invoke(null, Method.class.getMethod("equals", Object.class), new Object[] {null}));
assertEquals(false, handler.invoke(null, Method.class.getMethod("hashCode"), null));
assertEquals(true, handler.invoke(null, Method.class.getMethod("isWrapperFor", Class.class), new Object[] {Connection.class}));
assertEquals(null, handler.invoke(null, Method.class.getMethod("close"), null));
assertEquals(false, handler.invoke(null, Method.class.getMethod("isClosed"), null));
}

// Test case for edge case: Timeout while acquiring lock in getConnectionProxy()
@Test(expected = ConflictException.class)
public void testGetConnectionProxyTimeout() throws Exception {
ReentrantLock lock = new ReentrantLock();
SingleConnectionDataSource dataSourceWithLock = new SingleConnectionDataSource(false);
dataSourceWithLock.setLock(lock);
// Sleep for a long time to simulate a timeout
Thread.sleep(dataSourceWithLock.getTimeOutMillis() + 100);
dataSourceWithLock.getConnectionProxy(dataSourceWithLock.innerCreateConnection());
}

// Test case for exception handling: getConnection() when connection is not initialized
@Test
public void testGetConnectionNoConnection() {
assertThrows(IllegalStateException.class, dataSource::getConnection);
}

}