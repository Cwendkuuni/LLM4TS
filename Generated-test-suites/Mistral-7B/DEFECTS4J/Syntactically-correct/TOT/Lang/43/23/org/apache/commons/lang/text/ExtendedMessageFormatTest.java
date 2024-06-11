package org.apache.commons.lang.text;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.junit.Assert;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Test case for {@link ExtendedMessageFormat} class.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test constructor with a simple pattern.
     */
    @Test
    public void testSimplePattern() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Object[] args = {"John", 25};
        String result = format.format(args);
        Assert.assertEquals("Hello John, your age is 25.", result);
    }

    /**
     * Test constructor with a pattern containing custom format elements.
     */
    @Test
    public void testCustomFormatElements() {
        Map<String, CustomDateFormatFactory> registry = new HashMap<>();
        registry.put("customDateFormat", new CustomDateFormatFactory());

        String pattern = "Today is {customDateFormat format=date 'dd-MM-yyyy'}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, null, registry);
        Object[] args = new Object[0];
        String result = format.format(args);
        Assert.assertEquals("Today is -,-.", result);

        format = new ExtendedMessageFormat(pattern, Locale.ENGLISH, registry);
        args = new Object[0];
        result = format.format(args);
        Assert.assertEquals("Today is --.--.----", result);
    }

    /**
     * Test constructor with a pattern containing invalid format elements.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatElements() {
        String pattern = "Hello {0}, your age is {1}{2}.";
        new ExtendedMessageFormat(pattern);
    }

    /**
     * Test constructor with a pattern containing missing format elements.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMissingFormatElements() {
        String pattern = "Hello {0}, your age is {1}.";
        new ExtendedMessageFormat(pattern, null, Collections.emptyMap());
    }

    /**
     * Test method {@link ExtendedMessageFormat#applyPattern(String)}.
     */
    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0}, your age is {1}. Your favorite color is {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Object[] args = {"John", 25, "red"};
        String originalPattern = format.toPattern();
        format.applyPattern(pattern);
        Object[] newArgs = {"John Doe", 30, "blue"};
        String result = format.format(newArgs);
        Assert.assertEquals("Hello John Doe, your age is 30. Your favorite color is blue.", result);
        Assert.assertNotEquals(originalPattern, format.toPattern());
    }

    /**
     * Test method {@link ExtendedMessageFormat#setLocale(Locale)}.
     */
    @Test
    public void testSetLocale() {
        String pattern = "Hello {0}, your age is {1}. Your favorite color is {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Object[] args = {"John", 25, "red"};
        format.setLocale(Locale.FRENCH);
        String result = format.format(args);
        Assert.assertEquals("Hello John, votre âge est 25. votre couleur préférée est rouge.", result);
    }

    /**
     * Test method {@link ExtendedMessageFormat#format(Object[])}.
     */
    @Test
    public void testFormat() {
        String pattern = "Hello {0}, your age is {1}. Your favorite color is {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Object[] args = {"John", 25, "red"};
        String result = format.format(args);
        Assert.assertEquals("Hello John, your age is 25. Your favorite color is red.", result);

        format = new ExtendedMessageFormat(pattern, Locale.FRENCH);
        args = new Object[]{"Jean", 30, "bleu"};
        result = format.format(args);
        Assert.assertEquals("Hello Jean, votre âge est 30. votre couleur préférée est bleu.", result);
    }

    /**
     * Test method {@link ExtendedMessageFormat#format(Object[], Locale)} with invalid locale.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidLocale() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Object[] args = {"John", 25};
        format.format(args, new Locale("not-a-valid-locale"));
    }

    /**
     * Test method {@link ExtendedMessageFormat#toString()}.
     */
    @Test
    public void testToString() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String expected = "ExtendedMessageFormat[pattern=Hello {0}, your age is {1}., locale=sun.locale.util.SimpleLocale[platform=sun.util.platform.Window32, provider=sun.locale.win, language=en, country=US]]";
        String result = ReflectionToStringBuilder.toString(format, ToStringStyle.SHORT_PREFIX_STYLE);
        Assert.assertEquals(expected, result);
    }
}

class CustomDateFormatFactory implements FormatFactory {

    @Override
    public Format getFormat(String format, String formatStyle, Locale locale) {
        if (format.equalsIgnoreCase("customDateFormat")) {
            return new CustomDateFormat();
        }
        return null;
    }

    @Override
    public String[] getFormatNames() {
        return new String[]{"customDateFormat"};
    }
}

class CustomDateFormat implements Format {

    @Override
    public String format(Object obj, Locale locale, String format, ParsePosition parsePosition) {
        return String.format("%d-%02d-%02d", obj);
    }

    @Override
    public Object parseObject(String source, ParsePosition parsePosition) {
        int year = Integer.parseInt(source.substring(0, 4));
        int month = Integer.parseInt(source.substring(5, 7)) - 1;
        int day = Integer.parseInt(source.substring(8, 10));
        return new Object[]{year, month, day};
    }

    @Override
    public String parameterString() {
        return "date";
    }

    @Override
    public Class[] getParameterTypes() {
        return ArrayUtils.toPrimitiveClasses(Object[].class);
    }
}