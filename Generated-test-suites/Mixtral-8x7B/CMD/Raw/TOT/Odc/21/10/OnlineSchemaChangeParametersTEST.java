import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
        onlineSchemaChangeParameters.setDelimiter(";");

        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");
        onlineSchemaChangeParameters.setLockUsers(lockUsers);

        onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.NO_SWAP);
    }

    @Test
    public void testGenerateSubTaskParameters() throws UnexpectedException {
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema1");

        assertEquals(1, subTaskParameters.size());
        assertEquals("schema1", subTaskParameters.get(0).getDatabaseName());
        assertEquals("table1", subTaskParameters.get(0).getOriginTableName());
        assertEquals("ALTER TABLE table1 ADD COLUMN c1 INT", subTaskParameters.get(0).getSqlsToBeExecuted().get(0));
    }

    @Test
    public void testIsContinueOnError() {
        assertEquals(true, onlineSchemaChangeParameters.isContinueOnError());
    }
}