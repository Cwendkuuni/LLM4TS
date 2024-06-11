package org.apache.commons.lang3.test;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.Validate;
import org.junit.Test;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

 @Test
 public void testSimplePattern() {
 final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
 Validate.isTrue(format.toPattern().equals("Hello {0}!"));
 final String result = format.format(new Object[]{"John"});
 assertEquals("Hello John!", result);
 }

 @Test
 public void testLocale() {
 final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, welcome to {1}!", Locale.FRENCH);
 Validate.isTrue(format.toPattern().equals("Hello {0}, welcome to {1}!"));
 final String result = format.format(new Object[]{"Jean-Pierre", "France"});
 assertEquals("Hello Jean-Pierre, welcome to France!", result);
 }

 @Test
 public void testRegistry() {
 final FormatFactory dateFormatFactory = new SimpleDateFormatFactory();
 final Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", dateFormatFactory);
 final ExtendedMessageFormat format = new ExtendedMessageFormat("Today is {date,date,dd-MM-yyyy}", registry);
 Validate.isTrue(format.toPattern().equals("Today is {date,date,dd-MM-yyyy}"));
 final String result = format.format(Collections.emptyMap());
 final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
 final Date today = new Date();
 final String expected = dateFormat.format(today);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPattern() {
 final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, {1}!");
 throw new AssertionError("Should not reach this point.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatDescription() {
 final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, invalid}", Collections.emptyMap());
 throw new AssertionError("Should not reach this point.");
 }

 @Test(expected = ParseException.class)
 public void testInvalidFormatElement() {
 final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {3}", Collections.emptyMap());
 throw new AssertionError("Should not reach this point.");
 }

 @Test
 public void testQuotedString() {
 final ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}! Your quote is {1}'{1}'!", Collections.emptyMap());
 final String result = format.format(new Object[]{"John", "'John'"});
 assertEquals("Hello John! Your quote is 'John''!", result);
 }

}