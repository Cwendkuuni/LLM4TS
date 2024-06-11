import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode
class TestOnlineSchemaChangeParameters implements Serializable, TaskParameters {

    private OnlineSchemaChangeSqlType sqlType;
    private String sqlContent;
    private TaskErrorStrategy errorStrategy;
    private Integer lockTableTimeOutSeconds;
    private Integer swapTableNameRetryTimes;
    private OnlineSchemaChangeParameters.OriginTableCleanStrategy originTableCleanStrategy;
    private String delimiter;
    private List<String> lockUsers;
    private OnlineSchemaChangeParameters.SwapTableType swapTableType;

    public TestOnlineSchemaChangeParameters(OnlineSchemaChangeSqlType sqlType, String sqlContent,
                                            TaskErrorStrategy errorStrategy, Integer lockTableTimeOutSeconds,
                                            Integer swapTableNameRetryTimes,
                                            OnlineSchemaChangeParameters.OriginTableCleanStrategy originTableCleanStrategy,
                                            String delimiter, List<String> lockUsers,
                                            OnlineSchemaChangeParameters.SwapTableType swapTableType) {
        this.sqlType = sqlType;
        this.sqlContent = sqlContent;
        this.errorStrategy = errorStrategy;
        this.lockTableTimeOutSeconds = lockTableTimeOutSeconds;
        this.swapTableNameRetryTimes = swapTableNameRetryTimes;
        this.originTableCleanStrategy = originTableCleanStrategy;
        this.delimiter = delimiter;
        this.lockUsers = lockUsers;
        this.swapTableType = swapTableType;
    }

    @Override
    public boolean isContinueOnError() {
        return this.errorStrategy == TaskErrorStrategy.CONTINUE;
    }

    @Override
    public String toString() {
        return "TestOnlineSchemaChangeParameters{" +
                "sqlType=" + sqlType +
                ", sqlContent='" + sqlContent + '\'' +
                ", errorStrategy=" + errorStrategy +
                ", lockTableTimeOutSeconds=" + lockTableTimeOutSeconds +
                ", swapTableNameRetryTimes=" + swapTableNameRetryTimes +
                ", originTableCleanStrategy=" + originTableCleanStrategy +
                ", delimiter='" + delimiter + '\'' +
                ", lockUsers=" + lockUsers +
                ", swapTableType=" + swapTableType +
                '}';
    }
}

class TestConnectionConfig implements Serializable {

    private String dialectType;

    public TestConnectionConfig(String dialectType) {
        this.dialectType = dialectType;
    }
}

public class OnlineSchemaChangeParametersTest {

    private TestOnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private TestConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new TestOnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER,
                "ALTER TABLE table_name ADD COLUMN column_name varchar(255);",
                TaskErrorStrategy.CONTINUE, 10, 3,
                OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP_ORIGINAL, ";",
                new ArrayList<String>() {{
                    add("testUser1");
                    add("testUser2");
                }},
                OnlineSchemaChangeParameters.SwapTableType.SWAP_TABLE);

        connectionConfig = new TestConnectionConfig("mysql");
    }

    @Test
    public void test_getSubTaskParameters() {
        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
                    .generateSubTaskParameters(connectionConfig, "test_schema");

            Assert.assertNotNull(subTaskParameters);
            Assert.assertEquals(1, subTaskParameters.size());

            OnlineSchemaChangeScheduleTaskParameters parameters = subTaskParameters.get(0);
            Assert.assertEquals("test_schema", parameters.getDatabaseName());
            Assert.assertEquals("table_name", parameters.getOriginTableName());
            Assert.assertEquals("column_name", parameters.getColumnName());
            Assert.assertEquals("varchar(255)", parameters.getColumnType());
            Assert.assertEquals(1, parameters.getSqlsToBeExecuted().size());
            Assert.assertEquals("ALTER TABLE table_name ADD COLUMN column_name varchar(255);",
                    parameters.getSqlsToBeExecuted().get(0));
            Assert.assertEquals("test_schema.table_name", parameters.getTableIdentity().getFullTableName());
        } catch (UnexpectedException e) {
            Assert.fail("generateSubTaskParameters throws an UnexpectedException: " + e.getMessage());
        }
    }

    @Test
    public void test_isContinueOnError() {
        OnlineSchemaChangeParameters.TaskErrorStrategy errorStrategy = OnlineSchemaChangeParameters.TaskErrorStrategy.CONTINUE;
        onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);

        Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        errorStrategy = OnlineSchemaChangeParameters.TaskErrorStrategy.TERMINATE;
        onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);

        Assert.assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    // Add test cases for other methods and edge cases

}