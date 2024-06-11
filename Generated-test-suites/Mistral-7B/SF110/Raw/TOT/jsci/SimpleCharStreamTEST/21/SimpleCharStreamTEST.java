package JSci.maths.symbolic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private Reader mockInput;

 @Before
 public void setup() {
 mockInput = mock(Reader.class);
 charStream = new SimpleCharStream(mockInput, 1, 1, 4096);
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 assertEquals(-1, charStream.getBeginLine());
 assertEquals(1, charStream.getBeginColumn());
 assertEquals(c, charStream.readChar());
 }

 @Test
 public void testFillBuff() throws IOException {
 char[] buffer = new char[10];
 char[] expected = "HelloWorld".toCharArray();
 charStream.inputStream = new StringReader("HelloWorld");
 charStream.ExpandBuff(false);
 charStream.FillBuff();
 charStream.read(buffer, 0, buffer.length);
 assertArrayEquals(expected, buffer);
 }

 @Test(expected = IOException.class)
 public void testFillBuffWithIOException() throws IOException {
 charStream.inputStream = new StringReader("HelloWorld");
 charStream.FillBuff();
 charStream.inputStream.close();
 charStream.readChar();
 }

 @Test
 public void testGetLineColumn() {
 charStream.UpdateLineColumn('A');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('B');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('C');
 charStream.UpdateLineColumn('\n');

 assertEquals(2, charStream.getLine());
 assertEquals(8, charStream.getColumn());
 }

 @Test
 public void testUpdateLineColumnWithLF() {
 charStream.UpdateLineColumn('A');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('B');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('C');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('\n');

 assertEquals(3, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 }

 @Test
 public void testUpdateLineColumnWithCR() {
 charStream.UpdateLineColumn('A');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('B');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('C');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('D');

 assertEquals(3, charStream.getLine());
 assertEquals(4, charStream.getColumn());
 }

 @Test
 public void testBackup() {
 charStream.UpdateLineColumn('A');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('B');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('C');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('D');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('E');
 charStream.backup(2);

 assertEquals(3, charStream.getLine());
 assertEquals(5, charStream.getColumn());
 }

 @Test
 public void testReInit() throws IOException {
 charStream.UpdateLineColumn('A');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('B');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('C');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('D');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('E');
 charStream.backup(5);
 charStream.ReInit(mockInput, 1, 1, 4096);

 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 }

 @Test
 public void testGetImage() {
 charStream.UpdateLineColumn('A');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('B');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('C');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('D');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('E');
 charStream.backup(5);
 String image = charStream.GetImage();
 assertEquals("BCRD", image);
 }

 @Test
 public void testGetSuffix() {
 charStream.UpdateLineColumn('A');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('B');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('C');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('D');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('E');
 charStream.backup(5);
 char[] suffix = charStream.GetSuffix(3);
 assertEquals("CRD", new String(suffix));
 }
}