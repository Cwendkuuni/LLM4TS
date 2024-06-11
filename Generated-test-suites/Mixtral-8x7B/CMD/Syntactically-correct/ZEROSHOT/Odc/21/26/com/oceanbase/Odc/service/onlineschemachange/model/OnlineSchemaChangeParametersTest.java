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

import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testGettersAndSetters() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table_name ADD COLUMN column_name datatype");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(300);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP_ORIGIN);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP_TABLE);

        Assert.assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
        Assert.assertEquals("ALTER TABLE table_name ADD COLUMN column_name datatype",
                onlineSchemaChangeParameters.getSqlContent());
        Assert.assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
        Assert.assertEquals(Integer.valueOf(300), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        Assert.assertEquals(Integer.valueOf(5), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
        Assert.assertEquals(OriginTableCleanStrategy.KEEP_ORIGIN,
                onlineSchemaChangeParameters.getOriginTableCleanStrategy());
        Assert.assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
        Assert.assertEquals(new ArrayList<>(), onlineSchemaChangeParameters.getLockUsers());
        Assert.assertEquals(SwapTableType.SWAP_TABLE, onlineSchemaChangeParameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
        Assert.assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws UnexpectedException {
        String sqlContent = "ALTER TABLE table_name ADD COLUMN column_name datatype; "
                + "ALTER TABLE table_name ADD COLUMN column_name2 datatype2";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        when(connectionConfig.getDialectType()).thenReturn("mysql");
        when(connectionConfig.getDatabaseName()).thenReturn("test_db");
        when(connectionConfig.getSchemaName()).thenReturn("test_schema");
        when(connectionConfig.getTableName()).thenReturn("test_table");

        SubTaskParameterFactory subTaskParameterFactory =
                new SubTaskParameterFactory(connectionConfig, "test_schema");

        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters =
                new LinkedHashMap<>();
        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, onlineSchemaChangeParameters.getDelimiter());

        for (String sql : sqls) {
            OnlineSchemaChangeScheduleTaskParameters parameter =
                    subTaskParameterFactory.generate(sql, onlineSchemaChangeParameters.getSqlType());
            TableIdentity key = new TableIdentity(parameter.getDatabaseName(), parameter.getOriginTableName());
            taskParameters.putIfAbsent(key, parameter);
            if (onlineSchemaChangeParameters.getSqlType() == OnlineSchemaChangeSqlType.ALTER) {
                String newAlterStmt = DdlUtils.replaceTableName(sql, parameter.getNewTableName(),
                        connectionConfig.getDialectType(), onlineSchemaChangeParameters.getSqlType());
                taskParameters.get(key).getSqlsToBeExecuted().add(newAlterStmt);
            }
        }

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
                onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");

        Assert.assertEquals(taskParameters.size(), subTaskParameters.size());

        for (Map.Entry<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> entry : taskParameters.entrySet()) {
            boolean found = false;
            for (OnlineSchemaChangeScheduleTaskParameters subTaskParameter : subTaskParameters) {
                if (entry.getKey().equals(subTaskParameter.getTableIdentity())) {
                    found = true;
                    break;
                }
            }
            Assert.assertTrue(found);
        }
    }

}