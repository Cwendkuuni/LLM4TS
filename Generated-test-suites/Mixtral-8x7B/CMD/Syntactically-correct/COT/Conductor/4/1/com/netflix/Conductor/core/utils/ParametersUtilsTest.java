import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowDef workflowDef = new WorkflowDef();
        TaskDef taskDef = new TaskDef();
        String taskId = "task1";

        when(objectMapper.writeValueAsBytes(inputParams))
                .thenReturn("{}".getBytes());
        when(objectMapper.readValue("{}".getBytes(), new TypeReference<Map<String, Object>>() {}))
                .thenReturn(inputParams);

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, new WorkflowModel(), taskDef, taskId);

        assertEquals(inputParams, result);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        String taskId = "task1";

        when(objectMapper.writeValueAsBytes(inputParams))
                .thenReturn("{}".getBytes());
        when(objectMapper.readValue("{}".getBytes(), new TypeReference<Map<String, Object>>() {}))
                .thenReturn(inputParams);

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, taskId, taskDef);

        assertEquals(inputParams, result);
    }

    @Test
    public void testClone() {
        Map<String, Object> inputParams = new HashMap<>();
        ParametersUtils parametersUtilsSpy = spy(parametersUtils);

        Map<String, Object> clone = parametersUtilsSpy.clone(inputParams);

        verify(objectMapper).writeValueAsBytes(inputParams);
        verify(objectMapper).readValue(any(), eq(new TypeReference<Map<String, Object>>() {}));
        assertEquals(inputParams, clone);
    }

    @Test
    public void testReplace() {
        Map<String, Object> inputParams = new HashMap<>();
        String json = "{\"key\": \"value\"}";

        Map<String, Object> result = parametersUtils.replace(inputParams, json);

        assertEquals(inputParams, result);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${param}";
        Map<String, Object> inputParams = new HashMap<>();
        DocumentContext documentContext = JsonPath.parse("{\"param\": \"value\"}");
        String taskId = "task1";

        when(objectMapper.writeValueAsBytes(inputParams))
                .thenReturn("{}".getBytes());
        when(objectMapper.readValue("{}".getBytes(), new TypeReference<Map<String, Object>>() {}))
                .thenReturn(inputParams);

        String result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertEquals("value", result);
    }

    @Test
    public void testReplaceVariablesWithEnvVar() {
        String paramString = "${ENV_VAR}";
        Map<String, Object> inputParams = new HashMap<>();
        DocumentContext documentContext = JsonPath.parse("{}");
        String taskId = "task1";

        when(objectMapper.writeValueAsBytes(inputParams))
                .thenReturn("{}".getBytes());
        when(objectMapper.readValue("{}".getBytes(), new TypeReference<Map<String, Object>>() {}))
                .thenReturn(inputParams);

        System.setProperty("ENV_VAR", "env_value");

        String result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertEquals("env_value", result);

        System.clearProperty("ENV_VAR");
    }

    @Test
    public void testReplaceVariablesWithNullValue() {
        String paramString = "${param}";
        Map<String, Object> inputParams = new HashMap<>();
        DocumentContext documentContext = JsonPath.parse("{\"param\": null}");
        String taskId = "task1";

        when(objectMapper.writeValueAsBytes(inputParams))
                .thenReturn("{}".getBytes());
        when(objectMapper.readValue("{}".getBytes(), new TypeReference<Map<String, Object>>() {}))
                .thenReturn(inputParams);

        String result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

        assertEquals("", result);
    }

    @Test
    public void testReplaceList() {
        List<String> values = new LinkedList<>();
        values.add("value1");
        values.add("value2");

        List<Object> replacedList = parametersUtils.replaceList(values, "task1", JsonPath.parse("{}"));

        assertEquals("value1", replacedList.get(0));
        assertEquals("value2", replacedList.get(1));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();

        when(objectMapper.writeValueAsBytes(workflowDef.getInputTemplate()))
                .thenReturn("{}".getBytes());
        when(objectMapper.readValue("{}".getBytes(), new TypeReference<Map<String, Object>>() {}))
                .thenReturn(inputParams);

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals(inputParams, result);
    }
}