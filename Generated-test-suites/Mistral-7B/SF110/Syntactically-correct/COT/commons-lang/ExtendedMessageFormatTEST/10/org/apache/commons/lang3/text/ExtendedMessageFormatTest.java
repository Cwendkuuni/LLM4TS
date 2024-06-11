package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.util.*;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.junit.Test;

public class ExtendedMessageFormatTest {

 private static final FormatFactory DUMMY_FORMAT_FACTORY = new FormatFactory() {
 @Override
 public Format getFormat(String name, String pattern, Locale locale) {
 throw new UnsupportedOperationException();
 }
 };

 @Test
 public void testConstructWithDefaultPattern() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{}");
 assertEquals("", messageFormat.toPattern());
 }

 @Test
 public void testConstructWithDefaultLocale() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{}", Locale.getDefault());
 assertEquals("", messageFormat.toPattern());
 }

 @Test
 public void testConstructWithDefaultRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{}", new HashMap<>());
 assertEquals("", messageFormat.toPattern());
 }

 @Test
 public void testConstructWithCustomPatternLocaleAndRegistry() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{}", Locale.getDefault(), new HashMap<>());
 assertEquals("", messageFormat.toPattern());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyPatternWithNullPattern() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("", Locale.getDefault(), new HashMap<>());
 messageFormat.applyPattern(null);
 }

 @Test
 public void testApplyPatternWithEmptyPattern() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("", Locale.getDefault(), new HashMap<>());
 messageFormat.applyPattern("");
 assertEquals("", messageFormat.toPattern());
 }

 @Test
 public void testApplyPatternWithSimplePattern() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{}", Locale.getDefault(), new HashMap<>());
 messageFormat.applyPattern("{}");
 assertEquals("{}", messageFormat.toPattern());
 }

 @Test
 public void testApplyPatternWithFormatElement() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{}", Locale.getDefault(), new HashMap<>());
 messageFormat.applyPattern("{0}");
 assertEquals("{0}", messageFormat.toPattern());
 }

 @Test
 public void testApplyPatternWithFormatElementAndFormat() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{}", Locale.getDefault(), new HashMap<>());
 messageFormat.applyPattern("{0,date}");
 assertEquals("{0,date}", messageFormat.toPattern());
 }

 @Test
 public void testApplyPatternWithComplexPattern() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{}", Locale.getDefault(), new HashMap<>());
 messageFormat.applyPattern("{0,date,short} {1,choice,0#Hello|1#World}");
 assertEquals("{0,date,short} {1,choice,0#Hello|1#World}", messageFormat.toPattern());
 }

 @Test
 public void testApplyPatternWithMultipleFormatElements() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{}", Locale.getDefault(), new HashMap<>());
 messageFormat.applyPattern("{0} {1} {2}");
 assertEquals("{0} {1} {2}", messageFormat.toPattern());
 }

 @Test
 public void testApplyPatternWithEscapedQuotes() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{}", Locale.getDefault(), new HashMap<>());
 messageFormat.applyPattern("{0}'{}'{1}");
 assertEquals("{0}'{}'{1}", messageFormat.toPattern());
 }

 @Test
 public void testApplyPatternWithCustomFormat() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{}", Locale.getDefault(), new HashMap<>());
 messageFormat.applyPattern("{0,format}");
 assertEquals("{0,format}", messageFormat.toPattern());
 }

 @Test
 public void testApplyPatternWithCustomFormatAndFormatFactory() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("", Locale.getDefault(), new HashMap<String, FormatFactory>() {{ put("customFormat", DUMMY_FORMAT_FACTORY); }});
 messageFormat.applyPattern("{customFormat}");
 assertEquals("{customFormat}", messageFormat.toPattern());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testApplyPatternWithInvalidFormatElement() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("", Locale.getDefault(), new HashMap<>());
 messageFormat.applyPattern("{0,invalid}");
 }

 @Test
 public void testGetFormat() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0,date}", Locale.getDefault(), new HashMap<>());
 Format format = messageFormat.getFormat(0);
 assertNotNull(format);
 assertEquals("date", format.getClass().getName());
 }

 @Test
 public void testGetFormatWithNullName() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0,date}", Locale.getDefault(), new HashMap<>());
 Format format = messageFormat.getFormat(null);
 assertNull(format);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetFormatWithInvalidName() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0,date}", Locale.getDefault(), new HashMap<>());
 messageFormat.getFormat("invalid");
 }

 @Test
 public void testSetFormatByArgumentIndex() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}{1}", Locale.getDefault(), new HashMap<>());
 messageFormat.applyPattern("{0}{1}");
 Format format = new SimpleDateFormat("dd-MM-yyyy");
 messageFormat.setFormatByArgumentIndex(0, format);
 messageFormat.applyPattern("{0}{1}");
 assertEquals("{0,date(dd-MM-yyyy)}{1}", messageFormat.toPattern());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormat() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}", Locale.getDefault(), new HashMap<>());
 messageFormat.setFormat(0, new SimpleDateFormat("dd-MM-yyyy"));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormats() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}", Locale.getDefault(), new HashMap<>());
 messageFormat.setFormats(new Format[]{new SimpleDateFormat("dd-MM-yyyy")});
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndex() {
 ExtendedMessageFormat messageFormat = new ExtendedMessageFormat("{0}", Locale.getDefault(), new HashMap<>());
 messageFormat.setFormatsByArgumentIndex(new Format[]{new SimpleDateFormat("dd-MM-yyyy")});
 }

 @Test
 public void testEquals() {
 ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("{0}{1}", Locale.getDefault(), new HashMap<>());
 ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("{0}{1}", Locale.getDefault(), new HashMap<>());
 assertTrue(messageFormat1.equals(messageFormat2));
 ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat("{0}{1}", Locale.getDefault(), new HashMap<>());
 messageFormat3.applyPattern("{}");
 assertFalse(messageFormat1.equals(messageFormat3));
 ExtendedMessageFormat messageFormat4 = new ExtendedMessageFormat("{0}{1}", new Locale("en_US"), new HashMap<>());
 assertFalse(messageFormat1.equals(messageFormat4));
 }

 @Test
 public void testHashCode() {
 ExtendedMessageFormat messageFormat1 = new ExtendedMessageFormat("{0}{1}", Locale.getDefault(), new HashMap<>());
 ExtendedMessageFormat messageFormat2 = new ExtendedMessageFormat("{0}{1}", Locale.getDefault(), new HashMap<>());
 assertEquals(messageFormat1.hashCode(), messageFormat2.hashCode());
 ExtendedMessageFormat messageFormat3 = new ExtendedMessageFormat("{0}{1}", Locale.getDefault(), new HashMap<>());
 messageFormat3.applyPattern("{}");
 assertNotEqual(messageFormat1.hashCode(), messageFormat3.hashCode());
 ExtendedMessageFormat messageFormat4 = new ExtendedMessageFormat("{0}{1}", new Locale("en_US"), new HashMap<>());
 assertNotEqual(messageFormat1.hashCode(), messageFormat4.hashCode());
 }

}