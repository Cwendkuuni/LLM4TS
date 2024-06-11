package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 assertEquals(0, reader.pos());
 assertTrue(reader.isEmpty());
 assertNull(reader.current());
 }

 @Test
 public void testCharacterReaderSingleCharacter() {
 CharacterReader reader = new CharacterReader(new StringReader("a"));
 assertEquals(1, reader.pos());
 assertFalse(reader.isEmpty());
 assertEquals('a', reader.current());
 }

 @Test
 public void testCharacterReaderMultipleCharacters() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 assertEquals(3, reader.pos());
 assertFalse(reader.isEmpty());
 assertEquals('a', reader.current());
 reader.advance();
 assertEquals('b', reader.current());
 reader.advance();
 assertEquals('c', reader.current());
 }

 @Test(expected = UncheckedIOException.class)
 public void testCharacterReaderIOException() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 reader.mark();
 reader.bufferUp();
 reader.reader.mark(maxBufferLen);
 reader.reader.skip(maxBufferLen); // IOException should be thrown
 reader.rewindToMark();
 }

 @Test
 public void testCharacterReaderConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 assertEquals('a', reader.consume());
 assertEquals('b', reader.consume());
 assertEquals('c', reader.consume());
 assertEquals(EOF, reader.consume());
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("abcd"));
 assertEquals("ab", reader.consumeTo('b'));
 assertEquals("cd", reader.consumeTo('d'));
 assertEquals("", reader.consumeTo('e'));
 }

 @Test
 public void testCharacterReaderConsumeToIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("AbCdEfGhIjKlMnOpQrStUvWxYz"));
 assertEquals("ab", reader.consumeToIgnoreCase("ab"));
 assertEquals("efg", reader.consumeToIgnoreCase("efg"));
 assertEquals("", reader.consumeToIgnoreCase("xyz"));
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader(new StringReader("0123456789ABCDEF"));
 assertEquals("012345", reader.consumeToAny(new char[]{'0', '1', '2', '3', '4'}));
 assertEquals("ABCDEF", reader.consumeToAnySorted(new char[]{'F', 'E', 'D', 'C', 'B', 'A'}));
 assertEquals("", reader.consumeToAny(new char[]{'Z'}));
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
 assertEquals("abcdef", reader.consumeLetterSequence());
 }

 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("abc123def456"));
 assertEquals("abc123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("0123456789ABCDEF"));
 assertEquals("0123", reader.consumeHexSequence());
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
 assertEquals("1234567890", reader.consumeDigitSequence());
 }

 @Test
 public void testCharacterReaderMarkAndRewind() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 reader.mark();
 reader.advance();
 reader.advance();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
 assertEquals(10, reader.nextIndexOf('j'));
 assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testCharacterReaderNextIndexOfSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"));
 assertEquals(11, reader.nextIndexOf("ij"));
 assertEquals(-1, reader.nextIndexOf("xyz"));
 }

 @Test
 public void testCharacterReaderToString() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 assertEquals("abc", reader.toString());
 }
}