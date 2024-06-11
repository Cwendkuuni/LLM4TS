import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RunWith(JUnit4.class)
@Slf4j
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateSubTaskParametersWithNormalCase() {
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test (id INT); ALTER TABLE test ADD COLUMN name VARCHAR(255);");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_TABLE);
        onlineSchemaChangeParameters.setDelimiter(";");

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
            assertEquals(2, subTaskParameters.size());
            // check other properties of subTaskParameters
        } catch (UnexpectedException e) {
            fail("generateSubTaskParameters should not throw an UnexpectedException.");
        }
    }

    @Test
    public void testGenerateSubTaskParametersWithErrorStrategyContinue() {
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test (id INT); ALTER TABLE test ADD COLUMN name VARCHAR(255);");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_TABLE);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
            // check other properties of subTaskParameters
        } catch (UnexpectedException e) {
            fail("generateSubTaskParameters should not throw an UnexpectedException with errorStrategy CONTINUE.");
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithErrorStrategyRollback() {
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test (id INT); ALTER TABLE test ADD COLUMN name VARCHAR(255);");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_TABLE);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
    }

    @Test
    public void testGenerateSubTaskParametersWithSwapTableType() {
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test (id INT); ALTER TABLE test ADD COLUMN name VARCHAR(255);");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_TABLE);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP_TABLE);

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
            // check other properties of subTaskParameters
        } catch (UnexpectedException e) {
            fail("generateSubTaskParameters should not throw an UnexpectedException.");
        }
    }

    @Test
    public void testGenerateSubTaskParametersWithOriginTableCleanStrategy() {
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test (id INT); ALTER TABLE test ADD COLUMN name VARCHAR(255);");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_TABLE);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE);

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
            // check other properties of subTaskParameters
        } catch (UnexpectedException e) {
            fail("generateSubTaskParameters should not throw an UnexpectedException.");
        }
    }

    @Test
    public void testGenerateSubTaskParametersWithMultipleSqls() {
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test (id INT); ALTER TABLE test ADD COLUMN name VARCHAR(255);");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_TABLE);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
            assertEquals(2, subTaskParameters.size());
            // check other properties of subTaskParameters
        } catch (UnexpectedException e) {
            fail("generateSubTaskParameters should not throw an UnexpectedException.");
        }
    }

    @Test
    public void testGenerateSubTaskParametersWithSingleSql() {
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test (id INT);");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_TABLE);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
            assertEquals(1, subTaskParameters.size());
            // check other properties of subTaskParameters
        } catch (UnexpectedException e) {
            fail("generateSubTaskParameters should not throw an UnexpectedException.");
        }
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptySql() {
        onlineSchemaChangeParameters.setSqlContent("");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_TABLE);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
            assertTrue(subTaskParameters.isEmpty());
        } catch (UnexpectedException e) {
            fail("generateSubTaskParameters should not throw an UnexpectedException.");
        }
    }
}