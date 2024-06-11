package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private BeanUtilsBean beanUtilsBean;
    private PropertyUtilsBean propertyUtilsBean;
    private Configuration config;
    private Context context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        config = mock(Configuration.class);
        context = mock(Context.class);
        beanUtilsBean = new BeanUtilsBean(new ConvertUtilsBean(), propertyUtilsBean = new PropertyUtilsBean());
        automaticBean = new AutomaticBean() {
            @Override
            public void configure(Configuration config) throws CheckstyleException {
                super.configure(config);
            }

            @Override
            public void contextualize(Context context) throws CheckstyleException {
                super.contextualize(context);
            }
        };
    }

    @Test
    public void testConfigure() throws CheckstyleException {
        when(config.getAttributeNames()).thenReturn(new String[]{"attribute1", "attribute2"});
        automaticBean.configure(config);
        verify(automaticBean).tryCopyProperty(eq(automaticBean.getClass().getName()), eq("attribute1"), anyObject(), true);
        verify(automaticBean).tryCopyProperty(eq(automaticBean.getClass().getName()), eq("attribute2"), anyObject(), true);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidProperty() throws CheckstyleException {
        when(config.getAttributeNames()).thenReturn(new String[]{"attribute1"});
        when(config.getAttribute("nonExistentAttribute")).thenReturn("value");
        automaticBean.configure(config);
        verify(automaticBean).tryCopyProperty(eq(automaticBean.getClass().getName()), eq("attribute1"), anyObject(), true);
        verify(automaticBean).tryCopyProperty(eq(automaticBean.getClass().getName()), eq("nonExistentAttribute"), anyObject(), true);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureNullConfig() throws CheckstyleException {
        automaticBean.configure(null);
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        when(context.getAttributeNames()).thenReturn(new String[]{"contextAttribute1", "contextAttribute2"});
        automaticBean.contextualize(context);
        verify(automaticBean).tryCopyProperty(eq(automaticBean.getClass().getName()), eq("contextAttribute1"), anyObject(), false);
        verify(automaticBean).tryCopyProperty(eq(automaticBean.getClass().getName()), eq("contextAttribute2"), anyObject(), false);
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeNullContext() throws CheckstyleException {
        automaticBean.contextualize(null);
    }

    @Test
    public void testTryCopyProperty() throws CheckstyleException {
        when(propertyUtilsBean.getPropertyDescriptor(automaticBean, "property1")).thenReturn(new PropertyDescriptor("property1", AutomaticBean.class));
        beanUtilsBean.copyProperty(automaticBean, "property1", "value");
        assertEquals("value", automaticBean.getConfiguration().getAttribute("property1"));

        PropertyDescriptor descriptor = propertyUtilsBean.getPropertyDescriptor(automaticBean, "nonExistentProperty");
        assertNull(descriptor);
        try {
            beanUtilsBean.copyProperty(automaticBean, "nonExistentProperty", "value");
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            beanUtilsBean.copyProperty(automaticBean, "property1", 123);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            beanUtilsBean.copyProperty(automaticBean, "property1", new Object());
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            beanUtilsBean.copyProperty(automaticBean, "property1", null);
            fail("Should have thrown an exception");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            beanUtilsBean.copyProperty(automaticBean, "property1", new String[]{"value1", "value2"});
            fail("Should have thrown an exception");
        } catch (ConversionException e) {
            // Expected
        }
    }

    @Test
    public void testCreateBeanUtilsBean() {
        BeanUtilsBean result = AutomaticBean.createBeanUtilsBean();
        assertEquals(BeanUtils.class, result.getClass().getSuperclass());
        assertEquals(PropertyUtils.class, result.getPropertyUtils().getClass().getSuperclass());
        assertTrue(result.getConvertUtils().isRegistered(BooleanConverter.class));
        assertTrue(result.getConvertUtils().isRegistered(Boolean[].class));
        assertTrue(result.getConvertUtils().isRegistered(ByteConverter.class));
        assertTrue(result.getConvertUtils().isRegistered(Byte[].class));
        assertTrue(result.getConvertUtils().isRegistered(CharacterConverter.class));
        assertTrue(result.getConvertUtils().isRegistered(Character.class));
        assertTrue(result.getConvertUtils().isRegistered(Character[].class));
        assertTrue(result.getConvertUtils().isRegistered(DoubleConverter.class));
        assertTrue(result.getConvertUtils().isRegistered(Double.class));
        assertTrue(result.getConvertUtils().isRegistered(Double[].class));
        assertTrue(result.getConvertUtils().isRegistered(FloatConverter.class));
        assertTrue(result.getConvertUtils().isRegistered(Float.class));
        assertTrue(result.getConvertUtils().isRegistered(Float[].class));
        assertTrue(result.getConvertUtils().isRegistered(IntegerConverter.class));
        assertTrue(result.getConvertUtils().isRegistered(Integer.TYPE));
        assertTrue(result.getConvertUtils().isRegistered(Integer.class));
        assertTrue(result.getConvertUtils().isRegistered(Integer[].class));
        assertTrue(result.getConvertUtils().isRegistered(LongConverter.class));
        assertTrue(result.getConvertUtils().isRegistered(Long.TYPE));
        assertTrue(result.getConvertUtils().isRegistered(Long.class));
        assertTrue(result.getConvertUtils().isRegistered(Long[].class));
        assertTrue(result.getConvertUtils().isRegistered(ShortConverter.class));
        assertTrue(result.getConvertUtils().isRegistered(Short.TYPE));
        assertTrue(result.getConvertUtils().isRegistered(Short.class));
        assertTrue(result.getConvertUtils().isRegistered(Short[].class));
        assertTrue(result.getConvertUtils().isRegistered(RelaxedStringArrayConverter.class));
    }
}