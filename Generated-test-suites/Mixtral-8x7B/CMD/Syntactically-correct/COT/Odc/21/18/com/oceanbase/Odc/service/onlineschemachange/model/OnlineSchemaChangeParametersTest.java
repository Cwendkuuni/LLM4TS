package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@SuppressWarnings("unused")
public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        assertEquals(OnlineSchemaChangeSqlType.DDL, parameters.getSqlType());
        assertEquals(";", parameters.getDelimiter());
        assertNull(parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.STOP, parameters.getErrorStrategy());
        assertEquals(0, parameters.getLockTableTimeOutSeconds().intValue());
        assertEquals(0, parameters.getSwapTableNameRetryTimes().intValue());
        assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
        assertNull(parameters.getLockUsers());
        assertEquals(SwapTableType.AUTO, parameters.getSwapTableType());
    }

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE table_name ADD COLUMN column_name datatype");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(300);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter("//");

        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");
        parameters.setLockUsers(lockUsers);

        parameters.setSwapTableType(SwapTableType.MANUAL);

        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
        assertEquals("ALTER TABLE table_name ADD COLUMN column_name datatype", parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(300, parameters.getLockTableTimeOutSeconds().intValue());
        assertEquals(5, parameters.getSwapTableNameRetryTimes().intValue());
        assertEquals(OriginTableCleanStrategy.TRUNCATE, parameters.getOriginTableCleanStrategy());
        assertEquals("//", parameters.getDelimiter());
        assertEquals(lockUsers, parameters.getLockUsers());
        assertEquals(SwapTableType.MANUAL, parameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        assertEquals(false, parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(true, parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        String schema = "test_schema";
        String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 INT; ALTER TABLE table2 ADD COLUMN col2 VARCHAR(100);";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setSchema(schema);

        SubTaskParameterFactory subTaskParameterFactory =
                new SubTaskParameterFactory(connectionConfig, schema);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
                parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(2, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters parameter1 =
                subTaskParameterFactory.generate("ALTER TABLE table1 ADD COLUMN col1 INT",
                        OnlineSchemaChangeSqlType.ALTER);

        OnlineSchemaChangeScheduleTaskParameters parameter2 =
                subTaskParameterFactory.generate("ALTER TABLE table2 ADD COLUMN col2 VARCHAR(100)",
                        OnlineSchemaChangeSqlType.ALTER);

        TableIdentity key1 = new TableIdentity(parameter1.getDatabaseName(), parameter1.getOriginTableName());
        TableIdentity key2 = new TableIdentity(parameter2.getDatabaseName(), parameter2.getOriginTableName());

        assertEquals(parameter1.getDatabaseName(), subTaskParameters.get(0).getDatabaseName());
        assertEquals(parameter1.getOriginTableName(), subTaskParameters.get(0).getOriginTableName());
        assertEquals(parameter1.getNewTableName(), subTaskParameters.get(0).getNewTableName());
        assertEquals(parameter1.getSqlsToBeExecuted(), subTaskParameters.get(0).getSqlsToBeExecuted());

        assertEquals(parameter2.getDatabaseName(), subTaskParameters.get(1).getDatabaseName());
        assertEquals(parameter2.getOriginTableName(), subTaskParameters.get(1).getOriginTableName());
        assertEquals(parameter2.getNewTableName(), subTaskParameters.get(1).getNewTableName());
        assertEquals(parameter2.getSqlsToBeExecuted(), subTaskParameters.get(1).getSqlsToBeExecuted());

        String newAlterStmt1 =
                DdlUtils.replaceTableName(
                        "ALTER TABLE table1 ADD COLUMN col1 INT",
                        parameter1.getNewTableName(),
                        connectionConfig.getDialectType(),
                        OnlineSchemaChangeSqlType.ALTER);

        String newAlterStmt2 =
                DdlUtils.replaceTableName(
                        "ALTER TABLE table2 ADD COLUMN col2 VARCHAR(100)",
                        parameter2.getNewTableName(),
                        connectionConfig.getDialectType(),
                        OnlineSchemaChangeSqlType.ALTER);

        assertEquals(2, subTaskParameters.get(0).getSqlsToBeExecuted().size());
        assertEquals(newAlterStmt1, subTaskParameters.get(0).getSqlsToBeExecuted().get(1));

        assertEquals(2, subTaskParameters.get(1).getSqlsToBeExecuted().size());
        assertEquals(newAlterStmt2, subTaskParameters.get(1).getSqlsToBeExecuted().get(1));
    }

    @Test
    public void testGenerateSubTaskParameters_EmptySqlContent() throws Exception {
        String schema = "test_schema";
        String sqlContent = "";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setSchema(schema);

        SubTaskParameterFactory subTaskParameterFactory =
                new SubTaskParameterFactory(connectionConfig, schema);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
                parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(0, subTaskParameters.size());
    }

    @Test
    public void testGenerateSubTaskParameters_NullSqlContent() throws Exception {
        String schema = "test_schema";
        String sqlContent = null;

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setSchema(schema);

        SubTaskParameterFactory subTaskParameterFactory =
                new SubTaskParameterFactory(connectionConfig, schema);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
                parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(0, subTaskParameters.size());
    }

    @Test
    public void testGenerateSubTaskParameters_Exception() throws Exception {
        String schema = "test_schema";
        String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 INT";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setSchema(null);

        SubTaskParameterFactory subTaskParameterFactory =
                new SubTaskParameterFactory(connectionConfig, schema);

        assertThrows(UnexpectedException.class, () -> {
            parameters.generateSubTaskParameters(connectionConfig, schema);
        });
    }

    @Test
    public void testGenerateSubTaskParameters_EmptySchema() throws Exception {
        String schema = "";
        String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 INT";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setSchema(schema);

        SubTaskParameterFactory subTaskParameterFactory =
                new SubTaskParameterFactory(connectionConfig, schema);

        assertThrows(UnexpectedException.class, () -> {
            parameters.generateSubTaskParameters(connectionConfig, schema);
        });
    }

    @Test
    public void testGenerateSubTaskParameters_NullSchema() throws Exception {
        String schema = null;
        String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 INT";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setSchema(schema);

        SubTaskParameterFactory subTaskParameterFactory =
                new SubTaskParameterFactory(connectionConfig, schema);

        assertThrows(UnexpectedException.class, () -> {
            parameters.generateSubTaskParameters(connectionConfig, schema);
        });
    }

}