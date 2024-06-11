package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.parser.CharacterReader;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader reader;
 private String input;

 @Before
 public void setup() {
 input = "abc123DEF456";
 reader = new CharacterReader(input);
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
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('a', reader.consume());
 assertEquals('b', reader.current());
 }

 @Test
 public void testUnconsume() {
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
 assertEquals("ab", reader.consumeAsString());
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("DEF456", reader.consumeToEnd());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("123", reader.consumeTo('3'));
 }

 @Test
 public void testConsumeToIgnoreCase() {
 reader.pos = 1;
 assertEquals("bc", reader.consumeTo("c"));
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToEmpty() {
 reader.consumeTo('z');
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("12", reader.consumeToAny('1', '2'));
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("abc", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("12", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('b'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 reader.pos = 1;
 assertTrue(reader.matchesIgnoreCase("b"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny(new char[]{'b', 'c'}));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 reader.pos = 1;
 assertEquals("bc", reader.matchConsume("bc"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 reader.pos = 1;
 assertEquals("BC", reader.matchConsumeIgnoreCase("bc"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("bc"));
 }

 @Test(expected = AssertionError.class)
 public void testInvalidInput() {
 new CharacterReader(null);
 }

 @Test
 public void testInputLength() {
 CharacterReader reader2 = new CharacterReader("123");
 assertEquals(3, reader.pos());
 }
}