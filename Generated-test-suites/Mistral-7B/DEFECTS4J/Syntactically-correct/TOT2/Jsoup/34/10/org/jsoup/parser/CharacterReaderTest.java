package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 // Test for isEmpty() method
 @Test
 public void testIsEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 assertTrue(characterReader.isEmpty());
 }

 // Test for isEmpty() method with non-empty input
 @Test
 public void testIsNotEmpty() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertFalse(characterReader.isEmpty());
 }

 // Test for pos() method
 @Test
 public void testPos() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertEquals(0, characterReader.pos());
 }

 // Test for pos() method with consumed characters
 @Test
 public void testPosAfterConsumption() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 characterReader.consume();
 assertEquals(1, characterReader.pos());
 }

 // Test for current() method with empty input
 @Test
 public void testCurrentEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 assertEquals(CharacterReader.EOF, characterReader.current());
 }

 // Test for current() method with non-empty input
 @Test
 public void testCurrent() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertEquals('H', characterReader.current());
 }

 // Test for consume() method with empty input
 @Test(expected = java.lang.IllegalStateException.class)
 public void testConsumeEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 characterReader.consume();
 }

 // Test for consume() method with non-empty input
 @Test
 public void testConsume() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertEquals('H', characterReader.consume());
 assertEquals('H', characterReader.current());
 }

 // Test for unconsume() method with empty input
 @Test
 public void testUnconsumeEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 characterReader.unconsume();
 assertEquals(CharacterReader.EOF, characterReader.current());
 }

 // Test for unconsume() method with consumed character
 @Test
 public void testUnconsumeConsumed() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 characterReader.consume();
 characterReader.unconsume();
 assertEquals('H', characterReader.current());
 }

 // Test for advance() method with empty input
 @Test
 public void testAdvanceEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 characterReader.advance();
 assertEquals(CharacterReader.EOF, characterReader.current());
 }

 // Test for advance() method with non-empty input
 @Test
 public void testAdvance() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 characterReader.advance();
 assertEquals('H', characterReader.current());
 }

 // Test for mark() method with empty input
 @Test
 public void testMarkEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 characterReader.mark();
 assertEquals(0, characterReader.mark);
 }

 // Test for mark() method with non-empty input
 @Test
 public void testMark() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 characterReader.mark();
 assertEquals(0, characterReader.mark);
 characterReader.advance();
 characterReader.mark();
 assertEquals(1, characterReader.mark);
 }

 // Test for rewindToMark() method with empty input
 @Test
 public void testRewindToMarkEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 characterReader.mark();
 characterReader.rewindToMark();
 assertEquals(CharacterReader.EOF, characterReader.current());
 }

 // Test for rewindToMark() method with non-empty input
 @Test
 public void testRewindToMark() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 characterReader.mark();
 characterReader.advance();
 characterReader.rewindToMark();
 assertEquals('H', characterReader.current());
 }

 // Test for consumeAsString() method with empty input
 @Test
 public void testConsumeAsStringEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 assertEquals("", characterReader.consumeAsString());
 }

 // Test for consumeAsString() method with one character input
 @Test
 public void testConsumeAsStringOneChar() {
 CharacterReader characterReader = new CharacterReader("A");
 assertEquals("A", characterReader.consumeAsString());
 }

 // Test for consumeAsString() method with multiple characters input
 @Test
 public void testConsumeAsStringMultipleChars() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertEquals("Hello", characterReader.consumeAsString());
 }

 // Test for nextIndexOf() method with empty input and char target
 @Test
 public void testNextIndexOfEmptyAndChar() {
 CharacterReader characterReader = new CharacterReader("");
 assertEquals(-1, characterReader.nextIndexOf('A'));
 }

 // Test for nextIndexOf() method with empty input and CharSequence target
 @Test
 public void testNextIndexOfEmptyAndCharSequence() {
 CharacterReader characterReader = new CharacterReader("");
 assertEquals(-1, characterReader.nextIndexOf("ABC"));
 }

 // Test for nextIndexOf() method with non-empty input and char target
 @Test
 public void testNextIndexOfChar() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertEquals(4, characterReader.nextIndexOf('W'));
 }

 // Test for nextIndexOf() method with non-empty input and CharSequence target
 @Test
 public void testNextIndexOfCharSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertEquals(6, characterReader.nextIndexOf("World"));
 }

 // Test for consumeTo() method with empty input and char target
 @Test
 public void testConsumeToEmptyAndChar() {
 CharacterReader characterReader = new CharacterReader("");
 assertEquals("", characterReader.consumeTo('A'));
 }

 // Test for consumeTo() method with non-empty input and char target
 @Test
 public void testConsumeToChar() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertEquals("Hello", characterReader.consumeTo('W'));
 }

 // Test for consumeTo() method with non-empty input and CharSequence target
 @Test
 public void testConsumeToCharSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertEquals("Hello World!", characterReader.consumeTo("World!"));
 }

 // Test for consumeToAny() method with empty input and char array target
 @Test
 public void testConsumeToAnyEmptyAndCharArray() {
 CharacterReader characterReader = new CharacterReader("");
 assertEquals("", characterReader.consumeToAny(new char[] {'A'}));
 }

 // Test for consumeToAny() method with non-empty input and char array target
 @Test
 public void testConsumeToAnyCharArray() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertEquals("Hello", characterReader.consumeToAny(new char[] {'H', 'e'}));
 }

 // Test for consumeToEnd() method with empty input
 @Test
 public void testConsumeToEndEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 assertEquals("", characterReader.consumeToEnd());
 }

 // Test for consumeToEnd() method with non-empty input
 @Test
 public void testConsumeToEnd() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertEquals("World!", characterReader.consumeToEnd());
 }

 // Test for consumeLetterSequence() method with empty input
 @Test
 public void testConsumeLetterSequenceEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 assertEquals("", characterReader.consumeLetterSequence());
 }

 // Test for consumeLetterSequence() method with non-empty input
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertEquals("Hello", characterReader.consumeLetterSequence());
 }

 // Test for consumeLetterThenDigitSequence() method with empty input
 @Test
 public void testConsumeLetterThenDigitSequenceEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 assertEquals("", characterReader.consumeLetterThenDigitSequence());
 }

 // Test for consumeLetterThenDigitSequence() method with non-empty input
 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader characterReader = new CharacterReader("Hello123 World!");
 assertEquals("Hello123", characterReader.consumeLetterThenDigitSequence());
 }

 // Test for consumeHexSequence() method with empty input
 @Test
 public void testConsumeHexSequenceEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 assertEquals("", characterReader.consumeHexSequence());
 }

 // Test for consumeHexSequence() method with non-empty input
 @Test
 public void testConsumeHexSequence() {
 CharacterReader characterReader = new CharacterReader("0x1234ABCDEF");
 assertEquals("0x1234ABCDEF", characterReader.consumeHexSequence());
 }

 // Test for consumeDigitSequence() method with empty input
 @Test
 public void testConsumeDigitSequenceEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 assertEquals("", characterReader.consumeDigitSequence());
 }

 // Test for consumeDigitSequence() method with non-empty input
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader characterReader = new CharacterReader("123456");
 assertEquals("123456", characterReader.consumeDigitSequence());
 }

 // Test for matches() method with empty input and char target
 @Test
 public void testMatchesEmptyAndChar() {
 CharacterReader characterReader = new CharacterReader("");
 assertFalse(characterReader.matches('A'));
 }

 // Test for matches() method with empty input and CharSequence target
 @Test
 public void testMatchesEmptyAndCharSequence() {
 CharacterReader characterReader = new CharacterReader("");
 assertFalse(characterReader.matches("ABC"));
 }

 // Test for matches() method with non-empty input and char target
 @Test
 public void testMatchesChar() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertTrue(characterReader.matches('W'));
 }

 // Test for matches() method with non-empty input and CharSequence target
 @Test
 public void testMatchesCharSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertTrue(characterReader.matches("World"));
 }

 // Test for matchesIgnoreCase() method with empty input and CharSequence target
 @Test
 public void testMatchesIgnoreCaseEmptyAndCharSequence() {
 CharacterReader characterReader = new CharacterReader("");
 assertFalse(characterReader.matchesIgnoreCase("ABC"));
 }

 // Test for matchesIgnoreCase() method with non-empty input and CharSequence target
 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertTrue(characterReader.matchesIgnoreCase("world"));
 }

 // Test for matchesAny() method with empty input and char array target
 @Test
 public void testMatchesAnyEmptyAndCharArray() {
 CharacterReader characterReader = new CharacterReader("");
 assertFalse(characterReader.matchesAny(new char[] {'A'}));
 }

 // Test for matchesAny() method with non-empty input and char array target
 @Test
 public void testMatchesAnyCharArray() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertTrue(characterReader.matchesAny(new char[] {'H', 'W'}));
 }

 // Test for matchesLetter() method with empty input
 @Test
 public void testMatchesLetterEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 assertFalse(characterReader.matchesLetter());
 }

 // Test for matchesLetter() method with non-empty input
 @Test
 public void testMatchesLetter() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertTrue(characterReader.matchesLetter());
 }

 // Test for matchesDigit() method with empty input
 @Test
 public void testMatchesDigitEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 assertFalse(characterReader.matchesDigit());
 }

 // Test for matchesDigit() method with non-empty input
 @Test
 public void testMatchesDigit() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertTrue(characterReader.matchesDigit());
 }

 // Test for matchConsume() method with empty input and CharSequence target
 @Test
 public void testMatchConsumeEmptyAndCharSequence() {
 CharacterReader characterReader = new CharacterReader("");
 assertFalse(characterReader.matchConsume("ABC"));
 }

 // Test for matchConsume() method with non-empty input and CharSequence target
 @Test
 public void testMatchConsume() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertTrue(characterReader.matchConsume("World"));
 }

 // Test for matchConsumeIgnoreCase() method with empty input and CharSequence target
 @Test
 public void testMatchConsumeIgnoreCaseEmptyAndCharSequence() {
 CharacterReader characterReader = new CharacterReader("");
 assertFalse(characterReader.matchConsumeIgnoreCase("ABC"));
 }

 // Test for matchConsumeIgnoreCase() method with non-empty input and CharSequence target
 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertTrue(characterReader.matchConsumeIgnoreCase("world"));
 }

 // Test for containsIgnoreCase() method with empty input and CharSequence target
 @Test
 public void testContainsIgnoreCaseEmptyAndCharSequence() {
 CharacterReader characterReader = new CharacterReader("");
 assertFalse(characterReader.containsIgnoreCase("ABC"));
 }

 // Test for containsIgnoreCase() method with non-empty input and CharSequence target
 @Test
 public void testContainsIgnoreCase() {
 CharacterReader characterReader = new CharacterReader("Hello World!");
 assertTrue(characterReader.containsIgnoreCase("World"));
 }
}