import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;

 private ConnectionExtensionPoint connectionExtensionPoint;

 private ConnectionConfig connectionConfig;

 private EventPublisher eventPublisher;

 @Before
 public void setup() {
 connectionExtensionPoint = mock(ConnectionExtensionPoint.class);
 eventPublisher = mock(EventPublisher.class);

 connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setDefaultSchema("testSchema");

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String expectedJdbcUrl = "jdbc:oceanbase:myql://localhost:3306/testSchema";
 when(connectionExtensionPoint.generateJdbcUrl(dataSourceFactory.host, dataSourceFactory.port,
 dataSourceFactory.defaultSchema, dataSourceFactory.parameters)).thenReturn(expectedJdbcUrl);

 String jdbcUrl = dataSourceFactory.getJdbcUrl();

 assertEquals(expectedJdbcUrl, jdbcUrl);
 }

 @Test
 public void testGetUsername() {
 String expectedUsername = "testUser";

 String username = dataSourceFactory.getUsername();

 assertEquals(expectedUsername, username);
 }

 @Test
 public void testGetPassword() {
 String expectedPassword = "testPassword";

 String password = dataSourceFactory.getPassword();

 assertEquals(expectedPassword, password);
 }

 @Test
 public void testGetConnectType() {
 ConnectType expectedConnectType = ConnectType.CONSOLE;

 ConnectType connectType = dataSourceFactory.getConnectType();

 assertEquals(expectedConnectType, connectType);
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();

 assertNotNull(dataSource);
 assertEquals(dataSourceFactory.getUsername(), dataSource.getUsername());
 assertEquals(dataSourceFactory.getPassword(), dataSource.getPassword());
 assertEquals("com.oceanbase.odc.plugin.connect.oceanbase.ObMySQLDriver",
 dataSource.getDriverClassName());
 }

 @Test
 public void testGetDialectType() {
 DialectType expectedDialectType = DialectType.OB_MYSQL;

 DialectType dialectType = dataSourceFactory.getDialectType();

 assertEquals(expectedDialectType, dialectType);
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copyDataSourceFactory = dataSourceFactory.deepCopy();

 assertEquals(dataSourceFactory.getUsername(), copyDataSourceFactory.getUsername());
 assertEquals(dataSourceFactory.getPassword(), copyDataSourceFactory.getPassword());
 assertEquals(dataSourceFactory.getHost(), copyDataSourceFactory.getHost());
 assertEquals(dataSourceFactory.getPort(), copyDataSourceFactory.getPort());
 assertEquals(dataSourceFactory.getDefaultSchema(), copyDataSourceFactory.getDefaultSchema());
 assertEquals(dataSourceFactory.getParameters(), copyDataSourceFactory.getParameters());
 assertEquals(dataSourceFactory.getDialectType(), copyDataSourceFactory.getDialectType());
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
 String newSchema = "newSchema";
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

 @Test
 public void testGetDefaultSchema() {
 String expectedDefaultSchema = "testSchema";

 String defaultSchema = OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig);

 assertEquals(expectedDefaultSchema, defaultSchema);
 }

}