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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@PrepareForTest({JdbcOperationsUtil.class, OBOracleSessionExtension.class})
public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private PluginWrapper pluginWrapper;
    private Connection connection;
    private PowerMockSideEffectFreezer sideEffectFreezer;

    @Before
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        pluginWrapper = mock(PluginWrapper.class);
        Whitebox.setInternalState(obOracleSessionExtension, "pluginWrapper", pluginWrapper);
        connection = mock(Connection.class);
        sideEffectFreezer = new PowerMockSideEffectFreezer();
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connection, "queryId");
        verify(connection, times(1)).createStatement();
        verify(connection, times(1)).createStatement().execute("KILL QUERY queryId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).createStatement();
        verify(connection, times(1)).createStatement().execute("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        String currentSchema = "testSchema";
        when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"))
                .thenReturn(mock(java.sql.Statement.ResultSet.class));
        when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")
                .next()).thenReturn(true);
        when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")
                .getString(1)).thenReturn(currentSchema);
        String result = obOracleSessionExtension.getCurrentSchema(connection);
        verify(connection, times(1)).createStatement();
        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual"))
                .thenReturn(mock(java.sql.Statement.ResultSet.class));
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual").next())
                .thenReturn(true);
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual")
                .getString(1)).thenReturn("connectionId");
        String result = obOracleSessionExtension.getConnectionId(connection);
        verify(connection, times(1)).createStatement();
        assertEquals("connectionId", result);
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual"))
                .thenThrow(mock(SQLException.class));
        when(pluginWrapper.getPluginId()).thenReturn("testPluginId");
        String result = obOracleSessionExtension.getConnectionId(connection);
        verify(connection, times(1)).createStatement();
        verify(pluginWrapper, times(1)).getPluginId();
        assertNotNull(result);
    }

    @Test
    public void testSideEffects() throws Exception {
        sideEffectFreezer. freeze();
        // Add your test methods that need side effects frozen here
        sideEffectFreezer. restore();
    }
}