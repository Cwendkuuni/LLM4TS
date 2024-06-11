package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
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
import org.apache.commons.beanutils.converters.StringArrayConverter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        final BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtilsBean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfigureNullConfiguration() {
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(null);
    }

    @Test
    public void testConfigureEmptyConfiguration() {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration emptyConfig = new Configuration();
        automaticBean.configure(emptyConfig);
    }

    @Test
    public void testConfigureWithAttributes() {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.addAttribute("test1", "value1");
        config.addAttribute("test2", "value2");
        automaticBean.configure(config);
        assertNotNull(automaticBean.getConfiguration());
        assertEquals(2, automaticBean.getConfiguration().size());
        assertArrayEquals(new String[]{"test1", "test2"}, automaticBean.getConfiguration().getAttributeNames());
        assertEquals("value1", automaticBean.getConfiguration().getAttribute("test1"));
        assertEquals("value2", automaticBean.getConfiguration().getAttribute("test2"));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithInvalidAttribute() {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.addAttribute("test", new Object());
        automaticBean.configure(config);
        fail("Should throw an exception");
    }

    @Test
    public void testTryCopyPropertyWithExistingProperty() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.setAttribute("test1", "value1");
        automaticBean.configure(config);
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "test1");
        assertNotNull(descriptor);
        final Object originalValue = descriptor.getReadMethod().invoke(automaticBean);
        assertEquals("value1", originalValue);
        final Object newValue = new Object();
        final Object copiedValue = automaticBean.tryCopyProperty("ModuleName", "test1", newValue, true);
        assertNull(copiedValue);
        final Object readValue = descriptor.getReadMethod().invoke(automaticBean);
        assertEquals("value1", readValue);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNonExistentProperty() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.setAttribute("test1", "value1");
        automaticBean.configure(config);
        try {
            automaticBean.tryCopyProperty("ModuleName", "test2", new Object(), true);
            fail("Should throw an exception");
        } catch (CheckstyleException ex) {
            // Expected
        }
    }

    @Test
    public void testTryCopyPropertyWithInvalidType() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.setAttribute("test1", "value1");
        automaticBean.configure(config);
        try {
            automaticBean.tryCopyProperty("ModuleName", "test1", new Integer(1), false);
            fail("Should throw an exception");
        } catch (CheckstyleException ex) {
            // Expected
        }
    }

    @Test
    public void testTryCopyPropertyWithValidType() throws Exception {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.setAttribute("test1", "1");
        automaticBean.configure(config);
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "test1");
        assertNotNull(descriptor);
        final Object originalValue = descriptor.getReadMethod().invoke(automaticBean);
        assertEquals(1, originalValue);
        final int newValue = 2;
        final Object copiedValue = automaticBean.tryCopyProperty("ModuleName", "test1", newValue, true);
        assertEquals(newValue, copiedValue);
        final Object readValue = descriptor.getReadMethod().invoke(automaticBean);
        assertEquals(newValue, readValue);
    }

    @Test
    public void testTryCopyPropertyWithParentProperty() throws Exception {
        final Configuration parentConfig = new Configuration();
        parentConfig.setAttribute("parentTest1", "parentValue1");
        final Configuration childConfig = new Configuration();
        childConfig.setParent(parentConfig);
        childConfig.setAttribute("test1", "value1");
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(childConfig);
        final PropertyDescriptor parentDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "parentTest1");
        final PropertyDescriptor childDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "test1");
        assertNotNull(parentDescriptor);
        assertNotNull(childDescriptor);
        final Object originalParentValue = parentDescriptor.getReadMethod().invoke(automaticBean);
        assertEquals("parentValue1", originalParentValue);
        final Object originalChildValue = childDescriptor.getReadMethod().invoke(automaticBean);
        assertEquals("value1", originalChildValue);
        final Object newParentValue = "newParentValue";
        final Object newChildValue = 2;
        automaticBean.tryCopyProperty("ModuleName", "parentTest1", newParentValue, true);
        final Object readParentValue = parentDescriptor.getReadMethod().invoke(automaticBean);
        assertEquals(newParentValue, readParentValue);
        assertSame(newParentValue, automaticBean.getConfiguration().getAttribute("parentTest1"));
        automaticBean.tryCopyProperty("ModuleName", "test1", newChildValue, true);
        final Object readChildValue = childDescriptor.getReadMethod().invoke(automaticBean);
        assertEquals(newChildValue, readChildValue);
        assertSame(newChildValue, automaticBean.getConfiguration().getAttribute("test1"));
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidParentProperty() throws Exception {
        final Configuration parentConfig = new Configuration();
        parentConfig.setAttribute("parentTest1", "parentValue1");
        final Configuration childConfig = new Configuration();
        childConfig.setParent(parentConfig);
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(childConfig);
        try {
            automaticBean.tryCopyProperty("ModuleName", "test2", new Object(), true);
            fail("Should throw an exception");
        } catch (CheckstyleException ex) {
            // Expected
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidParentType() throws Exception {
        final Configuration parentConfig = new Configuration();
        parentConfig.setAttribute("parentTest1", "parentValue1");
        final Configuration childConfig = new Configuration();
        childConfig.setParent(parentConfig);
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(childConfig);
        try {
            automaticBean.tryCopyProperty("ModuleName", "parentTest1", new Integer(1), false);
            fail("Should throw an exception");
        } catch (CheckstyleException ex) {
            // Expected
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNonEmptyConfiguration() throws Exception {
        final Configuration childConfig = new Configuration();
        childConfig.setAttribute("test1", "value1");
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(new Configuration());
        try {
            automaticBean.setupChild(childConfig);
            fail("Should throw an exception");
        } catch (CheckstyleException ex) {
            // Expected
        }
    }

    @Test
    public void testGetConfiguration() {
        final AutomaticBean automaticBean = new AutomaticBean();
        assertNull(automaticBean.getConfiguration());
        final Configuration config = new Configuration();
        automaticBean.configure(config);
        assertSame(config, automaticBean.getConfiguration());
    }

    @Test(expected = CheckstyleException.class)
    public void testFinishLocalSetupWithNonEmptyConfiguration() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        try {
            automaticBean.finishLocalSetup();
            fail("Should throw an exception");
        } catch (CheckstyleException ex) {
            // Expected
        }
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.setAttribute("test1", "value1,value2,value3");
        automaticBean.configure(config);
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "test1");
        assertNotNull(descriptor);
        final Object originalValue = descriptor.getReadMethod().invoke(automaticBean);
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, (String[])originalValue);
    }

    @Test
    public void testContextualizeEmptyContext() {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Context context = PowerMockito.mock(Context.class);
        automaticBean.contextualize(context);
    }

    @Test
    public void testContextualizeWithAttributes() {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Context context = PowerMockito.mock(Context.class);
        final Configuration config = new Configuration();
        config.addAttribute("test1", "value1");
        config.addAttribute("test2", "value2");
        PowerMockito.when(context.get(eq("test1"))).thenReturn("value1");
        PowerMockito.when(context.get(eq("test2"))).thenReturn("value2");
        PowerMockito.when(context.getAttributeNames()).thenReturn(Arrays.asList("test1", "test2"));
        automaticBean.contextualize(context);
        assertSame(config, automaticBean.getConfiguration());
        assertArrayEquals(new String[]{"test1", "test2"}, automaticBean.getConfiguration().getAttributeNames());
        assertEquals("value1", automaticBean.getConfiguration().getAttribute("test1"));
        assertEquals("value2", automaticBean.getConfiguration().getAttribute("test2"));
    }

    @Test
    public void testContextualizeWithParentContext() {
        final Configuration parentConfig = new Configuration();
        parentConfig.setAttribute("parentTest1", "parentValue1");
        final Configuration childConfig = new Configuration();
        childConfig.setParent(parentConfig);
        childConfig.addAttribute("test1", "value1");
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(childConfig);
        final Context context = PowerMockito.mock(Context.class);
        PowerMockito.when(context.get(eq("parentTest1"))).thenReturn("parentValue1");
        PowerMockito.when(context.get(eq("test1"))).thenReturn("value1");
        PowerMockito.when(context.getAttributeNames()).thenReturn(Arrays.asList("parentTest1", "test1"));
        automaticBean.contextualize(context);
        assertSame(parentConfig, automaticBean.getConfiguration());
        assertArrayEquals(new String[]{"parentTest1", "test1"}, automaticBean.getConfiguration().getAttributeNames());
        assertEquals("parentValue1", automaticBean.getConfiguration().getAttribute("parentTest1"));
        assertEquals("value1", automaticBean.getConfiguration().getAttribute("test1"));
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeWithInvalidAttribute() {
        final AutomaticBean automaticBean = new AutomaticBean();
        final Context context = PowerMockito.mock(Context.class);
        PowerMockito.when(context.get(eq("test1"))).thenReturn("value1");
        PowerMockito.when(context.getAttributeNames()).thenReturn(Arrays.asList("test1"));
        automaticBean.contextualize(context);
        PowerMockito.verifyStatic();
        throw new CheckstyleException(eq("test2"), anyString());
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeWithNullContext() {
        final AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.contextualize(null);
        fail("Should throw an exception");
    }

}