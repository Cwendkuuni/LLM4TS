import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.jupiter.api.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

class SingleConnectionDataSourceTest {

private static SingleConnectionDataSource dataSource;
private static DataSource dataSourceNoAutoReconnect;

@BeforeAll
static void setup() {
dataSource = new SingleConnectionDataSource();
dataSourceNoAutoReconnect = new SingleConnectionDataSource(false);
}

@Test
@DisplayName("Test getConnection() with valid settings")
void testGetConnectionValid() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
assertTrue(connection.isValid(dataSource.getLoginTimeout()));
}

@Test
@DisplayName("Test getConnection(username, password) with valid credentials")
void testGetConnectionValidCredentials() throws SQLException {
Connection connection = dataSource.getConnection(dataSource.getUsername(), dataSource.getPassword());
assertNotNull(connection);
assertTrue(connection.isValid(dataSource.getLoginTimeout()));
}

@Test
@DisplayName("Test getConnection(username, password) with invalid credentials")
void testGetConnectionInvalidCredentials() {
assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPassword"));
}

@Test
@DisplayName("Test getConnection() after resetConnection()")
void testGetConnectionAfterResetConnection() throws SQLException {
dataSource.resetConnection();
Connection connection = dataSource.getConnection();
assertNotNull(connection);
assertTrue(connection.isValid(dataSource.getLoginTimeout()));
}

@Test
@DisplayName("Test resetConnection() with autoReconnect=false")
void testResetConnectionAutoReconnectFalse() {
assertThrows(SQLException.class, dataSourceNoAutoReconnect::resetConnection);
}

@Test
@DisplayName("Test addInitializer() and initConnection()")
void testAddInitializer() throws SQLException {
dataSource.addInitializer(connection -> {
assertSame(dataSource.getConnection(), connection);
connection.setAutoCommit(false);
});

Connection connection = dataSource.getConnection();
assertFalse(connection.getAutoCommit());
}

@Test
@DisplayName("Test close()")
void testClose() throws SQLException {
dataSource.close();
assertThrows(SQLException.class, dataSource::getConnection);
}

@Test
@DisplayName("Test connection proxy")
void testConnectionProxy() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);

InvocationHandler handler = Proxy.getInvocationHandler(connection);
CloseIgnoreInvocationHandler closeIgnoreInvocationHandler = (CloseIgnoreInvocationHandler) handler;
assertSame(connection, closeIgnoreInvocationHandler.target);
assertNotNull(closeIgnoreInvocationHandler.lock);

// Verify that close() call is ignored and lock is released
closeIgnoreInvocationHandler.invoke(connection, "close", null);
assertNotNull(dataSource.getConnection());

// Verify that isClosed() returns false
Method isClosedMethod = Connection.class.getDeclaredMethod("isClosed");
isClosedMethod.setAccessible(true);
assertFalse((Boolean) isClosedMethod.invoke(connection));
}

@Test
@DisplayName("Test connection proxy tryLock()")
void testConnectionProxyTryLock() {
InvocationHandler handler = Proxy.getInvocationHandler(dataSource.getConnection());
CloseIgnoreInvocationHandler closeIgnoreInvocationHandler = (CloseIgnoreInvocationHandler) handler;
assertTrue(closeIgnoreInvocationHandler.tryLock());
assertFalse(closeIgnoreInvocationHandler.tryLock());
}

@Test
@DisplayName("Test ConnectionOccupied ConflictException")
void testConnectionOccupied() {
assertThrows(ConflictException.class, () -> {
dataSource.getConnectionProxy(dataSource.getConnection());
});
}

@Test
@DisplayName("Test ConnectionOccupied ConflictException with timeout")
void testConnectionOccupiedWithTimeout() {
dataSource.setTimeOutMillis(100);
assertThrows(ConflictException.class, () -> {
dataSource.getConnectionProxy(dataSource.getConnection());
}, "Connection is occupied, waited 100 millis");
}

@Test
@DisplayName("Test parallel connection acquiring and releasing")
void testParallelConnectionAcquiringAndReleasing() throws InterruptedException {
ExecutorService executor = Executors.newFixedThreadPool(2);
executor.submit(() -> {
try {
dataSource.getConnection();
} catch (SQLException e) {
fail("Failed to acquire connection", e);
}
});

executor.submit(() -> {
try {
dataSource.getConnection();
} catch (SQLException e) {
fail("Failed to acquire connection", e);
}
});

executor.shutdown();
executor.awaitTermination(1, TimeUnit.SECONDS);
}

@Test
@DisplayName("Test onConnectionReset() with EventPublisher")
void testOnConnectionResetWithEventPublisher() throws SQLException {
dataSource.setEventPublisher(event -> {
assertSame(dataSource.getConnection(), event.getConnection());
});

dataSource.resetConnection();
}

}