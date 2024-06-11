import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.pf4j.Extension;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.Connection;
import java.sql.SQLException;

public class OBOracleSessionExtensionTest {

    private JdbcOperations jdbcOperations;
    private Connection connection;
    private OBOracleSessionExtension obOracleSessionExtension;

    @BeforeEach
    public void setUp() {
        jdbcOperations = Mockito.mock(JdbcOperations.class);
        connection = Mockito.mock(Connection.class);
        obOracleSessionExtension = new OBOracleSessionExtension();
        obOracleSessionExtension.setJdbcOperations(jdbcOperations);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "12345";
        obOracleSessionExtension.killQuery(connection, connectionId);
        Mockito.verify(jdbcOperations).execute("KILL QUERY " + connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        String currentSchema = "schema1";
        String targetSchema = "schema2";
        Mockito.when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(currentSchema);
        obOracleSessionExtension.switchSchema(connection, targetSchema);
        Mockito.verify(jdbcOperations).update("ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(targetSchema));

        Mockito.reset(jdbcOperations);
        Mockito.when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(targetSchema);
        obOracleSessionExtension.switchSchema(connection, targetSchema);
        Mockito.verify(jdbcOperations, Mockito.never()).update(Mockito.anyString());
    }

    @Test
    public void testGetCurrentSchema() {
        String currentSchema = "schema1";
        Mockito.when(jdbcOperations.queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(currentSchema);
        String result = obOracleSessionExtension.getCurrentSchema(connection);
        Assertions.assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId() {
        String connectionId = "12345";
        Mockito.when(jdbcOperations.queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(connectionId);
        String result = obOracleSessionExtension.getConnectionId(connection);
        Assertions.assertEquals(connectionId, result);

        Mockito.reset(jdbcOperations);
        Mockito.when(jdbcOperations.queryForObject("select userenv('sessionid') from dual", String.class)).thenThrow(new RuntimeException());
        Mockito.when(connection instanceof OBOracleConnection).thenReturn(true);
        result = obOracleSessionExtension.getConnectionId(connection);
        Assertions.assertNotNull(result);
    }
}