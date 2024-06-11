package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.RelaxedStringArrayConverter;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.properties.PropertiesConfiguration;
import org.junit.Test;
import org.mockito.Mockito;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        final BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        assertSame(BeanUtils.class, beanUtilsBean.getClass().getSuperclass());
        assertSame(PropertyUtilsBean.class, beanUtilsBean.getPropertyUtils().getClass().getSuperclass());

        final Class<?>[] converterTypes = {Boolean.TYPE, Boolean.class, byte[].class, byte.class, char[].class, char.class,
                double[].class, Double.TYPE, Double.class, float[].class, Float.TYPE, Float.class, int[].class, Integer.TYPE,
                Integer.class, long[].class, Long.TYPE, Long.class, short[].class, Short.TYPE, Short.class};

        for (final Class<?> converterType : converterTypes) {
            final Object converter = beanUtilsBean.getConvertUtils().getConverter(converterType);
            assertTrue(converter instanceof AbstractConverter);
        }
    }

    @Test
    public void testConfigurable() throws CheckstyleException, ConfigurationException, IntrospectionException {
        final Configuration configuration = new PropertiesConfiguration();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);

        final List<PropertyDescriptor> propertyDescriptors = Arrays.asList(PropertyUtils.getPropertyDescriptors(automaticBean));
        for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            final String name = propertyDescriptor.getName();
            final Object value = configuration.getProperty(name);
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), name, value, true);
            assertSame(value, propertyDescriptor.getReadMethod().invoke(automaticBean));
        }

        configuration.setProperty("uninitialized", "defaultValue");
        assertNull(AutomaticBean.class.cast(BeanUtils.instantiate(AutomaticBean.class)).getConfiguration().getAttribute("uninitialized"));
    }

    @Test
    public void testContextualizable() throws CheckstyleException {
        final PropertiesConfiguration context = new PropertiesConfiguration();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.contextualize(context);

        final List<PropertyDescriptor> propertyDescriptors = Arrays.asList(PropertyUtils.getPropertyDescriptors(automaticBean));
        for (final PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            final String name = propertyDescriptor.getName();
            final Object value = context.getProperty(name);
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), name, value, false);
            assertSame(value, propertyDescriptor.getReadMethod().invoke(automaticBean));
        }
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        final RelaxedStringArrayConverter converter = new RelaxedStringArrayConverter();
        assertArrayEquals(new String[]{}, converter.convert(String[].class, null));
        assertArrayEquals(new String[]{"foo", "bar"}, converter.convert(String[].class, "foo,bar"));
        assertArrayEquals(new String[]{"foo", "bar"}, converter.convert(String[].class, new String[]{"foo", "bar"}));
    }

    @Test
    public void testCopyProperty_NullValue() {
        final Object target = new Object() {
            private int value = 0;
        };
        final AutomaticBean automaticBean = new AutomaticBean();

        assertNull(automaticBean.tryCopyProperty("moduleName", "key", null, true));
        assertSame(0, ReflectUtils.getFieldValue(target, "value"));
    }

    @Test
    public void testCopyProperty_InvalidProperty() {
        final Object target = new Object() {
            private int value = 0;
        };
        final AutomaticBean automaticBean = new AutomaticBean();

        final String message = assertThrows(CheckstyleException.class, () -> automaticBean.tryCopyProperty("moduleName", "nonExistentProperty", 1, true)).getMessage();
        assertTrue(message.contains("Property 'nonExistentProperty' in module moduleName does not exist"));
        assertSame(0, ReflectUtils.getFieldValue(target, "value"));
    }

    @Test
    public void testCopyProperty_InvalidType() {
        final Object target = new Object() {
            private int value = 0;
        };
        final AutomaticBean automaticBean = new AutomaticBean();

        final ReflectiveOperationException exception = assertThrows(ReflectiveOperationException.class, () -> automaticBean.tryCopyProperty("moduleName", "key", new Object(), true));
        final Method setter = ReflectUtils.findSetter(target.getClass(), "value", int.class);
        final Method getter = ReflectUtils.findGetter(target.getClass(), "value");

        final String actualMessage = exception.getMessage();
        final String expectedMessage = String.format(Locale.ROOT, "Cannot set property '%s' to '%s' in module moduleName", "key", new Object());
        assertTrue(actualMessage.contains(expectedMessage));
        assertSame(0, ReflectUtils.getFieldValue(target, "value"));
    }

    @Test
    public void testCopyProperty_InvalidValue() {
        final Object target = new Object() {
            private int value = 0;
        };
        final AutomaticBean automaticBean = new AutomaticBean();

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> automaticBean.tryCopyProperty("moduleName", "key", new RuntimeException(), true));
        final String actualMessage = exception.getMessage();
        final String expectedMessage = String.format(Locale.ROOT, "illegal value class java.lang.RuntimeException for property 'key' of module moduleName");
        assertTrue(actualMessage.contains(expectedMessage));
        assertSame(0, ReflectUtils.getFieldValue(target, "value"));
    }

    @Test
    public void testCopyProperty_ValidValues() throws CheckstyleException {
        final Object target = new Object() {
            private String value = "";
        };
        final AutomaticBean automaticBean = new AutomaticBean();

        automaticBean.tryCopyProperty("moduleName", "key", "value", true);
        assertSame("value", ReflectUtils.getFieldValue(target, "value"));

        automaticBean.tryCopyProperty("moduleName", "key", 1, true);
        assertSame(1, ReflectUtils.getFieldValue(target, "value"));

        automaticBean.tryCopyProperty("moduleName", "key", true, true);
        assertTrue(ReflectUtils.getFieldValue(target, "value").equals(true));

        automaticBean.tryCopyProperty("moduleName", "key", Arrays.asList("foo", "bar"), true);
        assertTrue(ListUtils.isEqual(Arrays.asList("foo", "bar"), ReflectUtils.getFieldValue(target, "value")));

        automaticBean.tryCopyProperty("moduleName", "key", new byte[]{(byte)0x12}, true);
        assertArrayEquals(new byte[]{(byte)0x12}, (byte[])ReflectUtils.getFieldValue(target, "value"));
    }

    @Test
    public void testInitializeChild() {
        final Configuration childConfig = new PropertiesConfiguration();
        final AutomaticBean parentBean = new AutomaticBean();

        assertThrows(CheckstyleException.class, () -> parentBean.setupChild(childConfig));
    }

    private static class ReflectUtils {
        private static final Method findSetter(Class<?> clazz, String fieldName, Class<?> fieldType) {
            for (final Method method : clazz.getMethods()) {
                if (method.getName().startsWith("set") && method.getParameterTypes().length == 1 &&
                        method.getParameterTypes()[0].equals(fieldType)) {
                    return method;
                }
            }
            return null;
        }

        private static final Method findGetter(Class<?> clazz, String fieldName) {
            for (final Method method : clazz.getMethods()) {
                if (method.getName().startsWith("get") && method.getReturnType().equals(clazz.getDeclaredField(fieldName).getType())) {
                    return method;
                }
            }
            return null;
        }

        private static Object getFieldValue(Object obj, String fieldName) {
            try {
                return ReflectUtils.getFieldValue(obj.getClass(), fieldName, obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        private static Object getFieldValue(Class<?> clazz, String fieldName, Object obj) throws IllegalAccessException {
            final Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        }
    }
}