import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.netflix.conductor.model.TaskModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class ParametersUtilsTest {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ParametersUtils.class);
    private static final Pattern PATTERN =
            Pattern.compile(
                    "(?=(?<!\\$)\\$\\{)(?:(?=.*?\\{(?!.*?\\1)(.*\\}(?!.*\\2).*))(?=.*?\\}(?!.*?\\2)(.*)).)+?.*?(?=\\1)[^{]*(?=\\2$)",
                    Pattern.DOTALL);

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private EnvUtils envUtils;
    private TaskUtils taskUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectMapper = mock(ObjectMapper.class);
        envUtils = mock(EnvUtils.class);
        taskUtils = mock(TaskUtils.class);
        parametersUtils = new ParametersUtils(objectMapper, envUtils, taskUtils);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskDef taskDefinition = mock(TaskDef.class);
        Map<String, Object> inputParams = new HashMap<>();

        when(workflow.getWorkflowDefinition().getSchemaVersion())
                .thenReturn(1); // Testing with schema version 1

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");

        assertEquals(0, result.size()); // Ensure no unexpected modifications
    }

    // Add more test cases for different scenarios and schema versions

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflow = mock(WorkflowModel.class);
        TaskDef taskDefinition = mock(TaskDef.class);
        Map<String, Object> inputParams = new HashMap<>();

        when(workflow.getWorkflowDefinition().getSchemaVersion())
                .thenReturn(2); // Testing with schema version 2

        when(workflow.getInput()).thenReturn(Collections.singletonMap("inputKey", "inputValue"));
        when(workflow.getOutput()).thenReturn(Collections.singletonMap("outputKey", "outputValue"));
        when(workflow.getStatus()).thenReturn("status");
        when(workflow.getWorkflowId()).thenReturn("workflowId");
        when(workflow.getParentWorkflowId()).thenReturn("parentWorkflowId");
        when(workflow.getParentWorkflowTaskId()).thenReturn("parentWorkflowTaskId");
        when(workflow.getWorkflowName()).thenReturn("workflowType");
        when(workflow.getWorkflowVersion())
                .thenReturn(1); // Workflow version should be greater than 0
        when(workflow.getCorrelationId())
                .thenReturn("correlationId"); // Correlation ID should not be null
        when(workflow.getReasonForIncompletion())
                .thenReturn("reasonForIncompletion"); // Reason for incompletion should not be null
        when(workflow.getVariables())
                .thenReturn(Collections.singletonMap("variableKey", "variableValue"));

        when(taskDefinition.getInputTemplate())
                .thenReturn(Collections.singletonMap("inputTemplateKey", "inputTemplateValue"));

        Map<String, Object> result =
                parametersUtils.getTaskInputV2(
                        inputParams, workflow, "taskId", taskDefinition);

        assertEquals(
                21, result.size()); // Ensure all workflow properties are present
        assertEquals(
                "inputValue",
                result.get("workflow.input.inputKey")); // Ensure input values are resolved
        assertEquals(
                "outputValue",
                result.get("workflow.output.outputKey")); // Ensure output values are resolved
        assertEquals("status", result.get("workflow.status")); // Ensure status is resolved
        assertEquals("workflowId", result.get("workflow.workflowId")); // Ensure workflow ID is resolved
        assertEquals(
                "parentWorkflowId", result.get("workflow.parentWorkflowId")); // Ensure parent workflow ID is resolved
        assertEquals(
                "parentWorkflowTaskId",
                result.get("workflow.parentWorkflowTaskId")); // Ensure parent workflow task ID is resolved
        assertEquals("workflowType", result.get("workflow.workflowType")); // Ensure workflow type is resolved
        assertEquals("1", result.get("workflow.version")); // Ensure workflow version is resolved
        assertEquals(
                "correlationId",
                result.get("workflow.correlationId")); // Ensure correlation ID is resolved
        assertEquals(
                "reasonForIncompletion",
                result.get("workflow.reasonForIncompletion")); // Ensure reason for incompletion is resolved
        assertEquals("variableValue", result.get("workflow.variables.variableKey")); // Ensure variables are resolved

        assertEquals("inputTemplateValue", result.get("inputTemplateKey")); // Ensure input template is resolved
    }

    // Add more test cases for different scenarios and input templates

    @Test
    public void testClone() throws IOException {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("key", "value");

        when(objectMapper.writeValueAsBytes(inputTemplate))
                .thenReturn("test bytes".getBytes());

        TypeReference<Map<String, Object>> mapTypeReference = new TypeReference<>() {};
        Map<String, Object> clonedMap =
                parametersUtils.clone(inputTemplate, mapTypeReference);

        assertEquals(1, clonedMap.size());
        assertEquals("value", clonedMap.get("key"));
    }

    // Add more test cases for invalid inputs and other scenarios

    @Test
    public void testReplace() {
        String paramString = "test ${param} and ${param2}";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param", "value1");
        inputMap.put("param2", "value2");

        DocumentContext documentContext = JsonPath.parse(inputMap);
        String result = parametersUtils.replace(paramString, documentContext);

        assertEquals("test value1 and value2", result);
    }

    // Add more test cases for different scenarios, null inputs, and empty maps

    @Test
    public void testReplaceVariables() {
        String paramString = "test ${param} and ${param2}";
        DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("param", "value1"));

        String result =
                parametersUtils.replaceVariables(
                        paramString, documentContext, "taskId", 0);

        assertEquals("test value1 and ${param2}", result);
    }

    // Add more test cases for different scenarios, null inputs,
    // and empty maps with different depth levels

    @Test
    public void testReplaceVariablesWithEnvUtils() {
        String paramString = "test ${param} and ${env:TEST_ENV_VARIABLE}";
        when(envUtils.getSystemParametersValue("TEST_ENV_VARIABLE", "taskId"))
                .thenReturn("envValue");

        DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("param", "value1"));

        String result =
                parametersUtils.replaceVariables(
                        paramString, documentContext, "taskId", 0);

        assertEquals("test value1 and envValue", result);
    }

    // Add more test cases for different scenarios, null inputs,
    // and empty maps with different depth levels and environment variables

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        Map<String, Object> inputParams = new HashMap<>();

        when(workflowDef.getInputTemplate())
                .thenReturn(Collections.singletonMap("inputTemplateKey", "inputTemplateValue"));

        Map<String, Object> result =
                parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals(1, result.size());
        assertEquals("inputTemplateValue", result.get("inputTemplateKey"));
    }

    // Add more test cases for different scenarios and input templates
}