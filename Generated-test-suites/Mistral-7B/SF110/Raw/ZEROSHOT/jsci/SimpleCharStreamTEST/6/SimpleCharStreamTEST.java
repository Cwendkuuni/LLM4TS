import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private int startLine;
 private int startColumn;
 private int bufferSize;
 private byte[] inputData;

 @Before
 public void setUp() {
 inputData = new byte[] {'t', 'h', 'i', 's', ' ', 'i', 's', ' ', 'a', ' ', 's', ' ', 's', 'i', 'm', 'p', 'l', 'e', '\n', 'a', ' ', 'f', 'i', 'l', 'e', '\r', '\n', ' ', 't', 'e', 's', 't', '\r', '\n', ' ', 'c', 'l', 'a', 's', 's', ' '};
 startLine = 1;
 startColumn = 1;
 bufferSize = 1024;
 charStream = new SimpleCharStream(new ByteArrayInputStream(inputData), startLine, startColumn, bufferSize);
 }

 @After
 public void tearDown() {
 try {
 charStream.Done();
 } catch (Exception e) {
 // Ignore any exceptions
 }
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = charStream.BeginToken();
 assertEquals('t', c);
 }

 @Test
 public void testReadChar() throws IOException {
 char c;
 for (int i = 0; i < inputData.length; ++i) {
 c = charStream.readChar();
 assertEquals((char) inputData[i], c);
 }
 }

 @Test
 public void testGetColumn() throws IOException {
 int column;
 for (int i = 0; i < inputData.length; ++i) {
 char c = charStream.readChar();
 column = charStream.getColumn();
 assertEquals(i + 2, column);
 }
 }

 @Test
 public void testGetLine() throws IOException {
 int line;
 for (int i = 0; i < 10; ++i) {
 char c = charStream.readChar();
 line = charStream.getLine();
 assertEquals(i + 1, line);
 }
 }

 @Test
 public void testGetEndColumn() throws IOException {
 int column;
 for (int i = 0; i < inputData.length; ++i) {
 char c = charStream.readChar();
 column = charStream.getEndColumn();
 assertEquals(i + 2, column);
 }
 }

 @Test
 public void testGetEndLine() throws IOException {
 int line;
 for (int i = 0; i < 10; ++i) {
 char c = charStream.readChar();
 line = charStream.getEndLine();
 assertEquals(i + 1, line);
 }
 }

 @Test
 public void testGetBeginColumn() throws IOException {
 int column;
 for (int i = 0; i < inputData.length; ++i) {
 char c = charStream.readChar();
 column = charStream.getBeginColumn();
 assertEquals(i + 1, column);
 }
 }

 @Test
 public void testGetBeginLine() throws IOException {
 int line;
 for (int i = 0; i < inputData.length; ++i) {
 char c = charStream.readChar();
 line = charStream.getBeginLine();
 assertEquals(1, line);
 }
 }

 @Test
 public void testBackup() throws IOException {
 char c;
 for (int i = 0; i < inputData.length; ++i) {
 charStream.readChar();
 if (i % 2 == 0) {
 charStream.backup(1);
 c = charStream.readChar();
 assertEquals((char) inputData[i - 1], c);
 }
 }
 }

 @Test
 public void testExpandBuff() throws IOException {
 char c;
 for (int i = 0; i < inputData.length * 2; ++i) {
 charStream.readChar();
 }
 charStream.ExpandBuff(false);
 c = charStream.readChar();
 assertEquals(inputData[inputData.length - 1], c);
 }

 @Test(expected = IOException.class)
 public void testFillBuffException() throws IOException {
 charStream.FillBuff();
 }
}