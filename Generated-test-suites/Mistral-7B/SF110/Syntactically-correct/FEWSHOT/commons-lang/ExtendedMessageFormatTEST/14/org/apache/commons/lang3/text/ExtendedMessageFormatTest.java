package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.cli.util.Validate;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormat() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern);
        assertEquals("Hello {0}, your age is {1}.", formatter.toPattern());

        String[] args = {"John", "25"};
        String formattedMessage = formatter.format(args);
        assertEquals("Hello John, your age is 25.", formattedMessage);

        formatter.setLocale(Locale.FR);
        formattedMessage = formatter.format(args);
        assertEquals("Hello John, votre âge est 25.", formattedMessage);
    }

    @Test
    public void testExtendedMessageFormatWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        String pattern = "Today is {date,date,short}.";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern, Locale.US, registry);
        assertEquals("Today is Aug 23, 2023.", formatter.format(null));
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0,choice,0#Mr.|1#Ms.|1#Mrs.|1#Ms.} {1}, your age is {2,number,integer}.{3}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern);
        assertNull(formatter.toPattern());

        formatter.applyPattern(pattern);
        String formattedMessage = formatter.format(new Object[]{"John Doe", 25, "."});
        assertEquals("Hello Mr. John Doe, your age is 25.", formattedMessage);

        formatter.setLocale(Locale.FR);
        formattedMessage = formatter.format(new Object[]{"Mme. Doe", 42, "!"});
        assertEquals("Hello Mme. Doe, votre âge est 42!", formattedMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatElement() {
        String pattern = "Invalid {0}.{1}";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern);
        formatter.applyPattern(pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnterminatedFormatElement() {
        String pattern = "Invalid {0";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern);
        formatter.applyPattern(pattern);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatArgumentIndex() {
        String pattern = "Hello {-1}, your age is {2}.";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern);
        formatter.applyPattern(pattern);
    }

    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("", Locale.US, registry);
        String pattern = "Today is {date,date,short}.";
        formatter.applyPattern(pattern);
        Format format = formatter.getFormat("date");
        assertNotNull(format);
        assertEquals(DateFormat.class, format.getClass());
    }

    @Test
    public void testParseFormatDescription() {
        String pattern = "Hello {date,date,short}, your age is {age,number,integer}.";
        ParsePosition pos = new ParsePosition(0);
        String description = formatter.parseFormatDescription(pattern, pos);
        assertEquals("date,date,short", description);
    }

    @Test
    public void testReadArgumentIndex() {
        String pattern = "Hello {0}, your age is {1}.";
        ParsePosition pos = new ParsePosition(0);
        int index = formatter.readArgumentIndex(pattern, pos);
        assertEquals(0, index);

        pos.setIndex(5);
        index = formatter.readArgumentIndex(pattern, pos);
        assertEquals(1, index);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescription() {
        String pattern = "Hello {date,date,short,invalid}., your age is {1}.";
        ParsePosition pos = new ParsePosition(0);
        formatter.parseFormatDescription(pattern, pos);
    }
}