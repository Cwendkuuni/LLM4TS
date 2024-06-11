import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters createDefaultOnlineSchemaChangeParameters() {
return new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER,
"ALTER TABLE table1 ADD COLUMN c1 INT", TaskErrorStrategy.CONTINUE, 60, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, null,
SwapTableType.SYS_TEMPORARY, new ArrayList<>());
}

@Test
public void testGettersAndSetters() {
OnlineSchemaChangeParameters params = createDefaultOnlineSchemaChangeParameters();
assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
assertEquals("ALTER TABLE table1 ADD COLUMN c1 INT", params.getSqlContent());
assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
assertEquals(Integer.valueOf(60), params.getLockTableTimeOutSeconds());
assertEquals(Integer.valueOf(3), params.getSwapTableNameRetryTimes());
assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, params.getOriginTableCleanStrategy());
assertEquals(";", params.getDelimiter());
assertEquals(SwapTableType.SYS_TEMPORARY, params.getSwapTableType());
assertEquals(new ArrayList<>(), params.getLockUsers());
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters params = createDefaultOnlineSchemaChangeParameters();
assertEquals(true, params.isContinueOnError());
params.setErrorStrategy(TaskErrorStrategy.STOP);
assertEquals(false, params.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
OnlineSchemaChangeParameters params = createDefaultOnlineSchemaChangeParameters();
List<OnlineSchemaChangeScheduleTaskParameters> result = params.generateSubTaskParameters(connectionConfig, schema);
// Add assertions based on the expected output
}

@Test
public void testGenerateSubTaskParametersWithException() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters(null, "", null, null, null, null, null,
null, null);
assertThrows(UnexpectedException.class, () -> params.generateSubTaskParameters(connectionConfig, schema));
}

@Test
public void testGenerateSubTaskParametersWithEmptySqlContent() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER, "",
TaskErrorStrategy.CONTINUE, null, null, null, null, null, null);
List<OnlineSchemaChangeScheduleTaskParameters> result = params.generateSubTaskParameters(connectionConfig, schema);
assertEquals(0, result.size());
}

@Test
public void testGenerateSubTaskParametersWithNullSqlContent() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER, null,
TaskErrorStrategy.CONTINUE, null, null, null, null, null, null);
assertThrows(IllegalArgumentException.class,
() -> params.generateSubTaskParameters(connectionConfig, schema));
}

@Test
public void testGenerateSubTaskParametersWithNullSqlType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters(null, "sql",
TaskErrorStrategy.CONTINUE, null, null, null, null, null, null);
assertThrows(IllegalArgumentException.class,
() -> params.generateSubTaskParameters(connectionConfig, schema));
}

@Test
public void testGenerateSubTaskParametersWithNullErrorStrategy() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER, "sql",
null, null, null, null, null, null, null);
assertThrows(IllegalArgumentException.class,
() -> params.generateSubTaskParameters(connectionConfig, schema));
}

@Test
public void testGenerateSubTaskParametersWithEmptySqls() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("testDialect");
String schema = "testSchema";
List<String> sqls = new ArrayList<>();
sqls.add("");
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER,
String.join(";", sqls), TaskErrorStrategy.CONTINUE, 60, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, null,
SwapTableType.SYS_TEMPORARY, sqls);
List<OnlineSchemaChangeScheduleTaskParameters> result = params.generateSubTaskParameters(connectionConfig, schema);
assertEquals(0, result.size());
}

@Test
public void testGenerateSubTaskParametersWithNullLockTableTimeout() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
List<String> sqls = new ArrayList<>();
sqls.add("sql");
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER,
String.join(";", sqls), TaskErrorStrategy.CONTINUE, null, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, null,
SwapTableType.SYS_TEMPORARY, sqls);
assertThrows(NullPointerException.class,
() -> params.generateSubTaskParameters(connectionConfig, schema));
}

@Test
public void testGenerateSubTaskParametersWithNullSwapTableRetryTimes() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
List<String> sqls = new ArrayList<>();
sqls.add("sql");
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER,
String.join(";", sqls), TaskErrorStrategy.CONTINUE, 60, null,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, null,
SwapTableType.SYS_TEMPORARY, sqls);
assertThrows(NullPointerException.class,
() -> params.generateSubTaskParameters(connectionConfig, schema));
}

@Test
public void testGenerateSubTaskParametersWithNullOriginTableCleanStrategy() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
List<String> sqls = new ArrayList<>();
sqls.add("sql");
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER,
String.join(";", sqls), TaskErrorStrategy.CONTINUE, 60, 3, null, null,
SwapTableType.SYS_TEMPORARY, sqls);
assertThrows(NullPointerException.class,
() -> params.generateSubTaskParameters(connectionConfig, schema));
}

@Test
public void testGenerateSubTaskParametersWithNullDelimiter() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
List<String> sqls = new ArrayList<>();
sqls.add("sql");
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER,
String.join(";", sqls), TaskErrorStrategy.CONTINUE, 60, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, null,
SwapTableType.SYS_TEMPORARY, sqls);
params.setDelimiter(null);
assertThrows(NullPointerException.class,
() -> params.generateSubTaskParameters(connectionConfig, schema));
}

@Test
public void testGenerateSubTaskParametersWithNullLockUsers() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
List<String> sqls = new ArrayList<>();
sqls.add("sql");
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER,
String.join(";", sqls), TaskErrorStrategy.CONTINUE, 60, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, null,
SwapTableType.SYS_TEMPORARY, sqls);
params.setLockUsers(null);
assertThrows(NullPointerException.class,
() -> params.generateSubTaskParameters(connectionConfig, schema));
}

@Test
public void testGenerateSubTaskParametersWithNullSwapTableType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
List<String> sqls = new ArrayList<>();
sqls.add("sql");
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER,
String.join(";", sqls), TaskErrorStrategy.CONTINUE, 60, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, null,
null, sqls);
assertThrows(NullPointerException.class,
() -> params.generateSubTaskParameters(connectionConfig, schema));
}

}