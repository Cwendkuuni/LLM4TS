import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters parameters;

@Before
public void setUp() {
parameters = new OnlineSchemaChangeParameters();
}

@Test
public void testGettersAndSetters() {
// Test typical use cases
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table_name ADD column_name datatype");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(120);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
parameters.setDelimiter(";");
parameters.setLockUsers(Arrays.asList("user1", "user2"));
parameters.setSwapTableType(SwapTableType.TEMPORARY);

assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
assertEquals("ALTER TABLE table_name ADD column_name datatype", parameters.getSqlContent());
assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
assertEquals(Integer.valueOf(120), parameters.getLockTableTimeOutSeconds());
assertEquals(Integer.valueOf(3), parameters.getSwapTableNameRetryTimes());
assertEquals(OriginTableCleanStrategy.KEEP, parameters.getOriginTableCleanStrategy());
assertEquals(";", parameters.getDelimiter());
assertEquals(Arrays.asList("user1", "user2"), parameters.getLockUsers());
assertEquals(SwapTableType.TEMPORARY, parameters.getSwapTableType());

// Test edge cases
parameters.setSqlType(null);
parameters.setSqlContent(null);
parameters.setErrorStrategy(null);
parameters.setLockTableTimeOutSeconds(null);
parameters.setSwapTableNameRetryTimes(null);
parameters.setOriginTableCleanStrategy(null);
parameters.setLockUsers(null);
parameters.setSwapTableType(null);

assertEquals(null, parameters.getSqlType());
assertEquals(null, parameters.getSqlContent());
assertEquals(null, parameters.getErrorStrategy());
assertEquals(null, parameters.getLockTableTimeOutSeconds());
assertEquals(null, parameters.getSwapTableNameRetryTimes());
assertEquals(null, parameters.getOriginTableCleanStrategy());
assertEquals(null, parameters.getLockUsers());
assertEquals(null, parameters.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
// Test typical use cases
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertTrue(parameters.isContinueOnError());

parameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
assertFalse(parameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";

try {
SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, schema);

// Test typical use cases
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table_name ADD column_name datatype; ALTER TABLE table_name ADD column2_name datatype2");
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig,
schema);
assertEquals(1, subTaskParameters.size());

OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
TableIdentity key = new TableIdentity(subTaskParameter.getDatabaseName(),
subTaskParameter.getOriginTableName());
Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
taskParameters.put(key, subTaskParameter);

String newAlterStmt = DdlUtils.replaceTableName(subTaskParameter.getSqlsToBeExecuted().get(0),
subTaskParameter.getNewTableName(), connectionConfig.getDialectType(),
OnlineSchemaChangeSqlType.ALTER);
taskParameters.get(key).getSqlsToBeExecuted().add(newAlterStmt);

assertEquals(subTaskParameter.getSqlsToBeExecuted().get(0), "ALTER TABLE table_name ADD column_name datatype");
assertEquals(subTaskParameter.getSqlsToBeExecuted().get(1), newAlterStmt);

// Test error scenarios
parameters.setSqlContent("ALTER TABLE non_existent_table_name ADD column_name datatype");
subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
} catch (UnexpectedException e) {
fail("UnexpectedException should not be thrown.");
}
}
}