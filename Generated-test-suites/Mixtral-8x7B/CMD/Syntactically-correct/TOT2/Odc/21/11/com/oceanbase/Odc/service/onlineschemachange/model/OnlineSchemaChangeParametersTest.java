import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters createDefaultOnlineSchemaChangeParameters() {
return new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ALTER,
"CREATE TABLE test (id INT)", TaskErrorStrategy.CONTINUE, 10, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, "DELIMITER",
new ArrayList<>(), SwapTableType.INPLACE);
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters parameters = createDefaultOnlineSchemaChangeParameters();
assertTrue(parameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = createDefaultOnlineSchemaChangeParameters();
List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
"schema");
assertEquals(1, result.size());
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersException() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("unknown");
OnlineSchemaChangeParameters parameters = createDefaultOnlineSchemaChangeParameters();
parameters.generateSubTaskParameters(connectionConfig, "schema");
}

@Test
public void testGenerateSubTaskParametersEmptySql() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
OnlineSchemaChangeSqlType.ALTER,
"", TaskErrorStrategy.CONTINUE, 10, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, "DELIMITER",
new ArrayList<>(), SwapTableType.INPLACE);
List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
"schema");
assertEquals(0, result.size());
}

@Test
public void testGenerateSubTaskParametersEmptySqls() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
OnlineSchemaChangeSqlType.ALTER,
"", TaskErrorStrategy.CONTINUE, 10, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, "DELIMITER",
new ArrayList<>(), SwapTableType.INPLACE);
List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
"schema");
assertEquals(0, result.size());
}

@Test
public void testGenerateSubTaskParametersNullSqls() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
OnlineSchemaChangeSqlType.ALTER,
"CREATE TABLE test (id INT)", TaskErrorStrategy.CONTINUE, 10, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, "DELIMITER",
null, SwapTableType.INPLACE);
List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
"schema");
assertEquals(1, result.size());
}

@Test
public void testGenerateSubTaskParametersMultiSql() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
OnlineSchemaChangeSqlType.ALTER,
"CREATE TABLE test1 (id INT); CREATE TABLE test2 (id INT)",
TaskErrorStrategy.CONTINUE, 10, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, "DELIMITER",
new ArrayList<>(), SwapTableType.INPLACE);
List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
"schema");
assertEquals(2, result.size());
}

@Test
public void testGenerateSubTaskParametersAlterTable() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
OnlineSchemaChangeSqlType.ALTER,
"CREATE TABLE test (id INT)", TaskErrorStrategy.CONTINUE, 10, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, "DELIMITER",
new ArrayList<>(), SwapTableType.INPLACE);
List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
"schema");
assertEquals(1, result.size());
String alteredSql = result.get(0).getSqlsToBeExecuted().get(1);
assertEquals("CREATE TABLE test (id INT) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci",
alteredSql);
}

@Test
public void testGenerateSubTaskParametersAddColumn() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
OnlineSchemaChangeSqlType.ALTER,
"ALTER TABLE test ADD COLUMN name VARCHAR(255)", TaskErrorStrategy.CONTINUE, 10, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, "DELIMITER",
new ArrayList<>(), SwapTableType.INPLACE);
List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
"schema");
assertEquals(1, result.size());
String alteredSql = result.get(0).getSqlsToBeExecuted().get(1);
assertEquals("ALTER TABLE test ADD COLUMN name VARCHAR(255) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci",
alteredSql);
}

@Test
public void testGenerateSubTaskParametersSwapTable() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
OnlineSchemaChangeSqlType.SWAP,
"CREATE TABLE test (id INT)", TaskErrorStrategy.CONTINUE, 10, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, "DELIMITER",
new ArrayList<>(), SwapTableType.INPLACE);
List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
"schema");
assertEquals(1, result.size());
String swapSql = result.get(0).getSqlsToBeExecuted().get(0);
assertEquals("RENAME TABLE test TO test_new, test_bak TO test", swapSql);
}

@Test
public void testGenerateSubTaskParametersDropTable() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
OnlineSchemaChangeSqlType.DROP,
"CREATE TABLE test (id INT)", TaskErrorStrategy.CONTINUE, 10, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, "DELIMITER",
new ArrayList<>(), SwapTableType.INPLACE);
List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
"schema");
assertEquals(1, result.size());
String dropSql = result.get(0).getSqlsToBeExecuted().get(0);
assertEquals("DROP TABLE IF EXISTS test_bak", dropSql);
}

@Test
public void testGenerateSubTaskParametersDropTableKeep() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
OnlineSchemaChangeSqlType.DROP,
"CREATE TABLE test (id INT)", TaskErrorStrategy.CONTINUE, 10, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, "DELIMITER",
new ArrayList<>(), SwapTableType.INPLACE);
List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
"schema");
assertEquals(1, result.size());
String dropSql = result.get(0).getSqlsToBeExecuted().get(1);
assertEquals("DROP TABLE IF EXISTS test", dropSql);
}

@Test
public void testGenerateSubTaskParametersDropTableDrop() throws Exception {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
OnlineSchemaChangeSqlType.DROP,
"CREATE TABLE test (id INT)", TaskErrorStrategy.CONTINUE, 10, 3,
OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP, "DELIMITER",
new ArrayList<>(), SwapTableType.INPLACE);
List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
"schema");
assertEquals(1, result.size());
String dropSql = result.get(0).getSqlsToBeExecuted().get(0);
assertEquals("DROP TABLE IF EXISTS test", dropSql);
}
}