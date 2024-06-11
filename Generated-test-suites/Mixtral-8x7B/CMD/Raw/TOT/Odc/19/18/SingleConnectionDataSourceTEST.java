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
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private ConnectionInitializer initializer;
private EventPublisher eventPublisher;
private ExecutorService executorService;

@Before
public void setUp() {
dataSource = new SingleConnectionDataSource();
initializer = mock(ConnectionInitializer.class);
dataSource.addInitializer(initializer);
eventPublisher = mock(EventPublisher.class);
dataSource.setEventPublisher(eventPublisher);
executorService = Executors.newSingleThreadExecutor();
}

@After
public void tearDown() throws Exception {
executorService.shutdown();
executorService.awaitTermination(1, TimeUnit.SECONDS);
dataSource.close();
}

@Test
public void testGetConnectionWithDefaultSettings() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
}

@Test
public void testGetConnectionWithSpecificUsernameAndPassword() throws SQLException {
String username = "test_user";
String password = "test_password";
dataSource.setUsername(username);
dataSource.setPassword(password);
Connection connection = dataSource.getConnection(username, password);
assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
String username = "invalid_user";
String password = "invalid_password";
dataSource.getConnection(username, password);
}

@Test
public void testAutoCommit() throws SQLException {
dataSource.setAutoCommit(true);
Connection connection = dataSource.getConnection();
assertTrue(connection.getAutoCommit());
}

@Test
public void testManualCommit() throws SQLException {
dataSource.setAutoCommit(false);
Connection connection = dataSource.getConnection();
assertFalse(connection.getAutoCommit());
connection.commit();
}

@Test
public void testAutomaticReconnect() throws SQLException {
dataSource.setAutoReconnect(true);
Connection connection = dataSource.getConnection();
assertNotNull(connection);
connection.close();
assertNotNull(dataSource.getConnection());
}

@Test
public void testConnectionInitialization() throws SQLException {
Connection connection = mock(Connection.class);
when(initializer.init(any(Connection.class))).thenReturn(connection);
Connection returnedConnection = dataSource.getConnection();
verify(initializer, times(1)).init(any(Connection.class));
assertSame(connection, returnedConnection);
}

@Test
public void testConnectionResetEvent() throws Exception {
Connection connection = mock(Connection.class);
dataSource.getConnection();
verify(eventPublisher, never()).publishEvent(any());
dataSource.onConnectionReset(connection);
verify(eventPublisher, times(1))
.publishEvent(Mockito.argThat(arg -> arg instanceof SingleConnectionDataSource.ConnectionResetEvent));
}

@Test
public void testLocking() throws SQLException {
Connection connection1 = dataSource.getConnection();
Connection connection2 = dataSource.getConnection();
assertSame(connection1, connection2);
connection1.close();
Connection connection3 = dataSource.getConnection();
assertNotSame(connection1, connection3);
}

@Test(expected = ConflictException.class)
public void testLockingConflict() throws SQLException {
executorService.submit(() -> {
try {
dataSource.getConnection();
} catch (SQLException e) {
throw new RuntimeException(e);
}
});
dataSource.getConnection();
}

@Test
public void testLockTimeout() throws SQLException {
dataSource.setTimeOutMillis(100);
executorService.submit(() -> {
try {
dataSource.getConnection();
} catch (SQLException e) {
throw new RuntimeException(e);
}
});
try {
dataSource.getConnection();
fail("Expected ConflictException");
} catch (ConflictException e) {
assertEquals("Connection is occupied, waited 100 millis", e.getMessage());
}
}

@Test
public void testClose() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
dataSource.close();
connection.close();
try {
dataSource.getConnection();
fail("Expected SQLException");
} catch (SQLException e) {
assertEquals("Connection was closed or not valid", e.getMessage());
}
}

@Test
public void testResetConnection() throws SQLException {
dataSource.getConnection();
verify(eventPublisher, never()).publishEvent(any());
dataSource.resetConnection();
verify(eventPublisher, times(1))
.publishEvent(Mockito.argThat(arg -> arg instanceof SingleConnectionDataSource.ConnectionResetEvent));
}
}