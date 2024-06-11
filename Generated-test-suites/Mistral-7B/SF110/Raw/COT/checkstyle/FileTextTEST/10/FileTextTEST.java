import static org.junit.Assert.*;
import static com.google.common.io.Files.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class FileTextTest {

 private static final String TEST_FILE_PATH = "src/test/resources/FileTextTest.txt";

 @Test
 public void testFileTextConstructionFromFile() throws IOException {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), "UTF-8");
 assertEquals(10, fileText.size());
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileTextConstructionFromNonExistingFile() throws IOException {
 new FileText(new File("nonExistentFile.txt"), "UTF-8");
 }

 @Test(expected = UnsupportedCharsetException.class)
 public void testFileTextConstructionWithUnsupportedCharset() {
 try {
 new FileText(new File(TEST_FILE_PATH), "X-1234");
 fail();
 } catch (IllegalStateException ex) {
 String expectedMessage = "Unsupported charset: X-1234";
 assertEquals(expectedMessage, ex.getMessage());
 }
 }

 @Test
 public void testFileTextConstructionFromFileText() {
 FileText fileText = new FileText(Arrays.asList("Test line 1", "Test line 2", "Test line 3"));
 assertEquals(3, fileText.size());
 }

 @Test
 public void testGetFile() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), "UTF-8");
 assertEquals(new File(TEST_FILE_PATH), fileText.getFile());
 }

 @Test
 public void testGetCharset() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), "UTF-8");
 assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
 }

 @Test
 public void testGetFullText() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), "UTF-8");
 String fullText = fileText.getFullText();
 try {
 String content = readLinesFromFile(TEST_FILE_PATH, "UTF-8").get(0);
 assertEquals(content, fullText);
 } catch (IOException e) {
 e.printStackTrace();
 fail("Failed to read file content.");
 }
 }

 @Test
 public void testToLinesArray() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), "UTF-8");
 String[] expectedLines = readLinesFromFile(TEST_FILE_PATH, "UTF-8").toArray(new String[0]);
 assertArrayEquals(expectedLines, fileText.toLinesArray());
 }

 @Test
 public void testFindLineBreaks() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), "UTF-8");
 int[] lineBreaks = fileText.findLineBreaks();
 int lineNo = 1;
 int expectedLineBreak = 7;
 assertEquals(expectedLineBreak, lineBreaks[lineNo]);
 }

 @Test
 public void testLineColumn() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), "UTF-8");
 int columnNo = 5;
 LineColumn expectedLineColumn = new LineColumn(2, 5);
 LineColumn actualLineColumn = fileText.lineColumn(10 + columnNo);
 assertEquals(expectedLineColumn, actualLineColumn);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testLineColumnOutOfBounds() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), "UTF-8");
 LineColumn lineColumn = fileText.lineColumn(100);
 }

 @Test
 public void testToString() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), "UTF-8");
 StringBuilder expectedString = new StringBuilder();
 expectedString.append("Test line 1\n");
 expectedString.append("Test line 2\n");
 expectedString.append("Test line 3\n");
 expectedString.append("Test line 4\n");
 expectedString.append("Test line 5\n");
 expectedString.append("Test line 6\n");
 expectedString.append("Test line 7\n");
 expectedString.append("Test line 8\n");
 expectedString.append("Test line 9\n");
 expectedString.append("Test line 10\n");
 String actualString = fileText.toString();
 assertEquals(expectedString.toString(), actualString);
 }
}