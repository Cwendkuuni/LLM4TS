package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the CharacterReader class
 */
public class CharacterReaderTest {

 // Test for empty CharacterReader
 CharacterReader emptyCharacterReader = new CharacterReader("");

 // Test for CharacterReader with data
 CharacterReader characterReader = new CharacterReader("Hello World");

 @Test
 public void testIsEmpty() {
 // Test empty CharacterReader
 assertTrue(emptyCharacterReader.isEmpty());

 // Test CharacterReader with data
 assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 // Test initial position
 assertEquals(0, characterReader.pos());

 // Test after consumption
 characterReader.consume();
 assertEquals(1, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 // Test empty CharacterReader
 assertEquals(CharacterReader.EOF, emptyCharacterReader.current());

 // Test CharacterReader with data
 assertEquals('H', characterReader.current());
 }

 @Test
 public void testConsume() {
 // Test empty CharacterReader
 assertEquals(CharacterReader.EOF, emptyCharacterReader.consume());

 // Test CharacterReader with data
 assertEquals('H', characterReader.consume());
 assertEquals('e', characterReader.current());
 }

 @Test
 public void testUnconsume() {
 // Test CharacterReader with data
 characterReader.consume();
 characterReader.unconsume();
 assertEquals('H', characterReader.current());
 }

 @Test
 public void testAdvance() {
 // Test empty CharacterReader
 characterReader.advance();
 assertEquals(0, characterReader.pos());

 // Test CharacterReader with data
 characterReader.consume();
 characterReader.advance();
 assertEquals('e', characterReader.current());
 }

 @Test
 public void testMark() {
 // Test marking position
 characterReader.mark();
 characterReader.consume();

 // Test rewinding to mark
 characterReader.rewindToMark();
 assertEquals('H', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 // Test empty CharacterReader
 assertEquals("", emptyCharacterReader.consumeAsString());

 // Test CharacterReader with data
 assertEquals("H", characterReader.consumeAsString());
 }

 @Test
 public void testNextIndexOf() {
 // Test next index of single character
 assertEquals(1, characterReader.nextIndexOf('l'));

 // Test next index of multiple occurrences
 assertEquals(2, characterReader.nextIndexOf('l', 1));

 // Test next index of empty sequence
 assertEquals(-1, characterReader.nextIndexOf(""));

 // Test next index of null character
 assertEquals(0, characterReader.nextIndexOf(CharacterReader.nullChar));
 }

 @Test
 public void testNextIndexOfString() {
 // Test next index of empty sequence
 assertEquals(-1, characterReader.nextIndexOf(""));

 // Test next index of sequence in CharacterReader with data
 assertEquals(7, characterReader.nextIndexOf("ld"));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testNextIndexOfOutOfBounds() {
 // Test next index of character not in sequence
 characterReader.nextIndexOf('z');
 }

 @Test
 public void testConsumeTo() {
 // Test consumeTo with single character
 assertEquals("H", characterReader.consumeTo('l'));

 // Test consumeTo with sequence
 assertEquals("Hello", characterReader.consumeTo("llo"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeToEmptySequence() {
 // Test consumeTo with empty sequence
 characterReader.consumeTo("");
 }

 @Test
 public void testConsumeToAny() {
 // Test consumeToAny with single character
 assertEquals("Hello", characterReader.consumeToAny('l'));

 // Test consumeToAny with multiple characters
 assertEquals("HelloWorld", characterReader.consumeToAny(new char[]{'l', 'r'}));
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToAnyUnexpectedCharacter() {
 // Test consumeToAny with unexpected character
 characterReader.consumeToAny(new char[]{'z'});
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToAnySorted() {
 // Test consumeToAnySorted with unexpected character
 characterReader.consumeToAnySorted(new char[]{'z'});
 }

 @Test(expected = AssertionError.class)
 public void testConsumeData() {
 // Test consumeData with character not in sequence
 characterReader.consumeData();
 }

 @Test
 public void testConsumeTagName() {
 // Test consumeTagName with tag name
 assertEquals("Hello", characterReader.consumeTagName());
 }

 @Test(expected = AssertionError.class)
 public void testConsumeTagNameUnexpectedCharacter() {
 // Test consumeTagName with unexpected character
 characterReader.consumeTagName();
 }

 @Test
 public void testConsumeToEnd() {
 // Test consumeToEnd
 assertEquals("Hello World", characterReader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 // Test consumeLetterSequence
 assertEquals("Hello", characterReader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 // Test consumeLetterThenDigitSequence
 assertEquals("12345", characterReader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testMatches() {
 // Test matches single character
 assertTrue(characterReader.matches('l'));

 // Test matches sequence
 assertTrue(characterReader.matches("llo"));
 }

 @Test(expected = AssertionError.class)
 public void testMatchesUnexpectedCharacter() {
 // Test matches with unexpected character
 characterReader.matches('z');
 }

 @Test
 public void testMatchesIgnoreCase() {
 // Test matchesIgnoreCase with single character
 assertTrue(characterReader.matchesIgnoreCase("L"));

 // Test matchesIgnoreCase with sequence
 assertTrue(characterReader.matchesIgnoreCase("Hello"));
 }

 @Test(expected = AssertionError.class)
 public void testMatchesIgnoreCaseUnexpectedCharacter() {
 // Test matchesIgnoreCase with unexpected character
 characterReader.matchesIgnoreCase("Z");
 }

 @Test
 public void testMatchesAny() {
 // Test matchesAny with single character
 assertTrue(characterReader.matchesAny('l'));

 // Test matchesAny with multiple characters
 assertTrue(characterReader.matchesAny(new char[]{'l', 'r'}));
 }

 @Test(expected = AssertionError.class)
 public void testMatchesAnyUnexpectedCharacter() {
 // Test matchesAny with unexpected character
 characterReader.matchesAny('z');
 }

 @Test
 public void testMatchesAnySorted() {
 // Test matchesAnySorted with unexpected character
 assertTrue(characterReader.matchesAnySorted(new char[]{'z'}));
 }

 @Test
 public void testMatchesLetter() {
 // Test matchesLetter with letter
 assertTrue(characterReader.matchesLetter());

 // Test matchesLetter with non-letter
 assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchesDigit() {
 // Test matchesDigit with digit
 assertTrue(characterReader.matchesDigit());

 // Test matchesDigit with non-digit
 assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchConsume() {
 // Test matchConsume with single character
 assertEquals("Hello", characterReader.consume(2));

 // Test matchConsume with sequence
 assertEquals("World", characterReader.consume(7, "ld"));
 }

 @Test(expected = AssertionError.class)
 public void testMatchConsumeUnexpectedCharacter() {
 // Test matchConsume with unexpected character
 characterReader.matchConsume("z");
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 // Test matchConsumeIgnoreCase with single character
 assertEquals("Hello", characterReader.consumeIgnoreCase(2));

 // Test matchConsumeIgnoreCase with sequence
 assertEquals("World", characterReader.consumeIgnoreCase(7, "ld"));
 }

 @Test(expected = AssertionError.class)
 public void testMatchConsumeIgnoreCaseUnexpectedCharacter() {
 // Test matchConsumeIgnoreCase with unexpected character
 characterReader.consumeIgnoreCase("z");
 }

 @Test
 public void testContainsIgnoreCase() {
 // Test containsIgnoreCase with empty sequence
 assertFalse(characterReader.containsIgnoreCase(""));

 // Test containsIgnoreCase with sequence in CharacterReader with data
 assertTrue(characterReader.containsIgnoreCase("/title"));
 }

 @Test
 public void testToString() {
 // Test toString with empty CharacterReader
 assertEquals("", emptyCharacterReader.toString());

 // Test toString with CharacterReader with data
 assertEquals("Hello World", characterReader.toString());
 }
}