package org.apache.commons.lang3.text;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.apache.commons.lang3.text.FormatFactory;
import org.apache.commons.lang3.text.MessageFormat;

public class ExtendedMessageFormatTest {

 private static final String DUMMY_FORMAT = "{0,date,dd-MM-yyyy}, {1}";
 private static final String DUMMY_PATTERN = "{}";
 private static final char START_FMT = ',';
 private static final char END_FE = '}';
 private static final char START_FE = '{';
 private static final char QUOTE = '\'';

 @SuppressWarnings("unchecked")
 private static FormatFactory dummyFormatFactory = new FormatFactory() {
 @Override
 public Format getFormat(String name, String argument, Locale locale) {
 return new MessageFormat(DUMMY_PATTERN);
 }
 };

 @org.junit.Test
 public void testExtendedMessageFormatConstructorWithPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT);
 assertEquals(DUMMY_FORMAT, format.toPattern());
 }

 @org.junit.Test
 public void testExtendedMessageFormatConstructorWithPatternAndLocale() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT, Locale.ENGLISH);
 assertEquals(DUMMY_FORMAT, format.toPattern());
 }

 @org.junit.Test
 public void testExtendedMessageFormatConstructorWithPatternAndRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT, null, new HashMap<String, FormatFactory>() {{
 put("dummyFormatFactory", dummyFormatFactory);
 }});
 assertEquals(DUMMY_FORMAT, format.toPattern());
 }

 @org.junit.Test
 public void testExtendedMessageFormatConstructorWithPatternAndLocaleAndRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT, Locale.ENGLISH, new HashMap<String, FormatFactory>() {{
 put("dummyFormatFactory", dummyFormatFactory);
 }});
 assertEquals(DUMMY_FORMAT, format.toPattern());
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetFormatUnsupported() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT);
 format.setFormat(0, new MessageFormat(DUMMY_PATTERN));
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetFormatByArgumentIndexUnsupported() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT);
 format.setFormatByArgumentIndex(0, new MessageFormat(DUMMY_PATTERN));
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsUnsupported() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT);
 format.setFormats(new MessageFormat[0]);
 }

 @org.junit.Test(expected = UnsupportedOperationException.class)
 public void testSetFormatsByArgumentIndexUnsupported() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT);
 format.setFormatsByArgumentIndex(new MessageFormat[0]);
 }

 @org.junit.Test
 public void testEquals() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat(DUMMY_FORMAT);
 ExtendedMessageFormat format2 = new ExtendedMessageFormat(DUMMY_FORMAT);
 assertTrue(format1.equals(format2));
 }

 @org.junit.Test
 public void testHashCode() {
 ExtendedMessageFormat format1 = new ExtendedMessageFormat(DUMMY_FORMAT);
 ExtendedMessageFormat format2 = new ExtendedMessageFormat(DUMMY_FORMAT);
 assertEquals(format1.hashCode(), format2.hashCode());
 }

 @org.junit.Test
 public void testGetFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT, null, new HashMap<String, FormatFactory>() {{
 put("dummyFormatFactory", dummyFormatFactory);
 }});
 String name = "dummyFormatFactory";
 Format result = format.getFormat(name);
 assertTrue(result instanceof MessageFormat);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testGetFormatWithNullName() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT, null, new HashMap<String, FormatFactory>() {{
 put("dummyFormatFactory", dummyFormatFactory);
 }});
 format.getFormat(null);
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testGetFormatWithEmptyName() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT, null, new HashMap<String, FormatFactory>() {{
 put("dummyFormatFactory", dummyFormatFactory);
 }});
 format.getFormat("");
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testGetFormatWithInvalidName() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT, null, new HashMap<String, FormatFactory>() {{
 put("dummyFormatFactory", dummyFormatFactory);
 }});
 format.getFormat("invalidName");
 }

 @org.junit.Test
 public void testApplyPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT, null, new HashMap<String, FormatFactory>() {{
 put("dummyFormatFactory", dummyFormatFactory);
 }});
 format.applyPattern("{0,date,dd-MM-yyyy},{1}");
 assertEquals(DUMMY_FORMAT, format.toPattern());
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testApplyPatternWithInvalidFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT, null, new HashMap<String, FormatFactory>() {{
 put("dummyFormatFactory", dummyFormatFactory);
 }});
 format.applyPattern("{invalidFormat}");
 }

 @org.junit.Test
 public void testApplyPatternWithCustomFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT, null, new HashMap<String, FormatFactory>() {{
 put("dummyFormatFactory", dummyFormatFactory);
 }});
 format.applyPattern("{0,customFormat,dummy}");
 assertEquals("{0,customFormat,dummy}", format.toPattern());
 }

 @org.junit.Test(expected = IllegalArgumentException.class)
 public void testApplyPatternWithInvalidCustomFormat() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT, null, new HashMap<String, FormatFactory>() {{
 put("dummyFormatFactory", dummyFormatFactory);
 }});
 format.applyPattern("{0,invalidCustomFormat,dummy}");
 }

 @org.junit.Test
 public void testToPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat(DUMMY_FORMAT, null, new HashMap<String, FormatFactory>() {{
 put("dummyFormatFactory", dummyFormatFactory);
 }});
 assertEquals(DUMMY_FORMAT, format.toPattern());
 }
}