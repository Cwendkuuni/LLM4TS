package org.jsoup.parser.test;

import org.jsoup.parser.CharacterReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstructor() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertNotNull(reader);
 assertEquals("Hello World", reader.input);
 assertEquals(11, reader.length);
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 reader = new CharacterReader("Hello World");
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrentCharacter() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals('H', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.consume();
 assertEquals('e', reader.current());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumePastEnd() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.consumeToEnd();
 reader.consume();
 }

 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.consume();
 reader.unconsume();
 assertEquals('H', reader.current());
 }

 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.advance();
 assertEquals('e', reader.current());
 }

 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('H', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testRewindToMarkWhenAtStart() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.mark();
 reader.rewindToMark();
 }

 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("llo Wor", reader.consumeAsString());
 reader.consume();
 reader.consume();
 assertEquals("ld", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("llo", reader.consumeTo('l'));
 reader.consume();
 assertEquals("ld", reader.consumeTo('d'));
 reader.consume();
 assertNull(reader.consumeTo('x'));
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("Hello", reader.consumeToAny('e', 'l', 'o'));
 reader.consume();
 reader.consume();
 assertEquals("ld", reader.consumeToAny('d'));
 reader.consume();
 assertNull(reader.consumeToAny('x'));
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("World", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("Hello", reader.consumeLetterSequence());
 reader.consume();
 reader.consume();
 assertEquals("ld", reader.consumeLetterSequence());
 reader.consume();
 assertEquals("", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0xA1B2C3");
 assertEquals("A1B2C3", reader.consumeHexSequence());
 reader.consume();
 assertEquals("", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("12345");
 assertEquals("12345", reader.consumeDigitSequence());
 reader.consume();
 reader.consume();
 assertEquals("3", reader.consumeDigitSequence());
 reader.consume();
 assertEquals("", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matches('H'));
 assertTrue(reader.matches("ll"));
 assertFalse(reader.matches('X'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesIgnoreCase("H"));
 assertTrue(reader.matchesIgnoreCase("ll"));
 assertFalse(reader.matchesIgnoreCase("X"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesAny('H', 'l', 'o'));
 assertTrue(reader.matchesAny('l', 'l'));
 assertFalse(reader.matchesAny('X'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesLetter());
 reader.consume();
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("12345");
 assertTrue(reader.matchesDigit());
 reader.consume();
 reader.consume();
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("llo", reader.matchConsume("ll"));
 assertEquals("World", reader.matchConsume("World"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("llo", reader.matchConsumeIgnoreCase("ll"));
 assertEquals("World", reader.matchConsumeIgnoreCase("world"));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("Hello <html><title>Title</title></html>");
 assertTrue(reader.containsIgnoreCase("title"));
 assertTrue(reader.containsIgnoreCase("html"));
 assertFalse(reader.containsIgnoreCase("foo"));
 }
}