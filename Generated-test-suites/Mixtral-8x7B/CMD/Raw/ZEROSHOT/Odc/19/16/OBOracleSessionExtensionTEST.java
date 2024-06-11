import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import org.junit.Before;
import org.junit.Test;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.test. côdeinjection.PowerMockitoClassLoaderLogger;
import org.powermock.modules.test. côdeinjection.PowerMockitoInjector;
import org.powermock.modules.test. côdeinjection.PowerMockitoInjectorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PrepareForTest({LoggerFactory.class, JdbcOperationsUtil.class})
public class OBOracleSessionExtensionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OBOracleSessionExtensionTest.class);

    private OBOracleSessionExtension obOracleSessionExtension;

    @Before
    public void setUp() {
        PowerMockitoInjector injector = PowerMockitoInjectorBuilder.type(OBOracleSessionExtension.class)
                .addFields("log", LoggerFactory.class, LOGGER)
                .build();

        obOracleSessionExtension = injector.getField("log", OBOracleSessionExtension.class);
    }

    @Test
    public void testKillQuery() throws Exception {
        Connection connection = mock(Connection.class);
        String connectionId = "1234";

        obOracleSessionExtension.killQuery(connection, connectionId);

        PowerMockito.verifyPrivate(obOracleSessionExtension, PowerMockito.times(1)).invoke("killQuery", connection, connectionId);
    }

    @Test
    public void testSwitchSchema() throws SQLException {
        Connection connection = mock(Connection.class);
        String schemaName = "testSchema";

        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).update(PowerMockito.anyString())).thenReturn(1);

        obOracleSessionExtension.switchSchema(connection, schemaName);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class, PowerMockito.times(1)).getJdbcOperations(connection);
        PowerMockito.verifyPrivate(obOracleSessionExtension, PowerMockito.times(1)).invoke("switchSchema", connection, schemaName);
    }

    @Test
    public void testGetCurrentSchema() throws Exception {
        Connection connection = mock(Connection.class);
        String currentSchema = "testSchema";

        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(), PowerMockito.any())).thenReturn(currentSchema);

        String result = obOracleSessionExtension.getCurrentSchema(connection);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class, PowerMockito.times(1)).getJdbcOperations(connection);
        PowerMockito.verifyPrivate(obOracleSessionExtension, PowerMockito.times(1)).invoke("getCurrentSchema", connection);
        assertEquals(currentSchema, result);
    }

    @Test
    public void testGetConnectionId() throws Exception {
        Connection connection = mock(Connection.class);
        String connectionId = "5678";

        PowerMockito.mockStatic(JdbcOperationsUtil.class);
        PowerMockito.mockStatic(LoggerFactory.class);
        when(LoggerFactory.getLogger(OBOracleSessionExtensionTest.class)).thenReturn(LOGGER);
        when(JdbcOperationsUtil.getJdbcOperations(connection).queryForObject(PowerMockito.anyString(), PowerMockito.any())).thenThrow(new SQLException());
        when(connection instanceof OceanBaseConnection).thenReturn(true);
        when(((OceanBaseConnection) connection).getServerThreadId()).thenReturn(Integer.parseInt(connectionId));

        String result = obOracleSessionExtension.getConnectionId(connection);

        PowerMockito.verifyStatic(JdbcOperationsUtil.class, PowerMockito.times(1)).getJdbcOperations(connection);
        PowerMockito.verifyPrivate(obOracleSessionExtension, PowerMockito.times(1)).invoke("getConnectionId", connection);
        assertEquals(connectionId, result);
    }
}