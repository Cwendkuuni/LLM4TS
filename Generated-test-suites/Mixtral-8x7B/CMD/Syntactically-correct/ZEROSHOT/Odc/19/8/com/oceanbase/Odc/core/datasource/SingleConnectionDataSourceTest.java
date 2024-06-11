import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

private static final Logger LOGGER = LoggerFactory.getLogger(SingleConnectionDataSourceTest.class);
private SingleConnectionDataSource dataSource;
private DataSource dataSourceSpy;
private ConnectionInitializer connectionInitializer;
private Connection connection;
private Lock lock;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource();
dataSourceSpy = Mockito.spy(dataSource);
connectionInitializer = mock(ConnectionInitializer.class);
connection = mock(Connection.class);
lock = new ReentrantLock();
}

@After
public void teardown() {
dataSource = null;
dataSourceSpy = null;
connectionInitializer = null;
connection = null;
lock = null;
}

@Test
public void testConstructor() {
assertFalse(dataSource.isAutoReconnect());
assertNull(dataSource.getConnection());
assertNull(dataSource.getEventPublisher());
assertEquals(10 * 1000, dataSource.getTimeOutMillis());
}

@Test
public void testConstructorWithAutoReconnect() {
dataSource = new SingleConnectionDataSource(true);
assertTrue(dataSource.isAutoReconnect());
}

@Test
public void testGetConnection() throws SQLException {
when(dataSourceSpy.innerCreateConnection()).thenReturn(connection);

Connection result = dataSourceSpy.getConnection();

assertEquals(connection, result);
verify(dataSourceSpy, times(1)).innerCreateConnection();
verify(dataSourceSpy, times(1)).prepareConnection(connection);
verify(dataSourceSpy, times(1)).getConnectionProxy(connection);
}

@Test
public void testGetConnectionWithAutoCommit() throws SQLException {
when(dataSourceSpy.innerCreateConnection()).thenReturn(connection);
dataSourceSpy.setAutoCommit(true);

Connection result = dataSourceSpy.getConnection();

assertEquals(connection, result);
verify(dataSourceSpy, times(1)).innerCreateConnection();
verify(dataSourceSpy, times(1)).prepareConnection(connection);
verify(dataSourceSpy, times(1)).getConnectionProxy(connection);
assertTrue(result.getAutoCommit());
}

@Test
public void testGetConnectionWithWrongUsernameAndPassword() throws SQLException {
when(dataSourceSpy.innerCreateConnection()).thenThrow(SQLException.class);

try {
dataSourceSpy.getConnection("wrongUsername", "wrongPassword");
fail();
} catch (SQLException e) {
// expected
}
verify(dataSourceSpy, times(1)).innerCreateConnection();
}

@Test
public void testGetConnectionProxy() throws SQLException {
when(dataSourceSpy.innerCreateConnection()).thenReturn(connection);

Connection result = dataSourceSpy.getConnectionProxy(connection);

// result should be a proxy connection
assertNotSame(connection, result);
assertTrue(result instanceof Connection);
}

@Test
public void testResetConnection() throws SQLException {
dataSourceSpy.setConnection(connection);

dataSourceSpy.resetConnection();

verify(dataSourceSpy, times(1)).close();
verify(dataSourceSpy, times(1)).innerCreateConnection();
}

@Test
public void testAddInitializer() {
dataSourceSpy.addInitializer(connectionInitializer);

List<ConnectionInitializer> result = dataSourceSpy.getInitializerList();

assertEquals(1, result.size());
assertEquals(connectionInitializer, result.get(0));
}

@Test
public void testAddInitializers() {
List<ConnectionInitializer> initializers = Arrays.asList(connectionInitializer, mock(ConnectionInitializer.class));
dataSourceSpy.addInitializers(initializers);

List<ConnectionInitializer> result = dataSourceSpy.getInitializerList();

assertEquals(2, result.size());
assertEquals(initializers, result);
}

@Test
public void testClose() throws SQLException {
dataSourceSpy.setConnection(connection);

dataSourceSpy.close();

verify(connection, times(1)).close();
}

@Test
public void testPrepareConnection() throws SQLException {
dataSourceSpy.setAutoCommit(true);
Connection connection = mock(Connection.class);

dataSourceSpy.prepareConnection(connection);

verify(connection, times(1)).setAutoCommit(true);
}

@Test
public void testInitConnection() throws SQLException {
dataSourceSpy.setConnection(connection);

dataSourceSpy.initConnection(connection);

verify(connectionInitializer, times(1)).init(connection);
verify(dataSourceSpy, times(1)).onConnectionCreated(connection);
}

@Test
public void testOnConnectionReset() {
EventPublisher eventPublisher = mock(EventPublisher.class);
dataSourceSpy.setEventPublisher(eventPublisher);

dataSourceSpy.onConnectionReset(connection);

verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
}

@Test
public void testGetConnectionProxyWithLock() throws SQLException, InterruptedException {
when(dataSourceSpy.innerCreateConnection()).thenReturn(connection);

Connection result = dataSourceSpy.getConnectionProxy(connection);

// result should be a proxy connection
assertNotSame(connection, result);
assertTrue(result instanceof Connection);
// verify that the lock was acquired
verify(lock, times(1)).lock();
// verify that the lock was released
verify(lock, times(1)).unlock();
}

@Test
public void testGetConnectionProxyWithLockInterrupted() throws SQLException, InterruptedException {
when(dataSourceSpy.innerCreateConnection()).thenReturn(connection);
doThrow(new InterruptedException()).when(lock).tryLock(anyLong(), any(TimeUnit.class));

try {
dataSourceSpy.getConnectionProxy(connection);
fail();
} catch (ConflictException e) {
// expected
}
// verify that the lock was attempted to be acquired
verify(lock, times(1)).tryLock(anyLong(), any(TimeUnit.class));
// verify that the lock was released
verify(lock, times(1)).unlock();
}

}