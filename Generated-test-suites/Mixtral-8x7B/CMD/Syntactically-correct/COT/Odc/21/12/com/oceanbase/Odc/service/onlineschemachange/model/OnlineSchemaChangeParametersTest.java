package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        onlineSchemaChangeParameters.setSqlType(sqlType);
        assertEquals(sqlType, onlineSchemaChangeParameters.getSqlType());

        String sqlContent = "CREATE TABLE test (id INT PRIMARY KEY)";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());

        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
        assertEquals(errorStrategy, onlineSchemaChangeParameters.getErrorStrategy());

        Integer lockTableTimeOutSeconds = 10;
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        assertEquals(lockTableTimeOutSeconds, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());

        Integer swapTableNameRetryTimes = 5;
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        assertEquals(swapTableNameRetryTimes, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());

        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.DROP;
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        assertEquals(originTableCleanStrategy, onlineSchemaChangeParameters.getOriginTableCleanStrategy());

        String delimiter = ",";
        onlineSchemaChangeParameters.setDelimiter(delimiter);
        assertEquals(delimiter, onlineSchemaChangeParameters.getDelimiter());

        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        onlineSchemaChangeParameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());

        SwapTableType swapTableType = SwapTableType.NO_SWAP;
        onlineSchemaChangeParameters.setSwapTableType(swapTableType);
        assertEquals(swapTableType, onlineSchemaChangeParameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test";

        String sqlContent = "CREATE TABLE test (id INT PRIMARY KEY); ALTER TABLE test ADD COLUMN name VARCHAR(255)";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
                onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(2, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters firstParameter = subTaskParameters.get(0);
        assertEquals("test", firstParameter.getDatabaseName());
        assertEquals("test", firstParameter.getOriginTableName());
        assertEquals("CREATE TABLE test (id INT PRIMARY KEY)", firstParameter.getSqlsToBeExecuted().get(0));

        OnlineSchemaChangeScheduleTaskParameters secondParameter = subTaskParameters.get(1);
        assertEquals("test", secondParameter.getDatabaseName());
        assertEquals("test", secondParameter.getOriginTableName());
        assertEquals("ALTER TABLE test ADD COLUMN name VARCHAR(255)",
                secondParameter.getSqlsToBeExecuted().get(0));
    }

    @Test
    public void testGenerateSubTaskParametersException() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test";

        onlineSchemaChangeParameters.setSqlContent("INVALID SQL");

        assertThrows(UnexpectedException.class,
                () -> onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema));
    }

    @Test
    public void testSerializable() throws Exception {
        Byte[] serialized = serialize(onlineSchemaChangeParameters);
        DeserializedObject deserialized = deserialize(serialized);

        assertEquals(onlineSchemaChangeParameters, deserialized.getObject());
    }

    private static Byte[] serialize(Serializable obj) throws Exception {
        // TODO: Implement serialization
        return null;
    }

    private static DeserializedObject deserialize(Byte[] bytes) throws Exception {
        // TODO: Implement deserialization
        return null;
    }

    private static class DeserializedObject {
        private OnlineSchemaChangeParameters object;

        public DeserializedObject(OnlineSchemaChangeParameters object) {
            this.object = object;
        }

        public OnlineSchemaChangeParameters getObject() {
            return object;
        }
    }
}

