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
@PrepareForTest({ SqlUtils.class, DdlUtils.class, SubTaskParameterFactory.class })
public class OnlineSchemaChangeParametersTest {

    @Test
    public void testConstructor() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
        assertEquals(";", params.getDelimiter());
        assertNull(params.getLockUsers());
        assertEquals(TaskErrorStrategy.STOP, params.getErrorStrategy());
        assertEquals(null, params.getLockTableTimeOutSeconds());
        assertEquals(null, params.getOriginTableCleanStrategy());
        assertEquals(null, params.getSwapTableNameRetryTimes());
        assertEquals(null, params.getSwapTableType());
        assertEquals("", params.getSqlContent());
    }

    @Test
    public void testSettersAndGetters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

        params.setSqlType(OnlineSchemaChangeSqlType.RENAME);
        params.setDelimiter(",");
        params.setLockUsers(List.of("user1", "user2"));
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(120);
        params.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
        params.setSwapTableNameRetryTimes(3);
        params.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY);
        params.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 INT;");

        assertEquals(OnlineSchemaChangeSqlType.RENAME, params.getSqlType());
        assertEquals(",", params.getDelimiter());
        assertEquals(List.of("user1", "user2"), params.getLockUsers());
        assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
        assertEquals(120, params.getLockTableTimeOutSeconds());
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP,
                params.getOriginTableCleanStrategy());
        assertEquals(3, params.getSwapTableNameRetryTimes());
        assertEquals(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY, params.getSwapTableType());
        assertEquals("ALTER TABLE t1 ADD COLUMN c1 INT;", params.getSqlContent());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        String sqlContent = "ALTER TABLE t1 ADD COLUMN c1 INT; ALTER TABLE t1 DROP COLUMN c2;";
        String schema = "test_schema";

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        SqlUtils sqlUtilsMock = PowerMockito.mock(SqlUtils.class);
        PowerMockito.when(SqlUtils.split(connectionConfig.getDialectType(), sqlContent, ";"))
                .thenReturn(List.of("ALTER TABLE t1 ADD COLUMN c1 INT;", "ALTER TABLE t1 DROP COLUMN c2;"));
        PowerMockito.when(sqlUtilsMock.split(connectionConfig.getDialectType(), sqlContent, ";"))
                .thenReturn(List.of("ALTER TABLE t1 ADD COLUMN c1 INT;", "ALTER TABLE t1 DROP COLUMN c2;"));

        DdlUtils ddlUtilsMock = PowerMockito.mock(DdlUtils.class);
        PowerMockito.when(
                DdlUtils.replaceTableName("ALTER TABLE t1 ADD COLUMN c1 INT;", "new_table_name", "mysql",
                        OnlineSchemaChangeSqlType.ALTER))
                .thenReturn("ALTER TABLE new_table_name ADD COLUMN c1 INT;");

        SubTaskParameterFactory subTaskParameterFactoryMock = PowerMockito.mock(SubTaskParameterFactory.class);

        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent(sqlContent);
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig,
                schema);

        assertEquals(2, subTaskParameters.size());

        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
        for (OnlineSchemaChangeScheduleTaskParameters subTaskParameter : subTaskParameters) {
            TableIdentity key = new TableIdentity(subTaskParameter.getDatabaseName(),
                    subTaskParameter.getOriginTableName());
            taskParameters.putIfAbsent(key, subTaskParameter);

            if (params.getSqlType() == OnlineSchemaChangeSqlType.ALTER) {
                String newAlterStmt = DdlUtils.replaceTableName(subTaskParameter.getSqlsToBeExecuted().get(0),
                        subTaskParameter.getNewTableName(), connectionConfig.getDialectType(), sqlType);
                taskParameters.get(key).getSqlsToBeExecuted().add(newAlterStmt);
            }
        }

        assertEquals(1, taskParameters.get(new TableIdentity(params.getDatabaseName(), "t1")).getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE t1 ADD COLUMN c1 INT;",
                taskParameters.get(new TableIdentity(params.getDatabaseName(), "t1")).getSqlsToBeExecuted().get(0));

        assertEquals(1, taskParameters.get(new TableIdentity(params.getDatabaseName(), "new_table_name")).getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE new_table_name ADD COLUMN c1 INT;",
                taskParameters.get(new TableIdentity(params.getDatabaseName(), "new_table_name")).getSqlsToBeExecuted().get(0));
    }
}