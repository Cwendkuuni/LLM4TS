package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for LookupTranslator.
 */
public class LookupTranslatorTest {

 private CharSequence[][] lookup = new CharSequence[][] {{"hello", "hi"}, {"world", "planet"}, {"goodbye", "farewell"}};

 @Test
 public void testTranslationWithValidLookupTable() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "hello world goodbye";
 Writer out = new StringBuilder();
 int index = 0;
 int translationLength = translator.translate(input, index, out);
 assertEquals("hi planet farewell", out.toString());
 assertEquals(3, translationLength);
 }

 @Test
 public void testTranslationWithEmptyInput() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "";
 Writer out = new StringBuilder();
 int index = 0;
 int translationLength = translator.translate(input, index, out);
 assertEquals("", out.toString());
 assertEquals(0, translationLength);
 }

 @Test
 public void testTranslationWithInvalidInputLength() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "helloworldgoodbye";
 Writer out = new StringBuilder();
 int index = 0;
 int translationLength = translator.translate(input, index, out);
 assertEquals("hi planet farewell", out.toString());
 assertEquals(3, translationLength);
 }

 @Test
 public void testTranslationWithInvalidInputSequence() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "helloworldhello";
 Writer out = new StringBuilder();
 int index = 0;
 int translationLength = translator.translate(input, index, out);
 assertEquals("hi planet hi", out.toString());
 assertEquals(3, translationLength);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslationWithNullLookupTable() {
 LookupTranslator translator = new LookupTranslator(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTranslationWithEmptyLookupTable() {
 LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testTranslationWithIndexOutOfBounds() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "helloworldgoodbye";
 Writer out = new StringBuilder();
 int index = input.length();
 int translationLength = translator.translate(input, index, out);
 }

 @Test(expected = IOException.class)
 public void testTranslationWithNullWriter() throws Exception {
 LookupTranslator translator = new LookupTranslator(lookup);
 CharSequence input = "helloworldgoodbye";
 int index = 0;
 translator.translate(input, index, null);
 }
}