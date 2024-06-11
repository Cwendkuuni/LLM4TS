import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;

 @Before
 public void setUp() {
 charStream = new SimpleCharStream(new ByteArrayInputStream("test\nsample".getBytes()), 1, 1);
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 assertEquals('\t', c);
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('c');

 assertEquals(2, charStream.getLine());
 assertEquals(6, charStream.getColumn());
 }

 @Test
 public void testReadChar() throws IOException {
 char c;
 for (int i = 0; i < 10; i++) {
 c = charStream.readChar();
 assertEquals((char) (i + 97), c);
 }
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 charStream.readChar();
 charStream.inputStream.close();
 charStream.readChar();
 }

 @Test
 public void testGetLineColumn() {
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 assertEquals(2, charStream.getLine());
 assertEquals(6, charStream.getColumn());
 }

 @Test
 public void testGetBeginLineColumn() {
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('c');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('d');
 charStream.UpdateLineColumn('e');

 assertEquals(2, charStream.getBeginLine());
 assertEquals(7, charStream.getBeginColumn());
 }

 @Test
 public void testBackup() throws IOException {
 charStream.readChar();
 charStream.readChar();
 charStream.backup(2);
 char c2 = charStream.readChar();
 assertEquals('a', c2);
 }

 @Test
 public void testExpandBuff() throws IOException {
 SimpleCharStream charStream2 = new SimpleCharStream(new ByteArrayInputStream("test\nsample\nsimple\nchar\nstream".getBytes()), 1, 1);
 charStream2.ExpandBuff(false);
 charStream2.ExpandBuff(true);
 charStream2.ExpandBuff(false);
 charStream2.ExpandBuff(true);
 charStream2.ExpandBuff(false);

 assertEquals(8192, charStream2.bufsize);
 assertEquals(1, charStream2.getLine());
 assertEquals(1, charStream2.getColumn());
 }

 @Test
 public void testFillBuff() throws IOException {
 SimpleCharStream charStream3 = new SimpleCharStream(new ByteArrayInputStream("test\nsample\nsimple\nchar\nstream".getBytes()), 1, 1);
 char c3;
 for (int i = 0; i < 10; i++) {
 c3 = charStream3.readChar();
 assertEquals((char) (i + 97), c3);
 }
 charStream3.FillBuff();
 for (int i = 0; i < 5; i++) {
 c3 = charStream3.readChar();
 assertEquals((char) (i + 97 + 10), c3);
 }
 }

 @Test(expected = IOException.class)
 public void testFillBuffException() throws IOException {
 SimpleCharStream charStream4 = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
 charStream4.FillBuff();
 charStream4.inputStream.close();
 charStream4.readChar();
 }
}