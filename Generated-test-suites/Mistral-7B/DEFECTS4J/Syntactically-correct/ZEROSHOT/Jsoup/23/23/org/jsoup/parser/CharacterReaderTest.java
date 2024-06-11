package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.parser.CharacterReader;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'t', 'e', 's', 't', '\n', 's', 't', 'r', 'i', 'n', 'g', ' ', 'd', 'a', 't', 'a', ':', '\n', 'h', 'e', 'l', 'l', 'o', '\n'};
 reader = new CharacterReader(new String(testInput));
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
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('t', reader.consume());
 assertEquals('s', reader.current());
 }

 @Test
 public void testUnconsume() {
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
 }

 @Test(expected = IllegalStateException.class)
 public void testRewindToMarkWithNoMark() {
 reader.rewindToMark();
 }

 @Test
 public void testConsumeAsString() {
 String consumed = reader.consumeAsString();
 assertEquals("st", consumed);
 }

 @Test
 public void testConsumeTo() {
 String consumed = reader.consumeTo(':');
 assertEquals("str", consumed);
 }

 @Test
 public void testConsumeToString() {
 String consumed = reader.consumeTo(":");
 assertEquals("str", consumed);
 }

 @Test
 public void testConsumeToAny() {
 char[] seq = {'d', 'a'};
 String consumed = reader.consumeToAny(seq);
 assertEquals("at", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = reader.consumeToEnd();
 assertEquals("string", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = reader.consumeLetterSequence();
 assertEquals("testing", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = reader.consumeHexSequence();
 assertEquals("dat:", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = reader.consumeDigitSequence();
 assertEquals("123", consumed);
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('t'));
 assertFalse(reader.matches(' '));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches("st"));
 assertFalse(reader.matches("tr"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(reader.matchesIgnoreCase("str"));
 assertFalse(reader.matchesIgnoreCase("TRING"));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = {'t', 's'};
 assertTrue(reader.matchesAny(seq));
 assertFalse(reader.matchesAny(new char[]{'d'}));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('5'));
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testMatchConsume() {
 String consumed = reader.matchConsume(":");
 assertEquals(": ", consumed);
 assertEquals('h', reader.current());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String consumed = reader.matchConsumeIgnoreCase(":");
 assertEquals(": ", consumed);
 assertEquals('h', reader.current());
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase(":"));
 assertTrue(reader.containsIgnoreCase("HELLO"));
 }

 @Test
 public void testToString() {
 assertEquals("string", reader.toString());
 }

}