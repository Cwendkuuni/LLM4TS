import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.Map.Entry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.stereotype.Component;

@Component
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
parametersUtils =
new ParametersUtils(
objectMapper, envUtils, taskUtils) {
{
setComponentName("ParametersUtilsTest");
}
};
}

@Test
public void testGetTaskInputV2() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("inputKey", "inputValue");
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(Collections.singletonMap("workflowInputKey", "workflowInputValue"));
workflowModel.setTasks(
new LinkedList<TaskModel>() {
{
add(
new TaskModel() {
{
setInputData(Collections.singletonMap("taskInputKey", "taskInputValue"));
}
});
}
});
TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(Collections.singletonMap("taskInputTemplateKey", "taskInputTemplateValue"));

Map<String, Object> result =
parametersUtils.getTaskInputV2(
inputParams, workflowModel, "taskId", taskDef);

ArgumentCaptor<TypeReference> typeRefCaptor = ArgumentCaptor.forClass(TypeReference.class);
verify(objectMapper).readValue(any(), typeRefCaptor.capture());
TypeReference typeRef = typeRefCaptor.getValue();
assertEquals(new TypeReference<Map<String, Object>>() {}, typeRef);

Map<String, Map<String, Object>> inputMap =
(Map<String, Map<String, Object>>) typeRef.getType().getRawClass().cast(typeRef.getValue());
Map<String, Object> workflowParams = inputMap.get("workflow");
Map<String, Object> taskParams = inputMap.get("task");

assertEquals("inputValue", inputParams.get("inputKey"));
assertEquals("workflowInputValue", workflowParams.get("input"));
assertEquals("taskInputValue", taskParams.get("input"));
assertEquals("taskInputTemplateValue", taskDef.getInputTemplate().get("taskInputTemplateKey"));
}

private Configuration configurationWithSuppressExceptions() {
return Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
}

@Test
public void testReplace() {
DocumentContext documentContext =
JsonPath.parse("{\"key\":\"value\"}", configurationWithSuppressExceptions());

assertEquals(
"replacedValue",
parametersUtils.replace("${key}", documentContext, "taskId", 0).toString());
}

@Test
public void testReplaceList() {
List<String> values = new LinkedList<String>() {
{
add("value1");
add("${key}");
add("value3");
}
};
DocumentContext documentContext =
JsonPath.parse("{\"key\":\"replacedValue\"}", configurationWithSuppressExceptions());

assertEquals(
new LinkedList<String>() {{
add("value1");
add("replacedValue");
add("value3");
}},
parametersUtils.replaceList(values, "taskId", documentContext));
}

@Test
public void testReplaceVariables() {
String paramString = "pre${key}post";
DocumentContext documentContext =
JsonPath.parse("{\"key\":\"value\"}", configurationWithSuppressExceptions());

assertEquals(
"prevaluepost",
parametersUtils.replaceVariables(paramString, documentContext, "taskId", 0).toString());
}

@Test
public void testReplaceVariablesDepthOne() {
String paramString = "${inner${key}}";
DocumentContext documentContext =
JsonPath.parse("{\"key\":\"value\"}", configurationWithSuppressExceptions());

assertEquals(
"innervalue",
parametersUtils.replaceVariables(paramString, documentContext, "taskId", 1).toString());
}

@Test
public void testReplaceVariablesWithEmptyParamPath() {
String paramString = "pre${}post";
DocumentContext documentContext =
JsonPath.parse("{}", configurationWithSuppressExceptions());

assertEquals(
"prepost",
parametersUtils.replaceVariables(paramString, documentContext, "taskId", 0).toString());
}

@Test
public void testReplaceVariablesWithEmptyDocumentContext() {
String paramString = "pre${key}post";
DocumentContext documentContext =
JsonPath.parse("", configurationWithSuppressExceptions());

assertEquals(
paramString,
parametersUtils.replaceVariables(paramString, documentContext, "taskId", 0).toString());
}

@Test
public void testReplaceVariablesWithEnvironmentVariable() {
String paramString = "${ENV_VAR}";
DocumentContext documentContext =
JsonPath.parse("{}", configurationWithSuppressExceptions());

when(envUtils.isEnvironmentVariable(paramString))
.thenReturn(true);
when(envUtils.getSystemParametersValue(paramString, "taskId"))
.thenReturn("envValue");

assertEquals(
"envValue",
parametersUtils.replaceVariables(paramString, documentContext, "taskId", 0).toString());
}

@Test
public void testReplaceVariablesWithException() {
String paramString = "${key}";
DocumentContext documentContext =
JsonPath.parse("{}", configurationWithSuppressExceptions());

when(envUtils.isEnvironmentVariable(paramString))
.thenReturn(false);
doThrow(new RuntimeException("test exception"))
.when(envUtils)
.getSystemParametersValue(anyString(), anyString());

assertEquals(
paramString,
parametersUtils.replaceVariables(paramString, documentContext, "taskId", 0).toString());
}

@Test
public void testGetWorkflowInput() {
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(Collections.singletonMap("workflowInputTemplateKey", "workflowInputTemplateValue"));

Map<String, Object> inputParams = new HashMap<>();

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals("workflowInputTemplateValue", workflowDef.getInputTemplate().get("workflowInputTemplateKey"));
assertEquals(inputParams, result);
}
}