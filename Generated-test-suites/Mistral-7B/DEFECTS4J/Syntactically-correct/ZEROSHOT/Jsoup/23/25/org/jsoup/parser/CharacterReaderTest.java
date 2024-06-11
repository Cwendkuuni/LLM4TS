package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] input = new char[]{'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '\n'};
 this.reader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
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
 }

 @Test
 public void testRewindToMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('h', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 reader.consume();
 reader.consume();
 String result = reader.consumeAsString();
 assertEquals("ll", result);
 }

 @Test
 public void testConsumeTo() {
 String result = reader.consumeTo('l');
 assertEquals("ello", reader.toString());
 assertEquals("ld", result);
 }

 @Test
 public void testConsumeToEnd() {
 String result = reader.consumeToEnd();
 assertEquals("world\n", reader.toString());
 assertEquals("world\n", result);
 }

 @Test
 public void testConsumeLetterSequence() {
 String result = reader.consumeLetterSequence();
 assertEquals("ello", reader.toString());
 assertEquals("hello", result);
 }

 @Test
 public void testConsumeHexSequence() {
 reader.advance();
 reader.advance();
 String result = reader.consumeHexSequence();
 assertEquals("rl", reader.toString());
 assertEquals("rl", result);
 }

 @Test
 public void testConsumeDigitSequence() {
 reader.advance();
 reader.advance();
 reader.advance();
 reader.advance();
 reader.advance();
 String result = reader.consumeDigitSequence();
 assertEquals("ld", reader.toString());
 assertEquals("ld", result);
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('l'));
 assertFalse(reader.matches('w'));
 }

 @Test
 public void testMatchesString() {
 String sequence = "ll";
 assertTrue(reader.matches(sequence));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 String sequence = "EL";
 assertTrue(reader.matchesIgnoreCase(sequence));
 }

 @Test
 public void testMatchesAny() {
 char[] sequence = new char[]{'l', 'd'};
 assertTrue(reader.matchesAny(sequence));
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
 public void testMatchConsumeString() {
 String sequence = "ll";
 assertTrue(reader.matchConsume(sequence));
 assertEquals("ld", reader.toString());
 }

 @Test
 public void testMatchConsumeIgnoreCaseString() {
 String sequence = "EL";
 assertTrue(reader.matchConsumeIgnoreCase(sequence));
 assertEquals("ld", reader.toString());
 }

 @Test
 public void testContainsIgnoreCaseString() {
 String sequence = "EL";
 assertTrue(reader.containsIgnoreCase(sequence));
 }

 @Test(expected = AssertionError.class)
 public void testConsumePastEnd() {
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
 String result = reader.consumeAsString();
 fail("Should have thrown an AssertionError!");
 }
}