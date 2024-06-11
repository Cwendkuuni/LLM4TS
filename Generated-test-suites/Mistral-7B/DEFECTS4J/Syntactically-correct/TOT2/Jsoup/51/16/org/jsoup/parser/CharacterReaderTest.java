package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 // Test constructor
 @Test
 public void testCharacterReaderConstructor() {
 CharacterReader reader = new CharacterReader("Hello World");
 }

 // Test isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 Assert.assertTrue(reader.isEmpty());

 CharacterReader reader1 = new CharacterReader("Hello World");
 Assert.assertFalse(reader1.isEmpty());
 }

 // Test pos() method
 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals(0, reader.pos());

 reader.advance();
 reader.advance();
 reader.advance();
 Assert.assertEquals(3, reader.pos());
 }

 // Test current() method
 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals('H', reader.current());
 reader.advance();
 Assert.assertEquals('e', reader.current());
 }

 // Test consume() method
 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals('H', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 // Test unconsume() method
 @Test
 public void testUnconsume() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('H', reader.current());
 }

 // Test mark() and rewindToMark() methods
 @Test
 public void testMarkAndRewindToMark() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.mark();
 reader.advance();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('H', reader.current());
 }

 // Test consumeAsString() method
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("Hello World");
 String str = reader.consumeAsString();
 Assert.assertEquals("Hello", str);
 }

 // Test nextIndexOf() method with single character
 @Test
 public void testNextIndexOfSingleCharacter() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals(4, reader.nextIndexOf('l'));
 }

 // Test nextIndexOf() method with multiple occurrences
 @Test
 public void testNextIndexOfMultipleOccurrences() {
 CharacterReader reader = new CharacterReader("Hello Hello World");
 Assert.assertEquals(4, reader.nextIndexOf('l'));
 Assert.assertEquals(11, reader.nextIndexOf('l'));
 }

 // Test nextIndexOf() method with no occurrences
 @Test
 public void testNextIndexOfNoOccurrences() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals(-1, reader.nextIndexOf('x'));
 }

 // Test consumeTo() method with single character
 @Test
 public void testConsumeToSingleCharacter() {
 CharacterReader reader = new CharacterReader("Hello World");
 String str = reader.consumeTo('l');
 Assert.assertEquals("Hello", str);
 }

 // Test consumeTo() method with multiple characters
 @Test
 public void testConsumeToMultipleCharacters() {
 CharacterReader reader = new CharacterReader("Hello World");
 String str = reader.consumeTo("ll");
 Assert.assertEquals("Helllo", str);
 }

 // Test consumeToAny() method with single character
 @Test
 public void testConsumeToAnySingleCharacter() {
 CharacterReader reader = new CharacterReader("Hello World");
 String str = reader.consumeToAny('l');
 Assert.assertEquals("Hello", str);
 }

 // Test consumeToAny() method with multiple characters
 @Test
 public void testConsumeToAnyMultipleCharacters() {
 CharacterReader reader = new CharacterReader("Hello World");
 String str = reader.consumeToAny(new char[]{'l', 'o'});
 Assert.assertEquals("Hello", str);
 }

 // Test consumeToEnd() method
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("Hello World");
 String str = reader.consumeToEnd();
 Assert.assertEquals("Hello World", str);
 }

 // Test matches() method with single character
 @Test
 public void testMatchesSingleCharacter() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matches('l'));
 }

 // Test matches() method with multiple characters
 @Test
 public void testMatchesMultipleCharacters() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertFalse(reader.matches("ll"));
 }

 // Test matches() method with empty string
 @Test
 public void testMatchesEmptyString() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertFalse(reader.matches(""));
 }

 // Test matchesIgnoreCase() method with lowercase characters
 @Test
 public void testMatchesIgnoreCaseLowercase() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matchesIgnoreCase("hello"));
 }

 // Test matchesIgnoreCase() method with uppercase characters
 @Test
 public void testMatchesIgnoreCaseUppercase() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matchesIgnoreCase("HELLO"));
 }

 // Test matchesAny() method with single character
 @Test
 public void testMatchesAnySingleCharacter() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matchesAny('l'));
 }

 // Test matchesAny() method with multiple characters
 @Test
 public void testMatchesAnyMultipleCharacters() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o'}));
 }

 // Test matchesAny() method with no matching characters
 @Test
 public void testMatchesAnyNoMatchingCharacters() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertFalse(reader.matchesAny(new char[]{'x', 'y'}));
 }

 // Test matchesLetter() method
 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matchesLetter());
 reader.advance();
 Assert.assertTrue(reader.matchesLetter());
 reader.advance();
 Assert.assertFalse(reader.matchesLetter());
 }

 // Test matchesDigit() method
 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertFalse(reader.matchesDigit());
 reader.advance();
 reader.advance();
 Assert.assertTrue(reader.matchesDigit());
 }

 // Test matchConsume() method with single character
 @Test
 public void testMatchConsumeSingleCharacter() {
 CharacterReader reader = new CharacterReader("Hello World");
 String str = reader.matchConsume("l");
 Assert.assertEquals("llo World", reader.toString());
 Assert.assertEquals("l", str);
 }

 // Test matchConsume() method with multiple characters
 @Test
 public void testMatchConsumeMultipleCharacters() {
 CharacterReader reader = new CharacterReader("Hello World");
 String str = reader.matchConsume("ll");
 Assert.assertEquals("Helllo World", reader.toString());
 Assert.assertEquals("ll", str);
 }

 // Test containsIgnoreCase() method
 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head><body>Hello World</body></html>");
 Assert.assertTrue(reader.containsIgnoreCase("/title"));
 Assert.assertTrue(reader.containsIgnoreCase("/body"));
 }
}