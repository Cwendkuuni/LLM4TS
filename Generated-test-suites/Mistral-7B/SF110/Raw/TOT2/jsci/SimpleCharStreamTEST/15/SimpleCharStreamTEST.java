package JSci.maths.symbolic.test;

import org.junit.Test;
import static org.junit.Assert.*;

import JSci.maths.symbolic.SimpleCharStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 @Test
 public void testBeginToken() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abcdefg".getBytes()), 1, 1, 4096);
 assertEquals('a', charStream.BeginToken());
 }

 @Test(expected = IOException.class)
 public void testBeginTokenWithIOException() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abcdefg".getBytes()), 1, 1, 4096);
 charStream.inputStream = new ByteArrayInputStream(new byte[0]);
 charStream.BeginToken();
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abc\r\ndef\tghi\rjkl\nmno\r\n".getBytes()), 1, 1, 4096);
 char c;
 for (int i = 0; i < 12; i++) {
 c = charStream.readChar();
 charStream.UpdateLineColumn(c);
 }
 int expectedLine = 4;
 int expectedColumn = 9;
 int actualLine = charStream.getLine();
 int actualColumn = charStream.getColumn();
 assertEquals("Unexpected line and column after reading 12 characters.", expectedLine, actualLine);
 assertEquals("Unexpected line and column after reading 12 characters.", expectedColumn, actualColumn);
 }

 @Test
 public void testReadChar() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abcdefg".getBytes()), 1, 1, 4096);
 char c;
 for (int i = 0; i < 7; i++) {
 c = charStream.readChar();
 assertEquals("Unexpected character after reading " + i + " characters.", 'a' + i, c);
 }
 assertEquals("Unexpected character after reading 7 characters.", -1, charStream.readChar());
 }

 @Test(expected = IOException.class)
 public void testReadCharWithIOException() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abcdefg".getBytes()), 1, 1, 4096);
 charStream.inputStream = new ByteArrayInputStream(new byte[0]);
 charStream.readChar();
 }

 @Test
 public void testGetColumn() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abc\r\ndef\tghi\rjkl\nmno\r\n".getBytes()), 1, 1, 4096);
 int expectedColumn;
 int actualColumn;
 for (int i = 0; i < 12; i++) {
 char c = charStream.readChar();
 expectedColumn = i + 1;
 actualColumn = charStream.getColumn();
 assertEquals("Unexpected column after reading character " + (char) c, expectedColumn, actualColumn);
 }
 }

 @Test
 public void testGetLine() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abc\r\ndef\tghi\rjkl\nmno\r\n".getBytes()), 1, 1, 4096);
 int expectedLine;
 int actualLine;
 for (int i = 0; i < 6; i++) {
 char c = charStream.readChar();
 expectedLine = i + 1;
 actualLine = charStream.getLine();
 assertEquals("Unexpected line number after reading character " + (char) c, expectedLine, actualLine);
 }
 }

 @Test
 public void testGetBeginColumn() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abcdefg".getBytes()), 1, 1, 4096);
 int expectedBeginColumn = 1;
 int actualBeginColumn = charStream.getBeginColumn();
 assertEquals("Unexpected begin column.", expectedBeginColumn, actualBeginColumn);
 }

 @Test
 public void testGetBeginLine() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abcdefg".getBytes()), 1, 1, 4096);
 int expectedBeginLine = 1;
 int actualBeginLine = charStream.getBeginLine();
 assertEquals("Unexpected begin line.", expectedBeginLine, actualBeginLine);
 }

 @Test
 public void testBackup() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abcdefg".getBytes()), 1, 1, 4096);
 char c = charStream.readChar();
 charStream.backup(1);
 charStream.UpdateLineColumn(c);
 int expectedLine = 1;
 int expectedColumn = 1;
 int actualLine = charStream.getLine();
 int actualColumn = charStream.getColumn();
 assertEquals("Unexpected line and column after backing up.", expectedLine, actualLine);
 assertEquals("Unexpected line and column after backing up.", expectedColumn, actualColumn);
 }

 @Test
 public void testReInit() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abcdefg".getBytes()), 1, 1, 4096);
 charStream.ReInit(new ByteArrayInputStream("hijklmnop".getBytes()), 7, 8, 4096);
 char c = charStream.readChar();
 assertEquals("Unexpected character after reinitializing.", 'h', c);
 }

 @Test
 public void testGetImage() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abcdefg".getBytes()), 1, 1, 4096);
 String expectedImage = "abcdefg";
 String actualImage = SimpleCharStream.GetImage();
 assertEquals("Unexpected image.", expectedImage, actualImage);
 }

 @Test
 public void testGetSuffix() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abcdefg".getBytes()), 1, 1, 4096);
 char[] expectedSuffix = new char[]{'g'};
 char[] actualSuffix = SimpleCharStream.GetSuffix(1);
 assertArrayEquals("Unexpected suffix.", expectedSuffix, actualSuffix);
 }

 @Test
 public void testDone() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abcdefg".getBytes()), 1, 1, 4096);
 SimpleCharStream.Done();
 }

 @Test
 public void testAdjustBeginLineColumn() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("abc\r\ndef\tghi\rjkl\nmno\r\n".getBytes()), 1, 1, 4096);
 SimpleCharStream.adjustBeginLineColumn(2, 2);
 int expectedLine = 2;
 int expectedColumn = 2;
 int actualLine = charStream.getLine();
 int actualColumn = charStream.getColumn();
 assertEquals("Unexpected line and column after adjusting begin line and column.", expectedLine, actualLine);
 assertEquals("Unexpected line and column after adjusting begin line and column.", expectedColumn, actualColumn);
 }
}