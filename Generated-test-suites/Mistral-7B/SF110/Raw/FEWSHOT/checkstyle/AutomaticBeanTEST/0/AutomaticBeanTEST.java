package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtils);
    }

    @Test
    public void testConfiguration() {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertSame(config, bean.getConfiguration());
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithNullConfig() {
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(null);
    }

    @Test
    public void testTryCopyPropertyWithExistingProperty() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final String key = "test";
        final Object value = "testValue";
        final Configuration config = new Configuration();
        config.addAttribute(key, value);
        bean.configure(config);
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, key);
        assertTrue(descriptor != null);
        assertSame(value, descriptor.getReadMethod().invoke(bean));
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullKey() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Configuration config = new Configuration();
        bean.configure(config);
        bean.tryCopyProperty(null, null, null, true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidKey() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Configuration config = new Configuration();
        bean.configure(config);
        bean.tryCopyProperty("InvalidKey", null, null, true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidType() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Configuration config = new Configuration();
        config.addAttribute("test", new Object());
        bean.configure(config);
        bean.tryCopyProperty("test", "test", new Object(), true);
    }

    @Test(expected = InvocationTargetException.class)
    public void testTryCopyPropertyWithInvalidSetter() throws Exception {
        final AutomaticBean bean = new AutomaticBean() {
            private List<String> test;

            @SuppressWarnings("unused")
            private void setTest(final List<String> value) {
                // empty setter
            }
        };
        final Configuration config = new Configuration();
        config.addAttribute("test", Collections.singletonList("testValue"));
        bean.configure(config);
        bean.tryCopyProperty("test", "test", Arrays.asList("testValue"), true);
    }

    @Test(expected = IllegalAccessException.class)
    public void testTryCopyPropertyWithPrivateSetter() throws Exception {
        final AutomaticBean bean = new AutomaticBean() {
            private List<String> test;

            @SuppressWarnings("unused")
            private void setTest(final List<String> value) {
                this.test = value;
            }
        };
        final Configuration config = new Configuration();
        config.addAttribute("test", Collections.singletonList("testValue"));
        bean.configure(config);
        bean.tryCopyProperty("test", "test", Arrays.asList("testValue"), true);
        final Field field = bean.getClass().getDeclaredField("test");
        field.setAccessible(true);
        assertNotNull(field.get(bean));
    }

    @Test(expected = NoSuchMethodException.class)
    public void testTryCopyPropertyWithMissingSetter() throws Exception {
        final AutomaticBean bean = new AutomaticBean() {
            private List<String> test;
        };
        final Configuration config = new Configuration();
        config.addAttribute("test", Collections.singletonList("testValue"));
        bean.configure(config);
        bean.tryCopyProperty("test", "test", Arrays.asList("testValue"), true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullValue() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Configuration config = new Configuration();
        bean.configure(config);
        bean.tryCopyProperty("test", "test", null, true);
    }

    @Test(expected = ConversionException.class)
    public void testTryCopyPropertyWithInvalidValue() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Configuration config = new Configuration();
        bean.configure(config);
        bean.tryCopyProperty("test", "test", new Object(), true);
    }

    @Test
    public void testContextualize() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Context context = new Context();
        context.setAttributes(Map.of("test", "testValue"));
        bean.contextualize(context);
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "test");
        assertSame("testValue", descriptor.getReadMethod().invoke(bean));
    }

    @Test
    public void testGetConfiguration() {
        final AutomaticBean bean = new AutomaticBean();
        assertNull(bean.getConfiguration());

        final Configuration config = new Configuration();
        bean.configure(config);
        assertSame(config, bean.getConfiguration());
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithNullChildConf() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        bean.setupChild(null);
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithInvalidChildConfType() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        bean.setupChild(new Object());
    }
}