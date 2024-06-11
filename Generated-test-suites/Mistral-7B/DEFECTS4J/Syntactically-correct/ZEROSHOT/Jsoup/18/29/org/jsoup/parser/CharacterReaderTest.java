import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] testInput = {'t', 'e', 's', 't', ' ', 'i', 's', ' ', 'a', ' ', 'C', 'h', 'a', 'r', 'a', 'c', 't', 'e', 'r', ' ', 'R', 'e', 'a', 'd', 'e', 'r', ':'};
 String testInputString = new String(testInput);
 reader = new CharacterReader(testInputString);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 }

 @Test
 public void testConsume() {
 assertEquals('t', reader.consume());
 assertEquals('i', reader.consume());
 assertFalse(reader.isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeAfterEndOfInput() {
 reader.consume();
 reader.consume();
 reader.consume(); // consume past the end of input
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 assertEquals('i', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('s', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('i', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 assertEquals(" is ", reader.consumeAsString());
 }

 @Test
 public void testConsumeTo() {
 assertEquals(" a ", reader.consumeTo(' '));
 }

 @Test
 public void testConsumeToEnd() {
 assertEquals("CharacterReaderReader: ", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 assertEquals("Charcter", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 assertEquals("red:", reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 assertEquals("123", reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches(' '));
 assertFalse(reader.matches('A'));
 }

 @Test
 public void testMatchesString() {
 assertTrue(reader.matches(" is "));
 assertFalse(reader.matches("is "));
 }

 @Test
 public void testMatchesIgnoreCaseString() {
 assertTrue(reader.matchesIgnoreCase(" is "));
 assertTrue(reader.matchesIgnoreCase("IS "));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny(' ', 's'));
 assertFalse(reader.matchesAny('A'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 assertFalse(reader.matchesLetter('2'));
 }

 @Test
 public void testMatchesDigit() {
 assertFalse(reader.matchesDigit());
 assertTrue(reader.matchesDigit('3'));
 }

 @Test
 public void testMatchConsume() {
 assertTrue(reader.matchConsume(" is "));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 assertTrue(reader.matchConsumeIgnoreCase(" is "));
 }

 @Test
 public void testContainsIgnoreCase() {
 assertTrue(reader.containsIgnoreCase("is"));
 assertFalse(reader.containsIgnoreCase("test"));
 }

 @Test
 public void testToString() {
 assertEquals("Reader: ", reader.toString());
 }

}