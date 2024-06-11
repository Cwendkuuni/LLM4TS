package JSci.maths.symbolic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private int line, column;

 @Before
 public void setUp() {
 charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\r\nline2\r\nline3\r\n".getBytes()), 1, 1);
 line = charStream.getLine();
 column = charStream.getColumn();
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 assertEquals(line, 1);
 assertEquals(column, 1);
 assertEquals(c, '\r');
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 charStream.UpdateLineColumn('a');
 assertEquals(line, 1);
 assertEquals(column, 2);
 charStream.UpdateLineColumn('\n');
 assertEquals(line, 2);
 assertEquals(column, 1);
 }

 @Test
 public void testReadChar() throws IOException {
 char c;
 for (int i = 0; i < 5; i++) {
 c = charStream.readChar();
 assertEquals(c, (i % 2 == 0) ? '\r' : 't');
 }
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 charStream.readChar();
 charStream.FillBuff();
 charStream.readChar();
 }

 @Test
 public void testGetLineColumn() throws IOException {
 assertEquals(charStream.getLine(), 3);
 assertEquals(charStream.getColumn(), 1);
 assertEquals(charStream.getEndLine(), 3);
 assertEquals(charStream.getEndColumn(), 1);
 assertEquals(charStream.getBeginLine(), 1);
 assertEquals(charStream.getBeginColumn(), 1);
 }

 @Test
 public void testBackup() throws IOException {
 charStream.backup(2);
 assertEquals(charStream.getLine(), 1);
 assertEquals(charStream.getColumn(), 1);
 charStream.UpdateLineColumn('a');
 charStream.backup(1);
 assertEquals(charStream.getLine(), 1);
 assertEquals(charStream.getColumn(), 1);
 }

 @After
 public void tearDown() throws IOException {
 charStream.backup(SimpleCharStream.bufsize);
 charStream.FillBuff();
 charStream.Close();
 }
}