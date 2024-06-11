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
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private EnvUtils envUtils;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        envUtils = mock(EnvUtils.class);
        parametersUtils = new ParametersUtils(objectMapper) {
            @Override
            EnvUtils getEnvUtils() {
                return envUtils;
            }
        };
    }

    @Test
    public void testGetTaskInputV1() throws Exception {
        WorkflowDef workflowDef = TestUtils.getWorkflowDef();
        WorkflowModel workflowModel = TestUtils.getWorkflowModel(workflowDef);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.param1", "value1");
        inputParams.put("task1.input.param2", "value2");
        inputParams.put("task1.output.param3", "value3");

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Collections.singletonMap("param4", "value4"));

        Map<String, Object> result =
                parametersUtils.getTaskInputV1(workflowModel, inputParams);

        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("param2"));
        assertEquals("value3", result.get("param3"));
        assertEquals("value4", result.get("param4"));
    }

    @Test
    public void testGetTaskInputV2() throws Exception {
        WorkflowDef workflowDef = TestUtils.getWorkflowDef();
        WorkflowModel workflowModel = TestUtils.getWorkflowModel(workflowDef);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("${workflow.input.param1}", "value1");
        inputParams.put("${task1.input.param2}", "value2");
        inputParams.put("${task1.output.param3}", "value3");

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(Collections.singletonMap("param4", "value4"));

        Map<String, Object> result =
                parametersUtils.getTaskInputV2(inputParams, workflowModel, "task1", taskDef);

        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("param2"));
        assertEquals("value3", result.get("param3"));
        assertEquals("value4", result.get("param4"));
    }

    @Test
    public void testReplace_String_DocumentContext_String() throws Exception {
        String paramString = "${param1}.${param2}.${param3}";
        Map<String, Object> input = new HashMap<>();
        input.put("param1", 1);
        input.put("param2", "2");
        DocumentContext documentContext =
                JsonPath.parse(Collections.singletonMap("param3", 3));

        Object replaced = parametersUtils.replace(paramString, documentContext, input);

        assertEquals("1.2.3", replaced);
    }

    @Test
    public void testReplace_String_DocumentContext_String_WithEnv() throws Exception {
        String paramString = "${param1}.${param2}.${param3}";
        Map<String, Object> input = new HashMap<>();
        input.put("param1", 1);
        input.put("param2", "2");
        DocumentContext documentContext =
                JsonPath.parse(Collections.singletonMap("param3", 3));

        when(envUtils.getSystemParametersValue("param3", null))
                .thenReturn("envValue");

        Object replaced = parametersUtils.replace(paramString, documentContext, input, envUtils);

        assertEquals("1.2.envValue", replaced);
    }

    @Test
    public void testReplace_String_DocumentContext_String_WithEmptyReplacement()
            throws Exception {
        String paramString = "${param1}.${param2}.${param3}";
        Map<String, Object> input = new HashMap<>();
        input.put("param1", 1);
        input.put("param2", "2");

        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        Object replaced = parametersUtils.replace(paramString, documentContext, input);

        assertEquals("1.2.${param3}", replaced);
    }

    @Test
    public void testReplace_String_DocumentContext_String_WithNullReplacement()
            throws Exception {
        String paramString = "${param1}.${param2}.${param3}";
        Map<String, Object> input = new HashMap<>();
        input.put("param1", 1);
        input.put("param2", "2");

        DocumentContext documentContext = JsonPath.parse("null");

        Object replaced = parametersUtils.replace(paramString, documentContext, input);

        assertEquals("1.2.null", replaced);
    }

    @Test
    public void testReplace_String_DocumentContext_String_WithEmptyDocumentContext()
            throws Exception {
        String paramString = "${param1}.${param2}.${param3}";
        Map<String, Object> input = new HashMap<>();
        input.put("param1", 1);
        input.put("param2", "2");

        DocumentContext documentContext = JsonPath.parse(Collections.emptyList());

        Object replaced = parametersUtils.replace(paramString, documentContext, input);

        assertEquals("${param1}.${param2}.${param3}", replaced);
    }

    @Test
    public void testReplace_String_DocumentContext_String_WithNoMatch()
            throws Exception {
        String paramString = "${param1}.${param2}.${param3}";
        Map<String, Object> input = new HashMap<>();
        input.put("param1", 1);
        input.put("param2", "2");

        DocumentContext documentContext =
                JsonPath.parse(Collections.singletonMap("param4", 4));

        Object replaced = parametersUtils.replace(paramString, documentContext, input);

        assertEquals("${param1}.${param2}.${param3}", replaced);
    }

    @Test
    public void testReplaceVariables() throws Exception {
        String paramString = "${param1}.${param2}.${param3}";
        Map<String, Object> input = new HashMap<>();
        input.put("param1", 1);
        input.put("param2", "2");

        DocumentContext documentContext =
                JsonPath.parse(Collections.singletonMap("param3", 3));

        Object replaced =
                parametersUtils.replaceVariables(paramString, documentContext, input);

        assertEquals("1.2.3", replaced);
    }
}