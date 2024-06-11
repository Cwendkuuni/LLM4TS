package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

/**
 * CharacterReaderTest class for testing the CharacterReader class.
 */
public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");

 Assert.assertEquals(0, reader.pos());
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testReaderWithCharacters() {
 CharacterReader reader = new CharacterReader("Hello World");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 Assert.assertEquals('H', reader.current());
 Assert.assertEquals('H', reader.consume());

 Assert.assertEquals(1, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testReaderMarkAndRewind() {
 CharacterReader reader = new CharacterReader("Hello World");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 reader.mark();
 reader.consume();
 reader.advance();
 reader.advance();

 Assert.assertEquals(2, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 reader.rewindToMark();

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testReaderConsumeToChar() {
 CharacterReader reader = new CharacterReader("Hello World");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 String consumed = reader.consumeTo('l');

 Assert.assertEquals(2, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 Assert.assertEquals("He", consumed);
 }

 @Test
 public void testReaderConsumeToString() {
 CharacterReader reader = new CharacterReader("Hello World");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 String consumed = reader.consumeTo("ll");

 Assert.assertEquals(4, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 Assert.assertEquals("Hello", consumed);
 }

 @Test
 public void testReaderConsumeToAny() {
 CharacterReader reader = new CharacterReader("Hello World & < null");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 String consumed = reader.consumeToAny('&', '<', (char) 0);

 Assert.assertEquals(4, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 Assert.assertEquals("Hello", consumed);
 }

 @Test
 public void testReaderConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader("Hello World & < null");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 String consumed = reader.consumeToAnySorted('&', '<', (char) 0);

 Assert.assertEquals(4, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 Assert.assertEquals("Hello", consumed);
 }

 @Test
 public void testReaderConsumeData() {
 CharacterReader reader = new CharacterReader("Hello World & < null");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 String consumed = reader.consumeData();

 Assert.assertEquals(7, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 Assert.assertEquals("Hello World & < null", consumed);
 }

 @Test
 public void testReaderConsumeTagName() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body></body></html>");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 String consumed = reader.consumeTagName();

 Assert.assertEquals(6, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 Assert.assertEquals("html", consumed);
 }

 @Test(expected = IllegalStateException.class)
 public void testReaderConsumeAfterEnd() {
 CharacterReader reader = new CharacterReader("Hello World");

 Assert.assertEquals(6, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 String consumed = reader.consumeToEnd();

 Assert.assertEquals(11, reader.pos());
 Assert.assertTrue(reader.isEmpty());

 // Consume after end should throw an exception
 reader.consume();
 }

 @Test
 public void testReaderConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("Hello World");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 String consumed = reader.consumeLetterSequence();

 Assert.assertEquals(6, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 Assert.assertEquals("Hello", consumed);
 }

 @Test
 public void testReaderConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("Hello12345");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 String consumed = reader.consumeLetterThenDigitSequence();

 Assert.assertEquals(6, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 Assert.assertEquals("Hello12345", consumed);
 }

 @Test
 public void testReaderConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("Hello &x0A&x0B&x0C World");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 String consumed = reader.consumeHexSequence();

 Assert.assertEquals(11, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 Assert.assertEquals("Hello &amp;x0A&amp;x0B&amp;x0C", consumed);
 }

 @Test
 public void testReaderConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("Hello1234567");

 Assert.assertEquals(0, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 String consumed = reader.consumeDigitSequence();

 Assert.assertEquals(7, reader.pos());
 Assert.assertFalse(reader.isEmpty());

 Assert.assertEquals("1234567", consumed);
 }

 @Test
 public void testReaderMatches() {
 CharacterReader reader = new CharacterReader("Hello World");

 Assert.assertTrue(reader.matches('o'));
 Assert.assertTrue(reader.matches("ld"));
 }

 @Test
 public void testReaderMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("Hello World");

 Assert.assertTrue(reader.matchesIgnoreCase("ld"));
 Assert.assertTrue(reader.matchesIgnoreCase("ld"));
 }

 @Test
 public void testReaderMatchesAny() {
 CharacterReader reader = new CharacterReader("Hello World");

 Assert.assertTrue(reader.matchesAny('o', 'l'));
 Assert.assertTrue(reader.matchesAny('l', 'o'));
 }

 @Test
 public void testReaderMatchesAnySorted() {
 CharacterReader reader = new CharacterReader("Hello World");

 Assert.assertTrue(reader.matchesAnySorted('o', 'l'));
 Assert.assertTrue(reader.matchesAnySorted('l', 'o'));
 }

 @Test
 public void testReaderMatchesLetter() {
 CharacterReader reader = new CharacterReader("Hello World");

 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testReaderMatchesDigit() {
 CharacterReader reader = new CharacterReader("Hello1234567");

 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testReaderMatchConsume() {
 CharacterReader reader = new CharacterReader("Hello World");

 Assert.assertTrue(reader.matchConsume("ld"));
 }

 @Test
 public void testReaderMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader("Hello World");

 Assert.assertTrue(reader.matchConsumeIgnoreCase("ld"));
 }

 @Test
 public void testReaderContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body></body></html>");

 Assert.assertTrue(reader.containsIgnoreCase("title"));
 Assert.assertTrue(reader.containsIgnoreCase("test"));
 }
}