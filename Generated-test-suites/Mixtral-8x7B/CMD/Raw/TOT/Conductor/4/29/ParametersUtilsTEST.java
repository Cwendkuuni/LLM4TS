import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

 private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
 private ParametersUtils parametersUtils;
 private ObjectMapper objectMapper;
 private EnvUtils envUtils;

 @Before
 public void init() {
 objectMapper = mock(ObjectMapper.class);
 envUtils = mock(EnvUtils.class);
 parametersUtils = new ParametersUtils(objectMapper) {
 @Override
 EnvUtils getEnvUtils() {
 return envUtils;
 }
 };
 }

 @Test
 public void testGetTaskInputV1() {
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setInput(new HashMap<>());
 workflowModel.getInput().put("testInput", "testValue");
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("workflow.input.testInput", "");

 Map<String, Object> result = parametersUtils.getTaskInputV1(workflowModel, inputParams);
 Assert.assertEquals("testValue", result.get(""));
 }

 @Test
 public void testGetTaskInputV2() throws IOException {
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setSchemaVersion(2);
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setInput(new HashMap<>());
 workflowModel.getInput().put("testInput", "testValue");
 workflowModel.setOutput(new HashMap<>());
 workflowModel.getOutput().put("testOutput", "testValue");
 workflowModel.setTasks(new LinkedList<>());
 TaskDef taskDef = new TaskDef();
 taskDef.setTaskReferenceName("testTask");
 taskDef.setInputTemplate(new HashMap<>());
 taskDef.getInputTemplate().put("testParam", "testValue");
 workflowModel.getTasks().add(taskDef);
 Map<String, Object> inputParams = new HashMap<>();
 inputParams.put("workflow.input.testInput", "");
 inputParams.put("workflow.output.testOutput", "");
 inputParams.put("workflow.schemaVersion", "");
 inputParams.put("workflow.variables", "");
 inputParams.put("workflow.status", "");
 inputParams.put("workflow.workflowId", "");
 inputParams.put("workflow.parentWorkflowId", "");
 inputParams.put("workflow.parentWorkflowTaskId", "");
 inputParams.put("workflow.workflowType", "");
 inputParams.put("workflow.version", "");
 inputParams.put("workflow.correlationId", "");
 inputParams.put("workflow.reasonForIncompletion", "");
 inputParams.put("task.taskType", "");
 inputParams.put("task.input", "");
 inputParams.put("task.output", "");
 inputParams.put("task.status", "");
 inputParams.put("task.referenceTaskName", "");
 inputParams.put("task.retryCount", "");
 inputParams.put("task.correlationId", "");
 inputParams.put("task.pollCount", "");
 inputParams.put("task.taskDefName", "");
 inputParams.put("task.scheduledTime", "");
 inputParams.put("task.startTime", "");
 inputParams.put("task.endTime", "");
 inputParams.put("task.workflowInstanceId", "");
 inputParams.put("task.taskId", "");
 inputParams.put("task.reasonForIncompletion", "");
 inputParams.put("task.callbackAfterSeconds", "");
 inputParams.put("task.workerId", "");
 inputParams.put("task.iteration", "");

 when(objectMapper.writeValueAsBytes(any())).thenReturn(new byte[0]);
 when(objectMapper.readValue(any(byte[].class), any(TypeReference.class))).thenReturn(new HashMap<>());
 when(envUtils.isEnvironmentVariable(anyString())).thenReturn(false);
 when(envUtils.getSystemParametersValue(anyString(), anyString())).thenReturn("testEnvValue");

 Map<String, Object> result = parametersUtils.getTaskInputV2(
 inputParams,
 workflowModel,
 taskDef,
 "testTask"
 );

 Assert.assertEquals("testValue", result.get("testParam"));
 Assert.assertEquals("testEnvValue", result.get("envValue"));
 Assert.assertEquals("testValue", result.get("input"));
 Assert.assertEquals("testValue", result.get("output"));
 Assert.assertEquals("COMPLETED", result.get("status"));
 Assert.assertEquals("testTask", result.get("referenceTaskName"));
 Assert.assertEquals(0, result.get("retryCount"));
 Assert.assertEquals("testTask", result.get("correlationId"));
 Assert.assertEquals(0, result.get("pollCount"));
 Assert.assertEquals("testTask", result.get("taskDefName"));
 Assert.assertEquals(0, result.get("scheduledTime").getClass().getMethod("getTime").invoke(result.get("scheduledTime")));
 Assert.assertEquals(0, result.get("startTime").getClass().getMethod("getTime").invoke(result.get("startTime")));
 Assert.assertEquals(0, result.get("endTime").getClass().getMethod("getTime").invoke(result.get("endTime")));
 Assert.assertEquals("testTask", result.get("workflowInstanceId"));
 Assert.assertEquals("testTask", result.get("taskId"));
 Assert.assertEquals("testValue", result.get("reasonForIncompletion"));
 Assert.assertEquals(0, result.get("callbackAfterSeconds"));
 Assert.assertEquals("testTask", result.get("workerId"));
 Assert.assertEquals(0, result.get("iteration"));
 }

 @Test
 public void testReplace() {
 String input = "This is a ${test} and ${test2}.";
 Map<String, Object> inputMap = new HashMap<>();
 inputMap.put("test", "value1");
 inputMap.put("test2", "value2");
 DocumentContext documentContext = JsonPath.parse(inputMap);
 String result = parametersUtils.replace(input, documentContext, null).toString();
 Assert.assertEquals("This is a value1 and value2.", result);
 }

 @Test
 public void testReplaceVariables() {
 String input = "This is a ${test} and ${test2}.";
 DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());
 String result = parametersUtils.replaceVariables(input, documentContext, null).toString();
 Assert.assertEquals("This is a ${test} and ${test2}.", result);
 }

 @Test
 public void testReplaceVariablesWithEnvVar() {
 String input = "This is a ${test} and ${test2}.";
 DocumentContext documentContext = JsonPath.parse(Collections.emptyMap());
 when(envUtils.isEnvironmentVariable(anyString())).thenReturn(true);
 when(envUtils.getSystemParametersValue(anyString(), anyString())).thenReturn("testEnvValue");
 String result = parametersUtils.replaceVariables(input, documentContext, null).toString();
 Assert.assertEquals("This is a ${test} and testEnvValue.", result);
 }

 @Test
 public void testReplaceVariablesWithPattern() {
 String input = "${test.test2}";
 DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("test.test2", "value"));
 String result = parametersUtils.replaceVariables(input, documentContext, null).toString();
 Assert.assertEquals("value", result);
 }

 @Test
 public void testReplaceVariablesWithPatternAndEnvVar() {
 String input = "${test.test2}";
 DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("test.test2", "${env}"));
 when(envUtils.isEnvironmentVariable(anyString())).thenReturn(true);
 when(envUtils.getSystemParametersValue(anyString(), anyString())).thenReturn("testEnvValue");
 String result = parametersUtils.replaceVariables(input, documentContext, null).toString();
 Assert.assertEquals("testEnvValue", result);
 }

 @Test
 public void testReplaceVariablesWithPatternAndList() {
 String input = "${test[1]}";
 DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("test", new String[]{"value1", "value2"}));
 String result = parametersUtils.replaceVariables(input, documentContext, null).toString();
 Assert.assertEquals("value2", result);
 }

 @Test
 public void testReplaceVariablesWithPatternListAndEnvVar() {
 String input = "${test[1]}";
 DocumentContext documentContext = JsonPath.parse(Collections.singletonMap("test", new String[]{"${env}", "${env2}"}));
 when(envUtils.isEnvironmentVariable(anyString())).thenReturn(true);
 when(envUtils.getSystemParametersValue(anyString(), anyString())).thenReturn("testEnvValue", "testEnvValue2");
 String result = parametersUtils.replaceVariables(input, documentContext, null).toString();
 Assert.assertEquals("testEnvValue2", result);
 }
}