import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

@Component
class ParametersUtilsTest {

@Mock private ObjectMapper objectMapper;
@Mock private EnvUtils envUtils;
@Mock private TaskUtils taskUtils;

@InjectMocks private ParametersUtils parametersUtils;

private DocumentContext documentContext;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

documentContext = JsonPath.parse(createInputMap());
}

@Test
public void testGetTaskInputV2() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key1", "value1");
inputParams.put("key2", "${workflow.input.param1}");

WorkflowModel workflow = createWorkflowModel();
TaskDef taskDefinition = createTaskDef();

Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, "task-1", taskDefinition);

assertEquals("value1", result.get("key1"));
assertEquals("input-value", result.get("key2"));
}

@Test
public void testReplace() {
String input = "This is a ${workflow.input.param1} test";

DocumentContext documentContext =
JsonPath.parse("{\"input\": {\"param1\": \"input-value\"}}");

String result = parametersUtils.replace(input, documentContext, null).toString();

assertEquals("This is a input-value test", result);
}

@Test
public void testReplaceVariables() {
String input = "This is a ${workflow.input.param1} test";

DocumentContext documentContext =
JsonPath.parse("{\"input\": {\"param1\": \"input-value\"}}");

String result = parametersUtils.replaceVariables(input, documentContext, null).toString();

assertEquals("This is a input-value test", result);
}

@Test
public void testReplaceVariablesEmptyParamPath() {
String input = "This is a ${} test";

DocumentContext documentContext = JsonPath.parse("{}");

String result = parametersUtils.replaceVariables(input, documentContext, null).toString();

assertEquals("This is a  test", result);
}

@Test
public void testReplaceVariablesEnvVariable() {
String input = "This is a ${ENV_VAR} test";

when(envUtils.isEnvironmentVariable("ENV_VAR")).thenReturn(true);
when(envUtils.getSystemParametersValue("ENV_VAR", null)).thenReturn("env-value");

String result = parametersUtils.replaceVariables(input, documentContext, null).toString();

assertEquals("This is a env-value test", result);
}

@Test
public void testReplaceVariablesException() {
String input = "This is a ${workflow.input.param1} test";

DocumentContext documentContext = JsonPath.parse("{}");

String result = parametersUtils.replaceVariables(input, documentContext, null).toString();

assertEquals(input, result);
}

@Test
public void testReplaceList() {
List<String> input = Arrays.asList("item1", "${workflow.input.param1}", "item3");

DocumentContext documentContext =
JsonPath.parse("{\"input\": {\"param1\": \"input-value\"}}");

List<Object> result = parametersUtils.replaceList(input, null, documentContext);

assertEquals(Arrays.asList("item1", "input-value", "item3"), result);
}

private Map<String, Object> createInputMap() {
return new HashMap<String, Object>() {{
put("workflow", new HashMap<String, Object>() {{
put("input", new HashMap<String, Object>() {{
put("param1", "input-value");
}});
}});
}};
}

private WorkflowModel createWorkflowModel() {
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(new HashMap<String, Object>() {{
put("param1", "input-value");
}});
return workflowModel;
}

private TaskDef createTaskDef() {
return new TaskDef() {{
setInputTemplate(new HashMap<String, Object>() {{
put("key1", "value1");
}});
}};
}
}