package JSci.maths.symbolic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SimpleCharStreamTest {

 private SimpleCharStream charStream;
 private Reader mockReader;

 @Before
 public void setUp() {
 mockReader = Mockito.mock(Reader.class);
 charStream = new SimpleCharStream(mockReader, 1, 1, 4096);
 }

 @Test
 public void testBeginToken() throws IOException {
 char testChar = (char) 0x0A; // LF character
 char expectedChar = testChar;
 char actualChar = charStream.BeginToken();
 assertEquals("BeginToken should return the first character in the input stream", expectedChar, actualChar);
 }

 @Test
 public void testUpdateLineColumn() throws IOException {
 char testChar = 'A';
 char expectedCR = '\r';
 char expectedLF = '\n';
 char expectedTab = '\t';
 char expectedSpace = ' ';
 char expectedChar = testChar;
 char actualChar;
 int line = 1;
 int column = 1;
 int newColumn = 2;
 int newLine = 2;

 // Test \r character
 charStream.UpdateLineColumn(expectedCR);
 assertEquals("Line should be incremented and column should be reset to 1 for \r character", line + 1, charStream.getLine());
 assertEquals("Column should be reset to 1 for \r character", 1, charStream.getColumn());

 // Test \n character
 charStream.UpdateLineColumn(expectedLF);
 assertEquals("Line should be incremented and column should be reset to 1 for \n character", line + 1, charStream.getLine());
 assertEquals("Column should be reset to 1 for \n character", 1, charStream.getColumn());

 // Test \t character
 charStream.UpdateLineColumn(expectedTab);
 assertEquals("Line should be incremented and column should be updated based on \t character", line, charStream.getLine());
 assertEquals("Column should be updated based on \t character", column + 8, charStream.getColumn());

 // Test space character
 charStream.UpdateLineColumn(expectedSpace);
 assertEquals("Line should be incremented and column should be updated based on space character", line, charStream.getLine());
 assertEquals("Column should be updated based on space character", column + 1, charStream.getColumn());

 // Test normal character
 charStream.UpdateLineColumn(expectedChar);
 assertEquals("Line should be the same and column should be updated based on the character", line, charStream.getLine());
 assertEquals("Column should be updated based on the character", column + 1, charStream.getColumn());

 // Test multiple characters
 charStream.UpdateLineColumn(expectedCR);
 charStream.UpdateLineColumn(expectedTab);
 charStream.UpdateLineColumn(expectedSpace);
 charStream.UpdateLineColumn(expectedLF);
 charStream.UpdateLineColumn(expectedChar);
 assertEquals("Line should be incremented twice and column should be updated based on the characters", line + 2, charStream.getLine());
 assertEquals("Column should be updated based on the characters", column + 11, charStream.getColumn());
 }

 @Test(expected = IOException.class)
 public void testFillBuffThrowsIOException() throws IOException {
 char[] testBuffer = new char[1];
 charStream.FillBuff();
 charStream.inputStream = Mockito.mock(IOException.class);
 charStream.FillBuff();
 }

 @Test
 public void testGetLineColumn() throws IOException {
 int line = 1;
 int column = 1;
 char testChar = 'A';
 int newLine = 2;
 int newColumn = 2;

 // Test getLine() and getColumn() for the same character
 charStream.UpdateLineColumn(testChar);
 assertEquals("GetLine() should return the line number where the character was read", line, charStream.getLine());
 assertEquals("GetColumn() should return the column number where the character was read", column, charStream.getColumn());

 // Test getLine() and getColumn() for a new line
 charStream.UpdateLineColumn(testChar);
 charStream.UpdateLineColumn('\n');
 assertEquals("GetLine() should return the line number where the newline was read", line + 1, charStream.getLine());
 assertEquals("GetColumn() should return 1 as the column number is reset to 1 for a new line", 1, charStream.getColumn());

 // Test getEndLine() and getEndColumn() for the same character
 charStream.UpdateLineColumn(testChar);
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn(testChar);
 assertEquals("GetEndLine() should return the line number where the last character was read", line + 1, charStream.getEndLine());
 assertEquals("GetEndColumn() should return the column number where the last character was read", column + 2, charStream.getEndColumn());

 // Test getBeginLine() and getBeginColumn() for the same character
 charStream.UpdateLineColumn(testChar);
 charStream.UpdateLineColumn('\n');
 charStream.UpdateLineColumn(testChar);
 charStream.backup(1);
 assertEquals("GetBeginLine() should return the line number where the previous character was read", line, charStream.getBeginLine());
 assertEquals("GetBeginColumn() should return the column number where the previous character was read", column, charStream.getBeginColumn());
 }

 @Test
 public void testBackup() throws IOException {
 int amount = 1;
 int expectedBackPos = charStream.bufpos - amount;
 int expectedBufPos = charStream.bufpos;
 char expectedChar = (char) 0x0A; // LF character
 char actualChar;

 // Test backing up by less than the amount of characters in the buffer
 charStream.UpdateLineColumn(expectedChar);
 charStream.backup(amount + 1);
 assertEquals("Buffer position should not be changed", expectedBufPos, charStream.bufpos);
 assertEquals("Character read should be the same", expectedChar, charStream.buffer[charStream.bufpos]);

 // Test backing up by the exact amount of characters in the buffer
 charStream.UpdateLineColumn(expectedChar);
 charStream.backup(amount);
 assertEquals("Buffer position should be changed", expectedBackPos, charStream.bufpos);
 assertEquals("Character read should be the same", expectedChar, charStream.buffer[charStream.bufpos]);

 // Test backing up by more characters than in the buffer
 charStream.UpdateLineColumn(expectedChar);
 charStream.backup(charStream.bufsize + amount);
 assertEquals("Buffer position should be equal to the beginning of the buffer", 0, charStream.bufpos);
 assertEquals("Character read should be the same", expectedChar, charStream.buffer[charStream.bufpos]);
 }

 @Test
 public void testReInit() throws IOException {
 Reader mockReader2 = Mockito.mock(Reader.class);
 SimpleCharStream charStream2 = new SimpleCharStream(mockReader2, 3, 3, 4096);
 charStream.ReInit(mockReader, 1, 1, 4096);
 assertEquals("Buffer position should be reset to -1", -1, charStream.bufpos);
 assertEquals("Line and column should be reset to the initial values", 1, charStream.getLine());
 assertEquals("Line and column should be reset to the initial values", 0, charStream.getColumn());
 charStream2.ReInit(mockReader, 3, 3, 4096);
 assertEquals("Buffer position, line and column should not be changed for the second charStream instance", charStream.bufpos, charStream2.bufpos);
 assertEquals("Line and column should be the same for the second charStream instance", charStream.getLine(), charStream2.getLine());
 assertEquals("Column should be the same for the second charStream instance", charStream.getColumn(), charStream2.getColumn());
 }
}