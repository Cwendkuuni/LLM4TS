import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
import java.util.Map.Entry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class ParametersUtilsTest {

private static final Logger LOGGER =
LoggerFactory.getLogger(ParametersUtilsTest.class);
private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;
private WorkflowModel workflowModel;
private TaskDef taskDef;
private Map<String, Object> inputParams;
private Map<String, Object> workflowInput;
private Map<String, Object> inputTemplate;
private DocumentContext documentContext;
private EnvUtils envUtils;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
objectMapper = mock(ObjectMapper.class);
envUtils = mock(EnvUtils.class);
parametersUtils =
new ParametersUtils(objectMapper, envUtils);
workflowInput = new HashMap<>();
workflowInput.put("inputKey", "workflowValue");
workflowModel = new WorkflowModel();
workflowModel.setInput(workflowInput);
taskDef = new TaskDef();
inputParams = new HashMap<>();
inputParams.put("paramString", "${workflow.input.inputKey}");
documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.build()
.parse(Collections.emptyMap());
}

@Test
public void testGetTaskInput() {
Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");
assertEquals(workflowInput.get("inputKey"), result.get("paramString"));
}

@Test
public void testGetTaskInputV2() {
Map<String, Object> taskInputTemplate = new HashMap<>();
taskInputTemplate.put("paramString", "taskValue");
taskDef.setInputTemplate(taskInputTemplate);
inputParams.put("paramString", "${task.input.paramString}");
Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);
assertEquals(taskInputTemplate.get("paramString"), result.get("paramString"));
}

@Test
public void testGetTaskInputV2NullInputParams() {
inputParams = null;
Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);
assertEquals(0, result.size());
}

@Test
public void testGetTaskInputV2NullTaskDefinition() {
taskDef = null;
Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);
assertEquals(0, result.size());
}

@Test
public void testClone() throws IOException {
Map<String, Object> inputTemplate = new HashMap<>();
inputTemplate.put("key", "value");
when(objectMapper.writeValueAsBytes(inputTemplate))
.thenReturn("testBytes".getBytes());
when(objectMapper.readValue("testBytes".getBytes(), new TypeReference<Map<String, Object>>() {}))
.thenReturn(inputTemplate);
Map<String, Object> clonedMap = parametersUtils.clone(inputTemplate);
assertEquals(inputTemplate, clonedMap);
}

@Test
public void testReplace() {
inputParams.put("paramString", "${workflow.input.inputKey}");
documentContext.set("inputKey", "replacedValue");
Map<String, Object> result =
parametersUtils.replace(inputParams, documentContext, "taskId");
assertEquals("replacedValue", result.get("paramString"));
}

@Test
public void testReplaceList() {
List<Object> list = new LinkedList<>();
list.add("${workflow.input.inputKey}");
documentContext.set("inputKey", "replacedValue");
List<Object> replacedList =
(List<Object>) parametersUtils.replaceList(list, "taskId", documentContext);
assertEquals("replacedValue", replacedList.get(0));
}

@Test
public void testReplaceVariables() {
String replaced =
parametersUtils.replaceVariables("${workflow.input.inputKey}", documentContext, "taskId");
assertEquals("replacedValue", replaced);
}

@Test
public void testReplaceVariablesNullParam() {
String replaced =
parametersUtils.replaceVariables(null, documentContext, "taskId");
assertNull(replaced);
}

@Test
public void testReplaceVariablesNullDocumentContext() {
String replaced =
parametersUtils.replaceVariables("${workflow.input.inputKey}", null, "taskId");
assertNull(replaced);
}

@Test
public void testReplaceVariablesEmptyParam() {
String replaced =
parametersUtils.replaceVariables("", documentContext, "taskId");
assertEquals("", replaced);
}

@Test
public void testReplaceVariablesNoMatch() {
String replaced =
parametersUtils.replaceVariables("noMatch", documentContext, "taskId");
assertEquals("noMatch", replaced);
}

@Test
public void testReplaceVariablesDepth() {
String replaced =
parametersUtils.replaceVariables("${${workflow.input.inputKey}}", documentContext, "taskId", 1);
assertEquals("${replacedValue}", replaced);
}

@Test
public void testReplaceVariablesEnvVariable() {
String replaced =
parametersUtils.replaceVariables("${ENV_VAR}", documentContext, "taskId");
when(envUtils.getSystemParametersValue("ENV_VAR", "taskId"))
.thenReturn("envValue");
assertEquals("envValue", replaced);
}

@Test
public void testReplaceVariablesEnvVariableNull() {
String replaced =
parametersUtils.replaceVariables("${ENV_VAR}", documentContext, "taskId");
when(envUtils.getSystemParametersValue("ENV_VAR", "taskId"))
.thenReturn(null);
assertEquals("${ENV_VAR}", replaced);
}

@Test
public void testGetWorkflowInput() {
Map<String, Object> workflowInputTemplate = new HashMap<>();
workflowInputTemplate.put("workflowKey", "workflowValue");
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(workflowInputTemplate);
Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);
assertEquals(workflowInputTemplate, result);
}

@Test
public void testGetWorkflowInputNullWorkflowDef() {
WorkflowDef workflowDef = null;
Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);
assertEquals(0, result.size());
}

@Test
public void testGetWorkflowInputNullInputParams() {
inputParams = null;
Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);
assertEquals(0, result.size());
}
}