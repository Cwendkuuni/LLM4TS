import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private ConnectionInitializer connectionInitializer;
private EventPublisher eventPublisher;
private DataSource spyDataSource;
private Connection connection;
private ExecutorService executorService;

@Before
public void setUp() throws SQLException {
dataSource = new SingleConnectionDataSource();
connectionInitializer = Mockito.mock(ConnectionInitializer.class);
dataSource.addInitializer(connectionInitializer);
eventPublisher = Mockito.mock(EventPublisher.class);
dataSource.setEventPublisher(eventPublisher);
spyDataSource = Mockito.spy(dataSource);
connection = DriverManager.getConnection("jdbc:h2:mem:test", "user", "password");
executorService = Executors.newSingleThreadExecutor();
}

@After
public void tearDown() throws InterruptedException {
executorService.shutdownNow();
if (dataSource != null) {
dataSource.close();
}
if (connection != null) {
connection.close();
}
}

@Test
public void testGetConnection() throws SQLException {
Connection result = spyDataSource.getConnection();
verify(spyDataSource).innerCreateConnection();
assertEquals(spyDataSource.getConnectionProxy(connection), result);
}

@Test
public void testGetConnectionWithInvalidUsernameAndPassword() {
assertThrows(SQLException.class, () -> spyDataSource.getConnection("invalidUser", "invalidPassword"));
}

@Test
public void testResetConnection() throws SQLException {
spyDataSource.resetConnection();
verify(spyDataSource).innerCreateConnection();
verify(connectionInitializer).init(connection);
}

@Test
public void testAddInitializer() {
spyDataSource.addInitializer(connectionInitializer);
verify(spyDataSource).initializerList.add(connectionInitializer);
}

@Test
public void testClose() throws SQLException {
spyDataSource.close();
verify(connection).close();
}

@Test
public void testPrepareConnection() throws SQLException {
Boolean autoCommit = true;
spyDataSource.setAutoCommit(autoCommit);
spyDataSource.prepareConnection(connection);
assertEquals(autoCommit, connection.getAutoCommit());
}

@Test
public void testInitConnection() throws SQLException {
spyDataSource.initConnection(connection);
verify(connectionInitializer).init(connection);
verify(spyDataSource).onConnectionCreated(connection);
}

@Test
public void testOnConnectionReset() {
spyDataSource.onConnectionReset(connection);
verify(eventPublisher).publishEvent(any(ConnectionResetEvent.class));
}

@Test
public void testInnerCreateConnection() throws SQLException {
spyDataSource.innerCreateConnection();
verify(connection).close();
verify(spyDataSource).prepareConnection(connection);
verify(spyDataSource).onConnectionCreated(connection);
}

@Test
public void testGetConnectionProxy() {
ConnectionProxy connectionProxy = (ConnectionProxy) spyDataSource.getConnectionProxy(connection);
assertFalse(connectionProxy.equals(spyDataSource));
assertEquals(System.identityHashCode(spyDataSource), connectionProxy.hashCode());
assertThrows(ConflictException.class, () -> connectionProxy.close());
assertFalse(connectionProxy.isClosed());
}

@Test
public void testCloseIgnoreInvocationHandlerCloseMethod() throws Throwable {
CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, new ReentrantLock());
Method closeMethod = Connection.class.getDeclaredMethod("close");
closeMethod.setAccessible(true);
handler.invoke(null, connection, closeMethod, new Object[0]);
verify(connection).close();
}

@Test
public void testGetConnectionProxyLockInterruption() throws InterruptedException, SQLException {
ConnectionProxy connectionProxy = (ConnectionProxy) spyDataSource.getConnectionProxy(connection);
executorService.submit(() -> {
try {
connectionProxy.close();
} catch (Throwable e) {
// Ignore
}
});
Thread.sleep(100);
assertThrows(ConflictException.class, () -> spyDataSource.getConnection());
}

@Test
public void testGetConnectionProxyLockTimeout() throws InterruptedException, SQLException {
ConnectionProxy connectionProxy = (ConnectionProxy) spyDataSource.getConnectionProxy(connection);
executorService.submit(() -> {
Thread.sleep(spyDataSource.getTimeOutMillis() + 100);
try {
connectionProxy.close();
} catch (Throwable e) {
// Ignore
}
});
Thread.sleep(100);
assertThrows(ConflictException.class, () -> spyDataSource.getConnection());
}
}