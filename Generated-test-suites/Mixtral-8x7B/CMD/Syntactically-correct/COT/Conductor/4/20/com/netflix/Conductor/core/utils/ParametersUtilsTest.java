import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.utils.EnvUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.core.utils.TaskUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private Configuration configuration;
    private EnvUtils envUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        configuration = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        envUtils = mock(EnvUtils.class);
        parametersUtils = new ParametersUtils(objectMapper) {
            @Override
            EnvUtils getEnvUtils() {
                return envUtils;
            }
        };
    }

    @Test
    public void testGetTaskInput() throws IOException {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.inputKey", "$${task.input.taskInputKey}");
        when(envUtils.getSystemParametersValue("task.input.taskInputKey", "taskId"))
                .thenReturn("taskInputValue");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        Assert.assertEquals("inputValue", result.get("workflow.input.inputKey"));
        Assert.assertEquals("taskInputValue", result.get("task.input.taskInputKey"));
    }

    @Test
    public void testReplace() throws IOException {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.inputKey", "$${task.input.taskInputKey}");
        when(envUtils.getSystemParametersValue("task.input.taskInputKey", "taskId"))
                .thenReturn("taskInputValue");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        Assert.assertEquals("inputValue", result.get("workflow.input.inputKey"));
        Assert.assertEquals("taskInputValue", result.get("task.input.taskInputKey"));
    }

    @Test
    public void testReplaceVariables() throws IOException {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.inputKey", "$${workflow.input.inputKey}");

        Map<String, Object> result = parametersUtils.replace(inputParams, workflowModel);

        Assert.assertEquals("inputValue", result.get("workflow.input.inputKey"));
    }

    @Test
    public void testReplaceVariablesWithJson() throws IOException {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input", "{\"inputKey\": \"$${workflow.input.inputKey}\"}");

        Map<String, Object> result = parametersUtils.replace(inputParams, workflowModel);

        Assert.assertEquals("inputValue", ((Map<String, Object>) result.get("workflow")).get("inputKey"));
    }

    @Test
    public void testReplaceVariablesWithJsonAndTask() throws IOException {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input", "{\"inputKey\": \"$${task.input.taskInputKey}\"}");
        when(envUtils.getSystemParametersValue("task.input.taskInputKey", "taskId"))
                .thenReturn("taskInputValue");

        Map<String, Object> result = parametersUtils.replace(inputParams, workflowModel);

        Assert.assertEquals("taskInputValue", ((Map<String, Object>) result.get("workflow")).get("inputKey"));
    }

    @Test
    public void testReplaceVariablesWithJsonAndTaskAndException() throws IOException {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input", "{\"inputKey\": \"$${task.input.taskInputKey}\"}");

        Assert.assertThrows(
                IOException.class,
                () -> parametersUtils.replace(inputParams, workflowModel, "taskId"));
    }

    @Test
    public void testReplaceVariablesWithException() throws IOException {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input", "{\"inputKey\": \"$${exception}\"}");

        Assert.assertThrows(
                IOException.class,
                () -> parametersUtils.replace(inputParams, workflowModel));
    }

    @Test
    public void testReplaceList() throws IOException {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
        TaskDef taskDef = new TaskDef();
        List<Object> inputParams = new LinkedList<>();
        inputParams.add("workflow.input.inputKey");
        inputParams.add("$${task.input.taskInputKey}");
        when(envUtils.getSystemParametersValue("task.input.taskInputKey", "taskId"))
                .thenReturn("taskInputValue");

        List<Object> result = parametersUtils.replaceList(inputParams, "taskId", workflowModel);

        Assert.assertEquals("inputValue", ((Map<String, Object>) result.get(0)).get("inputKey"));
        Assert.assertEquals("taskInputValue", ((Map<String, Object>) result.get(1)).get("inputKey"));
    }

    @Test
    public void testGetWorkflowInput() throws IOException {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Collections.singletonMap("inputKey", "inputValue"));

        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> result =
                parametersUtils.getWorkflowInput(workflowDef, inputParams);

        Assert.assertEquals("inputValue", result.get("inputKey"));
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Collections.singletonMap("taskInputKey", "taskInputValue"));
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.inputKey", "$${task.input.taskInputKey}");

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);

        Assert.assertEquals("inputValue", result.get("workflow.input.inputKey"));
        Assert.assertEquals("taskInputValue", result.get("task.input.taskInputKey"));
    }

    @Test
    public void testDeepCloneUsingJson() throws IOException {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Collections.singletonMap("inputKey", "inputValue"));

        Map<String, Object> result = parametersUtils.clone(workflowDef.getInputTemplate());

        Assert.assertNotNull(result);
        Assert.assertEquals("inputValue", result.get("inputKey"));
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Collections.singletonMap("taskInputKey", "taskInputValue"));
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.inputKey", "$${task.input.taskInputKey}");

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);

        Assert.assertEquals("inputValue", result.get("workflow.input.inputKey"));
        Assert.assertEquals("taskInputValue", result.get("task.input.taskInputKey"));
    }

    @Test
    public void testReplaceListWithEmptyTaskId() {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
        TaskDef taskDef = new TaskDef();
        List<Object> inputParams = new LinkedList<>();
        inputParams.add("workflow.input.inputKey");
        inputParams.add("$${task.input.taskInputKey}");

        List<Object> result = parametersUtils.replaceList(inputParams, "", workflowModel);

        Assert.assertEquals("inputValue", ((Map<String, Object>) result.get(0)).get("inputKey"));
        Assert.assertEquals(null, result.get(1));
    }
}