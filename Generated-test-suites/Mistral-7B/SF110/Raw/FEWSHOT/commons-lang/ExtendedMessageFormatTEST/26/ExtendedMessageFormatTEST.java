package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.ParsePosition;
import org.apache.commons.lang3.text.StrMatcher;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;

import java.text.*;
import java.util.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern);
        assertEquals("", messageFormat.toPattern());
        assertEquals(Locale.getDefault(), messageFormat.getLocale());
        assertNull(messageFormat.getRegistry());

        pattern = "{0,number,currency}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.US);
        assertEquals(pattern, messageFormat.toPattern());
        assertEquals(Locale.US, messageFormat.getLocale());
        assertNull(messageFormat.getRegistry());

        pattern = "{0,date,long,Full}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.US, new HashMap<>());
        assertEquals(pattern, messageFormat.toPattern());
        assertEquals(Locale.US, messageFormat.getLocale());
        assertNotNull(messageFormat.getRegistry());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.getDefault());

        Object[] objects = {"John Doe", 30};
        String formattedMessage = messageFormat.format(objects);
        assertEquals("Hello John Doe, your age is 30.", formattedMessage);

        pattern = "{0,number,currency}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.US);
        messageFormat.applyPattern("{0,number,currency} {1,number,currency}");
        Object[] objects2 = {100.5, 200.75};
        formattedMessage = messageFormat.format(objects2);
        assertEquals("$100.50 $200.75", formattedMessage);

        pattern = "{0,date,long,Full}";
        messageFormat = new ExtendedMessageFormat(pattern, Locale.US);
        messageFormat.applyPattern("{0,date,long,Full} {1,time,short,gregory}");
        Date date = new Date();
        formattedMessage = messageFormat.format(new Object[]{date, date});
        String expected = String.format("%tc %tb %td %ye %AM %HH:%MM %ss", date);
        assertEquals(expected, formattedMessage);
    }

    @Test
    public void testGetFormat() {
        String pattern = "{0,date,long,Full}";
        ExtendedMessageFormat messageFormat = new ExtendedMessageFormat(pattern, Locale.US, new HashMap<>());
        messageFormat.applyPattern("{0,date,long,Full} {1,number,percent}");
        Format dateFormat = messageFormat.getFormat("{0}");
        assertTrue(dateFormat instanceof DateFormat);
        DateFormat dateFormatCast = (DateFormat) dateFormat;
        assertEquals(Pattern.FULL, dateFormatCast.getCalendar().getTimeZone().inDaylightTime(new Date()) ? Pattern.FULL_DAYLIGHT : Pattern.FULL);

        Format percentFormat = messageFormat.getFormat("{1}");
        assertTrue(percentFormat instanceof DecimalFormat);
        DecimalFormat percentFormatCast = (DecimalFormat) percentFormat;
        assertEquals("#,##0.0%", percentFormatCast.toPattern());
    }

    @Test
    public void testEqualsAndHashCode() {
        String pattern1 = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat(pattern1, Locale.getDefault());
        String pattern2 = "Hello {1}, your age is {0}.";
        ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat(pattern2, Locale.getDefault());
        assertNotEquals(messageFormat1, messageFormat2);
        assertFalse(messageFormat1.equals(messageFormat2));
        assertNotEquals(messageFormat1.hashCode(), messageFormat2.hashCode());

        String pattern3 = "Hello {0}, your age is {1}.";
        ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat(pattern3, Locale.getDefault());
        messageFormat3.setRegistry(new HashMap<>());
        messageFormat3.applyPattern("{0,number,integer} {1,date,long,Full}");
        ExtendedMessageFormat messageFormat4 = new ExtendedMessageFormat(pattern3, Locale.getDefault());
        messageFormat4.setRegistry(new HashMap<>());
        messageFormat4.applyPattern("{0,number,integer} {1,date,long,Full}");
        assertTrue(messageFormat3.equals(messageFormat4));
        assertTrue(messageFormat3.hashCode() == messageFormat4.hashCode());
    }
}