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

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private Logger logger;
    private EnvUtils envUtils;
    private TaskUtils taskUtils;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = mock(ObjectMapper.class);
        logger = mock(Logger.class);
        envUtils = mock(EnvUtils.class);
        taskUtils = mock(TaskUtils.class);
        parametersUtils =
                new ParametersUtils(
                        objectMapper, logger, envUtils, taskUtils, new TypeReference<Map<String, Object>>() {});
    }

    // Test for getTaskInput method
    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParam1", "value1");

        when(objectMapper.clone(inputParams)).thenReturn(inputParams);

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertEquals(inputParams, result);
    }

    @Test
    public void testGetTaskInput_withInputTemplate() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());
        taskDefinition.getInputTemplate().put("inputParam2", "value2");
        String taskId = "task1";

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputParam1", "value1");

        when(objectMapper.clone(inputParams)).thenReturn(inputParams);

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

        assertEquals(3, result.size());
        assertEquals("value1", result.get("inputParam1"));
        assertEquals("value2", result.get("inputParam2"));
    }

    // Test for replace method
    @Test
    public void testReplace() {
        String json = "{\"key1\": \"value1\", \"key2\": \"value2\"}";
        DocumentContext documentContext = JsonPath.parse(json);

        Map<String, Object> result = (Map<String, Object>) parametersUtils.replace(documentContext);

        assertEquals(json, result.toString());
    }

    @Test
    public void testReplace_withNullValue() {
        String json = "{\"key1\": \"value1\", \"key2\": null}";
        DocumentContext documentContext = JsonPath.parse(json);

        Map<String, Object> result = (Map<String, Object>) parametersUtils.replace(documentContext);

        assertEquals(json, result.toString());
    }

    @Test
    public void testReplace_withEmptyJson() {
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        Map<String, Object> result = (Map<String, Object>) parametersUtils.replace(documentContext);

        assertEquals("{}", result.toString());
    }

    // Test for replaceVariables method
    @Test
    public void testReplaceVariables() {
        String paramString = "${key1}";
        DocumentContext documentContext = JsonPath.parse("{\"key1\": \"value1\"}");

        String result =
                parametersUtils.replaceVariables(paramString, documentContext, null).toString();

        assertEquals("value1", result);
    }

    @Test
    public void testReplaceVariables_withEnvVariables() {
        String paramString = "${ENV_VARIABLE}";
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        String sysValue = "sysValue";
        when(envUtils.getSystemParametersValue(paramString, null)).thenReturn(sysValue);

        String result =
                parametersUtils.replaceVariables(paramString, documentContext, null).toString();

        assertEquals(sysValue, result);
    }

    @Test
    public void testReplaceVariables_withNullValue() {
        String paramString = "${key1}";
        DocumentContext documentContext = JsonPath.parse("{\"key1\": null}");

        String result =
                parametersUtils.replaceVariables(paramString, documentContext, null).toString();

        assertNull(result);
    }

    @Test
    public void testReplaceVariables_withNonExistentPath() {
        String paramString = "${key1}";
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        String result =
                parametersUtils.replaceVariables(paramString, documentContext, null).toString();

        assertEquals("", result);
    }

    @Test
    public void testReplaceVariables_withExtraDollarSign() {
        String paramString = "$${key1}";
        DocumentContext documentContext = JsonPath.parse("{\"key1\": \"value1\"}");

        String result =
                parametersUtils.replaceVariables(paramString, documentContext, null).toString();

        assertEquals("${key1}", result);
    }

    @Test
    public void testReplaceVariables_withExtraDollarSignAndEnvVariables() {
        String paramString = "$${ENV_VARIABLE}";
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        String sysValue = "sysValue";
        when(envUtils.getSystemParametersValue(paramString, null)).thenReturn(sysValue);

        String result =
                parametersUtils.replaceVariables(paramString, documentContext, null).toString();

        assertEquals("${ENV_VARIABLE}", result);
    }
}