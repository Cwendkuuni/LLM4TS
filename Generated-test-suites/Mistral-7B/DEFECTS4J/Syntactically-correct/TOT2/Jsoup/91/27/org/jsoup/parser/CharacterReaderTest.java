package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstructorWithReader() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals("Hello World", reader.toString());
 }

 @Test
 public void testCharacterReaderConstructorWithString() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("Hello World", reader.toString());
 }

 @Test
 public void testCharacterReaderEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderIsEmptyFalse() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 Assert.assertFalse(reader.isEmpty());
 }

 @Test(expected = UncheckedIOException.class)
 public void testCharacterReaderIOException() {
 CharacterReader reader = new CharacterReader(new IOException("Test IOException").getReader()) {
 @Override
 public int read() throws IOException {
 throw new IOException("Test IOException");
 }
 };
 Assert.assertFalse(reader.isEmpty()); // Should throw IOException
 }

 @Test
 public void testCharacterReadergetCurrent() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testCharacterReaderconsume() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 reader.consume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testCharacterReaderadvance() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 reader.advance();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testCharacterReaderunconsume() {
 CharacterReader reader = new CharacterReader(new StringReader("ab"));
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testCharacterReaderrewindToMark() {
 CharacterReader reader = new CharacterReader(new StringReader("abc"));
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testCharacterReadernextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("abccdef"));
 Assert.assertEquals(2, reader.nextIndexOf('c'));
 Assert.assertEquals(6, reader.nextIndexOf('f'));
 Assert.assertEquals(-1, reader.nextIndexOf('g'));
 }

 @Test
 public void testCharacterReadernextIndexOfWithEmptyBuffer() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 Assert.assertEquals(-1, reader.nextIndexOf('a'));
 }

 @Test
 public void testCharacterReaderconsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("abcd"));
 String result = reader.consumeTo('c');
 Assert.assertEquals("ab", result);
 }

 @Test
 public void testCharacterReaderconsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("abcd"));
 String result = reader.consumeToEnd();
 Assert.assertEquals("abcd", result);
 }

 @Test
 public void testCharacterReaderconsumeToAny() {
 CharacterReader reader = new CharacterReader(new StringReader("abcd"));
 String result = reader.consumeToAny(new char[]{'c', 'd'});
 Assert.assertEquals("cd", result);
 }

 @Test
 public void testCharacterReaderconsumeToAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("abcd"));
 String result = reader.consumeToAnySorted(new char[]{'c', 'd'});
 Assert.assertEquals("cd", result);
 }

 @Test
 public void testCharacterReaderconsumeData() {
 CharacterReader reader = new CharacterReader(new StringReader("&<null"));
 String result = reader.consumeData();
 Assert.assertEquals("&<null", result);
 }

 @Test
 public void testCharacterReaderconsumeTagName() {
 CharacterReader reader = new CharacterReader(new StringReader("<title>Hello World</title>"));
 String result = reader.consumeTagName();
 Assert.assertEquals("title", result);
 }

 @Test
 public void testCharacterReaderconsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 String result = reader.consumeLetterSequence();
 Assert.assertEquals("Hello", result);
 }

 @Test
 public void testCharacterReaderconsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello123World"));
 String result = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("Hello123", result);
 }

 @Test
 public void testCharacterReaderconsumeHexSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("&#x61;&#x6C;&#x65;"));
 String result = reader.consumeHexSequence();
 Assert.assertEquals("abc", result);
 }

 @Test
 public void testCharacterReaderconsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("12345"));
 String result = reader.consumeDigitSequence();
 Assert.assertEquals("12345", result);
 }

 @Test
 public void testCharacterReadermatches() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matches('H'));
 Assert.assertTrue(reader.matches("Hello"));
 Assert.assertFalse(reader.matches('Z'));
 }

 @Test
 public void testCharacterReadermatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesIgnoreCase("hello"));
 Assert.assertFalse(reader.matchesIgnoreCase("Z"));
 }

 @Test
 public void testCharacterReadermatchesAny() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesAny('H', 'o'));
 Assert.assertFalse(reader.matchesAny('Z'));
 }

 @Test
 public void testCharacterReadermatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'H', 'o'}));
 Assert.assertFalse(reader.matchesAnySorted(new char[]{'Z'}));
 }

 @Test
 public void testCharacterReadermatchesLetter() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testCharacterReadermatchesDigit() {
 CharacterReader reader = new CharacterReader(new StringReader("12345"));
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testCharacterReadertoString() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 String result = reader.toString();
 Assert.assertEquals("Hello World", result);
 }

 @Test
 public void testCharacterReadercacheString() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 String result = reader.cacheString(reader.charBuf, reader.stringCache, reader.bufPos, reader.bufLength - reader.bufPos);
 Assert.assertEquals("Hello World", result);
 }
}