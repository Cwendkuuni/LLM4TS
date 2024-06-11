package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
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
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;
private EnvUtils envUtils;
private TaskUtils taskUtils;

@Before
public void setUp() {
 objectMapper = mock(ObjectMapper.class);
 envUtils = mock(EnvUtils.class);
 taskUtils = mock(TaskUtils.class);
 parametersUtils = new ParametersUtils(objectMapper, envUtils, taskUtils);
}

@Test
public void testGetTaskInputV2() {
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("key1", "value1");
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowId("workflowId");
 workflow.setWorkflowName("workflowName");
 workflow.setWorkflowVersion("workflowVersion");

 TaskDef taskDefinition = new TaskDef();
 taskDefinition.setInputTemplate(inputParams);

 Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, "taskId", taskDefinition);

 assertEquals(inputParams, result);
}

@Test
public void testGetTaskInputV2WithTaskTasks() {
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("key1", "value1");
 WorkflowModel workflow = new WorkflowModel();
 workflow.setWorkflowId("workflowId");
 workflow.setWorkflowName("workflowName");
 workflow.setWorkflowVersion("workflowVersion");
 workflow.setTasks(Collections.singletonList(getTaskModel()));

 TaskDef taskDefinition = new TaskDef();
 taskDefinition.setInputTemplate(inputParams);

 Map<String, Object> result = parametersUtils.getTaskInputV2(inputParams, workflow, "taskId", taskDefinition);

 assertEquals(inputParams, result);
}

@Test
public void testReplace() {
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("key1", "${key2}");
 inputParams.put("key2", "value2");

 DocumentContext documentContext = JsonPath.parse("{\"key2\": \"replacementValue\"}");

 Map<String, Object> result = parametersUtils.replace(inputParams, documentContext, null);

 assertEquals("replacementValue", result.get("key1"));
}

@Test
public void testReplaceWithEnvUtils() {
 when(envUtils.isEnvironmentVariable(anyString()))
 .thenReturn(true)
 .thenReturn(false);
 when(envUtils.getSystemParametersValue(anyString(), anyString()))
 .thenReturn("envValue")
 .thenReturn(null);

 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("key1", "${key2}");
 inputParams.put("key2", "${env:key3}");

 DocumentContext documentContext = JsonPath.parse("{}");

 Map<String, Object> result = parametersUtils.replace(inputParams, documentContext, null);

 assertEquals("envValue", result.get("key1"));
}

@Test
public void testReplaceVariables() {
 when(envUtils.isEnvironmentVariable(anyString()))
 .thenReturn(true)
 .thenReturn(false);
 when(envUtils.getSystemParametersValue(anyString(), anyString()))
 .thenReturn("envValue")
 .thenReturn(null);

 String paramString = "${key2}";

 DocumentContext documentContext = JsonPath.parse("{\"key2\": \"replacementValue\"}");

 Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

 assertEquals("replacementValue", result);
}

@Test
public void testReplaceVariablesWithEnvUtils() {
 when(envUtils.isEnvironmentVariable(anyString()))
 .thenReturn(true)
 .thenReturn(false);
 when(envUtils.getSystemParametersValue(anyString(), anyString()))
 .thenReturn("envValue")
 .thenReturn(null);

 String paramString = "${env:key3}";

 DocumentContext documentContext = JsonPath.parse("{}");

 Object result = parametersUtils.replaceVariables(paramString, documentContext, null);

 assertEquals("envValue", result);
}

@Test
public void testReplaceVariablesWithPattern() {
 Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
 String paramString = "key1 ${key2} ${key3}";

 DocumentContext documentContext = JsonPath.parse("{\"key2\": \"replacementValue1\", \"key3\": \"replacementValue2\"}");

 Object result = parametersUtils.replaceVariables(paramString, pattern, documentContext, null);

 assertEquals("key1 replacementValue1 replacementValue2", result);
}

@Test
public void testReplaceVariablesWithPatternAndEnvUtils() {
 when(envUtils.isEnvironmentVariable(anyString()))
 .thenReturn(true)
 .thenReturn(false);
 when(envUtils.getSystemParametersValue(anyString(), anyString()))
 .thenReturn("envValue")
 .thenReturn(null);

 Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
 String paramString = "key1 ${key2} ${env:key3}";

 DocumentContext documentContext = JsonPath.parse("{\"key2\": \"replacementValue1\"}");

 Object result = parametersUtils.replaceVariables(paramString, pattern, documentContext, null);

 assertEquals("key1 replacementValue1 envValue", result);
}

private TaskModel getTaskModel() {
 TaskModel taskModel = new TaskModel();
 taskModel.setInputData(Collections.singletonMap("inputKey", "inputValue"));
 taskModel.setOutputData(Collections.singletonMap("outputKey", "outputValue"));

 return taskModel;
}
}