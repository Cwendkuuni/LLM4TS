import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.stereotype.Component;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @Mock private ObjectMapper objectMapper;
    @Mock private EnvUtils envUtils;
    @Mock private TaskUtils taskUtils;

    @InjectMocks private ParametersUtils parametersUtils;

    private WorkflowModel workflowModel;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private Map<String, Object> inputParams;
    private Map<String, Object> inputTemplate;
    private TypeReference<Map<String, Object>> typeRef;

    @Before
    public void setup() {
        workflowModel = new WorkflowModel();
        workflowDef = new WorkflowDef();
        taskDef = new TaskDef();
        inputParams = new HashMap<>();
        inputTemplate = new HashMap<>();
        typeRef = new TypeReference<Map<String, Object>>() {};

        workflowModel.setInput(Collections.singletonMap("inputKey", "inputValue"));
        workflowModel.setOutput(Collections.singletonMap("outputKey", "outputValue"));
        workflowModel.setStatus("COMPLETED");
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setParentWorkflowId("parentWorkflowId");
        workflowModel.setParentWorkflowTaskId("parentWorkflowTaskId");
        workflowModel.setWorkflowType("workflowType");
        workflowModel.setVersion(1);
        workflowModel.setCorrelationId("correlationId");
        workflowModel.setReasonForIncompletion("reasonForIncompletion");
        workflowModel.setSchemaVersion(1);

        workflowDef.setInputTemplate(inputTemplate);

        taskDef.setInputTemplate(inputTemplate);

        when(objectMapper.writeValueAsBytes(inputTemplate)).thenReturn("inputTemplateBytes".getBytes());
        when(objectMapper.readValue(any(byte[].class), eq(typeRef))).thenReturn(inputTemplate);

        when(envUtils.isEnvironmentVariable(anyString())).thenReturn(false);
        when(envUtils.getSystemParametersValue(anyString(), anyString())).thenReturn("envValue");

        when(taskUtils.removeIterationFromTaskRefName(anyString())).thenReturn("taskRef");
    }

    @Test
    public void testGetTaskInput() {
        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        assertEquals(
                "inputValue",
                result.get("workflow.input.inputKey").toString());
        assertEquals("outputValue", result.get("workflow.output.outputKey"));
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> result =
                parametersUtils.getTaskInputV2(
                        inputParams, workflowModel, "taskId", taskDef);

        assertEquals(
                "inputValue",
                result.get("workflow.input.inputKey").toString());
        assertEquals("outputValue", result.get("workflow.output.outputKey"));
    }

    @Test
    public void testClone() {
        Map<String, Object> cloned = parametersUtils.clone(inputParams);

        assertEquals(inputParams, cloned);
    }

    @Test
    public void testReplace() {
        DocumentContext context =
                JsonPath.parse(
                        "{\"a\": {\"b\": {\"c\": \"value\"}}, \"d\": \"$$${\\\"a.b.c\\\"}\"}");

        String replaced = parametersUtils.replace(context);

        assertEquals("{\"a\": {\"b\": {\"c\": \"value\"}}, \"d\": \"value\"}", replaced);
    }

    @Test
    public void testReplaceVariables() {
        String paramString =
                "before$$${workflow.input.inputKey}after$$${workflow.output.outputKey}before";

        DocumentContext context =
                JsonPath.parse(
                        "{\"a\": {\"b\": {\"c\": \"value\"}}, \"workflow\": {\"input\": {\"inputKey\": \"inputValue\"}, \"output\": {\"outputKey\": \"outputValue\"}}}");

        String replaced = parametersUtils.replaceVariables(paramString, context, null);

        assertEquals(
                "beforeinputValueafteroutputValuebefore", replaced);
    }

    @Test
    public void testReplaceVariablesWithEnvVar() {
        when(envUtils.isEnvironmentVariable("envVar")).thenReturn(true);

        String paramString = "before$$${envVar}after";

        DocumentContext context = JsonPath.parse("{}");

        String replaced = parametersUtils.replaceVariables(paramString, context, "taskId");

        assertEquals("beforeenvValueafter", replaced);
    }

    @Test
    public void testReplaceVariablesWithNullValue() {
        String paramString = "before$$${workflow.output.outputKey}after";

        DocumentContext context =
                JsonPath.parse(
                        "{\"workflow\": {\"output\": {\"outputKey\": null}}}");

        String replaced = parametersUtils.replaceVariables(paramString, context, null);

        assertNull(replaced);
    }

    @Test
    public void testGetWorkflowInput() {
        Map<String, Object> result =
                parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals(inputParams, result);
    }
}