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
 void testSimpleConsumeAndAdvance() {
 CharacterReader reader = new CharacterReader("abcdef");
 char expected = 'a';
 Assertions.assertEquals(expected, reader.current());
 Assertions.assertFalse(reader.isEmpty());
 reader.advance();
 reader.consume();
 expected = 'b';
 Assertions.assertEquals(expected, reader.current());
 Assertions.assertFalse(reader.isEmpty());
 }

 @Test
 void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("abcd1234ef");
 String expected = "abcd1234ef";
 String actual = reader.consumeToEnd();
 Assertions.assertEquals(expected, actual);
 Assertions.assertTrue(reader.isEmpty());
 }

 @Test
 void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("abracadabra");
 String expected = "abra";
 String actual = reader.consumeLetterSequence();
 Assertions.assertEquals(expected, actual);
 }

 @Test
 void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("ab12cd34ef");
 String expected = "12";
 String actual = reader.consumeHexSequence();
 Assertions.assertEquals(expected, actual);
 }

 @Test
 void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("ab12cd34ef");
 String expected = "1234";
 String actual = reader.consumeDigitSequence();
 Assertions.assertEquals(expected, actual);
 }

 @Test
 void testMatches() {
 CharacterReader reader = new CharacterReader("abcd1234efg");
 char c = 'd';
 Assertions.assertTrue(reader.matches(c));
 reader.advance();
 Assertions.assertFalse(reader.matches(c));
 }

 @Test
 void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("AbCd1234eFg");
 String seq = "cd";
 Assertions.assertTrue(reader.matchesIgnoreCase(seq));
 }

 @Test
 void testMatchesAny() {
 CharacterReader reader = new CharacterReader("abcdefg123456");
 char[] seq = new char[]{'e', '5'};
 Assertions.assertTrue(reader.matchesAny(seq));
 }

 @Test
 void testConsumeTo() {
 CharacterReader reader = new CharacterReader("abcd1234efg");
 String expected = "cd";
 String actual = reader.consumeTo('d');
 Assertions.assertEquals(expected, actual);
 }

 @Test
 void testConsumeToAny() {
 CharacterReader reader = new CharacterReader("abcd1234efg123456");
 char[] seq = new char[]{'e', '5'};
 String expected = "5";
 String actual = reader.consumeToAny(seq);
 Assertions.assertEquals(expected, actual);
 }

 @Test
 void testConsumeToEndWithMark() {
 CharacterReader reader = new CharacterReader("abcd1234efg");
 reader.mark();
 String expected = "abcd";
 String actual = reader.consumeToEnd();
 Assertions.assertEquals(expected, actual);
 reader.rewindToMark();
 Assertions.assertFalse(reader.isEmpty());
 Assertions.assertEquals('a', reader.current());
 }
}