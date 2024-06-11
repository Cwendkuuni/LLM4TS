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

@SuppressWarnings("unchecked")
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

        String currentSchema = "existing_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(connection, schemaName);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class);
        JdbcOperationsUtil.getJdbcOperations(connection).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName));
    }

    @Test
    public void testSwitchSchemaWhenNoChange() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "existing_schema";

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        extension.switchSchema(connection, schemaName);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class, PowerMockito.times(0));
        JdbcOperationsUtil.getJdbcOperations(connection).update(PowerMockito.anyString());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);

        String currentSchema = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String result = extension.getCurrentSchema(connection);

        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);

        when(connection instanceof OceanBaseConnection).thenReturn(true);
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(42L);
        when(connection.getClass()).thenReturn(oceanBaseConnection.getClass());
        when(connection.getClass().getDeclaredMethod("getServerThreadId")).thenReturn(oceanBaseConnection.getServerThreadId());

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String result = extension.getConnectionId(connection);

        assertEquals("42", result);
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        Connection connection = mock(Connection.class);

        when(connection instanceof OceanBaseConnection).thenReturn(false);

        OBOracleSessionExtension extension = new OBOracleSessionExtension();
        String result = extension.getConnectionId(connection);

        assertNull(result);
    }
}