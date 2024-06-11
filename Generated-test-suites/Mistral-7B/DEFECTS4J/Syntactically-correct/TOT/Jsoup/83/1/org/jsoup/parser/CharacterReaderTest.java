package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 String testString = "This is a test string for CharacterReader. It contains various characters and sequences.";
 reader = new CharacterReader(new StringReader(testString));
 }

 @Test
 public void testConstructor() {
 assertNotNull(reader);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.advance();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrentChar() {
 assertEquals('T', reader.current());
 reader.consume();
 assertEquals('h', reader.current());
 }

 @Test(expected = UncheckedIOException.class)
 public void testIOException() throws IOException {
 Reader invalidInput = new StringReader("Invalid Input");
 CharacterReader reader = new CharacterReader(invalidInput);
 reader.bufferUp(); // simulate bufferUp call to trigger IOException
 }

 @Test
 public void testConsume() {
 assertEquals('T', reader.consume());
 reader.advance();
 assertEquals('h', reader.consume());
 }

 @Test
 public void testUnconsume() {
 reader.advance();
 reader.consume();
 reader.unconsume();
 assertEquals('h', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('h', reader.current());
 }

 @Test
 public void testMark() {
 reader.advance();
 reader.advance();
 reader.mark();
 reader.rewindToMark();
 assertEquals('h', reader.current());
 }

 @Test
 public void testNextIndexOf() {
 assertEquals(2, reader.nextIndexOf('h'));
 assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testNextIndexOfWithSequence() {
 String sequence = "test";
 assertEquals(6, reader.nextIndexOf(sequence));
 assertEquals(-1, reader.nextIndexOf("invalidSequence"));
 }

 @Test
 public void testConsumeTo() {
 String expected = "is a test string for CharacterReader.";
 assertEquals(expected, reader.consumeTo(' '));
 }

 @Test
 public void testConsumeToWithSequence() {
 String expected = "CharacterReader.";
 assertEquals(expected, reader.consumeTo(sequence));
 }

 @Test
 public void testConsumeToAny() {
 String expected = "test string for CharacterReader.";
 assertEquals(expected, reader.consumeToAny(new char[]{'t', 's', '.', ' '}));
 }

 @Test
 public void testConsumeToAnySorted() {
 String expected = "CharacterReader.";
 assertEquals(expected, reader.consumeToAnySorted(new char[]{'t', '.', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', '.', '.', 'R', 'e', 'a', 'd', 'e', 'r'}));
 }

 @Test
 public void testConsumeData() {
 String expected = "This is a test string for CharacterReader. It contains various characters and sequences.";
 assertEquals(expected, reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 String expected = "CharacterReader";
 assertEquals(expected, reader.consumeTagName());
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "This is a test string for CharacterReader. It contains various characters and sequences.";
 assertEquals(expected, reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "is a test string for CharacterReader.";
 assertEquals(expected, reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 String expected = "test string for CharacterReader.";
 assertEquals(expected, reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "test string for CharacterReader.";
 assertEquals(expected, reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "test string for CharacterReader.";
 assertEquals(expected, reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('T'));
 assertFalse(reader.matches('Z'));
 }

 @Test
 public void testMatchesWithSequence() {
 assertTrue(reader.matches("is a"));
 assertFalse(reader.matches("is not a"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("is a"));
 assertFalse(reader.matchesIgnoreCase("is not a"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny('t'));
 assertFalse(reader.matchesAny('z'));
 }

 @Test
 public void testMatchesAnySorted() {
 assertTrue(reader.matchesAnySorted(new char[]{'t', 's', '.', ' '}));
 assertFalse(reader.matchesAnySorted(new char[]{'z', 'a'}));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('9'));
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testMatchConsume() {
 String expected = "is a test string for CharacterReader.";
 reader.mark();
 assertEquals(expected, reader.matchConsume("is a"));
 assertEquals(expected.length() - 5, reader.pos());
 reader.rewindToMark();
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expected = "is a test string for CharacterReader.";
 reader.mark();
 assertEquals(expected, reader.matchConsumeIgnoreCase("Is A"));
 assertEquals(expected.length() - 5, reader.pos());
 reader.rewindToMark();
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("test"));
 assertFalse(reader.containsIgnoreCase("invalidTest"));
 }

 @Test
 public void testToString() {
 assertEquals("This is a test string for CharacterReader. It contains various characters and sequences.", reader.toString());
 }
}