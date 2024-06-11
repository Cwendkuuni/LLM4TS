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
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.hamcrest.collection.IsMapContaining;
import org.junit.Assert;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;

@Before
public void setup() {
objectMapper = mock(ObjectMapper.class);
parametersUtils = new ParametersUtils(objectMapper);
}

@Test
public void testGetTaskInput_WithInputParams_ShouldReturnMergedMap() {
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(Collections.singletonMap("workflowKey", "workflowValue"));
workflowModel.setWorkflowName("testWorkflow");
workflowModel.setWorkflowVersion("1.0");
TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(Collections.singletonMap("taskKey", "taskValue"));
String taskId = "123";

Map<String, Object> inputParams = new HashMap<>();
inputParams.put("inputParamKey", "inputParamValue");
inputParams.put("workflowKey", "workflowValueFromInputParams");

Map<String, Object> expected = new HashMap<>();
expected.put("workflowKey", "workflowValueFromInputParams");
expected.put("taskKey", "taskValue");
expected.put("inputParamKey", "inputParamValue");

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, taskId);

Assert.assertThat(result, IsMapContaining.hasEntry("workflowKey", "workflowValueFromInputParams"));
Assert.assertThat(result, IsMapContaining.hasEntry("taskKey", "taskValue"));
Assert.assertThat(result, IsMapContaining.hasEntry("inputParamKey", "inputParamValue"));
}

@Test
public void testReplace_WithSimpleJson_ShouldReplaceCorrectly() {
String json = "{\"key\": \"${workflow.input.key}\"}";
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(Collections.singletonMap("key", "value"));
DocumentContext context = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.build()
.parse(json);

String result = parametersUtils.replace(context, workflowModel);

Assert.assertEquals("{\"key\": \"value\"}", result);
}

@Test
public void testReplace_WithJsonPath_ShouldReplaceCorrectly() {
String json = "{\"key\": \"${$.workflow.input.key}\"}";
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(Collections.singletonMap("key", "value"));
DocumentContext context = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS)
.build()
.parse(json);

String result = parametersUtils.replace(context, workflowModel);

Assert.assertEquals("{\"key\": \"value\"}", result);
}

@Test
public void testReplaceVariables_ShouldReplaceCorrectly() {
String input = "${workflow.input.key}";
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(Collections.singletonMap("key", "value"));
String result =
parametersUtils.replaceVariables(input, workflowModel, null, 0);

Assert.assertEquals("value", result);
}

@Test
public void testGetWorkflowInput_WithInputTemplate_ShouldReturnMergedMap() {
WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(Collections.singletonMap("workflowKey", "workflowValue"));
workflowDef.setWorkflowName("testWorkflow");
workflowDef.setVersion("1.0");

Map<String, Object> inputParams = new HashMap<>();
inputParams.put("inputParamKey", "inputParamValue");

Map<String, Object> expected = new HashMap<>();
expected.put("workflowKey", "workflowValue");
expected.put("inputParamKey", "inputParamValue");

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

Assert.assertThat(result, IsMapContaining.hasEntry("workflowKey", "workflowValue"));
Assert.assertThat(result, IsMapContaining.hasEntry("inputParamKey", "inputParamValue"));
}

private class TestUtils {
public static void checkArgument(boolean condition, String errorMessage) {
if (!condition) {
throw new IllegalArgumentException(errorMessage);
}
}
}
}