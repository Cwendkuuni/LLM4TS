import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.ODCException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource wrongCredentialDataSource;
private ConnectionInitializer initializer;

@Before
public void setUp() throws Exception {
dataSource = new SingleConnectionDataSource();
dataSource.setUsername("testUser");
dataSource.setPassword("testPassword");
dataSource.addInitializer(initializer = new ConnectionInitializerImpl());

wrongCredentialDataSource = new SingleConnectionDataSource();
wrongCredentialDataSource.setUsername("wrongUser");
wrongCredentialDataSource.setPassword("wrongPassword");
}

@After
public void tearDown() throws Exception {
if (dataSource != null) {
dataSource.close();
}
if (wrongCredentialDataSource != null) {
wrongCredentialDataSource.close();
}
}

@Test
public void testGetConnectionWithCorrectCredentials() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithWrongCredentials() throws SQLException {
wrongCredentialDataSource.getConnection();
}

@Test
public void testGetConnectionProxy() throws SQLException {
Connection originalConnection = dataSource.getConnection();
Connection proxyConnection = getConnectionProxy(dataSource, originalConnection);
assertNotSame(originalConnection, proxyConnection);
assertEquals(originalConnection, ((InvocationHandler) Proxy.getInvocationHandler(proxyConnection)).invoke(proxyConnection, new Object[]{"isValid", new Object[0]}));
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithOccupiedConnection() throws SQLException, InterruptedException {
Connection originalConnection = dataSource.getConnection();
Connection proxyConnection1 = getConnectionProxy(dataSource, originalConnection);
Connection proxyConnection2 = getConnectionProxy(dataSource, originalConnection);
assertNotSame(proxyConnection1, proxyConnection2);
}

@Test
public void testResetConnection() throws Exception {
Connection originalConnection = dataSource.getConnection();
dataSource.resetConnection();
assertThrows(SQLException.class, () -> originalConnection.createStatement());
}

@Test
public void testClose() throws Exception {
Connection connection = dataSource.getConnection();
dataSource.close();
assertThrows(IllegalStateException.class, () -> connection.createStatement());
}

@Test
public void testAutoCommit() throws SQLException {
Connection connection = dataSource.getConnection();
dataSource.setAutoCommit(true);
assertTrue(connection.getAutoCommit());
dataSource.setAutoCommit(false);
assertFalse(connection.getAutoCommit());
}

@Test
public void testInitialization() throws Exception {
dataSource.getConnection();
assertTrue(initializer.isConnectionInitialized());
}

@Test
public void testAddInitializer() throws Exception {
ConnectionInitializer initializer2 = new ConnectionInitializerImpl();
dataSource.addInitializer(initializer2);
dataSource.getConnection();
assertTrue(initializer.isConnectionInitialized());
assertTrue(initializer2.isConnectionInitialized());
}

private Connection getConnectionProxy(SingleConnectionDataSource dataSource, Connection connection)
throws SQLException {
return dataSource.getConnectionProxy(connection);
}

private static class ConnectionInitializerImpl implements ConnectionInitializer {
private boolean isConnectionInitialized;
@Override
public void init(Connection connection) throws Exception {
isConnectionInitialized = true;
}
@Override
public boolean isConnectionInitialized() {
return isConnectionInitialized;
}
}

private static class TestExecutor implements Runnable {
private SingleConnectionDataSource dataSource;
private int numberOfThreads;
public TestExecutor(SingleConnectionDataSource dataSource, int numberOfThreads) {
this.dataSource = dataSource;
this.numberOfThreads = numberOfThreads;
}
@Override
public void run() {
ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
for (int i = 0; i < numberOfThreads; i++) {
executor.submit(() -> {
try {
Connection connection = dataSource.getConnection();
connection.close();
} catch (Exception e) {
throw new RuntimeException(e);
}
});
}
executor.shutdown();
try {
executor.awaitTermination(10, TimeUnit.SECONDS);
} catch (InterruptedException e) {
throw new RuntimeException(e);
}
}
}
}