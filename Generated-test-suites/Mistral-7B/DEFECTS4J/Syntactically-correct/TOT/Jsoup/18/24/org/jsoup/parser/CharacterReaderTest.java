package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 char[] testInput = {'t', 'e', 's', 't', ' ', 'i', 'n', 'p', 'u', 't', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'R', 'e', 'a', 'd', 'e', 'r'};
 reader = new CharacterReader(new String(testInput));
 }

 @After
 public void teardown() {
 reader = null;
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 reader.consume();
 reader.consume();
 assertEquals(2, reader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('t', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsume() {
 assertTrue(reader.matches('t'));
 reader.consume();
 assertFalse(reader.matches('t'));
 assertEquals('e', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.unconsume();
 assertEquals('t', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('s', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("te", reader.consumeAsString());
 reader.consume();
 reader.consume();
 assertEquals("in", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("Cha", reader.consumeTo('a'));
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals("Rea", reader.consumeTo('a'));
 }

 @Test
 public void testConsumeToAny() {
 reader.consume();
 reader.consume();
 assertEquals("rd", reader.consumeToAny('d', 'e'));
 reader.consume();
 reader.consume();
 assertEquals("der", reader.consumeToAny('d', 'e'));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("CharacterReader", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("test", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("Charact", reader.consumeHexSequence());
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals("erReader", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('t'));
 assertFalse(reader.matches('a'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("te"));
 assertFalse(reader.matches("inp"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("te"));
 assertTrue(reader.matchesIgnoreCase("INP"));
 assertFalse(reader.matchesIgnoreCase("Cha"));
 }

 @Test
 public void testMatchesAny() {
 assertFalse(reader.matchesAny('z'));
 assertTrue(reader.matchesAny('t', 's'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('0'));
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesDigit());
 assertTrue(reader.matchesDigit('3'));
 }

 @Test
 public void testMatchConsume() {
 assertEquals("te", reader.consumeAsString());
 assertTrue(reader.matchConsume("te"));
 assertEquals("in", reader.consumeAsString());
 assertTrue(reader.matchConsume("in"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("te", reader.consumeAsString());
 assertTrue(reader.matchConsumeIgnoreCase("te"));
 assertEquals("in", reader.consumeAsString());
 assertTrue(reader.matchConsumeIgnoreCase("in"));
 assertTrue(reader.matchConsumeIgnoreCase("In"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("test"));
 assertTrue(reader.containsIgnoreCase("Test"));
 assertFalse(reader.containsIgnoreCase("CharacterReader"));
 }

}