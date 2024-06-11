package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphoneInstance;

 @Before
 public void setUp() {
 caverphoneInstance = new Caverphone();
 }

 @Test
 public void testNullString() {
 String input = null;
 String expectedOutput = "1111111111";
 String output = caverphoneInstance.caverphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEmptyString() {
 String input = "";
 String expectedOutput = "1111111111";
 String output = caverphoneInstance.caverphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLowerCaseString() {
 String input = "hello world";
 String expectedOutput = "h3l3l3o2w2o2r2l2d";
 String output = caverphoneInstance.caverphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStringWithOnlyLetters() {
 String input = "CavershamProject";
 String expectedOutput = "C2v3r5h4m3P2r3o3j3e3ct";
 String output = caverphoneInstance.caverphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testStartOptions() {
 String input = "CoughEnoughTrough";
 String expectedOutput = "cou2f2nou2ftrou2f";
 String output = caverphoneInstance.caverphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testHandleReplacements() {
 String input = "CeasarTchisCycles";
 String expectedOutput = "s3a2r2s3i2l3s2";
 String output = caverphoneInstance.caverphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testHandleRemovals() {
 String input = "TwoThousandTwentyTwo";
 String expectedOutput = "2Th20202";
 String output = caverphoneInstance.caverphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testHandleAdditions() {
 String input = "SeventeenMillionOneHundredTwentyThreeThousandFourHundredAndSixteen";
 String expectedOutput = "17M123000416";
 String output = caverphoneInstance.caverphone(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEqualsMethod() {
 String str1 = "hello world";
 String str2 = "hello world";
 String str3 = "hello wORLD";
 boolean expectedOutput1 = true;
 boolean expectedOutput2 = false;
 boolean output1 = caverphoneInstance.isCaverphoneEqual(str1, str2);
 boolean output2 = caverphoneInstance.isCaverphoneEqual(str1, str3);
 assertEquals(expectedOutput1, output1);
 assertEquals(expectedOutput2, output2);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringParameter() {
 int param = 12;
 caverphoneInstance.encode(param);
 }
}