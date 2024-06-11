import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
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
public void setup() {
dataSource = new SingleConnectionDataSource(true);
dataSource.addInitializer(new ConnectionInitializerImpl());
}

@After
public void teardown() throws SQLException {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
Assert.assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
dataSource.setUsername("invalid");
dataSource.setPassword("invalid");
dataSource.getConnection("invalid", "invalid");
}

@Test
public void testResetConnection() throws SQLException {
Connection connection1 = dataSource.getConnection();
Assert.assertNotNull(connection1);
dataSource.resetConnection();
Connection connection2 = dataSource.getConnection();
Assert.assertNotNull(connection2);
Assert.assertNotSame(connection1, connection2);
}

@Test(expected = ConflictException.class)
public void testGetConnectionWithConflict() throws SQLException, InterruptedException {
ExecutorService executorService = Executors.newFixedThreadPool(2);
executorService.submit(() -> {
try {
dataSource.getConnection();
} catch (SQLException e) {
throw new RuntimeException(e);
}
});
executorService.submit(() -> {
try {
dataSource.getConnection();
} catch (SQLException e) {
throw new RuntimeException(e);
}
});
executorService.shutdown();
executorService.awaitTermination(1, TimeUnit.SECONDS);
}

@Test
public void testAutoCommit() throws SQLException {
dataSource.setAutoCommit(true);
Connection connection = dataSource.getConnection();
Assert.assertTrue(connection.getAutoCommit());
}

@Test
public void testAutoReconnect() throws SQLException, InterruptedException {
dataSource.setAutoReconnect(true);
Connection connection1 = dataSource.getConnection();
Assert.assertNotNull(connection1);
dataSource.resetConnection();
TimeUnit.MILLISECONDS.sleep(500);
Connection connection2 = dataSource.getConnection();
Assert.assertNotNull(connection2);
Assert.assertNotSame(connection1, connection2);
}

@Test
public void testEventPublisher() throws SQLException {
// Assuming EventPublisher and ConnectionResetEvent are mocked/spied
// ...
dataSource.setEventPublisher(eventPublisher);
dataSource.resetConnection();
// Verify that ConnectionResetEvent is published
// ...
}
}