import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource singleConnectionDataSource;
private ConnectionInitializer connectionInitializer;
private Lock lock;

@Before
public void setup() {
singleConnectionDataSource = new SingleConnectionDataSource(true);
connectionInitializer = new ConnectionInitializerImpl();
lock = new ReentrantLock();
}

@After
public void teardown() throws SQLException {
singleConnectionDataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = singleConnectionDataSource.getConnection();
assertTrue(connection.isValid(singleConnectionDataSource.getLoginTimeout()));
}

@Test
public void testGetConnectionWithUsernameAndPassword() {
assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection("invalidUser", "invalidPassword"));
}

@Test
public void testGetConnectionWithNullUsernameAndPassword() {
assertThrows(SQLException.class, () -> singleConnectionDataSource.getConnection(null, null));
}

@Test
public void testResetConnection() throws SQLException {
singleConnectionDataSource.getConnection();
singleConnectionDataSource.resetConnection();
assertFalse(singleConnectionDataSource.isConnectionOpen());
}

@Test
public void testAddInitializer() {
singleConnectionDataSource.addInitializer(connectionInitializer);
singleConnectionDataSource.initConnection(newConnectionFromDriver("testUser", "testPassword"));
assertEquals(1, singleConnectionDataSource.getInitializerList().size());
}

@Test
public void testGetConnectionProxy() throws SQLException, InterruptedException {
Connection connection = singleConnectionDataSource.getConnection();
InvocationHandler invocationHandler = Proxy.getInvocationHandler(connection);
assertTrue(invocationHandler instanceof CloseIgnoreInvocationHandler);
CloseIgnoreInvocationHandler closeIgnoreInvocationHandler = (CloseIgnoreInvocationHandler) invocationHandler;
closeIgnoreInvocationHandler.lock.lock();
singleConnectionDataSource.getConnectionProxy(connection);
closeIgnoreInvocationHandler.lock.unlock();
}

@Test
public void testTryLock() throws InterruptedException {
lock.lock();
assertFalse(singleConnectionDataSource.tryLock());
lock.unlock();
assertTrue(singleConnectionDataSource.tryLock());
}

@Test
public void testConnectionOccupied() {
assertThrows(ConflictException.class, () -> singleConnectionDataSource.getConnectionProxy(newConnectionFromDriver("testUser", "testPassword")));
}

@Test
public void testOnConnectionReset() {
singleConnectionDataSource.setEventPublisher(event -> {
if (event instanceof ConnectionResetEvent) {
assertEquals(singleConnectionDataSource.getConnection(), ((ConnectionResetEvent) event).getConnection());
}
});
singleConnectionDataSource.getConnection();
singleConnectionDataSource.resetConnection();
}

private Connection newConnectionFromDriver(String username, String password) {
// Implementation not provided
return null;
}
}