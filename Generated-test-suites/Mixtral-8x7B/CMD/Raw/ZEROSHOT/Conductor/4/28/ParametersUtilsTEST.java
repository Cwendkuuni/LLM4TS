import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.core.utils.ParametersUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

public class ParametersUtilsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametersUtilsTest.class);
    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInputV1() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(createInputTemplate());

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(createInput());
        workflowModel.setTasks(createTasks());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.name", "${workflow.input.firstName}.${workflow.input.lastName}");
        inputParams.put("task1.output.result", "${workflow.input.name}");

        Map<String, Object> result =
                parametersUtils.getTaskInputV1(workflowModel, inputParams);

        assertEquals("JohnDoe", result.get("task1.output.result"));
    }

    @Test
    public void testGetTaskInputV2() {
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(createInputTemplate());
        workflowDef.setSchemaVersion(2);

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setInput(createInput());
        workflowModel.setTasks(createTasks());
        workflowModel.setWorkflowDefinition(workflowDef);

        TaskDef taskDef = new TaskDef();
        taskDef.setInputTemplate(createTaskInputTemplate());

        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("workflow.input.name", "${workflow.input.firstName}.${workflow.input.lastName}");
        inputParams.put("task1.input.result", "${workflow.input.name}");
        inputParams.put("task2.input.result", "${workflow.input.firstName}");
        inputParams.put("task3.input.result", "${workflow.input.lastName}");

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "task1");

        assertEquals("JohnDoe", result.get("result"));
    }

    private Map<String, Object> createInputTemplate() {
        Map<String, Object> inputTemplate = new HashMap<>();
        inputTemplate.put("firstName", "John");
        inputTemplate.put("lastName", "Doe");
        return inputTemplate;
    }

    private Map<String, Object> createInput() {
        Map<String, Object> input = new HashMap<>();
        input.put("firstName", "John");
        input.put("lastName", "Doe");
        return input;
    }

    private List<TaskModel> createTasks() {
        List<TaskModel> tasks = new LinkedList<>();
        TaskModel taskModel = new TaskModel();
        taskModel.setInputData(createTaskInput());
        taskModel.setOutputData(createTaskOutput());
        tasks.add(taskModel);
        return tasks;
    }

    private Map<String, Object> createTaskInput() {
        Map<String, Object> taskInput = new HashMap<>();
        taskInput.put("result", "default");
        return taskInput;
    }

    private Map<String, Object> createTaskOutput() {
        Map<String, Object> taskOutput = new HashMap<>();
        taskOutput.put("result", "default");
        return taskOutput;
    }

    private Map<String, Object> createTaskInputTemplate() {
        Map<String, Object> taskInputTemplate = new HashMap<>();
        taskInputTemplate.put("result", "${workflow.input.firstName} ${workflow.input.lastName}");
        return taskInputTemplate;
    }
}