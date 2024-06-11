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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DdlUtils.class, SubTaskParameterFactory.class, UnexpectedException.class})
public class OnlineSchemaChangeParametersTest {

    @Test
    public void testConstructor() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeSqlType.NONE, params.getSqlType());
        assertNull(params.getSqlContent());
        assertEquals(TaskErrorStrategy.STOP, params.getErrorStrategy());
        assertEquals(null, params.getLockTableTimeOutSeconds());
        assertEquals(null, params.getSwapTableNameRetryTimes());
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP, params.getOriginTableCleanStrategy());
        assertEquals(";", params.getDelimiter());
        assertNull(params.getLockUsers());
        assertEquals(null, params.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(true, params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertEquals(false, params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("mysql");
        when(connectionConfig.getSchema()).thenReturn("test");

        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT; ALTER TABLE table1 DROP COLUMN c2;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setLockTableTimeOutSeconds(100);
        params.setSwapTableNameRetryTimes(3);
        params.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.TRUNCATE);
        params.setLockUsers(new ArrayList<>());
        params.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.SAME_TABLE);

        PowerMockito.mockStatic(SubTaskParameterFactory.class);
        SubTaskParameterFactory subTaskParameterFactoryMock = Mockito.mock(SubTaskParameterFactory.class);
        PowerMockito.when(SubTaskParameterFactory.newInstance(connectionConfig, "test")).thenReturn(subTaskParameterFactoryMock);

        OnlineSchemaChangeScheduleTaskParameters parameter1 = new OnlineSchemaChangeScheduleTaskParameters();
        parameter1.setDatabaseName("test");
        parameter1.setOriginTableName("table1");
        parameter1.setNewTableName("table1_new");
        parameter1.setSqlsToBeExecuted(new ArrayList<>());
        parameter1.getSqlsToBeExecuted().add("ALTER TABLE table1 ADD COLUMN c1 INT");
        parameter1.getSqlsToBeExecuted().add("ALTER TABLE table1_new ADD COLUMN c1 INT");

        OnlineSchemaChangeScheduleTaskParameters parameter2 = new OnlineSchemaChangeScheduleTaskParameters();
        parameter2.setDatabaseName("test");
        parameter2.setOriginTableName("table1");
        parameter2.setNewTableName("table1_new");
        parameter2.setSqlsToBeExecuted(new ArrayList<>());
        parameter2.getSqlsToBeExecuted().add("ALTER TABLE table1 DROP COLUMN c2");
        parameter2.getSqlsToBeExecuted().add("ALTER TABLE table1_new DROP COLUMN c2");

        PowerMockito.when(subTaskParameterFactoryMock.generate(Mockito.anyString(), Mockito.any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(parameter1, parameter2);

        List<OnlineSchemaChangeScheduleTaskParameters> result = params.generateSubTaskParameters(connectionConfig, "test");

        assertEquals(2, result.size());
        assertEquals("test", result.get(0).getDatabaseName());
        assertEquals("table1", result.get(0).getOriginTableName());
        assertEquals("table1_new", result.get(0).getNewTableName());
        assertEquals(2, result.get(0).getSqlsToBeExecuted().size());

        assertEquals("test", result.get(1).getDatabaseName());
        assertEquals("table1", result.get(1).getOriginTableName());
        assertEquals("table1_new", result.get(1).getNewTableName());
        assertEquals(2, result.get(1).getSqlsToBeExecuted().size());
    }

}