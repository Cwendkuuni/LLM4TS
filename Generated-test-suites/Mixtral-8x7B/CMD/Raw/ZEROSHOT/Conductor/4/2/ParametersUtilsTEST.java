import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private EnvUtils envUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        objectMapper = mock(ObjectMapper.class);
        envUtils = mock(EnvUtils.class);

        parametersUtils = new ParametersUtils(objectMapper);
        parametersUtils.setEnvUtils(envUtils);
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setSchemaVersion(2);
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(Collections.singletonMap("workflowInputKey", "workflowInputValue"));
        workflowModel.setOutput(Collections.singletonMap("workflowOutputKey", "workflowOutputValue"));
        workflowModel.setStatus("WORKFLOW_STATUS");
        workflowModel.setWorkflowId("WORKFLOW_ID");
        workflowModel.setParentWorkflowId("PARENT_WORKFLOW_ID");
        workflowModel.setParentWorkflowTaskId("PARENT_WORKFLOW_TASK_ID");
        workflowModel.setWorkflowName("WORKFLOW_NAME");
        workflowModel.setWorkflowVersion(1);
        workflowModel.setCorrelationId("CORRELATION_ID");
        workflowModel.setReasonForIncompletion("REASON_FOR_INCOMPLETION");

        List<TaskModel> tasks = new LinkedList<>();
        tasks.add(createTaskModel("TASK_1", "TASK_INPUT_KEY_1", "TASK_OUTPUT_KEY_1"));
        tasks.add(createTaskModel("TASK_2", "TASK_INPUT_KEY_2", "TASK_OUTPUT_KEY_2"));
        workflowModel.setTasks(tasks);

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Collections.singletonMap("taskInputKey", "taskInputValue"));

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.workflowInputKey", "WORKFLOW_INPUT_VALUE");
        inputParams.put("task.input.TASK_1.input.TASK_INPUT_KEY_1", "TASK_INPUT_VALUE_1");
        inputParams.put("task.input.TASK_2.input.TASK_INPUT_KEY_2", "TASK_INPUT_VALUE_2");

        Map<String, Object> result =
                parametersUtils.getTaskInputV2(
                        inputParams, workflowModel, taskDef, "TASK_1_ID");

        assertEquals(
                result.get("workflow.input.workflowInputKey"), "WORKFLOW_INPUT_VALUE");
        assertEquals(
                result.get("task.input.TASK_1.input.TASK_INPUT_KEY_1"), "TASK_INPUT_VALUE_1");
        assertEquals(
                result.get("task.input.TASK_1.input.TASK_INPUT_KEY_2"), "TASK_INPUT_VALUE_2");
    }

    @Test
    public void testReplace() {
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse("{\"key\":\"value\"}", option);

        String input = "This is a ${key} in the string.";
        String result = parametersUtils.replace(input, documentContext, null);
        assertEquals("This is a value in the string.", result);
    }

    @Test
    public void testReplaceVariables() {
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse("{\"key\":\"value\"}", option);

        String input = "This is a ${key} in the string.";
        String result = parametersUtils.replaceVariables(input, documentContext, null);
        assertEquals("This is a value in the string.", result);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Collections.singletonMap("workflowInputKey", "workflowInputValue"));

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.workflowInputKey", "WORKFLOW_INPUT_VALUE");

        Map<String, Object> result =
                parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals(result.get("workflowInputKey"), "WORKFLOW_INPUT_VALUE");
    }

    private TaskModel createTaskModel(String referenceTaskName, String inputKey, String outputKey) {
        TaskModel taskModel = new TaskModel();
        taskModel.setReferenceTaskName(referenceTaskName);

        Map<String, Object> inputData = new HashMap<>();
        inputData.put(inputKey, "TASK_INPUT_VALUE");
        taskModel.setInputData(inputData);

        Map<String, Object> outputData = new HashMap<>();
        outputData.put(outputKey, "TASK_OUTPUT_VALUE");
        taskModel.setOutputData(outputData);

        return taskModel;
    }
}