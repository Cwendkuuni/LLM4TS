package JSci.maths.symbolic.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
 char c = charStream.BeginToken();
 assertEquals('\r', c);
 }

 @Test(expected = IOException.class)
 public void testBeginToken_IOException() throws IOException {
 charStream.BeginToken();
 charStream.inputStream.close();
 charStream.BeginToken();
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 charStream.UpdateLineColumn('\r');
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());

 charStream.UpdateLineColumn('\n');
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());

 charStream.UpdateLineColumn('t');
 assertEquals(1, charStream.getLine());
 assertEquals(9, charStream.getColumn());
 }

 @Test
 public void testReadChar() throws IOException {
 char expected = '\r';
 char actual;

 actual = charStream.readChar();
 assertEquals(expected, actual);
 assertEquals(0, charStream.getColumn());
 assertEquals(1, charStream.getLine());

 expected = '\n';
 actual = charStream.readChar();
 assertEquals(expected, actual);
 assertEquals(0, charStream.getColumn());
 assertEquals(1, charStream.getLine());

 expected = 't';
 actual = charStream.readChar();
 assertEquals(expected, actual);
 assertEquals(9, charStream.getColumn());
 assertEquals(1, charStream.getLine());
 }

 @Test
 public void testGetColumn() throws IOException {
 assertEquals(0, charStream.getColumn());

 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('t');
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('b');

 assertEquals(9, charStream.getColumn());
 }

 @Test
 public void testGetLine() throws IOException {
 assertEquals(1, charStream.getLine());

 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('t');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('s');
 charStream.UpdateLineColumn('t');

 assertEquals(2, charStream.getLine());
 }

 @Test
 public void testGetEndColumn() throws IOException {
 assertEquals(0, charStream.getEndColumn());

 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('t');
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('b');

 assertEquals(9, charStream.getEndColumn());
 }

 @Test
 public void testGetEndLine() throws IOException {
 assertEquals(1, charStream.getEndLine());

 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('t');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('s');
 charStream.UpdateLineColumn('t');

 assertEquals(2, charStream.getEndLine());
 }

 @Test
 public void testGetBeginColumn() throws IOException {
 assertEquals(-1, charStream.getBeginColumn());

 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('t');
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('b');

 assertEquals(0, charStream.getBeginColumn());
 }

 @Test
 public void testGetBeginLine() throws IOException {
 assertEquals(1, charStream.getBeginLine());

 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('t');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('s');
 charStream.UpdateLineColumn('t');

 assertEquals(2, charStream.getBeginLine());
 }

 @Test
 public void testBackup() throws IOException {
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('t');
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('c');
 charStream.UpdateLineColumn('d');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('f');
 charStream.UpdateLineColumn('g');

 assertEquals(13, charStream.getBeginLine());
 assertEquals(9, charStream.getBeginColumn());

 charStream.backup(3);
 assertEquals(10, charStream.getBeginLine());
 assertEquals(9, charStream.getBeginColumn());

 charStream.backup(5);
 assertEquals(6, charStream.getBeginLine());
 assertEquals(4, charStream.getBeginColumn());
 }

 @Test
 public void testFillBuff() throws IOException {
 char[] buffer = new char[20];
 int available;

 charStream.FillBuff();
 available = charStream.available();
 assertEquals(4096, available);
 System.arraycopy(charStream.buffer, 0, buffer, 0, available);
 assertEquals("test\r\ntest\r\n", new String(buffer, 0, available));
 }

 @Test
 public void testExpandBuff() throws IOException {
 charStream.ExpandBuff(false);
 assertEquals(6144, charStream.bufsize);
 assertEquals(6144, charStream.available());

 charStream.ExpandBuff(true);
 assertEquals(8192, charStream.bufsize);
 assertEquals(8192, charStream.available());
 }
}