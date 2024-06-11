import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeSqlType.NONE, parameters.getSqlType());
        assertEquals(";", parameters.getDelimiter());
        assertEquals(new ArrayList<>(), parameters.getLockUsers());
        assertEquals(TaskErrorStrategy.TERMINATE, parameters.getErrorStrategy());
        assertEquals(null, parameters.getSqlContent());
        assertEquals(0, parameters.getLockTableTimeOutSeconds().intValue());
        assertEquals(0, parameters.getSwapTableNameRetryTimes().intValue());
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, parameters.getOriginTableCleanStrategy());
        assertEquals(OnlineSchemaChangeParameters.SwapTableType.NORMAL, parameters.getSwapTableType());
    }

    @Test
    public void testOnlineSchemaChangeParametersSerialization() throws Exception {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE t1 ADD c1 INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<String>() {{
            add("user1");
            add("user2");
        }});
        parameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY);

        Byte[] serializedParameters = SerializationUtils.serialize(parameters);

        OnlineSchemaChangeParameters deserializedParameters = (OnlineSchemaChangeParameters) SerializationUtils.deserialize(serializedParameters);

        assertEquals(parameters.getSqlType(), deserializedParameters.getSqlType());
        assertEquals(parameters.getSqlContent(), deserializedParameters.getSqlContent());
        assertEquals(parameters.getErrorStrategy(), deserializedParameters.getErrorStrategy());
        assertEquals(parameters.getLockTableTimeOutSeconds(), deserializedParameters.getLockTableTimeOutSeconds());
        assertEquals(parameters.getSwapTableNameRetryTimes(), deserializedParameters.getSwapTableNameRetryTimes());
        assertEquals(parameters.getOriginTableCleanStrategy(), deserializedParameters.getOriginTableCleanStrategy());
        assertEquals(parameters.getDelimiter(), deserializedParameters.getDelimiter());
        assertEquals(parameters.getLockUsers(), deserializedParameters.getLockUsers());
        assertEquals(parameters.getSwapTableType(), deserializedParameters.getSwapTableType());
    }

    @Test
    public void testGenerateSubTaskParameters() throws UnexpectedException {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE t1 ADD c1 INT; ALTER TABLE t2 ADD c2 INT;");
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<String>() {{
            add("user1");
            add("user2");
        }});
        parameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "testSchema");

        assertEquals(2, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters subTaskParameter1 = subTaskParameters.get(0);
        assertEquals("testSchema", subTaskParameter1.getDatabaseName());
        assertEquals("t1", subTaskParameter1.getOriginTableName());
        assertEquals("c1", subTaskParameter1.getNewColumnName());
        assertEquals("INT", subTaskParameter1.getNewColumnType());
        assertEquals(1, subTaskParameter1.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE t1 ADD c1 INT", subTaskParameter1.getSqlsToBeExecuted().get(0));

        OnlineSchemaChangeScheduleTaskParameters subTaskParameter2 = subTaskParameters.get(1);
        assertEquals("testSchema", subTaskParameter2.getDatabaseName());
        assertEquals("t2", subTaskParameter2.getOriginTableName());
        assertEquals("c2", subTaskParameter2.getNewColumnName());
        assertEquals("INT", subTaskParameter2.getNewColumnType());
        assertEquals(1, subTaskParameter2.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE t2 ADD c2 INT", subTaskParameter2.getSqlsToBeExecuted().get(0));
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGetErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(TaskErrorStrategy.TERMINATE, parameters.getErrorStrategy());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
    }

    @Test
    public void testGetLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(0, parameters.getLockTableTimeOutSeconds().intValue());

        parameters.setLockTableTimeOutSeconds(10);
        assertEquals(10, parameters.getLockTableTimeOutSeconds().intValue());
    }

    @Test
    public void testGetSwapTableNameRetryTimes() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(0, parameters.getSwapTableNameRetryTimes().intValue());

        parameters.setSwapTableNameRetryTimes(3);
        assertEquals(3, parameters.getSwapTableNameRetryTimes().intValue());
    }

    @Test
    public void testGetOriginTableCleanStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP, parameters.getOriginTableCleanStrategy());

        parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP);
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testGetDelimiter() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(";", parameters.getDelimiter());

        parameters.setDelimiter(",");
        assertEquals(",", parameters.getDelimiter());
    }

    @Test
    public void testGetLockUsers() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(new ArrayList<>(), parameters.getLockUsers());

        parameters.setLockUsers(new ArrayList<String>() {{
            add("user1");
            add("user2");
        }});

        assertEquals(2, parameters.getLockUsers().size());
        assertEquals("user1", parameters.getLockUsers().get(0));
        assertEquals("user2", parameters.getLockUsers().get(1));
    }

    @Test
    public void testGetSwapTableType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeParameters.SwapTableType.NORMAL, parameters.getSwapTableType());

        parameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY);
        assertEquals(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY, parameters.getSwapTableType());
    }

    @Test
    public void testGenerateSubTaskParametersWithException() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE t1 ADD c1 INT; ALTER TABLE t2 ADD c2 INT;");
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<String>() {{
            add("user1");
            add("user2");
        }});
        parameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY);

        SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "testSchema") {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters generate(String sql, OnlineSchemaChangeSqlType sqlType)
                    throws Exception {
                throw new UnexpectedException("Failed to generate subtasks with parameter: " + this);
            }
        };

        assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(connectionConfig, "testSchema"));
    }

}