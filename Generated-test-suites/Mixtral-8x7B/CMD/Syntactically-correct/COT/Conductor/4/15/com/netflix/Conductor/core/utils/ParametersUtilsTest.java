import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput_v1() {
        WorkflowModel workflowModel = createWorkflowModel_v1();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.param1", "value1");
        inputParams.put("task.input.param2", "value2");

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);

        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("param2"));
    }

    @Test
    public void testGetTaskInput_v2() {
        WorkflowModel workflowModel = createWorkflowModel_v2();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.input.param1}", "value1");
        inputParams.put("${task.input.param2}", "value2");

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "task-1", null);

        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("param2"));
    }

    @Test
    public void testGetTaskInput_v2WithInputTemplate() {
        WorkflowModel workflowModel = createWorkflowModel_v2();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.input.param1}", "value1");
        inputParams.put("${task.input.param2}", "value2");

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(createInputTemplate("${task.input.param3}", "value3"));

        Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "task-1", taskDef);

        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("param2"));
        assertEquals("value3", result.get("param3"));
    }

    @Test
    public void testReplace_withString() {
        String jsonString = "{\"workflow\":{\"input\":{\"param1\":\"value1\"}}}";
        DocumentContext documentContext = JsonPath.parse(jsonString);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "${workflow.input.param1}");

        Map<String, Object> result = parametersUtils.replace(inputParams, documentContext, null);

        assertEquals("value1", result.get("param1"));
    }

    @Test
    public void testReplace_withDocumentContext() {
        String jsonString = "{\"workflow\":{\"input\":{\"param1\":\"value1\"}}}";
        DocumentContext documentContext = JsonPath.parse(jsonString);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "${workflow.input.param1}");

        Map<String, Object> result = parametersUtils.replace(inputParams, documentContext, null);

        assertEquals("value1", result.get("param1"));
    }

    @Test
    public void testReplace_withNullTaskId() {
        String jsonString = "{\"workflow\":{\"input\":{\"param1\":\"value1\"}}}";
        DocumentContext documentContext = JsonPath.parse(jsonString);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "${workflow.input.param1}");

        Map<String, Object> result = parametersUtils.replace(inputParams, documentContext, null);

        assertEquals("value1", result.get("param1"));
    }

    @Test
    public void testReplaceVariables_withoutVariables() {
        String paramString = "This is a test string";
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(paramString, result);
    }

    @Test
    public void testReplaceVariables_withVariables() {
        String paramString = "This is a ${workflow.input.test} string";
        DocumentContext documentContext =
                JsonPath.parse("{\"workflow\":{\"input\":{\"test\":\"value\"}}}");

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("This is a value string", result);
    }

    @Test
    public void testReplaceVariables_withEnvVariable() {
        String paramString = "${ENV_VARIABLE}";
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());
        System.setProperty("ENV_VARIABLE", "value");

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("value", result);
    }

    @Test
    public void testReplaceVariables_withNullValue() {
        String paramString = "${workflow.input.test}";
        DocumentContext documentContext =
                JsonPath.parse("{\"workflow\":{\"input\":{\"test\":null}}}");

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertNull(result);
    }

    // Add more test cases to cover all edge cases and methods

    private Map<String, Object> createInputTemplate(String param, Object value) {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put(param, value);
        return inputTemplate;
    }

    private WorkflowModel createWorkflowModel_v1() {
        // Implement this method to create WorkflowModel for version 1
    }

    private WorkflowModel createWorkflowModel_v2() {
        // Implement this method to create WorkflowModel for version 2
    }
}