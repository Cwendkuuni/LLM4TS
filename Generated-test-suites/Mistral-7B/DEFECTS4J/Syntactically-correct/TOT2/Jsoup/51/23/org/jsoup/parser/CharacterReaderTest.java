import org.junit.Test;
import org.jsoup.parser.CharacterReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderInstance() {
 CharacterReader reader = new CharacterReader("Test String");
 assertNotNull(reader);
 }

 @Test
 public void testEmptyCharacterReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrentCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals('a', reader.current());
 reader.consume();
 assertEquals('b', reader.current());
 }

 @Test
 public void testConsumeCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.consume();
 assertEquals('b', reader.current());
 }

 @Test
 public void testConsumeMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.consume(2);
 assertEquals('c', reader.current());
 }

 @Test
 public void testConsumeEndOfString() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.consumeToEnd();
 assertNull(reader.current());
 assertEquals("", reader.consumeAsString());
 }

 @Test
 public void testConsumeSubstring() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals("bc", reader.consumeTo("cd"));
 assertEquals("ab", reader.consumeTo("e"));
 }

 @Test
 public void testConsumeSubstringEmpty() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals("", reader.consumeTo(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConsumeSubstringNull() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.consumeTo(null);
 }

 @Test
 public void testConsumeSubstringNotPresent() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals("", reader.consumeTo("ef"));
 }

 @Test
 public void testConsumeSubstringWithMark() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.mark();
 reader.consume(1);
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testNextIndexOfSingleCharacter() {
 CharacterReader reader = new CharacterReader("abba");
 assertEquals(2, reader.nextIndexOf('b'));
 }

 @Test
 public void testNextIndexOfMultipleCharacters() {
 CharacterReader reader = new CharacterReader("abba");
 assertEquals(3, reader.nextIndexOf("ba"));
 }

 @Test
 public void testNextIndexOfEmptySequence() {
 CharacterReader reader = new CharacterReader("abba");
 assertEquals(-1, reader.nextIndexOf(""));
 }

 @Test
 public void testNextIndexOfNullSequence() {
 CharacterReader reader = new CharacterReader("abba");
 assertEquals(-1, reader.nextIndexOf(null));
 }

 @Test
 public void testConsumeToEndWithMark() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.mark();
 reader.consumeToEnd();
 assertNull(reader.current());
 assertEquals("", reader.consumeAsString());
 reader.rewindToMark();
 assertEquals("cd", reader.consumeToEnd());
 }

 @Test
 public void testConsumeToEndWithMarkTwice() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.mark();
 reader.consumeToEnd();
 reader.mark();
 reader.consumeToEnd();
 assertNull(reader.current());
 assertEquals("", reader.consumeAsString());
 reader.rewindToMark();
 assertEquals("cd", reader.consumeToEnd());
 reader.rewindToMark();
 assertEquals("ab", reader.consumeToEnd());
 }

 @Test
 public void testConsumeToEndWithMarkAndConsume() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.mark();
 reader.consume(1);
 reader.rewindToMark();
 assertEquals("bc", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("1A2B3C");
 assertEquals("A", reader.consumeLetterSequence());
 assertEquals("B", reader.consumeLetterSequence());
 assertEquals("C", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterSequenceEmpty() {
 CharacterReader reader = new CharacterReader("1A2B3C");
 assertEquals("", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("1A2B3C");
 assertEquals("A1", reader.consumeLetterThenDigitSequence());
 assertEquals("B2", reader.consumeLetterThenDigitSequence());
 assertEquals("C3", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequenceEmpty() {
 CharacterReader reader = new CharacterReader("1A2B3C");
 assertEquals("", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("1A2B3C%25");
 assertEquals("%", reader.current());
 assertEquals("25", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeHexSequenceEmpty() {
 CharacterReader reader = new CharacterReader("1A2B3C%25");
 assertEquals("", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1A2B3C%25");
 assertEquals("1", reader.consumeDigitSequence());
 assertEquals("2", reader.consumeDigitSequence());
 assertEquals("3", reader.consumeDigitSequence());
 assertEquals("", reader.consumeDigitSequence());
 }

 @Test
 public void testConsumeDigitSequenceEmpty() {
 CharacterReader reader = new CharacterReader("1A2B3C%25");
 assertEquals("", reader.consumeDigitSequence());
 }

 @Test
 public void testConsumeLetter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matches('a'));
 assertTrue(reader.matches('b'));
 assertFalse(reader.matches('c'));
 assertFalse(reader.matches('d'));
 }

 @Test
 public void testConsumeString() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matches("ab"));
 assertTrue(reader.matches("cd"));
 assertFalse(reader.matches("ef"));
 }

 @Test
 public void testConsumeStringIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesIgnoreCase("Ab"));
 assertTrue(reader.matchesIgnoreCase("cd"));
 assertFalse(reader.matchesIgnoreCase("EF"));
 }

 @Test
 public void testConsumeAnyCharacter() {
 CharacterReader reader = new CharacterReader("abcdABCD");
 assertTrue(reader.matchesAny('a'));
 assertTrue(reader.matchesAny('b'));
 assertTrue(reader.matchesAny('c'));
 assertTrue(reader.matchesAny('d'));
 assertTrue(reader.matchesAny('A'));
 assertTrue(reader.matchesAny('B'));
 }

 @Test
 public void testConsumeAnyCharacterEmpty() {
 CharacterReader reader = new CharacterReader("abcdABCD");
 assertFalse(reader.matchesAny('e'));
 }

 @Test
 public void testConsumeAnyCharacterSorted() {
 CharacterReader reader = new CharacterReader("abcdABCD");
 assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c', 'd', 'A', 'B'}));
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("& <null>");
 assertEquals("&", reader.consumeData());
 assertEquals("<", reader.consumeData());
 assertEquals("null", reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<title>Test</title>");
 assertEquals("/title", reader.consumeTagName());
 }

 @Test
 public void testCacheString() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals("bc", reader.cacheString(1, 2));
 assertEquals("abcd", reader.cacheString(0, 5));
 }

 @Test
 public void testRangeEquals() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.rangeEquals(0, 3, "bc"));
 }

 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals("abcd", reader.toString());
 }
}