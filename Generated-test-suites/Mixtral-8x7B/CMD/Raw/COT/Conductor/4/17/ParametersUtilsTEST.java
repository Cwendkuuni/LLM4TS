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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class ParametersUtilsTest {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ParametersUtilsTest.class);

    private ParametersUtils parametersUtils;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        this.parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(new HashMap<>());
        workflowModel.setOutput(new HashMap<>());
        workflowModel.setStatus("status");
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setParentWorkflowId("parentWorkflowId");
        workflowModel.setParentWorkflowTaskId("parentWorkflowTaskId");
        workflowModel.setWorkflowName("workflowName");
        workflowModel.setWorkflowVersion(1);
        workflowModel.setCorrelationId("correlationId");
        workflowModel.setReasonForIncompletion("reasonForIncompletion");
        workflowModel.setSchemaVersion(1);
        workflowModel.setVariables(new HashMap<>());
        workflowModel.setTasks(new LinkedList<>());

        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflowModel, taskDefinition, "taskId");

        assertEquals("value", result.get("key"));
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(new HashMap<>());
        workflowModel.setOutput(new HashMap<>());
        workflowModel.setStatus("status");
        workflowModel.setWorkflowId("workflowId");
        workflowModel.setParentWorkflowId("parentWorkflowId");
        workflowModel.setParentWorkflowTaskId("parentWorkflowTaskId");
        workflowModel.setWorkflowName("workflowName");
        workflowModel.setWorkflowVersion(1);
        workflowModel.setCorrelationId("correlationId");
        workflowModel.setReasonForIncompletion("reasonForIncompletion");
        workflowModel.setSchemaVersion(2);
        workflowModel.setVariables(new HashMap<>());
        workflowModel.setTasks(new LinkedList<>());

        TaskModel taskModel1 = new TaskModel();
        taskModel1.setInputData(new HashMap<>());
        taskModel1.setOutputData(new HashMap<>());
        taskModel1.setTaskType("taskType1");
        taskModel1.setStatus("status1");
        taskModel1.setReferenceTaskName("referenceTaskName1");
        taskModel1.setRetryCount(1);
        taskModel1.setCorrelationId("correlationId1");
        taskModel1.setPollCount(1);
        taskModel1.setTaskDefName("taskDefName1");
        taskModel1.setScheduledTime(1L);
        taskModel1.setStartTime(1L);
        taskModel1.setEndTime(1L);
        taskModel1.setWorkflowInstanceId("workflowInstanceId1");
        taskModel1.setTaskId("taskId1");
        taskModel1.setReasonForIncompletion("reasonForIncompletion1");
        taskModel1.setCallbackAfterSeconds(1);
        taskModel1.setWorkerId("workerId1");
        taskModel1.setIteration(1);

        TaskModel taskModel2 = new TaskModel();
        taskModel2.setInputData(new HashMap<>());
        taskModel2.setOutputData(new HashMap<>());
        taskModel2.setTaskType("taskType2");
        taskModel2.setStatus("status2");
        taskModel2.setReferenceTaskName("referenceTaskName2");
        taskModel2.setRetryCount(2);
        taskModel2.setCorrelationId("correlationId2");
        taskModel2.setPollCount(2);
        taskModel2.setTaskDefName("taskDefName2");
        taskModel2.setScheduledTime(2L);
        taskModel2.setStartTime(2L);
        taskModel2.setEndTime(2L);
        taskModel2.setWorkflowInstanceId("workflowInstanceId2");
        taskModel2.setTaskId("taskId2");
        taskModel2.setReasonForIncompletion("reasonForIncompletion2");
        taskModel2.setCallbackAfterSeconds(2);
        taskModel2.setWorkerId("workerId2");
        taskModel2.setIteration(2);

        workflowModel.getTasks().add(taskModel1);
        workflowModel.getTasks().add(taskModel2);

        TaskDef taskDefinition = new TaskDef();
        taskDefinition.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "workflow.input.key");

        Map<String, Object> result =
                parametersUtils.getTaskInputV2(inputParams, workflowModel, "taskId1", taskDefinition);

        assertEquals("value", result.get("key"));
    }

    @Test
    public void testGetWorkflowInput() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(new HashMap<>());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key", "value");

        Map<String, Object> result =
                parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals("value", result.get("key"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        DocumentContext documentContext =
                JsonPath.parse(map, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        String json = "{\"key3\": \"${key1}\"}";

        String result = parametersUtils.replace(json, documentContext, null).toString();

        assertEquals("{\"key3\": \"value1\"}", result);
    }

    @Test
    public void testReplaceList() {
        List<String> list = new LinkedList<>();
        list.add("item1");
        list.add("${key}");

        String json = "{\"key\": \"value\"}";

        DocumentContext documentContext = JsonPath.parse(json, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        List<Object> result =
                parametersUtils.replaceList(list, null, documentContext);

        assertEquals("item1", result.get(0));
        assertEquals("value", result.get(1));
    }

    @Test
    public void testReplaceVariables() {
        String json = "${key1}.${key2}";

        DocumentContext documentContext =
                JsonPath.parse(new HashMap<>(), Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        String result = parametersUtils.replaceVariables(json, documentContext, null).toString();

        assertEquals("${key1}.${key2}", result);
    }

    @Test
    public void testReplaceVariablesWithEnvVar() {
        String json = "${ENV_VAR}";

        Map<String, String> envVars = new HashMap<>();
        envVars.put("ENV_VAR", "envValue");

        DocumentContext documentContext =
                JsonPath.parse(envVars, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        String result = parametersUtils.replaceVariables(json, documentContext, null).toString();

        assertEquals("envValue", result);
    }

    @Test
    public void testReplaceVariablesWithTaskId() {
        String json = "${taskId}";

        Map<String, String> envVars = new HashMap<>();
        envVars.put("ENV_VAR", "envValue");

        DocumentContext documentContext =
                JsonPath.parse(envVars, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        String result = parametersUtils.replaceVariables(json, documentContext, "taskId").toString();

        assertEquals("taskId", result);
    }

    @Test
    public void testReplaceVariablesWithNullValue() {
        String json = "${key}";

        Map<String, Object> variables = new HashMap<>();
        variables.put("otherKey", "otherValue");

        DocumentContext documentContext =
                JsonPath.parse(variables, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        String result = parametersUtils.replaceVariables(json, documentContext, null).toString();

        assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithEmptyString() {
        String json = "${key}";

        Map<String, String> variables = new HashMap<>();
        variables.put("key", "");

        DocumentContext documentContext =
                JsonPath.parse(variables, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        String result = parametersUtils.replaceVariables(json, documentContext, null).toString();

        assertEquals("", result);
    }

    @Test
    public void testReplaceVariablesWithComplexPath() {
        String json = "${workflow.input.key}";

        Map<String, Object> variables = new HashMap<>();
        variables.put("workflow", new HashMap<>());
        variables.get("workflow").put("input", new HashMap<>());
        variables.get("workflow").get("input").put("key", "value");

        DocumentContext documentContext =
                JsonPath.parse(variables, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        String result = parametersUtils.replaceVariables(json, documentContext, null).toString();

        assertEquals("value", result);
    }

    @Test
    public void testReplaceVariablesWithComplexPathEnvVar() {
        String json = "${ENV_VAR_WORKFLOW_INPUT_KEY}";

        Map<String, String> envVars = new HashMap<>();
        envVars.put("ENV_VAR_WORKFLOW_INPUT_KEY", "envValue");

        DocumentContext documentContext =
                JsonPath.parse(envVars, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        String result = parametersUtils.replaceVariables(json, documentContext, null).toString();

        assertEquals("envValue", result);
    }

    @Test
    public void testReplaceVariablesWithComplexPathTaskId() {
        String json = "${taskId.input.key}";

        Map<String, Object> variables = new HashMap<>();
        variables.put("taskId", new HashMap<>());
        variables.get("taskId").put("input", new HashMap<>());
        variables.get("taskId").get("input").put("key", "value");

        DocumentContext documentContext =
                JsonPath.parse(variables, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        String result = parametersUtils.replaceVariables(json, documentContext, "taskId").toString();

        assertEquals("value", result);
    }

    @Test
    public void testReplaceVariablesWithComplexPathNullValue() {
        String json = "${workflow.input.key}";

        Map<String, Object> variables = new HashMap<>();
        variables.put("workflow", new HashMap<>());

        DocumentContext documentContext =
                JsonPath.parse(variables, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        String result = parametersUtils.replaceVariables(json, documentContext, null).toString();

        assertNull(result);
    }

    @Test
    public void testReplaceVariablesWithComplexPathEmptyString() {
        String json = "${workflow.input.key}";

        Map<String, String> variables = new HashMap<>();
        variables.put("workflow", new HashMap<>());
        variables.get("workflow").put("input", new HashMap<>());
        variables.get("workflow").get("input").put("key", "");

        DocumentContext documentContext =
                JsonPath.parse(variables, Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));

        String result = parametersUtils.replaceVariables(json, documentContext, null).toString();

        assertEquals("", result);
    }
}