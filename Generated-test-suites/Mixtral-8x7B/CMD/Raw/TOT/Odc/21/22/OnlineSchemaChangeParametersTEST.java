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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
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
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testIsContinueOnError() {
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(onlineSchemaChangeParameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() throws Exception {
 String sqlContent = "ALTER TABLE table1 ADD COLUMN c1 int;";
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, "schema");
 assertEquals(1, result.size());
 assertEquals("schema", result.get(0).getDatabaseName());
 assertEquals("table1", result.get(0).getOriginTableName());
 assertEquals(sqlContent, result.get(0).getSqlsToBeExecuted().get(0));
 String newAlterStmt = DdlUtils.replaceTableName(sqlContent, result.get(0).getNewTableName(),
 connectionConfig.getDialectType(), sqlType);
 assertEquals(2, result.get(0).getSqlsToBeExecuted().size());
 assertEquals(newAlterStmt, result.get(0).getSqlsToBeExecuted().get(1));
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersException() throws Exception {
 onlineSchemaChangeParameters.setSqlContent("invalid sql");
 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
 }

 @Data
 private static class OnlineSchemaChangeScheduleTaskParameters {
 String databaseName;
 String originTableName;
 String newTableName;
 List<String> sqlsToBeExecuted;
 }

}