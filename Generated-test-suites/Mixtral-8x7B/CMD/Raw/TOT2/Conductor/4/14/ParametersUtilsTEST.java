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
import org.mockito.Mockito;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;
private WorkflowModel workflowModel;
private WorkflowDef workflowDef;
private TaskDef taskDef;
private TaskModel taskModel;
private EnvUtils envUtils;

@Before
public void setup() {
objectMapper = mock(ObjectMapper.class);
parametersUtils = new ParametersUtils(objectMapper);
envUtils = mock(EnvUtils.class);
workflowModel = new WorkflowModel();
workflowDef = mock(WorkflowDef.class);
taskDef = new TaskDef();
taskModel = new TaskModel();
}

@Test
public void testGetTaskInput_WithInputParams_ReturnsClonedMap() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "value");

when(workflowDef.getSchemaVersion()).thenReturn(1);
when(workflowDef.getInputTemplate()).thenReturn(new HashMap<>());
workflowModel.setInput(new HashMap<>());
workflowModel.setTasks(Collections.singletonList(taskModel));
taskModel.setInputData(new HashMap<>());
taskModel.setOutputData(new HashMap<>());

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

assertEquals(inputParams, result);
assertEquals(inputParams.get("key"), result.get("key"));
}

@Test
public void testGetTaskInput_WithInputTemplate_ReturnsMergedMap() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "value");
Map<String, Object> inputTemplate = new HashMap<>();
inputTemplate.put("key1", "value1");

when(workflowDef.getSchemaVersion()).thenReturn(1);
when(workflowDef.getInputTemplate()).thenReturn(inputTemplate);
workflowModel.setInput(new HashMap<>());
workflowModel.setTasks(Collections.singletonList(taskModel));
taskModel.setInputData(new HashMap<>());
taskModel.setOutputData(new HashMap<>());

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

assertEquals(3, result.size());
assertEquals(inputParams.get("key"), result.get("key"));
assertEquals(inputTemplate.get("key1"), result.get("key1"));
}

@Test
public void testGetTaskInput_WithSchemaVersion2_CallsCorrectMethod() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "value");

when(workflowDef.getSchemaVersion()).thenReturn(2);
when(workflowModel.getWorkflowDefinition()).thenReturn(workflowDef);
when(workflowDef.getInputTemplate()).thenReturn(new HashMap<>());
workflowModel.setInput(new HashMap<>());
workflowModel.setTasks(Collections.singletonList(taskModel));
taskModel.setInputData(new HashMap<>());
taskModel.setOutputData(new HashMap<>());

parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

Mockito.verify(parametersUtils).getTaskInputV2(eq(inputParams), eq(workflowModel), eq("taskId"), any(TaskDef.class));
}

@Test
public void testGetTaskInputV2_WithNoInputParams_ReturnsEmptyMap() {
Map<String, Object> result = parametersUtils.getTaskInputV2(null, workflowModel, "taskId", taskDef);

assertEquals(0, result.size());
}

@Test
public void testGetTaskInputV2_WithNoTaskDefinition_DoesNotModifyInputParams() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "value");

when(workflowDef.getSchemaVersion()).thenReturn(2);
workflowModel.setInput(new HashMap<>());
workflowModel.setTasks(Collections.singletonList(taskModel));

Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, null, "taskId");

assertEquals(1, result.size());
assertEquals(inputParams.get("key"), result.get("key"));
}

@Test
public void testGetTaskInputV2_WithInputTemplateAndNullInputParamValue_ReturnsInputTemplateValue() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", null);
Map<String, Object> inputTemplate = new HashMap<>();
inputTemplate.put("key", "value");

when(workflowDef.getSchemaVersion()).thenReturn(2);
workflowModel.setInput(new HashMap<>());
workflowModel.setTasks(Collections.singletonList(taskModel));
taskDef.setInputTemplate(inputTemplate);

Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, taskDef, "taskId");

assertEquals(1, result.size());
assertEquals(inputTemplate.get("key"), result.get("key"));
}

@Test
public void testGetTaskInputV2_WithSchemaVersion2AndAllParameters_ReturnsCorrectMap() throws IOException {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflow", new HashMap<>());
inputParams.get("workflow").put("input", new HashMap<>());
inputParams.get("workflow").put("output", new HashMap<>());
inputParams.get("workflow").put("status", "status");
inputParams.get("workflow").put("workflowId", "workflowId");
inputParams.get("workflow").put("parentWorkflowId", "parentWorkflowId");
inputParams.get("workflow").put("parentWorkflowTaskId", "parentWorkflowTaskId");
inputParams.get("workflow").put("workflowType", "workflowType");
inputParams.get("workflow").put("version", "version");
inputParams.get("workflow").put("correlationId", "correlationId");
inputParams.get("workflow").put("reasonForIncompletion", "reasonForIncompletion");
inputParams.get("workflow").put("schemaVersion", 2);
inputParams.get("workflow").put("variables", new HashMap<>());
inputParams.put("tasks", new HashMap<>());
inputParams.get("tasks").put("task1", new HashMap<>());
inputParams.get("tasks").get("task1").put("input", new HashMap<>());
inputParams.get("tasks").get("task1").put("output", new HashMap<>());
inputParams.get("tasks").get("task1").put("taskType", "taskType");
inputParams.get("tasks").get("task1").put("status", "status");
inputParams.get("tasks").get("task1").put("referenceTaskName", "referenceTaskName");
inputParams.get("tasks").get("task1").put("retryCount", 0);
inputParams.get("tasks").get("task1").put("correlationId", "correlationId");
inputParams.get("tasks").get("task1").put("scheduledTime", "scheduledTime");
inputParams.get("tasks").get("task1").put("startTime", "startTime");
inputParams.get("tasks").get("task1").put("endTime", "endTime");
inputParams.get("tasks").get("task1").put("workflowInstanceId", "workflowInstanceId");
inputParams.get("tasks").get("task1").put("taskId", "taskId");
inputParams.get("tasks").get("task1").put("reasonForIncompletion", "reasonForIncompletion");
inputParams.get("tasks").get("task1").put("callbackAfterSeconds", 0);
inputParams.get("tasks").get("task1").put("workerId", "workerId");
inputParams.get("tasks").get("task1").put("iteration", 0);

when(workflowDef.getSchemaVersion()).thenReturn(2);
workflowModel.setInput(new HashMap<>());
workflowModel.setTasks(Collections.singletonList(taskModel));
taskModel.setInputData(new HashMap<>());
taskModel.setOutputData(new HashMap<>());

Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflowModel, taskDef, "taskId");

assertEquals(inputParams, result);
}

@Test
public void testReplace_WithString_ReturnsCorrectString() {
String inputString = "${input}";
String replacement = "replacement";
Map<String, Object> inputMap = new HashMap<>();
inputMap.put("input", replacement);

String result = parametersUtils.replace(inputString, inputMap);

assertEquals(replacement, result);
}

@Test
public void testReplace_WithJson_ReturnsCorrectString() {
String inputString = "{\"input\": \"${input}\"}";
String replacement = "replacement";
Map<String, Object> inputMap = new HashMap<>();
inputMap.put("input", replacement);

String result = parametersUtils.replace(inputString, inputMap);

assertEquals("{\"input\": \"replacement\"}", result);
}

@Test
public void testReplace_WithEnvVar_ReturnsCorrectString() throws IOException {
String inputString = "${ENV_VAR}";
String envValue = "envValue";
when(envUtils.getSystemParametersValue(eq(inputString), any())).thenReturn(envValue);

String result = parametersUtils.replace(inputString);

assertEquals(envValue, result);
}

@Test
public void testReplace_WithNestedJson_ReturnsCorrectString() {
String inputString = "{\"input\": {\"child\": \"${child}\"}}";
String childReplacement = "childReplacement";
Map<String, Object> inputMap = new HashMap<>();
inputMap.put("child", childReplacement);

String result = parametersUtils.replace(inputString, inputMap);

assertEquals("{\"input\": {\"child\": \"childReplacement\"}}", result);
}

@Test
public void testReplace_WithNestedJsonAndEnvVar_ReturnsCorrectString() throws IOException {
String inputString = "{\"input\": {\"envVar\": \"${ENV_VAR}\"}}";
String envValue = "envValue";
when(envUtils.getSystemParametersValue(eq("ENV_VAR"), any())).thenReturn(envValue);

String result = parametersUtils.replace(inputString);

assertEquals("{\"input\": {\"envVar\": \"envValue\"}}", result);
}

@Test
public void testReplace_WithInvalidJson_ThrowsRuntimeException() {
String inputString = "invalid json";

try {
parametersUtils.replace(inputString);
} catch (RuntimeException e) {
assertEquals("Unable to clone input params", e.getMessage());
}
}

@Test
public void testReplaceVariables_WithEmptyMap_ReturnsOriginalString() {
String inputString = "test";
String result =
parametersUtils.replaceVariables(inputString, JsonPath.parse(Collections.emptyMap()));

assertEquals(inputString, result);
}

@Test
public void testReplaceVariables_WithNullJson_ThrowsRuntimeException() {
String inputString = "test";

try {
parametersUtils.replaceVariables(inputString, null);
} catch (RuntimeException e) {
assertEquals("Unable to clone input params", e.getMessage());
}
}

@Test
public void testReplaceVariables_WithSimpleExpression_ReturnsCorrectString() {
String inputString = "test ${input} test";
String replacement = "replacement";
Map<String, Object> inputMap = new HashMap<>();
inputMap.put("input", replacement);

String result =
parametersUtils.replaceVariables(inputString, JsonPath.parse(inputMap));

assertEquals("test replacement test", result);
}

@Test
public void testReplaceVariables_WithNestedExpression_ReturnsCorrectString() {
String inputString = "test ${input.child} test";
String childReplacement = "childReplacement";
Map<String, Object> inputMap = new HashMap<>();
List<Map<String, Object>> list = new LinkedList<>();
Map<String, Object> childMap = new HashMap<>();
childMap.put("child", childReplacement);
list.add(childMap);
inputMap.put("input", list);

String result =
parametersUtils.replaceVariables(inputString, JsonPath.parse(inputMap));

assertEquals("test childReplacement test", result);
}

@Test
public void testReplaceVariables_WithEnvVarExpression_ReturnsCorrectString() throws IOException {
String inputString = "test ${ENV_VAR} test";
String envValue = "envValue";
when(envUtils.getSystemParametersValue(eq("ENV_VAR"), any())).thenReturn(envValue);

String result =
parametersUtils.replaceVariables(inputString, JsonPath.parse(Collections.emptyMap()));

assertEquals("test envValue test", result);
}

@Test
public void testGetWorkflowInput_WithInputTemplate_ReturnsMergedMap() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "value");
Map<String, Object> inputTemplate = new HashMap<>();
inputTemplate.put("key1", "value1");

workflowDef.setInputTemplate(inputTemplate);
workflowModel.setInput(new HashMap<>());

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(3, result.size());
assertEquals(inputParams.get("key"), result.get("key"));
assertEquals(inputTemplate.get("key1"), result.get("key1"));
}

@Test
public void testGetWorkflowInput_WithNoInputTemplate_ReturnsInputParams() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "value");

workflowDef.setInputTemplate(null);
workflowModel.setInput(new HashMap<>());

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(1, result.size());
assertEquals(inputParams.get("key"), result.get("key"));
}
}