import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

@Mock
private EventPublisher eventPublisher;

private SingleConnectionDataSource dataSource;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
dataSource = new SingleConnectionDataSource(eventPublisher);
}

@After
public void tearDown() {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
}

@Test
public void testGetConnectionWithUsernameAndPassword() throws SQLException {
Connection connection = dataSource.getConnection("testUser", "testPass");
assertNotNull(connection);
}

@Test
public void testGetConnectionWithInvalidUsernameAndPassword() {
SQLException exception = assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPass"));
assertEquals("Invalid username or password", exception.getMessage());
}

@Test
public void testResetConnection() throws SQLException {
dataSource.getConnection();
dataSource.resetConnection();
verify(eventPublisher).publishEvent(new ConnectionResetEvent(any(Connection.class)));
}

@Test
public void testAddInitializer() {
ConnectionInitializer initializer = Mockito.mock(ConnectionInitializer.class);
dataSource.addInitializer(initializer);
assertEquals(1, dataSource.getInitializerList().size());
}

@Test
public void testClose() throws SQLException {
Connection connection = dataSource.getConnection();
dataSource.close();
assertThrows(SQLException.class, connection::close);
}

private static class ConnectionResetEvent {
Connection connection;

ConnectionResetEvent(Connection connection) {
this.connection = connection;
}
}

private static class ConnectionInitializer {
public void init(Connection connection) throws SQLException {
// Initialization logic
}
}

private static class CloseIgnoreInvocationHandler implements InvocationHandler {
private final Connection target;
private final Lock lock;

public CloseIgnoreInvocationHandler(Connection target, Lock lock) {
this.target = target;
this.lock = lock;
}

@Override
@SuppressWarnings("all")
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
// Handle method invocations on the proxy
}
}
}