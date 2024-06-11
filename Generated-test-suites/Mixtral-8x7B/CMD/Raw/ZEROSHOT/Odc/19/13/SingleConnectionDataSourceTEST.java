import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource.ConnectionInitializer;
import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.core.shared.exception.ConflictException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class SingleConnectionDataSourceTest {
 private static final Logger LOGGER = LoggerFactory.getLogger(SingleConnectionDataSourceTest.class);
 private SingleConnectionDataSource dataSource;
 private DataSource testDataSource;

 @Before
 public void setUp() {
 dataSource = new SingleConnectionDataSource();
 }

 @After
 public void tearDown() {
 if (dataSource != null) {
 dataSource.close();
 }
 }

 @Test
 public void testGetConnection() throws SQLException {
 Connection connection = dataSource.getConnection();
 Assert.assertNotNull(connection);
 }

 @Test
 public void testGetConnectionWithUsernameAndPassword() throws SQLException {
 dataSource.setUsername("testUser");
 dataSource.setPassword("testPassword");
 Connection connection = dataSource.getConnection("testUser", "testPassword");
 Assert.assertNotNull(connection);
 }

 @Test(expected = SQLException.class)
 public void testGetConnectionWithInvalidUsernameAndPassword() throws SQLException {
 dataSource.setUsername("testUser");
 dataSource.setPassword("testPassword");
 dataSource.getConnection("invalidUser", "invalidPassword");
 }

 @Test
 public void testResetConnection() throws SQLException {
 dataSource.getConnection();
 dataSource.resetConnection();
 Assert.assertNull(dataSource.getConnection());
 }

 @Test
 public void testAddInitializer() {
 ConnectionInitializer initializer1 = (Connection connection) -> {
 LOGGER.info("Initializer 1 called");
 };
 ConnectionInitializer initializer2 = (Connection connection) -> {
 LOGGER.info("Initializer 2 called");
 };
 dataSource.addInitializer(initializer1);
 dataSource.addInitializer(initializer2);
 Assert.assertEquals(2, dataSource.getInitializerList().size());
 }

 @Test
 public void testConnectionProxy() throws SQLException {
 Connection connection = dataSource.getConnection();
 InvocationHandler handler = Proxy.getInvocationHandler(connection);
 Assert.assertTrue(handler instanceof SingleConnectionDataSource.CloseIgnoreInvocationHandler);
 }

 @Test
 public void testInitConnection() throws SQLException {
 dataSource.setInitializerList(Arrays.asList(
 (Connection connection) -> {
 LOGGER.info("Initializer called");
 }
 ));
 dataSource.initConnection(newConnectionFromDriver("testUser", "testPassword"));
 }

 @Test
 public void testOnConnectionCreated() throws SQLException {
 dataSource.setOnConnectionCreated((connection) -> {
 LOGGER.info("Connection created");
 });
 dataSource.getConnection();
 }

 @Test
 public void testOnConnectionReset() throws SQLException {
 dataSource.setEventPublisher(new EventPublisher() {
 @Override
 public void publishEvent(Object event) {
 Assert.assertTrue(event instanceof SingleConnectionDataSource.ConnectionResetEvent);
 }
 });
 dataSource.getConnection();
 dataSource.resetConnection();
 }

 @Test
 public void testClose() throws SQLException {
 dataSource.getConnection();
 dataSource.close();
 Assert.assertNull(dataSource.getConnection());
 }

 @Test
 public void testInnerCreateConnection() throws SQLException {
 Connection connection = dataSource.innerCreateConnection();
 Assert.assertNotNull(connection);
 }

 @Test
 public void testMultipleThreads() throws InterruptedException {
 ExecutorService executor = Executors.newFixedThreadPool(10);
 ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
 for (int i = 0; i < 100; i++) {
 final int finalI = i;
 executor.submit(() -> {
 try {
 Connection connection = dataSource.getConnection();
 connections.put("Thread-" + finalI, connection);
 } catch (SQLException e) {
 LOGGER.error("Error occurred while getting connection", e);
 }
 });
 }
 executor.shutdown();
 executor.awaitTermination(1, TimeUnit.MINUTES);
 Assert.assertEquals(100, connections.size());
 }

 @Test(expected = ConflictException.class)
 public void testMultipleThreadsWithLock() throws InterruptedException, SQLException {
 ExecutorService executor = Executors.newFixedThreadPool(10);
 ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();
 for (int i = 0; i < 100; i++) {
 final int finalI = i;
 executor.submit(() -> {
 try {
 Connection connection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
 new Class[] {Connection.class},
 new SingleConnectionDataSource.CloseIgnoreInvocationHandler(dataSource.getConnection(),
 dataSource.getLock())
 );
 connections.put("Thread-" + finalI, connection);
 } catch (Exception e) {
 LOGGER.error("Error occurred while getting connection", e);
 }
 });
 }
 executor.shutdown();
 executor.awaitTermination(1, TimeUnit.MINUTES);
 Assert.assertEquals(100, connections.size());
 }

 private Connection newConnectionFromDriver(String user, String password) {
 // Implement this method to create a new Connection instance
 // for testing purposes.
 throw new UnsupportedOperationException("newConnectionFromDriver not implemented");
 }

}