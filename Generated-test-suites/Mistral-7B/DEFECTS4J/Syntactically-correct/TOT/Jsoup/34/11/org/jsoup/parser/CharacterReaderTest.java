import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");
 assertTrue(reader.isEmpty());
 assertEquals("", reader.toString());
 }

 @Test
 public void testSingleCharacterReader() {
 CharacterReader reader = new CharacterReader("abcd");
 assertEquals('a', reader.current());
 assertEquals('a', reader.consume());
 assertEquals('b', reader.current());
 }

 @Test
 public void testReadSequence() {
 CharacterReader reader = new CharacterReader("12345abcde");
 assertEquals("12345", reader.consumeTo('5'));
 assertEquals("abcde", reader.consumeToEnd());
 }

 @Test
 public void testReadSequenceWithMark() {
 CharacterReader reader = new CharacterReader("12345abcde");
 reader.mark();
 reader.consume(2);
 reader.rewindToMark();
 assertEquals("12", reader.consumeTo('3'));
 }

 @Test(expected = IllegalStateException.class)
 public void testReadBeyondEnd() {
 CharacterReader reader = new CharacterReader("abcd");
 reader.consumeTo('e');
 }

 @Test
 public void testScanCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matches('a'));
 assertTrue(reader.matches('b'));
 assertTrue(reader.matches('c'));
 assertFalse(reader.matches('d'));
 assertFalse(reader.matches('e'));
 }

 @Test
 public void testScanString() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matches("ab"));
 assertFalse(reader.matches("cd"));
 }

 @Test
 public void testScanIgnoreCase() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesIgnoreCase("ab"));
 assertFalse(reader.matchesIgnoreCase("CD"));
 }

 @Test
 public void testScanAnyCharacter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
 }

 @Test
 public void testScanLetter() {
 CharacterReader reader = new CharacterReader("abcd");
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('5'));
 }

 @Test
 public void testScanDigit() {
 CharacterReader reader = new CharacterReader("12345");
 assertTrue(reader.matchesDigit());
 assertFalse(reader.matchesDigit('a'));
 }

 @Test
 public void testScanHexSequence() {
 CharacterReader reader = new CharacterReader("0xF0x");
 String hex = reader.consumeTo('x');
 assertEquals("0xF", hex);
 }
}