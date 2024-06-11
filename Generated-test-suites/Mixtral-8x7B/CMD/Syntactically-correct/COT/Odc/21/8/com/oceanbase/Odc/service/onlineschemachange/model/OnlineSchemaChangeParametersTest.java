import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

@Before
public void setUp() {
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
}

@Test
public void testGettersAndSetters() {
// Testing getters and setters for all fields
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());

onlineSchemaChangeParameters.setSqlContent("SQL CONTENT");
assertEquals("SQL CONTENT", onlineSchemaChangeParameters.getSqlContent());

onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());

onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
assertEquals(Integer.valueOf(10), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());

onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
assertEquals(Integer.valueOf(5), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());

onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP_ORIGIN_TABLE);
assertEquals(OriginTableCleanStrategy.KEEP_ORIGIN_TABLE,
onlineSchemaChangeParameters.getOriginTableCleanStrategy());

onlineSchemaChangeParameters.setDelimiter(";");
assertEquals(";", onlineSchemaChangeParameters.getDelimiter());

onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
assertTrue(onlineSchemaChangeParameters.getLockUsers().isEmpty());

onlineSchemaChangeParameters.setSwapTableType(SwapTableType.GLOBAL_TEMPORARY_TABLE);
assertEquals(SwapTableType.GLOBAL_TEMPORARY_TABLE, onlineSchemaChangeParameters.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
// Testing isContinueOnError() method with valid input
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertTrue(onlineSchemaChangeParameters.isContinueOnError());

// Testing isContinueOnError() method with invalid input
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
assertFalse(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() {
// Creating mock ConnectionConfig and String schema
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";

// Testing generateSubTaskParameters() method with empty sqlContent
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
assertEquals(0, subTaskParameters.size());

// Testing generateSubTaskParameters() method with non-empty sqlContent
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT;");
List<String> sqls =
SqlUtils.split(connectionConfig.getDialectType(), onlineSchemaChangeParameters.getSqlContent(),
onlineSchemaChangeParameters.getDelimiter());
assertEquals(1, sqls.size());

// Testing generateSubTaskParameters() method with SwapTableType as GLOBAL_TEMPORARY_TABLE
onlineSchemaChangeParameters.setSwapTableType(SwapTableType.GLOBAL_TEMPORARY_TABLE);
subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
assertEquals(1, subTaskParameters.size());

// Testing generateSubTaskParameters() method with SwapTableType as NORMAL_TABLE
onlineSchemaChangeParameters.setSwapTableType(SwapTableType.NORMAL_TABLE);
subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
assertEquals(1, subTaskParameters.size());

// Testing generateSubTaskParameters() method with OnlineSchemaChangeSqlType as ALTER
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
assertEquals(1, subTaskParameters.size());

// Testing generateSubTaskParameters() method with OnlineSchemaChangeSqlType as CREATE
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
assertEquals(1, subTaskParameters.size());

// Testing generateSubTaskParameters() method with OnlineSchemaChangeSqlType as DROP
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.DROP);
subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
assertEquals(1, subTaskParameters.size());
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersWithException() throws UnexpectedException {
// Creating mock ConnectionConfig and String schema
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";

// Testing generateSubTaskParameters() method with empty sqlContent
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
}
}