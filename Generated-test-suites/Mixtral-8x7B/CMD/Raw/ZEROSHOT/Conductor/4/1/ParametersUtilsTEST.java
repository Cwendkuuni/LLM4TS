package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Option;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Component
public class ParametersUtilsTest {

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = mock(ObjectMapper.class);
        parametersUtils = new ParametersUtils(objectMapper);
    }

    @Test
    public void testGetTaskInput() {
        // Test data setup
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");

        // Mock dependencies
        when(objectMapper.writeValueAsBytes(inputParams)).thenReturn(new byte[0]);
        when(objectMapper.readValue(new byte[0], new TypeReference<Map<String, Object>>() {
        })).thenReturn(inputParams);

        // Execute method under test
        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        // Verify results
        assertEquals(inputParams, result);
    }

    @Test
    public void testReplace() {
        // Test data setup
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("key1", "value1");
        inputParams.put("key2", "${key1}");
        inputParams.put("key3", "${key4}");
        DocumentContext documentContext = Configuration.defaultConfiguration()
                .addOptions(Option.SUPPRESS_EXCEPTIONS)
                .parse("{\"key4\": \"value4\"}");

        // Execute method under test
        Map<String, Object> result = parametersUtils.replace(inputParams, documentContext, null);

        // Verify results
        assertEquals("value1", result.get("key2"));
        assertEquals("value4", result.get("key3"));
    }

    @Test
    public void testReplaceVariables() {
        // Test data setup
        String paramString = "param${paramPath}";
        DocumentContext documentContext = Configuration.defaultConfiguration()
                .addOptions(Option.SUPPRESS_EXCEPTIONS)
                .parse("{\"paramPath\": \"value\"}");

        // Execute method under test
        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Verify results
        assertEquals("paramvalue", result);
    }

    @Test
    public void testReplaceVariablesWithEnvVar() {
        // Test data setup
        String paramString = "${ENV_VAR}";
        DocumentContext documentContext = Configuration.defaultConfiguration()
                .addOptions(Option.SUPPRESS_EXCEPTIONS)
                .parse("{}");

        // Execute method under test
        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Verify results
        assertEquals(System.getenv("ENV_VAR"), result);
    }

    @Test
    public void testReplaceVariablesWithEmptyEnvVar() {
        // Test data setup
        String paramString = "${ENV_VAR}";
        DocumentContext documentContext = Configuration.defaultConfiguration()
                .addOptions(Option.SUPPRESS_EXCEPTIONS)
                .parse("{}");

        // Execute method under test
        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Verify results
        assertEquals("", result);
    }

    @Test
    public void testReplaceList() {
        // Test data setup
        List<String> values = new LinkedList<>();
        values.add("value1");
        values.add("${value2}");
        DocumentContext documentContext = Configuration.defaultConfiguration()
                .addOptions(Option.SUPPRESS_EXCEPTIONS)
                .parse("{\"value2\": \"value3\"}");

        // Execute method under test
        Object result = parametersUtils.replaceList(values, null, documentContext);

        // Verify results
        assertEquals("value1", ((List<?>) result).get(0));
        assertEquals("value3", ((List<?>) result).get(1));
    }

    @Test
    public void testReplaceVariablesWithEmptyParamPath() {
        // Test data setup
        String paramString = "${  }";
        DocumentContext documentContext = Configuration.defaultConfiguration()
                .addOptions(Option.SUPPRESS_EXCEPTIONS)
                .parse("{}");

        // Execute method under test
        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        // Verify results
        assertEquals("", result);
    }

    @Test
    public void testGetWorkflowInput() {
        // Test data setup
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Collections.singletonMap("key1", "value1"));
        Map<String, Object> inputParams = new HashMap<>();

        // Execute method under test
        Map<String, Object> result =
                parametersUtils.getWorkflowInput(workflowDef, inputParams);

        // Verify results
        assertEquals("value1", result.get("key1"));
    }

    @Test
    public void testGetWorkflowInputWithNullInputParams() {
        // Test data setup
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setInputTemplate(Collections.singletonMap("key1", "value1"));
        Map<String, Object> inputParams = null;

        // Execute method under test
        Map<String, Object> result =
                parametersUtils.getWorkflowInput(workflowDef, inputParams);

        // Verify results
        assertEquals("value1", result.get("key1"));
    }
}