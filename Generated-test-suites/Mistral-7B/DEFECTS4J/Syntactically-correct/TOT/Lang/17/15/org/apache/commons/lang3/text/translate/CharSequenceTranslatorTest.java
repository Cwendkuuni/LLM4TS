package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

 private final CharSequenceTranslator translator = new ASCIIToHTMLTranslator();

 @Test
 public void testTranslate() {
 String input = "This is a test!";
 String expectedOutput = "This is &lt;i&gt;a&lt;/i&gt; test!";
 String output = translator.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullWriter() {
 CharSequence input = "Input";
 try {
 translator.translate(input, null);
 fail();
 } catch (IllegalArgumentException iae) {
 // expected
 }
 }

 @Test
 public void testTranslatorWith() {
 CharSequenceTranslator htmlToASCII = new HTMLToASCIITranslator();
 CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] { translator, htmlToASCII });
 String input = "This is <i>a</i> test!";
 String expectedOutput = "This is a test!";
 String output = translator.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEmptyString() {
 String input = "";
 String output = translator.translate(input);
 assertEquals(input, output);
 }

 @Test
 public void testTranslateSurrogatePair() {
 String input = "\uD800\uDC00";
 String expectedOutput = "\uD800\uDC00"; // no translation for surrogate pair
 String output = translator.translate(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testTranslateEmptyWriter() {
 Writer writer = new StringWriter();
 String input = "Input";
 try {
 translator.translate(input, writer);
 fail();
 } catch (IOException ioe) {
 // expected
 }
 }

 @Test
 public void testTranslateNullInput() {
 String output = translator.translate(null);
 assertNull(output);
 }
}