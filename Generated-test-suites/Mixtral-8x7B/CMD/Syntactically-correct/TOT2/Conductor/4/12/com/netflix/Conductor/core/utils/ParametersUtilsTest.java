import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;
private WorkflowModel workflowModel;
private TaskDef taskDef;
private EnvUtils envUtils;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

objectMapper = new ObjectMapper();
parametersUtils = new ParametersUtils(objectMapper);

workflowModel = new WorkflowModel();
workflowModel.setInput(new HashMap<>());
workflowModel.setOutput(new HashMap<>());
workflowModel.setStatus("COMPLETED");
workflowModel.setWorkflowId("workflow-123");
workflowModel.setParentWorkflowId("parent-123");
workflowModel.setParentWorkflowTaskId("parentTask-123");
workflowModel.setWorkflowType("TestWorkflowType");
workflowModel.setVersion(1);
workflowModel.setCorrelationId("correlation-123");
workflowModel.setReasonForIncompletion("TestReason");
workflowModel.setSchemaVersion(1);
workflowModel.setVariables(new HashMap<>());

taskDef = mock(TaskDef.class);
when(taskDef.getInputTemplate()).thenReturn(new HashMap<>());

envUtils = mock(EnvUtils.class);
}

@Test
public void testGetTaskInputWithV1Schema() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflow.input.param1", "value1");

Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "task-123");

assertThat(result, notNullValue());
assertThat(result.size(), equalTo(1));
assertThat(result.get("param1"), equalTo("value1"));
}

@Test
public void testGetTaskInputWithV2Schema() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflow.input.param1", "value1");
inputParams.put("task.input.param2", "value2");

WorkflowModel workflowModelV2 = new WorkflowModel();
workflowModelV2.setInput(new HashMap<>());
workflowModelV2.setOutput(new HashMap<>());
workflowModelV2.setStatus("COMPLETED");
workflowModelV2.setWorkflowId("workflow-123");
workflowModelV2.setParentWorkflowId("parent-123");
workflowModelV2.setParentWorkflowTaskId("parentTask-123");
workflowModelV2.setWorkflowType("TestWorkflowType");
workflowModelV2.setVersion(2);
workflowModelV2.setCorrelationId("correlation-123");
workflowModelV2.setReasonForIncompletion("TestReason");
workflowModelV2.setSchemaVersion(2);
workflowModelV2.setVariables(new HashMap<>());
workflowModelV2.setTasks(new LinkedList<>());

TaskModel taskModel = new TaskModel();
taskModel.setInputData(new HashMap<>());
taskModel.setOutputData(new HashMap<>());
taskModel.setTaskType("TestTaskType");
taskModel.setReferenceTaskName("task");
taskModel.setCorrelationId("correlation-123");
taskModel.setWorkerId("worker-123");
workflowModelV2.getTasks().add(taskModel);

Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflowModelV2, taskDef, "task-123");

assertThat(result, notNullValue());
assertThat(result.size(), equalTo(2));
assertThat(result.get("param1"), equalTo("value1"));
assertThat(result.get("param2"), equalTo("value2"));
}

@Test
public void testReplaceWithJson() throws IOException {

String json = "{\"workflow\":{\"input\":{\"param1\":\"value1\"}},\"task\":{\"input\":{\"param2\":\"value2\"}}}";
String result = parametersUtils.replace(json);

assertThat(result, notNullValue());
assertThat(result, equalTo("{\"workflow\":{\"input\":{\"param1\":\"value1\"}},\"task\":{\"input\":{\"param2\":\"value2\"}}}"));
}

@Test
public void testReplaceWithJsonPath() {

Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflow.input.param1", "value1");
inputParams.put("task.input.param2", "value2");

DocumentContext documentContext = JsonPath.parse(inputParams);

String result = parametersUtils.replace("${workflow.input.param1}.${task.input.param2}", documentContext, null);

assertThat(result, notNullValue());
assertThat(result, equalTo("value1.value2"));
}

@Test
public void testReplaceWithEnvVar() {

String result = parametersUtils.replace("${ENV_VAR}", mock(DocumentContext.class), "task-123");

assertThat(result, notNullValue());
assertThat(result, equalTo(""));

when(envUtils.isEnvironmentVariable("ENV_VAR")).thenReturn(true);
when(envUtils.getSystemParametersValue("ENV_VAR", "task-123")).thenReturn("env_value");

result = parametersUtils.replace("${ENV_VAR}", mock(DocumentContext.class), "task-123");

assertThat(result, notNullValue());
assertThat(result, equalTo("env_value"));
}

@Test
public void testReplaceVariables() {
String paramString = "${workflow.input.param1}.${task.input.param2}";
DocumentContext documentContext = JsonPath.parse(new HashMap<>());

String result = parametersUtils.replaceVariables(paramString, documentContext, "task-123").toString();

assertThat(result, notNullValue());
assertThat(result, equalTo("${workflow.input.param1}.${task.input.param2}"));

Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflow.input.param1", "value1");
inputParams.put("task.input.param2", "value2");

documentContext = JsonPath.parse(inputParams);

result = parametersUtils.replaceVariables(paramString, documentContext, "task-123").toString();

assertThat(result, notNullValue());
assertThat(result, equalTo("value1.value2"));
}

@Test
public void testGetWorkflowInput() {
Map<String, Object> workflowInputTemplate = new HashMap<>();
workflowInputTemplate.put("param1", "value1");

WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(workflowInputTemplate);
workflowDef.setSchemaVersion(1);

Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, new HashMap<>());

assertThat(result, notNullValue());
assertThat(result.size(), equalTo(1));
assertThat(result.get("param1"), equalTo("value1"));
}

@Test
public void testReplaceList() {
List<Object> inputList = new LinkedList<>();
inputList.add("${workflow.input.param1}");
inputList.add("${task.input.param2}");

List<Object> result = parametersUtils.replaceList(inputList, "task-123", JsonPath.parse(new HashMap<>()));

assertThat(result, notNullValue());
assertThat(result.size(), equalTo(2));
assertThat(result.get(0), equalTo("${workflow.input.param1}"));
assertThat(result.get(1), equalTo("${task.input.param2}"));

Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflow.input.param1", "value1");
inputParams.put("task.input.param2", "value2");

DocumentContext documentContext = JsonPath.parse(inputParams);

result = parametersUtils.replaceList(inputList, "task-123", documentContext);

assertThat(result, notNullValue());
assertThat(result.size(), equalTo(2));
assertThat(result.get(0), equalTo("value1"));
assertThat(result.get(1), equalTo("value2"));
}
}