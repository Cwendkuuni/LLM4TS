package com.example.test.automaticbean;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Context;

@RunWith(org.junit.runners.BlockJUnit4ClassRunner.class)
public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws Exception {
        Configuration config = Mockito.spy(new Configuration());
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);

        // Check configuration property is set
        assertEquals(config, automaticBean.getConfiguration());

        // Test with null configuration
        try {
            automaticBean.configure(null);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with empty configuration
        Configuration emptyConfig = new Configuration();
        try {
            automaticBean.configure(emptyConfig);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with attributes
        config.addAttribute("attribute1", "value1");
        config.addAttribute("attribute2", "value2");

        automaticBean.configure(config);

        // Check attributes are copied
        assertArrayEquals(new String[]{"value1", "value2"}, automaticBean.getConfiguration().getAttribute("attributeNames"));
    }

    @Test
    public void testTryCopyProperty_boolean() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.tryCopyProperty("moduleName", "booleanProperty", true, false);

        // Test with null moduleName
        try {
            automaticBean.tryCopyProperty(null, "booleanProperty", true, false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with non-existent property
        try {
            automaticBean.tryCopyProperty("moduleName", "nonExistentProperty", true, false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with boolean value
        Object originalValue = automaticBean.getBooleanProperty();
        Boolean newValue = true;
        automaticBean.tryCopyProperty("moduleName", "booleanProperty", newValue, false);
        assertEquals(newValue, automaticBean.getBooleanProperty());
        assertEquals(originalValue, automaticBean.getBooleanPropertyBeforeCopy());
    }

    @Test
    public void testTryCopyProperty_Integer() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.tryCopyProperty("moduleName", "integerProperty", 1, false);

        // Test with null moduleName
        try {
            automaticBean.tryCopyProperty(null, "integerProperty", 1, false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with non-existent property
        try {
            automaticBean.tryCopyProperty("moduleName", "nonExistentProperty", 1, false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with int value
        int originalValue = automaticBean.getIntProperty();
        int newValue = 42;
        automaticBean.tryCopyProperty("moduleName", "integerProperty", newValue, false);
        assertEquals(newValue, automaticBean.getIntProperty());
        assertEquals(originalValue, automaticBean.getIntPropertyBeforeCopy());
    }

    @Test
    public void testTryCopyProperty_String() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.tryCopyProperty("moduleName", "stringProperty", "test", false);

        // Test with null moduleName
        try {
            automaticBean.tryCopyProperty(null, "stringProperty", "test", false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with non-existent property
        try {
            automaticBean.tryCopyProperty("moduleName", "nonExistentProperty", "test", false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with String value
        String originalValue = automaticBean.getStringProperty();
        String newValue = "test2";
        automaticBean.tryCopyProperty("moduleName", "stringProperty", newValue, false);
        assertEquals(newValue, automaticBean.getStringProperty());
        assertEquals(originalValue, automaticBean.getStringPropertyBeforeCopy());
    }

    @Test
    public void testTryCopyProperty_List() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();

        List<String> originalValue = automaticBean.getListProperty();
        List<String> newValue = Arrays.asList("test1", "test2");
        automaticBean.tryCopyProperty("moduleName", "listProperty", newValue, false);
        List<String> copiedValue = automaticBean.getListProperty();

        // Test with null moduleName
        try {
            automaticBean.tryCopyProperty(null, "listProperty", newValue, false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with non-existent property
        try {
            automaticBean.tryCopyProperty("moduleName", "nonExistentProperty", newValue, false);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with empty list value
        List<String> emptyList = Collections.emptyList();
        automaticBean.tryCopyProperty("moduleName", "listProperty", emptyList, false);
        assertEquals(emptyList, automaticBean.getListProperty());

        // Test with list value
        assertTrue(newValue.equals(copiedValue));
    }

    @Test
    public void testContextualize() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();

        Context context = Mockito.spy(new Context());
        context.put("attribute1", "value1");
        context.put("attribute2", "value2");

        automaticBean.contextualize(context);

        // Test with null context
        try {
            automaticBean.contextualize(null);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with empty context
        Context emptyContext = new Context();
        try {
            automaticBean.contextualize(emptyContext);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with attributes
        String attribute1 = "attribute1";
        String attribute2 = "attribute2";
        Object attributeValue1 = "value1";
        Object attributeValue2 = "value2";
        context.put(attribute1, attributeValue1);
        context.put(attribute2, attributeValue2);

        automaticBean.contextualize(context);

        // Check attributes are copied
        assertArrayEquals(new String[]{attributeValue1, attributeValue2}, automaticBean.getConfiguration().getAttribute(new String[]{"attributeNames"}));
    }

    @Test
    public void testSetupChild() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration childConfig = Mockito.spy(new Configuration());

        automaticBean.setupChild(childConfig);

        // Test with null childConfig
        try {
            automaticBean.setupChild(null);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with valid childConfig
        try {
            automaticBean.setupChild(childConfig);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }
    }
}