import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.model.TaskModel;

public class ParametersUtilsTest {

@Mock
private ObjectMapper objectMapper;

private ParametersUtils parametersUtils;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
parametersUtils = new ParametersUtils(objectMapper);
}

@Test
public void testGetTaskInput() {
// Given
Map<String, Object> inputParams = new HashMap<>();
WorkflowModel workflow = createWorkflowModel();
TaskDef taskDefinition = createTaskDef();
String taskId = "task1";

when(objectMapper.writeValueAsBytes(inputParams))
.thenReturn("someBytes".getBytes());
when(objectMapper.readValue("someBytes".getBytes(), map))
.thenReturn(inputParams);

// When
Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

// Then
assertEquals(inputParams, result);
}

@Test
public void testGetTaskInputV2() {
// Given
Map<String, Object> inputParams = new HashMap<>();
WorkflowModel workflow = createWorkflowModel();
TaskDef taskDefinition = createTaskDef();
String taskId = "task1";

when(objectMapper.writeValueAsBytes(inputParams))
.thenReturn("someBytes".getBytes());
when(objectMapper.readValue("someBytes".getBytes(), map))
.thenReturn(inputParams);

DocumentContext documentContext =
JsonPath.parse("{\"workflow\":{}}");

when(objectMapper.readValue("{}", map)).thenReturn(new HashMap<>());

// When
Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);

// Then
assertEquals(inputParams, result);
}

private WorkflowModel createWorkflowModel() {
WorkflowModel workflow = new WorkflowModel();
workflow.setInput(Collections.singletonMap("inputKey", "inputValue"));
workflow.setOutput(Collections.singletonMap("outputKey", "outputValue"));
workflow.setStatus("COMPLETED");
workflow.setWorkflowId("workflowId");
workflow.setParentWorkflowId("parentWorkflowId");
workflow.setParentWorkflowTaskId("parentWorkflowTaskId");
workflow.setWorkflowName("workflowName");
workflow.setWorkflowVersion(1);
workflow.setCorrelationId("correlationId");
workflow.setReasonForIncompletion("reasonForIncompletion");
workflow.setSchemaVersion(2);
workflow.setVariables(Collections.singletonMap("variableKey", "variableValue"));
return workflow;
}

private TaskDef createTaskDef() {
TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(Collections.singletonMap("taskInputKey", "taskInputValue"));
return taskDef;
}

@Test
public void testReplace_withStringJson() {
// Given
String paramString = "test ${workflow.input} ${workflow.output}";
DocumentContext documentContext =
JsonPath.parse("{\"input\":\"inputValue\",\"output\":\"outputValue\"}");

// When
String result =
parametersUtils.replace(paramString, documentContext, null).toString();

// Then
assertEquals("test inputValue outputValue", result);
}

@Test
public void testReplace_withNullJson() {
// Given
String paramString = "test ${workflow.input} ${workflow.output}";

// When
String result =
parametersUtils.replace(paramString, JsonPath.parse(Collections.emptyMap()), null).toString();

// Then
assertEquals(paramString, result);
}

@Test
public void testReplaceVariables() {
// Given
String paramString = "test ${workflow.input} ${workflow.output}";
DocumentContext documentContext =
JsonPath.parse("{\"input\":\"inputValue\",\"output\":\"outputValue\"}");

// When
String result =
parametersUtils.replaceVariables(paramString, documentContext, null).toString();

// Then
assertEquals("test inputValue outputValue", result);
}

@Test
public void testReplaceVariables_withBlankParamPath() {
// Given
String paramString = "test ${} ${ } ${  }";
DocumentContext documentContext =
JsonPath.parse("{\"\":\"value\"}");

// When
String result =
parametersUtils.replaceVariables(paramString, documentContext, null).toString();

// Then
assertEquals("test value value ", result);
}

@Test
public void testReplaceVariables_withEnvironmentVariable() {
// Given
String paramString = "test ${ENV_VAR}";
DocumentContext documentContext =
JsonPath.parse("{}");

// When
String result =
parametersUtils.replaceVariables(paramString, documentContext, null).toString();

// Then
assertEquals("test envValue", result);
}

@Test
public void testReplaceVariables_withException() {
// Given
String paramString = "test ${workflow.input} ${workflow.output}";
DocumentContext documentContext =
JsonPath.parse("{}");

// When
String result =
parametersUtils.replaceVariables(paramString, documentContext, null).toString();

// Then
assertEquals("test ${workflow.input} ${workflow.output}", result);
}

private Map<String, Object> createInputParams() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("param1", "value1");
inputParams.put("param2", "value2");
return inputParams;
}

private List<TaskModel> createTasks() {
List<TaskModel> tasks = new LinkedList<>();
TaskModel task1 = new TaskModel();
task1.setInputData(createInputParams());
TaskModel task2 = new TaskModel();
task2.setInputData(createInputParams());
tasks.add(task1);
tasks.add(task2);
return tasks;
}

@Test
public void testReplace_withMapJson() {
// Given
Map<String, Object> inputParams = createInputParams();
List<TaskModel> tasks = createTasks();
WorkflowModel workflow = createWorkflowModel();
workflow.setTasks(tasks);

DocumentContext documentContext =
JsonPath.parse(createDocumentContext(workflow, inputParams));

String paramString = "${param1} ${param2}";

// When
String result =
parametersUtils.replace(paramString, documentContext, null).toString();

// Then
assertEquals("value1 value2", result);
}

@Test
public void testReplace_withMapJson_taskInput() {
// Given
Map<String, Object> inputParams = createInputParams();
List<TaskModel> tasks = createTasks();
WorkflowModel workflow = createWorkflowModel();
workflow.setTasks(tasks);

DocumentContext documentContext =
JsonPath.parse(createDocumentContext(workflow, inputParams));

String paramString = "${task1.input.param1}";

// When
String result =
parametersUtils.replace(paramString, documentContext, null).toString();

// Then
assertEquals("value1", result);
}

@Test
public void testReplace_withMapJson_taskInput_nullValue() {
// Given
Map<String, Object> inputParams = createInputParams();
List<TaskModel> tasks = createTasks();
WorkflowModel workflow = createWorkflowModel();
workflow.setTasks(tasks);

DocumentContext documentContext =
JsonPath.parse(createDocumentContext(workflow, inputParams));

String paramString = "${task1.input.nonExistingKey}";

// When
String result =
parametersUtils.replace(paramString, documentContext, null).toString();

// Then
assertEquals("", result);
}

private String createDocumentContext(WorkflowModel workflow, Map<String, Object> inputParams) {
String documentContext =
"{" + "\"workflow\":{" + "\"input\":" + inputParams + "," + "\"output\":\"outputValue\"" + "},"
+ "\"tasks\":" + convertToJson(workflow.getTasks()) + "}";
return documentContext;
}

private String convertToJson(List<?> objects) {
List<Map<String, Object>> list = new LinkedList<>();
for (Object obj : objects) {
list.add((Map<String, Object>) obj);
}
return new ObjectMapper().valueToTree(list).toString();
}
}