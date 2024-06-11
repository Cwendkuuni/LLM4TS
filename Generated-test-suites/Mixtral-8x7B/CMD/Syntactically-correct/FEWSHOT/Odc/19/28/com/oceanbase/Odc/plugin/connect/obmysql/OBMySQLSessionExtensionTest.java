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
        String connectionId = "123456";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).execute(PowerMockito.anyString())).thenReturn(null);
        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        obmysqlSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "test_schema";
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(), PowerMockito.any())).thenReturn("current_schema");
        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        obmysqlSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(), PowerMockito.any())).thenReturn("test_schema");
        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        String result = obmysqlSessionExtension.getCurrentSchema(connection);
        assertEquals("test_schema", result);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(JdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(mock(JdbcOperationsUtil.class));
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(), PowerMockito.any())).thenThrow(new SQLException("test exception"));
        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        String result = obmysqlSessionExtension.getConnectionId(connection);
        assertEquals("0", result);
    }

    @Test
    public void testGetConnectionId_notOceanBaseConnection() throws SQLException {
        Connection connection = mock(Connection.class);
        when(connection instanceof OceanBaseConnection).thenReturn(false);
        OBMySQLSessionExtension obmysqlSessionExtension = new OBMySQLSessionExtension();
        String result = obmysqlSessionExtension.getConnectionId(connection);
        assertNull(result);
    }
}