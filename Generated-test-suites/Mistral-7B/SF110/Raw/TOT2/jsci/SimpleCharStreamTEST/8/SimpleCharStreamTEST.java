import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;

 @Before
 public void setup() {
 charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest123".getBytes()), 1, 1, 4096);
 }

 @Test
 public void testBeginToken() throws IOException {
 assertEquals('t', charStream.BeginToken());
 }

 @Test(expected = IOException.class)
 public void testBeginTokenFail() throws IOException {
 charStream.bufpos = 2;
 charStream.BeginToken();
 }

 @Test
 public void testUpdateLineColumn() {
 charStream.readChar();
 charStream.UpdateLineColumn('a');
 assertEquals(1, charStream.getLine());
 assertEquals(2, charStream.getColumn());
 }

 @Test
 public void testUpdateLineColumnLF() {
 charStream.UpdateLineColumn('\n');
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 }

 @Test
 public void testUpdateLineColumnCR() {
 charStream.UpdateLineColumn('\r');
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 }

 @Test
 public void testUpdateLineColumnTab() {
 charStream.UpdateLineColumn('\t');
 assertEquals(1, charStream.getLine());
 assertEquals(9, charStream.getColumn());
 }

 @Test
 public void testReadChar() throws IOException {
 assertEquals('t', charStream.readChar());
 }

 @Test(expected = IOException.class)
 public void testReadCharFail() throws IOException {
 charStream.bufpos = 1;
 charStream.readChar();
 }

 @Test
 public void testGetColumn() {
 assertEquals(2, charStream.getColumn());
 }

 @Test
 public void testGetLine() {
 assertEquals(1, charStream.getLine());
 }

 @Test
 public void testGetEndColumn() {
 assertEquals(2, charStream.getEndColumn());
 }

 @Test
 public void testGetEndLine() {
 assertEquals(1, charStream.getEndLine());
 }

 @Test
 public void testGetBeginColumn() {
 assertEquals(1, charStream.getBeginColumn());
 }

 @Test
 public void testGetBeginLine() {
 assertEquals(1, charStream.getBeginLine());
 }

 @Test
 public void testBackup() throws IOException {
 charStream.readChar();
 charStream.readChar();
 charStream.backup(1);
 assertEquals('t', charStream.readChar());
 }

 @Test(expected = IOException.class)
 public void testBackupFail() throws IOException {
 charStream.backup(5);
 }

 @Test
 public void testExpandBuff() {
 charStream.bufpos = 1;
 charStream.ExpandBuff(false);
 assertEquals(4096, charStream.bufsize);
 }

 @Test
 public void testFillBuff() throws IOException {
 charStream.FillBuff();
 assertEquals(4096, charStream.bufsize);
 assertEquals(4095, charStream.available);
 }

 @Test
 public void testFillBuffIOException() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
 SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1, 4096);
 charStream.FillBuff();
 inputStream.close();
 assertThrows(IOException.class, () -> charStream.FillBuff());
 }

 @Test
 public void testGetImage() {
 assertEquals("test\r\ntest123", SimpleCharStream.GetImage());
 }

 @Test
 public void testGetSuffix() {
 assertEquals("3", SimpleCharStream.GetSuffix(3).toString());
 }

 @Test
 public void testDone() {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest123".getBytes()), 1, 1, 4096);
 SimpleCharStream.Done();
 assertNull(charStream.buffer);
 assertNull(charStream.bufline);
 assertNull(charStream.bufcolumn);
 }

 @Test
 public void testAdjustBeginLineColumn() {
 SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest123".getBytes()), 1, 1, 4096);
 charStream.adjustBeginLineColumn(2, 2);
 assertEquals(2, charStream.getBeginLine());
 assertEquals(2, charStream.getBeginColumn());
 }
}