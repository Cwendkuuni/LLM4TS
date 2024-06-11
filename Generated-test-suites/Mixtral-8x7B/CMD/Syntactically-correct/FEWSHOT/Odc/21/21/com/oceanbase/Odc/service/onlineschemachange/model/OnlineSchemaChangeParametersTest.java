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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.SwapTableType;

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
assertEquals(TaskErrorStrategy.ERROR, TaskErrorStrategy.valueOf("ERROR"));
assertEquals(TaskErrorStrategy.IGNORE, TaskErrorStrategy.valueOf("IGNORE"));
}

@Test
public void testOriginTableCleanStrategy() {
assertEquals(OriginTableCleanStrategy.DELETE, OriginTableCleanStrategy.valueOf("DELETE"));
assertEquals(OriginTableCleanStrategy.TRUNCATE, OriginTableCleanStrategy.valueOf("TRUNCATE"));
}

@Test
public void testSwapTableType() {
assertEquals(SwapTableType.RENAME, SwapTableType.valueOf("RENAME"));
assertEquals(SwapTableType.TEMPORARY_TABLE, SwapTableType.valueOf("TEMPORARY_TABLE"));
}

@Test
public void testGenerateSubTaskParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";
OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
params.setSqlContent("ALTER TABLE table1 ADD column1 varchar(255)");
params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
params.setLockTableTimeOutSeconds(10);
params.setSwapTableNameRetryTimes(3);
params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE);
params.setDelimiter(";");
List<String> lockUsers = new ArrayList<>();
lockUsers.add("user1");
lockUsers.add("user2");
params.setLockUsers(lockUsers);
params.setSwapTableType(SwapTableType.RENAME);

List<OnlineSchemaChangeScheduleTaskParameters> result = params.generateSubTaskParameters(connectionConfig, schema);

// Add assertions based on the expected output for your test case
}

}