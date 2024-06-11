import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.core.datasource.ConnectionInitializer;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

public class OBConsoleDataSourceFactoryTest {

 private OBConsoleDataSourceFactory dataSourceFactory;

 @Before
 public void setUp() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_ORACLE);
 connectionConfig.setUsername("username");
 connectionConfig.setPassword("password");
 connectionConfig.setHost("host");
 connectionConfig.setPort(1234);
 connectionConfig.setDefaultSchema("schema");
 connectionConfig.setEndPoint(new OBTenantEndpoint());
 Map<String, String> jdbcParams = new HashMap<>();
 jdbcParams.put("key", "value");
 connectionConfig.setJdbcUrlParameters(jdbcParams);

 dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String expectedJdbcUrl = "jdbc:oceanbase:oracle:@host:1234:schema";
 assertEquals(expectedJdbcUrl, dataSourceFactory.getJdbcUrl());
 }

 @Test
 public void testGetUsername() {
 String expectedUsername = "\"username\"@OBTenantName#ClusterName";
 assertEquals(expectedUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testGetPassword() {
 assertEquals("password", dataSourceFactory.getPassword());
 }

 @Test
 public void testGetDialectType() {
 assertEquals(DialectType.OB_ORACLE, dataSourceFactory.getDialectType());
 }

 @Test
 public void testGetDataSource() {
 SingleConnectionDataSource dataSource = (SingleConnectionDataSource) dataSourceFactory.getDataSource();
 assertEquals("host", dataSource.getHost());
 assertEquals(1234, dataSource.getPort());
 assertEquals("schema", dataSource.getSchemaName());
 assertEquals("username", dataSource.getUsername());
 assertEquals("password", dataSource.getPassword());
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copyDataSourceFactory = dataSourceFactory.deepCopy();
 assertNotSame(dataSourceFactory, copyDataSourceFactory);
 }

 @Test
 public void testResetUsername() {
 String newUsername = "new_username";
 dataSourceFactory.resetUsername(value -> newUsername);
 assertEquals(newUsername, dataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 String newPassword = "new_password";
 dataSourceFactory.resetPassword(value -> newPassword);
 assertEquals(newPassword, dataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 String newHost = "new_host";
 dataSourceFactory.resetHost(value -> newHost);
 assertEquals(newHost, dataSourceFactory.getConnectType().getValue());
 }

 @Test
 public void testResetPort() {
 int newPort = 4321;
 dataSourceFactory.resetPort(value -> newPort);
 assertEquals(newPort, dataSourceFactory.getConnectType().getValue());
 }

 @Test
 public void testResetSchema() {
 String newSchema = "new_schema";
 dataSourceFactory.resetSchema(value -> newSchema);
 assertEquals(newSchema, dataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newJdbcParams = new HashMap<>();
 newJdbcParams.put("new_key", "new_value");
 dataSourceFactory.resetParameters(value -> newJdbcParams);
 assertEquals(newJdbcParams, dataSourceFactory.getJdbcParams(dataSourceFactory.getConnectionConfig()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResetPortNegativeValue() {
 int newPort = -1;
 dataSourceFactory.resetPort(value -> newPort);
 }
}