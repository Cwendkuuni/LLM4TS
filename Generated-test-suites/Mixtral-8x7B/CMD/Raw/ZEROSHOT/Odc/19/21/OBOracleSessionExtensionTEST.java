import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private Connection connection;

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        obOracleSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("default_schema");
        obOracleSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("test_schema");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("test_schema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(obOracleSessionExtension.getConnectionId(connection)).thenReturn("123456");
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("123456", connectionId);
    }

    @Test
    public void testGetConnectionIdWithException() throws SQLException {
        when(obOracleSessionExtension.getConnectionId(connection)).thenThrow(new SQLException());
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(null, connectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(obOracleSessionExtension.getConnectionId(connection)).thenReturn("123456");
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("123456", connectionId);
    }
}