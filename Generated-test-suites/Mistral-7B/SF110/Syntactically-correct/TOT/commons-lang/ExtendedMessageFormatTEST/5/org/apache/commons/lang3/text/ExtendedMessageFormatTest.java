package org.apache.commons.lang3.text;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ExtendedMessageFormatTest {

 @Test
 public void testEmptyPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("");
 assertEquals("", format.toPattern());
 }

 @Test
 public void testDefaultLocalePattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}!");
 assertEquals("Hello {0}, {1}!", format.toPattern());
 Format[] formats = format.getFormats();
 assertNull(formats);
 }

 @Test
 public void testLocalePattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}!", Locale.FRENCH);
 assertEquals("Hello {0}, {1}!", format.toPattern());
 Format[] formats = format.getFormats();
 assertNull(formats);
 }

 @Test
 public void testRegistryPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0,date,\"dd-MM-yyyy\"}, {1}!", new HashMap<String, FormatFactory>() {{
 put("date", new SimpleDateFormat("dd-MM-yyyy"));
 }});
 assertEquals("Hello {0,date,\"dd-MM-yyyy\"}, {1}!", format.toPattern());
 Format[] formats = format.getFormats();
 Format dateFormat = formats[0];
 assertEquals("dd-MM-yyyy", dateFormat.getClass().getSimpleName());
 }

 @Test
 public void testInvalidFormatArgumentIndex() {
 assertThrows(IllegalArgumentException.class, () -> new ExtendedMessageFormat("Hello {3, invalid}!", null));
 }

 @Test
 public void testInvalidFormatDescription() {
 assertThrows(IllegalArgumentException.class, () -> new ExtendedMessageFormat("Hello {invalid}!", null));
 }

 @Test
 public void testFormatDescriptionWithArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1} {2}!", new HashMap<String, FormatFactory>() {{
 put("date", new SimpleDateFormat("dd-MM-yyyy"));
 put("time", new SimpleTimeFormat("HH:mm:ss"));
 }});
 String pattern = format.toPattern();
 assertEquals("Hello {0}, {1} {2}!", pattern);
 Format[] formats = format.getFormats();
 Format dateFormat = formats[0];
 assertEquals("dd-MM-yyyy", dateFormat.getClass().getSimpleName());
 Format timeFormat = formats[1];
 assertEquals("HH:mm:ss", timeFormat.getClass().getSimpleName());
 }

 @Test
 public void testFormatDescriptionWithCustomPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0, date, \"dd-MM-yyyy\"}, {1, time, \"HH:mm:ss z\"}!", new HashMap<String, FormatFactory>() {{
 put("date", (format, name, args, locale) -> new SimpleDateFormat("dd-MM-yyyy"));
 put("time", (format, name, args, locale) -> new SimpleTimeFormat("HH:mm:ss z"));
 put("custom", (format, name, args, locale) -> new CustomFormat("Custom {0}: {1}"));
 }});
 String pattern = format.toPattern();
 assertEquals("Hello {0, date, \"dd-MM-yyyy\"}, {1, time, \"HH:mm:ss z\"} {2, custom, \"Custom {0}: {1}\"}!", pattern);
 Format[] formats = format.getFormats();
 Format dateFormat = formats[0];
 assertEquals("dd-MM-yyyy", dateFormat.getClass().getSimpleName());
 Format timeFormat = formats[1];
 assertEquals("HH:mm:ss z", timeFormat.getClass().getSimpleName());
 Format customFormat = formats[2];
 assertEquals("Custom {0}: {1}", customFormat.getClass().getSimpleName());
 }

 @Test
 public void testFormatDescriptionWithMultipleFormats() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, \"dd-MM-yyyy\"} {1, time, \"HH:mm:ss z\"}!", new HashMap<String, FormatFactory>() {{
 put("date", (format, name, args, locale) -> new SimpleDateFormat("dd-MM-yyyy"));
 put("time", (format, name, args, locale) -> new SimpleTimeFormat("HH:mm:ss z"));
 put("custom", (format, name, args, locale) -> new CustomFormat("Custom {0}: {1}"));
 put("format", (format, name, args, locale) -> new MessageFormat("Format {0} {1} {2}"));
 }});
 String pattern = format.toPattern();
 assertEquals("{0, date, \"dd-MM-yyyy\"} {1, time, \"HH:mm:ss z\"} {2, custom, \"Custom {0}: {1}\"}!", pattern);
 Format[] formats = format.getFormats();
 Format dateFormat = formats[0];
 assertEquals("dd-MM-yyyy", dateFormat.getClass().getSimpleName());
 Format timeFormat = formats[1];
 assertEquals("HH:mm:ss z", timeFormat.getClass().getSimpleName());
 Format customFormat = formats[2];
 assertEquals("Custom {0}: {1}", customFormat.getClass().getSimpleName());
 Format messageFormat = formats[3];
 assertEquals("Format {0} {1} {2}", messageFormat.getClass().getSimpleName());
 }

 @Test
 public void testFormatWithInvalidFormatFactory() {
 assertThrows(IllegalArgumentException.class, () -> new ExtendedMessageFormat("{0, invalid, \"invalid\"}!", null));
 }

 @Test
 public void testFormatWithEmptyFormatDescription() {
 assertThrows(IllegalArgumentException.class, () -> new ExtendedMessageFormat("{0, , \"invalid\"}!", null));
 }
}