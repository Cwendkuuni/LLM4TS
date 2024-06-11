import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import lombok.Data;
import lombok.EqualsAndHashCode;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private SubTaskParameterFactory subTaskParameterFactory;

    @Mock
    private DdlUtils ddlUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_normalCase() throws Exception {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE table1 ADD COLUMN c1 int;";
        String schema = "test";
        ConnectionConfig connectionConfig = new ConnectionConfig();
        List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters param1 = new OnlineSchemaChangeScheduleTaskParameters();
        param1.setDatabaseName("test");
        param1.setOriginTableName("table1");
        param1.setSqlsToBeExecuted(Arrays.asList("ALTER TABLE table1 ADD COLUMN c1 int"));
        expected.add(param1);

        when(subTaskParameterFactory.generate(sqlContent, sqlType)).thenReturn(param1);

        List<OnlineSchemaChangeScheduleTaskParameters> actual = onlineSchemaChangeParameters.generateSubTaskParameters(
                connectionConfig, schema);

        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateSubTaskParameters_multipleStatements() throws Exception {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE table1 ADD COLUMN c1 int; ALTER TABLE table2 ADD COLUMN c1 int;";
        String schema = "test";
        ConnectionConfig connectionConfig = new ConnectionConfig();
        List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters param1 = new OnlineSchemaChangeScheduleTaskParameters();
        param1.setDatabaseName("test");
        param1.setOriginTableName("table1");
        param1.setSqlsToBeExecuted(Arrays.asList("ALTER TABLE table1 ADD COLUMN c1 int"));
        expected.add(param1);

        OnlineSchemaChangeScheduleTaskParameters param2 = new OnlineSchemaChangeScheduleTaskParameters();
        param2.setDatabaseName("test");
        param2.setOriginTableName("table2");
        param2.setSqlsToBeExecuted(Arrays.asList("ALTER TABLE table2 ADD COLUMN c1 int"));
        expected.add(param2);

        when(subTaskParameterFactory.generate(anyString(), any())).thenReturn(param1, param2);

        List<OnlineSchemaChangeScheduleTaskParameters> actual = onlineSchemaChangeParameters.generateSubTaskParameters(
                connectionConfig, schema);

        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateSubTaskParameters_exception() throws Exception {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE table1 ADD COLUMN c1 int;";
        String schema = "test";
        ConnectionConfig connectionConfig = new ConnectionConfig();

        when(subTaskParameterFactory.generate(sqlContent, sqlType)).thenThrow(new Exception());

        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
            fail("An UnexpectedException should be thrown");
        } catch (UnexpectedException e) {
            assertEquals("Failed to generate subtasks with parameter: " + onlineSchemaChangeParameters,
                    e.getMessage());
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    private static class OnlineSchemaChangeScheduleTaskParameters extends TaskParameters {

        private String databaseName;
        private String originTableName;
        private String newTableName;
        private List<String> sqlsToBeExecuted;

    }
}