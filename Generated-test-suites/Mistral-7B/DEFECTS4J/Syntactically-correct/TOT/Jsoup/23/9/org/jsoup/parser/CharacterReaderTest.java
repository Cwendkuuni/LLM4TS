package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 // Test case 1: Testing isEmpty() method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());
 }

 // Test case 2: Testing pos() method
 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertEquals(0, reader.pos());
 }

 // Test case 3: Testing current() method
 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.advance();
 Assert.assertEquals('a', reader.current());
 }

 // Test case 4: Testing consume() method
 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("abcdef");
 char consumed = reader.consume();
 Assert.assertEquals('a', consumed);
 Assert.assertEquals(1, reader.pos());
 }

 // Test case 5: Testing unconsume() method
 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('a', reader.current());
 }

 // Test case 6: Testing advance() method
 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.advance();
 reader.advance();
 Assert.assertEquals('c', reader.current());
 }

 // Test case 7: Testing mark() and rewindToMark() methods
 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 // Test case 8: Testing consumeAsString() method
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeAsString();
 Assert.assertEquals("abc", consumed);
 }

 // Test case 9: Testing consumeTo() method with a single character
 @Test
 public void testConsumeToSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 String consumed = reader.consumeTo('d');
 Assert.assertEquals("bc", consumed);
 }

 // Test case 10: Testing consumeTo() method with a multi-character sequence
 @Test
 public void testConsumeToSequence() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 String consumed = reader.consumeTo("ab");
 Assert.assertEquals("cd", consumed);
 }

 // Test case 11: Testing consumeTo() method with an empty sequence
 @Test (expected = AssertionError.class)
 public void testConsumeToEmptySequence() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 reader.consumeTo("");
 }

 // Test case 12: Testing consumeToEnd() method
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("def", consumed);
 }

 // Test case 13: Testing consumeLetterSequence() method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijk");
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("abcdef", consumed);
 }

 // Test case 14: Testing consumeHexSequence() method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0x1a2b3c4d");
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("1a2b3c4d", consumed);
 }

 // Test case 15: Testing consumeDigitSequence() method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("1234567890", consumed);
 }

 // Test case 16: Testing matches() method with a single character
 @Test
 public void testMatchesSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertTrue(reader.matches('e'));
 }

 // Test case 17: Testing matches() method with a multi-character sequence
 @Test
 public void testMatchesSequence() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertTrue(reader.matches("ef"));
 }

 // Test case 18: Testing matchesIgnoreCase() method with a single character
 @Test
 public void testMatchesIgnoreCaseSingleCharacter() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 Assert.assertTrue(reader.matchesIgnoreCase("e"));
 }

 // Test case 19: Testing matchesIgnoreCase() method with a multi-character sequence
 @Test
 public void testMatchesIgnoreCaseSequence() {
 CharacterReader reader = new CharacterReader("ABCDEF");
 Assert.assertTrue(reader.matchesIgnoreCase("ef"));
 }

 // Test case 20: Testing matchesAny() method with a single character
 @Test
 public void testMatchesAnySingleCharacter() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertTrue(reader.matchesAny('e'));
 }

 // Test case 21: Testing matchesAny() method with a multi-character array
 @Test
 public void testMatchesAnyMultiCharacterArray() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertTrue(reader.matchesAny(new char[] {'e', 'f'}));
 }

 // Test case 22: Testing matchesLetter() method
 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertTrue(reader.matchesLetter());
 }

 // Test case 23: Testing matchesDigit() method
 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("1234567890");
 Assert.assertTrue(reader.matchesDigit());
 }

 // Test case 24: Testing matchConsume() method with a single character
 @Test
 public void testMatchConsumeSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.matchConsume('e');
 Assert.assertEquals(1, reader.pos());
 }

 // Test case 25: Testing matchConsume() method with a multi-character sequence
 @Test
 public void testMatchConsumeSequence() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.matchConsume("ef");
 Assert.assertEquals(3, reader.pos());
 }

 // Test case 26: Testing containsIgnoreCase() method with a single-character sequence
 @Test
 public void testContainsIgnoreCaseSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcdefghijklmABCDEFGHIJKLM");
 Assert.assertTrue(reader.containsIgnoreCase("a"));
 }

 // Test case 27: Testing containsIgnoreCase() method with a multi-character sequence
 @Test
 public void testContainsIgnoreCaseSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijklmABCDEFGHIJKLM");
 Assert.assertTrue(reader.containsIgnoreCase("ef"));
 }

 // Test case 28: Testing containsIgnoreCase() method with an empty string
 @Test
 public void testContainsIgnoreCaseEmptyString() {
 CharacterReader reader = new CharacterReader("abcdefghijklmABCDEFGHIJKLM");
 Assert.assertFalse(reader.containsIgnoreCase(""));
 }
}