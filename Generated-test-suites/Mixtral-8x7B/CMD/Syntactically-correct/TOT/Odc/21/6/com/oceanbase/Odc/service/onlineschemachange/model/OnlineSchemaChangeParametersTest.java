import static org.junit.Assert.*;

import java.io.Serializable;
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
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY); ALTER TABLE test ADD COLUMN name VARCHAR(255);");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
                    .generateSubTaskParameters(connectionConfig, "testSchema");

            assertEquals(2, subTaskParameters.size());

            OnlineSchemaChangeScheduleTaskParameters firstParameter = subTaskParameters.get(0);
            assertEquals("testSchema", firstParameter.getDatabaseName());
            assertEquals("test", firstParameter.getOriginTableName());
            assertEquals("CREATE TABLE test (id INT PRIMARY KEY);", firstParameter.getSqlsToBeExecuted().get(0));

            OnlineSchemaChangeScheduleTaskParameters secondParameter = subTaskParameters.get(1);
            assertEquals("testSchema", secondParameter.getDatabaseName());
            assertEquals("test", secondParameter.getOriginTableName());
            assertEquals("ALTER TABLE test ADD COLUMN name VARCHAR(255);",
                    secondParameter.getSqlsToBeExecuted().get(0));

            assertEquals("test", secondParameter.getNewTableName());

        } catch (UnexpectedException e) {
            fail("Failed to generate subtasks due to unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGenerateSubTaskParametersEmptySqlContent() {
        onlineSchemaChangeParameters.setSqlContent("");

        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testSchema");
            fail("Expected UnexpectedException to be thrown due to empty sqlContent.");
        } catch (UnexpectedException e) {
            assertEquals("Failed to generate subtasks with parameter: OnlineSchemaChangeParameters(sqlType=null, sqlContent=, errorStrategy=null, lockTableTimeOutSeconds=null, swapTableNameRetryTimes=null, originTableCleanStrategy=null, delimiter=;, lockUsers=null, swapTableType=null)",
                    e.getMessage());
        }
    }

}