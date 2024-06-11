import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@RunWith(MockitoJUnitRunner.class)
public class OBMySQLSessionExtensionTest {

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Mock
    private OceanBaseConnection oceanBaseConnection;

    @InjectMocks
    private OBMySQLSessionExtension obMySQLSessionExtension = new OBMySQLSessionExtension();

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        Connection connection = null;
        obMySQLSessionExtension.killQuery(connection, connectionId);
    }

    @Test
    public void testKillQueryWithNullConnectionId() throws SQLException {
        Connection connection = null;
        obMySQLSessionExtension.killQuery(connection, null);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "test_schema";
        Connection connection = null;
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testSwitchSchemaWithNullSchemaName() throws SQLException {
        Connection connection = null;
        obMySQLSessionExtension.switchSchema(connection, null);
    }

    @Test
    public void testSwitchSchemaWithSameSchema() throws SQLException {
        String schemaName = "current_schema";
        Connection connection = null;
        obMySQLSessionExtension.switchSchema(connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        Connection connection = null;
        String currentSchema = obMySQLSessionExtension.getCurrentSchema(connection);
        assertNull(currentSchema);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        Connection connection = oceanBaseConnection;
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertNotNull(connectionId);
    }

    @Test
    public void testGetConnectionIdWithNullConnection() throws SQLException {
        Connection connection = null;
        String connectionId = obMySQLSessionExtension.getConnectionId(connection);
        assertNull(connectionId);
    }

}