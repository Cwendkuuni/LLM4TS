package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

public class SoundexTest {

 private Soundex soundex;

 @Test
 public void testDefaultSoundexInstance() {
 soundex = Soundex.US_ENGLISH;
 String input = "Apple";
 String expectedOutput = "A111";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = EncoderException.class)
 public void testInvalidInputEncoding() {
 soundex = Soundex.US_ENGLISH;
 String input = null;
 try {
 soundex.encode(input);
 } catch (EncoderException e) {
 // expected
 }
 }

 @Test
 public void testCustomSoundexInstance() {
 soundex = new Soundex("012301253210123101123101014401622101020102".toCharArray());
 String input = "Einvolved";
 String expectedOutput = "E112";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testWhiteSpaceHandling() {
 soundex = Soundex.US_ENGLISH;
 String input = " Apple ";
 String expectedOutput = "A111";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEmptyString() {
 soundex = Soundex.US_ENGLISH;
 String input = "";
 String expectedOutput = "";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSameStringComparison() {
 soundex = Soundex.US_ENGLISH;
 String input1 = "Apple";
 String input2 = "Apple";
 String expectedOutput = "A111";
 String actualOutput1 = soundex.encode(input1);
 String actualOutput2 = soundex.encode(input2);
 assertEquals(expectedOutput, actualOutput1);
 assertEquals(expectedOutput, actualOutput2);
 int difference = soundex.difference(input1, input2);
 assertEquals(4, difference);
 }

 @Test
 public void testDifferentStringComparison() {
 soundex = Soundex.US_ENGLISH;
 String input1 = "Apple";
 String input2 = "Banana";
 int difference = soundex.difference(input1, input2);
 assertEquals(0, difference);
 }

 @Test
 public void testEdgeCase1() {
 soundex = Soundex.US_ENGLISH;
 String input = "Aardvark";
 String expectedOutput = "A261";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEdgeCase2() {
 soundex = Soundex.US_ENGLISH;
 String input = "Hippopotamus";
 String expectedOutput = "H132";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEdgeCase3() {
 soundex = Soundex.US_ENGLISH;
 String input = "Indigo";
 String expectedOutput = "I423";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEdgeCase4() {
 soundex = Soundex.US_ENGLISH;
 String input = "Quiz";
 String expectedOutput = "K250";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEdgeCase5() {
 soundex = Soundex.US_ENGLISH;
 String input = "Through";
 String expectedOutput = "T202";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEdgeCase6() {
 soundex = Soundex.US_ENGLISH;
 String input = "Rhythm";
 String expectedOutput = "R202";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEdgeCase7() {
 soundex = Soundex.US_ENGLISH;
 String input = "Shh";
 String expectedOutput = "S200";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEdgeCase8() {
 soundex = Soundex.US_ENGLISH;
 String input = "Silence";
 String expectedOutput = "S150";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEdgeCase9() {
 soundex = Soundex.US_ENGLISH;
 String input = "Soundex";
 String expectedOutput = "S163";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testEdgeCase10() {
 soundex = Soundex.US_ENGLISH;
 String input = "Test";
 String expectedOutput = "T160";
 String actualOutput = soundex.encode(input);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testExceptionHandling() {
 soundex = Soundex.US_ENGLISH;
 String input = "!@#$%^&*()_+";
 try {
 soundex.encode(input);
 fail("Expected EncoderException");
 } catch (EncoderException e) {
 // Expected
 }
 }
}