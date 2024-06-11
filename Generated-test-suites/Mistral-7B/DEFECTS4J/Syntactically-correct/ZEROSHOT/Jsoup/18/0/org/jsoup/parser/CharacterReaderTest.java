import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setup() {
 char[] testInput = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
 String testInputString = new String(testInput);
 reader = new CharacterReader(testInputString);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(reader.isEmpty());
 reader.consume();
 assertFalse(reader.isEmpty());
 }

 @Test
 public void testPosAndCurrent() {
 assertEquals(0, reader.pos());
 assertEquals('a', reader.current());
 reader.consume();
 assertEquals(1, reader.pos());
 assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 reader.consume();
 reader.consume();
 assertEquals('c', reader.current());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumePastEnd() {
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume(); // should throw exception
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 assertEquals('c', reader.current());
 reader.unconsume();
 assertEquals('b', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 assertEquals('b', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 assertEquals('b', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String result = reader.consumeAsString();
 assertEquals("ab", result);
 reader.consume();
 reader.consume();
 result = reader.consumeAsString();
 assertEquals("cd", result);
 }

 @Test
 public void testConsumeTo() {
 String result = reader.consumeTo('d');
 assertEquals("bc", result);
 reader.consume();
 reader.consume();
 result = reader.consumeTo('f');
 assertEquals("de", result);
 }

 @Test
 public void testConsumeToString() {
 String result = reader.consumeTo("ef");
 assertEquals("g", result.charAt(0));
 assertEquals(2, result.length());
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 result = reader.consumeTo("gh");
 assertEquals("i", result.charAt(0));
 assertEquals(2, result.length());
 }

 @Test
 public void testConsumeToAny() {
 String result = reader.consumeToAny('a', 'b', 'c');
 assertEquals("c", result.charAt(0));
 assertEquals(1, result.length());
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 result = reader.consumeToAny('c', 'd', 'e');
 assertEquals("e", result.charAt(0));
 assertEquals(1, result.length());
 }

 @Test
 public void testConsumeToEnd() {
 String result = reader.consumeToEnd();
 assertEquals("g", result.charAt(0));
 assertEquals(1, result.length());
 }

 @Test
 public void testConsumeLetterSequence() {
 String result = reader.consumeLetterSequence();
 assertEquals("abc", result);
 }

 @Test
 public void testConsumeHexSequence() {
 String result = reader.consumeHexSequence();
 assertEquals("def", result);
 }

 @Test
 public void testConsumeDigitSequence() {
 String result = reader.consumeDigitSequence();
 assertEquals("123", result);
 }

 @Test
 public void testMatches() {
 assertTrue(reader.matches('b'));
 reader.consume();
 assertFalse(reader.matches('b'));
 }

 @Test
 public void testMatchesString() {
 String testString = "abcd";
 assertTrue(reader.matches("ab"));
 reader.consume();
 reader.consume();
 assertFalse(reader.matches("ab"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String testString = "AbCd";
 assertTrue(reader.matchesIgnoreCase("AB"));
 reader.consume();
 reader.consume();
 assertFalse(reader.matchesIgnoreCase("AB"));
 }

 @Test
 public void testMatchesAny() {
 assertTrue(reader.matchesAny('b', 'c'));
 reader.consume();
 assertFalse(reader.matchesAny('b', 'c'));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(reader.matchesLetter());
 reader.consume();
 assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(reader.matchesDigit());
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String testString = "abc";
 assertTrue(reader.matchConsume("ab"));
 reader.consume();
 assertFalse(reader.matchConsume("ab"));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String testString = "AbC";
 assertTrue(reader.matchConsumeIgnoreCase("Ab"));
 reader.consume();
 reader.consume();
 assertFalse(reader.matchConsumeIgnoreCase("Ab"));
 }

 @Test
 public void testContainsIgnoreCase() {
 String testString = "abcdAbcd";
 assertTrue(reader.containsIgnoreCase("Ab"));
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 assertFalse(reader.containsIgnoreCase("Ab"));
 }

 @Test
 public void testToString() {
 String result = reader.toString();
 assertEquals("", result);
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume();
 result = reader.toString();
 assertEquals("", result);
 }
}