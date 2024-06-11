package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;
    private BeanUtilsBean beanUtils;
    private Configuration configuration;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        automaticBean = new AutomaticBean();
        beanUtils = new BeanUtilsBean(new ConvertUtilsBean(), new PropertyUtilsBean());
        configuration = mock(Configuration.class);
    }

    @Test
    public void testConfigure() throws Exception {
        when(configuration.getAttributeNames()).thenReturn(new String[] {"booleanAttribute", "byteAttribute", "shortAttribute"});
        automaticBean.configure(configuration);
        assertNotNull(automaticBean.getConfiguration());
        verify(configuration).getAttributeNames();
        verify(configuration).getAttribute("booleanAttribute");
        verify(configuration).getAttribute("byteAttribute");
        verify(configuration).getAttribute("shortAttribute");
    }

    @Test
    public void testTryCopyProperty_Boolean() throws Exception {
        when(configuration.getAttributeNames()).thenReturn(new String[] {"booleanAttribute"});
        when(configuration.getAttribute("booleanAttribute")).thenReturn(true);
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "booleanAttribute", true, true);
        assertTrue(automaticBean.getConfiguration().getBoolean("booleanAttribute"));
    }

    @Test
    public void testTryCopyProperty_Boolean_InvalidProperty() throws Exception {
        when(configuration.getAttributeNames()).thenReturn(new String[] {"booleanAttribute"});
        when(configuration.getAttribute("nonExistentProperty")).thenReturn("invalidValue");
        String errorMessage = null;
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "nonExistentProperty", "invalidValue", true);
        } catch (CheckstyleException e) {
            errorMessage = e.getMessage();
        }
        assertNotNull(errorMessage);
        assertTrue(StringUtils.contains(errorMessage, "Property 'nonExistentProperty' in module"));
    }

    @Test
    public void testTryCopyProperty_Byte() throws Exception {
        when(configuration.getAttributeNames()).thenReturn(new String[] {"byteAttribute"});
        when(configuration.getAttribute("byteAttribute")).thenReturn((byte) 5);
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "byteAttribute", (byte) 5, true);
        assertEquals((byte) 5, automaticBean.getConfiguration().getByte("byteAttribute"));
    }

    @Test
    public void testTryCopyProperty_Byte_InvalidProperty() throws Exception {
        when(configuration.getAttributeNames()).thenReturn(new String[] {"byteAttribute"});
        when(configuration.getAttribute("nonExistentProperty")).thenReturn("invalidValue");
        String errorMessage = null;
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "nonExistentProperty", "invalidValue", true);
        } catch (CheckstyleException e) {
            errorMessage = e.getMessage();
        }
        assertNotNull(errorMessage);
        assertTrue(StringUtils.contains(errorMessage, "Property 'nonExistentProperty' in module"));
    }

    @Test
    public void testTryCopyProperty_Short() throws Exception {
        when(configuration.getAttributeNames()).thenReturn(new String[] {"shortAttribute"});
        when(configuration.getAttribute("shortAttribute")).thenReturn((short) 10);
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "shortAttribute", (short) 10, true);
        assertEquals((short) 10, automaticBean.getConfiguration().getShort("shortAttribute"));
    }

    @Test
    public void testTryCopyProperty_Short_InvalidProperty() throws Exception {
        when(configuration.getAttributeNames()).thenReturn(new String[] {"shortAttribute"});
        when(configuration.getAttribute("nonExistentProperty")).thenReturn("invalidValue");
        String errorMessage = null;
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "nonExistentProperty", "invalidValue", true);
        } catch (CheckstyleException e) {
            errorMessage = e.getMessage();
        }
        assertNotNull(errorMessage);
        assertTrue(StringUtils.contains(errorMessage, "Property 'nonExistentProperty' in module"));
    }

    @Test
    public void testTryCopyProperty_StringArray() throws Exception {
        when(configuration.getAttributeNames()).thenReturn(new String[] {"stringArrayAttribute"});
        when(configuration.getAttribute("stringArrayAttribute")).thenReturn("value1,value2");
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "stringArrayAttribute", "value1,value2", true);
        List<String> expected = Arrays.asList("value1", "value2");
        List<String> actual = ListUtils.toList(automaticBean.getConfiguration().getStringArray("stringArrayAttribute"));
        assertEquals(expected, actual);
    }

    @Test
    public void testTryCopyProperty_StringArray_InvalidProperty() throws Exception {
        when(configuration.getAttributeNames()).thenReturn(new String[] {"stringArrayAttribute"});
        when(configuration.getAttribute("nonExistentProperty")).thenReturn("invalidValue");
        String errorMessage = null;
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "nonExistentProperty", "invalidValue", true);
        } catch (CheckstyleException e) {
            errorMessage = e.getMessage();
        }
        assertNotNull(errorMessage);
        assertTrue(StringUtils.contains(errorMessage, "Property 'nonExistentProperty' in module"));
    }

    @Test
    public void testTryCopyProperty_InvalidType() throws Exception {
        when(configuration.getAttributeNames()).thenReturn(new String[] {"booleanAttribute"});
        when(configuration.getAttribute("booleanAttribute")).thenReturn(42);
        String errorMessage = null;
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "booleanAttribute", 42, true);
        } catch (CheckstyleException e) {
            errorMessage = e.getMessage();
        }
        assertNotNull(errorMessage);
        assertTrue(StringUtils.contains(errorMessage, "illegal value '42' for property 'booleanAttribute' of module"));
    }

    @Test
    public void testContextualize() throws Exception {
        Configuration contextConfig = mock(Configuration.class);
        when(contextConfig.getAttributeNames()).thenReturn(new String[] {"booleanAttribute", "byteArrayAttribute"});
        when(contextConfig.getAttribute("booleanAttribute")).thenReturn(true);
        when(contextConfig.getAttribute("byteArrayAttribute")).thenReturn(new byte[] {1, 2, 3});
        automaticBean.contextualize(contextConfig);
        assertEquals(true, automaticBean.getConfiguration().getBoolean("booleanAttribute"));
        byte[] expectedByteArray = {1, 2, 3};
        byte[] actualByteArray = automaticBean.getConfiguration().getByteArray("byteArrayAttribute");
        assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testContextualize_NullContext() throws Exception {
        automaticBean.contextualize(null);
        assertTrue(automaticBean.getConfiguration().isEmpty());
    }

    @Test
    public void testContextualize_InvalidProperty() throws Exception {
        Configuration contextConfig = mock(Configuration.class);
        when(contextConfig.getAttributeNames()).thenReturn(new String[] {"booleanAttribute"});
        when(contextConfig.getAttribute("nonExistentProperty")).thenReturn("invalidValue");
        String errorMessage = null;
        try {
            automaticBean.contextualize(contextConfig);
        } catch (CheckstyleException e) {
            errorMessage = e.getMessage();
        }
        assertNotNull(errorMessage);
        assertTrue(StringUtils.contains(errorMessage, "Property 'nonExistentProperty' in context does not exist"));
    }

    @Test
    public void testCopyProperties() throws Exception {
        Configuration config = new Configuration();
        config.setBooleanProperty("booleanAttribute", true);
        config.setByteProperty("byteAttribute", (byte) 5);
        config.setShortProperty("shortAttribute", (short) 10);
        config.setStringProperty("stringAttribute", "value");
        automaticBean.configure(config);
        assertEquals(true, automaticBean.getConfiguration().getBoolean("booleanAttribute"));
        assertEquals((byte) 5, automaticBean.getConfiguration().getByte("byteAttribute"));
        assertEquals((short) 10, automaticBean.getConfiguration().getShort("shortAttribute"));
        assertEquals("value", automaticBean.getConfiguration().getString("stringAttribute"));
    }

    @Test
    public void testCopyProperties_NullConfiguration() throws Exception {
        automaticBean.configure(null);
        assertTrue(automaticBean.getConfiguration().isEmpty());
    }

    @Test
    public void testCopyProperties_InvalidProperty() throws Exception {
        Configuration config = new Configuration();
        config.setBooleanProperty("booleanAttribute", true);
        when(configuration.getAttributeNames()).thenReturn(new String[] {"nonExistentProperty"});
        String errorMessage = null;
        try {
            automaticBean.configure(config);
        } catch (CheckstyleException e) {
            errorMessage = e.getMessage();
        }
        assertNotNull(errorMessage);
        assertTrue(StringUtils.contains(errorMessage, "Property 'nonExistentProperty' in configuration does not exist"));
    }
}