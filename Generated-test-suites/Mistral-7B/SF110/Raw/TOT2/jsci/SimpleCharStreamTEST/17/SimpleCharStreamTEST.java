import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 // Test case for constructor with reader and int arguments
 @Test
 public void testSimpleCharStreamWithReaderAndInt() {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
 assertEquals(1, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for BeginToken() method
 @Test
 public void testBeginToken() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals('\r', scs.BeginToken());
 assertEquals(1, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for UpdateLineColumn() method
 @Test
 public void testUpdateLineColumn() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 scs.UpdateLineColumn('\n');
 assertEquals(2, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for readChar() method
 @Test
 public void testReadChar() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals('\r', scs.readChar());
 assertEquals(1, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for getColumn() method
 @Test
 public void testGetColumn() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals(1, scs.getColumn());
 }

 // Test case for getLine() method
 @Test
 public void testGetLine() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals(1, scs.getLine());
 }

 // Test case for getEndLine() method
 @Test
 public void testGetEndLine() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals(2, scs.getEndLine());
 }

 // Test case for getBeginLine() method
 @Test
 public void testGetBeginLine() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals(1, scs.getBeginLine());
 }

 // Test case for getBeginColumn() method
 @Test
 public void testGetBeginColumn() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals(1, scs.getBeginColumn());
 }

 // Test case for getEndColumn() method
 @Test
 public void testGetEndColumn() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals(5, scs.getEndColumn());
 }

 // Test case for backup() method
 @Test
 public void testBackup() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 scs.backup(1);
 assertEquals(4, scs.getColumn());
 }

 // Test case for constructor with reader argument
 @Test
 public void testSimpleCharStreamWithReader() {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
 assertEquals(1, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for ReInit() method with reader and int arguments
 @Test
 public void testReInitWithReaderAndInt() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
 scs.ReInit(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals(2, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for ReInit() method with reader argument
 @Test
 public void testReInitWithReader() {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);scs.ReInit(new ByteArrayInputStream("test\r\ntest".getBytes()));
 assertEquals(2, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for constructor with input stream and int arguments
 @Test
 public void testSimpleCharStreamWithInputStreamAndInt() {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new ByteArrayInputStream("test".getBytes()).readAllBytes()), 1, 1);
 assertEquals(1, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for constructor with input stream argument
 @Test
 public void testSimpleCharStreamWithInputStream() {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new ByteArrayInputStream("test".getBytes()).readAllBytes()), 1, 1);
 assertEquals(1, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for ReInit() method with input stream and int arguments
 @Test
 public void testReInitWithInputStreamAndInt() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
 scs.ReInit(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals(2, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for ReInit() method with input stream argument
 @Test
 public void testReInitWithInputStream() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
 scs.ReInit(new ByteArrayInputStream("test\r\ntest".getBytes()));
 assertEquals(2, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for GetImage() method
 @Test
 public void testGetImage() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals("test\r\ntest", SimpleCharStream.GetImage());
 }

 // Test case for GetSuffix() method
 @Test
 public void testGetSuffix() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntesttest".getBytes()), 1, 1);
 assertEquals("test", SimpleCharStream.GetSuffix(4));
 }

 // Test case for Done() method
 @Test
 public void testDone() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 SimpleCharStream.Done();
 }

 // Test case for adjustBeginLineColumn() method
 @Test
 public void testAdjustBeginLineColumn() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest12345\r\ntest67890".getBytes()), 1, 1);
 SimpleCharStream.adjustBeginLineColumn(3, 6);
 assertEquals(3, scs.getLine());
 assertEquals(6, scs.getColumn());
 }

 // Test case for constructor with reader, int startline, int startcolumn, and int buffersize arguments
 @Test
 public void testSimpleCharStreamWithReaderStartlineStartcolumnBuffersize() {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
 assertEquals(1, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for constructor with reader, int startline, int startcolumn arguments
 @Test
 public void testSimpleCharStreamWithReaderStartlineStartcolumn() {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
 assertEquals(1, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for constructor with reader argument
 @Test
 public void testSimpleCharStreamWithReaderOnly() {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
 assertEquals(1, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for ReInit() method with reader, int startline, int startcolumn, and int buffersize arguments
 @Test
 public void testReInitWithReaderStartlineStartcolumnBuffersize() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
 scs.ReInit(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
 assertEquals(2, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for ReInit() method with reader, int startline, int startcolumn arguments
 @Test
 public void testReInitWithReaderStartlineStartcolumn() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
 scs.ReInit(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals(2, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for ReInit() method with reader argument
 @Test
 public void testReInitWithReaderOnly() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
 scs.ReInit(new ByteArrayInputStream("test\r\ntest".getBytes()));
 assertEquals(2, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for constructor with input stream, int startline, int startcolumn, and int buffersize arguments
 @Test
 public void testSimpleCharStreamWithInputStreamStartlineStartcolumnBuffersize() {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new ByteArrayInputStream("test".getBytes()).readAllBytes()), 1, 1, 4096);
 assertEquals(1, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for constructor with input stream, int startline, int startcolumn arguments
 @Test
 public void testSimpleCharStreamWithInputStreamStartlineStartcolumn() {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new ByteArrayInputStream("test".getBytes()).readAllBytes()), 1, 1);
 assertEquals(1, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for constructor with input stream argument
 @Test
 public void testSimpleCharStreamWithInputStreamOnly() {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new ByteArrayInputStream("test".getBytes()).readAllBytes()), 1, 1);
 assertEquals(1, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for ReInit() method with input stream, int startline, int startcolumn, and int buffersize arguments
 @Test
 public void testReInitWithInputStreamStartlineStartcolumnBuffersize() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new ByteArrayInputStream("test".getBytes()).readAllBytes()), 1, 1);
 scs.ReInit(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
 assertEquals(2, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for ReInit() method with input stream, int startline, int startcolumn arguments
 @Test
 public void testReInitWithInputStreamStartlineStartcolumn() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new ByteArrayInputStream("test".getBytes()).readAllBytes()), 1, 1);
 scs.ReInit(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 assertEquals(2, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Test case for ReInit() method with input stream argument
 @Test
 public void testReInitWithInputStreamOnly() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new ByteArrayInputStream("test".getBytes()).readAllBytes()), 1, 1);
 scs.ReInit(new ByteArrayInputStream("test\r\ntest".getBytes()));
 assertEquals(2, scs.getLine());
 assertEquals(1, scs.getColumn());
 }

 // Edge case test for ExpandBuff() method with wrapAround = true and available == bufsize
 @Test(expected = Error.class)
 public void testExpandBuffWithWrapAroundAndAvailableEqualToBufsize() throws IOException, Error {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 scs.ExpandBuff(true);
 }

 // Edge case test for ExpandBuff() method with wrapAround = false and tokenBegin < 0
 @Test(expected = Error.class)
 public void testExpandBuffWithWrapAroundFalseAndTokenBeginLessThan0() throws IOException, Error {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 scs.ExpandBuff(false);
 }

 // Error scenario test for FillBuff() method with IOException
 @Test(expected = IOException.class)
 public void testFillBuffWithIOException() throws IOException {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 scs.FillBuff();
 }

 // Error scenario test for FillBuff() method with Error
 @Test(expected = Error.class)
 public void testFillBuffWithError() throws IOException, Error {
 SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
 throw new Error("test error");
 }
}