import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private ConnectionExtensionPoint connectionExtensionPoint;
 private ConnectionConfig connectionConfig;
 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setup() {
 connectionExtensionPoint = new ConnectionExtensionPoint() {
 @Override
 public String generateJdbcUrl(String host, Integer port, String defaultSchema,
 Map<String, String> parameters) {
 return "jdbc:obmysql://" + host + ":" + port + "/" + defaultSchema;
 }

 @Override
 public String getDriverClassName() {
 return "com.oceanbase.odc.core.datasource.OBMySQLDriver";
 }

 @Override
 public List<ConnectionInitializer> getConnectionInitializers() {
 return null;
 }
 };

 connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setDefaultSchema("testSchema");

 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(true);
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("path/to/keystore.jks");
 sslFileEntry.setKeyStoreFilePassword("keystorePassword");
 sslConfig.setSslFileEntry(sslFileEntry);
 connectionConfig.setSslConfig(sslConfig);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 assertEquals("jdbc:obmysql://localhost:3306/testSchema", dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 assertEquals("testUser", dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 assertEquals("testPassword", dataSourceFactory.getPassword());
 }

 @Test
 public void testGetConnectType() {
 assertEquals(ConnectType.CONSOLE, dataSourceFactory.getConnectType());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertEquals("com.oceanbase.odc.core.datasource.OBMySQLDriver",
 dataSource.getDriverClassName());
 assertEquals("jdbc:obmysql://localhost:3306/testSchema", dataSource.getUrl());
 assertEquals("testUser", dataSource.getUsername());
 assertEquals("testPassword", dataSource.getPassword());
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copyFactory = dataSourceFactory.deepCopy();
 assertNotSame(dataSourceFactory, copyFactory);
 assertEquals(dataSourceFactory.getUsername(), copyFactory.getUsername());
 assertEquals(dataSourceFactory.getPassword(), copyFactory.getPassword());
 assertEquals(dataSourceFactory.getConnectType(), copyFactory.getConnectType());
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
 String newHost = "newLocalhost";
 dataSourceFactory.resetHost(value -> newHost);
 assertEquals(newHost, dataSourceFactory.getHost());
 }

 @Test
 public void testResetPort() {
 int newPort = 3307;
 dataSourceFactory.resetPort(value -> newPort);
 assertEquals(newPort, dataSourceFactory.getPort());
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newTestSchema";
 dataSourceFactory.resetSchema(value -> newSchema);
 assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("param1", "value1");
 newParameters.put("param2", "value2");
 dataSourceFactory.resetParameters(value -> newParameters);
 assertEquals(newParameters, dataSourceFactory.getParameters());
 }
}