import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource dataSourceMock;
private Connection connectionMock;
private ConnectionInitializer connectionInitializerMock;
private Lock lockMock;

@Before
public void setUp() throws Exception {
dataSourceMock = mock(DataSource.class);
connectionMock = mock(Connection.class);
connectionInitializerMock = mock(ConnectionInitializer.class);
lockMock = mock(ReentrantLock.class);
when(dataSourceMock.getConnection()).thenReturn(connectionMock);
dataSource = new SingleConnectionDataSource(dataSourceMock, connectionInitializerMock, lockMock);
}

@After
public void tearDown() throws Exception {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
verify(connectionMock, never()).close();
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
String username = "testUser";
String password = "testPassword";
when(dataSourceMock.getConnection(username, password)).thenReturn(connectionMock);
Connection connection = dataSource.getConnection(username, password);
assertNotNull(connection);
verify(connectionMock, never()).close();
}

@Test
public void testGetConnectionWithInvalidUsernameAndPassword() {
String username = "invalidUser";
String password = "invalidPassword";
assertThrows(SQLException.class, () -> dataSource.getConnection(username, password));
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();
verify(connectionMock).close();
}

@Test
public void testAddInitializer() {
dataSource.addInitializer(connectionInitializerMock);
assertEquals(1, dataSource.getInitializerList().size());
}

@Test
public void testClose() throws SQLException {
dataSource.close();
verify(connectionMock).close();
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithLockTimeout() throws SQLException, InterruptedException {
when(lockMock.tryLock(1, TimeUnit.MILLISECONDS)).thenReturn(false);
dataSource.getConnectionProxy(connectionMock);
}

@Test
public void testGetConnectionProxyWithLockAcquired() throws SQLException, InterruptedException {
when(lockMock.tryLock(10, TimeUnit.MILLISECONDS)).thenReturn(true);
Connection connection = dataSource.getConnectionProxy(connectionMock);
assertNotNull(connection);
assertFalse(connection.isClosed());
verify(connectionMock).close();
}

@Test
public void testPrepareConnection() throws SQLException {
Boolean autoCommit = true;
dataSource.setAutoCommit(autoCommit);
Connection connection = mock(Connection.class);
when(connection.getAutoCommit()).thenReturn(false);
dataSource.prepareConnection(connection);
verify(connection).setAutoCommit(autoCommit);
}
}