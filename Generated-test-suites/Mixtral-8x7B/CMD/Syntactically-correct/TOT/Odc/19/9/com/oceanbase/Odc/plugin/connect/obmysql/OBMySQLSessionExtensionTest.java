import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.PluginManager;
import org.pf4j.SimplePluginManager;
import org.pf4j.extension.ExtensionPoint;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private PluginManager pluginManager;

    @Mock
    private Connection connection;

    @InjectMocks
    private OBMySQLSessionExtension obMySQLSessionExtension;

    private final SimplePluginManager simplePluginManager = new SimplePluginManager();

    @Before
    public void setUp() {
        simplePluginManager.setPluginManager(pluginManager);
        obMySQLSessionExtension = new OBMySQLSessionExtension();
        obMySQLSessionExtension.setPluginManager(simplePluginManager);
    }

    @Test
    public void testKillQuery_whenConnectionIdExists_shouldSendKillQuery() throws SQLException {
        String connectionId = "12345";
        when(getConnectionId(connection)).thenReturn(connectionId);

        obMySQLSessionExtension.killQuery(connection, connectionId);

        verify(pluginManager, times(1)).listen(any(ExtensionPoint.class));
        verify(connection, times(1)).createStatement();
        verify(connection, times(1)).prepareStatement(anyString());
        verify(connection, times(1)).execute(anyString());
    }

    @Test
    public void testKillQuery_whenConnectionIdDoesNotExist_shouldNotSendKillQuery() throws SQLException {
        when(getConnectionId(connection)).thenReturn(null);

        obMySQLSessionExtension.killQuery(connection, "connectionId");

        verify(pluginManager, times(1)).listen(any(ExtensionPoint.class));
        verify(connection, times(0)).createStatement();
        verify(connection, times(0)).prepareStatement(anyString());
        verify(connection, times(0)).execute(anyString());
    }

    @Test
    public void testSwitchSchema_whenSchemaExists_shouldSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(getCurrentSchema(connection)).thenReturn("default");
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(1)).setCatalog(schemaName);
    }

    @Test
    public void testSwitchSchema_whenSchemaDoesNotExist_shouldDoNothing() throws SQLException {
        String schemaName = "nonexistent_schema";
        when(getCurrentSchema(connection)).thenReturn(schemaName);
        obMySQLSessionExtension.switchSchema(connection, schemaName);
        verify(connection, times(0)).setCatalog(schemaName);
    }

    @Test
    public void testGetCurrentSchema_whenConnectionIsNotNull_shouldReturnCurrentSchema() throws SQLException {
        String expectedSchema = "test_schema";
        when(connection.createStatement())
                .thenReturn(connection.createStatement())
                .thenThrow(new SQLException("Test exception"));

        when(connection.createStatement().executeQuery(anyString()))
                .thenReturn(connection.createStatement().executeQuery(anyString()))
                .thenThrow(new SQLException("Test exception"));

        when(connection.createStatement().executeQuery(anyString()).getResultSet())
                .thenReturn(connection.createStatement().executeQuery(anyString()).getResultSet())
                .thenThrow(new SQLException("Test exception"));

        when(connection.createStatement().executeQuery(anyString()).getResultSet().next())
                .thenReturn(true)
                .thenThrow(new SQLException("Test exception"));

        when(connection.createStatement().executeQuery(anyString()).getResultSet().getString(anyString()))
                .thenReturn(expectedSchema);

        String actualSchema = obMySQLSessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetCurrentSchema_whenConnectionIsNull_shouldReturnNull() throws SQLException {
        String actualSchema = obMySQLSessionExtension.getCurrentSchema(null);

        assertEquals(null, actualSchema);
    }

    @Test
    public void testGetConnectionId_whenConnectionIsInstanceOfOceanBaseConnection_shouldReturnConnectionId() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(connection.prepareStatement(anyString()));

        when(connection.prepareStatement(anyString()).executeQuery()).thenReturn(connection.prepareStatement
                (anyString()).executeQuery());

        when(connection.prepareStatement(anyString()).executeQuery().next())
                .thenReturn(true)
                .thenThrow(new SQLException("Test exception"));

        when(connection.prepareStatement(anyString()).executeQuery().getResultSet()).thenReturn(connection.prepareStatement
                (anyString()).executeQuery().getResultSet());

        when(connection.prepareStatement(anyString()).executeQuery().getResultSet().getString(anyString()))
                .thenReturn("12345");

        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals("12345", actualConnectionId);
    }

    @Test
    public void testGetConnectionId_whenConnectionIsNotInstanceOfOceanBaseConnection_shouldReturnNull() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(connection.prepareStatement(anyString()));

        when(connection.prepareStatement(anyString()).executeQuery()).thenReturn(connection.prepareStatement
                (anyString()).executeQuery());

        when(connection.prepareStatement(anyString()).executeQuery().next())
                .thenThrow(new SQLException("Test exception"));

        String actualConnectionId = obMySQLSessionExtension.getConnectionId(connection);

        assertEquals(null, actualConnectionId);
    }
}