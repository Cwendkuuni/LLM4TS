import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void test_isContinueOnError_basic() {
        // Arrange/Act
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        boolean actual = onlineSchemaChangeParameters.isContinueOnError();

        // Assert
        assertTrue(actual);
    }

    @Test
    public void test_isContinueOnError_none() {
        // Arrange/Act
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.NONE);
        boolean actual = onlineSchemaChangeParameters.isContinueOnError();

        // Assert
        assertFalse(actual);
    }

    @Test
    public void test_generateSubTaskParameters_basic() throws UnexpectedException {
        // Arrange
        String schema = "test_schema";

        when(connectionConfig.getDialectType()).thenReturn("dialect");
        when(connectionConfig.getDatabaseName()).thenReturn("test_db");

        OnlineSchemaChangeScheduleTaskParameters sqlTaskParameters1 = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        OnlineSchemaChangeScheduleTaskParameters sqlTaskParameters2 = mock(OnlineSchemaChangeScheduleTaskParameters.class);

        List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
        expected.add(sqlTaskParameters1);
        expected.add(sqlTaskParameters2);

        when(sqlTaskParameters1.getSqlsToBeExecuted()).thenReturn(new ArrayList<>());
        when(sqlTaskParameters2.getSqlsToBeExecuted()).thenReturn(new ArrayList<>());

        when(connectionConfig.getDatabaseName()).thenReturn("test_db");

        when(connectionConfig.createTaskParameterFactory()).thenReturn(mockSubTaskParameterFactory(
            sqlTaskParameters1,
            sqlTaskParameters2
        ));

        String sqlContent = "test_sql_1;" + System.lineSeparator() + "test_sql_2;";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);

        // Act
        List<OnlineSchemaChangeScheduleTaskParameters> actual = onlineSchemaChangeParameters.generateSubTaskParameters(
            connectionConfig,
            schema
        );

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void test_generateSubTaskParameters_emptySqlContent() {
        // Act
        assertThrows(UnexpectedException.class,
            () -> onlineSchemaChangeParameters.generateSubTaskParameters(
                connectionConfig,
                "test_schema"
            )
        );
    }

    @Test
    public void test_generateSubTaskParameters_invalidSql() {
        // Arrange
        String sqlContent = "test_sql_1;";

        when(connectionConfig.createTaskParameterFactory()).thenReturn(mockSubTaskParameterFactory(
            mock(OnlineSchemaChangeScheduleTaskParameters.class)
        ));

        onlineSchemaChangeParameters.setSqlContent(sqlContent);

        // Act & Assert
        assertThrows(UnexpectedException.class,
            () -> onlineSchemaChangeParameters.generateSubTaskParameters(
                connectionConfig,
                "test_schema"
            )
        );
    }

    private SubTaskParameterFactory mockSubTaskParameterFactory(
        OnlineSchemaChangeScheduleTaskParameters... taskParameters
    ) {
        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);

        when(subTaskParameterFactory.generate(any(String.class), eq(OnlineSchemaChangeSqlType.ALTER)))
            .thenReturn(taskParameters);

        return subTaskParameterFactory;
    }

    @Data
    public static class MockTableIdentity implements Serializable, TaskParameters {

        private static final long serialVersionUID = 1L;

        private String databaseName;
        private String originTableName;

    }

    @Data
    public static class MockSubTaskParameterFactory implements SubTaskParameterFactory {

        private final List<OnlineSchemaChangeScheduleTaskParameters> taskParameters;

        public MockSubTaskParameterFactory(List<OnlineSchemaChangeScheduleTaskParameters> taskParameters) {
            this.taskParameters = taskParameters;
        }

        @Override
        public OnlineSchemaChangeScheduleTaskParameters generate(String sql,
            OnlineSchemaChangeSqlType sqlType) {
            return taskParameters.get(0);
        }

        @Override
        public void close() {
            // No-op
        }

    }
}