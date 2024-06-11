import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        Assert.assertNull(params.getSqlContent());
        Assert.assertEquals(TaskErrorStrategy.STOP, params.getErrorStrategy());
        Assert.assertNull(params.getLockTableTimeOutSeconds());
        Assert.assertNull(params.getSwapTableNameRetryTimes());
        Assert.assertNull(params.getOriginTableCleanStrategy());
        Assert.assertEquals(";", params.getDelimiter());
        Assert.assertNull(params.getLockUsers());
        Assert.assertEquals(SwapTableType.NORMAL, params.getSwapTableType());
    }

    @Test
    public void testOnlineSchemaChangeParametersWithValues() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("SELECT * FROM users;");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(10);
        params.setSwapTableNameRetryTimes(5);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        params.setDelimiter(";");
        params.setLockUsers(new ArrayList<>());
        params.setSwapTableType(SwapTableType.SHARDING);

        Assert.assertEquals("SELECT * FROM users;", params.getSqlContent());
        Assert.assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
        Assert.assertEquals(Integer.valueOf(10), params.getLockTableTimeOutSeconds());
        Assert.assertEquals(Integer.valueOf(5), params.getSwapTableNameRetryTimes());
        Assert.assertEquals(OriginTableCleanStrategy.DROP, params.getOriginTableCleanStrategy());
        Assert.assertEquals(";", params.getDelimiter());
        Assert.assertNotNull(params.getLockUsers());
        Assert.assertEquals(SwapTableType.SHARDING, params.getSwapTableType());
    }

    @Test
    public void testGenerateSubTaskParametersHappyPath() {
        String connectionConfigJson = "{\"databaseType\": \"MYSQL\", \"serverVersion\": \"8.0.28\", "
                + "\"connectionUrl\": \"jdbc:mysql://127.0.0.1:3306/test?useSSL=false\", "
                + "\"username\": \"root\", \"password\": \"password\", \"dialectType\": \"MYSQL\", "
                + "\"maxActive\": 1, \"maxIdle\": 1, \"initialSize\": 1, \"minIdle\": 1, \"maxWait\": 10000}";
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.parse(connectionConfigJson);

        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE users ADD COLUMN age INT; ALTER TABLE users ADD COLUMN email VARCHAR(50);");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig,
                "test");

        Assert.assertFalse(subTaskParameters.isEmpty());
        Assert.assertEquals(2, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters firstParam = subTaskParameters.get(0);
        Assert.assertEquals("test", firstParam.getDatabaseName());
        Assert.assertEquals("users", firstParam.getOriginTableName());
        Assert.assertEquals("age", firstParam.getNewColumnName());
        Assert.assertEquals("INT", firstParam.getNewColumnType());
        Assert.assertFalse(firstParam.getSqlsToBeExecuted().isEmpty());
        Assert.assertEquals(1, firstParam.getSqlsToBeExecuted().size());
        Assert.assertEquals("ALTER TABLE users ADD COLUMN age INT",
                firstParam.getSqlsToBeExecuted().get(0));

        OnlineSchemaChangeScheduleTaskParameters secondParam = subTaskParameters.get(1);
        Assert.assertEquals("test", secondParam.getDatabaseName());
        Assert.assertEquals("users", secondParam.getOriginTableName());
        Assert.assertEquals("email", secondParam.getNewColumnName());
        Assert.assertEquals("VARCHAR(50)", secondParam.getNewColumnType());
        Assert.assertFalse(secondParam.getSqlsToBeExecuted().isEmpty());
        Assert.assertEquals(1, secondParam.getSqlsToBeExecuted().size());
        Assert.assertEquals("ALTER TABLE users ADD COLUMN email VARCHAR(50)",
                secondParam.getSqlsToBeExecuted().get(0));
    }

    @Test(expected = UnexpectedException.class)
    @SneakyThrows
    public void testGenerateSubTaskParametersException() {
        String connectionConfigJson = "{\"databaseType\": \"MYSQL\", \"serverVersion\": \"8.0.28\", "
                + "\"connectionUrl\": \"jdbc:mysql://127.0.0.1:3306/test?useSSL=false\", "
                + "\"username\": \"root\", \"password\": \"password\", \"dialectType\": \"MYSQL\", "
                + "\"maxActive\": 1, \"maxIdle\": 1, \"initialSize\": 1, \"minIdle\": 1, \"maxWait\": 10000}";
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.parse(connectionConfigJson);

        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("INVALID SQL");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        params.generateSubTaskParameters(connectionConfig, "test");
    }

}