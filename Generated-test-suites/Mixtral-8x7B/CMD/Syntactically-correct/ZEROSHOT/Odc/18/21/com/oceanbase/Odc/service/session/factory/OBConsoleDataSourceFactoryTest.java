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
import org.springframework.util.CollectionUtils;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;
 private ConnectionConfig connectionConfig;
 private ConnectionExtensionPoint connectionExtensionPoint;
 private EventPublisher eventPublisher;

 @Before
 public void setup() {
 connectionConfig = new ConnectionConfig();
 connectionExtensionPoint = Mockito.mock(ConnectionExtensionPoint.class);
 eventPublisher = Mockito.mock(EventPublisher.class);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, connectionExtensionPoint, eventPublisher);
 }

 @Test
 public void testGetUsername() {
 String expectedUsername = "testUser";
 connectionConfig.setUsername(expectedUsername);
 assertEquals(expectedUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 String expectedPassword = "testPassword";
 connectionConfig.setPassword(expectedPassword);
 assertEquals(expectedPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testGetHost() {
 String expectedHost = "testHost";
 connectionConfig.setHost(expectedHost);
 assertEquals(expectedHost, dataSourceFactory.getHost());
 }

 @Test
 public void testGetPort() {
 Integer expectedPort = 1234;
 connectionConfig.setPort(expectedPort);
 assertEquals(expectedPort, dataSourceFactory.getPort());
 }

 @Test
 public void testGetDefaultSchema() {
 String expectedDefaultSchema = "testSchema";
 connectionConfig.setDefaultSchema(expectedDefaultSchema);
 assertEquals(expectedDefaultSchema, dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> expectedJdbcParams = new HashMap<>();
 expectedJdbcParams.put("key1", "value1");
 expectedJdbcParams.put("key2", "value2");
 connectionConfig.setJdbcUrlParameters(expectedJdbcParams);
 assertEquals(expectedJdbcParams, dataSourceFactory.getJdbcParams());
 }

 @Test
 public void testGetJdbcUrl() {
 String expectedJdbcUrl = "jdbc:test:testHost:1234/testSchema";
 connectionConfig.setHost("testHost");
 connectionConfig.setPort(1234);
 connectionConfig.setDefaultSchema("testSchema");
 Mockito.when(connectionExtensionPoint.generateJdbcUrl(Mockito.anyString(), Mockito.anyInt(),
 Mockito.anyString(), Mockito.anyMap())).thenReturn(expectedJdbcUrl);
 assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource expectedDataSource = new SingleConnectionDataSource();
 expectedDataSource.setUrl(getJdbcUrl());
 expectedDataSource.setUsername(getUsername());
 expectedDataSource.setPassword(getPassword());
 expectedDataSource.setDriverClassName(connectionExtensionPoint.getDriverClassName());

 dataSourceFactory.getDataSource();
 Mockito.verify(eventPublisher).register(expectedDataSource);
 }

 @Test
 public void testGetDialectType() {
 DialectType expectedDialectType = DialectType.OB_MYSQL;
 connectionConfig.setDialectType(expectedDialectType);
 assertEquals(expectedDialectType, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 CloneableDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
 assertNotSame(dataSourceFactory, clonedDataSourceFactory);
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newUsername";
 CloneableDataSourceFactory.ValueMapper<String> valueMapper = new CloneableDataSourceFactory.ValueMapper<String>() {
 @Override
 public String map(String value) {
 return newUsername;
 }
 };
 dataSourceFactory.resetUsername(valueMapper);
 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newPassword";
 CloneableDataSourceFactory.ValueMapper<String> valueMapper = new CloneableDataSourceFactory.ValueMapper<String>() {
 @Override
 public String map(String value) {
 return newPassword;
 }
 };
 dataSourceFactory.resetPassword(valueMapper);
 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "newHost";
 CloneableDataSourceFactory.ValueMapper<String> valueMapper = new CloneableDataSourceFactory.ValueMapper<String>() {
 @Override
 public String map(String value) {
 return newHost;
 }
 };
 dataSourceFactory.resetHost(valueMapper);
 assertEquals(newHost, dataSourceFactory.getHost());
 }

 @Test
 public void testResetPort() {
 Integer newPort = 5678;
 CloneableDataSourceFactory.ValueMapper<Integer> valueMapper = new CloneableDataSourceFactory.ValueMapper<Integer>() {
 @Override
 public Integer map(Integer value) {
 return newPort;
 }
 };
 dataSourceFactory.resetPort(valueMapper);
 assertEquals(newPort, dataSourceFactory.getPort());
 }

 @Test
 public void testResetSchema() {
 String newDefaultSchema = "newSchema";
 CloneableDataSourceFactory.ValueMapper<String> valueMapper = new CloneableDataSourceFactory.ValueMapper<String>() {
 @Override
 public String map(String value) {
 return newDefaultSchema;
 }
 };
 dataSourceFactory.resetSchema(valueMapper);
 assertEquals(newDefaultSchema, dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newJdbcParams = new HashMap<>();
 newJdbcParams.put("key3", "value3");
 newJdbcParams.put("key4", "value4");
 CloneableDataSourceFactory.ValueMapper<Map<String, String>> valueMapper = new CloneableDataSourceFactory.ValueMapper<Map<String, String>>() {
 @Override
 public Map<String, String> map(Map<String, String> value) {
 return newJdbcParams;
 }
 };
 dataSourceFactory.resetParameters(valueMapper);
 assertEquals(newJdbcParams, dataSourceFactory.getJdbcParams());
 }

 private String getJdbcUrl() {
 String host = "testHost";
 Integer port = 1234;
 String defaultSchema = "testSchema";
 Map<String, String> jdbcParams = new HashMap<>();
 jdbcParams.put("key1", "value1");
 jdbcParams.put("key2", "value2");
 return connectionExtensionPoint.generateJdbcUrl(host, port, defaultSchema, jdbcParams);
 }

 private String getUsername() {
 return "testUser";
 }

}