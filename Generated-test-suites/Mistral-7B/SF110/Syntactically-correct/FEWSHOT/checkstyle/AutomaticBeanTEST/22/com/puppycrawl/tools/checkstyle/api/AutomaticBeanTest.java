package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
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
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticBeanTest {

    private static final String[] EMPTY_STRING_ARRAY = {};

    @Test
    public void testCreateBeanUtilsBean() {
        final BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtilsBean);
    }

    @Test
    public void testGetBeanUtilsBeanConvertedTypes() {
        final BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        assertSame(Boolean.TYPE, beanUtilsBean.getConvertUtils().getConverter(Boolean.TYPE).getClass());
        assertSame(Boolean.class, beanUtilsBean.getConvertUtils().getConverter(Boolean.class).getClass());
        assertSame(boolean[].class,
                beanUtilsBean.getConvertUtils().getConverter(boolean[].class).getClass());
        assertSame(Byte.TYPE, beanUtilsBean.getConvertUtils().getConverter(Byte.TYPE).getClass());
        assertSame(Byte.class, beanUtilsBean.getConvertUtils().getConverter(Byte.class).getClass());
        assertSame(byte[].class, beanUtilsBean.getConvertUtils().getConverter(byte[].class).getClass());
        assertSame(Character.TYPE, beanUtilsBean.getConvertUtils().getConverter(Character.TYPE).getClass());
        assertSame(Character.class, beanUtilsBean.getConvertUtils().getConverter(Character.class).getClass());
        assertSame(char[].class, beanUtilsBean.getConvertUtils().getConverter(char[].class).getClass());
        assertSame(Double.TYPE, beanUtilsBean.getConvertUtils().getConverter(Double.TYPE).getClass());
        assertSame(Double.class, beanUtilsBean.getConvertUtils().getConverter(Double.class).getClass());
        assertSame(double[].class, beanUtilsBean.getConvertUtils().getConverter(double[].class).getClass());
        assertSame(Float.TYPE, beanUtilsBean.getConvertUtils().getConverter(Float.TYPE).getClass());
        assertSame(Float.class, beanUtilsBean.getConvertUtils().getConverter(Float.class).getClass());
        assertSame(float[].class, beanUtilsBean.getConvertUtils().getConverter(float[].class).getClass());
        assertSame(Integer.TYPE, beanUtilsBean.getConvertUtils().getConverter(Integer.TYPE).getClass());
        assertSame(Integer.class, beanUtilsBean.getConvertUtils().getConverter(Integer.class).getClass());
        assertSame(int[].class, beanUtilsBean.getConvertUtils().getConverter(int[].class).getClass());
        assertSame(Long.TYPE, beanUtilsBean.getConvertUtils().getConverter(Long.TYPE).getClass());
        assertSame(Long.class, beanUtilsBean.getConvertUtils().getConverter(Long.class).getClass());
        assertSame(long[].class, beanUtilsBean.getConvertUtils().getConverter(long[].class).getClass());
        assertSame(Short.TYPE, beanUtilsBean.getConvertUtils().getConverter(Short.TYPE).getClass());
        assertSame(Short.class, beanUtilsBean.getConvertUtils().getConverter(Short.class).getClass());
        assertSame(short[].class, beanUtilsBean.getConvertUtils().getConverter(short[].class).getClass());
        assertSame(String[].class, beanUtilsBean.getConvertUtils().getConverter(String[].class).getClass());
    }

    @Test
    public void testConfiguration() {
        final Configuration config = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        assertNotNull(automaticBean.getConfiguration());
    }

    @Test
    public void testConfigurationAttributeNames() {
        final Configuration config = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);

        final String[] attributeNames = config.getAttributeNames();
        assertTrue(attributeNames.length > 0);
        for (final String attributeName : attributeNames) {
            assertFalse(attributeName.isEmpty());
        }
    }

    @Test
    public void testTryCopyPropertyWithNonNullValue() throws Exception {
        final String key = "testKey";
        final String value = "testValue";
        final Object testObject = new Object();
        final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(testObject, key);

        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(new Configuration());
        automaticBean.tryCopyProperty(testObject.getClass().getName(), key, value, true);

        if (propertyDescriptor != null) {
            assertEquals(value, propertyDescriptor.getReadMethod().invoke(testObject));
        }
    }

    @Test
    public void testTryCopyPropertyWithNullValue() throws Exception {
        final String key = "testKey";
        final Object testObject = new Object();

        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(new Configuration());
        automaticBean.tryCopyProperty(testObject.getClass().getName(), key, null, true);

        assertNull(PropertyUtils.getPropertyDescriptor(testObject, key).getReadMethod().invoke(testObject));
    }

    @Test
    public void testTryCopyPropertyWithInvalidType() throws Exception {
        final String key = "testKey";
        final Object testObject = new Object();
        final Configuration config = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();

        automaticBean.configure(config);
        assertThrows(CheckstyleException.class, () -> automaticBean.tryCopyProperty(testObject.getClass().getName(), key, new Object(), true));
    }

    @Test
    public void testTryCopyPropertyWithInvalidTypeAndRecheck() throws Exception {
        final String key = "testKey";
        final Object testObject = new Object();
        final Configuration config = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();

        automaticBean.configure(config);
        assertThrows(CheckstyleException.class, () -> automaticBean.tryCopyProperty(testObject.getClass().getName(), key, new Object(), true));
    }

    @Test
    public void testTryCopyPropertyWithNullConfig() throws Exception {
        final String key = "testKey";
        final Object value = new Object();
        final Object testObject = new Object();
        final AutomaticBean automaticBean = new AutomaticBean();

        automaticBean.tryCopyProperty(testObject.getClass().getName(), key, value, false);
        assertNull(PropertyUtils.getPropertyDescriptor(testObject, key).getReadMethod().invoke(testObject));
    }

    @Test
    public void testTryCopyPropertyWithNullContext() throws Exception {
        final String key = "testKey";
        final Object value = new Object();
        final Object testObject = new Object();
        final AutomaticBean automaticBean = new AutomaticBean();

        automaticBean.contextualize(new Context());
        automaticBean.tryCopyProperty(testObject.getClass().getName(), key, value, false);
        assertNull(PropertyUtils.getPropertyDescriptor(testObject, key).getReadMethod().invoke(testObject));
    }

    @Test
    public void testTryCopyPropertyWithInvalidContextKey() throws Exception {
        final String key = "testKey";
        final Object value = new Object();
        final Object testObject = new Object();
        final AutomaticBean automaticBean = new AutomaticBean();

        automaticBean.contextualize(new Context());
        automaticBean.tryCopyProperty(testObject.getClass().getName(), key, value, false);
        assertNull(PropertyUtils.getPropertyDescriptor(testObject, key).getReadMethod().invoke(testObject));
    }

    @Test
    public void testTryCopyPropertyWithValidContextKey() throws Exception {
        final String key = "testKey";
        final Object value = new Object();
        final Object testObject = new Object();
        final Context context = new Context();
        context.put(key, value);
        final AutomaticBean automaticBean = new AutomaticBean();

        automaticBean.contextualize(context);
        assertSame(value, PropertyUtils.getPropertyDescriptor(testObject, key).getReadMethod().invoke(testObject));
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        assertDoesNotThrow(() -> automaticBean.finishLocalSetup());
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNullChildConf() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.setupChild(null);
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNonEmptyChildConf() throws Exception {
        final Configuration childConf = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.setupChild(childConf);
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        final AutomaticBean automaticBean = new AutomaticBean();
        final String value = "test1, test2, test3";
        final Object convertedValue = automaticBean.getConvertUtils().getConverter(String[].class).convert(String[].class, value);

        assertArrayEquals(new String[]{"test1", "test2", "test3"}, (String[])convertedValue);
    }

    @Test
    public void testRelaxedStringArrayConverterWithEmptyString() {
        final AutomaticBean automaticBean = new AutomaticBean();
        final String value = "";
        final Object convertedValue = automaticBean.getConvertUtils().getConverter(String[].class).convert(String[].class, value);

        assertArrayEquals(EMPTY_STRING_ARRAY, (String[])convertedValue);
    }

    @Test
    public void testRelaxedStringArrayConverterWithNullValue() {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Object convertedValue = automaticBean.getConvertUtils().getConverter(String[].class).convert(String[].class, null);

        assertArrayEquals(EMPTY_STRING_ARRAY, (String[])convertedValue);
    }

    @Test
    public void testRelaxedStringArrayConverterWithEmptyList() {
        final AutomaticBean automaticBean = new AutomaticBean();
        final List<String> list = Collections.emptyList();
        final Object convertedValue = automaticBean.getConvertUtils().getConverter(String[].class).convert(String[].class, list.toArray());

        assertArrayEquals(EMPTY_STRING_ARRAY, (String[])convertedValue);
    }

    @Test
    public void testRelaxedStringArrayConverterWithList() {
        final AutomaticBean automaticBean = new AutomaticBean();
        final List<String> list = Arrays.asList("test1", "test2", "test3");
        final Object convertedValue = automaticBean.getConvertUtils().getConverter(String[].class).convert(String[].class, list.toArray());

        assertArrayEquals(new String[]{"test1", "test2", "test3"}, (String[])convertedValue);
    }
}