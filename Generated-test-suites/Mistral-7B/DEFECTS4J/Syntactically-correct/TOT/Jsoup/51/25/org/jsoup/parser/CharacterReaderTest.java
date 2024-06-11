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
 public void testBasicReader() {
 String input = "abcdef";
 CharacterReader reader = new CharacterReader(input);

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('a', reader.current());

 reader.consume();
 Assert.assertEquals('b', reader.current());

 reader.unconsume();
 Assert.assertEquals('a', reader.current());

 reader.advance();
 Assert.assertEquals('b', reader.current());

 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());

 reader.consumeAsString();
 Assert.assertEquals("a", reader.consumeAsString());
 Assert.assertEquals("ab", reader.consume("bc"));
 Assert.assertEquals("def", reader.consumeToEnd());
 }

 @Test
 public void testScanString() {
 CharacterReader reader = new CharacterReader("abcdefabcdefabcdef");

 Assert.assertEquals("abcdef", reader.consumeTo("abcdef"));
 Assert.assertEquals("abcdefabcdef", reader.consumeToEnd());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testScanNullString() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.consumeTo(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testScanEmptyString() {
 CharacterReader reader = new CharacterReader("abcdef");
 reader.consumeTo("");
 }

 @Test
 public void testScanCharacter() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertEquals('e', reader.nextIndexOf('e'));
 Assert.assertEquals(-1, reader.nextIndexOf('g'));
 }

 @Test
 public void testScanSequence() {
 CharacterReader reader = new CharacterReader("abcabc");

 Assert.assertEquals("abc", reader.consumeTo("abc"));
 Assert.assertEquals("abcabc", reader.consumeToEnd());
 Assert.assertEquals("abc", reader.consumeToAny(new char[]{'a', 'b', 'c'}));
 }

 @Test
 public void testScanSortedSequence() {
 CharacterReader reader = new CharacterReader("abcdabcdabcdabcd");

 Assert.assertEquals("abcd", reader.consumeToAnySorted(new char[]{'a', 'b', 'c', 'd'}));
 }

 @Test
 public void testScanData() {
 CharacterReader reader = new CharacterReader("&<null");

 Assert.assertEquals("&", reader.consumeData().substring(0, 1));
 Assert.assertEquals("<", reader.consumeData().substring(reader.consumeData().length() - 1, reader.consumeData().length()));
 }

 @Test
 public void testScanTagName() {
 CharacterReader reader = new CharacterReader("<title>Hello</title>");

 Assert.assertEquals("/title", reader.consumeTagName());
 }

 @Test
 public void testScanToEnd() {
 CharacterReader reader = new CharacterReader("<title>Hello</title>");

 Assert.assertEquals("<title>Hello</title>", reader.consumeToEnd());
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertTrue(reader.matches('e'));
 Assert.assertFalse(reader.matches('g'));
 }

 @Test
 public void testMatchesString() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertTrue(reader.matches("ef"));
 Assert.assertFalse(reader.matches("cd"));
 }

 @Test
 public void testMatchesIgnoringCase() {
 CharacterReader reader = new CharacterReader("abcABCdef");

 Assert.assertTrue(reader.matchesIgnoreCase("ef"));
 Assert.assertFalse(reader.matchesIgnoreCase("CD"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'd', 'e', 'f'));
 Assert.assertFalse(reader.matchesAny('g'));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c', 'd', 'e', 'f'}));
 Assert.assertFalse(reader.matchesAnySorted(new char[]{'g', 'f', 'e'}));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesLetter('0'));
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("abc123def");

 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testConsumeTagNameWithWhitespace() {
 CharacterReader reader = new CharacterReader("<  title  >Hello</title>");

 Assert.assertEquals("/title", reader.consumeTagName());
 }

 @Test
 public void testConsumeTagNameWithAttributes() {
 CharacterReader reader = new CharacterReader("<title id=\"titleId\" class=\"titleClass\" >Hello</title>");

 Assert.assertEquals("/title", reader.consumeTagName());
 }
}