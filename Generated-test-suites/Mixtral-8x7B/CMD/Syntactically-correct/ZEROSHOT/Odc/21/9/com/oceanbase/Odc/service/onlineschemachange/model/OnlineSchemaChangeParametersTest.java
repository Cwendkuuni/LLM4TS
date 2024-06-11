import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

@Before
public void setUp() {
onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
}

@Test
public void testGetSqlType() {
onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
Assert.assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
}

@Test
public void testGetSqlContent() {
onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 int");
Assert.assertEquals("ALTER TABLE table1 ADD column1 int", onlineSchemaChangeParameters.getSqlContent());
}

@Test
public void testGetErrorStrategy() {
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
Assert.assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
}

@Test
public void testIsContinueOnError() {
onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
}

@Test
public void testGetLockTableTimeOutSeconds() {
onlineSchemaChangeParameters.setLockTableTimeOutSeconds(60);
Assert.assertEquals(Integer.valueOf(60), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
}

@Test
public void testGetSwapTableNameRetryTimes() {
onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
Assert.assertEquals(Integer.valueOf(3), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
}

@Test
public void testGetOriginTableCleanStrategy() {
onlineSchemaChangeParameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.AFTER_DDL);
Assert.assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.AFTER_DDL, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
}

@Test
public void testGetDelimiter() {
onlineSchemaChangeParameters.setDelimiter(";");
Assert.assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
}

@Test
public void testGetLockUsers() {
onlineSchemaChangeParameters.setLockUsers(new ArrayList<String>() {{
add("user1");
add("user2");
}});
Assert.assertEquals(2, onlineSchemaChangeParameters.getLockUsers().size());
Assert.assertEquals("user1", onlineSchemaChangeParameters.getLockUsers().get(0));
Assert.assertEquals("user2", onlineSchemaChangeParameters.getLockUsers().get(1));
}

@Test
public void testGetSwapTableType() {
onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.SWAP_TABLE);
Assert.assertEquals(OnlineSchemaChangeParameters.SwapTableType.SWAP_TABLE, onlineSchemaChangeParameters.getSwapTableType());
}

@Test
public void testGenerateSubTaskParameters() throws UnexpectedException {
ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
String schema = "test_schema";
List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
Mockito.verify(connectionConfig, Mockito.times(1)).getDialectType();
}

}