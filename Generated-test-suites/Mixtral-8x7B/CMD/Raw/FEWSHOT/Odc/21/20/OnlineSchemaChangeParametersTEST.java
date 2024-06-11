import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ SqlUtils.class, DdlUtils.class, SubTaskParameterFactory.class })
public class OnlineSchemaChangeParametersTest {

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

        // Set up required parameters
        params.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";

        // Set up SqlUtils.split to return a list of SQL statements
        PowerMockito.mockStatic(SqlUtils.class);
        List<String> sqls = new ArrayList<>();
        sqls.add("ALTER TABLE t1 ADD COLUMN c1 INT;");
        when(SqlUtils.split(Mockito.any(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(sqls);

        // Set up DdlUtils.replaceTableName to replace table names
        PowerMockito.mockStatic(DdlUtils.class);
        String newAlterStmt = "ALTER TABLE t2 ADD COLUMN c1 INT;";
        when(DdlUtils.replaceTableName(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.any(OnlineSchemaChangeSqlType.class))).thenReturn(newAlterStmt);

        // Set up SubTaskParameterFactory.generate to return a subtask parameter
        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        PowerMockito.whenNew(SubTaskParameterFactory.class).withAnyArguments().thenReturn(subTaskParameterFactory);
        OnlineSchemaChangeScheduleTaskParameters subTaskParam = new OnlineSchemaChangeScheduleTaskParameters();
        when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(subTaskParam);

        // Test the generateSubTaskParameters method
        List<OnlineSchemaChangeScheduleTaskParameters> result = params.generateSubTaskParameters(connectionConfig,
                schema);

        // Assert that the result contains the expected subtask parameter
        assertEquals(1, result.size());
        assertEquals(subTaskParam, result.get(0));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws Exception {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

        // Set up required parameters
        params.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";

        // Set up SqlUtils.split to throw an exception
        PowerMockito.mockStatic(SqlUtils.class);
        when(SqlUtils.split(Mockito.any(), Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new Exception("Mock exception"));

        // Test the generateSubTaskParameters method
        params.generateSubTaskParameters(connectionConfig, schema);
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());

        params.setErrorStrategy(null);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGetDelimiter() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertEquals(";", params.getDelimiter());

        params.setDelimiter("//");
        assertEquals("//", params.getDelimiter());
    }

    @Test
    public void testGetLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertEquals(null, params.getLockTableTimeOutSeconds());

        params.setLockTableTimeOutSeconds(10);
        assertEquals(Integer.valueOf(10), params.getLockTableTimeOutSeconds());
    }

    @Test
    public void testGetSwapTableNameRetryTimes() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertEquals(null, params.getSwapTableNameRetryTimes());

        params.setSwapTableNameRetryTimes(3);
        assertEquals(Integer.valueOf(3), params.getSwapTableNameRetryTimes());
    }

    @Test
    public void testGetOriginTableCleanStrategy() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertEquals(null, params.getOriginTableCleanStrategy());

        params.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP,
                params.getOriginTableCleanStrategy());
    }

    @Test
    public void testGetLockUsers() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertEquals(null, params.getLockUsers());

        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");
        params.setLockUsers(lockUsers);
        assertEquals(lockUsers, params.getLockUsers());
    }

    @Test
    public void testGetSwapTableType() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertEquals(null, params.getSwapTableType());

        params.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY);
        assertEquals(OnlineSchemaChangeParameters.SwapTableType.TEMPORARY, params.getSwapTableType());
    }

    @Test
    public void testSerializable() throws Exception {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

        // Set up required parameters
        params.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(params);
        oos.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        OnlineSchemaChangeParameters deserializedParams = (OnlineSchemaChangeParameters) ois.readObject();
        ois.close();

        assertEquals(params.getSqlContent(), deserializedParams.getSqlContent());
        assertEquals(params.getSqlType(), deserializedParams.getSqlType());
    }

    @Test
    public void testHashCodeAndEquals() {
        OnlineSchemaChangeParameters params1 = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeParameters params2 = new OnlineSchemaChangeParameters();

        // Set up required parameters
        params1.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 INT;");
        params1.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        params2.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 INT;");
        params2.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        // Test hashCode and equals methods
        assertEquals(params1.hashCode(), params2.hashCode());
        assertEquals(params1, params2);

        params2.setSqlContent("ALTER TABLE t1 ADD COLUMN c2 INT;");
        assertNotEquals(params1, params2);

        params2.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 INT;");
        params2.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        assertNotEquals(params1, params2);
    }
}