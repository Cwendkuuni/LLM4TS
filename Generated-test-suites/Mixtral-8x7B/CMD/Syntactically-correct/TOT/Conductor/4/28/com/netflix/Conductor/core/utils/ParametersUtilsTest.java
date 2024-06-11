import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TestUtils;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private EnvUtils envUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        objectMapper = new ObjectMapper();
        envUtils = mock(EnvUtils.class);

        parametersUtils = new ParametersUtils(objectMapper);
        ReflectionTestUtils.setField(parametersUtils, "envUtils", envUtils);
    }

    @Test
    public void testGetTaskInputV1() throws IOException {
        // Setup
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.param1", "value1");
        inputParams.put("task.input.param2", "value2");

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Collections.singletonMap("param3", "value3"));

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(workflowDef);
        workflowModel.setInput(Collections.singletonMap("param1", "workflowValue1"));

        TaskModel task = new TaskModel();
        task.setInputData(Collections.singletonMap("param2", "taskValue2"));
        workflowModel.addTask(task);

        when(envUtils.isEnvironmentVariable("envVar1")).thenReturn(true);
        when(envUtils.getSystemParametersValue("envVar1", "task1"))
                .thenReturn("envVarValue1");

        // Invoke
        Map<String, Object> result =
                parametersUtils.getTaskInputV1(workflowModel, inputParams, taskDef, "task1");

        // Analyze
        assertEquals("value1", result.get("param1"));
        assertEquals("taskValue2", result.get("param2"));
        assertEquals("value3", result.get("param3"));
        assertEquals("envVarValue1", result.get("envVar1"));
    }

    @Test
    public void testGetTaskInputV2() throws IOException {
        // Setup
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        workflowDef.setSchemaVersion(2);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("$.input.param1", "value1");
        inputParams.put("$.tasks[0].input.param2", "value2");

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Collections.singletonMap("param3", "value3"));

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(workflowDef);
        workflowModel.setInput(Collections.singletonMap("param1", "workflowValue1"));

        TaskModel task = new TaskModel();
        task.setInputData(Collections.singletonMap("param2", "taskValue2"));
        workflowModel.addTask(task);

        when(envUtils.isEnvironmentVariable("envVar1")).thenReturn(true);
        when(envUtils.getSystemParametersValue("envVar1", "task1"))
                .thenReturn("envVarValue1");

        // Invoke
        Map<String, Object> result =
                parametersUtils.getTaskInputV2(inputParams, workflowModel, "task1", taskDef);

        // Analyze
        assertEquals("value1", result.get("param1"));
        assertEquals("taskValue2", result.get("param2"));
        assertEquals("value3", result.get("param3"));
        assertEquals("envVarValue1", result.get("envVar1"));
    }

    @Test
    public void testReplace() throws IOException {
        // Setup
        String json =
                "{\n"
                        + "  \"input\": {\n"
                        + "    \"param1\": \"${workflow.param1}\",\n"
                        + "    \"param2\": \"${task.input.param2}\"\n"
                        + "  },\n"
                        + "  \"envVar\": \"${envVar1}\"\n"
                        + "}";

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.param1", "workflowValue1");
        inputParams.put("task.input.param2", "taskValue2");

        when(envUtils.isEnvironmentVariable("envVar1")).thenReturn(true);
        when(envUtils.getSystemParametersValue("envVar1", "task1"))
                .thenReturn("envVarValue1");

        // Invoke
        Map<String, Object> result =
                parametersUtils.replace(
                        json, objectMapper.readTree(json), "task1", new ParametersUtils.DocumentContext());

        // Analyze
        assertEquals(
                "{\n"
                        + "  \"input\": {\n"
                        + "    \"param1\": \"workflowValue1\",\n"
                        + "    \"param2\": \"taskValue2\"\n"
                        + "  },\n"
                        + "  \"envVar\": \"envVarValue1\"\n"
                        + "}",
                result.toString());
    }

    @Test
    public void testReplaceVariables() throws IOException {
        // Setup
        String paramString = "${workflow.param1}.${task.input.param2}.${envVar1}";

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.param1", "workflowValue1");
        inputParams.put("task.input.param2", "taskValue2");

        when(envUtils.isEnvironmentVariable("envVar1")).thenReturn(true);
        when(envUtils.getSystemParametersValue("envVar1", "task1"))
                .thenReturn("envVarValue1");

        // Invoke
        String result =
                parametersUtils.replaceVariables(
                        paramString, objectMapper.readTree(paramString), "task1");

        // Analyze
        assertEquals("workflowValue1.taskValue2.envVarValue1", result);
    }

    @Test
    public void testGetWorkflowInput() throws IOException {
        // Setup
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        workflowDef.setInputTemplate(Collections.singletonMap("param1", "workflowInputValue1"));

        Map<String, Object> inputParams = new HashMap<>();

        // Invoke
        parametersUtils.getWorkflowInput(workflowDef, inputParams);

        // Analyze
        assertEquals("workflowInputValue1", inputParams.get("param1"));
    }

    // Add more test cases for different scenarios, edge cases, and error scenarios
}