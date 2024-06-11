import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectionInitializer.TestConnectionInitializer;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource testDataSource;
private Connection connection;
private EventPublisher eventPublisher;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource();
dataSource.addInitializer(new TestConnectionInitializer());
eventPublisher = Mockito.mock(EventPublisher.class);
dataSource.setEventPublisher(eventPublisher);
connection = Mockito.mock(Connection.class);
dataSource.setConnection(connection);
}

@After
public void teardown() throws SQLException {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection result = dataSource.getConnection();
Assert.assertSame(connection, result);
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
String username = "testUser";
String password = "testPassword";
dataSource.setUsername(username);
dataSource.setPassword(password);
Connection result = dataSource.getConnection(username, password);
Assert.assertSame(connection, result);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithWrongUsernameAndPassword() throws SQLException {
String username = "wrongUser";
String password = "wrongPassword";
dataSource.setUsername(username);
dataSource.setPassword(password);
dataSource.getConnection(username, password);
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();
Mockito.verify(connection, Mockito.times(1)).close();
connection = Mockito.mock(Connection.class);
dataSource.setConnection(connection);
}

@Test
public void testAddInitializer() throws SQLException {
ConnectionInitializer initializer = new TestConnectionInitializer();
dataSource.addInitializer(initializer);
dataSource.initConnection(connection);
Mockito.verify(initializer, Mockito.times(1)).init(connection);
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyConflict() throws SQLException, InterruptedException {
ExecutorService executorService = Executors.newSingleThreadExecutor();
executorService.execute(() -> {
try {
dataSource.getConnection();
} catch (SQLException e) {
throw new RuntimeException(e);
}
});
 TimeUnit.MILLISECONDS.sleep(dataSource.getTimeOutMillis() + 100);
dataSource.getConnection();
}

@Test
public void testOnConnectionReset() {
dataSource.onConnectionReset(connection);
Mockito.verify(eventPublisher, Mockito.times(1))
.publishEvent(Mockito.argThat(arg -> arg instanceof ConnectionResetEvent));
}

@Test
public void testInnerCreateConnection() throws SQLException {
dataSource.innerCreateConnection();
Mockito.verify(connection, Mockito.times(1)).close();
connection = Mockito.mock(Connection.class);
dataSource.setConnection(connection);
}

@Test
public void testCloseIgnoreInvocationHandlerCloseMethod() throws SQLException {
InvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, new ReentrantLock());
Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class}, handler);
proxyConnection.close();
Mockito.verify(connection, Mockito.times(1)).close();
}

@Test
public void testCloseIgnoreInvocationHandlerIsClosedMethod() throws SQLException {
InvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, new ReentrantLock());
Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class}, handler);
Assert.assertFalse(proxyConnection.isClosed());
}

@Test
public void testCloseIgnoreInvocationHandlerOtherMethods() throws SQLException {
InvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, new ReentrantLock());
Connection proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class}, handler);
Assert.assertSame(proxyConnection, Proxy.getProxyClass(Connection.class.getClassLoader(), new Class[] {Connection.class})
.cast(proxyConnection));
}
}