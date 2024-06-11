import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.common.util.SqlUtils;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private SubTaskParameterFactory subTaskParameterFactory;

    @Test
    public void testGetLockTableTimeOutSeconds() {
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
        assertEquals(30, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(0, result.size());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_Exception() throws Exception {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));
        doThrow(mock(Exception.class)).when(subTaskParameterFactory).close();

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(true, onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
        assertEquals(false, onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_withSqls() {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        List<String> sqls = new ArrayList<>();
        sqls.add("CREATE TABLE t1 (a int);");
        sqls.add("CREATE TABLE t2 (a int);");
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(2, result.size());
    }

    @Test
    public void testGenerateSubTaskParameters_withDifferentDatabase() {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        List<String> sqls = new ArrayList<>();
        sqls.add("CREATE TABLE db1.t1 (a int);");
        sqls.add("CREATE TABLE db2.t2 (a int);");
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(2, result.size());
    }

    public static class OnlineSchemaChangeScheduleTaskParameters implements Serializable {

        private String databaseName;
        private String originTableName;
        private String newTableName;
        private List<String> sqlsToBeExecuted;

        public OnlineSchemaChangeScheduleTaskParameters(String databaseName, String originTableName,
                                                         String newTableName, List<String> sqlsToBeExecuted) {
            this.databaseName = databaseName;
            this.originTableName = originTableName;
            this.newTableName = newTableName;
            this.sqlsToBeExecuted = sqlsToBeExecuted;
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public String getOriginTableName() {
            return originTableName;
        }

        public String getNewTableName() {
            return newTableName;
        }

        public List<String> getSqlsToBeExecuted() {
            return sqlsToBeExecuted;
        }
    }

    public static class SubTaskParameterFactory implements AutoCloseable {
        private ConnectionConfig connectionConfig;
        private String schema;

        public SubTaskParameterFactory(ConnectionConfig connectionConfig, String schema) {
            this.connectionConfig = connectionConfig;
            this.schema = schema;
        }

        public OnlineSchemaChangeScheduleTaskParameters generate(String sql,
                                                                  OnlineSchemaChangeSqlType sqlType) {
            return new OnlineSchemaChangeScheduleTaskParameters("dbName", "tableName", "newTableName",
                    new ArrayList<>());
        }

        @Override
        public void close() throws Exception {
            // Do nothing
        }
    }

    public enum OnlineSchemaChangeSqlType {
        CREATE,
        ALTER
    }

    public enum OriginTableCleanStrategy {
        DROP,
        TRUNCATE
    }

    public enum SwapTableType {
        NONE,
        SWAP
    }
}