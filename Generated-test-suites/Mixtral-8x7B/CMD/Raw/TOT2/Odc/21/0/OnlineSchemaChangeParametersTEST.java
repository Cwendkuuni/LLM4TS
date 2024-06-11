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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

 @Mock
 private ConnectionConfig connectionConfig;

 @Mock
 private SubTaskParameterFactory subTaskParameterFactory;

 @InjectMocks
 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 // Test for constructor and getter/setter methods
 @Test
 public void testOnlineSchemaChangeParameters() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 assertNull(params.getSqlType());
 assertNull(params.getSqlContent());
 assertEquals(TaskErrorStrategy.TERMINATE, params.getErrorStrategy());
 assertEquals(0, params.getLockTableTimeOutSeconds().intValue());
 assertEquals(0, params.getSwapTableNameRetryTimes().intValue());
 assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP,
 params.getOriginTableCleanStrategy());
 assertEquals(";", params.getDelimiter());
 assertNull(params.getLockUsers());
 assertEquals(OnlineSchemaChangeParameters.SwapTableType.SWAP_TABLE,
 params.getSwapTableType());
 }

 // Test for isContinueOnError method
 @Test
 public void testIsContinueOnError() {
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(onlineSchemaChangeParameters.isContinueOnError());

 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
 assertFalse(onlineSchemaChangeParameters.isContinueOnError());
 }

 // Test for generateSubTaskParameters method with normal use case
 @Test
 public void testGenerateSubTaskParametersNormalUseCase() throws UnexpectedException {
 List<String> sqls = new ArrayList<>();
 sqls.add("ALTER TABLE table1 ADD COLUMN col1 INT");
 when(subTaskParameterFactory.generate(anyString(), any())).thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, "schema");

 assertEquals(1, result.size());
 }

 // Test for generateSubTaskParameters method with Exception scenario
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersExceptionScenario() throws UnexpectedException {
 List<String> sqls = new ArrayList<>();
 sqls.add("ALTER TABLE table1 ADD COLUMN col1 INT");
 when(subTaskParameterFactory.generate(anyString(), any())).thenThrow(new RuntimeException());

 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
 }

 // Edge case: Test for generateSubTaskParameters method with empty sqlContent
 @Test
 public void testGenerateSubTaskParametersEmptySqlContent() {
 when(subTaskParameterFactory.generate(anyString(), any())).thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, "schema");

 assertEquals(0, result.size());
 }

 // Edge case: Test for generateSubTaskParameters method with null sqlContent
 @Test
 public void testGenerateSubTaskParametersNullSqlContent() {
 when(subTaskParameterFactory.generate(anyString(), any())).thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 onlineSchemaChangeParameters.setSqlContent(null);
 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, "schema");

 assertEquals(0, result.size());
 }

 // Edge case: Test for generateSubTaskParameters method with empty sqls list
 @Test
 public void testGenerateSubTaskParametersEmptySqlsList() throws UnexpectedException {
 List<String> sqls = new ArrayList<>();
 when(subTaskParameterFactory.generate(anyString(), any())).thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 when(SqlUtils.split(any(), anyString(), anyString())).thenReturn(sqls);

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, "schema");

 assertEquals(0, result.size());
 }

 // Edge case: Test for generateSubTaskParameters method with null sqls list
 @Test
 public void testGenerateSubTaskParametersNullSqlsList() throws UnexpectedException {
 List<String> sqls = null;
 when(subTaskParameterFactory.generate(anyString(), any())).thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

 when(SqlUtils.split(any(), anyString(), anyString())).thenReturn(sqls);

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, "schema");

 assertEquals(0, result.size());
 }
}