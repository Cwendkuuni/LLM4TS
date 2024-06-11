import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testGettersAndSetters() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("SQL CONTENT");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.BACKUP_AND_DELETE);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.INTERNAL);

        assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
        assertEquals("SQL CONTENT", onlineSchemaChangeParameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
        assertEquals(Integer.valueOf(10), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        assertEquals(Integer.valueOf(5), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.BACKUP_AND_DELETE,
                onlineSchemaChangeParameters.getOriginTableCleanStrategy());
        assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
        assertEquals(new ArrayList<>(), onlineSchemaChangeParameters.getLockUsers());
        assertEquals(SwapTableType.INTERNAL, onlineSchemaChangeParameters.getSwapTableType());
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
        String schema = "schema";

        onlineSchemaChangeParameters.setSqlContent("sql1;sql2;sql3");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, schema);

        assertEquals(3, subTaskParameters.size());

        // Verify SQLs and table names
        for (OnlineSchemaChangeScheduleTaskParameters parameter : subTaskParameters) {
            assertEquals(schema, parameter.getDatabaseName());

            if (parameter.getSqlsToBeExecuted().size() == 1) {
                assertEquals("sql1", parameter.getSqlsToBeExecuted().get(0));
            } else if (parameter.getSqlsToBeExecuted().size() == 2) {
                assertEquals("sql2", parameter.getSqlsToBeExecuted().get(0));
                assertEquals("sql3", parameter.getSqlsToBeExecuted().get(1));
            }
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schema";

        onlineSchemaChangeParameters.setSqlContent("sql1");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
    }
}