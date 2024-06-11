import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

 private SimpleCharStream simpleCharStream;
 private final char[] testInput = {'t', 'e', 'st', '\r', '\n', 'i', 's', ' ', 'a', ' ', 'J', 'u', 'n', 'i', 't', ' ', '4', '\r', '\n'};

 @Before
 public void setUp() {
 simpleCharStream = new SimpleCharStream(new ByteArrayInputStream(testInput), 1, 1);
 }

 @Test
 public void testBeginToken() throws IOException {
 char c = simpleCharStream.BeginToken();
 assertEquals(c, 't');
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 simpleCharStream.UpdateLineColumn('t');
 assertEquals(simpleCharStream.getColumn(), 1);
 simpleCharStream.UpdateLineColumn('\n');
 assertEquals(simpleCharStream.getLine(), 2);
 simpleCharStream.UpdateLineColumn('i');
 assertEquals(simpleCharStream.getColumn(), 2);
 simpleCharStream.UpdateLineColumn('\r');
 assertEquals(simpleCharStream.getLine(), 2);
 assertEquals(simpleCharStream.getColumn(), 1);
 }

 @Test
 public void testReadChar() throws IOException {
 char c;
 for (int i = 0; i < testInput.length; ++i) {
 c = simpleCharStream.readChar();
 assertEquals(c, testInput[i]);
 }
 }

 @Test
 public void testGetLineColumn() throws IOException {
 for (int line = 1; line <= 3; ++line) {
 for (int col = 1; col <= 8; ++col) {
 int expectedLine = line;
 int expectedCol = col;
 if (col > 8 && (col - 8) % 8 == 0) {
 expectedCol -= (col - 8) / 8 * 8;
 }
 int lineTest = simpleCharStream.getLine();
 int colTest = simpleCharStream.getColumn();
 assertEquals(expectedLine, lineTest);
 assertEquals(expectedCol, colTest);
 simpleCharStream.UpdateLineColumn(testInput[simpleCharStream.bufpos]);
 }
 }
 }

 @Test(expected = IOException.class)
 public void testIOException() throws IOException {
 simpleCharStream.FillBuff();
 simpleCharStream.inputStream.close();
 simpleCharStream.BeginToken();
 }

 @Test
 public void testExpandBuff() {
 simpleCharStream.ExpandBuff(false);
 assertEquals(simpleCharStream.bufsize, 6144);
 simpleCharStream.ExpandBuff(true);
 assertEquals(simpleCharStream.bufsize, 12288);
 }
}