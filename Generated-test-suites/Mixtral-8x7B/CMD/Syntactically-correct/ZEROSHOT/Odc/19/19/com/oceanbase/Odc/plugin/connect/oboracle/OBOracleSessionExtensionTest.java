import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.test.sideeffects.PowerMockSideEffectFreezer;
import org.powermock.reflect.Whitebox;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@PrepareForTest({JdbcOperationsUtil.class, OBOracleSessionExtension.class})
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;
    private PluginWrapper pluginWrapper;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
        pluginWrapper = mock(PluginWrapper.class);
        Whitebox.setInternalState(obOracleSessionExtension, "pluginWrapper", pluginWrapper);
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connection, "connectionId");
        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).execute("KILL QUERY connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).update(anyString())).thenReturn(1);

        obOracleSessionExtension.switchSchema(connection, "schemaName");
        verify(JdbcOperationsUtil.getJdbcOperations(connection), times(1)).update("ALTER SESSION SET CURRENT_SCHEMA=schemaName");
    }

    @Test
    public void testSwitchSchemaWhenCurrentSchemaIsSame() throws SQLException {
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("schemaName");

        obOracleSessionExtension.switchSchema(connection, "schemaName");
        verify(JdbcOperationsUtil.getJdbcOperations(connection), never()).update(anyString());
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), any())).thenReturn("schemaName");

        String schemaName = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("schemaName", schemaName);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), any())).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(123456);

        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("123456", connectionId);
    }

    @Test
    public void testGetConnectionIdWhenJdbcOperationsUtilWorks() throws SQLException {
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), any())).thenReturn("connectionId");

        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("connectionId", connectionId);
    }

    @Test
    public void testGetConnectionIdWhenConnectionIsNotOceanBaseConnection() throws SQLException {
        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), any())).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(false);

        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertNull(connectionId);
    }
}