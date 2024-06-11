import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private SubTaskParameterFactory subTaskParameterFactory;

    @Mock
    private DdlUtils ddlUtils;

    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(Arrays.asList("user1", "user2"));
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP_TABLE);

        doReturn(ddlUtils).when(subTaskParameterFactory).getDdlUtils();
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_TypicalUseCase() throws Exception {
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(1, result.size());

        verify(subTaskParameterFactory, times(1)).generate(sqlCaptor.capture(), eq(OnlineSchemaChangeSqlType.ALTER));
        String sql = sqlCaptor.getValue();
        assertTrue(sql.startsWith("ALTER TABLE schema.table1 ADD COLUMN c1 INT"));
    }

    @Test
    public void testGenerateSubTaskParameters_EdgeCase_MultipleSqls() throws Exception {
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT; ALTER TABLE table1 ADD COLUMN c2 INT");
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(2, result.size());

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(subTaskParameterFactory, times(2)).generate(sqlCaptor.capture(), eq(OnlineSchemaChangeSqlType.ALTER));
        List<String> sqls = sqlCaptor.getAllValues();
        assertTrue(sqls.get(0).startsWith("ALTER TABLE schema.table1 ADD COLUMN c1 INT"));
        assertTrue(sqls.get(1).startsWith("ALTER TABLE schema.table1 ADD COLUMN c2 INT"));
    }

    @Test
    public void testGenerateSubTaskParameters_ExceptionHandling_FailedToGenerateSubTasks() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenThrow(new UnexpectedException("Failed to generate subtasks"));

        assertThrows(UnexpectedException.class, () -> onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema"));
    }
}