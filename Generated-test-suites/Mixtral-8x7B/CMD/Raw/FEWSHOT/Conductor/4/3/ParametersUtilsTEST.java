package com.netflix.conductor.core.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.netflix.conductor.common.metadata.tasks.TaskDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowModel;
import com.netflix.conductor.common.utils.EnvUtils;
import com.netflix.conductor.model.TaskModel;
import com.netflix.conductor.model.WorkflowModel;
import org.junit.Test;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ParametersUtilsTest {

@Test
public void testGetTaskInput() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("param1", "workflow.input.paramX");
inputParams.put("param2", "task1.output.paramY");

WorkflowDef workflowDef = mock(WorkflowDef.class);
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(Collections.singletonMap("paramX", "valueX"));
workflowModel.setOutput(Collections.singletonMap("paramY", "valueY"));
workflowModel.setTasks(Collections.singletonList(getTaskModel("task1")));
workflowModel.setWorkflowName("workflowName");
when(workflowDef.getInputTemplate()).thenReturn(Collections.singletonMap("paramZ", "valueZ"));

TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(Collections.singletonMap("paramA", "valueA"));

ParametersUtils parametersUtils = new ParametersUtils(mock(ObjectMapper.class));

Map<String, Object> result =
parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "task1");

assertEquals("valueX", result.get("param1"));
assertEquals("valueY", result.get("param2"));
assertEquals("valueZ", result.get("param3"));
assertEquals("valueA", result.get("param4"));
}

@Test
public void testReplace() {
Map<String, Object> inputParams = new HashMap<>();
inputParams.put("param1", "${paramX}");
inputParams.put("param2", "${task1.output.paramY}");

WorkflowDef workflowDef = mock(WorkflowDef.class);
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(Collections.singletonMap("paramX", "valueX"));
workflowModel.setOutput(Collections.singletonMap("paramY", "valueY"));
workflowModel.setTasks(Collections.singletonList(getTaskModel("task1")));
workflowModel.setWorkflowName("workflowName");
when(workflowDef.getInputTemplate()).thenReturn(Collections.singletonMap("paramZ", "valueZ"));

TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(Collections.singletonMap("paramA", "valueA"));

DocumentContext context =
JsonPath.parse("{\"task1\":{\"output\":{\"paramY\":\"taskValueY\"}}}");

ParametersUtils parametersUtils = new ParametersUtils(mock(ObjectMapper.class));

Map<String, Object> result = parametersUtils.replace(inputParams, context, "task1");

assertEquals("valueX", result.get("param1"));
assertEquals("taskValueY", result.get("param2"));
assertEquals("valueZ", result.get("param3"));
assertEquals("valueA", result.get("param4"));
}

@Test
public void testReplaceVariables() {
String paramString = "${paramX}.${task1.output.paramY}";

WorkflowDef workflowDef = mock(WorkflowDef.class);
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(Collections.singletonMap("paramX", "valueX"));
workflowModel.setOutput(Collections.singletonMap("paramY", "valueY"));
workflowModel.setTasks(Collections.singletonList(getTaskModel("task1")));
workflowModel.setWorkflowName("workflowName");
when(workflowDef.getInputTemplate()).thenReturn(Collections.singletonMap("paramZ", "valueZ"));

TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(Collections.singletonMap("paramA", "valueA"));

DocumentContext context =
JsonPath.parse("{\"task1\":{\"output\":{\"paramY\":\"taskValueY\"}}}");

ParametersUtils parametersUtils = new ParametersUtils(mock(ObjectMapper.class));

String result =
parametersUtils.replaceVariables(paramString, context, "task1", 0);

assertEquals("valueX.taskValueY", result);
}

@Test
public void testReplaceVariables_noMatch() {
String paramString = "${paramX}.${task1.output.paramY}";

WorkflowDef workflowDef = mock(WorkflowDef.class);
WorkflowModel workflowModel = new WorkflowModel();
workflowModel.setInput(Collections.singletonMap("paramX", "valueX"));
workflowModel.setOutput(Collections.singletonMap("paramY", "valueY"));
workflowModel.setTasks(Collections.singletonList(getTaskModel("task1")));
workflowModel.setWorkflowName("workflowName");
when(workflowDef.getInputTemplate()).thenReturn(Collections.singletonMap("paramZ", "valueZ"));

TaskDef taskDef = new TaskDef();
taskDef.setInputTemplate(Collections.singletonMap("paramA", "valueA"));

DocumentContext context =
JsonPath.parse("{\"task2\":{\"output\":{\"paramY\":\"taskValueY\"}}}");

ParametersUtils parametersUtils = new ParametersUtils(mock(ObjectMapper.class));

String result =
parametersUtils.replaceVariables(paramString, context, "task1", 0);

assertEquals(paramString, result);
}

private TaskModel getTaskModel(String taskName) {
TaskModel taskModel = mock(TaskModel.class);
when(taskModel.getReferenceTaskName()).thenReturn(taskName);
when(taskModel.getOutputData()).thenReturn(Collections.singletonMap("paramY", "taskValueY"));
return taskModel;
}

}