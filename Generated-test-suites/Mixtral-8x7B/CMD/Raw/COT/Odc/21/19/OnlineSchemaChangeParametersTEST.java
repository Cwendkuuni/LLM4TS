import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Before;
import org.junit.Test;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    // Test for getter and setter of sqlType
    @Test
    public void testGetSetSqlType() {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        onlineSchemaChangeParameters.setSqlType(sqlType);
        assertEquals(sqlType, onlineSchemaChangeParameters.getSqlType());
    }

    // Test for getter and setter of sqlContent
    @Test
    public void testGetSetSqlContent() {
        String sqlContent = "ALTER TABLE table_name ADD column_name datatype";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());
    }

    // Test for getter and setter of errorStrategy
    @Test
    public void testGetSetErrorStrategy() {
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
        assertEquals(errorStrategy, onlineSchemaChangeParameters.getErrorStrategy());
    }

    // Test for getter and setter of lockTableTimeOutSeconds
    @Test
    public void testGetSetLockTableTimeOutSeconds() {
        Integer lockTableTimeOutSeconds = 300;
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        assertEquals(lockTableTimeOutSeconds, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
    }

    // Test for getter and setter of swapTableNameRetryTimes
    @Test
    public void testGetSetSwapTableNameRetryTimes() {
        Integer swapTableNameRetryTimes = 5;
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        assertEquals(swapTableNameRetryTimes, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
    }

    // Test for getter and setter of originTableCleanStrategy
    @Test
    public void testGetSetOriginTableCleanStrategy() {
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.BACKUP_AND_DELETE;
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        assertEquals(originTableCleanStrategy, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
    }

    // Test for getter and setter of delimiter
    @Test
    public void testGetSetDelimiter() {
        String delimiter = ";";
        onlineSchemaChangeParameters.setDelimiter(delimiter);
        assertEquals(delimiter, onlineSchemaChangeParameters.getDelimiter());
    }

    // Test for getter and setter of lockUsers
    @Test
    public void testGetSetLockUsers() {
        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");
        onlineSchemaChangeParameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
    }

    // Test for getter and setter of swapTableType
    @Test
    public void testGetSetSwapTableType() {
        SwapTableType swapTableType = SwapTableType.TEMPORARY;
        onlineSchemaChangeParameters.setSwapTableType(swapTableType);
        assertEquals(swapTableType, onlineSchemaChangeParameters.getSwapTableType());
    }

    // Test for isContinueOnError
    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    // Test for generateSubTaskParameters method
    @Test
    public void testGenerateSubTaskParameters() throws UnexpectedException {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table_name ADD column_name datatype");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schema_name";

        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setDatabaseName("database_name");
        taskParameters.setOriginTableName("origin_table_name");
        taskParameters.setNewTableName("new_table_name");

        List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
        expected.add(taskParameters);

        onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());

        OnlineSchemaChangeParameters spyOnlineSchemaChangeParameters =
                spy(onlineSchemaChangeParameters);

        doReturn(taskParameters).when(spyOnlineSchemaChangeParameters)
                .generateSubTaskParameter(eq(connectionConfig), eq(schema), anyString(),
                        eq(OnlineSchemaChangeSqlType.ALTER));

        List<OnlineSchemaChangeScheduleTaskParameters> result =
                spyOnlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(expected, result);
    }

    // Test for generateSubTaskParameters method with SQL exception
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithSqlException() throws UnexpectedException {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table_name ADD column_name datatype");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schema_name";

        OnlineSchemaChangeParameters spyOnlineSchemaChangeParameters =
                spy(onlineSchemaChangeParameters);

        doThrow(new RuntimeException()).when(spyOnlineSchemaChangeParameters)
                .generateSubTaskParameter(eq(connectionConfig), eq(schema), anyString(),
                        eq(OnlineSchemaChangeSqlType.ALTER));

        spyOnlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
    }

    // Test for generateSubTaskParameter method
    private OnlineSchemaChangeScheduleTaskParameters generateSubTaskParameter(ConnectionConfig connectionConfig,
                                                                              String schema, String sql,
                                                                              OnlineSchemaChangeSqlType sqlType) {
        // Implement the logic for generating the subtask parameter based on the input
        return new OnlineSchemaChangeScheduleTaskParameters();
    }

}