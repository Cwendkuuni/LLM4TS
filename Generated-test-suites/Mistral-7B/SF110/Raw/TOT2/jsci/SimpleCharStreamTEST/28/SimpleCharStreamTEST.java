import org.junit.Test;
import static org.junit.Assert.*;
import JSci.maths.symbolic.SimpleCharStream;
import java.io.StringReader;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;

 @Test
 public void testBeginToken() throws Exception {
 charStream = new SimpleCharStream(new StringReader("Hello World"));
 assertEquals('H', charStream.BeginToken());
 }

 @Test(expected = IOException.class)
 public void testBeginToken_IOException() throws Exception {
 charStream = new SimpleCharStream(new StringReader(""));
 charStream.BeginToken();
 }

 @Test
 public void testUpdateLineColumn() throws Exception {
 charStream = new SimpleCharStream(new StringReader("Hello\rWorld\r\n"));
 charStream.BeginToken();
 charStream.updateLineColumn('H');
 charStream.updateLineColumn('\r');
 charStream.updateLineColumn('W');
 charStream.updateLineColumn('\r');
 charStream.updateLineColumn('o');
 charStream.updateLineColumn(' ');
 charStream.updateLineColumn('l');
 charStream.updateLineColumn('d');
 charStream.updateLineColumn('\n');
 assertEquals(1, charStream.getLine());
 assertEquals(11, charStream.getColumn());
 }

 @Test
 public void testReadChar() throws Exception {
 charStream = new SimpleCharStream(new StringReader("Hello World"));
 char c = charStream.readChar();
 assertEquals('H', c);
 c = charStream.readChar();
 assertEquals('e', c);
 c = charStream.readChar();
 assertEquals('l', c);
 // Test reading past EOF
 c = charStream.readChar();
 assertNull(c);
 }

 @Test
 public void testGetLineColumn() throws Exception {
 charStream = new SimpleCharStream(new StringReader("Hello World\r\nTest\r\n12345"));
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 charStream.UpdateLineColumn('H');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn(' ');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('l');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('T');
 charStream.UpdateLineColumn('e');
 charStream.UpdateLineColumn('s');
 charStream.UpdateLineColumn('t');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('1');
 charStream.UpdateLineColumn('2');
 charStream.UpdateLineColumn('3');
 charStream.UpdateLineColumn('4');
 charStream.UpdateLineColumn('5');
 assertEquals(2, charStream.getLine());
 assertEquals(15, charStream.getColumn());
 }

 @Test
 public void testBackup() throws Exception {
 charStream = new SimpleCharStream(new StringReader("Hello World"));
 charStream.BeginToken();
 charStream.backup(1);
 assertEquals('H', charStream.readChar());
 charStream.backup(5);
 assertEquals('W', charStream.readChar());
 charStream.backup(10);
 assertNull(charStream.readChar());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testBackup_IndexOutOfBoundsException() throws Exception {
 charStream = new SimpleCharStream(new StringReader("Hello World"));
 charStream.backup(100);
 }

 @Test
 public void testSimpleCharStreamConstructor() {
 charStream = new SimpleCharStream(null, 1, 1);
 assertNull(charStream.inputStream);
 charStream = new SimpleCharStream(new StringReader(""), 1, 1);
 assertNotNull(charStream.inputStream);
 }

 @Test
 public void testReInit() throws Exception {
 charStream = new SimpleCharStream(new StringReader("Hello World"));
 charStream.ReInit(new StringReader("Test"), 2, 2);
 assertEquals(2, charStream.getLine());
 assertEquals(2, charStream.getColumn());
 charStream.ReInit(new StringReader("Test"), 2, 2, 4096);
 assertEquals(2, charStream.getLine());
 assertEquals(2, charStream.getColumn());
 }

 @Test
 public void testGetImage() throws Exception {
 charStream = new SimpleCharStream(new StringReader("Hello World"));
 assertEquals("Hello World", SimpleCharStream.GetImage());
 }

 @Test
 public void testGetSuffix() throws Exception {
 charStream = new SimpleCharStream(new StringReader("Hello World"));
 char[] suffix = SimpleCharStream.GetSuffix(6);
 assertEquals("World", new String(suffix));
 }

 @Test
 public void testDone() throws Exception {
 charStream = new SimpleCharStream(new StringReader("Hello World"));
 SimpleCharStream.Done();
 assertNull(SimpleCharStream.buffer);
 assertNull(SimpleCharStream.bufline);
 assertNull(SimpleCharStream.bufcolumn);
 }

 @Test
 public void testAdjustBeginLineColumn() throws Exception {
 charStream = new SimpleCharStream(new StringReader("1\r\n2\r3\r4\r5"));
 SimpleCharStream.adjustBeginLineColumn(3, 1);
 assertEquals(3, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 }
}