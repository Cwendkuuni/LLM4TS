import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 @Test
 void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 Assertions.assertTrue(reader.isEmpty());
 Assertions.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 void testSimpleConsume() {
 CharacterReader reader = new CharacterReader("abcdef");
 char expected = 'a';
 Assertions.assertEquals(expected, reader.current());
 Assertions.assertEquals(expected, reader.consume());
 Assertions.assertEquals(expected + 1, reader.pos());
 }

 @Test
 void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader("12345");
 reader.mark();
 reader.consume();
 reader.consume();
 reader.rewindToMark();
 char expected = '1';
 Assertions.assertEquals(expected, reader.current());
 }

 @Test
 void testConsumeSequence() {
 CharacterReader reader = new CharacterReader("abcdef");
 String expected = "ab";
 Assertions.assertEquals(expected, reader.consumeAsString());
 }

 @Test
 void testConsumeTo() {
 CharacterReader reader = new CharacterReader("123abcd45ef");
 String expected = "abcd";
 Assertions.assertEquals(expected, reader.consumeTo('d'));
 Assertions.assertEquals('e', reader.current());
 }

 @Test
 void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("123abcd45ef");
 String expected = "ef";
 Assertions.assertEquals(expected, reader.consumeToEnd());
 Assertions.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("qwertyuiopasdfghjklzxcvbnm");
 String expected = "qwertyuiop";
 Assertions.assertEquals(expected, reader.consumeLetterSequence());
 }

 @Test
 void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0123456789ABCDEF");
 String expected = "ABCDEF";
 Assertions.assertEquals(expected, reader.consumeHexSequence());
 }

 @Test
 void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("1234567890");
 String expected = "1234567890";
 Assertions.assertEquals(expected, reader.consumeDigitSequence());
 }

 @Test
 void testMatches() {
 CharacterReader reader = new CharacterReader("abcdabcd");
 Assertions.assertTrue(reader.matches('a'));
 Assertions.assertFalse(reader.matches('z'));
 }

 @Test
 void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcdABCD");
 Assertions.assertTrue(reader.matchesIgnoreCase("AB"));
 Assertions.assertFalse(reader.matchesIgnoreCase("Z"));
 }

 @Test
 void testMatchesAny() {
 CharacterReader reader = new CharacterReader("012345abcd56ef");
 Assertions.assertTrue(reader.matchesAny('0', '5'));
 Assertions.assertFalse(reader.matchesAny('z'));
 }

 @Test
 void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(" <html><head><title>Test</title></head><body> <p>Sample text.</p> </body></html> ");
 Assertions.assertTrue(reader.containsIgnoreCase("title"));
 Assertions.assertTrue(reader.containsIgnoreCase("body"));
 }

 @Test
 void testToString() {
 CharacterReader reader = new CharacterReader("123abcd45ef");
 String expected = "ef";
 Assertions.assertEquals(expected, reader.toString());
 }
}