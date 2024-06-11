package com.example.automaticbean;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
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
import org.apache.commons.beanutils.relaxed.RelaxedStringArrayConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticBeanTest {

    @Mock
    private Configuration configuration;

    @InjectMocks
    private AutomaticBean automaticBean;

    @Test
    public void testConfigure() throws Exception {
        final String[] attributeNames = new String[]{"attribute1", "attribute2"};
        when(configuration.getAttributeNames()).thenReturn(attributeNames);

        for (final String key : attributeNames) {
            final Object value = new Object(){};
            automaticBean.configure(configuration);
            verify(configuration, times(2)).getAttribute(key);
            PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
            assertNotNull(descriptor);
        }

        automaticBean.finishLocalSetup();

        final Configuration[] children = new Configuration[0];
        when(configuration.getChildren()).thenReturn(children);

        automaticBean.configure(configuration);
        verify(configuration, times(2)).getChildren();
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidProperty() throws Exception {
        final String attributeName = "invalidProperty";
        when(configuration.getAttributeNames()).thenReturn(new String[0]);
        when(configuration.getAttribute(attributeName)).thenReturn("value");

        automaticBean.configure(configuration);

        fail("Expected CheckstyleException to be thrown");
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureNullConfiguration() throws Exception {
        automaticBean.configure(null);

        fail("Expected CheckstyleException to be thrown");
    }

    @Test
    public void testTryCopyProperty() throws Exception {
        final String key = "property";
        final Object value = new Object(){};

        final BeanUtilsBean beanUtils = new BeanUtilsBean();
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);

        try {
            automaticBean.tryCopyProperty("moduleName", key, value, true);
            assertNotNull(descriptor.getWriteMethod());
            beanUtils.copyProperty(automaticBean, key, value);
        } catch (Throwable e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            automaticBean.tryCopyProperty("moduleName", key, null, true);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            automaticBean.tryCopyProperty("moduleName", null, value, true);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            automaticBean.tryCopyProperty("moduleName", key, value, false);
            fail("Expected ConversionException to be thrown");
        } catch (ConversionException e) {
            // Expected
        }

        try {
            automaticBean.tryCopyProperty("moduleName", key, "invalidValue", false);
            fail("Expected CheckstyleException to be thrown");
        } catch (CheckstyleException e) {
            // Expected
        }
    }

    @Test
    public void testContextualize() throws Exception {
        final String attributeName = "contextAttribute";
        final Object contextValue = new Object(){};
        final Map<String, Object> context = new HashMap<>();
        context.put(attributeName, contextValue);

        automaticBean.contextualize(context);

        verify(configuration, times(1)).getAttribute(attributeName);
        Object propertyValue = automaticBean.getConfiguration().getAttribute(attributeName);
        assertEquals(contextValue, propertyValue);
    }

    @Test
    public void testContextualizeNoAttributes() throws Exception {
        final Map<String, Object> context = new HashMap<>();

        automaticBean.contextualize(context);

        verify(configuration, never()).getAttribute(anyString());
    }

    @Test
    public void testCreateBeanUtilsBean() throws Exception {
        final ConvertUtilsBean cub = new ConvertUtilsBean();
        final List<Converter<?>> converters = new ArrayList<>();

        for (final Class<?> clazz : Arrays.asList(Boolean.TYPE, Boolean.class, Byte.TYPE, Byte.class,
                Character.TYPE, Character.class, Double.TYPE, Double.class, Float.TYPE, Float.class,
                Integer.TYPE, Integer.class, Long.TYPE, Long.class, Short.TYPE, Short.class)) {
            final Converter<?> converter = createConverterForType(clazz);
            converters.add(converter);
            cub.register(converter, clazz);
        }

        final BeanUtilsBean beanUtilsBean = new BeanUtilsBean(cub, new PropertyUtilsBean());
        final List<Converter<?>> beanUtilsBeanConverters = beanUtilsBean.getConvertUtils().getConvertUtils();

        assertEquals(converters.size(), beanUtilsBeanConverters.size());
        for (int i = 0; i < converters.size(); i++) {
            final Converter<?> converter1 = converters.get(i);
            final Converter<?> converter2 = beanUtilsBeanConverters.get(i);
            assertEquals(converter1.getClass(), converter2.getClass());
        }
    }

    private Converter<?> createConverterForType(final Class<?> clazz) {
        if (clazz.isArray()) {
            final Class componentType = clazz.getComponentType();
            return new RelaxedStringArrayConverter();
        }

        final String baseTypeName = clazz.getName().substring(0, clazz.getName().lastIndexOf("."));
        final Class converterType = Class.forName("org.apache.commons.beanutils.converters." + baseTypeName + "Converter");
        return converterType.newInstance();
    }
}