package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");

 Assert.assertEquals(0, reader.pos());
 Assert.assertTrue(reader.isEmpty());
 Assert.assertEquals(EOF, reader.current());
 }

 @Test
 public void testBasicConsumption() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('a', reader.current());

 reader.consume();
 reader.consume();
 reader.consume();

 Assert.assertEquals(3, reader.pos());
 Assert.assertEquals('d', reader.current());
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("abcdabcd");

 reader.mark();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.rewindToMark();

 Assert.assertEquals('a', reader.current());
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testConsumeSubstring() {
 CharacterReader reader = new CharacterReader("abcdefghijklm");

 String substring = reader.consumeTo('l');

 Assert.assertEquals("abcdefg", substring);
 Assert.assertEquals(8, reader.pos());
 }

 @Test
 public void testConsumeSequence() {
 CharacterReader reader = new CharacterReader("1234567890");

 String sequence = reader.consumeTo("56");

 Assert.assertEquals("1234", sequence);
 Assert.assertEquals(6, reader.pos());
 }

 @Test(expected = AssertionError.class)
 public void testConsumeEndOfString() {
 CharacterReader reader = new CharacterReader("abcdef");

 String endOfString = reader.consumeToEnd();

 Assert.assertEquals("def", endOfString);
 Assert.assertEquals(6, reader.pos());
 reader.consume();

 // This should throw an AssertionError as pos should be equal to length
 Assert.assertEquals(7, reader.pos());
 }

 @Test
 public void testNextIndexOf() {
 CharacterReader reader = new CharacterReader("abccdeffeabccde");

 Assert.assertEquals(-1, reader.nextIndexOf('z'));
 Assert.assertEquals(2, reader.nextIndexOf('c'));
 Assert.assertEquals(4, reader.nextIndexOf("ff"));
 }

 @Test
 public void testConsumeAny() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");

 String any = reader.consumeToAny('a', 'b');

 Assert.assertEquals("ab", any);
 Assert.assertEquals(3, reader.pos());
 }

 @Test
 public void testConsumeAnySorted() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");

 String sortedAny = reader.consumeToAnySorted(new char[]{'a', 'd'});

 Assert.assertEquals("abcd", sortedAny);
 Assert.assertEquals(5, reader.pos());
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&<abc&def>");

 String data = reader.consumeData();

 Assert.assertEquals("&<abc", data);
 Assert.assertEquals(6, reader.pos());
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<tag data=\"value\">");

 String tagName = reader.consumeTagName();

 Assert.assertEquals("tag", tagName);
 Assert.assertEquals(11, reader.pos());
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("<tag data=\"value\">");

 String end = reader.consumeToEnd();

 Assert.assertEquals("value>", end);
 Assert.assertEquals(15, reader.pos());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijk");

 String letterSequence = reader.consumeLetterSequence();

 Assert.assertEquals("abcdef", letterSequence);
 Assert.assertEquals(7, reader.pos());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("abc123def456");

 String letterThenDigitSequence = reader.consumeLetterThenDigitSequence();

 Assert.assertEquals("abc123def", letterThenDigitSequence);
 Assert.assertEquals(11, reader.pos());
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");

 Assert.assertTrue(reader.matches('d'));
 Assert.assertTrue(reader.matches("cd"));
 Assert.assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("AbCdABCdABCd");

 Assert.assertTrue(reader.matchesIgnoreCase("cd"));
 Assert.assertTrue(reader.matchesIgnoreCase("D"));
 Assert.assertFalse(reader.matchesIgnoreCase("z"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");

 Assert.assertTrue(reader.matchesAny('a', 'd'));
 Assert.assertFalse(reader.matchesAny('z'));
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'd'}));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");

 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("1234567890");

 Assert.assertTrue(reader.matchesDigit());
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");

 Assert.assertTrue(reader.matchConsume("cd"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("ABCdABCdABCd");

 Assert.assertTrue(reader.matchConsumeIgnoreCase("cd"));
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Title</title></head><body>Body</body></html>");

 Assert.assertTrue(reader.containsIgnoreCase("/title"));
 Assert.assertTrue(reader.containsIgnoreCase("/body"));
 Assert.assertFalse(reader.containsIgnoreCase("/footer"));
 }
}