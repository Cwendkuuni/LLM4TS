package JSci.maths.symbolic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private Reader inputStream;

 @Before
 public void setUp() {
 inputStream = new StringReader("Hello\nWorld\n");
 charStream = new SimpleCharStream(inputStream, 1, 1, 4096);
 }

 @Test
 public void testBeginToken() {
 char c = charStream.BeginToken();
 assertEquals('\n', c);

 int beginLine = charStream.getBeginLine();
 int beginColumn = charStream.getBeginColumn();
 assertEquals(1, beginLine);
 assertEquals(1, beginColumn);
 }

 @Test
 public void testFillBuff() {
 charStream.FillBuff();
 char c = charStream.readChar();
 assertEquals('H', c);
 }

 @Test
 public void testUpdateLineColumn() {
 charStream.UpdateLineColumn('H');
 assertEquals(1, charStream.getLine());
 assertEquals(2, charStream.getColumn());
 }

 @Test
 public void testReadChar() {
 char c = charStream.readChar();
 assertEquals('H', c);

 c = charStream.readChar();
 assertEquals('e', c);
 }

 @Test
 public void testGetLineColumn() {
 char c = charStream.readChar();
 charStream.UpdateLineColumn(c);
 int line = charStream.getLine();
 int column = charStream.getColumn();
 assertEquals(1, line);
 assertEquals(3, column);
 }

 @Test
 public void testBackup() {
 char[] buffer = new char[5];
 charStream.read(buffer, 0, buffer.length);
 charStream.backup(buffer.length);
 char c = charStream.readChar();
 assertEquals('\n', c);
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 charStream.FillBuff();
 charStream.inputStream.close();
 char c = charStream.readChar();
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testArrayIndexOutOfBoundsException() {
 charStream.bufsize = 1;
 char c = charStream.readChar();
 }
}