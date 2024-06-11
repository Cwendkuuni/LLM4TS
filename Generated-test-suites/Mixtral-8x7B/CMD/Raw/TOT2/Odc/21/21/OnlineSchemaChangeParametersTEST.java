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
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 // Test for getters and setters
 @Test
 public void testGetterSetter() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("ALTER TABLE table_name ADD column_name datatype");
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(300);
 params.setSwapTableNameRetryTimes(3);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.BACKUP_AND_RENAME);
 params.setDelimiter(";");
 params.setLockUsers(new ArrayList<>());
 params.setSwapTableType(SwapTableType.SAME_TABLE);

 assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
 assertEquals("ALTER TABLE table_name ADD column_name datatype", params.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
 assertEquals(Integer.valueOf(300), params.getLockTableTimeOutSeconds());
 assertEquals(Integer.valueOf(3), params.getSwapTableNameRetryTimes());
 assertEquals(OriginTableCleanStrategy.BACKUP_AND_RENAME, params.getOriginTableCleanStrategy());
 assertEquals(";", params.getDelimiter());
 assertEquals(new ArrayList<>(), params.getLockUsers());
 assertEquals(SwapTableType.SAME_TABLE, params.getSwapTableType());
 }

 // Test for isContinueOnError()
 @Test
 public void testIsContinueOnError() {
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(onlineSchemaChangeParameters.isContinueOnError());

 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
 assertFalse(onlineSchemaChangeParameters.isContinueOnError());
 }

 // Test for generateSubTaskParameters() with valid parameters
 @Test
 public void testGenerateSubTaskParametersValid() throws Exception {
 String sqlContent = "ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 varchar(255);";
 List<OnlineSchemaChangeScheduleTaskParameters> result = new ArrayList<>();
 result.add(new OnlineSchemaChangeScheduleTaskParameters());
 result.add(new OnlineSchemaChangeScheduleTaskParameters());

 when(connectionConfig.getDialectType()).thenReturn("mysql");
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters())
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent(sqlContent);
 params.setConnectionConfig(connectionConfig);

 assertEquals(result, params.generateSubTaskParameters(connectionConfig, "schema"));
 }

 // Test for generateSubTaskParameters() with invalid parameters
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersInvalid() throws Exception {
 when(connectionConfig.getDialectType()).thenThrow(new RuntimeException("DialectType error"));

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("");
 params.setConnectionConfig(connectionConfig);

 params.generateSubTaskParameters(connectionConfig, "schema");
 }

 // Test for generateSubTaskParameters() with empty sqlContent
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersEmptySqlContent() throws Exception {
 when(connectionConfig.getDialectType()).thenReturn("mysql");

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("");
 params.setConnectionConfig(connectionConfig);

 params.generateSubTaskParameters(connectionConfig, "schema");
 }

 // Test for generateSubTaskParameters() with null sqlContent
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersNullSqlContent() throws Exception {
 when(connectionConfig.getDialectType()).thenReturn("mysql");

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent(null);
 params.setConnectionConfig(connectionConfig);

 params.generateSubTaskParameters(connectionConfig, "schema");
 }

 // Test for generateSubTaskParameters() with null ConnectionConfig
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersNullConnectionConfig() throws Exception {
 when(connectionConfig.getDialectType()).thenReturn("mysql");

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("ALTER TABLE table1 ADD column1 int;");
 params.setConnectionConfig(null);

 params.generateSubTaskParameters(connectionConfig, "schema");
 }

 // Test for generateSubTaskParameters() with empty sql
 @Test
 public void testGenerateSubTaskParametersEmptySql() throws Exception {
 String sqlContent = "ALTER TABLE table1 ADD column1 int;";
 List<OnlineSchemaChangeScheduleTaskParameters> result = new ArrayList<>();
 result.add(new OnlineSchemaChangeScheduleTaskParameters());

 when(connectionConfig.getDialectType()).thenReturn("mysql");
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent(sqlContent);
 params.setConnectionConfig(connectionConfig);

 assertEquals(result, params.generateSubTaskParameters(connectionConfig, "schema"));
 }

 // Test for generateSubTaskParameters() with multiple sql statements
 @Test
 public void testGenerateSubTaskParametersMultipleSql() throws Exception {
 String sqlContent = "ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 varchar(255);";
 List<OnlineSchemaChangeScheduleTaskParameters> result = new ArrayList<>();
 result.add(new OnlineSchemaChangeScheduleTaskParameters());
 result.add(new OnlineSchemaChangeScheduleTaskParameters());

 when(connectionConfig.getDialectType()).thenReturn("mysql");
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters())
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent(sqlContent);
 params.setConnectionConfig(connectionConfig);

 assertEquals(result, params.generateSubTaskParameters(connectionConfig, "schema"));
 }

 // Test for generateSubTaskParameters() with different sqlType
 @Test
 public void testGenerateSubTaskParametersDifferentSqlType() throws Exception {
 String sqlContent = "DROP TABLE table1;";
 List<OnlineSchemaChangeScheduleTaskParameters> result = new ArrayList<>();
 result.add(new OnlineSchemaChangeScheduleTaskParameters());

 when(connectionConfig.getDialectType()).thenReturn("mysql");
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.DROP);
 params.setSqlContent(sqlContent);
 params.setConnectionConfig(connectionConfig);

 assertEquals(result, params.generateSubTaskParameters(connectionConfig, "schema"));
 }

 // Test for generateSubTaskParameters() with different delimiter
 @Test
 public void testGenerateSubTaskParametersDifferentDelimiter() throws Exception {
 String sqlContent = "ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 varchar(255);";
 List<OnlineSchemaChangeScheduleTaskParameters> result = new ArrayList<>();
 result.add(new OnlineSchemaChangeScheduleTaskParameters());
 result.add(new OnlineSchemaChangeScheduleTaskParameters());

 when(connectionConfig.getDialectType()).thenReturn("mysql");
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters())
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent(sqlContent);
 params.setDelimiter("//");
 params.setConnectionConfig(connectionConfig);

 assertEquals(result, params.generateSubTaskParameters(connectionConfig, "schema"));
 }

 // Test for generateSubTaskParameters() with lockUsers
 @Test
 public void testGenerateSubTaskParametersLockUsers() throws Exception {
 String sqlContent = "ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 varchar(255);";
 List<OnlineSchemaChangeScheduleTaskParameters> result = new ArrayList<>();
 result.add(new OnlineSchemaChangeScheduleTaskParameters());
 result.add(new OnlineSchemaChangeScheduleTaskParameters());

 when(connectionConfig.getDialectType()).thenReturn("mysql");
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters())
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent(sqlContent);
 params.setLockUsers(new ArrayList<String>() {{ add("user1"); add("user2"); }});
 params.setConnectionConfig(connectionConfig);

 assertEquals(result, params.generateSubTaskParameters(connectionConfig, "schema"));
 }

}