import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

	@Mock
	private ConnectionConfig connectionConfig;

	@Mock
	private SubTaskParameterFactory subTaskParameterFactory;

	@InjectMocks
	private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGenerateSubTaskParametersWithAlterSql() {
		OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
		String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 int";
		String delimiter = ";";
		String databaseName = "db1";
		String originTableName = "table1";
		String newTableName = "table2";
		List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, delimiter);
		OnlineSchemaChangeScheduleTaskParameters parameter = new OnlineSchemaChangeScheduleTaskParameters();
		parameter.setDatabaseName(databaseName);
		parameter.setOriginTableName(originTableName);
		parameter.setNewTableName(newTableName);
		parameter.setSqlsToBeExecuted(sqls);

		Mockito.when(connectionConfig.getDialectType()).thenReturn("mysql");
		Mockito.when(subTaskParameterFactory.generate(sqls.get(0), sqlType)).thenReturn(parameter);
		Mockito.when(DdlUtils.replaceTableName(sqls.get(0), newTableName, "mysql", sqlType)).thenReturn(
				"ALTER TABLE table2 ADD COLUMN col1 int");

		List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
				.generateSubTaskParameters(connectionConfig, "db1");

		assertEquals(subTaskParameters.size(), 1);
		assertEquals(subTaskParameters.get(0).getDatabaseName(), databaseName);
		assertEquals(subTaskParameters.get(0).getOriginTableName(), originTableName);
		assertEquals(subTaskParameters.get(0).getNewTableName(), newTableName);
		assertEquals(subTaskParameters.get(0).getSqlsToBeExecuted().size(), 2);
		assertTrue(subTaskParameters.get(0).getSqlsToBeExecuted().contains(sqls.get(0)));
		assertTrue(subTaskParameters.get(0).getSqlsToBeExecuted().contains("ALTER TABLE table2 ADD COLUMN col1 int"));
	}

	@Test
	public void testGenerateSubTaskParametersWithCreateIndexSql() {
		OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.CREATE_INDEX;
		String sqlContent = "CREATE INDEX idx1 ON table1 (col1)";
		String delimiter = ";";
		String databaseName = "db1";
		String originTableName = "table1";
		List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, delimiter);
		OnlineSchemaChangeScheduleTaskParameters parameter = new OnlineSchemaChangeScheduleTaskParameters();
		parameter.setDatabaseName(databaseName);
		parameter.setOriginTableName(originTableName);
		parameter.setSqlsToBeExecuted(sqls);

		Mockito.when(connectionConfig.getDialectType()).thenReturn("mysql");
		Mockito.when(subTaskParameterFactory.generate(sqls.get(0), sqlType)).thenReturn(parameter);

		List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
				.generateSubTaskParameters(connectionConfig, "db1");

		assertEquals(subTaskParameters.size(), 1);
		assertEquals(subTaskParameters.get(0).getDatabaseName(), databaseName);
		assertEquals(subTaskParameters.get(0).getOriginTableName(), originTableName);
		assertEquals(subTaskParameters.get(0).getSqlsToBeExecuted().size(), 1);
		assertTrue(subTaskParameters.get(0).getSqlsToBeExecuted().contains(sqls.get(0)));
	}

	@Test(expected = UnexpectedException.class)
	public void testGenerateSubTaskParametersWithNullSqlType() {
		OnlineSchemaChangeSqlType sqlType = null;
		String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 int";
		String delimiter = ";";

		onlineSchemaChangeParameters.setSqlType(sqlType);
		onlineSchemaChangeParameters.setSqlContent(sqlContent);
		onlineSchemaChangeParameters.setDelimiter(delimiter);

		onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "db1");
	}

	@Test(expected = UnexpectedException.class)
	public void testGenerateSubTaskParametersWithEmptySqlContent() {
		OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
		String sqlContent = "";
		String delimiter = ";";

		onlineSchemaChangeParameters.setSqlType(sqlType);
		onlineSchemaChangeParameters.setSqlContent(sqlContent);
		onlineSchemaChangeParameters.setDelimiter(delimiter);

		onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "db1");
	}

	@Test(expected = UnexpectedException.class)
	public void testGenerateSubTaskParametersWithNullDelimiter() {
		OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
		String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 int";
		String delimiter = null;

		onlineSchemaChangeParameters.setSqlType(sqlType);
		onlineSchemaChangeParameters.setSqlContent(sqlContent);
		onlineSchemaChangeParameters.setDelimiter(delimiter);

		onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "db1");
	}

	@Test(expected = UnexpectedException.class)
	public void testGenerateSubTaskParametersWithEmptyDelimiter() {
		OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
		String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 int";
		String delimiter = "";

		onlineSchemaChangeParameters.setSqlType(sqlType);
		onlineSchemaChangeParameters.setSqlContent(sqlContent);
		onlineSchemaChangeParameters.setDelimiter(delimiter);

		onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "db1");
	}
}