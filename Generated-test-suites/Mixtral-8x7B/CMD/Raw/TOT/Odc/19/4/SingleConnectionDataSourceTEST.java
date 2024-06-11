import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer1;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;

@Before
public void setUp() {
dataSource = new SingleConnectionDataSource();
dataSource.addInitializer(new ConnectionInitializer1());
dataSource.addInitializer(new ConnectionInitializer2());
}

@After
public void tearDown() throws Exception {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
// Perform some operations with the connection
connection.createStatement().executeQuery("SELECT 1");
// Verify that the connection was returned to the pool
verify(connection).close();
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
dataSource.setUsername("testUser");
dataSource.setPassword("testPassword");
Connection connection = dataSource.getConnection("testUser", "testPassword");
assertNotNull(connection);
// Perform some operations with the connection
connection.createStatement().executeQuery("SELECT 1");
// Verify that the connection was returned to the pool
verify(connection).close();
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
// Perform some operations with the connection
connection.createStatement().executeQuery("SELECT 1");
dataSource.resetConnection();
// Verify that the connection was reset
assertNull(dataSource.getConnection());
}

@Test
public void testAutoReconnect() throws SQLException {
dataSource.setAutoReconnect(true);
Connection connection1 = dataSource.getConnection();
// Close the connection
connection1.close();
Connection connection2 = dataSource.getConnection();
// Verify that a new connection was created
assertNotSame(connection1, connection2);
}

@Test
public void testAutoCommit() throws SQLException {
dataSource.setAutoCommit(true);
Connection connection = dataSource.getConnection();
assertEquals(true, connection.getAutoCommit());
dataSource.setAutoCommit(false);
Connection connection2 = dataSource.getConnection();
assertEquals(false, connection2.getAutoCommit());
}

@Test
public void testEventPublisher() throws SQLException {
EventPublisher eventPublisher = mock(EventPublisher.class);
dataSource.setEventPublisher(eventPublisher);
Connection connection = dataSource.getConnection();
// Perform some operations with the connection
connection.createStatement().executeQuery("SELECT 1");
// Verify that the ConnectionResetEvent was published
verify(eventPublisher).publishEvent(new SingleConnectionDataSource.ConnectionResetEvent(connection));
}

@Test(timeout = 5000)
public void testTimeOutMillis() throws SQLException, InterruptedException {
dataSource.setTimeOutMillis(500);
Connection connection = dataSource.getConnection();
// Release the connection
connection.releaseSavepoint();
// Wait for the lock to be released
TimeUnit.MILLISECONDS.sleep(1000);
// Try to acquire the lock again
Connection connection2 = dataSource.getConnection();
assertNotSame(connection, connection2);
}

@Test(expected = ConflictException.class)
public void testTimeOutMillisWithConflict() throws SQLException, InterruptedException {
dataSource.setTimeOutMillis(500);
Connection connection = dataSource.getConnection();
// Release the connection
connection.releaseSavepoint();
// Wait for the lock to be released
TimeUnit.MILLISECONDS.sleep(1500);
// Try to acquire the lock again
dataSource.getConnection();
}
}