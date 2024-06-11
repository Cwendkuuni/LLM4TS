import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;

 @Test
 public void testBeginToken() throws IOException {
 charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals('t', charStream.BeginToken());
 }

 @Test(expected = IOException.class)
 public void testBeginTokenWithEmptyStream() throws IOException {
 charStream = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1);
 charStream.BeginToken();
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n test\t".getBytes()), 1, 1);
 char c = charStream.readChar();
 UpdateLineColumn(c);
 assertEquals(2, charStream.getLine());
 assertEquals(13, charStream.getColumn());
 }

 @Test
 public void testReadChar() throws IOException {
 charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n test\t".getBytes()), 1, 1);
 assertEquals('t', charStream.readChar());
 assertEquals(1, charStream.getColumn());
 }

 @Test
 public void testGetLineAndColumn() throws IOException {
 charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n test\t".getBytes()), 1, 1);
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 char c = charStream.readChar();
 UpdateLineColumn(c);
 assertEquals(1, charStream.getLine());
 assertEquals(2, charStream.getColumn());
 }

 @Test
 public void testExpandBuff() throws IOException {
 charStream = new SimpleCharStream(new InputStreamReader(System.in), 1, 1, 1024);
 for (int i = 0; i < 1024 * 2; i++) {
 charStream.readChar();
 }
 charStream.ExpandBuff(true);
 char c = charStream.readChar();
 assertNotEquals(-1, c);
 }

 @Test
 public void testFillBuff() throws IOException {
 charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n test\t".getBytes()), 1, 1);
 assertEquals(0, charStream.available);
 char c = charStream.readChar();
 UpdateLineColumn(c);
 charStream.FillBuff();
 assertEquals(1024, charStream.available);
 }

 @Test(expected = IOException.class)
 public void testFillBuffWithEmptyStream() throws IOException {
 charStream = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1);
 charStream.FillBuff();
 }
}