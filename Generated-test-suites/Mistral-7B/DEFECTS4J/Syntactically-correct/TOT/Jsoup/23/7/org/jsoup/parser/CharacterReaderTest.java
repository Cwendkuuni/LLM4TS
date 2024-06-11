package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

 @Test
 public void testEmptyCharacterReader() {
 CharacterReader reader = new CharacterReader("");

 // Test isEmpty
 Assert.assertTrue(reader.isEmpty());

 // Test pos, mark, and length
 Assert.assertEquals(0, reader.pos());
 Assert.assertEquals(0, reader.mark());
 Assert.assertEquals(0, reader.length());
 }

 @Test
 public void testCharacterReaderWithValidInput() {
 CharacterReader reader = new CharacterReader("abcdef");

 // Test pos, mark, and length
 Assert.assertEquals(0, reader.pos());
 Assert.assertEquals(0, reader.mark());
 Assert.assertEquals(6, reader.length());

 // Test current and consume
 Assert.assertEquals('a', reader.current());
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals('b', reader.current());

 // Test unconsume and advance
 reader.unconsume();
 Assert.assertEquals('a', reader.current());
 reader.advance();
 Assert.assertEquals('b', reader.current());

 // Test mark and rewindToMark
 reader.mark();
 reader.consume();
 Assert.assertEquals('b', reader.current());
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());

 // Test consumeAsString, consumeTo, consumeTo, consumeToAny, and consumeToEnd
 Assert.assertEquals("abc", reader.consumeAsString());
 Assert.assertEquals("def", reader.consumeTo('e'));
 Assert.assertEquals("cdef", reader.consumeTo('f'));
 Assert.assertEquals("b", reader.consumeToAny('b'));
 Assert.assertEquals("def", reader.consumeToEnd());

 // Test matches, matchesIgnoreCase, matchesAny, matchesLetter, matchesDigit, matchConsume, matchConsumeIgnoreCase, and containsIgnoreCase
 Assert.assertTrue(reader.matches('a'));
 Assert.assertTrue(reader.matches("ab"));
 Assert.assertFalse(reader.matchesAny('z'));
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertTrue(reader.matchConsume("ab"));
 Assert.assertTrue(reader.matchConsumeIgnoreCase("Ab"));
 Assert.assertTrue(reader.containsIgnoreCase("ab"));
 }

 @Test(expected = AssertionError.class)
 public void testCharacterReaderWithNullInput() {
 CharacterReader reader = new CharacterReader(null);
 }

 @Test
 public void testCharacterReaderWithInputContainingSpecialCharacters() {
 CharacterReader reader = new CharacterReader("abc\r\n123def");

 // Test pos, mark, and length
 Assert.assertEquals(0, reader.pos());
 Assert.assertEquals(0, reader.mark());
 Assert.assertEquals(11, reader.length());

 // Test current and consume
 Assert.assertEquals('a', reader.current());
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals('\r', reader.current());
 Assert.assertEquals('\r', reader.consume());
 Assert.assertEquals('\n', reader.current());
 Assert.assertEquals('\n', reader.consume());
 Assert.assertEquals('1', reader.current());
 Assert.assertEquals('1', reader.consume());
 Assert.assertEquals('2', reader.current());
 Assert.assertEquals('2', reader.consume());
 Assert.assertEquals('3', reader.current());
 Assert.assertEquals('3', reader.consume());
 Assert.assertEquals('d', reader.current());
 Assert.assertEquals('d', reader.consume());
 Assert.assertEquals('e', reader.current());
 Assert.assertEquals('e', reader.consume());
 Assert.assertEquals('f', reader.current());
 Assert.assertEquals('f', reader.consume());
 Assert.assertEquals(EOF, reader.current());

 // Test unconsume and advance
 reader.unconsume();
 reader.advance();
 Assert.assertEquals('1', reader.current());

 // Test mark and rewindToMark
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());

 // Test consumeAsString, consumeTo, consumeTo, consumeToAny, and consumeToEnd
 Assert.assertEquals("abc", reader.consumeAsString());
 Assert.assertEquals("def", reader.consumeTo('f'));
 Assert.assertEquals("abc\r\n123def", reader.consumeToEnd());

 // Test matches, matchesIgnoreCase, matchesAny, matchesLetter, matchesDigit, matchConsume, matchConsumeIgnoreCase, and containsIgnoreCase
 Assert.assertTrue(reader.matches('\r'));
 Assert.assertTrue(reader.matches("\n"));
 Assert.assertTrue(reader.matches("1"));
 Assert.assertTrue(reader.matches("3"));
 Assert.assertFalse(reader.matches('z'));
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertTrue(reader.matchConsume("\r\n"));
 Assert.assertTrue(reader.matchConsumeIgnoreCase("AB"));
 Assert.assertTrue(reader.containsIgnoreCase("123"));
 }
}