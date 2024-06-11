package JSci.maths.symbolic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private Reader reader;

 @Before
 public void setUp() throws IOException {
 reader = new FileReader("input.txt");
 charStream = new SimpleCharStream(reader, 1, 1, 4096);
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 assertEquals(c, charStream.readChar());
 }

 @Test
 public void testFillBuff() throws IOException {
 char c;
 for (int i = 0; i < 1000; i++) {
 c = charStream.readChar();
 }
 charStream.FillBuff();
 c = charStream.readChar();
 assertEquals(c, '\u0000'); // Check if the buffer is refilled with '\u0000'
 }

 @Test(expected = IOException.class)
 public void testFillBuffWithIOException() throws IOException {
 charStream.FillBuff();
 charStream.inputStream.close(); // Simulate an IOException
 char c = charStream.readChar();
 fail("An IOException should have been thrown.");
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 char c;
 for (int i = 0; i < 5; i++) {
 c = charStream.readChar();
 charStream.UpdateLineColumn(c);
 }
 int line = charStream.getLine();
 int column = charStream.getColumn();
 assertEquals(line, 1);
 assertEquals(column, 6);
 }

 @Test
 public void testGetLineColumn() throws IOException {
 int line, column;
 for (int i = 0; i < 10; i++) {
 char c = charStream.readChar();
 charStream.UpdateLineColumn(c);
 if (c == '\n') {
 line = charStream.getLine();
 column = charStream.getColumn();
 assertEquals(line, charStream.getLine() + 1);
 column = 1;
 }
 }
 assertEquals(charStream.getBeginLine(), 1);
 assertEquals(charStream.getBeginColumn(), 1);
 }

 @Test
 public void testBackup() throws IOException {
 char c;
 for (int i = 0; i < 10; i++) {
 c = charStream.readChar();
 }
 charStream.backup(5);
 for (int i = 0; i < 5; i++) {
 c = charStream.readChar();
 assertEquals(c, charStream.readChar());
 }
 }

 @Test
 public void testReInit() throws IOException {
 char c;
 reader.close();
 charStream.ReInit(reader, 1, 1, 4096);
 c = charStream.BeginToken();
 assertEquals(c, charStream.readChar());
 }

 @Test
 public void testGetImage() throws IOException {
 String image = charStream.GetImage();
 assertNotNull(image);
 }

 @Test
 public void testGetSuffix() throws IOException {
 char[] suffix = charStream.GetSuffix(10);
 assertArrayEquals(new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, suffix);
 }

 @Test
 public void testDone() throws IOException {
 char c;
 for (int i = 0; i < 1000; i++) {
 c = charStream.readChar();
 }
 charStream.Done();
 }

 @Test
 public void testAdjustBeginLineColumn() throws IOException {
 charStream.adjustBeginLineColumn(2, 5);
 assertEquals(charStream.getBeginLine(), 2);
 assertEquals(charStream.getBeginColumn(), 5);
 }
}