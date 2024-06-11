import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

	@Mock
	private ConnectionConfig connectionConfig;

	private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

	@Before
	public void setup() {
		onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
		onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
		onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT;");
		onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
		onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
		onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
		onlineSchemaChangeParameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.KEEP);
		onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
		onlineSchemaChangeParameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.INTERNAL);
	}

	@Test
	public void testIsContinueOnError() {
		assertTrue(onlineSchemaChangeParameters.isContinueOnError());
	}

	@Test
	public void testGenerateSubTaskParameters() throws Exception {
		SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "schema");
		Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
		List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
		assertEquals(1, subTaskParameters.size());
		OnlineSchemaChangeScheduleTaskParameters parameter = subTaskParameters.get(0);
		TableIdentity key = new TableIdentity("schema", "table1");
		taskParameters.putIfAbsent(key, parameter);
		assertEquals(1, taskParameters.size());
		assertEquals("ALTER TABLE table1 ADD COLUMN col1 INT;", taskParameters.get(key).getSqlsToBeExecuted().get(0));
	}

	@Test(expected = UnexpectedException.class)
	public void testGenerateSubTaskParametersException() throws Exception {
		SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "schema");
		onlineSchemaChangeParameters.setSqlContent(null);
		onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
	}

}