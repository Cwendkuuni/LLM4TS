package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.Translate;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Before;
import org.junit.Test;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

 private CharSequenceTranslator translator;
 private CharSequenceTranslator lookupTranslator;
 private CharSequenceTranslator aggregateTranslator;

 @Before
 public void setUp() {
 Translate translate = new Translate();
 lookupTranslator = new LookupTranslator(translate);
 translator = new AggregateTranslator(new CharSequenceTranslator[] { translate, lookupTranslator });
 aggregateTranslator = translator.with(lookupTranslator);
 }

 @Test
 public void testEmpty() {
 String result = translator.translate("");
 assertEquals("", result);
 }

 @Test
 public void testNull() {
 String result = translator.translate(null);
 assertNull(result);
 }

 @Test
 public void testTranslate() {
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
 String expected = "4142434445464748494A4B4C4D4E4F50515253545556575859";
 String result = translator.translate(input);
 assertEquals(expected, result);
 }

 @Test
 public void testTranslateWithLookup() {
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\u00A0\u00E2";
 String expected = "4142434445464748494A4B4C4D4E4F50515253545556575859\u00A0\u00E2";
 String result = lookupTranslator.translate(input);
 assertEquals(expected, result);
 }

 @Test
 public void testTranslateWithAggregate() {
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\u00A0\u00E2";
 String expected = "4142434445464748494A4B4C4D4E4F50515253545556575859\u00A0\u00E2";
 String result = aggregateTranslator.translate(input);
 assertEquals(expected, result);
 }

 @Test
 public void testTranslateCharSequence() throws IOException {
 Writer out = new StringWriter();
 String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\u00A0\u00E2";
 int length = translator.translate(input, 0, out);
 String result = out.toString();
 assertEquals(expected, result);
 assertEquals(expected.length(), length);
 }

 @Test
 public void testTranslateCharArray() throws IOException {
 Writer out = new StringWriter();
 char[] input = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '\u00A0', '\u00E2'};
 int index = 0;
 int length = translator.translate(input, index, out);
 String result = out.toString();
 assertEquals(expected, result);
 assertEquals(expected.length(), length);
 }

}