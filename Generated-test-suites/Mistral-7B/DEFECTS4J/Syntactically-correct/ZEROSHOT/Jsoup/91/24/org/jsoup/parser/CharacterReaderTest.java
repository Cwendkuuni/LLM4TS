package org.jsoup.parser;

import org.junit.Assert;import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.StringReader;

/**
 * Test class for CharacterReader
 */
public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 reader = new CharacterReader(new StringReader("test string"));
 }

 /**
 * Test constructor CharacterReader(Reader input)
 */
 @Test
 public void testConstructorReaderInput() {
 reader = new CharacterReader(new StringReader("test string"));
 Assert.assertThat(reader, notNullValue());
 }

 /**
 * Test constructor CharacterReader(Reader input, int sz)
 */
 @Test
 public void testConstructorReaderInputInt() {
 reader = new CharacterReader(new StringReader("test string"), 10);
 Assert.assertThat(reader, notNullValue());
 }

 /**
 * Test constructor CharacterReader(String input)
 */
 @Test
 public void testConstructorString() {
 reader = new CharacterReader("test string");
 Assert.assertThat(reader, notNullValue());
 }

 /**
 * Test method pos
 */
 @Test
 public void testPos() {
 int pos = reader.pos();
 Assert.assertEquals("Incorrect initial position", 0, pos);
 }

 /**
 * Test method isEmpty
 */
 @Test
 public void testIsEmpty() {
 Assert.assertThat(reader.isEmpty(), is(false));
 reader.consume();
 reader.consume();
 Assert.assertThat(reader.isEmpty(), is(true));
 }

 /**
 * Test method current
 */
 @Test
 public void testCurrent() {
 char c = reader.current();
 Assert.assertEquals("Incorrect initial character", 't', c);
 }

 /**
 * Test method consume
 */
 @Test
 public void testConsume() {
 char c = reader.consume();
 Assert.assertEquals("Incorrect character consumed", 't', c);
 int pos = reader.pos();
 Assert.assertEquals("Incorrect position after consume", 2, pos);
 }

 /**
 * Test method unconsume
 */
 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 char c = reader.current();
 Assert.assertEquals("Incorrect character after unconsume", 't', c);
 }

 /**
 * Test method advance
 */
 @Test
 public void testAdvance() {
 reader.advance();
 char c = reader.current();
 Assert.assertEquals("Incorrect character after advance", 'e', c);
 }

 /**
 * Test method mark
 */
 @Test
 public void testMark() {
 reader.mark();
 char c = reader.current();
 reader.rewindToMark();
 char c1 = reader.current();
 Assert.assertEquals("Incorrect character after mark and rewind", 't', c);
 Assert.assertEquals("Incorrect character after mark and rewind", 't', c1);
 }

 /**
 * Test method rewindToMark
 */
 @Test
 public void testRewindToMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 char c = reader.current();
 Assert.assertEquals("Incorrect character after mark and rewind", 't', c);
 }

 /**
 * Test method nextIndexOf
 */
 @Test
 public void testNextIndexOf() {
 Assert.assertEquals("Incorrect indexOf 's'", 3, reader.nextIndexOf('s'));
 Assert.assertEquals("Incorrect indexOf 'z'", -1, reader.nextIndexOf('z'));
 }

 /**
 * Test method nextIndexOf
 */
 @Test
 public void testNextIndexOfCharSequence() {
 CharSequence seq = "hello";
 Assert.assertEquals("Incorrect indexOf 'hello'", 4, reader.nextIndexOf(seq));
 Assert.assertEquals("Incorrect indexOf 'world'", -1, reader.nextIndexOf("world"));
 }

 /**
 * Test method consumeTo
 */
 @Test
 public void testConsumeTo() {
 String consumed = reader.consumeTo('s');
 Assert.assertEquals("Incorrect consumed string", "est string", consumed);
 int pos = reader.pos();
 Assert.assertEquals("Incorrect position after consumeTo", 7, pos);
 }

 /**
 * Test method consumeTo
 */
 @Test
 public void testConsumeToString() {
 String consumed = reader.consumeTo("hello");
 Assert.assertEquals("Incorrect consumed string", "test stringhel", consumed);
 int pos = reader.pos();
 Assert.assertEquals("Incorrect position after consumeTo", 10, pos);
 }

 /**
 * Test method consumeToAny
 */
 @Test
 public void testConsumeToAny() {
 String consumed = reader.consumeToAny('s', 'd');
 Assert.assertEquals("Incorrect consumed string", "est string", consumed);
 int pos = reader.pos();
 Assert.assertEquals("Incorrect position after consumeToAny", 7, pos);
 }

 /**
 * Test method consumeToAnySorted
 */
 @Test
 public void testConsumeToAnySorted() {
 String consumed = reader.consumeToAnySorted('d', 's');
 Assert.assertEquals("Incorrect consumed string", "est string", consumed);
 int pos = reader.pos();
 Assert.assertEquals("Incorrect position after consumeToAnySorted", 7, pos);
 }

 /**
 * Test method consumeData
 */
 @Test
 public void testConsumeData() {
 String consumed = reader.consumeData();
 Assert.assertEquals("Incorrect consumed string", "&<null", consumed);
 int pos = reader.pos();
 Assert.assertEquals("Incorrect position after consumeData", 4, pos);
 }

 /**
 * Test method consumeTagName
 */
 @Test
 public void testConsumeTagName() {
 String consumed = reader.consumeTagName();
 Assert.assertEquals("Incorrect consumed string", "test", consumed);
 int pos = reader.pos();
 Assert.assertEquals("Incorrect position after consumeTagName", 5, pos);
 }

 /**
 * Test method consumeToEnd
 */
 @Test
 public void testConsumeToEnd() {
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("Incorrect consumed string", "test string", consumed);
 int pos = reader.pos();
 Assert.assertEquals("Incorrect position after consumeToEnd", 13, pos);
 }

 /**
 * Test method consumeLetterSequence
 */
 @Test
 public void testConsumeLetterSequence() {
 String consumed = reader.consumeLetterSequence();
 Assert.assertEquals("Incorrect consumed string", "est", consumed);
 int pos = reader.pos();
 Assert.assertEquals("Incorrect position after consumeLetterSequence", 5, pos);
 }

 /**
 * Test method consumeLetterThenDigitSequence
 */
 @Test
 public void testConsumeLetterThenDigitSequence() {
 String consumed = reader.consumeLetterThenDigitSequence();
 Assert.assertEquals("Incorrect consumed string", "est1", consumed);
 int pos = reader.pos();
 Assert.assertEquals("Incorrect position after consumeLetterThenDigitSequence", 7, pos);
 }

 /**
 * Test method consumeHexSequence
 */
 @Test
 public void testConsumeHexSequence() {
 String consumed = reader.consumeHexSequence();
 Assert.assertEquals("Incorrect consumed string", "est%", consumed);
 int pos = reader.pos();
 Assert.assertEquals("Incorrect position after consumeHexSequence", 6, pos);
 }

 /**
 * Test method consumeDigitSequence
 */
 @Test
 public void testConsumeDigitSequence() {
 String consumed = reader.consumeDigitSequence();
 Assert.assertEquals("Incorrect consumed string", "est1", consumed);
 int pos = reader.pos();
 Assert.assertEquals("Incorrect position after consumeDigitSequence", 6, pos);
 }

 /**
 * Test method matches
 */
 @Test
 public void testMatches() {
 Assert.assertEquals("Incorrect match 't'", true, reader.matches('t'));
 Assert.assertEquals("Incorrect match 's'", false, reader.matches('s'));
 }

 /**
 * Test method matches
 */
 @Test
 public void testMatchesString() {
 Assert.assertEquals("Incorrect match 'test'", true, reader.matches("test"));
 Assert.assertEquals("Incorrect match 'test1'", false, reader.matches("test1"));
 }

 /**
 * Test method matchesIgnoreCase
 */
 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertEquals("Incorrect match 'Test'", true, reader.matchesIgnoreCase("Test"));
 Assert.assertEquals("Incorrect match 'test1'", false, reader.matchesIgnoreCase("test1"));
 }

 /**
 * Test method matchesAny
 */
 @Test
 public void testMatchesAny() {
 Assert.assertEquals("Incorrect match 's'", true, reader.matchesAny('s'));
 Assert.assertEquals("Incorrect match 'd'", false, reader.matchesAny('d'));
 }

 /**
 * Test method matchesAnySorted
 */
 @Test
 public void testMatchesAnySorted() {
 char[] seq = {'d', 's'};
 Assert.assertEquals("Incorrect match 'd' sorted", true, reader.matchesAnySorted(seq));
 }

 /**
 * Test method matchesLetter
 */
 @Test
 public void testMatchesLetter() {
 Assert.assertEquals("Incorrect match letter", true, reader.matchesLetter());
 }

 /**
 * Test method matchesDigit
 */
 @Test
 public void testMatchesDigit() {
 Assert.assertEquals("Incorrect match digit", true, reader.matchesDigit());
 }

 /**
 * Test method matchConsume
 */
 @Test
 public void testMatchConsume() {
 String consumed = reader.matchConsume("test");
 int pos = reader.pos();
 Assert.assertEquals("Incorrect consumed string", "test", consumed);
 Assert.assertEquals("Incorrect position after matchConsume", 5, pos);
 }

 /**
 * Test method matchConsumeIgnoreCase
 */
 @Test
 public void testMatchConsumeIgnoreCase() {
 String consumed = reader.matchConsumeIgnoreCase("Test");
 int pos = reader.pos();
 Assert.assertEquals("Incorrect consumed string", "test", consumed);
 Assert.assertEquals("Incorrect position after matchConsumeIgnoreCase", 5, pos);
 }

 /**
 * Test method containsIgnoreCase
 */
 @Test
 public void testContainsIgnoreCase() {
 Assert.assertEquals("Incorrect containsIgnoreCase 'hello'", true, reader.containsIgnoreCase("hello"));
 Assert.assertEquals("Incorrect containsIgnoreCase 'world'", false, reader.containsIgnoreCase("world"));
 }
}