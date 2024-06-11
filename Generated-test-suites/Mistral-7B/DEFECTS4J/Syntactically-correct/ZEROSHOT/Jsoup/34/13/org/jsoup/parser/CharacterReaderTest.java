import org.jsoup.parser.CharacterReader;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstruction() {
 CharacterReader reader = new CharacterReader("hello world");
 assertEquals("hello world", reader.toString());
 }

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testCurrentCharacter() {
 CharacterReader reader = new CharacterReader("hello world");
 assertEquals('h', reader.current());
 reader.consume();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsumeCharacter() {
 CharacterReader reader = new CharacterReader("hello world");
 reader.consume();
 assertEquals('e', reader.current());
 }

 @Test
 public void testConsumeString() {
 CharacterReader reader = new CharacterReader("hello world");
 assertEquals("ello world", reader.consumeAsString());
 }

 @Test
 public void testNextIndexOfSingleCharacter() {
 CharacterReader reader = new CharacterReader("hello world");
 assertEquals(6, reader.nextIndexOf('l'));
 }

 @Test
 public void testNextIndexOfMultiCharSequence() {
 CharacterReader reader = new CharacterReader("hello world");
 assertEquals(8, reader.nextIndexOf("ll"));
 }

 @Test
 public void testConsumeToCharacter() {
 CharacterReader reader = new CharacterReader("hello world");
 assertEquals("ello", reader.consumeTo('l'));
 }

 @Test
 public void testConsumeToCharSequence() {
 CharacterReader reader = new CharacterReader("hello world");
 assertEquals("ello world", reader.consumeTo("ll"));
 }

 @Test
 public void testConsumeToAnyCharacter() {
 CharacterReader reader = new CharacterReader("helloworld123");
 assertEquals("ow", reader.consumeToAny('o', 'w'));
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader("hello world");
 assertEquals(" world", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader("hello world");
 assertEquals("hello", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader("hello123world");
 assertEquals("hello123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 CharacterReader reader = new CharacterReader("0x1a");
 assertEquals("1a", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 CharacterReader reader = new CharacterReader("12345");
 assertEquals("5", reader.consumeDigitSequence());
 }

 @Test
 public void testMatchesSingleCharacter() {
 CharacterReader reader = new CharacterReader("hello world");
 assertTrue(reader.matches('h'));
 }

 @Test
 public void testMatchesMultiCharacterSequence() {
 CharacterReader reader = new CharacterReader("hello world");
 assertTrue(reader.matches("ll"));
 }

 @Test
 public void testMatchesIgnoreCaseMultiCharacterSequence() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchesIgnoreCase("ll"));
 }

 @Test
 public void testMatchesAnyCharacter() {
 CharacterReader reader = new CharacterReader("hello world");
 assertTrue(reader.matchesAny('l', 'w'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("hello world");
 assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("hello world");
 assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsumeSingleCharacter() {
 CharacterReader reader = new CharacterReader("hello world");
 assertTrue(reader.matchConsume('l'));
 }

 @Test
 public void testMatchConsumeMultiCharacterSequence() {
 CharacterReader reader = new CharacterReader("hello world");
 assertTrue(reader.matchConsume("ll"));
 }

 @Test
 public void testMatchConsumeIgnoreCaseMultiCharacterSequence() {
 CharacterReader reader = new CharacterReader("Hello World");
 assertTrue(reader.matchConsumeIgnoreCase("ll"));
 }

 @Test
 public void testContainsIgnoreCaseMultiCharacterSequence() {
 CharacterReader reader = new CharacterReader("<html><head><title>My Test Page</title></head><body>");
 assertTrue(reader.containsIgnoreCase("<title>"));
 }

 @Test
 public void testMarkUnmarkRewind() {
 CharacterReader reader = new CharacterReader("hello world");
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 assertEquals('h', reader.current());
 }
}