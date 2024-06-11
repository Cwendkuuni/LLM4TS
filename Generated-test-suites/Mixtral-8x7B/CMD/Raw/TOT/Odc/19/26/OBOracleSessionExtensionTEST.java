import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.JdbcOperationsUtil;

@RunWith(Theories.class)
public class OBOracleSessionExtensionTest {

    private final OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        JdbcOperationsUtil.getJdbcOperations(connection).execute("KILL QUERY " + connectionId);

        obOracleSessionExtension.killQuery(connection, connectionId);

        Mockito.verify(connection).createStatement();
        Mockito.verify(connection).close();
    }

    @Test
    public void testKillQueryWithException() {
        Connection connection = mock(Connection.class);
        when(connection.createStatement()).thenThrow(new SQLException());
        String connectionId = "12345";

        assertThrows(SQLException.class, () -> obOracleSessionExtension.killQuery(connection, connectionId));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "new_schema";

        String currentSchema = "old_schema";
        when(connection.createStatement()).thenReturn(mock(Connection.class));
        when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"))
                .thenReturn(mock(Connection.class));
        when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")
                .next()).thenReturn(true);
        when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")
                .getString(1)).thenReturn(currentSchema);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        Mockito.verify(connection).createStatement();
        Mockito.verify(connection).createStatement().executeQuery("ALTER SESSION SET CURRENT_SCHEMA="
                + StringUtils.quoteOracleIdentifier(schemaName));
        Mockito.verify(connection).createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL");
        Mockito.verify(connection).createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")
                .next();
        Mockito.verify(connection).createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")
                .getString(1);
        Mockito.verify(connection).close();
    }

    @Test
    public void testSwitchSchemaWithInvalidSchemaName() {
        Connection connection = mock(Connection.class);
        String schemaName = "invalid_schema";

        when(connection.createStatement()).thenReturn(mock(Connection.class));
        when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"))
                .thenReturn(mock(Connection.class));
        when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")
                .next()).thenReturn(true);
        when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")
                .getString(1)).thenReturn("old_schema");

        assertThrows(SQLException.class, () -> obOracleSessionExtension.switchSchema(connection, schemaName));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);

        when(connection.createStatement()).thenReturn(mock(Connection.class));
        when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"))
                .thenReturn(mock(Connection.class));
        when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")
                .next()).thenReturn(true);
        when(connection.createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")
                .getString(1)).thenReturn("schema_name");

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        Mockito.verify(connection).createStatement();
        Mockito.verify(connection).createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL");
        Mockito.verify(connection).createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")
                .next();
        Mockito.verify(connection).createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL")
                .getString(1);
        Mockito.verify(connection).close();

        assertEquals("schema_name", currentSchema);
    }

    @Test
    public void testGetCurrentSchemaWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.getCurrentSchema(null));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);

        when(connection.createStatement()).thenReturn(mock(Connection.class));
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual"))
                .thenReturn(mock(Connection.class));
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual").next())
                .thenReturn(true);
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual").getString(1))
                .thenReturn("connection_id");

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        Mockito.verify(connection).createStatement();
        Mockito.verify(connection).createStatement().executeQuery("select userenv('sessionid') from dual");
        Mockito.verify(connection).createStatement().executeQuery("select userenv('sessionid') from dual").next();
        Mockito.verify(connection).createStatement().executeQuery("select userenv('sessionid') from dual").getString(1);
        Mockito.verify(connection).close();

        assertEquals("connection_id", connectionId);
    }

    @Test
    public void testGetConnectionIdWithNullConnection() {
        assertThrows(NullPointerException.class, () -> obOracleSessionExtension.getConnectionId(null));
    }

    @Test
    public void testGetConnectionIdWithInvalidConnection() {
        Connection connection = mock(Connection.class);
        when(connection.createStatement()).thenReturn(mock(Connection.class));
        when(connection.createStatement().executeQuery("select userenv('sessionid') from dual"))
                .thenThrow(new SQLException());

        assertThrows(SQLException.class, () -> obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithOceanBaseConnection() throws SQLException {
        OceanBaseConnection oceanBaseConnection = mock(OceanBaseConnection.class);
        when(oceanBaseConnection.getServerThreadId()).thenReturn(1234L);

        String connectionId = obOracleSessionExtension.getConnectionId(oceanBaseConnection);

        Mockito.verify(oceanBaseConnection).getServerThreadId();

        assertEquals("1234", connectionId);
    }
}