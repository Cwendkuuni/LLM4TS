import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;
private EnvUtils envUtils;
private TaskUtils taskUtils;

@Before
public void setup() {
objectMapper = mock(ObjectMapper.class);
envUtils = mock(EnvUtils.class);
taskUtils = mock(TaskUtils.class);
parametersUtils = new ParametersUtils(objectMapper) {
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
// Arrange
Map<String, Object> inputParams = new HashMap<>();
WorkflowModel workflowModel = new WorkflowModel();
TaskDef taskDef = new TaskDef();
String taskId = "task-1";

when(objectMapper.convertValue(any(Map.class), ArgumentMatchers.<TypeReference<Map<String, Object>>>any()))
.thenReturn(inputParams);

// Act
Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);

// Assert
assertNotNull(result);
}

@Test
public void testGetTaskInputV2() {
// Arrange
Map<String, Object> inputParams = new HashMap<>();
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowId("workflow-1");
workflowModel.setTaskByRefName("task-1", new TaskModel());
TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(inputParams);
String taskId = "task-1";

when(objectMapper.convertValue(any(Map.class), ArgumentMatchers.<TypeReference<Map<String, Object>>>any()))
.thenReturn(inputParams);

// Act
Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflowModel, taskId, taskDef);

// Assert
assertNotNull(result);
assertEquals(inputParams, result);
}

@Test
public void testGetTaskInputV2WithNullInputParams() {
// Arrange
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setWorkflowId("workflow-1");
workflowModel.setTaskByRefName("task-1", new TaskModel());
TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(null);
String taskId = "task-1";

// Act
Map<String, Object> result =
parametersUtils.getTaskInputV2(null, workflowModel, taskId, taskDef);

// Assert
assertNotNull(result);
assertEquals(new HashMap<>(), result);
}

@Test
public void testReplace() {
// Arrange
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("param1", "value1");
inputParams.put("param2", "workflow.input.param3");
DocumentContext documentContext = JsonPath.parse("{\"param3\": \"replacementValue\"}");

// Act
Map<String, Object> result =
parametersUtils.replace(inputParams, documentContext, "workflow-1");

// Assert
assertNotNull(result);
assertEquals("replacementValue", result.get("param2"));
}

@Test
public void testReplaceWithNullDocumentContext() {
// Arrange
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("param1", "value1");
inputParams.put("param2", "workflow.input.param3");

// Act
Map<String, Object> result = parametersUtils.replace(inputParams, null, "workflow-1");

// Assert
assertNotNull(result);
assertEquals("workflow.input.param3", result.get("param2"));
}

@Test
public void testReplaceVariables() {
// Arrange
String paramString = "${param1}.${param2}";
DocumentContext documentContext =
JsonPath.parse("{\"param1\": \"replacementValue1\", \"param2\": \"replacementValue2\"}");

// Act
Object result =
parametersUtils.replaceVariables(paramString, documentContext, "workflow-1");

// Assert
assertNotNull(result);
assertEquals("replacementValue1.replacementValue2", result.toString());
}

@Test
public void testReplaceVariablesWithNullDocumentContext() {
// Arrange
String paramString = "${param1}.${param2}";

// Act
Object result = parametersUtils.replaceVariables(paramString, null, "workflow-1");

// Assert
assertNull(result);
}

@Test
public void testReplaceVariablesWithNoMatchingPaths() {
// Arrange
String paramString = "${param1}.${param2}";
DocumentContext documentContext = JsonPath.parse("{\"param3\": \"replacementValue3\"}");

// Act
Object result =
parametersUtils.replaceVariables(paramString, documentContext, "workflow-1");

// Assert
assertNull(result);
}

@Test
public void testGetWorkflowInput() {
// Arrange
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(Collections.singletonMap("param1", "workflowInputValue"));
Map<String, Object> inputParams = new HashMap<>();

// Act
Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

// Assert
assertNotNull(result);
assertEquals("workflowInputValue", result.get("param1"));
}

@Test
public void testGetWorkflowInputWithNullWorkflowDef() {
// Arrange
Map<String, Object> inputParams = new HashMap<>();

// Act
Map<String, Object> result = parametersUtils.getWorkflowInput(null, inputParams);

// Assert
assertNotNull(result);
assertEquals(inputParams, result);
}
}