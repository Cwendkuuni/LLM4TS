import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory.SubTaskParameterFactoryException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SqlUtils.class, DdlUtils.class, SubTaskParameterFactory.class})
public class OnlineSchemaChangeParametersTest {

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 int;");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        SqlUtils sqlUtils = Mockito.mock(SqlUtils.class);
        PowerMockito.mockStatic(SqlUtils.class);
        when(SqlUtils.split(connectionConfig.getDialectType(), onlineSchemaChangeParameters.getSqlContent(),
                onlineSchemaChangeParameters.getDelimiter())).thenReturn(
                new ArrayList<String>() {{
                    add("ALTER TABLE table1 ADD COLUMN c1 int;");
                }});
        PowerMockito.when(SqlUtils.class, "getSqlUtilsInstance").thenReturn(sqlUtils);

        DdlUtils ddlUtils = Mockito.mock(DdlUtils.class);
        PowerMockito.mockStatic(DdlUtils.class);
        when(DdlUtils.replaceTableName(onlineSchemaChangeParameters.getSqlContent(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString())).thenReturn("REPLACED TABLE NAME");
        PowerMockito.when(DdlUtils.class, "getDdlUtilsInstance").thenReturn(ddlUtils);

        SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
        PowerMockito.mockStatic(SubTaskParameterFactory.class);
        when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any())).thenReturn(
                new OnlineSchemaChangeScheduleTaskParameters());
        whenNew(SubTaskParameterFactory.class).withParameterTypes(ConnectionConfig.class, String.class)
                .withArguments(connectionConfig, "schema").thenReturn(subTaskParameterFactory);

        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(1, result.size());
    }

    @Test
    public void testGenerateSubTaskParametersWithException() throws Exception {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN c1 int;");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        SqlUtils sqlUtils = Mockito.mock(SqlUtils.class);
        PowerMockito.mockStatic(SqlUtils.class);
        when(SqlUtils.split(connectionConfig.getDialectType(), onlineSchemaChangeParameters.getSqlContent(),
                onlineSchemaChangeParameters.getDelimiter())).thenReturn(
                new ArrayList<String>() {{
                    add("ALTER TABLE table1 ADD COLUMN c1 int;");
                }});
        PowerMockito.when(SqlUtils.class, "getSqlUtilsInstance").thenReturn(sqlUtils);

        DdlUtils ddlUtils = Mockito.mock(DdlUtils.class);
        PowerMockito.mockStatic(DdlUtils.class);
        when(DdlUtils.replaceTableName(onlineSchemaChangeParameters.getSqlContent(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString())).thenThrow(new Exception("test exception"));
        PowerMockito.when(DdlUtils.class, "getDdlUtilsInstance").thenReturn(ddlUtils);

        SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
        PowerMockito.mockStatic(SubTaskParameterFactory.class);
        when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any())).thenThrow(
                new SubTaskParameterFactoryException("test exception"));
        whenNew(SubTaskParameterFactory.class).withParameterTypes(ConnectionConfig.class, String.class)
                .withArguments(connectionConfig, "schema").thenReturn(subTaskParameterFactory);

        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
        } catch (UnexpectedException e) {
            assertEquals("test exception", e.getMessage());
        }
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(true, onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertEquals(false, onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGetTableIdentity() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setDatabaseName("db");
        taskParameters.setOriginTableName("table1");
        TableIdentity tableIdentity = new TableIdentity(taskParameters.getDatabaseName(),
                taskParameters.getOriginTableName());
        onlineSchemaChangeParameters.setTaskParameters(taskParameters);

        assertEquals(tableIdentity.getDatabaseName(),
                onlineSchemaChangeParameters.getTableIdentity().getDatabaseName());
        assertEquals(tableIdentity.getTableName(), onlineSchemaChangeParameters.getTableIdentity().getTableName());
    }
}