package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.common.util.SqlUtils;

@SuppressWarnings("unchecked")
public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        connectionConfig = mock(ConnectionConfig.class);
    }

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE table_name ADD column_name datatype";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        Integer lockTableTimeOutSeconds = 300;
        Integer swapTableNameRetryTimes = 5;
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.KEEP;
        String delimiter = ",";
        List<String> lockUsers = Arrays.asList("user1", "user2");
        SwapTableType swapTableType = SwapTableType.INTERNAL;

        onlineSchemaChangeParameters.setSqlType(sqlType);
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        onlineSchemaChangeParameters.setDelimiter(delimiter);
        onlineSchemaChangeParameters.setLockUsers(lockUsers);
        onlineSchemaChangeParameters.setSwapTableType(swapTableType);

        assertEquals(sqlType, onlineSchemaChangeParameters.getSqlType());
        assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());
        assertEquals(errorStrategy, onlineSchemaChangeParameters.getErrorStrategy());
        assertEquals(lockTableTimeOutSeconds, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        assertEquals(swapTableNameRetryTimes, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
        assertEquals(originTableCleanStrategy, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
        assertEquals(delimiter, onlineSchemaChangeParameters.getDelimiter());
        assertEquals(lockUsers, onlineSchemaChangeParameters.getLockUsers());
        assertEquals(swapTableType, onlineSchemaChangeParameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParametersHappyPath() throws Exception {
        String sqlContent = "ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 int";
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.INTERNAL);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
                onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(2, subTaskParameters.size());
        assertNotNull(subTaskParameters.get(0).getSqlsToBeExecuted());
        assertEquals(1, subTaskParameters.get(0).getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE table1 ADD column1 int",
                subTaskParameters.get(0).getSqlsToBeExecuted().get(0));

        assertNotNull(subTaskParameters.get(1).getSqlsToBeExecuted());
        assertEquals(1, subTaskParameters.get(1).getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE table2 ADD column2 int",
                subTaskParameters.get(1).getSqlsToBeExecuted().get(0));
    }

    @Test
    public void testGenerateSubTaskParametersEmptySqlContent() throws Exception {
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        onlineSchemaChangeParameters.setSqlContent("");
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.INTERNAL);

        assertThrows(UnexpectedException.class,
                () -> onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema"));
    }

    @Test
    public void testGenerateSubTaskParametersNullSqlContent() throws Exception {
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        onlineSchemaChangeParameters.setSqlContent(null);
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.INTERNAL);

        assertThrows(UnexpectedException.class,
                () -> onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema"));
    }

    @Test
    public void testGenerateSubTaskParametersNoSqls() throws Exception {
        String sqlContent = "ALTER TABLE table1 ADD CONSTRAINT fk_table1_column1 FOREIGN KEY (column1) REFERENCES "
                + "table2(column1)";
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.INTERNAL);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
                onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(1, subTaskParameters.size());
        assertNotNull(subTaskParameters.get(0).getSqlsToBeExecuted());
        assertEquals(1, subTaskParameters.get(0).getSqlsToBeExecuted().size());
        assertEquals(sqlContent, subTaskParameters.get(0).getSqlsToBeExecuted().get(0));
    }

    @Test
    public void testGenerateSubTaskParametersNullConnectionConfig() throws Exception {
        String sqlContent = "ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 int";

        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.INTERNAL);

        assertThrows(NullPointerException.class,
                () -> onlineSchemaChangeParameters.generateSubTaskParameters(null, "schema"));
    }

    @Test
    public void testGenerateSubTaskParametersNullSchema() throws Exception {
        String sqlContent = "ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 int";
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.INTERNAL);

        assertThrows(NullPointerException.class,
                () -> onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, null));
    }

    @Test
    public void testGenerateSubTaskParametersSwapTableTypeExternal() throws Exception {
        String sqlContent = "ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 int";
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.EXTERNAL);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
                onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(2, subTaskParameters.size());

        for (OnlineSchemaChangeScheduleTaskParameters parameter : subTaskParameters) {
            assertEquals("", parameter.getNewTableName());
        }
    }

    @Test
    public void testGenerateSubTaskParametersSwapTableTypeInternalWithNewTableName() throws Exception {
        String sqlContent = "ALTER TABLE table1 ADD column1 int; ALTER TABLE table2 ADD column2 int";
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.INTERNAL);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
                onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(2, subTaskParameters.size());

        for (OnlineSchemaChangeScheduleTaskParameters parameter : subTaskParameters) {
            assertNotNull(parameter.getNewTableName());
        }
    }
}