package org.junit.tests;

import static org.apache.commons.lang3.text.ExtendedMessageFormat.START_FE;
import static org.apache.commons.lang3.text.ExtendedMessageFormat.START_FMT;
import static org.apache.commons.lang3.text.ExtendedMessageFormat.END_FE;
import static org.apache.commons.lang3.text.ExtendedMessageFormat.QUOTE;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.*;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    

    @Test
    public void testExtendedMessageFormatConstructorWithDefaults() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertNull(format.getRegistry());
        assertEquals("", format.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}");
        assertEquals("{0}, {1}", format.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        assertEquals("{0}, {1}", format.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", new HashMap<>());
        assertEquals("{0}, {1}", format.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithPatternAndRegistryAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", new HashMap<>(), Locale.US);
        assertEquals("{0}, {1}", format.toPattern());
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, date '{dd-MM-yyyy}'},{1}", Locale.US, new HashMap<>());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Format customFormat = new SimpleDateFormat("MM-yy");
        format.getRegistry().put("date", () -> dateFormat);
        format.getRegistry().put("custom", () -> customFormat);

        Object[] args = new Object[]{new Date(), "12-19"};
        String formattedMessage = format.format(args);
        String expectedMessage = "12-19-2019, 12-19";
        assertEquals(expectedMessage, formattedMessage);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatThrowsException() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US);
        format.setFormat(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndexThrowsException() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US);
        format.setFormatByArgumentIndex(0, new SimpleDateFormat("dd-MM-yyyy"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsThrowsException() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US);
        format.setFormats(new Format[]{new SimpleDateFormat("dd-MM-yyyy"), new SimpleDateFormat("dd-MM-yy")});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndexThrowsException() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US);
        format.setFormatsByArgumentIndex(new Format[]{new SimpleDateFormat("dd-MM-yyyy"), new SimpleDateFormat("dd-MM-yy")});
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0},{1}", Locale.US);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0},{1}", Locale.US);
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0},{2}", Locale.US);

        assertTrue(format1.equals(format1));
        assertTrue(format1.equals(format2));
        assertFalse(format1.equals(format3));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0},{1}", Locale.US);
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0},{1}", Locale.US);
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0},{2}", Locale.US);

        int hashCode1 = format1.hashCode();
        int hashCode2 = format2.hashCode();
        int hashCode3 = format3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,invalidPattern}", Locale.US);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US);
        format.applyPattern("{0},{2}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US);
        format.applyPattern("{0,date,invalidDescription}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnterminatedFormatElement() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US);
        format.applyPattern("{0},{1}{0}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidQuotedStringEscape() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US);
        format.applyPattern("{0,quote{{''}}}, {1}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnmatchedQuotedString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US);
        format.applyPattern("{0,quote{abc}, {1}}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapedCharacter() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US);
        format.applyPattern("{0,quote{xxxx}, {1}}");
    }

    @Test(expected = ParseException.class)
    public void testParseFormatArgumentIndexWithInvalidValue() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{2}", Locale.US);
        format.applyPattern("{0,invalidIndex}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormatFactory() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0},{1}", Locale.US, null);
    }

    @Test
    public void testPassingArguments() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}, {1}", Locale.US);
        Object[] args = new Object[]{new Date(), "Test"};
        String formattedMessage = format.format(args);
        String expectedMessage = "Test, " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(args[0]);
        assertEquals(expectedMessage, formattedMessage);
    }

    @Test
    public void testPassingCustomFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,customFormat:{MM-yy}}, {1}", Locale.US, new HashMap<>());
        format.getRegistry().put("customFormat", () -> new SimpleDateFormat("MM-yy"));
        Object[] args = new Object[]{new Date(), "Test"};
        String formattedMessage = format.format(args);
        String expectedMessage = "Test, 12-19";
        assertEquals(expectedMessage, formattedMessage);
    }

    
}