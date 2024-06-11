import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;

@Before
public void setUp() {
dataSource = new SingleConnectionDataSource();
}

@After
public void tearDown() throws SQLException {
if (dataSource != null) {
dataSource.close();
}
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
Assert.assertNotNull(connection);
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
dataSource.setUsername("testUser");
dataSource.setPassword("testPassword");
Connection connection = dataSource.getConnection("testUser", "testPassword");
Assert.assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
dataSource.setUsername("testUser");
dataSource.setPassword("testPassword");
dataSource.getConnection("invalidUser", "invalidPassword");
}

@Test
public void testResetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
Assert.assertNotNull(connection);
dataSource.resetConnection();
Connection newConnection = dataSource.getConnection();
Assert.assertNotNull(newConnection);
Assert.assertNotSame(connection, newConnection);
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyConflict() throws ConflictException, SQLException {
Connection connection = dataSource.getConnection();
Assert.assertNotNull(connection);
dataSource.getConnectionProxy(connection);
}

@Test
public void testAddInitializer() throws Exception {
ConnectionInitializer initializer = connection -> {
// Initialization logic
};
dataSource.addInitializer(initializer);
Connection connection = dataSource.getConnection();
Assert.assertNotNull(connection);
}

@Test
public void testTimeOutMillis() throws Exception {
dataSource.setTimeOutMillis(5000);
ExecutorService executorService = Executors.newSingleThreadExecutor();
executorService.execute(() -> {
try {
dataSource.getConnectionProxy(dataSource.innerCreateConnection());
} catch (ConflictException e) {
// Expected exception
} catch (Exception e) {
throw new RuntimeException(e);
}
});
boolean completed = executorService.awaitTermination(6000, TimeUnit.MILLISECONDS);
Assert.assertTrue(completed);
}
}