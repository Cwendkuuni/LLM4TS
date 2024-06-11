package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderInstance() {
 CharacterReader reader = new CharacterReader("Hello World!");
 assertEquals(11, reader.length);
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testIsNotEmpty() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrentCharacter() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.advance();
 assertEquals('H', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEndOfString() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.consume();
 reader.consume();
 reader.consume();
 }

 @Test
 public void testConsumeSingleCharacter() {
 CharacterReader reader = new CharacterReader("Hello World");
 String consumed = reader.consumeAsString();
 assertEquals("H", consumed);
 }

 @Test
 public void testConsumeMultipleCharacters() {
 CharacterReader reader = new CharacterReader("Hello World");
 String consumed = reader.consumeAsString(3);
 assertEquals(" Hell", consumed);
 }

 @Test
 public void testConsumeStringWithMark() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.mark();
 reader.advance();
 String consumed = reader.consumeAsString();
 assertEquals("H", consumed);
 reader.rewindToMark();
 consumed = reader.consumeAsString();
 assertEquals("e", consumed);
 }

 @Test
 public void testConsumeToCharacter() {
 CharacterReader reader = new CharacterReader("Hello World");
 String consumed = reader.consumeTo('l');
 assertEquals("He", consumed);
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("Hello World");
 String consumed = reader.consumeToEnd();
 assertEquals("World", consumed);
 }

 @Test
 public void testConsumeToSequence() {
 CharacterReader reader = new CharacterReader("Hello World");
 String consumed = reader.consumeTo("llo");
 assertEquals("Hello", consumed);
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("Hello World");
 String consumed = reader.consumeToAny('l');
 assertEquals("Hello", consumed);
 }

 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader("Hello World");
 String consumed = reader.consumeToAnySorted(new char[]{'l', 'o'});
 assertEquals("Hello", consumed);
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&amp; <test> &lt; &gt;");
 String consumed = reader.consumeData();
 assertEquals("&amp; <test> &lt; &gt;", consumed);
 consumed = reader.consumeData();
 assertEquals("amp; test> lt; gt;", consumed);
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<title>Test</title>");
 String consumed = reader.consumeTagName();
 assertEquals("title", consumed);
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("Hello World");
 String consumed = reader.consumeLetterSequence();
 assertEquals("Hello", consumed);
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("Hello123 World456");
 String consumed = reader.consumeLetterThenDigitSequence();
 assertEquals("Hello123", consumed);
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("&#x61;&#x6C;&#x65;&#x6E;&#x63;&#x6F;&#x64;");
 String consumed = reader.consumeHexSequence();
 assertEquals("abcdef", consumed);
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 String consumed = reader.consumeDigitSequence();
 assertEquals("1234567890", consumed);
 }

 @Test
 public void testMatchesCharacter() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matches('H'));
 }

 @Test
 public void testMatchesString() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matches("Hel"));
 }

 @Test
 public void testMatchesStringIgnoreCase() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesIgnoreCase("hel"));
 }

 @Test
 public void testMatchesAnyChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesAny('l'));
 }

 @Test
 public void testMatchesAnyCharSorted() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesAnySorted(new char[]{'l', 'o'}));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("Hello123 World456");
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsumeString() {
 CharacterReader reader = new CharacterReader("Hello World");
 String consumed = reader.matchConsume("Hel");
 assertEquals("Hello", consumed);
 }

 @Test
 public void testMatchConsumeStringIgnoreCase() {
 CharacterReader reader = new CharacterReader("Hello World");
 String consumed = reader.matchConsumeIgnoreCase("hel");
 assertEquals("Hello", consumed);
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body><h1>Test</h1></body></html>");
 assertTrue(reader.containsIgnoreCase("/title"));
 }

 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals("World", reader.toString());
 }
}