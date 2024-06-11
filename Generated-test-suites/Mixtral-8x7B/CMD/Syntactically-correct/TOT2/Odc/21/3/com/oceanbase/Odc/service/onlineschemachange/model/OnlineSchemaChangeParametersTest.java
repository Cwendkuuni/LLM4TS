import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

private OnlineSchemaChangeParameters createOnlineSchemaChangeParameters() {
ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
when(connectionConfig.getDialectType()).thenReturn("h2");

OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
parameters.setSqlContent("ALTER TABLE table_name ADD column_name varchar(255)");
parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
parameters.setLockTableTimeOutSeconds(1800);
parameters.setSwapTableNameRetryTimes(3);
parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
parameters.setDelimiter(";");
parameters.setLockUsers(new ArrayList<>());
parameters.setSwapTableType(SwapTableType.GENERAL);

return parameters;
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters parameters = createOnlineSchemaChangeParameters();
assertEquals(true, parameters.isContinueOnError());
}

@Test
public void testGenerateSubTaskParameters() throws UnexpectedException {
OnlineSchemaChangeParameters parameters = createOnlineSchemaChangeParameters();
ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
when(connectionConfig.getDialectType()).thenReturn("h2");
String schema = "test_schema";

List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

// Assert that the subTaskParameters list is not empty
assertEquals(true, !subTaskParameters.isEmpty());

// Add additional assertions based on the expected content of subTaskParameters
}
}