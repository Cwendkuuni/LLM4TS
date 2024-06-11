package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testNewInstance() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}.");
        assertEquals("Hello {0}, your age is {1}", messageFormat.toPattern());
        assertEquals(Locale.getDefault(), messageFormat.getLocale());
        assertNull(messageFormat.getRegistry());
    }

    @Test
    public void testNewInstanceWithLocale() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}, your age is {1}", Locale.US);
        assertEquals("Hello {0}, your age is {1}", messageFormat.toPattern());
        assertEquals(Locale.US, messageFormat.getLocale());
        assertNull(messageFormat.getRegistry());
    }

    @Test
    public void testNewInstanceWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory numberFormatFactory = new SimpleFormatFactory();
        registry.put("number", numberFormatFactory);

        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {number,number}");
        assertEquals("Hello {number,number}", messageFormat.toPattern());
        assertEquals(Locale.getDefault(), messageFormat.getLocale());
        assertEquals(registry, messageFormat.getRegistry());
    }

    @Test
    public void testNewInstanceWithRegistryAndLocale() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory numberFormatFactory = new SimpleFormatFactory();
        registry.put("number", numberFormatFactory);

        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {number,number}", Locale.US, registry);
        assertEquals("Hello {number,number}", messageFormat.toPattern());
        assertEquals(Locale.US, messageFormat.getLocale());
        assertEquals(registry, messageFormat.getRegistry());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}");
        messageFormat.setFormat(0, new SimpleDateFormat("dd/MM/yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}");
        messageFormat.setFormatByArgumentIndex(0, new SimpleDateFormat("dd/MM/yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}");
        messageFormat.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0}");
        messageFormat.setFormatsByArgumentIndex(new Format[0]);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello {0}");

        assertTrue(messageFormat1.equals(messageFormat2));
        assertTrue(messageFormat2.equals(messageFormat1));

        messageFormat1.setRegistry(Collections.singletonMap("number", new SimpleFormatFactory()));
        assertFalse(messageFormat1.equals(messageFormat2));
        assertFalse(messageFormat2.equals(messageFormat1));

        messageFormat2.setRegistry(Collections.singletonMap("number", new SimpleFormatFactory()));
        assertTrue(messageFormat1.equals(messageFormat2));
        assertTrue(messageFormat2.equals(messageFormat1));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("Hello {0}");

        int hashCode1 = messageFormat1.hashCode();
        int hashCode2 = messageFormat2.hashCode();

        assertEquals(hashCode1, hashCode2);

        messageFormat1.setRegistry(Collections.singletonMap("number", new SimpleFormatFactory()));
        int newHashCode1 = messageFormat1.hashCode();
        int newHashCode2 = messageFormat2.hashCode();

        assertNotEquals(hashCode1, newHashCode1);
        assertNotEquals(hashCode1, newHashCode2);
        assertEquals(newHashCode1, newHashCode2);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {0} {1}");

        String[] args = {"John", "Doe"};
        Object[] objects = {"John", "Doe"};
        String formatted = messageFormat.format(objects);
        assertEquals("Hello John Doe", formatted);
        assertEquals("Hello {0} {1}", messageFormat.toPattern());

        messageFormat.applyPattern("Hello {1} {0}");
        formatted = messageFormat.format(objects);
        assertEquals("Hello Doe John", formatted);
        assertEquals("Hello {1} {0}", messageFormat.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory numberFormatFactory = new SimpleFormatFactory();
        registry.put("number", numberFormatFactory);

        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("Hello {number,number} {0}");

        String[] args = {"John"};
        Object[] objects = {"123"};
        String formatted = messageFormat.format(objects);
        assertEquals("Hello 123 John", formatted);
        assertEquals("Hello {number,number} {0}", messageFormat.toPattern());

        messageFormat.applyPattern("Hello {0} {number,number}");
        formatted = messageFormat.format(objects);
        assertEquals("Hello John 123", formatted);
        assertEquals("Hello {0} {number,number}", messageFormat.toPattern());
    }
}

class SimpleFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String pattern, Locale locale, Object format) {
        return new SimpleDateFormat(pattern);
    }
}

class SimpleDateFormat extends DateFormat {
    @Override
    public StringBuffer format(Date date, StringBuffer toappend, FieldPosition pos) {
        return null;
    }
}