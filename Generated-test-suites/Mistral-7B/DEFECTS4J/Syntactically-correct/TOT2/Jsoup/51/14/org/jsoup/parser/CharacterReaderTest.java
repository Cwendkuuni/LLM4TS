package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CharacterReaderTest class
 */
public class CharacterReaderTest {

 @Test
 public void testConstructor() {
 CharacterReader reader = new CharacterReader("Test String");
 assertEquals("Test String", reader.input.toString());
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertEquals(0, reader.pos());
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("abcdef");
 assertEquals('a', reader.consume());
 assertEquals('b', reader.consume());
 assertEquals('c', reader.consume());
 assertEquals('d', reader.consume());
 assertEquals('e', reader.consume());
 assertEquals('f', reader.consume());
 assertEquals(CharacterReader.EOF, reader.consume());
 }

 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("abcde");
 reader.consume();
 reader.unconsume();
 assertEquals('a', reader.current());
 }

 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader("abcde");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("abc");
 assertEquals("abc", reader.consumeAsString());
 }

 @Test
 public void testNextIndexOfChar() {
 CharacterReader reader = new CharacterReader("aaaabbc");
 assertEquals(1, reader.nextIndexOf('a'));
 assertEquals(5, reader.nextIndexOf('b'));
 assertEquals(-1, reader.nextIndexOf('z'));
 }

 @Test
 public void testNextIndexOfCharSequence() {
 CharacterReader reader = new CharacterReader("aaabbaaaabba");
 assertEquals(2, reader.nextIndexOf("aa"));
 assertEquals(5, reader.nextIndexOf("bb"));
 assertEquals(11, reader.nextIndexOf("aa"));
 }

 @Test
 public void testConsumeToChar() {
 CharacterReader reader = new CharacterReader("abcdefghijkl");
 assertEquals("abc", reader.consumeTo('c'));
 }

 @Test
 public void testConsumeToCharSequence() {
 CharacterReader reader = new CharacterReader("abcdefghijkl");
 assertEquals("abcdef", reader.consumeTo("def"));
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 assertEquals("abcd", reader.consumeToAny('a', 'b', 'c', 'd'));
 }

 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 assertEquals("abcd", reader.consumeToAnySorted('a', 'b', 'c', 'd'));
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&<>&&<null>");
 assertEquals("&<", reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<TAG1>\t<TAG2>\n<TAG3>\r<TAG4>\f<TAG5>\t<TAG6>\t/TAG7>");
 assertEquals("\t<TAG2>", reader.consumeTagName());
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToEndWithEmptyInput() {
 CharacterReader reader = new CharacterReader("");
 reader.consumeToEnd();
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToEndWithNullInput() {
 CharacterReader reader = new CharacterReader(null);
 reader.consumeToEnd();
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToAnyWithEmptyInput() {
 CharacterReader reader = new CharacterReader("");
 reader.consumeToAny('a');
 }

 @Test(expected = AssertionError.class)
 public void testConsumeToAnyWithNullInput() {
 CharacterReader reader = new CharacterReader(null);
 reader.consumeToAny('a');
 }

 @Test
 public void testMatchesChar() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 assertTrue(reader.matches('a'));
 assertFalse(reader.matches('z'));
 }

 @Test
 public void testMatchesString() {
 CharacterReader reader = new CharacterReader("abcdefghijkl");
 assertTrue(reader.matches("abc"));
 assertFalse(reader.matches("xyz"));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 CharacterReader reader = new CharacterReader("ABCDEFGHIJKL");
 assertTrue(reader.matchesIgnoreCase("abc"));
 assertFalse(reader.matchesIgnoreCase("XYZ"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 assertTrue(reader.matchesAny('a'));
 assertTrue(reader.matchesAny('b'));
 assertFalse(reader.matchesAny('z'));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 assertTrue(reader.matchesAnySorted('a'));
 assertTrue(reader.matchesAnySorted('b'));
 assertFalse(reader.matchesAnySorted('z'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdefghijkl");
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("1234567890");
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsumeChar() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 reader.matchConsume('a');
 assertEquals('a', reader.current());
 }

 @Test
 public void testMatchConsumeString() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 reader.matchConsume("ab");
 assertEquals("ab", reader.consumeToEnd());
 }

 @Test
 public void testContainsIgnoreCaseString() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>Test</body></html>");
 assertTrue(reader.containsIgnoreCase("title"));
 assertTrue(reader.containsIgnoreCase("test"));
 assertFalse(reader.containsIgnoreCase("header"));
 }
}