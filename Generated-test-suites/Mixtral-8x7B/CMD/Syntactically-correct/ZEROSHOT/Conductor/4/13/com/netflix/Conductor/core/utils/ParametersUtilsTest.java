import com.fasterxml.jackson.core.type.TypeReference;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParametersUtilsTest {

    @InjectMocks
    private ParametersUtils parametersUtils;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private EnvUtils envUtils;

    @Test
    public void testGetTaskInput() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskDef taskDef = mock(TaskDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("param1", "value1");
        inputParams.put("param2", "workflow.input.param3");
        inputParams.put("param4", "task.input.param5");
        inputParams.put("param6", "task.output.param7");
        Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
        assertEquals("value3", taskInput.get("param2"));
        assertEquals("value5", taskInput.get("param4"));
        assertEquals("value7", taskInput.get("param6"));
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        WorkflowModel workflowModel = mock(WorkflowModel.class);
        TaskDef taskDef = mock(TaskDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("param1", "value1");
        inputParams.put("param2", "${workflow.input.param3}");
        inputParams.put("param4", "${task.input.param5}");
        inputParams.put("param6", "${task.output.param7}");
        Map<String, Object> taskInput = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);
        assertEquals("value3", taskInput.get("param2"));
        assertEquals("value5", taskInput.get("param4"));
        assertEquals("value7", taskInput.get("param6"));
    }

    @Test
    public void testClone() {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("param1", "value1");
        Map<String, Object> clonedMap = parametersUtils.clone(inputTemplate);
        assertEquals(clonedMap.get("param1"), "value1");
    }

    @Test
    public void testReplace() {
        Map<String, Object> input = new HashMap<>();
        input.put("param1", "${workflow.input.param2}");
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("workflow", new HashMap<>());
        inputMap.get("workflow").put("input", new HashMap<>());
        inputMap.get("workflow").put("input").put("param2", "value2");
        Map<String, Object> replacedInput = parametersUtils.replace(input, inputMap);
        assertEquals(replacedInput.get("param1"), "value2");
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "${workflow.input.param2}";
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("workflow", new HashMap<>());
        inputMap.put("workflow").put("input", new HashMap<>());
        inputMap.get("workflow").put("input").put("param2", "value2");
        String replacedParamString = parametersUtils.replaceVariables(paramString, inputMap, null).toString();
        assertEquals(replacedParamString, "value2");
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = mock(WorkflowDef.class);
        Map<String, Object> inputParams = new HashMap<>();
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("param1", "value1");
        when(workflowDef.getInputTemplate()).thenReturn(inputTemplate);
        Map<String, Object> workflowInput = parametersUtils.getWorkflowInput(workflowDef, inputParams);
        assertEquals(workflowInput.get("param1"), "value1");
    }
}