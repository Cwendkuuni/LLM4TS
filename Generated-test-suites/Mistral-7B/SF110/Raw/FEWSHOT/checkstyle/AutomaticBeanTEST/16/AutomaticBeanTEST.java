package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.beanutils.Converter;
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        final BeanUtilsBean beanUtils = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtils);
    }

    @Test
    public void testConfigurationSetter() throws Exception {
        final Configuration config = new Configuration();
        final AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertNotNull(bean.getConfiguration());
        assertEquals(config, bean.getConfiguration());
    }

    @Test
    public void testPropertiesCopying() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("name", "John Doe");
        config.addAttribute("age", 30);
        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertSame(config.getAttribute("name"), bean.getName());
        assertEquals(config.getIntAttribute("age"), bean.getAge());
    }

    @Test
    public void testCopyingInvalidProperty() throws Exception {
        final Configuration config = new Configuration();
        config.setName("AutomaticBeanTest");
        AutomaticBean bean = new AutomaticBean();
        assertThrows(CheckstyleException.class, () -> bean.configure(config));
    }

    @Test
    public void testCopyingUnregisteredProperty() throws Exception {
        final Configuration config = new Configuration();
        config.setName("AutomaticBeanTest");
        config.addAttribute("unregisteredProperty", "value");
        AutomaticBean bean = new AutomaticBean();
        assertThrows(CheckstyleException.class, () -> bean.configure(config));
    }

    @Test
    public void testContextualization() throws Exception {
        final Context context = new Context();
        context.putAttribute("name", "John Doe");
        context.putAttribute("age", 30);
        AutomaticBean bean = new AutomaticBean();
        bean.contextualize(context);
        assertSame(context.getAttribute("name"), bean.getName());
        assertEquals(context.getIntAttribute("age"), bean.getAge());
    }

    @Test
    public void testContextualizationWithInvalidAttribute() throws Exception {
        final Context context = new Context();
        context.putAttribute("invalidAttribute", new Object());
        AutomaticBean bean = new AutomaticBean();
        assertThrows(CheckstyleException.class, () -> bean.contextualize(context));
    }

    @Test
    public void testGetConfiguration() throws Exception {
        final Configuration config = new Configuration();
        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        assertSame(config, bean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        assertDoesNotThrow(() -> bean.finishLocalSetup());
    }

    @Test
    public void testSetupChildWithNullConfiguration() throws Exception {
        AutomaticBean bean = new AutomaticBean();
        assertThrows(CheckstyleException.class, () -> bean.setupChild(null));
    }

    @Test
    public void testRelaxedStringArrayConverter() throws Exception {
        final Configuration config = new Configuration();
        config.addAttribute("stringArray", "value1, value2, value3");
        AutomaticBean bean = new AutomaticBean();
        bean.configure(config);
        final String[] result = bean.getConfiguration().getStringArray("stringArray");
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, result);
    }
}