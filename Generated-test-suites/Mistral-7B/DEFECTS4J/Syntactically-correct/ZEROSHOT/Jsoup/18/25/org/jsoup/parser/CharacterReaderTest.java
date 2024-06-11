import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private String input;
 private CharacterReader reader;

 @Before
 public void setup() {
 input = "abcdef123ABC<title>My Title</title>456xYZ";
 reader = new CharacterReader(input);
 }

 @Test
 public void testIsEmpty() {
 Assert.assertFalse(reader.isEmpty());
 reader.consumeToEnd();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testPos() {
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testCurrent() {
 Assert.assertEquals('a', reader.current());
 reader.consume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testConsume() {
 reader.consume();
 reader.consume();
 Assert.assertEquals('c', reader.current());
 }

 @Test
 public void testUnconsume() {
 reader.consume();
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.advance();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testMark() {
 reader.mark();
 reader.consume();
 reader.rewindToMark();
 Assert.assertEquals('b', reader.current());
 }

 @Test
 public void testConsumeAsString() {
 String expected = "def";
 String result = reader.consumeAsString();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testConsumeTo() {
 String expected = "ef1";
 String result = reader.consumeTo('1');
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testConsumeToString() {
 String expected = "<title>My Title</title>";
 String result = reader.consumeTo("<title>");
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testConsumeToAny() {
 char[] expectedCharArray = {'x', 'Y'};
 char[] resultCharArray = reader.consumeToAny(expectedCharArray).toCharArray();
 Assert.assertArrayEquals(expectedCharArray, resultCharArray);
 }

 @Test
 public void testConsumeToEnd() {
 String expected = "xYZ";
 String result = reader.consumeToEnd();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "def";
 String result = reader.consumeLetterSequence();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "123";
 String result = reader.consumeHexSequence();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "123";
 String result = reader.consumeDigitSequence();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('d'));
 reader.consume();
 Assert.assertFalse(reader.matches('d'));
 }

 @Test
 public void testMatchesString() {
 String expected = "ef";
 String result = reader.consumeTo(expected).trim();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("ef"));
 }

 @Test
 public void testMatchesAny() {
 char[] expectedCharArray = {'x', 'Y'};
 char[] resultCharArray = reader.consumeToAny(expectedCharArray).toCharArray();
 Assert.assertArrayEquals(expectedCharArray, resultCharArray);
 }

 @Test
 public void testMatchesLetter() {
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 String expected = "ef";
 String result = reader.matchConsume("ef").trim();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expected = "ef";
 String result = reader.matchConsumeIgnoreCase("ef").trim();
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testContainsIgnoreCase() {
 String expected = "<title>";
 Assert.assertTrue(reader.containsIgnoreCase(expected));
 }

 @Test
 public void testToString() {
 String expected = "My Title";
 String result = reader.toString();
 Assert.assertEquals(expected, result);
 }
}