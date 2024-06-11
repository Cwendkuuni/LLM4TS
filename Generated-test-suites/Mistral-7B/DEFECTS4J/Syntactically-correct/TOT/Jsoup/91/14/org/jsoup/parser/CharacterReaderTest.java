package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstruction() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 assertEquals("Test String", reader.toString());
 }

 @Test
 public void testCharacterReaderReader() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 assertEquals('T', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 reader.unconsume();
 assertEquals('e', reader.current());
 reader.consume();
 reader.mark();
 reader.rewindToMark();
 assertEquals('e', reader.current());
 reader.advance();
 assertEquals('t', reader.current());
 reader.unconsume();
 reader.advance();
 assertEquals('s', reader.current());
 reader.consumeTo('g');
 assertEquals("Test", reader.toString());
 reader.consumeToEnd();
 assertEquals("Test String", reader.toString());
 }

 @Test
 public void testCharacterReaderNullReader() {
 try {
 new CharacterReader(null, 10);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCharacterReaderNegativeReaderSize() {
 try {
 new CharacterReader(new StringReader("Test String"), -1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCharacterReaderEmptyString() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 assertTrue(reader.isEmpty());
 assertFalse(reader.matches('a'));
 assertNull(reader.consume());
 }

 @Test
 public void testCharacterReaderConsumer() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 assertEquals('T', reader.current());
 assertEquals('T', reader.consume());
 assertEquals('e', reader.current());
 assertEquals('e', reader.consume());
 assertEquals('s', reader.current());
 assertEquals(' ', reader.consume()); // consume space
 assertEquals('S', reader.current());
 assertEquals('t', reader.consume());
 assertEquals('r', reader.consume());
 assertEquals('i', reader.consume());
 assertEquals('n', reader.consume());
 assertEquals('g', reader.consume());
 assertFalse(reader.isEmpty());
 assertFalse(reader.matchesIgnoreCase("random"));
 assertNull(reader.consume());
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderMark() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 reader.mark();
 reader.consumeTo('s');
 reader.rewindToMark();
 assertEquals('T', reader.current());
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 assertEquals(6, reader.nextIndexOf('s'));
 assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testCharacterReaderNextIndexOfCharSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 assertEquals(6, reader.nextIndexOf("st"));
 assertEquals(-1, reader.nextIndexOf("ab"));
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 assertEquals("Test", reader.consumeTo('s'));
 assertEquals("String", reader.consumeTo("ng"));
 assertEquals("Test String", reader.consumeToEnd());
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 assertEquals("Test", reader.consumeToAny(new char[]{'s'}));
 assertEquals("Test String", reader.consumeToEnd());
 }

 @Test
 public void testCharacterReaderConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("Test String"));
 assertEquals("Test String", reader.consumeToAnySorted(new char[]{'s', 't'}));
 assertEquals("Test String", reader.consumeToEnd());
 }

 @Test
 public void testCharacterReaderConsumeData() {
 CharacterReader reader = new CharacterReader(new StringReader("Test & < null"));
 assertEquals("Test", reader.consumeData());
 }

 @Test
 public void testCharacterReaderConsumeTagName() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
 assertEquals("title", reader.consumeTagName());
 }

 @Test(expected = UncheckedIOException.class)
 public void testCharacterReaderIOException() {
 CharacterReader reader = new CharacterReader(new StringReader("Test") {
 @Override public int read(char[] cbuf, int off, int len) throws IOException { throw new IOException(); }
 });
 reader.consume(); // should throw exception
 }
}