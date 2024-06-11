package org.apache.commons.codec.language.test;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

 @Test
 public void testCleanMethod() {
 String input = "The quick Brown fox jumps over the lazy dog";
 String expectedOutput = "The quick Brown Fox Jumps over the lazy Dog";
 String actualOutput = SoundexUtils.clean(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = NullPointerException.class)
 public void testCleanNullInput() {
 String input = null;
 SoundexUtils.clean(input);
 }

 @Test
 public void testDifferenceMethodSoundex() {
 String encoderType = "Soundex";
 String s1 = "Apple";
 String s2 = "Apricot";
 int difference = SoundexUtils.difference(new Soundex(), s1, s2);
 int expectedOutput = 2;
 assertEquals(expectedOutput, difference);
 }

 @Test
 public void testDifferenceMethodSoundexIdentical() {
 String encoderType = "Soundex";
 String s1 = "Apple";
 String s2 = "Apple";
 int difference = SoundexUtils.difference(new Soundex(), s1, s2);
 int expectedOutput = 4;
 assertEquals(expectedOutput, difference);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceMethodSoundexNullInput() throws EncoderException {
 String encoderType = "Soundex";
 String s1 = null;
 String s2 = "Apple";
 Soundex soundex = new Soundex();
 int difference = SoundexUtils.difference(soundex, s1, s2);
 }

 @Test(expected = EncoderException.class)
 public void testDifferenceMethodSoundexNullInput2() throws EncoderException {
 String encoderType = "Soundex";
 String s1 = "Apple";
 String s2 = null;
 Soundex soundex = new Soundex();
 int difference = SoundexUtils.difference(soundex, s1, s2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDifferenceMethodSoundexEmptyString() {
 String encoderType = "Soundex";
 String s1 = "";
 String s2 = "Apple";
 Soundex soundex = new Soundex();
 int difference = SoundexUtils.difference(soundex, s1, s2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDifferenceMethodSoundexEmptyString2() {
 String encoderType = "Soundex";
 String s1 = "Apple";
 String s2 = "";
 Soundex soundex = new Soundex();
 int difference = SoundexUtils.difference(soundex, s1, s2);
 }

 @Test
 public void testDifferenceMethodRefinedSoundex() {
 String encoderType = "RefinedSoundex";
 String s1 = "Apple";
 String s2 = "Apricot";
 int difference = SoundexUtils.difference(new RefinedSoundex(), s1, s2);
 int expectedOutput = 3;
 assertEquals(expectedOutput, difference);
 }

}