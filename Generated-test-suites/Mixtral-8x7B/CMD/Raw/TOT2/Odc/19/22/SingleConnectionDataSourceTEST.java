import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SingleConnectionDataSourceTest {

private SingleConnectionDataSource dataSource;
private ConnectionInitializer connectionInitializer;
private EventPublisher eventPublisher;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource();
connectionInitializer = mock(ConnectionInitializer.class);
dataSource.addInitializer(connectionInitializer);
eventPublisher = mock(EventPublisher.class);
dataSource.setEventPublisher(eventPublisher);
}

@After
public void teardown() throws SQLException {
dataSource.close();
}

@Test
public void testGetConnection() throws SQLException {
Connection connection = dataSource.getConnection();
assertNotNull(connection);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithNullUsernameAndPassword() throws SQLException {
dataSource.getConnection(null, null);
}

@Test(expected = SQLException.class)
public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
dataSource.getConnection("invalid_username", "invalid_password");
}

@Test
public void testResetConnection() throws SQLException {
dataSource.resetConnection();
verify(connectionInitializer, times(1)).init(dataSource.getConnection());
}

@Test
public void testAddInitializer() {
dataSource.addInitializer(connectionInitializer);
assertEquals(1, dataSource.getInitializerList().size());
}

@Test
public void testClose() throws SQLException {
dataSource.close();
verify(connectionInitializer, times(1)).close();
}

@Test
public void testGetConnectionProxy() throws Exception {
dataSource.getConnectionProxy(mock(Connection.class));
verify(dataSource.getLock(), times(1)).tryLock(dataSource.getTimeOutMillis(), TimeUnit.MILLISECONDS);
}

@Test(expected = ConflictException.class)
public void testGetConnectionProxyWithConflict() throws Exception {
Lock lock = mock(Lock.class);
when(lock.tryLock(dataSource.getTimeOutMillis(), TimeUnit.MILLISECONDS)).thenReturn(false);
dataSource.setLock(lock);
dataSource.getConnectionProxy(mock(Connection.class));
}

@Test
public void testInnerCreateConnection() throws Exception {
Connection connection = dataSource.innerCreateConnection();
assertNotNull(connection);
verify(connectionInitializer, times(1)).init(connection);
dataSource.onConnectionCreated(connection);
}

@Test
public void testOnConnectionReset() throws Exception {
Connection connection = mock(Connection.class);
dataSource.onConnectionReset(connection);
verify(eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
}

@Test
public void testMultipleThreads() throws InterruptedException, SQLException {
ExecutorService executor = Executors.newFixedThreadPool(10);
for (int i = 0; i < 10; i++) {
executor.submit(() -> {
try {
dataSource.getConnection();
} catch (SQLException e) {
fail("Unexpected exception: " + e.getMessage());
}
});
}
executor.shutdown();
executor.awaitTermination(5, TimeUnit.SECONDS);
}

}