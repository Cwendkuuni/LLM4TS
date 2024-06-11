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
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetIsContinueOnError() {
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(onlineSchemaChangeParameters.isContinueOnError());

 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
 assertFalse(onlineSchemaChangeParameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() throws Exception {
 String sqlContent = "ALTER TABLE table1 ADD COLUMN c1 INT;";
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;

 onlineSchemaChangeParameters.setSqlContent(sqlContent);
 onlineSchemaChangeParameters.setSqlType(sqlType);

 Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters =
 new LinkedHashMap<TableIdentity, OnlineSchemaChangeScheduleTaskParameters>() {{
 put(new TableIdentity("schema1", "table1"), new OnlineSchemaChangeScheduleTaskParameters());
 }};

 List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>(taskParameters.values());

 DdlUtils ddlUtils = new DdlUtils();
 ddlUtils.setDialectType("mysql");

 when(subTaskParameterFactory.generate(sqlContent, sqlType)).thenReturn(taskParameters.get(new TableIdentity("schema1", "table1")));
 when(subTaskParameterFactory.generate(ddlUtils.replaceTableName(sqlContent, "table2", "mysql", sqlType), sqlType))
 .thenReturn(taskParameters.get(new TableIdentity("schema1", "table1")));

 assertEquals(expected, onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema1"));
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersException() throws Exception {
 String sqlContent = "ALTER TABLE table1 ADD COLUMN c1 INT;";
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;

 onlineSchemaChangeParameters.setSqlContent(sqlContent);
 onlineSchemaChangeParameters.setSqlType(sqlType);

 when(subTaskParameterFactory.generate(sqlContent, sqlType)).thenThrow(new Exception());

 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema1");
 }
}