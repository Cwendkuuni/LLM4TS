package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

 private static final CharSequenceTranslator IDENTITY = new IdentityTranslator();
 private static final CharSequenceTranslator HEX = new HexTranslator();

 @Test
 public void testIdentityTranslation() {
 CharSequence input = "Hello, World!";
 String result = IDENTITY.translate(input);
 assertEquals(input.toString(), result);
 }

 @Test
 public void testIdentityTranslationWithEmptyInput() {
 CharSequence input = Character.EMPTY;
 String result = IDENTITY.translate(input);
 assertNull(result);
 }

 @Test
 public void testIdentityTranslationWithNullInput() {
 CharSequence input = null;
 try {
 IDENTITY.translate(input);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testHexTranslation() {
 CharSequence input = "abcd\u00E5ef";
 String result = HEX.translate(input);
 assertEquals("414243445C3A5C3A4546", result);
 }

 @Test
 public void testHexTranslationWithEmptyInput() {
 CharSequence input = Character.EMPTY;
 String result = HEX.translate(input);
 assertNull(result);
 }

 @Test
 public void testHexTranslationWithNullInput() {
 CharSequence input = null;
 try {
 HEX.translate(input);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testIdentityAndHexTranslation() {
 CharSequence input = "Hello, World! abcd\u00E5ef";
 AggregateTranslator translator = IDENTITY.with(HEX);
 String result = translator.translate(input);
 assertEquals("Hello, World! 414243445C3A5C3A4546", result);
 }
}