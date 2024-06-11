import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setUp() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("root");
 connectionConfig.setPassword("password");
 connectionConfig.setDefaultSchema("testdb");

 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(true);
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("/path/to/keystore.jks");
 sslFileEntry.setKeyStoreFilePassword("keystore_password");
 sslConfig.setSslFileEntry(sslFileEntry);
 connectionConfig.setSslConfig(sslConfig);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String expectedJdbcUrl = "jdbc:oceanbase:mysql://localhost:3306/testdb?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=false&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=true&trustServerCertificate=true&trustStore=/path/to/keystore.jks&trustStorePassword=keystore_password&keyStore=/path/to/keystore.jks&keyStorePassword=keystore_password&disableSslHostnameVerification=true";
 assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 assertEquals("root", dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 assertEquals("password", dataSourceFactory.getPassword());
 }

 @Test
 public void testGetConnectType() {
 assertEquals(ConnectType.CONSOLE, dataSourceFactory.getConnectType());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertEquals("jdbc:oceanbase:mysql://localhost:3306/testdb?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=false&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=true&trustServerCertificate=true&trustStore=/path/to/keystore.jks&trustStorePassword=keystore_password&keyStore=/path/to/keystore.jks&keyStorePassword=keystore_password&disableSslHostnameVerification=true",
 dataSource.getUrl());
 assertEquals("root", dataSource.getUsername());
 assertEquals("password", dataSource.getPassword());
 assertTrue(dataSource.isAutoCommit());
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copiedDataSourceFactory = dataSourceFactory.deepCopy();
 assertNotSame(dataSourceFactory, copiedDataSourceFactory);
 assertEquals(dataSourceFactory.getUsername(), copiedDataSourceFactory.getUsername());
 assertEquals(dataSourceFactory.getPassword(), copiedDataSourceFactory.getPassword());
 assertEquals(dataSourceFactory.getHost(), copiedDataSourceFactory.getHost());
 assertEquals(dataSourceFactory.getPort(), copiedDataSourceFactory.getPort());
 assertEquals(dataSourceFactory.getDefaultSchema(), copiedDataSourceFactory.getDefaultSchema());
 assertEquals(dataSourceFactory.getParameters(), copiedDataSourceFactory.getParameters());
 }

 @Test
 public void testResetUsername() {
 String newUsername = "new_username";
 dataSourceFactory.resetUsername(value -> newUsername);
 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "new_password";
 dataSourceFactory.resetPassword(value -> newPassword);
 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "new_host";
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
 String newSchema = "new_schema";
 dataSourceFactory.resetSchema(value -> newSchema);
 assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("key1", "value1");
 newParameters.put("key2", "value2");
 dataSourceFactory.resetParameters(value -> newParameters);
 assertEquals(newParameters, dataSourceFactory.getParameters());
 }

}