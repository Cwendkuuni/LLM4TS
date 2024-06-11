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
 private char expectedChar;

 @Before
 public void setUp() throws IOException {
 charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
 expectedLine = 1;
 expectedColumn = 1;
 expectedChar = 't';
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 assertEquals(expectedChar, c);
 assertEquals(expectedLine, charStream.getLine());
 assertEquals(expectedColumn, charStream.getColumn());
 }

 @Test
 public void testFillBuff() throws IOException {
 charStream.FillBuff();
 assertEquals((char) 't', charStream.readChar());
 assertEquals((char) 'e', charStream.readChar());
 assertEquals((char) 'st', charStream.readChar());
 assertEquals((char) '\r', charStream.readChar());
 assertEquals((char) 'n', charStream.readChar());
 assertEquals((char) 't', charStream.readChar());
 assertEquals((char) '\r', charStream.readChar());
 assertEquals((char) ' ', charStream.readChar());
 assertEquals((char) '\n', charStream.readChar());
 assertEquals((char) '\0', charStream.readChar());
 }

 @Test(expected = IOException.class)
 public void testFillBuffWithEOF() throws IOException {
 charStream.FillBuff();
 charStream.FillBuff();
 char c = charStream.readChar();
 fail("Expected IOException");
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 charStream.UpdateLineColumn('t');
 assertEquals(expectedLine, charStream.getLine());
 assertEquals(expectedColumn + 1, charStream.getColumn());
 }

 @Test
 public void testGetLineColumn() throws IOException {
 assertEquals(expectedLine, charStream.getLine());
 assertEquals(expectedColumn, charStream.getColumn());
 assertEquals(expectedLine, charStream.getBeginLine());
 assertEquals(expectedColumn, charStream.getBeginColumn());
 }

 @Test
 public void testBackup() throws IOException {
 charStream.backup(2);
 assertEquals(expectedChar, charStream.readChar());
 assertEquals(expectedChar, charStream.readChar());
 }

 @After
 public void tearDown() throws IOException {
 charStream.Done();
 }
}