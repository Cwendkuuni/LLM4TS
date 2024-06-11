package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.cli.ParsePosition;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstruction() {
        String pattern = "Hello {0}, your {1} is {2}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
        assertNull(format.registry);
    }

    @Test
    public void testExtendedMessageFormatWithRegistry() {
        String pattern = "Hello {0,date,dd-MM-yyyy}, your {1} is {2,number,currency}.";
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory dateFormatFactory = new SimpleDateFormat("dd-MM-yyyy");
        registry.put("date", dateFormatFactory);
        FormatFactory currencyFormatFactory = NumberFormat.getCurrencyInstance();
        registry.put("number", currencyFormatFactory);
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());
        assertEquals(registry, format.registry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        String pattern = "Hello {0}, your {1} is {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern("Hello {0, {1}}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndex() {
        String pattern = "Hello {0}, your {1} is {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern(pattern + "{" + Integer.MAX_VALUE + "}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnterminatedFormatElement() {
        String pattern = "Hello {0}, your {1} is {";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        format.applyPattern(pattern);
    }

    @Test
    public void testAppendQuotedString() {
        String pattern = "Hello {{'foo'}}";
        ParsePosition pos = new ParsePosition(0);
        StringBuilder sb = new StringBuilder();
        sb.appendQuotedString(pattern, pos, sb, false);
        assertEquals("foo", sb.toString());
    }

    @Test
    public void testInsertFormats() {
        String pattern = "Hello {0}, your {1} is {2}";
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("{format,integer,#,##0.##}");
        String formattedPattern = ExtendedMessageFormat.insertFormats(pattern, customPatterns);
        assertEquals("Hello {0}, your {1} is {format,integer,#,##0.##}", formattedPattern);
    }

    @Test
    public void testGetFormat() {
        String pattern = "Hello {format,date,dd-MM-yyyy}, your {number,currency,USD} is {2}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        Format dateFormat = format.getFormat("date");
        assertSame(SimpleDateFormat.class, dateFormat.getClass());
        Format currencyFormat = format.getFormat("number");
        assertSame(NumberFormat.class, currencyFormat.getClass());
        FormatFactory dateFormatFactory = new SimpleDateFormat("dd-MM-yyyy");
        FormatFactory currencyFormatFactory = NumberFormat.getCurrencyInstance();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", dateFormatFactory);
        registry.put("number", currencyFormatFactory);
        ExtendedMessageFormat formatWithRegistry = new ExtendedMessageFormat(pattern, registry);
        assertEquals(dateFormat, formatWithRegistry.getFormat("date"));
        assertEquals(currencyFormat, formatWithRegistry.getFormat("number"));
    }

    @Test
    public void testReadArgumentIndex() {
        String pattern = "Hello {0}, your {1} is {2}";
        ParsePosition pos = new ParsePosition(0);
        int index = ExtendedMessageFormat.readArgumentIndex(pattern, pos);
        assertEquals(0, index);
        ExtendedMessageFormat.next(pos);
        index = ExtendedMessageFormat.readArgumentIndex(pattern, pos);
        assertEquals(1, index);
        ExtendedMessageFormat.next(pos);
        index = ExtendedMessageFormat.readArgumentIndex(pattern, pos);
        assertEquals(2, index);
        ExtendedMessageFormat.next(pos);
        assertEquals(Pattern.Position.NO_MATCH, pos.getIndex());
    }

    @Test
    public void testParseFormatDescription() {
        String pattern = "Hello {0,date,dd-MM-yyyy}, your {1,number,currency,USD} is {2}";
        ParsePosition pos = new ParsePosition(0);
        String description = ExtendedMessageFormat.parseFormatDescription(pattern, pos);
        assertEquals("date", description);
        ExtendedMessageFormat.next(pos);
        description = ExtendedMessageFormat.parseFormatDescription(pattern, pos);
        assertEquals("number", description);
        ExtendedMessageFormat.next(pos);
        description = ExtendedMessageFormat.parseFormatDescription(pattern, pos);
        assertEquals("currency", description);
        ExtendedMessageFormat.next(pos);
        assertEquals(Pattern.Position.NO_MATCH, pos.getIndex());
    }

    @Test
    public void testContainsElements() {
        Collection<?> collection = new ArrayList<>();
        collection.add("date");
        collection.add(null);
        collection.add("currency");
        assertTrue(ExtendedMessageFormat.containsElements(collection));
        collection.clear();
        assertFalse(ExtendedMessageFormat.containsElements(collection));
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0}, your {1} is {2}";
        Object[] args = {"Alice", "25", "150.50"};
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String formattedMessage = format.format(args);
        assertEquals("Hello Alice, your 25 is 150.50", formattedMessage);

        pattern = "Hello {0}, your {1,date,dd-MM-yyyy} is {2,number,currency,USD}";
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory dateFormatFactory = new SimpleDateFormat("dd-MM-yyyy");
        registry.put("date", dateFormatFactory);
        FormatFactory currencyFormatFactory = NumberFormat.getCurrencyInstance();
        registry.put("number", currencyFormatFactory);
        format = new ExtendedMessageFormat(pattern, registry);
        args = new Object[]{new Date(), new BigDecimal("12345.67"), null};
        formattedMessage = format.format(args);
        assertEquals("Hello [Wed Mar 29 15:24:56 CET 2023], your 12345.67 USD is null", formattedMessage);
    }
}