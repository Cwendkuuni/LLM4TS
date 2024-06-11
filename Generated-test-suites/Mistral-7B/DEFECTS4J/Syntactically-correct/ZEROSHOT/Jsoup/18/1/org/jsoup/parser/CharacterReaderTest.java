import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 char[] input = new char[]{'a', 'b', 'c', 'd', 'e'};
 reader = new CharacterReader(new String(input));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.advance();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testPos() {
 assertEquals(0, reader.pos());
 reader.advance();
 assertEquals(1, reader.pos());
 reader.unconsume();
 assertEquals(1, reader.pos());
 }

 @Test
 public void testCurrent() {
 assertEquals('a', reader.current());
 reader.advance();
 assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 assertEquals('a', reader.consume());
 assertEquals('b', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.advance();
 reader.unconsume();
 assertEquals('a', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 reader.advance();
 assertEquals('c', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String str = reader.consumeAsString();
 assertEquals("ab", str);
 }

 @Test
 public void testConsumeTo() {
 String str = reader.consumeTo('c');
 assertEquals("ab", str);
 reader.advance();
 reader.advance();
 str = reader.consumeTo('c');
 assertEquals("cd", str);
 }

 @Test
 public void testConsumeToEnd() {
 String str = reader.consumeToEnd();
 assertEquals("def", str);
 }

 @Test
 public void testConsumeLetterSequence() {
 String str = reader.consumeLetterSequence();
 assertEquals("abcde", str);
 }

 @Test
 public void testConsumeHexSequence() {
 String str = reader.consumeHexSequence();
 assertEquals("abcdbe", str);
 }

 @Test
 public void testConsumeDigitSequence() {
 String str = reader.consumeDigitSequence();
 assertEquals("12345", str);
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('a'));
 reader.advance();
 assertFalse(reader.matches('a'));
 }

 @Test
 public void testMatchesString() {
 String seq = "ab";
 assertTrue(reader.matches(seq));
 reader.advance();
 reader.advance();
 assertFalse(reader.matches(seq));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String seq = "Ab";
 assertTrue(reader.matchesIgnoreCase(seq));
 reader.advance();
 reader.advance();
 assertFalse(reader.matchesIgnoreCase(seq));
 }

 @Test
 public void testMatchesAny() {
 char[] seq = new char[]{'a', 'b'};
 assertTrue(reader.matchesAny(seq));
 reader.advance();
 reader.advance();
 assertTrue(reader.matchesAny(seq));
 reader.advance();
 assertFalse(reader.matchesAny(seq));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 reader.advance();
 assertTrue(reader.matchesLetter());
 reader.advance();
 assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 reader.advance();
 reader.advance();
 assertTrue(reader.matchesDigit());
 reader.advance();
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String seq = "ab";
 assertTrue(reader.matchConsume(seq));
 assertEquals("cd", reader.current());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String seq = "Ab";
 assertTrue(reader.matchConsumeIgnoreCase(seq));
 assertEquals("cd", reader.current());
 }

 @Test
 public void testContainsIgnoreCase() {
 String seq = "ab";
 assertTrue(reader.containsIgnoreCase(seq));
 reader.advance();
 reader.advance();
 assertTrue(reader.containsIgnoreCase(seq));
 reader.advance();
 reader.advance();
 assertFalse(reader.containsIgnoreCase(seq));
 }
}