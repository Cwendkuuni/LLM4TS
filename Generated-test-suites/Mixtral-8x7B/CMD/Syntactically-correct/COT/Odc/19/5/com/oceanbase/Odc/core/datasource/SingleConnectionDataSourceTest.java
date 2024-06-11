import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectionInitializer.TestConnectionInitializer;
import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource testDataSource;
private Connection testConnection;
private Lock lock;
private EventPublisher eventPublisher;

@Before
public void setUp() {
testDataSource = mock(DataSource.class);
testConnection = mock(Connection.class);
lock = new ReentrantLock();
eventPublisher = mock(EventPublisher.class);
dataSource = new SingleConnectionDataSource(testDataSource, lock, eventPublisher);
}

@After
public void tearDown() {
dataSource.close();
}

@Test
public void testGetConnection_WithoutInitialization() throws SQLException {
when(testDataSource.getConnection()).thenReturn(testConnection);
Connection result = dataSource.getConnection();
assertEquals(testConnection, result);
}

@Test
public void testGetConnection_WithInitialization() throws SQLException {
when(testDataSource.getConnection()).thenReturn(testConnection);
dataSource.addInitializer(new TestConnectionInitializer());
Connection result = dataSource.getConnection();
verify(testConnection, times(1)).addConnectionEventListener(any());
assertEquals(testConnection, result);
}

@Test
public void testGetConnection_WithAutoReconnectFalse_ClosedConnection() {
assertThrows(SQLException.class, () -> {
dataSource.setAutoReconnect(false);
when(testConnection.isClosed()).thenReturn(true);
dataSource.getConnection();
});
}

@Test
public void testGetConnection_WithAutoReconnectTrue_ClosedConnection() throws SQLException {
dataSource.setAutoReconnect(true);
when(testConnection.isClosed()).thenReturn(true);
Connection result = dataSource.getConnection();
assertEquals(testConnection, result);
}

@Test
public void testGetConnection_WithAutoReconnectTrue_InvalidConnection() throws SQLException {
dataSource.setAutoReconnect(true);
when(testConnection.isClosed()).thenReturn(false);
when(testConnection.isValid(anyLong())).thenReturn(false);
Connection result = dataSource.getConnection();
assertEquals(testConnection, result);
}

@Test
public void testResetConnection() throws SQLException {
dataSource.addInitializer(new TestConnectionInitializer());
dataSource.getConnection();
verify(testConnection, times(1)).close();
verify(testConnection, times(1)).addConnectionEventListener(any());
dataSource.resetConnection();
verify(testConnection, times(2)).close();
verify(eventPublisher, times(1)).publishEvent(any());
}

@Test
public void testAddInitializer() {
dataSource.addInitializer(new TestConnectionInitializer());
List<ConnectionInitializer> result = dataSource.getInitializerList();
assertEquals(1, result.size());
}

@Test
public void testClose() throws SQLException {
dataSource.getConnection();
dataSource.close();
verify(testConnection, times(1)).close();
}

@Test
public void testLock_TryLock() {
assertTrue(dataSource.tryLock());
}

@Test
public void testLock_TryLockInterruptedException() {
assertFalse(dataSource.tryLock());
}

@Test
public void testGetConnectionProxy() {
InvocationHandler handler = dataSource.getConnectionProxy(testConnection);
assertNotNull(handler);
assertEquals(CloseIgnoreInvocationHandler.class, handler.getClass());
}

static class TestConnectionInitializer implements ConnectionInitializer {

@Override
public void init(Connection connection) throws SQLException {
connection.addConnectionEventListener(event -> {
});
}
}
}