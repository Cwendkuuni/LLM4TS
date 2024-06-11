import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;

public class OBConsoleDataSourceFactoryTest {

 private ConnectionExtensionPoint connectionExtensionPoint;
 private ConnectionConfig connectionConfig;
 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setup() {
 connectionExtensionPoint = new TestConnectionExtensionPoint();
 connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setDefaultSchema("testSchema");
 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(true);
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("testKeystore.p12");
 sslFileEntry.setKeyStoreFilePassword("testPassword");
 sslFileEntry.setKeyStoreType("PKCS12");
 sslConfig.setSslFileEntry(sslFileEntry);
 connectionConfig.setSslConfig(sslConfig);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String expectedJdbcUrl = "jdbc:oceanbase:mysql://localhost:3306/testSchema?user=testUser&password=testPassword&maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
 + DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=true&trustServerCertificate=true&trustStore=testKeystore.p12&trustStorePassword=testPassword&keyStore=testKeystore.p12&keyStorePassword=testPassword&trustServerCertificate=false";
 assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
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
 assertEquals(ConnectType.LONG, dataSourceFactory.getConnectType());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertTrue(dataSource.isAutoCommit());
 assertEquals("jdbc:oceanbase:mysql://localhost:3306/testSchema?user=testUser&password=testPassword&maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior="
 + DEFAULT_ZERO_DATE_TIME_BEHAVIOR + "&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=true&trustServerCertificate=true&trustStore=testKeystore.p12&trustStorePassword=testPassword&keyStore=testKeystore.p12&keyStorePassword=testPassword&trustServerCertificate=false",
 dataSource.getUrl());
 assertEquals("testUser", dataSource.getUsername());
 assertEquals("testPassword", dataSource.getPassword());
 assertFalse(CollectionUtils.isEmpty(dataSource.getInitializers()));
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copyDataSourceFactory = dataSourceFactory.deepCopy();
 assertNotSame(dataSourceFactory, copyDataSourceFactory);
 assertEquals(dataSourceFactory.getUsername(), copyDataSourceFactory.getUsername());
 assertEquals(dataSourceFactory.getPassword(), copyDataSourceFactory.getPassword());
 assertEquals(dataSourceFactory.getConnectType(), copyDataSourceFactory.getConnectType());
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
 assertEquals(newHost, dataSourceFactory.connectionConfig.getHost());
 }

 @Test
 public void testResetPort() {
 int newPort = 4444;
 dataSourceFactory.resetPort(value -> newPort);
 assertEquals(newPort, dataSourceFactory.connectionConfig.getPort().intValue());
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newSchema";
 dataSourceFactory.resetSchema(value -> newSchema);
 assertEquals(newSchema, dataSourceFactory.connectionConfig.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("newParam1", "value1");
 newParameters.put("newParam2", "value2");
 dataSourceFactory.resetParameters(value -> newParameters);
 assertEquals(newParameters, dataSourceFactory.connectionConfig.getJdbcUrlParameters());
 }

}

class TestConnectionExtensionPoint implements ConnectionExtensionPoint {

 @Override
 public String generateJdbcUrl(String host, Integer port, String defaultSchema,
 Map<String, String> jdbcUrlParams) {
 return "jdbc:oceanbase:mysql://" + host + ":" + port + "/" + defaultSchema + "?" + StringUtils.collectionToDelimitedString(jdbcUrlParams.entrySet(), "&", e -> e.getKey() + "=" + e.getValue());
 }

 @Override
 public List<ConnectionInitializer> getConnectionInitializers() {
 return Collections.emptyList();
 }

 @Override
 public String getDriverClassName() {
 return "oceanbase.jdbc.Driver";
 }

}