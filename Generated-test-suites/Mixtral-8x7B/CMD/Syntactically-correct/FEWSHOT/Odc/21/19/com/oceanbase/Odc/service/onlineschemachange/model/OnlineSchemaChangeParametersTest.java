import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

@Test
public void testGenerateSubTaskParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 varchar(255); ALTER TABLE table2 DROP column2;");
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, schema);
assertEquals(2, subTaskParameters.size());
assertEquals(schema, subTaskParameters.get(0).getDatabaseName());
assertEquals("table1", subTaskParameters.get(0).getOriginTableName());
assertEquals("column1", subTaskParameters.get(0).getColumnName());
assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameters.get(0).getSqlType());
assertEquals(schema, subTaskParameters.get(1).getDatabaseName());
assertEquals("table2", subTaskParameters.get(1).getOriginTableName());
assertEquals("column2", subTaskParameters.get(1).getColumnName());
assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameters.get(1).getSqlType());
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
assertTrue(onlineSchemaChangeParameters.isContinueOnError());
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
assertFalse(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParametersWithNullSqlContent() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent(null);
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, schema);
assertEquals(0, subTaskParameters.size());
}

@Test
public void testGenerateSubTaskParametersWithEmptySqlContent() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent("");
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, schema);
assertEquals(0, subTaskParameters.size());
}

@Test
public void testGenerateSubTaskParametersWithNullSqlType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 varchar(255);");
onlineSchemaChangeParameters.setSqlType(null);
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, schema);
assertEquals(0, subTaskParameters.size());
}

@Test
public void testGenerateSubTaskParametersWithEmptySqlType() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 varchar(255);");
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.values()[0]);
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, schema);
assertEquals(0, subTaskParameters.size());
}

@Test
public void testGenerateSubTaskParametersWithNullConnectionConfig() {
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 varchar(255);");
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
assertThrows(NullPointerException.class, () -> onlineSchemaChangeParameters
.generateSubTaskParameters(null, "test_schema"));
}

@Test
public void testGenerateSubTaskParametersWithNullSchema() {
ConnectionConfig connectionConfig = new ConnectionConfig();
OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 varchar(255);");
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
assertThrows(NullPointerException.class, () -> onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, null));
}

}