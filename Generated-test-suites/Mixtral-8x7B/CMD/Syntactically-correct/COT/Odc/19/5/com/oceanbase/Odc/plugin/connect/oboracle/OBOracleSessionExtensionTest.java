import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Mock
    private Connection connection;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
    }

    @Test
    public void testKillQuery() throws SQLException {
        String connectionId = "123456";
        obOracleSessionExtension.killQuery(connection, connectionId);
        jdbcOperationsUtil.execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchemaSameSchema() throws SQLException {
        String schemaName = "testSchema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        // No operations should be performed if the schema is already the current schema
    }

    @Test
    public void testSwitchSchemaDifferentSchema() throws SQLException {
        String currentSchema = "oldSchema";
        String newSchema = "newSchema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        when(jdbcOperationsUtil.update(eq("ALTER SESSION SET CURRENT_SCHEMA=" + newSchema)))
                .thenReturn(1);
        obOracleSessionExtension.switchSchema(connection, newSchema);
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(newSchema);
        jdbcOperationsUtil.update(alterSql);
    }

    @Test
    public void testGetCurrentSchema() {
        String schemaName = "testSchema";
        when(jdbcOperationsUtil.queryForObject(eq("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL"), eq(String.class)))
                .thenReturn(schemaName);
        assertEquals(schemaName, obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionIdConnectionWithSessionId() {
        String sessionId = "123456";
        when(jdbcOperationsUtil.queryForObject(eq("select userenv('sessionid') from dual"), eq(String.class)))
                .thenReturn(sessionId);
        assertEquals(sessionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdConnectionWithoutSessionId() {
        when(jdbcOperationsUtil.queryForObject(eq("select userenv('sessionid') from dual"), any()))
                .thenThrow(new RuntimeException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        String serverThreadId = "789012";
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Integer.parseInt(serverThreadId));
        assertEquals(serverThreadId, obOracleSessionExtension.getConnectionId(connection));
    }
}