package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class AutomaticBeanTest {
    private AutomaticBean automaticBean;
    private BeanUtilsBean beanUtilsBean;
    private Configuration configuration;
    private ConvertUtilsBean convertUtilsBean;
    private PropertyUtilsBean propertyUtilsBean;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        configuration = new Configuration();
        automaticBean = new AutomaticBean();
        beanUtilsBean = new BeanUtilsBean(convertUtilsBean, propertyUtilsBean);
    }

    @Test
    public void testConfigureWithNullConfig() {
        assertThrows(CheckstyleException.class, () -> automaticBean.configure(null));
    }

    @Test
    public void testConfigureWithEmptyConfig() {
        automaticBean.configure(new Configuration());
        assertEquals(configuration, automaticBean.getConfiguration());
        verify(propertyUtilsBean, never()).getPropertyDescriptor(automaticBean, "configuration");
    }

    @Test
    public void testConfigureWithAttributes() {
        String attributeName = "attributeName";
        String attributeValue = "attributeValue";
        configuration.addAttribute(attributeName, attributeValue);

        automaticBean.configure(configuration);
        assertEquals(attributeValue, automaticBean.tryGetProperty(attributeName));
        verify(propertyUtilsBean, times(1)).getPropertyDescriptor(automaticBean, attributeName);
    }

    @Test
    public void testConfigureWithChildren() {
        Configuration childConfiguration = new Configuration();
        Configuration childConfig = new Configuration();
        childConfiguration.addChild(childConfig);
        configuration.addChild(childConfiguration);

        automaticBean.configure(configuration);
        assertEquals(ListUtils.typeSafeList(childConfig), automaticBean.getChildren());
    }

    @Test
    public void testTryCopyPropertyWithNullModuleName() {
        assertThrows(NullPointerException.class, () -> automaticBean.tryCopyProperty(null, "attributeName", "attributeValue", true));
    }

    @Test
    public void testTryCopyPropertyWithNullKey() {
        assertThrows(NullPointerException.class, () -> automaticBean.tryCopyProperty(this.getClass().getName(), null, "attributeValue", true));
    }

    @Test
    public void testTryCopyPropertyWithNullValue() {
        assertThrows(NullPointerException.class, () -> automaticBean.tryCopyProperty(this.getClass().getName(), "attributeName", null, true));
    }

    @Test
    public void testTryCopyPropertyWithNonExistentProperty() {
        assertThrows(CheckstyleException.class, () -> automaticBean.tryCopyProperty(this.getClass().getName(), "nonExistentProperty", "attributeValue", true));
    }

    @Test
    public void testTryCopyPropertyWithRecheck() throws CheckstyleException {
        String key = "attributeName";
        String value = "attributeValue";
        configuration.addAttribute(key, value);

        automaticBean.configure(configuration);
        automaticBean.tryCopyProperty(this.getClass().getName(), key, value, true);

        verify(beanUtilsBean, times(2)).copyProperty(automaticBean, key, value);
    }

    @Test
    public void testTryCopyPropertyWithValueOfDifferentType() throws CheckstyleException {
        String key = "attributeName";
        int value = 42;

        automaticBean.tryCopyProperty(this.getClass().getName(), key, value, true);

        verify(beanUtilsBean, never()).copyProperty(automaticBean, key, value);
        verify(beanUtilsBean, times(1)).copyProperty(automaticBean, key, Integer.valueOf(value));
    }

    @Test
    public void testTryCopyPropertyWithInvalidValue() throws CheckstyleException {
        String key = "attributeName";
        String value = "invalidValue";

        assertThrows(RuntimeException.class, () -> automaticBean.tryCopyProperty(this.getClass().getName(), key, value, true));

        verify(beanUtilsBean, never()).copyProperty(automaticBean, key, value);
    }

    @Test
    public void testContextualizeWithNullContext() {
        assertThrows(CheckstyleException.class, () -> automaticBean.contextualize(null));
    }

    @Test
    public void testContextualizeWithEmptyContext() {
        automaticBean.contextualize(new Properties());
        assertEquals(new Properties(), automaticBean.getConfiguration());
        verify(propertyUtilsBean, never()).getPropertyDescriptor(automaticBean, "configuration");
    }

    @Test
    public void testContextualizeWithAttributes() {
        String key = "attributeName";
        String value = "attributeValue";
        Properties context = new Properties();
        context.setProperty(key, value);

        automaticBean.contextualize(context);
        assertEquals(value, automaticBean.tryGetProperty(key));
        verify(propertyUtilsBean, times(1)).getPropertyDescriptor(automaticBean, key);
    }

    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        // No implementation, so no test needed
    }

    @Test
    public void testSetupChildWithNullChildConfiguration() {
        Configuration childConfig = null;
        assertThrows(CheckstyleException.class, () -> automaticBean.setupChild(childConfig));
    }

    @Test
    public void testSetupChildWithValidChildConfiguration() {
        Configuration childConfig = new Configuration();

        automaticBean.setupChild(childConfig);
        List<Configuration> children = Arrays.asList(childConfig);
        assertEquals(children, automaticBean.getChildren());

        verify(beanUtilsBean, times(1)).copyProperty(automaticBean, "children", children.toArray());
    }

    @Test
    public void testGetConfiguration() {
        Configuration config = new Configuration();
        automaticBean.configure(config);
        assertEquals(config, automaticBean.getConfiguration());
    }

    @Test
    public void testTryGetPropertyWithNullPropertyName() {
        assertNull(automaticBean.tryGetProperty(null));
    }

    @Test
    public void testTryGetPropertyWithNonExistentProperty() {
        assertNull(automaticBean.tryGetProperty("nonExistentProperty"));
    }

    @Test
    public void testTryGetPropertyWithExistentProperty() {
        String key = "attributeName";
        String value = "attributeValue";
        configuration.addAttribute(key, value);
        automaticBean.configure(configuration);

        assertEquals(value, automaticBean.tryGetProperty(key));
    }

    @Test
    public void testToString() {
        assertEquals("AutomaticBean{configuration=null}", automaticBean.toString());
    }

    @Test
    public void testCreateBeanUtilsBean() {
        BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        assertEquals(BooleanConverter.class, beanUtilsBean.getConvertUtils().getConverter(Boolean.class));
        assertEquals(ByteConverter.class, beanUtilsBean.getConvertUtils().getConverter(Byte.class));
        assertEquals(CharacterConverter.class, beanUtilsBean.getConvertUtils().getConverter(Character.class));
        assertEquals(DoubleConverter.class, beanUtilsBean.getConvertUtils().getConverter(Double.class));
        assertEquals(FloatConverter.class, beanUtilsBean.getConvertUtils().getConverter(Float.class));
        assertEquals(IntegerConverter.class, beanUtilsBean.getConvertUtils().getConverter(Integer.class));
        assertEquals(LongConverter.class, beanUtilsBean.getConvertUtils().getConverter(Long.class));
        assertEquals(ShortConverter.class, beanUtilsBean.getConvertUtils().getConverter(Short.class));
        assertEquals(RelaxedStringArrayConverter.class, beanUtilsBean.getConvertUtils().getConverter(String[].class));
    }

    @Test
    public void testPropertyUtilsBeanGetPropertyDescriptor() throws IntrospectionException {
        PropertyDescriptor propertyDescriptor = PropertyUtilsBean.getPropertyDescriptor(automaticBean, "configuration");
        assertNotNull(propertyDescriptor);
    }

    @Test
    public void testTryGetPropertyWithArrayProperty() {
        String key = "children";
        Configuration[] children = new Configuration[0];
        automaticBean.tryCopyProperty(this.getClass().getName(), key, children, true);

        assertEquals(children, automaticBean.tryGetProperty(key));
    }

    @Test
    public void testTryGetPropertyWithNullArrayProperty() {
        String key = "children";
        automaticBean.tryCopyProperty(this.getClass().getName(), key, null, true);

        assertNull(automaticBean.tryGetProperty(key));
    }

    @Test
    public void testTryGetPropertyWithEmptyArrayProperty() {
        String key = "children";
        automaticBean.tryCopyProperty(this.getClass().getName(), key, new Configuration[0], true);

        assertArrayEquals(new Configuration[0], automaticBean.tryGetProperty(key));
    }

    @Test
    public void testTryGetPropertyWithNonEmptyArrayProperty() {
        String key = "children";
        Configuration[] children = new Configuration[] { new Configuration() };
        automaticBean.tryCopyProperty(this.getClass().getName(), key, children, true);

        assertEquals(Arrays.asList(children[0]), automaticBean.tryGetProperty(key));
    }

    @Test
    public void testCopyPropertyWithArrayProperty() throws CheckstyleException {
        String key = "children";
        Configuration[] children = { new Configuration(), new Configuration() };

        // Set up mocks to verify that the correct setter method is called
        Configuration targetConfiguration = mock(Configuration.class);
        PropertyDescriptor propertyDescriptor = mock(PropertyDescriptor.class);
        when(propertyUtilsBean.getPropertyDescriptor(targetConfiguration, key)).thenReturn(propertyDescriptor);
        when(propertyDescriptor.getWriteMethod()).thenReturn(mock(Method.class));

        automaticBean.configure(configuration);
        automaticBean.tryCopyProperty(this.getClass().getName(), key, children, true);

        verify(targetConfiguration, times(1)).setChildren(children);
    }

    @Test
    public void testGettersAndSetters() throws CheckstyleException {
        String key = "attributeName";
        String value = "attributeValue";

        automaticBean.configure(configuration);
        automaticBean.setConfiguration(configuration);

        automaticBean.tryCopyProperty(this.getClass().getName(), key, value, true);

        assertEquals(value, automaticBean.tryGetProperty(key));
        assertEquals(configuration, automaticBean.getConfiguration());
    }
}