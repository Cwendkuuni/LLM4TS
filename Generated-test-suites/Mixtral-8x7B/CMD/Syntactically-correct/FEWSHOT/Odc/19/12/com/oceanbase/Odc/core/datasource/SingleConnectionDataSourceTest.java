import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.connectioninitializer.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.connectioninitializer.SimpleConnectionInitializer;
import com.oceanbase.odc.core.shared.event.ConnectionResetEvent;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import com.oceanbase.odc.core.shared.exception.SQLException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

@Slf4j
public class SingleConnectionDataSourceTest {

  private SingleConnectionDataSource dataSource;
  private ConnectionInitializer connectionInitializer;
  private Connection connection;
  private Lock lock;
  private EventPublisher eventPublisher;
  private ConcurrentMap<String, DataSource> dataSourceMap;

  @Before
  public void setUp() throws Exception {
    dataSource = new SingleConnectionDataSource();
    connectionInitializer = new SimpleConnectionInitializer();
    connection = mock(Connection.class);
    lock = new ReentrantLock();
    eventPublisher = mock(EventPublisher.class);
    dataSourceMap = new ConcurrentHashMap<>();
  }

  @After
  public void tearDown() throws SQLException {
    if (dataSource != null) {
      dataSource.close();
    }
  }

  @Test
  public void testConstructor() {
    assertEquals(false, dataSource.isAutoReconnect());
  }

  @Test
  public void testGetSetAutoCommit() {
    assertNull(dataSource.getAutoCommit());
    dataSource.setAutoCommit(true);
    assertEquals(true, dataSource.getAutoCommit());
  }

  @Test
  public void testGetSetEventPublisher() {
    assertNull(dataSource.getEventPublisher());
    dataSource.setEventPublisher(eventPublisher);
    assertEquals(eventPublisher, dataSource.getEventPublisher());
  }

  @Test
  public void testGetConnection() throws SQLException {
    when(connection.isClosed()).thenReturn(true);
    when(connection.isValid(anyInt())).thenReturn(false);
    dataSource.setConnection(connection);
    dataSource.getConnection();
    verify(connection, times(1)).close();
    assertNull(dataSource.getConnection());

    when(connection.isClosed()).thenReturn(false);
    when(connection.isValid(anyInt())).thenReturn(true);
    dataSource.setConnection(connection);
    dataSource.getConnection();
    verify(connection, times(1)).close();
    assertSame(connection, dataSource.getConnection());
  }

  @Test
  public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
    when(connection.isValid(anyInt())).thenReturn(true);
    dataSource.setConnection(connection);
    assertThrows(
        SQLException.class,
        () ->
            dataSource.getConnection(
                "invalid_username", "invalid_password"));
  }

  @Test
  public void testResetConnection() throws SQLException {
    when(connection.isValid(anyInt())).thenReturn(true);
    dataSource.setConnection(connection);
    dataSource.getConnection();
    dataSource.resetConnection();
    verify(connection, times(2)).close();
    assertNull(dataSource.getConnection());
  }

  @Test
  public void testAddInitializer() {
    dataSource.addInitializer(connectionInitializer);
    assertEquals(1, dataSource.getInitializerList().size());
  }

  @Test
  public void testClose() {
    when(connection.isValid(anyInt())).thenReturn(true);
    dataSource.setConnection(connection);
    dataSource.close();
    verify(connection, times(1)).close();
  }

  @Test
  public void testPrepareConnection() throws SQLException {
    dataSource.setAutoCommit(false);
    when(connection.getAutoCommit()).thenReturn(true);
    dataSource.prepareConnection(connection);
    verify(connection, times(1)).setAutoCommit(false);
  }

  @Test
  public void testTryLock() throws InterruptedException {
    when(connection.isValid(anyInt())).thenReturn(true);
    dataSource.setConnection(connection);
    assertTrue(dataSource.tryLock());
  }

  @Test
  public void testGetConnectionProxy() throws SQLException {
    when(connection.isValid(anyInt())).thenReturn(true);
    dataSource.setConnection(connection);
    dataSource.getConnectionProxy(connection);
    verify(connection, times(1)).close();
  }

  @Test
  public void testInitConnection() throws SQLException {
    when(connection.isValid(anyInt())).thenReturn(true);
    dataSource.setConnection(connection);
    dataSource.initConnection(connection);
    verify(connectionInitializer, times(1)).init(connection);
  }

  @Test
  public void testOnConnectionCreated() {
    dataSource.onConnectionCreated(connection);
    verifyNoMoreInteractions(connection);
  }

  @Test
  public void testOnConnectionReset() throws SQLException, InterruptedException {
    when(connection.isValid(anyInt())).thenReturn(true);
    dataSource.setConnection(connection);
    dataSource.setEventPublisher(eventPublisher);
    dataSource.onConnectionReset(connection);
    ArgumentCaptor<ConnectionResetEvent> captor =
        ArgumentCaptor.forClass(ConnectionResetEvent.class);
    verify(eventPublisher, times(1))
        .publishEvent(captor.capture());
    ConnectionResetEvent event = captor.getValue();
    assertEquals(connection, event.getConnection());
  }

  @Test
  public void testInnerCreateConnection() throws SQLException {
    when(connection.isValid(anyInt())).thenReturn(true);
    dataSource.innerCreateConnection();
    verify(connection, times(1)).close();
  }

  @Test
  public void testNewConnectionFromDriver() throws SQLException {
    String url = "jdbc:mysql://localhost:3306/db";
    dataSourceMap.put("test_key", dataSource);
    DataSource dataSourceSpy = spy(dataSource);
    doReturn(dataSourceMap).when(dataSourceSpy).getDataSourceMap();
    when(dataSourceSpy.getUrl()).thenReturn(url);
    when(dataSourceSpy.getDriver()).thenReturn("com.mysql.cj.jdbc.Driver");
    when(dataSourceSpy.getUsername()).thenReturn("username");
    when(dataSourceSpy.getPassword()).thenReturn("password");
    dataSourceSpy.newConnectionFromDriver(
        dataSourceSpy.getUsername(), dataSourceSpy.getPassword());
    verify(DriverManager.class, times(1)).getConnection(url, "username", "password");
  }

  @Test
  public void testCloseIgnoreInvocationHandler() {
    InvocationHandler handler =
        new SingleConnectionDataSource.CloseIgnoreInvocationHandler(connection, lock);
    Object proxy =
        Proxy.newProxyInstance(
            Connection.class.getClassLoader(),
            new Class[] {Connection.class},
            handler);
    Method closeMethod = Connection.class.getDeclaredMethod("close");
    closeMethod.invoke(proxy);
    verify(connection, times(1)).close();
  }

  @Test
  public void testConflictException() {
    String errorCode = "ConnectionOccupied";
    ConflictException conflictException =
        new ConflictException(errorCode, new Object[] {}, "test message");
    assertEquals(errorCode, conflictException.getErrorCode());
    assertEquals("test message", conflictException.getMessage());
  }
}