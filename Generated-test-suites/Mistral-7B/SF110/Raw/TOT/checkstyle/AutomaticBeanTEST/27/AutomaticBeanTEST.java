package com.example.junit;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {
    private AutomaticBean automaticBean;
    private Configuration configuration;

    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
        configuration = new Configuration();
    }

    @Test
    public void testConfigure() throws Exception {
        automaticBean.configure(configuration);
        assertEquals("testValue", automaticBean.getConfiguration().getAttribute("testAttribute"));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithInvalidAttribute() throws Exception {
        configuration.addAttribute("test", new Object()); // Invalid attribute value
        automaticBean.configure(configuration);
        fail("Should throw CheckstyleException with message: 'illegal value ' for property 'test' of module AutomaticBean'");
    }

    @Test
    public void testContextualize() throws Exception {
        Context context = new Context();
        context.put("testAttribute", "testValue");
        automaticBean.contextualize(context);
        assertEquals("testValue", automaticBean.getConfiguration().getAttribute("testAttribute"));
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeWithInvalidAttribute() throws Exception {
        Context context = new Context();
        context.put("test", new Object()); // Invalid attribute value
        automaticBean.contextualize(context);
        fail("Should throw CheckstyleException with message: 'Cannot set property 'test' to ' in module AutomaticBean'");
    }

    @Test
    public void testTryCopyProperty() throws Exception {
        configuration = new Configuration();
        automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);

        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "testAttribute", "testValue", true);
        assertEquals("testValue", automaticBean.getConfiguration().getAttribute("testAttribute"));
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidProperty() throws Exception {
        configuration = new Configuration();
        automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);

        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "nonExistentProperty", true, true);
            fail("Should throw CheckstyleException with message: 'Property 'nonExistentProperty' in module AutomaticBean does not exist, please check the documentation'");
        } catch (CheckstyleException ex) {
            // Expected
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidType() throws Exception {
        configuration.addAttribute("testAttribute", 42);
        automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);

        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "testAttribute", 42, true);
            fail("Should throw CheckstyleException with message: 'illegal value '42' for property 'testAttribute' of module AutomaticBean'");
        } catch (CheckstyleException ex) {
            // Expected
        }
    }
}