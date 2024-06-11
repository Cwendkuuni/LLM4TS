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
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
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

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;
private EnvUtils envUtils;

@Before
public void setup() {
objectMapper = Mockito.mock(ObjectMapper.class);
envUtils = Mockito.spy(new EnvUtils());
parametersUtils = new ParametersUtils(objectMapper) {
@Override
protected ObjectMapper getObjectMapper() {
return objectMapper;
}

@Override
protected EnvUtils getEnvUtils() {
return envUtils;
}
};
}

@Test
public void testGetTaskInput_WithNullInputParams() {
Map<String, Object> result = parametersUtils.getTaskInput(null, new WorkflowModel(), new TaskDef(), "taskId");
Assert.assertTrue(result.isEmpty());
}

@Test
public void testGetTaskInput_WithNullTaskDefinition() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "${workflow.input.param}");
WorkflowModel workflow = TestUtils.createWorkflowModel();
Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, null, "taskId");
Assert.assertEquals(result.get("key"), workflow.getInput().get("param"));
}

@Test
public void testGetTaskInput_WithInputTemplate() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "${workflow.input.param}");
TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(Collections.singletonMap("key", "inputTemplateValue"));
WorkflowModel workflow = TestUtils.createWorkflowModel();
Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, taskDef, "taskId");
Assert.assertEquals(result.get("key"), "inputTemplateValue");
}

@Test
public void testGetTaskInput_WithNestedParams() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "${workflow.input.param.nestedParam}");
WorkflowModel workflow = TestUtils.createWorkflowModel();
workflow.getInput().put("param", Collections.singletonMap("nestedParam", "nestedValue"));
Map<String, Object> result = parametersUtils.getTaskInput(inputParams, workflow, new TaskDef(), "taskId");
Assert.assertEquals(result.get("key"), "nestedValue");
}

@Test
public void testReplace_WithNullJson() {
String input = "This is a ${param} string";
Assert.assertEquals(
parametersUtils.replace(input),
input.replace("${param}", "null"));
}

@Test
public void testReplace_WithJsonAsString() {
String json = "{\"param\": \"value\", \"nested\": {\"param\": \"nestedValue\"}}";
String input = "This is a ${param} string";
Assert.assertEquals(
parametersUtils.replace(input, json),
input.replace("${param}", "value"));
}

@Test
public void testReplace_WithJsonAsMap() {
Map<String, Object> json = new HashMap<>();
json.put("param", "value");
json.put("nested", Collections.singletonMap("param", "nestedValue"));
String input = "This is a ${param} string";
Assert.assertEquals(
parametersUtils.replace(input, json),
input.replace("${param}", "value"));
}

@Test
public void testReplace_WithNullParamInJson() {
String json = "{\"param\": null}";
String input = "This is a ${param} string";
Assert.assertEquals(
parametersUtils.replace(input, json),
input.replace("${param}", "null"));
}

@Test
public void testReplace_WithMissingParamInJson() {
String json = "{}";
String input = "This is a ${param} string";
Assert.assertEquals(parametersUtils.replace(input, json), input);
}

@Test
public void testReplace_WithNestedParams() {
String json = "{\"param\": {\"nested\": \"nestedValue\"}}";
String input = "This is a ${param.nested} string";
Assert.assertEquals(
parametersUtils.replace(input, json),
input.replace("${param.nested}", "nestedValue"));
}

@Test
public void testReplace_WithEnvVariable() {
System.setProperty("test", "envValue");
String json = "{}";
String input = "This is a ${test} string";
Assert.assertEquals(
parametersUtils.replace(input),
input.replace("${test}", "envValue"));
System.clearProperty("test");
}

@Test
public void testReplace_WithEnvVariableInJson() {
System.setProperty("test", "envValue");
Map<String, Object> json = new HashMap<>();
json.put("param", "${test}");
String input = "This is a ${param} string";
Assert.assertEquals(
parametersUtils.replace(input, json),
input.replace("${param}", "envValue"));
System.clearProperty("test");
}

@Test
public void testReplaceVariables_WithNullJson() {
String input = "This is a ${param} string";
Assert.assertEquals(
parametersUtils.replaceVariables(input),
input.replace("${param}", "null"));
}

@Test
public void testReplaceVariables_WithJsonAsString() {
String json = "{\"param\": \"value\", \"nested\": {\"param\": \"nestedValue\"}}";
String input = "This is a ${param} string";
Assert.assertEquals(
parametersUtils.replaceVariables(input, JsonPath.parse(json)),
input.replace("${param}", "value"));
}

@Test
public void testReplaceVariables_WithJsonAsMap() {
Map<String, Object> json = new HashMap<>();
json.put("param", "value");
json.put("nested", Collections.singletonMap("param", "nestedValue"));
String input = "This is a ${param} string";
Assert.assertEquals(
parametersUtils.replaceVariables(input, JsonPath.parse(json.toString())),
input.replace("${param}", "value"));
}

@Test
public void testReplaceVariables_WithNullParamInJson() {
String json = "{\"param\": null}";
String input = "This is a ${param} string";
Assert.assertEquals(
parametersUtils.replaceVariables(input, JsonPath.parse(json)),
input.replace("${param}", "null"));
}

@Test
public void testReplaceVariables_WithMissingParamInJson() {
String json = "{}";
String input = "This is a ${param} string";
Assert.assertEquals(parametersUtils.replaceVariables(input, JsonPath.parse(json)), input);
}

@Test
public void testReplaceVariables_WithNestedParams() {
String json = "{\"param\": {\"nested\": \"nestedValue\"}}";
String input = "This is a ${param.nested} string";
Assert.assertEquals(
parametersUtils.replaceVariables(input, JsonPath.parse(json)),
input.replace("${param.nested}", "nestedValue"));
}

@Test
public void testReplaceVariables_WithEnvVariable() {
System.setProperty("test", "envValue");
String json = "{}";
String input = "This is a ${test} string";
Assert.assertEquals(
parametersUtils.replaceVariables(input),
input.replace("${test}", "envValue"));
System.clearProperty("test");
}

@Test
public void testReplaceVariables_WithEnvVariableInJson() {
System.setProperty("test", "envValue");
DocumentContext documentContext = JsonPath.parse("{\"param\": \"${test}\"}");
String input = "This is a ${param} string";
Assert.assertEquals(
parametersUtils.replaceVariables(input, documentContext),
input.replace("${param}", "envValue"));
System.clearProperty("test");
}

@Test
public void testGetWorkflowInput_WithNullWorkflowDefAndInputParams() {
Map<String, Object> result =
parametersUtils.getWorkflowInput(null, new HashMap<>());
Assert.assertTrue(result.isEmpty());
}

@Test
public void testGetWorkflowInput_WithInputTemplate() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "${workflow.input.param}");
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(Collections.singletonMap("key", "inputTemplateValue"));
Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);
Assert.assertEquals(result.get("key"), "inputTemplateValue");
}

@Test
public void testGetWorkflowInput_WithNestedParams() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "${workflow.input.param.nestedParam}");
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInput(Collections.singletonMap("param", Collections.singletonMap("nestedParam", "nestedValue")));
Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);
Assert.assertEquals(result.get("key"), "nestedValue");
}

}