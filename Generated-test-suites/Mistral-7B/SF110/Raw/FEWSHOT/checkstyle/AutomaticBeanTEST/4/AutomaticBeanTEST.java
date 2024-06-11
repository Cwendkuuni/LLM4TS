package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.Converters;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.StringArrayConverter;
import org.apache.commons.beanutils.converters.ValueEnumConverter;
import org.apache.commons.beanutils.converters.ValueEnumConverterImpl;
import org.apache.commons.beanutils.converters.ValueStack;
import org.apache.commons.beanutils.converters.WeightedValue;
import org.apache.commons.beanutils.propertyeditors.BooleanArrayPropertyEditor;
import org.apache.commons.beanutils.propertyeditors.ByteArrayPropertyEditor;
import org.apache.commons.beanutils.propertyeditors.CharacterArrayPropertyEditor;
import org.apache.commons.beanutils.propertyeditors.DoubleArrayPropertyEditor;
import org.apache.commons.beanutils.propertyeditors.FloatArrayPropertyEditor;
import org.apache.commons.beanutils.propertyeditors.IntArrayPropertyEditor;
import org.apache.commons.beanutils.propertyeditors.LongArrayPropertyEditor;
import org.apache.commons.beanutils.propertyeditors.ShortArrayPropertyEditor;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;

public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        final BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtilsBean);
        assertNotNull(beanUtilsBean.getBeanUtils());
        assertNotNull(beanUtilsBean.getPropertyUtils());
    }

    @Test
    public void testConfigure() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertNotNull(bean.getConfiguration());
        assertEquals(config, bean.getConfiguration());

        final String[] attributeNames = config.getAttributeNames();
        assertEquals(2, attributeNames.length);
        assertTrue(Arrays.asList(attributeNames).contains("property1"));
        assertTrue(Arrays.asList(attributeNames).contains("property2"));

        final Object value1 = "true";
        config.setProperty("property1", value1);
        bean.configure(config);
        final Boolean result1 = (Boolean) bean.getClass().getDeclaredField("property1").get(bean);
        assertTrue(result1);

        final Object value2 = new Boolean(false);
        config.setProperty("property2", value2);
        bean.configure(config);
        final Boolean result2 = (Boolean) bean.getClass().getDeclaredField("property2").get(bean);
        assertFalse(result2);

        final String message = "Property 'property3' in module AutomaticBean does not exist, please check the documentation";
        try {
            config.setProperty("property3", "value3");
            bean.configure(config);
            fail(message);
        } catch (CheckstyleException e) {
            assertEquals(message, e.getMessage());
        }
    }

    @Test
    public void testTryCopyProperty() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();

        final String setterName = "setProperty1";
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "property1");
        assertEquals("property1", descriptor.getName());
        final Method setter = descriptor.getWriteMethod();
        assertEquals(setterName, setter.getName());
        final Class<?>[] parameterTypes = setter.getParameterTypes();
        assertEquals(1, parameterTypes.length);
        assertEquals(String.class, parameterTypes[0]);

        final String value1 = "value1";
        config.setProperty("property1", value1);
        bean.configure(config);
        bean.tryCopyProperty(AutomaticBean.class.getName(), "property1", value1, true);
        final Object result1 = bean.getClass().getDeclaredField("property1").get(bean);
        assertEquals(value1, result1);

        final String message = "illegal value 'value2' for property 'property1' of module AutomaticBean";
        config.setProperty("property1", "value2");
        bean.configure(config);
        try {
            bean.tryCopyProperty(AutomaticBean.class.getName(), "property1", "value2", true);
            fail(message);
        } catch (CheckstyleException e) {
            assertEquals(message, e.getMessage());
        }

        final String setterName2 = "setProperty2";
        final PropertyDescriptor descriptor2 = PropertyUtils.getPropertyDescriptor(bean, "property2");
        assertEquals("property2", descriptor2.getName());
        final Method setter2 = descriptor2.getWriteMethod();
        assertEquals(setterName2, setter2.getName());
        final Class<?>[] parameterTypes2 = setter2.getParameterTypes();
        assertEquals(1, parameterTypes2.length);
        assertEquals(Integer.TYPE, parameterTypes2[0]);

        final int value3 = 42;
        config.setProperty("property2", value3);
        bean.configure(config);
        bean.tryCopyProperty(AutomaticBean.class.getName(), "property2", value3, true);
        final Integer result3 = (Integer) bean.getClass().getDeclaredField("property2").get(bean);
        assertEquals(value3, result3.intValue());

        final int value4 = 43;
        config.setProperty("property2", value4);
        bean.configure(config);
        try {
            bean.tryCopyProperty(AutomaticBean.class.getName(), "property2", value4, true);
            fail("expecting CheckstyleException for illegal value");
        } catch (CheckstyleException e) {
            // expected
        }

        final Boolean value5 = Boolean.FALSE;
        config.setProperty("property2", value5);
        bean.configure(config);
        bean.tryCopyProperty(AutomaticBean.class.getName(), "property2", value5, false);
        final Boolean result5 = (Boolean) bean.getClass().getDeclaredField("property2").get(bean);
        assertFalse(result5);

        final Boolean value6 = Boolean.TRUE;
        config.setProperty("property2", value6);
        bean.configure(config);
        bean.tryCopyProperty(AutomaticBean.class.getName(), "property2", value6, false);
        final Boolean result6 = (Boolean) bean.getClass().getDeclaredField("property2").get(bean);
        assertTrue(result6);

        final Integer value7 = null;
        config.setProperty("property2", value7);
        bean.configure(config);
        try {
            bean.tryCopyProperty(AutomaticBean.class.getName(), "property2", value7, false);
            fail("expecting NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testContextualize() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Properties context = new Properties();
        context.setProperty("property1", "value1");
        bean.contextualize(context);
        final Boolean result1 = (Boolean) bean.getClass().getDeclaredField("property1").get(bean);
        assertTrue(result1);

        final Properties context2 = new Properties();
        context2.setProperty("property2", "value2");
        bean.contextualize(context2);
        final Boolean result2 = (Boolean) bean.getClass().getDeclaredField("property2").get(bean);
        assertFalse(result2);

        final Properties context3 = new Properties();
        context3.setProperty("property3", "value3");
        bean.contextualize(context3);
        final String message = "Property 'property3' in module AutomaticBean does not exist, please check the documentation";
        try {
            final Boolean result3 = (Boolean) bean.getClass().getDeclaredField("property3").get(bean);
            fail(message);
        } catch (CheckstyleException e) {
            assertEquals(message, e.getMessage());
        }

        final Properties context4 = new Properties();
        context4.setProperty("property1", "value4");
        bean.contextualize(context4);
        final Boolean result4 = (Boolean) bean.getClass().getDeclaredField("property1").get(bean);
        assertTrue(result4);
    }

    @Test
    public void testGetConfiguration() {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertNotNull(bean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Configuration config = new Configuration();
        bean.configure(config);
        bean.finishLocalSetup();
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild() throws Exception {
        final Configuration childConf = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.setupChild(childConf);
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        final AutomaticBean bean = new AutomaticBean();
        final BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        final ConvertUtils converters = beanUtilsBean.getConvertUtils();
        converters.register(new AutomaticBean.RelaxedStringArrayConverter(), String[].class);

        final Object value = "value1,value2,value3";
        final String[] result = (String[]) beanUtilsBean.convert(String[].class, value);
        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
        assertEquals("value3", result[2]);

        final Object value2 = "value1, value2, value3";
        final String[] result2 = (String[]) beanUtilsBean.convert(String[].class, value2);
        assertEquals(3, result2.length);
        assertEquals("value1", result2[0]);
        assertEquals("value2", result2[1]);
        assertEquals("value3", result2[2]);

        final Object value3 = "value1,value2, value3, value4";
        final String[] result3 = (String[]) beanUtilsBean.convert(String[].class, value3);
        assertEquals(4, result3.length);
        assertEquals("value1", result3[0]);
        assertEquals("value2", result3[1]);
        assertEquals("value3", result3[2]);
        assertEquals("value4", result3[3]);

        final Object value4 = "";
        final String[] result4 = (String[]) beanUtilsBean.convert(String[].class, value4);
        assertEquals(0, result4.length);

        final Object value5 = null;
        final String[] result5 = (String[]) beanUtilsBean.convert(String[].class, value5);
        assertNull(result5);
    }
}