import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;
private Logger logger;

@Before
public void setup() {
mockitoAnnotations.initMocks(this);
objectMapper = new ObjectMapper();
logger = mock(Logger.class);
parametersUtils = new ParametersUtils(objectMapper) {
@Override
Logger getLogger() {
return logger;
}
};
}

@Test
public void testGetTaskInput() {
WorkflowModel workflowModel = createWorkflowModel();
TaskDef taskDef = createTaskDef();
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "value");

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "task-1");

assertEquals(3, result.size());
assertEquals("value", result.get("key"));
assertEquals("workflow-1", result.get("workflowId"));
assertEquals("task-1", result.get("taskId"));
}

@Test
public void testGetTaskInput_nullInputParams() {
WorkflowModel workflowModel = createWorkflowModel();
TaskDef taskDef = createTaskDef();

Map<String, Object> result =
parametersUtils.getTaskInput(null, workflowModel, taskDef, "task-1");

assertEquals(10, result.size());
assertEquals("workflow-1", result.get("workflowId"));
assertNull(result.get("input"));
}

@Test
public void testGetTaskInputV2() {
WorkflowModel workflowModel = createWorkflowModel();
TaskDef taskDef = createTaskDef();
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "value");

Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflowModel, "task-1", taskDef);

assertEquals(3, result.size());
assertEquals("value", result.get("key"));
assertEquals("workflow-1", result.get("workflowId"));
assertEquals("task-1", result.get("taskId"));
}

@Test
public void testGetTaskInputV2_nullInputParams() {
WorkflowModel workflowModel = createWorkflowModel();
TaskDef taskDef = createTaskDef();

Map<String, Object> result =
parametersUtils.getTaskInputV2(null, workflowModel, "task-1", taskDef);

assertEquals(10, result.size());
assertEquals("workflow-1", result.get("workflowId"));
assertNull(result.get("input"));
}

@Test
public void testGetTaskInputV2_inputTemplate() {
WorkflowModel workflowModel = createWorkflowModel();
TaskDef taskDef =
createTaskDefWithInputTemplate(Map.of("key", "inputTemplateValue"));
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "value");

Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflowModel, "task-1", taskDef);

assertEquals(3, result.size());
assertEquals("inputTemplateValue", result.get("key"));
}

@Test
public void testGetTaskInputV2_inputTemplateNullValue() {
WorkflowModel workflowModel = createWorkflowModel();
TaskDef taskDef =
createTaskDefWithInputTemplate(Map.of("key", null));
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("key", "value");

Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflowModel, "task-1", taskDef);

assertEquals(3, result.size());
assertEquals("value", result.get("key"));
}

@Test
public void testReplace_string() throws IOException {
String jsonString =
"{\"workflow\":{\"input\":{\"name\":\"workflow-input\", \"id\":1}, \"output\":null}, \"task\":null}";
DocumentContext documentContext =
JsonPath.parse(jsonString, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflowId", "workflow-1");

String result =
parametersUtils.replace(inputParams, documentContext, "task-1").get("workflowId").toString();

assertEquals("workflow-1", result);
}

@Test
public void testReplace_stringWithEnvVar() throws IOException {
String jsonString =
"{\"workflow\":{\"input\":{\"name\":\"$ENV_VAR\", \"id\":1}, \"output\":null}, \"task\":null}";
DocumentContext documentContext =
JsonPath.parse(jsonString, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));
Map<String, Object> inputParams = new HashMap<>();
EnvUtils.setSystemParameter("ENV_VAR", "env-value");

String result =
parametersUtils.replace(inputParams, documentContext, "task-1").get("workflow").get("input").get("name")
.toString();

assertEquals("env-value", result);
}

@Test
public void testReplace_nullValue() throws IOException {
String jsonString =
"{\"workflow\":{\"input\":{\"name\":\"workflow-input\", \"id\":1}, \"output\":null}, \"task\":null}";
DocumentContext documentContext =
JsonPath.parse(jsonString, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflowId", "workflow-1");

String result =
parametersUtils.replace(inputParams, documentContext, "task-1").get("task").toString();

assertNull(result);
}

private WorkflowModel createWorkflowModel() {
return new WorkflowModel()
.withInput(Map.of("name", "workflow-input"))
.withOutput(null)
.withStatus("status")
.withWorkflowId("workflow-1")
.withParentWorkflowId("parent-workflow-1")
.withParentWorkflowTaskId("parent-task-1")
.withWorkflowName("workflow-name")
.withWorkflowVersion("1")
.withCorrelationId("correlation-id")
.withReasonForIncompletion("reason-for-incompletion")
.withSchemaVersion(2)
.withVariables(Map.of("key", "value"))
.withTasks(List.of(new TaskModel().withInputData(Map.of("key", "task-input"))))
.withTaskByRefName("task-1", new TaskModel().withTaskId("task-1"));
}

private TaskDef createTaskDef() {
return new TaskDef().withInputTemplate(Map.of("key", "task-input"));
}

private TaskDef createTaskDefWithInputTemplate(Map<String, Object> inputTemplate) {
return new TaskDef().withInputTemplate(inputTemplate);
}
}