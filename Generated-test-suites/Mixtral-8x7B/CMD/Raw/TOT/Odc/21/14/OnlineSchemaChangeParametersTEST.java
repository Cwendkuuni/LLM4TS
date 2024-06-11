import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

 @Mock
 private ConnectionConfig connectionConfig;

 @Mock
 private SubTaskParameterFactory subTaskParameterFactory;

 @InjectMocks
 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGenerateSubTaskParameters() throws Exception {
 String schema = "testSchema";
 String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 INT;";
 TableIdentity tableIdentity = new TableIdentity("testSchema", "table1");
 ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
 List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
 expected.add(new OnlineSchemaChangeScheduleTaskParameters(schema, "table1", "testSchema", "table1", "col1",
 "INT", OnlineSchemaChangeSqlType.ALTER, null, null, null, null, null, null, null, null, null, null,
 null, null, null));
 Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
 .thenReturn(expected.get(0));
 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, schema);
 assertEquals(expected, result);
 }

 @Test
 public void testGenerateSubTaskParametersWithException() {
 String schema = "testSchema";
 String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 INT;";
 TableIdentity tableIdentity = new TableIdentity("testSchema", "table1");
 ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
 Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
 .thenThrow(new UnexpectedException("Failed to generate subtasks with parameter: " + onlineSchemaChangeParameters));
 try {
 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 fail("UnexpectedException should be thrown.");
 } catch (UnexpectedException e) {
 // expected
 }
 }

 @Test
 public void testIsContinueOnError() {
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(onlineSchemaChangeParameters.isContinueOnError());
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
 assertFalse(onlineSchemaChangeParameters.isContinueOnError());
 }
}