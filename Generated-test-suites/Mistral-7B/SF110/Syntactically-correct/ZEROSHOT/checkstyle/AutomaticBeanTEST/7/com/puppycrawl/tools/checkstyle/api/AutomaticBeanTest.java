package com.example.junit;

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
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

@RunWith(MockitoJUnitRunner.class)
public class AutomaticBeanTest {

    @Test
    public void testAutomaticBean() throws Exception {
        // Test configure method
        Configuration configuration = Mockito.mock(Configuration.class);
        AutomaticBean automaticBean = new AutomaticBean();
        automaticBean.configure(configuration);
        assertNotNull(automaticBean.getConfiguration());
        assertEquals(configuration, automaticBean.getConfiguration());

        String[] attributeNames = configuration.getAttributeNames();
        for (String key : attributeNames) {
            Object value = configuration.getAttribute(key);
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), key, value, true);
            assertNotNull(automaticBean.getClass().getField(key));
            assertEquals(value, automaticBean.getClass().getField(key).get(automaticBean));
        }

        Configuration[] children = configuration.getChildren();
        for (Configuration childConfig : children) {
            automaticBean.setupChild(childConfig);
        }

        // Test contextualize method
        Context context = Mockito.mock(Context.class);
        List<String> attributes = Arrays.asList("key1", "key2");
        Mockito.when(context.getAttributeNames()).thenReturn(attributes.iterator());
        for (String key : attributes) {
            Object value = "value";
            Mockito.when(context.get(key)).thenReturn(value);
            automaticBean.contextualize(context);
            assertNotNull(automaticBean.getClass().getField(key));
            assertEquals(value, automaticBean.getClass().getField(key).get(automaticBean));
        }

        // Test tryCopyProperty method with valid values
        BeanUtilsBean beanUtils = Mockito.spy(new BeanUtilsBean());
        Object target = Mockito.mock(Object.class);
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "booleanProperty", true, true);
        assertFalse(automaticBean.getClass().getField("booleanProperty").isAccessible(automaticBean));
        PropertyDescriptor booleanPropertyDescriptor = PropertyUtils.getPropertyDescriptor(target, "booleanProperty");
        Mockito.doCallRealMethod().when(beanUtils).copyProperty(target, "booleanProperty", true);
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "booleanProperty", true, true);
        assertTrue(booleanPropertyDescriptor.getReadMethod().invoke(target));

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "byteProperty", (byte) 5, true);
        assertFalse(automaticBean.getClass().getField("byteProperty").isAccessible(automaticBean));
        PropertyDescriptor bytePropertyDescriptor = PropertyUtils.getPropertyDescriptor(target, "byteProperty");
        Mockito.doCallRealMethod().when(beanUtils).copyProperty(target, "byteProperty", (byte) 5);
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "byteProperty", (byte) 5, true);
        assertEquals((byte) 5, bytePropertyDescriptor.getReadMethod().invoke(target));

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "charProperty", 'A', true);
        assertFalse(automaticBean.getClass().getField("charProperty").isAccessible(automaticBean));
        PropertyDescriptor charPropertyDescriptor = PropertyUtils.getPropertyDescriptor(target, "charProperty");
        Mockito.doCallRealMethod().when(beanUtils).copyProperty(target, "charProperty", 'A');
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "charProperty", 'A', true);
        assertEquals('A', charPropertyDescriptor.getReadMethod().invoke(target));

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "doubleProperty", 3.14, true);
        assertFalse(automaticBean.getClass().getField("doubleProperty").isAccessible(automaticBean));
        PropertyDescriptor doublePropertyDescriptor = PropertyUtils.getPropertyDescriptor(target, "doubleProperty");
        Mockito.doCallRealMethod().when(beanUtils).copyProperty(target, "doubleProperty", 3.14);
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "doubleProperty", 3.14, true);
        assertEquals(3.14, doublePropertyDescriptor.getReadMethod().invoke(target));

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "floatProperty", 2.71f, true);
        assertFalse(automaticBean.getClass().getField("floatProperty").isAccessible(automaticBean));
        PropertyDescriptor floatPropertyDescriptor = PropertyUtils.getPropertyDescriptor(target, "floatProperty");
        Mockito.doCallRealMethod().when(beanUtils).copyProperty(target, "floatProperty", 2.71f);
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "floatProperty", 2.71f, true);
        assertEquals(2.71f, floatPropertyDescriptor.getReadMethod().invoke(target));

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "integerProperty", 7, true);
        assertFalse(automaticBean.getClass().getField("integerProperty").isAccessible(automaticBean));
        PropertyDescriptor integerPropertyDescriptor = PropertyUtils.getPropertyDescriptor(target, "integerProperty");
        Mockito.doCallRealMethod().when(beanUtils).copyProperty(target, "integerProperty", 7);
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "integerProperty", 7, true);
        assertEquals(7, integerPropertyDescriptor.getReadMethod().invoke(target));

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "longProperty", 8L, true);
        assertFalse(automaticBean.getClass().getField("longProperty").isAccessible(automaticBean));
        PropertyDescriptor longPropertyDescriptor = PropertyUtils.getPropertyDescriptor(target, "longProperty");
        Mockito.doCallRealMethod().when(beanUtils).copyProperty(target, "longProperty", 8L);
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "longProperty", 8L, true);
        assertEquals(8L, longPropertyDescriptor.getReadMethod().invoke(target));

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "shortProperty", (short) 11, true);
        assertFalse(automaticBean.getClass().getField("shortProperty").isAccessible(automaticBean));
        PropertyDescriptor shortPropertyDescriptor = PropertyUtils.getPropertyDescriptor(target, "shortProperty");
        Mockito.doCallRealMethod().when(beanUtils).copyProperty(target, "shortProperty", (short) 11);
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "shortProperty", (short) 11, true);
        assertEquals((short) 11, shortPropertyDescriptor.getReadMethod().invoke(target));

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "stringArrayProperty", new String[]{"A", "B"}, true);
        assertFalse(automaticBean.getClass().getField("stringArrayProperty").isAccessible(automaticBean));
        PropertyDescriptor stringArrayPropertyDescriptor = PropertyUtils.getPropertyDescriptor(target, "stringArrayProperty");
        Mockito.doCallRealMethod().when(beanUtils).copyProperty(target, "stringArrayProperty", new String[]{"A", "B"});
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "stringArrayProperty", new String[]{"A", "B"}, true);
        assertEquals(ListUtils.toList(new String[]{"A", "B"}), Arrays.asList((String[])stringArrayPropertyDescriptor.getReadMethod().invoke(target)));

        // Test tryCopyProperty method with invalid values
        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidBooleanProperty", "invalidValue", true);
        String errorMessage = "illegal value 'invalidValue' for property 'invalidBooleanProperty' of module AutomaticBean";
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidBooleanProperty", "invalidValue", true);
        } catch (CheckstyleException e) {
            assertEquals(errorMessage, e.getMessage());
        }

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidByteProperty", "invalidValue", true);
        errorMessage = "Cannot set property 'invalidByteProperty' to 'invalidValue' in module AutomaticBean";
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidByteProperty", "invalidValue", true);
        } catch (CheckstyleException e) {
            assertEquals(errorMessage, e.getMessage());
        }

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidCharProperty", '!', true);
        errorMessage = "illegal value '!' for property 'invalidCharProperty' of module AutomaticBean";
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidCharProperty", '!', true);
        } catch (CheckstyleException e) {
            assertEquals(errorMessage, e.getMessage());
        }

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidDoubleProperty", "invalidValue", true);
        errorMessage = "illegal value 'invalidValue' for property 'invalidDoubleProperty' of module AutomaticBean";
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidDoubleProperty", "invalidValue", true);
        } catch (CheckstyleException e) {
            assertEquals(errorMessage, e.getMessage());
        }

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidFloatProperty", "invalidValue", true);
        errorMessage = "illegal value 'invalidValue' for property 'invalidFloatProperty' of module AutomaticBean";
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidFloatProperty", "invalidValue", true);
        } catch (CheckstyleException e) {
            assertEquals(errorMessage, e.getMessage());
        }

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidIntegerProperty", "invalidValue", true);
        errorMessage = "illegal value 'invalidValue' for property 'invalidIntegerProperty' of module AutomaticBean";
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidIntegerProperty", "invalidValue", true);
        } catch (CheckstyleException e) {
            assertEquals(errorMessage, e.getMessage());
        }

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidLongProperty", "invalidValue", true);
        errorMessage = "illegal value 'invalidValue' for property 'invalidLongProperty' of module AutomaticBean";
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidLongProperty", "invalidValue", true);
        } catch (CheckstyleException e) {
            assertEquals(errorMessage, e.getMessage());
        }

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidShortProperty", "invalidValue", true);
        errorMessage = "illegal value 'invalidValue' for property 'invalidShortProperty' of module AutomaticBean";
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidShortProperty", "invalidValue", true);
        } catch (CheckstyleException e) {
            assertEquals(errorMessage, e.getMessage());
        }

        automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidStringArrayProperty", new String[]{"A", "B", "C"}, true);
        errorMessage = "Property 'invalidStringArrayProperty' in module AutomaticBean does not exist, please check the documentation";
        try {
            automaticBean.tryCopyProperty(AutomaticBean.class.getName(), "invalidStringArrayProperty", new String[]{"A", "B", "C"}, true);
        } catch (CheckstyleException e) {
            assertEquals(errorMessage, e.getMessage());
        }
    }

    @Test
    public void testRelaxedStringArrayConverter() {
        String commaSeparatedString = "hello,world,!hello,world";
        String trimmedString = StringUtils.trim(commaSeparatedString);
        List<String> expectedList = Arrays.asList("hello", "world", "hello", "world");
        List<String> actualList = Arrays.asList((String[]) new RelaxedStringArrayConverter().convert(String[].class, trimmedString));
        assertEquals(expectedList, actualList);
    }
}