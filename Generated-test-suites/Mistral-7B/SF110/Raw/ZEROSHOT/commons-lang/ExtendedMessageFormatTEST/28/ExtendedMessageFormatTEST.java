package org.apache.commons.lang3.test;

import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;

public class ExtendedMessageFormatTest {

 private static final String BASE_PATTERN = "{0}, {1}, {2}";
 private static final String CUSTOM_PATTERN_1 = "{3,date,long:'Date'}";
 private static final String CUSTOM_PATTERN_2 = "{4,date,short:'Short Date'}";
 private static final String CUSTOM_PATTERN_3 = "{5,number,percentage:0.2}'Percentage'";

 @SuppressWarnings("unchecked")
 private static final Map<String, FormatFactory> FORMAT_FACTORY_MAP = new HashMap<String, FormatFactory>() {{
 put("date", new DateFormatFactory());
 }};

 @Test
 public void testExtendedMessageFormat() {
 Object[] args = new Object[]{"Hello", 42, new Date(), new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24), 1.23};

 ExtendedMessageFormat format = new ExtendedMessageFormat(BASE_PATTERN, Locale.ENGLISH);
 format.applyPattern(BASE_PATTERN);
 format.setLocale(Locale.ENGLISH);

 String result = format.format(args);
 assertEquals("Hello, 42, Thu Feb 01 10:33:17 CET 2023, Thu Feb 01 10:33:17 CET 2023, 123.00%", result);

 format = new ExtendedMessageFormat(BASE_PATTERN, Locale.ENGLISH, FORMAT_FACTORY_MAP);
 format.applyPattern(BASE_PATTERN + CUSTOM_PATTERN_1 + CUSTOM_PATTERN_2 + CUSTOM_PATTERN_3);

 result = format.format(args);
 assertEquals("Hello, 42, Thu Feb 01 10:33:17 CET 2023, Thu Feb 01 10:33:17 CET 2023, 123.00%Date: Thu Feb 01 10:33:17 CET 2023Short Date: Feb 01, 10:33, 2023Percentage: 123.00%", result);

 format = new ExtendedMessageFormat(BASE_PATTERN, Locale.ENGLISH, FORMAT_FACTORY_MAP);
 format.applyPattern(BASE_PATTERN + CUSTOM_PATTERN_1 + CUSTOM_PATTERN_2 + CUSTOM_PATTERN_3);
 format.setFormat("3", new PercentFormat("0.2"));

 result = format.format(args);
 assertEquals("Hello, 42, Thu Feb 01 10:33:17 CET 2023, Thu Feb 01 10:33:17 CET 2023, 123.25%", result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPattern() {
 String invalidPattern = "{0,invalidFormat}";
 ExtendedMessageFormat format = new ExtendedMessageFormat(invalidPattern, Locale.ENGLISH);
 format.applyPattern(invalidPattern);
 fail("Exception expected: IllegalArgumentException");
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingFormatFactory() {
 String missingFormatPattern = "{invalidFactory,integer,number:'Number'}";
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASE_PATTERN, Locale.ENGLISH, Map.of());
 format.applyPattern(BASE_PATTERN + missingFormatPattern);
 fail("Exception expected: MissingResourceException");
 }

 @Test
 public void testToString() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASE_PATTERN, Locale.ENGLISH);
 format.applyPattern(BASE_PATTERN);
 assertEquals("ExtendedMessageFormat[pattern=, locale=null, registry=null]", format.toString());

 format = new ExtendedMessageFormat(BASE_PATTERN, Locale.ENGLISH, FORMAT_FACTORY_MAP);
 format.applyPattern(BASE_PATTERN + CUSTOM_PATTERN_1 + CUSTOM_PATTERN_2 + CUSTOM_PATTERN_3);
 assertEquals("ExtendedMessageFormat[pattern=, locale=en_US, registry={date=class org.apache.commons.lang3.text.DateFormatFactory}]", format.toString());
 }
}

class DateFormatFactory implements FormatFactory {
 @Override
 public Format getFormat(String name, String format, Locale locale) {
 if (name.equals("date")) {
 return new SimpleDateFormat(format, locale);
 }
 return null;
 }
}

class PercentFormat extends DecimalFormat {
 public PercentFormat(String pattern) {
 super("0.##%", new Locale("en", "US"));
 }
}