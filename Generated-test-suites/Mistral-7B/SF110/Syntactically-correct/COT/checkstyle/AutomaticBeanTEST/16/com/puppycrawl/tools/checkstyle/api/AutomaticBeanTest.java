package com.example.automaticbean.test;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.convert.BooleanConverter;
import org.apache.commons.beanutils.convert.ByteConverter;
import org.apache.commons.beanutils.convert.CharacterConverter;
import org.apache.commons.beanutils.convert.DoubleConverter;
import org.apache.commons.beanutils.convert.FloatConverter;
import org.apache.commons.beanutils.convert.IntegerConverter;
import org.apache.commons.beanutils.convert.LongConverter;
import org.apache.commons.beanutils.convert.ShortConverter;
import org.apache.commons.beanutils.converters.RelaxedStringArrayConverter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.common.collect.Lists;

import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Context;

@RunWith(org.junit.runners.JUnit4.class)
public class AutomaticBeanTest {

    // Test configure method

    @Test
    public void testConfigure() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        AutomaticBean automaticBean = new AutomaticBean();

        String[] attributeNames = config.getAttributeNames();
        Mockito.when(config.getAttributeNames()).thenReturn(attributeNames);
        String[] attributes = Arrays.copyOf(attributeNames, attributeNames.length);

        for (String key : attributeNames) {
            Object value = new Object() {};
            Mockito.doReturn(value).when(config).getAttribute(key);
            automaticBean.configure(config);
            Object propertyValue = BeanUtils.getPropertyValue(automaticBean, key);
            assertEquals(value, propertyValue);
        }
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigureWithInvalidAttributeName() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        AutomaticBean automaticBean = new AutomaticBean();

        String invalidAttributeName = "invalid_attribute_name";
        Mockito.when(config.getAttributeNames()).thenReturn(new String[]{"attribute1", "attribute2"});
        Mockito.when(config.getAttribute(invalidAttributeName)).thenReturn(null);

        automaticBean.configure(config);

        // Check for specific exception message
        String expectedMessage = "Property 'invalid_attribute_name' in module null does not exist, please check the documentation";
        String actualMessage = "";
        try {
            Object invalidPropertyValue = BeanUtils.getPropertyValue(automaticBean, invalidAttributeName);
            actualMessage = invalidPropertyValue.toString();
        } catch (Exception e) {
            actualMessage = e.getMessage();
        }
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testConfigureWithChildConfiguration() throws Exception {
        Configuration config = Mockito.mock(Configuration.class);
        Configuration childConfig = Mockito.mock(Configuration.class);
        AutomaticBean automaticBean = new AutomaticBean();

        String[] attributeNames = config.getAttributeNames();
        Mockito.when(config.getAttributeNames()).thenReturn(attributeNames);
        String[] attributes = Arrays.copyOf(attributeNames, attributeNames.length);
        Mockito.when(config.getChildren()).thenReturn(new Configuration[]{childConfig});

        String childConfigName = "child_config";
        Mockito.when(childConfig.getName()).thenReturn(childConfigName);

        for (String key : attributeNames) {
            Object value = new Object() {};
            Mockito.doReturn(value).when(config).getAttribute(key);
            automaticBean.configure(config);
            Object propertyValue = BeanUtils.getPropertyValue(automaticBean, key);
            assertEquals(value, propertyValue);
        }

        String childConfigKey = "child_config_key";
        Object childConfigValue = new Object() {};
        Mockito.doReturn(childConfigValue).when(childConfig).getAttribute(childConfigKey);

        String actualChildConfigValue = "";
        try {
            actualChildConfigValue = (String)BeanUtils.getPropertyValue(automaticBean, childConfigKey);
        } catch (Exception e) {
            fail();
        }
        assertEquals(childConfigValue.toString(), actualChildConfigValue);
    }

    // Test tryCopyProperty method

    @Test
    public void testTryCopyPropertyWithBooleanValue() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Object booleanValue = true;

        PropertyDescriptor propertyDescriptor = Mockito.mock(PropertyDescriptor.class);
        Mockito.when(propertyDescriptor.getReadMethod()).thenReturn(Mockito.doReturn(automaticBean));
        Mockito.when(propertyDescriptor.getName()).thenReturn("booleanProperty");

        BeanUtilsBean beanUtilsBean = Mockito.mock(BeanUtilsBean.class);
        Mockito.when(beanUtilsBean.copyProperty(automaticBean, "booleanProperty", booleanValue)).thenReturn(null);

        automaticBean.tryCopyProperty("AutomaticBean", "booleanProperty", booleanValue, true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidPropertyName() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Object invalidValue = new Object() {};

        String invalidPropertyName = "invalid_property_name";
        PropertyDescriptor propertyDescriptor = Mockito.mock(PropertyDescriptor.class);
        Mockito.when(propertyDescriptor.getReadMethod()).thenReturn(Mockito.doReturn(automaticBean));
        Mockito.when(propertyDescriptor.getName()).thenReturn(invalidPropertyName);

        BeanUtilsBean beanUtilsBean = Mockito.mock(BeanUtilsBean.class);
        Mockito.when(beanUtilsBean.copyProperty(automaticBean, invalidPropertyName, invalidValue)).thenThrow(new CheckstyleException(""));

        try {
            automaticBean.tryCopyProperty("AutomaticBean", invalidPropertyName, invalidValue, true);
        } catch (CheckstyleException e) {
            // Expected exception
        }
    }

    @Test
    public void testTryCopyPropertyWithStringValue() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        String stringValue = "string_value";

        PropertyDescriptor propertyDescriptor = Mockito.mock(PropertyDescriptor.class);
        Mockito.when(propertyDescriptor.getReadMethod()).thenReturn(Mockito.doReturn(automaticBean));
        Mockito.when(propertyDescriptor.getName()).thenReturn("stringProperty");
        Mockito.when(propertyDescriptor.getWriteMethod()).thenReturn(Mockito.doReturn(automaticBean));

        BeanUtilsBean beanUtilsBean = Mockito.mock(BeanUtilsBean.class);
        Mockito.when(beanUtilsBean.getPropertyDescriptor(AutomaticBean.class, "stringProperty")).thenReturn(propertyDescriptor);
        Mockito.when(beanUtilsBean.copyProperty(automaticBean, "stringProperty", stringValue)).thenReturn(null);

        automaticBean.tryCopyProperty("AutomaticBean", "stringProperty", stringValue, true);
    }

    @Test(expected = CheckstyleException.class)
    public void testTryCopyPropertyWithInvalidValueType() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Integer integerValue = 1;

        PropertyDescriptor propertyDescriptor = Mockito.mock(PropertyDescriptor.class);
        Mockito.when(propertyDescriptor.getReadMethod()).thenReturn(Mockito.doReturn(automaticBean));
        Mockito.when(propertyDescriptor.getName()).thenReturn("intProperty");
        Mockito.when(propertyDescriptor.getWriteMethod()).thenReturn(Mockito.doReturn(automaticBean));

        BeanUtilsBean beanUtilsBean = Mockito.mock(BeanUtilsBean.class);
        Mockito.when(beanUtilsBean.getPropertyDescriptor(AutomaticBean.class, "intProperty")).thenReturn(propertyDescriptor);
        Mockito.when(beanUtilsBean.copyProperty(automaticBean, "intProperty", integerValue)).thenThrow(new CheckstyleException(""));

        try {
            automaticBean.tryCopyProperty("AutomaticBean", "intProperty", integerValue, true);
        } catch (CheckstyleException e) {
            // Expected exception
        }
    }

    // Test contextualize method

    @Test
    public void testContextualizeWithSingleAttribute() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = Mockito.mock(Context.class);
        Object attributeValue = new Object() {};

        String attributeName = "attribute_name";
        Mockito.when(context.getAttributeNames()).thenReturn(new String[]{attributeName});
        Mockito.when(context.get(attributeName)).thenReturn(attributeValue);

        automaticBean.contextualize(context);

        Object propertyValue = BeanUtils.getPropertyValue(automaticBean, attributeName);
        assertEquals(attributeValue, propertyValue);
    }

    @Test
    public void testContextualizeWithMultipleAttributes() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Context context = Mockito.mock(Context.class);
        Object attributeValue1 = new Object() {};
        Object attributeValue2 = new Object() {};

        List<String> attributes = Arrays.asList("attribute1", "attribute2");
        Mockito.when(context.getAttributeNames()).thenReturn(attributes.toArray(new String[0]));
        Mockito.when(context.get(attributes.get(0))).thenReturn(attributeValue1);
        Mockito.when(context.get(attributes.get(1))).thenReturn(attributeValue2);

        automaticBean.contextualize(context);

        List<Object> propertyValues = Lists.newArrayList();
        try {
            propertyValues.add(BeanUtils.getPropertyValue(automaticBean, "attribute1"));
            propertyValues.add(BeanUtils.getPropertyValue(automaticBean, "attribute2"));
        } catch (Exception e) {
            fail();
        }

        List<Object> expectedPropertyValues = Lists.newArrayList(attributeValue1, attributeValue2);
        assertEquals(expectedPropertyValues, propertyValues);
    }

    // Test getConfiguration method

    @Test
    public void testGetConfiguration() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration configuration = new Configuration();

        automaticBean.configure(configuration);

        Configuration actualConfiguration = automaticBean.getConfiguration();
        assertEquals(configuration, actualConfiguration);
    }

    // Test finishLocalSetup method

    @Test
    public void testFinishLocalSetup() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();

        automaticBean.finishLocalSetup();
    }

    // Test setupChild method

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithValidChildConfiguration() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();
        Configuration childConfig = new Configuration();

        automaticBean.setupChild(childConfig);
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChildWithInvalidChildConfiguration() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();

        Configuration invalidChildConfig = null;
        automaticBean.setupChild(invalidChildConfig);

        String expectedMessage = "child_config is not allowed as a child in AutomaticBean";
        String actualMessage = "";
        try {
            String childConfigName = BeanUtils.getPropertyValue(automaticBean, "childConfig").toString();
            actualMessage = childConfigName;
        } catch (Exception e) {
            fail();
        }
        assertEquals(expectedMessage, actualMessage);
    }
}