import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private Connection connectionMock;
private EventPublisher eventPublisherMock;
private ExecutorService singleThreadExecutor;

@Before
public void setUp() throws Exception {
connectionMock = mock(Connection.class);
eventPublisherMock = mock(EventPublisher.class);
dataSource = new SingleConnectionDataSource(true);
dataSource.setEventPublisher(eventPublisherMock);
dataSource.addInitializer(new ConnectionInitializerImpl());
Field connectionField = SingleConnectionDataSource.class.getDeclaredField("connection");
connectionField.setAccessible(true);
connectionField.set(dataSource, connectionMock);
singleThreadExecutor = Executors.newSingleThreadExecutor();
}

@After
public void tearDown() throws Exception {
singleThreadExecutor.shutdownNow();
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
verify(connectionMock, times(1)).setAutoCommit(anyBoolean());
}

@Test
public void testGetConnectionWithWrongCredentials() throws SQLException {
assertThrows(SQLException.class, () -> dataSource.getConnection("wrongUser", "wrongPassword"));
verify(connectionMock, never()).close();
}

@Test
public void testResetConnection() throws SQLException, InterruptedException {
when(connectionMock.isClosed()).thenReturn(false);
dataSource.getConnection();
verify(connectionMock, times(1)).isClosed();
dataSource.resetConnection();
verify(connectionMock, times(1)).close();
}

@Test
public void testAddInitializer() throws SQLException {
dataSource.addInitializer(new ConnectionInitializerImpl());
dataSource.getConnection();
verify(connectionMock, times(1)).setAutoCommit(anyBoolean());
}

@Test
public void testMultipleGetConnection() throws SQLException, InterruptedException {
singleThreadExecutor.submit(() -> {
try {
dataSource.getConnection();
} catch (SQLException e) {
throw new RuntimeException(e);
}
});
Thread.sleep(100);
Connection connection = dataSource.getConnection();
assertNotNull(connection);
verify(connectionMock, times(2)).setAutoCommit(anyBoolean());
}

@Test
public void testClose() throws SQLException {
dataSource.getConnection();
dataSource.close();
verify(connectionMock, times(1)).close();
}

@Test
public void testConnectionProxyClose() throws SQLException {
dataSource.getConnection();
dataSource.close();
verify(connectionMock, times(1)).close();
}

@Test
public void testTryLockTimeout() throws InterruptedException {
Field lockField = SingleConnectionDataSource.class.getDeclaredField("lock");
lockField.setAccessible(true);
Lock lock = (Lock) lockField.get(dataSource);
assertFalse(lock.tryLock(1, TimeUnit.MILLISECONDS));
}

@Test
public void testConnectionProxyUnlock() throws InterruptedException {
dataSource.getConnection();
Field lockField = SingleConnectionDataSource.class.getDeclaredField("lock");
lockField.setAccessible(true);
Lock lock = (Lock) lockField.get(dataSource);
lock.unlock();
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithLockTimeout() throws SQLException, InterruptedException {
dataSource.getConnection();
Field lockField = SingleConnectionDataSource.class.getDeclaredField("lock");
lockField.setAccessible(true);
Lock lock = (Lock) lockField.get(dataSource);
lock.lock();
dataSource.getConnectionProxy(connectionMock);
}

@Test
public void testGetConnectionProxyWithLockUnlock() throws SQLException, InterruptedException {
dataSource.getConnection();
Field lockField = SingleConnectionDataSource.class.getDeclaredField("lock");
lockField.setAccessible(true);
Lock lock = (Lock) lockField.get(dataSource);
lock.lock();
dataSource.getConnectionProxy(connectionMock);
lock.unlock();
}

@Test
public void testOnConnectionResetEventPublished() throws Exception {
dataSource.getConnection();
Field lockField = SingleConnectionDataSource.class.getDeclaredField("lock");
lockField.setAccessible(true);
Lock lock = (Lock) lockField.get(dataSource);
lock.lock();
dataSource.resetConnection();
verify(eventPublisherMock, times(1)).publishEvent(any());
}

}