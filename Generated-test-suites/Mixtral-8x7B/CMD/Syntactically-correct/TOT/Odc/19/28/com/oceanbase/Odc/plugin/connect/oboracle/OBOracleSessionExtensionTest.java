import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.JdbcOperationsUtil;
import com.oceanbase.jdbc.OceanBaseConnection;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class OBOracleSessionExtensionTest {

    private OceanBaseConnection connection;
    private OBOracleSessionExtension obOracleSessionExtension = new OBOracleSessionExtension();

    @Before
    public void setUp() throws SQLException {
        connection = new OceanBaseConnection();
        connection.open();
    }

    @After
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    private String getConnectionId(Connection connection) {
        String querySql = "select userenv('sessionid') from dual";
        String connectionId = null;
        try {
            return JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class);
        } catch (Exception e) {
            if (connection instanceof OceanBaseConnection) {
                connectionId = ((OceanBaseConnection) connection).getServerThreadId() + "";
            }
        }
        return connectionId;
    }

    private String getCurrentSchema(Connection connection) {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        return JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class);
    }

    @Test
    public void testKillQuery() throws SQLException {
        // Test code from Expert 1
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        // Test code from Expert 2
    }

    @Test
    public void testGetCurrentSchema() throws SQLException {
        // Test code from Expert 3
    }
}