import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource dataSourceWithEventPublisher;
private Lock lock;
private ConnectionInitializer connectionInitializer;
private ConnectionInitializer connectionInitializer2;
private ConnectionInitializer connectionInitializer3;
private ConnectionInitializer connectionInitializer4;
private Connection connection;
private EventPublisher eventPublisher;

@Before
public void setUp() {
connectionInitializer = new ConnectionInitializerImpl();
connectionInitializer2 = new ConnectionInitializerImpl();
connectionInitializer3 = new ConnectionInitializerImpl();
connectionInitializer4 = new ConnectionInitializerImpl();
initializerListSetup();
lock = new ReentrantLock();
connection = mock(Connection.class);
eventPublisher = mock(EventPublisher.class);
dataSource = new SingleConnectionDataSource(false, eventPublisher);
dataSource.addInitializer(connectionInitializer);
dataSource.addInitializer(connectionInitializer2);
dataSource.addInitializer(connectionInitializer3);
dataSource.addInitializer(connectionInitializer4);
}

@After
public void tearDown() {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
dataSource.setConnection(connection);
dataSource.setAutoCommit(true);
Connection result = dataSource.getConnection();
verify(connection, times(1)).setAutoCommit(true);
assertTrue(result instanceof Proxy);
}

@Test
public void testGetConnectionWithInvalidUsernameAndPassword() {
assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPass"));
}

@Test
public void testGetConnectionWithLock() throws SQLException, InterruptedException {
dataSource.setConnection(connection);
dataSource.setAutoCommit(true);
Thread thread1 = new Thread(() -> {
try {
dataSource.getConnection();
} catch (SQLException e) {
throw new RuntimeException(e);
}
});
Thread thread2 = new Thread(() -> {
try {
dataSource.getConnection();
} catch (SQLException e) {
throw new RuntimeException(e);
}
});
thread1.start();
thread2.start();
thread1.join();
thread2.join();
}

@Test
public void testResetConnection() throws SQLException {
dataSource.setConnection(connection);
dataSource.resetConnection();
verify(connection, times(1)).close();
}

@Test
public void testAddInitializer() {
dataSource.addInitializer(connectionInitializer4);
List<ConnectionInitializer> initializerList = dataSource.getInitializerList();
assertEquals(4, initializerList.size());
assertTrue(initializerList.contains(connectionInitializer4));
}

@Test
public void testInitConnection() throws SQLException {
dataSource.setConnection(connection);
dataSource.initConnection(connection);
verify(connectionInitializer, times(1)).init(connection);
verify(connectionInitializer2, times(1)).init(connection);
verify(connectionInitializer3, times(1)).init(connection);
verify(connectionInitializer4, times(1)).init(connection);
}

@Test
public void testClose() throws SQLException {
dataSource.setConnection(connection);
dataSource.close();
verify(connection, times(1)).close();
}

@Test
public void testInnerCreateConnection() throws SQLException {
dataSourceWithEventPublisher = new SingleConnectionDataSource(false, eventPublisher);
dataSourceWithEventPublisher.setConnection(connection);
dataSourceWithEventPublisher.innerCreateConnection();
verify(connectionInitializer, times(1)).init(connection);
verify(connectionInitializer2, times(1)).init(connection);
verify(connectionInitializer3, times(1)).init(connection);
verify(connectionInitializer4, times(1)).init(connection);
verify(eventPublisher, times(1)).publishEvent(Mockito.any(ConnectionResetEvent.class));
}

@Test
public void testGetConnectionProxy() throws SQLException {
dataSource.setConnection(connection);
dataSource.setLock(lock);
Connection result = dataSource.getConnectionProxy(connection);
assertTrue(result instanceof Proxy);
assertFalse(lock.isHeldByCurrentThread());
}

@Test
public void testGetConnectionProxyWithLockInterruptedException() {
Lock lock = Mockito.mock(Lock.class);
SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false, eventPublisher);
dataSource.setLock(lock);
assertThrows(RuntimeException.class, () -> dataSource.getConnectionProxy(connection));
}

@Test
public void testGetConnectionProxyWithLockTimeout() throws SQLException, InterruptedException {
Lock lock = Mockito.mock(Lock.class);
SingleConnectionDataSource dataSource = new SingleConnectionDataSource(false, eventPublisher);
dataSource.setLock(lock);
Mockito.when(lock.tryLock(1, TimeUnit.MILLISECONDS)).thenReturn(false);
assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));
}

private void initializerListSetup() {
List<ConnectionInitializer> initializerList = new LinkedList<>();
initializerList.add(connectionInitializer);
initializerList.add(connectionInitializer2);
initializerList.add(connectionInitializer3);
initializerList.add(connectionInitializer4);
}

}