package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private Configuration configuration;

    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
        configuration = new Configuration();
        configuration.addAttribute("moduleName", "test");
        configuration.addAttribute("key1", "value1");
        configuration.addAttribute("key2", "value2");
        configuration.addAttribute("key3", "value3");
        configuration.addAttribute("key4", "value4, value5");
        configuration.addChild(createChildConfig());
    }

    @Test
    public void testConfigure() throws Exception {
        automaticBean.configure(configuration);
        assertEquals("test", automaticBean.getConfiguration().getName());
        assertEquals("value1", automaticBean.configuration.getAttribute("key1"));
        assertEquals("value2", automaticBean.configuration.getAttribute("key2"));
        assertEquals(Arrays.asList("value3", "value4", "value5"), Arrays.asList(automaticBean.configuration.getAttribute("key4")));
        assertNull(automaticBean.configuration.getAttribute("nonexistentKey"));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureNonexistentProperty() throws Exception {
        automaticBean.configure(configuration);
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "nonexistentKey", "value", true);
        fail("Should throw CheckstyleException");
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidType() throws Exception {
        configuration.setName("test");
        configuration.addAttribute("key1", new Object());
        automaticBean.configure(configuration);
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "key1", new Object().getClass(), true);
        fail("Should throw CheckstyleException");
    }

    @Test
    public void testContextualize() throws Exception {
        automaticBean.contextualize(configuration);
        assertEquals("test", automaticBean.getConfiguration().getName());
        assertEquals("value1", automaticBean.configuration.getAttribute("key1"));
        assertEquals("value2", automaticBean.configuration.getAttribute("key2"));
        assertEquals(Arrays.asList("value3", "value4", "value5"), Arrays.asList(automaticBean.configuration.getAttribute("key4")));
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeNullContext() throws Exception {
        automaticBean.contextualize(null);
        fail("Should throw CheckstyleException");
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeInvalidType() throws Exception {
        configuration.put("context", new Object());
        automaticBean.contextualize(configuration);
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "nonexistentKey", "value", false);
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "key1", 1, false);
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "key3", new List<String>(){{add("test");}}, false);
        fail("Should throw CheckstyleException");
    }

    private Configuration createChildConfig() {
        Configuration childConfig = new Configuration();
        childConfig.setName("childConfig");
        return childConfig;
    }

}