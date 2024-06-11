import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private EnvUtils envUtils;
    private TaskUtils taskUtils;

    @Before
    public void setUp() {
        objectMapper = mock(ObjectMapper.class);
        envUtils = mock(EnvUtils.class);
        taskUtils = mock(TaskUtils.class);

        when(envUtils.isEnvironmentVariable(Mockito.anyString()))
                .thenReturn(false);
        when(envUtils.getSystemParametersValue(Mockito.anyString(), Mockito.anyString()))
                .thenReturn("sys_value");

        parametersUtils =
                new ParametersUtils(
                        objectMapper,
                        envUtils,
                        taskUtils) {
                    @Override
                    TypeReference<Map<String, Object>> map =
                            new TypeReference<Map<String, Object>>() {};
                };
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setSchemaVersion(2);
        workflowDef.setWorkflowType("testType");
        workflowDef.setWorkflowVersion("1.0");
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(workflowDef);
        workflowModel.setInput(createTestInput());
        workflowModel.setOutput(createTestOutput());
        workflowModel.setStatus("COMPLETED");
        workflowModel.setWorkflowId("test_workflow_id");
        workflowModel.setParentWorkflowId("parent_workflow_id");
        workflowModel.setParentWorkflowTaskId("parent_task_id");
        Map<String, TaskModel> tasks = new HashMap<>();
        TaskModel taskModel1 = new TaskModel();
        taskModel1.setReferenceTaskName("task1");
        taskModel1.setInputData(createTestInput());
        taskModel1.setOutputData(createTestOutput());
        taskModel1.setTaskDefName("testTaskDef");
        taskModel1.setTaskType("testTaskType");
        tasks.put("task1", taskModel1);
        TaskModel taskModel2 = new TaskModel();
        taskModel2.setReferenceTaskName("task2");
        taskModel2.setInputData(createTestInput());
        taskModel2.setOutputData(createTestOutput());
        taskModel2.setTaskDefName("testTaskDef");
        taskModel2.setTaskType("testTaskType");
        tasks.put("task2", taskModel2);
        workflowModel.setTasks(tasks);

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(createTestInput());

        Map<String, Object> inputParams = createTestInput();

        Map<String, Object> result =
                parametersUtils.getTaskInputV2(
                        inputParams, workflowModel, taskDef, "task1");

        assertEquals("test_workflow_id", result.get("workflowId"));
        assertEquals("parent_workflow_id", result.get("parentWorkflowId"));
        assertEquals("parent_task_id", result.get("parentWorkflowTaskId"));
        assertEquals("testType", result.get("workflowType"));
        assertEquals("1.0", result.get("version"));
        assertEquals("COMPLETED", result.get("status"));
        assertEquals(createTestInput(), result.get("input"));
        assertEquals(createTestOutput(), result.get("output"));
        assertEquals("testTaskType", result.get("taskType"));
        assertEquals("testTaskDef", result.get("taskDefName"));
        assertEquals("task1", result.get("referenceTaskName"));
        assertEquals(0, result.get("retryCount"));
        assertEquals("correlation_id", result.get("correlationId"));
        assertEquals(0, result.get("pollCount"));
        assertEquals("test_scheduled_time", result.get("scheduledTime"));
        assertEquals("test_start_time", result.get("startTime"));
        assertEquals("test_end_time", result.get("endTime"));
        assertEquals("test_workflow_instance_id", result.get("workflowInstanceId"));
        assertEquals("task1", result.get("taskId"));
        assertEquals(null, result.get("reasonForIncompletion"));
        assertEquals(0, result.get("callbackAfterSeconds"));
        assertEquals("worker_id", result.get("workerId"));
        assertEquals(0, result.get("iteration"));
    }

    @Test
    public void testReplace() {
        String testString = "${workflow.input.param1} ${task.output.param2}";
        DocumentContext documentContext =
                JsonPath.parse("{\"input\": {\"param1\": \"value1\"}, \"output\": {\"param2\": \"value2\"}}");

        String result = parametersUtils.replace(testString, documentContext, "task");

        assertEquals("value1 value2", result);
    }

    @Test
    public void testReplaceVariables() {
        String testString = "${workflow.input.param1} ${task.output.param2}";
        String result =
                parametersUtils.replaceVariables(
                        testString,
                        JsonPath.parse("{\"input\": {\"param1\": \"value1\"}, \"output\": {\"param2\": \"value2\"}}"),
                        "task");

        assertEquals("value1 value2", result);
    }

    @Test
    public void testReplaceVariablesNull() {
        String testString = "${workflow.input.param1} ${task.output.param2}";
        String result = parametersUtils.replaceVariables(testString, JsonPath.parse("{}"), "task");

        assertEquals("${workflow.input.param1} ${task.output.param2}", result);
    }

    @Test
    public void testReplaceVariablesEmpty() {
        String testString = "${workflow.input.param1} ${task.output.param2}";
        String result =
                parametersUtils.replaceVariables(
                        testString, JsonPath.parse("{\"input\": {}, \"output\": {}}"), "task");

        assertEquals("${workflow.input.param1} ${task.output.param2}", result);
    }

    @Test
    public void testReplaceVariablesTaskRef() {
        String testString = "${task.output.param2}";
        String result =
                parametersUtils.replaceVariables(
                        testString,
                        JsonPath.parse("{\"output\": {\"param2\": \"value2\"}}"),
                        "task_ref");

        assertEquals("${task_ref.output.param2}", result);
    }

    @Test
    public void testReplaceVariablesTaskRefNull() {
        String testString = "${task.output.param2}";
        String result =
                parametersUtils.replaceVariables(
                        testString, JsonPath.parse("{}"), "task_ref");

        assertEquals("${task.output.param2}", result);
    }

    @Test
    public void testReplaceVariablesTaskRefEmpty() {
        String testString = "${task.output.param2}";
        String result =
                parametersUtils.replaceVariables(
                        testString, JsonPath.parse("{\"output\": {}}"), "task_ref");

        assertEquals("${task.output.param2}", result);
    }

    @Test
    public void testReplaceVariablesEnvVariable() {
        when(envUtils.isEnvironmentVariable(Mockito.anyString()))
                .thenReturn(true);
        String testString = "${env:TEST_ENV_VAR}";

        String result = parametersUtils.replace(testString);

        assertEquals("sys_value", result);
    }

    @Test
    public void testReplaceVariablesEnvVariableNull() {
        when(envUtils.isEnvironmentVariable(Mockito.anyString()))
                .thenReturn(true);
        String testString = "${env:TEST_ENV_VAR}";

        when(envUtils.getSystemParametersValue(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(null);

        String result = parametersUtils.replace(testString);

        assertEquals("", result);
    }

    @Test
    public void testReplaceVariablesEnvVariableEmpty() {
        when(envUtils.isEnvironmentVariable(Mockito.anyString()))
                .thenReturn(true);
        String testString = "${env:}";

        String result = parametersUtils.replace(testString);

        assertEquals("", result);
    }

    private Map<String, Object> createTestInput() {
        Map<String, Object> input = new TreeMap<>();
        input.put("param1", "value1");
        input.put("param2", "value2");
        return input;
    }

    private Map<String, Object> createTestOutput() {
        Map<String, Object> output = new TreeMap<>();
        output.put("param1", "value1");
        output.put("param2", "value2");
        return output;
    }
}