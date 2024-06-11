import com.oceanbase.odc.plugin.connect.oboracle.OBOracleSessionExtension;
import com.oceanbase.odc.plugin.connect.oboracle.JdbcOperationsUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.pf4j.Extension;
import org.pf4j.PluginWrapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OBOracleSessionExtensionTest {

    @Mock
    private PluginWrapper pluginWrapper;

    @Mock
    private JdbcOperationsUtil jdbcOperationsUtil;

    @InjectMocks
    private OBOracleSessionExtension obOracleSessionExtension;

    private DataSource dataSource;
    private Connection connection;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource(createConnection(), true);
        when(jdbcOperationsUtil.getJdbcOperations(any(Connection.class))).thenReturn(new JdbcTemplate(dataSource));
    }

    private Connection createConnection() {
        if (connection == null) {
            connection = MockConnection.newInstance();
        }
        return connection;
    }

    @Test
    public void testKillQuery_TypicalUseCase() throws SQLException {
        String connectionId = "123456";
        obOracleSessionExtension.killQuery(connection, connectionId);

        // Assert that the KILL QUERY statement was executed with the provided connectionId
        assertEquals("KILL QUERY " + connectionId, MockConnection.getExecutedStatement());
    }

    @Test
    public void testSwitchSchema_TypicalUseCase() throws SQLException {
        String schemaName = "test_schema";
        obOracleSessionExtension.switchSchema(connection, schemaName);

        // Assert that the ALTER SESSION SET CURRENT_SCHEMA statement was executed with the provided schemaName
        assertEquals("ALTER SESSION SET CURRENT_SCHEMA=" + MockConnection.quoteIdentifier(schemaName), MockConnection.getExecutedStatement());
    }

    @Test
    public void testSwitchSchema_SameSchema() throws SQLException {
        String schemaName = getCurrentSchema(connection);
        obOracleSessionExtension.switchSchema(connection, schemaName);

        // Assert that the method returned without executing any statement
        assertEquals("", MockConnection.getExecutedStatement());
    }

    @Test
    public void testGetCurrentSchema_TypicalUseCase() {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(new JdbcTemplate(dataSource));

        String currentSchema = obOracleSessionExtension.getCurrentSchema(connection);

        // Assert that the SELECT SYS_CONTEXT statement was executed and the correct schema name was returned
        assertEquals(dataSource.getConnection().createStatement().executeQuery("SELECT SYS_CONTEXT('userenv', 'CURRENT_SCHEMA') FROM DUAL").getString(1), currentSchema);
    }

    @Test
    public void testGetConnectionId_TypicalUseCase() {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenReturn(new JdbcTemplate(dataSource));

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        // Assert that the userenv('sessionid') statement was executed and the correct connectionId was returned
        assertEquals(dataSource.getConnection().createStatement().executeQuery("select userenv('sessionid') from dual").getString(1), connectionId);
    }

    @Test
    public void testGetConnectionId_Exception() {
        when(jdbcOperationsUtil.getJdbcOperations(connection)).thenThrow(new RuntimeException());

        String connectionId = obOracleSessionExtension.getConnectionId(connection);

        // Assert that the method returned a connectionId even when an exception was thrown
        assertEquals(MockConnection.getConnectionId(), connectionId);
    }

    @Test
    public void testGetConnectionId_OceanBaseConnection() throws SQLException {
        when(pluginWrapper.getPluginInfo().getPluginId()).thenReturn("oceanbase");

        String connectionId = obOracleSessionExtension.getConnectionId((OceanBaseConnection) connection);

        // Assert that the correct connectionId was returned for an OceanBaseConnection
        assertEquals(((OceanBaseConnection) connection).getServerThreadId() + "", connectionId);
    }
}

class MockConnection extends SingleConnectionDataSource {

    private static String executedStatement;
    private static String connectionId;

    public MockConnection(DataSource dataSource) {
        super(dataSource);
    }

    public static MockConnection newInstance() {
        return new MockConnection();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new Connection() {
            @Override
            public Statement createStatement() throws SQLException {
                return new Statement() {
                    @Override
                    public ResultSet executeQuery(String sql) throws SQLException {
                        executedStatement = sql;
                        return new ResultSet() {
                            @Override
                            public boolean next() throws SQLException {
                                return false;
                            }
                        };
                    }

                    @Override
                    public int executeUpdate(String sql) throws SQLException {
                        executedStatement = sql;
                        return 0;
                    }
                };
            }

            @Override
            public String getSchema() throws SQLException {
                return connectionId;
            }

            @Override
            public void setSchema(String schema) throws SQLException {
                connectionId = schema;
            }
        };
    }

    public static String getExecutedStatement() {
        return executedStatement;
    }

    public static String getConnectionId() {
        return connectionId;
    }
}