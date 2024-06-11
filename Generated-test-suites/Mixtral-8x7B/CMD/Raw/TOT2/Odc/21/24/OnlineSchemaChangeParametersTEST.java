import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testGetSqlType() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
    }

    @Test
    public void testGetSqlContent() {
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT;");
        assertEquals("ALTER TABLE table1 ADD COLUMN col1 INT;", onlineSchemaChangeParameters.getSqlContent());
    }

    @Test
    public void testGetErrorStrategy() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
    }

    @Test
    public void testGetLockTableTimeOutSeconds() {
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
        assertEquals(Integer.valueOf(10), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testGetSwapTableNameRetryTimes() {
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(5);
        assertEquals(Integer.valueOf(5), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
    }

    @Test
    public void testGetOriginTableCleanStrategy() {
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
        assertEquals(OriginTableCleanStrategy.KEEP, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testGetDelimiter() {
        onlineSchemaChangeParameters.setDelimiter(";");
        assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
    }

    @Test
    public void testGetLockUsers() {
        onlineSchemaChangeParameters.setLockUsers(new ArrayList<String>() {{ add("user1"); }});
        assertEquals(new ArrayList<String>() {{ add("user1"); }},
                onlineSchemaChangeParameters.getLockUsers());
    }

    @Test
    public void testGetSwapTableType() {
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.MINUS);
        assertEquals(SwapTableType.MINUS, onlineSchemaChangeParameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schema1";

        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT; ALTER TABLE table2 ADD COLUMN col2 INT;");
        onlineSchemaChangeParameters.setDelimiter(";");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParametersList =
                onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(2, subTaskParametersList.size());

        OnlineSchemaChangeScheduleTaskParameters subTaskParameters1 = subTaskParametersList.get(0);
        assertEquals("schema1", subTaskParameters1.getDatabaseName());
        assertEquals("table1", subTaskParameters1.getOriginTableName());
        assertEquals("col1", subTaskParameters1.getColumnName());
        assertEquals("INT", subTaskParameters1.getColumnType());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameters1.getSqlType());
        assertEquals(1, subTaskParameters1.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE table1 ADD COLUMN col1 INT",
                subTaskParameters1.getSqlsToBeExecuted().get(0));

        OnlineSchemaChangeScheduleTaskParameters subTaskParameters2 = subTaskParametersList.get(1);
        assertEquals("schema1", subTaskParameters2.getDatabaseName());
        assertEquals("table2", subTaskParameters2.getOriginTableName());
        assertEquals("col2", subTaskParameters2.getColumnName());
        assertEquals("INT", subTaskParameters2.getColumnType());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameters2.getSqlType());
        assertEquals(1, subTaskParameters2.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE table2 ADD COLUMN col2 INT",
                subTaskParameters2.getSqlsToBeExecuted().get(0));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schema1";

        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("NOT VALID SQL CONTENT");
        onlineSchemaChangeParameters.setDelimiter(";");

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
    }

    @Test
    public void testGenerateSubTaskParametersWithNewTableName() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schema1";

        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT; ALTER TABLE table2 ADD COLUMN col2 INT;");
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.MINUS);

        SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, schema);
        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new HashMap<>();

        taskParameters.put(new TableIdentity(schema, "table1"),
                subTaskParameterFactory.generate("ALTER TABLE table1 ADD COLUMN col1 INT",
                        OnlineSchemaChangeSqlType.ALTER));

        taskParameters.put(new TableIdentity(schema, "table2"),
                subTaskParameterFactory.generate("ALTER TABLE table2 ADD COLUMN col2 INT",
                        OnlineSchemaChangeSqlType.ALTER));

        List<String> sqlsToBeExecuted = new ArrayList<>();
        sqlsToBeExecuted.add("ALTER TABLE table1 ADD COLUMN col1 INT");
        sqlsToBeExecuted.add("ALTER TABLE table2_new ADD COLUMN col2 INT");

        taskParameters.put(new TableIdentity(schema, "table2_new"),
                subTaskParameterFactory.generate(sqlsToBeExecuted,
                        OnlineSchemaChangeSqlType.ALTER, "table2_new"));

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParametersList =
                new ArrayList<>(taskParameters.values());

        onlineSchemaChangeParameters.setGenerateSubTaskParameters(subTaskParameterFactory, taskParameters);

        assertEquals(3, subTaskParametersList.size());

        OnlineSchemaChangeScheduleTaskParameters subTaskParameters1 = subTaskParametersList.get(0);
        assertEquals("schema1", subTaskParameters1.getDatabaseName());
        assertEquals("table1", subTaskParameters1.getOriginTableName());
        assertEquals("col1", subTaskParameters1.getColumnName());
        assertEquals("INT", subTaskParameters1.getColumnType());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameters1.getSqlType());
        assertEquals(1, subTaskParameters1.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE table1 ADD COLUMN col1 INT",
                subTaskParameters1.getSqlsToBeExecuted().get(0));

        OnlineSchemaChangeScheduleTaskParameters subTaskParameters2 = subTaskParametersList.get(1);
        assertEquals("schema1", subTaskParameters2.getDatabaseName());
        assertEquals("table2", subTaskParameters2.getOriginTableName());
        assertEquals("col2", subTaskParameters2.getColumnName());
        assertEquals("INT", subTaskParameters2.getColumnType());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameters2.getSqlType());
        assertEquals(1, subTaskParameters2.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE table2_new ADD COLUMN col2 INT",
                subTaskParameters2.getSqlsToBeExecuted().get(0));

        OnlineSchemaChangeScheduleTaskParameters subTaskParameters3 = subTaskParametersList.get(2);
        assertEquals("schema1", subTaskParameters3.getDatabaseName());
        assertEquals("table2_new", subTaskParameters3.getOriginTableName());
        assertEquals("col2", subTaskParameters3.getColumnName());
        assertEquals("INT", subTaskParameters3.getColumnType());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameters3.getSqlType());
        assertEquals(2, subTaskParameters3.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE table1 ADD COLUMN col1 INT",
                subTaskParameters3.getSqlsToBeExecuted().get(0));
        assertEquals("ALTER TABLE table2_new ADD COLUMN col2 INT",
                subTaskParameters3.getSqlsToBeExecuted().get(1));
    }
}