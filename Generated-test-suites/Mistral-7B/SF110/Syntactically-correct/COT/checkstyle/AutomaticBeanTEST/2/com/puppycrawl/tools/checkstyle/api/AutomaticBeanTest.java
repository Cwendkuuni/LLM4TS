package com.example.junit.test;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
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
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws CheckstyleException {
        Configuration config = Mockito.mock(Configuration.class);
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        assertNotNull(automaticBean.getConfiguration());
        assertEquals(config, automaticBean.getConfiguration());

        String[] attributeNames = config.getAttributeNames();
        for (String key : attributeNames) {
            Object value = config.getAttribute(key);
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), key, value, true);
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithNullConfig() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(null);
    }

    @Test
    public void testTryCopyPropertyWithExistingProperty() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        String propertyName = "nonexistent";
        Object value = "testValue";
        PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, propertyName);
        BeanUtilsBean beanUtils = Mockito.spy(new BeanUtilsBean());
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), propertyName, value, true);
        Mockito.verify(beanUtils, Mockito.times(1)).copyProperty(automaticBean, propertyName, value);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullPropertyName() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), null, "testValue", true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNonExistentProperty() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        String propertyName = "nonexistent";
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), propertyName, "testValue", true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithNullValue() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        String propertyName = "testProperty";
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), propertyName, null, true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidValueType() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        String propertyName = "testProperty";
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), propertyName, 123, true);
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = Mockito.mock(Context.class);
        Map<String, Object> attributes = Arrays.asList("key1", "value1", "key2", "value2").stream()
                .collect(Collectors.toMap(String::toString, String::toString));
        Mockito.when(context.getAttributeNames()).thenReturn(attributes.keySet().toArray(new String[0]));

        for (String key : attributes.keySet()) {
            Object value = attributes.get(key);
            automaticBean.contextualize(context);
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), key, value, false);
        }
    }

    @Test
    public void testGetConfiguration() {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration config = new Configuration();
        automaticBean.configure(config);
        assertEquals(config, automaticBean.getConfiguration());
    }

    @Test(expected = CheckstyleException.class)
    public void testSetUpChildWithNullChildConfig() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration childConfig = null;
        automaticBean.setupChild(childConfig);
    }

    @Test(expected = CheckstyleException.class)
    public void testSetUpChildWithValidChildConfig() throws CheckstyleException {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration childConfig = new Configuration();
        automaticBean.setupChild(childConfig);
    }

}