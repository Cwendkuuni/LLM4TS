package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 // Test case for isEmpty method
 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 }

 // Test case for isEmpty method with non-empty input
 @Test
 public void testNotEmpty() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertFalse(reader.isEmpty());
 }

 // Test case for pos method
 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals(0, reader.pos());
 }

 // Test case for pos method after consumption
 @Test
 public void testPosAfterConsumption() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.consume();
 assertEquals(1, reader.pos());
 }

 // Test case for current method
 @Test
 public void testCurrent() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertEquals('H', reader.current());
 }

 // Test case for current method after consumption
 @Test
 public void testCurrentAfterConsumption() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.consume();
 assertEquals('H', reader.current()); // still H, as pos hasn't been updated yet
 reader.consume();
 assertEquals('e', reader.current());
 }

 // Test case for consume method
 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.consume();
 assertEquals('H', reader.current());
 assertEquals(1, reader.pos());
 }

 // Test case for consume method when at end
 @Test(expected = IllegalStateException.class)
 public void testConsumeAtEnd() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.consumeToEnd();
 reader.consume();
 }

 // Test case for consumeAsString method
 @Test
 public void testConsumeAsString() {
 CharacterReader reader = new CharacterReader("Hello World");
 String result = reader.consumeAsString();
 assertEquals("World", result);
 }

 // Test case for nextIndexOf method with single character
 @Test
 public void testNextIndexOfSingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 int index = reader.nextIndexOf('l');
 assertEquals(4, index);
 }

 // Test case for nextIndexOf method with multiple occurrences
 @Test
 public void testNextIndexOfMultipleOccurrences() {
 CharacterReader reader = new CharacterReader("Hello Hello");
 int index = reader.nextIndexOf('l');
 assertEquals(4, index);
 index = reader.nextIndexOf('l');
 assertEquals(7, index);
 }

 // Test case for nextIndexOf method when character not found
 @Test
 public void testNextIndexOfNotFound() {
 CharacterReader reader = new CharacterReader("Hello World");
 int index = reader.nextIndexOf('x');
 assertEquals(-1, index);
 }

 // Test case for consumeTo method with single character
 @Test
 public void testConsumeToSingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 String result = reader.consumeTo('l');
 assertEquals("llo", result);
 }

 // Test case for consumeTo method with multiple characters
 @Test
 public void testConsumeToMultipleChars() {
 CharacterReader reader = new CharacterReader("Hello World");
 String result = reader.consumeTo("llo");
 assertEquals("llo", result);
 }

 // Test case for consumeTo method when target not found
 @Test
 public void testConsumeToNotFound() {
 CharacterReader reader = new CharacterReader("Hello World");
 String result = reader.consumeTo("moo");
 assertEquals("", result);
 }

 // Test case for consumeToAny method with single character
 @Test
 public void testConsumeToAnySingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 String result = reader.consumeToAny('l');
 assertEquals("llo", result);
 }

 // Test case for consumeToAny method with multiple characters
 @Test
 public void testConsumeToAnyMultipleChars() {
 CharacterReader reader = new CharacterReader("Hello World");
 String result = reader.consumeToAny(new char[]{'l', 'o'});
 assertEquals("llo", result);
 }

 // Test case for consumeToAny method when no matching characters found
 @Test
 public void testConsumeToAnyNoMatch() {
 CharacterReader reader = new CharacterReader("Hello World");
 String result = reader.consumeToAny(new char[]{'m', 'o'});
 assertEquals("", result);
 }

 // Test case for consumeToAnySorted method with single character
 @Test
 public void testConsumeToAnySortedSingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 String result = reader.consumeToAnySorted('l');
 assertEquals("llo", result);
 }

 // Test case for consumeToAnySorted method with multiple characters
 @Test
 public void testConsumeToAnySortedMultipleChars() {
 CharacterReader reader = new CharacterReader("Hello World");
 String result = reader.consumeToAnySorted(new char[]{'l', 'o'});
 assertEquals("llo", result);
 }

 // Test case for consumeToAnySorted method when no matching characters found
 @Test
 public void testConsumeToAnySortedNoMatch() {
 CharacterReader reader = new CharacterReader("Hello World");
 String result = reader.consumeToAnySorted(new char[]{'m', 'o'});
 assertEquals("", result);
 }

 // Test case for consumeData method
 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader("&<Hello World>");
 String result = reader.consumeData();
 assertEquals("&<Hello World>", result);
 }

 // Test case for consumeTagName method
 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head>");
 String result = reader.consumeTagName();
 assertEquals("title", result);
 }

 // Test case for consumeToEnd method
 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head>");
 String result = reader.consumeToEnd();
 assertEquals("<html><head><title>Test</title></head>", result);
 }

 // Test case for consumeLetterSequence method
 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("Hello World");
 String result = reader.consumeLetterSequence();
 assertEquals("Hello", result);
 }

 // Test case for consumeLetterThenDigitSequence method
 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("Hello123World456");
 String result = reader.consumeLetterThenDigitSequence();
 assertEquals("Hello123", result);
 }

 // Test case for consumeHexSequence method
 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("&xxHelloWorld&yy");
 String result = reader.consumeHexSequence();
 assertEquals("xxHelloWorldyy", result);
 }

 // Test case for consumeDigitSequence method
 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 String result = reader.consumeDigitSequence();
 assertEquals("1234567890", result);
 }

 // Test case for matches method with single character
 @Test
 public void testMatchesSingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matches('l'));
 }

 // Test case for matches method with multiple characters
 @Test
 public void testMatchesMultipleChars() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matches("ll"));
 }

 // Test case for matches method when no match found
 @Test
 public void testMatchesNoMatch() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertFalse(reader.matches("moo"));
 }

 // Test case for matchesIgnoreCase method with single character
 @Test
 public void testMatchesIgnoreCaseSingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesIgnoreCase("L"));
 }

 // Test case for matchesIgnoreCase method with multiple characters
 @Test
 public void testMatchesIgnoreCaseMultipleChars() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesIgnoreCase("ll"));
 }

 // Test case for matchesIgnoreCase method when no match found
 @Test
 public void testMatchesIgnoreCaseNoMatch() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertFalse(reader.matchesIgnoreCase("moo"));
 }

 // Test case for matchesAny method with single character
 @Test
 public void testMatchesAnySingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesAny('l'));
 }

 // Test case for matchesAny method with multiple characters
 @Test
 public void testMatchesAnyMultipleChars() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesAny(new char[]{'l', 'o'}));
 }

 // Test case for matchesAny method when no match found
 @Test
 public void testMatchesAnyNoMatch() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertFalse(reader.matchesAny(new char[]{'m', 'o'}));
 }

 // Test case for matchesLetter method
 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesLetter());
 }

 // Test case for matchesLetter method when no match found
 @Test
 public void testMatchesLetterNoMatch() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertFalse(reader.matchesLetter());
 }

 // Test case for matchConsume method with single character
 @Test
 public void testMatchConsumeSingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.matchConsume('l');
 assertEquals(5, reader.pos());
 }

 // Test case for matchConsume method with multiple characters
 @Test
 public void testMatchConsumeMultipleChars() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.matchConsume("ll");
 assertEquals(7, reader.pos());
 }

 // Test case for matchConsume method when no match found
 @Test(expected = IllegalStateException.class)
 public void testMatchConsumeNoMatch() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.matchConsume("moo");
 }

 // Test case for matchConsumeIgnoreCase method with single character
 @Test
 public void testMatchConsumeIgnoreCaseSingleChar() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.matchConsumeIgnoreCase("L");
 assertEquals(5, reader.pos());
 }

 // Test case for matchConsumeIgnoreCase method with multiple characters
 @Test
 public void testMatchConsumeIgnoreCaseMultipleChars() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.matchConsumeIgnoreCase("ll");
 assertEquals(7, reader.pos());
 }

 // Test case for matchConsumeIgnoreCase method when no match found
 @Test(expected = IllegalStateException.class)
 public void testMatchConsumeIgnoreCaseNoMatch() {
 CharacterReader reader = new CharacterReader("Hello World");
 reader.matchConsumeIgnoreCase("moo");
 }

 // Test case for containsIgnoreCase method with single character
 @Test
 public void testContainsIgnoreCaseSingleChar() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head>");
 assertTrue(reader.containsIgnoreCase("t"));
 }

 // Test case for containsIgnoreCase method with multiple characters
 @Test
 public void testContainsIgnoreCaseMultipleChars() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head>");
 assertTrue(reader.containsIgnoreCase("es"));
 }

 // Test case for containsIgnoreCase method when no match found
 @Test
 public void testContainsIgnoreCaseNoMatch() {
 CharacterReader reader = new CharacterReader("<html><head><title>Test</title></head>");
 assertFalse(reader.containsIgnoreCase("moo"));
 }
}