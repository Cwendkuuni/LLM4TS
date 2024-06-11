package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for LookupTranslator.
 */
public class LookupTranslatorTest {

 private CharSequence[][] lookupTable = {{"foo", "bar"}, {"hello", "world"}, {"apple", "orange"}};

 @Test
 public void testTranslateWithValidInput() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookupTable);
 Writer out = new StringWriter();
 String input = "foobarhelloaPPLE";
 int index = 0;
 int translatedLength = translator.translate(input, index, out);
 String expected = "barworldorange";
 String actual = out.toString();
 assertEquals(expected, actual.substring(0, translatedLength));
 }

 @Test
 public void testTranslateWithShortestInput() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookupTable);
 Writer out = new StringWriter();
 String input = "foo";
 int index = 0;
 int translatedLength = translator.translate(input, index, out);
 String expected = "bar";
 String actual = out.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testTranslateWithLongestInput() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookupTable);
 Writer out = new StringWriter();
 String input = "appleappleappleappleapple";
 int index = 0;
 int translatedLength = translator.translate(input, index, out);
 String expected = "orangeorangeorangeorangeorange";
 String actual = out.toString();
 assertEquals(expected, actual.substring(0, translatedLength));
 }

 @Test
 public void testTranslateWithMissingInput() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookupTable);
 Writer out = new StringWriter();
 String input = "quxquxquxquxqux";
 int index = 0;
 int translatedLength = translator.translate(input, index, out);
 assertEquals(0, translatedLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslateWithNullInput() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookupTable);
 Writer out = new StringWriter();
 String input = null;
 int index = 0;
 int translatedLength = translator.translate(input, index, out);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslateWithInputBeyondTableBounds() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookupTable);
 Writer out = new StringWriter();
 String input = "foobarfoobarfoobarfoo";
 int index = 11; // index should be less than input.length() - input.length() of "foo"
 int translatedLength = translator.translate(input, index, out);
 }

 @Test(expected = IllegalStateException.class)
 public void testTranslateWithTableEmpty() throws Exception {
 LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
 Writer out = new StringWriter();
 String input = "foobar";
 int index = 0;
 int translatedLength = translator.translate(input, index, out);
 }
}