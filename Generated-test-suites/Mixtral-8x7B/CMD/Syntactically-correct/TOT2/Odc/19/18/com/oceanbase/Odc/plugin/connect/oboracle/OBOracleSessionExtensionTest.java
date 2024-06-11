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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        obOracleSessionExtension.killQuery(connection, connectionId);

        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        jdbcOperationsUtil.execute("KILL QUERY " + connectionId);

        verify(jdbcOperationsUtil).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema_whenSchemaIsTheSame() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        obOracleSessionExtension.switchSchema(connection, schemaName);

        String currentSchema = "test_schema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);

        assertDoesNotThrow(() -> obOracleSessionExtension.switchSchema(connection, schemaName));
    }

    @Test
    public void testSwitchSchema_whenSchemaIsDifferent() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("different_schema");

        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
        when(jdbcOperationsUtil.update(connection, alterSql)).thenReturn(1);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        verify(jdbcOperationsUtil).update(alterSql);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);

        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String currentSchema = "test_schema";

        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject(connection, querySql, String.class)).thenReturn(currentSchema);

        String result = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId_whenConnectionIsInstanceOfOceanBaseConnection() {
        Connection connection = mock(OceanBaseConnection.class);
        String connectionId = "12345";

        when(connection.getClass().getName()).thenReturn(OceanBaseConnection.class.getName());
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Integer.parseInt(connectionId));

        String result = obOracleSessionExtension.getConnectionId(connection);

        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionId_whenConnectionIsNotInstanceOfOceanBaseConnection() {
        Connection connection = mock(Connection.class);

        when(connection.getClass().getName()).thenReturn("OtherConnectionClass");

        String result = obOracleSessionExtension.getConnectionId(connection);

        assertNull(result);
    }
}