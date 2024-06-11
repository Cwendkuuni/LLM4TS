package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;

import java.util.Arrays;
import java.util.Locale;

public class CharacterReaderTest {

 private CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] input = "test<tag>test".toCharArray();
 characterReader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(characterReader.isEmpty());
 characterReader.consume();
 assertFalse(characterReader.isEmpty());
 characterReader.unconsume();
 assertTrue(characterReader.isEmpty());
 }

 @Test
 public void testPos() {
 int expectedPos = 0;
 assertEquals(expectedPos, characterReader.pos());
 characterReader.consume();
 expectedPos++;
 assertEquals(expectedPos, characterReader.pos());
 }

 @Test
 public void testCurrent() {
 char expectedCurrent = 't';
 assertEquals(expectedCurrent, characterReader.current());
 characterReader.consume();
 expectedCurrent = 'e';
 assertEquals(expectedCurrent, characterReader.current());
 }

 @Test
 public void testConsume() {
 char expectedCurrent = 't';
 characterReader.consume();
 assertEquals(expectedCurrent, characterReader.current());
 assertEquals('e', characterReader.consume());
 }

 @Test
 public void testUnconsume() {
 char expectedCurrent = 't';
 characterReader.consume();
 characterReader.unconsume();
 assertEquals(expectedCurrent, characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 int expectedPos = 1;
 assertEquals(expectedPos, characterReader.pos());
 }

 @Test
 public void testMark() {
 int markPos = 0;
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();
 int expectedPos = markPos;
 assertEquals(expectedPos, characterReader.pos());
 }

 @Test
 public void testConsumeAsString() {
 String expected = "test";
 assertEquals(expected, characterReader.consumeAsString());
 characterReader.consume();
 expected = "tag";
 assertEquals(expected, characterReader.consumeAsString());
 }

 @Test
 public void testNextIndexOf() {
 char expectedChar = 't';
 int index = characterReader.nextIndexOf(expectedChar);
 assertEquals(-1, index);
 characterReader.consume();
 index = characterReader.nextIndexOf(expectedChar);
 assertEquals(0, index);
 }

 @Test
 public void testNextIndexOfSequences() {
 String expectedSequence = "tag";
 int index = characterReader.nextIndexOf(expectedSequence);
 assertEquals(-1, index);
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 index = characterReader.nextIndexOf(expectedSequence);
 assertEquals(4, index);
 }

 @Test
 public void testConsumeTo() {
 String expected = "tag";
 String result = characterReader.consumeTo(expected.charAt(0));
 assertEquals(expected, result);
 characterReader.rewindToMark();
 result = characterReader.consumeTo(expected);
 assertEquals(expected, result);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "test<tag>test";
 String result = characterReader.consumeToEnd();
 assertEquals(expected, result);
 }

 @Test
 public void testConsumeToAny() {
 char[] expectedChars = {'t', 'e', '<'};
 String result = characterReader.consumeToAny(expectedChars);
 assertEquals("test<", result);
 }

 @Test
 public void testConsumeToAnySorted() {
 char[] expectedChars = {'<', 't', 'e'};
 String result = characterReader.consumeToAnySorted(expectedChars);
 assertEquals("test<", result);
 }

 @Test
 public void testConsumeData() {
 String expected = "test<tag>test";
 String result = characterReader.consumeData();
 assertEquals("test<", result);
 }

 @Test
 public void testConsumeTagName() {
 String expected = "tag";
 String result = characterReader.consumeTagName();
 assertEquals(expected, result);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "test";
 String result = characterReader.consumeLetterSequence();
 assertEquals(expected, result);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 String expected = "test123";
 String result = characterReader.consumeLetterThenDigitSequence();
 assertEquals(expected, result);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "test#657665";
 String result = characterReader.consumeHexSequence();
 assertEquals(expected, result);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "test123";
 String result = characterReader.consumeDigitSequence();
 assertEquals(expected, result);
 }

 @Test
 public void testMatches() {
 char expectedChar = 't';
 assertTrue(characterReader.matches(expectedChar));
 characterReader.consume();
 assertFalse(characterReader.matches(expectedChar));
 }

 @Test
 public void testMatchesSequences() {
 String expectedSequence = "tag";
 assertTrue(characterReader.matches(expectedSequence));
 characterReader.consumeTagName();
 assertFalse(characterReader.matches(expectedSequence));
 }

 @Test
 public void testMatchesAny() {
 char[] expectedChars = {'t', 'e'};
 assertTrue(characterReader.matchesAny(expectedChars[0]));
 assertTrue(characterReader.matchesAny(expectedChars[1]));
 assertTrue(characterReader.matchesAnySorted(expectedChars));
 }

 @Test
 public void testMatchesAnySorted() {
 char[] expectedChars = {'t', 'e'};
 assertTrue(characterReader.matchesAnySorted(expectedChars));
 }

 @Test
 public void testMatchesLetter() {
 char expectedChar = 't';
 assertTrue(characterReader.matchesLetter());
 assertFalse(characterReader.matchesLetter());
 characterReader.consumeLetter();
 assertTrue(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 char expectedChar = '1';
 assertTrue(characterReader.matchesDigit());
 assertFalse(characterReader.matchesDigit());
 characterReader.consumeDigit();
 assertTrue(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String expectedSequence = "tag";
 assertTrue(characterReader.matchConsume(expectedSequence));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expectedSequence = "TAG";
 assertTrue(characterReader.matchConsumeIgnoreCase(expectedSequence));
 }

 @Test
 public void testContainsIgnoreCase() {
 String expectedSequence = "</title>";
 assertTrue(characterReader.containsIgnoreCase(expectedSequence));
 }

 @Test(expected = AssertionError.class)
 public void testToString() {
 characterReader.consumeAll();
 String result = characterReader.toString();
 fail("Expected an AssertionError but got " + result);
 }

 @Test
 public void testCacheString() {
 char[] input = "test<tag>test".toCharArray();
 CharacterReader characterReader = new CharacterReader(new String(input));
 String cacheString = characterReader.cacheString(0, 4);
 assertEquals("test", cacheString);
 characterReader.consume(4);
 cacheString = characterReader.cacheString(0, 4);
 assertEquals("tag", cacheString);
 }

 @Test
 public void testRangeEquals() {
 char[] input = "test<tag>test".toCharArray();
 CharacterReader characterReader = new CharacterReader(new String(input));
 assertTrue(characterReader.rangeEquals(0, 4, "test"));
 characterReader.consume(4);
 assertTrue(characterReader.rangeEquals(0, 4, "tag"));
 }
}