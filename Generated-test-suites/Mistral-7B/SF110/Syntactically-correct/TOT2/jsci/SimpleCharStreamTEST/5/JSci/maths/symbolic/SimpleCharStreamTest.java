import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private int expectedLine;
 private int expectedColumn;
 private int expectedEndLine;
 private int expectedEndColumn;
 private int expectedBeginLine;
 private int expectedBeginColumn;
 private char[] expectedImage;

 @Before
 public void setUp() {
 charStream = new SimpleCharStream(new ByteArrayInputStream("Test String".getBytes()), 1, 1, 4096);
 expectedLine = 1;
 expectedColumn = 1;
 expectedEndLine = 1;
 expectedEndColumn = 6;
 expectedBeginLine = 1;
 expectedBeginColumn = 1;
 expectedImage = new char[]{'T', 'e', 's', 't', ' ', 'S', 't', 'r', 'i', 'n', 'g', '\n'};
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 assertEquals(expectedLine, charStream.getLine());
 assertEquals(expectedColumn, charStream.getColumn());
 assertEquals(c, charStream.readChar());
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 for (char c : "Test\r\n String".toCharArray()) {
 charStream.UpdateLineColumn(c);
 assertEquals(charStream.getLine(), expectedLine++);
 assertEquals(charStream.getColumn(), expectedColumn++);
 }
 }

 @Test
 public void testReadChar() throws IOException {
 int i = 0;
 char expectedChar;
 while ((expectedChar = "Test\r\n String".charAt(i++)) != '\0') {
 char c = charStream.readChar();
 assertEquals(expectedChar, c);
 }
 assertEquals(expectedEndLine, charStream.getLine());
 assertEquals(expectedEndColumn, charStream.getColumn());
 }

 @Test
 public void testGetLineColumn() throws IOException {
 assertEquals(expectedLine, charStream.getLine());
 assertEquals(expectedColumn, charStream.getColumn());
 assertEquals(expectedEndLine, charStream.getEndLine());
 assertEquals(expectedEndColumn, charStream.getEndColumn());
 assertEquals(expectedBeginLine, charStream.getBeginLine());
 assertEquals(expectedBeginColumn, charStream.getBeginColumn());
 }

 @Test
 public void testBackup() throws IOException {
 charStream.backup(2);
 assertEquals(charStream.getLine(), expectedLine - 2);
 assertEquals(charStream.getColumn(), expectedColumn - 2);
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 charStream.FillBuff();
 charStream.FillBuff();
 char c = charStream.readChar();
 }

 @Test
 public void testGetImage() throws IOException {
 char[] image = charStream.GetImage();
 assertEquals(expectedImage.length, image.length);
 for (int i = 0; i < image.length; i++) {
 assertEquals(expectedImage[i], image[i]);
 }
 }

 @Test
 public void testGetSuffix() throws IOException {
 char[] suffix = charStream.GetSuffix(5);
 assertEquals(5, suffix.length);
 assertEquals("Strin", new String(suffix));
 }

 @Test
 public void testDone() throws IOException {
 charStream.Done();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReInitNegativeBuffersize() {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("Test String".getBytes()), 1, 1, -4096);
 }

}