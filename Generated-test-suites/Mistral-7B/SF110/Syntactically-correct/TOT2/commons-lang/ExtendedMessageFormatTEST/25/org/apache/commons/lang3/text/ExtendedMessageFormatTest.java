package org.apache.commons.lang3.text;

import org.apache.commons.lang3.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithValidPattern() {
        String pattern = "Hello, {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        assertEquals("", messageFormat.toPattern());
        assertNotNull(messageFormat.registry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyPattern() {
        String pattern = "";
        try {
            new ExtendedMessageFormat(pattern);
        } catch (IllegalArgumentException e) {
            assertEquals("Unreadable format element at position 0", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidPattern() {
        String pattern = "Hello, {0}, your age is {1}x";
        try {
            new ExtendedMessageFormat(pattern);
        } catch (IllegalArgumentException e) {
            assertEquals("Unreadable format element at position 0", e.getMessage());
        }
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello, {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        assertArrayEquals(new Object[]{"John", 25}, messageFormat.format(new Object[]{"John", 25}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithNullObject() {
        String pattern = "Hello, {0}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        try {
            messageFormat.format(null);
        } catch (IllegalArgumentException e) {
            assertEquals("The arguments cannot be null", e.getMessage());
        }
    }

    @Test
    public void testApplyPatternWithInvalidArgumentType() {
        String pattern = "Hello, {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        try {
            messageFormat.format(new Object[]{new Object(){}, 25});
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format argument type", e.getMessage());
        }
    }

    @Test
    public void testApplyPatternWithInvalidFormatIndex() {
        String pattern = "Hello, {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        try {
            messageFormat.format(new Object[]{"John", new Object(){}});
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format index", e.getMessage());
        }
    }

    @Test
    public void testApplyPatternWithInvalidFormatDescription() {
        String pattern = "Hello, {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        try {
            messageFormat.format(new Object[]{"John", new Format(){}});
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid format description", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFormatWithNullRegistry() {
        String pattern = "Hello, {0}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        try {
            messageFormat.getFormat("InvalidFormat");
        } catch (IllegalArgumentException e) {
            assertEquals("The registry is null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFormatWithInvalidFormatName() {
        String pattern = "Hello, {0}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        Map<String, FormatFactory> registry = new HashMap<>();
        messageFormat.setRegistry(registry);
        try {
            messageFormat.getFormat("InvalidFormat");
        } catch (IllegalArgumentException e) {
            assertEquals("Format name 'InvalidFormat' not found in registry", e.getMessage());
        }
    }

    @Test
    public void testSetRegistry() {
        String pattern = "Hello, {0}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("StringFormat", new SimpleFormatFactory());
        messageFormat.setRegistry(registry);
        assertEquals("StringFormat", messageFormat.getRegistry().get("StringFormat").getClass().getSimpleName());
    }

    @Test
    public void testSetRegistryWithNullRegistry() {
        String pattern = "Hello, {0}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        try {
            messageFormat.setRegistry(null);
        } catch (NullPointerException e) {
            assertEquals("The registry cannot be null", e.getMessage());
        }
    }
}