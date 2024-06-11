import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE users ADD COLUMN age INT;");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.BACKUP_AND_DELETE);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(Arrays.asList("user1", "user2"));
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.TEMPORARY);
    }

    @Test
    public void testIsContinueOnError() {
        Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParametersWithDifferentSqlTypes() {
        OnlineSchemaChangeParameters parameters1 = new OnlineSchemaChangeParameters();
        parameters1.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters1.setSqlContent("ALTER TABLE users ADD COLUMN age INT;");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters1 = parameters1.generateSubTaskParameters(
                new ConnectionConfig(), "schema");

        OnlineSchemaChangeParameters parameters2 = new OnlineSchemaChangeParameters();
        parameters2.setSqlType(OnlineSchemaChangeSqlType.DROP_INDEX);
        parameters2.setSqlContent("DROP INDEX idx_users_status;");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters2 = parameters2.generateSubTaskParameters(
                new ConnectionConfig(), "schema");

        Assert.assertNotNull(subTaskParameters1);
        Assert.assertNotNull(subTaskParameters2);
    }

    @Test
    public void testGenerateSubTaskParametersWithException() {
        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(null, "schema");
            Assert.fail("UnexpectedException should be thrown.");
        } catch (UnexpectedException e) {
            Assert.assertEquals("Failed to generate subtasks with parameter: " + onlineSchemaChangeParameters,
                    e.getMessage());
        }
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptySqlContent() {
        onlineSchemaChangeParameters.setSqlContent("");
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(new ConnectionConfig(), "schema");
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testGenerateSubTaskParametersWithNullLockUsers() {
        onlineSchemaChangeParameters.setLockUsers(null);
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(new ConnectionConfig(), "schema");
        Assert.assertNotNull(result);
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptyLockUsers() {
        onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(new ConnectionConfig(), "schema");
        Assert.assertNotNull(result);
    }

    @Test
    public void testGenerateSubTaskParametersWithNullDelimiter() {
        onlineSchemaChangeParameters.setDelimiter(null);
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(new ConnectionConfig(), "schema");
        Assert.assertNotNull(result);
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptyDelimiter() {
        onlineSchemaChangeParameters.setDelimiter("");
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(new ConnectionConfig(), "schema");
        Assert.assertNotNull(result);
    }

    @Test
    public void testGenerateSubTaskParameters() {
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE users ADD COLUMN age INT; ALTER TABLE users ADD COLUMN "
                + "gender VARCHAR(10);");

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(new ConnectionConfig(), "schema");

        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testGenerateSubTaskParametersWithSwapTableTypeTemporary() {
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.TEMPORARY);
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(new ConnectionConfig(), "schema");

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = result.get(0);
        Assert.assertEquals("temp_users", taskParameter.getNewTableName());
    }

    @Test
    public void testGenerateSubTaskParametersWithSwapTableTypePermanent() {
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.PERMANENT);
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(new ConnectionConfig(), "schema");

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = result.get(0);
        Assert.assertEquals("users_new", taskParameter.getNewTableName());
    }
}