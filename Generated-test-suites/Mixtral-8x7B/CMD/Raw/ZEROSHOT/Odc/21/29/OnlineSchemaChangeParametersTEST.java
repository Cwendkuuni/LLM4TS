import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Mock
    private SubTaskParameterFactory subTaskParameterFactory;

    @Mock
    private ConnectionConfig connectionConfig;

    @Test
    public void testGenerateSubTaskParameters() throws UnexpectedException {
        when(connectionConfig.getDialectType()).thenReturn("dialectType");
        when(subTaskParameterFactory.generate(mock(String.class), OnlineSchemaChangeSqlType.ALTER))
                .thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(1, result.size());
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(true, onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertEquals(false, onlineSchemaChangeParameters.isContinueOnError());
    }
}