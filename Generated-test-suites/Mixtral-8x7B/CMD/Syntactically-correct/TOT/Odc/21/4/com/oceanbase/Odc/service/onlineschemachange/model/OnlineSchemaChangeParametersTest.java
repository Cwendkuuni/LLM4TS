import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

private ConnectionConfig connectionConfig;

@Before
public void setup() {
connectionConfig = Mockito.mock(ConnectionConfig.class);
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 int");
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
onlineSchemaChangeParameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY);
}

@Test
public void testGettersAndSetters() {
assertNull(onlineSchemaChangeParameters.getDelimiter());
assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
assertEquals("ALTER TABLE table1 ADD column1 int", onlineSchemaChangeParameters.getSqlContent());
assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
assertEquals(Integer.valueOf(10), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
assertEquals(Integer.valueOf(3), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP,
onlineSchemaChangeParameters.getOriginTableCleanStrategy());
assertEquals(0, onlineSchemaChangeParameters.getLockUsers().size());
assertEquals(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY,
onlineSchemaChangeParameters.getSwapTableType());

onlineSchemaChangeParameters.setDelimiter(";");
assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
}

@Test
public void testIsContinueOnError() {
assertTrue(onlineSchemaChangeParameters.isContinueOnError());

onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
assertFalse(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws UnexpectedException {
Mockito.when(connectionConfig.getDialectType()).thenReturn("mysql");

List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, "schema");

Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> expected = new LinkedHashMap<>();
TableIdentity key = new TableIdentity("schema", "table1");
expected.put(key,
new OnlineSchemaChangeScheduleTaskParameters("schema", "table1", "table1",
"ALTER TABLE table1 ADD column1 int", OnlineSchemaChangeSqlType.ALTER));
expected.get(key).getSqlsToBeExecuted().add(
DdlUtils.replaceTableName("ALTER TABLE table1 ADD column1 int", "table2", "mysql",
OnlineSchemaChangeSqlType.ALTER));

assertEquals(expected.size(), result.size());
for (OnlineSchemaChangeScheduleTaskParameters parameter : result) {
TableIdentity tableIdentity = new TableIdentity(parameter.getDatabaseName(),
parameter.getOriginTableName());
assertEquals(expected.get(tableIdentity).getDatabaseName(),
parameter.getDatabaseName());
assertEquals(expected.get(tableIdentity).getOriginTableName(),
parameter.getOriginTableName());
assertEquals(expected.get(tableIdentity).getNewTableName(),
parameter.getNewTableName());
assertEquals(expected.get(tableIdentity).getSqlsToBeExecuted(),
parameter.getSqlsToBeExecuted());
assertEquals(expected.get(tableIdentity).getSqlType(), parameter.getSqlType());
}
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersException() throws UnexpectedException {
Mockito.when(connectionConfig.getDialectType()).thenThrow(RuntimeException.class);

onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
}
}