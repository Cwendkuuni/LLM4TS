package JSci.maths.symbolic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private final String testInput = "Hello\nWorld\nTest\n123\t456\tTabTest\n";

 @Before
 public void setup() {
 try {
 charStream = new SimpleCharStream(new StringReader(testInput), 1, 1);
 } catch (Exception e) {
 e.printStackTrace();
 fail("Failed to initialize SimpleCharStream");
 }
 }

 @Test
 public void testBeginToken() {
 char c = charStream.BeginToken();
 assertEquals('\u0000', c);
 c = charStream.readChar();
 assertEquals('H', c);
 assertEquals(1, charStream.getLine());
 assertEquals(1, charStream.getColumn());
 }

 @Test
 public void testUpdateLineColumn() {
 char c;
 for (int i = 0; i < testInput.length(); ++i) {
 c = charStream.readChar();
 UpdateLineColumn(c);
 }
 assertEquals(8, charStream.getEndColumn());
 assertEquals(5, charStream.getEndLine());
 }

 @Test
 public void testReadChar() {
 char c;
 for (int i = 0; i < testInput.length(); ++i) {
 c = charStream.readChar();
 assertEquals(testInput.charAt(i), c);
 }
 assertEquals('\u0000', charStream.readChar());
 }

 @Test
 public void testGetLineAndColumn() {
 char c;
 for (int i = 0; i < testInput.length(); ++i) {
 c = charStream.readChar();
 UpdateLineColumn(c);
 }
 assertEquals(6, charStream.getEndLine());
 assertEquals(8, charStream.getEndColumn());
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 char c = charStream.BeginToken();
 charStream.backup(1);
 charStream.readChar();
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBoundsException() {
 charStream.backup(testInput.length());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() {
 new SimpleCharStream(null, 1, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionNegative() {
 new SimpleCharStream(new StringReader(""), -1, 1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentExceptionBufferSizeZero() {
 new SimpleCharStream(new StringReader(""), 1, 0);
 }
}