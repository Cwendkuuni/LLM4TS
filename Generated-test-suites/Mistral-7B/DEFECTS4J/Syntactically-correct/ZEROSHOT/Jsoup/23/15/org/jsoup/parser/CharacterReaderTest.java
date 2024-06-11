package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
 public void testEmpty() {
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testConsumerEmpty() {
 assertEquals((char) -1, reader.consume());
 }

 @Test
 public void testConsumerNotEmpty() {
 assertEquals('h', reader.consume());
 assertEquals('h', reader.current());
 assertEquals(1, reader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('h', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "hello";
 String result = reader.consumeLetterSequence();
 assertEquals(expected, result);
 assertEquals(6, reader.pos());
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "world";
 String result = reader.consumeHexSequence();
 assertEquals(expected, result);
 assertEquals(12, reader.pos());
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "123";
 String result = reader.consumeDigitSequence();
 assertEquals(expected, result);
 assertEquals(3, reader.pos());
 }

 @Test
 public void testConsumeTo() {
 String expected = "world";
 String result = reader.consumeTo('l');
 assertEquals(expected, result);
 assertEquals(11, reader.pos());
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "world\n";
 String result = reader.consumeToEnd();
 assertEquals(expected, result);
 assertEquals(17, reader.pos());
 }

 @Test
 public void testConsumeToAny() {
 char[] expected = {'l', 'o', 'r', 'l'};
 char[] result = reader.consumeToAny('l', 'o', 'r', 'l').toCharArray();
 assertFalse(result.length == 0);
 assertEquals(expected.length, result.length);
 for (int i = 0; i < result.length; i++) {
 assertEquals(expected[i], result[i]);
 }
 }

 @Test
 public void testMatches() {
 char c = 'l';
 assertTrue(reader.matches(c));
 }

 @Test
 public void testMatchesIgnoreCase() {
 char c = 'L';
 assertTrue(reader.matchesIgnoreCase("l"));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = {'l', 'o'};
 assertTrue(reader.matchesAny(seq));
 }

 @Test
 public void testMatchesLetter() {
 char c = 'l';
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 char c = '1';
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String seq = "ll";
 assertTrue(reader.matchConsume(seq));
 assertEquals(2, reader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String seq = "Lo";
 assertTrue(reader.matchConsumeIgnoreCase(seq));
 assertEquals(2, reader.pos());
 }

 @Test
 public void testContainsIgnoreCase() {
 String seq = "ll";
 assertTrue(reader.containsIgnoreCase(seq));
 }

 @Test
 public void testMark() {
 reader.mark();
 }

 @Test
 public void testRewindToMark() {
 reader.rewindToMark();
 assertEquals('h', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testRewindToMarkFailed() {
 reader.rewindToMark();
 reader.rewindToMark();
 }
}