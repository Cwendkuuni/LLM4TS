import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.parameterfactory.impl.DefaultSubTaskParameterFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private DefaultSubTaskParameterFactory subTaskParameterFactory;

    @Mock
    private DdlUtils ddlUtils;

    @Before
    public void setUp() {
        when(connectionConfig.getDialectType()).thenReturn("dialectType");
        when(subTaskParameterFactory.generate(null, null)).thenReturn(null);
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("sqlContent");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        when(subTaskParameterFactory.generate(parameters.getSqlContent(), parameters.getSqlType()))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(1, result.size());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws Exception {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("sqlContent");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        when(subTaskParameterFactory.generate(parameters.getSqlContent(), parameters.getSqlType()))
                .thenThrow(new Exception("test exception"));

        parameters.generateSubTaskParameters(connectionConfig, "schema");
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        assertEquals(true, parameters.isContinueOnError());
    }

    @Test
    public void testGetDelimiter() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setDelimiter(";");

        assertEquals(";", parameters.getDelimiter());
    }

    @Test
    public void testGetLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setLockTableTimeOutSeconds(10);

        assertEquals(10, parameters.getLockTableTimeOutSeconds().intValue());
    }

    @Test
    public void testGetSwapTableNameRetryTimes() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableNameRetryTimes(10);

        assertEquals(10, parameters.getSwapTableNameRetryTimes().intValue());
    }

    @Test
    public void testGetOriginTableCleanStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);

        assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testGetLockUsers() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        parameters.setLockUsers(lockUsers);

        assertEquals(lockUsers, parameters.getLockUsers());
    }

    @Test
    public void testGetSwapTableType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.TEMPORARY);

        assertEquals(SwapTableType.TEMPORARY, parameters.getSwapTableType());
    }

    @Test
    public void testSerializable() throws Exception {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        Serializable serializable = (Serializable) parameters;

        OnlineSchemaChangeParameters deserialized = (OnlineSchemaChangeParameters) serializable;

        assertEquals(parameters, deserialized);
    }

    @Test
    public void testEquals() {
        OnlineSchemaChangeParameters parameters1 = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeParameters parameters2 = new OnlineSchemaChangeParameters();

        assertEquals(parameters1, parameters2);
    }

    @Test
    public void testNotEquals() {
        OnlineSchemaChangeParameters parameters1 = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeParameters parameters2 = new OnlineSchemaChangeParameters();

        parameters2.setSqlContent("different");

        assertEquals(parameters1, parameters1);
        assertNull(parameters1.equals(null));
        assertFalse(parameters1.equals(new Object()));
        assertFalse(parameters1.equals(parameters2));
    }
}