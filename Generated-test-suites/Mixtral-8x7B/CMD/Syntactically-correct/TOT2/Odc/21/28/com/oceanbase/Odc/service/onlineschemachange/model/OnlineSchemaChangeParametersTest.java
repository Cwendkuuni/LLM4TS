import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 int");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.BACKUP);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.PT);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
        assertEquals("ALTER TABLE table1 ADD COLUMN c1 int", onlineSchemaChangeParameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
        assertEquals(Integer.valueOf(10), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        assertEquals(Integer.valueOf(3), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.BACKUP, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
        assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
        assertEquals(new ArrayList<>(), onlineSchemaChangeParameters.getLockUsers());
        assertEquals(SwapTableType.PT, onlineSchemaChangeParameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        assertEquals(true, onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        try {
            Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
            taskParameters.put(new TableIdentity("schema1", "table1"),
                    new OnlineSchemaChangeScheduleTaskParameters("schema1", "table1", "table2", "ALTER TABLE",
                            "ALTER TABLE table1 ADD COLUMN c1 int", "", ""));
            List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>(taskParameters.values());

            List<OnlineSchemaChangeScheduleTaskParameters> actual = onlineSchemaChangeParameters
                    .generateSubTaskParameters(connectionConfig, "schema1");
            assertEquals(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGenerateSubTaskParameters_withError() {
        onlineSchemaChangeParameters.setSqlContent(null);
        assertThrows(UnexpectedException.class,
                () -> onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema1"));
    }
}