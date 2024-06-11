package com.example.junit.test;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.RelaxedStringArrayConverter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws Exception {
        Configuration config = new Configuration();
        config.addAttribute("testKey1", "testValue1");
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);

        Object testValue1 = automaticBean.getConfiguration().getAttribute("testKey1");
        assertEquals("testValue1", testValue1);
    }

    @Test
    public void testConfigure_noAttribute() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);

        assertNull(automaticBean.getConfiguration().getAttribute("nonExistentKey"));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_propertyDoesNotExist() throws Exception {
        Configuration config = new Configuration();
        config.addAttribute("nonExistentProperty", new Object());
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
    }

    @Test
    public void testConfigure_children() throws Exception {
        Configuration parentConfig = new Configuration();
        parentConfig.setName("parent");
        Configuration childConfig = new Configuration();
        childConfig.setName("child");
        parentConfig.addChild(childConfig);

        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(parentConfig);

        assertNotNull(automaticBean.getConfiguration().getChild(0));
        assertEquals("child", automaticBean.getConfiguration().getChild(0).getName());
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_childIsNotAllowed() throws Exception {
        Configuration config = new Configuration();
        config.setName("notAllowedChild");
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
    }

    @Test
    public void testTryCopyProperty_recheck() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        BeanUtilsBean beanUtils = new BeanUtilsBean();

        try {
            automaticBean.tryCopyProperty("testModule", "testKey", "testValue", true);
            PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testKey");
            assertNotNull(descriptor);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_propertyDoesNotExist() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        BeanUtilsBean beanUtils = new BeanUtilsBean();

        try {
            automaticBean.tryCopyProperty("testModule", "nonExistentProperty", new Object(), true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_wrongType() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        BeanUtilsBean beanUtils = new BeanUtilsBean();

        try {
            automaticBean.tryCopyProperty("testModule", "testKey", 1, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test(expected = InvocationTargetException.class)
    public void testTryCopyProperty_targetIllegal() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        BeanUtilsBean beanUtils = new BeanUtilsBean();

        try {
            automaticBean.tryCopyProperty("testModule", "testKey", new Object(), true);
            fail("Expected InvocationTargetException");
        } catch (InvocationTargetException e) {
            // expected
        }
    }

    @Test(expected = IllegalAccessException.class)
    public void testTryCopyProperty_sourceIllegal() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        BeanUtilsBean beanUtils = new BeanUtilsBean();

        try {
            Method method = ReflectUtils.getMethod(AutomaticBean.class, "tryCopyProperty", String.class, String.class, Object.class, boolean.class);
            method.setAccessible(true);
            method.invoke(automaticBean, "testModule", "testKey", new Object(), true);
            fail("Expected IllegalAccessException");
        } catch (IllegalAccessException e) {
            // expected
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void testTryCopyProperty_noSuchMethod() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        BeanUtilsBean beanUtils = new BeanUtilsBean();

        try {
            Method method = ReflectUtils.getMethod(AutomaticBean.class, "tryCopyProperty", String.class, String.class, int.class, boolean.class);
            method.invoke(automaticBean, "testModule", "testKey", 1, true);
            fail("Expected NoSuchMethodException");
        } catch (NoSuchMethodException e) {
            // expected
        }
    }

    @Test
    public void testTryCopyProperty_success() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        BeanUtilsBean beanUtils = new BeanUtilsBean();

        automaticBean.tryCopyProperty("testModule", "testKey", "testValue", false);

        Object testValue = PropertyUtils.getProperty(automaticBean, "testKey");
        assertEquals("testValue", testValue);
    }

    @Test
    public void testContextualize() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = new Context();
        context.put("testKey", "testValue");

        automaticBean.contextualize(context);

        Object testValue = PropertyUtils.getProperty(automaticBean, "testKey");
        assertEquals("testValue", testValue);
    }

    @Test
    public void testContextualize_noAttribute() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = new Context();

        automaticBean.contextualize(context);

        assertNull(PropertyUtils.getProperty(automaticBean, "nonExistentKey"));
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.finishLocalSetup();
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild_nullChild() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration childConfig = null;

        try {
            automaticBean.setupChild(childConfig);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        AutomaticBean automaticBean = new AutomaticBean();
        String input = "test1, test2, test3";
        Object output = automaticBean.tryCopyProperty("testModule", "testKey", input, true);

        assertEquals(Arrays.asList("test1", "test2", "test3"), Arrays.asList((String[]) output));
    }

}