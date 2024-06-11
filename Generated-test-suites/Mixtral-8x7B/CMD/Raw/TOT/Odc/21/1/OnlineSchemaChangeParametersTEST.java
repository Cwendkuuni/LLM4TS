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

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Test case 1: Verify correct sqlType
    @Test
    public void testSqlType() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
    }

    // Test case 2: Verify correct sqlContent
    @Test
    public void testSqlContent() {
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD column1 int;");
        assertEquals("ALTER TABLE table1 ADD column1 int;", onlineSchemaChangeParameters.getSqlContent());
    }

    // Test case 3: Verify correct errorStrategy
    @Test
    public void testErrorStrategy() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
    }

    // Test case 4: Verify correct lockTableTimeOutSeconds
    @Test
    public void testLockTableTimeOutSeconds() {
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(300);
        assertEquals(Integer.valueOf(300), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
    }

    // Test case 5: Verify correct swapTableNameRetryTimes
    @Test
    public void testSwapTableNameRetryTimes() {
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
        assertEquals(Integer.valueOf(5), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
    }

    // Test case 6: Verify correct originTableCleanStrategy
    @Test
    public void testOriginTableCleanStrategy() {
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
        assertEquals(OriginTableCleanStrategy.KEEP, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
    }

    // Test case 7: Verify correct delimiter
    @Test
    public void testDelimiter() {
        onlineSchemaChangeParameters.setDelimiter(";");
        assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
    }

    // Test case 8: Verify correct lockUsers
    @Test
    public void testLockUsers() {
        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");
        onlineSchemaChangeParameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
    }

    // Test case 9: Verify correct swapTableType
    @Test
    public void testSwapTableType() {
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.TEMPORARY);
        assertEquals(SwapTableType.TEMPORARY, onlineSchemaChangeParameters.getSwapTableType());
    }

    // Test case 10: Verify generateSubTaskParameters()
    @Test
    public void testGenerateSubTaskParameters()
            throws Exception, UnexpectedException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schema1";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE table1 ADD column1 int; ALTER TABLE table1 ADD column2 int;");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
                parameters.generateSubTaskParameters(connectionConfig, schema);
        assertTrue(subTaskParameters.size() == 2);
    }

}