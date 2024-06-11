import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private Connection connection;
private ExecutorService executorService = Executors.newSingleThreadExecutor();

@Before
public void setup() throws SQLException {
dataSource = new SingleConnectionDataSource();
connection = DriverManager.getConnection("jdbc:h2:mem:test", "user", "password");
dataSource.addInitializer(new ConnectionInitializer() {
@Override
public void init(Connection connection) throws Exception {
// Initialize connection
}
});
}

@After
public void teardown() throws SQLException {
if (connection != null) {
connection.close();
}
executorService.shutdown();
try {
executorService.awaitTermination(1, TimeUnit.SECONDS);
} catch (InterruptedException e) {
e.printStackTrace();
}
}

@Test
public void testGetConnection() throws SQLException {
Connection result = dataSource.getConnection();
assertFalse(result.isClosed());
}

@Test
public void testGetConnectionWithInvalidUsernameAndPassword() {
assertThrows(SQLException.class, () -> dataSource.getConnection("invalid_user", "invalid_password"));
}

@Test
public void testResetConnection() throws SQLException {
dataSource.getConnection();
dataSource.resetConnection();
assertThrows(SQLException.class, dataSource::getConnection);
}

@Test
public void testGetConnectionProxy() {
InvocationHandler handler = mock(InvocationHandler.class);
Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, handler);
assertTrue(proxy instanceof Connection);
}

@Test
public void testCloseIgnoreInvocationHandler() {
InvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, new ReentrantLock());
Object proxy = Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[] { Connection.class }, handler);
proxy.getClass().getMethod("close").invoke(proxy);
assertTrue(connection.isClosed());
}

@Test
public void testLock() throws SQLException, InterruptedException {
dataSource.getConnection();
assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));
}

@Test
public void testOnConnectionResetEvent() throws SQLException {
EventPublisher eventPublisher = mock(EventPublisher.class);
dataSource.setEventPublisher(eventPublisher);
dataSource.getConnection();
dataSource.resetConnection();
Mockito.verify(eventPublisher).publishEvent(Mockito.any(SingleConnectionDataSource.ConnectionResetEvent.class));
}

@Test
public void testConcurrentGetConnection() throws SQLException, InterruptedException {
List<Connection> connections = Arrays.asList(DriverManager.getConnection("jdbc:h2:mem:test", "user", "password"),
DriverManager.getConnection("jdbc:h2:mem:test", "user", "password"));
dataSource.getConnection();
dataSource.addInitializer(new ConnectionInitializer() {
@Override
public void init(Connection connection) throws Exception {
connection.getClass().getMethod("close").invoke(connection);
}
});
executorService.submit(() -> {
try {
dataSource.getConnectionProxy(connections.get(0));
} catch (SQLException e) {
e.printStackTrace();
}
});
executorService.submit(() -> {
try {
dataSource.getConnectionProxy(connections.get(1));
} catch (SQLException e) {
e.printStackTrace();
}
});
executorService.awaitTermination(1, TimeUnit.SECONDS);
assertEquals(ErrorCodes.ConnectionOccupied, dataSource.getConnectionProxy(connection).getErrorCode());
}
}