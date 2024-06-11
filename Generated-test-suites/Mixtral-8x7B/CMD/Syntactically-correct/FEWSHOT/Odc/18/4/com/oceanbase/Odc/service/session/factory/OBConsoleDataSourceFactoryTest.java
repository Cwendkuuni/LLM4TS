import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 @Mock
 private ConnectionExtensionPoint connectionExtensionPoint;

 @Mock
 private EventPublisher eventPublisher;

 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);

 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("root");
 connectionConfig.setPassword("password");

 this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 this.dataSourceFactory.connectionExtensionPoint = connectionExtensionPoint;
 this.dataSourceFactory.eventPublisher = eventPublisher;
 }

 @Test
 public void testGetJdbcUrl() {
 Map<String, String> parameters = new HashMap<>();
 parameters.put("key1", "value1");
 parameters.put("key2", "value2");

 when(connectionExtensionPoint.generateJdbcUrl(anyString(), anyInt(), anyString(), anyMap()))
 .thenReturn("jdbc:oceanbase://localhost:3306/test?key1=value1&key2=value2");

 assertEquals("jdbc:oceanbase://localhost:3306/test?key1=value1&key2=value2",
 dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_ORACLE);
 connectionConfig.setUsername("user");
 connectionConfig.setTenantName("tenant");
 connectionConfig.setClusterName("cluster");

 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 assertEquals("\"user@tenant#cluster\"", dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setPassword("password");

 OBConsoleDataSourceFactory dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);

 assertEquals("password", dataSourceFactory.getPassword());
 }

 @Test
 public void testGetJdbcParams() {
 Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(new ConnectionConfig());

 assertEquals(10, jdbcParams.size());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();

 assertEquals("jdbc:oceanbase://localhost:3306/null?allowMultiQueries=true&autoDeserialize=false&connectTimeout=5000&"
 + "disableSslHostnameVerification=true&jdbcCompliantTruncation=false&maxAllowedPacket=64000000&noDatetimeStringSync=true&"
 + "sendConnectionAttributes=false&trustServerCertificate=true&zeroDateTimeBehavior=false",
 dataSource.getConnectionProperties().getProperty("url"));
 assertEquals("root", dataSource.getUsername());
 assertEquals("password", dataSource.getPassword());
 assertEquals("com.oceanbase.odc.plugin.connect.jdbc.Driver", dataSource.getDriverClassName());
 }

 @Test
 public void testGetDialectType() {
 DialectType dialectType = dataSourceFactory.getDialectType();

 assertEquals(DialectType.OB_MYSQL, dialectType);
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copy = dataSourceFactory.deepCopy();

 assertNotSame(dataSourceFactory, copy);
 assertEquals(dataSourceFactory.getUsername(), copy.getUsername());
 assertEquals(dataSourceFactory.getPassword(), copy.getPassword());
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newUsername";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newUsername;
 dataSourceFactory.resetUsername(mapper);

 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newPassword";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newPassword;
 dataSourceFactory.resetPassword(mapper);

 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "newHost";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newHost;
 dataSourceFactory.resetHost(mapper);

 assertEquals(newHost, dataSourceFactory.host);
 }

 @Test
 public void testResetPort() {
 int newPort = 4444;
 CloneableDataSourceFactory.ValueMapper<Integer> mapper = value -> newPort;
 dataSourceFactory.resetPort(mapper);

 assertEquals(newPort, dataSourceFactory.port);
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newSchema";
 CloneableDataSourceFactory.ValueMapper<String> mapper = value -> newSchema;
 dataSourceFactory.resetSchema(mapper);

 assertEquals(newSchema, dataSourceFactory.defaultSchema);
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParams = new HashMap<>();
 newParams.put("param1", "value1");
 newParams.put("param2", "value2");

 CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = value -> newParams;
 dataSourceFactory.resetParameters(mapper);

 assertEquals(newParams, dataSourceFactory.parameters);
 }

}