import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ExtendedBufferedReaderTest {

 private Reader reader;
 private ExtendedBufferedReader extendedBufferedReader;

 @Before
 public void setUp() throws Exception {
 reader = new StringReader("line1\r\nline2\r\nline3\r\nline4");
 extendedBufferedReader = new ExtendedBufferedReader(reader);
 }

 @After
 public void tearDown() throws Exception {
 reader.close();
 extendedBufferedReader.close();
 }

 @Test
 public void testRead() throws Exception {
 assertEquals('l', extendedBufferedReader.read());
 assertEquals('i', extendedBufferedReader.read());
 assertEquals('n', extendedBufferedReader.read());
 assertEquals('e', extendedBufferedReader.read());
 assertEquals('1', extendedBufferedReader.read());
 assertEquals('\r', extendedBufferedReader.read()); // line separator
 assertEquals(' ', extendedBufferedReader.read()); // space
 assertEquals('l', extendedBufferedReader.read());
 // next line
 assertEquals('i', extendedBufferedReader.read());
 // ...

 assertEquals(-1, extendedBufferedReader.read()); // End of stream
 }

 @Test
 public void testReadAgain() throws Exception {
 assertEquals('l', extendedBufferedReader.read());
 assertEquals('l', extendedBufferedReader.readAgain());
 assertEquals('i', extendedBufferedReader.read());
 assertEquals('i', extendedBufferedReader.readAgain());
 }

 @Test
 public void testReadLine() throws Exception {
 assertEquals("line1", extendedBufferedReader.readLine());
 assertEquals("line2", extendedBufferedReader.readLine());
 assertEquals("line3", extendedBufferedReader.readLine());
 assertEquals("line4", extendedBufferedReader.readLine());
 assertNull(extendedBufferedReader.readLine()); // End of stream
 }

 @Test
 public void testLookAhead() throws Exception {
 assertEquals('l', extendedBufferedReader.lookAhead()); // peek at next character
 assertEquals('l', extendedBufferedReader.read());
 }

 @Test
 public void testGetLineNumber() throws Exception {
 assertEquals(0, extendedBufferedReader.getLineNumber());
 assertEquals(1, extendedBufferedReader.readLine() == null ? 0 : 1);
 assertEquals(2, extendedBufferedReader.readLine() == null ? 0 : 1);
 assertEquals(3, extendedBufferedReader.readLine() == null ? 0 : 1);
 assertEquals(4, extendedBufferedReader.getLineNumber());
 }
}