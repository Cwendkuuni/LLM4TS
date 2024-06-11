import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.TestConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource dataSourceSpy;
private Connection connection;
private TestConnectionInitializer connectionInitializer;

@Before
public void setup() {
connectionInitializer = new TestConnectionInitializer();
dataSource = new SingleConnectionDataSource();
dataSource.addInitializer(connectionInitializer);
connection = mock(Connection.class);
dataSourceSpy = Mockito.spy(dataSource);
}

@After
public void tearDown() throws SQLException {
dataSourceSpy.close();
}

@Test
public void testGetConnection_WithoutInitializers() throws SQLException {
when(dataSourceSpy.newConnectionFromDriver(Mockito.anyString(), Mockito.anyString()))
.thenReturn(connection);
dataSource.setAutoCommit(true);
Connection result = dataSourceSpy.getConnection();
verify(connection, times(1)).setAutoCommit(true);
assertEquals(connection, result);
}

@Test
public void testGetConnection_WithInitializers() throws SQLException {
when(dataSourceSpy.newConnectionFromDriver(Mockito.anyString(), Mockito.anyString()))
.thenReturn(connection);
dataSource.setAutoCommit(true);
Connection result = dataSourceSpy.getConnection();
verify(connectionInitializer, times(1)).init(connection);
verify(connection, times(1)).setAutoCommit(true);
assertEquals(connection, result);
}

@Test
public void testGetConnectionWithUserAndPassword_InvalidCredentials() {
assertThrows(SQLException.class, () -> dataSourceSpy.getConnection("invalidUser", "invalidPassword"));
}

@Test
public void testGetConnectionWithUserAndPassword_ValidCredentials() throws SQLException {
when(dataSourceSpy.newConnectionFromDriver(Mockito.anyString(), Mockito.anyString()))
.thenReturn(connection);
dataSource.setUsername("testUser");
dataSource.setPassword("testPassword");
Connection result = dataSourceSpy.getConnection("testUser", "testPassword");
assertEquals(connection, result);
}

@Test
public void testResetConnection() throws SQLException {
when(dataSourceSpy.newConnectionFromDriver(Mockito.anyString(), Mockito.anyString()))
.thenReturn(connection);
dataSourceSpy.getConnection();
dataSourceSpy.resetConnection();
verify(dataSourceSpy, times(1)).newConnectionFromDriver(Mockito.anyString(), Mockito.anyString());
}

@Test
public void testClose() throws SQLException {
when(dataSourceSpy.newConnectionFromDriver(Mockito.anyString(), Mockito.anyString()))
.thenReturn(connection);
dataSourceSpy.getConnection();
dataSourceSpy.close();
verify(connection, times(1)).close();
}

@Test
public void testCloseMultipleTimes() throws SQLException {
when(dataSourceSpy.newConnectionFromDriver(Mockito.anyString(), Mockito.anyString()))
.thenReturn(connection);
dataSourceSpy.getConnection();
dataSourceSpy.close();
dataSourceSpy.close();
verify(connection, times(1)).close();
}

@Test
public void testTryLock_WhenLockIsAvailable() {
assertTrue(dataSourceSpy.tryLock());
}

@Test
public void testTryLock_WhenLockIsNotAvailable() {
dataSourceSpy.lock.lock();
assertFalse(dataSourceSpy.tryLock());
}

@Test
public void testGetConnectionProxy_WithTryLockSuccess() throws SQLException {
dataSourceSpy.lock.lock();
Connection result = dataSourceSpy.getConnectionProxy(connection);
verify(connection, times(1)).isValid(Mockito.anyLong());
assertEquals(connection, result);
}

@Test
public void testGetConnectionProxy_WithTryLockFailure() {
when(dataSourceSpy.tryLock()).thenReturn(false);
assertThrows(ConflictException.class, () -> dataSourceSpy.getConnectionProxy(connection));
}

@Test
public void testOnConnectionReset() throws Exception {
EventPublisher eventPublisher = mock(EventPublisher.class);
dataSourceSpy.setEventPublisher(eventPublisher);
dataSourceSpy.onConnectionReset(connection);
verify(eventPublisher, times(1)).publishEvent(Mockito.any(ConnectionResetEvent.class));
}
}