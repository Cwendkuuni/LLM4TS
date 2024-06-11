import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.shared.event.EventPublisher;
import com.oceanbase.odc.core.shared.event.EventPublisherImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private DataSource dataSourceWithoutAutoReconnect;
private ConnectionInitializer connectionInitializer;
private EventPublisher eventPublisher;
private ReentrantLock lock;

@Before
public void setup() {
connectionInitializer = new ConnectionInitializerImpl();
eventPublisher = new EventPublisherImpl();
lock = new ReentrantLock();
dataSource = new SingleConnectionDataSource(true);
dataSource.addInitializer(connectionInitializer);
dataSource.setEventPublisher(eventPublisher);
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertFalse(connection.isClosed());
}

@Test
public void testGetConnectionWithInvalidUsernameAndPassword() {
assertThrows(SQLException.class, () -> dataSource.getConnection("invalidUser", "invalidPass"));
}

@Test
public void testGetConnectionWithNullUsernameAndPassword() {
assertThrows(SQLException.class, () -> dataSource.getConnection(null, null));
}

@Test(expected = SQLException.class)
public void testGetConnectionWithClosedConnection() throws SQLException {
Connection connection = dataSource.getConnection();
connection.close();
dataSource.getConnection();
}

@Test
public void testResetConnection() throws SQLException {
dataSource.getConnection();
dataSource.resetConnection();
Connection connection = dataSource.getConnection();
assertTrue(connection.isClosed());
}

@Test
public void testResetConnectionWithEventPublisher() throws SQLException {
dataSource.getConnection();
verify(eventPublisher, times(0)).publishEvent(any());
dataSource.resetConnection();
verify(eventPublisher, times(1)).publishEvent(any());
}

@Test
public void testAddInitializer() {
dataSource.addInitializer(connectionInitializer);
assertTrue(dataSource.getInitializerList().contains(connectionInitializer));
}

@Test
public void testClose() throws SQLException {
Connection connection = dataSource.getConnection();
dataSource.close();
assertThrows(SQLException.class, () -> connection.createStatement());
}

@Test
public void testCloseWithAlreadyClosedConnection() throws SQLException {
Connection connection = dataSource.getConnection();
connection.close();
dataSource.close();
}

@Test
public void testConstructorWithoutAutoReconnect() {
dataSourceWithoutAutoReconnect = new SingleConnectionDataSource();
assertFalse(dataSourceWithoutAutoReconnect.isAutoReconnect());
}

@Test(expected = SQLException.class)
public void testInnerCreateConnectionWithInvalidDriver() {
dataSource.setDriverClassName("invalidDriver");
dataSource.innerCreateConnection();
}

@Test
public void testTryLock() throws InterruptedException {
dataSource.setTimeOutMillis(100);
assertTrue(dataSource.tryLock());
}

@Test
public void testTryLockWithTimeout() throws InterruptedException {
dataSource.setTimeOutMillis(1);
assertFalse(dataSource.tryLock());
}

@After
public void tearDown() {
dataSource = null;
dataSourceWithoutAutoReconnect = null;
connectionInitializer = null;
eventPublisher = null;
lock = null;
}
}