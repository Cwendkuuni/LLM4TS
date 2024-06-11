import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

	@Mock
	private SubTaskParameterFactory subTaskParameterFactory;

	@InjectMocks
	private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

	@Before
	public void setUp() throws Exception {
		onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
	}

	@Test
	public void testGetSqlType() {
		OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
		onlineSchemaChangeParameters.setSqlType(sqlType);
		assertEquals(sqlType, onlineSchemaChangeParameters.getSqlType());
	}

	@Test
	public void testGenerateSubTaskParameters_happyPath() {
		String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 INT;";
		OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
		onlineSchemaChangeParameters.setSqlContent(sqlContent);
		onlineSchemaChangeParameters.setSqlType(sqlType);
		List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
		assertEquals(expected, onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema"));
	}

	@Test(expected = UnexpectedException.class)
	public void testGenerateSubTaskParameters_exception() throws Exception {
		String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 INT;";
		OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
		onlineSchemaChangeParameters.setSqlContent(sqlContent);
		onlineSchemaChangeParameters.setSqlType(sqlType);
		when(subTaskParameterFactory.generate(anyString(), any())).thenThrow(new Exception());
		onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
	}

}