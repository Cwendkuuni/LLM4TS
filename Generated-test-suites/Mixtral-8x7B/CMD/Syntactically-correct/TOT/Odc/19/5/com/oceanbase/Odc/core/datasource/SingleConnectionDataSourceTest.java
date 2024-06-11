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
import java.util.concurrent.locks.Lock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private ConnectionInitializer initializer;
private Connection connection;
private Lock lock;
private DataSource mockDataSource;

@Before
public void setUp() throws Exception {
mockDataSource = mock(DataSource.class);
initializer = mock(ConnectionInitializer.class);
connection = mock(Connection.class);
lock = mock(Lock.class);

when(mockDataSource.getConnection()).thenReturn(connection);

dataSource = new SingleConnectionDataSource(mockDataSource, true, initializer, lock);
}

@After
public void tearDown() throws Exception {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection result = dataSource.getConnection();
assertEquals(connection, result);
verify(mockDataSource, times(1)).getConnection();
}

@Test
public void testGetConnectionWithInvalidCredentials() {
assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUsername", "invalidPassword"));
verify(mockDataSource, never()).getConnection();
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();
verify(mockDataSource, times(1)).getConnection();
}

@Test
public void testAddInitializer() {
dataSource.addInitializer(initializer);
verify(initializer, times(1)).init(any(Connection.class));
}

@Test
public void testGetConnectionProxy() throws SQLException {
Connection result = dataSource.getConnectionProxy(connection);
assertEquals(connection, result);
}

@Test
public void testGetConnectionProxyWithLockConflict() {
assertThrows(ConflictException.class, () -> dataSource.getConnectionProxy(connection));
}

@Test
public void testInitConnection() throws SQLException {
dataSource.initConnection(connection);
verify(initializer, times(1)).init(connection);
}

@Test
public void testOnConnectionCreated() {
dataSource.onConnectionCreated(connection);
verify(lock, times(1)).unlock();
}

@Test
public void testOnConnectionReset() {
dataSource.onConnectionReset(connection);
verify(initializer, times(1)).init(connection);
}

@Test(expected = IllegalStateException.class)
public void testInnerCreateConnectionWithExistingConnection() throws SQLException {
dataSource.innerCreateConnection();
}

private static class CloseIgnoreInvocationHandlerTest implements InvocationHandler {

private final Connection target;
private final Lock lock;

public CloseIgnoreInvocationHandlerTest(Connection target, Lock lock) {
this.target = target;
this.lock = lock;
}

@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
if ("equals".equals(method.getName())) {
return (proxy == args[0]);
} else if ("hashCode".equals(method.getName())) {
return System.identityHashCode(proxy);
} else if ("unwrap".equals(method.getName())) {
if (((Class<?>) args[0]).isInstance(proxy)) {
return proxy;
}
} else if ("isWrapperFor".equals(method.getName())) {
if (((Class<?>) args[0]).isInstance(proxy)) {
return true;
}
} else if ("close".equals(method.getName())) {
lock.unlock();
return null;
} else if ("isClosed".equals(method.getName())) {
return false;
}
try {
return method.invoke(this.target, args);
} catch (InvocationTargetException ex) {
throw ex.getTargetException();
}
}
}
}