import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.powermock.api.mockito.PowerMockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;

@org.powermock.modules.junit4.PowerMockAnnotation.Core(preparseStatements = false)
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testSwitchSchema_whenCurrentSchemaIsTargetSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String currentSchema = "test_schema";
        PowerMockito.doReturn(currentSchema).when(JdbcOperationsUtil.class, "getJdbcOperations", connection)
                .queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
        assertEquals(currentSchema, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetCurrentSchema_whenJdbcOperationsUtilThrowsException() throws SQLException {
        Connection connection = mock(Connection.class);
        PowerMockito.doThrow(new SQLException("test exception")).when(JdbcOperationsUtil.class, "getJdbcOperations",
                connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class);
        assertNull(obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = PowerMockito.mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = PowerMockito.mock(OceanBaseConnection.class);
        PowerMockito.when(connection.getClass()).thenReturn(oceanBaseConnection.getClass());
        PowerMockito.when(oceanBaseConnection.getServerThreadId()).thenReturn(12345L);
        PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(null);
        assertEquals("12345", obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_whenConnectionIsNotInstanceOfOceanBaseConnection() throws SQLException {
        Connection connection = PowerMockito.mock(Connection.class);
        PowerMockito.when(connection.getClass()).thenReturn(Connection.class);
        PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(null);
        assertNull(obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_whenJdbcOperationsUtilDoesNotReturnNull() throws SQLException {
        Connection connection = PowerMockito.mock(Connection.class);
        OceanBaseConnection oceanBaseConnection = PowerMockito.mock(OceanBaseConnection.class);
        PowerMockito.when(connection.getClass()).thenReturn(oceanBaseConnection.getClass());
        PowerMockito.when(oceanBaseConnection.getServerThreadId()).thenReturn(12345L);
        PowerMockito.when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        assertNull(obOracleSessionExtension.getConnectionId(connection));
    }
}