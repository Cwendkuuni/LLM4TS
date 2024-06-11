package org.apache.commons.lang3.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.junit.Test;

public class ExtendedMessageFormatTest {

 private static final String PATTERN_WITH_PLACEHOLDERS = "{0},{1},{2}";
 private static final String PATTERN_WITH_CUSTOM_FORMAT = "{0,'date','dd-MM-yyyy'}";
 private static final String PATTERN_WITH_INVALID_FORMAT = "{0,'invalidFormat'}";
 private static final Locale LOCALE_EN = Locale.ENGLISH;
 private static final Locale LOCALE_FR = Locale.FRENCH;

 @Test
 public void testExtendedMessageFormatWithDefaultRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 String result = format.format(new Object[]{"Hello", "World", "!"});
 assertEquals("Hello{0}, World{1}, !", format.toPattern());
 assertEquals("HelloWorld!", result);
 }

 @Test
 public void testExtendedMessageFormatWithDefaultRegistryAndLocale() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, LOCALE_EN);
 String result = format.format(new Object[]{"Hello", "World", "!"});
 assertEquals("Hello{0}, World{1}, !", format.toPattern());
 assertEquals("HelloWorld!", result);
 }

 @Test
 public void testExtendedMessageFormatWithCustomRegistry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new SimpleDateFormat("dd-MM-yyyy", LOCALE_EN));
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_CUSTOM_FORMAT, null, registry);
 Object[] args = new Object[]{"2022", LOCALE_EN};
 String result = format.format(args);
 assertEquals("{0,'date','dd-MM-yyyy'}", format.toPattern());
 assertEquals("22-12-2022", result);
 }

 @Test
 public void testExtendedMessageFormatWithCustomRegistryAndLocale() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new SimpleDateFormat("dd-MM-yyyy", LOCALE_FR));
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_CUSTOM_FORMAT, LOCALE_FR, registry);
 Object[] args = new Object[]{"2022", LOCALE_FR};
 String result = format.format(args);
 assertEquals("{0,'date','dd-MM-yyyy'}", format.toPattern());
 assertEquals("22-12-2022", result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatUnsupported() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 format.setFormat(1, new SimpleDateFormat("dd-MM-yyyy", LOCALE_EN));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndexUnsupported() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 format.setFormatByArgumentIndex(1, new SimpleDateFormat("dd-MM-yyyy", LOCALE_EN));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsUnsupported() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 Format[] formats = new Format[]{new SimpleDateFormat("dd-MM-yyyy", LOCALE_EN), new SimpleDateFormat("dd-MM-yyyy", LOCALE_FR)};
 format.setFormats(formats);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndexUnsupported() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 Format[] formats = new Format[]{new SimpleDateFormat("dd-MM-yyyy", LOCALE_EN), new SimpleDateFormat("dd-MM-yyyy", LOCALE_FR)};
 format.setFormatsByArgumentIndex(formats);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 assertThrows(IllegalArgumentException.class, () -> format.applyPattern("{ invalid }"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingFormatDescription() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_CUSTOM_FORMAT);
 assertThrows(IllegalArgumentException.class, () -> format.applyPattern(PATTERN_WITH_CUSTOM_FORMAT.replace(",'date','dd-MM-yyyy'", "")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormatDescription() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 assertThrows(IllegalArgumentException.class, () -> format.applyPattern("{0,'invalidFormat'}"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingFormatFactory() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_CUSTOM_FORMAT);
 assertThrows(IllegalArgumentException.class, () -> format.applyPattern("{0,'date','invalidFactory'}"));
 }

 @Test
 public void testEquals() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 ExtendedMessageFormat format2 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 ExtendedMessageFormat format3 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS, LOCALE_EN);
 Object[] args = new Object[]{"Hello", "World", "!"};
 ExtendedMessageFormat format4 = new ExtendedMessageFormat(format1);
 ExtendedMessageFormat format5 = new ExtendedMessageFormat(format2);
 ExtendedMessageFormat format6 = new ExtendedMessageFormat(format3);
 ExtendedMessageFormat format7 = new ExtendedMessageFormat(format4);

 assertEquals(format1, format1);
 assertEquals(format1, format4);
 assertEquals(format1.hashCode(), format4.hashCode());
 assertNotEquals(format1, format2);
 assertNotEquals(format1, format3);
 assertNotEquals(format1, args);
 assertNotEquals(format1, new Object());
 assertNotEquals(format1, null);

 assertEquals(format2, format2);
 assertEquals(format2, format5);
 assertEquals(format2.hashCode(), format5.hashCode());
 assertNotEquals(format1, format2);
 assertNotEquals(format2, format3);
 assertNotEquals(format2, args);
 assertNotEquals(format2, new Object());
 assertNotEquals(format2, null);

 assertEquals(format3, format3);
 assertEquals(format3, format6);
 assertEquals(format3.hashCode(), format6.hashCode());
 assertNotEquals(format1, format3);
 assertNotEquals(format2, format3);
 assertNotEquals(format3, args);
 assertNotEquals(format3, new Object());
 assertNotEquals(format3, null);

 assertEquals(format4, format4);
 assertEquals(format4, format7);
 assertEquals(format4.hashCode(), format7.hashCode());
 assertEquals(format1, format4);
 assertNotEquals(format1, format4);
 assertNotEquals(format2, format4);
 assertNotEquals(format3, format4);
 assertNotEquals(format4, args);
 assertNotEquals(format4, new Object());
 assertNotEquals(format4, null);
 }

}