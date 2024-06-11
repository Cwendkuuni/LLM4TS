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
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

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
mockSqlUtils();
mockDdlUtils();
mockSubTaskParameterFactory();
}

@Test
public void testGenerateSubTaskParameters() throws UnexpectedException {
OnlineSchemaChangeScheduleTaskParameters expectedParameter1 = new OnlineSchemaChangeScheduleTaskParameters();
OnlineSchemaChangeScheduleTaskParameters expectedParameter2 = new OnlineSchemaChangeScheduleTaskParameters();

List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTaskParameters = new ArrayList<>();
expectedSubTaskParameters.add(expectedParameter1);
expectedSubTaskParameters.add(expectedParameter2);

Mockito.when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
.thenReturn(expectedParameter1, expectedParameter2);

List<OnlineSchemaChangeScheduleTaskParameters> actualSubTaskParameters = onlineSchemaChangeParameters
.generateSubTaskParameters(connectionConfig, "schema");

assertEquals(expectedSubTaskParameters, actualSubTaskParameters);
}

private void mockSqlUtils() {
Mockito.lenient().when(SqlUtils.split(Mockito.any(), Mockito.anyString(), Mockito.anyString()))
.thenReturn(new ArrayList<>());
}

private void mockDdlUtils() {
Mockito.lenient().when(DdlUtils.replaceTableName(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(),
Mockito.any()))
.thenReturn("newAlterStmt");
}

private void mockSubTaskParameterFactory() {
Mockito.lenient().when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.any()))
.thenReturn(new OnlineSchemaChangeScheduleTaskParameters());
}

}