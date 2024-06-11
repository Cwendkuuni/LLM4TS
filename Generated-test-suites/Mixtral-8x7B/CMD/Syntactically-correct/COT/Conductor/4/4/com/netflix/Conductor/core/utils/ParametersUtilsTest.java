import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    // Helper methods for test cases

    private WorkflowModel createWorkflowModel() {
        return new WorkflowModel()
                .setInput(new HashMap<>())
                .setOutput(new HashMap<>())
                .setStatus("COMPLETED")
                .setWorkflowId("workflow-1")
                .setParentWorkflowId("parent-workflow-1")
                .setParentWorkflowTaskId("parent-task-1")
                .setWorkflowName("test-workflow")
                .setWorkflowVersion(1)
                .setCorrelationId("correlation-id-1")
                .setReasonForIncompletion(null)
                .setSchemaVersion(1)
                .setVariables(new HashMap<>());
    }

    private TaskDef createTaskDef() {
        return new TaskDef()
                .setInputTemplate(new HashMap<>());
    }

    private Map<String, Object> createInputParams() {
        return new HashMap<>();
    }

    // Test cases for getTaskInput()

    @Test
    public void testGetTaskInput_SchemaVersion1() throws IOException {
        WorkflowModel workflow = createWorkflowModel();
        TaskDef taskDefinition = createTaskDef();
        Map<String, Object> inputParams = createInputParams();

        when(objectMapper.writeValueAsBytes(inputParams)).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), eq(new TypeReference<Map<String, Object>>() {})))
                .thenReturn(new HashMap<>());

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "task-1");

        assertEquals(0, result.size());
        verify(objectMapper, times(1)).writeValueAsBytes(inputParams);
        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(new TypeReference<Map<String, Object>>() {}));
    }

    @Test
    public void testGetTaskInput_SchemaVersion2() throws IOException {
        WorkflowModel workflow = createWorkflowModel();
        workflow.setWorkflowDefinition(new WorkflowDef().setSchemaVersion(2));
        TaskDef taskDefinition = createTaskDef();
        Map<String, Object> inputParams = createInputParams();

        when(objectMapper.writeValueAsBytes(inputParams)).thenReturn(new byte[0]);
        when(objectMapper.readValue(any(byte[].class), eq(new TypeReference<Map<String, Object>>() {})))
                .thenReturn(new HashMap<>());

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "task-1");

        assertEquals(0, result.size());
        verify(objectMapper, times(1)).writeValueAsBytes(inputParams);
        verify(objectMapper, times(1)).readValue(any(byte[].class), eq(new TypeReference<Map<String, Object>>() {}));
    }

    // Test cases for replace()

    @Test
    public void testReplace_String() {
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("workflow", new HashMap<>());
        inputMap.put("task-1", new HashMap<>());

        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(inputMap, option);

        String result =
                parametersUtils.replace(
                        "This is a ${workflow.input} with a ${task-1.input} and ${nonexistent}",
                        documentContext,
                        "task-1");

        assertEquals("This is a {} with a {} and ${nonexistent}", result);
    }

    // Test cases for replaceVariables()

    @Test
    public void testReplaceVariables_String_DocumentContext_Null() {
        String paramString = "This is a ${workflow.input} with a ${task-1.input} and ${nonexistent}";
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), option);

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals(paramString, result);
    }

    @Test
    public void testReplaceVariables_String_DocumentContext_String() {
        String paramString = "This is a ${workflow.input} with a ${task-1.input} and ${nonexistent}";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("workflow", new HashMap<>());
        inputMap.put("task-1", new HashMap<>());
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(inputMap, option);

        String result = parametersUtils.replaceVariables(paramString, documentContext, "task-1");

        assertEquals(paramString, result);
    }

    @Test
    public void testReplaceVariables_String_DocumentContext_String_Depth() {
        String paramString = "This is a ${workflow.input} with a ${task-1.input} and ${nonexistent}";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("workflow", new HashMap<>());
        inputMap.put("task-1", new HashMap<>());
        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext = JsonPath.parse(inputMap, option);

        String result =
                parametersUtils.replaceVariables(paramString, documentContext, "task-1", 1);

        assertEquals(paramString, result);
    }

    // Test cases for getWorkflowInput()

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef().setInputTemplate(new HashMap<>());
        Map<String, Object> inputParams = new HashMap<>();

        Map<String, Object> result =
                parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals(0, result.size());
    }
}