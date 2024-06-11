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
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
objectMapper = mock(ObjectMapper.class);
parametersUtils = new ParametersUtils(objectMapper);
}

@Test
public void testGetTaskInput() {
// Add test cases for this method
}

@Test
public void testGetTaskInputV2() {
// Add test cases for this method
}

@Test
public void testClone() throws IOException {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "value");
String json = objectMapper.writeValueAsString(inputParams);
when(objectMapper.readValue(any(byte[].class), any(TypeReference.class)))
.thenReturn(new HashMap<>());
parametersUtils.clone(inputParams);
}

@Test
public void testReplace() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "${param}");
DocumentContext documentContext = Configuration.defaultConfiguration()
.addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{}");
parametersUtils.replace(inputParams, documentContext, "taskId");
assertEquals(inputParams.get("key"), "taskId");
}

@Test
public void testReplaceList() {
List<String> values = new LinkedList<>();
values.add("${param}");
values.add("value");
DocumentContext documentContext = Configuration.defaultConfiguration()
.addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{}");
parametersUtils.replaceList(values, "taskId", documentContext);
assertEquals(values.get(0), "taskId");
}

@Test
public void testReplaceVariables() {
String paramString = "param${param}";
DocumentContext documentContext = Configuration.defaultConfiguration()
.addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{\"param\": \"value\"}");
assertEquals(parametersUtils.replaceVariables(paramString, documentContext, "taskId"), "valuevalue");
}

@Test
public void testGetWorkflowInput() {
// Add test cases for this method
}
}