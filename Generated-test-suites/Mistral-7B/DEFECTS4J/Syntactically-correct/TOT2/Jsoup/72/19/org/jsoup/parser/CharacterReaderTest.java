package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstructor() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
 assertEquals(11, reader.pos());
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderBufferUp() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
 reader.bufferUp();
 assertEquals(11, reader.pos());
 assertFalse(reader.isEmpty());
 reader.consume();
 reader.bufferUp();
 assertEquals(12, reader.pos());
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderPos() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
 reader.mark();
 reader.advance();
 reader.advance();
 reader.rewindToMark();
 assertEquals(2, reader.pos());
 }

 @Test
 public void testCharacterReaderIsEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
 assertFalse(reader.isEmpty());
 reader.consumeToEnd();
 reader.consumeToEnd(); // empty
 assertTrue(reader.isEmpty());
 }

 @Test(expected = UncheckedIOException.class)
 public void testCharacterReaderIOException() throws IOException {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World!")) {
 @Override
 public int read(char[] cbuf, int off, int len) throws IOException {
 throw new IOException();
 }
 };
 reader.consume();
 }

 @Test
 public void testCharacterReaderConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
 assertEquals('H', reader.current());
 assertEquals('H', reader.consume());
 assertEquals('e', reader.current());
 assertEquals('l', reader.consume());
 assertEquals('l', reader.consume());
 assertEquals('o', reader.consume());
 assertEquals(' ', reader.current());
 assertEquals('W', reader.consume());
 assertEquals('o', reader.consume());
 assertEquals('r', reader.consume());
 assertEquals('l', reader.consume());
 assertEquals('d', reader.consume());
 assertEquals('!', reader.consume());
 assertEquals(EOF, reader.current());
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
 assertEquals("llo", reader.consumeTo('l'));
 assertEquals("llo", reader.consumeTo('l').toString());
 assertEquals("ld!", reader.consumeTo('d').toString());
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
 assertEquals("Hello World!", reader.consumeToEnd().toString());
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
 assertEquals("abc", reader.consumeToAny(new char[]{'a', 'b', 'c'}));
 assertEquals("xyz", reader.consumeToAny(new char[]{'x', 'y', 'z'}));
 }

 @Test
 public void testCharacterReaderConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
 assertEquals("def", reader.consumeToAnySorted(new char[]{'d', 'e', 'f'}));
 }

 @Test
 public void testCharacterReaderConsumeData() {
 CharacterReader reader = new CharacterReader(new StringReader("&<null"));
 assertEquals("&<null", reader.consumeData().toString());
 }

 @Test
 public void testCharacterReaderConsumeTagName() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
 assertEquals("<title>", reader.consumeTagName());
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
 assertEquals(10, reader.nextIndexOf('d'));
 assertEquals(6, reader.nextIndexOf('o'));
 assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testCharacterReaderNextIndexOfSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
 assertEquals(16, reader.nextIndexOf("/title>"));
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcdabcd"));
 assertTrue(reader.matches('a'));
 assertTrue(reader.matches('b'));
 assertFalse(reader.matches('z'));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("ABCDabcdABCDabcd"));
 assertTrue(reader.matchesIgnoreCase("ab"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
 assertTrue(reader.matchesAny('H', 'e', 'l', 'o', 'W'));
 }

 @Test
 public void testCharacterReaderMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World!"));
 assertTrue(reader.matchesAnySorted(new char[]{'H', 'l'}));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcdabcd"));
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcdabcd"));
 assertFalse(reader.matchesDigit());
 reader.consumeLetterSequence();
 assertTrue(reader.matchesDigit());
 }
}