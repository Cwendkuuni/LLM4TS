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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mock
    private EnvUtils envUtils;

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        when(envUtils.isEnvironmentVariable("test")).thenReturn(false);
        when(envUtils.isEnvironmentVariable("${test}")).thenReturn(false);
        when(envUtils.isEnvironmentVariable("${test.env}")).thenReturn(true);
        when(envUtils.getSystemParametersValue("${test.env}", "testTaskId"))
                .thenReturn("testEnvValue");
    }

    @Test
    public void testGetTaskInputV2() throws IOException {
        WorkflowDef workflowDef = TestUtils.getWorkflowDef();
        WorkflowModel workflowModel = TestUtils.getWorkflowModel(workflowDef);
        TaskDef taskDef = TestUtils.getTaskDef();
        TaskModel taskModel = TestUtils.getTaskModel(taskDef);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.testParam", "testValue");
        inputParams.put("task.input.testParam", "taskInputValue");
        inputParams.put("task.output.testParam", "taskOutputValue");
        inputParams.put("workflow.output.testParam", "workflowOutputValue");
        inputParams.put("workflow.variables.testVariable", "testVariableValue");
        inputParams.put("workflow.version", "1");
        inputParams.put("workflow.status", "COMPLETED");
        inputParams.put("workflow.workflowId", "testWorkflowId");
        inputParams.put("workflow.parentWorkflowId", "parentWorkflowId");
        inputParams.put("workflow.parentWorkflowTaskId", "parentWorkflowTaskId");
        inputParams.put("workflow.correlationId", "testCorrelationId");
        inputParams.put("workflow.reasonForIncompletion", "testReasonForIncompletion");
        inputParams.put("workflow.taskId", "testTaskId");
        inputParams.put("workflow.workflowType", "testWorkflowType");

        Map<String, Object> taskInput =
                parametersUtils.getTaskInputV2(inputParams, workflowModel, taskDef, "testTaskId");

        LOGGER.info("taskInput: {}", taskInput);

        assertEquals(
                "testValue",
                Objects.toString(
                        taskInput.get("workflow.input.testParam"), taskInput.get("workflow_input_testParam")));
        assertEquals("taskInputValue", taskInput.get("task.input.testParam"));
        assertEquals("taskOutputValue", taskInput.get("task.output.testParam"));
        assertEquals("workflowOutputValue", taskInput.get("workflow.output.testParam"));
        assertEquals("testVariableValue", taskInput.get("workflow.variables.testVariable"));
        assertEquals("1", taskInput.get("workflow.version"));
        assertEquals("COMPLETED", taskInput.get("workflow.status"));
        assertEquals("testWorkflowId", taskInput.get("workflow.workflowId"));
        assertEquals("parentWorkflowId", taskInput.get("workflow.parentWorkflowId"));
        assertEquals("parentWorkflowTaskId", taskInput.get("workflow.parentWorkflowTaskId"));
        assertEquals("testCorrelationId", taskInput.get("workflow.correlationId"));
        assertEquals("testReasonForIncompletion", taskInput.get("workflow.reasonForIncompletion"));
        assertEquals("testTaskId", taskInput.get("workflow.taskId"));
        assertEquals("testWorkflowType", taskInput.get("workflow.workflowType"));
    }

    @Test
    public void testReplace() throws IOException {
        String json = "{\"test\": \"workflow.input.testParam\", \"testArr\": [\"task.input.testParam\", \"workflow.version\"]}";
        Map<String, Object> input = OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});

        WorkflowDef workflowDef = TestUtils.getWorkflowDef();
        WorkflowModel workflowModel = TestUtils.getWorkflowModel(workflowDef);
        TaskDef taskDef = TestUtils.getTaskDef();
        TaskModel taskModel = TestUtils.getTaskModel(taskDef);

        when(envUtils.getSystemParametersValue("${test.env}", "testTaskId"))
                .thenReturn("testEnvValue");

        Map<String, Object> replacedInput =
                parametersUtils.replace(input, workflowModel, taskModel, "testTaskId");

        LOGGER.info("replacedInput: {}", replacedInput);

        assertEquals(
                "workflow.input.testParam",
                Objects.toString(
                        replacedInput.get("test"), replacedInput.get("test_")));
        List<String> testArr = (List<String>) replacedInput.get("testArr");
        assertEquals("task.input.testParam", testArr.get(0));
        assertEquals("1", testArr.get(1));
    }

    @Test
    public void testReplaceVariables() throws IOException {
        String paramString = "This is a ${test} and this is another ${test.env}";

        WorkflowDef workflowDef = TestUtils.getWorkflowDef();
        WorkflowModel workflowModel = TestUtils.getWorkflowModel(workflowDef);
        TaskDef taskDef = TestUtils.getTaskDef();
        TaskModel taskModel = TestUtils.getTaskModel(taskDef);

        String replacedParam =
                parametersUtils.replaceVariables(paramString, workflowModel, taskModel, "testTaskId");

        LOGGER.info("replacedParam: {}", replacedParam);

        assertEquals(
                "This is a test and this is another testEnvValue", replacedParam);
    }

}