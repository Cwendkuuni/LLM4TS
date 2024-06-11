package JSci.maths.symbolic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private int expectedLine;
 private int expectedColumn;
 private int expectedEndLine;
 private int expectedEndColumn;
 private int expectedBeginLine;
 private int expectedBeginColumn;

 @Before
 public void setUp() {
 charStream = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 4096);
 expectedLine = 1;
 expectedColumn = 1;
 expectedEndLine = 1;
 expectedEndColumn = 5;
 expectedBeginLine = 1;
 expectedBeginColumn = 1;
 }

 @After
 public void tearDown() throws IOException {
 charStream.Close();
 }

 @Test
 public void testBeginToken() throws IOException {
 char expectedChar = 't';
 char actualChar = charStream.BeginToken();
 assertEquals("Unexpected character returned", expectedChar, actualChar);
 assertEquals("Unexpected line number", expectedLine, charStream.getLine());
 assertEquals("Unexpected column number", expectedColumn, charStream.getColumn());
 assertEquals("Unexpected end line number", expectedEndLine, charStream.getEndLine());
 assertEquals("Unexpected end column number", expectedEndColumn, charStream.getEndColumn());
 assertEquals("Unexpected begin line number", expectedBeginLine, charStream.getBeginLine());
 assertEquals("Unexpected begin column number", expectedBeginColumn, charStream.getBeginColumn());
 }

 @Test(expected = IOException.class)
 public void testBeginTokenWithNoCharAvailable() throws IOException {
 charStream.FillBuff(); // Fill the buffer with a single character
 charStream.readChar(); // Read the character
 charStream.BeginToken(); // This should throw an IOException since there's no character available for tokenization
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 char expectedChar = 'e';
 char actualChar = charStream.BeginToken(); // Advance the stream to the expected character
 charStream.UpdateLineColumn(expectedChar); // Update the line and column information
 assertEquals("Unexpected character", expectedChar, charStream.readChar());
 assertEquals("Unexpected line number", expectedLine, charStream.getLine());
 assertEquals("Unexpected column number", expectedColumn + 1, charStream.getColumn());
 assertEquals("Unexpected end line number", expectedEndLine, charStream.getEndLine());
 assertEquals("Unexpected end column number", expectedEndColumn, charStream.getEndColumn());
 assertEquals("Unexpected begin line number", expectedBeginLine, charStream.getBeginLine());
 assertEquals("Unexpected begin column number", expectedBeginColumn, charStream.getBeginColumn());
 }

 @Test
 public void testGetChar() throws IOException {
 char expectedChar = 's';
 char actualChar = charStream.readChar();
 assertEquals("Unexpected character", expectedChar, actualChar);
 }

 @Test(expected = IOException.class)
 public void testGetCharWithNoCharAvailable() throws IOException {
 charStream.FillBuff(); // Fill the buffer with a single character
 charStream.readChar(); // Read the character
 }

 @Test
 public void testGetLineColumn() throws IOException {
 int expectedLineNumber = 1;
 int expectedColumnNumber = 4;
 int actualLineNumber = charStream.getLine();
 int actualColumnNumber = charStream.getColumn();
 assertEquals("Unexpected line number", expectedLineNumber, actualLineNumber);
 assertEquals("Unexpected column number", expectedColumnNumber, actualColumnNumber);
 }

 @Test
 public void testGetEndLineColumn() throws IOException {
 int expectedEndLineNumber = 1;
 int expectedEndColumnNumber = 5;
 int actualEndLineNumber = charStream.getEndLine();
 int actualEndColumnNumber = charStream.getEndColumn();
 assertEquals("Unexpected end line number", expectedEndLineNumber, actualEndLineNumber);
 assertEquals("Unexpected end column number", expectedEndColumnNumber, actualEndColumnNumber);
 }

 @Test
 public void testGetBeginLineColumn() throws IOException {
 int expectedBeginLineNumber = 1;
 int expectedBeginColumnNumber = 1;
 int actualBeginLineNumber = charStream.getBeginLine();
 int actualBeginColumnNumber = charStream.getBeginColumn();
 assertEquals("Unexpected begin line number", expectedBeginLineNumber, actualBeginLineNumber);
 assertEquals("Unexpected begin column number", expectedBeginColumnNumber, actualBeginColumnNumber);
 }

 @Test
 public void testBackup() throws IOException {
 charStream.backup(1); // Backup by one character
 char expectedChar = 't';
 char actualChar = charStream.readChar();
 assertEquals("Unexpected character", expectedChar, actualChar);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testBackupWithNegativeAmount() throws IOException {
 charStream.backup(-1); // Backup by one negative character
 }

 @Test(expected = IOException.class)
 public void testBackupWithEndOfStream() throws IOException {
 charStream.FillBuff(); // Fill the buffer with a single character
 charStream.backup(1); // Backup by one character
 charStream.backup(1); // Backup by another character
 charStream.backup(1); // Backup by another character
 charStream.Backup(1); // Call the method with a capital B (should throw an IOException)
 }
}