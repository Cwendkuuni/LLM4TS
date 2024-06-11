import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.UncheckedIOException;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstructorWithReader() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals("Hello World", reader.toString());
 }

 @Test
 public void testCharacterReaderConstructorWithString() {
 CharacterReader reader = new CharacterReader("Hello World");
 Assert.assertEquals("Hello World", reader.toString());
 }

 @Test
 public void testCharacterReaderConstructorWithEmptyString() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 Assert.assertEquals("", reader.toString());
 }

 @Test
 public void testBufferUpMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 reader.advance();
 reader.advance();
 Assert.assertEquals('H', reader.current());
 reader.unconsume();
 }

 @Test(expected = UncheckedIOException.class)
 public void testIOException() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World")) {
 @Override
 public int read(char[] cbuf, int off, int len) throws IOException {
 throw new IOException();
 }
 };
 reader.bufferUp();
 }

 @Test
 public void testPosMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals(0, reader.pos());
 reader.advance();
 reader.advance();
 Assert.assertEquals(2, reader.pos());
 }

 @Test
 public void testIsEmptyMethod() {
 CharacterReader reader = new CharacterReader(new StringReader(""));
 Assert.assertTrue(reader.isEmpty());
 reader.consume();
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testCurrentMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals('H', reader.current());
 reader.advance();
 Assert.assertEquals('H', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testConsumeMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals('H', reader.consume());
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testUnconsumeMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.advance();
 reader.advance();
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('H', reader.current());
 }

 @Test
 public void testAdvanceMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.advance();
 reader.advance();
 Assert.assertEquals('H', reader.current());
 }

 @Test
 public void testMarkMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('H', reader.current());
 }

 @Test
 public void testNextIndexOfMethodWithSingleChar() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals(6, reader.nextIndexOf('l'));
 }

 @Test
 public void testNextIndexOfMethodWithMultiChars() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals(4, reader.nextIndexOf("ll"));
 }

 @Test
 public void testNextIndexOfMethodWithEmptySeq() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals(-1, reader.nextIndexOf(""));
 }

 @Test
 public void testConsumeToMethodWithSingleChar() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals("ll", reader.consumeTo('l'));
 }

 @Test
 public void testConsumeToMethodWithMultiChars() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals("llo", reader.consumeTo("llo"));
 }

 @Test
 public void testConsumeToAnyMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals("orld", reader.consumeToAny('r', 'l', 'd'));
 }

 @Test
 public void testConsumeToAnySortedMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals("rldo", reader.consumeToAnySorted(new char[]{'r', 'd'}));
 }

 @Test
 public void testConsumeDataMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("&<foo&bar>"));
 Assert.assertEquals("&<foo&bar>", reader.consumeData());
 }

 @Test
 public void testConsumeTagNameMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("<title>Test</title>"));
 Assert.assertEquals("<title>", reader.consumeTagName());
 }

 @Test
 public void testConsumeToEndMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals("World", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequenceMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals("Hello", reader.consumeLetterSequence());
 }

 @Test
 public void testConsumeLetterThenDigitSequenceMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello1234"));
 Assert.assertEquals("Hello123", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testMatchesMethodWithSingleChar() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matches('l'));
 }

 @Test
 public void testMatchesMethodWithMultiChars() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matches("ll"));
 }

 @Test
 public void testMatchesIgnoreCaseMethodWithSingleChar() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesIgnoreCase("L"));
 }

 @Test
 public void testMatchesIgnoreCaseMethodWithMultiChars() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesIgnoreCase("ll"));
 }

 @Test
 public void testMatchesAnyMethodWithSingleChar() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesAny('l'));
 }

 @Test
 public void testMatchesAnyMethodWithMultiChars() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesAny(new char[]{'l', 'o'}));
 }

 @Test
 public void testMatchesLetterMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigitMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsumeMethodWithSingleChar() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.matchConsume('l');
 Assert.assertEquals(7, reader.pos());
 }

 @Test
 public void testMatchConsumeMethodWithMultiChars() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 reader.matchConsume("ll");
 Assert.assertEquals(9, reader.pos());
 }

 @Test
 public void testContainsIgnoreCaseMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><title>Test</title></html>"));
 Assert.assertTrue(reader.containsIgnoreCase("/title"));
 }

 @Test
 public void testToStringMethod() {
 CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals("Hello World", reader.toString());
 }
}