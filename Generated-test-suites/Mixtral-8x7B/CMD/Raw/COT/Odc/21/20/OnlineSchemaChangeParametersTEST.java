import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

@Before
public void setUp() {
String sqlContent = "ALTER TABLE table1 ADD COLUMN newColumn INT;";
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
onlineSchemaChangeParameters.setSqlContent(sqlContent);
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
}

@Test
public void testIsContinueOnError() {
Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParametersWithAlterSql() throws UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
Assert.assertEquals(1, subTaskParameters.size());
OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
Assert.assertEquals("testSchema", subTaskParameter.getDatabaseName());
Assert.assertEquals("table1", subTaskParameter.getOriginTableName());
Assert.assertEquals(1, subTaskParameter.getSqlsToBeExecuted().size());
String alteredStmt = DdlUtils.replaceTableName(onlineSchemaChangeParameters.getSqlContent(), subTaskParameter.getNewTableName(),
connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER);
Assert.assertEquals(alteredStmt, subTaskParameter.getSqlsToBeExecuted().get(0));
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParametersWithException() throws UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
onlineSchemaChangeParameters.setSqlContent(null);
onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
}

@Test
public void testGenerateSubTaskParametersWithMultipleSqls() throws UnexpectedException {
ConnectionConfig connectionConfig = new ConnectionConfig();
String schema = "testSchema";
String sqlContent = "ALTER TABLE table1 ADD COLUMN newColumn INT; ALTER TABLE table2 ADD COLUMN newColumn INT;";
onlineSchemaChangeParameters.setSqlContent(sqlContent);
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
Assert.assertEquals(2, subTaskParameters.size());
}

@Test
public void testOriginTableCleanStrategy() {
onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
Assert.assertEquals("KEEP", onlineSchemaChangeParameters.getOriginTableCleanStrategy().getValue());
}

@Test
public void testSwapTableType() {
onlineSchemaChangeParameters.setSwapTableType(SwapTableType.CLONE_AND_SWAP);
Assert.assertEquals("CLONE_AND_SWAP", onlineSchemaChangeParameters.getSwapTableType().getValue());
}

@Test
public void testLockTableTimeOutSeconds() {
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(300);
Assert.assertEquals(300, onlineSchemaChangeParameters.getLockTableTimeOutSeconds().intValue());
}

@Test
public void testSwapTableNameRetryTimes() {
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
Assert.assertEquals(5, onlineSchemaChangeParameters.getSwapTableNameRetryTimes().intValue());
}

@Test
public void testDelimiter() {
onlineSchemaChangeParameters.setDelimiter("//");
Assert.assertEquals("//", onlineSchemaChangeParameters.getDelimiter());
}

@Test
public void testLockUsers() {
List<String> lockUsers = new ArrayList<>();
lockUsers.add("user1");
lockUsers.add("user2");
onlineSchemaChangeParameters.setLockUsers(lockUsers);
Assert.assertEquals(2, onlineSchemaChangeParameters.getLockUsers().size());
}
}