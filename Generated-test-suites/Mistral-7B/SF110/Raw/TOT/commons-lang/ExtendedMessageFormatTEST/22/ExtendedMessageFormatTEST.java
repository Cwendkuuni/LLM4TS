package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

 private static final String PATTERN_WITH_PLACEHOLDERS = "{0}, {1}, {2}";
 private static final String PATTERN_WITH_FORMATS = "{0, date, short}, {1, number, integer}, {2, number, floating}";

 @Test
 public void testEmptyConstructor() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("");
 assertEquals("", format.toPattern());
 }

 @Test
 public void testSimpleConstructor() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}!");
 assertEquals("Hello {0}", format.toPattern());
 }

 @Test
 public void testLocaleConstructor() {
 ExtendedMessageFormat formatFR = new ExtendedMessageFormat("Bonjour {0}!", Locale.FRENCH);
 assertEquals("Bonjour {0}", formatFR.toPattern());
 }

 @Test
 public void testRegistryConstructor() {
 Map<String, FormatFactory> registry = new HashMap<>();
 FormatFactory dateFormatFactory = new DateFormatFactory();
 registry.put("date", dateFormatFactory);
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short}", registry);
 assertEquals("{0, date, short}", format.toPattern());
 }

 @Test
 public void testComplexConstructor() {
 Map<String, FormatFactory> registry = new HashMap<>();
 FormatFactory dateFormatFactory = new DateFormatFactory();
 registry.put("date", dateFormatFactory);
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short}, {1, number, integer}, {2, number, floating}", registry);
 assertEquals("{0, date, short}, {1, number, integer}, {2, number, floating}", format.toPattern());
 }

 @Test
 public void testApplyingPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 assertArrayEquals(new Object[]{"apple", "banana", "cherry"}, format.parseObjects(new Object[]{"apple", "banana", "cherry"}, new Object[]{}, new ParsePosition(0)));
 }

 @Test
 public void testApplyingPatternWithFormats() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_FORMATS);
 assertArrayEquals(new Object[]{"2022-01-01", 1, 3.14}, format.parseObjects(new Object[]{"2022-01-01", 1, 3.14}, new Object[]{}, new ParsePosition(0)));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSettingFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 format.setFormat(0, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSettingFormatByArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 format.setFormatByArgumentIndex(0, null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSettingFormats() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 format.setFormats(new Format[0]);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSettingFormatsByArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 format.setFormatsByArgumentIndex(new Format[0]);
 }

 @Test
 public void testEquals() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 ExtendedMessageFormat format2 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 ExtendedMessageFormat format3 = new ExtendedMessageFormat(PATTERN_WITH_FORMATS);
 assertTrue(format1.equals(format1));
 assertTrue(format1.equals(format2));
 assertFalse(format1.equals(format3));
 }

 @Test
 public void testHashCode() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 ExtendedMessageFormat format2 = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 ExtendedMessageFormat format3 = new ExtendedMessageFormat(PATTERN_WITH_FORMATS);
 assertEquals(format1.hashCode(), format1.hashCode());
 assertEquals(format1.hashCode(), format2.hashCode());
 assertNotEquals(format1.hashCode(), format3.hashCode());
 }

 @Test
 public void testGetFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_FORMATS, new HashMap< String, FormatFactory >(){
 @Override
 public FormatFactory getFormatFactory(String key) {
 if ("date".equals(key)) {
 return new DateFormatFactory();
 }
 return null;
 }
 });
 Format dateFormat = format.getFormat("date");
 assertNotNull(dateFormat);
 }

 @Test(expected = NullPointerException.class)
 public void testGetFormatNullRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 format.getFormat("date");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadArgumentIndexInvalid() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 try {
 format.readArgumentIndex(null, new ParsePosition(0));
 fail();
 } catch (IllegalArgumentException e) {
}
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadArgumentIndexInvalidFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 try {
 format.readArgumentIndex("{X", new ParsePosition(0));
 fail();
 } catch (IllegalArgumentException e) {
}
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFormatDescriptionInvalid() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 try {
 format.parseFormatDescription(null, new ParsePosition(0));
 fail();
 } catch (IllegalArgumentException e) {
}
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFormatDescriptionInvalidFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 try {
 format.parseFormatDescription("{X", new ParsePosition(0));
 fail();
 } catch (IllegalArgumentException e) {
}
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFormatDescriptionMissingComma() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 try {
 format.parseFormatDescription("{date}format", new ParsePosition(0));
 fail();
 } catch (IllegalArgumentException e) {
}
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFormatDescriptionMissingFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 try {
 format.parseFormatDescription("{date}", new ParsePosition(0));
 fail();
 } catch (IllegalArgumentException e) {
}
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertFormatsEmptyPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 try {
 format.insertFormats(null, new ArrayList<>());
 fail();
 } catch (IllegalArgumentException e) {
}
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertFormatsEmptyCustomPatterns() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 try {
 format.insertFormats(PATTERN_WITH_PLACEHOLDERS, new ArrayList<>());
 fail();
 } catch (IllegalArgumentException e) {
}
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertFormatsIncompleteFormatElement() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 try {
 format.insertFormats(PATTERN_WITH_PLACEHOLDERS, new ArrayList<String>(){{
 add("{start");
 }});
 fail();
 } catch (IllegalArgumentException e) {
}
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertFormatsInvalidFormatIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 try {
 format.insertFormats(PATTERN_WITH_PLACEHOLDERS, new ArrayList<String>(){{
 add("{10}");
 }});
 fail();
 } catch (IllegalArgumentException e) {
}
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertFormatsInvalidCustomFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(PATTERN_WITH_PLACEHOLDERS);
 try {
 format.insertFormats(PATTERN_WITH_PLACEHOLDERS, new ArrayList<String>(){{
 add("{date, format}");
 }});
 fail();
 } catch (IllegalArgumentException e) {
}
 }
}