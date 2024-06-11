package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
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
import org.apache.commons.beanutils.converters.RelaxedStringArrayConverter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private Configuration configurationMock;

    @Before
    public void initMocks() {
        configurationMock = mock(Configuration.class);
        automaticBean = new AutomaticBean();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConfigure_NoAttributes() throws CheckstyleException {
        // Empty configuration
        when(configurationMock.getAttributeNames()).thenReturn(new String[0]);

        automaticBean.configure(configurationMock);

        // Configuration should not be set
        assertNull(automaticBean.getConfiguration());
        
        verifyNoMoreInteractions(configurationMock);
    }

    @Test
    public void testConfigure_SingleAttribute() throws CheckstyleException {
        // Configuration with one attribute
        String attributeName = "testAttribute";
        String attributeValue = "testValue";
        when(configurationMock.getAttributeNames()).thenReturn(new String[] { attributeName });
        when(configurationMock.getAttribute(attributeName)).thenReturn(attributeValue);

        automaticBean.configure(configurationMock);

        // Configuration should be set correctly
        Configuration config = automaticBean.getConfiguration();
        assertEquals(attributeValue, config.getAttribute(attributeName));
        
        // Properties should be copied
        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testAttribute");
        assertEquals(attributeValue, propertyDescriptor.getReadMethod().invoke(automaticBean));

        verify(configurationMock).getAttributeNames();
        verify(configurationMock).getAttribute(attributeName);
        verifyNoMoreInteractions(configurationMock);
    }

    @Test
    public void testConfigure_MultipleAttributes() throws CheckstyleException {
        // Configuration with multiple attributes
        String attributeName1 = "testAttribute1";
        String attributeValue1 = "testValue1";
        String attributeName2 = "testAttribute2";
        String attributeValue2 = "testValue2";

        when(configurationMock.getAttributeNames()).thenReturn(new String[] { attributeName1, attributeName2 });
        when(configurationMock.getAttribute(attributeName1)).thenReturn(attributeValue1);
        when(configurationMock.getAttribute(attributeName2)).thenReturn(attributeValue2);

        automaticBean.configure(configurationMock);

        // Configuration should be set correctly
        Configuration config = automaticBean.getConfiguration();
        assertEquals(attributeValue1, config.getAttribute(attributeName1));
        assertEquals(attributeValue2, config.getAttribute(attributeName2));

        // Properties should be copied
        PropertyDescriptor propertyDescriptor1 = PropertyUtils.getPropertyDescriptor(automaticBean, "testAttribute1");
        assertEquals(attributeValue1, propertyDescriptor1.getReadMethod().invoke(automaticBean));
        PropertyDescriptor propertyDescriptor2 = PropertyUtils.getPropertyDescriptor(automaticBean, "testAttribute2");
        assertEquals(attributeValue2, propertyDescriptor2.getReadMethod().invoke(automaticBean));

        verify(configurationMock).getAttributeNames();
        verify(configurationMock).getAttribute(attributeName1);
        verify(configurationMock).getAttribute(attributeName2);
        verifyNoMoreInteractions(configurationMock);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_NullConfiguration() throws CheckstyleException {
        automaticBean.configure(null);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_InvalidAttributeName() throws CheckstyleException {
        String attributeName = "testAttribute";
        when(configurationMock.getAttributeNames()).thenReturn(new String[] { attributeName });
        when(configurationMock.getAttribute(attributeName)).thenReturn("testValue");

        doThrow(NoSuchMethodException.class)
                .when(PropertyUtils)
                .getPropertyDescriptor(automaticBean, attributeName);

        automaticBean.configure(configurationMock);
    }

    @Test
    public void testContextualize_NoAttributes() throws CheckstyleException {
        // Empty context
        when(configurationMock.getAttributeNames()).thenReturn(Collections.emptyList());

        automaticBean.contextualize(configurationMock);

        // No properties should be set
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(automaticBean);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            assertNull(propertyDescriptor.getReadMethod().invoke(automaticBean));
        }

        verifyNoMoreInteractions(configurationMock);
    }

    @Test
    public void testContextualize_SingleAttribute() throws CheckstyleException {
        // Context with one attribute
        String attributeName = "testAttribute";
        String attributeValue = "testValue";
        when(configurationMock.getAttributeNames()).thenReturn(Arrays.asList(attributeName));
        when(configurationMock.get(attributeName)).thenReturn(attributeValue);

        automaticBean.contextualize(configurationMock);

        // Property should be set correctly
        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "testAttribute");
        assertEquals(attributeValue, propertyDescriptor.getReadMethod().invoke(automaticBean));

        verify(configurationMock).getAttributeNames();
        for (Map.Entry<String, Object> entry : configurationMock.entrySet()) {
            assertEquals(entry.getKey(), attributeName);
        }
        verifyNoMoreInteractions(configurationMock);
    }

    @Test
    public void testContextualize_MultipleAttributes() throws CheckstyleException {
        // Context with multiple attributes
        String attributeName1 = "testAttribute1";
        String attributeValue1 = "testValue1";
        String attributeName2 = "testAttribute2";
        String attributeValue2 = "testValue2";

        when(configurationMock.getAttributeNames()).thenReturn(ListUtils.union(Arrays.asList(attributeName1, attributeName2), Collections.singletonList("anotherAttribute")));
        when(configurationMock.get(attributeName1)).thenReturn(attributeValue1);
        when(configurationMock.get(attributeName2)).thenReturn(attributeValue2);

        automaticBean.contextualize(configurationMock);

        // Properties should be set correctly
        PropertyDescriptor propertyDescriptor1 = PropertyUtils.getPropertyDescriptor(automaticBean, "testAttribute1");
        assertEquals(attributeValue1, propertyDescriptor1.getReadMethod().invoke(automaticBean));
        PropertyDescriptor propertyDescriptor2 = PropertyUtils.getPropertyDescriptor(automaticBean, "testAttribute2");
        assertEquals(attributeValue2, propertyDescriptor2.getReadMethod().invoke(automaticBean));

        verify(configurationMock).getAttributeNames();
        for (Map.Entry<String, Object> entry : configurationMock.entrySet()) {
            assertEquals(entry.getKey(), attributeName1
                    || attributeName2);
        }
        verifyNoMoreInteractions(configurationMock);
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualize_InvalidAttributeName() throws CheckstyleException {
        String attributeName = "testAttribute";
        when(configurationMock.getAttributeNames()).thenReturn(Collections.singletonList(attributeName));
        when(configurationMock.get(attributeName)).thenReturn("testValue");

        doThrow(NoSuchMethodException.class)
                .when(PropertyUtils)
                .getPropertyDescriptor(automaticBean, attributeName);

        automaticBean.contextualize(configurationMock);
    }

    @Test
    public void testTryCopyProperty_ValidProperty() throws CheckstyleException {
        String key = "testKey";
        Object value = "testValue";

        automaticBean.tryCopyProperty("testModuleName", key, value, true);

        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
        assertEquals(value, propertyDescriptor.getReadMethod().invoke(automaticBean));
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_InvalidProperty() throws CheckstyleException {
        String key = "testKey";

        doThrow(NoSuchMethodException.class)
                .when(PropertyUtils)
                .getPropertyDescriptor(automaticBean, key);

        automaticBean.tryCopyProperty("testModuleName", key, "testValue", true);
    }

    @Test
    public void testTryCopyProperty_ValidPropertyWithRecheck() throws CheckstyleException {
        String key = "testKey";
        Object value = "testValue";
        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);

        automaticBean.tryCopyProperty("testModuleName", key, value, true);
        automaticBean.tryCopyProperty("testModuleName", key, value, true);

        assertEquals(value, propertyDescriptor.getReadMethod().invoke(automaticBean));
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyProperty_InvalidPropertyWithRecheck() throws CheckstyleException {
        String key = "testKey";
        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);

        automaticBean.tryCopyProperty("testModuleName", key, null, true);
        automaticBean.tryCopyProperty("testModuleName", key, null, true);

        assertNull(propertyDescriptor.getReadMethod().invoke(automaticBean));
    }

    @Test
    public void testTryCopyProperty_ValidPropertyWithBooleanValue() throws CheckstyleException {
        String key = "testKey";
        Boolean value = true;

        automaticBean.tryCopyProperty("testModuleName", key, value, true);

        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
        assertTrue(propertyDescriptor.getReadMethod().invoke(automaticBean));
    }

    @Test
    public void testTryCopyProperty_InvalidPropertyWithBooleanValue() throws CheckstyleException {
        String key = "testKey";

        doThrow(NoSuchMethodException.class)
                .when(PropertyUtils)
                .getPropertyDescriptor(automaticBean, key);

        automaticBean.tryCopyProperty("testModuleName", key, true, true);
    }

    @Test
    public void testTryCopyProperty_ValidPropertyWithArrayValue() throws CheckstyleException {
        String key = "testKey";
        Object arrayValue = new boolean[] { true, false };

        automaticBean.tryCopyProperty("testModuleName", key, arrayValue, true);

        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
        assertArrayEquals(arrayValue, propertyDescriptor.getReadMethod().invoke(automaticBean));
    }

    @Test
    public void testTryCopyProperty_InvalidPropertyWithArrayValue() throws CheckstyleException {
        String key = "testKey";

        doThrow(NoSuchMethodException.class)
                .when(PropertyUtils)
                .getPropertyDescriptor(automaticBean, key);

        automaticBean.tryCopyProperty("testModuleName", key, new boolean[] { true, false }, true);
    }

    @Test
    public void testTryCopyProperty_ValidPropertyWithStringArrayValue() throws CheckstyleException {
        String key = "testKey";
        Object stringArrayValue = new String[] { "testString1", "testString2" };

        automaticBean.tryCopyProperty("testModuleName", key, stringArrayValue, true);

        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, key);
        assertArrayEquals(stringArrayValue, propertyDescriptor.getReadMethod().invoke(automaticBean));
    }

    @Test
    public void testTryCopyProperty_InvalidPropertyWithStringArrayValue() throws CheckstyleException {
        String key = "testKey";

        doThrow(NoSuchMethodException.class)
                .when(PropertyUtils)
                .getPropertyDescriptor(automaticBean, key);

        automaticBean.tryCopyProperty("testModuleName", key, new String[] { "testString1", "testString2" }, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTryCopyProperty_InvalidValue() throws CheckstyleException {
        String key = "testKey";
        Object invalidValue = 123;

        automaticBean.tryCopyProperty("testModuleName", key, invalidValue, true);
    }

    @Test(expected = ConversionException.class)
    public void testTryCopyProperty_InvalidValueWithStringConverter() throws CheckstyleException {
        String key = "testKey";
        Object value = "testValue";

        doThrow(ConversionException.class)
                .when(ConvertUtils)
                .convert(PropertyUtils.getPropertyDescriptor(automaticBean, key).getPropertyType(), value);

        automaticBean.tryCopyProperty("testModuleName", key, value, true);
    }

    @Test(expected = InvocationTargetException.class)
    public void testTryCopyProperty_InvocationTargetException() throws CheckstyleException {
        String key = "testKey";
        Object value = "testValue";

        doThrow(new InvocationTargetException(new RuntimeException()))
                .when(PropertyUtils)
                .setProperty(automaticBean, key, value);

        automaticBean.tryCopyProperty("testModuleName", key, value, true);
    }

    @Test(expected = NoSuchMethodException.class)
    public void testTryCopyProperty_NoSuchMethodException() throws CheckstyleException {
        String key = "testKey";

        doThrow(new NoSuchMethodException())
                .when(PropertyUtils)
                .getPropertyDescriptor(automaticBean, key);

        automaticBean.tryCopyProperty("testModuleName", key, null, true);
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild_ValidChild() throws CheckstyleException {
        Configuration childConfig = mock(Configuration.class);
        automaticBean.setupChild(childConfig);

        verify(childConfig).getName();
        verifyThrowable(new CheckstyleException("childConfig is not allowed as a child in " + automaticBean.getConfiguration().getName()));
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild_InvalidChild() throws CheckstyleException {
        Configuration childConfig = null;
        automaticBean.setupChild(childConfig);

        verify(childConfig).getName();
        verifyThrowable(new CheckstyleException("childConfig is not allowed as a child in " + automaticBean.getConfiguration().getName()));
    }
}