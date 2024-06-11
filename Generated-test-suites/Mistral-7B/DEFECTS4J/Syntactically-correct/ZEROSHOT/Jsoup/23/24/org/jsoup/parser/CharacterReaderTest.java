package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 char[] testInput = {'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd', '\n'};
 reader = new CharacterReader(new String(testInput));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.advance();
 assertFalse(reader.isEmpty());
 reader.rewindToMark();
 assertFalse(reader.isEmpty());
 reader.consumeToEnd();
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrent() {
 assertEquals('h', reader.current());
 reader.advance();
 assertEquals('e', reader.current());
 reader.unconsume();
 assertEquals('e', reader.current());
 reader.consume();
 assertEquals('l', reader.current());
 reader.consumeToEnd();
 assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testConsume() {
 reader.consume();
 assertEquals('h', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 reader.consume();
 assertEquals('l', reader.current());
 reader.consumeToEnd();
 assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.advance();
 reader.advance();
 reader.unconsume();
 assertEquals('e', reader.current());
 reader.consume();
 reader.unconsume();
 assertEquals('l', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('e', reader.current());
 reader.advance(3);
 assertEquals('w', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.consume();
 reader.rewindToMark();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals("ello", reader.consumeAsString());
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals("worl", reader.consumeAsString());
 reader.consumeToEnd();
 assertEquals("", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals("ello", reader.consumeTo('l'));
 reader.advance();
 reader.advance();
 reader.consume();
 reader.consume();
 assertEquals("ld", reader.consumeTo('d'));
 reader.consumeToEnd();
 assertEquals("", reader.consumeTo('d'));
 }

 @Test
 public void testConsumeToAny() {
 assertEquals("ello", reader.consumeToAny('l', 'o'));
 reader.advance();
 reader.advance();
 reader.consume();
 reader.consume();
 assertEquals("ld", reader.consumeToAny('l', 'd'));
 reader.consumeToEnd();
 assertEquals("", reader.consumeToAny('l', 'd'));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("world\n", reader.consumeToEnd());
 reader.consumeToEnd();
 assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("ello", reader.consumeLetterSequence());
 reader.advance();
 reader.advance();
 reader.consume();
 reader.consume();
 assertEquals("ld", reader.consumeLetterSequence());
 reader.consumeToEnd();
 assertEquals("", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 reader.advance();
 reader.advance();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals("orld\n", reader.consumeHexSequence());
 reader.consumeToEnd();
 assertEquals("", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 reader.advance();
 reader.advance();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals("12345", reader.consumeDigitSequence());
 reader.consumeToEnd();
 assertEquals("", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertEquals(true, reader.matches('l'));
 reader.advance();
 reader.advance();
 reader.consume();
 reader.consume();
 assertEquals(false, reader.matches('l'));
 reader.consumeToEnd();
 assertEquals(false, reader.matches('l'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 reader.consumeLetterSequence();
 assertEquals(true, reader.matchesIgnoreCase("ELLO"));
 reader.consumeHexSequence();
 assertEquals(true, reader.matchesIgnoreCase("ORLD"));
 reader.consumeToEnd();
 assertEquals(false, reader.matchesIgnoreCase("ELLO"));
 }

 @Test
 public void testMatchesAny() {
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals(true, reader.matchesAny('d', 'l'));
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 assertEquals(true, reader.matchesAny('d', 'o'));
 reader.consumeToEnd();
 assertEquals(false, reader.matchesAny('d', 'o'));
 }

 @Test
 public void testMatchConsume() {
 assertEquals("ello", reader.matchConsume("ello"));
 assertEquals("ld", reader.matchConsume("ld"));
 reader.consumeToEnd();
 assertEquals("", reader.matchConsume("ello"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 reader.consumeLetterSequence();
 assertEquals("ELLO", reader.matchConsumeIgnoreCase("ELLO"));
 reader.consumeHexSequence();
 assertEquals("ORLD", reader.matchConsumeIgnoreCase("ORLD"));
 reader.consumeToEnd();
 assertEquals("", reader.matchConsumeIgnoreCase("ELLO"));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("ORLD"));
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeLetterSequence();
 reader.consumeHexSequence();
 reader.consumeHexSequence();
 reader.consumeHexSequence();
 reader.consumeHexSequence();
 reader.consumeHexSequence();
 reader.consumeHexSequence();
 reader.consumeHexSequence();
 assertFalse(reader.containsIgnoreCase("ORLD"));
 }

 @Test
 public void testToString() {
 assertEquals("ld", reader.toString());
 reader.consumeToEnd();
 assertEquals("", reader.toString());
 }
}