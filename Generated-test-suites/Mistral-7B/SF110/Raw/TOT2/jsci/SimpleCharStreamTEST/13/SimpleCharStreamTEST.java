import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;
import JSci.maths.symbolic.SimpleCharStream;

public class SimpleCharStreamTest {

 @Test
 public void testBeginToken() throws Exception {
 SimpleCharStream scs = new SimpleCharStream(new StringReader("Test string"), 1, 1);
 char c = scs.BeginToken();
 assertEquals("Expected the first character in the test string to be returned", 'T', c);
 }

 @Test
 public void testUpdateLineColumn() throws Exception {
 SimpleCharStream scs = new SimpleCharStream(new StringReader("Test\nstring"), 1, 1);
 scs.UpdateLineColumn('T');
 scs.UpdateLineColumn('\n');
 scs.UpdateLineColumn('s');
 scs.UpdateLineColumn('t');
 scs.UpdateLineColumn('r');
 scs.UpdateLineColumn('i');
 scs.UpdateLineColumn('n');
 scs.UpdateLineColumn('g');
 scs.UpdateLineColumn('\r');

 assertEquals("Expected line number to be set correctly", 2, scs.getLine());
 assertEquals("Expected column number to be set correctly", 9, scs.getColumn());
 }

 @Test
 public void testFillBuff() throws Exception {
 SimpleCharStream scs = new SimpleCharStream(new StringReader("Test\nstring"), 1, 1);

 // Test filling the buffer with normal characters
 for (int i = 0; i < 100; i++) {
 scs.readChar();
 }

 // Test filling the buffer with an EOF character
 scs.readChar();

 // Check that the buffer has been correctly filled
 for (int i = 0; i < 100; i++) {
 char c = scs.readChar();
 assertNotNull("Expected to be able to read characters from the buffer", c);
 }
 }

 @Test
 public void testExpandBuff() throws Exception {
 SimpleCharStream scs = new SimpleCharStream(new StringReader("Test\nstring"), 1, 1);

 // Test expanding the buffer when it is full
 for (int i = 0; i < 1000; i++) {
 scs.readChar();
 }

 // Check that the buffer has been expanded correctly
 assertEquals("Expected the buffer size to have been increased by 2048", 4096, scs.bufsize);
 }

 @Test(expected = IOException.class)
 public void testIOExceptionWhenFillingBuff() throws Exception {
 SimpleCharStream scs = new SimpleCharStream(new StringReader("Test\nstring"), 1, 1);

 // Test throwing an IOException when filling the buffer fails
 scs.inputStream = new StringReader("Test"); // Intentionally create a malformed input stream
 scs.FillBuff();
 }

 @Test
 public void testBackup() throws Exception {
 SimpleCharStream scs = new SimpleCharStream(new StringReader("Test\nstring"), 1, 1);

 // Test backing up through the buffer
 scs.backup(5);
 char c = scs.readChar();
 assertEquals("Expected to read the fifth character from the beginning of the buffer", 'T', c);
 }

 @Test
 public void testGetLineColumn() throws Exception {
 SimpleCharStream scs = new SimpleCharStream(new StringReader("Test\nstring\nTest2"), 1, 1);

 // Test getting the line and column numbers for different characters
 assertEquals("Expected the line number to be set correctly for the first character", 1, scs.getLine());
 assertEquals("Expected the column number to be set correctly for the first character", 1, scs.getColumn());

 scs.UpdateLineColumn('T');
 scs.UpdateLineColumn('\n');
 scs.UpdateLineColumn('s');
 scs.UpdateLineColumn('t');
 scs.UpdateLineColumn('r');
 scs.UpdateLineColumn('i');
 scs.UpdateLineColumn('n');
 scs.UpdateLineColumn('\r');
 scs.UpdateLineColumn('g');
 scs.UpdateLineColumn('\n');
 scs.UpdateLineColumn('T');
 scs.UpdateLineColumn('e');
 scs.UpdateLineColumn('s');
 scs.UpdateLineColumn('t');
 scs.UpdateLineColumn('2');

 assertEquals("Expected the line number to be set correctly for the second line", 2, scs.getLine());
 assertEquals("Expected the column number to be set correctly for the first character on the second line", 1, scs.getColumn());
 }
}