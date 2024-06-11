import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.SQLException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleConnectionDataSourceTest {

private static final Logger logger = LoggerFactory.getLogger(SingleConnectionDataSourceTest.class);
private SingleConnectionDataSource dataSource;
private Connection connection;
private Lock lock;
private EventPublisher eventPublisher;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource();
connection = Mockito.mock(Connection.class);
lock = new ReentrantLock();
eventPublisher = Mockito.mock(EventPublisher.class);
dataSource.setConnection(connection);
dataSource.setLock(lock);
dataSource.setEventPublisher(eventPublisher);
}

@Test
public void testGetConnection() throws SQLException {
Connection result = dataSource.getConnection();
Assert.assertEquals(connection, result);
}

@Test
public void testGetConnectionWithAutoCommit() throws SQLException {
dataSource.setAutoCommit(true);
Connection result = dataSource.getConnection();
Assert.assertTrue(result.getAutoCommit());
}

@Test(expected = SQLException.class)
public void testGetConnectionWithNullConnection() throws SQLException {
dataSource.setConnection(null);
dataSource.getConnection();
}

@Test
public void testAddInitializer() {
ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
dataSource.addInitializer(initializer);
List<ConnectionInitializer> result = dataSource.initializerList;
Assert.assertTrue(result.contains(initializer));
}

@Test
public void testClose() throws SQLException {
dataSource.close();
Mockito.verify(connection).close();
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();
Mockito.verify(connection).close();
Connection newConnection = dataSource.innerCreateConnection();
Assert.assertNotSame(connection, newConnection);
}

@Test
public void testGetConnectionProxy() {
ConnectionProxy proxy = dataSource.getConnectionProxy(connection);
Assert.assertSame(connection, proxy.getTarget());
Assert.assertTrue(proxy instanceof ConnectionProxy);
}

@Test
public void testPrepareConnection() throws SQLException {
Boolean autoCommit = true;
dataSource.setAutoCommit(autoCommit);
dataSource.prepareConnection(connection);
Assert.assertEquals(autoCommit, connection.getAutoCommit());
}

@Test
public void testTryLock() throws InterruptedException {
dataSource.setTimeOutMillis(1000);
Assert.assertTrue(dataSource.tryLock());
Assert.assertFalse(dataSource.tryLock());
}

@Test
public void testInitConnection() throws SQLException {
dataSource.initConnection(connection);
Mockito.verify(connection).setAutoCommit(dataSource.getAutoCommit());
}

@Test
public void testOnConnectionCreated() {
dataSource.onConnectionCreated(connection);
Mockito.verify(connection).isValid(Mockito.anyInt());
}

@Test
public void testOnConnectionReset() {
ConnectionResetEvent event = Mockito.mock(ConnectionResetEvent.class);
dataSource.onConnectionReset(connection);
Mockito.verify(eventPublisher).publishEvent(event);
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithLockConflict()
throws ConflictException, SQLException {
dataSource.getConnectionProxy(connection);
dataSource.getConnectionProxy(connection);
}

@Test
public void testInnerCreateConnection() throws SQLException {
Connection result = dataSource.innerCreateConnection();
Assert.assertSame(connection, result);
}

@Test(expected = IllegalStateException.class)
public void testInnerCreateConnectionWithNotClosedConnection() throws SQLException {
dataSource.setConnection(connection);
dataSource.innerCreateConnection();
}

static class ConnectionProxy implements Connection {

private final Connection target;
private final Lock lock;

public ConnectionProxy(Connection target, Lock lock) {
this.target = target;
this.lock = lock;
}

@Override
public Connection getTarget() {
return target;
}

@Override
public <T> T unwrap(Class<T> iface) throws SQLException {
return target.unwrap(iface);
}

@Override
public boolean isWrapperFor(Class<?> iface) throws SQLException {
return target.isWrapperFor(iface);
}

@Override
public void close() throws SQLException {
lock.unlock();
}

@Override
public boolean isClosed() throws SQLException {
return target.isClosed();
}

@Override
public boolean isReadOnly() throws SQLException {
return target.isReadOnly();
}

@Override
public void setReadOnly(boolean readOnly) throws SQLException {
target.setReadOnly(readOnly);
}

@Override
public void setAutoCommit(boolean autoCommit) throws SQLException {
target.setAutoCommit(autoCommit);
}

@Override
public boolean getAutoCommit() throws SQLException {
return target.getAutoCommit();
}

@Override
public void commit() throws SQLException {
target.commit();
}

@Override
public void rollback() throws SQLException {
target.rollback();
}

@Override
public void close() throws SQLException {
// Do nothing
}

@Override
public Statement createStatement() throws SQLException {
return target.createStatement();
}

@Override
public CallableStatement createCallableStatement(String sql) throws SQLException {
return target.createCallableStatement(sql);
}

@Override
public PreparedStatement prepareStatement(String sql) throws SQLException {
return target.prepareStatement(sql);
}

@Override
public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
throws SQLException {
return target.prepareStatement(sql, columnIndexes);
}

@Override
public PreparedStatement prepareStatement(String sql, String[] columnNames)
throws SQLException {
return target.prepareStatement(sql, columnNames);
}

@Override
public DatabaseMetaData getMetaData() throws SQLException {
return target.getMetaData();
}

@Override
public void setTransactionIsolation(int level) throws SQLException {
target.setTransactionIsolation(level);
}

@Override
public int getTransactionIsolation() throws SQLException {
return target.getTransactionIsolation();
}

@Override
public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
target.setTypeMap(map);
}

@Override
public Map<String, Class<?>> getTypeMap() throws SQLException {
return target.getTypeMap();
}

@Override
public PropertySetter getPropertySetter(String propertyName) throws SQLException {
return target.getPropertySetter(propertyName);
}

@Override
public PropertyGetter getPropertyGetter(String propertyName) throws SQLException {
return target.getPropertyGetter(propertyName);
}

@Override
public <T> T unwrap(Class<T> iface, Class<?> iface2) throws SQLException {
return target.unwrap(iface, iface2);
}

@Override
public boolean isWrapperFor(Class<?> iface, Class<?> iface2) throws SQLException {
return target.isWrapperFor(iface, iface2);
}
}

static class ConnectionInitializerMock implements ConnectionInitializer {

@Override
public void init(Connection connection) throws Exception {
// Do nothing
}
}

static class CloseIgnoreInvocationHandlerMock implements InvocationHandler {

private final Connection target;
private final Lock lock;

public CloseIgnoreInvocationHandlerMock(Connection target, Lock lock) {
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
return method.invoke(target, args);
}
}
}