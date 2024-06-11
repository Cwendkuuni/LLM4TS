import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory obConsoleDataSourceFactory;

@Before
public void setup() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.OB_MYSQL);
connectionConfig.setHost("localhost");
connectionConfig.setPort(3306);
connectionConfig.setUsername("root");
connectionConfig.setPassword("password");
connectionConfig.setDefaultSchema("testdb");

obConsoleDataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
}

@Test
public void testGetJdbcUrl() {
String jdbcUrl = obConsoleDataSourceFactory.getJdbcUrl();
assertEquals("jdbc:mysql://localhost:3306/testdb?allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false", jdbcUrl);
}

@Test
public void testGetDataSource() {
DataSource dataSource = obConsoleDataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals("root", ((SingleConnectionDataSource) dataSource).getUsername());
assertEquals("password", ((SingleConnectionDataSource) dataSource).getPassword());
assertEquals("jdbc:mysql://localhost:3306/testdb?allowMultiQueries=true&connectTimeout=5000&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&jdbcCompliantTruncation=false&sendConnectionAttributes=false", ((SingleConnectionDataSource) dataSource).getUrl());
}

@Test
public void testGetDialectType() {
assertEquals(DialectType.OB_MYSQL, obConsoleDataSourceFactory.getDialectType());
}

@Test
public void testDeepCopy() {
CloneableDataSourceFactory clonedDataSourceFactory = obConsoleDataSourceFactory.deepCopy();
assertEquals(obConsoleDataSourceFactory.getDialectType(), clonedDataSourceFactory.getDialectType());
assertEquals(obConsoleDataSourceFactory.getUsername(), clonedDataSourceFactory.getUsername());
assertEquals(obConsoleDataSourceFactory.getPassword(), clonedDataSourceFactory.getPassword());
assertEquals(obConsoleDataSourceFactory.getHost(), clonedDataSourceFactory.getHost());
assertEquals(obConsoleDataSourceFactory.getPort(), clonedDataSourceFactory.getPort());
assertEquals(obConsoleDataSourceFactory.getDefaultSchema(), clonedDataSourceFactory.getDefaultSchema());
assertEquals(obConsoleDataSourceFactory.getJdbcParams(new ConnectionConfig()), clonedDataSourceFactory.getJdbcParams(new ConnectionConfig()));
}

@Test
public void testResetUsername() {
String newUsername = "newUsername";
obConsoleDataSourceFactory.resetUsername(value -> newUsername);
assertEquals(newUsername, obConsoleDataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
String newPassword = "newPassword";
obConsoleDataSourceFactory.resetPassword(value -> newPassword);
assertEquals(newPassword, obConsoleDataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
String newHost = "newHost";
obConsoleDataSourceFactory.resetHost(value -> newHost);
assertEquals(newHost, obConsoleDataSourceFactory.getHost());
}

@Test
public void testResetPort() {
Integer newPort = 4406;
obConsoleDataSourceFactory.resetPort(value -> newPort);
assertEquals(newPort, obConsoleDataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
String newSchema = "newSchema";
obConsoleDataSourceFactory.resetSchema(value -> newSchema);
assertEquals(newSchema, obConsoleDataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
Map<String, String> newParams = new HashMap<>();
newParams.put("param1", "value1");
newParams.put("param2", "value2");
obConsoleDataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, obConsoleDataSourceFactory.getJdbcParams(new ConnectionConfig()));
}

}