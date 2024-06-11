import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private int initialLine = 1;
 private int initialColumn = 1;
 private int initialBufferSize = 4096;
 private Reader mockInput;

 @Before
 public void setUp() {
 mockInput = new MockReader();
 charStream = new SimpleCharStream(mockInput, initialLine, initialColumn, initialBufferSize);
 }

 @After
 public void tearDown() {
 charStream.Done();
 mockInput.close();
 }

 @Test
 public void testBeginToken() throws IOException {
 char expectedChar = (char) 0x01;
 char actualChar = charStream.BeginToken();
 assertEquals(expectedChar, actualChar);
 assertEquals(-1, charStream.tokenBegin);
 }

 @Test
 public void testExpandBuff() {
 charStream.ExpandBuff(true);
 assertEquals(SimpleCharStream.bufsize + 2048, charStream.bufsize);
 }

 @Test(expected = Error.class)
 public void testExpandBuffWithError() {
 charStream.ExpandBuff(true);
 charStream.ExpandBuff(true); // Should throw an error
 }

 @Test
 public void testFillBuff() throws IOException {
 char[] buffer = new char[10];
 int available = 10;
 charStream.inputStream = new ByteArrayInputStream("TestString".getBytes());
 charStream.FillBuff();
 charStream.read(buffer, 0, available);
 String expectedString = "TestString";
 String actualString = new String(buffer);
 assertEquals(expectedString, actualString);
 }

 @Test(expected = IOException.class)
 public void testFillBuffWithIOException() throws IOException {
 charStream.inputStream = new ByteArrayInputStream("TestString".getBytes());
 charStream.FillBuff();
 charStream.inputStream.close();
 }

 @Test
 public void testGetColumn() {
 int expectedColumn = 5;
 charStream.UpdateLineColumn((char) 0x05);
 int actualColumn = charStream.getColumn();
 assertEquals(expectedColumn, actualColumn);
 }

 @Test
 public void testGetEndColumn() {
 int expectedEndColumn = 10;
 charStream.UpdateLineColumn((char) 0x05);
 charStream.UpdateLineColumn((char) 0x0A);
 int actualEndColumn = charStream.getEndColumn();
 assertEquals(expectedEndColumn, actualEndColumn);
 }

 @Test
 public void testGetEndLine() {
 int expectedEndLine = 3;
 charStream.UpdateLineColumn((char) 0x0D);
 charStream.UpdateLineColumn((char) 0x0A);
 charStream.UpdateLineColumn((char) 0x0D);
 int actualEndLine = charStream.getEndLine();
 assertEquals(expectedEndLine, actualEndLine);
 }

 @Test
 public void testGetLine() {
 int expectedLine = 2;
 charStream.UpdateLineColumn((char) 0x0A);
 int actualLine = charStream.getLine();
 assertEquals(expectedLine, actualLine);
 }

 @Test
 public void testGetBeginColumn() {
 int expectedBeginColumn = 10;
 charStream.UpdateLineColumn((char) 0x05);
 charStream.UpdateLineColumn((char) 0x0A);
 charStream.UpdateLineColumn((char) 0x0D);
 int actualBeginColumn = charStream.getBeginColumn();
 assertEquals(expectedBeginColumn, actualBeginColumn);
 }

 @Test
 public void testGetBeginLine() {
 int expectedBeginLine = 1;
 int actualBeginLine = charStream.getBeginLine();
 assertEquals(expectedBeginLine, actualBeginLine);
 }

 @Test
 public void testBackup() throws IOException {
 charStream.UpdateLineColumn((char) 0x05);
 charStream.backup(2);
 int expectedColumn = 3;
 int actualColumn = charStream.getColumn();
 assertEquals(expectedColumn, actualColumn);
 }

 @Test
 public void testConstructor() {
 SimpleCharStream charStream1 = new SimpleCharStream(null, 1, 1);
 assertNull(charStream1.inputStream);
 SimpleCharStream charStream2 = new SimpleCharStream(new StringReader("TestString"), 1, 1);
 assertEquals("TestString", charStream2.GetImage());
 }

 @Test
 public void testReInit() throws IOException {
 charStream.ReInit(mockInput, initialLine, initialColumn, initialBufferSize);
 assertEquals(initialLine, charStream.line);
 assertEquals(initialColumn, charStream.column);
 }

 @Test(expected = Error.class)
 public void testReInitWithError() throws IOException {
 charStream.ReInit(mockInput, initialLine, initialColumn, initialBufferSize);
 charStream.ReInit(mockInput, initialLine, initialColumn, initialBufferSize); // Should throw an error
 }

 @Test
 public void testGetImage() {
 charStream.UpdateLineColumn((char) 0x05);
 charStream.UpdateLineColumn((char) 0x0A);
 charStream.UpdateLineColumn((char) 0x0D);
 charStream.UpdateLineColumn((char) 0x05);
 charStream.UpdateLineColumn((char) 0x0A);
 charStream.UpdateLineColumn((char) 0x0D);
 charStream.UpdateLineColumn((char) 0x05);
 String expectedString = "05\n0D\n05";
 String actualString = charStream.GetImage();
 assertEquals(expectedString, actualString);
 }

 @Test
 public void testGetSuffix() {
 charStream.UpdateLineColumn((char) 0x05);
 charStream.UpdateLineColumn((char) 0x0A);
 charStream.UpdateLineColumn((char) 0x0D);
 charStream.UpdateLineColumn((char) 0x05);
 charStream.UpdateLineColumn((char) 0x0A);
 charStream.UpdateLineColumn((char) 0x0D);
 charStream.UpdateLineColumn((char) 0x05);
 char[] expectedSuffix = {"05", "0A", "0D", "05"};
 char[] actualSuffix = charStream.GetSuffix(4);
 assertArrayEquals(expectedSuffix, actualSuffix);
 }
}