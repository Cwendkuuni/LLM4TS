import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.model.TaskModel;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;

@Before
public void setUp() throws Exception {
MockitoAnnotations.initMocks(this);

objectMapper = new ObjectMapper();
parametersUtils = new ParametersUtils(objectMapper);
}

@Test
public void testGetTaskInput_whenWorkflowSchemaVersion1() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflow.input.task1.input.param1", "value1");

WorkflowModel workflow = mock(WorkflowModel.class);
when(workflow.getInput()).thenReturn(Collections.singletonMap("param1", "workflowValue"));
when(workflow.getTasks()).thenReturn(Collections.emptyList());

TaskDef taskDefinition = mock(TaskDef.class);

String taskId = "1";

Map<String, Object> taskInput =
parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

assertThat(taskInput.size(), is(1));
assertThat(taskInput.get("task1.input.param1"), is("value1"));
}

@Test
public void testGetTaskInput_whenWorkflowSchemaVersion2() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflow.input.task1.input.param1", "value1");
inputParams.put("workflow.output.task1.output.param1", "value2");

WorkflowModel workflow = mock(WorkflowModel.class);
when(workflow.getInput()).thenReturn(Collections.singletonMap("param1", "workflowValue"));
when(workflow.getTasks()).thenReturn(getTasks());

TaskDef taskDefinition = mock(TaskDef.class);

String taskId = "1";

Map<String, Object> taskInput =
parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, taskId);

assertThat(taskInput.size(), is(2));
assertThat(taskInput.get("input.param1"), is("value1"));
assertThat(taskInput.get("output.param1"), is("value2"));
}

@Test
public void testReplace_whenStringWithNoParams() {
String paramString = "test string";

Object result = parametersUtils.replace(paramString);

assertThat(result, is(paramString));
}

@Test
public void testReplace_whenStringWithParams() {
String paramString = "${param1}.${param2}";

DocumentContext documentContext =
JsonPath.parse("{\"param1\": \"value1\", \"param2\": \"value2\"}");

Object result = parametersUtils.replace(paramString, documentContext);

assertThat(result, is("value1.value2"));
}

@Test
public void testReplaceVariables_whenStringWithParams() {
String paramString = "${param1}.${param2}";

DocumentContext documentContext =
JsonPath.parse("{\"param1\": \"value1\", \"param2\": \"value2\"}");

Object result =
parametersUtils.replaceVariables(paramString, documentContext, "taskId");

assertThat(result, is("value1.value2"));
}

@Test
public void testReplace_whenStringWithMissingParams() {
String paramString = "${param1}.${param2}";

DocumentContext documentContext = JsonPath.parse("{}");

Object result = parametersUtils.replace(paramString, documentContext);

assertThat(result, is(paramString));
}

@Test
public void testReplace_whenDeeplyNestedStringWithParams() throws IOException {
String paramString =
"${workflow.input.task1.input.param1}.${workflow.output.task1.output.param1}";

WorkflowModel workflow = mock(WorkflowModel.class);
when(workflow.getInput()).thenReturn(Collections.singletonMap("param1", "workflowValue"));
when(workflow.getTasks()).thenReturn(getTasks());

DocumentContext documentContext = JsonPath.parse(objectMapper.writeValueAsString(workflow));

Object result = parametersUtils.replace(paramString, documentContext);

assertThat(result, is("value1.value2"));
}

@Test(expected = RuntimeException.class)
public void testReplace_whenStringWithCycleParams() {
String paramString = "${param1}.${param2}.${param1}";

DocumentContext documentContext =
JsonPath.parse("{\"param1\": \"value1\", \"param2\": \"value2\"}");

parametersUtils.replace(paramString, documentContext);
}

private List<TaskModel> getTasks() {
List<TaskModel> tasks = new LinkedList<>();
TaskModel task1 = new TaskModel();
task1.setInputData(Collections.singletonMap("param1", "value1"));
task1.setOutputData(Collections.singletonMap("param1", "value2"));
task1.setReferenceTaskName("task1");
tasks.add(task1);
return tasks;
}
}