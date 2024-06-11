import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource dataSourceWithInitializer;
private ConnectionInitializer connectionInitializer;
private Connection connection;
private ReentrantLock lock;

@Before
public void setUp() throws Exception {
dataSource = new SingleConnectionDataSource();
connectionInitializer = new ConnectionInitializerImpl();
dataSourceWithInitializer = new SingleConnectionDataSource();
dataSourceWithInitializer.addInitializer(connectionInitializer);
lock = new ReentrantLock();
}

@After
public void tearDown() throws Exception {
dataSource.close();
dataSourceWithInitializer.close();
}

@Test
public void testGetConnection() throws SQLException {
connection = dataSource.getConnection();
assertFalse(connection.isClosed());
}

@Test
public void testGetConnectionWithUsernameAndPassword() {
assertThrows(SQLException.class, () -> {
dataSource.getConnection("invalidUsername", "invalidPassword");
});
}

@Test
public void testAddInitializer() throws Exception {
dataSourceWithInitializer.getConnection();
assertTrue(connectionInitializer.isInitCalled());
}

@Test
public void testResetConnection() throws Exception {
dataSource.getConnection();
dataSource.resetConnection();
assertThrows(ConflictException.class, () -> {
dataSource.getConnection();
});
}

@Test
public void testGetConnectionProxy() throws Exception {
connection = dataSource.getConnection();
InvocationHandler handler = Proxy.getInvocationHandler(connection);
assertTrue(handler instanceof CloseIgnoreInvocationHandler);
}

@Test
public void testClose() throws Exception {
dataSource.getConnection();
dataSource.close();
assertThrows(IllegalStateException.class, () -> {
dataSource.getConnection();
});
}

@Test
public void testInnerCreateConnection() throws Exception {
connection = dataSource.innerCreateConnection();
assertFalse(connection.isClosed());
}

@Test
public void testConnectionResetEvent() throws Exception {
dataSource.setEventPublisher(event -> {
assertEquals(dataSource.getConnection(), event.getConnection());
});
dataSource.getConnection();
dataSource.resetConnection();
}

class ConnectionInitializerImpl implements ConnectionInitializer {

private boolean isInitCalled;

@Override
public void init(Connection connection) throws Exception {
isInitCalled = true;
}

public boolean isInitCalled() {
return isInitCalled;
}

}

class ConnectionResetEvent {

private Connection connection;

public ConnectionResetEvent(Connection connection) {
this.connection = connection;
}

public Connection getConnection() {
return connection;
}

}
}