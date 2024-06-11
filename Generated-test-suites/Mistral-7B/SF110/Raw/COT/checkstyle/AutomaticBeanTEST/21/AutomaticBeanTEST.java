package com.example.automaticbean;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        assertNotNull(automaticBean.getConfiguration());
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithNullConfig() {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(null);
    }

    @Test
    public void testTryCopyPropertyWithExistingProperty() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        String key = "testProperty";
        String value = "testValue";
        Configuration config = new Configuration();
        config.setProperty(key, value);
        automaticBean.configure(config);
        Object actualValue = BeanUtils.getProperty(automaticBean, key);
        assertEquals(value, actualValue);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNonExistingProperty() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        String key = "nonExistingProperty";
        String value = "testValue";
        Configuration config = new Configuration();
        automaticBean.configure(config);
        try {
            automaticBean.tryCopyProperty("testModule", key, value, true);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | CheckstyleException e) {
            throw new AssertionError("Unexpected exception", e);
        }
        fail("Expected CheckstyleException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTryCopyPropertyWithInvalidType() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        String key = "testProperty";
        List<Integer> value = Arrays.asList(1, 2, 3);
        Configuration config = new Configuration();
        automaticBean.configure(config);
        try {
            automaticBean.tryCopyProperty("testModule", key, value, true);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | CheckstyleException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test(expected = ConversionException.class)
    public void testTryCopyPropertyWithInvalidValue() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        String key = "testProperty";
        int value = 42;
        Configuration config = new Configuration();
        automaticBean.configure(config);
        try {
            automaticBean.tryCopyProperty("testModule", key, value, true);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | CheckstyleException e) {
            throw new AssertionError("Unexpected exception", e);
        }
    }

    @Test
    public void testContextualize() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = new Context();
        context.put("testProperty", "testValue");
        automaticBean.contextualize(context);
        Object actualValue = BeanUtils.getProperty(automaticBean, "testProperty");
        assertEquals("testValue", actualValue);
    }

    @Test
    public void testContextualizeWithEmptyContext() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = new Context();
        automaticBean.contextualize(context);
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNullChildConfig() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration childConfig = null;
        try {
            automaticBean.setupChild(childConfig);
        } catch (CheckstyleException e) {
            // Expected
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNonEmptyChildConfig() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration childConfig = new Configuration();
        try {
            automaticBean.setupChild(childConfig);
        } catch (CheckstyleException e) {
            // Expected
        }
    }

}