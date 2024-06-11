import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
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
import com.netflix.conductor.model.WorkflowModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

@Component
public class ParametersUtilsTest {

    @Mock private ObjectMapper objectMapper;
    @Mock private EnvUtils envUtils;
    @Mock private TaskUtils taskUtils;

    private ParametersUtils parametersUtils;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        when(objectMapper.writeValueAsBytes(anyMap())).thenReturn(new byte[0]);
        when(objectMapper.readValue(anyByteArray(), any(TypeReference.class)))
                .thenReturn(new HashMap<>());

        when(envUtils.isEnvironmentVariable(anyString()))
                .thenReturn(false)
                .thenReturn(true)
                .thenReturn(false);
        when(envUtils.getSystemParametersValue(anyString(), anyString()))
                .thenReturn("sysValue1")
                .thenReturn("sysValue2");

        parametersUtils = new ParametersUtils(objectMapper, envUtils, taskUtils);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflow = new WorkflowModel();
        Map<String, Object> inputParams = new HashMap<>();
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(inputParams);

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");

        assertEquals(inputParams, result);
        verify(objectMapper).writeValueAsBytes(inputParams);
        verify(objectMapper).readValue(anyByteArray(), any(TypeReference.class));
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.setInput(Collections.singletonMap("workflowInput", "value1"));
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.workflowInput", "param1");

        Map<String, Object> result = parametersUtils.getTaskInputV1(workflow, inputParams);

        assertEquals(Collections.singletonMap("param1", "value1"), result);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Collections.singletonMap("workflowInput", "value2"));
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> result =
                parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals(
                Collections.singletonMap("workflowInput", "value2"), result);
    }

    @Test
    public void testReplace() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "${param1}");

        DocumentContext documentContext =
                JsonPath.parse("{\"param1\": \"value2\"}");

        Map<String, Object> result =
                parametersUtils.replace(inputParams, documentContext, null);

        assertEquals(
                Collections.singletonMap("param2", "value2"),
                result);
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "param1 ${param2} ${param3}";

        DocumentContext documentContext =
                JsonPath.parse("{\"param2\": \"value2\", \"param3\": {\"subParam\": \"value3\"}}");

        Object result =
                parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("param1 value2 value3", result);
    }

    @Test
    public void testReplaceVariablesNull() {
        String paramString = "param1 ${param2} ${param3}";

        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());

        Object result =
                parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("param1 ${param2} ${param3}", result);
    }

    @Test
    public void testReplaceVariablesSystemValue() {
        String paramString = "param1 ${param2} ${param3}";

        when(envUtils.getSystemParametersValue("param3", null))
                .thenReturn("sysValue1");

        DocumentContext documentContext =
                JsonPath.parse("{\"param2\": \"value2\"}");

        Object result =
                parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("param1 value2 sysValue1", result);
    }
}