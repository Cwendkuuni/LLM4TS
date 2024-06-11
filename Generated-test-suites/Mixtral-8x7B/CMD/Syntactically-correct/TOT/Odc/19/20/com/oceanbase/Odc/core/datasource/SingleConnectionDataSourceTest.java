import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private ConnectionInitializer connectionInitializer;
private Connection connection;
private Lock lock;

@Before
public void setUp() throws SQLException {
this.dataSource = new SingleConnectionDataSource();
this.connectionInitializer = new ConnectionInitializerImpl();
this.connection = mock(Connection.class);
this.lock = new ReentrantLock();

dataSource.addInitializer(connectionInitializer);
dataSource.setConnection(connection);
dataSource.setLock(lock);
}

@Test
public void testGetConnection() throws SQLException {
Connection result = dataSource.getConnection();

verify(connection).isClosed();
assertEquals(connection, result);
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
String username = "testUser";
String password = "testPass";
Connection result = dataSource.getConnection(username, password);

verify(connection).isClosed();
assertEquals(connection, result);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
String username = "invalidUser";
String password = "invalidPass";
dataSource.getConnection(username, password);
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();

verify(connection).close();
assertNull(dataSource.getConnection());
}

@Test
public void testAddInitializer() {
List<ConnectionInitializer> initializers = new ArrayList<>();
ConnectionInitializer initializer1 = mock(ConnectionInitializer.class);
ConnectionInitializer initializer2 = mock(ConnectionInitializer.class);

initializers.add(initializer1);
initializers.add(initializer2);

dataSource.addInitializer(initializer1);
dataSource.addInitializer(initializer2);

assertEquals(initializers, dataSource.getInitializerList());
}

@Test
public void testClose() throws SQLException {
dataSource.close();

verify(connection).close();
}

@Test
public void testPrepareConnection() throws SQLException {
Boolean autoCommit = true;

dataSource.prepareConnection(connection);

verify(connection).setAutoCommit(autoCommit);
}

@Test
public void testGetConnectionProxy() throws SQLException {
Connection result = dataSource.getConnectionProxy(connection);

verify(lock).tryLock(10000, TimeUnit.MILLISECONDS);
verify(connection).isClosed();
assertEquals(result, Proxy.getInvocationHandler(result));
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithOccupiedConnection() throws SQLException {
dataSource.getConnectionProxy(connection);
dataSource.getConnectionProxy(connection);
}

@Test
public void testOnConnectionReset() throws Exception {
EventPublisher eventPublisher = mock(EventPublisher.class);
ConnectionResetEvent event = mock(ConnectionResetEvent.class);

dataSource.setEventPublisher(eventPublisher);
dataSource.onConnectionReset(connection);

verify(eventPublisher).publishEvent(event);
}

@Test
public void testInnerCreateConnection() throws SQLException {
Connection result = dataSource.innerCreateConnection();

verify(connection).close();
verify(connection).isClosed();
verify(connectionInitializer).init(connection);
assertEquals(result, dataSource.getConnection());
}

@After
public void tearDown() {
Mockito.framework().clearStubs();
}
}