import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.jupiter.api.*;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {TestConfig.class})
@ContextConfiguration
public class OBOracleSessionExtensionTest {

@Autowired
private DataSource dataSource;

private OBOracleSessionExtension obOracleSessionExtension;

@BeforeEach
public void setUp() {
obOracleSessionExtension = new OBOracleSessionExtension();
ReflectionTestUtils.setField(obOracleSessionExtension, "dataSource", dataSource);
}

@Test
@DisplayName("Test killQuery method")
public void testKillQuery() throws SQLException {
Connection connection = dataSource.getConnection();
String connectionId = "123456";
obOracleSessionExtension.killQuery(connection, connectionId);
}

@Test
@DisplayName("Test switchSchema method with valid schema name")
public void testSwitchSchemaValidSchemaName() throws SQLException {
Connection connection = dataSource.getConnection();
String schemaName = "TEST_SCHEMA";
obOracleSessionExtension.switchSchema(connection, schemaName);
String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
assertEquals(schemaName, currentSchema, "Schemas don't match");
}

@Test
@DisplayName("Test switchSchema method with empty schema name")
public void testSwitchSchemaEmptySchemaName() {
Connection connection = dataSource.getConnection();
assertThrows(SQLException.class, () -> obOracleSessionExtension.switchSchema(connection, ""));
}

@Test
@DisplayName("Test switchSchema method with null schema name")
public void testSwitchSchemaNullSchemaName() {
Connection connection = dataSource.getConnection();
assertThrows(SQLException.class, () -> obOracleSessionExtension.switchSchema(connection, null));
}

@Test
@DisplayName("Test switchSchema method with invalid schema name")
public void testSwitchSchemaInvalidSchemaName() throws SQLException {
Connection connection = dataSource.getConnection();
String schemaName = "INVALID_SCHEMA";
assertThrows(SQLException.class, () -> obOracleSessionExtension.switchSchema(connection, schemaName));
}

@Test
@DisplayName("Test getCurrentSchema method")
public void testGetCurrentSchema() throws SQLException {
Connection connection = dataSource.getConnection();
String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
assertNotNull(currentSchema);
}

@Test
@DisplayName("Test getConnectionId method with valid connection")
public void testGetConnectionIdValidConnection() throws SQLException {
Connection connection = dataSource.getConnection();
String connectionId = obOracleSessionExtension.getConnectionId(connection);
assertNotNull(connectionId);
}

@Test
@DisplayName("Test getConnectionId method with invalid connection")
public void testGetConnectionIdInvalidConnection() {
assertThrows(NullPointerException.class, () -> obOracleSessionExtension.getConnectionId(null));
}

@Test
@DisplayName("Test getConnectionId method with OceanBaseConnection")
public void testGetConnectionIdOceanBaseConnection() throws SQLException {
Connection connection = new OceanBaseConnection(dataSource.getConnection(), null);
String connectionId = obOracleSessionExtension.getConnectionId(connection);
assertNotNull(connectionId);
}
}

class TestConfig {

@Bean
public DataSource dataSource() {
// Implement the DataSource configuration here
}
}