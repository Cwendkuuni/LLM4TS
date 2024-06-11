import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        EnvUtils envUtils = mock(EnvUtils.class);
        parametersUtils = new ParametersUtils(objectMapper, envUtils);
    }

    // Extract and list all the public methods including their signatures
    // For each methods, generate a basic JUnit 4 test case that checks the method's functionality

    @Test
    public void testGetTaskInput() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";

        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);
        assertEquals(inputParams, result);
    }

    @Test
    public void testGetTaskInputV2() {
        Map<String, Object> inputParams = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(inputParams);
        String taskId = "task1";

        Map<String, Object> result = parametersUtils.getTaskInputV2(null, workflow, taskId, taskDefinition);
        assertEquals(inputParams, result);
    }

    @Test
    public void testClone() throws IOException {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("key", "value");

        Map<String, Object> result = parametersUtils.clone(inputTemplate);
        assertEquals(inputTemplate, result);
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "${value}");

        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("value", "replacement");

        Map<String, Object> result = parametersUtils.replace(input, documentContext);
        assertEquals("replacement", result.get("key"));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${value}";

        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("value", "replacement");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("replacement", result);
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("key", "value");
        workflowDef.setInputTemplate(inputTemplate);

        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals(inputTemplate, result);
    }

    // Given the source code of the class and the listed methods, identify potential edge cases and exception handling scenarios that should be tested
    // Generate JUnit 4 test cases that specifically test for the identified edge cases and exceptions

    @Test
    public void testGetTaskInput_EdgeCase_EmptyInputParams() {
        WorkflowModel workflow = new WorkflowModel();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";

        Map<String, Object> result = parametersUtils.getTaskInput(null, workflow, taskDefinition, taskId);
        assertEquals(new HashMap<>(), result);
    }

    @Test
    public void testGetTaskInputV2_Exception_InvalidSchemaVersion() {
        WorkflowModel workflow = new WorkflowModel();
        workflow.getWorkflowDefinition().setSchemaVersion(-1);
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";

        try {
            parametersUtils.getTaskInputV2(null, workflow, taskId, taskDefinition);
        } catch (IllegalArgumentException e) {
            assertEquals("Unsupported schema version: -1", e.getMessage());
        }
    }

    @Test
    public void testGetTaskInputV2_EdgeCase_NullWorkflow() {
        Map<String, Object> input = new HashMap<>();
        TaskDef taskDefinition = new TaskDef();
        String taskId = "task1";

        Map<String, Object> result = parametersUtils.getTaskInputV2(input, null, taskId, taskDefinition);
        assertEquals(input, result);
    }

    @Test
    public void testGetTaskInputV2_EdgeCase_NullTaskDefinition() {
        Map<String, Object> input = new HashMap<>();
        WorkflowModel workflow = new WorkflowModel();

        Map<String, Object> result = parametersUtils.getTaskInputV2(input, workflow, "task1", null);
        assertEquals(input, result);
    }

    @Test
    public void testClone_Exception_InvalidInputTemplate() {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("key", new Object());

        try {
            parametersUtils.clone(inputTemplate);
        } catch (RuntimeException e) {
            assertEquals("Unable to clone input params", e.getMessage());
        }
    }

    @Test
    public void testReplace_EdgeCase_EmptyInput() {
        Map<String, Object> documentContext = new HashMap<>();

        Map<String, Object> result = parametersUtils.replace(new HashMap<>(), documentContext);
        assertEquals(new HashMap<>(), result);
    }

    @Test
    public void testReplace_EdgeCase_NullDocumentContext() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "${value}");

        Map<String, Object> result = parametersUtils.replace(input, null);
        assertEquals(input, result);
    }

    @Test
    public void testReplace_EdgeCase_NullKeyInDocumentContext() {
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put(null, "replacement");

        Map<String, Object> input = new HashMap<>();
        input.put("key", "${value}");

        Map<String, Object> result = parametersUtils.replace(input, documentContext);
        assertEquals(input, result);
    }

    @Test
    public void testReplace_EdgeCase_MissingKeyInDocumentContext() {
        Map<String, Object> documentContext = new HashMap<>();

        Map<String, Object> input = new HashMap<>();
        input.put("key", "${value}");

        Map<String, Object> result = parametersUtils.replace(input, documentContext);
        assertEquals(input, result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_EmptyParamString() {
        Map<String, Object> documentContext = new HashMap<>();

        String result = parametersUtils.replaceVariables("", documentContext, null);
        assertEquals("", result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_NullDocumentContext() {
        String paramString = "${value}";

        String result = parametersUtils.replaceVariables(paramString, null, null);
        assertEquals("${value}", result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_NullKeyInDocumentContext() {
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put(null, "replacement");

        String paramString = "${value}";

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("${value}", result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_MissingKeyInDocumentContext() {
        Map<String, Object> documentContext = new HashMap<>();

        String paramString = "${value}";

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("${value}", result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_EmptyParamPath() {
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("value", "replacement");

        String paramString = "${}";

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("", result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_EmptyParamPathComponent() {
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("value", "replacement");

        String paramString = "${value.}";

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("replacement.", result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_LeadingPeriodInParamPathComponent() {
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("value", "replacement");

        String paramString = "${.value}";

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("replacement", result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_TrailingPeriodInParamPathComponent() {
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("value", "replacement");

        String paramString = "${value.}";

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("replacement.", result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_MultipleAdjacentDollarSigns() {
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("value", "replacement");

        String paramString = "${$$value}";

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("$$replacement", result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_DoubleDollarSignsInParamPath() {
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("value", "replacement");

        String paramString = "${$value}";

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("replacement", result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_MultipleParamPaths() {
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("value", "replacement");
        documentContext.put("value1", "replacement1");

        String paramString = "${value}${value1}";

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("replacementreplacement1", result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_MissingParamPathComponent() {
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("value", "replacement");

        String paramString = "${value.";

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("${value.", result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_UnmatchedClosingCurlyBrace() {
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("value", "replacement");

        String paramString = "${value}";

        String result = parametersUtils.replaceVariables("${value", documentContext, null);
        assertEquals("${value", result);
    }

    @Test
    public void testReplaceVariables_EdgeCase_MissingOpeningCurlyBrace() {
        Map<String, Object> documentContext = new HashMap<>();
        documentContext.put("value", "replacement");

        String paramString = "value}";

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);
        assertEquals("value}", result);
    }

    @Test
    public void testGetWorkflowInput_EdgeCase_EmptyInputParams() {
        WorkflowDef workflowDef = new WorkflowDef();

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, new HashMap<>());
        assertEquals(new HashMap<>(), result);
    }

    @Test
    public void testGetWorkflowInput_EdgeCase_NullInputTemplate() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(null);

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");

        Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals(inputParams, result);
    }
}