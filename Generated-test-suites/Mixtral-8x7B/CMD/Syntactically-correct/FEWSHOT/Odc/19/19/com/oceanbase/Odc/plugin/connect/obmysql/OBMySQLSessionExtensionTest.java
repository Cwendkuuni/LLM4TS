import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.pf4j.Extension;
import org.powermock.api.mockito.PowerMockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@org.powermock.modules.junit4.PowerMockRunner
public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() throws SQLException {
        Connection connection = mock(Connection.class);
        String connectionId = "12345";

        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        obmysqlSessionExtension.killQuery(connection, connectionId);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class, times(1)).getJdbcOperations(connection);
        PowerMockito.verifyStatic(org.mockito.Mockito.times(1)).execute(anyString());
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq("SELECT DATABASE()"), anyString()))
                .thenReturn(schemaName);

        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        obmysqlSessionExtension.switchSchema(connection, schemaName);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class, times(1)).getJdbcOperations(connection);
        PowerMockito.verifyPrivate(obmysqlSessionExtension, never()).invoke("getCurrentSchema", connection);
    }

    @Test
    public void testSwitchSchemaDifferentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";
        String currentSchema = "differentSchema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq("SELECT DATABASE()"), anyString()))
                .thenReturn(currentSchema);

        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        obmysqlSessionExtension.switchSchema(connection, schemaName);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class, times(1)).getJdbcOperations(connection);
        PowerMockito.verifyPrivate(obmysqlSessionExtension, times(1)).invoke("getCurrentSchema", connection);
        PowerMockito.verifyPrivate(obmysqlSessionExtension, times(1)).invoke("setCurrentSchema", connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);

        String querySql = "SELECT DATABASE()";
        String expectedSchema = "testSchema";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq(querySql), anyString()))
                .thenReturn(expectedSchema);

        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        String actualSchema = obmysqlSessionExtension.getCurrentSchema(connection);

        assertEquals(expectedSchema, actualSchema);
        PowerMockito.verifyStatic(JdbcOperationsUtil.class, times(1)).getJdbcOperations(connection);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);

        String querySql = "select connection_id()";
        String connectionId = "12345";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq(querySql), anyString()))
                .thenReturn(connectionId);

        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        String actualConnectionId = obmysqlSessionExtension.getConnectionId(connection);

        assertEquals(connectionId, actualConnectionId);
        PowerMockito.verifyStatic(JdbcOperationsUtil.class, times(1)).getJdbcOperations(connection);
    }

    @Test
    public void testGetConnectionIdConnectionNotInstanceOfOceanBaseConnection() throws SQLException {
        Connection connection = mock(Connection.class);

        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        String actualConnectionId = obmysqlSessionExtension.getConnectionId(connection);

        assertNull(actualConnectionId);
        PowerMockito.verifyStatic(JdbcOperationsUtil.class, times(1)).getJdbcOperations(connection);
    }

    @Test
    public void testGetConnectionIdException() throws SQLException {
        Connection connection = mock(Connection.class);

        String querySql = "select connection_id()";

        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(eq(querySql), anyString()))
                .thenThrow(new RuntimeException());

        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        String actualConnectionId = obmysqlSessionExtension.getConnectionId(connection);

        assertNull(actualConnectionId);
        PowerMockito.verifyStatic(JdbcOperationsUtil.class, times(1)).getJdbcOperations(connection);
    }
}