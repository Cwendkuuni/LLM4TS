import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();

        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT;");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP_AND_CREATE_NEW);
        onlineSchemaChangeParameters.setDelimiter(";");

        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");
        onlineSchemaChangeParameters.setLockUsers(lockUsers);

        onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.TEMP_TABLE);
    }

    @Test
    public void testIsContinueOnError() {
        Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws SQLException, UnexpectedException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

        Assert.assertNotNull(subTaskParameters);
        Assert.assertTrue(subTaskParameters.size() > 0);

        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);

        Assert.assertEquals(onlineSchemaChangeParameters.getSqlType(), subTaskParameter.getSqlType());
        Assert.assertEquals(onlineSchemaChangeParameters.getErrorStrategy(), subTaskParameter.getErrorStrategy());
        Assert.assertEquals(onlineSchemaChangeParameters.getLockTableTimeOutSeconds(),
                subTaskParameter.getLockTableTimeOutSeconds());
        Assert.assertEquals(onlineSchemaChangeParameters.getSwapTableNameRetryTimes(),
                subTaskParameter.getSwapTableNameRetryTimes());
        Assert.assertEquals(onlineSchemaChangeParameters.getOriginTableCleanStrategy(),
                subTaskParameter.getOriginTableCleanStrategy());
        Assert.assertEquals(onlineSchemaChangeParameters.getDelimiter(), subTaskParameter.getDelimiter());
        Assert.assertEquals(onlineSchemaChangeParameters.getLockUsers().size(), subTaskParameter.getLockUsers().size());
        Assert.assertEquals(onlineSchemaChangeParameters.getSwapTableType(), subTaskParameter.getSwapTableType());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws SQLException, UnexpectedException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        onlineSchemaChangeParameters.setSqlContent(null);

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
    }

    @Data
    private static class TestOnlineSchemaChangeParameters implements Serializable, TaskParameters {

        private OnlineSchemaChangeSqlType sqlType;
        private String sqlContent;
        private TaskErrorStrategy errorStrategy;
        private Integer lockTableTimeOutSeconds;
        private Integer swapTableNameRetryTimes;
        private OriginTableCleanStrategy originTableCleanStrategy;
        private String delimiter;
        private List<String> lockUsers;
        private SwapTableType swapTableType;

        public TestOnlineSchemaChangeParameters() {
            sqlType = OnlineSchemaChangeSqlType.ALTER;
            sqlContent = "ALTER TABLE table1 ADD COLUMN c1 INT;";
            errorStrategy = TaskErrorStrategy.CONTINUE;
            lockTableTimeOutSeconds = 30;
            swapTableNameRetryTimes = 3;
            originTableCleanStrategy = OriginTableCleanStrategy.DROP_AND_CREATE_NEW;
            delimiter = ";";

            List<String> lockUsers = new ArrayList<>();
            lockUsers.add("user1");
            lockUsers.add("user2");
            this.lockUsers = lockUsers;

            swapTableType = SwapTableType.TEMP_TABLE;
        }
    }

    enum TestOriginTableCleanStrategy implements OnlineSchemaChangeParameters.OriginTableCleanStrategy {
        DROP_AND_CREATE_NEW
    }

    enum TestSwapTableType implements OnlineSchemaChangeParameters.SwapTableType {
        TEMP_TABLE
    }
}