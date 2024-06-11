package org.junit.tests; // Replace with your test package name

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.ParsePosition;
import org.apache.commons.lang3.Validate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ExtendedMessageFormatTest {

    private static final String DEFAULT_PATTERN = "Hello, {0}, your age is {1}.";
    private static final String CUSTOM_PATTERN = "Hello, {0}, your {2} is {1}.";
    private static final Map<String, FormatFactory> registry = new HashMap<>();
    private ExtendedMessageFormat extendedFormat;
    private MessageFormat messageFormat;
    private ParsePosition pos;

    @Before
    public void setUp() {
        registry.put("Date", new DateFormatFactory());
        extendedFormat = new ExtendedMessageFormat(DEFAULT_PATTERN, registry);
        messageFormat = new MessageFormat(DEFAULT_PATTERN);
        pos = new ParsePosition(0);
    }

    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat extForm = new ExtendedMessageFormat("");
        Assert.assertNotNull(extForm);
    }

    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat extForm = new ExtendedMessageFormat(DEFAULT_PATTERN);
        Assert.assertEquals(DEFAULT_PATTERN, extForm.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat extForm = new ExtendedMessageFormat(DEFAULT_PATTERN, Locale.US);
        Assert.assertEquals(Locale.US, extForm.getLocale());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat extForm = new ExtendedMessageFormat(DEFAULT_PATTERN, registry);
        Assert.assertEquals(DEFAULT_PATTERN, extForm.toPattern());
        Assert.assertEquals(registry, extForm.registry);
    }

    @Test
    public void testConstructorWithPatternAndLocaleAndRegistry() {
        ExtendedMessageFormat extForm = new ExtendedMessageFormat(DEFAULT_PATTERN, Locale.US, registry);
        Assert.assertEquals(Locale.US, extForm.getLocale());
        Assert.assertEquals(registry, extForm.registry);
    }

    @Test
    public void testToPattern() {
        Assert.assertEquals(DEFAULT_PATTERN, extendedFormat.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        extendedFormat.setFormat(0, messageFormat);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        extendedFormat.setFormatByArgumentIndex(0, messageFormat);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        Format[] formats = new Format[1];
        extendedFormat.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        Format[] formats = new Format[1];
        extendedFormat.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat extForm1 = new ExtendedMessageFormat(DEFAULT_PATTERN, registry);
        ExtendedMessageFormat extForm2 = new ExtendedMessageFormat(DEFAULT_PATTERN, registry);
        ExtendedMessageFormat extForm3 = new ExtendedMessageFormat(CUSTOM_PATTERN, registry);

        ExtendedMessageFormat clone1 = new ExtendedMessageFormat(extForm1);
        ExtendedMessageFormat clone2 = new ExtendedMessageFormat(extForm2);

        Assert.assertTrue(extForm1.equals(extForm1));
        Assert.assertTrue(extForm1.equals(clone1));
        Assert.assertTrue(extForm1.equals(extForm2));
        Assert.assertFalse(extForm1.equals(extForm3));
        Assert.assertFalse(extForm1.equals(null));
        Assert.assertFalse(extForm1.equals("Test"));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat extForm1 = new ExtendedMessageFormat(DEFAULT_PATTERN, registry);
        ExtendedMessageFormat extForm2 = new ExtendedMessageFormat(DEFAULT_PATTERN, registry);
        ExtendedMessageFormat extForm3 = new ExtendedMessageFormat(CUSTOM_PATTERN, registry);

        Assert.assertEquals(extForm1.hashCode(), extForm1.hashCode());
        Assert.assertEquals(extForm1.hashCode(), extForm2.hashCode());
        Assert.assertNotEquals(extForm1.hashCode(), extForm3.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        ExtendedMessageFormat invalidExtForm = new ExtendedMessageFormat("Invalid{0}Pattern");
    }

    @Test
    public void testApplyPatternBasic() {
        Object[] args = {"John", 25};
        String result = extendedFormat.format(args);
        String expected = "Hello, John, your age is 25.";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testApplyPatternCustom() {
        Object[] args = {"John", 25, "year"};
        ExtendedMessageFormat customExtForm = new ExtendedMessageFormat(CUSTOM_PATTERN, registry);
        String result = customExtForm.format(args);
        String expected = "Hello, John, your year is 25.";
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndex() {
        Object[] args = {"John", 25, "invalidindex"};
        ExtendedMessageFormat customExtForm = new ExtendedMessageFormat(CUSTOM_PATTERN, registry);
        customExtForm.applyPattern(CUSTOM_PATTERN);
        customExtForm.format(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescription() {
        ExtendedMessageFormat extForm = new ExtendedMessageFormat(CUSTOM_PATTERN, registry);
        extForm.applyPattern(CUSTOM_PATTERN);
        Object[] args = {"John", 25, "invalidformat"};
        extForm.format(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnterminatedFormatElement() {
        Object[] args = {"John", 25};
        ExtendedMessageFormat customExtForm = new ExtendedMessageFormat(CUSTOM_PATTERN, registry);
        customExtForm.applyPattern(CUSTOM_PATTERN + "}");
        customExtForm.format(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnterminatedQuotedString() {
        Object[] args = {"John", 25};
        ExtendedMessageFormat customExtForm = new ExtendedMessageFormat(CUSTOM_PATTERN, registry);
        customExtForm.applyPattern(CUSTOM_PATTERN + "\"");
        customExtForm.format(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatArgumentIndex() {
        Object[] args = {"John", 25, "invalidindex"};
        ExtendedMessageFormat customExtForm = new ExtendedMessageFormat(CUSTOM_PATTERN, registry);
        customExtForm.applyPattern(CUSTOM_PATTERN);
        customExtForm.setFormatByArgumentIndex(3, messageFormat);
        customExtForm.format(args);
    }

    @Test
    public void testFormatWithCustomRegistry() {
        Object[] args = {"John", 25};
        ExtendedMessageFormat customExtForm = new ExtendedMessageFormat(CUSTOM_PATTERN, registry);
        customExtForm.applyPattern(CUSTOM_PATTERN);
        customExtForm.setFormatByArgumentIndex(2, new SimpleDateFormat("dd-MM-yyyy"));
        String result = customExtForm.format(args);
        SimpleDateFormat dateFormat = (SimpleDateFormat) customExtForm.getFormats()[2];
        Date date = new Date();
        String expected = "Hello, John, your date is 12-12-2022.";
        Assert.assertEquals(expected, result);
        Assert.assertEquals("dd-MM-yyyy", dateFormat.toPattern());
    }

    @Test
    public void testGetFormat() {
        String formatDescription = "Date";
        Format format = extendedFormat.getFormat(formatDescription);
        Assert.assertNull(format);
    }

    @Test
    public void testReadArgumentIndexBasic() {
        int index = extendedFormat.readArgumentIndex(DEFAULT_PATTERN, pos);
        Assert.assertEquals(0, index);
    }

    @Test
    public void testReadArgumentIndexCustom() {
        int index = extendedFormat.readArgumentIndex(CUSTOM_PATTERN, pos);
        Assert.assertEquals(2, index);
    }

    @Test
    public void testParseFormatDescriptionBasic() {
        String formatDescription = "Date";
        String result = extendedFormat.parseFormatDescription(DEFAULT_PATTERN, pos);
        Assert.assertEquals(formatDescription, result);
    }

    @Test
    public void testParseFormatDescriptionCustom() {
        String formatDescription = "Date";
        String result = extendedFormat.parseFormatDescription(CUSTOM_PATTERN, pos);
        Assert.assertEquals(formatDescription, result);
    }

    @Test
    public void testInsertFormatsBasic() {
        String pattern = "Hello, {0}, your {2} is {1}.";
        ArrayList<String> customPatterns = new ArrayList<>();
        String result = extendedFormat.insertFormats(pattern, customPatterns);
        Assert.assertEquals(pattern, result);
    }

    @Test
    public void testInsertFormatsCustom() {
        String pattern = "Hello, {0}, your {2} is {1}.";
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("Date");
        String result = extendedFormat.insertFormats(pattern, customPatterns);
        Assert.assertEquals("Hello, {0}, your {2} is {1}.{Date}", result);
    }
}