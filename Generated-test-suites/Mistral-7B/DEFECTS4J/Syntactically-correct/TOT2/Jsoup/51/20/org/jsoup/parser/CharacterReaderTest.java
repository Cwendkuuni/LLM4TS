package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 // Test for constructor
 @Test
 public void testCharacterReaderConstruction() {
 CharacterReader charReader = new CharacterReader("Test String");
 assertEquals("Test String", charReader.input);
 assertEquals(14, charReader.length);
 }

 // Test for isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader charReader = new CharacterReader("Test String");
 assertFalse(charReader.isEmpty());
 charReader.consumeToEnd();
 assertTrue(charReader.isEmpty());
 }

 // Test for pos method
 @Test
 public void testPos() {
 CharacterReader charReader = new CharacterReader("Test String");
 assertEquals(0, charReader.pos());
 charReader.consume();
 assertEquals(1, charReader.pos());
 }

 // Test for current method
 @Test
 public void testCurrent() {
 CharacterReader charReader = new CharacterReader("Test String");
 assertEquals('T', charReader.current());
 charReader.consume();
 assertEquals('e', charReader.current());
 }

 // Test for consume method
 @Test
 public void testConsume() {
 CharacterReader charReader = new CharacterReader("Test String");
 char c = charReader.current();
 charReader.consume();
 assertEquals(c, charReader.current());
 charReader.consume();
 assertNotEquals(c, charReader.current());
 }

 // Test for unconsume method
 @Test
 public void testUnconsume() {
 CharacterReader charReader = new CharacterReader("Test String");
 char c = charReader.current();
 charReader.consume();
 charReader.unconsume();
 assertEquals(c, charReader.current());
 }

 // Test for advance method
 @Test
 public void testAdvance() {
 CharacterReader charReader = new CharacterReader("Test String");
 char c = charReader.current();
 charReader.advance();
 assertNotEquals(c, charReader.current());
 }

 // Test for mark method
 @Test
 public void testMark() {
 CharacterReader charReader = new CharacterReader("Test String");
 charReader.mark();
 charReader.consume();
 assertNotEquals(charReader.mark(), charReader.pos());
 }

 // Test for rewindToMark method
 @Test
 public void testRewindToMark() {
 CharacterReader charReader = new CharacterReader("Test String");
 charReader.mark();
 charReader.consume();
 charReader.rewindToMark();
 assertEquals(charReader.mark(), charReader.pos());
 }

 // Test for consumeAsString method
 @Test
 public void testConsumeAsString() {
 CharacterReader charReader = new CharacterReader("Test String");
 assertEquals("Test", charReader.consumeAsString());
 }

 // Test for nextIndexOf method with single character
 @Test
 public void testNextIndexOfSingleCharacter() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertEquals(3, charReader.nextIndexOf('s'));
 }

 // Test for nextIndexOf method with multiple occurrences
 @Test
 public void testNextIndexOfMultipleOccurrences() {
 CharacterReader charReader = new CharacterReader("TestTest");
 assertEquals(2, charReader.nextIndexOf('t'));
 }

 // Test for nextIndexOf method with no occurrences
 @Test
 public void testNextIndexOfNoOccurrences() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertEquals(-1, charReader.nextIndexOf('x'));
 }

 // Test for consumeTo method with single character
 @Test
 public void testConsumeToSingleCharacter() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertEquals("est", charReader.consumeTo('s'));
 }

 // Test for consumeTo method with multiple characters
 @Test
 public void testConsumeToMultipleCharacters() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertEquals("estString", charReader.consumeTo("String"));
 }

 // Test for consumeTo method with no occurrences
 @Test
 public void testConsumeToNoOccurrences() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertEquals("TestString", charReader.consumeToEnd());
 }

 // Test for consumeToAny method
 @Test
 public void testConsumeToAny() {
 CharacterReader charReader = new CharacterReader("TestString123");
 assertEquals("123", charReader.consumeToAny('1', '2', '3'));
 }

 // Test for consumeToAnySorted method
 @Test
 public void testConsumeToAnySorted() {
 CharacterReader charReader = new CharacterReader("TestString123");
 assertEquals("12", charReader.consumeToAnySorted('1', '2'));
 }

 // Test for consumeData method
 @Test
 public void testConsumeData() {
 CharacterReader charReader = new CharacterReader("Test&<null");
 assertEquals("Test", charReader.consumeData());
 }

 // Test for consumeTagName method
 @Test
 public void testConsumeTagName() {
 CharacterReader charReader = new CharacterReader("<title>Test</title>");
 assertEquals("title", charReader.consumeTagName());
 }

 // Test for consumeToEnd method
 @Test
 public void testConsumeToEnd() {
 CharacterReader charReader = new CharacterReader("<title>Test</title>");
 assertEquals("<title>Test</title>", charReader.consumeToEnd());
 }

 // Test for consumeLetterSequence method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader charReader = new CharacterReader("Test123");
 assertEquals("Test", charReader.consumeLetterSequence());
 }

 // Test for consumeLetterThenDigitSequence method
 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader charReader = new CharacterReader("Test123");
 assertEquals("Test123", charReader.consumeLetterThenDigitSequence());
 }

 // Test for consumeHexSequence method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader charReader = new CharacterReader("Test#fF");
 assertEquals("Test#fF", charReader.consumeHexSequence());
 }

 // Test for consumeDigitSequence method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader charReader = new CharacterReader("Test123");
 assertEquals("123", charReader.consumeDigitSequence());
 }

 // Test for matches method
 @Test
 public void testMatches() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertTrue(charReader.matches('t'));
 assertTrue(charReader.matches("ing"));
 }

 // Test for matches method with no match
 @Test
 public void testMatchesNoMatch() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertFalse(charReader.matches('x'));
 }

 // Test for matches method with empty sequence
 @Test
 public void testMatchesEmptySequence() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertTrue(charReader.matches(""));
 }

 // Test for matchesIgnoreCase method
 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertTrue(charReader.matchesIgnoreCase("t"));
 assertTrue(charReader.matchesIgnoreCase("ing"));
 assertTrue(charReader.matchesIgnoreCase("STRING"));
 }

 // Test for matchesIgnoreCase method with no match
 @Test
 public void testMatchesIgnoreCaseNoMatch() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertFalse(charReader.matchesIgnoreCase("x"));
 }

 // Test for matchesIgnoreCase method with empty sequence
 @Test
 public void testMatchesIgnoreCaseEmptySequence() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertTrue(charReader.matchesIgnoreCase(""));
 }

 // Test for matchesAny method
 @Test
 public void testMatchesAny() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertTrue(charReader.matchesAny('t', 's', 'e'));
 }

 // Test for matchesAny method with no match
 @Test
 public void testMatchesAnyNoMatch() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertFalse(charReader.matchesAny('x', 's', 'e'));
 }

 // Test for matchesAnySorted method
 @Test
 public void testMatchesAnySorted() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertTrue(charReader.matchesAnySorted('s', 't'));
 }

 // Test for matchesLetter method
 @Test
 public void testMatchesLetter() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertTrue(charReader.matchesLetter());
 }

 // Test for matchesLetter method with no match
 @Test
 public void testMatchesLetterNoMatch() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertFalse(charReader.matchesLetter());
 }

 // Test for matchConsume method
 @Test
 public void testMatchConsume() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertTrue(charReader.matchConsume("ing"));
 }

 // Test for matchConsume method with no match
 @Test
 public void testMatchConsumeNoMatch() {
 CharacterReader charReader = new CharacterReader("TestString");
 assertFalse(charReader.matchConsume("x"));
 }

 // Test for containsIgnoreCase method
 @Test
 public void testContainsIgnoreCase() {
 CharacterReader charReader = new CharacterReader("<html><title>Test</title></html>");
 assertTrue(charReader.containsIgnoreCase("/title"));
 }

 // Test for containsIgnoreCase method with no match
 @Test
 public void testContainsIgnoreCaseNoMatch() {
 CharacterReader charReader = new CharacterReader("<html><title>Test</title></html>");
 assertFalse(charReader.containsIgnoreCase("/body"));
 }
}