import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TestUtils;
import com.netflix.conductor.model.TaskModel;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EnvUtils envUtils;

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        parametersUtils = spy(new ParametersUtils(objectMapper));
        when(parametersUtils.getEnvUtils()).thenReturn(envUtils);
    }

    @Test
    public void testGetTaskInput() throws Exception {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = createWorkflowModel(workflowDef);

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(createInputTemplate());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.param1", "value1");
        inputParams.put("task:task1.input.param2", "value2");
        inputParams.put("task:task2.output.param3", "value3");

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "task1");

        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("param2"));

        taskDef = new TaskDef();
        result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "task2");

        assertEquals("value1", result.get("param1"));
        assertEquals("value3", result.get("param3"));

        taskDef = new TaskDef();
        result = parametersUtils.getTaskInput(null, workflowModel, taskDef, "task1");

        assertEquals(0, result.size());
    }

    @Test
    public void testGetTaskInputV2() throws Exception {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = createWorkflowModel(workflowDef);

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(createInputTemplate());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.param1", "value1");
        inputParams.put("task:task1.input.param2", "value2");
        inputParams.put("task:task2.output.param3", "value3");

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "task1", taskDef);

        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("param2"));

        result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "task2", taskDef);

        assertEquals("value1", result.get("param1"));
        assertEquals("value3", result.get("param3"));

        result = parametersUtils.getTaskInputV2(null, workflowModel, "task1", taskDef);

        assertEquals(0, result.size());
    }

    @Test
    public void testReplace() throws Exception {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = createWorkflowModel(workflowDef);

        Map<String, Object> input = new HashMap<>();
        input.put("param1", "${workflow.input.param1}");
        input.put("param2", "${task:task1.input.param2}");
        input.put("param3", "${task:task2.output.param3}");
        input.put("param4", "${nonExistent}");

        doReturn(workflowModel)
                .when(parametersUtils)
                .getWorkflow(anyString(), anyString(), anyString());

        Map<String, Object> result = parametersUtils.replace(input, objectMapper.writeValueAsString(workflowModel));

        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("param2"));
        assertEquals("value3", result.get("param3"));
        assertEquals("", result.get("param4"));
    }

    @Test
    public void testReplaceVariables() throws Exception {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = createWorkflowModel(workflowDef);

        String input = "${workflow.input.param1}.${task:task1.input.param2}.${task:task2.output.param3}."
                + "${nonExistent}";

        doReturn(workflowModel)
                .when(parametersUtils)
                .getWorkflow(anyString(), anyString(), anyString());

        String result = parametersUtils.replaceVariables(input, workflowModel);

        String expected = "value1.value2.value3.";
        assertEquals(expected, result);
    }

    @Test
    public void testReplaceVariablesWithEnvVar() throws Exception {
        String input = "${ENV_VAR_1}.${ENV_VAR_2}";

        when(envUtils.isEnvironmentVariable(anyString()))
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);

        when(envUtils.getSystemParametersValue(anyString(), anyString()))
                .thenReturn("envValue1")
                .thenReturn("envValue2");

        String result = parametersUtils.replaceVariables(input, mock(DocumentContext.class));

        String expected = "envValue1.envValue2";
        assertEquals(expected, result);
    }

    @Test
    public void testReplaceVariablesNonExistentEnvVar() throws Exception {
        String input = "${ENV_VAR_1}.${ENV_VAR_2}";

        when(envUtils.isEnvironmentVariable(anyString()))
                .thenReturn(false)
                .thenReturn(false);

        String result = parametersUtils.replaceVariables(input, mock(DocumentContext.class));

        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testReplaceVariablesWithJsonPath() throws Exception {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = createWorkflowModel(workflowDef);

        String input = "${workflow.input.param1}.${task:task1.input.param2}.${task:task2.output.param3}."
                + "${task:nonExistent}";

        doReturn(workflowModel)
                .when(parametersUtils)
                .getWorkflow(anyString(), anyString(), anyString());

        String result = parametersUtils.replaceVariables(input, workflowModel);

        String expected = "value1.value2.value3.";
        assertEquals(expected, result);
    }

    @Test
    public void testReplaceVariablesWithJsonPathAndEnvVar() throws Exception {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = createWorkflowModel(workflowDef);

        String input = "${ENV_VAR_1}.${workflow.input.param1}.${task:task1.input.param2}.${task:task2.output.param3}."
                + "${nonExistent}.${ENV_VAR_2}";

        when(envUtils.isEnvironmentVariable(anyString()))
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false)
                .thenReturn(true);

        when(envUtils.getSystemParametersValue(anyString(), anyString()))
                .thenReturn("envValue1")
                .thenReturn("envValue2");

        doReturn(workflowModel)
                .when(parametersUtils)
                .getWorkflow(anyString(), anyString(), anyString());

        String result = parametersUtils.replaceVariables(input, workflowModel);

        String expected = "envValue1.value1.value2.value3.envValue2";
        assertEquals(expected, result);
    }

    @Test
    public void testReplaceVariablesWithJsonPathAndNonExistentEnvVar() throws Exception {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = createWorkflowModel(workflowDef);

        String input = "${ENV_VAR_1}.${workflow.input.param1}.${task:task1.input.param2}.${task:task2.output.param3}."
                + "${nonExistent}.${ENV_VAR_2}";

        when(envUtils.isEnvironmentVariable(anyString()))
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false)
                .thenReturn(false);

        doReturn(workflowModel)
                .when(parametersUtils)
                .getWorkflow(anyString(), anyString(), anyString());

        String result = parametersUtils.replaceVariables(input, workflowModel);

        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testReplaceVariablesWithJsonPathAndEnvVarAndEmpty() throws Exception {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = createWorkflowModel(workflowDef);

        String input = "${ENV_VAR_1}.${workflow.input.param1}.${task:task1.input.param2}.${task:task2.output.param3}."
                + "${}.${ENV_VAR_2}";

        when(envUtils.isEnvironmentVariable(anyString()))
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false)
                .thenReturn(true);

        when(envUtils.getSystemParametersValue(anyString(), anyString()))
                .thenReturn("envValue1")
                .thenReturn("envValue2");

        doReturn(workflowModel)
                .when(parametersUtils)
                .getWorkflow(anyString(), anyString(), anyString());

        String result = parametersUtils.replaceVariables(input, workflowModel);

        String expected = "envValue1.value1.value2.value3.";
        assertEquals(expected, result);
    }

    @Test
    public void testReplaceVariablesWithJsonPathAndEnvVarAndEmptyInTheBeginning()
            throws Exception {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = createWorkflowModel(workflowDef);

        String input = "${}.${workflow.input.param1}.${task:task1.input.param2}.${task:task2.output.param3}."
                + "${ENV_VAR_1}.${ENV_VAR_2}";

        when(envUtils.isEnvironmentVariable(anyString()))
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false)
                .thenReturn(true);

        when(envUtils.getSystemParametersValue(anyString(), anyString()))
                .thenReturn("envValue1")
                .thenReturn("envValue2");

        doReturn(workflowModel)
                .when(parametersUtils)
                .getWorkflow(anyString(), anyString(), anyString());

        String result = parametersUtils.replaceVariables(input, workflowModel);

        String expected = "envValue1.value1.value2.value3.envValue1.envValue2";
        assertEquals(expected, result);
    }

    @Test
    public void testReplaceVariablesWithJsonPathAndEnvVarAndEmptyInTheEnd()
            throws Exception {
        WorkflowDef workflowDef = TestUtils.createWorkflowDef();
        WorkflowModel workflowModel = createWorkflowModel(workflowDef);

        String input = "${workflow.input.param1}.${task:task1.input.param2}.${task:task2.output.param3}."
                + "${ENV_VAR_1}.${ENV_VAR_2}.${}";

        when(envUtils.isEnvironmentVariable(anyString()))
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false)
                .thenReturn(true);

        when(envUtils.getSystemParametersValue(anyString(), anyString()))
                .thenReturn("envValue1")
                .thenReturn("envValue2");

        doReturn(workflowModel)
                .when(parametersUtils)
                .getWorkflow(anyString(), anyString(), anyString());

        String result = parametersUtils.replaceVariables(input, workflowModel);

        String expected = "envValue1.value1.value2.value3.envValue1.envValue2";
        assertEquals(expected, result);
    }

    private Map<String, Object> createInputTemplate() {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("param2", "defaultValue2");
        return inputTemplate;
    }

    private WorkflowModel createWorkflowModel(WorkflowDef workflowDef) {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(workflowDef);
        workflowModel.setInput(createInput());
        workflowModel.setTasks(createTasks());
        return workflowModel;
    }

    private Map<String, Object> createInput() {
        Map<String, Object> input = new HashMap<>();
        input.put("param1", "value1");
        return input;
    }

    private List<TaskModel> createTasks() {
        List<TaskModel> tasks = new LinkedList<>();
        tasks.add(createTask("task1"));
        tasks.add(createTask("task2"));
        return tasks;
    }

    private TaskModel createTask(String taskName) {
        TaskModel task = new TaskModel();
        task.setTaskType(taskName);
        task.setInputData(createInput());
        task.setOutputData(createOutput());
        return task;
    }

    private Map<String, Object> createOutput() {
        Map<String, Object> output = new HashMap<>();
        output.put("param3", "value3");
        return output;
    }
}