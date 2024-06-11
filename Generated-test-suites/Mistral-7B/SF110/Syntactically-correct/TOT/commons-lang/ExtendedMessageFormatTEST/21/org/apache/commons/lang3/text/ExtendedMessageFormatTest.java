package org.apache.commons.lang3.text;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testEmptyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNull(format.toPattern());
        try {
            format.applyPattern("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSimplePattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        assertEquals("Hello {0}!", format.toPattern());
        Object[] args = {"World"};
        String result = format.format(args);
        assertEquals("Hello World!", result);
    }

    @Test
    public void testSimplePatternWithLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, this is {1}!", Locale.US);
        assertEquals("Hello {0}, this is {1}!", format.toPattern());
        Object[] args = {"John", "New York"};
        String result = format.format(args);
        assertEquals("Hello John, this is New York!", result);
    }

    @Test
    public void testComplexPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,date,short} is the {1,choice,0#day|1#first day of the month|1<day#last day of the month}", Locale.US);
        assertEquals("{0,date,short} is the {1,choice,0#day|1#first day of the month|1<day#last day of the month}", format.toPattern());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 15);
        Object[] args = {calendar.get(Calendar.DAY_OF_MONTH), calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US)};
        String result = format.format(args);
        assertEquals("15th is the third day of the month", result);
    }

    @Test
    public void testFormatRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0,number,currency}!", new HashMap<String, FormatFactory>() {{
            put("number", new NumberFormatFactory());
        }});
        assertEquals("Hello {0,number,currency}!", format.toPattern());
        Object[] args = {123};
        String result = format.format(args);
        assertEquals("Hello $123!", result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
        fail("Expected UnsupportedOperationException");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
        fail("Expected UnsupportedOperationException");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setFormats(new Format[0]);
        fail("Expected UnsupportedOperationException");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.setFormatsByArgumentIndex(new Format[0]);
        fail("Expected UnsupportedOperationException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0,3}!");
        try {
            format.applyPattern("Hello {0,3}!");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}!");
        try {
            format.applyPattern("Hello {3} {1}!");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnreadableFormatElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}!");
        try {
            format.applyPattern("Hello {0} {1} {2}!");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            assertTrue(message.contains("Unreadable format element at position "));
            assertTrue(message.contains("Hello {0} {1} {2}!"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnterminatedFormatElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}!");
        try {
            format.applyPattern("Hello {0} {1} {");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            String message = e.getMessage();
            assertTrue(message.contains("Unterminated format element at position "));
            assertTrue(message.contains("Hello {0} {1} {"));
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testInvalidNumberFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0,number,#}!");
        try {
            format.applyPattern("Hello {0,number,#}!");
            Object[] args = {"ABC"};
            format.format(args);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0} {1}!");
        try {
            format.applyPattern("Hello {0} {2}!");
            format.getFormat("invalid");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(null);
        try {
            format.applyPattern("");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullFormatFactory() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
        try {
            format.applyPattern("Hello {0}!");
            format.setRegistry(new HashMap<String, FormatFactory>() {{
                put(null, new SimpleDateFormat("yyyy-MM-dd"));
            }});
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }
}