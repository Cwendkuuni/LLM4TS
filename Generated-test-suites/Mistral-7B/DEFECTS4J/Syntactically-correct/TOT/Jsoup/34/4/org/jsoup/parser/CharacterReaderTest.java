package org.jsoup.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CharacterReaderTest {

 @Test
 void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 Assertions.assertTrue(reader.isEmpty());
 }

 @Test
 void testBasicConsume() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assertions.assertEquals('a', reader.consume());
 Assertions.assertEquals('b', reader.consume());
 Assertions.assertEquals('c', reader.consume());
 Assertions.assertEquals('d', reader.consume());
 Assertions.assertEquals('e', reader.consume());
 Assertions.assertEquals('f', reader.consume());
 Assertions.assertTrue(reader.isEmpty());
 }

 @Test
 void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.mark();
 Assertions.assertEquals('a', reader.consume());
 reader.rewindToMark();
 Assertions.assertEquals('a', reader.current());
 }

 @Test
 void testConsumeToCharacter() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeTo('d');
 Assertions.assertEquals("abc", consumed);
 }

 @Test
 void testConsumeToCharacter_AtEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeTo('g');
 Assertions.assertEquals("", consumed);
 }

 @Test
 void testConsumeToSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijkl");
 String consumed = reader.consumeTo("def");
 Assertions.assertEquals("abcdef", consumed);
 }

 @Test
 void testConsumeToSequence_AtEnd() {
 CharacterReader reader = new CharacterReader("abcdefghijkl");
 String consumed = reader.consumeTo("mno");
 Assertions.assertEquals("", consumed);
 }

 @Test
 void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("abcd123efghijk");
 String consumed = reader.consumeToAny('1', '2');
 Assertions.assertEquals("123", consumed);
 }

 @Test
 void testConsumeToAny_AtEnd() {
 CharacterReader reader = new CharacterReader("abcd123efghijk");
 String consumed = reader.consumeToAny('m', 'n');
 Assertions.assertEquals("", consumed);
 }

 @Test
 void testNextIndexOf_Character() {
 CharacterReader reader = new CharacterReader("abcabcabc");
 int index = reader.nextIndexOf('a');
 Assertions.assertEquals(3, index);
 }

 @Test
 void testNextIndexOf_Character_NotPresent() {
 CharacterReader reader = new CharacterReader("abcabcabc");
 int index = reader.nextIndexOf('x');
 Assertions.assertEquals(-1, index);
 }

 @Test
 void testNextIndexOf_Sequence() {
 CharacterReader reader = new CharacterReader("abcdefabcdef");
 int index = reader.nextIndexOf("def");
 Assertions.assertEquals(5, index);
 }

 @Test
 void testNextIndexOf_Sequence_NotPresent() {
 CharacterReader reader = new CharacterReader("abcdefabcdef");
 int index = reader.nextIndexOf("xyz");
 Assertions.assertEquals(-1, index);
 }

 @Test
 void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
 String consumed = reader.consumeLetterSequence();
 Assertions.assertEquals("abcde", consumed);
 }

 @Test
 void testConsumeLetterSequence_AtEnd() {
 CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
 String consumed = reader.consumeLetterSequence();
 Assertions.assertEquals("", consumed);
 }

 @Test
 void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abc123def456");
 String consumed = reader.consumeLetterThenDigitSequence();
 Assertions.assertEquals("abc123", consumed);
 }

 @Test
 void testConsumeLetterThenDigitSequence_AtEnd() {
 CharacterReader reader = new CharacterReader("abc123def456");
 String consumed = reader.consumeLetterThenDigitSequence();
 Assertions.assertEquals("", consumed);
 }

 @Test
 void testMatches_Character() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assertions.assertTrue(reader.matches('e'));
 Assertions.assertFalse(reader.matches('f'));
 }

 @Test
 void testMatches_Sequence() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assertions.assertTrue(reader.matches("def"));
 Assertions.assertFalse(reader.matches("ghi"));
 }

 @Test
 void testMatchesIgnoreCase_Character() {
 CharacterReader reader = new CharacterReader("abcDEF");
 Assertions.assertTrue(reader.matchesIgnoreCase("d"));
 Assertions.assertFalse(reader.matchesIgnoreCase("z"));
 }

 @Test
 void testMatchesIgnoreCase_Sequence() {
 CharacterReader reader = new CharacterReader("abcDEF");
 Assertions.assertTrue(reader.matchesIgnoreCase("ef"));
 Assertions.assertFalse(reader.matchesIgnoreCase("xyz"));
 }

 @Test
 void testMatchesAny_Character() {
 CharacterReader reader = new CharacterReader("abcd123efghijk");
 Assertions.assertTrue(reader.matchesAny('1'));
 Assertions.assertTrue(reader.matchesAny('e'));
 Assertions.assertFalse(reader.matchesAny('x'));
 }

 @Test
 void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assertions.assertTrue(reader.matchesLetter());
 Assertions.assertFalse(reader.matchesLetter());
 }

 @Test
 void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("abc123");
 Assertions.assertTrue(reader.matchesDigit());
 Assertions.assertFalse(reader.matchesDigit());
 }

 @Test
 void testMatchConsume_Character() {
 CharacterReader reader = new CharacterReader("abcde");
 Assertions.assertTrue(reader.matchConsume('e'));
 Assertions.assertFalse(reader.matchConsume('f'));
 }

 @Test
 void testMatchConsumeIgnoreCase_Character() {
 CharacterReader reader = new CharacterReader("ABCDE");
 Assertions.assertTrue(reader.matchConsumeIgnoreCase('e'));
 Assertions.assertFalse(reader.matchConsumeIgnoreCase('f'));
 }

 @Test
 void testContainsIgnoreCase_Sequence() {
 CharacterReader reader = new CharacterReader("</title><body><h1>Hello</h1></body>");
 Assertions.assertTrue(reader.containsIgnoreCase("<title>"));
 Assertions.assertTrue(reader.containsIgnoreCase("<body>"));
 Assertions.assertFalse(reader.containsIgnoreCase("<head>"));
 }
}