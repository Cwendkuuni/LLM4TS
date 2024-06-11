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

    @Mock
    private SubTaskParameterFactory subTaskParameterFactory;

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 int;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        List<OnlineSchemaChangeScheduleTaskParameters> result = params.generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(1, result.size());
        OnlineSchemaChangeScheduleTaskParameters taskParams = result.get(0);
        assertEquals("schema", taskParams.getDatabaseName());
        assertEquals("table1", taskParams.getOriginTableName());
        assertEquals("col1", taskParams.getNewColumnName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParams.getSqlType());
    }

    @Test
    public void testGetLockTableTimeOutSeconds() {
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
        assertEquals(30, onlineSchemaChangeParameters.getLockTableTimeOutSeconds().intValue());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
    }

    @Test
    public void testSwapTableType() {
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.RENAME_TABLE);
        assertEquals(SwapTableType.RENAME_TABLE, onlineSchemaChangeParameters.getSwapTableType());
    }

    @Test
    public void testOriginTableCleanStrategy() {
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
        assertEquals(OriginTableCleanStrategy.KEEP, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws Exception {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("INVALID SQL");
        params.generateSubTaskParameters(connectionConfig, "schema");
    }

}