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
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;
private WorkflowModel workflowModel;
private TaskDef taskDef;
private Map<String, Object> inputParams;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

objectMapper = new ObjectMapper();

workflowModel = new WorkflowModel();
workflowModel.setInput(new HashMap<>());
workflowModel.setOutput(new HashMap<>());
workflowModel.setStatus("COMPLETED");
workflowModel.setWorkflowId("123");
workflowModel.setParentWorkflowId("456");
workflowModel.setParentWorkflowTaskId("789");
workflowModel.setWorkflowName("testWorkflow");
workflowModel.setWorkflowVersion(1);
workflowModel.setCorrelationId("correlationId");
workflowModel.setReasonForIncompletion("completed");
workflowModel.setSchemaVersion(1);
workflowModel.setVariables(new HashMap<>());

taskDef = mock(TaskDef.class);

inputParams = new HashMap<>();
inputParams.put("testKey", "${workflow.input.testValue}");
}

@Test
public void testGetTaskInputV2() {
Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflowModel, taskDef, "123");
assertEquals(inputParams, result);
}

@Test
public void testClone() {
Map<String, Object> inputTemplate = new HashMap<>();
inputTemplate.put("testKey", "testValue");

Map<String, Object> clonedMap = parametersUtils.clone(inputTemplate);
assertEquals(inputTemplate, clonedMap);
}

@Test
public void testReplace() {
Map<String, Object> input = new HashMap<>();
input.put("testKey", "${workflow.input.testValue}");

DocumentContext documentContext = JsonPath.parse("{\"input\": {\"testValue\": \"replaced\"}}");

Map<String, Object> replacedInput = parametersUtils.replace(input, documentContext);
assertEquals("replaced", replacedInput.get("testKey"));
}

@Test
public void testReplaceVariables() {
String paramString = "${workflow.input.testValue}";

DocumentContext documentContext = JsonPath.parse("{\"input\": {\"testValue\": \"replaced\"}}");

String replacedParam =
parametersUtils.replaceVariables(paramString, documentContext, null, 0).toString();
assertEquals("replaced", replacedParam);
}

@Test
public void testReplaceVariablesWithEnvVariable() {
String paramString = "${ENV_VAR}";

DocumentContext documentContext = JsonPath.parse("{}");

String replacedParam =
parametersUtils.replaceVariables(paramString, documentContext, "taskId", 0).toString();
assertEquals("envValue", replacedParam);
}

@Test
public void testGetWorkflowInput() {
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(new HashMap<>());
workflowDef.getInputTemplate().put("testKey", "testValue");

Map<String, Object> result = parametersUtils.getWorkflowInput(workflowDef, inputParams);
assertEquals(inputParams, result);
}
}

@Component
class Utils {

public static void checkArgument(boolean expression, String errorMessage) {
if (!expression) {
throw new IllegalArgumentException(errorMessage);
}
}
}