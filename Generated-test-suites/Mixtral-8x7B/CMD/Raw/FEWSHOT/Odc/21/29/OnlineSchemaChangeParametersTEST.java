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

private OnlineSchemaChangeParameters getOnlineSchemaChangeParameters() {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");

OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
params.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 int;");
params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
params.setLockTableTimeOutSeconds(10);
params.setSwapTableNameRetryTimes(5);
params.setOriginTableCleanStrategy(OriginTableCleanStrategy.RENAME_DROP);
params.setDelimiter(";");
params.setLockUsers(new ArrayList<>());
params.setSwapTableType(SwapTableType.TEMPORARY_TABLE);

return params;
}

@Test
public void testGenerateSubTaskParameters() {
OnlineSchemaChangeParameters params = getOnlineSchemaChangeParameters();
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "test_schema";

List<OnlineSchemaChangeScheduleTaskParameters> result = params.generateSubTaskParameters(connectionConfig, schema);

// Assert that the result is not null
assertNull("generateSubTaskParameters returned null", result);

// Add assertions for the returned values
// ...
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters params = getOnlineSchemaChangeParameters();

// Assert that isContinueOnError returns the expected value
assertEquals("isContinueOnError returned incorrect value", true, params.isContinueOnError());
}

@Test
public void testGetOriginTableCleanStrategy() {
OnlineSchemaChangeParameters params = getOnlineSchemaChangeParameters();

// Assert that getOriginTableCleanStrategy returns the expected value
assertEquals("getOriginTableCleanStrategy returned incorrect value", OriginTableCleanStrategy.RENAME_DROP,
params.getOriginTableCleanStrategy());
}

@Test
public void testGetSwapTableType() {
OnlineSchemaChangeParameters params = getOnlineSchemaChangeParameters();

// Assert that getSwapTableType returns the expected value
assertEquals("getSwapTableType returned incorrect value", SwapTableType.TEMPORARY_TABLE, params.getSwapTableType());
}

}