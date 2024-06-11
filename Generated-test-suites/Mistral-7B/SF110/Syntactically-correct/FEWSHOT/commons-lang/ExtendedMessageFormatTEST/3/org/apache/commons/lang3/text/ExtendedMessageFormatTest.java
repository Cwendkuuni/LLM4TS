package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {
    private ExtendedMessageFormat messageFormat;
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "hh:mm aa";
    private static final String DATE_TIME_FORMAT = "dd/MM/yyyy hh:mm aa";

    @Before
    public void setUp() {
        Map<String, FormatFactory> formatRegistry = new HashMap<>();
        formatRegistry.put("date", new SimpleDateFormat(DATE_FORMAT, Locale.US));
        formatRegistry.put("time", new SimpleDateFormat(TIME_FORMAT, Locale.US));
        messageFormat = new ExtendedMessageFormat(DATE_TIME_FORMAT, Locale.US, formatRegistry);
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello, {0,date} {1,time}.";
        String expectedResult = "Hello, 23/01/2023 11:05 AM.";

        messageFormat.applyPattern(pattern);

        String result = messageFormat.format(new Object[]{new GregorianCalendar(2023, 0, 23).getTime(), new GregorianCalendar(2023, 0, 23, 11, 5, 0, Calendar.AM).getTime()});
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatArgumentIndex() {
        String pattern = "Hello, {{0,date} {1,unsupportedFormat}}.";
        messageFormat.applyPattern(pattern);
    }

    @Test
    public void testGetFormat() {
        Format format = messageFormat.getFormat("date");
        assertTrue(format instanceof SimpleDateFormat);
        assertEquals(DATE_FORMAT, format.toPattern());
        assertEquals(Locale.US, format.getLocale());
    }

    @Test
    public void testReadArgumentIndex() {
        String pattern = "Hello, {{0,{1}}}. {2,number,integer}.";
        int argumentIndex = messageFormat.readArgumentIndex(pattern, ',');
        assertEquals(1, argumentIndex);
    }

    @Test
    public void testParseFormatDescription() {
        String pattern = "Hello, {{{date}}}. {{{time}}}. {{{0,number,integer}}}. {{{1}}}. {{{2}}}. {{{3}}}. {{{4}}}. ";
        String formatDescription = messageFormat.parseFormatDescription(pattern, '{');
        assertEquals("date", formatDescription);
    }

    @Test
    public void testInsertFormats() {
        String pattern = "Hello, {{0}} {{1,date}} {{2,time}}.";
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("{3,date,short,EN}");
        String customPatternResult = messageFormat.insertFormats(pattern, customPatterns);
        assertEquals("Hello, {{0}} {{1,date}} {{3,date,short,EN}} {{2,time}}.", customPatternResult);
    }
}