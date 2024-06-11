package com.example.junit;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.google.common.collect.Lists;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        Configuration config = new Configuration();
        bean.configure(config);
        assertNotNull(bean.getConfiguration());
    }

    // Edge Case: passing null Configuration
    @Test(expected = CheckstyleException.class)
    public void testConfigureWithNullConfig() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        bean.configure(null);
    }

    // Exception Scenario: if a property copy fails
    @Test(expected = CheckstyleException.class)
    public void testConfigureWithInvalidPropertyValue() throws Exception {
        Configuration config = new Configuration();
        config.addAttribute("test", "invalid value");
        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
    }

    @Test
    public void testContextualize() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        Context context = new Context();
        context.put("test", "test value");
        bean.contextualize(context);
        Assert.assertEquals("test value", bean.getConfiguration().getAttribute("test"));
    }

    // Edge Case: passing null Context
    @Test(expected = CheckstyleException.class)
    public void testContextualizeWithNullContext() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        bean.contextualize(null);
    }

    // Testing the getConfiguration() method
    @Test
    public void testGetConfiguration() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        Configuration config = new Configuration();
        bean.configure(config);
        Assert.assertEquals(config, bean.getConfiguration());
    }

    // Testing the finishLocalSetup() method
    @Test
    public void testFinishLocalSetup() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        bean.finishLocalSetup();
    }

    // Testing the setupChild() method with a valid Configuration
    @Test
    public void testSetupChildWithValidChildConfig() throws Exception {
        AutomaticBean parentBean = new AutomaticBean();
        Configuration childConfig = new Configuration();
        parentBean.setupChild(childConfig);
    }

    // Testing the setupChild() method with a null Configuration
    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNullChildConfig() throws Exception {
        AutomaticBean parentBean = new AutomaticBean();
        parentBean.setupChild(null);
    }

    // Testing the setupChild() method with an invalid Configuration
    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithInvalidChildConfig() throws Exception {
        AutomaticBean parentBean = new AutomaticBean();
        Configuration childConfig = new Configuration();
        childConfig.setName("testChildConfig");
        parentBean.setupChild(childConfig);
    }
}