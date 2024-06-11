package JSci.maths.symbolic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;

 @Before
 public void setUp() {
 charStream = new SimpleCharStream(new StringReader("test\r\ntest1\r\n2test\r\n3test\n4test"));
 charStream.backup(2);
 }

 @After
 public void tearDown() {
 SimpleCharStream.Done();
 charStream = null;
 }

 @Test
 public void testBeginToken() {
 char c = charStream.BeginToken();
 assertEquals('\r', c);
 char column = charStream.getColumn();
 char line = charStream.getLine();
 assertEquals(1, line);
 assertEquals(1, column);
 }

 @Test
 public void testFillBuff() throws Exception {
 char c;
 char column;
 char line;
 charStream.FillBuff();
 c = charStream.readChar();
 column = charStream.getColumn();
 line = charStream.getLine();
 assertEquals('\n', c);
 assertEquals(2, line);
 assertEquals(1, column);
 c = charStream.readChar();
 assertEquals('t', c);
 assertEquals(2, line);
 assertEquals(2, column);
 }

 @Test(expected = IOException.class)
 public void testFillBuffWithIOException() throws Exception {
 charStream.FillBuff();
 charStream.inputStream.close();
 char c = charStream.readChar();
 fail("IOException expected");
 }

 @Test
 public void testGetColumn() {
 assertEquals(1, charStream.getColumn());
 }

 @Test
 public void testGetEndColumn() {
 assertEquals(8, charStream.getEndColumn());
 }

 @Test
 public void testGetEndLine() {
 assertEquals(5, charStream.getEndLine());
 }

 @Test
 public void testGetLine() {
 assertEquals(1, charStream.getLine());
 }

 @Test
 public void testGetSuffix() {
 char[] suffix = charStream.GetSuffix(3);
 assertEquals("est", new String(suffix));
 }

 @Test
 public void testReadChar() throws Exception {
 char c;
 char column;
 char line;
 charStream.FillBuff();
 c = charStream.readChar();
 column = charStream.getColumn();
 line = charStream.getLine();
 assertEquals('t', c);
 assertEquals(2, line);
 assertEquals(3, column);
 c = charStream.readChar();
 assertEquals('\n', c);
 assertEquals(2, line);
 assertEquals(4, column);
 }

 @Test
 public void testUpdateLineColumn() throws Exception {
 char c = charStream.readChar();
 assertEquals(1, charStream.getColumn());
 assertEquals(1, charStream.getLine());
 charStream.UpdateLineColumn(c);
 char line2 = charStream.getLine();
 char column2 = charStream.getColumn();
 assertEquals(2, line2);
 assertEquals(1, column2);
 }
}