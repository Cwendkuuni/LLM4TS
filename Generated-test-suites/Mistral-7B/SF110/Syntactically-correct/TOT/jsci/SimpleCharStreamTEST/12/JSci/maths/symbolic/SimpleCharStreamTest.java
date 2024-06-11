package JSci.maths.symbolic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private Reader dummyReader;

 @Before
 public void setUp() throws Exception {
 dummyReader = new StringReader("some text for testing");
 charStream = new SimpleCharStream(dummyReader, 1, 1, 4096);
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 assertEquals(0, charStream.getBeginLine());
 assertEquals(0, charStream.getBeginColumn());
 assertEquals(c, charStream.readChar());
 }

 @Test
 public void testFillBuff() throws IOException {
 charStream.ExpandBuff(false);
 char c;
 for (int i = 0; i < 1000; i++) {
 c = charStream.readChar();
 assertTrue(c >= 0);
 }
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 charStream.UpdateLineColumn('\r');
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 charStream.UpdateLineColumn('\n');
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 charStream.UpdateLineColumn('\t');
 assertEquals(1, charStream.getLine());
 assertEquals(9, charStream.getColumn());
 charStream.UpdateLineColumn('a');
 assertEquals(1, charStream.getLine());
 assertEquals(10, charStream.getColumn());
 }

 @Test
 public void testReadChar() throws IOException {
 char c;
 for (int i = 0; i < 1000; i++) {
 c = charStream.readChar();
 assertTrue(c >= 0);
 }
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 charStream.FillBuff();
 charStream.inputStream.close();
 charStream.readChar();
 }

 @Test(expected = IllegalStateException.class)
 public void testReInitWithNullReader() {
 charStream.ReInit(null, 1, 1);
 }

 @Test
 public void testGetLineColumn() {
 int line = charStream.getLine();
 int column = charStream.getColumn();
 assertEquals(1, line);
 assertEquals(1, column);
 }

 @Test
 public void testGetEndLineColumn() throws IOException {
 charStream.readChar();
 charStream.readChar();
 charStream.UpdateLineColumn('\n');
 int line = charStream.getLine();
 int column = charStream.getColumn();
 assertEquals(2, line);
 assertEquals(1, column);
 }

 @Test
 public void testBackup() throws IOException {
 charStream.readChar();
 charStream.readChar();
 charStream.backup(2);
 char c1 = charStream.readChar();
 char c2 = charStream.readChar();
 assertEquals(c2, c1);
 }
}