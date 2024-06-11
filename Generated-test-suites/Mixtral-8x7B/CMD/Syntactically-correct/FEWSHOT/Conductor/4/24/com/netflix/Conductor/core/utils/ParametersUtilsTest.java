package com.netflix.conductor.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.stereotype.Component;

@SuppressWarnings("unchecked")
public class ParametersUtilsTest {

 @Before
 public void setUp() {
 objectMapper = mock(ObjectMapper.class);
 }

 private ParametersUtils createParametersUtils() {
 return new ParametersUtils(objectMapper);
 }

 private ObjectMapper objectMapper;

 @Test
 public void testGetTaskInputV2() {
 ParametersUtils paramsUtils = createParametersUtils();

 WorkflowModel workflow = mock(WorkflowModel.class);
 when(workflow.getInput()).thenReturn(Collections.singletonMap("inputKey", "inputValue"));
 when(workflow.getOutput()).thenReturn(Collections.singletonMap("outputKey", "outputValue"));
 when(workflow.getStatus()).thenReturn("status");
 when(workflow.getWorkflowId()).thenReturn("workflowId");
 when(workflow.getParentWorkflowId()).thenReturn("parentWorkflowId");
 when(workflow.getParentWorkflowTaskId()).thenReturn("parentWorkflowTaskId");
 when(workflow.getWorkflowName()).thenReturn("workflowName");
 when(workflow.getWorkflowVersion()).thenReturn(1);
 when(workflow.getCorrelationId()).thenReturn("correlationId");
 when(workflow.getReasonForIncompletion()).thenReturn("reasonForIncompletion");
 when(workflow.getWorkflowDefinition().getSchemaVersion()).thenReturn(2);
 when(workflow.getVariables()).thenReturn(Collections.singletonMap("variableKey", "variableValue"));

 TaskModel task = mock(TaskModel.class);
 when(task.getInputData()).thenReturn(Collections.singletonMap("taskInputKey", "taskInputValue"));
 when(task.getOutputData()).thenReturn(Collections.singletonMap("taskOutputKey", "taskOutputValue"));
 when(task.getTaskType()).thenReturn("taskType");
 when(task.getStatus()).thenReturn("status");
 when(task.getReferenceTaskName()).thenReturn("referenceTaskName");
 when(task.getRetryCount()).thenReturn(1);
 when(task.getCorrelationId()).thenReturn("correlationId");
 when(task.getPollCount()).thenReturn(1);
 when(task.getTaskDefName()).thenReturn("taskDefName");
 when(task.getScheduledTime()).thenReturn("scheduledTime");
 when(task.getStartTime()).thenReturn("startTime");
 when(task.getEndTime()).thenReturn("endTime");
 when(task.getWorkflowInstanceId()).thenReturn("workflowInstanceId");
 when(task.getTaskId()).thenReturn("taskId");
 when(task.getReasonForIncompletion()).thenReturn("reasonForIncompletion");
 when(task.getCallbackAfterSeconds()).thenReturn(1);
 when(task.getWorkerId()).thenReturn("workerId");
 when(task.getIteration()).thenReturn(1);
 when(workflow.getTaskByRefName(anyString())).thenReturn(task);

 TaskDef taskDefinition = mock(TaskDef.class);
 when(taskDefinition.getInputTemplate())
 .thenReturn(Collections.singletonMap("inputTemplateKey", "inputTemplateValue"));

 Map<String, Object> inputParams =
 new HashMap<String, Object>() {
 {
 put("paramKey", "${workflow.input.inputKey}");
 put("paramKeyWithTaskRef", "${task.input.taskInputKey}");
 put("paramKeyWithTaskRefAndTemplate", "${task.input.taskInputKeyAndTemplate}");
 put("paramKeyWithTaskStatus", "${task.status}");
 put("paramKeyWithTaskCorrelationId", "${task.correlationId}");
 put("paramKeyWithTaskRetryCount", "${task.retryCount}");
 put("paramKeyWithTaskPollCount", "${task.pollCount}");
 put("paramKeyWithTaskTaskDefName", "${task.taskDefName}");
 put("paramKeyWithTaskScheduledTime", "${task.scheduledTime}");
 put("paramKeyWithTaskStartTime", "${task.startTime}");
 put("paramKeyWithTaskEndTime", "${task.endTime}");
 put("paramKeyWithTaskWorkflowInstanceId", "${task.workflowInstanceId}");
 put("paramKeyWithTaskTaskId", "${task.taskId}");
 put("paramKeyWithTaskReason", "${task.reasonForIncompletion}");
 put("paramKeyWithTaskCallback", "${task.callbackAfterSeconds}");
 put("paramKeyWithTaskWorkerId", "${task.workerId}");
 put("paramKeyWithTaskIteration", "${task.iteration}");
 put("paramKeyWithTaskRefName", "${task.referenceTaskName}");
 put("paramKeyWithWorkflowStatus", "${workflow.status}");
 put("paramKeyWithWorkflowCorrelationId", "${workflow.correlationId}");
 put("paramKeyWithWorkflowVersion", "${workflow.version}");
 put("paramKeyWithWorkflowVariables", "${workflow.variables}");
 put("paramKeyWithWorkflowInput", "${workflow.input}");
 put("paramKeyWithWorkflowOutput", "${workflow.output}");
 put("paramKeyWithTaskRefNameAndTemplate", "${task.referenceTaskNameAndTemplate}");
 put("paramKeyWithTaskStatusAndTemplate", "${task.statusAndTemplate}");
 put("paramKeyWithNullEnvVar", "${nullEnvVar}");
 put("paramKeyWithNonExistentEnvVar", "${nonExistentEnvVar}");
 put("paramKeyWithEmptyEnvVar", "${emptyEnvVar}");
 put("paramKeyWithTaskInputTemplate", "${task.inputTemplate.inputTemplateKey}");
 }
 };

 Map<String, Object> result =
 paramsUtils.getTaskInputV2(
 inputParams, workflow, taskDefinition, "taskId");

 assertEquals(
 "inputValue",
 result.get("paramKey"));
 assertEquals(
 "taskInputValue",
 result.get("paramKeyWithTaskRef"));
 assertEquals(
 "inputTemplateValue",
 result.get("paramKeyWithTaskRefAndTemplate"));
 assertEquals(
 "status",
 result.get("paramKeyWithTaskStatus"));
 assertEquals(
 "correlationId",
 result.get("paramKeyWithTaskCorrelationId"));
 assertEquals(
 "1",
 result.get("paramKeyWithTaskRetryCount"));
 assertEquals(
 "1",
 result.get("paramKeyWithTaskPollCount"));
 assertEquals(
 "taskDefName",
 result.get("paramKeyWithTaskTaskDefName"));
 assertEquals(
 "scheduledTime",
 result.get("paramKeyWithTaskScheduledTime"));
 assertEquals(
 "startTime",
 result.get("paramKeyWithTaskStartTime"));
 assertEquals(
 "endTime",
 result.get("paramKeyWithTaskEndTime"));
 assertEquals(
 "workflowInstanceId",
 result.get("paramKeyWithTaskWorkflowInstanceId"));
 assertEquals(
 "taskId",
 result.get("paramKeyWithTaskTaskId"));
 assertEquals(
 "reasonForIncompletion",
 result.get("paramKeyWithTaskReason"));
 assertEquals(
 "1",
 result.get("paramKeyWithTaskCallback"));
 assertEquals(
 "workerId",
 result.get("paramKeyWithTaskWorkerId"));
 assertEquals(
 "1",
 result.get("paramKeyWithTaskIteration"));
 assertEquals(
 "referenceTaskName",
 result.get("paramKeyWithTaskRefName"));
 assertEquals(
 "status",
 result.get("paramKeyWithWorkflowStatus"));
 assertEquals(
 "correlationId",
 result.get("paramKeyWithWorkflowCorrelationId"));
 assertEquals(
 "1",
 result.get("paramKeyWithWorkflowVersion"));
 assertEquals(
 "{\"variableKey\":\"variableValue\"}",
 result.get("paramKeyWithWorkflowVariables"));
 assertEquals(
 "{\"inputKey\":\"inputValue\"}",
 result.get("paramKeyWithWorkflowInput"));
 assertEquals(
 "{\"outputKey\":\"outputValue\"}",
 result.get("paramKeyWithWorkflowOutput"));
 assertEquals(null, result.get("paramKeyWithTaskRefNameAndTemplate"));
 assertEquals(null, result.get("paramKeyWithTaskStatusAndTemplate"));
 assertEquals(null, result.get("paramKeyWithNullEnvVar"));
 assertEquals(null, result.get("paramKeyWithNonExistentEnvVar"));

 Map<String, Object> inputTemplate =
 new HashMap<String, Object>() {
 {
 put("inputTemplateKey", "inputTemplateValue");
 }
 };
 when(objectMapper.convertValue(any(), ArgumentMatchers.<TypeReference<Map<String, Object>>>any()))
 .thenReturn(inputTemplate);
 }

 @Test
 public void testGetTaskInputV1() {
 ParametersUtils paramsUtils = createParametersUtils();

 WorkflowModel workflow = mock(WorkflowModel.class);
 when(workflow.getInput()).thenReturn(Collections.singletonMap("inputKey", "inputValue"));

 TaskModel task = mock(TaskModel.class);
 when(task.getInputData()).thenReturn(Collections.singletonMap("taskInputKey", "taskInputValue"));

 TaskDef taskDefinition = mock(TaskDef.class);

 Map<String, Object> inputParams =
 new HashMap<String, Object>() {
 {
 put("paramKey", "${workflow.input.inputKey}");
 put("paramKeyWithTaskRef", "${task.input.taskInputKey}");
 }
 };

 Map<String, Object> result =
 paramsUtils.getTaskInputV1(
 workflow, inputParams);

 assertEquals(
 "inputValue",
 result.get("paramKey"));
 assertEquals(
 "taskInputValue",
 result.get("paramKeyWithTaskRef"));
 }

 @Test
 public void testReplace() {
 ParametersUtils paramsUtils = createParametersUtils();

 Configuration option =
 Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
 DocumentContext documentContext = JsonPath.parse("{\"key\": \"value\"}", option);

 Map<String, Object> input = new HashMap<>();
 input.put("paramKey", "${key}");

 Map<String, Object> result = paramsUtils.replace(input, documentContext, null);
 assertEquals("value", result.get("paramKey"));
 }

 @Test
 public void testReplaceVariables() {
 ParametersUtils paramsUtils = createParametersUtils();

 Configuration option =
 Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
 DocumentContext documentContext =
 JsonPath.parse("{\"key\": {\"nestedKey\": \"nestedValue\"}}", option);

 Object replacement = paramsUtils.replaceVariables("${key.nestedKey}", documentContext, null);
 assertEquals("nestedValue", replacement);

 replacement = paramsUtils.replaceVariables("$${key.nestedKey}", documentContext, null);
 assertEquals("${key.nestedKey}", replacement);

 replacement = paramsUtils.replaceVariables("${nonExistentKey}", documentContext, null);
 assertNull(replacement);
 }

 @Test
 public void testReplaceVariablesWithEnv() {
 ParametersUtils paramsUtils = createParametersUtils();

 Configuration option =
 Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
 DocumentContext documentContext = JsonPath.parse("{}", option);
 when(EnvUtils.isEnvironmentVariable(anyString())).thenReturn(true);
 when(EnvUtils.getSystemParametersValue(anyString(), anyString()))
 .thenReturn("envValue");

 Object replacement = paramsUtils.replaceVariables("${envVar}", documentContext, null);
 assertEquals("envValue", replacement);

 replacement = paramsUtils.replaceVariables("$${envVar}", documentContext, null);
 assertEquals("${envVar}", replacement);

 replacement = paramsUtils.replaceVariables("${nonExistentEnvVar}", documentContext, null);
 assertNull(replacement);
 }

 @Test
 public void testReplaceVariablesWithNullEnv() {
 ParametersUtils paramsUtils = createParametersUtils();

 Configuration option =
 Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
 DocumentContext documentContext = JsonPath.parse("{}", option);
 when(EnvUtils.isEnvironmentVariable(anyString())).thenReturn(false);

 Object replacement = paramsUtils.replaceVariables("${envVar}", documentContext, null);
 assertNull(replacement);
 }

 @Test
 public void testReplaceList() {
 ParametersUtils paramsUtils = createParametersUtils();

 List<Object> list = new LinkedList<>();
 list.add("${key}");
 list.add(
 "{\"key\": {\"nestedKey\": \"${nonExistentKey}\"}}");

 Object replacementList = paramsUtils.replaceList(list, null, null);
 List<Object> expectedList = new LinkedList<>();
 expectedList.add("value");
 expectedList.add("{\"key\": {\"nestedKey\": \"${nonExistentKey}\"}}");

 assertEquals(expectedList, replacementList);
 }
}