package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link LookupTranslator} class.
 */
public class LookupTranslatorTest {

 private final CharSequence[][] lookup = {{"hello", "hi"}, {"world", "greetings"}, {"java", "programming language"}};

 @Test
 public void testTranslateWithLookupTable() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookup);
 Writer writer = new StringWriter();
 String input = "hello world java";
 int index = 0;
 int translatedLength = translator.translate(CharSequenceUtils.subsequence(input, index, input.length()), index, writer);
 String expected = "hi greetings programming language";
 String actual = writer.toString().substring(0, translatedLength);
 assertEquals(expected, actual);
 }

 @Test
 public void testTranslateWithEmptyLookupTable() throws Exception {
 LookupTranslator translator = new LookupTranslator(new CharSequence[0][0]);
 Writer writer = new StringWriter();
 String input = "test";
 int index = 0;
 int translatedLength = translator.translate(CharSequenceUtils.subsequence(input, index, input.length()), index, writer);
 assertNull(writer.toString());
 }

 @Test
 public void testTranslateWithInvalidSequence() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookup);
 Writer writer = new StringWriter();
 String input = "helloworldjavafoo";
 int index = 0;
 int translatedLength = translator.translate(CharSequenceUtils.subsequence(input, index, input.length()), index, writer);
 String expected = "helloworldjavafoo";
 String actual = writer.toString().substring(0, translatedLength);
 assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testTranslateWithNullInput() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookup);
 Writer writer = new StringWriter();
 String input = null;
 int index = 0;
 translator.translate(input, index, writer);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateWithIndexOutOfBounds() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookup);
 Writer writer = new StringWriter();
 String input = "helloworldjava";
 int index = 12;
 translator.translate(CharSequenceUtils.subsequence(input, index, input.length()), index, writer);
 }

 @Test
 public void testTranslateWithEmptyInput() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookup);
 Writer writer = new StringWriter();
 String input = "";
 int index = 0;
 int translatedLength = translator.translate(CharSequenceUtils.subsequence(input, index, input.length()), index, writer);
 String actual = writer.toString();
 assertEquals("", actual);
 }

 @Test
 public void testTranslateWithShortInput() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookup);
 Writer writer = new StringWriter();
 String input = "hello";
 int index = 0;
 int translatedLength = translator.translate(CharSequenceUtils.subsequence(input, index, input.length()), index, writer);
 String expected = "hi";
 String actual = writer.toString().substring(0, translatedLength);
 assertEquals(expected, actual);
 }

 @Test
 public void testTranslateWithLongInput() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookup);
 Writer writer = new StringWriter();
 String input = StringUtils.repeat("hello world java", 10);
 int index = 0;
 int translatedLength = translator.translate(CharSequenceUtils.subsequence(input, index, input.length()), index, writer);
 String expected = String.format("%10s%10s%10s%10s%10s%10s%10s%10s%10s%10s", StringUtils.repeat("hi", 10), StringUtils.repeat("greetings", 10), StringUtils.repeat("programming language", 10));
 String actual = writer.toString().substring(0, translatedLength);
 assertEquals(expected, actual);
 }
}