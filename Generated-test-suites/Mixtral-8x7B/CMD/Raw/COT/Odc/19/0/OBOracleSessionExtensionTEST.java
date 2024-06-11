import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.pf4j.core.AbstractExtension;
import org.pf4j.core.PluginManager;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.test.conditional.Condition;
import org.powermock.modules.test.conditional.ConditionalIgnoreRule;
import org.powermock.modules.test.conditional.EachClass;
import org.powermock.reflect.Whitebox;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

@PrepareForTest({PluginManager.class, OBOracleSessionExtension.class})
public class OBOracleSessionExtensionTest {

    private PluginManager pluginManager;
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setup() {
        pluginManager = mock(PluginManager.class);
        OBOracleSessionExtension spyOBOracleSessionExtension = spy(new OBOracleSessionExtension());
        Whitebox.setInternalState(spyOBOracleSessionExtension, "pluginManager", pluginManager);
        obOracleSessionExtension = spyOBOracleSessionExtension;
    }

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        obOracleSessionExtension.killQuery(connection, connectionId);

        verify(obOracleSessionExtension).killQuery(connection, connectionId);
        verify(connection).createStatement();
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        String currentSchema = "different_schema";

        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        verify(obOracleSessionExtension).switchSchema(connection, schemaName);
        verify(connection).createStatement();
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";

        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        verify(obOracleSessionExtension, never()).switchSchema(connection, schemaName);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        when(connection.createStatement()).thenThrow(new SQLException("Failed to get statement"));
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Integer.parseInt(connectionId));

        String result = obOracleSessionExtension.getConnectionId(connection);

        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdUsingStatement() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        when(connection.createStatement()).thenReturn(mock(Connection.class));
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual")).thenReturn(mock(java.sql.Statement.ResultSet.class));
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual").next()).thenReturn(true);
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual").getString(1)).thenReturn(connectionId);

        String result = obOracleSessionExtension.getConnectionId(connection);

        assertEquals(connectionId, result);
    }

    @Test
    public void testGetConnectionIdException() throws SQLException {
        Connection connection = mock(Connection.class);

        when(connection.createStatement()).thenReturn(mock(Connection.class));
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual")).thenThrow(new SQLException("Failed to get result set"));
        when(connection instanceof OceanBaseConnection).thenReturn(false);

        assertThrows(SQLException.class, () -> obOracleSessionExtension.getConnectionId(connection));
    }

}

class IsNotExtension implements Condition {

    @Override
    public boolean matches(Object o) {
        return !(o instanceof Extension);
    }

}

@EachClass(IsNotExtension.class)
class IgnoreNotExtensionTests {

    @Rule
    public ConditionalIgnoreRule conditionalIgnoreRule = new ConditionalIgnoreRule();

}