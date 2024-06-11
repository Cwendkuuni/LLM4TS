import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

// Initialize mocked objects
@Before
public void setUp() {
ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
when(connectionInitializer.init(any(Connection.class))).thenReturn(null);
}

// Test for getConnection() method
@Test
public void testGetConnection() throws SQLException {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.addInitializer(new ConnectionInitializer() {
@Override
public void init(Connection connection) throws Exception {
// Do nothing
}
});
dataSource.setUsername("testUser");
dataSource.setPassword("testPass");
Connection connection = dataSource.getConnection();
assertNotNull(connection);
}

// Test for getConnection(username, password) method
@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernamePassword() throws SQLException {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setUsername("testUser");
dataSource.setPassword("testPass");
dataSource.getConnection("invalidUser", "invalidPass");
}

// Test for resetConnection() method
@Test
public void testResetConnection() throws SQLException {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.addInitializer(new ConnectionInitializer() {
@Override
public void init(Connection connection) throws Exception {
// Do nothing
}
});
dataSource.setUsername("testUser");
dataSource.setPassword("testPass");
Connection connection = dataSource.getConnection();
assertNotNull(connection);
dataSource.resetConnection();
}

// Test for addInitializer() method
@Test
public void testAddInitializer() {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
ConnectionInitializer connectionInitializer = mock(ConnectionInitializer.class);
dataSource.addInitializer(connectionInitializer);
assertTrue(dataSource.getInitializerList().contains(connectionInitializer));
}

// Test for close() method
@Test
public void testClose() throws SQLException {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.addInitializer(new ConnectionInitializer() {
@Override
public void init(Connection connection) throws Exception {
// Do nothing
}
});
dataSource.setUsername("testUser");
dataSource.setPassword("testPass");
Connection connection = dataSource.getConnection();
assertNotNull(connection);
dataSource.close();
}

// Test for prepareConnection() method
@Test
public void testPrepareConnection() throws SQLException {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setAutoCommit(true);
Connection connection = mock(Connection.class);
when(connection.getAutoCommit()).thenReturn(false);
dataSource.prepareConnection(connection);
verify(connection, times(1)).setAutoCommit(true);
}

// Test for tryLock() method
@Test
public void testTryLock() {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
ReentrantLock lock = mock(ReentrantLock.class);
when(lock.tryLock(anyLong(), any(TimeUnit.class))).thenReturn(true);
assertTrue(dataSource.tryLock());
}

// Test for getConnectionProxy() method
@Test
public void testGetConnectionProxy() {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
ReentrantLock lock = new ReentrantLock();
Connection connection = mock(Connection.class);
when(connection.isClosed()).thenReturn(true);
dataSource.setConnection(connection);
dataSource.setLock(lock);
Connection connectionProxy = dataSource.getConnectionProxy(connection);
assertNotNull(connectionProxy);
}

// Test for initConnection() method
@Test
public void testInitConnection() throws SQLException {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
Connection connection = mock(Connection.class);
dataSource.initConnection(connection);
verify(connection, times(1)).setAutoCommit(anyBoolean());
}

// Test for onConnectionCreated() method
@Test
public void testOnConnectionCreated() {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource() {
@Override
protected void onConnectionCreated(Connection connection) {
// Do nothing
}
};
Connection connection = mock(Connection.class);
dataSource.onConnectionCreated(connection);
verify(connection, times(1)).setAutoCommit(anyBoolean());
}

// Test for onConnectionReset() method
@Test
public void testOnConnectionReset() {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
ConnectionResetEvent connectionResetEvent = mock(ConnectionResetEvent.class);
dataSource.setEventPublisher(event -> {
});
dataSource.onConnectionReset(mock(Connection.class));
verify(connectionResetEvent, times(1)).getConnection();
}

// Test for innerCreateConnection() method
@Test
public void testInnerCreateConnection() throws SQLException {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
Connection connection = mock(Connection.class);
when(connection.isClosed()).thenReturn(false);
dataSource.setConnection(connection);
when(dataSource.newConnectionFromDriver(anyString(), anyString())).thenReturn(connection);
Connection newConnection = dataSource.innerCreateConnection();
assertEquals(connection, newConnection);
}

// Test for CloseIgnoreInvocationHandler's invoke method
@Test
public void testCloseIgnoreInvocationHandlerInvoke() throws Throwable {
Connection connection = mock(Connection.class);
ReentrantLock lock = mock(ReentrantLock.class);
CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
handler.invoke(connection, Methods.close.getMethod(), new Object[0]);
verify(lock, times(1)).unlock();
}

}

class Methods {
public static final sun.reflect.Method close = new sun.reflect.Method(Connection.class, "close", new Class[0]);
}