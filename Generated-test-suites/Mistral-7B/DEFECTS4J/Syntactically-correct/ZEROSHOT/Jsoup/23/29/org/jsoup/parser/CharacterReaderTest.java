package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 CharacterReader characterReader;

 @Before
 public void setup() {
 char[] input = new char[]{'a', 'b', 'c', 'd', 'e', 'f'};
 characterReader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 assertFalse(characterReader.isEmpty());
 characterReader.consume();
 characterReader.consume();
 assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, characterReader.pos());
 characterReader.consume();
 characterReader.consume();
 assertEquals(2, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('a', characterReader.current());
 characterReader.consume();
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('a', characterReader.consume());
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.consume();
 characterReader.unconsume();
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 characterReader.consume();
 assertEquals(1, characterReader.pos() - characterReader.mark());
 characterReader.rewindToMark();
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("ab", characterReader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("bc", characterReader.consumeTo('c'));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("def", characterReader.consumeToEnd());
 }

 @Test
 public void testConsumeToAny() {
 characterReader.advance();
 characterReader.advance();
 assertEquals("cd", characterReader.consumeToAny('d', 'e'));
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("abcde", characterReader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 characterReader.advance();
 characterReader.advance();
 assertEquals("12345f", characterReader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("12345", characterReader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('b'));
 assertFalse(characterReader.matches('z'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(characterReader.matchesIgnoreCase("b"));
 assertFalse(characterReader.matchesIgnoreCase("Z"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(characterReader.matchesAny('b', 'c'));
 assertFalse(characterReader.matchesAny('z'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesLetter('5'));
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(characterReader.matchesDigit());
 assertFalse(characterReader.matchesDigit('a'));
 }

 @Test
 public void testMatchConsume() {
 assertEquals("bc", characterReader.matchConsume("bc"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertEquals("bc", characterReader.matchConsumeIgnoreCase("Bc"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(characterReader.containsIgnoreCase("bc"));
 assertFalse(characterReader.containsIgnoreCase("AB"));
 }

 @Test(expected = AssertionError.class)
 public void testConsumePastEnd() {
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume(); // should throw an exception
 }

}