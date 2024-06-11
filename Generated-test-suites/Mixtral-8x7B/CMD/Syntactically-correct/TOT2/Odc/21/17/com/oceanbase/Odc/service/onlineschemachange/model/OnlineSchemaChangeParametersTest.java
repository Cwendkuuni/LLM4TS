import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

 @Mock
 private ConnectionConfig connectionConfig;

 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table_name ADD column_name varchar(255)");
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
 onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
 onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
 onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
 onlineSchemaChangeChangeParameters.setSwapTableType(SwapTableType.IN_PLACE);
 }

 @Test
 public void testIsContinueOnError() {
 assertEquals(true, onlineSchemaChangeParameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() throws Exception {
 when(connectionConfig.getDialectType()).thenReturn("mysql");
 when(connectionConfig.getDatabaseName()).thenReturn("db_name");
 when(connectionConfig.getSchemaName()).thenReturn("schema_name");

 Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
 taskParameters.put(new TableIdentity("db_name", "table_name"),
 OnlineSchemaChangeScheduleTaskParameters.builder()
 .databaseName("db_name")
 .originTableName("table_name")
 .newTableName("new_table_name")
 .sqlsToBeExecuted(new ArrayList<>())
 .build());

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig,
 "schema_name");

 assertEquals(1, result.size());
 assertEquals("db_name", result.get(0).getDatabaseName());
 assertEquals("table_name", result.get(0).getOriginTableName());
 assertEquals("new_table_name", result.get(0).getNewTableName());
 assertEquals(1, result.get(0).getSqlsToBeExecuted().size());
 assertEquals("ALTER TABLE new_table_name ADD column_name varchar(255)",
 result.get(0).getSqlsToBeExecuted().get(0));
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParameters_Exception() throws Exception {
 when(connectionConfig.getDialectType()).thenThrow(new Exception());

 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema_name");
 }
}

class OnlineSchemaChangeSqlType {
 public static final OnlineSchemaChangeSqlType ALTER = new OnlineSchemaChangeSqlType("ALTER");

 private final String name;

 OnlineSchemaChangeSqlType(String name) {
 this.name = name;
 }
}

class OriginTableCleanStrategy {
 public static final OriginTableCleanStrategy KEEP = new OriginTableCleanStrategy("KEEP");

 private final String name;

 OriginTableCleanStrategy(String name) {
 this.name = name;
 }
}

class SwapTableType {
 public static final SwapTableType IN_PLACE = new SwapTableType("IN_PLACE");

 private final String name;

 SwapTableType(String name) {
 this.name = name;
 }
}

@Data
class OnlineSchemaChangeScheduleTaskParameters {
 private String databaseName;
 private String originTableName;
 private String newTableName;
 private List<String> sqlsToBeExecuted;
 private String schemaName;
 private String databaseType;
}