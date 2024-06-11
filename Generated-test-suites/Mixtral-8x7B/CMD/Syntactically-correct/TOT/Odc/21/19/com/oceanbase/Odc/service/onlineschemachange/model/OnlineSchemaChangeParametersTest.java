import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testGenerateSubTaskParameters_withAlterSqls_returnsCorrectSubTaskParametersList() {
        // Arrange
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        OnlineSchemaChangeParameters parameters = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.ALTER)
                .sqlContent("ALTER TABLE t1 ADD COLUMN c1 INT; ALTER TABLE t1 ADD COLUMN c2 DATE;")
                .errorStrategy(TaskErrorStrategy.CONTINUE)
                .build();

        SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
        Mockito.when(subTaskParameterFactory.generate(anyString(), Mockito.eq(OnlineSchemaChangeSqlType.ALTER)))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters("test_database", "t1", "c1",
                        "INT", new ArrayList<>()));

        // Act
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema,
                subTaskParameterFactory);

        // Assert
        assertEquals(2, result.size());
        assertEquals("test_database", result.get(0).getDatabaseName());
        assertEquals("t1", result.get(0).getOriginTableName());
        assertEquals("c1", result.get(0).getNewColumnName());
        assertEquals("INT", result.get(0).getNewColumnType());
        assertEquals("ALTER TABLE t1 ADD COLUMN c1 INT", result.get(0).getSqlsToBeExecuted().get(0));

        assertEquals("test_database", result.get(1).getDatabaseName());
        assertEquals("t1", result.get(1).getOriginTableName());
        assertEquals("c2", result.get(1).getNewColumnName());
        assertEquals("DATE", result.get(1).getNewColumnType());
        assertEquals("ALTER TABLE t1 ADD COLUMN c2 DATE", result.get(1).getSqlsToBeExecuted().get(0));
    }

    @Data
    private static class OnlineSchemaChangeScheduleTaskParameters implements Serializable {
        private String databaseName;
        private String originTableName;
        private String newColumnName;
        private String newColumnType;
        private List<String> sqlsToBeExecuted;
    }

    @Test
    public void testGenerateSubTaskParameters_withCreateAndAlterSqls_returnsCorrectSubTaskParametersList() {
        // Arrange
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        OnlineSchemaChangeParameters parameters = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.MIXED)
                .sqlContent("CREATE TABLE t1 (c1 INT); ALTER TABLE t1 ADD COLUMN c2 DATE;")
                .errorStrategy(TaskErrorStrategy.CONTINUE)
                .build();

        SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
        Mockito.when(subTaskParameterFactory.generate(anyString(), Mockito.eq(OnlineSchemaChangeSqlType.CREATE)))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters("test_database", "t1", "c1",
                "INT", new ArrayList<>()));

        // Act
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema,
                subTaskParameterFactory);

        // Assert
        assertEquals(2, result.size());
        assertEquals("test_database", result.get(0).getDatabaseName());
        assertEquals("t1", result.get(0).getOriginTableName());
        assertEquals("c1", result.get(0).getNewColumnName());
        assertEquals("INT", result.get(0).getNewColumnType());
        assertEquals("CREATE TABLE t1 (c1 INT)", result.get(0).getSqlsToBeExecuted().get(0));

        assertEquals("test_database", result.get(1).getDatabaseName());
        assertEquals("t1", result.get(1).getOriginTableName());
        assertEquals("c2", result.get(1).getNewColumnName());
        assertEquals("DATE", result.get(1).getNewColumnType());
        assertEquals("ALTER TABLE t1 ADD COLUMN c2 DATE", result.get(1).getSqlsToBeExecuted().get(0));
    }

    @Test
    public void testGenerateSubTaskParameters_withCreateSql_returnsSingleSubTaskParameterWithCorrectValues() {
        // Arrange
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        OnlineSchemaChangeParameters parameters = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.CREATE)
                .sqlContent("CREATE TABLE t1 (c1 INT);")
                .errorStrategy(TaskErrorStrategy.CONTINUE)
                .build();

        SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
        Mockito.when(subTaskParameterFactory.generate(anyString(), Mockito.eq(OnlineSchemaChangeSqlType.CREATE)))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters("test_database", "t1", "c1",
                "INT", new ArrayList<>()));

        // Act
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema,
                subTaskParameterFactory);

        // Assert
        assertEquals(1, result.size());
        assertEquals("test_database", result.get(0).getDatabaseName());
        assertEquals("t1", result.get(0).getOriginTableName());
        assertEquals("c1", result.get(0).getNewColumnName());
        assertEquals("INT", result.get(0).getNewColumnType());
        assertEquals("CREATE TABLE t1 (c1 INT)", result.get(0).getSqlsToBeExecuted().get(0));
    }

    @Test
    public void testGenerateSubTaskParameters_withNoSqls_throwsUnexpectedException() {
        // Arrange
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        OnlineSchemaChangeParameters parameters = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.ALTER)
                .sqlContent("")
                .errorStrategy(TaskErrorStrategy.CONTINUE)
                .build();

        SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);

        // Act & Assert
        try {
            parameters.generateSubTaskParameters(connectionConfig, schema, subTaskParameterFactory);
        } catch (UnexpectedException e) {
            assertEquals("Failed to generate subtasks with parameter: " + parameters, e.getMessage());
        }
    }

    @Test
    public void testGenerateSubTaskParameters_withNullSqlContent_throwsUnexpectedException() {
        // Arrange
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        OnlineSchemaChangeParameters parameters = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.ALTER)
                .sqlContent(null)
                .errorStrategy(TaskErrorStrategy.CONTINUE)
                .build();

        SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);

        // Act & Assert
        try {
            parameters.generateSubTaskParameters(connectionConfig, schema, subTaskParameterFactory);
        } catch (UnexpectedException e) {
            assertEquals("Failed to generate subtasks with parameter: " + parameters, e.getMessage());
        }
    }

    @Test
    @SneakyThrows
    public void testGenerateSubTaskParameters_withNullSubTaskParameterFactory_throwsUnexpectedException() {
        // Arrange
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        OnlineSchemaChangeParameters parameters = OnlineSchemaChangeParameters.builder()
                .sqlType(OnlineSchemaChangeSqlType.ALTER)
                .sqlContent("CREATE TABLE t1 (c1 INT);")
                .errorStrategy(TaskErrorStrategy.CONTINUE)
                .build();

        // Act & Assert
        try {
            parameters.generateSubTaskParameters(connectionConfig, schema, null);
        } catch (UnexpectedException e) {
            assertEquals("Failed to generate subtasks with parameter: " + parameters, e.getMessage());
        }
    }
}