package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @org.junit.Test
    public void testEmptyConstruction() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertNull(format.getRegistry());
    }

    @org.junit.Test
    public void testDefaultLocalizationConstruction() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}");
        assertEquals("{0} {1}", format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertNull(format.getRegistry());
    }

    @org.junit.Test
    public void testLocalizationAndRegistryConstruction() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        assertEquals("{0}, {1}", format.toPattern());
        assertEquals(new Locale("de", "DE"), format.getLocale());
        assertEquals(new HashMap<String, FormatFactory>(), format.getRegistry());
        assertEquals(format.getFormat("date"), new DateFormat());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        format.setFormat(0, new DateFormat());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        format.setFormatByArgumentIndex(0, new DateFormat());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        Format[] formats = new Format[] { new DateFormat(), new NumberFormat() };
        format.setFormats(formats);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexUnsupported() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        Format[] formats = new Format[] { new DateFormat(), new NumberFormat() };
        format.setFormatsByArgumentIndex(new int[] { 0, 1 }, formats);
    }

    @org.junit.Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        assertTrue(format1.equals(format2));
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        assertFalse(format1.equals(format3));
        ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0} {1}", new Locale("en", "US"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        assertFalse(format1.equals(format4));
        ExtendedMessageFormat format5 = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        format5.applyPattern("{0}, {1}");
        assertFalse(format1.equals(format5));
        ExtendedMessageFormat format6 = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        format6.setRegistry(new HashMap<String, FormatFactory>());
        assertFalse(format1.equals(format6));
    }

    @org.junit.Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        assertEquals(format1.hashCode(), format2.hashCode());
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        assertNotEquals(format1.hashCode(), format3.hashCode());
        ExtendedMessageFormat format4 = new ExtendedMessageFormat("{0} {1}", new Locale("en", "US"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        assertNotEquals(format1.hashCode(), format4.hashCode());
        ExtendedMessageFormat format5 = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        format5.applyPattern("{0}, {1}");
        assertNotEquals(format1.hashCode(), format5.hashCode());
        ExtendedMessageFormat format6 = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        format6.setRegistry(new HashMap<String, FormatFactory>());
        assertNotEquals(format1.hashCode(), format6.hashCode());
    }

    @org.junit.Test
    public void testGetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        assertEquals(new DateFormat(), format.getFormat("date"));
        assertNull(format.getFormat("nonExistentFormat"));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testApplyPatternInvalidFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        format.applyPattern("{invalidFormat} {1}");
    }

    @org.junit.Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0} {1}", new Locale("de", "DE"), new HashMap<String, FormatFactory>() {{
            put("date", new DateFormatFactory());
        }});
        format.applyPattern("{0}, {1}");

        String pattern = format.toPattern();
        assertEquals("{0}, {1}", pattern);
        String dateString = formatMessage(format, "2023-02-14", "de_DE", "dd.MM.yyyy");
        assertEquals("2023, 14.02.2023", dateString);
    }

}

class DateFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String formatString, Locale locale, MessageFormat messageFormat) {
        return new SimpleDateFormat(formatString, locale);
    }
}