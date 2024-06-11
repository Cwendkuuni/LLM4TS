package jsit. SimpleCharStreamTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SimpleCharStreamTest {

 private SimpleCharStream simpleCharStream;
 private InputStream inputStream;

 @Before
 public void setUp() {
 inputStream = new ByteArrayInputStream("Test\r\nLine 1\tColumn 1: Test\r\nLine 2\tColumn 2: Test".getBytes());
 simpleCharStream = new SimpleCharStream(inputStream, 1, 1);
 }

 @After
 public void tearDown() {
 try {
 simpleCharStream.ReInit(null);
 } catch (IOException e) {
 // Ignore
 }
 }

 @Test
 public void testStaticFlag() {
 assertTrue(SimpleCharStream.staticFlag);
 }

 @Test
 public void testExpandBuff() throws IOException {
 simpleCharStream.ExpandBuff(false);
 assertEquals(4096 + 2048, SimpleCharStream.bufsize);

 simpleCharStream.ExpandBuff(true);
 assertEquals(4096 + 4096 + 2048, SimpleCharStream.bufsize);
 }

 @Test
 public void testBeginToken() throws IOException {
 assertEquals('\r', simpleCharStream.BeginToken());
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 simpleCharStream.readChar();
 simpleCharStream.UpdateLineColumn('\t');
 assertEquals(1, simpleCharStream.getLine());
 assertEquals(9, simpleCharStream.getColumn());
 }

 @Test
 public void testReadChar() throws IOException {
 char expectedChar;
 int expectedLine;
 int expectedColumn;
 int line;
 int column;

 expectedChar = '\r';
 line = 1;
 column = 1;
 assertEquals(expectedChar, simpleCharStream.readChar());
 assertEquals(expectedLine, simpleCharStream.getLine());
 assertEquals(expectedColumn, simpleCharStream.getColumn());

 expectedChar = '\n';
 expectedLine = 2;
 expectedColumn = 1;
 assertEquals(expectedChar, simpleCharStream.readChar());
 assertEquals(expectedLine, simpleCharStream.getLine());
 assertEquals(expectedColumn, simpleCharStream.getColumn());

 expectedChar = 'T';
 line = 2;
 column = 2;
 assertEquals(expectedChar, simpleCharStream.readChar());
 assertEquals(line, simpleCharStream.getLine());
 assertEquals(column, simpleCharStream.getColumn());

 expectedChar = 'e';
 line = 2;
 column = 3;
 assertEquals(expectedChar, simpleCharStream.readChar());
 assertEquals(line, simpleCharStream.getLine());
 assertEquals(column, simpleCharStream.getColumn());

 expectedChar = 's';
 line = 2;
 column = 4;
 assertEquals(expectedChar, simpleCharStream.readChar());
 assertEquals(line, simpleCharStream.getLine());
 assertEquals(column, simpleCharStream.getColumn());

 expectedChar = 't';
 line = 2;
 column = 5;
 assertEquals(expectedChar, simpleCharStream.readChar());
 assertEquals(line, simpleCharStream.getLine());
 assertEquals(column, simpleCharStream.getColumn());

 expectedChar = '\r';
 line = 3;
 column = 1;
 assertEquals(expectedChar, simpleCharStream.readChar());
 assertEquals(line, simpleCharStream.getLine());
 assertEquals(column, simpleCharStream.getColumn());

 expectedChar = '\n';
 line = 3;
 column = 1;
 assertEquals(expectedChar, simpleCharStream.readChar());
 assertNull(simpleCharStream.getLine());
 assertNull(simpleCharStream.getColumn());
 }

 @Test
 public void testGetLineColumn() throws IOException {
 int line;
 int column;

 line = 1;
 column = 1;
 assertEquals(line, simpleCharStream.getLine());
 assertEquals(column, simpleCharStream.getColumn());

 simpleCharStream.readChar();
 simpleCharStream.UpdateLineColumn('\t');
 line = 2;
 column = 1;
 assertEquals(line, simpleCharStream.getLine());
 assertEquals(column, simpleCharStream.getColumn());

 simpleCharStream.readChar();
 assertEquals(line, simpleCharStream.getLine());
 assertEquals(column + 1, simpleCharStream.getColumn());

 simpleCharStream.readChar();
 simpleCharStream.UpdateLineColumn('\n');
 line = 3;
 column = 1;
 assertEquals(line, simpleCharStream.getLine());
 assertEquals(column, simpleCharStream.getColumn());

 simpleCharStream.readChar();
 assertEquals(line, simpleCharStream.getLine());
 assertEquals(column + 1, simpleCharStream.getColumn());

 simpleCharStream.readChar();
 simpleCharStream.UpdateLineColumn('\r');
 line = 3;
 column = 1;
 assertEquals(line, simpleCharStream.getLine());
 assertEquals(column, simpleCharStream.getColumn());
 }

 @Test
 public void testBackup() throws IOException {
 int expectedLine;
 int expectedColumn;
 int line;
 int column;

 expectedLine = 1;
 expectedColumn = 1;
 simpleCharStream.readChar();
 assertEquals(expectedLine, simpleCharStream.getLine());
 assertEquals(expectedColumn, simpleCharStream.getColumn());
 simpleCharStream.backup(1);
 assertEquals(expectedLine, simpleCharStream.getLine());
 assertEquals(expectedColumn, simpleCharStream.getColumn());

 simpleCharStream.readChar();
 simpleCharStream.readChar();
 simpleCharStream.backup(2);
 assertEquals(expectedLine, simpleCharStream.getLine());
 assertEquals(expectedColumn, simpleCharStream.getColumn());

 expectedLine = 2;
 expectedColumn = 2;
 simpleCharStream.readChar();
 simpleCharStream.UpdateLineColumn('\n');
 simpleCharStream.readChar();
 simpleCharStream.readChar();
 simpleCharStream.Backup(1);
 assertEquals(expectedLine, simpleCharStream.getLine());
 assertEquals(expectedColumn, simpleCharStream.getColumn());
 }

 @Test
 public void testGetImage() throws IOException {
 assertEquals("Test\r\nLine 1	Column 1: Test\r\nLine 2	Column 2: Test", SimpleCharStream.GetImage());
 }

 @Test
 public void testGetSuffix() throws IOException {
 char[] expectedSuffix;
 int suffixLength = 5;

 expectedSuffix = new char[suffixLength];
 simpleCharStream.readChar();
 simpleCharStream.readChar();
 simpleCharStream.readChar();
 simpleCharStream.readChar();
 simpleCharStream.readChar();
 simpleCharStream.readChar();
 simpleCharStream.readChar();
 simpleCharStream.readChar();
 simpleCharStream.readChar();
 SimpleCharStream.GetSuffix(suffixLength, expectedSuffix);
 assertEquals("Test", new String(expectedSuffix));
 }

 @Test
 public void testDone() {
 SimpleCharStream.Done();
 assertNull(SimpleCharStream.buffer);
 assertNull(SimpleCharStream.bufline);
 assertNull(SimpleCharStream.bufcolumn);
 }

 @Test
 public void testAdjustBeginLineColumn() throws IOException {
 int newLine = 2;
 int newColumn = 5;
 simpleCharStream.readChar();
 simpleCharStream.UpdateLineColumn('\n');
 simpleCharStream.readChar();
 simpleCharStream.readChar();
 simpleCharStream.readChar();
 simpleCharStream.readChar();
 simpleCharStream.readChar();
 SimpleCharStream.adjustBeginLineColumn(newLine, newColumn);
 assertEquals(newLine, simpleCharStream.getLine());
 assertEquals(newColumn, simpleCharStream.getColumn());
 }
}