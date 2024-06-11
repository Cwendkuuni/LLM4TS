package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);

        // Test configuration of boolean properties
        config.addAttribute("booleanProperty", true);
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "booleanProperty");
        assertTrue(descriptor.getReadMethod().invoke(bean));

        // Test configuration of String properties
        config.addAttribute("stringProperty", "test");
        descriptor = PropertyUtils.getPropertyDescriptor(bean, "stringProperty");
        assertEquals("test", descriptor.getReadMethod().invoke(bean));

        // Test configuration of array properties
        config.addAttribute("booleanArrayProperty", new Boolean[]{true, false});
        descriptor = PropertyUtils.getPropertyDescriptor(bean, "booleanArrayProperty");
        Object propertyValue = descriptor.getReadMethod().invoke(bean);
        assertArrayEquals(new Boolean[]{true, false}, propertyValue);

        // Test configuration of nested properties
        Configuration childConfig = new Configuration();
        childConfig.addAttribute("stringProperty", "testChild");
        config.addChild(childConfig);
        descriptor = PropertyUtils.getPropertyDescriptor(bean, "childConfig.stringProperty");
        assertEquals("testChild", descriptor.getReadMethod().invoke(bean));
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureInvalidProperty() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "nonExistentProperty");
        assertNull(descriptor);
        bean.tryCopyProperty("moduleName", "nonExistentProperty", 42, true);
    }

    @Test
    public void testContextualize() throws Exception {
        Context context = new Context();
        AutomaticBean bean = new AutomaticBean();
        bean.contextualize(context);

        // Test contextualization of boolean properties
        context.put("booleanProperty", true);
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "booleanProperty");
        assertTrue(descriptor.getReadMethod().invoke(bean));

        // Test contextualization of String properties
        context.put("stringProperty", "test");
        descriptor = PropertyUtils.getPropertyDescriptor(bean, "stringProperty");
        assertEquals("test", descriptor.getReadMethod().invoke(bean));

        // Test contextualization of array properties
        context.put("booleanArrayProperty", Arrays.asList(true, false).toArray(new Boolean[0]));
        descriptor = PropertyUtils.getPropertyDescriptor(bean, "booleanArrayProperty");
        Object propertyValue = descriptor.getReadMethod().invoke(bean);
        assertArrayEquals(new Boolean[]{true, false}, propertyValue);

        // Test contextualization of nested properties
        Configuration childConfig = new Configuration();
        childConfig.addAttribute("stringProperty", "testChild");
        context.put("childConfig", childConfig);
        descriptor = PropertyUtils.getPropertyDescriptor(bean, "childConfig.stringProperty");
        assertEquals("testChild", descriptor.getReadMethod().invoke(bean));
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualizeInvalidProperty() throws Exception {
        Context context = new Context();
        AutomaticBean bean = new AutomaticBean();
        bean.contextualize(context);
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "nonExistentProperty");
        assertNull(descriptor);
        bean.tryCopyProperty(AutomaticBean.class.getName(), "nonExistentProperty", 42, false);
    }

    @Test
    public void testGetConfiguration() {
        AutomaticBean bean = new AutomaticBean();
        Configuration config = bean.getConfiguration();
        assertNotNull(config);
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        bean.finishLocalSetup();
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        Configuration childConfig = new Configuration();
        bean.setupChild(childConfig);
    }
}