package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * CharacterReaderTest.java
 */
public class CharacterReaderTest {

 @Test
 public void testCharacterReaderEmptyInput() {
 char[] expected = new char[0];
 CharacterReader reader = new CharacterReader("");
 Assert.assertArrayEquals(expected, reader.consumeToEnd().toCharArray());
 }

 @Test
 public void testCharacterReaderSingleCharacterInput() {
 char[] expected = new char[]{'a'};
 CharacterReader reader = new CharacterReader("a");
 Assert.assertArrayEquals(expected, reader.consumeToEnd().toCharArray());
 }

 @Test
 public void testCharacterReaderMultipleCharacterInput() {
 char[] expected = new char[]{'a', 'b', 'c'};
 CharacterReader reader = new CharacterReader("abc");
 Assert.assertArrayEquals(expected, reader.consumeToEnd().toCharArray());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharacterReaderNullInput() {
 CharacterReader reader = new CharacterReader(null);
 }

 @Test
 public void testCharacterReaderConsume() {
 CharacterReader reader = new CharacterReader("abcdef");
 char c = reader.current();
 Assert.assertEquals('a', c);
 c = reader.consume();
 Assert.assertEquals('a', c);
 Assert.assertEquals(1, reader.pos());
 }

 @Test
 public void testCharacterReaderConsumeString() {
 CharacterReader reader = new CharacterReader("abcdef");
 String expected = "abc";
 String result = reader.consumeAsString();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 String expected = "def";
 String result = reader.consumeToEnd();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCharacterReaderConsumeToCharacter() {
 CharacterReader reader = new CharacterReader("abcdef");
 String expected = "abc";
 String result = reader.consumeTo('c');
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCharacterReaderConsumeToString() {
 CharacterReader reader = new CharacterReader("abcdef");
 String expected = "def";
 String result = reader.consumeTo("ef");
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 String expected = "abcd";
 String result = reader.consumeToAny('a', 'b', 'c', 'd');
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharacterReaderConsumeToInvalidCharacter() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.consumeTo('#');
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testCharacterReaderConsumeToEndOutOfBounds() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.consumeToEnd();
 reader.consume();
 reader.consume();
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("123abcdEFGhi456");
 String expected = "abcdEFGhi";
 String result = reader.consumeLetterSequence();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("123abcd%#E0F1G%H2i");
 String expected = "%#E0F1";
 String result = reader.consumeHexSequence();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("123abcd123efg456");
 String expected = "123";
 String result = reader.consumeDigitSequence();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertTrue(reader.matches('a'));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testCharacterReaderMatchesString() {
 CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");
 Assert.assertTrue(reader.matches("abc"));
 Assert.assertFalse(reader.matches("xyz"));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("ABCDEFabcdef");
 Assert.assertTrue(reader.matchesIgnoreCase("AbC"));
 Assert.assertFalse(reader.matchesIgnoreCase("xyz"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader reader = new CharacterReader("1234abcdabcd1234");
 Assert.assertTrue(reader.matchesAny('1', '2', '3', '4'));
 Assert.assertFalse(reader.matchesAny('x', 'y', 'z'));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter('0'));
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader reader = new CharacterReader("123abcd123efg456");
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testCharacterReaderMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }
}