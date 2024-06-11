import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.params.OnlineSchemaChangeScheduleTaskParameters;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private SubTaskParameterFactory subTaskParameterFactory;

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testGettersAndSetters() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("sql content");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
        onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.CLONE);

        assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
        assertEquals("sql content", onlineSchemaChangeParameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
        assertEquals(Integer.valueOf(10), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        assertEquals(Integer.valueOf(5), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP,
                onlineSchemaChangeParameters.getOriginTableCleanStrategy());
        assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
        assertEquals(new ArrayList<>(), onlineSchemaChangeParameters.getLockUsers());
        assertEquals(OnlineSchemaChangeParameters.SwapTableType.CLONE, onlineSchemaChangeParameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_general() throws Exception {
        String sqlContent = "sql content";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);

        List<String> sqls = new ArrayList<>();
        sqls.add("sql1");
        sqls.add("sql2");
        when(subTaskParameterFactory.generate(anyString(), any()))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

        when(SqlUtils.split(any(), eq(sqlContent), eq(";"))).thenReturn(sqls);

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(sqls.size(), result.size());
    }

    @Test
    public void testGenerateSubTaskParameters_exception() {
        String sqlContent = "sql content";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);

        when(subTaskParameterFactory.generate(anyString(), any()))
                .thenThrow(new RuntimeException("test exception"));

        assertThrows(UnexpectedException.class,
                () -> onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema"));
    }

    @Test
    public void testGenerateSubTaskParameters_emptySql() throws Exception {
        onlineSchemaChangeParameters.setSqlContent("");

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(0, result.size());
    }

    @Test
    public void testGenerateSubTaskParameters_nullSql() throws Exception {
        onlineSchemaChangeParameters.setSqlContent(null);

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(0, result.size());
    }

}