import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Mock
    private Connection connection;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connection, "connectionId");
        assertEquals("KILL QUERY " + "connectionId", jdbcOperationsUtil.getLastExecutedSql());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn("schemaName");
        obOracleSessionExtension.switchSchema(connection, "schemaName");
        String expectedAlterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + "schemaName";
        assertEquals(expectedAlterSql, jdbcOperationsUtil.getLastExecutedSql());
    }

    @Test
    public void testSwitchSchemaWhenCurrentSchemaIsTheSame() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn("schemaName");
        obOracleSessionExtension.switchSchema(connection, "schemaName");
        assertEquals(0, jdbcOperationsUtil.getNumberOfExecutedStatements());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn("schemaName");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("schemaName", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn("connectionId");
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("connectionId", connectionId);
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenThrow(new SQLException());
        if (connection instanceof OceanBaseConnection) {
            assertEquals(((OceanBaseConnection) connection).getServerThreadId(), obOracleSessionExtension.getConnectionId(connection));
        } else {
            assertEquals(null, obOracleSessionExtension.getConnectionId(connection));
        }
    }
}