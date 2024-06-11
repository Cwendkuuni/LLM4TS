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

import static org.junit.Assert.assertEquals;
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
public void testGetTaskInput() throws IOException {
// given
Map<String, Object> inputParams = new HashMap<>();
WorkflowModel workflowModel = new WorkflowModel();
TaskDef taskDef = new TaskDef();
String taskId = "task1";

when(objectMapper.writeValueAsBytes(inputParams)).thenReturn("inputParamsBytes".getBytes());
when(objectMapper.readValue("inputParamsBytes".getBytes(), new TypeReference<Map<String, Object>>() {
})).thenReturn(inputParams);

// when
Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);

// then
assertEquals(inputParams, result);
}

@Test
public void testGetTaskInputV2() throws IOException {
// given
Map<String, Object> inputParams = new HashMap<>();
WorkflowModel workflowModel = new WorkflowModel();
TaskDef taskDef = new TaskDef();
String taskId = "task1";

when(objectMapper.writeValueAsBytes(inputParams)).thenReturn("inputParamsBytes".getBytes());
when(objectMapper.readValue("inputParamsBytes".getBytes(), new TypeReference<Map<String, Object>>() {
})).thenReturn(inputParams);

// when
Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflowModel, taskId, taskDef);

// then
assertEquals(inputParams, result);
}

@Test
public void testGetTaskInputV1() throws IOException {
// given
WorkflowModel workflowModel = new WorkflowModel();
Map<String, Object> inputParams = new HashMap<>();

when(objectMapper.writeValueAsBytes(inputParams)).thenReturn("inputParamsBytes".getBytes());
when(objectMapper.readValue("inputParamsBytes".getBytes(), new TypeReference<Map<String, Object>>() {
})).thenReturn(inputParams);

// when
Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);

// then
assertEquals(inputParams, result);
}

@Test
public void testReplace() throws IOException {
// given
Map<String, Object> inputParams = new HashMap<>();
Object json = "{\"key\": \"value\"}";

when(objectMapper.writeValueAsBytes(inputParams)).thenReturn("inputParamsBytes".getBytes());
when(objectMapper.readValue("inputParamsBytes".getBytes(), new TypeReference<Map<String, Object>>() {
})).thenReturn(inputParams);

// when
Map<String, Object> result = parametersUtils.replace(inputParams, json);

// then
assertEquals(inputParams, result);
}

@Test
public void testReplaceVariables() {
// given
String paramString = "${key}";
DocumentContext documentContext = Configuration.defaultConfiguration()
.addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{\"key\": \"value\"}");

// when
Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

// then
assertEquals("value", result);
}

@Test
public void testReplaceVariablesWithEnvVar() {
// given
String paramString = "${ENV_VAR}";
DocumentContext documentContext = Configuration.defaultConfiguration()
.addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{}");

// when
Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId");

// then
assertEquals("envValue", result);
}

@Test
public void testReplaceVariablesWithNoMatch() {
// given
String paramString = "notAMatch";
DocumentContext documentContext = Configuration.defaultConfiguration()
.addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{}");

// when
Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId");

// then
assertEquals("notAMatch", result);
}

@Test
public void testReplaceVariablesDepth0() {
// given
String paramString = "${key}";
DocumentContext documentContext = Configuration.defaultConfiguration()
.addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{\"key\": \"value\"}");

// when
Object result = parametersUtils.replaceVariables(paramString, documentContext, null, 0);

// then
assertEquals("value", result);
}

@Test
public void testReplaceVariablesDepth1() {
// given
String paramString = "${key2}";
DocumentContext documentContext = Configuration.defaultConfiguration()
.addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{\"key\": {\"key2\": \"value2\"}}");

// when
Object result = parametersUtils.replaceVariables(paramString, documentContext, null, 1);

// then
assertEquals("value2", result);
}

@Test
public void testReplaceVariablesDepth2() {
// given
String paramString = "${key3}";
DocumentContext documentContext = Configuration.defaultConfiguration()
.addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{\"key\": {\"key2\": {\"key3\": \"value3\"}}}");

// when
Object result = parametersUtils.replaceVariables(paramString, documentContext, null, 2);

// then
assertEquals("value3", result);
}

@Test
public void testReplaceVariablesWithEnvVarDepth0() {
// given
String paramString = "${ENV_VAR}";
DocumentContext documentContext = Configuration.defaultConfiguration()
.addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{}");

// when
Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId", 0);

// then
assertEquals("envValue", result);
}

@Test
public void testReplaceVariablesWithNoMatchDepth0() {
// given
String paramString = "notAMatch";
DocumentContext documentContext = Configuration.defaultConfiguration()
.addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{}");

// when
Object result = parametersUtils.replaceVariables(paramString, documentContext, "taskId", 0);

// then
assertEquals("notAMatch", result);
}
}