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
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
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
    public void testGenerateSubTaskParameters_withAlterSql() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table_name ADD COLUMN column_name varchar(255);");

        List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTasks = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters subTask = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        expectedSubTasks.add(subTask);

        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(subTask);

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(expectedSubTasks, result);
    }

    @Test
    public void testGenerateSubTaskParameters_withCreateIndexSql() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_INDEX);
        onlineSchemaChangeParameters.setSqlContent("CREATE INDEX idx_column_name ON table_name (column_name);");

        List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTasks = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters subTask = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        expectedSubTasks.add(subTask);

        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(subTask);

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(
                connectionConfig, "schema");

        assertEquals(expectedSubTasks, result);
    }

    @Test
    public void testGenerateSubTaskParameters_exception() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table_name ADD COLUMN column_name varchar(255);");

        doThrow(new UnexpectedException("Failed to generate subtasks"))
                .when(subTaskParameterFactory)
                .generate(anyString(), any(OnlineSchemaChangeSqlType.class));

        assertThrows(UnexpectedException.class,
                () -> onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema"));
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(true, onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
        assertEquals(false, onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_emptySqlList() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("");

        List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTasks = new ArrayList<>();

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(expectedSubTasks, result);
    }

    @Test
    public void testGenerateSubTaskParameters_singleSql() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table_name ADD COLUMN column_name varchar(255);");

        List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTasks = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters subTask = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        expectedSubTasks.add(subTask);

        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(subTask);

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(
                connectionConfig, "schema");

        assertEquals(expectedSubTasks, result);
    }

    @Test
    public void testGenerateSubTaskParameters_multipleSql() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table_name ADD COLUMN column_name varchar(255);" +
                "ALTER TABLE table_name ADD COLUMN column_name2 varchar(255);");

        List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTasks = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters subTask1 = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        OnlineSchemaChangeScheduleTaskParameters subTask2 = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        expectedSubTasks.add(subTask1);
        expectedSubTasks.add(subTask2);

        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(subTask1, subTask2);

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(
                connectionConfig, "schema");

        assertEquals(expectedSubTasks, result);
    }

    @Test
    public void testGenerateSubTaskParameters_withDelimiter() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table_name ADD COLUMN column_name varchar(255);--delimiter--" +
                "ALTER TABLE table_name ADD COLUMN column_name2 varchar(255);");
        onlineSchemaChangeParameters.setDelimiter("--delimiter--");

        List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTasks = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters subTask1 = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        OnlineSchemaChangeScheduleTaskParameters subTask2 = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        expectedSubTasks.add(subTask1);
        expectedSubTasks.add(subTask2);

        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(subTask1, subTask2);

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(
                connectionConfig, "schema");

        assertEquals(expectedSubTasks, result);
    }
}