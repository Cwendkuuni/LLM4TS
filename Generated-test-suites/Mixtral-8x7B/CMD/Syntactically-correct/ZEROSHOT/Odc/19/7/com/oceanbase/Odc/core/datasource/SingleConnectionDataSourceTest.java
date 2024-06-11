import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
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
private Connection connection;
private Lock lock;
private EventPublisher eventPublisher;

@Before
public void setUp() throws SQLException {
dataSource = new SingleConnectionDataSource();
connection = mock(Connection.class);
lock = new ReentrantLock();
eventPublisher = mock(EventPublisher.class);
dataSource.setConnection(connection);
dataSource.setLock(lock);
dataSource.setEventPublisher(eventPublisher);
}

@After
public void tearDown() throws SQLException {
dataSource.close();
}

@Test
public void testGetConnectionWithDefaultValues() throws SQLException {
when(connection.isClosed()).thenReturn(false);
Connection result = dataSource.getConnection();
assertEquals(connection, result);
}

@Test
public void testGetConnectionWithClosedConnection() throws SQLException {
when(connection.isClosed()).thenReturn(true);
when(dataSource.innerCreateConnection()).thenReturn(connection);
Connection result = dataSource.getConnection();
assertEquals(connection, result);
}

@Test
public void testGetConnectionWithAutoReconnectDisabled() {
dataSource.setAutoReconnect(false);
assertThrows(SQLException.class, dataSource::getConnection);
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();
verify(connection, times(1)).close();
}

@Test
public void testAddInitializer() {
ConnectionInitializer initializer1 = mock(ConnectionInitializer.class);
ConnectionInitializer initializer2 = mock(ConnectionInitializer.class);
dataSource.addInitializer(initializer1);
dataSource.addInitializer(initializer2);
List<ConnectionInitializer> result = dataSource.getInitializerList();
assertEquals(2, result.size());
assertEquals(initializer1, result.get(0));
assertEquals(initializer2, result.get(1));
}

@Test
public void testClose() throws SQLException {
dataSource.close();
verify(connection, times(1)).close();
}

@Test
public void testGetConnectionProxy() {
Connection result = dataSource.getConnectionProxy(connection);
assertTrue(result instanceof Connection);
}

@Test
public void testTryLock() {
assertTrue(dataSource.tryLock());
assertFalse(dataSource.tryLock());
}

@Test
public void testOnConnectionReset() {
dataSource.onConnectionReset(connection);
verify(eventPublisher, times(1)).publishEvent(Mockito.any(ConnectionResetEvent.class));
}

@Test
public void testInnerCreateConnection() throws SQLException {
when(dataSource.newConnectionFromDriver(Mockito.anyString(), Mockito.anyString()))
.thenReturn(connection);
Connection result = dataSource.innerCreateConnection();
assertEquals(connection, result);
}

@Test
public void testPrepareConnection() throws SQLException {
dataSource.setAutoCommit(true);
dataSource.prepareConnection(connection);
verify(connection, times(1)).setAutoCommit(true);
}

@Test
public void testConnectionResetEvent() {
ConnectionResetEvent event = new ConnectionResetEvent(connection);
assertEquals(connection, event.getConnection());
}

@Test
public void testCloseIgnoreInvocationHandler() {
CloseIgnoreInvocationHandler handler = new CloseIgnoreInvocationHandler(connection, lock);
Object proxy = Proxy.newProxyInstance(
Connection.class.getClassLoader(),
new Class[] {Connection.class},
handler);
assertThrows(UnsupportedOperationException.class, () -> {
((Connection) proxy).close();
});
}
}