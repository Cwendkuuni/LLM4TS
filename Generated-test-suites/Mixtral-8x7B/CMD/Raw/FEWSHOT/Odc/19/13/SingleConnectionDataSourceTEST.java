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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.oceanbase.odc.core.datasource.ConnectionInitializer;
import org.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import org.oceanbase.odc.core.shared.constant.ErrorCodes;
import org.oceanbase.odc.core.shared.exception.ConflictException;

@RunWith(MockitoJUnitRunner.class)
public class SingleConnectionDataSourceTest {

 @Mock
 private ConnectionInitializer connectionInitializer;

 private SingleConnectionDataSource dataSource;

 @Before
 public void setUp() {
 dataSource = new SingleConnectionDataSource();
 dataSource.addInitializer(connectionInitializer);
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
 Connection connection = dataSource.getConnection("username", "password");
 assertNotNull(connection);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithWrongUsernameAndPassword() throws SQLException {
 dataSource.getConnection("wrongUsername", "wrongPassword");
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.resetConnection();
 }

 @Test
 public void testAddInitializer() {
 dataSource.addInitializer(connectionInitializer);
 }

 @Test
 public void testClose() {
 dataSource.close();
 }

 @Test
 public void testGetConnectionProxy() {
 Connection connection = mock(Connection.class);
 dataSource.getConnectionProxy(connection);
 }

 @Test(expected = ConflictException.class)
 public void testGetConnectionProxyWithConflict() throws SQLException {
 dataSource.getConnection();
 }

 @Test
 public void testNewConnectionFromDriver() throws SQLException {
 Connection connection = dataSource.newConnectionFromDriver("username", "password");
 assertNotNull(connection);
 }

 @Test
 public void testCloseIgnoreInvocationHandler() {
 InvocationHandler handler = dataSource.new CloseIgnoreInvocationHandler(mock(Connection.class), mock(Lock.class));
 handler.invoke(null, "close", null);
 }

 @Test
 public void testInitConnection() throws SQLException {
 Connection connection = mock(Connection.class);
 dataSource.initConnection(connection);
 }

 @Test
 public void testOnConnectionCreated() {
 dataSource.onConnectionCreated(mock(Connection.class));
 }

 @Test
 public void testOnConnectionReset() {
 dataSource.onConnectionReset(mock(Connection.class));
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 Connection connection = dataSource.innerCreateConnection();
 assertNotNull(connection);
 }

}