import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
 private EventPublisher eventPublisher;

 @InjectMocks
 private OBConsoleDataSourceFactory dataSourceFactory;

 @Test
 public void testGetUsername() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername("test_user");
 connectionConfig.setDialectType(DialectType.OB_ORACLE);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 assertEquals("\"test_user\"", dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setPassword("test_password");
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 assertEquals("test_password", dataSourceFactory.getPassword());
 }

 @Test
 public void testGetConnectType() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setType(ConnectType.CONSOLE);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 assertEquals(ConnectType.CONSOLE, dataSourceFactory.getConnectType());
 }

 @Test
 public void testGetJdbcUrl() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("test_host");
 connectionConfig.setPort(1234);
 connectionConfig.setDefaultSchema("test_schema");
 Map<String, String> parameters = new HashMap<>();
 parameters.put("param1", "value1");
 connectionConfig.setParameters(parameters);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 String jdbcUrl = dataSourceFactory.getJdbcUrl();

 // Check if the JDBC URL is in the correct format
 assertTrue(jdbcUrl.contains("jdbc:oceanbase:"));
 assertTrue(jdbcUrl.contains("test_host"));
 assertTrue(jdbcUrl.contains("1234"));
 assertTrue(jdbcUrl.contains("test_schema"));
 assertTrue(jdbcUrl.contains("param1=value1"));
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(new ConnectionConfig());

 // Check if all expected parameters are present
 assertTrue(jdbcParams.containsKey("maxAllowedPacket"));
 assertTrue(jdbcParams.containsKey("allowMultiQueries"));
 assertTrue(jdbcParams.containsKey("connectTimeout"));
 assertTrue(jdbcParams.containsKey("zeroDateTimeBehavior"));
 assertTrue(jdbcParams.containsKey("noDatetimeStringSync"));
 assertTrue(jdbcParams.containsKey("jdbcCompliantTruncation"));
 assertTrue(jdbcParams.containsKey("sendConnectionAttributes"));
 }

 @Test
 public void testGetDataSource() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("test_host");
 connectionConfig.setPort(1234);
 connectionConfig.setUsername("test_user");
 connectionConfig.setPassword("test_password");

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 DataSource dataSource = dataSourceFactory.getDataSource();

 // Check if the data source is an instance of SingleConnectionDataSource
 assertTrue(dataSource instanceof SingleConnectionDataSource);

 // Check if the data source has the correct URL, username, and password
 assertEquals("jdbc:oceanbase:", dataSource.getConnection().getMetaData().getURL());
 assertEquals("test_user", dataSource.getConnection().getMetaData().getUserName());
 assertEquals("test_password", dataSource.getConnection().getPassword());
 }

 @Test
 public void testGetDialectType() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 ConnectionConfig connectionConfig = new ConnectionConfig();

 CloneableDataSourceFactory cloneableDataSourceFactory = dataSourceFactory.deepCopy();

 // Check if the cloneable data source factory is an instance of OBConsoleDataSourceFactory
 assertTrue(cloneableDataSourceFactory instanceof OBConsoleDataSourceFactory);

 // Check if the cloneable data source factory has the correct dialect type and auto commit value
 assertEquals(dataSourceFactory.getDialectType(), cloneableDataSourceFactory.getDialectType());
 assertEquals(dataSourceFactory.isAutoCommit(), cloneableDataSourceFactory.isAutoCommit());

 // Modify the original connection config and check if the cloneable data source factory is not affected
 connectionConfig.setUsername("modified_username");
 assertEquals("test_user", cloneableDataSourceFactory.getUsername());
 }

 @Test
 public void testResetUsername() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername("test_user");
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 dataSourceFactory.resetUsername(string -> "modified_username");

 assertEquals("modified_username", dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setPassword("test_password");
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 dataSourceFactory.resetPassword(string -> "modified_password");

 assertEquals("modified_password", dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setHost("test_host");
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 dataSourceFactory.resetHost(string -> "modified_host");

 assertEquals("modified_host", dataSourceFactory.getHost());
 }

 @Test
 public void testResetPort() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setPort(1234);
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 dataSourceFactory.resetPort(integer -> 5678);

 assertEquals(Integer.valueOf(5678), dataSourceFactory.getPort());
 }

 @Test
 public void testResetSchema() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDefaultSchema("test_schema");
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 dataSourceFactory.resetSchema(string -> "modified_schema");

 assertEquals("modified_schema", dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 Map<String, String> parameters = new HashMap<>();
 parameters.put("param1", "value1");
 connectionConfig.setParameters(parameters);

 CloneableDataSourceFactory cloneableDataSourceFactory = dataSourceFactory.deepCopy();

 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("param1", "modified_value1");

 cloneableDataSourceFactory.resetParameters(map -> newParameters);

 assertEquals(newParameters, cloneableDataSourceFactory.getParameters());
 }

}