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
import com.oceanbase.odc.core.datasource.CloneableDataSourceFactory;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

 @Mock
 private ConnectionExtensionPoint connectionExtensionPoint;

 @Mock
 private ConnectionPluginUtil connectionPluginUtil;

 @Mock
 private ConnectionMapper connectionMapper;

 @Mock
 private EventPublisher eventPublisher;

 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setup() {
 connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 connectionPluginUtil = mock(ConnectionPluginUtil.class);
 connectionMapper = mock(ConnectionMapper.class);
 eventPublisher = mock(EventPublisher.class);

 dataSourceFactory = new OBConsoleDataSourceFactory(getConnectionConfig(), true, true);
 }

 @Test
 public void testGetJdbcUrl() {
 when(connectionExtensionPoint.generateJdbcUrl("host", 1234, "defaultSchema", getParameters()))
 .thenReturn("jdbcUrl");

 assertEquals("jdbcUrl", dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 when(connectionPluginUtil.getDbUser("username", DialectType.OB_ORACLE))
 .thenReturn("dbUser");

 when(connectionPluginUtil.getTenantName("tenantName"))
 .thenReturn("tenant");

 when(connectionPluginUtil.getTenantClusterName("clusterName"))
 .thenReturn("cluster");

 assertEquals("\"dbUser\"@tenant@cluster", dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 assertEquals("password", dataSourceFactory.getPassword());
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> params = dataSourceFactory.getJdbcParams(getConnectionConfig());

 assertTrue(params.containsKey("maxAllowedPacket"));
 assertTrue(params.containsKey("allowMultiQueries"));
 assertTrue(params.containsKey("connectTimeout"));
 assertTrue(params.containsKey("zeroDateTimeBehavior"));
 assertTrue(params.containsKey("noDatetimeStringSync"));
 assertTrue(params.containsKey("jdbcCompliantTruncation"));
 assertTrue(params.containsKey("sendConnectionAttributes"));

 assertEquals("64000000", params.get("maxAllowedPacket"));
 assertEquals("true", params.get("allowMultiQueries"));
 assertEquals("5000", params.get("connectTimeout"));
 assertEquals(OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR, params.get("zeroDateTimeBehavior"));
 assertEquals("true", params.get("noDatetimeStringSync"));
 assertEquals("false", params.get("jdbcCompliantTruncation"));
 assertEquals("false", params.get("sendConnectionAttributes"));
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();

 assertEquals("jdbcUrl", dataSource.getUrl());
 assertEquals("username", dataSource.getUsername());
 assertEquals("password", dataSource.getPassword());

 Properties properties = dataSource.getConnectionProperties();

 assertEquals("false", properties.getProperty("allowLoadLocalInfile"));
 assertEquals("false", properties.getProperty("allowUrlInLocalInfile"));
 assertEquals("", properties.getProperty("allowLoadLocalInfileInPath"));
 assertEquals("false", properties.getProperty("autoDeserialize"));
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 CloneableDataSourceFactory clone = dataSourceFactory.deepCopy();

 assertNotSame(dataSourceFactory, clone);
 }

 @Test
 public void testResetUsername() {
 dataSourceFactory.resetUsername(str -> "newUsername");

 assertEquals("newUsername", dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 dataSourceFactory.resetPassword(str -> "newPassword");

 assertEquals("newPassword", dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 dataSourceFactory.resetHost(str -> "newHost");

 // host is not changed in this test since it is set in constructor
 assertEquals("host", dataSourceFactory.getJdbcUrl().split(":")[1]);
 }

 @Test
 public void testResetPort() {
 dataSourceFactory.resetPort(integer -> 5432);

 assertEquals("5432", dataSourceFactory.getJdbcUrl().split(":")[2].split("/")[0]);
 }

 @Test
 public void testResetSchema() {
 dataSourceFactory.resetSchema(str -> "newSchema");

 assertEquals("newSchema", dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParams = new HashMap<>();
 newParams.put("newParam", "newValue");

 dataSourceFactory.resetParameters(map -> newParams);

 assertTrue(dataSourceFactory.getJdbcParams(getConnectionConfig()).containsKey("newParam"));
 assertEquals("newValue", dataSourceFactory.getJdbcParams(getConnectionConfig()).get("newParam"));
 }

 private ConnectionConfig getConnectionConfig() {
 ConnectionConfig config = new ConnectionConfig();
 config.setUsername("username");
 config.setPassword("password");
 config.setHost("host");
 config.setPort(1234);
 config.setDialectType(DialectType.OB_ORACLE);
 config.setSslConfig(new SSLConfig());
 config.setSslConfig().setEnabled(false);
 config.setEndpoint(new OBTenantEndpoint());
 config.getEndpoint().setProxyHost("proxyHost");
 config.getEndpoint().setProxyPort(5678);
 config.getJdbcUrlParameters().put("param", "value");

 return config;
 }

 private Map<String, String> getParameters() {
 Map<String, String> params = new HashMap<>();

 params.put("maxAllowedPacket", "64000000");
 params.put("allowMultiQueries", "true");
 params.put("connectTimeout", "5000");
 params.put("zeroDateTimeBehavior", OdcConstants.DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
 params.put("noDatetimeStringSync", "true");
 params.put("jdbcCompliantTruncation", "false");
 params.put("sendConnectionAttributes", "false");

 return params;
 }
}