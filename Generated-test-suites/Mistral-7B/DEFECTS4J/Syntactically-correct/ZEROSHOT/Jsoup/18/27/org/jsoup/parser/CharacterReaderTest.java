package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 CharacterReader characterReader;

 @Before
 public void setup() {
 char[] input = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '\0'};
 characterReader = new CharacterReader(new String(input));
 }

 @Test
 public void testEmpty() {
 assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('h', characterReader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('h', characterReader.consume());
 assertEquals('e', characterReader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeAfterEnd() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume(); // consume past the end
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.unconsume();
 assertEquals('h', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 assertEquals('e', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 assertEquals('h', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("ello", characterReader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("ello", characterReader.consumeTo(' '));
 assertEquals("world", characterReader.consumeTo('d'));
 assertEquals("world", characterReader.consumeToEnd());
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("ello", characterReader.consumeToAny(' ', 'l'));
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("ello", characterReader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 characterReader.advance();
 characterReader.advance();
 assertEquals("ll", characterReader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("0ld", characterReader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('l'));
 assertFalse(characterReader.matches('.'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(characterReader.matches("ello"));
 assertFalse(characterReader.matches("world"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(characterReader.matchesIgnoreCase("ello"));
 assertFalse(characterReader.matchesIgnoreCase("world"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(characterReader.matchesAny('l', 'o'));
 assertFalse(characterReader.matchesAny('x', 'o'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesLetter('5'));
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(characterReader.matchesDigit());
 assertTrue(characterReader.matchesDigit('0'));
 }

 @Test
 public void testMatchConsume() {
 assertEquals("ello", characterReader.matchConsume("ello"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("ello", characterReader.matchConsumeIgnoreCase("ello"));
 assertFalse(characterReader.matchConsumeIgnoreCase("world"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(characterReader.containsIgnoreCase("ello"));
 assertFalse(characterReader.containsIgnoreCase("world"));
 }
}