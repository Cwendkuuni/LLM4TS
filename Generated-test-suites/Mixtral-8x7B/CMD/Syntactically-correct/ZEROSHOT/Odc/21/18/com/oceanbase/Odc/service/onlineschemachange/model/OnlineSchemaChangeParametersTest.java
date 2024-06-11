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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 int;");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.NOT_CLEAN);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.CLONE);
    }

    @Test
    public void testIsContinueOnError() {
        Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenReturn("mysql");
        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

        Assert.assertEquals(1, subTaskParameters.size());
        Assert.assertEquals("test_schema", subTaskParameters.get(0).getDatabaseName());
        Assert.assertEquals("table1", subTaskParameters.get(0).getOriginTableName());
        Assert.assertEquals("NOT_CLEAN", subTaskParameters.get(0).getOriginTableCleanStrategy().name());
        Assert.assertEquals(3, subTaskParameters.get(0).getSwapTableNameRetryTimes().intValue());
        Assert.assertEquals("CLONE", subTaskParameters.get(0).getSwapTableType().name());
        Assert.assertEquals(1, subTaskParameters.get(0).getSqlsToBeExecuted().size());
        Assert.assertEquals("ALTER TABLE table1 ADD COLUMN c1 int", subTaskParameters.get(0).getSqlsToBeExecuted().get(0));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws Exception {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenThrow(new Exception("Mock exception"));

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
    }

}