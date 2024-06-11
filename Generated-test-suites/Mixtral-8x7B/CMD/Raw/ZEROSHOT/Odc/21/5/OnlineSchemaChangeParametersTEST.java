import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table_name ADD column_name datatype");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.NORMAL);
    }

    @Test
    public void testIsContinueOnError() {
        Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "schema");
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        Assert.assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        Assert.assertEquals("schema", taskParameter.getDatabaseName());
        Assert.assertEquals("table_name", taskParameter.getOriginTableName());
        Assert.assertEquals("ADD column_name datatype", taskParameter.getSql());
        Assert.assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
        Assert.assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
        Assert.assertEquals("ALTER TABLE table_name ADD column_name datatype",
                taskParameter.getSqlsToBeExecuted().get(0));

        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParametersMap = new LinkedHashMap<>();
        taskParametersMap.put(
                new TableIdentity("schema", "table_name"),
                subTaskParameterFactory.generate(taskParameter.getSql(), OnlineSchemaChangeSqlType.ALTER));

        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenAnswer(invocation -> taskParametersMap.get(new TableIdentity(
                        invocation.getArgument(0),
                        invocation.getArgument(0).split(" ")[2])));

        subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");

        Assert.assertEquals(1, subTaskParameters.size());
        taskParameter = subTaskParameters.get(0);
        Assert.assertEquals("schema", taskParameter.getDatabaseName());
        Assert.assertEquals("table_name", taskParameter.getOriginTableName());
        Assert.assertEquals("ADD column_name datatype", taskParameter.getSql());
        Assert.assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
        Assert.assertEquals(2, taskParameter.getSqlsToBeExecuted().size());
        Assert.assertEquals("ALTER TABLE table_name ADD column_name datatype",
                taskParameter.getSqlsToBeExecuted().get(0));
        Assert.assertEquals("ALTER TABLE table_name ADD column_name datatype",
                taskParameter.getSqlsToBeExecuted().get(1));
    }

}