import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private Lock lock;
private Connection connection;

@Before
public void setup() throws NoSuchFieldException, IllegalAccessException {
this.dataSource = new SingleConnectionDataSource();
this.lock = new ReentrantLock();
this.connection = Mockito.mock(Connection.class);

Field lockField = SingleConnectionDataSource.class.getDeclaredField("lock");
lockField.setAccessible(true);
lockField.set(dataSource, lock);
}

@Test
public void testGetConnectionWithDefaults() throws SQLException {
Connection result = dataSource.getConnection();

Assert.assertEquals(connection, result);
Assert.assertTrue(lock.isHeldByCurrentThread());
}

@Test
public void testGetConnectionWithAutoCommitTrue() throws SQLException {
dataSource.setAutoCommit(true);
Connection result = dataSource.getConnection();

Assert.assertEquals(connection, result);
Assert.assertTrue(lock.isHeldByCurrentThread());
Assert.assertTrue(result.getAutoCommit());
}

@Test
public void testGetConnectionWithAutoCommitFalse() throws SQLException {
dataSource.setAutoCommit(false);
Connection result = dataSource.getConnection();

Assert.assertEquals(connection, result);
Assert.assertTrue(lock.isHeldByCurrentThread());
Assert.assertFalse(result.getAutoCommit());
}

@Test
public void testGetConnectionWithInvalidCredentials() {
SingleConnectionDataSource dataSourceWithCredentials = new SingleConnectionDataSource("username", "password");

try {
dataSourceWithCredentials.getConnection("invalidUsername", "invalidPassword");
Assert.fail();
} catch (SQLException e) {
Assert.assertEquals("Invalid username or password", e.getMessage());
}
}

@Test
public void testResetConnection() throws SQLException {
dataSource.getConnection();
dataSource.resetConnection();

Assert.assertNull(dataSource.getConnection());
Assert.assertNull(dataSource.lock);

try {
dataSource.getConnection();
Assert.fail();
} catch (SQLException e) {
Assert.assertEquals("Connection was closed or not valid", e.getMessage());
}
}

@Test
public void testAddInitializer() throws SQLException {
ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
dataSource.addInitializer(initializer);

dataSource.getConnection();

Mockito.verify(initializer, Mockito.times(1)).init(connection);
}

@Test
public void testClose() throws SQLException {
dataSource.getConnection();
dataSource.close();

Assert.assertNull(dataSource.getConnection());
Assert.assertNull(dataSource.lock);

try {
dataSource.getConnection();
Assert.fail();
} catch (SQLException e) {
Assert.assertEquals("Connection was closed or not valid", e.getMessage());
}
}

@Test(expected = ConflictException.class)
public void testGetConnectionWithOccupiedConnection()
throws SQLException, ConflictException, InterruptedException {
dataSource.getConnection();
dataSource.getConnection();
}

@Test
public void testGetConnectionProxy() {
Assert.assertEquals(new CloseIgnoreInvocationHandler(connection, lock),
dataSource.getConnectionProxy(connection));
}
}