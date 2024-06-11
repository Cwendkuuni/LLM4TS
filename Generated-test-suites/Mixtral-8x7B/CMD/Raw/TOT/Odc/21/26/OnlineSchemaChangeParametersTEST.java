import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

 @Mock
 private ConnectionConfig connectionConfig;

 @Mock
 private SubTaskParameterFactory subTaskParameterFactory;

 @InjectMocks
 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGettersAndSetters() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("ALTER TABLE table_name ADD column_name datatype");
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(30);
 params.setSwapTableNameRetryTimes(5);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP_ORIGINAL);
 params.setDelimiter(";");
 params.setLockUsers(new ArrayList<>());
 params.setSwapTableType(SwapTableType.SHARED_LOCK);

 assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
 assertEquals("ALTER TABLE table_name ADD column_name datatype", params.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
 assertEquals(Integer.valueOf(30), params.getLockTableTimeOutSeconds());
 assertEquals(Integer.valueOf(5), params.getSwapTableNameRetryTimes());
 assertEquals(OriginTableCleanStrategy.KEEP_ORIGINAL, params.getOriginTableCleanStrategy());
 assertEquals(";", params.getDelimiter());
 assertEquals(new ArrayList<>(), params.getLockUsers());
 assertEquals(SwapTableType.SHARED_LOCK, params.getSwapTableType());
 }

 @Test
 public void testIsContinueOnError() {
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(onlineSchemaChangeParameters.isContinueOnError());

 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
 assertFalse(onlineSchemaChangeParameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() throws Exception {
 Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters =
 Mockito.mock(Map.class);
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
 new ArrayList<>(taskParameters.values());

 String sql = "ALTER TABLE table_name ADD column_name datatype";
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;

 Mockito.when(connectionConfig.getDialectType()).thenReturn("MySQL");
 Mockito.when(subTaskParameterFactory.generate(sql, sqlType))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 Mockito.when(taskParameters.values()).thenReturn(subTaskParameters);

 List<OnlineSchemaChangeScheduleTaskParameters> result =
 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");

 assertEquals(subTaskParameters, result);
 Mockito.verify(subTaskParameterFactory, Mockito.times(1)).generate(sql, sqlType);
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersException() throws Exception {
 String sql = "ALTER TABLE table_name ADD column_name datatype";
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;

 Mockito.when(connectionConfig.getDialectType()).thenReturn("MySQL");
 Mockito.when(subTaskParameterFactory.generate(sql, sqlType))
 .thenThrow(new Exception());

 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
 }
}