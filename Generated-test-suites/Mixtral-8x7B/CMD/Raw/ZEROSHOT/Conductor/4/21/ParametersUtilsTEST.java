import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.common.utils.TaskUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.hamcrest.CoreMatchers;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParametersUtilsTest {

private ParametersUtils parametersUtils;
private ObjectMapper objectMapper;
private WorkflowModel workflowModel;
private TaskDef taskDef;
private TaskModel taskModel;
private EnvUtils envUtils;

@Before
public void setUp() {
objectMapper = mock(ObjectMapper.class);
envUtils = mock(EnvUtils.class);
parametersUtils = new ParametersUtils(objectMapper) {
@Override
EnvUtils getEnvUtils() {
return envUtils;
}
};
workflowModel = new WorkflowModel();
workflowModel.setInput(new HashMap<>());
workflowModel.setOutput(new HashMap<>());
workflowModel.setStatus("COMPLETED");
workflowModel.setWorkflowId("workflowId");
workflowModel.setParentWorkflowId("parentWorkflowId");
workflowModel.setParentWorkflowTaskId("parentWorkflowTaskId");
workflowModel.setWorkflowType("workflowType");
workflowModel.setVersion(1);
workflowModel.setCorrelationId("correlationId");
workflowModel.setReasonForIncompletion("reasonForIncompletion");
workflowModel.setSchemaVersion(1);
workflowModel.setVariables(new HashMap<>());

taskDef = new TaskDef();
taskDef.setInputTemplate(new HashMap<>());

taskModel = new TaskModel();
taskModel.setInputData(new HashMap<>());
taskModel.setOutputData(new HashMap<>());
}

@Test
public void testGetTaskInputV1() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflow.input.param1", "value1");

Map<String, Object> result =
parametersUtils.getTaskInputV1(workflowModel, inputParams);

assertThat(result, notNullValue());
assertThat(result.size(), equalTo(1));
assertThat(result.get("param1"), equalTo("value1"));
}

@Test
public void testGetTaskInputV2() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflow.input.param1", "value1");
inputParams.put("task.input.param2", "value2");
inputParams.put("task.output.param3", "value3");

when(envUtils.getSystemParametersValue("envParam", "taskId"))
.thenReturn("envValue");

Map<String, Object> result =
parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);

assertThat(result, notNullValue());
assertThat(result.size(), equalTo(3));
assertThat(result.get("param1"), equalTo("value1"));
assertThat(result.get("param2"), equalTo("value2"));
assertThat(result.get("param3"), equalTo("value3"));

Map<String, Object> taskInputTemplate = taskDef.getInputTemplate();
taskInputTemplate.put("param4", "templateValue");

result = parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId", taskDef);

assertThat(result.get("param4"), equalTo("templateValue"));
}

@Test
public void testReplace() {
String json =
"{\"param1\":\"${workflow.input.param1}\", \"param2\":\"${task.input.param2}\", \"param3\":\"${task.output.param3}\"}"
+ ",\"param4\":\"${nonexistent}\", \"param5\":\"${envParam}\"";
Object result =
parametersUtils.replace(json, workflowModel, "taskId", JsonPath.parse(json));

assertThat(result, notNullValue());
assertThat(result.toString(), equalTo(
"{\"param1\":\"value1\", \"param2\":\"value2\", \"param3\":\"value3\", \"param4\":null, \"param5\":" +
"\"envValue\"}"));
}

@Test
public void testReplaceVariables() {
String paramString = "${workflow.input.param1}.${task.input.param2}.${task.output.param3}";
Object result =
parametersUtils.replaceVariables(paramString, workflowModel, "taskId", JsonPath.parse(paramString));

assertThat(result, notNullValue());
assertThat(result.toString(), equalTo("value1.value2.value3"));
}

@Test
public void testGetWorkflowInput() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("workflow.input.param1", "value1");

WorkflowDef workflowDef = new WorkflowDef();
workflowDef.setInputTemplate(new HashMap<>());
workflowDef.getInputTemplate().put("param2", "value2");

Map<String, Object> result =
parametersUtils.getWorkflowInput(workflowDef, inputParams);

assertThat(result, notNullValue());
assertThat(result.get("param1"), equalTo("value1"));
assertThat(result.get("param2"), equalTo("value2"));
}

private <T> T clone(T object) throws IOException {
return objectMapper.readValue(objectMapper.writeValueAsBytes(object), object.getClass());
}
}