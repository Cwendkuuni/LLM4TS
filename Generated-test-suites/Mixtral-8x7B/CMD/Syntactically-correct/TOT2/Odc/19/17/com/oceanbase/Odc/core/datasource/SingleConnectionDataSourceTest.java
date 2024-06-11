import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectionInitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
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
private DataSource testDataSource;

@Before
public void setup() {
 dataSource = new SingleConnectionDataSource();
}

@After
public void teardown() {
 if (dataSource != null) {
 try {
 dataSource.close();
 } catch (Exception e) {
 //ignored
 }
 }
}

@Test
public void testGetConnection() throws SQLException {
 Connection connection = dataSource.getConnection();
 Assert.assertNotNull(connection);
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
 dataSource.setUsername("test");
 dataSource.setPassword("test");
 Connection connection = dataSource.getConnection("test", "test");
 Assert.assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
 dataSource.setUsername("test");
 dataSource.setPassword("test");
 dataSource.getConnection("invalid", "invalid");
}

@Test
public void testAddInitializer() throws SQLException {
 ConnectionInitializerImpl initializer = new ConnectionInitializerImpl();
 dataSource.addInitializer(initializer);
 Connection connection = dataSource.getConnection();
 Assert.assertNotNull(connection);
 Assert.assertTrue(initializer.isInvoked());
}

@Test
public void testResetConnection() throws SQLException {
 dataSource.getConnection();
 dataSource.resetConnection();
 Assert.assertNull(dataSource.getConnection());
}

@Test
public void testClose() throws SQLException {
 dataSource.getConnection();
 dataSource.close();
 Assert.assertNull(dataSource.getConnection());
}

@Test
public void testConnectionProxy() throws SQLException {
 Connection connection = dataSource.getConnection();
 Assert.assertNotNull(connection);
 Connection proxyConnection = dataSource.getConnectionProxy(connection);
 Assert.assertNotSame(connection, proxyConnection);
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyConflictException() throws SQLException, InterruptedException {
 ExecutorService executorService = Executors.newSingleThreadExecutor();
 executorService.submit(() -> {
 try {
 dataSource.getConnectionProxy(dataSource.getConnection());
 } catch (SQLException e) {
 throw new RuntimeException(e);
 }
 });
 executorService.submit(() -> {
 try {
 dataSource.getConnectionProxy(dataSource.getConnection());
 } catch (SQLException e) {
 throw new RuntimeException(e);
 }
 });
 executorService.shutdown();
 executorService.awaitTermination(1, TimeUnit.SECONDS);
}

@Test
public void testConnectionResetEvent() throws SQLException {
 dataSource.setEventPublisher(event -> {
 Assert.assertEquals(ConnectionResetEvent.class, event.getClass());
 Assert.assertNotNull(event.getConnection());
 });
 dataSource.resetConnection();
}

@Test
public void testInnerCreateConnection() throws Exception {
 testDataSource = new SingleConnectionDataSource();
 Connection connection = testDataSource.innerCreateConnection();
 Assert.assertNotNull(connection);
}
}