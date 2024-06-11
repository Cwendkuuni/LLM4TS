import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension;

    @BeforeEach
    public void setUp() {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
    }

    @Test
    public void testKillQueryWithValidConnectionId() throws SQLException {
        String connectionId = "123456";
        obOracleSessionExtension.killQuery(connection, connectionId);
        // Add assertions based on the expected behavior of the killQuery method
    }

    @Test
    public void testKillQueryWithInvalidConnectionId() {
        String connectionId = "invalid";
        assertThrows(SQLException.class, () -> obOracleSessionExtension.killQuery(connection, connectionId));
        // Add assertions based on the expected behavior of the killQuery method
    }

    @Test
    public void testSwitchSchemaWithValidSchemaName() throws SQLException {
        String schemaName = "test_schema";
        when(jdbcOperationsUtil.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, "new_schema");
        // Add assertions based on the expected behavior of the switchSchema method
    }

    @Test
    public void testSwitchSchemaWithEqualSchemaNames() throws SQLException {
        String schemaName = "test_schema";
        when(jdbcOperationsUtil.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        // Add assertions based on the expected behavior of the switchSchema method
    }

    @Test
    public void testSwitchSchemaWithException() throws SQLException {
        String schemaName = "test_schema";
        when(jdbcOperationsUtil.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn(schemaName);
        when(jdbcOperationsUtil.update(connection, "ALTER SESSION SET CURRENT_SCHEMA=test_schema"))
                .thenThrow(SQLException.class);
        assertThrows(SQLException.class, () -> obOracleSessionExtension.switchSchema(connection, "new_schema"));
        // Add assertions based on the expected behavior of the switchSchema method
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String schemaName = "test_schema";
        when(jdbcOperationsUtil.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class))
                .thenReturn(schemaName);
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals(schemaName, currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "123456";
        when(jdbcOperationsUtil.queryForObject("select userenv('sessionid') from dual", String.class))
                .thenReturn(connectionId);
        String actualConnectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(connectionId, actualConnectionId);
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        when(jdbcOperationsUtil.queryForObject("select userenv('sessionid') from dual", String.class))
                .thenThrow(SQLException.class);
        when(connection instanceof OceanBaseConnection)
                .thenReturn(true);
        when(connection instanceof Statement)
                .thenReturn(false);
        when(((OceanBaseConnection) connection).getServerThreadId())
                .thenReturn(123456);
        String actualConnectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("123456", actualConnectionId);
    }
}