import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParametersUtils;
import com.netflix.conductor.common.utils.TestUtils;
import com.netflix.conductor.model.TaskModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
 private EnvUtils envUtils;

 @Before
 public void setUp() {
 objectMapper = new ObjectMapper();
 envUtils = mock(EnvUtils.class);
 parametersUtils = new ParametersUtils(objectMapper, envUtils) {
 @Override
 TypeReference<Map<String, Object>> getMapTypeReference() {
 return new TypeReference<Map<String, Object>>() {};
 }
 };
 }

 @Test
 public void testGetTaskInputV2() throws Exception {
 WorkflowModel workflowModel = TestUtils.createWorkflowModel();
 TaskDef taskDef = new TaskDef();
 taskDef.setInputTemplate(TestUtils.createInputTemplate());
 Map<String, Object> inputParams = TestUtils.createInputParams();

 when(envUtils.getSystemParametersValue(Mockito.anyString(), Mockito.anyString()))
 .thenReturn("env_value");

 Map<String, Object> result =
 parametersUtils.getTaskInputV2(inputParams, workflowModel, taskDef, "task_id");

 // Verify the result contains values from inputParams, taskDef.inputTemplate, and workflowModel
 assertEquals(10, result.size());
 }

 @Test
 public void testReplaceList() throws Exception {
 List<Object> originalList = new LinkedList<>();
 originalList.add("item1");
 originalList.add("${item2}");
 originalList.add(new HashMap<String, String>() {{
 put("key1", "value1");
 put("key2", "${key3}");
 }});

 String taskId = "task_id";
 DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
 .parse(TestUtils.createJsonPathDocument());

 List<Object> replacedList =
 parametersUtils.replaceList(originalList, taskId, documentContext);

 // Verify the replaced values
 assertEquals(3, replacedList.size());
 assertEquals("replaced_item2", ((String) replacedList.get(1)));
 assertEquals("value1", ((Map<String, String>) replacedList.get(2)).get("key1"));
 assertEquals("replaced_key3", ((Map<String, String>) replacedList.get(2)).get("key2"));
 }

 @Test
 public void testReplaceVariables() throws Exception {
 String paramString = "workflow.input.param1, task_ref.output.param2, ${env_var}, ${sys_param}";
 String taskId = "task_id";
 DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
 .parse(TestUtils.createJsonPathDocument());

 Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

 // Verify the replaced values
 assertEquals("workflow_value1, task_output_value2, env_value, sys_value");
 }

 @Test
 public void testReplaceVariablesWithException() throws Exception {
 String paramString = "workflow.input.param1, task_ref.output.param2, ${invalid_var}, ${sys_param}";
 String taskId = "task_id";
 DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
 .parse(TestUtils.createJsonPathDocument());

 Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

 // Verify the replaced values
 assertEquals("workflow_value1, task_output_value2, , sys_value");
 }

 @Test
 public void testReplaceWhenEnvUtilsReturnsNull() throws Exception {
 String paramString = "${env_var}";
 String taskId = "task_id";
 DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
 .parse(TestUtils.createJsonPathDocument());

 when(envUtils.getSystemParametersValue(Mockito.anyString(), Mockito.anyString()))
 .thenReturn(null);

 Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

 // Verify the replaced values
 assertEquals("", result);
 }

 @Test
 public void testReplaceVariablesWithEmptyString() throws Exception {
 String paramString = "${  }";
 String taskId = "task_id";
 DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
 .parse(TestUtils.createJsonPathDocument());

 Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

 // Verify the replaced values
 assertEquals("", result);
 }

 @Test
 public void testReplaceVariablesNested() throws Exception {
 String paramString = "${workflow.input.param1}, task_ref.output.param2, ${env_var}, ${sys_param}";
 String taskId = "task_id";
 DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
 .parse(TestUtils.createJsonPathDocument());

 Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

 // Verify the replaced values
 assertEquals("workflow_value1, task_output_value2, env_value, sys_value");
 }

 @Test
 public void testReplaceVariablesDeeplyNested() throws Exception {
 String paramString = "${workflow.input.param1.param4}, task_ref.output.param2, ${env_var}, ${sys_param}";
 String taskId = "task_id";
 DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
 .parse(TestUtils.createJsonPathDocument());

 Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

 // Verify the replaced values
 assertEquals("workflow_value1.param4, task_output_value2, env_value, sys_value");
 }

 @Test
 public void testReplaceVariablesWithNullReplacement() throws Exception {
 String paramString = "workflow.input.param1, task_ref.output.param2, ${invalid_var}, ${sys_param}";
 String taskId = "task_id";
 DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
 .parse(TestUtils.createJsonPathDocument());

 Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

 // Verify the replaced values
 assertEquals("workflow_value1, task_output_value2, , sys_value");
 }

 @Test
 public void testReplaceVariablesWithEmptyReplacement() throws Exception {
 String paramString = "workflow.input.param1, task_ref.output.param2, ${  }, ${sys_param}";
 String taskId = "task_id";
 DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
 .parse(TestUtils.createJsonPathDocument());

 Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

 // Verify the replaced values
 assertEquals("workflow_value1, task_output_value2, , sys_value");
 }

 @Test
 public void testReplaceVariablesWithBlankPath() throws Exception {
 String paramString = "workflow.input.param1, task_ref.output.param2, ${ }, ${sys_param}";
 String taskId = "task_id";
 DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
 .parse(TestUtils.createJsonPathDocument());

 Object result = parametersUtils.replaceVariables(paramString, documentContext, taskId);

 // Verify the replaced values
 assertEquals("workflow_value1, task_output_value2, , sys_value");
 }

 @Test
 public void testGetWorkflowInput() throws Exception {
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setInputTemplate(TestUtils.createInputTemplate());
 Map<String, Object> inputParams = TestUtils.createInputParams();

 Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);

 // Verify the result contains values from inputParams and workflowDef.inputTemplate
 assertEquals(5, result.size());
 }

 @Test
 public void testReplace() throws Exception {
 String paramString = "workflow.input.param1, task_ref.output.param2, ${env_var}, ${sys_param}";
 String taskId = "task_id";
 DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
 .parse(TestUtils.createJsonPathDocument());

 Object result = parametersUtils.replace(paramString, documentContext, taskId);

 // Verify the replaced values
 assertEquals("workflow_value1, task_output_value2, env_value, sys_value");
 }

 @Test
 public void testReplaceWithNullDocContext() throws Exception {
 String paramString = "workflow.input.param1, task_ref.output.param2, ${env_var}, ${sys_param}";
 String taskId = "task_id";
 DocumentContext documentContext = null;

 Object result = parametersUtils.replace(paramString, documentContext, taskId);

 // Verify the replaced values
 assertEquals(paramString, result);
 }

 @Test
 public void testReplaceWithEmptyString() throws Exception {
 String paramString = "";
 String taskId = "task_id";
 DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
 .parse(TestUtils.createJsonPathDocument());

 Object result = parametersUtils.replace(paramString, documentContext, taskId);

 // Verify the replaced values
 assertEquals("", result);
 }

 @Test
 public void testReplaceWithNullParam() throws Exception {
 String paramString = null;
 String taskId = "task_id";
 DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
 .parse(TestUtils.createJsonPathDocument());

 Object result = parametersUtils.replace(paramString, documentContext, taskId);

 // Verify the replaced values
 assertEquals(null, result);
 }

 @Test
 public void testClone() throws Exception {
 Map<String, Object> inputTemplate = TestUtils.createInputTemplate();
 Map<String, Object> clonedMap = parametersUtils.clone(inputTemplate);

 // Verify the cloned map is not the same object
 assertEquals(inputTemplate.getClass(), clonedMap.getClass());
 }

 @Test
 public void testCloneWithNullInputTemplate() throws Exception {
 Map<String, Object> clonedMap = parametersUtils.clone(null);

 // Verify the cloned map is an empty map
 assertEquals(new HashMap<String, Object>(), clonedMap);
 }
}