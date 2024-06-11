import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.CharacterReader.TokeniserState;

public class CharacterReaderTest {

 @Test
 public void testCharacterReaderConstructorWithReader() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 Assert.assertEquals(5, reader.charBuf.length);
 }

 @Test
 public void testCharacterReaderConstructorWithString() {
 CharacterReader reader = new CharacterReader("testString");
 Assert.assertEquals(maxBufferLen, reader.charBuf.length);
 }

 @Test
 public void testBufferUp() throws IOException {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.bufferUp();
 Assert.assertEquals(maxBufferLen, reader.charBuf.length);
 }

 @Test
 public void testPos() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.advance();
 reader.advance();
 Assert.assertEquals(2, reader.pos());
 }

 @Test
 public void testIsEmpty() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 Assert.assertFalse(reader.isEmpty());
 reader.advance();
 reader.advance();
 Assert.assertTrue(reader.isEmpty());
 }

 @Test(expected = UncheckedIOException.class)
 public void testIOException() throws IOException {
 CharacterReader reader = new CharacterReader(new StringReader("test")) {
 @Override
 public void bufferUp() throws IOException {
 throw new IOException();
 }
 };
 reader.consume();
 }

 @Test
 public void testConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 Assert.assertEquals('t', reader.consume());
 Assert.assertEquals('e', reader.consume());
 }

 @Test
 public void testConsumeTo() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 Assert.assertEquals("est", reader.consumeTo('s'));
 reader.advance();
 reader.advance();
 Assert.assertEquals("st", reader.consumeTo('s'));
 }

 @Test
 public void testConsumeToEnd() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 Assert.assertEquals("test", reader.consumeToEnd());
 }

 @Test
 public void testConsumeLetterSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("testLetterSequence"));
 Assert.assertEquals("LetterSequence", reader.consumeLetterSequence().toUpperCase());
 }

 @Test
 public void testConsumeLetterThenDigitSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("testLetter1234"));
 Assert.assertEquals("Letter1234", reader.consumeLetterThenDigitSequence());
 }

 @Test
 public void testMatches() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 Assert.assertTrue(reader.matches('t'));
 }

 @Test
 public void testMatchesIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("Test"));
 Assert.assertTrue(reader.matchesIgnoreCase("test"));
 }

 @Test
 public void testMatchesAny() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 Assert.assertTrue(reader.matchesAny('t', 'e'));
 }

 @Test
 public void testMatchesAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 Assert.assertTrue(reader.matchesAnySorted(new char[]{'t', 'e'}));
 }

 @Test
 public void testConsumeTagName() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><head><title>Test</title></head>"));
 Assert.assertEquals("<title>", reader.consumeTagName());
 }

 @Test
 public void testConsumeData() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><body><script type=\"text/javascript\">//<![CDATA[ var s = document.createElement('script');s.type = 'text/javascript';s.src = 'test.js';document.head.appendChild(s);//]]>"));
 Assert.assertEquals("<!--[CDATA[ var s = document.createElement('script');s.type = 'text/javascript';s.src = 'test.js';document.head.appendChild(s);//]]>", reader.consumeData());
 }

 @Test
 public void testNextIndexOf() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 Assert.assertEquals(3, reader.nextIndexOf('s'));
 Assert.assertEquals(-1, reader.nextIndexOf('x'));
 }

 @Test
 public void testNextIndexOfWithCharSequence() {
 CharacterReader reader = new CharacterReader(new StringReader("test<html>"));
 Assert.assertEquals(4, reader.nextIndexOf("<html>"));
 }

 @Test
 public void testConsumeToAny() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><body><script type=\"text/javascript\">//<![CDATA[ var s = document.createElement('script');s.type = 'text/javascript';s.src = 'test.js';document.head.appendChild(s);//]]>"));
 String result = reader.consumeToAny(new char[]{'<', 's', 'c'});
 Assert.assertEquals("script type=\"text/javascript\" src = 'test.js'", result);
 }

 @Test
 public void testConsumeToAnySorted() {
 CharacterReader reader = new CharacterReader(new StringReader("test<html>"));
 String result = reader.consumeToAnySorted(new char[]{'<', 'h', 't', 'm'});
 Assert.assertEquals("html", result);
 }

 @Test
 public void testMatchesLetter() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 Assert.assertTrue(reader.matchesLetter());
 }

 @Test
 public void testMatchesDigit() {
 CharacterReader reader = new CharacterReader(new StringReader("test1234"));
 Assert.assertTrue(reader.matchesDigit());
 }

 @Test
 public void testMatchConsume() {
 CharacterReader reader = new CharacterReader(new StringReader("test<html>"));
 reader.matchConsume("<html>");
 Assert.assertEquals(5, reader.pos());
 }

 @Test
 public void testMatchConsumeIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("Test<html>"));
 reader.matchConsumeIgnoreCase("<html>");
 Assert.assertEquals(5, reader.pos());
 }

 @Test
 public void testContainsIgnoreCase() {
 CharacterReader reader = new CharacterReader(new StringReader("<html><body><title>Test</title></body></html>"));
 Assert.assertTrue(reader.containsIgnoreCase("title"));
 }

 @Test
 public void testMarkAndRewind() {
 CharacterReader reader = new CharacterReader(new StringReader("test"));
 reader.mark();
 reader.advance();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals(2, reader.pos());
 }
}