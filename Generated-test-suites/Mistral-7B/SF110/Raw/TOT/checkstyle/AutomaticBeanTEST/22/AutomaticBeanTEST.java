package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private Configuration configuration;
    private ConvertUtilsBean convertUtilsBean;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        configuration = mock(Configuration.class);
        automaticBean = new AutomaticBean();
        convertUtilsBean = new ConvertUtilsBean();
    }

    @Test
    public void testConfiguration() throws CheckstyleException {
        // Given: Empty configuration
        when(configuration.getAttributeNames()).thenReturn(new String[0]);

        // When: Configure bean with empty configuration
        automaticBean.configure(configuration);

        // Then: No properties should be copied
        verify(automaticBean, never()).tryCopyProperty(anyString(), anyString(), anyObject(), anyBoolean());

        // Given: Configuration with properties
        String[] attributeNames = {"attr1", "attr2"};
        when(configuration.getAttributeNames()).thenReturn(attributeNames);

        // When: Configure bean with configuration
        automaticBean.configure(configuration);

        // Then: Properties should be copied
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> valueCaptor = ArgumentCaptor.forClass(Object.class);
        verify(automaticBean).tryCopyProperty(anyString(), keyCaptor.capture(), valueCaptor.capture(), anyBoolean());
        String key1 = keyCaptor.getValue();
        Object value1 = valueCaptor.getValue();
        verify(automaticBean).tryCopyProperty(anyString(), keyCaptor.capture(), valueCaptor.capture(), anyBoolean());
        String key2 = keyCaptor.getValue();
        Object value2 = valueCaptor.getValue();

        assertEquals("attr1", key1);
        assertEquals("value1", value1);
        assertEquals("attr2", key2);
        assertEquals("value2", value2);

        // Given: Configuration with child configurations
        Configuration childConfiguration = mock(Configuration.class);
        when(configuration.getChildren()).thenReturn(new Configuration[] { childConfiguration });

        // When: Configure bean with configuration
        automaticBean.configure(configuration);

        // Then: Child configuration should be set up
        verify(automaticBean).setupChild(childConfiguration);
    }

    @Test(expected = CheckstyleException.class)
    public void testInvalidPropertyType() throws CheckstyleException {
        // Given: Bean with invalid property type
        String invalidPropertyName = "invalidProperty";
        automaticBean.setConfiguration(configuration);

        // When: Try to set invalid property
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), invalidPropertyName, new Object(), true);

        // Then: CheckstyleException should be thrown
    }

    @Test(expected = CheckstyleException.class)
    public void testInvalidPropertyValue() throws CheckstyleException {
        // Given: Bean with property
        String propertyName = "property";
        automaticBean.setConfiguration(configuration);

        // When: Try to set property with invalid value
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), propertyName, new Object() {}, true);

        // Then: CheckstyleException should be thrown
    }

    @Test(expected = CheckstyleException.class)
    public void testMissingProperty() throws CheckstyleException {
        // Given: Bean with missing property
        String missingPropertyName = "missingProperty";
        automaticBean.setConfiguration(configuration);

        // When: Try to set missing property
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), missingPropertyName, "value", true);

        // Then: CheckstyleException should be thrown
    }

    @Test
    public void testPropertyConversion() throws CheckstyleException {
        // Given: Bean with property to be converted
        String propertyName = "property";
        Object propertyValue = Arrays.asList("value1", "value2");
        automaticBean.setConfiguration(configuration);

        // When: Set property with list value
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), propertyName, propertyValue, true);

        // Then: Property should be set with converted value
        Object convertedValue = convertUtilsBean.convert(List.class, propertyValue.toString());
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, propertyName);
        assertEquals(convertedValue, descriptor.getReadMethod().invoke(automaticBean));
    }

    @Test
    public void testContextualization() throws CheckstyleException {
        // Given: Context with properties
        Map<String, Object> context = new HashMap<>();
        context.put("contextProperty1", "value1");
        context.put("contextProperty2", "value2");

        // When: Contextualize bean
        automaticBean.contextualize(context);

        // Then: Properties should be set
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "contextProperty1");
        assertEquals("value1", descriptor.getReadMethod().invoke(automaticBean));
        descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "contextProperty2");
        assertEquals("value2", descriptor.getReadMethod().invoke(automaticBean));
    }

    @Test(expected = CheckstyleException.class)
    public void testInvalidContextPropertyType() throws CheckstyleException {
        // Given: Context with invalid property type
        automaticBean.setConfiguration(configuration);

        // When: Try to set invalid context property
        automaticBean.contextualize(configuration);

        // Then: CheckstyleException should be thrown
    }

    @Test(expected = CheckstyleException.class)
    public void testInvalidContextPropertyValue() throws CheckstyleException {
        // Given: Context with property
        automaticBean.setConfiguration(configuration);

        // When: Try to set context property with invalid value
        automaticBean.contextualize(configuration);

        // Then: CheckstyleException should be thrown
    }

    @Test(expected = CheckstyleException.class)
    public void testMissingContextProperty() throws CheckstyleException {
        // Given: Context with missing property
        automaticBean.setConfiguration(configuration);

        // When: Try to set missing context property
        automaticBean.contextualize(configuration);

        // Then: CheckstyleException should be thrown
    }

}