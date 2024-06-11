package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for LookupTranslator.
 */
public class LookupTranslatorTest {

 private final LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"Java", "Python"}});

 /**
 * Test translation of a valid sequence.
 */
 @Test
 public void testTranslation() {
 String input = "Hello World Java";
 String expected = "Hi Earth Python";
 String result = translator.translateToString(input);
 assertEquals(expected, result);
 }

 /**
 * Test translation of an empty sequence.
 */
 @Test
 public void testTranslationEmpty() {
 String input = "";
 String expected = "";
 String result = translator.translateToString(input);
 assertEquals(expected, result);
 }

 /**
 * Test translation of a sequence shorter than the shortest key.
 */
 @Test
 public void testTranslationShort() {
 String input = "Hell";
 String expected = "Hell";
 String result = translator.translateToString(input);
 assertEquals(expected, result);
 }

 /**
 * Test translation of a sequence longer than the longest key.
 */
 @Test
 public void testTranslationLong() {
 String input = "Hello World Java Lorem Ipsum";
 String expected = "Hi Earth Python Lorem Ipsum";
 String result = translator.translateToString(input);
 assertEquals(expected, result);
 }

 /**
 * Test translation of a sequence containing an invalid sequence.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testTranslationInvalid() {
 String input = "Hello World Java Lorem Ipsum Hello";
 }

 /**
 * Test translation of a sequence containing a key that is not present in the lookup table.
 */
 @Test
 public void testTranslationMissing() {
 String input = "Hello World Java Lorem Ipsum Apple";
 String expected = "Hello World Java Lorem Ipsum Apple";
 String result = translator.translateToString(input);
 assertEquals(expected, result);
 }
}