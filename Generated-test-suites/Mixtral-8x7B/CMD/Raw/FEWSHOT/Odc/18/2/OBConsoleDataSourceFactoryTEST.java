import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.common.event.EventPublisher;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.plugin.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 @Mock
 private ConnectionExtensionPoint connectionExtensionPoint;

 @Mock
 private ConnectionMapper connectionMapper;

 @Mock
 private ConnectionPluginUtil connectionPluginUtil;

 @Mock
 private EventPublisher eventPublisher;

 @Mock
 private ConnectionSessionUtil connectionSessionUtil;

 @InjectMocks
 private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetJdbcUrl() {
 String host = "127.0.0.1";
 Integer port = 3306;
 String defaultSchema = "test_schema";
 Map<String, String> parameters = new HashMap<>();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost(host);
 connectionConfig.setPort(port);
 connectionConfig.setDefaultSchema(defaultSchema);
 connectionConfig.setJdbcUrlParameters(parameters);
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

 String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();

 assertEquals("jdbc:oceanbase:mysql://127.0.0.1:3306/test_schema", jdbcUrl);
 }

 @Test
 public void testGetJdbcUrlWithOBOracleDialectType() {
 String host = "127.0.0.1";
 Integer port = 1521;
 String defaultSchema = "test_schema";
 Map<String, String> parameters = new HashMap<>();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_ORACLE);
 connectionConfig.setHost(host);
 connectionConfig.setPort(port);
 connectionConfig.setDefaultSchema(defaultSchema);
 connectionConfig.setJdbcUrlParameters(parameters);
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

 String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();

 assertEquals("jdbc:oceanbase:oracle://127.0.0.1:1526/test_schema", jdbcUrl);
 }

 @Test
 public void testGetUsername() {
 String username = "test_user";
 String tenantName = "test_tenant";
 String clusterName = "test_cluster";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setUsername(username);
 connectionConfig.setTenantName(tenantName);
 connectionConfig.setClusterName(clusterName);
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

 String result = obConsoleDataSourceFactory.getUsername();

 assertEquals("test_user@test_tenant#test_cluster", result);
 }

 @Test
 public void testGetPassword() {
 String password = "test_password";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setPassword(password);
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

 String result = obConsoleDataSourceFactory.getPassword();

 assertEquals("test_password", result);
 }

 @Test
 public void testGetJdbcParams() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

 Map<String, String> result = obConsoleDataSourceFactory.getJdbcParams(connectionConfig);

 assertEquals(11, result.size());
 }

 @Test
 public void testGetDataSource() {
 String jdbcUrl = "jdbc:oceanbase:mysql://127.0.0.1:3306/test_schema";
 String username = "test_user";
 String password = "test_password";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost("127.0.0.1");
 connectionConfig.setPort(3306);
 connectionConfig.setDefaultSchema("test_schema");
 connectionConfig.setUsername(username);
 connectionConfig.setPassword(password);
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) obConsoleDataSourceFactory.getDataSource();

 assertEquals(jdbcUrl, dataSource.getUrl());
 assertEquals(username, dataSource.getUsername());
 assertEquals(password, dataSource.getPassword());
 }

 @Test
 public void testGetDataSourceWithSSLConfig() {
 String jdbcUrl = "jdbc:oceanbase:mysql://127.0.0.1:3306/test_schema?useSSL=true&trustStore=truststore.p12&trustStorePassword=password";
 String username = "test_user";
 String password = "test_password";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost("127.0.0.1");
 connectionConfig.setPort(3306);
 connectionConfig.setDefaultSchema("test_schema");
 connectionConfig.setUsername(username);
 connectionConfig.setPassword(password);
 SSLConfig sslConfig = new SSLConfig();
 sslConfig.setEnabled(true);
 SSLFileEntry sslFileEntry = new SSLFileEntry();
 sslFileEntry.setKeyStoreFilePath("truststore.p12");
 sslFileEntry.setKeyStoreFilePassword("password");
 sslConfig.setSslFileEntry(sslFileEntry);
 connectionConfig.setSslConfig(sslConfig);
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) obConsoleDataSourceFactory.getDataSource();

 assertEquals(jdbcUrl, dataSource.getUrl());
 assertEquals(username, dataSource.getUsername());
 assertEquals(password, dataSource.getPassword());
 }

 @Test
 public void testDeepCopy() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true, true);

 OBConsoleDataSourceFactory deepCopy = (OBConsoleDataSourceFactory) obConsoleDataSourceFactory.deepCopy();

 assertNotSame(obConsoleDataSourceFactory, deepCopy);
 }

 @Test
 public void testResetUsername() {
 String newUsername = "new_username";
 CloneableDataSourceFactory.ValueMapper<String> mapper = (oldUsername) -> newUsername;
 obConsoleDataSourceFactory.resetUsername(mapper);

 assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "new_password";
 CloneableDataSourceFactory.ValueMapper<String> mapper = (oldPassword) -> newPassword;
 obConsoleDataSourceFactory.resetPassword(mapper);

 assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "new_host";
 CloneableDataSourceFactory.ValueMapper<String> mapper = (oldHost) -> newHost;
 obConsoleDataSourceFactory.resetHost(mapper);

 assertEquals(newHost, obConsoleDataSourceFactory.getConnectType().getHost());
 }

 @Test
 public void testResetPort() {
 Integer newPort = 9999;
 CloneableDataSourceFactory.ValueMapper<Integer> mapper = (oldPort) -> newPort;
 obConsoleDataSourceFactory.resetPort(mapper);

 assertEquals(newPort, obConsoleDataSourceFactory.getConnectType().getPort());
 }

 @Test
 public void testResetSchema() {
 String newSchema = "new_schema";
 CloneableDataSourceFactory.ValueMapper<String> mapper = (oldSchema) -> newSchema;
 obConsoleDataSourceFactory.resetSchema(mapper);

 assertEquals(newSchema, obConsoleDataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("param1", "value1");
 newParameters.put("param2", "value2");
 CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = (oldParameters) -> newParameters;
 obConsoleDataSourceFactory.resetParameters(mapper);

 assertEquals(newParameters, obConsoleDataSourceFactory.getJdbcParams(new ConnectionConfig()));
 }

}