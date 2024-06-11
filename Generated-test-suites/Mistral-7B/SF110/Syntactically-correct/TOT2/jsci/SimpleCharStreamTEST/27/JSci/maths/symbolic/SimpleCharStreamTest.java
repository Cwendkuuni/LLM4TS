import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private static final String TEST_CHARS = "Hello World!";
 private static final String TEST_CHARS_WITH_CR_LF = "Hello\r\nWorld!\r\n";
 private static final String TEST_TAB_CHARS = "\t\t\tHello World!";
 private SimpleCharStream charStream;

 @Before
 public void setUp() {
 charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_CHARS.getBytes()), 1, 1, 4096);
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 Assert.assertEquals('H', c);
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 char c;
 for (int i = 0; i < TEST_CHARS.length(); i++) {
 c = charStream.readChar();
 charStream.UpdateLineColumn(c);
 }
 int line = charStream.getLine();
 int column = charStream.getColumn();
 Assert.assertEquals(1, line);
 Assert.assertEquals(TEST_CHARS.length(), column);
 }

 @Test
 public void testReadChar() throws IOException {
 char c;
 for (int i = 0; i < TEST_CHARS.length(); i++) {
 c = charStream.readChar();
 Assert.assertEquals(TEST_CHARS.charAt(i), c);
 }
 Assert.assertEquals(-1, charStream.readChar());
 }

 @Test
 public void testGetLine() throws IOException {
 char c;
 for (int i = 0; i < TEST_CHARS.length(); i++) {
 c = charStream.readChar();
 charStream.UpdateLineColumn(c);
 }
 int line = charStream.getLine();
 Assert.assertEquals(1, line);
 }

 @Test
 public void testGetColumn() throws IOException {
 char c;
 for (int i = 0; i < TEST_CHARS.length(); i++) {
 c = charStream.readChar();
 charStream.UpdateLineColumn(c);
 }
 int column = charStream.getColumn();
 Assert.assertEquals(TEST_CHARS.length(), column);
 }

 @Test
 public void testGetEndLine() throws IOException {
 char c;
 for (int i = 0; i < TEST_CHARS.length(); i++) {
 c = charStream.readChar();
 charStream.UpdateLineColumn(c);
 }
 int line = charStream.getEndLine();
 Assert.assertEquals(1, line);
 }

 @Test
 public void testGetEndColumn() throws IOException {
 char c;
 for (int i = 0; i < TEST_CHARS.length(); i++) {
 c = charStream.readChar();
 charStream.UpdateLineColumn(c);
 }
 int column = charStream.getEndColumn();
 Assert.assertEquals(TEST_CHARS.length(), column);
 }

 @Test
 public void testGetBeginLine() throws IOException {
 char c;
 for (int i = 0; i < TEST_CHARS.length(); i++) {
 c = charStream.readChar();
 charStream.UpdateLineColumn(c);
 }
 int line = charStream.getBeginLine();
 Assert.assertEquals(1, line);
 }

 @Test
 public void testGetBeginColumn() throws IOException {
 char c;
 for (int i = 0; i < TEST_CHARS.length(); i++) {
 c = charStream.readChar();
 charStream.UpdateLineColumn(c);
 }
 int column = charStream.getBeginColumn();
 Assert.assertEquals(1, column);
 }

 @Test
 public void testBackup() throws IOException {
 char c;
 for (int i = 0; i < TEST_CHARS.length(); i++) {
 c = charStream.readChar();
 charStream.UpdateLineColumn(c);
 }
 charStream.backup(1);
 int column = charStream.getColumn();
 Assert.assertEquals(TEST_CHARS.length() - 1, column);
 }

 @Test
 public void testFillBuff() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_CHARS_WITH_CR_LF.getBytes()), 1, 1, 4096);
 char c;
 for (int i = 0; i < TEST_CHARS_WITH_CR_LF.length(); i++) {
 c = charStream.readChar();
 outputStream.write(c);
 }
 String result = outputStream.toString();
 String expected = "Hello\rWorld!\r";
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_CHARS.getBytes()), 1, 1, 4096);
 charStream.FillBuff();
 charStream.inputStream.close();
 char c = charStream.readChar();
 }

 @Test
 public void testGetImage() throws IOException {
 charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_TAB_CHARS.getBytes()), 1, 1, 4096);
 String result = charStream.GetImage();
 String expected = "\t\t\tHello World!";
 Assert.assertEquals(expected, result);
 }

}