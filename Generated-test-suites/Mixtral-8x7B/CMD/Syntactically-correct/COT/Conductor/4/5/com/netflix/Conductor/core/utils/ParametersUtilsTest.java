import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import java.util.Map.Entry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private Logger logger;
    private EnvUtils envUtils;
    private TaskUtils taskUtils;

    @Before
    public void setup() {
        objectMapper = mock(ObjectMapper.class);
        logger = mock(Logger.class);
        envUtils = mock(EnvUtils.class);
        taskUtils = mock(TaskUtils.class);

        parametersUtils =
                new ParametersUtils(
                        objectMapper, logger, envUtils, taskUtils, Configuration.defaultConfiguration());
    }

    @Test
    public void testGetTaskInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setWorkflowDefinition(workflowDef);
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task1";

        when(objectMapper.writeValueAsBytes(any())).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), any())).thenReturn(new HashMap<>());

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);

        assertEquals(inputParams, result);
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflowModel = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        String taskId = "task1";
        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(new HashMap<>());

        when(objectMapper.writeValueAsBytes(any())).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), any())).thenReturn(inputParams);

        Map<String, Object> result =
                parametersUtils.getTaskInputV2(inputParams, workflowModel, taskId, taskDef);

        assertEquals(inputParams, result);
    }

    @Test
    public void testDeepCloneUsingJson_POJO() {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("key", "value");

        when(objectMapper.writeValueAsBytes(any())).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), any())).thenReturn(inputTemplate);

        Map<String, Object> result = parametersUtils.clone(inputTemplate);

        assertEquals(inputTemplate, result);
    }

    @Test
    public void testReplace() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("workflow", new HashMap<>());
        inputMap.put("task1", new HashMap<>());
        inputMap.put("task2", new HashMap<>());

        Object documentContext = JsonPath.parse(inputMap);

        when(objectMapper.writeValueAsBytes(any())).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), any())).thenReturn(new HashMap<>());

        Map<String, Object> result = parametersUtils.replace(new HashMap<>(), documentContext);

        assertEquals(inputMap, result);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.param1}";

        DocumentContext documentContext =
                JsonPath.parse("{\"input\": {\"param1\": \"value1\"}}");

        when(envUtils.getSystemParametersValue(anyString(), anyString()))
                .thenReturn("envValue");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("value1", result);
    }

    @Test
    public void testReplaceVariables_NoMatch() {
        String paramString = "paramString";

        DocumentContext documentContext = JsonPath.parse(new HashMap<>());

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(paramString, result);
    }

    @Test
    public void testReplaceVariables_NoParamPathComponents() {
        String paramString = "paramString";

        DocumentContext documentContext =
                JsonPath.parse("{\"input\": {\"param1\": \"value1\"}}");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(paramString, result);
    }

    @Test
    public void testReplaceVariables_WithEnvVar() {
        String paramString = "${param_env}";

        DocumentContext documentContext = JsonPath.parse(new HashMap<>());

        when(envUtils.getSystemParametersValue(anyString(), anyString()))
                .thenReturn("envValue");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("envValue", result);
    }

    @Test
    public void testReplaceVariables_WithEmptyParamPath() {
        String paramString = "${}";

        DocumentContext documentContext = JsonPath.parse(new HashMap<>());

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("", result);
    }

    @Test
    public void testReplaceVariables_WithException() {
        String paramString = "${workflow.input.param1}";

        DocumentContext documentContext = JsonPath.parse(new HashMap<>());

        when(envUtils.getSystemParametersValue(anyString(), anyString()))
                .thenThrow(new RuntimeException("Test exception"));

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertNull(result);
    }

    @Test
    public void testReplaceVariables_Depth() {
        String paramString = "$${ { ${param1} } }";

        DocumentContext documentContext = JsonPath.parse("{\"param1\": \"value1\"}");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("$ { value1 }", result);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();

        when(objectMapper.writeValueAsBytes(any())).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), any())).thenReturn(inputParams);

        Map<String, Object> result =
                parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals(inputParams, result);
    }
}