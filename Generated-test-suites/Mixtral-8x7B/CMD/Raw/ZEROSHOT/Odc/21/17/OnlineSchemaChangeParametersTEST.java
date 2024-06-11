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

import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 int;");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP_TABLE);
    }

    @Test
    public void testIsContinueOnError() {
        Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws UnexpectedException {
        when(connectionConfig.getDialectType()).thenReturn(1);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        Assert.assertNotNull(subTaskParameters);
        Assert.assertTrue(subTaskParameters.size() > 0);
    }
}