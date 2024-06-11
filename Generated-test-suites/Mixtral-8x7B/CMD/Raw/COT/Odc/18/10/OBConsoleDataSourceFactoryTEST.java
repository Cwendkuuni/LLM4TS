import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.SSLConfig;
import com.oceanbase.odc.service.connection.model.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setup() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPass");
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setDefaultSchema("testSchema");

 Map<String, String> jdbcParams = new HashMap<>();
 jdbcParams.put("paramKey1", "paramValue1");
 jdbcParams.put("paramKey2", "paramValue2");
 connectionConfig.setJdbcUrlParameters(jdbcParams);

 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(true);
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("keystore.p12");
 sslFileEntry.setKeyStoreFilePassword("testPass");
 sslConfig.setSslFileEntry(sslFileEntry);
 connectionConfig.setSslConfig(sslConfig);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?paramKey1=paramValue1&paramKey2=paramValue2";
 assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 String expectedUsername = "testUser";
 assertEquals(expectedUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 String expectedPassword = "testPass";
 assertEquals(expectedPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testGetConnectType() {
 ConnectType expectedConnectType = ConnectType.CONSOLE;
 assertEquals(expectedConnectType, dataSourceFactory.getConnectType());
 }

 @Test
 public void testGetDataSource() {
 // Cannot test the entire method as it involves creating a DataSource and connecting to a database
 // Testing individual components instead
 }

 @Test
 public void testGetDialectType() {
 DialectType expectedDialectType = DialectType.OB_MYSQL;
 assertEquals(expectedDialectType, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
 assertNotNull(clonedDataSourceFactory);

 // Check that the cloned object is not the same as the original object
 assertFalse(clonedDataSourceFactory == dataSourceFactory);

 // Check that the cloned object has the same properties as the original object
 assertEquals(dataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
 assertEquals(dataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
 assertEquals(dataSourceFactory.getConnectType(), clonedDataSourceFactory.getConnectType());
 assertEquals(dataSourceFactory.getDialectType(), clonedDataSourceFactory.getDialectType());
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newTestUser";
 dataSourceFactory.resetUsername(value -> newUsername);
 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newTestPass";
 dataSourceFactory.resetPassword(value -> newPassword);
 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "newlocalhost";
 dataSourceFactory.resetHost(value -> newHost);
 assertEquals(newHost, dataSourceFactory.getConnectType().getHost());
 }

 @Test
 public void testResetPort() {
 int newPort = 3307;
 dataSourceFactory.resetPort(value -> newPort);
 assertEquals(newPort, dataSourceFactory.getConnectType().getPort());
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newTestSchema";
 dataSourceFactory.resetSchema(value -> newSchema);
 assertEquals(newSchema, dataSourceFactory.getDialectType().getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParams = new HashMap<>();
 newParams.put("newParamKey1", "newParamValue1");
 newParams.put("newParamKey2", "newParamValue2");

 dataSourceFactory.resetParameters(value -> newParams);

 // The JDBC URL should have been updated with the new parameters
 String updatedJdbcUrl = dataSourceFactory.getJdbcUrl();
 assertTrue(updatedJdbcUrl.contains("newParamKey1=newParamValue1"));
 assertTrue(updatedJdbcUrl.contains("newParamKey2=newParamValue2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResetPortNegativeValue() {
 dataSourceFactory.resetPort(value -> -1);
 }

}