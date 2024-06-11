package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.RelaxedStringArrayConverter;
import org.junit.Test;

public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtils);
    }

    // Test configure method

    @Test
    public void testConfigure_noConfiguration() {
        final AutomaticBean bean = new AutomaticBean();
        assertThrows(CheckstyleException.class, () -> bean.configure(null));
    }

    @Test
    public void testConfigure_emptyConfiguration() {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertNull(bean.getConfiguration());
    }

    @Test
    public void testConfigure_configurationWithAttributes() {
        final Configuration config = new Configuration();
        config.addAttribute("test.attribute", "test value");
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        final Configuration config2 = bean.getConfiguration();
        assertEquals("test value", config2.getAttribute("test.attribute"));
    }

    @Test
    public void testConfigure_configurationWithChildren() {
        final Configuration config = new Configuration();
        final Configuration childConfig = new Configuration();
        childConfig.addAttribute("test.childAttribute", "test child value");
        config.addChild(childConfig);
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        final Configuration childConfig2 = bean.getChildConfig("test");
        assertEquals("test child value", childConfig2.getAttribute("test.childAttribute"));
    }

    @Test
    public void testConfigure_recheckingProperty() {
        final Configuration config = new Configuration();
        config.addAttribute("test.property", new Object() {});
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "test.property");
        assertNotNull(descriptor);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_propertyDoesNotExist() {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        PropertyUtils.getPropertyDescriptor(bean, "nonexistent.property");
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_propertyValueIsInvalid() {
        final Configuration config = new Configuration();
        config.addAttribute("test.property", new Integer(256));
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        final Method setTestPropertyMethod = ReflectionUtils.findSetterMethod(bean.getClass(), "testProperty");
        setTestPropertyMethod.invoke(bean, new Integer(128));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_propertyValueIsInvalid_recheck() {
        final Configuration config = new Configuration();
        config.addAttribute("test.property", new Integer(256));
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        bean.tryCopyProperty(bean.getClass().getName(), "test.property", new Integer(128), true);
    }

    // Test contextualize method

    @Test
    public void testContextualize_noContext() {
        final AutomaticBean bean = new AutomaticBean();
        assertThrows(CheckstyleException.class, () -> bean.contextualize(null));
    }

    @Test
    public void testContextualize_emptyContext() {
        final AutomaticBean bean = new AutomaticBean();
        bean.contextualize(Collections.emptyMap());
        assertNull(bean.getConfiguration());
    }

    @Test
    public void testContextualize_contextWithAttributes() {
        final AutomaticBean bean = new AutomaticBean();
        final Map<String, Object> context = new HashMap<>();
        context.put("test.property", new Object() {});
        bean.contextualize(context);
        final Object propertyValue = bean.getConfiguration().getAttribute("test.property");
        assertEquals(propertyValue, bean.getConfiguration().getAttribute("test.property"));
    }

    @Test
    public void testContextualize_propertyDoesNotExist() {
        final AutomaticBean bean = new AutomaticBean();
        final Map<String, Object> context = new HashMap<>();
        context.put("nonexistent.property", new Object());
        bean.contextualize(context);
        assertNull(bean.getConfiguration().getAttribute("nonexistent.property"));
    }

    // Test tryCopyProperty method

    @Test
    public void testTryCopyProperty_propertyExists() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("test.property", "test value");
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        bean.tryCopyProperty(bean.getClass().getName(), "test.property", "new test value", false);
        assertEquals("new test value", bean.getConfiguration().getAttribute("test.property"));
    }

    @Test
    public void testTryCopyProperty_propertyDoesNotExist() {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertThrows(CheckstyleException.class, () -> bean.tryCopyProperty(bean.getClass().getName(), "nonexistent.property", "value", false));
    }

    @Test
    public void testTryCopyProperty_propertyIsFinal() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        final Field finalField = bean.getClass().getDeclaredField("finalField");
        finalField.setAccessible(true);
        finalField.set(bean, "original value");
        assertThrows(CheckstyleException.class, () -> bean.tryCopyProperty(bean.getClass().getName(), "finalField", "new value", false));
    }

    @Test
    public void testTryCopyProperty_propertyIsNotWritable() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        final Field nonWritableField = AutomaticBean.class.getDeclaredField("nonWritableField");
        nonWritableField.setAccessible(true);
        nonWritableField.set(bean, "original value");
        assertThrows(CheckstyleException.class, () -> bean.tryCopyProperty(bean.getClass().getName(), "nonWritableField", "new value", false));
    }

    @Test
    public void testTryCopyProperty_propertyIsNotAssignable() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        final Field nonAssignableField = AutomaticBean.class.getDeclaredField("nonAssignableField");
        nonAssignableField.setAccessible(true);
        final Object nonAssignableValue = new Object();
        nonAssignableField.set(bean, nonAssignableValue);
        assertThrows(IllegalArgumentException.class, () -> bean.tryCopyProperty(bean.getClass().getName(), "nonAssignableField", "new value", false));
    }

    // Test getConfiguration method

    @Test
    public void testGetConfiguration() {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        final Configuration config2 = bean.getConfiguration();
        assertEquals(config, config2);
    }

    // Test finishLocalSetup method

    @Test
    public void testFinishLocalSetup() {
        final AutomaticBean bean = new AutomaticBean();
        assertDoesNotThrow(bean::finishLocalSetup);
    }

    // Test setupChild method

    @Test(expected = CheckstyleException.class)
    public void testSetupChild_childIsNotNull() {
        final Configuration parentConfig = new Configuration();
        final Configuration childConfig = new Configuration();
        final AutomaticBean parentBean = new AutomaticBean();
        parentBean.configure(parentConfig);
        parentBean.setupChild(childConfig);
    }
}