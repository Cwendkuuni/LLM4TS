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
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.ConnectType;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.plugin.connect.api.ConnectionExtensionPoint;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.SSLFileEntry;
import com.oceanbase.odc.service.connection.util.ConnectionMapper;
import com.oceanbase.odc.service.session.initializer.SessionCreatedInitializer;

import lombok.NonNull;
import lombok.Setter;

public class OBConsoleDataSourceFactoryTest {

 private ConnectionConfig connectionConfig;
 private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

 @Before
 public void setup() {
 connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.OB_MYSQL);
 connectionConfig.setHost("localhost");
 connectionConfig.setPort(3306);
 connectionConfig.setUsername("root");
 connectionConfig.setPassword("password");
 obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
 }

 @Test
 public void testGetJdbcUrl() {
 String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
 assertTrue(jdbcUrl.contains("localhost"));
 assertTrue(jdbcUrl.contains("3306"));
 assertTrue(jdbcUrl.contains("root"));
 }

 @Test
 public void testGetUsername() {
 String username = obConsoleDataSourceFactory.getUsername();
 assertEquals("root", username);
 }

 @Test
 public void testGetPassword() {
 String password = obConsoleDataSourceFactory.getPassword();
 assertEquals("password", password);
 }

 @Test
 public void testGetConnectType() {
 ConnectType connectType = obConsoleDataSourceFactory.getConnectType();
 assertEquals(ConnectType.LONG, connectType);
 }

 @Test
 public void testGetDataSource() {
 DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
 assertTrue(dataSource instanceof SingleConnectionDataSource);
 assertEquals("root", ((SingleConnectionDataSource) dataSource).getUsername());
 assertEquals("password", ((SingleConnectionDataSource) dataSource).getPassword());
 assertEquals("jdbc:oceanbase:myql://localhost:3306/",
 ((SingleConnectionDataSource) dataSource).getUrl());
 }

 @Test
 public void testGetDialectType() {
 DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
 assertEquals(DialectType.OB_MYSQL, dialectType);
 }

 @Test
 public void testDeepCopy() {
 OBConsoleDataSourceFactory copy = obConsoleDataSourceFactory.deepCopy();
 assertNotSame(obConsoleDataSourceFactory, copy);
 assertEquals(obConsoleDataSourceFactory.getDialectType(), copy.getDialectType());
 }

 @Test
 public void testResetUsername() {
 CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> "newUsername";
 obConsoleDataSourceFactory.resetUsername(mapper);
 assertEquals("newUsername", obConsoleDataSourceFactory.getUsername());
 }

 @Test
 public void testResetPassword() {
 CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> "newPassword";
 obConsoleDataSourceFactory.resetPassword(mapper);
 assertEquals("newPassword", obConsoleDataSourceFactory.getPassword());
 }

 @Test
 public void testResetHost() {
 CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> "192.168.1.1";
 obConsoleDataSourceFactory.resetHost(mapper);
 assertEquals("192.168.1.1", obConsoleDataSourceFactory.getHost());
 }

 @Test
 public void testResetPort() {
 CloneableDataSourceFactory.ValueMapper<Integer> mapper = (value) -> 3307;
 obConsoleDataSourceFactory.resetPort(mapper);
 assertEquals(3307, obConsoleDataSourceFactory.getPort());
 }

 @Test
 public void testResetSchema() {
 CloneableDataSourceFactory.ValueMapper<String> mapper = (value) -> "newSchema";
 obConsoleDataSourceFactory.resetSchema(mapper);
 assertEquals("newSchema", obConsoleDataSourceFactory.getDefaultSchema());
 }

 @Test
 public void testResetParameters() {
 Map<String, String> newParams = new HashMap<>();
 newParams.put("param1", "value1");
 newParams.put("param2", "value2");
 CloneableDataSourceFactory.ValueMapper<Map<String, String>> mapper = (value) -> newParams;
 obConsoleDataSourceFactory.resetParameters(mapper);
 assertEquals(newParams, obConsoleDataSourceFactory.getParameters());
 }

}