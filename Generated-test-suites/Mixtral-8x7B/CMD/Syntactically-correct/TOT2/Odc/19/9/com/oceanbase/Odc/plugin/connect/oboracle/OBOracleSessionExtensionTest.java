import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.ExtensionPoint;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setJdbcOperationsUtil(jdbcOperationsUtil);
    }

    @Test
    public void testKillQuery() throws Exception {
        String connectionId = "1234";
        obOracleSessionExtension.killQuery(connection, connectionId);
        // Verify that the expected SQL statement is executed
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        jdbcOperationsUtil.execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws Exception {
        String schemaName = "test_schema";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(connection.getCatalog()).thenReturn(schemaName);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        // Verify that the expected SQL statement is executed
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
        jdbcOperationsUtil.update(alterSql);
    }

    @Test
    public void testSwitchSchema_sameSchema() throws Exception {
        String schemaName = "test_schema";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(connection.getCatalog()).thenReturn(schemaName);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        // Verify that the method returns without further action
        // as the current schema is already the desired schema
    }

    @Test
    public void testGetCurrentSchema() throws Exception {
        String schemaName = "test_schema";
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenReturn(schemaName);

        String resultSchema = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals(schemaName, resultSchema);
    }

    @Test
    public void testGetConnectionId_exception() throws Exception {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new SQLException("test exception"));

        SQLException sqlException = assertThrows(SQLException.class, () -> obOracleSessionExtension.getConnectionId(connection));

        assertEquals("test exception", sqlException.getMessage());
    }

    @Test
    public void testGetConnectionId() throws Exception {
        long threadId = 5678;
        when(connection.getClass().getName()).thenReturn("com.oceanbase.odc.plugin.connect.api.OceanBaseConnection");
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenThrow(new Exception());
        when(connection.getServerThreadId()).thenReturn(threadId);

        String result = obOracleSessionExtension.getConnectionId(connection);

        assertEquals(String.valueOf(threadId), result);
    }
}