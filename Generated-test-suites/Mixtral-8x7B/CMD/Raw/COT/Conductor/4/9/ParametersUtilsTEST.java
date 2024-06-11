import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.utils.EnvUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;
private EnvUtils envUtils;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

objectMapper = mock(ObjectMapper.class);
envUtils = mock(EnvUtils.class);

parametersUtils = new ParametersUtils(objectMapper);
ReflectionTestUtils.setField(parametersUtils, "envUtils", envUtils);
}

@Test
public void testGetTaskInput() throws Exception {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflowId", "workflow-123");
inputParams.put("taskId", "task-456");

WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setSchemaVersion(1);
workflowDef.setInputTemplate(inputParams);

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowDefinition(workflowDef);
workflowModel.setInput(inputParams);

TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(inputParams);

parametersUtils = new ParametersUtils(objectMapper);

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "task-456");

assertEquals(inputParams, result);
}

@Test
public void testGetTaskInputV2() throws Exception {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflowId", "workflow-123");
inputParams.put("taskId", "task-456");

WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setSchemaVersion(2);
workflowDef.setInputTemplate(inputParams);

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowDefinition(workflowDef);
workflowModel.setInput(inputParams);
workflowModel.setTasks(Collections.singletonList(createTaskModel()));

TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(inputParams);

when(objectMapper.writeValueAsBytes(inputParams))
.thenReturn("some json bytes".getBytes());

when(objectMapper.readValue("some json bytes".getBytes(), new TypeReference<Map<String, Object>>() {}))
.thenReturn(inputParams);

Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflowModel, "task-456", taskDef);

assertEquals(inputParams, result);
}

@Test
public void testClone() throws Exception {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflowId", "workflow-123");
inputParams.put("taskId", "task-456");

ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

Map<String, Object> result = parametersUtils.clone(inputParams);

assertEquals(inputParams, result);
assertEquals(inputParams.hashCode(), result.hashCode());
}

@Test
public void testReplace() throws Exception {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflowId", "workflow-123");
inputParams.put("taskId", "task-456");

DocumentContext documentContext =
JsonPath.parse("{\"workflowId\": \"workflow-789\", \"taskId\": \"task-012\"}");

ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

Map<String, Object> result = parametersUtils.replace(inputParams, documentContext);

assertEquals(2, result.size());
assertEquals("workflow-789", result.get("workflowId"));
assertEquals("task-012", result.get("taskId"));
}

@Test
public void testReplace_withNullValue() throws Exception {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflowId", "workflow-123");
inputParams.put("taskId", "task-456");

DocumentContext documentContext =
JsonPath.parse("{\"workflowId\": \"workflow-789\", \"taskId\": null}");

ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

Map<String, Object> result = parametersUtils.replace(inputParams, documentContext);

assertEquals(1, result.size());
assertEquals("workflow-789", result.get("workflowId"));
assertEquals(null, result.get("taskId"));
}

@Test
public void testReplace_withEmptyValue() throws Exception {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflowId", "workflow-123");
inputParams.put("taskId", "task-456");

DocumentContext documentContext =
JsonPath.parse("{\"workflowId\": \"workflow-789\", \"taskId\": \"\"}");

ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

Map<String, Object> result = parametersUtils.replace(inputParams, documentContext);

assertEquals(1, result.size());
assertEquals("workflow-789", result.get("workflowId"));
assertEquals("", result.get("taskId"));
}

@Test
public void testReplace_withList() throws Exception {
List<String> list = new LinkedList<>();
list.add("value-1");
list.add("value-2");

DocumentContext documentContext = JsonPath.parse("['new-value-1', 'new-value-2']");

ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

List<Object> result =
parametersUtils.replaceList(list, "task-123", documentContext);

assertEquals(2, result.size());
assertEquals("new-value-1", result.get(0));
assertEquals("new-value-2", result.get(1));
}

@Test
public void testReplaceVariables() throws Exception {
ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

// Test with a simple variable
DocumentContext documentContext =
JsonPath.parse("{\"workflowId\": \"${workflowId}\"}");
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflowId", "workflow-123");

Object result =
parametersUtils.replaceVariables("task-${workflowId}", documentContext, null);

assertEquals("task-workflow-123", result);

// Test with a non-existing variable
DocumentContext documentContext2 =
JsonPath.parse("{\"workflowId\": \"workflow-123\", \"nonexisting\": \"${nonexisting}\"}");

Object result2 =
parametersUtils.replaceVariables("task-${nonexisting}", documentContext2, null);

assertEquals("task-${nonexisting}", result2);

// Test with a system parameter
when(envUtils.getSystemParametersValue("test-param", "task-123"))
.thenReturn("test-value");

DocumentContext documentContext3 =
JsonPath.parse("{\"workflowId\": \"${workflowId}\", \"testParam\": \"${test-param}\"}");

Object result3 =
parametersUtils.replaceVariables("task-${test-param}", documentContext3, "task-123");

assertEquals("task-test-value", result3);
}

@Test
public void testGetTaskInputV1_emptyWorkflow() throws Exception {
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setSchemaVersion(1);
workflowDef.setInputTemplate(new HashMap<>());

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowDefinition(workflowDef);
workflowModel.setInput(new HashMap<>());

TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(new HashMap<>());

parametersUtils = new ParametersUtils(objectMapper);

Map<String, Object> result =
parametersUtils.getTaskInputV1(workflowModel, new HashMap<>());

assertEquals(0, result.size());
}

@Test
public void testGetWorkflowInput() throws Exception {
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(new HashMap<>());

WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowDefinition(workflowDef);
workflowModel.setInput(new HashMap<>());

ParametersUtils parametersUtils = new ParametersUtils(objectMapper);

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, new HashMap<>());

assertEquals(0, result.size());
}

private WorkflowModel.TaskModel createTaskModel() {
WorkflowModel.TaskModel taskModel = new WorkflowModel.TaskModel();
taskModel.setInputData(new HashMap<>());
taskModel.setOutputData(new HashMap<>());
return taskModel;
}
}