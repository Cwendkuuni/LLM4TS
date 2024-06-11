package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.junit.Test;

public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtils);
    }

    @Test
    public void testConfigure() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertNotNull(bean.getConfiguration());
        assertEquals(config, bean.getConfiguration());
    }

    @Test
    public void testTryCopyPropertyWithValidValues() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.tryCopyProperty("testModule", "testProperty", "testValue", true);
        assertNotNull(bean.getConfiguration());
        assertEquals("testValue", bean.getConfiguration().getAttribute("testProperty"));
    }

    @Test
    public void testTryCopyPropertyWithNullValue() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.tryCopyProperty("testModule", "testProperty", null, true);
        assertNull(bean.getConfiguration().getAttribute("testProperty"));
    }

    @Test
    public void testTryCopyPropertyWithInvalidProperty() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        assertThrows(CheckstyleException.class, () -> bean.tryCopyProperty("testModule", "nonExistentProperty", "testValue", true));
    }

    @Test
    public void testTryCopyPropertyWithInvalidType() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        final Object invalidValue = new Object() {};
        assertThrows(CheckstyleException.class, () -> bean.tryCopyProperty("testModule", "testProperty", invalidValue, true));
    }

    @Test
    public void testTryCopyPropertyWithInvalidValue() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        assertThrows(InvocationTargetException.class, () -> bean.tryCopyProperty("testModule", "testProperty", "invalidValue", true));
    }

    @Test
    public void testContextualize() throws Exception {
        final Context context = new DefaultContext();
        final AutomaticBean bean = new AutomaticBean();
        bean.contextualize(context);
        final Configuration config = bean.getConfiguration();
        assertNotNull(config);
        final List<String> attributes = context.getAttributeNames();
        for (final String key : attributes) {
            final Object value = context.get(key);
            bean.tryCopyProperty(AutomaticBean.class.getName(), key, value, false);
        }
        assertNotNull(config);
        assertEquals(Collections.singletonMap("testAttribute", "testValue"), config.getAttributes());
    }

    @Test
    public void testGetConfiguration() {
        final AutomaticBean bean = new AutomaticBean();
        assertNull(bean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        assertDoesNotThrow(bean::finishLocalSetup);
    }

    @Test
    public void testSetupChildWithInvalidChild() throws Exception {
        final Configuration childConf = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        assertThrows(CheckstyleException.class, () -> bean.setupChild(childConf));
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();
        final String value = "test1, test2, test3";
        final Object convertedValue = beanUtils.getConverter().convert(String[].class, value);
        assertArrayEquals(new String[]{"test1", "test2", "test3"}, (String[])convertedValue);
    }

    @Test
    public void testConvertProperty() throws Exception {
        final Object source = new Object() {
            String property = "testValue";
        };
        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();
        final String destinationProperty = "testProperty";
        final PropertyDescriptor sourcePropertyDescriptor = PropertyUtils.getPropertyDescriptor(source, "property");
        final PropertyDescriptor destinationPropertyDescriptor = PropertyUtils.getPropertyDescriptor(beanUtils.getConvertedValue(source, String[].class), destinationProperty);
        beanUtils.copyProperty(beanUtils.getConvertedValue(source, String[].class), destinationProperty, sourcePropertyDescriptor.getValue(source));
        final String destinationValue = (String)destinationPropertyDescriptor.getReadMethod().invoke(beanUtils.getConvertedValue(source, String[].class), new Object[0]);
        assertEquals("testValue", destinationValue);
    }
}