package org.jsoup.parser.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testChars = new char[]{'a', 'b', 'c', 'd', 'e'};
 String testString = new String(testChars);
 reader = new CharacterReader(testString);
 }

 @After
 public void tearDown() {
 reader = null;
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.advance();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 reader.consume();
 assertEquals(1, reader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('a', reader.current());
 reader.consume();
 assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('a', reader.consume());
 assertEquals('b', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 assertEquals('a', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 reader.advance();
 assertEquals('c', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("", reader.consumeAsString());
 reader.consume();
 reader.consume();
 assertEquals("ab", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("", reader.consumeTo('b'));
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals("cd", reader.consumeTo('d'));
 }

 @Test
 public void testConsumeToAny() {
 reader.consume();
 reader.consume();
 reader.consume();
 String result = reader.consumeToAny('a', 'e');
 assertEquals("c", result);
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("d", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 String result = reader.consumeLetterSequence();
 assertEquals("a", result);
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 result = reader.consumeLetterSequence();
 assertEquals("e", result);
 }

 @Test
 public void testConsumeHexSequence() {
 String result = reader.consumeHexSequence();
 assertEquals("", result);
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 result = reader.consumeHexSequence();
 assertEquals("e", result);
 }

 @Test
 public void testConsumeDigitSequence() {
 String result = reader.consumeDigitSequence();
 assertEquals("", result);
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 result = reader.consumeDigitSequence();
 assertEquals("5", result);
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('a'));
 assertFalse(reader.matches('b'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("ab"));
 assertFalse(reader.matches("cd"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("Ab"));
 assertFalse(reader.matchesIgnoreCase("Cd"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny('a', 'e'));
 assertFalse(reader.matchesAny('f', 'e'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 reader.consume();
 assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesDigit());
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String sequence = "ab";
 reader.consume();
 reader.consume();
 assertTrue(reader.matchConsume(sequence));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String sequence = "Ab";
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 assertTrue(reader.matchConsumeIgnoreCase(sequence));
 }

 @Test
 public void testContainsIgnoreCase() {
 String sequence = "cd";
 assertTrue(reader.containsIgnoreCase(sequence));
 }
}