import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.ParamUtils;
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

private ParamUtils paramUtils;
private ObjectMapper objectMapper;
private EnvUtils envUtils;

@Before
public void setup() {
objectMapper = mock(ObjectMapper.class);
envUtils = mock(EnvUtils.class);
paramUtils = new ParametersUtils(objectMapper, envUtils);
}

@Test
public void testGetTaskInput() throws IOException {
WorkflowModel workflowModel = createWorkflowModel();
TaskDef taskDef = createTaskDef();
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "value");

Map<String, Object> result =
paramUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

Map<String, Object> expected = new HashMap<>();
expected.put("key", "value");
expected.put("workflowId", "workflowId");
expected.put("parentWorkflowId", "parentWorkflowId");
expected.put("parentWorkflowTaskId", "parentWorkflowTaskId");
expected.put("workflowType", "workflowType");
expected.put("version", 1);
expected.put("correlationId", "correlationId");

assertEquals(expected, result);
}

@Test
public void testGetTaskInput_v2() throws IOException {
WorkflowModel workflowModel = createWorkflowModelV2();
TaskDef taskDef = createTaskDef();
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "value");

Map<String, Object> result =
paramUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);

Map<String, Object> expected = new HashMap<>();
expected.put("key", "value");
expected.put("workflowId", "workflowId");
expected.put("parentWorkflowId", "parentWorkflowId");
expected.put("parentWorkflowTaskId", "parentWorkflowTaskId");
expected.put("workflowType", "workflowType");
expected.put("version", 2);
expected.put("correlationId", "correlationId");

assertEquals(expected, result);
}

@Test
public void testReplace_string() {
String input = "This is a ${param} and ${anotherParam}";
DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{\"param\": \"value\", \"anotherParam\": \"anotherValue\"}");

String result = paramUtils.replace(input, documentContext, null);

assertEquals("This is a value and anotherValue", result);
}

@Test
public void testReplace_list() {
List<String> input = Collections.singletonList("${param}");
DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{\"param\": [\"value1\", \"value2\"]}");

List<String> result = paramUtils.replaceList(input, null, documentContext);

assertEquals(2, result.size());
assertEquals("value1", result.get(0));
assertEquals("value2", result.get(1));
}

@Test
public void testReplaceVariables_emptyParamPath() {
String paramString = "This is a ${}";
DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{}");

String result = paramUtils.replaceVariables(paramString, documentContext, null, 0).toString();

assertEquals("This is a ", result);
}

@Test
public void testReplaceVariables_environmentVariable() {
String paramString = "This is a ${ENV_VAR}";
DocumentContext documentContext = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.parse("{}");
when(envUtils.isEnvironmentVariable("ENV_VAR")).thenReturn(true);
when(envUtils.getSystemParametersValue("ENV_VAR", null)).thenReturn("envValue");

String result = paramUtils.replaceVariables(paramString, documentContext, null, 0).toString();

assertEquals("This is a envValue", result);
}

@Test
public void testGetWorkflowInput() throws IOException {
WorkflowDef workflowDef = createWorkflowDef();
Map<String, Object> inputParams = new HashMap<>();

Map<String, Object> result = paramUtils.getWorkflowInput(workflowDef, inputParams);

Map<String, Object> expected = new HashMap<>();
expected.put("workflowParam", "workflowValue");

assertEquals(expected, result);
}

private WorkflowModel createWorkflowModel() {
return new WorkflowModel()
.withInput(Collections.singletonMap("workflowParam", "workflowValue"))
.withOutput(Collections.singletonMap("workflowParam", "workflowValue"))
.withStatus("status")
.withWorkflowId("workflowId")
.withParentWorkflowId("parentWorkflowId")
.withParentWorkflowTaskId("parentWorkflowTaskId")
.withWorkflowName("workflowType")
.withWorkflowVersion(1)
.withCorrelationId("correlationId")
.withReasonForIncompletion("reasonForIncompletion")
.withSchemaVersion(1);
}

private WorkflowModel createWorkflowModelV2() {
return new WorkflowModel()
.withInput(Collections.singletonMap("workflowParam", "workflowValue"))
.withOutput(Collections.singletonMap("workflowParam", "workflowValue"))
.withStatus("status")
.withWorkflowId("workflowId")
.withParentWorkflowId("parentWorkflowId")
.withParentWorkflowTaskId("parentWorkflowTaskId")
.withWorkflowName("workflowType")
.withWorkflowVersion(2)
.withCorrelationId("correlationId")
.withReasonForIncompletion("reasonForIncompletion")
.withSchemaVersion(2)
.withTasks(Collections.singletonList(new TaskModel().withInputData(new HashMap<>()).withOutputData(new HashMap<>())));
}

private TaskDef createTaskDef() {
return new TaskDef().withInputTemplate(Collections.singletonMap("taskParam", "taskValue"));
}

private WorkflowDef createWorkflowDef() throws IOException {
String workflowDefJson = "{\"inputTemplate\":{\"workflowParam\":\"workflowValue\"}}";
return objectMapper.readValue(workflowDefJson, new TypeReference<WorkflowDef>() {});
}
}