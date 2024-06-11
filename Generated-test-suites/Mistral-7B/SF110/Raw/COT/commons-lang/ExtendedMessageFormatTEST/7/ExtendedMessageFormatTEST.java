package org.junit.tests;

import static org.apache.commons.lang3.text.ExtendedMessageFormat.HASH_SEED;
import static org.apache.commons.lang3.text.ExtendedMessageFormat.START_FMT;
import static org.apache.commons.lang3.text.ExtendedMessageFormat.START_FE;
import static org.apache.commons.lang3.text.ExtendedMessageFormat.END_FE;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;

import java.text.Format;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.Validate;

public class ExtendedMessageFormatTest {

    

    @org.junit.Test
    public void testConstructorWithPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}, {1}");
        assertEquals("Hello, {0}, {1}", messageFormat.toPattern());
    }

    @org.junit.Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}, {1}", Locale.US);
        assertEquals("Hello, {0}, {1}", messageFormat.toPattern());
    }

    @org.junit.Test
    public void testConstructorWithPatternAndRegistry() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return new SimpleDateFormat(name, locale);
            }
        };
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0, date}, {1}", Collections.singletonMap("date", formatFactory));
        assertEquals("Hello, {0, date}, {1}", messageFormat.toPattern());
    }

    @org.junit.Test
    public void testConstructorWithPatternPatternAndRegistry() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return new SimpleDateFormat(name, locale);
            }
        };
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {1, date}, {0}", Collections.singletonMap("date", formatFactory));
        assertEquals("Hello, {1}, {0}", messageFormat.toPattern());
    }

    @org.junit.Test
    public void testToPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}, {1}");
        assertEquals("Hello, {0}, {1}", messageFormat.toPattern());
    }

    @org.junit.Test
    public void testApplyPatternWithEmptyPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("");
        assertNull(messageFormat.toPattern());
    }

    @org.junit.Test
    public void testApplyPatternWithInvalidFormat() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{invalidFormat}");
        assertNull(messageFormat.toPattern());
        assertThrows(IllegalArgumentException.class, () -> messageFormat.format(new Object[0]));
    }

    @org.junit.Test
    public void testApplyPatternWithQuotedString() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}'{1}'");
        assertEquals("Hello, {0}'{1}'", messageFormat.toPattern());
    }

    @org.junit.Test
    public void testApplyPatternWithFormatDescription() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return new SimpleDateFormat(name, locale);
            }
        };
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0, date}, {1}", Collections.singletonMap("date", formatFactory));
        assertEquals("Hello, {0, date}, {1}", messageFormat.toPattern());
    }

    @org.junit.Test
    public void testSetFormatUnsupportedOperationException() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}");
        assertThrows(UnsupportedOperationException.class, () -> messageFormat.setFormat(0, new SimpleDateFormat("dd-MM-yyyy")));
    }

    @org.junit.Test
    public void testSetFormatByArgumentIndexUnsupportedOperationException() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}");
        assertThrows(UnsupportedOperationException.class, () -> messageFormat.setFormatByArgumentIndex(0, new SimpleDateFormat("dd-MM-yyyy")));
    }

    @org.junit.Test
    public void testSetFormatsUnsupportedOperationException() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}");
        assertThrows(UnsupportedOperationException.class, () -> messageFormat.setFormats(new Format[0]));
    }

    @org.junit.Test
    public void testSetFormatsByArgumentIndexUnsupportedOperationException() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}");
        assertThrows(UnsupportedOperationException.class, () -> messageFormat.setFormatsByArgumentIndex(new Format[0]));
    }

    @org.junit.Test
    public void testEqualsWithNull() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}");
        assertFalse(messageFormat.equals(null));
    }

    @org.junit.Test
    public void testEqualsWithSameInstance() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}");
        assertTrue(messageFormat.equals(messageFormat));
    }

    @org.junit.Test
    public void testEqualsWithDifferentClass() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}");
        assertFalse(messageFormat.equals(new Object()));
    }

    @org.junit.Test
    public void testEqualsWithDifferentPattern() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello, {0}");
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello, {1}");
        assertFalse(messageFormat1.equals(messageFormat2));
    }

    @org.junit.Test
    public void testEqualsWithDifferentRegistry() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello, {0}", Locale.US, null);
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello, {0}", Locale.US, new HashMap<>());
        assertFalse(messageFormat1.equals(messageFormat2));
    }

    @org.junit.Test
    public void testHashCodeWithSameInstance() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}");
        int hashCode1 = messageFormat.hashCode();
        int hashCode2 = messageFormat.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @org.junit.Test
    public void testHashCodeWithDifferentPattern() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello, {0}");
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello, {1}");
        assertNotSame(messageFormat1.hashCode(), messageFormat2.hashCode());
    }

    @org.junit.Test
    public void testHashCodeWithDifferentRegistry() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello, {0}", Locale.US, null);
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello, {0}", Locale.US, new HashMap<>());
        assertNotSame(messageFormat1.hashCode(), messageFormat2.hashCode());
    }

    @org.junit.Test
    public void testGetFormatNull() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}");
        assertNull(messageFormat.getFormat(0));
    }

    @org.junit.Test
    public void testGetFormatByArgumentIndexNull() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}");
        assertNull(messageFormat.getFormatByArgumentIndex(0));
    }

    @org.junit.Test
    public void testGetFormatsNull() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}");
        assertNull(messageFormat.getFormats());
    }

    @org.junit.Test
    public void testGetFormatsByArgumentIndexNull() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}");
        assertNull(messageFormat.getFormatsByArgumentIndex(new int[0]));
    }

    @org.junit.Test
    public void testFormatSimpleCase() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}, {1}");
        Object[] args = {"John", "Doe"};
        String formattedMessage = messageFormat.format(args);
        assertEquals("Hello, John, Doe", formattedMessage);
    }

    @org.junit.Test
    public void testFormatWithNullArgument() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}, {1}");
        Object[] args = {null, "Doe"};
        String formattedMessage = messageFormat.format(args);
        assertEquals("Hello, null, Doe", formattedMessage);
    }

    @org.junit.Test
    public void testFormatWithEmptyArgument() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}, {1}");
        Object[] args = {"", "Doe"};
        String formattedMessage = messageFormat.format(args);
        assertEquals("Hello, , Doe", formattedMessage);
    }

    @org.junit.Test
    public void testFormatWithFormatAndNullArgument() {
        Format dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0, date}, {1}", new HashMap<String, FormatFactory>() {{
            put("date", (formatFactory) -> dateFormat);
        }});
        Object[] args = {null, new Date()};
        String formattedMessage = messageFormat.format(args);
        assertEquals("Hello, null, " + new SimpleDateFormat("dd-MM-yyyy").format(new Date()), formattedMessage);
    }

    @org.junit.Test
    public void testFormatWithFormatAndEmptyArgument() {
        Format dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0, date}, {1}", new HashMap<String, FormatFactory>() {{
            put("date", (formatFactory) -> dateFormat);
        }});
        Object[] args = {new Date(), ""};
        String formattedMessage = messageFormat.format(args);
        assertEquals("Hello, " + new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + ", ", formattedMessage);
    }

    @org.junit.Test
    public void testFormatWithFormatAndNullFormat() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0, date}, {1}", new HashMap<String, FormatFactory>() {{
            put("date", (formatFactory) -> null);
        }});
        Object[] args = {"John", "Doe"};
        String formattedMessage = messageFormat.format(args);
        assertNull(formattedMessage);
    }

    @org.junit.Test
    public void testFormatWithFormatAndEmptyFormatRegistry() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0, date}, {1}", Collections.emptyMap());
        Object[] args = {"John", "Doe"};
        String formattedMessage = messageFormat.format(args);
        assertNull(formattedMessage);
    }

    @org.junit.Test
    public void testFormatWithQuotedString() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0}'{1}'");
        Object[] args = {"'John'", "'Doe'"};
        String formattedMessage = messageFormat.format(args);
        assertEquals("Hello, 'John''Doe'", formattedMessage);
    }

    @org.junit.Test
    public void testFormatWithFormats() {
        Format dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Format currencyFormat = new DecimalFormat("#,##0.00");
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {0, date}, {1, number, currency}", new HashMap<String, FormatFactory>() {{
            put("date", (formatFactory) -> dateFormat);
            put("number", (formatFactory) -> currencyFormat);
        }});
        Object[] args = {"John", new BigDecimal("123456.78")};
        String formattedMessage = messageFormat.format(args);
        assertEquals("Hello, " + new SimpleDateFormat("dd-MM-yyyy").format(new Date()) + ", $123,456.78", formattedMessage);
    }

    @org.junit.Test
    public void testFormatWithArgumentIndex() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello, {1}, {0}");
        Object[] args = {"John", "Doe"};
        String formattedMessage = messageFormat.format(args);
        assertEquals("Hello, Doe, John", formattedMessage);
    }

    
}