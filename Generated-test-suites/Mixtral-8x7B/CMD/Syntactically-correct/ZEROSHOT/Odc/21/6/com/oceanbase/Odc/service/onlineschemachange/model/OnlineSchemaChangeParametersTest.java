import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private SubTaskParameterFactory subTaskParameterFactory;

    @Test
    public void testGenerateSubTaskParameters() throws UnexpectedException {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        onlineSchemaChangeParameters.setSqlType(sqlType);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 int; ALTER TABLE t1 DROP COLUMN c1;");
        onlineSchemaChangeParameters.setDelimiter(";");

        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), onlineSchemaChangeParameters.getSqlContent(),
                onlineSchemaChangeParameters.getDelimiter());

        when(subTaskParameterFactory.generate(sqls.get(0), sqlType)).thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));
        when(subTaskParameterFactory.generate(sqls.get(1), sqlType)).thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
        taskParameters.put(new TableIdentity("db1", "t1"), mock(OnlineSchemaChangeScheduleTaskParameters.class));
        taskParameters.put(new TableIdentity("db1", "t1"), mock(OnlineSchemaChangeScheduleTaskParameters.class));

        when(subTaskParameterFactory.newInstance()).thenReturn(subTaskParameterFactory);

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "db1");

        assertEquals(result.size(), 2);
        assertTrue(result.get(0) instanceof OnlineSchemaChangeScheduleTaskParameters);
        assertTrue(result.get(1) instanceof OnlineSchemaChangeScheduleTaskParameters);
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }
}