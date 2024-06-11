package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for CharacterReader class
 */
public class CharacterReaderTest {

 private static final String TEST_CHAR_SEQUENCE = "testCharSequence";
 private static final String TEST_STRING = "This is a test string.";
 private static final String TEST_LONG_STRING = "This is a very long test string that consumes more than maxCacheLen.";
 private static final char TEST_CHAR = 't';
 private static final char[] TEST_CHAR_ARRAY = {'t', 'e', 's', 't'};
 private static final CharSequence TEST_CHAR_SEQUENCE_INSTANCE = new String("testCharSequence");

 /**
 * Test instance creation
 */
 @Test
 public void testCharacterReaderCreation() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertNotNull(reader);
 }

 /**
 * Test method: pos()
 */
 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume();
 reader.consume();
 assertEquals(2, reader.pos());
 }

 /**
 * Test method: isEmpty()
 */
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 /**
 * Test method: current()
 */
 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals(TEST_STRING.charAt(0), reader.current());
 reader.consume();
 assertEquals(TEST_STRING.charAt(1), reader.current());
 }

 /**
 * Test method: consume()
 */
 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume();
 assertEquals(TEST_STRING.charAt(0), reader.current());
 reader.consume();
 assertEquals(TEST_STRING.charAt(1), reader.current());
 reader.consume();
 assertEquals(TEST_STRING.charAt(2), reader.current());
 reader.consume();
 assertEquals(TEST_STRING.charAt(3), reader.current());
 // consume beyond the last character
 assertEquals(CharacterReader.EOF, reader.current());
 }

 /**
 * Test method: unconsume()
 */
 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.consume();
 reader.consume();
 reader.unconsume();
 assertEquals(TEST_STRING.charAt(1), reader.current());
 }

 /**
 * Test method: advance()
 */
 @Test
 public void testAdvance() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.advance();
 reader.advance();
 reader.advance();
 assertEquals(TEST_STRING.charAt(3), reader.current());
 }

 /**
 * Test method: mark()
 */
 @Test
 public void testMark() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals(TEST_STRING.charAt(0), reader.current());
 }

 /**
 * Test method: consumeAsString()
 */
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeAsString();
 assertEquals(1, consumed.length());
 assertEquals(TEST_STRING.charAt(0), consumed.charAt(0));
 consumed = reader.consumeAsString();
 assertEquals(2, consumed.length());
 assertEquals(TEST_STRING.charAt(0), consumed.charAt(0));
 assertEquals(TEST_STRING.charAt(1), consumed.charAt(1));
 }

 /**
 * Test method: nextIndexOf(char)
 */
 @Test
 public void testNextIndexOfChar() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals(1, reader.nextIndexOf(TEST_CHAR));
 reader.consume();
 assertEquals(2, reader.nextIndexOf(TEST_CHAR));
 reader.consume();
 assertEquals(3, reader.nextIndexOf(TEST_CHAR));
 assertEquals(-1, reader.nextIndexOf(Character.MIN_VALUE));
 reader.consumeTo(TEST_CHAR); // consume all occurrences
 assertEquals(-1, reader.nextIndexOf(TEST_CHAR));
 }

 /**
 * Test method: nextIndexOf(CharSequence)
 */
 @Test
 public void testNextIndexOfCharSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals(0, reader.nextIndexOf(TEST_CHAR_SEQUENCE));
 reader.consume(TEST_CHAR_SEQUENCE.length());
 assertEquals(-1, reader.nextIndexOf(TEST_CHAR_SEQUENCE));
 assertEquals(0, reader.nextIndexOf(TEST_CHAR_SEQUENCE_INSTANCE));
 reader.consumeTo(TEST_CHAR_SEQUENCE_INSTANCE);
 assertEquals(-1, reader.nextIndexOf(TEST_CHAR_SEQUENCE_INSTANCE));
 reader.consumeTo(TEST_CHAR_SEQUENCE_INSTANCE + "more");
 assertEquals(-1, reader.nextIndexOf(TEST_CHAR_SEQUENCE_INSTANCE));
 }

 /**
 * Test method: consumeTo(char)
 */
 @Test
 public void testConsumeToChar() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeTo(TEST_CHAR);
 assertEquals(1, consumed.length());
 assertEquals(TEST_STRING.charAt(0), consumed.charAt(0));
 reader.consume();
 consumed = reader.consumeTo(TEST_CHAR);
 assertEquals(1, consumed.length());
 assertEquals(TEST_STRING.charAt(1), consumed.charAt(0));
 reader.consume();
 reader.consume();
 consumed = reader.consumeTo(TEST_CHAR);
 assertEquals(1, consumed.length());
 assertEquals(TEST_STRING.charAt(2), consumed.charAt(0));
 }

 /**
 * Test method: consumeTo(String)
 */
 @Test
 public void testConsumeToString() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeTo(TEST_CHAR_SEQUENCE);
 assertEquals(TEST_CHAR_SEQUENCE.length(), consumed.length());
 assertEquals(TEST_STRING.substring(0, TEST_CHAR_SEQUENCE.length()), consumed);
 reader.consume();
 consumed = reader.consumeTo(TEST_CHAR_SEQUENCE);
 assertEquals(TEST_STRING.length() - TEST_CHAR_SEQUENCE.length(), consumed.length());
 assertEquals(TEST_STRING.substring(TEST_CHAR_SEQUENCE.length()), consumed);
 }

 /**
 * Test method: consumeToAny(char[])
 */
 @Test
 public void testConsumeToAnyCharArray() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeToAny(TEST_CHAR_ARRAY);
 assertEquals(TEST_CHAR_ARRAY.length, consumed.length());
 for (int i = 0; i < TEST_CHAR_ARRAY.length; i++) {
 assertEquals(TEST_CHAR_ARRAY[i], consumed.charAt(i));
 }
 consumed = reader.consumeToAny(Character.toCharArray("test"));
 assertEquals("test".length(), consumed.length());
 assertEquals("test", consumed);
 }

 /**
 * Test method: consumeToAnySorted(char[])
 */
 @Test
 public void testConsumeToAnySortedCharArray() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeToAnySorted(TEST_CHAR_ARRAY);
 assertEquals(TEST_CHAR_ARRAY.length, consumed.length());
 for (int i = 0; i < TEST_CHAR_ARRAY.length; i++) {
 assertEquals(TEST_CHAR_ARRAY[i], consumed.charAt(i));
 }
 reader.consumeTo(TEST_CHAR_ARRAY[0]);
 consumed = reader.consumeToAnySorted(TEST_CHAR_ARRAY);
 assertEquals(TEST_CHAR_ARRAY.length - 1, consumed.length());
 assertNotSame(consumed, TEST_STRING.substring(0, TEST_CHAR_ARRAY[0]));
 }

 /**
 * Test method: consumeData()
 */
 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeData();
 assertEquals(TEST_STRING.indexOf("&"), consumed.lastIndexOf("&"));
 assertEquals(TEST_STRING.indexOf("<"), consumed.lastIndexOf("<"));
 assertEquals(Character.MIN_VALUE, consumed.charAt(consumed.length() - 1));
 }

 /**
 * Test method: consumeTagName()
 */
 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeTagName();
 assertEquals(TEST_STRING.indexOf("/"), consumed.lastIndexOf("/"));
 assertEquals(TEST_STRING.indexOf(">"), consumed.lastIndexOf(">"));
 assertEquals(Character.MIN_VALUE, consumed.charAt(consumed.length() - 1));
 }

 /**
 * Test method: consumeToEnd()
 */
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeToEnd();
 assertEquals(TEST_STRING, consumed);
 }

 /**
 * Test method: consumeLetterSequence()
 */
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeLetterSequence();
 assertEquals(TEST_STRING.substring(0, TEST_STRING.indexOf(" ") + 1).length(), consumed.length());
 assertEquals(TEST_STRING.substring(0, TEST_STRING.indexOf(" ") + 1), consumed);
 }

 /**
 * Test method: consumeLetterThenDigitSequence()
 */
 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeLetterThenDigitSequence();
 assertEquals(TEST_STRING.substring(0, TEST_STRING.indexOf(" ") + 3).length(), consumed.length());
 assertEquals(TEST_STRING.substring(0, TEST_STRING.indexOf(" ") + 3), consumed);
 }

 /**
 * Test method: consumeHexSequence()
 */
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeHexSequence();
 assertEquals(TEST_STRING.substring(0, TEST_STRING.indexOf(" ")).length(), consumed.length());
 assertEquals(TEST_STRING.substring(0, TEST_STRING.indexOf(" ")), consumed);
 }

 /**
 * Test method: consumeDigitSequence()
 */
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.consumeDigitSequence();
 assertEquals(TEST_STRING.substring(TEST_STRING.indexOf(" ") + 1).length(), consumed.length());
 assertEquals(TEST_STRING.substring(TEST_STRING.indexOf(" ") + 1), consumed);
 }

 /**
 * Test method: matches()
 */
 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matches(TEST_STRING.charAt(0)));
 assertFalse(reader.matches(TEST_STRING.charAt(1)));
 }

 /**
 * Test method: matches() for CharSequence
 */
 @Test
 public void testMatchesCharSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matches(TEST_CHAR_SEQUENCE));
 assertFalse(reader.matches(TEST_CHAR_SEQUENCE + "more"));
 }

 /**
 * Test method: matchesIgnoreCase() for CharSequence
 */
 @Test
 public void testMatchesIgnoreCaseCharSequence() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matchesIgnoreCase(TEST_CHAR_SEQUENCE));
 assertFalse(reader.matchesIgnoreCase(TEST_CHAR_SEQUENCE + "more"));
 }

 /**
 * Test method: matchesAny()
 */
 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matchesAny(TEST_CHAR));
 assertFalse(reader.matchesAny(TEST_CHAR, 'b'));
 }

 /**
 * Test method: matchesAnySorted()
 */
 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matchesAnySorted(TEST_CHAR_ARRAY));
 assertFalse(reader.matchesAnySorted(new char[]{'b'}));
 }

 /**
 * Test method: matchesLetter()
 */
 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter(Character.MIN_VALUE));
 }

 /**
 * Test method: matchesDigit()
 */
 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit(Character.MIN_VALUE));
 }

 /**
 * Test method: matchConsume()
 */
 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.matchConsume(TEST_CHAR_SEQUENCE);
 assertEquals(TEST_CHAR_SEQUENCE.length(), consumed.length());
 assertEquals(TEST_STRING.substring(0, TEST_CHAR_SEQUENCE.length()), consumed);
 }

 /**
 * Test method: matchConsumeIgnoreCase()
 */
 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 String consumed = reader.matchConsumeIgnoreCase(TEST_CHAR_SEQUENCE);
 assertEquals(TEST_CHAR_SEQUENCE.length(), consumed.length());
 assertEquals(TEST_STRING.substring(0, TEST_CHAR_SEQUENCE.length()).toLowerCase(), consumed.toLowerCase());
 }

 /**
 * Test method: containsIgnoreCase()
 */
 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.containsIgnoreCase(TEST_CHAR_SEQUENCE));
 assertTrue(reader.containsIgnoreCase(TEST_CHAR_SEQUENCE.toUpperCase()));
 assertFalse(reader.containsIgnoreCase("testOther"));
 }

 /**
 * Test method: toString()
 */
 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertEquals(TEST_STRING.substring(reader.pos(), TEST_STRING.length() - reader.pos()), reader.toString());
 reader.consumeToEnd();
 assertEquals(TEST_STRING, reader.toString());
 }

 /**
 * Test method: cacheString()
 */
 @Test
 public void testCacheString() {
 CharacterReader reader = new CharacterReader(TEST_LONG_STRING);
 String cached = reader.cacheString(0, TEST_LONG_STRING.length());
 assertEquals(TEST_LONG_STRING.length(), cached.length());
 assertEquals(TEST_LONG_STRING, cached);
 cached = reader.cacheString(0, TEST_LONG_STRING.length() - 1);
 assertEquals(TEST_LONG_STRING.length() - 1, cached.length());
 assertNotSame(cached, TEST_LONG_STRING.substring(0, TEST_LONG_STRING.length() - 1));
 cached = reader.cacheString(TEST_LONG_STRING.length() - 1, TEST_LONG_STRING.length());
 assertEquals(1, cached.length());
 assertEquals(TEST_LONG_STRING.charAt(TEST_LONG_STRING.length() - 1), cached.charAt(0));
 }

 /**
 * Test method: rangeEquals()
 */
 @Test
 public void testRangeEquals() {
 CharacterReader reader = new CharacterReader(TEST_STRING);
 assertTrue(reader.rangeEquals(0, TEST_STRING.length(), TEST_STRING));
 reader.consumeTo(TEST_CHAR_SEQUENCE);
 assertFalse(reader.rangeEquals(0, TEST_STRING.length(), TEST_STRING));
 reader.consumeToEnd();
 assertTrue(reader.rangeEquals(0, TEST_STRING.length(), TEST_STRING));
 }
}