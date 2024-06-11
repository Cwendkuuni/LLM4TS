import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
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
        String connectionId = "123456";
        obOracleSessionExtension.killQuery(connection, connectionId);
        jdbcOperationsUtil.execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        String currentSchema = "different_schema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + JdbcOperationsUtil.getJdbcOperations(connection)
                .quoteOracleIdentifier(schemaName);
        jdbcOperationsUtil.update(alterSql);
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        String schemaName = "test_schema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        // No action should be taken when the schema is already the current one
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String schemaName = "test_schema";
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn(schemaName);

        String result = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals(schemaName, result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        String connectionId = "789012";
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn(connectionId);

        String result = obOracleSessionExtension.getConnectionId(connection);

        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdException() throws SQLException {
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenThrow(new SQLException("Test exception"));

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(345678);

        String result = obOracleSessionExtension.getConnectionId(connection);

        assertEquals("345678", result);
    }
}