import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;

public class CharacterReaderTest {

 @Test
 public void testEmptyReader() {
 CharacterReader reader = new CharacterReader("");

 Assert.assertTrue(reader.isEmpty());
 Assert.assertEquals(CharacterReader.EOF, reader.current());
 }

 @Test
 public void testSimpleConsumption() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertEquals('a', reader.consume());
 Assert.assertEquals('b', reader.consume());
 Assert.assertEquals('c', reader.consume());
 Assert.assertEquals('d', reader.consume());
 Assert.assertEquals('e', reader.consume());
 Assert.assertEquals('f', reader.consume());
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testMarkUnconsume() {
 CharacterReader reader = new CharacterReader("abcd");

 reader.mark();
 reader.consume(); // consume 'a'
 reader.rewindToMark(); // go back to mark

 Assert.assertEquals('a', reader.current());
 }

 @Test
 public void testConsumeString() {
 CharacterReader reader = new CharacterReader("abcdefg");

 String consumed = reader.consumeAsString();
 Assert.assertEquals("abc", consumed);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumePastEnd() {
 CharacterReader reader = new CharacterReader("abc");

 reader.consume();
 reader.consume();
 reader.consume();
 reader.consume(); // consume 'd'
 reader.consume(); // consume 'e'
 reader.consume(); // consume 'f'
 reader.consume(); // consume 'g'
 reader.consume(); // consume past end
 }

 @Test
 public void testConsumeSequence() {
 CharacterReader reader = new CharacterReader("12345");

 String consumed = reader.consumeTo("3");
 Assert.assertEquals("12", consumed);
 }

 @Test
 public void testConsumeSequenceCaseSensitive() {
 CharacterReader reader = new CharacterReader("ABCabc123");

 String consumed = reader.consumeTo("AB");
 Assert.assertEquals("", consumed); // no match

 consumed = reader.consumeTo("Ab");
 Assert.assertEquals("Ab", consumed);
 }

 @Test
 public void testConsumeSequenceMixedCase() {
 CharacterReader reader = new CharacterReader("ABcDeF123");

 String consumed = reader.consumeToAny('a', 'B', 'C', 'e');
 Assert.assertEquals("Cde", consumed);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testConsumeSequenceNotFound() {
 CharacterReader reader = new CharacterReader("abcde");

 reader.consumeTo("f"); // consume sequence up to end
 String consumed = reader.consumeTo("x"); // consume past end
 }

 @Test
 public void testMatchesSimple() {
 CharacterReader reader = new CharacterReader("hello");

 Assert.assertTrue(reader.matches('h'));
 Assert.assertTrue(reader.matches("ello"));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader("HELLO");

 Assert.assertTrue(reader.matchesIgnoreCase("hello"));
 }

 @Test
 public void testMatchesAnyChar() {
 CharacterReader reader = new CharacterReader("123abc");

 Assert.assertTrue(reader.matchesAny('1', '2', '3', 'a', 'b', 'c'));
 Assert.assertFalse(reader.matchesAny('4'));
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader("abcdef");

 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertTrue(reader.matchesLetter());
 Assert.assertFalse(reader.matchesDigit());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader("12345");

 Assert.assertTrue(reader.matchesDigit());
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertTrue(reader.matchesDigit());
 Assert.assertFalse(reader.matchesLetter());
 }

 @Test
 public void testMatchConsumeSequence() {
 CharacterReader reader = new CharacterReader("12345");

 String consumed = reader.matchConsume("12");
 Assert.assertEquals("12", consumed);
 }

 @Test
 public void testMatchConsumeSequenceIgnoreCase() {
 CharacterReader reader = new CharacterReader("ABCabc123");

 String consumed = reader.matchConsumeIgnoreCase("ab");
 Assert.assertEquals("ab", consumed);
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(" <html><head><title>My Title</title></head><body>");

 Assert.assertTrue(reader.containsIgnoreCase("<title>"));
 Assert.assertTrue(reader.containsIgnoreCase("MY TITLE"));
 Assert.assertFalse(reader.containsIgnoreCase("<body>"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullInput() {
 CharacterReader reader = new CharacterReader(null);
 }
}