import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table_name ADD column_name datatype");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(300);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP_ORIGIN);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(Arrays.asList("user1", "user2"));
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP_TABLE);
    }

    @Test
    public void testIsContinueOnError() {
        Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenReturn("mysql");

        OnlineSchemaChangeScheduleTaskParameters taskParameter1 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter1.setDatabaseName("db1");
        taskParameter1.setOriginTableName("table1");
        taskParameter1.setNewTableName("new_table1");
        taskParameter1.setSqlsToBeExecuted(Arrays.asList("ALTER TABLE table1 ADD column_name datatype"));

        OnlineSchemaChangeScheduleTaskParameters taskParameter2 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter2.setDatabaseName("db2");
        taskParameter2.setOriginTableName("table2");
        taskParameter2.setNewTableName("new_table2");
        taskParameter2.setSqlsToBeExecuted(Arrays.asList("ALTER TABLE table2 ADD column_name datatype"));

        SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
        Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
                .thenReturn(taskParameter1, taskParameter2);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        Assert.assertEquals(2, subTaskParameters.size());
        Assert.assertEquals("db1", subTaskParameters.get(0).getDatabaseName());
        Assert.assertEquals("table1", subTaskParameters.get(0).getOriginTableName());
        Assert.assertEquals("new_table1", subTaskParameters.get(0).getNewTableName());
        Assert.assertEquals("ALTER TABLE table1 ADD column_name datatype",
                subTaskParameters.get(0).getSqlsToBeExecuted().get(0));

        Assert.assertEquals("db2", subTaskParameters.get(1).getDatabaseName());
        Assert.assertEquals("table2", subTaskParameters.get(1).getOriginTableName());
        Assert.assertEquals("new_table2", subTaskParameters.get(1).getNewTableName());
        Assert.assertEquals("ALTER TABLE table2 ADD column_name datatype",
                subTaskParameters.get(1).getSqlsToBeExecuted().get(0));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws Exception {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenReturn("mysql");

        SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
        Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
                .thenThrow(new Exception());

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
    }

}