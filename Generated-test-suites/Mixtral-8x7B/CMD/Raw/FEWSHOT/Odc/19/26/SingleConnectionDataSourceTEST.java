import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializerImpl;
import com.oceanbase.odc.core.datasource.event.ConnectionResetEvent;
import com.oceanbase.odc.core.datasource.event.EventPublisher;
import com.oceanbase.odc.core.shared.exception.ConflictException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SingleConnectionDataSourceTest {

 @Mock
 private EventPublisher eventPublisher;

 private SingleConnectionDataSource dataSource;

 private ConnectionInitializer connectionInitializer;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 connectionInitializer = new ConnectionInitializerImpl();
 dataSource = new SingleConnectionDataSource(true);
 dataSource.setEventPublisher(eventPublisher);
 dataSource.addInitializer(connectionInitializer);
 }

 @After
 public void teardown() throws SQLException {
 dataSource.close();
 }

 @Test
 public void testGetConnection_ShouldReturnConnectionInstance() throws SQLException {
 Connection connection = dataSource.getConnection();
 assertNotNull(connection);
 }

 @Test
 public void testGetConnection_WithUsernameAndPassword_ShouldReturnConnectionInstance()
 throws SQLException {
 Connection connection = dataSource.getConnection("username", "password");
 assertNotNull(connection);
 }

 @Test
 public void testGetConnection_WithInvalidUsernameAndPassword_ShouldThrowException() {
 try {
 dataSource.getConnection("invalidUsername", "invalidPassword");
 fail("Expected SQLException");
 } catch (SQLException e) {
 assertEquals("Invalid username or password", e.getMessage());
 }
 }

 @Test
 public void testResetConnection_ShouldResetConnectionAndPublishEvent() throws Exception {
 dataSource.resetConnection();
 verify(eventPublisher).publishEvent(argThat(connection -> connection.equals(dataSource.getConnection())));
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionProxy_WithLockedConnection_ShouldThrowConflictException()
 throws SQLException {
 dataSource.getConnectionProxy(dataSource.getConnection());
 dataSource.getConnectionProxy(dataSource.getConnection());
 }

 @Test
 public void testGetConnectionProxy_WithUnlockedConnection_ShouldReturnConnectionProxy()
 throws SQLException {
 dataSource.getConnectionProxy(dataSource.getConnection()).close();
 }

 @Test
 public void testClose_ShouldCloseConnection() throws Exception {
 dataSource.getConnection();
 dataSource.close();
 verify(eventPublisher).publishEvent(argThat(connection -> connection.isClosed()));
 }

 @Test
 public void testPrepareConnection_ShouldSetAutoCommit() throws SQLException {
 Connection connection = mock(Connection.class);
 dataSource.prepareConnection(connection);
 verify(connection).setAutoCommit(dataSource.getAutoCommit());
 }

 @Test
 public void testInnerCreateConnection_ShouldCreateConnectionAndInitialize()
 throws SQLException {
 when(dataSource.newConnectionFromDriver(anyString(), anyString())).thenReturn(mock(Connection.class));
 dataSource.innerCreateConnection();
 verify(connectionInitializer).init(dataSource.getConnection());
 verify(dataSource).onConnectionCreated(dataSource.getConnection());
 }

 private class ConnectionInitializerImpl implements ConnectionInitializer {

 @Override
 public void init(Connection connection) throws Exception {
 // Initialization logic
 }
 }
}