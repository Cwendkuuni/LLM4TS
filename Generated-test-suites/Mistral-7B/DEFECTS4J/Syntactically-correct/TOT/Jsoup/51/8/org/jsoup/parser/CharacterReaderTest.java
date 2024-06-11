package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * CharacterReaderTest
 */
public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 // empty reader should indicate empty
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testBasicConsumption() {
 CharacterReader reader = new CharacterReader("abcdef");
 // consume single character
 Assert.assertNotEquals('a', reader.consume());
 Assert.assertEquals('a', reader.current());
 // consume entire sequence
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("abcdef", consumed);
 // check current position
 Assert.assertEquals(7, reader.pos());
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcdef");
 // mark position and consume a character
 reader.mark();
 reader.consume();
 // rewind to mark and check current position
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testNextIndexOf() {
 CharacterReader reader = new CharacterReader("abcdef");
 // check next index of single character
 Assert.assertEquals(1, reader.nextIndexOf('b'));
 // check next index of sequence
 Assert.assertEquals(5, reader.nextIndexOf("def"));
 // check next index of non-existent sequence
 Assert.assertEquals(-1, reader.nextIndexOf("xyz"));
 }

 @Test
 public void testConsumeSubstring() {
 CharacterReader reader = new CharacterReader("abcdef");
 // consume substring 'def'
 String consumed = reader.consumeTo("def");
 Assert.assertEquals("def", consumed);
 // check current position
 Assert.assertEquals(7, reader.pos());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeBeyondEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 // consume beyond end
 reader.consumeToEnd();
 reader.consume();
 }

 @Test
 public void testConsumeSubstringCaseInsensitive() {
 CharacterReader reader = new CharacterReader("ABCDef");
 // consume substring 'def' case insensitive
 String consumed = reader.consumeToIgnoreCase("def");
 Assert.assertEquals("def", consumed);
 // check current position
 Assert.assertEquals(7, reader.pos());
 }

 @Test
 public void testConsumeSubstringAny() {
 CharacterReader reader = new CharacterReader("&<abcdef>");
 // consume substring containing special characters '&', '<'
 String consumed = reader.consumeToAny(TokeniserState.nullChar, '>');
 Assert.assertEquals("&<", consumed);
 // check current position
 Assert.assertEquals(4, reader.pos());
 }

 @Test
 public void testConsumeSubstringAnySorted() {
 CharacterReader reader = new CharacterReader("abcd123ef");
 // consume substring containing multiple characters sorted in sequence
 String consumed = reader.consumeToAnySorted('1', '3');
 Assert.assertEquals("123", consumed);
 // check current position
 Assert.assertEquals(6, reader.pos());
 }
}