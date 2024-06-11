import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.ODCException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SingleConnectionDataSourceTest {

@Before
public void setUp() {
// Initialize any mocks or test data here
}

@Test
public void testGetConnection() throws SQLException, ConflictException {
// Create a new instance of SingleConnectionDataSource
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

// Call getConnection() and verify the result
Connection connection = dataSource.getConnection();
assertNotNull(connection);
}

@Test(expected = ConflictException.class)
public void testGetConnectionWithConflict() throws SQLException, ConflictException {
// Create a new instance of SingleConnectionDataSource
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

// Mock the lock to return false for tryLock()
Lock lock = Mockito.mock(ReentrantLock.class);
when(lock.tryLock(anyLong(), any(TimeUnit.class))).thenReturn(false);
dataSource.lock = lock;

// Call getConnection() and verify the exception
dataSource.getConnection();
}

@Test
public void testGetConnectionWithInitializers() throws SQLException, ConflictException {
// Create a new instance of SingleConnectionDataSource
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

// Add some ConnectionInitializer mocks to the data source
List<ConnectionInitializer> initializers = new ArrayList<>();
ConnectionInitializer initializer1 = Mockito.mock(ConnectionInitializer.class);
ConnectionInitializer initializer2 = Mockito.mock(ConnectionInitializer.class);
initializers.add(initializer1);
initializers.add(initializer2);
dataSource.initializerList = initializers;

// Call getConnection() and verify the result
Connection connection = dataSource.getConnection();
assertNotNull(connection);

// Verify that the init() method of each ConnectionInitializer was called
verify(initializer1, times(1)).init(connection);
verify(initializer2, times(1)).init(connection);
}

@Test
public void testGetConnectionWithProxy() throws SQLException, ConflictException {
// Create a new instance of SingleConnectionDataSource
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

// Call getConnection() and verify the result
Connection connection = dataSource.getConnection();
assertNotNull(connection);

// Verify that the connection is a proxy that suppresses close calls
InvocationHandler handler = Proxy.getInvocationHandler(connection);
assertNotNull(handler);
assertTrue(handler instanceof SingleConnectionDataSource.CloseIgnoreInvocationHandler);

SingleConnectionDataSource.CloseIgnoreInvocationHandler closeIgnoreHandler =
(SingleConnectionDataSource.CloseIgnoreInvocationHandler) handler;
assertSame(dataSource.lock, closeIgnoreHandler.lock);
}

@Test
public void testResetConnection() throws SQLException, ConflictException {
// Create a new instance of SingleConnectionDataSource
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

// Call resetConnection() and verify the result
dataSource.resetConnection();
assertNull(dataSource.connection);
assertNull(dataSource.lock);

// Verify that onConnectionReset() was called with the correct argument
verify(dataSource.eventPublisher, times(1)).publishEvent(any(ConnectionResetEvent.class));
}

@Test(expected = SQLException.class)
public void testInnerCreateConnectionWithException() throws SQLException {
// Create a new instance of SingleConnectionDataSource
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();

// Mock newConnectionFromDriver() to throw an exception
when(dataSource.newConnectionFromDriver(anyString(), anyString())).thenThrow(new RuntimeException());

// Call innerCreateConnection() and verify the exception
dataSource.innerCreateConnection();
}
}