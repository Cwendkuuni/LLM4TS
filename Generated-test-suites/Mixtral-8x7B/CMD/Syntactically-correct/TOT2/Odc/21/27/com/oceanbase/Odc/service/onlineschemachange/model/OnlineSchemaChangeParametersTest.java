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
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testGettersAndSetters() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(300);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP_MASTER_SLAVE);

        assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
        assertEquals("ALTER TABLE table1 ADD COLUMN col1 INT", onlineSchemaChangeParameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
        assertEquals(Integer.valueOf(300), onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        assertEquals(Integer.valueOf(3), onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DROP_AND_CREATE, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
        assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
        assertEquals(new ArrayList<>(), onlineSchemaChangeParameters.getLockUsers());
        assertEquals(SwapTableType.SWAP_MASTER_SLAVE, onlineSchemaChangeParameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT; ALTER TABLE table1 ADD COLUMN "
                + "col2 INT");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, schema);

        assertEquals(2, subTaskParameters.size());

        TableIdentity tableIdentity1 = new TableIdentity(schema, "table1");
        TableIdentity tableIdentity2 = new TableIdentity(schema, "table1");

        for (OnlineSchemaChangeScheduleTaskParameters subTaskParameter : subTaskParameters) {
            if (subTaskParameter.getDatabaseName().equals(schema)
                    && subTaskParameter.getOriginTableName().equals("table1")) {
                assertEquals(tableIdentity1, new TableIdentity(subTaskParameter.getDatabaseName(),
                        subTaskParameter.getOriginTableName()));

                assertEquals(schema, subTaskParameter.getDatabaseName());
                assertEquals("table1", subTaskParameter.getOriginTableName());
                assertEquals(schema, subTaskParameter.getNewTableName());
                assertEquals(2, subTaskParameter.getSqlsToBeExecuted().size());
                assertTrue(subTaskParameter.getSqlsToBeExecuted().contains("ALTER TABLE table1 ADD COLUMN col1 INT"));
                assertTrue(subTaskParameter.getSqlsToBeExecuted().contains("ALTER TABLE table1 ADD COLUMN col2 INT"));

            } else if (subTaskParameter.getDatabaseName().equals(schema)
                    && subTaskParameter.getOriginTableName().equals("table1")
                    && subTaskParameter.getNewTableName().equals("table1_new")) {
                assertEquals(tableIdentity2, new TableIdentity(subTaskParameter.getDatabaseName(),
                        DdlUtils.replaceTableName(subTaskParameter.getSqlsToBeExecuted().get(0),
                                subTaskParameter.getNewTableName(),
                                connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER))
                        .getSecond());

                assertEquals(schema, subTaskParameter.getDatabaseName());
                assertEquals("table1", subTaskParameter.getOriginTableName());
                assertEquals("table1_new", subTaskParameter.getNewTableName());
                assertEquals(1, subTaskParameter.getSqlsToBeExecuted().size());
                assertTrue(subTaskParameter.getSqlsToBeExecuted().contains("ALTER TABLE table1_new ADD COLUMN col2 INT"));

            } else {
                fail("Unexpected subtask parameter: " + subTaskParameter);
            }
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersThrowsException() throws Exception {
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT; ALTER TABLE table1 ADD COLUMN "
                + "col2 INT");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();

        for (String sql : onlineSchemaChangeParameters.getSqlContent().split(";")) {
            OnlineSchemaChangeScheduleTaskParameters parameter = new SubTaskParameterFactory(connectionConfig,
                    schema).generate(sql, onlineSchemaChangeParameters.getSqlType());
            TableIdentity key = new TableIdentity(parameter.getDatabaseName(), parameter.getOriginTableName());
            taskParameters.putIfAbsent(key, parameter);
            if (onlineSchemaChangeParameters.getSqlType() == OnlineSchemaChangeSqlType.ALTER) {
                String newAlterStmt = DdlUtils.replaceTableName(sql, parameter.getNewTableName(),
                        connectionConfig.getDialectType(), onlineSchemaChangeParameters.getSqlType());
                taskParameters.get(key).getSqlsToBeExecuted().add(newAlterStmt);
            }
        }

        fail("Expected Exception not thrown");
    }

}