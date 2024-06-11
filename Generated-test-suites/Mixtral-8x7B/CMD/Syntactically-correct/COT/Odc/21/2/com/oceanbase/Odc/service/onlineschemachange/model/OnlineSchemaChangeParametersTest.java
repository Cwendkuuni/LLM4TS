package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@SuppressWarnings("unchecked")
public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "sql content";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        Integer lockTableTimeOutSeconds = 100;
        Integer swapTableNameRetryTimes = 5;
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.DROP_AND_CREATE;
        String delimiter = ",";

        onlineSchemaChangeParameters.setSqlType(sqlType);
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        onlineSchemaChangeParameters.setDelimiter(delimiter);

        assertEquals(sqlType, onlineSchemaChangeParameters.getSqlType());
        assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());
        assertEquals(errorStrategy, onlineSchemaChangeParameters.getErrorStrategy());
        assertEquals(lockTableTimeOutSeconds, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        assertEquals(swapTableNameRetryTimes, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
        assertEquals(originTableCleanStrategy, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
        assertEquals(delimiter, onlineSchemaChangeParameters.getDelimiter());
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        String schema = "schema";
        ConnectionConfig connectionConfig = new ConnectionConfig();

        OnlineSchemaChangeScheduleTaskParameters taskParameter1 = createTaskParameter("db1", "tbl1",
                "newTbl1", OnlineSchemaChangeSqlType.ALTER);
        OnlineSchemaChangeScheduleTaskParameters taskParameter2 = createTaskParameter("db2", "tbl2",
                "newTbl2", OnlineSchemaChangeSqlType.CREATE);

        List<OnlineSchemaChangeScheduleTaskParameters> expectedList = new ArrayList<>();
        expectedList.add(taskParameter1);
        expectedList.add(taskParameter2);

        // Mock sqlContent
        String sqlContent = "ALTER TABLE db1.tbl1 ADD COLUMN c1 INT; CREATE TABLE db2.tbl2 (c1 INT);";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, schema);

        assertEquals(expectedList.size(), result.size());
        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i).getDatabaseName(), result.get(i).getDatabaseName());
            assertEquals(expectedList.get(i).getOriginTableName(), result.get(i).getOriginTableName());
            assertEquals(expectedList.get(i).getNewTableName(), result.get(i).getNewTableName());
            assertEquals(expectedList.get(i).getSqlsToBeExecuted().size(),
                    result.get(i).getSqlsToBeExecuted().size());
        }
    }

    @Test
    public void testGenerateSubTaskParameters_Exception() {
        String schema = "schema";
        ConnectionConfig connectionConfig = new ConnectionConfig();

        // Mock sqlContent
        String sqlContent = "ALTER TABLE db1.tbl1 ADD COLUMN c1 INT; CREATE TABLE db2.tbl2 (c1 INT;";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);

        ThrowingRunnable runnable = () -> onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig,
                schema);

        assertThrows(UnexpectedException.class, runnable);
    }

    @Test
    public void testSerializable() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        Serializable serializedOnlineSchemaChangeParameters = (Serializable) onlineSchemaChangeParameters;

        OnlineSchemaChangeParameters deserializedOnlineSchemaChangeParameters = (OnlineSchemaChangeParameters) serializedOnlineSchemaChangeParameters;

        assertEquals(onlineSchemaChangeParameters.getSqlType(), deserializedOnlineSchemaChangeParameters.getSqlType());
        assertEquals(onlineSchemaChangeParameters.getSqlContent(),
                deserializedOnlineSchemaChangeParameters.getSqlContent());
        assertEquals(onlineSchemaChangeParameters.getErrorStrategy(),
                deserializedOnlineSchemaChangeParameters.getErrorStrategy());
        assertEquals(onlineSchemaChangeParameters.getLockTableTimeOutSeconds(),
                deserializedOnlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        assertEquals(onlineSchemaChangeParameters.getSwapTableNameRetryTimes(),
                deserializedOnlineSchemaChangeParameters.getSwapTableNameRetryTimes());
        assertEquals(onlineSchemaChangeParameters.getOriginTableCleanStrategy(),
                deserializedOnlineSchemaChangeParameters.getOriginTableCleanStrategy());
        assertEquals(onlineSchemaChangeParameters.getDelimiter(),
                deserializedOnlineSchemaChangeParameters.getDelimiter());
    }

    private OnlineSchemaChangeScheduleTaskParameters createTaskParameter(String dbName, String originTableName,
            String newTableName, OnlineSchemaChangeSqlType sqlType) {
        List<String> sqlsToBeExecuted = new ArrayList<>();
        sqlsToBeExecuted.add("ALTER TABLE " + dbName + "." + originTableName + " ADD COLUMN c1 INT;");

        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setDatabaseName(dbName);
        taskParameter.setOriginTableName(originTableName);
        taskParameter.setNewTableName(newTableName);
        taskParameter.setSqlsToBeExecuted(sqlsToBeExecuted);
        taskParameter.setSqlType(sqlType);
        return taskParameter;
    }
}

