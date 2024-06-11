import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.powermock.api.mockito.PowerMockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@SuppressWarnings("rawtypes")
public class OBOracleSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.killQuery(connection, connectionId);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class);
        JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        String currentSchema = "old_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);

        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        PowerMockito.doReturn(JdbcOperationsUtil.getJdbcOperations(connection)).when(JdbcOperationsUtil.class);
        PowerMockito.doNothing().when(JdbcOperationsUtil.class);
        JdbcOperationsUtil.getJdbcOperations(connection).update(alterSql);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(connection, schemaName);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class);
        JdbcOperationsUtil.getJdbcOperations(connection).update(alterSql);
    }

    @Test
    public void testSwitchSchema_sameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "old_schema";

        String currentSchema = "old_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(connection, schemaName);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class, PowerMockito.times(0));
        JdbcOperationsUtil.getJdbcOperations(connection).update(PowerMockito.anyString());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);

        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        String currentSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn(currentSchema);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String result = extension.getCurrentSchema(connection);

        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);

        String querySql = "select userenv('sessionid') from dual";
        String connectionId = "12345";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class)).thenReturn(connectionId);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String result = extension.getConnectionId(connection);

        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionId_exception() throws SQLException {
        Connection connection = mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(connection.getClass()).thenReturn(oceanBaseConnection.getClass());
        when(oceanBaseConnection.getServerThreadId()).thenReturn(67890);

        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        PowerMockito.doThrow(new SQLException()).when(JdbcOperationsUtil.class);
        JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String result = extension.getConnectionId(connection);

        assertEquals("67890", result);
    }
}