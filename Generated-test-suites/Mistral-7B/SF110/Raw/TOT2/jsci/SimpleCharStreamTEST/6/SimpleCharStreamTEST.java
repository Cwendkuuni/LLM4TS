import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private static SimpleCharStream charStream;
 private static ByteArrayInputStream inputStream;

 @BeforeClass
 public static void setup() {
 charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest123\r\n123test".getBytes()), 1, 1);
 inputStream = new ByteArrayInputStream("test\r\ntest123\r\n123test".getBytes());
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 assertEquals('\r', c);
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('c');

 assertEquals(1, charStream.getLine());
 assertEquals(5, charStream.getColumn());
 }

 @Test
 public void testReadChar() throws IOException {
 char c;
 for (int i = 0; i < 7; ++i) {
 c = charStream.readChar();
 assertEquals(Character.codePointAt((CharSequence) "test\r\ntest123\r\n123test".subSequence(i, i + 1)), (int) c);
 }
 }

 @Test
 public void testGetLineColumn() throws IOException {
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 assertEquals(1, charStream.getBeginLine());
 assertEquals(0, charStream.getBeginColumn());
 }

 @Test
 public void testBackup() throws IOException {
 charStream.backup(3);
 assertEquals(4, charStream.getColumn());
 }

 @Test(expected = IOException.class)
 public void testFillBuffException() throws IOException, IOException {
 charStream.FillBuff();
 inputStream.read(); // throw exception
 charStream.readChar();
 fail("Expected IOException");
 }

 @Test
 public void testReInit() throws IOException {
 charStream.ReInit(inputStream, 2, 2);
 assertEquals(2, charStream.getLine());
 assertEquals(2, charStream.getColumn());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReInitInvalidStartColumn() throws IOException {
 charStream.ReInit(inputStream, 0, 2);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testExpandBuff() {
 char[] newbuffer = new char[SimpleCharStream.bufsize + 2048];
 int[] newbufline = new int[SimpleCharStream.bufsize + 2048];
 int[] newbufcolumn = new int[SimpleCharStream.bufsize + 2048];
 SimpleCharStream.ExpandBuff(false);

 System.arraycopy(SimpleCharStream.buffer, 0, newbuffer, 0, SimpleCharStream.bufsize);
 System.arraycopy(SimpleCharStream.bufline, 0, newbufline, 0, SimpleCharStream.bufsize);
 System.arraycopy(SimpleCharStream.bufcolumn, 0, newbufcolumn, 0, SimpleCharStream.bufsize);

 assertArrayEquals(SimpleCharStream.buffer, newbuffer);
 assertArrayEquals(SimpleCharStream.bufline, newbufline);
 assertArrayEquals(SimpleCharStream.bufcolumn, newbufcolumn);
 }

 @Test
 public void testGetImage() {
 String image = SimpleCharStream.GetImage();
 assertEquals("test\r\ntest123\r\n123test", image);
 }

 @Test
 public void testGetSuffix() {
 char[] suffix = SimpleCharStream.GetSuffix(6);
 String suffixString = new String(suffix);
 assertEquals("test123\r\n123test", suffixString);
 }
}