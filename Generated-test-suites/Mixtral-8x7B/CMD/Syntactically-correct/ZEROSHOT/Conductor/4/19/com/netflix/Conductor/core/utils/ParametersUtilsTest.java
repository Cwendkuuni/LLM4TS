package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
objectMapper = mock(ObjectMapper.class);
parametersUtils = new ParametersUtils(objectMapper);
}

@Test
public void testGetTaskInputV2() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("inputParam1", "value1");
inputParams.put("inputParam2", "value2");

WorkflowModel workflow = new WorkflowModel();
workflow.setInput(Collections.singletonMap("workflowInput", "workflowValue"));
workflow.setTasks(Collections.singletonList(new TaskModel()));

TaskDef taskDefinition = new TaskDef();
taskDefinition.setInputTemplate(inputParams);

String taskId = "task1";

Map<String, Object> expectedResult = new HashMap<>();
expectedResult.put("inputParam1", "value1");
expectedResult.put("inputParam2", "value2");
expectedResult.put("workflow",
Map.of(
"input", "workflowValue",
"output", null,
"status", null,
"workflowId", null,
"parentWorkflowId", null,
"parentWorkflowTaskId", null,
"workflowType", null,
"version", null,
"correlationId", null,
"reasonForIncompletion", null,
"schemaVersion", 1,
"variables", null
)
);
expectedResult.put("task1",
Map.of(
"input", null,
"output", null,
"taskType", null,
"status", null,
"referenceTaskName", null,
"retryCount", null,
"correlationId", null,
"pollCount", null,
"taskDefName", null,
"scheduledTime", null,
"startTime", null,
"endTime", null,
"workflowInstanceId", null,
"taskId", null,
"reasonForIncompletion", null,
"callbackAfterSeconds", null,
"workerId", null,
"iteration", null
)
);

when(objectMapper.writeValueAsBytes(inputParams))
.thenReturn("{\"inputParam1\":\"value1\",\"inputParam2\":\"value2\"}".getBytes());
when(objectMapper.readValue(any(), eq(new TypeReference<Map<String, Object>>() {})))
.thenReturn(inputParams);

Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflow, taskId, taskDefinition);

assertEquals(expectedResult, result);
}

@Test
public void testReplace() {
String json = "{\"param1\": \"${param2}\", \"param3\": [\"${param4}\", \"${param5}\"]}";
DocumentContext documentContext = JsonPath.parse("{\"param2\": \"value2\", \"param4\": [\"value4\"]}");

String expectedResult = "{\"param1\": \"value2\", \"param3\": [\"value4\", null]}";

assertEquals(expectedResult, parametersUtils.replace(json, documentContext, null));
}

@Test
public void testReplaceVariables() {
String paramString = "${param1}.${param2}";
DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("param1", "value1"));

String expectedResult = "value1";

assertEquals(expectedResult, parametersUtils.replaceVariables(paramString, documentContext, null));
}

@Test
public void testGetWorkflowInput() {
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(Collections.singletonMap("workflowInput", "workflowValue"));

Map<String, Object> inputParams = new HashMap<>();
inputParams.put("inputParam1", "value1");

Map<String, Object> expectedResult = new HashMap<>();
expectedResult.put("workflowInput", "workflowValue");
expectedResult.put("inputParam1", "value1");

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(expectedResult, result);
}
}