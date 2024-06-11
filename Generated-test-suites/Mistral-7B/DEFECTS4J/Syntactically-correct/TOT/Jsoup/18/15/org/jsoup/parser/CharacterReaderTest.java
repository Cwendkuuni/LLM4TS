package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '\n'};
 reader = new CharacterReader(new String(testInput));
 }

 @After
 public void tearDown() {
 reader = null;
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('h', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('h', reader.consume());
 assertEquals('e', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.unconsume();
 assertEquals('h', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String expected = "llo";
 assertEquals(expected, reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 String expected = "ld";
 assertEquals(expected, reader.consumeTo('l'));
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "world\n";
 assertEquals(expected, reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "ello";
 assertEquals(expected, reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "206f6c6c6f"; // hex for "world"
 assertEquals(expected, reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "1151110108"; // digit for "world"
 assertEquals(expected, reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('l'));
 assertFalse(reader.matches('o'));
 }

 @Test
 public void testMatchesString() {
 String seq = "ll";
 assertTrue(reader.matches(seq));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String seq = "world";
 assertTrue(reader.matchesIgnoreCase(seq));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = {'l', 'o'};
 assertTrue(reader.matchesAny(seq));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String seq = "ll";
 assertTrue(reader.matchConsume(seq));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String seq = "world";
 assertTrue(reader.matchConsumeIgnoreCase(seq));
 }

 @Test
 public void testContainsIgnoreCase() {
 String seq = "</title>";
 assertTrue(reader.containsIgnoreCase(seq));
 }

 @Test
 public void testToString() {
 String expected = " world";
 assertEquals(expected, reader.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 CharacterReader reader = null; // should throw IllegalArgumentException
 }
}