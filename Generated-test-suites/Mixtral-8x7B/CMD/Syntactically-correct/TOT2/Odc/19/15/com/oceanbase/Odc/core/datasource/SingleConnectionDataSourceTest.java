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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource dataSourceWithoutInitializer;
private ConnectionInitializer initializer;
private ReentrantLock lock;

@Before
public void setup() {
initializer = new ConnectionInitializerImpl();
lock = new ReentrantLock();
dataSource = new SingleConnectionDataSource(true);
dataSource.addInitializer(initializer);
dataSourceWithoutInitializer = new SingleConnectionDataSource(true);
}

@After
public void teardown() throws SQLException {
dataSource.close();
dataSourceWithoutInitializer.close();
}

@Test
public void testGetConnection_ShouldReturnConnectionInstance() throws SQLException {
Connection connection = dataSource.getConnection();
assertFalse(connection.isClosed());
}

@Test
public void testGetConnectionWithUsernameAndPassword_ShouldThrowExceptionForInvalidCredentials() {
assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUsername", "invalidPassword"));
}

@Test
public void testGetConnectionWithUsernameAndPassword_ShouldReturnConnectionInstanceForValidCredentials()
throws SQLException {
dataSource.setUsername("username");
dataSource.setPassword("password");
Connection connection = dataSource.getConnection("username", "password");
assertFalse(connection.isClosed());
}

@Test
public void testResetConnection_ShouldResetConnectionAndReturnNewInstance() throws SQLException {
Connection originalConnection = dataSource.getConnection();
dataSource.resetConnection();
Connection newConnection = dataSource.getConnection();
assertFalse(originalConnection.isClosed());
assertFalse(newConnection.isClosed());
assertNotSame(originalConnection, newConnection);
}

@Test
public void testAddInitializer_ShouldExecuteInitializerWhenConnectionCreated() throws SQLException {
dataSource.addInitializer(initializer);
dataSource.getConnection();
assertTrue(initializer.isExecuted());
}

@Test
public void testAddInitializer_ShouldNotExecuteInitializerWhenConnectionNotCreated() {
assertFalse(initializer.isExecuted());
}

@Test
public void testClose_ShouldCloseConnection() throws SQLException {
Connection connection = dataSource.getConnection();
dataSource.close();
assertTrue(connection.isClosed());
}

@Test
public void testClose_ShouldNotCloseConnectionWhenDataSourceWithoutInitializer() throws SQLException {
Connection connection = dataSourceWithoutInitializer.getConnection();
dataSourceWithoutInitializer.close();
assertFalse(connection.isClosed());
}

@Test
public void testGetConnectionProxy_ShouldLockConnectionAndReturnProxyInstance() throws SQLException {
Connection connection = dataSource.getConnection();
dataSource.getConnectionProxy(connection);
assertTrue(lock.isHeldByCurrentThread());
}

@Test
public void testGetConnectionProxy_ShouldThrowExceptionWhenLockIsAlreadyHeld() {
dataSource.getConnection();
assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(dataSource.getConnection()));
}

}