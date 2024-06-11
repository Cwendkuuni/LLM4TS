import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Component
public class ParametersUtilsTest {

 private ParametersUtils parametersUtils;
 private ObjectMapper objectMapper;
 private EnvUtils envUtils;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 objectMapper = mock(ObjectMapper.class);
 envUtils = mock(EnvUtils.class);
 parametersUtils = new ParametersUtils(objectMapper, envUtils);
 }

 @Test
 public void testGetTaskInput() throws IOException {
 WorkflowModel workflowModel = new WorkflowModel();
 workflowModel.setInput(new HashMap<>());
 workflowModel.setOutput(new HashMap<>());
 workflowModel.setStatus("COMPLETED");
 workflowModel.setWorkflowId("123");
 workflowModel.setParentWorkflowId("456");
 workflowModel.setParentWorkflowTaskId("789");
 workflowModel.setWorkflowName("testWorkflow");
 workflowModel.setWorkflowVersion(1);
 workflowModel.setCorrelationId("correlationId");
 workflowModel.setReasonForIncompletion("incompletionReason");
 workflowModel.setSchemaVersion(1);
 workflowModel.setVariables(new HashMap<>());

 TaskDef taskDef = new TaskDef();
 taskDef.setInputTemplate(new HashMap<>());

 Map<String, Object> inputParams = new HashMap<>();

 when(objectMapper.writeValueAsBytes(any())).thenReturn("testBytes".getBytes());
 when(objectMapper.readValue(any(), any())).thenReturn(new HashMap<>());
 when(envUtils.isEnvironmentVariable(any())).thenReturn(false);
 when(envUtils.getSystemParametersValue(any(), any())).thenReturn("testEnvValue");

 Map<String, Object> result =
 parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "testTaskId");

 // Verify that the result contains the expected keys and values
 assertEquals(result.get("workflowId"), "123");
 assertEquals(result.get("parentWorkflowId"), "456");
 assertEquals(result.get("parentWorkflowTaskId"), "789");
 assertEquals(result.get("workflowName"), "testWorkflow");
 assertEquals(result.get("workflowVersion"), 1);
 assertEquals(result.get("correlationId"), "correlationId");
 assertEquals(result.get("reasonForIncompletion"), "incompletionReason");
 assertEquals(result.get("schemaVersion"), 1);
 }

 @Test
 public void testReplace_withString() {
 String input =
 "This is a ${workflow.input.param1} string with ${workflow.output.param2} and a ${task.input.param3}";

 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("param1", "value1");

 Map<String, Object> workflowOutput = new HashMap<>();
 workflowOutput.put("param2", "value2");

 Map<String, Object> taskInput = new HashMap<>();
 taskInput.put("param3", "value3");

 Map<String, Object> context = new HashMap<>();
 context.put("workflow", workflowInput);
 context.put("task", taskInput);

 String result =
 parametersUtils.replace(input, context, null);

 // Verify that the result is as expected
 assertEquals(result,
 "This is a value1 string with value2 and a value3");
 }

 @Test
 public void testReplace_withList() {
 String input =
 "${workflow.input.param1} ${workflow.input.param2} ${workflow.input.param3}";

 List<String> list = new LinkedList<>();
 list.add("value1");
 list.add("value2");
 list.add("value3");

 Map<String, Object> workflowInput = new HashMap<>();
 workflowInput.put("param1", list);

 Map<String, Object> context = new HashMap<>();
 context.put("workflow", workflowInput);

 String result =
 parametersUtils.replace(input, context, null);

 // Verify that the result is as expected
 assertEquals(result, "value1 value2 value3");
 }

 @Test
 public void testGetWorkflowInput() throws IOException {
 WorkflowDef workflowDef = new WorkflowDef();
 workflowDef.setInputTemplate(new HashMap<>());

 Map<String, Object> inputParams = new HashMap<>();

 when(objectMapper.writeValueAsBytes(any())).thenReturn("testBytes".getBytes());
 when(objectMapper.readValue(any(), any())).thenReturn(new HashMap<>());
 when(envUtils.isEnvironmentVariable(any())).thenReturn(false);
 when(envUtils.getSystemParametersValue(any(), any())).thenReturn("testEnvValue");

 Map<String, Object> result =
 parametersUtils.getWorkflowInput(workflowDef, inputParams);

 // Verify that the result contains the expected keys and values
 }

}