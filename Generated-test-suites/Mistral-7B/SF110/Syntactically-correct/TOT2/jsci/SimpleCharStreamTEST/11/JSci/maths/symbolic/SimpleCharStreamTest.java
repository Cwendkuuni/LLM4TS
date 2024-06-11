import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private Reader inputStream;

 @Before
 public void setup() throws Exception {
 inputStream = new FileReader("test.txt");
 charStream = new SimpleCharStream(inputStream, 1, 1);
 }

 @Test
 public void testBeginToken() throws Exception {
 char c = charStream.BeginToken();
 assertEquals(c, charStream.readChar());
 }

 @Test
 public void testUpdateLineColumn() throws Exception {
 charStream.UpdateLineColumn('a');
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 charStream.UpdateLineColumn('\n');
 assertEquals(2, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 charStream.UpdateLineColumn('\r');
 assertEquals(2, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 charStream.UpdateLineColumn('\t');
 assertEquals(2, charStream.getLine());
 assertEquals(9, charStream.getColumn());
 }

 @Test
 public void testReadChar() throws Exception {
 char[] expected = {'a', 'b', 'c'};
 for (char c : expected) {
 charStream.readChar();
 assertEquals(c, charStream.readChar());
 }
 }

 @Test(expected = IOException.class)
 public void testIOException() throws Exception {
 charStream.inputStream.close();
 charStream.readChar();
 }

 @Test
 public void testGetLineColumn() throws Exception {
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('c');
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn('d');
 charStream.UpdateLineColumn('\r');
 charStream.UpdateLineColumn('e');

 assertEquals("1,1\n2,1\n2,9\n3,1\n3,1\n3,5", SimpleCharStream.GetImage());
 assertEquals(1, charStream.getBeginLine());
 assertEquals(1, charStream.getBeginColumn());
 assertEquals(3, charStream.getLine());
 assertEquals(5, charStream.getColumn());
 }

 @Test
 public void testExpandBuff() throws Exception {
 int initialBufsize = charStream.bufsize;
 charStream.ExpandBuff(true);
 assertEquals(initialBufsize + 2048, charStream.bufsize);
 }

 @Test
 public void testFillBuff() throws Exception {
 int availableBefore = charStream.available;
 charStream.FillBuff();
 int availableAfter = charStream.available;
 assertEquals(availableBefore + 2048, availableAfter);
 }

 @Test(expected = IOException.class)
 public void testIOExceptionOnFillBuff() throws Exception {
 charStream.inputStream.close();
 charStream.FillBuff();
 }

 @Test
 public void testBackup() throws Exception {
 charStream.backup(2);
 assertEquals(charStream.getLine(), 1);
 assertEquals(charStream.getColumn(), 2);
 }
}