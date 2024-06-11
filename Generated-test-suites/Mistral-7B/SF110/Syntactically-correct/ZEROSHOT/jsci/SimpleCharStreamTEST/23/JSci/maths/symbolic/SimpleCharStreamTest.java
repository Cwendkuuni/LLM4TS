import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private static final String TEST_CHARS = "abc\r\ndef\r\t123\n456\t";
 private SimpleCharStream charStream;
 private byte[] testData;

 @Before
 public void setup() {
 testData = TEST_CHARS.getBytes();
 charStream = new SimpleCharStream(new ByteArrayInputStream(testData), 1, 1);
 }

 @Test
 public void testBeginToken() throws IOException {
 char actual = charStream.BeginToken();
 char expected = testData[0];
 assertEquals("Expected first character to be read.", expected, actual);
 }

 @Test(expected = IOException.class)
 public void testBeginToken_IOException() throws IOException {
 charStream.BeginToken();
 charStream.inputStream.close();
 charStream.BeginToken();
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 charStream.BeginToken();
 char c = charStream.readChar();
 UpdateLineColumn(c);
 int line = charStream.getLine();
 int column = charStream.getColumn();
 assertEquals("Line and column should be set correctly.", 1, line);
 assertEquals("Column should be set correctly.", 1, column);
 }

 @Test
 public void testReadChar() throws IOException {
 char actual = charStream.readChar();
 char expected = testData[0];
 assertEquals("Expected first character to be read.", expected, actual);
 }

 @Test(expected = IOException.class)
 public void testReadChar_IOException() throws IOException {
 charStream.readChar();
 charStream.inputStream.close();
 charStream.readChar();
 }

 @Test
 public void testGetColumn() {
 int actual = charStream.getColumn();
 int expected = 1;
 assertEquals("Column should be set correctly.", expected, actual);
 }

 @Test
 public void testGetLine() {
 int actual = charStream.getLine();
 int expected = 1;
 assertEquals("Line should be set correctly.", expected, actual);
 }

 @Test
 public void testGetEndColumn() {
 int actual = charStream.getEndColumn();
 int expected = 1;
 assertEquals("End column should be set correctly.", expected, actual);
 }

 @Test
 public void testGetEndLine() {
 int actual = charStream.getEndLine();
 int expected = 1;
 assertEquals("End line should be set correctly.", expected, actual);
 }

 @Test
 public void testGetBeginColumn() {
 int actual = charStream.getBeginColumn();
 int expected = 1;
 assertEquals("Begin column should be set correctly.", expected, actual);
 }

 @Test
 public void testGetBeginLine() {
 int actual = charStream.getBeginLine();
 int expected = 1;
 assertEquals("Begin line should be set correctly.", expected, actual);
 }

 @Test
 public void testBackup() throws IOException {
 charStream.BeginToken();
 char c = charStream.readChar();
 charStream.backup(1);
 char streamChar = charStream.readChar();
 char expectedChar = testData[0];
 assertEquals("Expected character read after backup to be the previous one.", expectedChar, streamChar);
 }

 @Test(expected = IOException.class)
 public void testBackup_IOException() throws IOException {
 charStream.BeginToken();
 char c = charStream.readChar();
 charStream.backup(-1);
 charStream.readChar();
 charStream.inputStream.close();
 charStream.backup(1);
 charStream.readChar();
 }

 @Test
 public void testReInit() throws IOException {
 charStream.ReInit(new ByteArrayInputStream(testData), 1, 1);
 char actual = charStream.readChar();
 char expected = testData[0];
 assertEquals("Expected first character to be read afterReInit.", expected, actual);
 }

 @Test(expected = IOException.class)
 public void testReInit_IOException() throws IOException {
 charStream.ReInit(new ByteArrayInputStream(testData), 1, 1);
 charStream.inputStream.close();
 charStream.ReInit(new ByteArrayInputStream(testData), 1, 1);
 charStream.readChar();
 }

 @Test
 public void testGetImage() {
 String actual = SimpleCharStream.GetImage();
 String expected = TEST_CHARS;
 assertEquals("Expected image to match test data.", expected, actual);
 }

 @Test
 public void testGetSuffix() {
 char[] actual = SimpleCharStream.GetSuffix(TEST_CHARS.length());
 char[] expected = TEST_CHARS.toCharArray();
 assertArrayEquals("Expected suffix to match test data.", expected, actual);
 }

 @Test
 public void testDone() {
 SimpleCharStream.Done();
 assertTrue("Expected buffer to be null after Done.", SimpleCharStream.buffer == null);
 assertTrue("Expected bufline to be null after Done.", SimpleCharStream.bufline == null);
 assertTrue("Expected bufcolumn to be null after Done.", SimpleCharStream.bufcolumn == null);
 }
}