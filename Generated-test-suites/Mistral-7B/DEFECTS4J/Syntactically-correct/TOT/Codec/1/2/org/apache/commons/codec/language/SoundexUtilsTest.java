package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link SoundexUtils} class
 */
public class SoundexUtilsTest {

 /**
  * Test method for {@link SoundexUtils#clean(String)}
  */
 @Test
 public void testClean() {
 String input = "The quick Brown fox jumps over the lazy dog.";
 String expected = "THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG.";
 String result = SoundexUtils.clean(input);
 Assert.assertEquals(expected, result);
 }

 /**
  * Test method for {@link SoundexUtils#difference(StringEncoder, String, String)}
  */
 @Test
 public void testDifference() throws EncoderException {
 StringEncoder encoder = new org.apache.commons.codec.Decoder(); // Use Decoder instead of Encoder for testing
 String s1 = "Apple";
 String s2 = "Orange";
 int expected = 0;
 int result = SoundexUtils.difference(encoder, s1, s2);
 Assert.assertEquals(expected, result);

 s1 = "Apple";
 s2 = "Apfel";
 expected = 3;
 result = SoundexUtils.difference(encoder, s1, s2);
 Assert.assertEquals(expected, result);

 s1 = "Apple";
 s2 = "Apple";
 expected = 4;
 result = SoundexUtils.difference(encoder, s1, s2);
 Assert.assertEquals(expected, result);
 }

 /**
  * Test method for {@link SoundexUtils#differenceEncoded(String, String)}
  */
 @Test
 public void testDifferenceEncoded() {
 String es1 = "122321010";
 String es2 = "120321010";
 int expected = 3;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 Assert.assertEquals(expected, result);
 }

 /**
  * Test method for {@link SoundexUtils#differenceEncoded(String, String)} with empty strings
  */
 @Test
 public void testDifferenceEncodedEmptyStrings() {
 String es1 = "";
 String es2 = "";
 int expected = 0;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 Assert.assertEquals(expected, result);
 }

 /**
  * Test method for {@link SoundexUtils#differenceEncoded(String, String)} with null strings
  */
 @Test(expected = NullPointerException.class)
 public void testDifferenceEncodedNullStrings() {
 String es1 = null;
 String es2 = null;
 int result = SoundexUtils.differenceEncoded(es1, es2);
 }
}