package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testExtendedMessageFormatConstruction() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat msgFormat = new ExtendedMessageFormat(pattern);
        assertEquals("", msgFormat.toPattern());
        assertNull(msgFormat.getFormat(0));
        assertEquals(Locale.getDefault(), msgFormat.getLocale());
        assertNull(msgFormat.registry);

        pattern = "{0,date,short} {1} {2,number,currency}";
        msgFormat = new ExtendedMessageFormat(pattern, new HashMap<String, FormatFactory>() {{
            put("date", new SimpleDateFormatFactory());
            put("currency", new DecimalFormatFactory());
        }});
        assertEquals(pattern, msgFormat.toPattern());
        assertNotNull(msgFormat.getFormat(0));
        assertEquals(Locale.getDefault(), msgFormat.getLocale());
        assertNotNull(msgFormat.registry);
        assertEquals(2, msgFormat.registry.size());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat msgFormat = new ExtendedMessageFormat(pattern);

        Object[] args = {"John", 30};
        String result = msgFormat.format(args);
        assertEquals("Hello John, your age is 30.", result);

        msgFormat.applyPattern("{1} {0} {2}");
        result = msgFormat.format(args);
        assertEquals("30 John Hello.", result);

        msgFormat.applyPattern("{1} {0,date,short} {2,number,currency}");
        args = new Object[]{new Date(), 100.5};
        result = msgFormat.format(args);
        String expected = "30 " + new SimpleDateFormat("MM/dd/yyyy").format(new Date()) + " 100.50";
        assertEquals(expected, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat msgFormat = new ExtendedMessageFormat(pattern);
        msgFormat.setFormat(0, new SimpleDateFormat()); // Unsupported
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat msgFormat = new ExtendedMessageFormat(pattern);
        msgFormat.setFormats(new Format[0]); // Unsupported
    }
}