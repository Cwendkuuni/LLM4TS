import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private SubTaskParameterFactory subTaskParameterFactory;

    @Mock
    private ConnectionConfig connectionConfig;

    @Test
    public void testGenerateSubTaskParameters() throws UnexpectedException {
        when(connectionConfig.getDialectType()).thenReturn("dialect");
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));
        doNothing().when(subTaskParameterFactory).close();

        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
        taskParameters.put(new TableIdentity("db", "tbl"), mock(OnlineSchemaChangeScheduleTaskParameters.class));

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(
                connectionConfig, "schema");

        assertEquals(1, result.size());
        assertEquals("db", result.get(0).getDatabaseName());
        assertEquals("tbl", result.get(0).getOriginTableName());
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(true, onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
        assertEquals(false, onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGetDelimiter() {
        assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
    }

    @Test
    public void testGetLockTableTimeOutSeconds() {
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
        assertEquals(Integer.valueOf(10), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testGetSwapTableNameRetryTimes() {
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
        assertEquals(Integer.valueOf(5), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
    }

    @Test
    public void testGetOriginTableCleanStrategy() {
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP,
                onlineSchemaChangeParameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testGetLockUsers() {
        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");
        onlineSchemaChangeParameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
    }

    @Test
    public void testGetSwapTableType() {
        onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY);
        assertEquals(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY, onlineSchemaChangeParameters.getSwapTableType());
    }
}