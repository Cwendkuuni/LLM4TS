import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@RunWith(JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testExtractAndListPublicMethods() {
        // List all public methods
        Method[] methods = OnlineSchemaChangeParameters.class.getDeclaredMethods();
        List<String> methodSignatures = new ArrayList<>();
        for (Method method : methods) {
            methodSignatures.add(method.getName() + " " + method.getParameterTypes()[0].getName());
        }

        // Expected method signatures
        List<String> expectedMethodSignatures = new ArrayList<>();
        expectedMethodSignatures.add("boolean isContinueOnError()");
        expectedMethodSignatures.add("List<OnlineSchemaChangeScheduleTaskParameters> generateSubTaskParameters(ConnectionConfig, String)");

        // Verify method signatures
        assertEquals(expectedMethodSignatures, methodSignatures);
    }

    @Test
    public void testIsContinueOnError() {
        // Test basic case
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        // Test edge case
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ROLLBACK);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Test typical case
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, schema);

        // Verify the output
        assertNotNull(subTaskParameters);
        assertEquals(0, subTaskParameters.size());

        // Test exception handling
        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(null, schema);
            fail("Expected exception due to null ConnectionConfig");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSerialization() {
        // Serialize the object
        OnlineSchemaChangeParameters serializedOnlineSchemaChangeParameters = OnlineSchemaChangeParametersCopier
                .deepCopy(onlineSchemaChangeParameters);

        // Verify that the object can be deserialized and its state is unchanged
        assertEquals(onlineSchemaChangeParameters, serializedOnlineSchemaChangeParameters);
    }
}

class OnlineSchemaChangeParametersCopier implements Serializable {

    private static final long serialVersionUID = 1L;

    public static OnlineSchemaChangeParameters deepCopy(OnlineSchemaChangeParameters onlineSchemaChangeParameters) {
        // Implement serialization-based deep copy
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(onlineSchemaChangeParameters);

            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                return (OnlineSchemaChangeParameters) objectInputStream.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to deep copy OnlineSchemaChangeParameters", e);
        }
    }
}