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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private static final Pattern PATTERN =
            Pattern.compile(
                    "(?=(?<!\\$)\\$\\{)(?:(?=.*?\\{(?!.*?\\1)(.*\\}(?!.*\\2).*))(?=.*?\\}(?!.*?\\2)(.*)).)+?.*?(?=\\1)[^{]*(?=\\2$)",
                    Pattern.DOTALL);

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper) {
            @Override
            public Map<String, Object> getTaskInputV1(WorkflowModel workflow, Map<String, Object> inputParams) {
                return super.getTaskInputV1(workflow, inputParams);
            }
        };
        when(objectMapper.writeValueAsBytes(any())).thenAnswer(invocation -> {
            Object obj = invocation.getArgument(0);
            if (obj instanceof Map) {
                return "{\"key\": \"value\"}".getBytes();
            }
            throw new IOException("Unexpected object to serialize: " + obj.getClass());
        });
        when(objectMapper.readValue(any(), any())).thenAnswer(invocation -> {
            TypeReference<Map<String, Object>> typeRef = (TypeReference<Map<String, Object>>) invocation.getArgument(1);
            return new HashMap<String, Object>() {{
                put("key", "value");
            }};
        });
    }

    @Test
    public void testGetTaskInput_v2() {
        WorkflowModel workflow = createWorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(createInputTemplate());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "${workflow.input.key}");

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "task-id");

        assertEquals("value", result.get("inputKey"));
    }

    @Test
    public void testGetTaskInput_v1() {
        WorkflowModel workflow = createWorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "${workflow.input.key}");

        Map<String, Object> result =
                parametersUtils.getTaskInputV1(workflow, inputParams);

        assertEquals("value", result.get("inputKey"));
    }

    @Test
    public void testGetTaskInput_inputTemplate() {
        WorkflowModel workflow = createWorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(createInputTemplate());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "${workflow.input.key}");

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "task-id");

        assertEquals("value", result.get("inputKey"));
    }

    @Test
    public void testGetTaskInput_nullInputParams() {
        WorkflowModel workflow = createWorkflowModel();
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> result =
                parametersUtils.getTaskInput(null, workflow, taskDefinition, "task-id");

        assertEquals(new HashMap<String, Object>(), result);
    }

    @Test
    public void testGetTaskInput_noTaskDefinition() {
        WorkflowModel workflow = createWorkflowModel();

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "${workflow.input.key}");

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflow, null, "task-id");

        assertEquals("value", result.get("inputKey"));
    }

    @Test
    public void testGetTaskInput_noInputTemplate() {
        WorkflowModel workflow = createWorkflowModel();
        TaskDef taskDefinition = new TaskDef();

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "${workflow.input.key}");

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "task-id");

        assertEquals("value", result.get("inputKey"));
    }

    @Test
    public void testReplace_documentContext() {
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse("{\"input\": {\"key\": \"value\"}}", option);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "${input.key}");

        Map<String, Object> result = parametersUtils.replace(inputParams, documentContext);

        assertEquals("value", result.get("inputKey"));
    }

    @Test
    public void testReplace_string() {
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse("{\"input\": {\"key\": \"value\"}}", option);

        Map<String, Object> result = parametersUtils.replace("${input.key}", documentContext);

        assertEquals("value", result);
    }

    @Test
    public void testReplaceVariables_depth0() {
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse("{\"input\": {\"key\": \"value\"}}", option);

        String paramString = "${input.key}";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null, 0);

        assertEquals("value", result);
    }

    @Test
    public void testReplaceVariables_depth1() {
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse("{\"input\": {\"key\": \"value\"}}", option);

        String paramString = "${${input.key}}";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null, 1);

        assertEquals("${value}", result);
    }

    @Test
    public void testReplaceVariables_blankParamPath() {
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse("{\"input\": {\"key\": \"value\"}}", option);

        String paramString = "${ }";

        Object result = parametersUtils.replaceVariables(paramString, documentContext, null, 0);

        assertEquals("", result);
    }

    @Test
    public void testReplaceVariables_envVariable() {
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse("{}", option);

        String paramString = "${ENV_VAR}";

        when(EnvUtils.isEnvironmentVariable(anyString())).thenReturn(true);
        when(EnvUtils.getSystemParametersValue(anyString(), anyString())).thenReturn("envValue");

        Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId", 0);

        assertEquals("envValue", result);
    }

    @Test
    public void testReplaceVariables_exception() {
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse("{}", option);

        String paramString = "${input.key}";

        when(documentContext.read(anyString())).thenThrow(new RuntimeException("Test exception"));

        Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId", 0);

        assertNull(result);
    }

    @Test
    public void testReplace_list() {
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse("{\"input\": [{\"key\": \"value\"}]}", option);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("inputKey", "${input[0].key}");

        Map<String, Object> result = parametersUtils.replace(inputParams, documentContext);

        assertEquals("value", result.get("inputKey"));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(createInputTemplate());

        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> result =
                parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals("value", result.get("inputKey"));
    }

    private WorkflowModel createWorkflowModel() {
        return new WorkflowModel() {{
            setInput(createInput());
            setTaskByRefName("task", new TaskModel() {{
                setInputData(createInput());
            }});
        }};
    }

    private Map<String, Object> createInput() {
        return new HashMap<String, Object>() {{
            put("key", "value");
        }};
    }

    private Map<String, Object> createInputTemplate() {
        return new HashMap<String, Object>() {{
            put("inputKey", "${workflow.input.key}");
        }};
    }
}