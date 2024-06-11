import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import java.util.Map.Entry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class ParametersUtilsTest {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ParametersUtilsTest.class);

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private TaskDef taskDefinition;
    private WorkflowModel workflow;
    private Configuration config;
    private DocumentContext documentContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
        taskDefinition = new TaskDef();
        workflow = mock(WorkflowModel.class);
        config = Configuration.defaultConfiguration();
        documentContext = JsonPath.using(config).parse("{}");
    }

    @Test
    public void testGetTaskInputWithNullInputParams() {
        Map<String, Object> result =
                parametersUtils.getTaskInput(null, workflow, taskDefinition, "taskId");
        assertEquals(new HashMap<>(), result);
    }

    @Test
    public void testGetTaskInputWithNullTaskDefinition() {
        when(workflow.getWorkflowDefinition().getSchemaVersion()).thenReturn(1);
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "workflow.input.key");
        Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, null, "taskId");
        assertEquals(inputParams, result);
    }

    @Test
    public void testGetTaskInputV2WithNullInput() {
        when(workflow.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);
        Map<String, Object> result =
                parametersUtils.getTaskInputV2(null, workflow, "taskId", taskDefinition);
        assertEquals(new HashMap<>(), result);
    }

    @Test
    public void testGetTaskInputV2WithNullTaskDefinitionInputTemplate() {
        when(workflow.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);
        Map<String, Object> input = new HashMap<>();
        input.put("key", "workflow.input.key");
        Map<String, Object> result =
                parametersUtils.getTaskInputV2(input, workflow, "taskId", null);
        assertEquals(input, result);
    }

    @Test
    public void testGetTaskInputV1WithNullWorkflow() {
        when(workflow.getWorkflowDefinition().getSchemaVersion()).thenReturn(1);
        Map<String, Object> input = new HashMap<>();
        input.put("key", "workflow.input.key");
        Map<String, Object> result =
                parametersUtils.getTaskInputV1(null, input);
        assertEquals(input, result);
    }

    @Test
    public void testGetWorkflowInputWithNullWorkflowDef() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "workflow.input.key");
        Map<String, Object> result =
                parametersUtils.getWorkflowInput(null, input);
        assertEquals(input, result);
    }

    @Test
    public void testReplaceListWithNull() {
        List<Object> list = null;
        String taskId = "taskId";
        DocumentContext dc = JsonPath.using(config).parse("{}");
        Object replacedList = parametersUtils.replaceList(list, taskId, dc);
        assertEquals(list, replacedList);
    }

    @Test
    public void testReplaceListWithEmptyList() {
        List<Object> list = new LinkedList<>();
        String taskId = "taskId";
        DocumentContext dc = JsonPath.using(config).parse("{}");
        Object replacedList = parametersUtils.replaceList(list, taskId, dc);
        assertEquals(list, replacedList);
    }

    @Test
    public void testReplaceListWithListOfStrings() {
        List<Object> inputList = new LinkedList<>();
        inputList.add("workflow.input.key");
        inputList.add("workflow.output.key");
        String taskId = "taskId";
        documentContext.set("workflow.input.key", "replaced-value-1");
        documentContext.set("workflow.output.key", "replaced-value-2");
        Object replacedList = parametersUtils.replaceList(inputList, taskId, documentContext);
        assertEquals(
                List.of("replaced-value-1", "replaced-value-2"), replacedList);
    }

    @Test
    public void testReplaceListWithListOfMaps() {
        List<Object> inputList = new LinkedList<>();
        inputList.add(Map.of("a", "workflow.input.key"));
        inputList.add(Map.of("b", "workflow.output.key"));
        String taskId = "taskId";
        documentContext.set("workflow.input.key", "replaced-value-1");
        documentContext.set("workflow.output.key", "replaced-value-2");
        Object replacedList = parametersUtils.replaceList(inputList, taskId, documentContext);
        assertEquals(
                List.of(Map.of("a", "replaced-value-1"), Map.of("b", "replaced-value-2")),
                replacedList);
    }

    @Test
    public void testReplaceWithNull() {
        String input = null;
        DocumentContext documentContext = JsonPath.using(config).parse("{}");
        Object replacedString = parametersUtils.replace(input, documentContext, "taskId");
        assertNull(replacedString);
    }

    @Test
    public void testReplaceWithEmptyString() {
        String input = "";
        DocumentContext documentContext = JsonPath.using(config).parse("{}");
        Object replacedString = parametersUtils.replace(input, documentContext, "taskId");
        assertEquals("", replacedString);
    }

    @Test
    public void testReplaceWithString() {
        String input = "workflow.input.key";
        documentContext.set("workflow.input.key", "replaced-value");
        Object replacedString = parametersUtils.replace(input, documentContext, "taskId");
        assertEquals("replaced-value", replacedString);
    }

    @Test
    public void testReplaceWithStringAndTaskId() {
        String input = "task.correlationId";
        documentContext.set("task.correlationId", "replaced-value");
        Object replacedString = parametersUtils.replace(input, documentContext, "taskId");
        assertEquals("replaced-value", replacedString);
    }

    @Test
    public void testReplaceWithMap() {
        Map<String, Object> input = new HashMap<>();
        input.put("key", "workflow.input.key");
        Object replacedMap = parametersUtils.replace(input, documentContext, "taskId");
        assertEquals(Map.of("key", "workflow.input.key"), replacedMap);
    }

    @Test
    public void testReplaceWithList() {
        List<Object> input = new LinkedList<>();
        input.add("workflow.input.key");
        input.add("workflow.output.key");
        Object replacedList = parametersUtils.replace(input, documentContext, "taskId");
        assertEquals(List.of("workflow.input.key", "workflow.output.key"), replacedList);
    }
}