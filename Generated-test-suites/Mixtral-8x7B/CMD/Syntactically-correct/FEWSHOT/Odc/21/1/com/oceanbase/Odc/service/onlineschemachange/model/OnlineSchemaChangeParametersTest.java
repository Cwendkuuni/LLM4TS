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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.AFTER_SWAP);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(List.of("user1", "user2"));
        onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.SHARDING_DB_SWAP);
    }

    @Test
    public void testIsContinueOnError() {
        assertEquals(true, onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 INT; ALTER TABLE table2 ADD COLUMN col2 INT;";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);

        when(connectionConfig.getDialectType()).thenReturn("mysql");
        
        Map<String, String> databaseAndTableMap = new LinkedHashMap<>();
        databaseAndTableMap.put("db1", "table1");
        databaseAndTableMap.put("db2", "table2");
        when(connectionConfig.getDatabaseAndTableMap()).thenReturn(databaseAndTableMap);

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testSchema");

        assertEquals(2, result.size());
        assertEquals("db1", result.get(0).getDatabaseName());
        assertEquals("table1", result.get(0).getOriginTableName());
        assertEquals(List.of("ALTER TABLE table1 ADD COLUMN col1 INT"), result.get(0).getSqlsToBeExecuted());
        assertEquals("db2", result.get(1).getDatabaseName());
        assertEquals("table2", result.get(1).getOriginTableName());
        assertEquals(List.of("ALTER TABLE table2 ADD COLUMN col2 INT"), result.get(1).getSqlsToBeExecuted());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() throws Exception {
        String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 INT; ALTER TABLE table2 ADD COLUMN col2 INT;";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);

        when(connectionConfig.getDialectType()).thenThrow(new RuntimeException("Unexpected exception"));

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testSchema");
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptySqlContent() throws Exception {
        onlineSchemaChangeParameters.setSqlContent("");

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testSchema");

        assertEquals(0, result.size());
    }

    @Test
    public void testGenerateSubTaskParametersWithNullSqlContent() throws Exception {
        onlineSchemaChangeParameters.setSqlContent(null);

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testSchema");

        assertEquals(0, result.size());
    }

    @Test
    public void testGenerateSubTaskParametersWithNullConnectionConfig() throws Exception {
        onlineSchemaChangeParameters.generateSubTaskParameters(null, "testSchema");
    }

    @Test
    public void testGenerateSubTaskParametersWithNullSchema() throws Exception {
        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, null);
    }
}