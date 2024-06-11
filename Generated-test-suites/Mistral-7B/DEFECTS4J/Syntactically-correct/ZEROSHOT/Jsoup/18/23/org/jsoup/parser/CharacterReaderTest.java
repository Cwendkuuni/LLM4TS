import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

 CharacterReader characterReader;

 @Before
 public void setUp() {
 char[] testInput = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
 characterReader = new CharacterReader(new String(testInput));
 }

 @Test
 public void testIsEmpty() {
 assertTrue(characterReader.isEmpty());
 characterReader.advance();
 assertFalse(characterReader.isEmpty());
 }

 @Test
 public void testCurrentAndConsume() {
 assertEquals('a', characterReader.current());
 assertEquals('a', characterReader.consume());
 }

 @Test(expected = IllegalStateException.class)
 public void testConsumeAfterEnd() {
 characterReader.consume();
 characterReader.consume();
 // consumes all characters, so next consume should throw an exception
 characterReader.consume();
 }

 @Test
 public void testUnconsume() {
 characterReader.consume();
 characterReader.unconsume();
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testAdvance() {
 characterReader.advance();
 assertEquals('b', characterReader.current());
 }

 @Test
 public void testMarkAndRewindToMark() {
 characterReader.mark();
 characterReader.advance();
 characterReader.rewindToMark();
 assertEquals('a', characterReader.current());
 }

 @Test
 public void testConsumeAsString() {
 String expected = "abc";
 String actual = characterReader.consumeAsString();
 assertEquals(expected, actual);
 }

 @Test
 public void testConsumeTo() {
 String expected = "bc";
 String actual = characterReader.consumeTo('c');
 assertEquals(expected, actual);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "defghij";
 String actual = characterReader.consumeToEnd();
 assertEquals(expected, actual);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "abc";
 String actual = characterReader.consumeLetterSequence();
 assertEquals(expected, actual);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "123";
 String actual = characterReader.consumeHexSequence();
 assertEquals(expected, actual);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "123";
 String actual = characterReader.consumeDigitSequence();
 assertEquals(expected, actual);
 }

 @Test
 public void testMatches() {
 char testChar = 'c';
 assertTrue(characterReader.matches(testChar));
 testChar = 'x';
 assertFalse(characterReader.matches(testChar));
 }

 @Test
 public void testMatchesString() {
 String testString = "abc";
 assertTrue(characterReader.matches(testString));
 testString = "def";
 assertFalse(characterReader.matches(testString));
 }

 @Test
 public void testMatchesIgnoreCase() {
 String testString = "AbC";
 assertTrue(characterReader.matchesIgnoreCase(testString));
 testString = "DEF";
 assertFalse(characterReader.matchesIgnoreCase(testString));
 }

 @Test
 public void testMatchesAny() {
 char[] testChars = {'a', 'b', 'c'};
 assertTrue(characterReader.matchesAny(testChars));
 testChars = new char[]{'x', 'y', 'z'};
 assertFalse(characterReader.matchesAny(testChars));
 }

 @Test
 public void testMatchesLetter() {
 assertTrue(characterReader.matchesLetter());
 characterReader.advance();
 assertFalse(characterReader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 assertTrue(characterReader.matchesDigit());
 characterReader.advance();
 assertFalse(characterReader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String testString = "abc";
 assertTrue(characterReader.matchConsume(testString));
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String testString = "AbC";
 assertTrue(characterReader.matchConsumeIgnoreCase(testString));
 }

 @Test
 public void testContainsIgnoreCase() {
 String testString = "def";
 assertFalse(characterReader.containsIgnoreCase(testString));
 testString = "efg";
 assertTrue(characterReader.containsIgnoreCase(testString));
 }

}