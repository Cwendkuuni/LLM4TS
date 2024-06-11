package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;

public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        final BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtilsBean);
    }

    @Test
    public void testConfigure() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Properties properties = new Properties();
        properties.setProperty("config.attribute1", "value1");
        properties.setProperty("config.attribute2.subAttribute", "value2");
        bean.configure(new Configuration(properties));

        final Configuration config = bean.getConfiguration();
        assertEquals(2, config.size());
        assertEquals("value1", config.getAttribute("attribute1"));
        assertEquals("value2", config.getAttribute("attribute2.subAttribute"));
    }

    @Test
    public void testConfigure_InvalidProperty() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Properties properties = new Properties();
        properties.setProperty("config.invalidProperty", "value");
        assertThrows(CheckstyleException.class, () -> bean.configure(new Configuration(properties)));
    }

    @Test
    public void testContextualize() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Context context = new Context();
        context.setProperty("context.attribute1", "value1");
        context.setProperty("context.attribute2", "value2");

        bean.contextualize(context);

        final Object attribute1 = bean.tryCopyProperty(AutomaticBean.class.getName(), "attribute1", null, false);
        assertEquals("value1", attribute1);

        final Object attribute2 = bean.tryCopyProperty(AutomaticBean.class.getName(), "attribute2", null, false);
        assertEquals("value2", attribute2);
    }

    @Test
    public void testContextualize_InvalidProperty() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Context context = new Context();
        context.setProperty("context.invalidProperty", "value");
        assertThrows(CheckstyleException.class, () -> bean.contextualize(context));
    }

    @Test
    public void testTryCopyProperty_NullValue() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        assertNull(bean.tryCopyProperty("moduleName", "key", null, false));
    }

    @Test
    public void testTryCopyProperty_InvalidValueType() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Object invalidValue = new Object();
        assertThrows(CheckstyleException.class, () -> bean.tryCopyProperty("moduleName", "key", invalidValue, false));
    }

    @Test
    public void testTryCopyProperty_PropertyNotFound() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final String moduleName = "moduleName";
        assertThrows(CheckstyleException.class, () -> {
            bean.tryCopyProperty(moduleName, "key", null, true);
        });
    }

    @Test
    public void testTryCopyProperty_SetProperty() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final String moduleName = "moduleName";
        final String value = "value";
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(bean, "property");
        bean.tryCopyProperty(moduleName, "property", value, true);
        assertEquals(value, descriptor.getReadMethod().invoke(bean));
    }

    @Test
    public void testTryCopyProperty_SetMethod() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final String moduleName = "moduleName";
        final String value = "value";
        final Method method = MethodUtils.getWriteMethod(bean.getClass(), "setProperty", String.class);
        bean.tryCopyProperty(moduleName, "property", value, true);
        assertEquals(value, MethodUtils.invokeMethod(method, bean));
    }

    @Test
    public void testTryCopyProperty_SetField() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final String moduleName = "moduleName";
        final String value = "value";
        final Field field = MethodUtils.getDeclaredField(bean.getClass(), "property", true);
        field.setAccessible(true);
        bean.tryCopyProperty(moduleName, "property", value, true);
        assertEquals(value, field.get(bean));
    }

    @Test
    public void testSetupChild() throws Exception {
        final AutomaticBean bean = new AutomaticBean();
        final Configuration childConfig = new Configuration();
        bean.setupChild(childConfig);
        assertThrows(CheckstyleException.class, () -> bean.setupChild(childConfig));
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        final ConvertUtilsBean cub = new ConvertUtilsBean();
        final BeanUtilsBean beanUtilsBean = new BeanUtilsBean(cub, new PropertyUtilsBean());
        final Object value = "value1, value2, value3";
        final Object convertedValue = beanUtilsBean.getConverter().convert(String[].class, value);
        assertEquals(Arrays.asList("value1", "value2", "value3"), Arrays.asList((String[])convertedValue));
    }
}