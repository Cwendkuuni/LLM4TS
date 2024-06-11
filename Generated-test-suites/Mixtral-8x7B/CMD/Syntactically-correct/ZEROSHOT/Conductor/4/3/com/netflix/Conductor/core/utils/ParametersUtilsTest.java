package com.netflix.conductor.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

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

    private ParametersUtils parametersUtils;
    private ObjectMapper objectMapper;
    private Logger logger;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = mock(ObjectMapper.class);
        logger = mock(Logger.class);
        parametersUtils = new ParametersUtils(objectMapper) {
            @Override
            LoggerFactory getLogger() {
                return () -> logger;
            }
        };
    }

    @Test
    public void testGetTaskInput() {
        WorkflowModel workflowModel = new WorkflowModel();
        TaskDef taskDef = new TaskDef();
        Map<String, Object> inputParams = new HashMap<>();

        when(objectMapper.readValue(objectMapper.writeValueAsString(taskDef.getInputTemplate()),
                new TypeReference<Map<String, Object>>() {
                })).thenReturn(Collections.singletonMap("param1", "value1"));

        Map<String, Object> result =
                parametersUtils.getTaskInput(inputParams, workflowModel, taskDef, "taskId");

        assertEquals(1, result.size());
        assertEquals("value1", result.get("param1"));
    }

    @Test
    public void testReplace() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "${param2}");
        inputParams.put("param2", "${param3}");
        inputParams.put("param3", "value3");

        DocumentContext documentContext = JsonPath.parse("{}");

        Map<String, Object> result = parametersUtils.replace(inputParams, documentContext, null);

        assertEquals(1, result.size());
        assertEquals("value3", result.get("param1"));
    }

    @Test
    public void testReplaceWithEnvVariable() {
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "${ENV_VARIABLE}");

        DocumentContext documentContext = JsonPath.parse("{}");

        when(EnvUtils.getSystemParametersValue("ENV_VARIABLE", null)).thenReturn("envValue");

        Map<String, Object> result = parametersUtils.replace(inputParams, documentContext, null);

        assertEquals(1, result.size());
        assertEquals("envValue", result.get("param1"));
    }

    @Test
    public void testReplaceVariables() {
        String paramString = "test ${param1} ${param2}";
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "value1");
        inputParams.put("param2", "${param3}");
        inputParams.put("param3", "value3");

        DocumentContext documentContext = JsonPath.parse("{}");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("test value1 value3", result);
    }

    @Test
    public void testReplaceVariablesWithEnvVariable() {
        String paramString = "test ${ENV_VARIABLE}";

        DocumentContext documentContext = JsonPath.parse("{}");

        when(EnvUtils.getSystemParametersValue("ENV_VARIABLE", null)).thenReturn("envValue");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("test envValue", result);
    }

    @Test
    public void testReplaceVariablesWithNullValue() {
        String paramString = "test ${param1}";
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", null);

        DocumentContext documentContext = JsonPath.parse("{}");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("test ", result);
    }

    @Test
    public void testReplaceVariablesWithEmptyString() {
        String paramString = "test ${param1}";
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("param1", "");

        DocumentContext documentContext = JsonPath.parse("{}");

        String result = parametersUtils.replaceVariables(paramString, documentContext, null);

        assertEquals("test ", result);
    }
}