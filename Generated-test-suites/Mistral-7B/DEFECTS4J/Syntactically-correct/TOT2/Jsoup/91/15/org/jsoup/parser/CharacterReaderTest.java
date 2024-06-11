import org.jsoup.parser.CharacterReader;
import org.jsoup.helper.Validate;
import org.junit.Assert;
import org.junit.Test;

public class CharacterReaderTest {

 private CharacterReader reader;

 @Test
 public void testCharacterReaderConstructorWithReader() {
 reader = new CharacterReader(new StringReader("Hello World"));
 Validate.notNull(reader);
 Validate.isTrue(reader.markSupported());
 }

 @Test
 public void testCharacterReaderConstructorWithString() {
 reader = new CharacterReader("Hello World");
 Validate.notNull(reader);
 Validate.isTrue(reader.markSupported());
 }

 @Test
 public void testCharacterReaderBufferUp() throws IOException {
 reader = new CharacterReader(new StringReader("Hello World"));
 reader.bufferUp();
 Assert.assertEquals(5, reader.bufLength);
 }

 @Test
 public void testCharacterReaderPos() {
 reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals(0, reader.pos());
 }

 @Test
 public void testCharacterReaderIsEmpty() {
 reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertFalse(reader.isEmpty());
 }

 @Test
 public void testCharacterReaderCurrent() {
 reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals('H', reader.current());
 reader.consume();
 Assert.assertEquals('H', reader.current());
 reader.consume();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testCharacterReaderConsume() {
 reader = new CharacterReader(new StringReader("Hello World"));
 char consumedChar = reader.consume();
 Assert.assertEquals('H', consumedChar);
 Assert.assertEquals(4, reader.bufLength);
 Assert.assertEquals(1, reader.bufPos);
 }

 @Test
 public void testCharacterReaderUnconsume() {
 reader = new CharacterReader(new StringReader("Hello World"));
 reader.consume();
 reader.unconsume();
 Assert.assertEquals('H', reader.current());
 }

 @Test
 public void testCharacterReaderAdvance() {
 reader = new CharacterReader(new StringReader("Hello World"));
 reader.advance();
 Assert.assertEquals('e', reader.current());
 }

 @Test
 public void testCharacterReaderMark() {
 reader = new CharacterReader(new StringReader("Hello World"));
 reader.mark();
 reader.advance();
 reader.rewindToMark();
 Assert.assertEquals('H', reader.current());
 }

 @Test
 public void testCharacterReaderNextIndexOf() {
 reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals(6, reader.nextIndexOf(' '));
 Assert.assertEquals(-1, reader.nextIndexOf('Z'));
 }

 @Test
 public void testCharacterReaderNextIndexOfWithSequence() {
 reader = new CharacterReader(new StringReader("Hello World"));
 Assert.assertEquals(5, reader.nextIndexOf("ll"));
 Assert.assertEquals(-1, reader.nextIndexOf("ZZ"));
 }

 @Test
 public void testCharacterReaderConsumeTo() {
 reader = new CharacterReader(new StringReader("Hello World"));
 String consumed = reader.consumeTo('l');
 Assert.assertEquals("ello", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToEnd() {
 reader = new CharacterReader(new StringReader("Hello World"));
 String consumed = reader.consumeToEnd();
 Assert.assertEquals("Hello World", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToAny() {
 reader = new CharacterReader(new StringReader("Hello World"));
 String consumed = reader.consumeToAny('l');
 Assert.assertEquals("ello", consumed);
 }

 @Test
 public void testCharacterReaderConsumeToAnySorted() {
 reader = new CharacterReader(new StringReader("Hello World"));
 String consumed = reader.consumeToAnySorted(new char[]{'l', 'o'});
 Assert.assertEquals("ello", consumed);
 }

 @Test
 public void testCharacterReaderConsumeData() {
 reader = new CharacterReader(new StringReader("&<null"));
 String consumed = reader.consumeData();
 Assert.assertEquals("&<null", consumed);
 }

 @Test
 public void testCharacterReaderConsumeTagName() {
 reader = new CharacterReader(new StringReader("<title>Test</title>"));
 String consumed = reader.consumeTagName();
 Assert.assertEquals("title", consumed);
 }

 @Test(expected = UncheckedIOException.class)
 public void testCharacterReaderIOException() throws IOException {
 reader = new CharacterReader(new StringReader(""));
 reader.bufferUp();
 reader.reader.close();
 reader.bufferUp(); // should throw an IOException
 }

 @Test
 public void testCharacterReaderToString() {
 reader = new CharacterReader(new StringReader("Hello World"));
 String string = reader.toString();
 Assert.assertEquals("Hello World", string);
 }
}