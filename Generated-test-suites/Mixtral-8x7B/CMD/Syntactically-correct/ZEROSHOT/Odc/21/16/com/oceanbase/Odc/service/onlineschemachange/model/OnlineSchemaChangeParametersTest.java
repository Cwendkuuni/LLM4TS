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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateSubTaskParameters() throws UnexpectedException {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("alter table t1 add column c1 int;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        when(subTaskParameterFactory.generate(parameters.getSqlContent(), OnlineSchemaChangeSqlType.ALTER))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "schema");
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        Assert.assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGetDelimiter() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setDelimiter(";");
        Assert.assertEquals(";", parameters.getDelimiter());

        parameters.setDelimiter(",");
        Assert.assertEquals(",", parameters.getDelimiter());
    }

    @Test
    public void testLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setLockTableTimeOutSeconds(10);
        Assert.assertEquals(Integer.valueOf(10), parameters.getLockTableTimeOutSeconds());

        parameters.setLockTableTimeOutSeconds(5);
        Assert.assertEquals(Integer.valueOf(5), parameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testSwapTableNameRetryTimes() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableNameRetryTimes(3);
        Assert.assertEquals(Integer.valueOf(3), parameters.getSwapTableNameRetryTimes());

        parameters.setSwapTableNameRetryTimes(5);
        Assert.assertEquals(Integer.valueOf(5), parameters.getSwapTableNameRetryTimes());
    }

    @Test
    public void testOriginTableCleanStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
        Assert.assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP,
                parameters.getOriginTableCleanStrategy());

        parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP);
        Assert.assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP,
                parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testLockUsers() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");
        parameters.setLockUsers(lockUsers);
        Assert.assertEquals(lockUsers, parameters.getLockUsers());
    }

    @Test
    public void testSwapTableType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.CLONE_SWAP);
        Assert.assertEquals(OnlineSchemaChangeParameters.SwapTableType.CLONE_SWAP, parameters.getSwapTableType());

        parameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.RENAME_SWAP);
        Assert.assertEquals(OnlineSchemaChangeParameters.SwapTableType.RENAME_SWAP, parameters.getSwapTableType());
    }
}