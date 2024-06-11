import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;

 @Before
 public void setUp() {
 charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1);
 }

 @Test
 public void testBeginToken() throws IOException {
 char expectedChar = '\r';
 char actualChar = charStream.BeginToken();
 assertEquals(expectedChar, actualChar);
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 char charToRead;
 for (int i = 0; i < 10; i++) {
 charToRead = charStream.readChar();
 charStream.UpdateLineColumn(charToRead);
 }

 // Test for normal character
 assertEquals(1, charStream.getLine());
 assertEquals(11, charStream.getColumn());

 // Test for CR character
 charStream.UpdateLineColumn('\r');
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());

 // Test for LF character
 charStream.UpdateLineColumn('\n');
 assertEquals(2, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 }

 @Test
 public void testReadChar() throws IOException {
 char expectedChar;
 char actualChar;
 for (int i = 0; i < 10; i++) {
 expectedChar = (char) ('a' + i);
 actualChar = charStream.readChar();
 assertEquals(expectedChar, actualChar);
 }
 }

 @Test
 public void testGetLineAndColumn() throws IOException {
 char charToRead;
 charStream.backup(1);
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('c');

 // Test for getLine and getColumn when reading a character
 expectedChar = 'c';
 actualChar = charStream.readChar();
 assertEquals(4, charStream.getLine());
 assertEquals(6, charStream.getColumn());

 // Test for getLine and getColumn when backing up a character
 charStream.backup(1);
 expectedChar = 'b';
 actualChar = charStream.readChar();
 assertEquals(4, charStream.getLine());
 assertEquals(5, charStream.getColumn());

 // Test for getLine and getColumn when reading CR and LF characters
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('\n');
 assertEquals(5, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 assertThrows(IOException.class, () -> charStream.BeginToken());
 }

 @Test(expected = IOException.class)
 public void testIOExceptionWhenReadingChar() throws IOException {
 charStream.backup(1);
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('c');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('d');

 // Test for IOException when trying to read a character after reaching the end of the stream
 charStream.backup(charStream.bufsize());
 assertEquals(7, charStream.getLine());
 assertEquals(1, charStream.getColumn());

 charStream.readChar();
 assertThrows(IOException.class, () -> charStream.readChar());
 }
}