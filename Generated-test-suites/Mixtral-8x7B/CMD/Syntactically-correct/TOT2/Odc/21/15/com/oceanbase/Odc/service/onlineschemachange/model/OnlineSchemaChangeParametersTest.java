import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters createOnlineSchemaChangeParameters() {
ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
when(connectionConfig.getDialectType()).thenReturn("mysql");

OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
params.setSqlContent("ALTER TABLE table_name ADD column_name datatype");
params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
params.setLockTableTimeOutSeconds(10);
params.setSwapTableNameRetryTimes(5);
params.setOriginTableCleanStrategy(OriginTableCleanStrategy.NOT_CLEAN);
params.setDelimiter(";");
params.setLockUsers(new ArrayList<>());
params.setSwapTableType(SwapTableType.SYSTABLE);

return params;
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters params = createOnlineSchemaChangeParameters();
assertEquals(true, params.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() {
ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
when(connectionConfig.getDialectType()).thenReturn("mysql");

OnlineSchemaChangeParameters params = createOnlineSchemaChangeParameters();
SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);

try {
Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
taskParameters.put(new TableIdentity("schema", "table_name"),
new OnlineSchemaChangeScheduleTaskParameters("schema", "table_name", "new_table_name"));

when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
.thenReturn(new OnlineSchemaChangeScheduleTaskParameters("schema", "table_name", "new_table_name"));

List<OnlineSchemaChangeScheduleTaskParameters> result = params.generateSubTaskParameters(connectionConfig, "schema");

assertEquals(1, result.size());
assertEquals("schema", result.get(0).getDatabaseName());
assertEquals("table_name", result.get(0).getOriginTableName());
assertEquals("new_table_name", result.get(0).getNewTableName());
} catch (UnexpectedException e) {
// Should not be thrown
}
}

@Test
public void testGenerateSubTaskParameters_exception() {
ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
when(connectionConfig.getDialectType()).thenReturn("mysql");

OnlineSchemaChangeParameters params = createOnlineSchemaChangeParameters();
SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);

try {
when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
.thenThrow(new RuntimeException());

assertThrows(UnexpectedException.class, () -> {
params.generateSubTaskParameters(connectionConfig, "schema");
});
} catch (UnexpectedException e) {
// Should be thrown
}
}
}