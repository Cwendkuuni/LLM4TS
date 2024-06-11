import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TestUtils;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

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
private WorkflowDef workflowDef;
private WorkflowModel workflowModel;
private TaskDef taskDef;
private Map<String, Object> inputParams;
private Map<String, Object> inputTemplateV1;
private Map<String, Object> inputTemplateV2;
private Map<String, Object> variables;
private Map<String, Object> taskInput;
private Map<String, Object> workflowInput;
private List<TaskModel> tasks;
private DocumentContext jsonContext;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
objectMapper = new ObjectMapper();
workflowDef = new WorkflowDef();
workflowModel = new WorkflowModel();
taskDef = new TaskDef();
inputParams = new HashMap<>();
inputTemplateV1 = new HashMap<>();
inputTemplateV2 = new HashMap<>();
variables = new HashMap<>();
taskInput = new HashMap<>();
workflowInput = new HashMap<>();
tasks = new LinkedList<>();
jsonContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{}");

parametersUtils = new ParametersUtils(objectMapper);

// Initialize inputParams
TestUtils.putInMap("key1", "value1", inputParams);
TestUtils.putInMap("key2", "workflow.input.key2", inputParams);
TestUtils.putInMap("key3", "task1.input.key3", inputParams);
TestUtils.putInMap("key4", "task1.output.key4", inputParams);

// Initialize inputTemplateV1
TestUtils.putInMap("key5", "value5", inputTemplateV1);
TestUtils.putInMap("key6", "workflow.input.key6", inputTemplateV1);

// Initialize inputTemplateV2
TestUtils.putInMap("key7", "value7", inputTemplateV2);
TestUtils.putInMap("key8", "workflow.output.key8", inputTemplateV2);

// Initialize variables
TestUtils.putInMap("key9", "value9", variables);
TestUtils.putInMap("key10", "workflow.variables.key10", variables);

// Initialize workflowInput
TestUtils.putInMap("key11", "value11", workflowInput);

// Initialize taskInput
TestUtils.putInMap("key12", "value12", taskInput);
TestUtils.putInMap("key13", "workflow.variables.key13", taskInput);

// Initialize tasks
TaskModel taskModel1 = new TaskModel();
taskModel1.setInputData(taskInput);
taskModel1.setOutputData(taskInput);
taskModel1.setTaskType("TASK_TYPE_1");
TaskModel taskModel2 = new TaskModel();
taskModel2.setInputData(taskInput);
taskModel2.setOutputData(taskInput);
taskModel2.setTaskType("TASK_TYPE_2");
tasks.add(taskModel1);
tasks.add(taskModel2);

// Initialize workflowDef
workflowDef.setInputTemplate(inputTemplateV1);
workflowDef.setSchemaVersion(1);

// Initialize workflowModel
workflowModel.setInput(workflowInput);
workflowModel.setTasks(tasks);
workflowModel.setVariables(variables);
workflowModel.setWorkflowDefinition(workflowDef);
workflowModel.setWorkflowInstanceId("workflow-instance-id");

// Initialize taskDef
taskDef.setInputTemplate(inputTemplateV2);
}

@Test
public void testGetTaskInput_SchemaVersion1() {
when(EnvUtils.isEnvironmentVariable("envVar")).thenReturn(true);
when(EnvUtils.getSystemParametersValue("envVar", "task-id")).thenReturn("env-value");

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "task-id");

// Check if the result contains correct values
assertEquals("value1", result.get("key1"));
assertEquals("value5", result.get("key5"));
assertEquals("workflow.input.key2", result.get("key2"));
assertEquals("task1.input.key3", result.get("key3"));
assertEquals("task1.output.key4", result.get("key4"));
assertEquals("env-value", result.get("envVar"));
}

@Test
public void testGetTaskInput_SchemaVersion2() {
// ... similar test cases for schema version 2
}

// Add more test cases for other methods like replace(), replaceVariables(), getWorkflowInput()

}