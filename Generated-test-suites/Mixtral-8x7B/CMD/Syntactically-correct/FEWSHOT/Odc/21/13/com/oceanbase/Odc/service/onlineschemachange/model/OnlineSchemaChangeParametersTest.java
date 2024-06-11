import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());

        parameters.setSqlContent("ALTER TABLE table_name ADD column_name datatype");
        assertEquals("ALTER TABLE table_name ADD column_name datatype", parameters.getSqlContent());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());

        parameters.setLockTableTimeOutSeconds(10);
        assertEquals(Integer.valueOf(10), parameters.getLockTableTimeOutSeconds());

        parameters.setSwapTableNameRetryTimes(5);
        assertEquals(Integer.valueOf(5), parameters.getSwapTableNameRetryTimes());

        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
        assertEquals(OriginTableCleanStrategy.KEEP, parameters.getOriginTableCleanStrategy());

        parameters.setDelimiter(";");
        assertEquals(";", parameters.getDelimiter());

        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");
        parameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, parameters.getLockUsers());

        parameters.setSwapTableType(SwapTableType.SWAP_TABLE);
        assertEquals(SwapTableType.SWAP_TABLE, parameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE table_name ADD column_name datatype; ALTER TABLE table_name DROP column_name");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        TableIdentity tableIdentity1 = new TableIdentity("schema1", "table_name");
        TableIdentity tableIdentity2 = new TableIdentity("schema2", "table_name");

        OnlineSchemaChangeScheduleTaskParameters taskParameters1 =
                new OnlineSchemaChangeScheduleTaskParameters(tableIdentity1, "schema1", "table_name",
                        "new_table_name", OnlineSchemaChangeSqlType.ALTER, "ALTER TABLE table_name ADD column_name datatype",
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), "", "");
        OnlineSchemaChangeScheduleTaskParameters taskParameters2 =
                new OnlineSchemaChangeScheduleTaskParameters(tableIdentity2, "schema2", "table_name",
                        "new_table_name", OnlineSchemaChangeSqlType.ALTER,
                        "ALTER TABLE table_name DROP column_name", new ArrayList<>(), new ArrayList<>(),
                        new ArrayList<>(), "", "");

        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        when(subTaskParameterFactory.generate(
                "ALTER TABLE table_name ADD column_name datatype", OnlineSchemaChangeSqlType.ALTER))
                        .thenReturn(taskParameters1);
        when(subTaskParameterFactory.generate(
                "ALTER TABLE table_name DROP column_name", OnlineSchemaChangeSqlType.ALTER))
                        .thenReturn(taskParameters2);

        List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
        expected.add(taskParameters1);
        expected.add(taskParameters2);

        OnlineSchemaChangeParameters onlineSchemaChangeParameters =
                new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlContent(parameters.getSqlContent());
        onlineSchemaChangeParameters.setSqlType(parameters.getSqlType());

        List<OnlineSchemaChangeScheduleTaskParameters> result =
                onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(expected, result);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws Exception {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE table_name ADD column_name datatype; ALTER TABLE table_name DROP column_name");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        when(subTaskParameterFactory.generate(anyString(), any()))
                .thenThrow(new Exception());

        OnlineSchemaChangeParameters onlineSchemaChangeParameters =
                new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlContent(parameters.getSqlContent());
        onlineSchemaChangeParameters.setSqlType(parameters.getSqlType());

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
    }

}