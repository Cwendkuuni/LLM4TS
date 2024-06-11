package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
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
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParametersUtilsTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> mapTypeReference = new TypeReference<>() {};

    private ParametersUtils parametersUtils;
    private WorkflowModel workflowModel;
    private WorkflowDef workflowDef;
    private TaskDef taskDef;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parametersUtils = new ParametersUtils(objectMapper);

        workflowDef = new WorkflowDef();
        workflowDef.setSchemaVersion(1);

        workflowModel = new WorkflowModel();
        workflowModel.setInput(new HashMap<>());
        workflowModel.setOutput(new HashMap<>());
        workflowModel.setStatus("COMPLETED");
        workflowModel.setWorkflowId("workflow1");
        workflowModel.setParentWorkflowId("parentWorkflow1");
        workflowModel.setParentWorkflowTaskId("parentTask1");
        workflowModel.setWorkflowType("testWorkflowType");
        workflowModel.setVersion(1);
        workflowModel.setCorrelationId("correlationId1");
        workflowModel.setReasonForIncompletion("taskFailed");
        workflowModel.setVariables(new HashMap<>());

        List<TaskModel> tasks = new LinkedList<>();
        TaskModel task1 = new TaskModel();
        task1.setInputData(new HashMap<>());
        task1.setOutputData(new HashMap<>());
        task1.setStatus("COMPLETED");
        task1.setReferenceTaskName("task1");
        task1.setTaskType("testTaskType1");
        task1.setRetryCount(3);
        task1.setCorrelationId("correlationId1");
        task1.setPollCount(2);
        task1.setTaskDefName("testTaskDef1");
        task1.setScheduledTime(1589432112000L);
        task1.setStartTime(1589432112000L);
        task1.setEndTime(1589432112000L);
        task1.setWorkflowInstanceId("workflowInstanceId1");
        task1.setTaskId("taskId1");
        task1.setReasonForIncompletion("taskFailed");
        task1.setCallbackAfterSeconds(10);
        task1.setWorkerId("workerId1");
        task1.setIteration(1);
        tasks.add(task1);

        TaskModel task2 = new TaskModel();
        task2.setInputData(new HashMap<>());
        task2.setOutputData(new HashMap<>());
        task2.setStatus("COMPLETED");
        task2.setReferenceTaskName("task2");
        task2.setTaskType("testTaskType2");
        task2.setRetryCount(3);
        task2.setCorrelationId("correlationId2");
        task2.setPollCount(2);
        task2.setTaskDefName("testTaskDef2");
        task2.setScheduledTime(1589432112000L);
        task2.setStartTime(1589432112000L);
        task2.setEndTime(1589432112000L);
        task2.setWorkflowInstanceId("workflowInstanceId2");
        task2.setTaskId("taskId2");
        task2.setReasonForIncompletion("taskFailed");
        task2.setCallbackAfterSeconds(10);
        task2.setWorkerId("workerId2");
        task2.setIteration(1);
        tasks.add(task2);

        workflowModel.setTasks(tasks);

        taskDef = new TaskDef();
        taskDef.setInputTemplate(new HashMap<>());
    }

    @Test
    public void testGetTaskInput() throws IOException {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.param1", "value1");
        inputParams.put("task1.output.param2", "value2");
        inputParams.put("task2.input.param3", "value3");

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "task1");

        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("param2"));
        assertNull(result.get("param3"));
    }

    @Test
    public void testGetTaskInputV1() throws IOException {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.param1", "value1");
        inputParams.put("task1.param2", "value2");

        Map<String, Object> result =
                parametersUtils.getTaskInputV1(workflowModel, inputParams);

        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("param2"));
    }

    @Test
    public void testGetWorkflowInput() throws IOException {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.param1", "value1");
        inputParams.put("workflow.input.param2", "value2");

        Map<String, Object> result =
                parametersUtils.getWorkflowInput(workflowDef, inputParams);

        assertEquals("value1", result.get("param1"));
        assertEquals("value2", result.get("param2"));
    }

    @Test
    public void testReplace() throws IOException {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "${workflow.input.paramA}");
        inputParams.put("param2", "${task1.output.paramB}");

        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext =
                JsonPath.parse("{\"paramA\": \"valueA\", \"paramB\": \"valueB\"}", option);
        Map<String, Object> result =
                parametersUtils.replace(inputParams, documentContext, "task1");

        assertEquals("valueA", result.get("param1"));
        assertEquals("valueB", result.get("param2"));
    }

    @Test
    public void testReplaceList() throws IOException {
        List<Object> inputList = new LinkedList<>();
        inputList.add("${workflow.input.paramA}");
        inputList.add("${task1.output.paramB}");

        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext =
                JsonPath.parse("{\"paramA\": \"valueA\", \"paramB\": \"valueB\"}", option);
        List<Object> result =
                parametersUtils.replaceList(inputList, "task1", documentContext);

        assertEquals("valueA", ((LinkedList<Object>) result).get(0));
        assertEquals("valueB", ((LinkedList<Object>) result).get(1));
    }

    @Test
    public void testReplaceVariables() throws IOException {
        String input = "This is a ${workflow.input.test} for replaceVariables method";

        Configuration option = Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS);
        DocumentContext documentContext =
                JsonPath.parse("{\"test\": \"testValue\"}", option);
        String result =
                parametersUtils.replaceVariables(input, documentContext, null);

        assertEquals(
                "This is a testValue for replaceVariables method", result);

        String inputNested = "This is a ${workflow.input.test.nested} for replaceVariables method";
        try {
            parametersUtils.replaceVariables(inputNested, documentContext, null);
            fail("Expected JsonPathException for non-existing path");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Invalid JsonPath"));
        }

        String inputEnv = "This is a ${env:TEST_ENV_VARIABLE} for replaceVariables method";
        System.setProperty("TEST_ENV_VARIABLE", "envValue");
        String resultEnv =
                parametersUtils.replaceVariables(inputEnv, documentContext, null);
        assertEquals(
                "This is a envValue for replaceVariables method", resultEnv);
        System.clearProperty("TEST_ENV_VARIABLE");
    }
}