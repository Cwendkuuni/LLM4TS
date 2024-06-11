import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.TestConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource dataSourceWithInitializer;
private ReentrantLock lock;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource();
dataSourceWithInitializer = new SingleConnectionDataSource();
dataSourceWithInitializer.addInitializer(new TestConnectionInitializer());
lock = new ReentrantLock();
}

@After
public void teardown() throws SQLException {
dataSource.close();
dataSourceWithInitializer.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
dataSource.setUsername("testUser");
dataSource.setPassword("testPassword");
Connection connection = dataSource.getConnection("testUser", "testPassword");
assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
dataSource.setUsername("testUser");
dataSource.setPassword("testPassword");
dataSource.getConnection("wrongUser", "wrongPassword");
}

@Test
public void testResetConnection() throws SQLException {
dataSource.getConnection();
dataSource.resetConnection();
assertNull(dataSource.getConnection());
}

@Test
public void testAddInitializer() throws SQLException {
dataSourceWithInitializer.getConnection();
assertTrue(dataSourceWithInitializer.getInitializerList().get(0).isExecuted());
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithLockConflict() throws ConflictException, SQLException {
dataSource.getConnectionProxy(dataSource.getConnection());
dataSource.getConnectionProxy(dataSource.getConnection());
}

@Test
public void testGetConnectionProxyWithLockTimeout() throws ConflictException, SQLException {
lock.lock();
Connection connection = dataSource.getConnectionProxy(dataSource.getConnection());
lock.unlock();
assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void testInnerCreateConnectionWithConnectionAlreadyExists() throws SQLException {
dataSource.getConnection();
dataSource.innerCreateConnection();
}

@Test
public void testInitializeConnection() throws SQLException {
dataSource.innerCreateConnection();
dataSource.initConnection(dataSource.getConnection());
assertTrue(dataSource.getInitializerList().get(0).isExecuted());
}

@Test
public void testOnConnectionReset() throws SQLException {
dataSource.onConnectionReset(dataSource.getConnection());
assertFalse(dataSource.getInitializerList().get(0).isExecuted());
}
}