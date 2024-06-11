import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

 @Mock
 private ConnectionExtensionPoint connectionExtensionPoint;

 @Mock
 private EventPublisher eventPublisher;

 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setup() {
 connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 eventPublisher = mock(EventPublisher.class);
 dataSourceFactory = new OBConsoleDataSourceFactory(getConnectionConfig(), true, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 // Verify the generated JDBC URL
 assertEquals("jdbc:oceanbase:oracle://host:port/defaultSchema", jdbcUrl);
 }

 @Test
 public void testGetUsername() {
 String username = dataSourceFactory.getUsername();
 // Verify the username
 assertEquals("username@OBTenantName#clusterName", username);
 }

 @Test
 public void testGetPassword() {
 String password = dataSourceFactory.getPassword();
 // Verify the password
 assertEquals("password", password);
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> jdbcParams = dataSourceFactory.getJdbcParams(getConnectionConfig());
 // Verify the JDBC parameters
 assertEquals("64000000", jdbcParams.get("maxAllowedPacket"));
 assertEquals("true", jdbcParams.get("allowMultiQueries"));
 assertEquals("5000", jdbcParams.get("connectTimeout"));
 assertEquals(DEFAULT_ZERO_DATE_TIME_BEHAVIOR, jdbcParams.get("zeroDateTimeBehavior"));
 assertEquals("true", jdbcParams.get("noDatetimeStringSync"));
 assertEquals("false", jdbcParams.get("jdbcCompliantTruncation"));
 assertEquals("false", jdbcParams.get("sendConnectionAttributes"));
 }

 @Test
 public void testGetDataSource() {
 DataSource dataSource = dataSourceFactory.getDataSource();
 // Verify the DataSource properties
 assertEquals("jdbc:oceanbase:oracle://host:port/defaultSchema", dataSource.getUrl());
 assertEquals("username", dataSource.getUsername());
 assertEquals("password", dataSource.getPassword());
 assertEquals(connectionExtensionPoint.getDriverClassName(), dataSource.getDriverClassName());
 Properties properties = dataSource.getConnectionProperties();
 assertEquals("false", properties.get("allowLoadLocalInfile").toString());
 assertEquals("false", properties.get("allowUrlInLocalInfile").toString());
 assertEquals("", properties.get("allowLoadLocalInfileInPath").toString());
 assertEquals("false", properties.get("autoDeserialize").toString());
 }

 @Test
 public void testGetDialectType() {
 DialectType dialectType = dataSourceFactory.getDialectType();
 // Verify the DialectType
 assertEquals(DialectType.OB_ORACLE, dialectType);
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();
 // Verify that the copy is a new instance
 assertNotSame(dataSourceFactory, copy);
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newUsername";
 dataSourceFactory.resetUsername(value -> newUsername);
 // Verify that the username has been reset
 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newPassword";
 dataSourceFactory.resetPassword(value -> newPassword);
 // Verify that the password has been reset
 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "newHost";
 dataSourceFactory.resetHost(value -> newHost);
 // Verify that the host has been reset
 assertEquals(newHost, dataSourceFactory.getHost());
 }

 @Test
 public void testResetPort() {
 int newPort = 1234;
 dataSourceFactory.resetPort(value -> newPort);
 // Verify that the port has been reset
 assertEquals(newPort, dataSourceFactory.getPort().intValue());
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newSchema";
 dataSourceFactory.resetSchema(value -> newSchema);
 // Verify that the schema has been reset
 assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParams = new HashMap<>();
 newParams.put("key", "value");
 dataSourceFactory.resetParameters(value -> newParams);
 // Verify that the parameters have been reset
 assertEquals(newParams, dataSourceFactory.getParameters());
 }

 private ConnectionConfig getConnectionConfig() {
 ConnectionConfig config = new ConnectionConfig();
 config.setDialectType(DialectType.OB_ORACLE);
 config.setUsername("username");
 config.setPassword("password");
 config.setHost("host");
 config.setPort(5000);
 config.setDefaultSchema("defaultSchema");
 config.setEndpoint(new OBTenantEndpoint());
 config.setSslConfig(new SSLConfig());
 config.setSslFileEntry(new SSLFileEntry());
 config.setJdbcUrlParameters(Collections.singletonMap("key", "value"));
 return config;
 }
}