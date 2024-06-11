import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private ConnectionInitializer connectionInitializer;
private Lock lock;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource();
connectionInitializer = new ConnectionInitializer() {
@Override
public void init(Connection connection) throws SQLException {
// Do nothing
}
};
dataSource.addInitializer(connectionInitializer);
lock = new ReentrantLock();
}

@After
public void teardown() throws SQLException {
dataSource.close();
}

@Test
public void testGetConnectionWithValidParameters() throws SQLException {
Connection connection = dataSource.getConnection();
Assert.assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidParameters() throws SQLException {
dataSource.setUsername("invalid_username");
Connection connection = dataSource.getConnection();
}

@Test
public void testResetConnection() throws SQLException {
dataSource.getConnection();
dataSource.resetConnection();
Assert.assertNull(dataSource.getConnection());
}

@Test
public void testGetConnectionProxy() {
Assert.assertNotNull(dataSource.getConnectionProxy(dataSource.getConnection()));
}

@Test
public void testTryLock() {
Assert.assertTrue(dataSource.tryLock());
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithLockConflict() throws SQLException, InterruptedException {
dataSource.getConnectionProxy(dataSource.getConnection());
dataSource.tryLock();
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithTimeout() throws SQLException, InterruptedException {
dataSource.getConnectionProxy(dataSource.getConnection());
Thread.sleep(dataSource.getTimeOutMillis() + 100);
dataSource.getConnectionProxy(dataSource.getConnection());
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithInterruptedException() throws SQLException, InterruptedException {
dataSource.getConnectionProxy(dataSource.getConnection());
Thread.sleep(dataSource.getTimeOutMillis() / 2);
dataSource.tryLock();
}
}