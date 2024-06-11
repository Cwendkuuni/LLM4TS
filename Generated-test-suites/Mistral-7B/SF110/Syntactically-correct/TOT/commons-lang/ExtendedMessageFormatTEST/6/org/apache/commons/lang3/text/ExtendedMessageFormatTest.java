package org.apache.commons.lang3.test;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.SimpleDateFormatFactory;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

 private ExtendedMessageFormat format;
 private FormatFactory dateFormatFactory;

 @Before
 public void setup() {
 dateFormatFactory = new SimpleDateFormatFactory();
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", dateFormatFactory);
 format = new ExtendedMessageFormat("Hello {0,date,dd-MM-yyyy}");
 format.setLocale(Locale.ENGLISH);
 format.setRegistry(registry);
 }

 @Test
 public void testSimpleUsage() {
 String output = format.format(new Date());
 System.out.println(output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingFormatDescription() {
 format = new ExtendedMessageFormat("Hello {0}");
 format.applyPattern("Hello {0}");
 }

 @Test
 public void testCustomFormat() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", dateFormatFactory);
 format = new ExtendedMessageFormat("Hello {0,date,dd/MM/yy HH:mm:ss.SSS}");
 format.setLocale(Locale.ENGLISH);
 format.setRegistry(registry);
 format.applyPattern("Hello {0,date,dd/MM/yy HH:mm:ss.SSS}");
 String output = format.format(new Date());
 System.out.println(output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatElement() {
 format.applyPattern("Hello {0,date,dd/MM/yy HH:mm:ss.SSS} invalid");
 }

 @Test
 public void testQuotedString() {
 format = new ExtendedMessageFormat("Hello {0} '{1}' {2}");
 format.applyPattern("Hello {0} ''{1}'' {2}");
 String output = format.format("World", "Java", "Programming");
 System.out.println(output);
 }
}