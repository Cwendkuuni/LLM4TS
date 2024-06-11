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
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ParametersUtilsTest {

  @Mock private ObjectMapper objectMapper;
  @Mock private EnvUtils envUtils;
  @Mock private TaskUtils taskUtils;

  private ParametersUtils parametersUtils;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    when(objectMapper.writeValueAsBytes(anyMap())).thenReturn(new byte[0]);
    when(objectMapper.readValue(anyByteArray(), any(TypeReference.class)))
        .thenAnswer(
            invocationOnMock -> {
              TypeReference typeReference = invocationOnMock.getArgument(1);
              return (Map<String, Object>) typeReference.getType().getClass();
            });

    when(envUtils.isEnvironmentVariable(anyString()))
        .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0].toString().startsWith("$"));
    when(envUtils.getSystemParametersValue(anyString(), anyString()))
        .thenReturn("env_value");

    parametersUtils = new ParametersUtils(objectMapper, envUtils, taskUtils);
  }

  @Test
  public void testGetTaskInput() {
    Map<String, Object> workflowInput = new HashMap<>();
    workflowInput.put("inputParam1", "workflow_value1");
    workflowInput.put("inputParam2", "workflow_value2");

    Map<String, Object> taskInputTemplate = new HashMap<>();
    taskInputTemplate.put("inputParam3", "task_template_value");

    TaskDef taskDef = new TaskDef();
    taskDef.setInputTemplate(taskInputTemplate);

    WorkflowModel workflowModel = new WorkflowModel();
    workflowModel.setInput(workflowInput);
    workflowModel.setTasks(
        Collections.singletonList(
            new TaskModel()
                .setReferenceTaskName("task1")
                .setInputData(Collections.singletonMap("inputParam4", "task_data_value"))
                .setOutputData(Collections.singletonMap("outputParam1", "task_output_value"))));

    Map<String, Object> result =
        parametersUtils.getTaskInput(
            null, workflowModel, taskDef, "task1", 1, new HashMap<>(), new HashMap<>());

    assertEquals("workflow_value1", result.get("inputParam1"));
    assertEquals("workflow_value2", result.get("inputParam2"));
    assertEquals("task_template_value", result.get("inputParam3"));
    assertEquals("task_data_value", result.get("inputParam4"));
  }

  @Test
  public void testGetTaskInputV2() {
    Map<String, Object> workflowInput = new HashMap<>();
    workflowInput.put("inputParam1", "workflow_value1");
    workflowInput.put("inputParam2", "workflow_value2");

    Map<String, Object> taskInputTemplate = new HashMap<>();
    taskInputTemplate.put("inputParam3", "task_template_value");

    TaskDef taskDef = new TaskDef();
    taskDef.setInputTemplate(taskInputTemplate);

    WorkflowModel workflowModel = new WorkflowModel();
    workflowModel.setInput(workflowInput);
    workflowModel.setTasks(
        Collections.singletonList(
            new TaskModel()
                .setReferenceTaskName("task1")
                .setInputData(Collections.singletonMap("inputParam4", "task_data_value"))
                .setOutputData(Collections.singletonMap("outputParam1", "task_output_value"))));

    Map<String, Object> result =
        parametersUtils.getTaskInputV2(
            workflowModel,
            Collections.singletonMap("inputParam5", "input_data_value"),
            "task1",
            taskDef,
            1,
            new HashMap<>(),
            new HashMap<>(),
            new ObjectMapper(),
            JsonPath.parse(""));

    assertEquals("workflow_value1", result.get("inputParam1"));
    assertEquals("workflow_value2", result.get("inputParam2"));
    assertEquals("task_template_value", result.get("inputParam3"));
    assertEquals("task_data_value", result.get("inputParam4"));
    assertEquals("input_data_value", result.get("inputParam5"));
  }

  @Test
  public void testReplace() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse("{\"inputParam1\": \"workflow_value1\", \"inputParam2\": \"workflow_value2\"}");
    Map<String, Object> input = new HashMap<>();
    input.put("inputParam1", "${inputParam1}");
    input.put("inputParam2", "${inputParam3}");
    input.put("inputParam3", "task_template_value");

    Map<String, Object> result = parametersUtils.replace(input, documentContext);

    assertEquals("workflow_value1", result.get("inputParam1"));
    assertEquals("task_template_value", result.get("inputParam2"));
  }

  @Test
  public void testReplaceWithNullValue() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext = JsonPath.parse("{\"inputParam1\": \"workflow_value1\", \"inputParam2\": null}");
    Map<String, Object> input = new HashMap<>();
    input.put("inputParam1", "${inputParam1}");
    input.put("inputParam2", "${inputParam3}");
    input.put("inputParam3", "task_template_value");

    Map<String, Object> result = parametersUtils.replace(input, documentContext);

    assertEquals("workflow_value1", result.get("inputParam1"));
    assertEquals("", result.get("inputParam2"));
  }

  @Test
  public void testReplaceVariables() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse("{\"inputParam1\": \"workflow_value1\", \"inputParam2\": \"workflow_value2\"}");
    String input = "${inputParam1}-${inputParam3}-${inputParam2}-${inputParam10}";

    String result = parametersUtils.replaceVariables(input, documentContext, null);

    assertEquals("workflow_value1-task_template_value-workflow_value2-", result);
  }

  @Test
  public void testReplaceVariablesWithEnvVar() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext = JsonPath.parse("{\"inputParam1\": \"env_value\", \"inputParam2\": \"workflow_value2\"}");
    String input = "${inputParam1}-${inputParam3}-${inputParam2}-${inputParam10}";

    String result = parametersUtils.replaceVariables(input, documentContext, null);

    assertEquals("env_value-task_template_value-workflow_value2-", result);
  }

  @Test
  public void testReplaceVariablesWithEmptyValue() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext = JsonPath.parse("{\"inputParam1\": \"\", \"inputParam2\": \"workflow_value2\"}");
    String input = "${inputParam1}-${inputParam3}-${inputParam2}-${inputParam10}";

    String result = parametersUtils.replaceVariables(input, documentContext, null);

    assertEquals("-task_template_value--", result);
  }

  @Test
  public void testReplaceVariablesWithNullValue() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext = JsonPath.parse("{\"inputParam1\": null, \"inputParam2\": \"workflow_value2\"}");
    String input = "${inputParam1}-${inputParam3}-${inputParam2}-${inputParam10}";

    String result = parametersUtils.replaceVariables(input, documentContext, null);

    assertEquals("--task_template_value-workflow_value2-", result);
  }

  @Test
  public void testReplaceVariablesWithEmptyJsonPath() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext = JsonPath.parse("{}");
    String input = "${inputParam1}-${inputParam3}-${inputParam2}-${inputParam10}";

    String result = parametersUtils.replaceVariables(input, documentContext, null);

    assertEquals("--task_template_value--", result);
  }

  @Test
  public void testReplaceVariablesWithNestedJsonPath() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse(
            "{\"inputParam1\": {\"inputParam11\": \"nested_value1\"}, \"inputParam2\": \"workflow_value2\"}");
    String input = "${inputParam1.inputParam11}-${inputParam3}-${inputParam2}-${inputParam10}";

    String result = parametersUtils.replaceVariables(input, documentContext, null);

    assertEquals("nested_value1-task_template_value-workflow_value2-", result);
  }

  @Test
  public void testReplaceVariablesWithListInJsonPath() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse("{\"inputParam1\": [\"list_value1\", \"list_value2\"], \"inputParam2\": \"workflow_value2\"}");
    String input = "${inputParam1[1]}-${inputParam3}-${inputParam2}-${inputParam10}";

    String result = parametersUtils.replaceVariables(input, documentContext, null);

    assertEquals("list_value2-task_template_value-workflow_value2-", result);
  }

  @Test
  public void testReplaceVariablesWithListInJsonPathAndIndexGreaterThanSize() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse("{\"inputParam1\": [\"list_value1\", \"list_value2\"], \"inputParam2\": \"workflow_value2\"}");
    String input = "${inputParam1[2]}-${inputParam3}-${inputParam2}-${inputParam10}";

    String result = parametersUtils.replaceVariables(input, documentContext, null);

    assertEquals("-task_template_value-workflow_value2-", result);
  }

  @Test
  public void testReplaceVariablesWithListInJsonPathAndIndexLessThanZero() {
    Configuration option =
        Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
    DocumentContext documentContext =
        JsonPath.parse("{\"inputParam1\": [\"list_value1\", \"list_value2\"], \"inputParam2\": \"workflow_value2\"}");
    String input = "${inputParam1[-1]}-${inputParam3}-${inputParam2}-${inputParam10}";

    String result = parametersUtils.replaceVariables(input, documentContext, null);

    assertEquals("-task_template_value-workflow_value2-", result);
  }
}