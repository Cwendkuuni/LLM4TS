package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderEmpty() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertEquals("", reader.consumeToEnd());
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderSimple() {
 CharacterReader reader = new CharacterReader("abcdef");

 // Test current method
 Assert.assertEquals('a', reader.current());
 // Test consume method
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals('b', reader.current());
 // Test isEmpty method
 reader.advance();
 reader.advance();
 Assert.assertFalse(reader.isEmpty());
 // Test unconsume method
 reader.unconsume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 CharacterReader reader = new CharacterReader("HelloWorld");

 Assert.assertEquals(5, reader.nextIndexOf('l'));
 Assert.assertEquals(6, reader.nextIndexOf('W'));
 Assert.assertEquals(-1, reader.nextIndexOf('Z'));
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 CharacterReader reader = new CharacterReader("HelloWorld");

 String actual = reader.consumeTo('l');
 Assert.assertEquals("Hello", actual);
 reader.advance();
 reader.advance();
 actual = reader.consumeTo('l');
 Assert.assertEquals("llo", actual);
 }

 @Test(expected = AssertionError.class)
 public void testCharacterReaderConsumeToNullSequence() {
 CharacterReader reader = new CharacterReader("HelloWorld");
 reader.consumeTo((CharSequence) null);
 }

 @Test(expected = AssertionError.class)
 public void testCharacterReaderConsumeToNonExistentSequence() {
 CharacterReader reader = new CharacterReader("HelloWorld");
 reader.consumeTo("Zz");
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 CharacterReader reader = new CharacterReader("HelloWorld");
 String actual = reader.consumeToEnd();
 Assert.assertEquals("HelloWorld", actual);
 }

 @Test
 public void testCharacterReaderConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdefgABCDEFG");
 String actual = reader.consumeLetterSequence();
 Assert.assertEquals("abcdefg", actual);
 }

 @Test
 public void testCharacterReaderConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abc123def456");
 String actual = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("abc123", actual);
 }

 @Test
 public void testCharacterReaderConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("&amp;#x61;#x64;#x64;#x72;#x65;#x73;#x73;");
 String actual = reader.consumeHexSequence();
 Assert.assertEquals("amen", actual);
 }

 @Test
 public void testCharacterReaderConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 String actual = reader.consumeDigitSequence();
 Assert.assertEquals("1234567890", actual);
 }

 @Test
 public void testCharacterReaderMatches() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertTrue(reader.matches('e'));
 Assert.assertTrue(reader.matches("ef"));
 }

 @Test
 public void testCharacterReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertTrue(reader.matchesIgnoreCase("ef"));
 Assert.assertFalse(reader.matchesIgnoreCase("ab"));
 }

 @Test
 public void testCharacterReaderMatchesAny() {
 CharacterReader reader = new CharacterReader("abcabc");

 Assert.assertTrue(reader.matchesAny('a', 'b', 'c'));
 }

 @Test
 public void testCharacterReaderMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("abcabc");

 Assert.assertTrue(reader.matchesAnySorted('a', 'b', 'c'));
 }

 @Test
 public void testCharacterReaderMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testCharacterReaderMatchesDigit() {
 CharacterReader reader = new CharacterReader("123456");

 Assert.assertTrue(reader.matchesDigit());
 }

 @Test(expected = AssertionError.class)
 public void testCharacterReaderMarkRewindToMarkBeyondEnd() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.mark();
 reader.rewindToMark();
 reader.advance();
 reader.rewindToMark();
 }

 @Test
 public void testCharacterReaderCacheString() {
 CharacterReader reader = new CharacterReader("abcabcabcabc");
 String actual = reader.cacheString(0, 5);
 Assert.assertEquals("abcab", actual);
 }

 @Test
 public void testCharacterReaderRangeEquals() {
 CharacterReader reader = new CharacterReader("abcabcabcabc");
 String actual = reader.cacheString(0, 5);
 String expected = "abcab";
 Assert.assertTrue(reader.rangeEquals(0, 5, expected));
 }
}