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
import com.oceanbase.odc.service.connection.util.ConnectionPluginUtil;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class OBConsoleDataSourceFactoryTest {

 @Mock
 private EventPublisher eventPublisher;
 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testUser";
 assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 String expectedUsername = "testUser";
 assertEquals(expectedUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 String expectedPassword = "testPassword";
 assertEquals(expectedPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertEquals("com.oceanbase.odc.core.datasource.SingleConnectionDataSource", dataSource.getClass().getName());
 }

 @Test
 public void testGetDialectType() {
 DialectType expectedDialectType = DialectType.OB_MYSQL;
 assertEquals(expectedDialectType, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 CloneableDataSourceFactory clonedDataSourceFactory = dataSourceFactory.deepCopy();
 assertEquals(dataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
 }

 @Test
 public void testResetUsername() {
 String newUsername = "newUsername";
 dataSourceFactory.resetUsername(value -> newUsername);
 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "newPassword";
 dataSourceFactory.resetPassword(value -> newPassword);
 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "newHost";
 dataSourceFactory.resetHost(value -> newHost);
 assertEquals(newHost, dataSourceFactory.host);
 }

 @Test
 public void testResetPort() {
 int newPort = 3307;
 dataSourceFactory.resetPort(value -> newPort);
 assertEquals(newPort, dataSourceFactory.port);
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newSchema";
 dataSourceFactory.resetSchema(value -> newSchema);
 assertEquals(newSchema, dataSourceFactory.defaultSchema);
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("key", "value");
 dataSourceFactory.resetParameters(value -> newParameters);
 assertEquals(newParameters, dataSourceFactory.parameters);
 }

}