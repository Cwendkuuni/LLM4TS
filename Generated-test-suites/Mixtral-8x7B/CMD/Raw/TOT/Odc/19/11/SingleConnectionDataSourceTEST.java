import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private ConnectionInitializer initializer;
private DataSource spyDataSource;
private Connection connection;

@Before
public void setUp() throws Exception {
dataSource = new SingleConnectionDataSource();
initializer = mock(ConnectionInitializer.class);
dataSource.addInitializer(initializer);
spyDataSource = spy(dataSource);
connection = mock(Connection.class);
doReturn(connection).when(spyDataSource).newConnectionFromDriver(anyString(), anyString());
}

@After
public void tearDown() throws Exception {
dataSource.close();
}

@Test
public void testGetConnectionWithDefaultValues() throws SQLException {
Connection result = dataSource.getConnection();
assertEquals(connection, result);
}

@Test
public void testGetConnectionWithInitializers() throws SQLException {
dataSource.addInitializer(initializer -> {
// Add your initialization logic here
});
Connection result = dataSource.getConnection();
verify(initializer).init(connection);
assertEquals(connection, result);
}

@Test
public void testGetConnectionWithWrongCredentials() {
assertThrows(SQLException.class, () -> dataSource.getConnection("wrongUsername", "wrongPassword"));
}

@Test
public void testResetConnection() throws SQLException {
spyDataSource.getConnection();
verify(spyDataSource).innerCreateConnection();
spyDataSource.resetConnection();
verify(spyDataSource).innerCreateConnection();
verify(spyDataSource).onConnectionReset(connection);
}

@Test
public void testClose() throws SQLException {
dataSource.getConnection();
dataSource.close();
verify(connection).close();
}

@Test
public void testGetConnectionProxy() throws SQLException {
Connection proxyConnection = dataSource.getConnectionProxy(connection);
assertNotNull(proxyConnection);
assertFalse(proxyConnection.isClosed());
}

@Test
public void testLockTimeout() {
ExecutorService executor = Executors.newSingleThreadExecutor();
executor.submit(() -> {
try {
dataSource.getConnection();
} catch (ConflictException e) {
// Expected
}
});
assertThrows(ConflictException.class, () -> dataSource.getConnection());
executor.shutdown();
try {
executor.awaitTermination(1, TimeUnit.SECONDS);
} catch (InterruptedException e) {
// Ignore
}
}

@Test
public void testCloseIgnoreInvocationHandler() throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
Connection realConnection = mock(Connection.class);
InvocationHandler handler = new CloseIgnoreInvocationHandler(realConnection, new ReentrantLock());
Connection proxyConnection = (Connection) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Connection.class}, handler);
proxyConnection.close();
verify(realConnection, times(0)).close();
assertTrue(proxyConnection.isClosed());
}
}