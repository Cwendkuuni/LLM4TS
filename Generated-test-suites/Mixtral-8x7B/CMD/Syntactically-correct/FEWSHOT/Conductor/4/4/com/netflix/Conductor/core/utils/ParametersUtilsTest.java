import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import com.netflix.conductor.model.WorkflowModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParametersUtilsTest {

  private ParametersUtils parametersUtils;

  @Mock private ObjectMapper objectMapper;
  @Mock private EnvUtils envUtils;
  @Mock private TaskUtils taskUtils;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(ParametersUtils.class);

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    when(objectMapper.writeValueAsBytes(any())).thenReturn(new byte[0]);
    when(objectMapper.readValue(any(), any())).thenReturn(new HashMap<>());

    when(envUtils.isEnvironmentVariable(any())).thenReturn(false);
    when(envUtils.getSystemParametersValue(any(), any())).thenReturn("testValue");

    when(taskUtils.removeIterationFromTaskRefName(any())).thenReturn("testTask");

    this.parametersUtils = new ParametersUtils(objectMapper, envUtils, taskUtils);
  }

  @Test
  public void testGetTaskInput() {
    WorkflowModel workflow = mock(WorkflowModel.class);
    TaskDef taskDefinition = mock(TaskDef.class);

    Map<String, Object> inputParams = new HashMap<>();
    inputParams.put("testKey", "testValue");

    Map<String, Object> result =
        parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "testTaskId");

    assertEquals(1, result.size());
    assertTrue(result.containsKey("testKey"));
    assertEquals("testValue", result.get("testKey"));
  }

  @Test
  public void testGetTaskInputV2() {
    WorkflowModel workflow = mock(WorkflowModel.class);
    TaskDef taskDefinition = mock(TaskDef.class);

    Map<String, Object> inputParams = new HashMap<>();
    inputParams.put("testKey", "testValue");

    when(workflow.getInput()).thenReturn(Collections.singletonMap("workflowInputKey", "workflowInputValue"));
    when(workflow.getTasks()).thenReturn(Collections.singletonList(mock(TaskModel.class)));
    when(taskDefinition.getInputTemplate()).thenReturn(Collections.singletonMap("taskInputKey", "taskInputValue"));

    Map<String, Object> result =
        parametersUtils.getTaskInputV2(inputParams, workflow, "testTaskId", taskDefinition);

    assertEquals(2, result.size());
    assertTrue(result.containsKey("testKey"));
    assertEquals("testValue", result.get("testKey"));
    assertTrue(result.containsKey("taskInputKey"));
    assertEquals("taskInputValue", result.get("taskInputKey"));
  }

  @Test
  public void testReplace() {
    String input = "This is a ${testKey} test string";
    Object value = "replacement";

    Map<String, Object> inputParams = new HashMap<>();
    inputParams.put("testKey", value);

    DocumentContext documentContext = JsonPath.parse("{\"testKey\": \"replacementValue\"}");

    String result = parametersUtils.replace(input, documentContext, null);

    assertEquals("This is a replacementValue test string", result);
  }

  @Test
  public void testReplaceVariables() {
    String input = "This is a ${testKey} test string";
    String taskId = "testTask";

    Map<String, Object> inputParams = new HashMap<>();
    inputParams.put("testKey", "${testPath}");

    DocumentContext documentContext =
        JsonPath.parse("{\"testPath\": \"replacementValue\"}");

    String result = parametersUtils.replaceVariables(input, documentContext, taskId);

    assertEquals("This is a replacementValue test string", result);
  }

  @Test
  public void testReplaceList() {
    List<String> input = Collections.singletonList("This is a ${testKey} test string");

    List<Object> replacedList =
        parametersUtils.replaceList(input, "testTask", JsonPath.parse("{\"testKey\": \"replacementValue\"}"));

    assertEquals(1, replacedList.size());
    assertEquals("This is a replacementValue test string", replacedList.get(0));
  }

  @Test
  public void testGetWorkflowInput() {
    WorkflowDef workflowDef = mock(WorkflowDef.class);

    Map<String, Object> inputParams = new HashMap<>();
    inputParams.put("testKey", "testValue");

    Map<String, Object> result =
        parametersUtils.getWorkflowInput(workflowDef, inputParams);

    assertEquals(1, result.size());
    assertTrue(result.containsKey("testKey"));
    assertEquals("testValue", result.get("testKey"));
  }
}