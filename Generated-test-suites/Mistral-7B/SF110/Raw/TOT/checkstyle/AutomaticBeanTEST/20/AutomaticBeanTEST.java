package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AutomaticBean.class, BeanUtils.class, PropertyUtils.class})
public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws Exception {
        Configuration config = new Configuration();
        config.addAttribute("test1", "true");
        config.addAttribute("test2", "123");
        config.addAttribute("test3", "abc,def,ghi");
        config.addAttribute("test4", "test4Value");
        config.addChild(new Configuration());

        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);

        assertEquals("test1Value", bean.getConfiguration().getAttribute("test1"));
        assertEquals(true, bean.getConfiguration().getAttribute("test1").booleanValue());
        assertEquals("123Value", bean.getConfiguration().getAttribute("test2"));
        assertEquals(123, bean.getConfiguration().getAttribute("test2").intValue());
        assertArrayEquals(new String[]{"abc", "def", "ghi"}, (String[])bean.getConfiguration().getAttribute("test3"));
        assertEquals("test4Value", bean.getConfiguration().getAttribute("test4"));
        assertNull(bean.getConfiguration().getChild(0));

        // test recheck
        config.removeAttribute("test1");
        config.addAttribute("test1", "test1Value");
        bean.configure(config);
        assertEquals("test1Value", bean.getConfiguration().getAttribute("test1"));
        assertTrue(bean.getConfiguration().getAttribute("test1").booleanValue());
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidProperty() throws Exception {
        Configuration config = new Configuration();
        config.addAttribute("test", new Object());
        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        fail("Should throw exception");
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureNonExistentProperty() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "nonExistentProperty");
        if (descriptor == null) {
            fail("Should throw exception");
        }
    }

    @Test
    public void testContextualize() throws Exception {
        Configuration config = new Configuration();
        config.putAttribute("test1", "true");
        config.putAttribute("test2", 123);
        config.putAttribute("test3", Arrays.asList("abc", "def", "ghi"));
        config.putAttribute("test4", "test4Value");

        AutomaticBean bean = new AutomaticBean();
        bean.contextualize(config);

        assertEquals("true", bean.getConfiguration().getAttribute("test1"));
        assertEquals(true, bean.getConfiguration().getAttribute("test1").booleanValue());
        assertEquals("123", bean.getConfiguration().getAttribute("test2"));
        assertEquals(123, bean.getConfiguration().getAttribute("test2").intValue());
        assertArrayEquals(new String[]{"abc", "def", "ghi"}, (String[])bean.getConfiguration().getAttribute("test3").toArray(new String[0]));
        assertEquals("test4Value", bean.getConfiguration().getAttribute("test4"));

        // test with null context
        bean = new AutomaticBean();
        bean.contextualize(null);
        assertNull(bean.getConfiguration());

        // test invalid context
        Configuration invalidContext = new Configuration();
        invalidContext.putAttribute("invalid", new Object());
        bean = new AutomaticBean();
        try {
            bean.contextualize(invalidContext);
            fail("Should throw exception");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testGetConfiguration() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertEquals(config, bean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        bean.finishLocalSetup();
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        Configuration childConf = new Configuration();
        bean.setupChild(childConf);
        fail("Should throw exception");
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        AutomaticBean bean = new AutomaticBean();
        Configuration config = new Configuration();
        config.putAttribute("test", "test,test2,test3");
        bean.configure(config);
        Object value = bean.getConfiguration().getAttribute("test");
        List<String> expectedList = Lists.newArrayList("test", "test2", "test3");
        List<String> actualList = (List<String>)ListUtils.cast(value);
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testBeanUtilsCopyProperty() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        Configuration config = new Configuration();
        config.putAttribute("test", "testValue");
        bean.configure(config);
        bean.tryCopyProperty("testBean", "test", "testValue", false);
        Object testValue = bean.getConfiguration().getAttribute("test");
        assertEquals("testValue", testValue);
        String testBeanTestValue = (String)FieldUtils.readField(bean, "testBean", true);
        assertEquals("testValue", testBeanTestValue);
    }
}