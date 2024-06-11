import org.junit.Test;
import static org.junit.Assert.*;
import JSci.maths.symbolic.SimpleCharStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 @Test
 public void testBeginToken() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 4096);
 assertEquals('t', charStream.BeginToken());
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\r\ntest2\r\nline2".getBytes()), 1, 1, 4096);
 char c = 't';
 charStream.UpdateLineColumn(c);
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 c = '\r';
 charStream.UpdateLineColumn(c);
 assertEquals(1, charStream.getLine());
 assertEquals(2, charStream.getColumn());
 c = 't';
 charStream.UpdateLineColumn(c);
 assertEquals(2, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 c = '\n';
 charStream.UpdateLineColumn(c);
 assertEquals(2, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 c = 'l';
 charStream.UpdateLineColumn(c);
 assertEquals(2, charStream.getLine());
 assertEquals(2, charStream.getColumn());
 c = 'i';
 charStream.UpdateLineColumn(c);
 assertEquals(2, charStream.getLine());
 assertEquals(3, charStream.getColumn());
 c = 'n';
 charStream.UpdateLineColumn(c);
 assertEquals(2, charStream.getLine());
 assertEquals(4, charStream.getColumn());
 c = 'e';
 charStream.UpdateLineColumn(c);
 assertEquals(2, charStream.getLine());
 assertEquals(5, charStream.getColumn());
 }

 @Test
 public void testGetLineAndColumn() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\r\ntest2\r\nline2".getBytes()), 1, 1, 4096);
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 char c = 't';
 charStream.UpdateLineColumn(c);
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 c = '\r';
 charStream.UpdateLineColumn(c);
 assertEquals(1, charStream.getLine());
 assertEquals(2, charStream.getColumn());
 c = 'e';
 charStream.UpdateLineColumn(c);
 assertEquals(2, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 c = 's';
 charStream.UpdateLineColumn(c);
 assertEquals(2, charStream.getLine());
 assertEquals(2, charStream.getColumn());
 c = 't';
 charStream.UpdateLineColumn(c);
 assertEquals(2, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 }

 @Test
 public void testFillBuff() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
 char[] buffer = new char[5];
 int expectedLength = 0;
 char c;
 while ((c = charStream.readChar()) != -1) {
 buffer[expectedLength++] = c;
 }
 assertEquals(4, expectedLength);
 assertEquals("test", new String(buffer));
 }

 @Test(expected = IOException.class)
 public void testFillBuffWhenEndOfStreamIsReached() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
 char[] buffer = new char[5];
 int expectedLength = 0;
 while ((charStream.readChar()) != -1) {
 buffer[expectedLength++] = charStream.readChar();
 }
 fail("Should throw IOException when end of stream is reached");
 }

 @Test
 public void testBackup() throws IOException {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\r\ntest2\r\nline2".getBytes()), 1, 1, 4096);
 char c = 't';
 charStream.UpdateLineColumn(c);
 charStream.Backup(1);
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 c = '\r';
 charStream.UpdateLineColumn(c);
 charStream.Backup(1);
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 }
}