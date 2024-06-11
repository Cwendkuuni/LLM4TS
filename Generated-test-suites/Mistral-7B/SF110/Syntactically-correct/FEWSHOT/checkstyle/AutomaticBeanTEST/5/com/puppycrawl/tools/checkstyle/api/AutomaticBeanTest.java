package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;

public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtils);
    }

    @Test
    public void testConfigure() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertNotNull(bean.getConfiguration());
        assertEquals(config, bean.getConfiguration());
    }

    @Test
    public void testTryCopyPropertyWithExistingProperty() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        config.addAttribute("myProperty", "test");
        bean.configure(config);
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "myProperty");
        assertNotNull(descriptor);
        assertEquals("test", descriptor.getReadMethod().invoke(bean));
    }

    @Test
    public void testTryCopyPropertyWithInvalidProperty() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        config.addAttribute("nonExistentProperty", "test");
        assertThrows(CheckstyleException.class, () -> bean.configure(config));
    }

    @Test
    public void testTryCopyPropertyWithInvalidType() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        config.addAttribute("myProperty", new Object() {});
        assertThrows(CheckstyleException.class, () -> bean.configure(config));
    }

    @Test
    public void testTryCopyPropertyWithInvalidValue() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        config.addAttribute("myProperty", 42);
        assertThrows(CheckstyleException.class, () -> bean.configure(config));
    }

    @Test
    public void testTryCopyPropertyWithBooleanValue() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        config.addAttribute("myProperty", true);
        bean.configure(config);
        final Boolean value = (Boolean) PropertyUtils.getProperty(bean, "myProperty");
        assertEquals(true, value);
    }

    @Test
    public void testTryCopyPropertyWithIntegerValue() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        config.addAttribute("myProperty", 42);
        bean.configure(config);
        final Integer value = (Integer) PropertyUtils.getProperty(bean, "myProperty");
        assertEquals(42, value);
    }

    @Test
    public void testTryCopyPropertyWithStringValue() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        config.addAttribute("myProperty", "test");
        bean.configure(config);
        final String value = (String) PropertyUtils.getProperty(bean, "myProperty");
        assertEquals("test", value);
    }

    @Test
    public void testTryCopyPropertyWithArrayValue() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        config.addAttribute("myProperty", new String[]{"test1", "test2"});
        bean.configure(config);
        final String[] value = (String[]) PropertyUtils.getProperty(bean, "myProperty");
        assertArrayEquals(new String[]{"test1", "test2"}, value);
    }

    @Test
    public void testContextualize() throws Exception {
        final Context context = new Context();
        final AutomaticBean bean = new AutomaticBean();
        context.put("myProperty", "test");
        bean.contextualize(context);
        final String value = (String) PropertyUtils.getProperty(bean, "myProperty");
        assertEquals("test", value);
    }

    @Test
    public void testContextualizeWithEmptyContext() throws Exception {
        final Context context = new Context();
        final AutomaticBean bean = new AutomaticBean();
        bean.contextualize(context);
        assertNull(PropertyUtils.getProperty(bean, "myProperty"));
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        assertDoesNotThrow(bean::finishLocalSetup);
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNullChildConfig() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.setupChild(null);
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithValidChildConfig() throws Exception {
        final Configuration childConfig = new Configuration();
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.setupChild(childConfig);
    }
}