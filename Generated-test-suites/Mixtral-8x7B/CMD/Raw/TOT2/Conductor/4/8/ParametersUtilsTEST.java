import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.utils.ParametersUtils;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
objectMapper = mock(ObjectMapper.class);
parametersUtils = new ParametersUtils(objectMapper);
}

// Test for getTaskInput method
@Test
public void testGetTaskInput() {
WorkflowModel workflow = new WorkflowModel();
workflow.setInput(new HashMap<>());
workflow.setOutput(new HashMap<>());
workflow.setStatus("COMPLETED");
workflow.setWorkflowId("123");
workflow.setParentWorkflowId("456");
workflow.setParentWorkflowTaskId("789");
workflow.setWorkflowName("testWorkflow");
workflow.setWorkflowVersion(1);
workflow.setCorrelationId("correlationId");
workflow.setReasonForIncompletion("taskFailed");
workflow.setSchemaVersion(1);
workflow.setVariables(new HashMap<>());

TaskDef taskDefinition = new TaskDef();
taskDefinition.setInputTemplate(new HashMap<>());

Map<String, Object> inputParams = new HashMap<>();

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");

assertEquals(inputParams, result);
}

// Test for getTaskInput method with inputTemplate
@Test
public void testGetTaskInputWithInputTemplate() {
WorkflowModel workflow = new WorkflowModel();
workflow.setInput(new HashMap<>());
workflow.setOutput(new HashMap<>());
workflow.setStatus("COMPLETED");
workflow.setWorkflowId("123");
workflow.setParentWorkflowId("456");
workflow.setParentWorkflowTaskId("789");
workflow.setWorkflowName("testWorkflow");
workflow.setWorkflowVersion(1);
workflow.setCorrelationId("correlationId");
workflow.setReasonForIncompletion("taskFailed");
workflow.setSchemaVersion(1);
workflow.setVariables(new HashMap<>());

TaskDef taskDefinition = new TaskDef();
Map<String, Object> inputTemplate = new HashMap<>();
inputTemplate.put("param1", "value1");
taskDefinition.setInputTemplate(inputTemplate);

Map<String, Object> inputParams = new HashMap<>();

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");

assertEquals(inputParams, result);
assertEquals(inputParams.get("param1"), "value1");
}

// Test for replace method with string input
@Test
public void testReplaceString() {
String json = "{\"param1\":\"${param2}\"}";
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("param2", "value2");

String result = parametersUtils.replace(json, inputParams).toString();

assertEquals(result, "{\"param1\":\"value2\"}");
}

// Test for replace method with object input
@Test
public void testReplaceObject() {
String json = "{\"param1\":\"${param2}\"}";
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("param2", "value2");

String result =
parametersUtils.replace(JsonPath.parse(json), inputParams).read("$").toString();

assertEquals(result, "{\"param1\":\"value2\"}");
}

// Test for replaceVariables method with string input
@Test
public void testReplaceVariables() {
String paramString = "${param1}";
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("param1", "value1");

String result =
parametersUtils.replaceVariables(paramString, inputParams, null).toString();

assertEquals(result, "value1");
}

// Test for replaceVariables method with null input
@Test
public void testReplaceVariablesWithNull() {
String paramString = "${param1}";
Map<String, Object> inputParams = new HashMap<>();

String result =
parametersUtils.replaceVariables(paramString, inputParams, "taskId").toString();

assertEquals(result, "${param1}");
}

// Test for getWorkflowInput method
@Test
public void testGetWorkflowInput() {
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(new HashMap<>());

Map<String, Object> inputParams = new HashMap<>();

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(inputParams, result);
}

// Test for deep clone using json - POJO
@Test
public void testDeepClone() throws IOException {
Map<String, Object> inputTemplate = new HashMap<>();
inputTemplate.put("param1", "value1");

Map<String, Object> clone = parametersUtils.clone(inputTemplate);

assertEquals(inputTemplate, clone);
assertEquals(inputTemplate.get("param1"), clone.get("param1"));
}
}