import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;

 @Before
 public void setup() {
 charStream = new SimpleCharStream(new ByteArrayInputStream("Test String".getBytes()), 1, 1);
 }

 @Test
 public void testBeginToken() throws IOException {
 char expectedChar = 'T';
 char actualChar = charStream.BeginToken();
 assertEquals("Unexpected character returned by BeginToken()", expectedChar, actualChar);
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 char testChar = 'a';
 charStream.readChar();
 charStream.UpdateLineColumn(testChar);
 int expectedLine = 1;
 int expectedColumn = 1;
 int actualLine = charStream.getLine();
 int actualColumn = charStream.getColumn();
 assertEquals("Unexpected line number after UpdateLineColumn() call", expectedLine, actualLine);
 assertEquals("Unexpected column number after UpdateLineColumn() call", expectedColumn, actualColumn);
 }

 @Test(expected = IOException.class)
 public void testBeginTokenWithEOF() throws IOException {
 charStream.BeginToken();
 charStream.readChar();
 charStream.Read(); // consume next character
 charStream.BeginToken(); // should throw an IOException
 }

 @Test
 public void testGetLineColumn() {
 int expectedLine = 1;
 int expectedColumn = 1;
 int actualLine = charStream.getLine();
 int actualColumn = charStream.getColumn();
 assertEquals("Unexpected line number", expectedLine, actualLine);
 assertEquals("Unexpected column number", expectedColumn, actualColumn);
 }

 @Test
 public void testGetEndLineColumn() {
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('\n');
 int expectedLine = 1;
 int expectedColumn = 3;
 int actualLine = charStream.getLine();
 int actualColumn = charStream.getEndColumn();
 assertEquals("Unexpected line number", expectedLine, actualLine);
 assertEquals("Unexpected end column number", expectedColumn, actualColumn);
 }

 @Test
 public void testBackup() throws IOException {
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('c');
 charStream.Backup(1);
 int expectedLine = 1;
 int expectedColumn = 2;
 int actualLine = charStream.getLine();
 int actualColumn = charStream.getColumn();
 assertEquals("Unexpected line number after Backup() call", expectedLine, actualLine);
 assertEquals("Unexpected column number after Backup() call", expectedColumn, actualColumn);
 }

 @Test
 public void testReInit() throws IOException {
 charStream.UpdateLineColumn('a');
 charStream.UpdateLineColumn('b');
 charStream.UpdateLineColumn('c');
 charStream.ReInit(new ByteArrayInputStream("Test String Two".getBytes()), 1, 1);
 int expectedLine = 1;
 int expectedColumn = 1;
 int actualLine = charStream.getLine();
 int actualColumn = charStream.getColumn();
 assertEquals("Unexpected line number after ReInit() call", expectedLine, actualLine);
 assertEquals("Unexpected column number after ReInit() call", expectedColumn, actualColumn);
 }

}