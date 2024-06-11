import static org.junit.Assert.*;

import java.io.Serializable;
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
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.OnlineSchemaChangeScheduleTaskParameters;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

 @Mock
 private ConnectionConfig connectionConfig;

 @Mock
 private SqlUtils sqlUtils;

 @Mock
 private DdlUtils ddlUtils;

 @Mock
 private SubTaskParameterFactory subTaskParameterFactory;

 @InjectMocks
 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetSqlType() {
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
 onlineSchemaChangeParameters.setSqlType(sqlType);
 assertEquals(sqlType, onlineSchemaChangeParameters.getSqlType());
 }

 @Test
 public void testGenerateSubTaskParameters() {
 String schema = "test_schema";
 List<String> sqls = new ArrayList<>();
 sqls.add("ALTER TABLE test_table ADD COLUMN c1 INT");
 Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
 taskParameters.put(new TableIdentity("test_schema", "test_table"),
 new OnlineSchemaChangeScheduleTaskParameters("test_schema", "test_table", "new_test_table", sqls));

 try {
 Mockito.when(connectionConfig.getDialectType()).thenReturn("MySQL");
 Mockito.when(sqlUtils.split(connectionConfig.getDialectType(), onlineSchemaChangeParameters.getSqlContent(),
 onlineSchemaChangeParameters.getDelimiter()))
 .thenReturn(sqls);
 Mockito.when(subTaskParameterFactory.generate(sqls.get(0), OnlineSchemaChangeSqlType.ALTER))
 .thenReturn(taskParameters.get(new TableIdentity("test_schema", "test_table")));
 } catch (Exception e) {
 fail("Failed to setup mock: " + e.getMessage());
 }

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, schema);
 assertEquals(taskParameters, result);
 }

 @Test
 public void testGenerateSubTaskParameters_exception() {
 String schema = "test_schema";
 try {
 Mockito.when(connectionConfig.getDialectType()).thenThrow(new UnexpectedException("mock error"));
 } catch (Exception e) {
 fail("Failed to setup mock: " + e.getMessage());
 }

 try {
 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 fail("Expected exception not thrown");
 } catch (UnexpectedException e) {
 assertEquals("Failed to generate subtasks with parameter: " + onlineSchemaChangeParameters, e.getMessage());
 }
 }

}