import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(PowerMockRunner.class)
public class OnlineSchemaChangeParametersTest {

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE table_name ADD column_name datatype";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        Integer lockTableTimeOutSeconds = 60;
        Integer swapTableNameRetryTimes = 3;
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.KEEP;
        String delimiter = ";";
        List<String> lockUsers = new ArrayList<>();
        SwapTableType swapTableType = SwapTableType.SWAP_AFTER;

        onlineSchemaChangeParameters.setSqlType(sqlType);
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        onlineSchemaChangeParameters.setDelimiter(delimiter);
        onlineSchemaChangeParameters.setLockUsers(lockUsers);
        onlineSchemaChangeParameters.setSwapTableType(swapTableType);

        assertEquals(sqlType, onlineSchemaChangeParameters.getSqlType());
        assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());
        assertEquals(errorStrategy, onlineSchemaChangeParameters.getErrorStrategy());
        assertEquals(lockTableTimeOutSeconds, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        assertEquals(swapTableNameRetryTimes, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
        assertEquals(originTableCleanStrategy, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
        assertEquals(delimiter, onlineSchemaChangeParameters.getDelimiter());
        assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
        assertEquals(swapTableType, onlineSchemaChangeParameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        String schema = "test_schema";

        OnlineSchemaChangeParameters.Builder parametersBuilder = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.ALTER)
                .sqlContent("ALTER TABLE test_table ADD test_column VARCHAR(255)")
                .errorStrategy(TaskErrorStrategy.CONTINUE)
                .lockTableTimeOutSeconds(60)
                .swapTableNameRetryTimes(3)
                .originTableCleanStrategy(OriginTableCleanStrategy.KEEP)
                .delimiter(";")
                .lockUsers(new ArrayList<>())
                .swapTableType(SwapTableType.SWAP_AFTER);

        OnlineSchemaChangeParameters parameters = parametersBuilder.build();

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters
                .generateSubTaskParameters(connectionConfig, schema);

        assertEquals(1, result.size());
        assertEquals(schema, result.get(0).getDatabaseName());
        assertEquals("test_table", result.get(0).getOriginTableName());
        assertEquals("test_column", result.get(0).getNewColumnName());
        assertEquals("VARCHAR(255)", result.get(0).getNewColumnType());
        assertEquals(3, result.get(0).getSqlsToBeExecuted().size());

        String alterStmt = DdlUtils.replaceTableName(result.get(0).getSqlsToBeExecuted().get(0),
                result.get(0).getNewTableName(), "mysql", OnlineSchemaChangeSqlType.ALTER);
        assertEquals("ALTER TABLE test_table ADD test_column VARCHAR(255)", alterStmt);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        String schema = "test_schema";

        OnlineSchemaChangeParameters.Builder parametersBuilder = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.ALTER)
                .sqlContent("INVALID SQL")
                .errorStrategy(TaskErrorStrategy.CONTINUE)
                .lockTableTimeOutSeconds(60)
                .swapTableNameRetryTimes(3)
                .originTableCleanStrategy(OriginTableCleanStrategy.KEEP)
                .delimiter(";")
                .lockUsers(new ArrayList<>())
                .swapTableType(SwapTableType.SWAP_AFTER);

        OnlineSchemaChangeParameters parameters = parametersBuilder.build();

        parameters.generateSubTaskParameters(connectionConfig, schema);
    }

    @Test
    public void testGenerateSubTaskParametersWithMultipleSqls() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        String schema = "test_schema";

        OnlineSchemaChangeParameters.Builder parametersBuilder = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.ALTER)
                .sqlContent("ALTER TABLE test_table ADD test_column1 VARCHAR(255); ALTER TABLE test_table ADD test_column2 VARCHAR(255)")
                .errorStrategy(TaskErrorStrategy.CONTINUE)
                .lockTableTimeOutSeconds(60)
                .swapTableNameRetryTimes(3)
                .originTableCleanStrategy(OriginTableCleanStrategy.KEEP)
                .delimiter(";")
                .lockUsers(new ArrayList<>())
                .swapTableType(SwapTableType.SWAP_AFTER);

        OnlineSchemaChangeParameters parameters = parametersBuilder.build();

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters
                .generateSubTaskParameters(connectionConfig, schema);

        assertEquals(1, result.size());
        assertEquals(schema, result.get(0).getDatabaseName());
        assertEquals("test_table", result.get(0).getOriginTableName());
        assertEquals("test_column1", result.get(0).getNewColumnName());
        assertEquals("VARCHAR(255)", result.get(0).getNewColumnType());
        assertEquals(2, result.get(0).getSqlsToBeExecuted().size());

        String alterStmt1 = DdlUtils.replaceTableName(result.get(0).getSqlsToBeExecuted().get(0),
                result.get(0).getNewTableName(), "mysql", OnlineSchemaChangeSqlType.ALTER);
        String alterStmt2 = DdlUtils.replaceTableName(result.get(0).getSqlsToBeExecuted().get(1),
                result.get(0).getNewTableName(), "mysql", OnlineSchemaChangeSqlType.ALTER);

        assertEquals("ALTER TABLE test_table ADD test_column1 VARCHAR(255)", alterStmt1);
        assertEquals("ALTER TABLE test_table ADD test_column2 VARCHAR(255)", alterStmt2);
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptySqlContent() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        String schema = "test_schema";

        OnlineSchemaChangeParameters.Builder parametersBuilder = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.ALTER)
                .sqlContent("")
                .errorStrategy(TaskErrorStrategy.CONTINUE)
                .lockTableTimeOutSeconds(60)
                .swapTableNameRetryTimes(3)
                .originTableCleanStrategy(OriginTableCleanStrategy.KEEP)
                .delimiter(";")
                .lockUsers(new ArrayList<>())
                .swapTableType(SwapTableType.SWAP_AFTER);

        OnlineSchemaChangeParameters parameters = parametersBuilder.build();

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters
                .generateSubTaskParameters(connectionConfig, schema);

        assertNull(result);
    }

    @Test
    public void testGenerateSubTaskParametersWithNullSqlContent() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        String schema = "test_schema";

        OnlineSchemaChangeParameters.Builder parametersBuilder = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.ALTER)
                .sqlContent(null)
                .errorStrategy(TaskErrorStrategy.CONTINUE)
                .lockTableTimeOutSeconds(60)
                .swapTableNameRetryTimes(3)
                .originTableCleanStrategy(OriginTableCleanStrategy.KEEP)
                .delimiter(";")
                .lockUsers(new ArrayList<>())
                .swapTableType(SwapTableType.SWAP_AFTER);

        OnlineSchemaChangeParameters parameters = parametersBuilder.build();

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters
                .generateSubTaskParameters(connectionConfig, schema);

        assertNull(result);
    }

    @Test
    public void testGenerateSubTaskParametersWithNullTaskErrorStrategy() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        String schema = "test_schema";

        OnlineSchemaChangeParameters.Builder parametersBuilder = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.ALTER)
                .sqlContent("ALTER TABLE test_table ADD test_column VARCHAR(255)")
                .errorStrategy(null)
                .lockTableTimeOutSeconds(60)
                .swapTableNameRetryTimes(3)
                .originTableCleanStrategy(OriginTableCleanStrategy.KEEP)
                .delimiter(";")
                .lockUsers(new ArrayList<>())
                .swapTableType(SwapTableType.SWAP_AFTER);

        OnlineSchemaChangeParameters parameters = parametersBuilder.build();

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters
                .generateSubTaskParameters(connectionConfig, schema);

        assertNotNull(result);
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptyLockUsers() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        String schema = "test_schema";

        OnlineSchemaChangeParameters.Builder parametersBuilder = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.ALTER)
                .sqlContent("ALTER TABLE test_table ADD test_column VARCHAR(255)")
                .errorStrategy(TaskErrorStrategy.CONTINUE)
                .lockTableTimeOutSeconds(60)
                .swapTableNameRetryTimes(3)
                .originTableCleanStrategy(OriginTableCleanStrategy.KEEP)
                .delimiter(";")
                .lockUsers(new ArrayList<>())
                .swapTableType(SwapTableType.SWAP_AFTER);

        OnlineSchemaChangeParameters parameters = parametersBuilder.build();

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters
                .generateSubTaskParameters(connectionConfig, schema);

        assertEquals(0, result.get(0).getLockUsers().size());
    }

    @Test
    public void testGenerateSubTaskParametersWithLockUsers() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        String schema = "test_schema";

        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");

        OnlineSchemaChangeParameters.Builder parametersBuilder = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.ALTER)
                .sqlContent("ALTER TABLE test_table ADD test_column VARCHAR(255)")
                .errorStrategy(TaskErrorStrategy.CONTINUE)
                .lockTableTimeOutSeconds(60)
                .swapTableNameRetryTimes(3)
                .originTableCleanStrategy(OriginTableCleanStrategy.KEEP)
                .delimiter(";")
                .lockUsers(lockUsers)
                .swapTableType(SwapTableType.SWAP_AFTER);

        OnlineSchemaChangeParameters parameters = parametersBuilder.build();

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters
                .generateSubTaskParameters(connectionConfig, schema);

        assertEquals(2, result.get(0).getLockUsers().size());
        assertTrue(result.get(0).getLockUsers().contains("user1"));
        assertTrue(result.get(0).getLockUsers().contains("user2"));
    }

    // Add more test cases for other methods and edge cases as needed
}