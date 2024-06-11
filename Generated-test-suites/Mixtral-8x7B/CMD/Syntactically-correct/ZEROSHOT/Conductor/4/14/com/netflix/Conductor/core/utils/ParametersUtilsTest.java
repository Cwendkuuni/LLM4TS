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
// Add your test code here
}

@Test
public void testGetTaskInputV2() {
// Add your test code here
}

@Test
public void testClone() {
// Add your test code here
}

@Test
public void testReplace() {
// Add your test code here
}

@Test
public void testReplaceVariables() {
// Add your test code here
}

@Test
public void testGetWorkflowInput() {
// Add your test code here
}
}