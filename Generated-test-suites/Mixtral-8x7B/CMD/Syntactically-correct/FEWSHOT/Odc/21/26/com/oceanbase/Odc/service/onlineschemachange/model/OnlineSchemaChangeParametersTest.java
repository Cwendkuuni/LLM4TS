import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.SwapTableType;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private SqlUtils sqlUtils;

    @Mock
    private DdlUtils ddlUtils;

    @Mock
    private SubTaskParameterFactory subTaskParameterFactory;

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE table_name ADD COLUMN new_column data_type";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        Integer lockTableTimeOutSeconds = 300;
        Integer swapTableNameRetryTimes = 5;
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.BACKUP;
        String delimiter = ";";
        List<String> lockUsers = new ArrayList<>();
        SwapTableType swapTableType = SwapTableType.NORMAL;

        onlineSchemaChangeParameters.setSqlType(sqlType);
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        onlineSchemaChangeParameters.setDelimiter(delimiter);
        onlineSchemaChangeParameters.setLockUsers(lockUsers);
        onlineSchemaChangeParameters.setSwapTableType(swapTableType);

        assertEquals(sqlType, onlineSchemaChangeParameters.getSqlType());
        assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());
        assertEquals(errorStrategy, onlineSchemaChangeParameters.getErrorStrategy());
        assertEquals(lockTableTimeOutSeconds, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        assertEquals(swapTableNameRetryTimes, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
        assertEquals(originTableCleanStrategy,
                onlineSchemaChangeParameters.getOriginTableCleanStrategy());
        assertEquals(delimiter, onlineSchemaChangeParameters.getDelimiter());
        assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
        assertEquals(swapTableType, onlineSchemaChangeParameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE table_name ADD COLUMN new_column data_type";
        String delimiter = ";";

        when(connectionConfig.getDialectType()).thenReturn("mysql");
        when(sqlUtils.split(eq("mysql"), anyString(), eq(delimiter)))
                .thenReturn(new ArrayList<>());

        when(subTaskParameterFactory.generate(anyString(), eq(sqlType)))
                .thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(0, result.size());
        verify(sqlUtils).split(eq("mysql"), eq(sqlContent), eq(delimiter));
        verify(subTaskParameterFactory).generate(anyString(), eq(sqlType));
    }

    @SuppressWarnings("serial")
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() throws Exception {
        String sqlContent = "ALTER TABLE table_name ADD COLUMN new_column data_type";

        when(connectionConfig.getDialectType()).thenReturn("mysql");
        when(sqlUtils.split(eq("mysql"), anyString(), anyString()))
                .thenReturn(new ArrayList<>());

        doThrow(new Exception("test exception"))
                .when(subTaskParameterFactory).generate(anyString(), any());

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGenerateSubTaskParametersWithALTER() throws Exception {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE table_name ADD COLUMN new_column data_type";
        String newAlterStmt = "ALTER TABLE new_table_name ADD COLUMN new_column data_type";
        String delimiter = ";";

        when(connectionConfig.getDialectType()).thenReturn("mysql");
        when(sqlUtils.split(eq("mysql"), anyString(), eq(delimiter)))
                .thenReturn(new ArrayList<String>() {{
                    add(sqlContent);
                }});

        when(subTaskParameterFactory.generate(eq(sqlContent), eq(sqlType)))
                .thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        when(ddlUtils.replaceTableName(eq(sqlContent), anyString(), anyString(), eq(sqlType)))
                .thenReturn(newAlterStmt);

        when(subTaskParameterFactory.generate(eq(newAlterStmt), eq(sqlType)))
                .thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(2, result.size());
        verify(sqlUtils).split(eq("mysql"), eq(sqlContent), eq(delimiter));
        verify(subTaskParameterFactory).generate(eq(sqlContent), eq(sqlType));
        verify(ddlUtils).replaceTableName(eq(sqlContent), anyString(), eq("mysql"), eq(sqlType));
        verify(subTaskParameterFactory).generate(eq(newAlterStmt), eq(sqlType));
    }

    @SuppressWarnings("serial")
    @Test
    public void testGenerateSubTaskParametersWithMap() throws Exception {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE table_name ADD COLUMN new_column data_type";
        String delimiter = ";";

        List<String> sqls = new ArrayList<String>() {{
            add(sqlContent);
        }};

        TableIdentity key1 = new TableIdentity("schema", "table_name");
        TableIdentity key2 = new TableIdentity("schema", "new_table_name");

        when(connectionConfig.getDialectType()).thenReturn("mysql");
        when(sqlUtils.split(eq("mysql"), anyString(), eq(delimiter))).thenReturn(sqls);

        when(subTaskParameterFactory.generate(eq(sqlContent), eq(sqlType)))
                .thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        when(subTaskParameterFactory.generate(any(), any()))
                .thenAnswer(invocation -> {
                    Object[] args = invocation.getArguments();
                    OnlineSchemaChangeScheduleTaskParameters parameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
                    TableIdentity key = (TableIdentity) args[0];
                    if (key.equals(key1)) {
                        when(parameter.getDatabaseName()).thenReturn("schema");
                        when(parameter.getOriginTableName()).thenReturn("table_name");
                    } else if (key.equals(key2)) {
                        when(parameter.getDatabaseName()).thenReturn("schema");
                        when(parameter.getNewTableName()).thenReturn("new_table_name");
                    }
                    return parameter;
                });

        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
        taskParameters.put(key1, mock(OnlineSchemaChangeScheduleTaskParameters.class));
        taskParameters.put(key2, mock(OnlineSchemaChangeScheduleTaskParameters.class));

        when(subTaskParameterFactory.generateParametersMap(eq(sqls), eq(sqlType)))
                .thenReturn(taskParameters);

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(2, result.size());
        verify(sqlUtils).split(eq("mysql"), eq(sqlContent), eq(delimiter));
        verify(subTaskParameterFactory).generate(eq(sqlContent), eq(sqlType));
        verify(subTaskParameterFactory).generateParametersMap(eq(sqls), eq(sqlType));
    }

}