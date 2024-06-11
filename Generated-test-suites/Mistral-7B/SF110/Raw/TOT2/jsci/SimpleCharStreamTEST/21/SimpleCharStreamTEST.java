import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;

 @Test
 public void testBeginToken() throws IOException {
 charStream = new SimpleCharStream(new StringReader("Hello World\n"), 1, 1, 4096);
 assertEquals('H', charStream.BeginToken());
 }

 @Test(expected = IOException.class)
 public void testBeginTokenWithIOException() throws IOException {
 charStream = new SimpleCharStream(new StringReader("Hello World\n"), 1, 1, 4096);
 charStream.inputStream.close();
 assertEquals('H', charStream.BeginToken()); // This should throw an IOException
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 charStream = new SimpleCharStream(new StringReader("Hello\nWorld\n"), 1, 1, 4096);
 char c = 'H';
 charStream.readChar();
 charStream.UpdateLineColumn(c);
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 c = '\n';
 charStream.readChar();
 charStream.UpdateLineColumn(c);
 assertEquals(2, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 }

 @Test
 public void testReadChar() throws IOException {
 charStream = new SimpleCharStream(new StringReader("Hello World\n"), 1, 1, 4096);
 assertEquals('H', charStream.readChar());
 assertEquals('\r', charStream.readChar()); // assumes that the '\r' character is present in the input stream
 assertEquals('\n', charStream.readChar());
 assertEquals(-1, charStream.readChar()); // end of stream
 }

 @Test(expected = IOException.class)
 public void testReadCharWithIOException() throws IOException {
 charStream = new SimpleCharStream(new StringReader("Hello World\n"), 1, 1, 4096);
 charStream.inputStream.close();
 assertEquals(-1, charStream.readChar()); // This should throw an IOException
 }

 @Test
 public void testGetLineAndColumn() throws IOException {
 charStream = new SimpleCharStream(new StringReader("Hello\nWorld\n"), 1, 1, 4096);
 char c = 'H';
 charStream.readChar();
 charStream.UpdateLineColumn(c);
 charStream.readChar();
 charStream.UpdateLineColumn(c = '\n');
 assertEquals(2, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 charStream.UpdateLineColumn(c);
 assertEquals(2, charStream.getLine());
 assertEquals(2, charStream.getColumn());
 }

 @Test
 public void testBackup() throws IOException {
 charStream = new SimpleCharStream(new StringReader("Hello World\n"), 1, 1, 4096);
 char c = 'H';
 charStream.readChar();
 charStream.backup(1);
 assertEquals('H', charStream.readChar());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testBackupWithIndexOutOfBoundsException() throws IOException {
 charStream = new SimpleCharStream(new StringReader("Hello World\n"), 1, 1, 4096);
 char c = 'H';
 charStream.readChar();
 charStream.backup(SimpleCharStream.bufsize + 1);
 }

 @Test
 public void testConstructor() {
 charStream = new SimpleCharStream(null, 1, 1, 4096);
 assertNull(charStream.inputStream);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullReader() {
 charStream = new SimpleCharStream(null, 1, 1, 4096);
 }

 @Test
 public void testExpandBuff() throws IOException {
 charStream = new SimpleCharStream(new StringReader(""), 1, 1, 4096);
 charStream.ExpandBuff(true);
 assertEquals(8192, charStream.bufsize);
 }

 @Test
 public void testFillBuff() throws IOException {
 charStream = new SimpleCharStream(new StringReader(""), 1, 1, 4096);
 charStream.ExpandBuff(true);
 char c = 'A';
 for (int i = 0; i < 4095; i++) {
 charStream.readChar();
 charStream.FillBuff();
 assertEquals(c++, charStream.readChar());
 }
 }

 @Test(expected = IOException.class)
 public void testFillBuffWithIOException() throws IOException {
 charStream = new SimpleCharStream(new StringReader(""), 1, 1, 4096);
 charStream.ExpandBuff(true);
 charStream.inputStream.close();
 charStream.FillBuff();
 }
}