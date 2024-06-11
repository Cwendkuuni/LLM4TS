package org.apache.commons.lang3.test;

import static org.junit.Assert.*;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.MessageFormat;
import org.apache.commons.lang3.text.SimpleDateFormat;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

 private static final String BASIC_PATTERN = "Hello {0}, your age is {1}.";
 private static final String FORMAT_DESCRIPTION_DATE = "date('yyyy-MM-dd')";
 private static final String FORMAT_DESCRIPTION_PERCENT = "percent(0.0)%";

 @SuppressWarnings("unchecked")
 @BeforeClass
 public static void setup() {
 Formatter formatter = new Formatter();
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("date", new SimpleDateFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault(), registry);
 formatter.format(format, new Object[]{"John Doe", 25});
 String result = formatter.toString();
 assertEquals(BASIC_PATTERN, format.toPattern());
 assertEquals("Hello John Doe, your age is 25.", result);
 }

 @Test
 public void testConstructorWithPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern with no placeholders {}");
 assertEquals("Pattern with no placeholders {}", format.toPattern());
 }

 @Test
 public void testConstructorWithPatternAndLocale() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern with no placeholders {0}", Locale.FRENCH);
 assertEquals("Pattern with no placeholders {0}", format.toPattern());
 }

 @Test
 public void testConstructorWithPatternAndRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern with no placeholders {0}", Locale.getDefault(), new HashMap<>());
 assertEquals("Pattern with no placeholders {0}", format.toPattern());
 }

 @Test
 public void testConstructorWithPatternPatternLocaleAndRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern with placeholders {0} and {1}", Locale.getDefault(), new HashMap<>());
 assertEquals("Pattern with placeholders {0} and {1}", format.toPattern());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatUnsupported() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 format.setFormat(1, new SimpleDateFormat());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndexUnsupported() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 format.setFormatByArgumentIndex(1, new SimpleDateFormat());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsUnsupported() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 format.setFormats(new MessageFormat[0]);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndexUnsupported() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 format.setFormatsByArgumentIndex(new MessageFormat[0]);
 }

 @Test
 public void testGetFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 FormatFactory factory = new SimpleDateFormatFactory();
 format.setFormat(FORMAT_DESCRIPTION_DATE, factory);
 Format dateFormat = format.getFormat(FORMAT_DESCRIPTION_DATE);
 assertEquals("date", dateFormat.getClass().getSimpleName());
 }

 @Test(expected = NullPointerException.class)
 public void testGetFormatNullDescription() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 format.getFormat(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFormatInvalidDescription() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 format.getFormat("InvalidDescription");
 }

 @Test
 public void testApplyPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern with placeholders {0} and {1}", Locale.getDefault());
 format.applyPattern(BASIC_PATTERN);
 assertEquals(BASIC_PATTERN, format.toPattern());
 }

 @Test
 public void testApplyPatternWithRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern with placeholders {0} and {1}", Locale.getDefault());
 format.applyPattern(BASIC_PATTERN);
 FormatFactory factory = new SimpleDateFormatFactory();
 format.setFormat(FORMAT_DESCRIPTION_DATE, factory);
 format.applyPattern(BASIC_PATTERN);
 assertEquals(BASIC_PATTERN, format.toPattern());
 }

 @Test
 public void testToPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern with placeholders {0} and {1}", Locale.getDefault());
 format.applyPattern(BASIC_PATTERN);
 format.applyPattern("Pattern with placeholders {0} and {1}");
 assertEquals(BASIC_PATTERN, format.toPattern());
 }

 @Test
 public void testEquals() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 ExtendedMessageFormat format2 = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 ExtendedMessageFormat format3 = new ExtendedMessageFormat(BASIC_PATTERN, Locale.FRENCH);
 ExtendedMessageFormat format4 = new ExtendedMessageFormat("Pattern with placeholders {0} and {1}", Locale.getDefault());
 ExtendedMessageFormat format5 = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault(), new HashMap<>());
 assertTrue(format1.equals(format1));
 assertTrue(format1.equals(format2));
 assertFalse(format1.equals(format3));
 assertFalse(format1.equals(format4));
 assertFalse(format1.equals(format5));
 }

 @Test
 public void testHashCode() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 ExtendedMessageFormat format2 = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 ExtendedMessageFormat format3 = new ExtendedMessageFormat(BASIC_PATTERN, Locale.FRENCH);
 ExtendedMessageFormat format4 = new ExtendedMessageFormat("Pattern with placeholders {0} and {1}", Locale.getDefault());
 ExtendedMessageFormat format5 = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault(), new HashMap<>());
 int hashCode1 = format1.hashCode();
 int hashCode2 = format2.hashCode();
 int hashCode3 = format3.hashCode();
 int hashCode4 = format4.hashCode();
 int hashCode5 = format5.hashCode();
 assertTrue(hashCode1 == hashCode2);
 assertFalse(hashCode1 == hashCode3);
 assertFalse(hashCode1 == hashCode4);
 assertFalse(hashCode1 == hashCode5);
 }

 @Test
 public void testInvalidFormatArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 try {
 format.readArgumentIndex(BASIC_PATTERN, new ParsePosition(-1));
 fail();
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testValidFormatArgumentIndex() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 ParsePosition pos = new ParsePosition(0);
 int index = format.readArgumentIndex(BASIC_PATTERN, pos);
 assertEquals(0, index);
 }

 @Test
 public void testParseFormatDescription() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 ParsePosition pos = new ParsePosition(0);
 String description = format.parseFormatDescription(BASIC_PATTERN, pos);
 assertEquals("", description);
 }

 @Test
 public void testParseFormatDescriptionCustomPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 ParsePosition pos = new ParsePosition(0);
 String description = format.parseFormatDescription(BASIC_PATTERN, pos);
 assertEquals("", description);
 ParsePosition pos2 = new ParsePosition(11);
 String description2 = format.parseFormatDescription(BASIC_PATTERN, pos2);
 assertEquals(FORMAT_DESCRIPTION_DATE, description2);
 }

 @Test
 public void testInsertFormats() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 format.applyPattern(BASIC_PATTERN);
 String result = format.format(new Object[]{"John Doe", 25});
 assertEquals("Hello John Doe, your age is 25.", result);
 ArrayList<String> customPatterns = new ArrayList<>();
 customPatterns.add(FORMAT_DESCRIPTION_DATE);
 String customPatternsResult = format.insertFormats(BASIC_PATTERN, customPatterns);
 result = format.format(new Object[]{new Object[]{"John Doe", new SimpleDateFormat(FORMAT_DESCRIPTION_DATE).format(new Date())}, 25});
 assertEquals("Hello John Doe, your age is date('yyyy-MM-dd'), 25.", result);
 assertEquals(customPatternsResult, BASIC_PATTERN);
 }

 @Test
 public void testInsertFormatsEmptyCustomPatterns() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 format.applyPattern(BASIC_PATTERN);
 String result = format.format(new Object[]{"John Doe", 25});
 assertEquals("Hello John Doe, your age is 25.", result);
 ArrayList<String> customPatterns = new ArrayList<>();
 String customPatternsResult = format.insertFormats(BASIC_PATTERN, customPatterns);
 assertEquals(BASIC_PATTERN, customPatternsResult);
 }

 @Test
 public void testInsertFormatsNullCustomPatterns() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 format.applyPattern(BASIC_PATTERN);
 String result = format.format(new Object[]{"John Doe", 25});
 assertEquals("Hello John Doe, your age is 25.", result);
 String customPatternsResult = format.insertFormats(BASIC_PATTERN, null);
 assertEquals(BASIC_PATTERN, customPatternsResult);
 }

 @Test
 public void testSeekNonWs() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 ParsePosition pos = new ParsePosition(0);
 int index = -1;
 try {
 index = format.seekNonWs(BASIC_PATTERN, pos);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // pass
 }
 assertEquals(-1, index);
 }

 @Test
 public void testNext() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 ParsePosition pos = new ParsePosition(0);
 int index = 0;
 ParsePosition nextPos = format.next(pos);
 assertEquals(1, index);
 assertEquals(1, nextPos.getIndex());
 }

 @Test
 public void testAppendQuotedString() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 StringBuilder appendTo = new StringBuilder();
 StringBuilder result = format.appendQuotedString(BASIC_PATTERN, new ParsePosition(0), appendTo, false);
 assertEquals("", result.toString());
 result = format.appendQuotedString(BASIC_PATTERN, new ParsePosition(11), appendTo, false);
 assertEquals("", result.toString());
 result = format.appendQuotedString(BASIC_PATTERN, new ParsePosition(13), appendTo, false);
 assertEquals("'age is'", result.toString());
 }

 @Test
 public void testAppendQuotedStringEscapedOn() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 StringBuilder appendTo = new StringBuilder();
 StringBuilder result = format.appendQuotedString(BASIC_PATTERN, new ParsePosition(11), appendTo, true);
 assertEquals("'date('yyyy-MM-dd')'", result.toString());
 result = format.appendQuotedString(BASIC_PATTERN, new ParsePosition(13), appendTo, true);
 assertEquals("'age is 25.\"", result.toString());
 }

 @Test
 public void testGetQuotedString() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 String result = format.getQuotedString(BASIC_PATTERN, new ParsePosition(11), false);
 assertEquals("", result);
 result = format.getQuotedString(BASIC_PATTERN, new ParsePosition(11), true);
 assertEquals("''", result);
 }

 @Test
 public void testFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 format.applyPattern(BASIC_PATTERN);
 String result = format.format(new Object[]{"John Doe", 25});
 assertEquals("Hello John Doe, your age is 25.", result);
 }

 @Test
 public void testFormatWithCustomFormats() throws ParseException {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 format.applyPattern(BASIC_PATTERN);
 FormatFactory factory = new SimpleDateFormatFactory();
 format.setFormat(FORMAT_DESCRIPTION_DATE, factory);
 Object[] arguments = new Object[]{new Object[]{"John Doe", new SimpleDateFormat(FORMAT_DESCRIPTION_DATE).format(new Date())}, 25};
 String result = format.format(arguments);
 assertEquals("Hello John Doe, your age is date('yyyy-MM-dd'), 25.", result);
 }

 @Test
 public void testFormatWithPlaceholders() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 format.applyPattern(BASIC_PATTERN);
 Object[] arguments = new Object[]{"John Doe", 25};
 String result = format.format(arguments);
 assertEquals("Hello John Doe, your age is 25.", result);
 }

 @Test
 public void testFormatWithPlaceholdersAndCustomFormats() throws ParseException {
 ExtendedMessageFormat format = new ExtendedMessageFormat(BASIC_PATTERN, Locale.getDefault());
 format.applyPattern(BASIC_PATTERN);
 FormatFactory factory = new SimpleDateFormatFactory();
 format.setFormat(FORMAT_DESCRIPTION_DATE, factory);
 Object[] arguments = new Object[]{new Object[]{"John Doe", new SimpleDateFormat(FORMAT_DESCRIPTION_DATE).format(new Date())}, 25};
 String result = format.format(arguments);
 assertEquals("Hello John Doe, your age is date('yyyy-MM-dd'), 25.", result);
 }

 @Test
 public void testToString() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("Pattern with placeholders {0} and {1}", Locale.getDefault());
 format.applyPattern("Pattern with placeholders {0} and {1}");
 String result = format.toString();
 ToStringBuilder toStringBuilder = new ToStringBuilder(format, ToStringStyle.SHORT_PREFIX_STYLE);
 toStringBuilder.append("pattern", format.toPattern());
 toStringBuilder.append("registry", format.registry);
 String expectedResult = toStringBuilder.toString();
 assertEquals(expectedResult, result);
 }
}