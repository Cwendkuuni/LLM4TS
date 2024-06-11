import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

@Test
public void testGenerateSubTaskParameters() throws UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType("mysql");
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlContent("CREATE TABLE test (id INT); ALTER TABLE test ADD COLUMN name VARCHAR(255)");
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test");
Assert.assertEquals(2, subTaskParameters.size());
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
Assert.assertTrue(parameters.isContinueOnError());
}

@Test
public void testGetLockTableTimeOutSeconds() {
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setLockTableTimeOutSeconds(10);
Assert.assertEquals(10, parameters.getLockTableTimeOutSeconds().intValue());
}

@Test
public void testGetSwapTableNameRetryTimes() {
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSwapTableNameRetryTimes(5);
Assert.assertEquals(5, parameters.getSwapTableNameRetryTimes().intValue());
}

@Test
public void testGetOriginTableCleanStrategy() {
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.BACKUP);
Assert.assertEquals(OriginTableCleanStrategy.BACKUP, parameters.getOriginTableCleanStrategy());
}

@Test
public void testGetDelimiter() {
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setDelimiter(";");
Assert.assertEquals(";", parameters.getDelimiter());
}

@Test
public void testGetLockUsers() {
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
List<String> lockUsers = new ArrayList<>();
lockUsers.add("user1");
lockUsers.add("user2");
parameters.setLockUsers(lockUsers);
Assert.assertEquals(lockUsers, parameters.getLockUsers());
}

@Test
public void testGetSwapTableType() {
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSwapTableType(SwapTableType.TEMPORARY);
Assert.assertEquals(SwapTableType.TEMPORARY, parameters.getSwapTableType());
}
}