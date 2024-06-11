import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setUp() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setDefaultSchema("testSchema");

 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(true);
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("/path/to/keyStore");
 sslFileEntry.setKeyStoreFilePassword("keyStorePassword");
 sslConfig.setSslFileEntry(sslFileEntry);
 connectionConfig.setSslConfig(sslConfig);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 // Assert the JDBC URL based on the provided connection configuration
 }

 @Test
 public void testGetUsername() {
 String username = dataSourceFactory.getUsername();
 assertEquals("testUser", username);
 }

 @Test
 public void testGetPassword() {
 String password = dataSourceFactory.getPassword();
 assertEquals("testPassword", password);
 }

 @Test
 public void testGetConnectType() {
 ConnectType connectType = dataSourceFactory.getConnectType();
 // Assert the connect type based on the provided connection configuration
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 // Assert the data source properties
 }

 @Test
 public void testGetDialectType() {
 DialectType dialectType = dataSourceFactory.getDialectType();
 assertEquals(DialectType.OB_MYSQL, dialectType);
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();
 // Assert that the copied instance has the same configuration
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newTestUser";
 dataSourceFactory.resetUsername(value -> newUsername);
 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newTestPassword";
 dataSourceFactory.resetPassword(value -> newPassword);
 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "newHost";
 dataSourceFactory.resetHost(value -> newHost);
 assertEquals(newHost, dataSourceFactory.getConnectType().getHost());
 }

 @Test
 public void testResetPort() {
 int newPort = 4444;
 dataSourceFactory.resetPort(value -> newPort);
 assertEquals(newPort, dataSourceFactory.getConnectType().getPort());
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newSchema";
 dataSourceFactory.resetSchema(value -> newSchema);
 // Assert that the default schema has been updated
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParams = new HashMap<>();
 newParams.put("newParam", "newValue");
 dataSourceFactory.resetParameters(value -> newParams);
 // Assert that the parameters have been updated
 }

}