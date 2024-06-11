package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for CharacterReader class
 */
public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");

 Assert.assertEquals(0, reader.pos());
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testReaderWithSingleCharacter() {
 CharacterReader reader = new CharacterReader("a");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testReaderWithMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abc");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('a', reader.current());
 Assert.assertEquals(1, reader.nextIndexOf('a'));
 Assert.assertEquals(2, reader.nextIndexOf('c'));
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 CharacterReader reader = new CharacterReader(null);
 }

 @Test
 public void testConsumeSingleCharacter() {
 CharacterReader reader = new CharacterReader("ab");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 reader.consume();

 Assert.assertEquals(1, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abc");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 reader.consume();
 reader.consume();
 reader.consume();

 Assert.assertEquals(3, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('c', reader.current());
 }

 @Test
 public void testConsumeEndOfStream() {
 CharacterReader reader = new CharacterReader("abc");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 reader.consume();
 reader.consume();
 reader.consume();

 Assert.assertEquals(3, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 reader.consume();

 Assert.assertEquals(4, reader.pos());
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testMarkUnconsume() {
 CharacterReader reader = new CharacterReader("abcd");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 reader.mark();
 reader.consume();
 reader.consume();
 reader.rewindToMark();

 Assert.assertEquals(2, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testConsumeSubString() {
 CharacterReader reader = new CharacterReader("abcd");

 String substring = reader.consumeTo('c');

 Assert.assertEquals(3, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals("bc", substring);
 }

 @Test
 public void testConsumeSubStringEmpty() {
 CharacterReader reader = new CharacterReader("ab");

 String substring = reader.consumeTo('e');

 Assert.assertEquals(2, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals("", substring);
 }

 @Test
 public void testNextIndexOfSingleCharacter() {
 CharacterReader reader = new CharacterReader("abac");

 Assert.assertEquals(1, reader.nextIndexOf('a'));
 Assert.assertEquals(-1, reader.nextIndexOf('e'));
 }

 @Test
 public void testNextIndexOfSubString() {
 CharacterReader reader = new CharacterReader("aaabbbccc");

 Assert.assertEquals(2, reader.nextIndexOf("aa"));
 Assert.assertEquals(-1, reader.nextIndexOf("ddd"));
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdef");

 String substring = reader.consumeToEnd();

 Assert.assertEquals(6, reader.pos());
 Assert.assertTrue(reader.isEmpty());
 Assert.assertEquals("def", substring);
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcd123ef");

 String substring = reader.consumeLetterSequence();

 Assert.assertEquals(4, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals("abcd", substring);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abcd123ef");

 String substring = reader.consumeLetterThenDigitSequence();

 Assert.assertEquals(6, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals("123ef", substring);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("abcd&#x61;ef");

 String substring = reader.consumeHexSequence();

 Assert.assertEquals(6, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals("ef", substring);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("abcd123ef");

 String substring = reader.consumeDigitSequence();

 Assert.assertEquals(3, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals("123", substring);
 }

 @Test
 public void testMatchesSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcd");

 Assert.assertTrue(reader.matches('d'));
 Assert.assertFalse(reader.matches('e'));
 }

 @Test
 public void testMatchesSubString() {
 CharacterReader reader = new CharacterReader("aaabbbccc");

 Assert.assertTrue(reader.matches("aa"));
 Assert.assertFalse(reader.matches("ddd"));
 }

 @Test
 public void testMatchesIgnoreCaseSingleCharacter() {
 CharacterReader reader = new CharacterReader("ABCD");

 Assert.assertTrue(reader.matchesIgnoreCase("d"));
 Assert.assertFalse(reader.matchesIgnoreCase("E"));
 }

 @Test
 public void testMatchesIgnoreCaseSubString() {
 CharacterReader reader = new CharacterReader("AAABBBCCC");

 Assert.assertTrue(reader.matchesIgnoreCase("aa"));
 Assert.assertFalse(reader.matchesIgnoreCase("DDD"));
 }

 @Test
 public void testMatchesAnySingleCharacter() {
 CharacterReader reader = new CharacterReader("abcd");

 Assert.assertTrue(reader.matchesAny('b', 'c'));
 Assert.assertFalse(reader.matchesAny('e'));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("abcd");

 Assert.assertTrue(reader.matchesAnySorted('c', 'b'));
 Assert.assertFalse(reader.matchesAnySorted('e'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcd");

 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter('2'));
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("abcd123ef");

 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testMatchConsumeSingleCharacter() {
 CharacterReader reader = new CharacterReader("abcd");

 Assert.assertTrue(reader.matchConsume('d'));
 Assert.assertEquals(3, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testMatchConsumeSubString() {
 CharacterReader reader = new CharacterReader("aaabbbccc");

 Assert.assertTrue(reader.matchConsume("aa"));
 Assert.assertEquals(3, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testMatchConsumeIgnoreCaseSingleCharacter() {
 CharacterReader reader = new CharacterReader("ABCD");

 Assert.assertTrue(reader.matchConsumeIgnoreCase("d"));
 Assert.assertEquals(3, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testMatchConsumeIgnoreCaseSubString() {
 CharacterReader reader = new CharacterReader("AAABBBCCC");

 Assert.assertTrue(reader.matchConsumeIgnoreCase("aa"));
 Assert.assertEquals(3, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testContainsIgnoreCaseSubString() {
 CharacterReader reader = new CharacterReader("aa<title>The quick brown fox<style>jumps over the lazy dog</style></title>");

 Assert.assertTrue(reader.containsIgnoreCase("/title"));
 Assert.assertTrue(reader.containsIgnoreCase("/style"));
 Assert.assertFalse(reader.containsIgnoreCase("/head"));
 }
}