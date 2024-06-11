import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 @Test
 void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 Assertions.assertTrue(reader.isEmpty());
 }

 @Test
 void testNormalConsume() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assertions.assertEquals('a', reader.current());
 Assertions.assertEquals('a', reader.consume());
 }

 @Test
 void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcd");
 String result = reader.consumeToEnd();
 Assertions.assertEquals("cd", result);
 }

 @Test
 void testConsumeToSpecificChar() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 String result = reader.consumeTo('d');
 Assertions.assertEquals("ab", result);
 }

 @Test
 void testConsumeToSpecificString() {
 CharacterReader reader = new CharacterReader("abcdabcdabcd");
 String result = reader.consumeTo("cd");
 Assertions.assertEquals("abab", result);
 }

 @Test
 void testConsumeToAnyCharacter() {
 CharacterReader reader = new CharacterReader("abxcdabcdabcx");
 String result = reader.consumeToAny('x', 'd');
 Assertions.assertEquals("abx", result);
 }

 @Test
 void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abc123defghijk");
 String result = reader.consumeLetterSequence();
 Assertions.assertEquals("abc", result);
 }

 @Test
 void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("1234abcd56efghijk");
 String result = reader.consumeHexSequence();
 Assertions.assertEquals("1234", result);
 }

 @Test
 void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234abcd56efghijk");
 String result = reader.consumeDigitSequence();
 Assertions.assertEquals("1234", result);
 }

 @Test
 void testConsumeNonMatchingChar() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assertions.assertFalse(reader.matches('g'));
 }

 @Test
 void testConsumeMatchingString() {
 CharacterReader reader = new CharacterReader("abcdefabcdef");
 Assertions.assertTrue(reader.matches("def"));
 }

 @Test
 void testConsumeMatchingStringIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcdefabcDEF");
 Assertions.assertTrue(reader.matchesIgnoreCase("def"));
 }

 @Test
 void testConsumeMatchingAnyCharacter() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assertions.assertTrue(reader.matchesAny('a', 'e'));
 }

 @Test
 void testConsumeMatchingLetter() {
 CharacterReader reader = new CharacterReader("abcdef");
 Assertions.assertTrue(reader.matchesLetter());
 }

 @Test
 void testConsumeMatchingDigit() {
 CharacterReader reader = new CharacterReader("123456abcdef");
 Assertions.assertTrue(reader.matchesDigit());
 }

 @Test
 void testConsumeMatchingSequence() {
 CharacterReader reader = new CharacterReader("</title><style>");
 Assertions.assertTrue(reader.matches("</title>"));
 reader.advance();
 Assertions.assertTrue(reader.matches("<style>"));
 }

 @Test
 void testConsumeToEndAfterMatchingSequence() {
 CharacterReader reader = new CharacterReader("</title><style>");
 String result = reader.consumeToEnd();
 Assertions.assertEquals("", result);
 }

 @Test
 void testConsumeToEndAfterMatchingSequenceWithMark() {
 CharacterReader reader = new CharacterReader("</title><style>");
 reader.mark();
 String result = reader.consumeToEnd();
 Assertions.assertEquals("", result);
 reader.rewindToMark();
 Assertions.assertTrue(reader.matches("<style>"));
 }
}