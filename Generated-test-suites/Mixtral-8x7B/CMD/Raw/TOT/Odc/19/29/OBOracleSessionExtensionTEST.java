import com.oceanbase.odc.common.util.JdbcOperationsUtil;
import com.oceanbase.odc.common.util.StringUtils;
import com.oceanbase.odc.plugin.connect.api.SessionExtensionPoint;
import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.jdbc.OceanBaseConnection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.pf4j.PluginManager;
import org.pf4j.junit.AbstractExtensionTest;
import org.pf4j.junit.CreatePluginManagerRule;
import org.pf4j.plugin.PluginWrapper;
import org.pf4j.util.Files;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OBOracleSessionExtensionTest extends AbstractExtensionTest {

    private static final String CONNECTION_ID = "123456";
    private static final String SCHEMA_NAME = "TEST_SCHEMA";

    @Rule
    public CreatePluginManagerRule pluginManagerRule = new CreatePluginManagerRule.Builder()
            .pluginStartStrategy(CreatePluginManagerRule.PluginStartStrategy.DEDICATED_CLASSLOADER)
            .build();

    private OBOracleSessionExtension obOracleSessionExtension;
    private Connection connection;
    private String currentSchema;

    @Before
    public void setUp() throws SQLException {
        pluginManager = pluginManagerRule.createPluginManager();
        File jarFile = Files.getTempFile("ob-oracle-session-extension", ".jar");
        try {
            Files.copyResourceToFile("ob-oracle-session-extension.jar", jarFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PluginWrapper pluginWrapper = pluginManager.loadPlugin(jarFile.getAbsolutePath());
        pluginManager.startPlugin(pluginWrapper);

        obOracleSessionExtension = pluginManager.getExtensionPoint(OBOracleSessionExtension.class).getExtensions().get(0);
        connection = mock(Connection.class);
        currentSchema = getCurrentSchema(connection);
    }

    @Test
    public void testKillQuery() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection).execute(StringUtils.quoteOracleIdentifier("KILL QUERY " + CONNECTION_ID))).thenReturn(true);

        obOracleSessionExtension.killQuery(connection, CONNECTION_ID);

        assertEquals(true, JdbcOperationsUtil.getJdbcOperations(connection).execute(StringUtils.quoteOracleIdentifier("KILL QUERY " + CONNECTION_ID)));
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        obOracleSessionExtension.switchSchema(connection, SCHEMA_NAME);

        assertEquals(SCHEMA_NAME, getCurrentSchema(connection));
        assertEquals(false, SCHEMA_NAME.equals(currentSchema));
    }

    @Test
    public void testGetCurrentSchema() {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL", String.class)).thenReturn(SCHEMA_NAME);

        String schemaName = obOracleSessionExtension.getCurrentSchema(connection);

        assertEquals(SCHEMA_NAME, schemaName);
    }

    @Test
    public void testGetConnectionId() throws SQLException {
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject("select userenv('sessionid') from dual", String.class)).thenReturn(CONNECTION_ID);

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        assertEquals(CONNECTION_ID, connectionId);
    }

    private String getCurrentSchema(Connection connection) {
        String querySql = "SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL";
        return JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(querySql, String.class);
    }
}