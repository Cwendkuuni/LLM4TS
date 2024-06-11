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
    public void setup() {
        obOracleSessionExtension = new OBOracleSessionExtension();
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connection, "connectionId");
        // Add assertions if necessary
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn("schemaName");
        obOracleSessionExtension.switchSchema(connection, "schemaName");
        // Add assertions if necessary
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(connection.getCatalog()).thenReturn("currentSchema");
        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);
        assertEquals("currentSchema", currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(connection.getMetaData().getDriverName()).thenReturn("OceanBase");
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual")).thenReturn(createResultSet("123"));
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("123", connectionId);

        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual")).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("456", connectionId); // assuming the OceanBaseConnection.getServerThreadId() returns 456
    }
}