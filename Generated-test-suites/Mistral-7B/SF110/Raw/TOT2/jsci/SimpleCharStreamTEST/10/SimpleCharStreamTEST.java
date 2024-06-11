import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 @Test
 public void testBeginToken() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Hello world\n".getBytes()), 1, 1, 4096);
 assertEquals('H', scs.BeginToken());
 assertEquals(-1, scs.bufpos);
 }

 @Test(expected = IOException.class)
 public void testBeginToken_IOException() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 4096);
 scs.BeginToken();
 }

 @Test
 public void testUpdateLineColumn() {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("r\r\nHello world\r\n".getBytes()), 1, 1, 4096);
 scs.UpdateLineColumn('r');
 assertEquals(1, scs.line);
 assertEquals(1, scs.column);
 scs.UpdateLineColumn('\n');
 assertEquals(1, scs.line);
 assertEquals(1, scs.column);
 scs.UpdateLineColumn('H');
 assertEquals(1, scs.line);
 assertEquals(2, scs.column);
 scs.UpdateLineColumn('e');
 assertEquals(1, scs.line);
 assertEquals(3, scs.column);
 scs.UpdateLineColumn('\r');
 assertEquals(2, scs.line);
 assertEquals(1, scs.column);
 scs.UpdateLineColumn('\n');
 assertEquals(2, scs.line);
 assertEquals(2, scs.column);
 scs.UpdateLineColumn('w');
 assertEquals(2, scs.line);
 assertEquals(3, scs.column);
 scs.UpdateLineColumn('o');
 assertEquals(2, scs.line);
 assertEquals(4, scs.column);
 scs.UpdateLineColumn('r');
 assertEquals(2, scs.line);
 assertEquals(5, scs.column);
 scs.UpdateLineColumn('l');
 assertEquals(2, scs.line);
 assertEquals(6, scs.column);
 scs.UpdateLineColumn('d');
 assertEquals(2, scs.line);
 assertEquals(7, scs.column);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testExpandBuff_IllegalArgumentException() {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Hello world\n".getBytes()), 1, 1, 4096);
 scs.ExpandBuff(-1);
 }

 @Test
 public void testExpandBuff() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Hello world\n".getBytes()), 1, 1, 4096);
 scs.ExpandBuff(false);
 assertEquals(8192, scs.bufsize);
 }

 @Test
 public void testFillBuff() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Hello world\n".getBytes()), 1, 1, 4096);
 scs.FillBuff();
 assertEquals('H', scs.buffer[0]);
 assertEquals('e', scs.buffer[1]);
 assertEquals('l', scs.buffer[2]);
 assertEquals('l', scs.buffer[3]);
 assertEquals('o', scs.buffer[4]);
 assertEquals(' ', scs.buffer[5]);
 assertEquals('w', scs.buffer[6]);
 assertEquals('o', scs.buffer[7]);
 assertEquals('r', scs.buffer[8]);
 assertEquals('l', scs.buffer[9]);
 assertEquals('d', scs.buffer[10]);
 assertEquals('\n', scs.buffer[11]);
 assertEquals(12, scs.maxNextCharInd);
 }

 @Test
 public void testReadChar() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Hello world\n".getBytes()), 1, 1, 4096);
 assertEquals('H', scs.readChar());
 assertEquals('e', scs.readChar());
 assertEquals('l', scs.readChar());
 assertEquals('l', scs.readChar());
 assertEquals('o', scs.readChar());
 assertEquals(' ', scs.readChar());
 assertEquals('w', scs.readChar());
 assertEquals('o', scs.readChar());
 assertEquals('r', scs.readChar());
 assertEquals('l', scs.readChar());
 assertEquals('\n', scs.readChar());
 assertEquals(-1, scs.readChar());
 }

 @Test
 public void testGetLine() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Hello world\nHello again\n".getBytes()), 1, 1, 4096);
 assertEquals(1, scs.getLine());
 assertEquals(1, scs.getBeginLine());
 assertEquals("Hello", scs.GetImage().substring(0, 5));
 assertEquals("world\n", scs.GetImage().substring(5));
 assertEquals(1, scs.getEndLine());
 assertEquals(1, scs.getBeginColumn());
 assertEquals("Hello", scs.GetSuffix(5));
 assertEquals("world", scs.GetSuffix(6));
 assertEquals(6, scs.getEndColumn());
 scs.backup(1);
 assertEquals(2, scs.getLine());
 assertEquals(5, scs.getEndColumn());
 assertEquals("world", scs.GetImage().substring(5));
 scs.backup(1);
 assertEquals(1, scs.getLine());
 assertEquals(4, scs.getEndColumn());
 assertEquals("world", scs.GetImage().substring(4));
 }
}