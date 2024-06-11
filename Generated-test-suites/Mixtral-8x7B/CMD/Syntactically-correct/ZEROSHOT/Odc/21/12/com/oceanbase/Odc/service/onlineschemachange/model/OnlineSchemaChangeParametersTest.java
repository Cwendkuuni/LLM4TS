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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateSubTaskParametersWithAlterSql() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 int");

        try (SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "schema")) {
            when(subTaskParameterFactory.generate(parameters.getSqlContent(), parameters.getSqlType()))
                    .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

            List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "schema");

            Assert.assertEquals(1, result.size());
            verify(subTaskParameterFactory).generate(parameters.getSqlContent(), parameters.getSqlType());
        } catch (UnexpectedException e) {
            Assert.fail("UnexpectedException should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGenerateSubTaskParametersWithCreateSql() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE table1 (c1 int)");

        try (SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "schema")) {
            when(subTaskParameterFactory.generate(parameters.getSqlContent(), parameters.getSqlType()))
                    .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

            List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "schema");

            Assert.assertEquals(1, result.size());
            verify(subTaskParameterFactory).generate(parameters.getSqlContent(), parameters.getSqlType());
        } catch (UnexpectedException e) {
            Assert.fail("UnexpectedException should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGenerateSubTaskParametersWithException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 int");

        try (SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "schema")) {
            doThrow(new RuntimeException("Expected exception")).when(subTaskParameterFactory).generate(parameters.getSqlContent(), parameters.getSqlType());

            try {
                parameters.generateSubTaskParameters(connectionConfig, "schema");
                Assert.fail("UnexpectedException should be thrown");
            } catch (UnexpectedException e) {
                Assert.assertEquals("Failed to generate subtasks with parameter: " + parameters, e.getMessage());
            }
        }
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        Assert.assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);

        Assert.assertFalse(parameters.isContinueOnError());
    }

    @Data
    private static class OnlineSchemaChangeScheduleTaskParameters implements Serializable {
    }

    public static class DdlUtils {
        public static String replaceTableName(String sql, String newTableName, String dialectType, OnlineSchemaChangeSqlType sqlType) {
            return sql;
        }
    }

    public static class SubTaskParameterFactory implements AutoCloseable {
        private ConnectionConfig connectionConfig;
        private String schema;

        public SubTaskParameterFactory(ConnectionConfig connectionConfig, String schema) {
        }

        public OnlineSchemaChangeScheduleTaskParameters generate(String sql, OnlineSchemaChangeSqlType sqlType) {
            return new OnlineSchemaChangeScheduleTaskParameters();
        }

        @Override
        public void close() {
        }
    }

    public enum SwapTableType {
    }

    public enum OnlineSchemaChangeSqlType {
        ALTER, CREATE
    }

    public enum OriginTableCleanStrategy {
    }

    public static class SqlUtils {
        public static List<String> split(String dialectType, String sqlContent, String delimiter) {
            return new ArrayList<>();
        }
    }

    public interface TaskParameters {
        boolean isContinueOnError();
    }

    public enum TaskErrorStrategy {
        CONTINUE, TERMINATE
    }
}