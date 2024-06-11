import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OBConsoleDataSourceFactoryTest {

private OBConsoleDataSourceFactory dataSourceFactory;

@Before
public void setUp() {
// Initialize the data source factory with default values
dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
}

@Test
public void testGetDialectType() {
// Test that the dialect type is correctly returned
assertEquals(DialectType.OB_MYSQL, dataSourceFactory.getDialectType());
}

@Test
public void testGetJdbcUrl() {
// Test that the JDBC URL is correctly generated
String jdbcUrl = dataSourceFactory.getJdbcUrl();
assertNotNull(jdbcUrl);
}

@Test
public void testGetUsername() {
// Test that the username is correctly returned
assertEquals("testUser", dataSourceFactory.getUsername());
}

@Test
public void testGetPassword() {
// Test that the password is correctly returned
assertEquals("testPassword", dataSourceFactory.getPassword());
}

@Test
public void testGetConnectType() {
// Test that the connect type is correctly returned
assertEquals(ConnectType.LONG, dataSourceFactory.getConnectType());
}

@Test
public void testGetDataSource() {
// Test that the data source is correctly created
DataSource dataSource = dataSourceFactory.getDataSource();
assertNotNull(dataSource);
assertEquals(SingleConnectionDataSource.class, dataSource.getClass());
}

@Test
public void testDeepCopy() {
// Test that the data source factory is correctly cloned
OBConsoleDataSourceFactory clonedFactory = dataSourceFactory.deepCopy();
assertNotNull(clonedFactory);
assertEquals(dataSourceFactory.getUsername(), clonedFactory.getUsername());

// Modify the original data source factory and ensure that the cloned one is not affected
dataSourceFactory.resetUsername(value -> "modifiedUser");
assertEquals("modifiedUser", dataSourceFactory.getUsername());
assertEquals("testUser", clonedFactory.getUsername());
}

@Test
public void testResetUsername() {
// Test that the username is correctly reset
dataSourceFactory.resetUsername(value -> "modifiedUser");
assertEquals("modifiedUser", dataSourceFactory.getUsername());
}

@Test
public void testResetPassword() {
// Test that the password is correctly reset
dataSourceFactory.resetPassword(value -> "modifiedPassword");
assertEquals("modifiedPassword", dataSourceFactory.getPassword());
}

@Test
public void testResetHost() {
// Test that the host is correctly reset
dataSourceFactory.resetHost(value -> "modifiedHost");
assertEquals("modifiedHost", dataSourceFactory.getHost());
}

@Test
public void testResetPort() {
// Test that the port is correctly reset
dataSourceFactory.resetPort(value -> 9000);
assertEquals(Integer.valueOf(9000), dataSourceFactory.getPort());
}

@Test
public void testResetSchema() {
// Test that the schema is correctly reset
dataSourceFactory.resetSchema(value -> "modifiedSchema");
assertEquals("modifiedSchema", dataSourceFactory.getDefaultSchema());
}

@Test
public void testResetParameters() {
// Test that the parameters are correctly reset
Map<String, String> newParams = new HashMap<>();
newParams.put("param1", "value1");
newParams.put("param2", "value2");
dataSourceFactory.resetParameters(value -> newParams);
assertEquals(newParams, dataSourceFactory.getParameters());
}

@Test
public void testResetParametersWithNull() {
// Test that the parameters are correctly reset to null
dataSourceFactory.resetParameters(value -> null);
assertNull(dataSourceFactory.getParameters());
}

@Test
public void testGetDefaultSchema() {
// Test that the default schema is correctly returned
assertEquals("testSchema", dataSourceFactory.getDefaultSchema());
}

@Test
public void testGetJdbcParams() {
// Test that the JDBC parameters are correctly returned
Map<String, String> jdbcParams = dataSourceFactory.getJdbcParams(new ConnectionConfig());
assertFalse(jdbcParams.isEmpty());
assertEquals("64000000", jdbcParams.get("maxAllowedPacket"));
}

@Test
public void testGetPasswordWithNull() {
// Test that the password is correctly returned as null
dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
assertNull(dataSourceFactory.getPassword());
}

@Test
public void testGetUsernameWithNull() {
// Test that the username is correctly returned as null
dataSourceFactory = new OBConsoleDataSourceFactory(new ConnectionConfig(), true);
assertNull(dataSourceFactory.getUsername());
}

}