package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 // Test constructor
 @Test
 public void testCharacterReaderConstructor() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 }

 // Test isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader characterReader = new CharacterReader("");
 Assert.assertTrue(characterReader.isEmpty());

 CharacterReader characterReader1 = new CharacterReader("Hello World");
 Assert.assertFalse(characterReader1.isEmpty());
 }

 // Test pos method
 @Test
 public void testPos() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertEquals(0, characterReader.pos());

 characterReader.consume();
 characterReader.consume();
 Assert.assertEquals(2, characterReader.pos());
 }

 // Test current method
 @Test
 public void testCurrent() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertEquals('H', characterReader.current());

 characterReader.consume();
 Assert.assertEquals('e', characterReader.current());

 characterReader.consume();
 Assert.assertEquals('l', characterReader.current());

 characterReader.consume();
 Assert.assertEquals('l', characterReader.current());

 characterReader.consume();
 Assert.assertEquals('o', characterReader.current());

 characterReader.consume();
 Assert.assertEquals(' ', characterReader.current());

 characterReader.consume();
 Assert.assertEquals('W', characterReader.current());
 }

 // Test consume method
 @Test
 public void testConsume() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 characterReader.consume();
 Assert.assertEquals('e', characterReader.current());

 characterReader.consume();
 Assert.assertEquals('l', characterReader.current());

 characterReader.consume();
 Assert.assertEquals('l', characterReader.current());

 characterReader.consume();
 Assert.assertEquals('o', characterReader.current());

 characterReader.consume();
 Assert.assertEquals(' ', characterReader.current());

 characterReader.consume();
 Assert.assertEquals('W', characterReader.current());

 characterReader.consume();
 Assert.assertEquals(' ', characterReader.current());

 characterReader.consume();
 Assert.assertEquals(CharacterReader.EOF, characterReader.current());
 }

 // Test unconsume method
 @Test
 public void testUnconsume() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 characterReader.consume();
 characterReader.consume();
 characterReader.consume();
 characterReader.unconsume();

 Assert.assertEquals('l', characterReader.current());
 }

 // Test advance method
 @Test
 public void testAdvance() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 characterReader.advance();
 Assert.assertEquals('e', characterReader.current());

 characterReader.advance();
 Assert.assertEquals('l', characterReader.current());

 characterReader.advance();
 Assert.assertEquals('l', characterReader.current());

 characterReader.advance();
 Assert.assertEquals('o', characterReader.current());

 characterReader.advance();
 Assert.assertEquals(' ', characterReader.current());

 characterReader.advance();
 Assert.assertEquals('W', characterReader.current());

 characterReader.advance();
 Assert.assertEquals(' ', characterReader.current());

 characterReader.advance();
 Assert.assertEquals(CharacterReader.EOF, characterReader.current());
 }

 // Test mark method
 @Test
 public void testMark() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 characterReader.mark();
 characterReader.consume();
 characterReader.consume();
 characterReader.rewindToMark();

 Assert.assertEquals('H', characterReader.current());
 }

 // Test rewindToMark method
 @Test
 public void testRewindToMark() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 characterReader.consume();
 characterReader.consume();
 characterReader.mark();
 characterReader.consume();
 characterReader.rewindToMark();

 Assert.assertEquals('l', characterReader.current());
 }

 // Test consumeAsString method
 @Test
 public void testConsumeAsString() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 String consumed = characterReader.consumeAsString();
 Assert.assertEquals("Hello", consumed);
 }

 // Test nextIndexOf method with char
 @Test
 public void testNextIndexOfWithChar() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 int index = characterReader.nextIndexOf('l');
 Assert.assertEquals(3, index);

 index = characterReader.nextIndexOf('x');
 Assert.assertEquals(-1, index);
 }

 // Test nextIndexOf method with CharSequence
 @Test
 public void testNextIndexOfWithCharSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 String sequence = "ll";
 int index = characterReader.nextIndexOf(sequence);
 Assert.assertEquals(3, index);

 sequence = "xyz";
 index = characterReader.nextIndexOf(sequence);
 Assert.assertEquals(-1, index);
 }

 // Test consumeTo method with char
 @Test
 public void testConsumeToWithChar() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 String consumed = characterReader.consumeTo('l');
 Assert.assertEquals("He", consumed);

 consumed = characterReader.consumeTo('l');
 Assert.assertEquals("Hell", consumed);

 consumed = characterReader.consumeTo('l');
 Assert.assertEquals("Hello World", consumed);
 }

 // Test consumeTo method with CharSequence
 @Test
 public void testConsumeToWithCharSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 String consumed = characterReader.consumeTo("ll");
 Assert.assertEquals("Hello World", consumed);

 consumed = characterReader.consumeTo("ll");
 Assert.assertEquals("Hello World", consumed);

 consumed = characterReader.consumeTo("World");
 Assert.assertEquals("Hello World", consumed);
 }

 // Test consumeToAny method with single char
 @Test
 public void testConsumeToAnyWithSingleChar() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 String consumed = characterReader.consumeToAny('l');
 Assert.assertEquals("Hello", consumed);
 }

 // Test consumeToAny method with multiple chars
 @Test
 public void testConsumeToAnyWithMultipleChars() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 String consumed = characterReader.consumeToAny(new char[]{'l', 'o'});
 Assert.assertEquals("Hello World", consumed);
 }

 // Test consumeToAnySorted method with multiple chars
 @Test
 public void testConsumeToAnySortedWithMultipleChars() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 String consumed = characterReader.consumeToAnySorted(new char[]{'l', 'o'});
 Assert.assertEquals("Hello World", consumed);
 }

 // Test consumeData method
 @Test
 public void testConsumeData() {
 CharacterReader characterReader = new CharacterReader("& < Hello World");
 String consumed = characterReader.consumeData();
 Assert.assertEquals("& <", consumed);
 }

 // Test consumeTagName method
 @Test
 public void testConsumeTagName() {
 CharacterReader characterReader = new CharacterReader("&lt;title&gt;Hello World");
 String consumed = characterReader.consumeTagName();
 Assert.assertEquals("<title>", consumed);
 }

 // Test consumeToEnd method
 @Test
 public void testConsumeToEnd() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 String consumed = characterReader.consumeToEnd();
 Assert.assertEquals("Hello World", consumed);
 }

 // Test consumeLetterSequence method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 String consumed = characterReader.consumeLetterSequence();
 Assert.assertEquals("Hello", consumed);
 }

 // Test consumeLetterThenDigitSequence method
 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader characterReader = new CharacterReader("Hello123 World");
 String consumed = characterReader.consumeLetterThenDigitSequence();
 Assert.assertEquals("Hello123", consumed);
 }

 // Test consumeHexSequence method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader characterReader = new CharacterReader("&x0A");
 String consumed = characterReader.consumeHexSequence();
 Assert.assertEquals("x0A", consumed);
 }

 // Test consumeDigitSequence method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader characterReader = new CharacterReader("123456");
 String consumed = characterReader.consumeDigitSequence();
 Assert.assertEquals("123456", consumed);
 }

 // Test matches method with char
 @Test
 public void testMatchesWithChar() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matches('l'));
 Assert.assertFalse(characterReader.matches('x'));
 }

 // Test matches method with CharSequence
 @Test
 public void testMatchesWithCharSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matches("ll"));
 Assert.assertFalse(characterReader.matches("xyz"));
 }

 // Test matchesIgnoreCase method with CharSequence
 @Test
 public void testMatchesIgnoreCaseWithCharSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matchesIgnoreCase("LL"));
 Assert.assertFalse(characterReader.matchesIgnoreCase("xyz"));
 }

 // Test matchesAny method with single char
 @Test
 public void testMatchesAnyWithSingleChar() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matchesAny('l'));
 }

 // Test matchesAny method with multiple chars
 @Test
 public void testMatchesAnyWithMultipleChars() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matchesAny(new char[]{'l', 'o'}));
 }

 // Test matchesLetter method
 @Test
 public void testMatchesLetter() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matchesLetter());
 }

 // Test matchesDigit method
 @Test
 public void testMatchesDigit() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matchesDigit());
 }

 // Test matchConsume method with CharSequence
 @Test
 public void testMatchConsumeWithCharSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matchConsume("ll"));
 }

 // Test matchConsumeIgnoreCase method with CharSequence
 @Test
 public void testMatchConsumeIgnoreCaseWithCharSequence() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertTrue(characterReader.matchConsumeIgnoreCase("LL"));
 }

 // Test containsIgnoreCase method with CharSequence
 @Test
 public void testContainsIgnoreCaseWithCharSequence() {
 CharacterReader characterReader = new CharacterReader("<html><head><title>Hello World</title>");
 Assert.assertTrue(characterReader.containsIgnoreCase("title"));
 Assert.assertTrue(characterReader.containsIgnoreCase("Title"));
 }

 // Test toString method
 @Test
 public void testToString() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 Assert.assertEquals("Hello World", characterReader.toString());
 }

 // Test cacheString method
 @Test
 public void testCacheString() {
 CharacterReader characterReader = new CharacterReader("Hello World");
 String cachedString = characterReader.cacheString(1, 5);
 Assert.assertEquals("Hello", cachedString);
 }
}