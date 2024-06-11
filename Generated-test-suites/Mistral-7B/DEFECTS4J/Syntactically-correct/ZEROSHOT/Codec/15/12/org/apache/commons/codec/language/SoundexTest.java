package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Soundex;

import junit.framework.TestCase;

/**
 * Test class for the Soundex class
 */
public class SoundexTest extends TestCase {

 /**
 * Test the US_ENGLISH encoding
 */
 public void testUS_ENGLISH() {
 Soundex soundex = Soundex.US_ENGLISH;

 // Test with empty string
 assertEquals("", soundex.encode(""));

 // Test some simple cases
 assertEquals("A100", soundex.encode("Apple"));
 assertEquals("B262", soundex.encode("Banana"));
 assertEquals("E111", soundex.encode("Elephant"));
 assertEquals("N161", soundex.encode("November"));
 assertEquals("S251", soundex.encode("Sunflower"));
 assertEquals("T210", soundex.encode("Tomato"));

 // Test with uppercase letters
 assertEquals("A100", soundex.encode("APPLE"));
 assertEquals("B262", soundex.encode("BANANA"));
 assertEquals("E111", soundex.encode("ELEPHANT"));
 assertEquals("N161", soundex.encode("NOVEMBER"));
 assertEquals("S251", soundex.encode("SUNFLOWER"));
 assertEquals("T210", soundex.encode("TOMATO"));

 // Test with numbers and special characters
 assertEquals("A100", soundex.encode("Apple1"));
 assertEquals("A100", soundex.encode("Apple#"));
 assertEquals("", soundex.encode(""));
 assertEquals("", soundex.encode(null));

 // Test with multiple words
 assertEquals("A100", soundex.encode("Apple Pie"));
 assertEquals("A100 A200", soundex.encode("Apple Pie Apple Sauce"));
 assertEquals("A100 A200 A100", soundex.encode("Apple Pie Apple Sauce Apple"));

 // Test with different encodings
 Soundex usEnglishSoundex = Soundex.US_ENGLISH;
 Soundex customSoundex = new Soundex(new char[]{'0', '1', '2', '3', '0', '1', '2', '4', '5'});
 assertEquals("A100", usEnglishSoundex.encode("Apple"));
 assertEquals("A100", customSoundex.encode("Apple"));
 assertEquals("1234", customSoundex.encode("Apple"));
 }

 /**
 * Test mapping with custom Soundex instance
 */
 public void testCustomMapping() {
 Soundex customSoundex = new Soundex(new char[]{'0', '1', '2', '3', '0', '1', '2', '4', '5'});

 // Test with empty string
 assertEquals("", customSoundex.encode(""));

 // Test some simple cases
 assertEquals("1234", customSoundex.encode("Apple"));
 assertEquals("2345", customSoundex.encode("Banana"));
 assertEquals("5111", customSoundex.encode("Elephant"));
 assertEquals("1616", customSoundex.encode("November"));
 assertEquals("2515", customSoundex.encode("Sunflower"));
 assertEquals("2102", customSoundex.encode("Tomato"));

 // Test with uppercase letters
 assertEquals("1234", customSoundex.encode("APPLE"));
 assertEquals("2345", customSoundex.encode("BANANA"));
 assertEquals("5111", customSoundex.encode("ELEPHANT"));
 assertEquals("1616", customSoundex.encode("NOVEMBER"));
 assertEquals("2515", customSoundex.encode("SUNFLOWER"));
 assertEquals("2102", customSoundex.encode("TOMATO"));

 // Test with numbers and special characters
 assertEquals("1234", customSoundex.encode("Apple1"));
 assertEquals("1234", customSoundex.encode("Apple#"));
 assertEquals("", customSoundex.encode(""));
 assertEquals("", customSoundex.encode(null));

 // Test with multiple words
 assertEquals("1234", customSoundex.encode("Apple Pie"));
 assertEquals("1234 2345", customSoundex.encode("Apple Pie Apple Sauce"));
 assertEquals("1234 2345 1234", customSoundex.encode("Apple Pie Apple Sauce Apple"));

 // Test difference method with customSoundex
 assertEquals(3, customSoundex.difference(customSoundex, "Apple", "Banana"));
 assertEquals(1, customSoundex.difference(customSoundex, "Apple", "Apple"));
 assertEquals(4, customSoundex.difference(customSoundex, "Apple", "Apple1"));
 }

 /**
 * Test encoding exception
 */
 public void testEncoderException() {
 Soundex soundex = new Soundex(new char[]{'0', '1', '2', '3', '0', '1', '2', '4', '5'});
 try {
 soundex.encode(5);
 fail("EncoderException expected");
 } catch (EncoderException e) {
 // expected
 }
 }

 /**
 * Test null object
 */
 public void testNullObject() {
 Soundex soundex = new Soundex(new char[]{'0', '1', '2', '3', '0', '1', '2', '4', '5'});
 assertNull(soundex.encode(null));
 }
}