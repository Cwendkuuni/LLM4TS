package org.jsoup.parser.test;

import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstruction() {
 CharacterReader reader = new CharacterReader("test string");
 Validate.notNull(reader);
 assertEquals("test string", reader.input);
 }

 @Test(expected = NullPointerException.class)
 public void testCharacterReaderNullInput() {
 new CharacterReader(null);
 }

 @Test
 public void testGetters() {
 CharacterReader reader = new CharacterReader("test string");
 assertEquals(0, reader.pos());
 assertFalse(reader.isEmpty());
 assertEquals('t', reader.current());
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("test string");
 assertEquals('t', reader.consume());
 assertEquals(1, reader.pos());
 assertFalse(reader.isEmpty());
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsumeEnd() {
 CharacterReader reader = new CharacterReader("test string");
 String consumed = reader.consumeToEnd();
 assertEquals("test ", consumed);
 assertEquals(6, reader.pos());
 assertFalse(reader.isEmpty());
 assertNull(reader.current());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("test string");
 String sequence = reader.consumeLetterSequence();
 assertEquals("test", sequence);
 assertEquals(6, reader.pos());
 assertNull(reader.current());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("test#ff0000");
 String sequence = reader.consumeHexSequence();
 assertEquals("ff0000", sequence);
 assertEquals(10, reader.pos());
 assertNull(reader.current());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("12345");
 String sequence = reader.consumeDigitSequence();
 assertEquals("12345", sequence);
 assertEquals(5, reader.pos());
 assertNull(reader.current());
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader("test string");
 String consumed = reader.consumeTo('t');
 assertEquals("es", consumed);
 assertEquals(3, reader.pos());
 assertFalse(reader.isEmpty());
 assertEquals('s', reader.current());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("test string");
 String consumed = reader.consumeToEnd();
 assertEquals("test string", consumed);
 assertEquals(12, reader.pos());
 assertNull(reader.current());
 }

 @Test
 public void testConsumeToIgnoreCase() {
 CharacterReader reader = new CharacterReader("Test String");
 String consumed = reader.consumeToIgnoreCase("test");
 assertEquals("Test String".substring(0, 4), consumed);
 assertEquals(4, reader.pos());
 assertFalse(reader.isEmpty());
 assertEquals('S', reader.current());
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("test#%&/string");
 String consumed = reader.consumeToAny('%', '#');
 assertEquals("test#", consumed);
 assertEquals(3, reader.pos());
 assertFalse(reader.isEmpty());
 assertEquals('%', reader.current());
 }

 @Test
 public void testConsumeToAnyEmpty() {
 CharacterReader reader = new CharacterReader("test");
 String consumed = reader.consumeToAny();
 assertEquals("", consumed);
 assertEquals(0, reader.pos());
 assertTrue(reader.isEmpty());
 assertNull(reader.current());
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("test string");
 assertTrue(reader.matches('t'));
 assertTrue(reader.matches("es"));
 assertFalse(reader.matches(' '));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("Test String");
 assertTrue(reader.matchesIgnoreCase("test"));
 assertFalse(reader.matchesIgnoreCase("Test"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("test#%&/string");
 assertTrue(reader.matchesAny('%', '#'));
 assertFalse(reader.matchesAny('s'));
 }

 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader("test string");
 reader.mark();
 reader.consumeTo('s');
 reader.rewindToMark();
 assertEquals('t', reader.current());
 }

 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader("test string");
 reader.advance();
 reader.advance();
 assertEquals('s', reader.current());
 }

 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("test string");
 reader.consume();
 reader.unconsume();
 assertEquals('t', reader.current());
 }

 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader("test string");
 assertEquals("string", reader.toString());
 }
}