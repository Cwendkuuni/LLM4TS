import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private ConnectionExtensionPoint connectionExtensionPoint;
 private ConnectionConfig connectionConfig;
 private OBConsoleDataSourceFactory dataSourceFactory;
 private EventPublisher eventPublisher;

 @Before
 public void setup() {
 connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
 eventPublisher = Mockito.mock(EventPublisher.class);

 Map<String, String> jdbcParams = new HashMap<>();
 jdbcParams.put("key1", "value1");
 jdbcParams.put("key2", "value2");

 connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setDefaultSchema("testSchema");
 connectionConfig.setJdbcUrlParameters(jdbcParams);
 connectionConfig.setSslConfig(new SSLConfig());

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, eventPublisher);
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
 public void testGetJdbcUrl() {
 String jdbcUrl = dataSourceFactory.getJdbcUrl();
 // Add assertions based on your expectation of the JDBC URL
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> jdbcParams = dataSourceFactory.getJdbcParams();
 assertTrue(jdbcParams.containsKey("key1"));
 assertTrue(jdbcParams.containsKey("key2"));
 }

 @Test
 public void testGetDataSource() {
 DataSource dataSource = dataSourceFactory.getDataSource();
 assertTrue(dataSource instanceof SingleConnectionDataSource);

 SingleConnectionDataSource singleConnectionDataSource = (SingleConnectionDataSource) dataSource;
 assertEquals(dataSourceFactory.getUsername(), singleConnectionDataSource.getUsername());
 assertEquals(dataSourceFactory.getPassword(), singleConnectionDataSource.getPassword());
 assertEquals(dataSourceFactory.getJdbcUrl(), singleConnectionDataSource.getUrl());
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory clonedFactory = dataSourceFactory.deepCopy();

 // Add assertions to ensure that the cloned factory is a deep copy
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

 assertEquals(newSchema, dataSourceFactory.connectionConfig.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParams = new HashMap<>();
 newParams.put("key3", "value3");

 dataSourceFactory.resetParameters(value -> newParams);

 Map<String, String> jdbcParams = dataSourceFactory.getJdbcParams();
 assertTrue(jdbcParams.containsKey("key3"));
 assertEquals("value3", jdbcParams.get("key3"));
 }

}