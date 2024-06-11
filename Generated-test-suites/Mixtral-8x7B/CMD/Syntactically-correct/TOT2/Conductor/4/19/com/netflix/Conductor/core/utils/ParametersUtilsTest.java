import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;
private WorkflowDef workflowDef;
private WorkflowModel workflowModel;
private TaskDef taskDef;
private Map<String, Object> inputParams;
private Map<String, Object> inputTemplate;
private TaskModel taskModel;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

objectMapper = mock(ObjectMapper.class);
parametersUtils = new ParametersUtils(objectMapper);

workflowDef = new WorkflowDef();
workflowDef.setSchemaVersion(1);

workflowModel = new WorkflowModel();
workflowModel.setInput(new HashMap<>());
workflowModel.setTasks(Collections.singletonList(taskModel));

taskDef = new TaskDef();
taskDef.setInputTemplate(inputTemplate);

inputParams = new HashMap<>();
inputTemplate = new HashMap<>();
taskModel = new TaskModel();
}

@Test
public void testGetTaskInputWithNullInputParams() {
Map<String, Object> result =
parametersUtils.getTaskInput(null, workflowModel, taskDef, "taskId");
assertThat(result, notNullValue());
assertThat(result.isEmpty(), equalTo(true));
}

@Test
public void testGetTaskInputWithNullWorkflowModel() {
Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, null, taskDef, "taskId");
assertThat(result, notNullValue());
assertThat(result.isEmpty(), equalTo(true));
}

@Test
public void testGetTaskInputWithNullTaskDefinition() {
Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, null, "taskId");
assertThat(result, notNullValue());
assertThat(result.isEmpty(), equalTo(true));
}

@Test
public void testGetTaskInputWithNullInputTemplate() {
inputTemplate = null;
taskDef.setInputTemplate(inputTemplate);

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
assertThat(result, notNullValue());
assertThat(result.isEmpty(), equalTo(true));
}

@Test
public void testGetTaskInputWithEmptyInputParams() {
inputParams = new HashMap<>();

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
assertThat(result, notNullValue());
assertThat(result.isEmpty(), equalTo(true));
}

@Test
public void testGetTaskInputWithEmptyWorkflowInput() {
workflowModel.getInput().clear();

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
assertThat(result, notNullValue());
assertThat(result.isEmpty(), equalTo(true));
}

@Test
public void testGetTaskInputWithEmptyTaskInput() {
taskModel.setInputData(new HashMap<>());

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
assertThat(result, notNullValue());
assertThat(result.isEmpty(), equalTo(true));
}

@Test
public void testGetTaskInputWithEmptyTaskOutput() {
taskModel.setOutputData(new HashMap<>());

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
assertThat(result, notNullValue());
assertThat(result.isEmpty(), equalTo(true));
}

@Test
public void testGetTaskInput() {
inputParams.put("inputKey", "inputValue");
workflowModel.getInput().put("workflowKey", "workflowValue");
taskModel.setInputData(Map.of("taskKey", "taskValue"));

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

Map<String, Object> expected = new HashMap<>();
expected.putAll(inputParams);
expected.putAll(workflowModel.getInput());
expected.put("workflow", workflowModel.getInput());
expected.put("task", taskModel.getInputData());

assertThat(result, notNullValue());
assertThat(result, equalTo(expected));
}

@Test
public void testReplaceWithNullString() {
String replaced = parametersUtils.replace(null);
assertThat(replaced, equalTo(""));
}

@Test
public void testReplaceWithEmptyString() {
String replaced = parametersUtils.replace("");
assertThat(replaced, equalTo(""));
}

@Test
public void testReplaceWithSimpleKey() {
String input = "This is a ${simpleKey} test.";
String replaced = parametersUtils.replace(input);
assertThat(replaced, equalTo("This is a  test."));
}

@Test
public void testReplaceWithComplexKey() {
String input = "This is a ${workflow.input.simpleKey} test.";
String replaced = parametersUtils.replace(input);
assertThat(replaced, equalTo("This is a  test."));
}

@Test
public void testReplaceWithEmptyKey() {
String input = "This is a ${} test.";
String replaced = parametersUtils.replace(input);
assertThat(replaced, equalTo("This is a $${} test."));
}

@Test
public void testReplaceWithNullJson() {
String replaced = parametersUtils.replace(inputParams, null);
assertThat(replaced, equalTo(""));
}

@Test
public void testReplaceWithEmptyJson() {
String replaced = parametersUtils.replace(inputParams, "{}");
assertThat(replaced, equalTo(""));
}

@Test
public void testReplaceWithJson() {
String json = "{\"simpleKey\": \"value\"}";
String replaced = parametersUtils.replace(inputParams, json);
assertThat(replaced, equalTo("simpleKey=value"));
}

@Test
public void testReplaceWithJsonPathDoesNotExist() {
String json = "{\"simpleKey\": \"value\"}";
String replaced = parametersUtils.replace(inputParams, "$.nonExistent");
assertThat(replaced, equalTo(""));
}

@Test
public void testReplaceWithJsonPath() {
String json = "{\"simpleKey\": \"value\"}";
String replaced = parametersUtils.replace(Map.of("jsonPath", "$.simpleKey"), json);
assertThat(replaced, equalTo("value"));
}

@Test
public void testReplaceWithJsonPathAndTaskId() {
String json = "{\"simpleKey\": \"value\"}";
String replaced =
parametersUtils.replace(Map.of("jsonPath", "$.simpleKey", "taskId", "task123"), json);
assertThat(replaced, equalTo("value"));
}

@Test
public void testReplaceWithJsonPathAndTaskIdWithEnvVariable() {
String json = "{\"envVar\": \"$ENV_VAR\"}";
System.setProperty("ENV_VAR", "envValue");

String replaced =
parametersUtils.replace(Map.of("jsonPath", "$.envVar", "taskId", "task123"), json);
assertThat(replaced, equalTo("envValue"));
}

@Test
public void testReplaceWithJsonPathAndTaskIdWithEnvVariableNotSet() {
String json = "{\"envVar\": \"$ENV_VAR\"}";
String replaced =
parametersUtils.replace(Map.of("jsonPath", "$.envVar", "taskId", "task123"), json);
assertThat(replaced, equalTo(""));
}

@Test
public void testReplaceWithJsonPathAndTaskIdWithEnvVariableAndDotInName() {
String json = "{\"envVar.dot\": \"$ENV_VAR\"}";
System.setProperty("ENV_VAR", "envValue");

String replaced =
parametersUtils.replace(Map.of("jsonPath", "$.envVar.dot", "taskId", "task123"), json);
assertThat(replaced, equalTo("envValue"));
}

@Test
public void testReplaceWithJsonPathAndTaskIdWithEnvVariableAndDotInNameNotSet() {
String json = "{\"envVar.dot\": \"$ENV_VAR\"}";
String replaced =
parametersUtils.replace(Map.of("jsonPath", "$.envVar.dot", "taskId", "task123"), json);
assertThat(replaced, equalTo(""));
}

@Test
public void testReplaceWithJsonPathAndTaskIdWithDotInName() {
String json = "{\"simpleKey\": \"value\"}";
String replaced =
parametersUtils.replace(Map.of("jsonPath", "$.simpleKey", "taskId", "task.123"), json);
assertThat(replaced, equalTo("value"));
}

@Test
public void testReplaceWithJsonPathAndTaskIdWithDotInNameAndEnvVariable() {
String json = "{\"envVar\": \"$ENV_VAR\"}";
System.setProperty("ENV_VAR", "envValue");

String replaced =
parametersUtils.replace(Map.of("jsonPath", "$.envVar", "taskId", "task.123"), json);
assertThat(replaced, equalTo("envValue"));
}

@Test
public void testReplaceWithJsonPathAndTaskIdWithDotInNameAndEnvVariableNotSet() {
String json = "{\"envVar\": \"$ENV_VAR\"}";
String replaced =
parametersUtils.replace(Map.of("jsonPath", "$.envVar", "taskId", "task.123"), json);
assertThat(replaced, equalTo(""));
}
}