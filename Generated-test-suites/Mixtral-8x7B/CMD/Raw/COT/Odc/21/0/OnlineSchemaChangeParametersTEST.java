import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.Serializable;
import java.sql.SQLException;
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
    }

    @Test
    public void testSqlType() {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        onlineSchemaChangeParameters.setSqlType(sqlType);
        Assert.assertEquals(sqlType, onlineSchemaChangeParameters.getSqlType());
    }

    @Test
    public void testSqlContent() {
        String sqlContent = "ALTER TABLE tab1 ADD column1 int";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        Assert.assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());
    }

    @Test
    public void testErrorStrategy() {
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
        Assert.assertEquals(errorStrategy, onlineSchemaChangeParameters.getErrorStrategy());
    }

    @Test
    public void testLockTableTimeOutSeconds() {
        Integer lockTableTimeOutSeconds = 500;
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        Assert.assertEquals(lockTableTimeOutSeconds, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testSwapTableNameRetryTimes() {
        Integer swapTableNameRetryTimes = 5;
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        Assert.assertEquals(swapTableNameRetryTimes, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
    }

    @Test
    public void testOriginTableCleanStrategy() {
        OnlineSchemaChangeParameters.OriginTableCleanStrategy originTableCleanStrategy =
                OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP;
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        Assert.assertEquals(originTableCleanStrategy, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testDelimiter() {
        String delimiter = ",";
        onlineSchemaChangeParameters.setDelimiter(delimiter);
        Assert.assertEquals(delimiter, onlineSchemaChangeParameters.getDelimiter());
    }

    @Test
    public void testLockUsers() {
        String lockUser1 = "user1";
        String lockUser2 = "user2";
        List<String> lockUsers = Arrays.asList(lockUser1, lockUser2);
        onlineSchemaChangeParameters.setLockUsers(lockUsers);
        Assert.assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
    }

    @Test
    public void testSwapTableType() {
        OnlineSchemaChangeParameters.SwapTableType swapTableType =
                OnlineSchemaChangeParameters.SwapTableType.SWAP_TABLE_NAMES;
        onlineSchemaChangeParameters.setSwapTableType(swapTableType);
        Assert.assertEquals(swapTableType, onlineSchemaChangeParameters.getSwapTableType());
    }

    @Test
    public void testGenerateSubTaskParameters() throws SQLException, UnexpectedException {
        String schema = "schema1";
        String sqlContent = "ALTER TABLE tab1 ADD column1 int; ALTER TABLE tab2 ADD column2 int";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.SWAP_TABLE_NAMES);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
                onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

        Assert.assertEquals(2, subTaskParameters.size());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_Exception() throws SQLException, UnexpectedException {
        String schema = "schema1";
        String sqlContent = "";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.SWAP_TABLE_NAMES);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
    }
}
//