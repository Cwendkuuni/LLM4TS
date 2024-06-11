package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 // Test for constructor
 @Test
 public void testCharacterReaderConstructor() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertNotNull(reader);
 }

 // Test for isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertFalse(reader.isEmpty());
 reader.consumeToEnd();
 Assert.assertTrue(reader.isEmpty());
 }

 // Test for current method
 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals('H', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 // Test for consume method
 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("Hello World");
 String expected = "ello Worl";
 reader.consumeN(5);
 Assert.assertEquals(expected, reader.toString());
 }

 // Test for consumeAsString method
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("Hello", reader.consumeAsString());
 }

 // Test for nextIndexOf method with single character
 @Test
 public void testNextIndexOfSingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals(4, reader.nextIndexOf('l'));
 }

 // Test for nextIndexOf method with sequence of characters
 @Test
 public void testNextIndexOfSequence() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals(6, reader.nextIndexOf("ll"));
 }

 // Test for nextIndexOf method when sequence not found
 @Test
 public void testNextIndexOfNotFound() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals(-1, reader.nextIndexOf("zz"));
 }

 // Test for consumeTo method with single character
 @Test
 public void testConsumeToSingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("World", reader.consumeTo('W').trim());
 }

 // Test for consumeTo method with string
 @Test
 public void testConsumeToString() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("llo World", reader.consumeTo("llo").trim());
 }

 // Test for consumeToAny method with single character
 @Test
 public void testConsumeToAnySingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("ld", reader.consumeToAny('l').trim());
 }

 // Test for consumeToAny method with multiple characters
 @Test
 public void testConsumeToAnyMultipleChars() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("ldr", reader.consumeToAny(new char[]{'l', 'd', 'r'}).trim());
 }

 // Test for consumeToAny method when no matching character found
 @Test
 public void testConsumeToAnyNoMatch() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("", reader.consumeToAny(new char[]{'z', 'x'}));
 }

 // Test for consumeData method
 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&lt;p class=&quot;some_class&quot;&gt;Hello World&lt;/p&gt;");
 Assert.assertEquals("<p class=\"some_class\">", reader.consumeData().trim());
 }

 // Test for consumeTagName method
 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("&lt;p class=&quot;some_class&quot;&gt;Hello World&lt;/p&gt;");
 Assert.assertEquals("p", reader.consumeTagName().trim());
 }

 // Test for consumeToEnd method
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("Hello World", reader.consumeToEnd().trim());
 }

 // Test for consumeLetterSequence method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("123abc123");
 Assert.assertEquals("abc", reader.consumeLetterSequence().trim());
 }

 // Test for consumeLetterThenDigitSequence method
 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("Hello123World456");
 Assert.assertEquals("Hello123", reader.consumeLetterThenDigitSequence().trim());
 }

 // Test for consumeHexSequence method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0x12ABCdef");
 Assert.assertEquals("12ABCdef", reader.consumeHexSequence().trim());
 }

 // Test for consumeDigitSequence method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("12345");
 Assert.assertEquals("12345", reader.consumeDigitSequence().trim());
 }

 // Test for matches method with single character
 @Test
 public void testMatchesSingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matches('l'));
 }

 // Test for matches method with multiple characters
 @Test
 public void testMatchesMultipleChars() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matches("ll"));
 }

 // Test for matches method when no match found
 @Test
 public void testMatchesNoMatch() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertFalse(reader.matches("zz"));
 }

 // Test for matchesIgnoreCase method with single character
 @Test
 public void testMatchesIgnoreCaseSingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matchesIgnoreCase("L"));
 }

 // Test for matchesIgnoreCase method with multiple characters
 @Test
 public void testMatchesIgnoreCaseMultipleChars() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matchesIgnoreCase("ll"));
 }

 // Test for matchesAny method with single character
 @Test
 public void testMatchesAnySingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matchesAny('l'));
 }

 // Test for matchesAny method with multiple characters
 @Test
 public void testMatchesAnyMultipleChars() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matchesAny(new char[]{'l', 'd'}));
 }

 // Test for matchesAny method when no matching character found
 @Test
 public void testMatchesAnyNoMatch() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertFalse(reader.matchesAny(new char[]{'z', 'x'}));
 }

 // Test for matchesLetter method
 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matchesLetter());
 }

 // Test for matchesDigit method
 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertTrue(reader.matchesDigit());
 }

 // Test for matchConsume method with single character
 @Test
 public void testMatchConsumeSingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("World", reader.matchConsume('l').toString());
 }

 // Test for matchConsume method with multiple characters
 @Test
 public void testMatchConsumeMultipleChars() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("llo World", reader.matchConsume("llo").toString());
 }

 // Test for containsIgnoreCase method
 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test Title</title></head><body>Test Body</body></html>");
 Assert.assertTrue(reader.containsIgnoreCase("/title/"));
 Assert.assertTrue(reader.containsIgnoreCase("/body/"));
 }

 // Test for toString method
 @Test
 public void testToString() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("Hello World", reader.toString());
 }
}