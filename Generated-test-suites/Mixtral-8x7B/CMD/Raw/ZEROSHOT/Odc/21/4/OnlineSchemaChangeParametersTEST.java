import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OnlineSchemaChangeParametersTest {

@Test
public void testGettersAndSetters() {
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table1 ADD column1 int");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(10);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE_NEW);
parameters.setDelimiter(";");
parameters.setLockUsers(new ArrayList<>());
parameters.setSwapTableType(SwapTableType.TEMPORARY_TABLE);

assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
assertEquals("ALTER TABLE table1 ADD column1 int", parameters.getSqlContent());
assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
assertEquals(Integer.valueOf(10), parameters.getLockTableTimeOutSeconds());
assertEquals(Integer.valueOf(3), parameters.getSwapTableNameRetryTimes());
assertEquals(OriginTableCleanStrategy.DROP_AND_CREATE_NEW, parameters.getOriginTableCleanStrategy());
assertEquals(";", parameters.getDelimiter());
assertEquals(new ArrayList<>(), parameters.getLockUsers());
assertEquals(SwapTableType.TEMPORARY_TABLE, parameters.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertTrue(parameters.isContinueOnError());

parameters.setErrorStrategy(TaskErrorStrategy.STOP);
assertFalse(parameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() {
ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
when(connectionConfig.getDialectType()).thenReturn("mysql");
when(connectionConfig.getSchema()).thenReturn("schema1");

OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlContent("ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 int");
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);

try {
when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
.thenReturn(new OnlineSchemaChangeScheduleTaskParameters());
} catch (UnexpectedException e) {
// should not happen
}

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
parameters.generateSubTaskParameters(connectionConfig, "schema1");

assertEquals(2, subTaskParameters.size());
}

@Test
public void testGenerateSubTaskParametersException() {
ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
when(connectionConfig.getDialectType()).thenReturn("mysql");
when(connectionConfig.getSchema()).thenReturn("schema1");

OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlContent("ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 int");
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);

try {
when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
.thenThrow(new UnexpectedException("test exception"));
} catch (UnexpectedException e) {
// expected
}

assertThrows(UnexpectedException.class, () ->
parameters.generateSubTaskParameters(connectionConfig, "schema1")
);
}
}