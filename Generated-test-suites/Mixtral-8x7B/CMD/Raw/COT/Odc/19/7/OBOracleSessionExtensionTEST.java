import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest {

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;

    @Before
    public void setUp() {
        obOracleSessionExtension = new OBOracleSessionExtension();
        connection = mock(Connection.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        obOracleSessionExtension.killQuery(connection, "connectionId");
        Mockito.verify(connection).createStatement().execute("KILL QUERY connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection).update("ALTER SESSION SET CURRENT_SCHEMA=schemaName"))
                .thenReturn(1);

        obOracleSessionExtension.switchSchema(connection, "schemaName");

        Mockito.verify(connection).createStatement().execute("ALTER SESSION SET CURRENT_SCHEMA=schemaName");
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        String currentSchema = "schemaName";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, currentSchema);

        Mockito.verifyNoInteractions(connection);
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL",
                String.class)).thenReturn("currentSchema");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals("currentSchema", currentSchema);
    }

    @Test
    public void testGetConnectionId() {
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual"))
                .thenReturn(mock(java.sql.ResultSet.class));

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertNull(connectionId);
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        long serverThreadId = 12345;
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual"))
                .thenThrow(new SQLException("Not supported yet."));

        when(connection instanceof OceanBaseConnection)
                .thenReturn(true);

        when(((OceanBaseConnection) connection).getServerThreadId())
                .thenReturn(serverThreadId);

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertEquals(String.valueOf(serverThreadId), connectionId);
    }
}