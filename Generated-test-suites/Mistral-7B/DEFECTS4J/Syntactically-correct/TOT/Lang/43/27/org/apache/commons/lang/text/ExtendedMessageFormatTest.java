package org.apache.commons.lang.test;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.apache.commons.lang.text.SimpleDateFormatFactory;
import org.junit.Test;

import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        assertEquals("", format.toPattern());
    }

    /**
     * Test constructor with pattern.
     */
    @Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", Locale.US);
        assertEquals("{0} {1}", format.toPattern());
    }

    /**
     * Test constructor with pattern and registry.
     */
    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = Map.of(
                "date", new SimpleDateFormatFactory("yyyy-MM-dd")
        );
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date(\"yyyy-MM-dd\")}", registry);
        assertEquals("{0, date(\"yyyy-MM-dd\")}", format.toPattern());
    }

    /**
     * Test applyPattern method with simple format.
     */
    @Test
    public void testApplyPatternWithSimpleFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", Locale.US);
        format.applyPattern("{0} {1}");
        assertEquals("{0} {1}", format.toPattern());
    }

    /**
     * Test applyPattern method with custom format.
     */
    @Test
    public void testApplyPatternWithCustomFormat() {
        Map<String, FormatFactory> registry = Map.of(
                "date", new SimpleDateFormatFactory("yyyy-MM-dd")
        );
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date(\"yyyy-MM-dd\")}", registry);
        format.applyPattern("{0, date(\"yyyy-MM-dd\")}");
        assertEquals("{0, date(\"yyyy-MM-dd\")}", format.toPattern());
    }

    /**
     * Test applyPattern method with invalid format.
     */
    @Test
    public void testApplyPatternWithInvalidFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", Locale.US);
        assertThrows(IllegalArgumentException.class, () -> format.applyPattern("{0} {1}{2}"));
    }

    /**
     * Test getFormat method.
     */
    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = Map.of(
                "date", new SimpleDateFormatFactory("yyyy-MM-dd")
        );
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date(\"yyyy-MM-dd\")}", registry);
        Format result = format.getFormat("date");
        assertEquals("yyyy-MM-dd", result.getClass().getName());
    }

    /**
     * Test getFormat method with unknown format.
     */
    @Test
    public void testGetFormatWithUnknownFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", Locale.US);
        assertNull(format.getFormat("unknown"));
    }

    /**
     * Test format method.
     */
    @Test
    public void testFormat() throws ParseException {
        Map<String, FormatFactory> registry = Map.of(
                "date", new SimpleDateFormatFactory("yyyy-MM-dd")
        );
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date(\"yyyy-MM-dd\")} {1}", registry);
        format.applyPattern("{0, date(\"yyyy-MM-dd\")} {1}");
        Object[] args = {"2023-03-31", "World"};
        String result = format.format(args);
        assertEquals("2023-03-31 World", result);
    }

    /**
     * Test format method with null arguments.
     */
    @Test
    public void testFormatWithNullArguments() {
        Map<String, FormatFactory> registry = Map.of(
                "date", new SimpleDateFormatFactory("yyyy-MM-dd")
        );
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date(\"yyyy-MM-dd\")} {1}", registry);
        format.applyPattern("{0, date(\"yyyy-MM-dd\")} {1}");
        Object[] args = {null, "World"};
        assertThrows(NullPointerException.class, () -> format.format(args));
    }

    /**
     * Test format method with undefined format.
     */
    @Test
    public void testFormatWithUndefinedFormat() throws ParseException {
        Map<String, FormatFactory> registry = Map.of(
                "date", new SimpleDateFormatFactory("yyyy-MM-dd")
        );
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, unknown} {1}", registry);
        format.applyPattern("{0, unknown} {1}");
        Object[] args = {"2023-03-31", "World"};
        assertThrows(IllegalArgumentException.class, () -> format.format(args));
    }
}