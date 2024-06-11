import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private static final String TEST_CHARS = "abc\r\ndef\r\n\tghij\nklm\r\nopqr\n";
 private SimpleCharStream charStream;
 private int[] expectedBufline;
 private int[] expectedBufcolumn;
 private int expectedLine;
 private int expectedColumn;

 @Before
 public void setUp() {
 charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_CHARS.getBytes()), 1, 1);
 expectedBufline = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
 expectedBufcolumn = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49};
 expectedLine = 1;
 expectedColumn = 1;
 }

 @After
 public void tearDown() {
 try {
 charStream.Done();
 } catch (IOException e) {
 //ignore
 }
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 assertEquals(c, TEST_CHARS.charAt(0));
 assertEquals(charStream.getLine(), expectedLine);
 assertEquals(charStream.getColumn(), expectedColumn + 1);
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 charStream.UpdateLineColumn('a');
 assertEquals(charStream.getLine(), expectedLine);
 assertEquals(charStream.getColumn(), expectedColumn + 1);
 }

 @Test
 public void testReadChar() throws IOException {
 char c = charStream.readChar();
 assertEquals(c, TEST_CHARS.charAt(charStream.getColumn() - charStream.getBeginColumn() + charStream.getBeginLine() * 1000));
 assertEquals(charStream.getLine(), expectedLine + (charStream.getColumn() - charStream.getBeginColumn()) / 1000);
 assertEquals(charStream.getColumn(), expectedColumn + 1);
 }

 @Test
 public void testGetLineColumn() throws IOException {
 assertEquals(charStream.getLine(), expectedLine);
 assertEquals(charStream.getColumn(), expectedColumn);
 }

 @Test
 public void testGetEndLineColumn() throws IOException {
 assertEquals(charStream.getEndLine(), expectedLine + TEST_CHARS.length() / 1000);
 assertEquals(charStream.getEndColumn(), expectedColumn + TEST_CHARS.length());
 }

 @Test
 public void testGetBeginLineColumn() throws IOException {
 assertEquals(charStream.getBeginLine(), expectedLine);
 assertEquals(charStream.getBeginColumn(), expectedColumn);
 }

 @Test
 public void testBackup() throws IOException {
 charStream.backup(2);
 assertEquals(charStream.getLine(), expectedLine);
 assertEquals(charStream.getColumn(), expectedColumn - 2);
 }

 @Test
 public void testFillBuff() throws IOException {
 char c = TEST_CHARS.charAt(TEST_CHARS.length() - 1);
 charStream.backup(TEST_CHARS.length());
 charStream.FillBuff();
 assertEquals(charStream.readChar(), c);
 }

 @Test(expected = IOException.class)
 public void testFillBuffFailed() throws IOException {
 charStream.backup(TEST_CHARS.length());
 charStream.FillBuff();
 charStream.readChar();
 }

 @Test
 public void testExpandBuff() {
 charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_CHARS.getBytes()), 1, 1, 10);
 charStream.ExpandBuff(false);
 assertEquals(charStream.bufsize, 12);
 }
}