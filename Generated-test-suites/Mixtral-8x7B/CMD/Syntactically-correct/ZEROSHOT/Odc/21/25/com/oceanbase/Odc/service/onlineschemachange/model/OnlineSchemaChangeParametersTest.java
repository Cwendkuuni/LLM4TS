import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
private ConnectionConfig connectionConfig;
private SubTaskParameterFactory subTaskParameterFactory;

@Before
public void setUp() {
connectionConfig = Mockito.mock(ConnectionConfig.class);
subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 int;");
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
}

@Test
public void testIsContinueOnError() {
Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws UnexpectedException {
List<OnlineSchemaChangeScheduleTaskParameters> expectedParameters = Arrays.asList(
new OnlineSchemaChangeScheduleTaskParameters(), new OnlineSchemaChangeScheduleTaskParameters());

Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
.thenReturn(expectedParameters.get(0), expectedParameters.get(1));

List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, "schema");

Assert.assertEquals(expectedParameters, result);

Mockito.verify(subTaskParameterFactory, Mockito.times(2)).generate(Mockito.anyString(), Mockito.any());
}

}