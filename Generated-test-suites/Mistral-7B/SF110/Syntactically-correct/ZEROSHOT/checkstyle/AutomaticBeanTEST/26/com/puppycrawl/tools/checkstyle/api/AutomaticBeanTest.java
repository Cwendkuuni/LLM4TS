package com.yourpackagename.test;

import static org.junit.Assert.*;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import com.puppycrawl.tools.checkstyle.api.Configuration;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AutomaticBean.class, PropertyUtils.class, BeanUtils.class})
public class AutomaticBeanTest {

    @Test
    public void testConfigure() throws Exception {
        Configuration config = new Configuration();
        AutomaticBean automaticBean = new AutomaticBean();

        // Test with valid configuration
        automaticBean.configure(config);
        assertNotNull(automaticBean.getConfiguration());

        // Test with null configuration
        Configuration nullConfig = null;
        try {
            automaticBean.configure(nullConfig);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with empty configuration
        Configuration emptyConfig = new Configuration();
        try {
            automaticBean.configure(emptyConfig);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test with invalid key
        String invalidKey = "InvalidKey";
        Configuration invalidConfig = new Configuration();
        invalidConfig.addAttribute(invalidKey, "InvalidValue");
        try {
            automaticBean.configure(invalidConfig);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            String expectedMessage = String.format(Locale.ROOT, "Property '%s' in module %s does not exist, please check the documentation", invalidKey, automaticBean.getClass().getName());
            assertEquals(expectedMessage, e.getMessage());
        }

        // Test with invalid value (boolean)
        Configuration booleanConfig = new Configuration();
        booleanConfig.addAttribute("booleanAttribute", "invalid");
        try {
            automaticBean.configure(booleanConfig);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            String expectedMessage = String.format(Locale.ROOT, "illegal value 'invalid' for property '%s' of module %s", "booleanAttribute", automaticBean.getClass().getName());
            assertEquals(expectedMessage, e.getMessage());
        }

        // Test with valid values
        Configuration validConfig = new Configuration();
        validConfig.addAttribute("stringAttribute", "Test Value");
        validConfig.addAttribute("integerAttribute", "123");
        validConfig.addAttribute("longAttribute", "456");
        validConfig.addAttribute("doubleAttribute", "3.14");
        validConfig.addAttribute("floatAttribute", "1.23");
        validConfig.addAttribute("booleanAttribute", "true");
        validConfig.addAttribute("booleanAttributeArray", "true,false");
        validConfig.addAttribute("byteAttribute", "127");
        validConfig.addAttribute("byteAttributeArray", "127,128");
        validConfig.addAttribute("charAttribute", "A");
        validConfig.addAttribute("charAttributeArray", "A,B");
        validConfig.addAttribute("configurationChild", new Configuration());

        automaticBean.configure(validConfig);

        // Test properties
        assertEquals("Test Value", automaticBean.getStringAttribute("stringAttribute"));
        assertEquals(123, automaticBean.getIntegerAttribute("integerAttribute").intValue());
        assertEquals(456L, automaticBean.getLongAttribute("longAttribute").longValue());
        assertEquals(3.14D, automaticBean.getDoubleAttribute("doubleAttribute"), 0.001);
        assertEquals(1.23F, automaticBean.getFloatAttribute("floatAttribute"), 0.001);
        assertTrue(automaticBean.getBooleanAttribute("booleanAttribute"));
        assertArrayEquals(new Boolean[]{true, false}, automaticBean.getBooleanAttributeArray("booleanAttributeArray"));
        assertEquals((byte)127, automaticBean.getByteAttribute("byteAttribute").byteValue());
        assertArrayEquals(new byte[]{127, 128}, automaticBean.getByteAttributeArray("byteAttributeArray"));
        assertEquals('A', automaticBean.getCharAttribute("charAttribute").charValue());
        assertArrayEquals(new char[]{'A', 'B'}, automaticBean.getCharAttributeArray("charAttributeArray"));

        // Test configuration child
        Configuration childConfiguration = automaticBean.getConfiguration("configurationChild");
        assertEquals("configurationChild", childConfiguration.getName());
    }

    @Test
    public void testContextualize() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();

        // Test with empty context
        Context emptyContext = new Context();
        automaticBean.contextualize(emptyContext);

        // Test with valid context
        Context validContext = new Context();
        validContext.put("stringAttribute", "Test Value");
        validContext.put("integerAttribute", "123");
        validContext.put("longAttribute", "456");
        validContext.put("doubleAttribute", "3.14");
        validContext.put("floatAttribute", "1.23");
        validContext.put("booleanAttribute", "true");
        validContext.put("booleanAttributeArray", Arrays.asList("true", "false").toArray());
        validContext.put("byteAttribute", "127");
        validContext.put("byteAttributeArray", Arrays.asList(new Integer[]{127, 128}).toArray());
        validContext.put("charAttribute", "A");
        validContext.put("charAttributeArray", Arrays.asList(new Character[]{'A', 'B'}).toArray());
        validContext.put("configurationChild.stringAttribute", "Test Child Value");

        automaticBean.contextualize(validContext);

        // Test properties
        assertEquals("Test Value", automaticBean.getStringAttribute("stringAttribute"));
        assertEquals(123, automaticBean.getIntegerAttribute("integerAttribute").intValue());
        assertEquals(456L, automaticBean.getLongAttribute("longAttribute").longValue());
        assertEquals(3.14D, automaticBean.getDoubleAttribute("doubleAttribute"), 0.001);
        assertEquals(1.23F, automaticBean.getFloatAttribute("floatAttribute"), 0.001);
        assertTrue(automaticBean.getBooleanAttribute("booleanAttribute"));
        assertArrayEquals(new Boolean[]{true, false}, automaticBean.getBooleanAttributeArray("booleanAttributeArray"));
        assertEquals((byte)127, automaticBean.getByteAttribute("byteAttribute").byteValue());
        assertArrayEquals(new byte[]{127, 128}, automaticBean.getByteAttributeArray("byteAttributeArray"));
        assertEquals('A', automaticBean.getCharAttribute("charAttribute").charValue());
        assertArrayEquals(new char[]{'A', 'B'}, automaticBean.getCharAttributeArray("charAttributeArray"));

        // Test configuration child
        Configuration childConfiguration = automaticBean.getConfiguration("configurationChild");
        assertEquals("configurationChild", childConfiguration.getName());
        assertEquals("Test Child Value", childConfiguration.getStringAttribute("stringAttribute"));
    }

    @Test
    public void testTryCopyProperty() throws Exception {
        AutomaticBean automaticBean = new AutomaticBean();

        // Test with valid properties
        Configuration config = new Configuration();
        config.addAttribute("booleanAttribute", "true");
        config.addAttribute("integerAttribute", "123");
        config.addAttribute("longAttribute", "456");
        config.addAttribute("doubleAttribute", "3.14");
        config.addAttribute("floatAttribute", "1.23");
        config.addAttribute("stringAttribute", "Test Value");
        config.addAttribute("byteAttribute", "127");
        config.addAttribute("charAttribute", "A");
        config.addAttribute("configurationChild.stringAttribute", "Test Child Value");

        automaticBean.configure(config);

        PropertyDescriptor descriptor = null;
        try {
            descriptor = PropertyUtils.getPropertyDescriptor(automaticBean, "booleanAttribute");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // ignore
        }
        assertNotNull(descriptor);

        // Test copyProperty
        Object value = new Boolean(true);
        automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "booleanAttribute", value, true);
        assertTrue(automaticBean.getBooleanAttribute("booleanAttribute"));

        // Test copyProperty with null property descriptor
        value = new Boolean(false);
        descriptor = null;
        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "invalidProperty", value, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
        }

        // Test copyProperty with invalid value (boolean)
        value = "invalid";
        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "booleanAttribute", value, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            String expectedMessage = String.format(Locale.ROOT, "illegal value '%s' for property '%s' of module %s", value, "booleanAttribute", automaticBean.getClass().getName());
            assertEquals(expectedMessage, e.getMessage());
        }

        // Test copyProperty with invalid value (integer)
        value = "invalid";
        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "integerAttribute", value, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            String expectedMessage = String.format(Locale.ROOT, "illegal value '%s' for property '%s' of module %s", value, "integerAttribute", automaticBean.getClass().getName());
            assertEquals(expectedMessage, e.getMessage());
        }

        // Test copyProperty with invalid value (long)
        value = "invalid";
        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "longAttribute", value, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            String expectedMessage = String.format(Locale.ROOT, "illegal value '%s' for property '%s' of module %s", value, "longAttribute", automaticBean.getClass().getName());
            assertEquals(expectedMessage, e.getMessage());
        }

        // Test copyProperty with invalid value (double)
        value = "invalid";
        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "doubleAttribute", value, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            String expectedMessage = String.format(Locale.ROOT, "illegal value '%s' for property '%s' of module %s", value, "doubleAttribute", automaticBean.getClass().getName());
            assertEquals(expectedMessage, e.getMessage());
        }

        // Test copyProperty with invalid value (float)
        value = "invalid";
        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "floatAttribute", value, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            String expectedMessage = String.format(Locale.ROOT, "illegal value '%s' for property '%s' of module %s", value, "floatAttribute", automaticBean.getClass().getName());
            assertEquals(expectedMessage, e.getMessage());
        }

        // Test copyProperty with invalid value (string)
        value = "invalid";
        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "stringAttribute", value, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            String expectedMessage = String.format(Locale.ROOT, "illegal value '%s' for property '%s' of module %s", value, "stringAttribute", automaticBean.getClass().getName());
            assertEquals(expectedMessage, e.getMessage());
        }

        // Test copyProperty with invalid value (byte)
        value = (byte)129;
        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "byteAttribute", value, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            String expectedMessage = String.format(Locale.ROOT, "illegal value '%s' for property '%s' of module %s", value, "byteAttribute", automaticBean.getClass().getName());
            assertEquals(expectedMessage, e.getMessage());
        }

        // Test copyProperty with invalid value (char)
        value = 'Z';
        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "charAttribute", value, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            String expectedMessage = String.format(Locale.ROOT, "illegal value '%s' for property '%s' of module %s", value, "charAttribute", automaticBean.getClass().getName());
            assertEquals(expectedMessage, e.getMessage());
        }

        // Test copyProperty with invalid value (configuration child)
        Configuration childConfig = new Configuration();
        try {
            automaticBean.tryCopyProperty(automaticBean.getClass().getName(), "configurationChild.stringAttribute", childConfig, true);
            fail("Expected CheckstyleException");
        } catch (CheckstyleException e) {
            // Expected
            String expectedMessage = String.format(Locale.ROOT, "Cannot set property '%s' to '%s' in module %s", "configurationChild.stringAttribute", childConfig, automaticBean.getClass().getName());
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    private static void assertArrayEquals(Object expected, Object actual) {
        if (expected == null && actual == null) {
            return;
        }
        if (expected == null || actual == null || expected.getClass() != actual.getClass()) {
            fail("Expected and actual arrays have different types");
        }
        if (expected.getClass().isArray() && actual.getClass().isArray()) {
            Object[] expectedArray = (Object[])expected;
            Object[] actualArray = (Object[])actual;
            for (int i = 0; i < expectedArray.length; i++) {
                assertEquals(expectedArray[i], actualArray[i]);
            }
        } else {
            fail("Expected and actual arrays have different classes");
        }
    }
}