import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 int");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
        onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.NORMAL);

        connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenReturn("mysql");
    }

    @Test
    public void testIsContinueOnError() {
        Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
        taskParameters.put(new TableIdentity("db1", "table1"),
                new OnlineSchemaChangeScheduleTaskParameters("db1", "table1", "new_table1"));
        taskParameters.put(new TableIdentity("db2", "table2"),
                new OnlineSchemaChangeScheduleTaskParameters("db2", "table2", "new_table2"));

        SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
        Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
                .thenReturn(taskParameters.get(new TableIdentity("db1", "table1")))
                .thenReturn(taskParameters.get(new TableIdentity("db2", "table2")));

        onlineSchemaChangeParameters.setSubTaskParameterFactory(subTaskParameterFactory);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        Assert.assertEquals(2, subTaskParameters.size());
        Assert.assertEquals("db1", subTaskParameters.get(0).getDatabaseName());
        Assert.assertEquals("table1", subTaskParameters.get(0).getOriginTableName());
        Assert.assertEquals("new_table1", subTaskParameters.get(0).getNewTableName());
        Assert.assertEquals("db2", subTaskParameters.get(1).getDatabaseName());
        Assert.assertEquals("table2", subTaskParameters.get(1).getOriginTableName());
        Assert.assertEquals("new_table2", subTaskParameters.get(1).getNewTableName());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_exception() throws Exception {
        SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
        Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
                .thenThrow(new Exception("Failed to generate subtasks"));

        onlineSchemaChangeParameters.setSubTaskParameterFactory(subTaskParameterFactory);

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
    }
}