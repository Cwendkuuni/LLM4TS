import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParameterUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

private static final Logger LOGGER = LoggerFactory.getLogger(ParameterUtils.class);
private ParameterUtils parameterUtils;
private ObjectMapper objectMapper = mock(ObjectMapper.class);
private EnvUtils envUtils = mock(EnvUtils.class);
private TaskUtils taskUtils = mock(TaskUtils.class);

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
when(objectMapper.writeValueAsBytes(mock(Map.class))).thenReturn(new byte[0]);
when(objectMapper.readValue(new byte[0], new TypeReference<Map<String, Object>>() {
}))
.thenReturn(new HashMap<>());
parameterUtils = new ParameterUtils(objectMapper, envUtils, taskUtils);
}

@Test
public void testGetTaskInput() throws Exception {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflow.input.testParam", "testValue");

WorkflowModel workflow = new WorkflowModel();
workflow.setInput(Collections.singletonMap("input", "workflowValue"));

TaskDef taskDefinition = new TaskDef();
taskDefinition.setInputTemplate(Collections.singletonMap("taskParam", "taskDefaultValue"));

Map<String, Object> result =
parameterUtils.getTaskInput(inputParams, workflow, taskDefinition, "123");

Assert.assertEquals("testValue", result.get("workflow.input.testParam"));
Assert.assertEquals("workflowValue", result.get("workflow.input.input"));
Assert.assertEquals("taskDefaultValue", result.get("taskParam"));
}

@Test
public void testGetTaskInputWithComplexParam() throws Exception {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflow.input.testParam[0]", "testValue0");
inputParams.put("workflow.input.testParam[1]", "testValue1");

WorkflowModel workflow = new WorkflowModel();
workflow.setInput(Collections.singletonMap("input", "workflowValue"));

TaskDef taskDefinition = new TaskDef();
taskDefinition.setInputTemplate(Collections.singletonMap("taskParam[0]", "taskDefaultValue0"));

Map<String, Object> result =
parameterUtils.getTaskInput(inputParams, workflow, taskDefinition, "123");

Assert.assertEquals("testValue0", result.get("workflow.input.testParam[0]"));
Assert.assertEquals("testValue1", result.get("workflow.input.testParam[1]"));
Assert.assertEquals("taskDefaultValue0", result.get("taskParam[0]"));
}

@Test
public void testReplace() throws Exception {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("testParam", "${workflow.input.testParam}");

WorkflowModel workflow = new WorkflowModel();
workflow.setInput(Collections.singletonMap("input", "workflowValue"));

DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.build()
.parse("{\"workflow\":{\"input\":{\"testParam\":\"workflowValue\"}}}");

Map<String, Object> result =
parameterUtils.replace(inputParams, documentContext, "123");

Assert.assertEquals("workflowValue", result.get("testParam"));
}

@Test
public void testReplaceWithComplexParam() throws Exception {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("testParam[0]", "${workflow.input.testParam[0]}");
inputParams.put("testParam[1]", "${workflow.input.testParam[1]}");

WorkflowModel workflow = new WorkflowModel();
workflow.setInput(
Collections.singletonMap("input", "workflowValue0,workflowValue1"));

DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.build()
.parse("{\"workflow\":{\"input\":{\"testParam\":[\"workflowValue0\",\"workflowValue1\"]}}}");

Map<String, Object> result =
parameterUtils.replace(inputParams, documentContext, "123");

Assert.assertEquals("workflowValue0", result.get("testParam[0]"));
Assert.assertEquals("workflowValue1", result.get("testParam[1]"));
}

@Test
public void testReplaceWithEnvVar() throws Exception {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("testParam", "${ENV_VAR_TEST}");

when(envUtils.isEnvironmentVariable("ENV_VAR_TEST"))
.thenReturn(true);
when(envUtils.getSystemParametersValue("ENV_VAR_TEST", "123"))
.thenReturn("envVarValue");

DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.build()
.parse("{}");

Map<String, Object> result =
parameterUtils.replace(inputParams, documentContext, "123");

Assert.assertEquals("envVarValue", result.get("testParam"));
}

@Test
public void testReplaceWithInvalidJsonPath() throws Exception {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("testParam", "${workflow.invalid.json.path}");

WorkflowModel workflow = new WorkflowModel();
workflow.setInput(Collections.singletonMap("input", "workflowValue"));

DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.build()
.parse("{}");

Map<String, Object> result =
parameterUtils.replace(inputParams, documentContext, "123");

Assert.assertNull(result.get("testParam"));

LOGGER.warn("Error reading documentContext for paramPath: workflow.invalid.json.path. Exception: {}",
LOGGER.isDebugEnabled()
? ""
: null);
}

@Test
public void testReplaceVariables() throws Exception {
String paramString = "test${workflow.input.testParam}test";

WorkflowModel workflow = new WorkflowModel();
workflow.setInput(Collections.singletonMap("input", "workflowValue"));

DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.build()
.parse("{\"workflow\":{\"input\":{\"testParam\":\"workflowValue\"}}}");

String result =
parameterUtils.replaceVariables(paramString, documentContext, null);

Assert.assertEquals("testworkflowValuetest", result);
}

@Test
public void testReplaceVariablesWithEnvVar() throws Exception {
String paramString = "test${ENV_VAR_TEST}test";

when(envUtils.isEnvironmentVariable("ENV_VAR_TEST"))
.thenReturn(true);
when(envUtils.getSystemParametersValue("ENV_VAR_TEST", null))
.thenReturn("envVarValue");

DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.build()
.parse("{}");

String result =
parameterUtils.replaceVariables(paramString, documentContext, null);

Assert.assertEquals("testenvVarValuetest", result);
}

@Test
public void testReplaceVariablesWithInvalidJsonPath() throws Exception {
String paramString = "test${workflow.invalid.json.path}test";

WorkflowModel workflow = new WorkflowModel();
workflow.setInput(Collections.singletonMap("input", "workflowValue"));

DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.build()
.parse("{}");

String result =
parameterUtils.replaceVariables(paramString, documentContext, null);

Assert.assertEquals("test${workflow.invalid.json.path}test", result);
}

@Test
public void testGetWorkflowInput() throws Exception {
Map<String, Object> inputParams = new HashMap<>();

WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(Collections.singletonMap("workflowParam", "workflowDefaultValue"));

Map<String, Object> result =
parameterUtils.getWorkflowInput(workflowDef, inputParams);

Assert.assertNull(result.get("workflowParam"));

inputParams.put("workflowParam", "workflowValue");

result = parameterUtils.getWorkflowInput(workflowDef, inputParams);

Assert.assertEquals("workflowValue", result.get("workflowParam"));
}

@Test
public void testDeepClone() throws Exception {
Map<String, Object> inputTemplate = new HashMap<>();
inputTemplate.put("testParam", "testValue");

TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
};

String json = objectMapper.writeValueAsString(inputTemplate);

Map<String, Object> result =
objectMapper.readValue(objectMapper.writeValueAsBytes(inputTemplate), typeReference);

Assert.assertEquals(inputTemplate, result);
Assert.assertSame(inputTemplate, result);
}
}