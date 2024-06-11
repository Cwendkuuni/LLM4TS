import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setup() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost("127.0.0.1");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("test");
 connectionConfig.setPassword("test");
 connectionConfig.setDefaultSchema("testdb");

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 assertTrue(jdbcUrl.contains("127.0.0.1"));
 assertTrue(jdbcUrl.contains("3306"));
 assertTrue(jdbcUrl.contains("testdb"));
 }

 @Test
 public void testGetUsername() {
 assertEquals(dataSourceFactory.getUsername(), "test");
 }

 @Test
 public void testGetPassword() {
 assertEquals(dataSourceFactory.getPassword(), "test");
 }

 @Test
 public void testGetConnectType() {
 assertEquals(dataSourceFactory.getConnectType(), ConnectType.CONSOLE);
 }

 @Test
 public void testGetDataSource() {
 DataSource dataSource = dataSourceFactory.getDataSource();
 assertEquals(dataSource.getClass(), SingleConnectionDataSource.class);
 }

 @Test
 public void testGetDialectType() {
 assertEquals(dataSourceFactory.getDialectType(), DialectType.OB_MYSQL);
 }

 @Test
 public void testDeepCopy() {
 CloneableDataSourceFactory clone = dataSourceFactory.deepCopy();
 assertNotSame(dataSourceFactory, clone);
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newusername";
 dataSourceFactory.resetUsername(value -> newUsername);
 assertEquals(dataSourceFactory.getUsername(), newUsername);
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newpassword";
 dataSourceFactory.resetPassword(value -> newPassword);
 assertEquals(dataSourceFactory.getPassword(), newPassword);
 }

 @Test
 public void testResetHost() {
 String newHost = "192.168.1.1";
 dataSourceFactory.resetHost(value -> newHost);
 assertEquals(dataSourceFactory.getHost(), newHost);
 }

 @Test
 public void testResetPort() {
 int newPort = 3307;
 dataSourceFactory.resetPort(value -> newPort);
 assertEquals(dataSourceFactory.getPort(), newPort);
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newschema";
 dataSourceFactory.resetSchema(value -> newSchema);
 assertEquals(dataSourceFactory.getDefaultSchema(), newSchema);
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParams = new HashMap<>();
 newParams.put("param1", "value1");
 newParams.put("param2", "value2");

 dataSourceFactory.resetParameters(value -> newParams);

 Map<String, String> parameters = dataSourceFactory.getParameters();

 assertTrue(parameters.containsKey("param1"));
 assertTrue(parameters.containsKey("param2"));
 }

 @Test(expected = AssertionError.class)
 public void testResetParametersWithNullValue() {
 dataSourceFactory.resetParameters(value -> null);
 }

 @Test(expected = AssertionError.class)
 public void testResetParametersWithEmptyMap() {
 dataSourceFactory.resetParameters(value -> new HashMap<>());
 }

 @Test
 public void testGetSSLParams() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setSslConfig(new SSLConfig());

 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 Map<String, String> sslParams = dataSourceFactory.getSSLParams();
 assertTrue(sslParams.isEmpty());

 connectionConfig.getSslConfig().setEnabled(true);
 sslParams = dataSourceFactory.getSSLParams();
 assertFalse(sslParams.isEmpty());
 }

 @Test
 public void testGetSSLParamsWithSslFileEntry() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setSslConfig(new SSLConfig());
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("keystore.p12");
 sslFileEntry.setKeyStoreFilePassword("password");
 connectionConfig.getSslConfig().setSslFileEntry(sslFileEntry);

 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 Map<String, String> sslParams = dataSourceFactory.getSSLParams();

 assertTrue(sslParams.containsKey("trustStore"));
 assertTrue(sslParams.containsKey("trustStorePassword"));
 assertTrue(sslParams.containsKey("keyStore"));
 assertTrue(sslParams.containsKey("keyStorePassword"));
 }

 @Test
 public void testGetSSLParamsWithSslFileEntryAndHostnameVerificationDisabled() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setSslConfig(new SSLConfig());
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("keystore.p12");
 sslFileEntry.setKeyStoreFilePassword("password");
 sslFileEntry.setDisableHostnameVerification(true);
 connectionConfig.getSslConfig().setSslFileEntry(sslFileEntry);

 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 Map<String, String> sslParams = dataSourceFactory.getSSLParams();

 assertTrue(sslParams.containsKey("trustStore"));
 assertTrue(sslParams.containsKey("trustStorePassword"));
 assertTrue(sslParams.containsKey("keyStore"));
 assertTrue(sslParams.containsKey("keyStorePassword"));
 assertFalse(sslParams.containsKey("trustServerCertificate"));
 }

 @Test
 public void testGetSSLParamsWithSslFileEntryAndHostnameVerificationEnabled() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setSslConfig(new SSLConfig());
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("keystore.p12");
 sslFileEntry.setKeyStoreFilePassword("password");
 connectionConfig.getSslConfig().setSslFileEntry(sslFileEntry);

 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 Map<String, String> sslParams = dataSourceFactory.getSSLParams();

 assertTrue(sslParams.containsKey("trustStore"));
 assertTrue(sslParams.containsKey("trustStorePassword"));
 assertTrue(sslParams.containsKey("keyStore"));
 assertTrue(sslParams.containsKey("keyStorePassword"));
 assertTrue(sslParams.containsKey("trustServerCertificate"));
 }

 @Test
 public void testGetSSLParamsWithSslFileEntryAndHostnameVerificationEnabledAndTrustStore() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setSslConfig(new SSLConfig());
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("keystore.p12");
 sslFileEntry.setKeyStoreFilePassword("password");
 sslFileEntry.setTrustStoreFilePath("truststore.p12");
 sslFileEntry.setTrustStoreFilePassword("password");
 connectionConfig.getSslConfig().setSslFileEntry(sslFileEntry);

 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 Map<String, String> sslParams = dataSourceFactory.getSSLParams();

 assertTrue(sslParams.containsKey("trustStore"));
 assertTrue(sslParams.containsKey("trustStorePassword"));
 assertTrue(sslParams.containsKey("keyStore"));
 assertTrue(sslParams.containsKey("keyStorePassword"));
 assertTrue(sslParams.containsKey("trustServerCertificate"));
 }
}