package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws Exception {
        Configuration config = new Configuration();
        config.addAttribute("booleanAttribute", true);
        config.addAttribute("byteAttribute", (byte) 1);
        config.addAttribute("shortAttribute", (short) 1);
        config.addAttribute("intAttribute", 1);
        config.addAttribute("longAttribute", 1L);
        config.addAttribute("floatAttribute", 1.0f);
        config.addAttribute("doubleAttribute", 1.0);
        config.addAttribute("stringAttribute", "test");
        config.addAttribute("arrayBooleanAttribute", new Boolean[] { true, false });
        config.addAttribute("arrayByteAttribute", new byte[] { 1, 2 });
        config.addAttribute("arrayShortAttribute", new short[] { 1, 2 });
        config.addAttribute("arrayIntAttribute", new int[] { 1, 2 });
        config.addAttribute("arrayLongAttribute", new long[] { 1, 2 });
        config.addAttribute("arrayFloatAttribute", new float[] { 1, 2 });
        config.addAttribute("arrayDoubleAttribute", new double[] { 1, 2 });
        config.addAttribute("arrayStringAttribute", new String[] { "test", "test2" });

        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);

        Object booleanAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "booleanAttribute");
        assertTrue((Boolean) booleanAttribute);

        Object byteAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "byteAttribute");
        assertEquals((Byte) 1, byteAttribute);

        Object shortAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "shortAttribute");
        assertEquals((Short) 1, shortAttribute);

        Object intAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "intAttribute");
        assertEquals(1, intAttribute);

        Object longAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "longAttribute");
        assertEquals(1L, longAttribute);

        Object floatAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "floatAttribute");
        assertEquals(1.0f, floatAttribute, 0.001f);

        Object doubleAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "doubleAttribute");
        assertEquals(1.0, doubleAttribute, 0.001);

        Object stringAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "stringAttribute");
        assertEquals("test", stringAttribute);

        Object[] arrayBooleanAttribute = (Object[]) BeanUtilsBean.getInstance().getPropertyValue(bean, "arrayBooleanAttribute");
        assertArrayEquals(new Boolean[] { true, false }, arrayBooleanAttribute);

        Object[] arrayByteAttribute = (Object[]) BeanUtilsBean.getInstance().getPropertyValue(bean, "arrayByteAttribute");
        assertArrayEquals(new byte[] { 1, 2 }, arrayByteAttribute);

        Object[] arrayShortAttribute = (Object[]) BeanUtilsBean.getInstance().getPropertyValue(bean, "arrayShortAttribute");
        assertArrayEquals(new short[] { 1, 2 }, arrayShortAttribute);

        Object[] arrayIntAttribute = (Object[]) BeanUtilsBean.getInstance().getPropertyValue(bean, "arrayIntAttribute");
        assertArrayEquals(new int[] { 1, 2 }, arrayIntAttribute);

        Object[] arrayLongAttribute = (Object[]) BeanUtilsBean.getInstance().getPropertyValue(bean, "arrayLongAttribute");
        assertArrayEquals(new long[] { 1, 2 }, arrayLongAttribute);

        Object[] arrayFloatAttribute = (Object[]) BeanUtilsBean.getInstance().getPropertyValue(bean, "arrayFloatAttribute");
        assertArrayEquals(new float[] { 1, 2 }, arrayFloatAttribute, 0.001f);

        Object[] arrayDoubleAttribute = (Object[]) BeanUtilsBean.getInstance().getPropertyValue(bean, "arrayDoubleAttribute");
        assertArrayEquals(new double[] { 1, 2 }, arrayDoubleAttribute, 0.001);

        Object[] arrayStringAttribute = (Object[]) BeanUtilsBean.getInstance().getPropertyValue(bean, "arrayStringAttribute");
        assertArrayEquals(new String[] { "test", "test2" }, arrayStringAttribute);
    }

    @Test
    public void testContextualize() throws Exception {
        Configuration config = new Configuration();
        config.putAttribute("booleanAttribute", true);
        config.putAttribute("byteAttribute", (byte) 1);
        config.putAttribute("shortAttribute", (short) 1);
        config.putAttribute("intAttribute", 1);
        config.putAttribute("longAttribute", 1L);
        config.putAttribute("floatAttribute", 1.0f);
        config.putAttribute("doubleAttribute", 1.0);
        config.putAttribute("stringAttribute", "test");

        AutomaticBean bean = new AutomaticBean();
        bean.contextualize(config);

        Object booleanAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "booleanAttribute");
        assertTrue((Boolean) booleanAttribute);

        Object byteAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "byteAttribute");
        assertEquals((Byte) 1, byteAttribute);

        Object shortAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "shortAttribute");
        assertEquals((Short) 1, shortAttribute);

        Object intAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "intAttribute");
        assertEquals(1, intAttribute);

        Object longAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "longAttribute");
        assertEquals(1L, longAttribute);

        Object floatAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "floatAttribute");
        assertEquals(1.0f, floatAttribute, 0.001f);

        Object doubleAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "doubleAttribute");
        assertEquals(1.0, doubleAttribute, 0.001);

        Object stringAttribute = BeanUtilsBean.getInstance().getPropertyValue(bean, "stringAttribute");
        assertEquals("test", stringAttribute);

        PropertyDescriptor desc = PropertyUtils.getPropertyDescriptor(bean, "stringAttribute");
        Converter converter = desc.getConvertor();
        Object convertedStringAttribute = converter.convert(String[].class, Arrays.asList("test3", "test4").toArray(new String[0]));
        assertEquals(Arrays.asList("test3", "test4").toArray(new String[0]), convertedStringAttribute);
    }

    @Test(expected = CheckstyleException.class)
    public void testInvalidConfigure() throws Exception {
        Configuration config = new Configuration();
        config.addAttribute("booleanAttribute", "test");

        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
    }

    @Test(expected = CheckstyleException.class)
    public void testInvalidContextualize() throws Exception {
        Configuration config = new Configuration();
        config.putAttribute("booleanAttribute", "test");

        AutomaticBean bean = new AutomaticBean();
        bean.contextualize(config);
    }

    @Test(expected = CheckstyleException.class)
    public void testInvalidChild() throws Exception {
        Configuration config = new Configuration();
        config.addChild(new Configuration());

        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
    }

    @Test(expected = CheckstyleException.class)
    public void testNullChild() throws Exception {
        Configuration config = new Configuration();
        config.addChild(null);

        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
    }
}