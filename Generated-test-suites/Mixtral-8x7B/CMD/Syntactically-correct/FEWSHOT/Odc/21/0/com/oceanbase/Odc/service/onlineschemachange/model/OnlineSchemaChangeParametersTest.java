import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

@Test
public void testOnlineSchemaChangeSqlType() {
assertEquals(OnlineSchemaChangeSqlType.ALTER, OnlineSchemaChangeSqlType.valueOf("ALTER"));
assertEquals(OnlineSchemaChangeSqlType.CREATE, OnlineSchemaChangeSqlType.valueOf("CREATE"));
assertEquals(OnlineSchemaChangeSqlType.DROP, OnlineSchemaChangeSqlType.valueOf("DROP"));
}

@Test
public void testTaskErrorStrategy() {
assertEquals(TaskErrorStrategy.CONTINUE, TaskErrorStrategy.valueOf("CONTINUE"));
assertEquals(TaskErrorStrategy.ROLLBACK, TaskErrorStrategy.valueOf("ROLLBACK"));
assertEquals(TaskErrorStrategy.STOP, TaskErrorStrategy.valueOf("STOP"));
}

@Test
public void testOriginTableCleanStrategy() {
// Add test cases for the enum if necessary
}

@Test
public void testSwapTableType() {
// Add test cases for the enum if necessary
}

@Test
public void testOnlineSchemaChangeParameters() {
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
assertEquals(";", params.getDelimiter());
assertNull(params.getLockUsers());
assertEquals(TaskErrorStrategy.ROLLBACK, params.getErrorStrategy());
assertEquals(0, params.getLockTableTimeOutSeconds().intValue());
assertEquals(0, params.getSwapTableNameRetryTimes().intValue());
assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, params.getOriginTableCleanStrategy());
}

@Test
public void testGenerateSubTaskParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
params.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT; ALTER TABLE table1 ADD COLUMN c2 INT");
params.setSqlType(OnlineSchemaChangeSqlType.ALTER);

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig,
schema);

// Add assertions based on the expected output
}

}