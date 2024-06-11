package com.example.junit.tests;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.IllegalAccessException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.collect.Lists;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

@RunWith(Parameterized.class)
public class AutomaticBeanTest {

    private final AutomaticBean autoBean;
    private final Configuration config;
    private final String propertyName;
    private final Object propertyValue;

    public AutomaticBeanTest(Configuration config, String propertyName, Object propertyValue) {
        this.autoBean = new AutomaticBean();
        this.config = config;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    @Parameterized.Parameters(name = "Test config.{0} with property {1} = {2}")
    public static List<Object[]> data() {
        Configuration config = new Configuration();
        config.addAttribute("test.property1", "testValue1");
        config.addAttribute("test.property2", "testValue2");
        config.addAttribute("test.property3", Arrays.asList("testValue3a", "testValue3b"));

        return Arrays.asList(new Object[][] {
                {config, "test.property1", "testValue1"},
                {config, "test.property2", "testValue2"},
                {config, "test.property3", "testValue3a"},
                {config, "test.property3", "testValue3b"},
                {config, "nonExistentProperty", null},
                {config, "nullProperty", null}
        });
    }

    @Test
    public void testConfigure() throws Exception {
        autoBean.configure(config);
        Object expectedValue = null;
        if (propertyValue != null) {
            expectedValue = PropertyUtils.getProperty(autoBean, propertyName);
            assertEquals("Property value should be set correctly", propertyValue, expectedValue);
        }
        else {
            assertNull("Property value should be null", expectedValue);
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_InvalidProperty() throws Exception {
        autoBean.configure(config);
        try {
            PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(autoBean, propertyName);
            fail("Property descriptor should be null for non-existent property");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_InvalidProperty() throws Exception {
        autoBean.tryCopyProperty(AutomaticBean.class.getName(), propertyName, propertyValue, true);
        fail("Invalid property should throw CheckstyleException");
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_IllegalValue() throws Exception {
        autoBean.setConfiguration(new Configuration());
        autoBean.getConfiguration().addAttribute(AutomaticBean.class.getName(), propertyName, new Integer(13));
        autoBean.tryCopyProperty(AutomaticBean.class.getName(), propertyName, "stringValue", true);
        fail("Illegal value should throw CheckstyleException");
    }

    @Test
    public void testTryCopyProperty_ValidProperty() throws Exception {
        autoBean.setConfiguration(new Configuration());
        autoBean.getConfiguration().addAttribute(AutomaticBean.class.getName(), propertyName, propertyValue);
        autoBean.tryCopyProperty(AutomaticBean.class.getName(), propertyName, propertyValue, true);
        Object expectedValue = PropertyUtils.getProperty(autoBean, propertyName);
        assertEquals("Property value should be set correctly", propertyValue, expectedValue);
    }

    @Test
    public void testSetupChild_NullChildConfig() throws Exception {
        autoBean.configure(config);
        autoBean.setupChild(null);
        assertDoesNotThrow("SetupChild should not throw an exception with null childConfig");
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild_InvalidChildConfig() throws Exception {
        autoBean.configure(config);
        Configuration childConfig = new Configuration();
        autoBean.setupChild(childConfig);
        fail("Invalid childConfig should throw CheckstyleException");
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        // No implementation in AutomaticBean. Can't test.
    }

    @Test
    public void testGetConfiguration() throws Exception {
        autoBean.configure(config);
        Configuration expectedConfig = config;
        Configuration actualConfig = autoBean.getConfiguration();
        assertEquals("Configuration should be the same", expectedConfig, actualConfig);
    }
}