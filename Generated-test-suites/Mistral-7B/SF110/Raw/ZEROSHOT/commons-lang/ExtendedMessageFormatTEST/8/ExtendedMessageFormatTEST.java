package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.*;
import java.util.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

 @BeforeClass
 public static void setup() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new SimpleDateFormat("yyyy-MM-dd"));
 registry.put("time", new SimpleTimeFormat("HH:mm:ss"));
 registry.put("percent", new DecimalFormat("0.0%"));
 }

 @AfterClass
 public static void tearDown() {
 // Clean up the registry to prevent memory leaks
 ExtendedMessageFormat.setInstance(null);
 }

 @Test
 public void testEmptyConstruction() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("");
 assertEquals("", format.toPattern());
 assertNull(format.registry);
 }

 @Test
 public void testSimpleConstruction() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
 assertEquals("Hello {0}!", format.toPattern());
 assertNull(format.registry);
 }

 @Test
 public void testConstructionWithLocale() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}!", Locale.FRENCH);
 assertEquals("Hello {0}, {1}!", format.toPattern());
 assertNull(format.registry);
 }

 @Test
 public void testConstructionWithRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {date}, {time}!", null, registry);
 assertEquals("Hello {date}, {time}!", format.toPattern());
 assertEquals(registry, format.registry);
 }

 @Test
 public void testConstructionWithRegistryAndLocale() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {date}, {time}!", Locale.FRENCH, registry);
 assertEquals("Hello {date}, {time}!", format.toPattern());
 assertEquals(registry, format.registry);
 }

 @Test
 public void testToPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {date}, {time}!", null, registry);
 assertEquals("Hello {date}, {time}!", format.toPattern());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {format}!", null, registry);
 format.setFormat(0, new SimpleDateFormat("yyyy-MM-dd"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {format}!", null, registry);
 format.setFormatByArgumentIndex(0, new SimpleDateFormat("yyyy-MM-dd"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}!", null, registry);
 format.setFormats(new Format[0]);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}!", null, registry);
 format.setFormatsByArgumentIndex(new Format[0]);
 }

 @Test
 public void testEquals() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {date}, {time}!", null, registry);
 ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {date}, {time}!", null, registry);
 ExtendedMessageFormat format3 = new ExtendedMessageFormat("Hello {date}, {time}!", null, registry);
 ExtendedMessageFormat format4 = new ExtendedMessageFormat("Hello {date}, {time}!", Locale.FRENCH, registry);
 ExtendedMessageFormat format5 = new ExtendedMessageFormat("Hello {date}, {time}!", null, new HashMap<>());
 assertTrue(format1.equals(format1));
 assertTrue(format1.equals(format2));
 assertTrue(format1.equals(format3));
 assertFalse(format1.equals(format4));
 assertFalse(format1.equals(format5));
 }

 @Test
 public void testHashCode() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {date}, {time}!", null, registry);
 ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {date}, {time}!", null, registry);
 ExtendedMessageFormat format3 = new ExtendedMessageFormat("Hello {date}, {time}!", Locale.FRENCH, registry);
 ExtendedMessageFormat format4 = new ExtendedMessageFormat("Hello {date}, {time}!", null, new HashMap<>());ExtendedMessageFormat format5 = new ExtendedMessageFormat("Hello {percent}!", null, registry);
 assertEquals(format1.hashCode(), format2.hashCode());
 assertEquals(format1.hashCode(), format3.hashCode());
 assertNotEquals(format1.hashCode(), format4.hashCode());
 assertNotEquals(format1.hashCode(), format5.hashCode());
 }

 @Test
 public void testGetFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {date}!", null, registry);
 Format dateFormat = format.getFormat("date");
 assertEquals(SimpleDateFormat.class, dateFormat.getClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFormatWithInvalidDescription() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {date}!", null, registry);
 format.getFormat("invalid");
 }

 @Test
 public void testApplyPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {date}, {time}!", null, registry);
 format.applyPattern("Hello {percent}, {date}!");
 assertEquals("Hello {percent}, {date}!", format.toPattern());
 }

 @Test
 public void testApplyPatternWithRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {date}, {time}!", null, registry);
 format.applyPattern("Hello {percent}, {date}, {time}!");
 assertEquals("Hello {percent}, {date}, {time}!", format.toPattern());
 }

 @Test
 public void testApplyPatternWithCustomPatterns() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {date}, {time}!", null, registry);
 format.applyPattern("Hello {percent}, {date}, {time}, {customDate}!");
 assertEquals("Hello {percent}, {date}, {time}, {customDate}!", format.toPattern());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyPatternWithMalformedPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {date}!", null, registry);
 format.applyPattern("Hello {date}, {time}!");
 }

 @Test
 public void testToString() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {date}, {time}!", null, registry);
 String pattern = format.toString();
 assertEquals("ExtendedMessageFormat[pattern=Hello {date}, {time}!, locale=null, formats=[]]", pattern);
 }
}