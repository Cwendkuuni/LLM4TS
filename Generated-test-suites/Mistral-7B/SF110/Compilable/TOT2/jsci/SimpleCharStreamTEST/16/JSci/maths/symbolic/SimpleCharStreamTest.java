package JSci.maths.symbolic;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream sut;

 @Before
 public void setUp() {
 sut = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest123\r\n".getBytes()), 1, 1, 4096);
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = sut.BeginToken();
 assertEquals(c, '\r');
 }

 @Test
 public void testReadChar() throws IOException {
 char c = sut.readChar();
 assertEquals(c, 't');
 }

 @Test
 public void testGetColumn() {
 int column = sut.getColumn();
 assertEquals(column, 1);
 }

 @Test
 public void testGetLine() {
 int line = sut.getLine();
 assertEquals(line, 1);
 }

 @Test
 public void testGetEndColumn() {
 int column = sut.getEndColumn();
 assertEquals(column, 6);
 }

 @Test
 public void testGetEndLine() {
 int line = sut.getEndLine();
 assertEquals(line, 1);
 }

 @Test
 public void testGetBeginColumn() {
 int column = sut.getBeginColumn();
 assertEquals(column, 1);
 }

 @Test
 public void testGetBeginLine() {
 int line = sut.getBeginLine();
 assertEquals(line, 1);
 }

 @Test
 public void testBackup() throws IOException {
 sut.backup(3);
 char c = sut.readChar();
 assertEquals(c, 'r');
 }

 @Test(expected = IOException.class)
 public void testExpandBuffException() throws IOException {
 SimpleCharStream sut2 = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 1);
 sut2.ExpandBuff(false);
 sut2.readChar();
 }

 @Test
 public void testFillBuff() throws IOException {
 int availableBefore = sut.available;
 sut.FillBuff();
 int availableAfter = sut.available;
 assertTrue(availableAfter > availableBefore);
 }

 @Test
 public void testFillBuffWithEmptyBuffer() throws IOException {
 SimpleCharStream sut2 = new SimpleCharStream(new ByteArrayInputStream("".getBytes()), 1, 1, 4096);
 sut2.FillBuff();
 assertEquals(sut2.bufpos, 0);
 }

 @Test
 public void testFillBuffWithEof() throws IOException {
 SimpleCharStream sut2 = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
 sut2.FillBuff();
 sut2.inputStream.close();
 sut2.FillBuff();
 assertThrows(IOException.class, () -> sut2.readChar());
 }
}