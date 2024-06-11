package com.example.automaticbean;

import static org.junit.Assert.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConverterRegistry;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyDescriptor;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RunWith(Parameterized.class)
public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private Configuration configuration;

    public AutomaticBeanTest(Configuration config) {
        this.configuration = config;
    }

    @Parameters(name = "{index}: Configuration = {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Configuration()},
                {new Configuration("key1", "value1")},
                {new Configuration("key1", "value1").setChildren(new Configuration("child1"))},
                {new Configuration("key1", "value1").setChildren(new Configuration("child1"), new Configuration("child2"))},
                {new Configuration("key1", "value1").setChildren(new Configuration[] {new Configuration("child1"), new Configuration("child2")})}
        });
    }

    @Before
    public void setup() throws Exception {
        automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);
    }

    @Test
    public void testConfigure() throws Exception {
        // Basic functionality test
        assertNotNull(automaticBean.getConfiguration());

        // Edge case: null Configuration
        try {
            automaticBean.configure(null);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Edge case: empty Configuration
        Configuration emptyConfiguration = new Configuration();
        try {
            automaticBean.configure(emptyConfiguration);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Exception handling: InvocationTargetException
        Configuration invalidValueConfiguration = new Configuration();
        invalidValueConfiguration.setAttribute("attribute", new Object() {});
        try {
            automaticBean.configure(invalidValueConfiguration);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            Throwable cause = e.getCause();
            assertTrue(cause instanceof InvocationTargetException);
        }

        // Exception handling: IllegalAccessException
        Configuration nullValueConfiguration = new Configuration();
        nullValueConfiguration.setAttribute("attribute", null);
        try {
            automaticBean.configure(nullValueConfiguration);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            Throwable cause = e.getCause();
            assertTrue(cause instanceof IllegalAccessException);
        }

        // Exception handling: NoSuchMethodException
        Configuration nonExistentMethodConfiguration = new Configuration();
        nonExistentMethodConfiguration.setName("nonExistentMethod");
        try {
            automaticBean.tryCopyProperty("moduleName", "nonExistentMethod", null, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            assertEquals("Property 'nonExistentMethod' in module moduleName does not exist, please check the documentation", e.getMessage());
        }

        // Edge case: array conversion
        Configuration arrayConfiguration = new Configuration();
        arrayConfiguration.setAttribute("array", new String[]{"value1", "value2"});
        Object result = automaticBean.tryCopyProperty("moduleName", "array", arrayConfiguration.getAttribute("array"), true);
        assertArrayEquals(new String[]{"value1", "value2"}, result);

        // Edge case: map conversion
        Configuration mapConfiguration = new Configuration();
        Map<String, String> attributes = new HashMap<>();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        mapConfiguration.setAttributes(attributes);
        Object resultMap = automaticBean.tryCopyProperty("moduleName", "map", mapConfiguration, true);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("key1", "value1");
        expectedMap.put("key2", "value2");
        assertEquals(expectedMap, resultMap);

        // Edge case: list conversion
        Configuration listConfiguration = new Configuration();
        List<String> list = Arrays.asList("value1", "value2");
        listConfiguration.setAttribute("list", list);
        Object resultList = automaticBean.tryCopyProperty("moduleName", "list", listConfiguration, true);
        List<String> expectedList = Arrays.asList("value1", "value2");
        assertEquals(expectedList, resultList);

        // Edge case: null value
        Configuration nullValue = new Configuration();
        nullValue.setAttribute("nullValue", null);
        try {
            automaticBean.tryCopyProperty("moduleName", "nullValue", nullValue, true);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
            String message = "illegal value 'null' for property 'nullValue' of module moduleName";
            assertEquals(message, e.getMessage());
        }

        // Edge case: empty string value
        Configuration emptyStringValueConfiguration = new Configuration();
        emptyStringValueConfiguration.setAttribute("emptyStringValue", "");
        Object resultEmptyStringValue = automaticBean.tryCopyProperty("moduleName", "emptyStringValue", emptyStringValueConfiguration, true);
        assertEquals("", resultEmptyStringValue);

        // Edge case: empty list conversion
        Configuration emptyListConfiguration = new Configuration();
        emptyListConfiguration.setAttribute("emptyList", (Object) Collections.emptyList());
        Object resultEmptyList = automaticBean.tryCopyProperty("moduleName", "emptyList", emptyListConfiguration, true);
        assertTrue(resultEmptyList instanceof Object[]);
        assertEquals(0, Array.getLength(resultEmptyList));

        // Edge case: empty map conversion
        Configuration emptyMapConfiguration = new Configuration();
        emptyMapConfiguration.setAttribute("emptyMap", (Object) Collections.emptyMap());
        Object resultEmptyMap = automaticBean.tryCopyProperty("moduleName", "emptyMap", emptyMapConfiguration, true);
        assertTrue(resultEmptyMap instanceof Map);
        assertTrue(resultEmptyMap.isEmpty());
    }

    @Test
    public void testContextualize() throws Exception {
        // Basic functionality test
        automaticBean.contextualize(configuration);
        Configuration configAttribute = configuration.getAttribute("attribute");
        Object attributeResult = automaticBean.tryCopyProperty("moduleName", "attribute", configAttribute, false);
        assertEquals(configAttribute.getValue(), attributeResult);

        // Edge case: null Configuration
        Configuration nullConfig = new Configuration();
        try {
            automaticBean.contextualize(nullConfig);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Edge case: empty Configuration
        Configuration emptyConfig = new Configuration();
        try {
            automaticBean.contextualize(emptyConfig);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Edge case: empty attribute
        Configuration emptyAttributeConfiguration = new Configuration();
        emptyAttributeConfiguration.setName("attribute");
        try {
            automaticBean.contextualize(emptyAttributeConfiguration);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Edge case: null value
        Configuration nullValueConfiguration = new Configuration();
        nullValueConfiguration.setAttribute("nullValue", null);
        try {
            automaticBean.contextualize(nullValueConfiguration);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            // Expected
            String message = "illegal value 'null' for property 'nullValue' of module moduleName";
            assertEquals(message, e.getMessage());
        }

        // Edge case: empty string value
        Configuration emptyStringValueConfiguration = new Configuration();
        emptyStringValueConfiguration.setAttribute("emptyStringValue", "");
        try {
            automaticBean.contextualize(emptyStringValueConfiguration);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }
    }

    @Test
    public void testGetConfiguration() {
        // Basic functionality test
        assertEquals(configuration, automaticBean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        // Cannot test directly as it is protected
    }

    @Test
    public void testSetupChild() throws Exception {
        // Basic functionality test
        Configuration childConfiguration = new Configuration();
        automaticBean.setupChild(childConfiguration);
        assertNull(childConfiguration);

        // Edge case: null Configuration
        Configuration nullChildConfiguration = new Configuration();
        try {
            automaticBean.setupChild(nullChildConfiguration);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            String message = "Configuration childConfig is not allowed as a child in " + automaticBean.getClass().getName();
            assertEquals(message, e.getMessage());
        }

        // Exception handling: CheckstyleException
        Configuration failedChildConfiguration = new Configuration();
        failedChildConfiguration.setName("child");
        try {
            automaticBean.setupChild(failedChildConfiguration);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            assertEquals("child is not allowed as a child in " + automaticBean.getClass().getName(), e.getMessage());
        }
    }
}