package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '\n'};
 String testInputStr = new String(testInput);
 reader = new CharacterReader(testInputStr);
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
 char consumed = reader.consume();
 assertEquals('h', consumed);
 assertEquals(1, reader.pos());
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
 assertEquals('h', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String consumed = reader.consumeAsString();
 assertEquals("he", consumed);
 }

 @Test
 public void testConsumeTo() {
 String consumed = reader.consumeTo('l');
 assertEquals("he", consumed);
 assertEquals(4, reader.pos());
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = reader.consumeToEnd();
 assertEquals("hello\n", consumed);
 }

 @Test
 public void testConsumeToAny() {
 char[] seq = {'o', 'r'};
 String consumed = reader.consumeToAny(seq);
 assertEquals("ello\n", consumed);
 }

 @Test
 public void testMatches() {
 char c = 'l';
 assertTrue(reader.matches(c));
 }

 @Test
 public void testMatchesString() {
 String seq = "ll";
 assertTrue(reader.matches(seq));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String seq = "HeLL";
 assertTrue(reader.matchesIgnoreCase(seq));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = {'o', 'r'};
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
 assertEquals(3, reader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String seq = "HeLL";
 assertTrue(reader.matchConsumeIgnoreCase(seq));
 assertEquals(3, reader.pos());
 }

 @Test
 public void testContainsIgnoreCase() {
 String seq = "world";
 assertTrue(reader.containsIgnoreCase(seq));
 }

 @Test
 public void testToString() {
 String expected = "ello\n";
 String actual = reader.toString();
 assertEquals(expected, actual);
 }

}