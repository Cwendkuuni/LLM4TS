import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

 @Mock
 private ConnectionConfig connectionConfig;

 @Mock
 private SubTaskParameterFactory subTaskParameterFactory;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGettersAndSetters() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("ALTER TABLE table_name ADD column_name datatype");
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(60);
 params.setSwapTableNameRetryTimes(3);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
 params.setDelimiter(";");
 params.setLockUsers(new ArrayList<String>() {{
 add("user1");
 add("user2");
 }});
 params.setSwapTableType(SwapTableType.WITH_TEMP_TABLE);

 assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
 assertEquals("ALTER TABLE table_name ADD column_name datatype", params.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
 assertEquals(Integer.valueOf(60), params.getLockTableTimeOutSeconds());
 assertEquals(Integer.valueOf(3), params.getSwapTableNameRetryTimes());
 assertEquals(OriginTableCleanStrategy.DROP, params.getOriginTableCleanStrategy());
 assertEquals(";", params.getDelimiter());
 assertEquals(new ArrayList<String>() {{
 add("user1");
 add("user2");
 }}, params.getLockUsers());
 assertEquals(SwapTableType.WITH_TEMP_TABLE, params.getSwapTableType());
 }

 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);

 assertTrue(params.isContinueOnError());

 params.setErrorStrategy(TaskErrorStrategy.ROLLBACK);

 assertFalse(params.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters_ALTER() throws UnexpectedException {
 String sql = "ALTER TABLE table_name ADD column_name datatype";

 when(subTaskParameterFactory.generate(sql, OnlineSchemaChangeSqlType.ALTER))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 when(connectionConfig.getDialectType())
 .thenReturn(SqlUtils.DialectType.MYSQL);

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent(sql);

 List<OnlineSchemaChangeScheduleTaskParameters> result = params
 .generateSubTaskParameters(connectionConfig, "schema");

 verify(subTaskParameterFactory, times(1)).generate(sql, OnlineSchemaChangeSqlType.ALTER);

 }

 @Test
 public void testGenerateSubTaskParameters_CREATE() throws UnexpectedException {
 String sql = "CREATE TABLE new_table_name (id INT PRIMARY KEY, name VARCHAR(50))";

 when(subTaskParameterFactory.generate(sql, OnlineSchemaChangeSqlType.CREATE))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 when(connectionConfig.getDialectType())
 .thenReturn(SqlUtils.DialectType.MYSQL);

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 params.setSqlContent(sql);

 List<OnlineSchemaChangeScheduleTaskParameters> result = params
 .generateSubTaskParameters(connectionConfig, "schema");

 verify(subTaskParameterFactory, times(1)).generate(sql, OnlineSchemaChangeSqlType.CREATE);

 }

 @Test
 public void testGenerateSubTaskParameters_DROP() throws UnexpectedException {
 String sql = "DROP TABLE table_name";

 when(subTaskParameterFactory.generate(sql, OnlineSchemaChangeSqlType.DROP))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 when(connectionConfig.getDialectType())
 .thenReturn(SqlUtils.DialectType.MYSQL);

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.DROP);
 params.setSqlContent(sql);

 List<OnlineSchemaChangeScheduleTaskParameters> result = params
 .generateSubTaskParameters(connectionConfig, "schema");

 verify(subTaskParameterFactory, times(1)).generate(sql, OnlineSchemaChangeSqlType.DROP);

 }

 @Test
 public void testGenerateSubTaskParameters_Exception() throws UnexpectedException {
 String sql = "INVALID SQL";

 when(subTaskParameterFactory.generate(sql, OnlineSchemaChangeSqlType.ALTER))
 .thenThrow(new UnexpectedException("Failed to generate subtask with SQL: " + sql));

 when(connectionConfig.getDialectType())
 .thenReturn(SqlUtils.DialectType.MYSQL);

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent(sql);

 try {
 params.generateSubTaskParameters(connectionConfig, "schema");
 } catch (UnexpectedException e) {
 assertEquals("Failed to generate subtask with SQL: INVALID SQL", e.getMessage());
 }

 }

}