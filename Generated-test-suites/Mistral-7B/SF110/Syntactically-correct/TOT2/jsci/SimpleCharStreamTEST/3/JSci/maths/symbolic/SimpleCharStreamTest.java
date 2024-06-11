import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;

 @Before
 public void setup() {
 charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 assertEquals('\r', c);
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 charStream.UpdateLineColumn('a');
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());

 charStream.UpdateLineColumn('\n');
 assertEquals(2, charStream.getLine());
 assertEquals(1, charStream.getColumn());

 charStream.UpdateLineColumn('b');
 assertEquals(2, charStream.getLine());
 assertEquals(2, charStream.getColumn());
 }

 @Test
 public void testReadChar() throws IOException {
 char c;
 c = charStream.readChar();
 assertEquals('\r', c);

 c = charStream.readChar();
 assertEquals('\n', c);

 c = charStream.readChar();
 assertEquals('t', c);
 }

 @Test
 public void testGetColumn() throws IOException {
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('c');
 assertEquals(3, charStream.getColumn());
 }

 @Test
 public void testGetLine() throws IOException {
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('c');
 assertEquals(3, charStream.getLine());
 }

 @Test
 public void testGetEndLine() throws IOException {
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('c');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('d');
 assertEquals(4, charStream.getEndLine());
 }

 @Test
 public void testGetBeginLine() throws IOException {
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('c');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('d');
 assertEquals(3, charStream.getBeginLine());
 }

 @Test
 public void testGetBeginColumn() throws IOException {
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('c');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('d');
 assertEquals(1, charStream.getBeginColumn());
 }

 @Test
 public void testGetEndColumn() throws IOException {
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('c');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('d');
 assertEquals(5, charStream.getEndColumn());
 }

 @Test(expected = IOException.class)
 public void testFillBuffWithEmptyBuffer() throws IOException {
 charStream.FillBuff();
 }

 @Test
 public void testFillBuffWithFilledBuffer() throws IOException {
 charStream.FillBuff();
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('c');
 assertEquals(3, charStream.available);
 }

 @Test
 public void testFillBuffWithWrappingBuffer() throws IOException {
 charStream.ExpandBuff(true);
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('c');
 assertEquals(3, charStream.available);
 }

 @Test
 public void testFillBuffWithTokenBeginGreaterThanMaxNextCharInd() throws IOException {
 charStream.tokenBegin = 100;
 charStream.maxNextCharInd = 10;
 charStream.FillBuff();
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('c');
 assertEquals(3, charStream.available);
 }

 @Test
 public void testFillBuffWithTokenBeginLessThanAvailable() throws IOException {
 charStream.tokenBegin = 10;
 charStream.available = 20;
 charStream.FillBuff();
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('c');
 assertEquals(10, charStream.available);
 }

 @Test
 public void testFillBuffWithTokenBeginAndAvailableEqual() throws IOException {
 charStream.tokenBegin = 10;
 charStream.available = 10;
 charStream.FillBuff();
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('c');
 assertEquals(0, charStream.available);
 }

 @Test
 public void testFillBuffWithTokenBeginAndAvailableLessThan2048() throws IOException {
 charStream.tokenBegin = 10;
 charStream.available = 5;
 charStream.FillBuff();
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('c');
 assertEquals(5, charStream.available);
 }

 @Test(expected = IOException.class)
 public void testFillBuffWithIOException() throws IOException {
 charStream.inputStream = new ByteArrayInputStream(new byte[0]);
 charStream.FillBuff();
 }

 @Test
 public void testBackup() throws IOException {
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('c');
 charStream.backup(2);
 assertEquals(3, charStream.getColumn());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBackupWithNegativeAmount() throws IOException {
 charStream.backup(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testBackupWithEndOfBuffer() throws IOException {
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('c');
 charStream.backup(SimpleCharStream.bufsize);
 }

 @Test(expected = IOException.class)
 public void testClose() throws IOException {
 charStream.FillBuff();
 charStream.Close();
 assertThrows(IOException.class, () -> charStream.BeginToken());
 }
}