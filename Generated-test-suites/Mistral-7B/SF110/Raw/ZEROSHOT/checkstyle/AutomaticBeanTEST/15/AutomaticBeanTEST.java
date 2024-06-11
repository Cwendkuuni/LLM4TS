package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.CharacterConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.beanutils.converters.StringArrayConverter;
import org.apache.commons.beanutils.converters.RelaxedStringArrayConverter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.Converter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Context;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AutomaticBean.class, BeanUtils.class, PropertyUtils.class})
public class AutomaticBeanTest {

    @Test
    public void testAutomaticBean() throws Exception {
        // Test configure method
        Configuration config = Mockito.mock(Configuration.class);
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(config);
        assertNull(automaticBean.getConfiguration());

        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "configuration");
        assertNotNull(propertyDescriptor);
        Object configuration = propertyDescriptor.getReadMethod().invoke(automaticBean);
        assertNotNull(configuration);
        assertEquals(config, configuration);

        String[] attributeNames = config.getAttributeNames();
        assertEquals(5, attributeNames.length);
        for (String attributeName : attributeNames) {
            Object attributeValue = config.getAttribute(attributeName);
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), attributeName, attributeValue, true);
            PropertyDescriptor property = PropertyUtils.getPropertyDescriptor(automaticBean, attributeName);
            Object propertyValue = property.getReadMethod().invoke(automaticBean);
            assertEquals(attributeValue, propertyValue);
        }

        // Test contextualize method
        Context context = Mockito.mock(Context.class);
        automaticBean.contextualize(context);
        Object configuration = automaticBean.getConfiguration();
        List<String> attributes = context.getAttributeNames();
        for (String attributeName : attributes) {
            Object attributeValue = context.get(attributeName);
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), attributeName, attributeValue, false);
            PropertyDescriptor property = PropertyUtils.getPropertyDescriptor(automaticBean, attributeName);
            Object propertyValue = property.getReadMethod().invoke(automaticBean);
            assertEquals(attributeValue, propertyValue);
        }

        // Test tryCopyProperty method for not existing property
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "nonExistingProperty", null, true);
            fail();
        } catch (CheckstyleException e) {
            assertEquals("Property 'nonExistingProperty' in module AutomaticBean does not exist, please check the documentation", e.getMessage());
        }

        // Test tryCopyProperty method for invalid type
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "booleanProperty", new Integer(1), true);
            fail();
        } catch (CheckstyleException e) {
            assertEquals("illegal value '1' for property 'booleanProperty' of module AutomaticBean", e.getMessage());
        }

        // Test tryCopyProperty method for invocation target exception
        BeanUtils beanUtils = Mockito.mock(BeanUtils.class);
        PowerMockito.when(beanUtils.copyProperty(automaticBean, "booleanProperty", Boolean.TRUE)).thenThrow(new InvocationTargetException(new RuntimeException()));
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "booleanProperty", Boolean.TRUE, true);
            fail();
        } catch (CheckstyleException e) {
            assertEquals("Cannot set property 'booleanProperty' to 'true' in module AutomaticBean", e.getMessage());
        }

        // Test tryCopyProperty method for illegal argument exception
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "booleanProperty", new Object(), true);
            fail();
        } catch (CheckstyleException e) {
            assertEquals("illegal value '[class java.lang.Object]' for property 'booleanProperty' of module AutomaticBean", e.getMessage());
        }

        // Test tryCopyProperty method for conversion exception
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "booleanProperty", new String("notBoolean"), true);
            fail();
        } catch (CheckstyleException e) {
            assertEquals("illegal value 'notBoolean' for property 'booleanProperty' of module AutomaticBean", e.getMessage());
        }

        // Test configure method with children
        Configuration childConfig1 = Mockito.mock(Configuration.class);
        Configuration childConfig2 = Mockito.mock(Configuration.class);
        Configuration configWithChildren = new Configuration();
        configWithChildren.addChild(childConfig1);
        configWithChildren.addChild(childConfig2);
        AutomaticBean automaticBeanWithChildren = new AutomaticBean();
        automaticBeanWithChildren.configure(configWithChildren);
        List<Configuration> children = Arrays.asList(childConfig1, childConfig2);
        assertEquals(children, automaticBeanWithChildren.getConfiguration().getChildren());

        // Test setupChild method for null child configuration
        try {
            automaticBean.setupChild(null);
            fail();
        } catch (CheckstyleException e) {
            assertEquals("childConfig is not allowed as a child in AutomaticBean", e.getMessage());
        }

        // Test finishLocalSetup method
        try {
            automaticBean.finishLocalSetup();
        } catch (CheckstyleException e) {
            fail();
        }

        // Test RelaxedStringArrayConverter
        Configuration configWithStringArrayProperty = new Configuration();
        configWithStringArrayProperty.setAttribute("stringArrayProperty", new String[]{"value1", "value2"});
        AutomaticBean automaticBeanWithStringArrayProperty = new AutomaticBean();
        automaticBeanWithStringArrayProperty.configure(configWithStringArrayProperty);
        String[] stringArrayProperty = (String[])automaticBeanWithStringArrayProperty.getPropertyValue("stringArrayProperty");
        assertEquals(new String[]{"value1", "value2"}, stringArrayProperty);

        Configuration configWithListProperty = new Configuration();
        configWithListProperty.setAttribute("listProperty", Arrays.asList("value1", "value2"));
        AutomaticBean automaticBeanWithListProperty = new AutomaticBean();
        automaticBeanWithListProperty.configure(configWithListProperty);
        List<String> listProperty = (List<String>)automaticBeanWithListProperty.getPropertyValue("listProperty");
        assertEquals(Arrays.asList("value1", "value2"), listProperty);

        Configuration configWithNestedListProperty = new Configuration();
        List<List<String>> nestedListProperty = Arrays.asList(Arrays.asList("level1Value1", "level1Value2"), Arrays.asList("level2Value1", "level2Value2"));
        configWithNestedListProperty.setAttribute("nestedListProperty", nestedListProperty);
        AutomaticBean automaticBeanWithNestedListProperty = new AutomaticBean();
        automaticBeanWithNestedListProperty.configure(configWithNestedListProperty);
        List<List<String>> nestedListPropertyValue = (List<List<String>>)automaticBeanWithNestedListProperty.getPropertyValue("nestedListProperty");
        assertEquals(nestedListProperty, nestedListPropertyValue);

        List<List<String>> nestedListPropertyWithMixedTypes = Arrays.asList(Arrays.asList("level1Value1", "level1Value2", 1), Arrays.asList("level2Value1", "level2Value2", Arrays.asList("level3Value1", "level3Value2")));
        configWithNestedListProperty = new Configuration();
        configWithNestedListProperty.setAttribute("nestedListPropertyWithMixedTypes", nestedListPropertyWithMixedTypes);
        automaticBeanWithNestedListProperty = new AutomaticBean();
        automaticBeanWithNestedListProperty.configure(configWithNestedListProperty);
        nestedListPropertyValue = (List<List<String>>)automaticBeanWithNestedListProperty.getPropertyValue("nestedListPropertyWithMixedTypes");
        assertEquals(nestedListPropertyWithMixedTypes, nestedListPropertyValue);

        List<List<List<String>>> nestedListPropertyWithMixedTypesWithMixedTypes = Arrays.asList(Arrays.asList(Arrays.asList("level1Value1", "level1Value2", 1), Arrays.asList("level2Value1", "level2Value2", Arrays.asList("level3Value1", "level3Value2", "level3Value3"))), Arrays.asList(Arrays.asList("level1Value3", "level1Value4", Arrays.asList("level3Value4", "level3Value5")), Arrays.asList("level2Value3", "level2Value4", Arrays.asList("level3Value6", "level3Value7"))));
        configWithNestedListProperty = new Configuration();
        configWithNestedListProperty.setAttribute("nestedListPropertyWithMixedTypesWithMixedTypes", nestedListPropertyWithMixedTypesWithMixedTypes);
        automaticBeanWithNestedListProperty = new AutomaticBean();
        automaticBeanWithNestedListProperty.configure(configWithNestedListProperty);
        nestedListPropertyValue = (List<List<List<String>>>)automaticBeanWithNestedListProperty.getPropertyValue("nestedListPropertyWithMixedTypesWithMixedTypes");
        assertEquals(nestedListPropertyWithMixedTypesWithMixedTypes, nestedListPropertyValue);
    }

    private Object getPropertyValue(Object obj, String propertyName) throws Exception {
        PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(obj, propertyName);
        return propertyDescriptor.getReadMethod().invoke(obj);
    }
}

class TestConverter implements Converter {
    @Override
    public Object convert(Class type, Object value) {
        throw new UnsupportedOperationException();
    }
}