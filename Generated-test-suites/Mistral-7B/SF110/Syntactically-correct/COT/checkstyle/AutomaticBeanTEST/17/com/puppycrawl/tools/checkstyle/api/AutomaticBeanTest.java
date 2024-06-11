package com.example.automaticbean;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.InvocationTargetException;
import org.apache.commons.beanutils.NoSuchMethodException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticBeanTest {

    private BeanUtilsBean beanUtils;
    private ConvertUtilsBean convertUtils;
    private AutomaticBean automaticBean;

    @Test
    public void testConfigure() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        automaticBean = new AutomaticBean();

        convertUtils = new ConvertUtilsBean();
        beanUtils = new BeanUtilsBean(convertUtils, PropertyUtils.getDefaultPropertyUtils());

        Mockito.when(config.getAttributeNames()).thenReturn(new String[]{"attribute1", "attribute2"});
        automaticBean.configure(config);

        assertEquals(config, automaticBean.getConfiguration());
        assertEquals("attribute1", automaticBean.getConfiguration().getAttribute("attribute1"));
        assertEquals("attribute2", automaticBean.getConfiguration().getAttribute("attribute2"));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithInvalidAttributeName() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        automaticBean = new AutomaticBean();

        convertUtils = new ConvertUtilsBean();
        beanUtils = new BeanUtilsBean(convertUtils, PropertyUtils.getDefaultPropertyUtils());

        Mockito.when(config.getAttributeNames()).thenReturn(new String[]{"invalidAttributeName"});
        automaticBean.configure(config);

        fail("Should throw CheckstyleException when invalid attribute name is provided.");
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithNullConfiguration() throws Exception {
        automaticBean = new AutomaticBean();
        automaticBean.configure(null);

        fail("Should throw CheckstyleException when configuration is null.");
    }

    @Test
    public void testTryCopyPropertyWithExistingProperty() throws Exception {
        automaticBean = new AutomaticBean();
        Configuration config = Mockito.mock(Configuration.class);

        convertUtils = new ConvertUtilsBean();
        beanUtils = new BeanUtilsBean(convertUtils, PropertyUtils.getDefaultPropertyUtils());

        Mockito.when(config.getAttributeNames()).thenReturn(new String[]{"propertyName"});
        Mockito.when(config.getAttribute("propertyName")).thenReturn("value");

        automaticBean.configure(config);

        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "propertyName");
        Object propertyValue = descriptor.getReadMethod().invoke(automaticBean);

        assertEquals("value", propertyValue);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullPropertyName() throws Exception {
        automaticBean = new AutomaticBean();
        Configuration config = Mockito.mock(Configuration.class);

        convertUtils = new ConvertUtilsBean();
        beanUtils = new BeanUtilsBean(convertUtils, PropertyUtils.getDefaultPropertyUtils());

        Mockito.when(config.getAttributeNames()).thenReturn(new String[0]);
        automaticBean.configure(config);

        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), null, "value", true);
            fail("Should throw CheckstyleException when property name is null.");
        } catch (CheckstyleException ex) {
            // Expected exception
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullValue() throws Exception {
        automaticBean = new AutomaticBean();
        Configuration config = Mockito.mock(Configuration.class);

        convertUtils = new ConvertUtilsBean();
        beanUtils = new BeanUtilsBean(convertUtils, PropertyUtils.getDefaultPropertyUtils());

        Mockito.when(config.getAttributeNames()).thenReturn(new String[]{"propertyName"});
        automaticBean.configure(config);

        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "propertyName", null, true);
            fail("Should throw CheckstyleException when value is null.");
        } catch (CheckstyleException ex) {
            // Expected exception
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidType() throws Exception {
        automaticBean = new AutomaticBean();
        Configuration config = Mockito.mock(Configuration.class);

        convertUtils = new ConvertUtilsBean();
        beanUtils = new BeanUtilsBean(convertUtils, PropertyUtils.getDefaultPropertyUtils());

        Mockito.when(config.getAttributeNames()).thenReturn(new String[]{"propertyName"});
        automaticBean.configure(config);

        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "propertyName", 123, true);
            fail("Should throw CheckstyleException when the provided value is not of the expected type.");
        } catch (CheckstyleException ex) {
            // Expected exception
        }
    }

    @Test(expected = InvocationTargetException.class)
    public void testTryCopyPropertyWithInvalidSetter() throws Exception {
        automaticBean = new AutomaticBean();
        Configuration config = Mockito.mock(Configuration.class);

        convertUtils = new ConvertUtilsBean();
        beanUtils = new BeanUtilsBean(convertUtils, PropertyUtils.getDefaultPropertyUtils());

        Mockito.when(config.getAttributeNames()).thenReturn(new String[]{"propertyName"});
        Mockito.when(config.getAttribute("propertyName")).thenReturn("value");

        PropertyDescriptor descriptor = Mockito.mock(PropertyDescriptor.class);
        Mockito.when(descriptor.getWriteMethod()).thenThrow(new RuntimeException());

        Mockito.when(PropertyUtils.getPropertyDescriptor(automaticBean, "propertyName")).thenReturn(descriptor);

        automaticBean.configure(config);

        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "propertyName", "value", true);
            fail("Should throw InvocationTargetException when the setter method throws an exception.");
        } catch (InvocationTargetException ex) {
            // Expected exception
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void testTryCopyPropertyWithMissingSetter() throws Exception {
        automaticBean = new AutomaticBean();
        Configuration config = Mockito.mock(Configuration.class);

        convertUtils = new ConvertUtilsBean();
        beanUtils = new BeanUtilsBean(convertUtils, PropertyUtils.getDefaultPropertyUtils());

        Mockito.when(config.getAttributeNames()).thenReturn(new String[]{"propertyName"});
        Mockito.when(config.getAttribute("propertyName")).thenReturn("value");

        PropertyDescriptor descriptor = Mockito.mock(PropertyDescriptor.class);
        Mockito.when(descriptor.getWriteMethod()).thenReturn(null);

        Mockito.when(PropertyUtils.getPropertyDescriptor(automaticBean, "propertyName")).thenReturn(descriptor);

        automaticBean.configure(config);

        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "propertyName", "value", true);
            fail("Should throw NoSuchMethodException when the setter method does not exist.");
        } catch (NoSuchMethodException ex) {
            // Expected exception
        }
    }

    @Test(expected = ConversionException.class)
    public void testTryCopyPropertyWithInvalidConversion() throws Exception {
        automaticBean = new AutomaticBean();
        Configuration config = Mockito.mock(Configuration.class);

        convertUtils = new ConvertUtilsBean();

        Mockito.when(config.getAttributeNames()).thenReturn(new String[]{"propertyName"});
        Mockito.when(config.getAttribute("propertyName")).thenReturn(123);

        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "propertyName", null, true);
            fail("Should throw ConversionException when the provided value cannot be converted to the expected type.");
        } catch (ConversionException ex) {
            // Expected exception
        }
    }

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        convertUtils.getConvertUtils().register(new BooleanConverter(), Boolean.TYPE);
        convertUtils.getConvertUtils().register(new BooleanConverter(), Boolean.class);
        convertUtils.getConvertUtils().register(new ArrayConverter(boolean[].class, new BooleanConverter()), boolean[].class);
        convertUtils.getConvertUtils().register(new ByteConverter(), Byte.TYPE);
        convertUtils.getConvertUtils().register(new ByteConverter(), Byte.class);
        convertUtils.getConvertUtils().register(new ArrayConverter(byte[].class, new ByteConverter()), byte[].class);
        convertUtils.getConvertUtils().register(new CharacterConverter(), Character.TYPE);
        convertUtils.getConvertUtils().register(new CharacterConverter(), Character.class);
        convertUtils.getConvertUtils().register(new ArrayConverter(char[].class, new CharacterConverter()), char[].class);
        convertUtils.getConvertUtils().register(new DoubleConverter(), Double.TYPE);
        convertUtils.getConvertUtils().register(new DoubleConverter(), Double.class);
        convertUtils.getConvertUtils().register(new ArrayConverter(double[].class, new DoubleConverter()), double[].class);
        convertUtils.getConvertUtils().register(new FloatConverter(), Float.TYPE);
        convertUtils.getConvertUtils().register(new FloatConverter(), Float.class);
        convertUtils.getConvertUtils().register(new ArrayConverter(float[].class, new FloatConverter()), float[].class);
        convertUtils.getConvertUtils().register(new IntegerConverter(), Integer.TYPE);
        convertUtils.getConvertUtils().register(new IntegerConverter(), Integer.class);
        convertUtils.getConvertUtils().register(new ArrayConverter(int[].class, new IntegerConverter()), int[].class);
        convertUtils.getConvertUtils().register(new LongConverter(), Long.TYPE);
        convertUtils.getConvertUtils().register(new LongConverter(), Long.class);
        convertUtils.getConvertUtils().register(new ArrayConverter(long[].class, new LongConverter()), long[].class);
        convertUtils.getConvertUtils().register(new ShortConverter(), Short.TYPE);
        convertUtils.getConvertUtils().register(new ShortConverter(), Short.class);
        convertUtils.getConvertUtils().register(new ArrayConverter(short[].class, new ShortConverter()), short[].class);
        convertUtils.getConvertUtils().register(new RelaxedStringArrayConverter(), String[].class);

        beanUtils = new BeanUtilsBean(convertUtils, PropertyUtils.getDefaultPropertyUtils());
        automaticBean = new AutomaticBean();
    }
}