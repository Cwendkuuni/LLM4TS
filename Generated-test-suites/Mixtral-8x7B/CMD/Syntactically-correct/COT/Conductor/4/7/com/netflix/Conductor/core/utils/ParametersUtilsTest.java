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
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Configuration configuration = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    private ParametersUtils parametersUtils;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;
    private WorkflowModel workflowModel;
    private EnvUtils envUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);

        workflowDef = new WorkflowDef();
        workflowDef.setSchemaVersion(1);

        taskDef = new TaskDef();
        taskDef.setInputTemplate(new HashMap<>());

        workflowModel = new WorkflowModel();
        workflowModel.setInput(new HashMap<>());
        workflowModel.setTasks(new LinkedList<>());
        workflowModel.getTasks().add(new TaskModel());

        envUtils = mock(EnvUtils.class);
        parametersUtils.setEnvUtils(envUtils);
    }

    @Test
    public void testGetTaskInput_schemaVersion1() {
        when(envUtils.isEnvironmentVariable("paramPath")).thenReturn(false);
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), configuration);
        Map<String, Object> inputParams = parametersUtils.getTaskInput(
                new HashMap<>(), workflowModel, taskDef, "taskId");

        assertEquals(0, inputParams.size());

        workflowModel.getTasks().get(0).setInputData(new HashMap<>());
        workflowModel.getTasks().get(0).getInputData().put("paramName", "paramValue");

        inputParams = parametersUtils.getTaskInput(
                new HashMap<>(), workflowModel, taskDef, "taskId");

        assertEquals(1, inputParams.size());
        assertTrue(inputParams.containsKey("paramName"));
        assertEquals("paramValue", inputParams.get("paramName"));

        workflowModel.getTasks().get(0).setOutputData(new HashMap<>());
        workflowModel.getTasks().get(0).getOutputData().put("paramName", "paramValue");

        inputParams = parametersUtils.getTaskInput(
                new HashMap<>(), workflowModel, taskDef, "taskId");

        assertEquals(1, inputParams.size());
        assertTrue(inputParams.containsKey("paramName"));
        assertEquals("paramValue", inputParams.get("paramName"));

        workflowDef.setSchemaVersion(2);
        inputParams = parametersUtils.getTaskInput(
                new HashMap<>(), workflowModel, taskDef, "taskId");

        verify(envUtils, times(1)).isEnvironmentVariable("paramPath");
        verifyNoMoreInteractions(envUtils);
    }

    @Test
    public void testGetTaskInput_schemaVersion2() {
        when(envUtils.isEnvironmentVariable("paramPath")).thenReturn(false);
        Map<String, Object> inputParams = parametersUtils.getTaskInputV2(
                null, workflowModel, "taskId", taskDef);

        assertEquals(0, inputParams.size());

        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("paramName", "paramValue");
        taskDef.setInputTemplate(inputTemplate);

        inputParams = parametersUtils.getTaskInputV2(
                null, workflowModel, "taskId", taskDef);

        assertEquals(1, inputParams.size());
        assertTrue(inputParams.containsKey("paramName"));
        assertEquals("paramValue", inputParams.get("paramName"));

        workflowModel.setInput(new HashMap<>());
        workflowModel.getInput().put("paramName", "paramValue");

        inputParams = parametersUtils.getTaskInputV2(
                null, workflowModel, "taskId", taskDef);

        assertEquals(1, inputParams.size());
        assertTrue(inputParams.containsKey("paramName"));
        assertEquals("paramValue", inputParams.get("paramName"));

        workflowModel.setTasks(new LinkedList<>());
        workflowModel.setTasks().add(new TaskModel());
        workflowModel.getTasks().get(0).setInputData(new HashMap<>());
        workflowModel.getTasks().get(0).getInputData().put("paramName", "paramValue");

        inputParams = parametersUtils.getTaskInputV2(
                null, workflowModel, "taskId", taskDef);

        assertEquals(1, inputParams.size());
        assertTrue(inputParams.containsKey("paramName"));
        assertEquals("paramValue", inputParams.get("paramName"));

        workflowModel.getTasks().get(0).setOutputData(new HashMap<>());
        workflowModel.getTasks().get(0).getOutputData().put("paramName", "paramValue");

        inputParams = parametersUtils.getTaskInputV2(
                null, workflowModel, "taskId", taskDef);

        assertEquals(1, inputParams.size());
        assertTrue(inputParams.containsKey("paramName"));
        assertEquals("paramValue", inputParams.get("paramName"));
    }

    @Test
    public void testClone() {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("paramName", "paramValue");

        Map<String, Object> clonedMap = parametersUtils.clone(inputTemplate);

        assertEquals(1, clonedMap.size());
        assertTrue(clonedMap.containsKey("paramName"));
        assertEquals("paramValue", clonedMap.get("paramName"));
    }

    @Test
    public void testReplace_String() {
        DocumentContext documentContext = JsonPath.parse(new HashMap<>(), configuration);

        String replaced = parametersUtils.replace("param");
        assertEquals("param", replaced);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param", "paramValue");
        documentContext = JsonPath.parse(inputMap, configuration);

        replaced = parametersUtils.replace("$.param", documentContext, "taskId");
        assertEquals("paramValue", replaced);

        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("paramName", "paramValue");
        taskDef.setInputTemplate(inputTemplate);

        replaced = parametersUtils.replace("param", documentContext, "taskId");
        assertEquals("param", replaced);

        replaced = parametersUtils.replace("paramName", documentContext, "taskId");
        assertEquals("paramValue", replaced);

        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("paramName", "workflowValue");
        workflowModel.setInput(workflowInput);

        replaced = parametersUtils.replace("paramName", documentContext, "taskId");
        assertEquals("workflowValue", replaced);
    }

    @Test
    public void testReplace_map() {
        Map<String, Object> input = new HashMap<>();
        input.put("paramName", "paramValue");

        Map<String, Object> replaced = parametersUtils.replace(input, new HashMap<>());
        assertEquals(1, replaced.size());
        assertTrue(replaced.containsKey("paramName"));
        assertEquals("paramValue", replaced.get("paramName"));

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param", "paramValue");
        DocumentContext documentContext = JsonPath.parse(inputMap, configuration);

        replaced = parametersUtils.replace(input, documentContext);
        assertEquals(1, replaced.size());
        assertTrue(replaced.containsKey("paramName"));
        assertEquals("paramValue", replaced.get("paramName"));

        replaced = parametersUtils.replace(input, documentContext, "taskId");
        assertEquals(1, replaced.size());
        assertTrue(replaced.containsKey("paramName"));
        assertEquals("paramValue", replaced.get("paramName"));

        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("paramName", "paramValue");
        taskDef.setInputTemplate(inputTemplate);

        replaced = parametersUtils.replace(input, documentContext, "taskId");
        assertEquals(1, replaced.size());
        assertTrue(replaced.containsKey("paramName"));
        assertEquals("paramValue", replaced.get("paramName"));

        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("paramName", "workflowValue");
        workflowModel.setInput(workflowInput);

        replaced = parametersUtils.replace(input, documentContext, "taskId");
        assertEquals(1, replaced.size());
        assertTrue(replaced.containsKey("paramName"));
        assertEquals("workflowValue", replaced.get("paramName"));
    }

    @Test
    public void testReplace_list() {
        List<Object> values = new LinkedList<>();
        values.add("paramValue");

        List<Object> replaced = parametersUtils.replaceList(values, "taskId", new HashMap<>());
        assertEquals(1, replaced.size());
        assertEquals("paramValue", replaced.get(0));

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param", "paramValue");
        DocumentContext documentContext = JsonPath.parse(inputMap, configuration);

        replaced = parametersUtils.replaceList(values, "taskId", documentContext);
        assertEquals(1, replaced.size());
        assertEquals("paramValue", replaced.get(0));

        replaced = parametersUtils.replaceList(values, null, documentContext);
        assertEquals(1, replaced.size());
        assertEquals("paramValue", replaced.get(0));

        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("paramName", "paramValue");
        taskDef.setInputTemplate(inputTemplate);

        replaced = parametersUtils.replaceList(values, "taskId", documentContext);
        assertEquals(1, replaced.size());
        assertEquals("paramValue", replaced.get(0));

        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("paramName", "workflowValue");
        workflowModel.setInput(workflowInput);

        replaced = parametersUtils.replaceList(values, "taskId", documentContext);
        assertEquals(1, replaced.size());
        assertEquals("workflowValue", replaced.get(0));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "param";
        DocumentContext documentContext = JsonPath.parse(new HashMap<>(), configuration);
        Object replaced = parametersUtils.replaceVariables(paramString, documentContext, "taskId");
        assertEquals("param", replaced);

        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("param", "paramValue");
        documentContext = JsonPath.parse(inputMap, configuration);

        replaced = parametersUtils.replaceVariables(paramString, documentContext, "taskId");
        assertEquals("param", replaced);

        replaced = parametersUtils.replaceVariables("$.param", documentContext, "taskId");
        assertEquals("paramValue", replaced);

        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("paramName", "paramValue");
        taskDef.setInputTemplate(inputTemplate);

        replaced = parametersUtils.replaceVariables("param", documentContext, "taskId");
        assertEquals("param", replaced);

        replaced = parametersUtils.replaceVariables("paramName", documentContext, "taskId");
        assertEquals("paramValue", replaced);

        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("paramName", "workflowValue");
        workflowModel.setInput(workflowInput);

        replaced = parametersUtils.replaceVariables("paramName", documentContext, "taskId");
        assertEquals("workflowValue", replaced);

        replaced = parametersUtils.replaceVariables("${paramPath}", documentContext, "taskId");
        assertEquals("paramPath", replaced);

        replaced = parametersUtils.replaceVariables("$${paramPath}", documentContext, "taskId");
        assertEquals("${paramPath}", replaced);

        replaced = parametersUtils.replaceVariables("$$$", documentContext, "taskId");
        assertEquals("$$$", replaced);

        replaced = parametersUtils.replaceVariables("${paramPath1}.${paramPath2}", documentContext, "taskId");
        assertEquals("${paramPath1}.${paramPath2}", replaced);

        replaced = parametersUtils.replaceVariables("$.paramPath1.paramPath2", documentContext, "taskId");
        assertEquals("paramPath1.paramPath2", replaced);

        replaced = parametersUtils.replaceVariables("$$paramPath", documentContext, "taskId");
        assertEquals("$paramPath", replaced);
    }

    @Test
    public void testGetWorkflowInput() {
        when(envUtils.isEnvironmentVariable("paramPath")).thenReturn(false);
        DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), configuration);
        Map<String, Object> inputParams = parametersUtils.getWorkflowInput(workflowDef, new HashMap<>());

        assertEquals(0, inputParams.size());

        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("paramName", "paramValue");
        workflowDef.setInputTemplate(inputTemplate);

        inputParams = parametersUtils.getWorkflowInput(workflowDef, new HashMap<>());

        assertEquals(1, inputParams.size());
        assertTrue(inputParams.containsKey("paramName"));
        assertEquals("paramValue", inputParams.get("paramName"));

        Map<String, Object> workflowInput = new HashMap<>();
        workflowInput.put("paramName", "workflowValue");
        workflowModel.setInput(workflowInput);

        inputParams = parametersUtils.getWorkflowInput(workflowDef, new HashMap<>());

        assertEquals(1, inputParams.size());
        assertTrue(inputParams.containsKey("paramName"));
        assertEquals("workflowValue", inputParams.get("paramName"));
    }
}