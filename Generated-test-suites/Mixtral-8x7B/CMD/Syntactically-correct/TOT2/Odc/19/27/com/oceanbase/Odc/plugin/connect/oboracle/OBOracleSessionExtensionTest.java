import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.killQuery;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.switchSchema;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.getCurrentSchema;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension.getConnectionId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

@Mock
private Connection connection;

private OBOracleSessionExtension obOracleSessionExtension;

@Before
public void setup() {
initMocks(this);
obOracleSessionExtension = new OBOracleSessionExtension();
}

@Test
public void testKillQuery() throws Exception {
doNothing().when(JdbcOperationsUtil).getJdbcOperations(connection);
obOracleSessionExtension.killQuery(connection, "connectionId");
}

@Test
public void testSwitchSchema() throws SQLException {
when(JdbcOperationsUtil.getJdbcOperations(connection).update(anyString())).thenReturn(1);
obOracleSessionExtension.switchSchema(connection, "schemaName");
}

@Test
public void testGetCurrentSchema() throws Exception {
when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), anyString())).thenReturn("currentSchema");
assertEquals("currentSchema", obOracleSessionExtension.getCurrentSchema(connection));
}

@Test
public void testGetConnectionId() throws Exception {
when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(anyString(), anyString())).thenThrow(new Exception());
when(connection instanceof OceanBaseConnection)
.thenReturn(true)
.get(connection)
.getServerThreadId()
.thenReturn(1L);
assertEquals("1", obOracleSessionExtension.getConnectionId(connection));
}
}