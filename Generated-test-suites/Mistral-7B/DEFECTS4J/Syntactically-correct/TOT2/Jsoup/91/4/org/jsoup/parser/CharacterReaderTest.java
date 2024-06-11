import org.junit.Assert;
import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Before
 public void setUp() {
 Validate.noException(reader = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("test data".getBytes()))));
 }

 @Test
 public void testConstructorWithReader() {
 CharacterReader reader1 = new CharacterReader(new InputStreamReader(new ByteArrayInputStream("test data".getBytes())));
 Assert.assertArrayEquals(new char[]{'t', 'e', 's', 't', ' ', 'd', 'a', 't', 'a', '\n'}, reader1.consumeTo("test data".toCharArray()));
 }

 @Test
 public void testConstructorWithString() {
 CharacterReader reader1 = new CharacterReader(new StringReader("test data"));
 Assert.assertArrayEquals(new char[]{'t', 'e', 's', 't', ' ', 'd', 'a', 't', 'a', '\n'}, reader1.consumeTo("test data".toCharArray()));
 }

 @Test
 public void testBufferUp() {
 reader.pos(); // read initial position
 reader.bufferUp();
 reader.pos(); // check if position has been updated
 }

 @Test
 public void testIsEmpty() {
 Assert.assertTrue(reader.isEmpty()); // initial state
 reader.consume('t');
 Assert.assertFalse(reader.isEmpty());
 reader.consumeTo(" test data".toCharArray());
 Assert.assertTrue(reader.isEmpty());
 }

 @Test(expected = UncheckedIOException.class)
 public void testIOException() {
 reader.bufferUp();
 reader.reader.mark(0);
 reader.reader.close();
 reader.bufferUp();
 }

 @Test
 public void testCurrentAndConsume() {
 reader.consume('t');
 Assert.assertEquals('t', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testAdvance() {
 reader.consume('t');
 reader.advance();
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testConsumeTo() {
 String expected = "test data";
 Assert.assertEquals(expected, reader.consumeTo(expected.toCharArray()));
 }

 @Test
 public void testConsumeToSequence() {
 String expected = "test data";
 Assert.assertEquals(expected, reader.consumeTo(expected));
 }

 @Test
 public void testConsumeToAny() {
 char[] expected = {'t', 'e', 's', 't', ' ', 'd', 'a', 't', 'a'};
 Assert.assertEquals(expected, reader.consumeToAny(expected));
 }

 @Test
 public void testConsumeToAnySorted() {
 char[] expected = {'t', 'e', 's', 't', ' ', 'd', 'a', 't', 'a'};
 Assert.assertEquals(expected, reader.consumeToAnySorted(expected));
 }

 @Test
 public void testConsumeData() {
 String expected = "&, <, null";
 Assert.assertEquals(expected, reader.consumeData());
 }

 @Test
 public void testConsumeTagName() {
 String expected = "\\t\\n\\r\\f \\/><";
 Assert.assertEquals(expected, reader.consumeTagName());
 }

 @Test
 public void testConsumeLetterSequence() {
 String expected = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
 Assert.assertEquals(expected, reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 String expected = "abc123def456";
 Assert.assertEquals(expected, reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testConsumeHexSequence() {
 String expected = "0123456789ABCDEF";
 Assert.assertEquals(expected, reader.consumeHexSequence());
 }

 @Test
 public void testConsumeDigitSequence() {
 String expected = "0123456789";
 Assert.assertEquals(expected, reader.consumeDigitSequence());
 }

 @Test
 public void testMatches() {
 Assert.assertTrue(reader.matches('t'));
 Assert.assertFalse(reader.matches(
));
 }

 @Test
 public void testMatchesString() {
 Assert.assertTrue(reader.matches("test data".toCharArray()));
 Assert.assertFalse(reader.matches("test data1".toCharArray()));
 }

 @Test
 public void testMatchesIgnoreCase() {
 Assert.assertTrue(reader.matchesIgnoreCase("Test data".toCharArray()));
 Assert.assertFalse(reader.matchesIgnoreCase("Test data1".toCharArray()));
 }

 @Test
 public void testMatchesAny() {
 char[] expected = {'t', 'e', 's', 't', ' ', 'd', 'a', 't', 'a'};
 Assert.assertArrayEquals(expected, reader.matchesAny(expected));
 }

 @Test
 public void testMatchesAnySorted() {
 char[] expected = {'t', 'e', 's', 't', ' ', 'd', 'a', 't', 'a'};
 Assert.assertArrayEquals(expected, reader.matchesAnySorted(expected));
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
 String expected = "test data";
 reader.matchConsume(expected);
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 String expected = "Test data";
 reader.matchConsumeIgnoreCase(expected);
 Assert.assertTrue(reader.isEmpty());
 }

 @Test
 public void testContainsIgnoreCase() {
 Assert.assertTrue(reader.containsIgnoreCase("/title</"));
 }

 @Test
 public void testToString() {
 String expected = "test data";
 Assert.assertEquals(expected, reader.toString());
 }

}