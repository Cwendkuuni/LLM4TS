import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pf4j.Extension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @Mock
    private Connection connection;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension;

    @BeforeEach
    public void setup() {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(jdbcOperationsUtil);
    }

    @Test
    public void testKillQuery() {
        String connectionId = "123";
        obOracleSessionExtension.killQuery(connection, connectionId);
        verify(jdbcOperationsUtil, times(1)).execute(eq("KILL QUERY " + connectionId));
    }

    @Test
    public void testSwitchSchema_sameSchema() {
        String schemaName = "testSchema";
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn(schemaName);
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(jdbcOperationsUtil, never()).update(any());
    }

    @Test
    public void testSwitchSchema_differentSchema() throws SQLException {
        String schemaName = "testSchema";
        String alterSql = "ALTER SESSION SET CURRENT_SCHEMA=" + StringUtils.quoteOracleIdentifier(schemaName);
        when(obOracleSessionExtension.getCurrentSchema(connection)).thenReturn("otherSchema");
        obOracleSessionExtension.switchSchema(connection, schemaName);
        verify(jdbcOperationsUtil, times(1)).update(eq(alterSql));
    }

    @Test
    public void testGetConnectionId_connectionIdInDatabase() {
        String connectionId = "123";
        when(jdbcOperationsUtil.queryForObject(any(), eq(String.class))).thenReturn(connectionId);
        assertEquals(connectionId, obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_exceptionDuringQuery() {
        when(jdbcOperationsUtil.queryForObject(any(), eq(String.class))).thenThrow(new RuntimeException());
        assertThrows(SQLException.class, () -> obOracleSessionExtension.getConnectionId(connection));
    }

    @Test
    public void testGetConnectionId_connectionIdFromOceanBaseConnection() {
        OceanBaseConnection oceanBaseConnection = new OceanBaseConnection() {
            @Override
            public long getServerThreadId() {
                return 456;
            }
        };
        when(connection.getClass().equals(OceanBaseConnection.class)).thenReturn(true);
        when(connection.getClass().cast(connection)).thenReturn(oceanBaseConnection);
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals("456", connectionId);
    }

    @Test
    public void testGetConnectionId_noConnectionIdFromDatabaseOrOceanBaseConnection() {
        when(connection.getClass().equals(OceanBaseConnection.class)).thenReturn(false);
        String connectionId = obOracleSessionExtension.getConnectionId(connection);
        assertEquals(null, connectionId);
    }
}