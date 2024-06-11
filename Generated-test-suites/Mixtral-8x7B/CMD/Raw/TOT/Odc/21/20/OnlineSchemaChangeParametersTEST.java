import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(PowerMockRunner.class)
public class OnlineSchemaChangeParametersTest {

 @InjectMocks
 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testGetSqlType() {
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
 onlineSchemaChangeParameters.setSqlType(sqlType);
 assertEquals(onlineSchemaChangeParameters.getSqlType(), sqlType);
 }

 @Test
 public void testGenerateSubTaskParameters() {
 String schema = "test_schema";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
 .generateSubTaskParameters(connectionConfig, schema);
 assertNotNull(subTaskParameters);
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersException() throws Exception {
 String schema = "test_schema";
 ConnectionConfig connectionConfig = new ConnectionConfig();
 SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, schema) {
 @Override
 protected OnlineSchemaChangeScheduleTaskParameters generate(String sql, OnlineSchemaChangeSqlType sqlType)
 throws Exception {
 throw new Exception("Exception for testing");
 }
 };
 subTaskParameterFactory.generate(null, null);
 }

 @Test
 public void testIsContinueOnError() {
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(onlineSchemaChangeParameters.isContinueOnError());
 }
}