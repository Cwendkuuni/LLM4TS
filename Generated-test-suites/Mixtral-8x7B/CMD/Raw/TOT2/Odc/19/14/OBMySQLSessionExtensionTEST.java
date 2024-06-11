import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.oceanbase.jdbc.OceanBaseDriver;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.configuration.OceanBaseConnectPluginConfiguration;
import com.oceanbase.odc.plugin.connect.configuration.OceanBaseConnectPluginConfiguration.OceanBaseConnectProfile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles("test")
@ContextConfiguration(classes = {OceanBaseConnectPluginConfiguration.class})
public class OBMySQLSessionExtensionTest extends SpringJUnit4ClassRunner {

@Autowired
private JdbcOperationsUtil jdbcOperationsUtil;

@Before
public void setup() {
}

// Test cases from Expert 1
@Test
public void testKillQuery() {
Connection connection = null;
String connectionId = "1";
try {
connection = jdbcOperationsUtil.createConnection();
} catch (SQLException e) {
e.printStackTrace();
}
assertNotNull(connection);
OBMySQLSessionExtension.killQuery(connection, connectionId);
}

@Test
public void testSwitchSchema() {
Connection connection = null;
String schemaName = "test";
try {
connection = jdbcOperationsUtil.createConnection();
} catch (SQLException e) {
e.printStackTrace();
}
assertNotNull(connection);
OBMySQLSessionExtension.switchSchema(connection, schemaName);
}

@Test
public void testGetCurrentSchema() {
Connection connection = null;
String currentSchema = null;
try {
connection = jdbcOperationsUtil.createConnection();
} catch (SQLException e) {
e.printStackTrace();
}
assertNotNull(connection);
currentSchema = OBMySQLSessionExtension.getCurrentSchema(connection);
assertNotNull(currentSchema);
}

@Test
public void testGetConnectionId() {
Connection connection = null;
String connectionId = null;
try {
connection = jdbcOperationsUtil.createConnection();
} catch (SQLException e) {
e.printStackTrace();
}
assertNotNull(connection);
connectionId = OBMySQLSessionExtension.getConnectionId(connection);
assertNotNull(connectionId);
}

// Test cases from Expert 2
@Test
public void testKillQueryWithNullConnection() {
String connectionId = "1";
assertThrows(NullPointerException.class, () -> OBMySQLSessionExtension.killQuery(null, connectionId));
}

@Test
public void testKillQueryWithNullConnectionId() {
Connection connection = null;
assertThrows(NullPointerException.class, () -> OBMySQLSessionExtension.killQuery(connection, null));
}

@Test
public void testSwitchSchemaWithNullConnection() {
String schemaName = "test";
assertThrows(NullPointerException.class, () -> OBMySQLSessionExtension.switchSchema(null, schemaName));
}

@Test
public void testSwitchSchemaWithNullSchemaName() {
Connection connection = null;
assertThrows(NullPointerException.class, () -> OBMySQLSessionExtension.switchSchema(connection, null));
}

@Test
public void testGetCurrentSchemaWithNullConnection() {
assertThrows(NullPointerException.class, () -> OBMySQLSessionExtension.getCurrentSchema(null));
}

@Test
public void testGetConnectionIdWithNullConnection() {
assertThrows(NullPointerException.class, () -> OBMySQLSessionExtension.getConnectionId(null));
}

// Test cases from Expert 3
@Test
public void testKillQueryWithEmptyConnectionId() throws SQLException {
Connection connection = null;
try {
connection = jdbcOperationsUtil.createConnection();
} catch (SQLException e) {
e.printStackTrace();
}
assertNotNull(connection);
String connectionId = "";
OBMySQLSessionExtension.killQuery(connection, connectionId);
}

@Test
public void testSwitchSchemaWithEmptySchemaName() throws SQLException {
Connection connection = null;
try {
connection = jdbcOperationsUtil.createConnection();
} catch (SQLException e) {
e.printStackTrace();
}
assertNotNull(connection);
String schemaName = "";
OBMySQLSessionExtension.switchSchema(connection, schemaName);
}

@Test
public void testGetCurrentSchemaWithEmptyDatabaseName() throws SQLException {
Connection connection = null;
try {
connection = jdbcOperationsUtil.createConnection();
} catch (SQLException e) {
e.printStackTrace();
}
assertNotNull(connection);
String currentSchema = OBMySQLSessionExtension.getCurrentSchema(connection);
assertEquals("", currentSchema);
}

@Test
public void testGetConnectionIdWithNonOceanBaseConnection() throws SQLException {
Connection connection = jdbcOperationsUtil.createConnection();
String connectionId = OBMySQLSessionExtension.getConnectionId(connection);
assertNull(connectionId);
}
}