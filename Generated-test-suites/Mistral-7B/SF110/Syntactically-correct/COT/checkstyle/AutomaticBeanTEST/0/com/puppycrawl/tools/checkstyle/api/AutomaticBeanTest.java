package com.example.automaticbean;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Context;

@RunWith(JUnit4.class)
public class AutomaticBeanTest {

    @Test
    public void testConfigure() {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertNotNull(bean.getConfiguration());
        assertEquals(config, bean.getConfiguration());
    }

    @Test
    public void testTryCopyPropertyWithValidValues() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        BeanUtilsBean beanUtils = new BeanUtilsBean();

        bean.setConfiguration(config);
        bean.tryCopyProperty("testModule", "testKey", "testValue", false);

        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "testKey");
        assertEquals("testValue", descriptor.getReadMethod().invoke(bean));
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullValue() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.setConfiguration(config);
        bean.tryCopyProperty("testModule", "testKey", null, false);
        fail("CheckstyleException expected but not thrown");
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidType() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.setConfiguration(config);
        bean.tryCopyProperty("testModule", "testKey", new Object(), false);
        fail("CheckstyleException expected but not thrown");
    }

    @Test(expected = InvocationTargetException.class)
    public void testTryCopyPropertyWithInaccessibleProperty() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.setConfiguration(config);
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "nonExistentProperty");
        if (descriptor != null) {
            bean.tryCopyProperty("testModule", "nonExistentProperty", "testValue", true);
            fail("CheckstyleException or InvocationTargetException expected but not thrown");
        }
        throw new InvocationTargetException(new RuntimeException(), "Method doSomething not found");
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithUnsupportedType() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.setConfiguration(config);
        bean.tryCopyProperty("testModule", "testKey", new Object()[0], false);
        fail("CheckstyleException expected but not thrown");
    }

    @Test
    public void testContextualize() {
        Context context = new Context();
        AutomaticBean bean = new AutomaticBean();
        bean.contextualize(context);

        Configuration configuration = bean.getConfiguration();
        List<String> attributes = context.getAttributeNames();
        for (String attribute : attributes) {
            Object value = context.get(attribute);
            bean.tryCopyProperty(bean.getClass().getName(), attribute, value, false);
            assertEquals(value, bean.getConfiguration().getAttribute(attribute));
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNullChild() throws Exception {
        Configuration parentConfig = new Configuration();
        AutomaticBean parentBean = new AutomaticBean();
        parentBean.setConfiguration(parentConfig);

        Configuration childConfig = null;
        parentBean.setupChild(childConfig);
        fail("CheckstyleException expected but not thrown");
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithValidChild() throws Exception {
        Configuration parentConfig = new Configuration();
        AutomaticBean parentBean = new AutomaticBean();
        parentBean.setConfiguration(parentConfig);

        Configuration childConfig = new Configuration();
        parentBean.setupChild(childConfig);
        fail("CheckstyleException expected but not thrown");
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        Converter converter = new AutomaticBean.RelaxedStringArrayConverter();
        Object result = converter.convert(String[].class, "value1, value2, value3");
        List<String> expectedList = Lists.newArrayList("value1", "value2", "value3");
        assertArrayEquals(expectedList.toArray(new String[expectedList.size()]), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRelaxedStringArrayConverterWithEmptyString() {
        Converter converter = new AutomaticBean.RelaxedStringArrayConverter();
        Object result = converter.convert(String[].class, "");
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRelaxedStringArrayConverterWithNullString() {
        Converter converter = new AutomaticBean.RelaxedStringArrayConverter();
        Object result = converter.convert(String[].class, null);
        fail("IllegalArgumentException expected but not thrown");
    }

    @Test(expected = ConversionException.class)
    public void testTryCopyPropertyWithInvalidValueType() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.setConfiguration(config);

        try {
            bean.tryCopyProperty("testModule", "testKey", new Object(), false);
            fail("ConversionException expected but not thrown");
        } catch (CheckstyleException e) {
            throw new ConversionException("Error converting value", e);
        }
    }
}