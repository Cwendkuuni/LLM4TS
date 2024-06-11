import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

@Before
public void setup() {
Map<String, String> jdbcParams = new HashMap<>();
jdbcParams.put("maxAllowedPacket", "64000000");
jdbcParams.put("allowMultiQueries", "true");
jdbcParams.put("connectTimeout", "5000");
jdbcParams.put("zeroDateTimeBehavior", DEFAULT_ZERO_DATE_TIME_BEHAVIOR);
jdbcParams.put("noDatetimeStringSync", "true");
jdbcParams.put("jdbcCompliantTruncation", "false");
jdbcParams.put("sendConnectionAttributes", "false");

ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_ORACLE);
connectionConfig.setHost("localhost");
connectionConfig.setPort(2000);
connectionConfig.setUsername("testUser");
connectionConfig.setPassword("testPassword");
connectionConfig.setJdbcUrlParameters(jdbcParams);

obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
assertNotNull(jdbcUrl);
assertEquals("jdbc:oceanbase:oracle:localhost:2000/testUser", jdbcUrl);
}

@Test
public void testGetDataSource() {
DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals(SingleConnectionDataSource.class, dataSource.getClass());
}

@Test
public void testGetDialectType() {
DialectType dialectType = obConsoleDataSourceFactory.getDialectType();
assertEquals(DialectType.OB_ORACLE, dialectType);
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory clone = obConsoleDataSourceFactory.deepCopy();
assertNotNull(clone);
assertEquals(obConsoleDataSourceFactory.getDialectType(), clone.getDialectType());
}

@Test
public void testResetUsername() {
String newUsername = "newTestUser";
obConsoleDataSourceFactory.resetUsername(value -> newUsername);
assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "newTestPassword";
obConsoleDataSourceFactory.resetPassword(value -> newPassword);
assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "127.0.0.1";
obConsoleDataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, obConsoleDataSourceFactory.host);
}

@Test
public void testResetPort() {
int newPort = 3000;
obConsoleDataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, obConsoleDataSourceFactory.port);
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
obConsoleDataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, obConsoleDataSourceFactory.defaultSchema);
}

@Test
public void testResetParameters() {
Map<String, String> newParams = Collections.singletonMap("newParam", "newValue");
obConsoleDataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, obConsoleDataSourceFactory.parameters);
}

}