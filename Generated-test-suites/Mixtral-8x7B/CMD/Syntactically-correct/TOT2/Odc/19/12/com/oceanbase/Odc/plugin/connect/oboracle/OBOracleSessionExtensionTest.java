import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@ExtendWith(MockitoExtension.class)
public class OBOracleSessionExtensionTest {

@Mock
private Connection connection;

@Mock
private Statement statement;

@InjectMocks
private OBOracleSessionExtension obOracleSessionExtension;

@BeforeEach
public void setup() throws SQLException {
initMocks(this);
when(connection.createStatement()).thenReturn(statement);
}

@Test
public void testKillQuery() {
obOracleSessionExtension.killQuery(connection, "connectionId");
}

@Test
public void testSwitchSchema() throws SQLException {
obOracleSessionExtension.switchSchema(connection, "schemaName");
}

@Test
public void testGetCurrentSchema() {
String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
Assertions.assertNotNull(currentSchema);
}

@Test
public void testGetConnectionId() {
String connectionId = obOracleSessionExtension.getConnectionId(connection);
Assertions.assertNotNull(connectionId);
}

@Test
public void testGetConnectionIdException() {
when(connection.createStatement()).thenThrow(new SQLException("Failed to create statement"));
Assertions.assertNull(obOracleSessionExtension.getConnectionId(connection));
}

@Test
public void testGetCurrentSchemaEmptyResult() {
when(statement.executeQuery(anyString())).thenReturn(null);
Assertions.assertNull(obOracleSessionExtension.getCurrentSchema(connection));
}
}