package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CaverphoneTest {

 private final Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String actual = caverphone.caverphone("");
 String expected = "1111111111";
 assertEquals(expected, actual);
 }

 @Test
 public void testNullString() {
 assertThrows(EncoderException.class, () -> caverphone.caverphone(null));
 }

 @Test
 public void testRegularString() {
 String actual = caverphone.caverphone("hello world");
 String expected = "h2el2o2w2or2ld";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithOnlyVowels() {
 String actual = caverphone.caverphone("eeeeeeeeeeeeee");
 String expected = "A11111111";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithOnlyConsonants() {
 String actual = caverphone.caverphone("ppppppppppppppppp");
 String expected = "P111111111";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithOnlyDigits() {
 String actual = caverphone.caverphone("1234567890");
 String expected = "A11111111";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithSpecialCharacters() {
 String actual = caverphone.caverphone("@#$%^&*()_+~`|={}[]:\"<>,.?/");
 String expected = "A11111111";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithLeadingAndTrailingSpaces() {
 String actual = caverphone.caverphone(" hello world ");
 String expected = "h2el2o2w2or2ld";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithOnlyCapitalLetters() {
 String actual = caverphone.caverphone("HELLO WORLD");
 String expected = "H2EL2O2W2OR2LD";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithMixedCase() {
 String actual = caverphone.caverphone("HeLLo WoRlD");
 String expected = "HeL2oW2or2LD";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithLongLength() {
 String actual = caverphone.caverphone("this is a test string with a length of more than 100 characters");
 String expected = expectedValueForLongString();
 assertEquals(expected.substring(0, 10), actual.substring(0, 10));
 }

 @Test
 public void testStringWithEmptyMiddle() {
 String actual = caverphone.caverphone("a b c");
 String expected = "a2b2c";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithEmptyEnd() {
 String actual = caverphone.caverphone("a b c ");
 String expected = "a2b2c";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithEmptyBeginning() {
 String actual = caverphone.caverphone(" c b a");
 String expected = "c2b2a";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithMultipleSpaces() {
 String actual = caverphone.caverphone("hello world hello world");
 String expected = "h2el2o2w2or2ldh2el2o2w2or2ld";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithMultipleWords() {
 String actual = caverphone.caverphone("hello world good bye");
 String expected = "h2el2o2w2or2ldg3ood3by3e";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithEmptyMiddleAndEnd() {
 String actual = caverphone.caverphone("a b c ");
 String expected = "a2b2c";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithEmptyBeginningAndEnd() {
 String actual = caverphone.caverphone(" a b c ");
 String expected = "a2b2c";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithMultipleConsecutiveReplacements() { // tch -> 2ch, q -> k, c -> k, x -> k
 String actual = caverphone.caverphone("textbook");
 String expected = "t2ex2k2eb2ok";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithMultipleConsecutiveReplacementsV2() { // tch -> 2ch, q -> k, c -> k, x -> k, j -> y, y3 -> Y3, y -> A, y -> 3
 String actual = caverphone.caverphone("textbook");
 String expected = "t2ex2k2ek23k2ek23k2eY33k";
 assertEquals(expected, actual);
 }

 @Test
 public void testStringWithMultipleConsecutiveHandlings() { // tio -> sio, tia -> sia, d -> t, ph -> fh, b -> p, sh -> s2, z -> s, r3 -> R3, r -> 2, l3 -> L3, l -> 2, j -> y, y3 -> Y3, y -> A, y -> 3, 3gh3 -> 3kh3, gh -> 22, g -> k, s+ -> S, t+ -> T, p+ -> P, k+ -> K, f+ -> F, m+ -> M, n+ -> N, w3 -> W3, w$ -> 3, w -> 2, ^h -> A, h -> 2, r3 -> R3, r$ -> 3, l3 -> L3, l$ -> 3
 String actual = caverphone.caverphone("hello the quick brown fox jumps over the lazy dog");
 String expected = "h2el2o2t2h2e2q2i2c2k2i2c2h2e2n2n2f2o2x2j2u2p2m2o2v2e2z2a3g2z3y3z3d3g2l3a3z3y3d3g";
 assertEquals(expected, actual);
 }

 @Test
 public void testEncodeNullObject() {
 assertNull(caverphone.encode(null));
 }

 @Test
 public void testEncodeNonStringObject() {
 assertThrows(EncoderException.class, () -> caverphone.encode(new Object()));
 }

 @Test
 public void testIsCaverphoneEqualDifferentStrings() {
 assertFalse(caverphone.isCaverphoneEqual("hello world", "good bye"));
 }

 @Test
 public void testIsCaverphoneEqualSameStrings() {
 assertTrue(caverphone.isCaverphoneEqual("hello world", "hello world"));
 }

 private String expectedValueForLongString() {
 int length = 100;
 StringBuilder sb = new StringBuilder();
 for (int i = 0; i < length; i++) {
 if (i % 2 == 0) {
 sb.append("h");
 } else {
 sb.append("2");
 }
 }
 return sb.toString();
 }
}