import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testPublicMethodsSignatures() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

        // Check method signatures
        Serializable serializable = params;
        TaskParameters taskParameters = params;
        OnlineSchemaChangeSqlType sqlType = params.getSqlType();
        String sqlContent = params.getSqlContent();
        TaskErrorStrategy errorStrategy = params.getErrorStrategy();
        Integer lockTableTimeOutSeconds = params.getLockTableTimeOutSeconds();
        Integer swapTableNameRetryTimes = params.getSwapTableNameRetryTimes();
        OriginTableCleanStrategy originTableCleanStrategy = params.getOriginTableCleanStrategy();
        String delimiter = params.getDelimiter();
        List<String> lockUsers = params.getLockUsers();
        SwapTableType swapTableType = params.getSwapTableType();
        boolean isContinueOnError = params.isContinueOnError();
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters =
                params.generateSubTaskParameters(new ConnectionConfig(), "schema");
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        Assert.assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 int; ALTER TABLE t1 ADD COLUMN c2 int;");
        params.setDelimiter(";");
        ConnectionConfig connectionConfig = new ConnectionConfig();

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters;
        try {
            subTaskParameters = params.generateSubTaskParameters(connectionConfig, "schema");
            Assert.assertFalse(subTaskParameters.isEmpty());
            Assert.assertEquals(2, subTaskParameters.size());
        } catch (UnexpectedException e) {
            Assert.fail("generateSubTaskParameters() should not throw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testEdgeCasesAndExceptions() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

        // Empty sqlContent
        params.setSqlContent("");
        try {
            params.generateSubTaskParameters(new ConnectionConfig(), "schema");
            Assert.fail("Expected UnexpectedException was not thrown");
        } catch (UnexpectedException e) {
            Assert.assertEquals("Failed to generate subtasks with parameter: OnlineSchemaChangeParameters(sqlType=null, "
                    + "sqlContent=, errorStrategy=CONTINUE, lockTableTimeOutSeconds=null, swapTableNameRetryTimes=null, "
                    + "originTableCleanStrategy=CLEAN_AFTER_SWAP, delimiter=;, lockUsers=[], swapTableType=NOT_SPECIFIED)",
                    e.getMessage());
        }

        // Null sqlType
        params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 int;");
        try {
            params.generateSubTaskParameters(new ConnectionConfig(), "schema");
            Assert.fail("Expected UnexpectedException was not thrown");
        } catch (UnexpectedException e) {
            Assert.assertEquals("sqlType is null. Please set a valid value.", e.getMessage());
        }

        // Invalid sqlType
        params.setSqlType(null);
        try {
            params.generateSubTaskParameters(new ConnectionConfig(), "schema");
            Assert.fail("Expected UnexpectedException was not thrown");
        } catch (UnexpectedException e) {
            Assert.assertEquals("sqlType is null. Please set a valid value.", e.getMessage());
        }

        // Null delimiter
        params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 int;");
        params.setDelimiter(null);
        try {
            params.generateSubTaskParameters(new ConnectionConfig(), "schema");
            Assert.fail("Expected UnexpectedException was not thrown");
        } catch (UnexpectedException e) {
            Assert.assertEquals("delimiter is null. Please set a valid value.", e.getMessage());
        }

        // Empty delimiter
        params.setDelimiter("");
        try {
            params.generateSubTaskParameters(new ConnectionConfig(), "schema");
            Assert.fail("Expected UnexpectedException was not thrown");
        } catch (UnexpectedException e) {
            Assert.assertEquals("delimiter is empty. Please set a valid value.", e.getMessage());
        }

        // Invalid delimiter
        params.setDelimiter("|");
        params.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 int;ALTER TABLE t1 ADD COLUMN c2 int;");
        try {
            params.generateSubTaskParameters(new ConnectionConfig(), "schema");
            Assert.fail("Expected UnexpectedException was not thrown");
        } catch (UnexpectedException e) {
            Assert.assertEquals("The delimiter '|' is not present in the sqlContent. Please set a valid delimiter.",
                    e.getMessage());
        }
    }
}