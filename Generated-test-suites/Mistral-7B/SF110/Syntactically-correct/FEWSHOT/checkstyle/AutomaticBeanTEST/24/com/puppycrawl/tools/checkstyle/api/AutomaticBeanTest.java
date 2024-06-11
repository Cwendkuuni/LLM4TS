package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
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
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

public class AutomaticBeanTest {

    @Test
    public void testCreateBeanUtilsBean() {
        BeanUtilsBean beanUtilsBean = AutomaticBean.createBeanUtilsBean();
        assertNotNull(beanUtilsBean);
        ConvertUtilsBean cub = (ConvertUtilsBean)beanUtilsBean.getConvertUtils();
        assertNotNull(cub);
        PropertyUtilsBean pub = (PropertyUtilsBean)beanUtilsBean.getPropertyUtils();
        assertNotNull(pub);
    }

    @Test
    public void testConfiguration() {
        Configuration configuration = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);
        assertSame(configuration, automaticBean.getConfiguration());
    }

    @Test
    public void testConfigureWithNullConfig() {
        AutomaticBean automaticBean = new AutomaticBean();
        assertThrows(CheckstyleException.class, () -> automaticBean.configure(null));
    }

    @Test
    public void testConfigureWithEmptyConfig() {
        Configuration configuration = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);
        assertNull(automaticBean.getConfiguration());
    }

    @Test
    public void testConfigureWithAttributes() {
        Configuration configuration = new Configuration();
        configuration.addAttribute("test.attribute", "test value");
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);
        Configuration config = automaticBean.getConfiguration();
        assertEquals(1, config.getAttributeNames().length);
        String attributeName = config.getAttributeNames()[0];
        assertEquals("test.attribute", attributeName);
        assertEquals("test value", config.getAttribute(attributeName));
    }

    @Test
    public void testConfigureWithChildren() {
        Configuration parentConfig = new Configuration();
        Configuration childConfig1 = new Configuration();
        Configuration childConfig2 = new Configuration();
        parentConfig.addChild(childConfig1);
        parentConfig.addChild(childConfig2);
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(parentConfig);
        assertEquals(2, automaticBean.getConfiguration().getChildren().length);
    }

    @Test
    public void testTryCopyPropertyWithExistingProperty() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        String propertyName = "testProperty";
        Object propertyValue = "test value";
        Configuration configuration = new Configuration();
        configuration.addAttribute(propertyName, propertyValue);
        automaticBean.configure(configuration);
        Object beanPropertyValue = MethodUtils.invokeMethod(automaticBean, "get" + StringUtils.capitalize(propertyName));
        assertSame(propertyValue, beanPropertyValue);
    }

    @Test
    public void testTryCopyPropertyWithMissingProperty() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        String propertyName = "missingProperty";
        Configuration configuration = new Configuration();
        automaticBean.configure(configuration);
        assertThrows(CheckstyleException.class, () -> automaticBean.tryCopyProperty(AutomaticBean.class.getName(), propertyName, null, true));
    }

    @Test
    public void testTryCopyPropertyWithInvalidType() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        String propertyName = "testProperty";
        Object propertyValue = 123;
        Configuration configuration = new Configuration();
        automaticBean.configure(configuration);
        assertThrows(IllegalArgumentException.class, () -> automaticBean.tryCopyProperty(AutomaticBean.class.getName(), propertyName, propertyValue, true));
    }

    @Test
    public void testTryCopyPropertyWithInvalidValue() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        String propertyName = "testProperty";
        Object propertyValue = "invalid value";
        Configuration configuration = new Configuration();
        automaticBean.configure(configuration);
        assertThrows(ConversionException.class, () -> automaticBean.tryCopyProperty(AutomaticBean.class.getName(), propertyName, propertyValue, true));
    }

    @Test
    public void testTryCopyPropertyWithRecheck() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        String propertyName = "testProperty";
        Object propertyValue = "test value";
        Configuration configuration = new Configuration();
        configuration.addAttribute(propertyName, propertyValue);
        automaticBean.configure(configuration);
        assertSame(propertyValue, MethodUtils.invokeMethod(automaticBean, "get" + StringUtils.capitalize(propertyName)));
        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, propertyName);
        assertNotNull(propertyDescriptor);
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), propertyName, propertyValue, true);
        assertSame(propertyValue, MethodUtils.invokeMethod(automaticBean, "get" + StringUtils.capitalize(propertyName)));
    }

    @Test
    public void testContextualizeWithEmptyContext() {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = new Context();
        automaticBean.contextualize(context);
    }

    @Test
    public void testContextualizeWithAttributes() {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = new Context();
        context.put("test.attribute", "test value");
        automaticBean.contextualize(context);
        Configuration configuration = automaticBean.getConfiguration();
        assertEquals(1, configuration.getAttributeNames().length);
        String attributeName = configuration.getAttributeNames()[0];
        assertEquals("test.attribute", attributeName);
        assertEquals("test value", configuration.getAttribute(attributeName));
    }

    @Test
    public void testTryCopyPropertyWithContext() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        String propertyName = "testProperty";
        Object propertyValue = "test value";
        Context context = new Context();
        context.put(propertyName, propertyValue);
        automaticBean.contextualize(context);
        Object beanPropertyValue = MethodUtils.invokeMethod(automaticBean, "get" + StringUtils.capitalize(propertyName));
        assertSame(propertyValue, beanPropertyValue);
    }

    @Test
    public void testTryCopyPropertyWithMissingContextProperty() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        String propertyName = "testProperty";
        Context context = new Context();
        automaticBean.contextualize(context);
        assertNull(MethodUtils.invokeMethod(automaticBean, "get" + StringUtils.capitalize(propertyName)));
    }

    @Test
    public void testGetConfiguration() {
        AutomaticBean automaticBean = new AutomaticBean();
        assertNull(automaticBean.getConfiguration());
        Configuration configuration = new Configuration();
        automaticBean.configure(configuration);
        assertSame(configuration, automaticBean.getConfiguration());
    }

    @Test
    public void testFinishLocalSetup() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        assertDoesNotThrow(automaticBean::finishLocalSetup);
    }

    @Test
    public void testSetupChildWithNullChildConfig() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration childConfig = null;
        assertThrows(CheckstyleException.class, () -> automaticBean.setupChild(childConfig));
    }

    @Test
    public void testSetupChildWithValidChildConfig() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration childConfig = new Configuration();
        automaticBean.setupChild(childConfig);
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        AutomaticBean automaticBean = new AutomaticBean();
        String testValue = "test, value1, value2";
        Object result = MethodUtils.invokeMethod(automaticBean, "convert", String[].class, testValue);
        assertArrayEquals(new String[]{"test", "value1", "value2"}, (String[])result);
    }

}