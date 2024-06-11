import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.utils.EnvUtils;
import com.netflix.conductor.core.utils.ParametersUtils;
import com.netflix.conductor.core.utils.TaskUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);

objectMapper = spy(new ObjectMapper());
parametersUtils = new ParametersUtils(objectMapper);
}

@Test
public void testGetTaskInput() {
WorkflowModel workflow = new WorkflowModel();
workflow.setWorkflowDefinition(new WorkflowDef());
workflow.setInput(new HashMap<>());
workflow.getWorkflowDefinition().setSchemaVersion(1);

TaskDef taskDefinition = new TaskDef();
taskDefinition.setInputTemplate(new HashMap<>());

Map<String, Object> inputParams = new HashMap<>();

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflow, taskDefinition, "taskId");

assertEquals(inputParams, result);
}

@Test
public void testGetTaskInputV2() {
WorkflowModel workflow = new WorkflowModel();
workflow.setWorkflowDefinition(new WorkflowDef());
workflow.setInput(new HashMap<>());
workflow.setOutput(new HashMap<>());
workflow.setStatus("status");
workflow.setWorkflowId("workflowId");
workflow.setParentWorkflowId("parentWorkflowId");
workflow.setParentWorkflowTaskId("parentWorkflowTaskId");
workflow.setWorkflowType("workflowType");
workflow.setVersion(1);
workflow.setCorrelationId("correlationId");
workflow.setReasonForIncompletion("reasonForIncompletion");
workflow.setSchemaVersion(2);
workflow.setVariables(new HashMap<>());

TaskDef taskDefinition = new TaskDef();
taskDefinition.setInputTemplate(new HashMap<>());
taskDefinition.setTaskDefName("taskDefName");

Map<String, Object> inputParams = new HashMap<>();

Map<String, Object> inputMap = new HashMap<>();
inputMap.put("workflow", workflow);

Map<String, Map<String, Object>> taskMap = new HashMap<>();
Map<String, Object> taskParams = new HashMap<>();
taskParams.put("input", "inputData");
taskParams.put("output", "outputData");
taskParams.put("taskType", "taskType");
taskParams.put("status", "status");
taskParams.put("referenceTaskName", "referenceTaskName");
taskParams.put("retryCount", 1);
taskParams.put("correlationId", "correlationId");
taskParams.put("pollCount", 1);
taskMap.put("taskRefName", taskParams);

when(objectMapper.readValue(inputMap.toString(), Map.class))
.thenReturn(inputMap);

Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflow, "taskId", taskDefinition);

assertEquals(inputParams, result);
}

@Test
public void testClone() throws IOException {
Map<String, Object> inputTemplate = new HashMap<>();
inputTemplate.put("key", "value");

byte[] bytes = objectMapper.writeValueAsBytes(inputTemplate);
Map<String, Object> cloned = objectMapper.readValue(bytes, new TypeReference<>() {});

assertEquals(inputTemplate, cloned);
}

@Test
public void testReplace() {
Map<String, Object> input = new HashMap<>();
input.put("key", "${param}");

Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), option);

Map<String, Object> replaced =
parametersUtils.replace(input, documentContext, "paramValue");

assertEquals("paramValue", replaced.get("key"));
}

@Test
public void testReplaceVariables() {
String paramString = "This is a ${param} string";

Configuration option =
Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
DocumentContext documentContext = JsonPath.parse(Collections.emptyMap(), option);

String replaced = parametersUtils.replaceVariables(paramString, documentContext, "paramValue");

assertEquals("This is a paramValue string", replaced);
}

@Test
public void testGetWorkflowInput() {
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(new HashMap<>());

Map<String, Object> inputParams = new HashMap<>();

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertEquals(inputParams, result);
}
}