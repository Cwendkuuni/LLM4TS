package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] testInput = {'a', 'b', 'c', 'd', 'e', 'f'};
 characterReader = new CharacterReader(new String(testInput));
 }

 @Test
 public void testCharacterReaderConstructor() {
 assertNotNull(characterReader);
 assertEquals(6, characterReader.length());
 }

 @Test
 public void testPos() {
 assertEquals(0, characterReader.pos());
 }

 @Test
 public void testIsEmpty() {
 assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testConsume() {
 assertEquals('a', characterReader.consume());
 assertEquals(1, characterReader.pos());
 }

 @Test
 public void testUnconsume() {
 characterReader.unconsume();
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testMark() {
 characterReader.mark();
 assertEquals(1, characterReader.pos());
 characterReader.advance();
 characterReader.rewindToMark();
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 String consumed = characterReader.consumeAsString();
 assertEquals("ab", consumed);
 assertEquals(2, characterReader.pos());
 }

 @Test
 public void testConsumeTo() {
 String consumed = characterReader.consumeTo('c');
 assertEquals("ab", consumed);
 assertEquals(2, characterReader.pos());
 }

 @Test
 public void testConsumeToEnd() {
 String consumed = characterReader.consumeToEnd();
 assertEquals("def", consumed);
 assertEquals(6, characterReader.pos());
 }

 @Test
 public void testConsumeLetterSequence() {
 String consumed = characterReader.consumeLetterSequence();
 assertEquals("abcde", consumed);
 assertEquals(6, characterReader.pos());
 }

 @Test
 public void testConsumeHexSequence() {
 String consumed = characterReader.consumeHexSequence();
 assertEquals("f", consumed);
 assertEquals(6, characterReader.pos());
 }

 @Test
 public void testConsumeDigitSequence() {
 String consumed = characterReader.consumeDigitSequence();
 assertEquals("12345", consumed);
 assertEquals(5, characterReader.pos());
 }

 @Test
 public void testMatches() {
 assertTrue(characterReader.matches('b'));
 assertFalse(characterReader.matches('g'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(characterReader.matches("ab"));
 assertFalse(characterReader.matches("cd"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 assertTrue(characterReader.matchesIgnoreCase("ab"));
 assertFalse(characterReader.matchesIgnoreCase("cd"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(characterReader.matchesAny('b', 'c'));
 assertFalse(characterReader.matchesAny('g', 'h'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(characterReader.matchesDigit());
 assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String consumed = characterReader.matchConsume("ab");
 assertEquals("ab", consumed);
 assertEquals(3, characterReader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String consumed = characterReader.matchConsumeIgnoreCase("Ab");
 assertEquals("ab", consumed);
 assertEquals(3, characterReader.pos());
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(characterReader.containsIgnoreCase("ab"));
 assertFalse(characterReader.containsIgnoreCase("cd"));
 }
}