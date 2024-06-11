import static org.junit.Assert.*;
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
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class ParametersUtilsTest {

  private ParametersUtils parametersUtils;
  private ObjectMapper objectMapper;
  private EnvUtils envUtils;
  private TaskUtils taskUtils;
  private static final Logger LOGGER =
      LoggerFactory.getLogger(ParametersUtils.class);

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    objectMapper = mock(ObjectMapper.class);
    envUtils = mock(EnvUtils.class);
    taskUtils = mock(TaskUtils.class);
    when(objectMapper.writeValueAsBytes(any())).thenReturn(new byte[0]);
    when(objectMapper.readValue(any(), any())).thenReturn(new HashMap<>());
    when(envUtils.isEnvironmentVariable(anyString())).thenReturn(false);
    when(envUtils.getSystemParametersValue(anyString(), anyString())).thenReturn("");
    when(taskUtils.removeIterationFromTaskRefName(anyString())).then(invocation -> invocation.getArgument(0));
    parametersUtils =
        new ParametersUtils(objectMapper) {
          @Override
          LoggerFactory getLogger() {
            return LoggerFactory.getLogger(ParametersUtils.class);
          }

          @Override
          EnvUtils getEnvUtils() {
            return envUtils;
          }

          @Override
          TaskUtils getTaskUtils() {
            return taskUtils;
          }
        };
  }

  @Test
  public void testGetTaskInput() {
    WorkflowModel workflowModel = createWorkflowModel();
    Map<String, Object> inputParams = createInputParams();
    Map<String, Object> taskInput = parametersUtils.getTaskInput(inputParams, workflowModel, null, "1");
    assertEquals(3, taskInput.size());
    assertEquals("input value", taskInput.get("inputKey"));
    assertEquals("output value", taskInput.get("outputKey"));
    assertEquals("workflow value", taskInput.get("workflowValue"));
  }

  @Test
  public void testGetTaskInputV2() {
    WorkflowModel workflowModel = createWorkflowModel();
    Map<String, Object> inputParams = createInputParams();
    Map<String, Object> taskInput =
        parametersUtils.getTaskInputV2(inputParams, workflowModel, "1", null);
    assertEquals(6, taskInput.size());
    assertEquals("input value", taskInput.get("inputKey"));
    assertEquals("output value", taskInput.get("outputKey"));
    assertEquals("workflow value", taskInput.get("workflowValue"));
    assertEquals(0, taskInput.get("task1").get("retryCount"));
    assertEquals("task1 input value", taskInput.get("task1").get("input"));
    assertEquals("task1 output value", taskInput.get("task1").get("output"));
  }

  @Test
  public void testGetTaskInputV2_WithTaskInputTemplate() {
    WorkflowModel workflowModel = createWorkflowModel();
    Map<String, Object> inputParams = createInputParams();
    TaskDef taskDef = new TaskDef();
    taskDef.setInputTemplate(createInputTemplate());
    Map<String, Object> taskInput =
        parametersUtils.getTaskInputV2(inputParams, workflowModel, "1", taskDef);
    assertEquals(7, taskInput.size());
    assertEquals("input value", taskInput.get("inputKey"));
    assertEquals("output value", taskInput.get("outputKey"));
    assertEquals("workflow value", taskInput.get("workflowValue"));
    assertEquals(0, taskInput.get("task1").get("retryCount"));
    assertEquals("task1 input value", taskInput.get("task1").get("input"));
    assertEquals("task1 output value", taskInput.get("task1").get("output"));
    assertEquals("task input template", taskInput.get("task1").get("inputKeyFromTemplate"));
  }

  @Test
  public void testReplace() {
    Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse("{\"a\":{\"b\":\"value\"}}", option);
    Map<String, Object> input = new HashMap<>();
    input.put("a.b", "{{a.b}}");
    Map<String, Object> replaced = parametersUtils.replace(input, documentContext, null);
    assertEquals("value", replaced.get("a.b"));
  }

  @Test
  public void testReplace_List() {
    Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse("{\"a\":[{\"b\":\"value\"}, {\"b\":\"value2\"}]}", option);
    List<Object> input = new LinkedList<>();
    input.add("{{a[0].b}}");
    input.add("{{a[1].b}}");
    List<Object> replaced =
        (List<Object>) parametersUtils.replaceList(input, null, documentContext);
    assertEquals("value", replaced.get(0));
    assertEquals("value2", replaced.get(1));
  }

  @Test
  public void testReplaceVariables() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse("{\"a\":{\"b\":\"value\"}}", option);
    String paramString = "{{a.b}}";
    String replaced =
        (String) parametersUtils.replaceVariables(paramString, documentContext, null);
    assertEquals("value", replaced);
  }

  @Test
  public void testReplaceVariables_Depth() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse("{\"a\":{\"b\":\"value\"}}", option);
    String paramString = "{{a.{{b}}}}";
    String replaced =
        (String) parametersUtils.replaceVariables(paramString, documentContext, null, 1);
    assertEquals("value", replaced);
  }

  @Test
  public void testReplaceVariables_NoMatch() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse("{\"a\":{\"b\":\"value\"}}", option);
    String paramString = "no match {{a.b}}";
    String replaced =
        (String) parametersUtils.replaceVariables(paramString, documentContext, null);
    assertEquals("no match {{a.b}}", replaced);
  }

  @Test
  public void testReplaceVariables_EnvVariable() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse("{\"a\":{\"b\":\"value\"}}", option);
    when(envUtils.isEnvironmentVariable("a.b")).thenReturn(true);
    String paramString = "{{a.b}}";
    String replaced =
        (String) parametersUtils.replaceVariables(paramString, documentContext, null);
    assertEquals("", replaced);
  }

  @Test
  public void testReplaceVariables_EnvValue() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse("{\"a\":{\"b\":\"value\"}}", option);
    when(envUtils.isEnvironmentVariable("a.b")).thenReturn(true);
    when(envUtils.getSystemParametersValue("a.b", null)).thenReturn("env value");
    String paramString = "{{a.b}}";
    String replaced =
        (String) parametersUtils.replaceVariables(paramString, documentContext, null);
    assertEquals("env value", replaced);
  }

  @Test
  public void testReplaceVariables_NullEnvValue() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse("{\"a\":{\"b\":\"value\"}}", option);
    when(envUtils.isEnvironmentVariable("a.b")).thenReturn(true);
    when(envUtils.getSystemParametersValue("a.b", null)).thenReturn(null);
    String paramString = "{{a.b}}";
    String replaced =
        (String) parametersUtils.replaceVariables(paramString, documentContext, null);
    assertEquals("", replaced);
  }

  @Test
  public void testGetWorkflowInput() {
    WorkflowDef workflowDef = createWorkflowDef();
    Map<String, Object> inputParams = createInputParams();
    Map<String, Object> workflowInput =
        parametersUtils.getWorkflowInput(workflowDef, inputParams);
    assertEquals(2, workflowInput.size());
    assertEquals("workflow input value", workflowInput.get("workflowInputKey"));
    assertEquals("workflow output value", workflowInput.get("workflowOutputKey"));
  }

  private Map<String, Object> createInputTemplate() {
    Map<String, Object> inputTemplate = new HashMap<>();
    inputTemplate.put("inputKeyFromTemplate", "task input template");
    return inputTemplate;
  }

  private WorkflowDef createWorkflowDef() {
    WorkflowDef workflowDef = new WorkflowDef();
    workflowDef.setInputTemplate(createInputTemplate());
    return workflowDef;
  }

  private WorkflowModel createWorkflowModel() {
    WorkflowModel workflowModel = new WorkflowModel();
    workflowModel.setInput(createInput());
    workflowModel.setOutput(createOutput());
    workflowModel.setStatus("workflow status");
    workflowModel.setWorkflowId("workflow id");
    workflowModel.setParentWorkflowId("parent workflow id");
    workflowModel.setParentWorkflowTaskId("parent workflow task id");
    workflowModel.setWorkflowName("workflow name");
    workflowModel.setWorkflowVersion("workflow version");
    workflowModel.setCorrelationId("correlation id");
    workflowModel.setReasonForIncompletion("reason for incompletion");
    workflowModel.setSchemaVersion(2);
    workflowModel.setVariables(createVariables());
    workflowModel.setTasks(createTasks());
    return workflowModel;
  }

  private List<TaskModel> createTasks() {
    List<TaskModel> tasks = new LinkedList<>();
    tasks.add(createTaskModel("task1", "task1 input data", "task1 output data"));
    tasks.add(createTaskModel("task2", "task2 input data", "task2 output data"));
    return tasks;
  }

  private TaskModel createTaskModel(
      String referenceTaskName, Object inputData, Object outputData) {
    TaskModel taskModel = new TaskModel();
    taskModel.setReferenceTaskName(referenceTaskName);
    taskModel.setInputData(createInput(inputData));
    taskModel.setOutputData(createOutput(outputData));
    return taskModel;
  }

  private Map<String, Object> createOutput(Object output) {
    Map<String, Object> outputMap = new HashMap<>();
    outputMap.put("outputKey", output);
    return outputMap;
  }

  private Map<String, Object> createInput(Object input) {
    Map<String, Object> inputMap = new HashMap<>();
    inputMap.put("inputKey", input);
    return inputMap;
  }

  private Map<String, Object> createInputParams() {
    Map<String, Object> inputParams = new HashMap<>();
    inputParams.put("inputKey", "input value");
    inputParams.put("outputKey", "output value");
    inputParams.put("workflowValue", "workflow value");
    return inputParams;
  }

  private Map<String, Object> createVariables() {
    Map<String, Object> variables = new HashMap<>();
    variables.put("variableKey", "variable value");
    return variables;
  }

  private Map<String, Object> createInput() {
    Map<String, Object> input = new HashMap<>();
    input.put("inputKey", "input value");
    return input;
  }

  private Map<String, Object> createOutput() {
    Map<String, Object> output = new HashMap<>();
    output.put("outputKey", "output value");
    return output;
  }
}