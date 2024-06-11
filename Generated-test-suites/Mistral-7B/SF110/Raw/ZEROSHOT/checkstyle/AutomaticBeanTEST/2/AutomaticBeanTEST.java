package com.example.automaticbean;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
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
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticBeanTest {

    @InjectMocks
    private AutomaticBean automaticBean;

    @Mock
    private Configuration mockConfiguration;

    @Mock
    private Context mockContext;

    private ConvertUtilsBean convertUtilsBean;
    private BeanUtilsBean beanUtilsBean;
    private PropertyUtils propertyUtils;

    @Before
    public void setUp() {
        convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(new BooleanConverter(), Boolean.TYPE);
        convertUtilsBean.register(new BooleanConverter(), Boolean.class);
        convertUtilsBean.register(new ArrayConverter(boolean[].class, new BooleanConverter()), boolean[].class);
        convertUtilsBean.register(new ByteConverter(), Byte.TYPE);
        convertUtilsBean.register(new ByteConverter(), Byte.class);
        convertUtilsBean.register(new ArrayConverter(byte[].class, new ByteConverter()), byte[].class);
        convertUtilsBean.register(new CharacterConverter(), Character.TYPE);
        convertUtilsBean.register(new CharacterConverter(), Character.class);
        convertUtilsBean.register(new ArrayConverter(char[].class, new CharacterConverter()), char[].class);
        convertUtilsBean.register(new DoubleConverter(), Double.TYPE);
        convertUtilsBean.register(new DoubleConverter(), Double.class);
        convertUtilsBean.register(new ArrayConverter(double[].class, new DoubleConverter()), double[].class);
        convertUtilsBean.register(new FloatConverter(), Float.TYPE);
        convertUtilsBean.register(new FloatConverter(), Float.class);
        convertUtilsBean.register(new ArrayConverter(float[].class, new FloatConverter()), float[].class);
        convertUtilsBean.register(new IntegerConverter(), Integer.TYPE);
        convertUtilsBean.register(new IntegerConverter(), Integer.class);
        convertUtilsBean.register(new ArrayConverter(int[].class, new IntegerConverter()), int[].class);
        convertUtilsBean.register(new LongConverter(), Long.TYPE);
        convertUtilsBean.register(new LongConverter(), Long.class);
        convertUtilsBean.register(new ArrayConverter(long[].class, new LongConverter()), long[].class);
        convertUtilsBean.register(new ShortConverter(), Short.TYPE);
        convertUtilsBean.register(new ShortConverter(), Short.class);
        convertUtilsBean.register(new ArrayConverter(short[].class, new ShortConverter()), short[].class);
        convertUtilsBean.register(new RelaxedStringArrayConverter(), String[].class);

        beanUtilsBean = new BeanUtilsBean(convertUtilsBean, propertyUtils = PropertyUtils.getDefaultPropertyUtils());
    }

    @Test
    public void testConfigure() throws Exception {
        automaticBean.configure(mockConfiguration);

        final String expectedConfigurationName = mockConfiguration.getName();
        assertEquals(expectedConfigurationName, automaticBean.getConfiguration().getName());

        final String[] attributeNames = mockConfiguration.getAttributeNames();
        for (final String key : attributeNames) {
            final Object value = mockConfiguration.getAttribute(key);
            automaticBean.tryCopyProperty(expectedConfigurationName, key, value, true);

            final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
            assertNotNull(descriptor);
            final Object actualValue = descriptor.getReadMethod().invoke(automaticBean);
            assertEquals(value, actualValue);
        }
    }

    @Test
    public void testContextualize() throws Exception {
        automaticBean.contextualize(mockContext);

        final Collection<String> attributes = mockContext.getAttributeNames();
        for (final String key : attributes) {
            final Object value = mockContext.get(key);
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), key, value, false);

            final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
            assertNotNull(descriptor);
            final Object actualValue = descriptor.getReadMethod().invoke(automaticBean);
            assertEquals(value, actualValue);
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_NullChildConfiguration() throws Exception {
        Mockito.when(mockConfiguration.getChildren()).thenReturn(new Configuration[0]);

        automaticBean.configure(mockConfiguration);

        automaticBean.setupChild(Mockito.mock(Configuration.class));

        fail("Expected CheckstyleException");
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_NonExistentProperty() throws Exception {
        automaticBean.configure(mockConfiguration);

        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "nonExistentProperty", null, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_InvalidValue() throws Exception {
        automaticBean.configure(mockConfiguration);

        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "propertyName", new Object() {}, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_InvalidType() throws Exception {
        automaticBean.configure(mockConfiguration);

        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "propertyName", 42, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTryCopyProperty_NullValue() throws Exception {
        automaticBean.configure(mockConfiguration);

        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "propertyName", null, true);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = ConversionException.class)
    public void testTryCopyProperty_InvalidStringArrayValue() throws Exception {
        automaticBean.configure(mockConfiguration);

        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "propertyName", Arrays.asList("abc,def,123").toArray(new String[0]), true);
            fail("Expected ConversionException");
        } catch (ConversionException e) {
            // expected
        }
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        automaticBean.configure(mockConfiguration);
        automaticBean.finishLocalSetup();

        // no exception
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild_NullChildConfiguration() throws Exception {
        automaticBean.configure(mockConfiguration);

        try {
            automaticBean.setupChild(Mockito.mock(Configuration.class));
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // expected
        }
    }

    @Test
    public void testGetConfiguration() throws Exception {
        automaticBean.configure(mockConfiguration);

        final Configuration expectedConfiguration = automaticBean.getConfiguration();
        assertEquals(mockConfiguration, expectedConfiguration);
    }
}