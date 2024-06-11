import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
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

private WorkflowModel workflowModel;
private TaskDef taskDef;
private String taskId;
private Map<String, Object> inputParams;
private Map<String, Object> workflowParams;
private Map<String, Map<String, Object>> inputMap;
private DocumentContext documentContext;
private Configuration config;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

workflowModel = mock(WorkflowModel.class);
taskDef = mock(TaskDef.class);
taskId = "task1";
inputParams = new HashMap<>();
workflowParams = new HashMap<>();
inputMap = new HashMap<>();
config = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);

when(workflowModel.getTasks())
.thenReturn(Arrays.asList(createTaskModel("task1", "inputData1"), createTaskModel("task2", "inputData2")));

when(workflowModel.getWorkflowDefinition().getSchemaVersion())
.thenReturn(1);
}

@Test
public void testGetTaskInputV1() {
Map<String, Object> result =
parametersUtils.getTaskInputV1(workflowModel, inputParams);

assertEquals(0, result.size());
}

@Test
public void testGetTaskInputV2() {
when(taskDef.getInputTemplate())
.thenReturn(createTaskInputTemplate());

Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflowModel, taskId, taskDef);

assertEquals(1, result.size());
assertEquals("inputData1", result.get("param1"));
}

@Test
public void testReplace() {
documentContext =
JsonPath.parse("{\"a\":{\"b\":\"c\"}}", config);

String result =
parametersUtils.replace(inputParams, documentContext, null);

assertEquals("{\"a\":{\"b\":\"c\"}}", result);
}

@Test
public void testReplaceVariables() {
Object result =
parametersUtils.replaceVariables("${a.b}", documentContext, null);

assertEquals("c", result);
}

@Test
public void testReplaceVariablesWithEnvVariable() {
when(envUtils.getSystemParametersValue("a", taskId))
.thenReturn("envValue");

Object result =
parametersUtils.replaceVariables("${a}", documentContext, taskId);

assertEquals("envValue", result);
}

@Test
public void testReplaceVariablesWithNothingInside() {
Object result =
parametersUtils.replaceVariables("$$", documentContext, null);

assertEquals("${", result);
}

@Test
public void testClone() throws Exception {
Map<String, Object> inputTemplate = new HashMap<>();
inputTemplate.put("param1", "value1");

Map<String, Object> cloned = parametersUtils.clone(inputTemplate);

assertEquals("value1", cloned.get("param1"));
}

private TaskModel createTaskModel(String taskName, String inputData) {
TaskModel taskModel = new TaskModel();
taskModel.setReferenceTaskName(taskName);
taskModel.setInputData(createInputMap(inputData));
return taskModel;
}

private Map<String, Object> createTaskInputTemplate() {
Map<String, Object> inputTemplate = new HashMap<>();
inputTemplate.put("param1", "inputData1");
return inputTemplate;
}

private Map<String, Object> createInputMap(String inputData) {
Map<String, Object> inputMap = new HashMap<>();
inputMap.put("param1", inputData);
return inputMap;
}
}