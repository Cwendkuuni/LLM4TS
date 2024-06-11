package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for CharacterReader.
 */
public class CharacterReaderTest {

 private CharacterReader reader;

 @Test
 public void testEmptyReader() {
 reader = new CharacterReader("");
 // test isEmpty
 Assert.assertTrue(reader.isEmpty());
 // test current, consume, and isEmpty
 Assert.assertEquals(-1, reader.current());
 Assert.assertEquals(-1, reader.consume());
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testSimpleReader() {
 reader = new CharacterReader("abcdef");
 // test pos, isEmpty, and current
 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 Assert.assertEquals('a', reader.current());
 // test consume
 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals(1, reader.pos());
 Assert.assertEquals('b', reader.current());
 // test consumeTo and consumeToEnd
 Assert.assertEquals("bc", reader.consumeTo('c'));
 Assert.assertEquals("def", reader.consumeToEnd());
 }

 @Test
 public void testMarkAndRewind() {
 reader = new CharacterReader("abcdef");
 // test mark and rewind
 reader.mark();
 reader.advance();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeSequence() {
 reader = new CharacterReader("abcd1234efgh");
 // test consumeLetterSequence
 Assert.assertEquals("cd", reader.consumeLetterSequence());
 // test consumeHexSequence
 Assert.assertEquals("12", reader.consumeHexSequence());
 // test consumeDigitSequence
 Assert.assertEquals("34", reader.consumeDigitSequence());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidCharacter() {
 reader = new CharacterReader("abçdef");
 // test consume an invalid character
 reader.consume();
 }

 @Test(expected = StringIndexOutOfBoundsException.class)
 public void testConsumePastEnd() {
 reader = new CharacterReader("abcdef");
 // test consume past end of input
 reader.advance();
 reader.advance();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 }

 @Test
 public void testMatching() {
 reader = new CharacterReader("abcdabcdabcdabcd");
 // test matches and matchConsume
 Assert.assertTrue(reader.matches('a'));
 Assert.assertTrue(reader.matches("ab"));
 Assert.assertTrue(reader.matchConsume("ab"));
 // test matchesIgnoreCase
 Assert.assertTrue(reader.matchesIgnoreCase("Ab"));
 }

 @Test
 public void testMismatched() {
 reader = new CharacterReader("abcdabcdabcdabcd");
 // test matches and matchConsume with mismatched input
 Assert.assertFalse(reader.matches('e'));
 Assert.assertFalse(reader.matchConsume("ef"));
 }

 @Test
 public void testContains() {
 reader = new CharacterReader("abcd<title>The Test</title>efgh");
 // test containsIgnoreCase
 Assert.assertTrue(reader.containsIgnoreCase("title"));
 }

 @Test
 public void testConsumeToSequence() {
 reader = new CharacterReader("abcd<title>The Test</title>efgh");
 // test consumeTo and consumeToAny
 Assert.assertEquals("title", reader.consumeTo("<title>"));
 Assert.assertEquals("The Test</title>", reader.consumeToAny('>'));
 }

}