import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.common.event.EventPublisher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource mockDataSource;
private Connection mockConnection;
private Lock mockLock;
private ConnectionInitializer mockInitializer;
private EventPublisher mockEventPublisher;

@Before
public void setUp() throws Exception {
// Initialize mocks
mockConnection = mock(Connection.class);
mockLock = mock(ReentrantLock.class);
mockInitializer = mock(ConnectionInitializer.class);
mockEventPublisher = mock(EventPublisher.class);

// Initialize dataSource
dataSource = new SingleConnectionDataSource(false);
dataSource.setConnection(mockConnection);
dataSource.setLock(mockLock);
dataSource.addInitializer(mockInitializer);
dataSource.setEventPublisher(mockEventPublisher);

// Initialize mockDataSource
mockDataSource = (DataSource) Proxy.newProxyInstance(DataSource.class.getClassLoader(),
new Class[] {DataSource.class},
new InvocationHandler() {
@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
if ("getConnection".equals(method.getName())) {
return SingleConnectionDataSourceTest.this.mockConnection;
}
throw new UnsupportedOperationException();
}
});

// Set up a concurrent map to hold the connections
ConcurrentMap<String, Connection> connections = new ConcurrentHashMap<>();
connections.put("mockConnection", mockConnection);

// Set up a driver manager that returns the mock connection
doReturn(connections).when(DriverManager.class).getDrivers();
doReturn(mockConnection).when(DriverManager.class).getConnection(anyString(), anyString(), anyInt());
}

@After
public void tearDown() throws Exception {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertEquals(mockConnection, connection);
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
Connection connection = dataSource.getConnection("username", "password");
assertEquals(mockConnection, connection);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
dataSource.getConnection("invalidUsername", "invalidPassword");
}

@Test(expected = ConflictException.class)
public void testGetConnectionWithOccupiedConnection() throws SQLException {
dataSource.getConnection();
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();
verify(mockConnection).close();
}

@Test
public void testAddInitializer() throws SQLException {
dataSource.addInitializer(mockInitializer);
dataSource.getConnection();
verify(mockInitializer).init(mockConnection);
}

@Test
public void testGetConnectionProxy() throws SQLException {
Connection proxyConnection = dataSource.getConnectionProxy(mockConnection);
assertNotSame(mockConnection, proxyConnection);
}

@Test
public void testCreateConnectionFromDriver() throws SQLException {
Connection connection = dataSource.newConnectionFromDriver("user", "password");
assertNotNull(connection);
verify(DriverManager.class, times(1)).getConnection(eq("jdbc:mysql://localhost:3306/odc"), eq("user"),
eq("password"));
}

@Test
public void testCloseIgnoreInvocationHandler() throws SQLException {
InvocationHandler handler = new SingleConnectionDataSource.CloseIgnoreInvocationHandler(mockConnection,
mockLock);
Connection proxy = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
new Class[] {Connection.class}, handler);
proxy.close();
verify(mockLock).unlock();
}
}