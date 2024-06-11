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
    public void testKillQuery() {
        String connectionId = "123456";
        obOracleSessionExtension.killQuery(connection, connectionId);
        Mockito.verify(connection).createStatement().execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(JdbcOperationsUtil.getJdbcOperations(connection).update(Mockito.anyString()))
                .thenReturn(1);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        Mockito.verify(connection).createStatement().execute("ALTER SESSION SET CURRENT_SCHEMA=" + schemaName);
    }

    @Test
    public void testSwitchSchemaWhenCurrentSchemaIsTargetSchema() throws SQLException {
        String schemaName = "testSchema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        Mockito.verifyNoInteractions(connection);
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.any()))
                .thenReturn("testSchema");
        assertEquals("testSchema", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.any()))
                .thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection)
                .thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId())
                .thenReturn(123456);
        assertEquals("123456", obOracleSessionExtension.getConnectionId(connection));
        Mockito.verify(connection).createStatement().execute("select userenv('sessionid') from dual");
    }

    @Test
    public void testGetConnectionIdWhenConnectionIsNotOceanBaseConnection() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(Mockito.anyString(), Mockito.any()))
                .thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection)
                .thenReturn(false);
        assertNull(obOracleSessionExtension.getConnectionId(connection));
    }
}