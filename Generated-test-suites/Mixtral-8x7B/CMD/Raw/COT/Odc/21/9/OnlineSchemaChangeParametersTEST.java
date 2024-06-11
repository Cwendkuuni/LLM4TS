import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Test;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeParametersTest {

private static final OnlineSchemaChangeSqlType SQL_TYPE = OnlineSchemaChangeSqlType.ALTER;
private static final String SQL_CONTENT = "ALTER TABLE table_name ADD column_name datatype";
private static final TaskErrorStrategy ERROR_STRATEGY = TaskErrorStrategy.CONTINUE;
private static final Integer LOCK_TABLE_TIME_OUT_SECONDS = 300;
private static final Integer SWAP_TABLE_NAME_RETRY_TIMES = 5;
private static final OriginTableCleanStrategy ORIGIN_TABLE_CLEAN_STRATEGY = OriginTableCleanStrategy.BACKUP_AND_TRUNCATE;
private static final String DELIMITER = ",";
private static final String LOCK_USER1 = "lock_user1";
private static final String LOCK_USER2 = "lock_user2";
private static final List<String> LOCK_USERS = List.of(LOCK_USER1, LOCK_USER2);
private static final SwapTableType SWAP_TABLE_TYPE = SwapTableType.SHARDING;

@Test
public void testGettersAndSetters() {
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setSqlType(SQL_TYPE);
parameters.setSqlContent(SQL_CONTENT);
parameters.setErrorStrategy(ERROR_STRATEGY);
parameters.setLockTableTimeOutSeconds(LOCK_TABLE_TIME_OUT_SECONDS);
parameters.setSwapTableNameRetryTimes(SWAP_TABLE_NAME_RETRY_TIMES);
parameters.setOriginTableCleanStrategy(ORIGIN_TABLE_CLEAN_STRATEGY);
parameters.setDelimiter(DELIMITER);
parameters.setLockUsers(LOCK_USERS);
parameters.setSwapTableType(SWAP_TABLE_TYPE);

assertEquals(SQL_TYPE, parameters.getSqlType());
assertEquals(SQL_CONTENT, parameters.getSqlContent());
assertEquals(ERROR_STRATEGY, parameters.getErrorStrategy());
assertEquals(LOCK_TABLE_TIME_OUT_SECONDS, parameters.getLockTableTimeOutSeconds());
assertEquals(SWAP_TABLE_NAME_RETRY_TIMES, parameters.getSwapTableNameRetryTimes());
assertEquals(ORIGIN_TABLE_CLEAN_STRATEGY, parameters.getOriginTableCleanStrategy());
assertEquals(DELIMITER, parameters.getDelimiter());
assertEquals(LOCK_USERS, parameters.getLockUsers());
assertEquals(SWAP_TABLE_TYPE, parameters.getSwapTableType());
}

@Test
public void testIsContinueOnError() {
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
parameters.setErrorStrategy(ERROR_STRATEGY);
assertTrue(parameters.isContinueOnError());
}

@Test(expected = UnexpectedException.class)
public void testGenerateSubTaskParameters_Exception() throws SQLException {
ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
String schema = "mock_schema";
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
when(subTaskParameterFactory.generate(anyString(), eq(SQL_TYPE)))
.thenThrow(new SQLException("Mock SQLException"));

parameters.setSqlContent(SQL_CONTENT);

try (SubTaskParameterFactory factory = subTaskParameterFactory) {
parameters.generateSubTaskParameters(connectionConfig, schema);
}
}

@Test
public void testGenerateSubTaskParameters_HappyPath() throws SQLException {
ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
String schema = "mock_schema";
OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

parameters.setSqlContent(SQL_CONTENT);
parameters.setSqlType(SQL_TYPE);

List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTasks = new ArrayList<>();
// Adding some mocked OnlineSchemaChangeScheduleTaskParameters to expectedSubTasks

SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
when(subTaskParameterFactory.generate(anyString(), eq(SQL_TYPE)))
.thenReturn(expectedSubTasks.get(0))
.thenReturn(expectedSubTasks.get(1));

List<OnlineSchemaChangeScheduleTaskParameters> subTasks = parameters.generateSubTaskParameters(connectionConfig, schema);

assertEquals(expectedSubTasks, subTasks);
}

}