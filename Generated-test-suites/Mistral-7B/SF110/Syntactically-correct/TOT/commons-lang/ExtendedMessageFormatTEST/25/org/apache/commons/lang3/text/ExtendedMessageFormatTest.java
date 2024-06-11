package org.apache.commons.lang3.text;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.text.simplifier.Simplifier;
import org.junit.Test;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ExtendedMessageFormatTest {

    @Test
    public void testEmptyPattern() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNull(format.toPattern());
    }

    @Test
    public void testSimplePattern() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        assertEquals("", format.toPattern());
    }

    @Test
    public void testPatternWithQuotedString() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0}'{'1'}'");
        assertEquals("{0}'{'1}'", format.toPattern());
    }

    @Test
    public void testPatternWithFormatElement() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,date,dd-MM-yyyy}");
        assertEquals("{0} {1,date,dd-MM-yyyy}", format.toPattern());
    }

    @Test
    public void testPatternWithFormatElementAndQuotedString() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0}'{'1,date,dd-MM-yyyy}'");
        assertEquals("{0}'{'1,date,dd-MM-yyyy}'", format.toPattern());
    }

    @Test
    public void testPatternWithMultipleFormatElements() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,date,dd-MM-yyyy} {2,number,#,##0.##}");
        assertEquals("{0} {1,date,dd-MM-yyyy} {2,number,#,##0.##}", format.toPattern());
    }

    @Test
    public void testPatternWithCustomFormat() {
        final Simplifier simplifier = new Simplifier();
        final Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("MyFormat", (formatName, args, locale) -> simplifier.createPatternFormatter(formatName, args, locale));
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0,format,MyFormat}");
        assertEquals("{0,format,MyFormat}", format.toPattern());
    }

    @Test
    public void testInvalidFormatArgumentIndex() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0,invalidIndex} {1}");
        assertThrows(IllegalArgumentException.class, () -> format.applyPattern(""));
    }

    @Test
    public void testUnterminatedFormatElement() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1");
        assertThrows(IllegalArgumentException.class, () -> format.applyPattern(""));
    }

    @Test
    public void testMissingFormat() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{z} {0}");
        assertThrows(IllegalArgumentException.class, () -> format.applyPattern(""));
    }

    @Test
    public void testParseException() {
        final ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1,date,XXX}");
        assertThrows(ParseException.class, () -> format.format(null, new Object[]{1, 2}));
    }

    @Test
    public void testFormatWithLocale() {
        final Locale locale = Locale.US;
        final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!", locale);
        assertEquals("Hello, {0}!", format.toPattern());
        assertEquals("Hello, John!", format.format(new Object[]{"John"}));
    }
}