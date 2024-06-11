import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.powermock.api.mockito.PowerMockito;

import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;

@SuppressWarnings("rawtypes")
public class OBMySQLSessionExtensionTest {

    private static final String CONNECTION_ID = "12345";

    private static final String CURRENT_SCHEMA = "test_schema";

    private static final String OTHER_SCHEMA = "other_schema";

    private OBMySQLSessionExtension obMySQLSessionExtension;

    private Connection mockConnection;

    private JdbcOperationsUtil mockJdbcOperationsUtil;

    @Before
    public void setup() {
        obMySQLSessionExtension = new OBMySQLSessionExtension();

        mockConnection = mock(Connection.class);

        mockJdbcOperationsUtil = mock(JdbcOperationsUtil.class);

        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperationsUtil(mockConnection)).thenReturn(mockJdbcOperationsUtil);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "67890";

        when(mockJdbcOperationsUtil.execute(mockConnection, "KILL QUERY " + connectionId)).thenReturn(null);

        obMySQLSessionExtension.killQuery(mockConnection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        when(mockJdbcOperationsUtil.queryForObject(mockConnection, "SELECT DATABASE()", String.class))
                .thenReturn(CURRENT_SCHEMA);

        obMySQLSessionExtension.switchSchema(mockConnection, OTHER_SCHEMA);

        assertEquals(OTHER_SCHEMA, getCurrentSchema(mockConnection));
    }

    @Test
    public void testSwitchSchema_sameSchema() throws SQLException {
        when(mockJdbcOperationsUtil.queryForObject(mockConnection, "SELECT DATABASE()", String.class))
                .thenReturn(CURRENT_SCHEMA);

        obMySQLSessionExtension.switchSchema(mockConnection, CURRENT_SCHEMA);

        assertEquals(CURRENT_SCHEMA, getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        when(mockJdbcOperationsUtil.queryForObject(mockConnection, "SELECT DATABASE()", String.class))
                .thenReturn(CURRENT_SCHEMA);

        assertEquals(CURRENT_SCHEMA, obMySQLSessionExtension.getCurrentSchema(mockConnection));
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(mockJdbcOperationsUtil.queryForObject(mockConnection, "select connection_id()", String.class))
                .thenReturn(CONNECTION_ID);

        assertEquals(CONNECTION_ID, obMySQLSessionExtension.getConnectionId(mockConnection));
    }

    @Test
    public void testGetConnectionId_oceanBaseConnection() throws SQLException {
        when(mockConnection.getClass().getName()).thenReturn(OceanBaseConnection.class.getName());

        String connectionId = "98765";

        when(mockJdbcOperationsUtil.queryForObject(mockConnection, "select connection_id()", String.class))
                .thenThrow(new SQLException());

        when(mockConnection.getServerThreadId()).thenReturn(Integer.parseInt(connectionId));

        assertEquals(connectionId, obMySQLSessionExtension.getConnectionId(mockConnection));
    }

    @Test
    public void testGetConnectionId_null() {
        assertNull(obMySQLSessionExtension.getConnectionId(null));
    }

    private String getCurrentSchema(Connection connection) throws SQLException {
        when(mockJdbcOperationsUtil.queryForObject(connection, "SELECT DATABASE()", String.class))
                .thenReturn(CURRENT_SCHEMA);

        return obMySQLSessionExtension.getCurrentSchema(connection);
    }
}