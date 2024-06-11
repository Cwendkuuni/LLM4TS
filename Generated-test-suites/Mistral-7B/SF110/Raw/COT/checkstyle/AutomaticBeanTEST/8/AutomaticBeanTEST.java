package com.example.automaticbean;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Context;

import junit.framework.TestCase;
import powernik.junit5.JUnit4TestRunner;

@RunWith(JUnit4TestRunner.class)
public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtils);
    }

    // Test configure method

    @Test
    public void testConfigure_NullConfig() {
        final AutomaticBean bean = new AutomaticBean();
        assertThrows(NullPointerException.class, () -> bean.configure(null));
    }

    @Test
    public void testConfigure() {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertSame(config, bean.getConfiguration());
    }

    @Test
    public void testConfigure_EmptyConfig() {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertEquals(Collections.emptyList(), config.getAttributeNames());
    }

    @Test
    public void testConfigure_ConfigWithAttributes() {
        final String moduleName = "TestModule";
        final String attributeName1 = "Attribute1";
        final String attributeName2 = "Attribute2";
        final Configuration config = new Configuration();
        config.addAttribute(attributeName1, "Value1");
        config.addAttribute(attributeName2, "Value2");
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        final List<String> attributeNames = Arrays.asList(attributeName1, attributeName2);
        assertEquals(attributeNames, Arrays.asList(bean.configuration.getAttributeNames()));
        assertEquals("Value1", bean.configuration.getAttribute(attributeName1));
        assertEquals("Value2", bean.configuration.getAttribute(attributeName2));
    }

    // Test tryCopyProperty method

    @Test
    public void testTryCopyProperty_NullValue() {
        final AutomaticBean bean = new AutomaticBean();
        assertThrows(NullPointerException.class, () -> bean.tryCopyProperty("ModuleName", "Key", null, true));
    }

    @Test
    public void testTryCopyProperty_NullModuleName() {
        final AutomaticBean bean = new AutomaticBean();
        assertThrows(NullPointerException.class, () -> bean.tryCopyProperty(null, "Key", "Value", true));
    }

    @Test
    public void testTryCopyProperty_NullKey() {
        final AutomaticBean bean = new AutomaticBean();
        assertThrows(NullPointerException.class, () -> bean.tryCopyProperty("ModuleName", null, "Value", true));
    }

    @Test
    public void testTryCopyProperty_NoSuchProperty() {
        final AutomaticBean bean = new AutomaticBean();
        assertThrows(NoSuchMethodException.class, () -> bean.tryCopyProperty("ModuleName", "NonExistentKey", "Value", true));
    }

    @Test
    public void testTryCopyProperty_Success() {
        final AutomaticBean bean = Mockito.spy(new AutomaticBean());
        final BeanUtilsBean beanUtils = Mockito.mock(BeanUtilsBean.class);
        Mockito.when(bean.createBeanUtilsBean()).thenReturn(beanUtils);
        final Object value = new Object();
        Mockito.when(beanUtils.copyProperty(bean, "Key", value)).thenReturn(null);
        try {
            bean.tryCopyProperty("ModuleName", "Key", value, true);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        Mockito.verify(beanUtils).copyProperty(bean, "Key", value);
    }

    @Test
    public void testTryCopyProperty_IllegalValue() {
        final AutomaticBean bean = new AutomaticBean();
        final BeanUtilsBean beanUtils = Mockito.mock(BeanUtilsBean.class);
        Mockito.when(bean.createBeanUtilsBean()).thenReturn(beanUtils);
        final Object value = new Object();
        Mockito.when(beanUtils.copyProperty(bean, "Key", value)).thenThrow(new ConversionException());
        try {
            bean.tryCopyProperty("ModuleName", "Key", value, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }
    }

    // Test contextualize method

    @Test
    public void testContextualize_NullContext() {
        final AutomaticBean bean = new AutomaticBean();
        assertThrows(NullPointerException.class, () -> bean.contextualize(null));
    }

    @Test
    public void testContextualize() {
        final Context context = Mockito.mock(Context.class);
        final AutomaticBean bean = new AutomaticBean();
        bean.contextualize(context);
        final Configuration config = bean.getConfiguration();
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "configuration");
        Mockito.verify(context).get(eq("Attribute1"));
        Mockito.verify(context).get(eq("Attribute2"));
        assertEquals(config, bean.getConfiguration());
    }

    @Test
    public void testContextualize_EmptyContext() {
        final Context context = Mockito.mock(Context.class);
        final AutomaticBean bean = new AutomaticBean();
        bean.contextualize(context);
        assertTrue(Collections.emptyList().equals(context.getAttributeNames()));
    }

    @Test
    public void testContextualize_ContextWithAttributes() {
        final Context context = Mockito.mock(Context.class);
        final String attributeName1 = "Attribute1";
        final String attributeName2 = "Attribute2";
        final AutomaticBean bean = new AutomaticBean();
        Mockito.when(context.getAttributeNames()).thenReturn(Arrays.asList(attributeName1, attributeName2));
        Mockito.when(context.getAttribute(attributeName1)).thenReturn("Value1");
        Mockito.when(context.getAttribute(attributeName2)).thenReturn("Value2");
        bean.contextualize(context);
        assertEquals("Value1", bean.configuration.getAttribute(attributeName1));
        assertEquals("Value2", bean.configuration.getAttribute(attributeName2));
    }

    // Test getConfiguration method

    @Test
    public void testGetConfiguration() {
        final AutomaticBean bean = new AutomaticBean();
        final Configuration config = new Configuration();
        bean.configuration = config;
        assertSame(config, bean.getConfiguration());
    }

    // Test finishLocalSetup method

    @Test
    public void testFinishLocalSetup() {
        final AutomaticBean bean = new AutomaticBean();
        try {
            bean.finishLocalSetup();
        } catch (CheckstyleException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    // Test setupChild method

    @Test
    public void testSetupChild_NullChildConfig() {
        final AutomaticBean bean = new AutomaticBean();
        assertThrows(NullPointerException.class, () -> bean.setupChild(null));
    }

    @Test
    public void testSetupChild() {
        final AutomaticBean bean = new AutomaticBean();
        try {
            bean.setupChild(new Configuration());
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }
    }

}