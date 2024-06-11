package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CaverphoneTest {

 Caverphone caverphone = new Caverphone();

 @Test
 public void testNullInput() {
 String result = caverphone.caverphone(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testEmptyInput() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testSimpleString() {
 String input = "apple";
 String expected = "12233A3F2F";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testStringWithInitialE() {
 String input = "enough";
 String expected = "enou2f12233A3F2F";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testStringWithStartOptions() {
 String input = "coughtough";
 String expected = "cou2fTou2f12233A3F2F";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testStringWithHandledReplacements() {
 String input = "ceciqeatcy";
 String expected = "si2q3kA2tse12233A3F2F";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testStringWithHandledRemovals() {
 String input = "thissstringhasnoneofthehandledreplacements";
 String expected = "S3T3h3a12233A3F2F";
 String result = caverphone.caverphone(input);
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeObject() throws EncoderException {
 String input = "testString";
 String expected = "12233A3F2F";
 String result = (String) caverphone.encode(input);
 assertEquals(expected, result);
 }

 @Test
 public void testIsCaverphoneEqualTrue() {
 String string1 = "testString1";
 String string2 = "testString2";
 String string3 = "testString1";
 boolean result1 = caverphone.isCaverphoneEqual(string1, string2);
 assert False(result1);
 boolean result2 = caverphone.isCaverphoneEqual(string1, string3);
 assert True(result2);
 }

 @Test
 public void testIsCaverphoneEqualFalse() {
 String string1 = "testString1";
 String string2 = "testString2";
 String string3 = "testString3";
 boolean result1 = caverphone.isCaverphoneEqual(string1, string2);
 assert False(result1);
 boolean result2 = caverphone.isCaverphoneEqual(string1, string3);
 assert False(result2);
 }
}