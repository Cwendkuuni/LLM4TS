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
import static org.mockito.ArgumentMatchers.anyString;
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
        obOracleSessionExtension.killQuery(connection, "connectionId");
        jdbcOperationsUtil.execute("KILL QUERY connectionId");
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String schemaName = "testSchema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + JdbcOperationsUtil.getJdbcOperations(connection).quoteOracleIdentifier(schemaName);
        jdbcOperationsUtil.update(alterSql);
    }

    @Test
    public void testSwitchSchemaDifferentSchema() throws SQLException {
        String schemaName = "testSchema";
        String currentSchema = "differentSchema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + JdbcOperationsUtil.getJdbcOperations(connection).quoteOracleIdentifier(schemaName);
        jdbcOperationsUtil.update(alterSql);
    }

    @Test
    public void testGetCurrentSchema() {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn("testSchema");
        assertEquals("testSchema", obOracleSessionExtension.getCurrentSchema(connection));
    }

    @Test
    public void testGetConnectionId() {
        String querySql = "select userenv('sessionid') from dual";
        when(jdbcOperationsUtil.queryForObject(querySql, String.class)).thenReturn("12345");
        assertEquals("12345", obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionIdWithException() {
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(jdbcOperationsUtil.queryForObject(anyString(), any())).thenThrow(new RuntimeException());
        assertEquals("0", obOracleSessionExtension.getConnectionId(connection));
    }
}