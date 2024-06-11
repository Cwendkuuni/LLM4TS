import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.Serializable;
import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 @Before
 public void setup() {
 onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 INT");
 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
 onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
 onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
 onlineSchemaChangeParameters.setDelimiter(";");
 onlineSchemaChangeParameters.setLockUsers(Arrays.asList("user1", "user2"));
 onlineSchemaChangeParameters.setSwapTableType(SwapTableType.LOCAL);
 }

 @Test
 public void testIsContinueOnError() {
 Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParametersWithValidParameters() throws Exception {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
 Assert.assertNotNull(result);
 Assert.assertTrue(!result.isEmpty());
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersWithNullConnectionConfig() throws Exception {
 onlineSchemaChangeParameters.generateSubTaskParameters(null, "schema");
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersWithNullSchema() throws Exception {
 onlineSchemaChangeParameters.generateSubTaskParameters(new ConnectionConfig(), null);
 }

 @Test(expected = SQLSyntaxErrorException.class)
 public void testGenerateSubTaskParametersWithInvalidSqlContent() throws Exception {
 onlineSchemaChangeParameters.setSqlContent("INVALID SQL");
 onlineSchemaChangeParameters.generateSubTaskParameters(new ConnectionConfig(), "schema");
 }

 @Test
 public void testGenerateSubTaskParametersWithEmptySqlContent() throws Exception {
 onlineSchemaChangeParameters.setSqlContent("");
 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(new ConnectionConfig(), "schema");
 Assert.assertTrue(result.isEmpty());
 }

 @Test
 public void testGenerateSubTaskParametersWithEmptySqls() throws Exception {
 onlineSchemaChangeParameters.setSqlContent(";");
 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(new ConnectionConfig(), "schema");
 Assert.assertTrue(result.isEmpty());
 }
}