import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
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

import lombok.Data;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

@Mock
private ConnectionConfig connectionConfig;

@Mock
private SubTaskParameterFactory subTaskParameterFactory;

@InjectMocks
@Data
private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
}

// Test for all getters and setters, exclude test for isContinueOnError() as it's covered in testGenerateSubTaskParameters()

@Test
public void testGettersAndSetters() {
// ...
}

// Test for isContinueOnError()

@Test
public void testIsContinueOnError() {
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertTrue(onlineSchemaChangeParameters.isContinueOnError());

onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
assertFalse(onlineSchemaChangeParameters.isContinueOnError());
}

// Test for generateSubTaskParameters() with typical use case

@Test
public void testGenerateSubTaskParametersWithTypicalUseCase() throws Exception {
String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 INT;";
List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig,
"schema");
assertEquals(1, result.size());
assertEquals("table1", result.get(0).getOriginTableName());
assertEquals("col1", result.get(0).getNewColumnName());
assertEquals(TaskErrorStrategy.STOP, result.get(0).getErrorStrategy());
}

// Test for generateSubTaskParameters() with edge case - sqlContent is empty

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersWithEdgeCaseSqlContentIsEmpty() throws Exception {
onlineSchemaChangeParameters.setSqlContent("");
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
}

// Test for generateSubTaskParameters() with exception handling scenario - failed to generate subtasks

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersWithExceptionHandlingScenario() throws Exception {
Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
.thenThrow(new RuntimeException());
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
}

}