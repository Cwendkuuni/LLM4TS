import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.CloseIgnoreInvocationHandler;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private Connection connection;
private Lock lock;

@Before
public void setup() throws SQLException {
dataSource = new SingleConnectionDataSource();
connection = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
lock = new ReentrantLock();
CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class},
handler);
dataSource.setConnection(proxyConnection);
}

@After
public void teardown() throws SQLException {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection actualConnection = dataSource.getConnection();
assertEquals(connection, actualConnection);
}

@Test
public void testGetConnectionWithAutoCommit() throws SQLException {
dataSource.setAutoCommit(true);
Connection actualConnection = dataSource.getConnection();
assertTrue(actualConnection.getAutoCommit());
}

@Test
public void testGetConnectionWithTransaction() throws SQLException {
dataSource.setAutoCommit(false);
Connection actualConnection = dataSource.getConnection();
assertFalse(actualConnection.getAutoCommit());
}

@Test(expected = ConflictException.class)
public void testGetConnectionWithLockTimeout() throws SQLException, InterruptedException {
dataSource.setTimeOutMillis(100);
dataSource.getConnection();
dataSource.getConnection();
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();
verify(connection, times(1)).close();
}

@Test
public void testAddInitializer() throws SQLException {
ConnectionInitializer initializer = mock(ConnectionInitializer.class);
dataSource.addInitializer(initializer);
dataSource.getConnection();
verify(initializer, times(1)).init(connection);
}

@Test
public void testClose() throws SQLException {
dataSource.close();
verify(connection, times(1)).close();
}

}