import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.CollectionUtils;

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
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setup() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("testUser");
 connectionConfig.setPassword("testPassword");
 connectionConfig.setDefaultSchema("testSchema");

 this.dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String expectedJdbcUrl = "jdbc:mysql://localhost:3306/testSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=false&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=false";
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
 assertEquals(ConnectType.CONSOLE, dataSourceFactory.getConnectType());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertEquals("jdbc:mysql://localhost:3306/testSchema?maxAllowedPacket=64000000&allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=false&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false&useSSL=false", dataSource.getUrl());
 assertEquals("testUser", dataSource.getUsername());
 assertEquals("testPassword", dataSource.getPassword());
 assertEquals("com.oceanbase.odc.core.datasource.SingleConnectionDataSource", dataSource.getClass().getName());
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copyDataSourceFactory = (OBConsoleDataSourceFactory) dataSourceFactory.deepCopy();
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
 assertEquals(newHost, dataSourceFactory.getConnectType().getHost());
 }

 @Test
 public void testResetPort() {
 int newPort = 3307;
 dataSourceFactory.resetPort(value -> newPort);
 assertEquals(newPort, dataSourceFactory.getConnectType().getPort());
 }

 @Test
 public void testResetSchema() {
 String newSchema = "newSchema";
 dataSourceFactory.resetSchema(value -> newSchema);
 assertEquals(newSchema, dataSourceFactory.getConnectType().getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParameters = new HashMap<>();
 newParameters.put("param1", "value1");
 newParameters.put("param2", "value2");

 dataSourceFactory.resetParameters(value -> newParameters);

 Map<String, String> parameters = dataSourceFactory.getConnectType().getParameters();
 assertEquals(newParameters.size(), parameters.size());
 assertTrue(parameters.containsKey("param1"));
 assertTrue(parameters.containsKey("param2"));
 }
}