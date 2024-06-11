package com.example.automaticbean; // replace with your package name

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Assert;
import org.junit.Test;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new Configuration();
        automaticBean.configure(configuration);
        Assert.assertNotNull(automaticBean.getConfiguration());
    }

    @Test
    public void testTryCopyPropertyWithValidProperty() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new Configuration();
        configuration.setProperty("testProperty", "testValue");
        automaticBean.configure(configuration);
        Object value = BeanUtils.getProperty(automaticBean, "testProperty");
        Assert.assertEquals("testValue", value);
    }

    @Test
    public void testTryCopyPropertyWithInvalidProperty() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new Configuration();
        try {
            automaticBean.tryCopyProperty("testModule", "invalidProperty", null, true);
            Assert.fail();
        } catch (CheckstyleException e) {
            String expectedMessage = "Property 'invalidProperty' in module testModule does not exist, please check the documentation";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testTryCopyPropertyWithInvalidValueType() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new Configuration();
        try {
            automaticBean.tryCopyProperty("testModule", "testProperty", new Object() {}, true);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            String expectedMessage = "illegal value class org.junit.Assert$1 for property testProperty of module testModule";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testTryCopyPropertyWithInvalidValue() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new Configuration();
        try {
            automaticBean.tryCopyProperty("testModule", "testProperty", "invalidValue", true);
            Assert.fail();
        } catch (CheckstyleException e) {
            String expectedMessage = "illegal value 'invalidValue' for property testProperty of module testModule";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testContextualize() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = new Context();
        context.put("testProperty", "testValue");
        automaticBean.contextualize(context);
        Object value = BeanUtils.getProperty(automaticBean, "testProperty");
        Assert.assertEquals("testValue", value);
    }

    @Test
    public void testTryCopyPropertyWithNullContext() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = null;
        try {
            automaticBean.contextualize(context);
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testGetConfiguration() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new Configuration();
        automaticBean.configure(configuration);
        Assert.assertEquals(configuration, automaticBean.getConfiguration());
    }

    @Test(expected = CheckstyleException.class)
    public void testFinishLocalSetup() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.finishLocalSetup();
        throw new AssertionError("finishLocalSetup should throw CheckstyleException");
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithValidChild() throws Exception {
        AutomaticBean parentBean = new AutomaticBean();
        Configuration childConfig = new Configuration();
        parentBean.setupChild(childConfig);
        Assert.fail();
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithInvalidChild() throws Exception {
        AutomaticBean parentBean = new AutomaticBean();
        Object invalidChild = new Object();
        parentBean.setupChild(invalidChild);
        Assert.fail();
    }
}