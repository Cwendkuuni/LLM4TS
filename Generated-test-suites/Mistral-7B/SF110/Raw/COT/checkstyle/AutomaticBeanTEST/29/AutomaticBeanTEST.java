package com.example.automaticbean;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.InvocationTargetException;
import org.apache.commons.beanutils.NoSuchMethodException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Context;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AutomaticBean.class, BeanUtilsBean.class, PropertyUtils.class})
public class AutomaticBeanTest {

    @Test
    public void testConfigureSuccess() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        PowerMockito.when(config.getAttributeNames()).thenReturn(new String[]{"testKey"});
        PowerMockito.when(config.getAttribute("testKey")).thenReturn("testValue");

        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);

        assertNotNull(bean.getConfiguration());
        assertEquals("testValue", bean.getConfiguration().getAttribute("testKey"));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigurePropertyNotFound() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        PowerMockito.when(config.getAttributeNames()).thenReturn(new String[]{"testKey"});

        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        String[] attributeNames = bean.getConfiguration().getAttributeNames();
        assertFalse(Arrays.asList(attributeNames).contains("nonexistentProperty"));

        try {
            bean.tryCopyProperty(bean.getClass().getName(), "nonexistentProperty", null, true);
            fail("Should throw CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidType() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        PowerMockito.when(config.getAttributeNames()).thenReturn(new String[]{"testKey"});
        PowerMockito.when(config.getAttribute("testKey")).thenReturn(123);

        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);

        try {
            bean.tryCopyProperty(bean.getClass().getName(), "testKey", null, true);
            fail("Should throw CheckstyleException with incorrect type message");
        } catch (CheckstyleException e) {
            assertEquals("Property 'testKey' in module " + bean.getClass().getName() + " does not exist, please check the documentation", e.getMessage());
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureSetPropertyFailure() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        PowerMockito.when(config.getAttributeNames()).thenReturn(new String[]{"testKey"});
        PowerMockito.when(config.getAttribute("testKey")).thenReturn("testValue");

        AutomaticBean bean = new AutomaticBean();
        PowerMockito.mockStatic(PropertyUtils.class);
        PowerMockito.when(PropertyUtils.getPropertyDescriptor(bean, "testKey")).thenThrow(new NoSuchMethodException());

        try {
            bean.configure(config);
            fail("Should throw CheckstyleException with property not found message");
        } catch (CheckstyleException e) {
            assertEquals("Property 'testKey' in module " + bean.getClass().getName() + " does not exist, please check the documentation", e.getMessage());
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureIllegalValue() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        PowerMockito.when(config.getAttributeNames()).thenReturn(new String[]{"testKey"});
        PowerMockito.when(config.getAttribute("testKey")).thenReturn("not a boolean");

        AutomaticBean bean = new AutomaticBean();
        try {
            bean.tryCopyProperty(bean.getClass().getName(), "testKey", "not a boolean", true);
            fail("Should throw CheckstyleException with illegal value message");
        } catch (CheckstyleException e) {
            assertEquals("illegal value 'not a boolean' for property 'testKey' of module " + bean.getClass().getName(), e.getMessage());
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureIllegalTargetType() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        PowerMockito.when(config.getAttributeNames()).thenReturn(new String[]{"testKey"});
        PowerMockito.when(config.getAttribute("testKey")).thenReturn("testValue");

        AutomaticBean bean = new AutomaticBean();
        BeanUtilsBean beanUtilsBean = Mockito.mock(BeanUtilsBean.class);
        PowerMockito.when(BeanUtilsBean.getInstance()).thenReturn(beanUtilsBean);
        PowerMockito.when(beanUtilsBean.copyProperty(Mockito.any(Object.class), Mockito.anyString(), Mockito.anyObject())).thenThrow(new UnsupportedOperationException());

        try {
            bean.configure(config);
            fail("Should throw CheckstyleException with illegal target type message");
        } catch (CheckstyleException e) {
            assertEquals("Cannot set property 'testKey' to 'testValue' in module " + bean.getClass().getName(), e.getMessage());
        }
    }

    @Test
    public void testContextualizeSuccess() throws Exception {
        Context context = Mockito.mock(Context.class);
        PowerMockito.when(context.getAttributeNames()).thenReturn(new String[]{"testKey"});
        PowerMockito.when(context.get("testKey")).thenReturn("testValue");

        AutomaticBean bean = new AutomaticBean();
        bean.contextualize(context);

        assertEquals("testValue", bean.getConfiguration().getAttribute("testKey"));
    }

    @Test
    public void testContextualizeEmptyContext() throws Exception {
        Context context = Mockito.mock(Context.class);

        AutomaticBean bean = new AutomaticBean();
        bean.contextualize(context);

        assertEquals(Collections.emptyMap(), bean.getConfiguration().getAttributeMap());
    }

    @Test
    public void testGetConfiguration() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        Configuration config = new Configuration();
        bean.configure(config);

        Configuration retrievedConfig = bean.getConfiguration();
        assertEquals(config, retrievedConfig);
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        bean.finishLocalSetup();
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildNullConfig() throws Exception {
        AutomaticBean parentBean = new AutomaticBean();

        try {
            parentBean.setupChild(null);
            fail("Should throw CheckstyleException with null child config message");
        } catch (CheckstyleException e) {
            assertEquals("Child config is not allowed to be null", e.getMessage());
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildInvalidChildType() throws Exception {
        AutomaticBean parentBean = new AutomaticBean();
        Configuration childConfig = Mockito.mock(Configuration.class);

        try {
            parentBean.setupChild(childConfig);
            fail("Should throw CheckstyleException with invalid child type message");
        } catch (CheckstyleException e) {
            assertEquals(childConfig.getClass().getName(), e.getMessage());
        }
    }
}