package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;
private Logger logger;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
objectMapper = mock(ObjectMapper.class);
logger = mock(Logger.class);
parametersUtils = new ParametersUtils(objectMapper) {
@Override
Logger getLogger() {
return logger;
}
};
}

@Test
public void testGetTaskInputV2() throws IOException {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("inputParam1", "value1");
inputParams.put("inputParam2", "value2");

WorkflowModel workflow = new WorkflowModel();
workflow.setWorkflowId("workflowId");
workflow.setWorkflowName("workflowName");
workflow.setWorkflowVersion(1);
workflow.setStatus("RUNNING");
workflow.setInput(new HashMap<>());
workflow.setOutput(new HashMap<>());
workflow.setVariables(new HashMap<>());

TaskDef taskDefinition = new TaskDef();
taskDefinition.setInputTemplate(new HashMap<>());

String taskId = "taskId";

Map<String, Object> expected = new HashMap<>();
expected.put("inputParam1", "value1");
expected.put("inputParam2", "value2");
expected.put("workflowId", "workflowId");
expected.put("workflowName", "workflowName");
expected.put("workflowVersion", 1);
expected.put("status", "RUNNING");
expected.put("input", new HashMap<>());
expected.put("output", new HashMap<>());
expected.put("variables", new HashMap<>());
expected.put("taskId", "taskId");
expected.put("taskDefName", taskDefinition.getName());

Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflow, taskDefinition, taskId);

assertEquals(expected, result);
}

@Test
public void testReplace() throws IOException {
String input = "This is a ${param1} and this is another ${param2}";
Map<String, Object> inputMap = new HashMap<>();
inputMap.put("param1", "value1");
inputMap.put("param2", "value2");

DocumentContext documentContext = JsonPath.parse(inputMap);
String expected = "This is a value1 and this is another value2";

String result = parametersUtils.replace(input, documentContext);

assertEquals(expected, result);
}

@Test
public void testReplaceVariables() throws IOException {
String input = "This is a ${param1} and this is another ${param2}";
Map<String, Object> inputMap = new HashMap<>();
inputMap.put("param1", "value1");
inputMap.put("param2", "value2");

Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = JsonPath.parse(inputMap, option);
String taskId = "taskId";
String expected = "This is a value1 and this is another value2";

String result =
parametersUtils.replaceVariables(input, documentContext, taskId);

assertEquals(expected, result);
}

@Test
public void testReplaceList() throws IOException {
List<String> input = new LinkedList<>();
input.add("This is a ${param1} and this is another ${param2}");
String taskId = "taskId";
Map<String, Object> inputMap = new HashMap<>();
inputMap.put("param1", "value1");
inputMap.put("param2", "value2");

Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = JsonPath.parse(inputMap, option);
List<Object> expected = new LinkedList<>();
expected.add("This is a value1 and this is another value2");

List<Object> result =
parametersUtils.replaceList(input, taskId, documentContext);

assertEquals(expected, result);
}

@Test
public void testReplaceVariablesWithEmptyParam() {
String input = "This is a ${} and this is another ${  }";
Map<String, Object> inputMap = new HashMap<>();
inputMap.put("param1", "value1");
inputMap.put("param2", "value2");

Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = JsonPath.parse(inputMap, option);
String taskId = "taskId";
String expected = "This is a  and this is another   ";

String result =
parametersUtils.replaceVariables(input, documentContext, taskId);

assertEquals(expected, result);
}

@Test
public void testReplaceVariablesWithSystemParameters() {
String input = "${sysParam1}";
Map<String, Object> inputMap = new HashMap<>();

Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = JsonPath.parse(inputMap, option);
String taskId = "taskId";
String sysValue = "sysValue1";

when(EnvUtils.getSystemParametersValue(input, taskId))
.thenReturn(sysValue);

String expected = "sysValue1";

String result =
parametersUtils.replaceVariables(input, documentContext, taskId);

assertEquals(expected, result);
}

@Test
public void testReplaceVariablesWithSystemParametersWithNullValue() {
String input = "${sysParam1}";
Map<String, Object> inputMap = new HashMap<>();

Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = JsonPath.parse(inputMap, option);
String taskId = "taskId";
String sysValue = null;

when(EnvUtils.getSystemParametersValue(input, taskId))
.thenReturn(sysValue);

String expected = null;

String result =
parametersUtils.replaceVariables(input, documentContext, taskId);

assertEquals(expected, result);
}

@Test
public void testReplaceVariablesWithException() {
String input = "${sysParam1}";
Map<String, Object> inputMap = new HashMap<>();

Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = JsonPath.parse(inputMap, option);
String taskId = "taskId";
String sysValue = "sysValue1";

when(EnvUtils.getSystemParametersValue(input, taskId))
.thenThrow(new RuntimeException("Exception"));

String expected = null;

String result =
parametersUtils.replaceVariables(input, documentContext, taskId);

assertEquals(expected, result);
}

@Test
public void testReplaceVariablesWithEmptyDocumentContext() {
String input = "${sysParam1}";
Map<String, Object> inputMap = new HashMap<>();

Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), option);
String taskId = "taskId";
String sysValue = "sysValue1";

when(EnvUtils.getSystemParametersValue(input, taskId))
.thenReturn(sysValue);

String expected = "sysValue1";

String result =
parametersUtils.replaceVariables(input, documentContext, taskId);

assertEquals(expected, result);
}

@Test
public void testReplaceVariablesWithNullDocumentContext() {
String input = "${sysParam1}";
Map<String, Object> inputMap = new HashMap<>();

Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = null;
String taskId = "taskId";
String sysValue = "sysValue1";

when(EnvUtils.getSystemParametersValue(input, taskId))
.thenReturn(sysValue);

String expected = "sysValue1";

String result =
parametersUtils.replaceVariables(input, documentContext, taskId);

assertEquals(expected, result);
}

@Test
public void testReplaceVariablesWithNullInput() {
String input = "This is a ${param1} and this is another ${param2}";

Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), option);
String taskId = "taskId";
String expected = null;

String result =
parametersUtils.replaceVariables(input, documentContext, taskId);

assertEquals(expected, result);
}

@Test
public void testReplaceWithNullInput() {
String input = "This is a ${param1} and this is another ${param2}";
Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), option);
String expected = null;

String result = parametersUtils.replace(input, documentContext);

assertEquals(expected, result);
}

@Test
public void testReplaceWithNullDocumentContext() {
String input = "This is a ${param1} and this is another ${param2}";
Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = null;
String expected = null;

String result = parametersUtils.replace(input, documentContext);

assertEquals(expected, result);
}

@Test
public void testReplaceWithEmptyDocumentContext() {
String input = "This is a ${param1} and this is another ${param2}";
Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), option);
String expected = null;

String result = parametersUtils.replace(input, documentContext);

assertEquals(expected, result);
}

@Test
public void testGetWorkflowInput() {
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(new HashMap<>());

Map<String, Object> inputParams = new HashMap<>();
inputParams.put("inputParam1", "value1");
inputParams.put("inputParam2", "value2");

Map<String, Object> expected = new HashMap<>();
expected.put("inputParam1", "value1");
expected.put("inputParam2", "value2");

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(expected, result);
}

@Test
public void testGetWorkflowInputWithNullInputParams() {
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(new HashMap<>());

Map<String, Object> inputParams = null;

Map<String, Object> expected = new HashMap<>();

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(expected, result);
}

@Test
public void testGetWorkflowInputWithNullWorkflowDef() {
WorkflowDef workflowDef = null;

Map<String, Object> inputParams = new HashMap<>();
inputParams.put("inputParam1", "value1");
inputParams.put("inputParam2", "value2");

Map<String, Object> expected = new HashMap<>();
expected.put("inputParam1", "value1");
expected.put("inputParam2", "value2");

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(expected, result);
}

@Test
public void testGetWorkflowInputWithEmptyWorkflowDef() {
WorkflowDef workflowDef = new WorkflowDef();

Map<String, Object> inputParams = new HashMap<>();
inputParams.put("inputParam1", "value1");
inputParams.put("inputParam2", "value2");

Map<String, Object> expected = new HashMap<>();
expected.put("inputParam1", "value1");
expected.put("inputParam2", "value2");

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(expected, result);
}

@Test
public void testGetWorkflowInputWithNullInputParamsAndEmptyWorkflowDef() {
WorkflowDef workflowDef = new WorkflowDef();

Map<String, Object> inputParams = null;

Map<String, Object> expected = new HashMap<>();

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(expected, result);
}

@Test
public void testGetWorkflowInputWithNullWorkflowDefAndEmptyInputParams() {
WorkflowDef workflowDef = null;

Map<String, Object> inputParams = new HashMap<>();

Map<String, Object> expected = new HashMap<>();

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(expected, result);
}

@Test
public void testGetWorkflowInputWithNullWorkflowDefAndNullInputParams() {
WorkflowDef workflowDef = null;

Map<String, Object> inputParams = null;

Map<String, Object> expected = new HashMap<>();

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(expected, result);
}

@Test
public void testGetWorkflowInputWithEmptyWorkflowDefAndNullInputParams() {
WorkflowDef workflowDef = new WorkflowDef();

Map<String, Object> inputParams = null;

Map<String, Object> expected = new HashMap<>();

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(expected, result);
}

@Test
public void testGetWorkflowInputWithEmptyWorkflowDefAndEmptyInputParams() {
WorkflowDef workflowDef = new WorkflowDef();

Map<String, Object> inputParams = new HashMap<>();

Map<String, Object> expected = new HashMap<>();

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(expected, result);
}

@Test
public void testGetWorkflowInputWithNullWorkflowDefAndEmptyInputParams() {
WorkflowDef workflowDef = null;

Map<String, Object> inputParams = new HashMap<>();

Map<String, Object> expected = new HashMap<>();

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(expected, result);
}

@Test
public void testGetWorkflowInputWithNullWorkflowDefAndEmptyInputParams() {
WorkflowDef workflowDef = null;

Map<String, Object> inputParams = new HashMap<>();

Map<String, Object> expected = new HashMap<>();

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(expected, result);
}

@Test
public void testGetWorkflowInputWithNullWorkflowDefAndEmptyInputParams() {
WorkflowDef workflowDef = null;

Map<String, Object> inputParams = new HashMap<>();

Map<String, Object> expected = new HashMap<>();

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(expected, result);
}
}