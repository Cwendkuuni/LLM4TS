import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.nio.charset.StandardCharsets;
import com.google.common.io.CharStreams;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

 private static File getTestFile(String fileName) {
 return new File("path/to/your/test/files", fileName);
 }

 @Test
 public void testFileTextConstructor() throws IOException {
 File file = getTestFile("testFile.txt");
 FileText fileText = new FileText(file, "UTF-8");

 Assert.assertEquals("testFile.txt", fileText.getFile().getName());
 Assert.assertEquals("UTF-8", fileText.getCharset().name());

 String expectedText = CharStreams.readAllLines(file.newBufferedReader(StandardCharsets.UTF_8)).toString();
 Assert.assertEquals(expectedText, fileText.getFullText());
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileTextConstructorWithInvalidFile() throws IOException {
 File invalidFile = new File("non-existent-file.txt");
 try {
 new FileText(invalidFile, "UTF-8");
 }
 catch (FileNotFoundException e) {
 // expected
 }
 }

 @Test
 public void testFileTextCopyConstructor() throws IOException {
 File file = getTestFile("testFile.txt");
 FileText fileTextOriginal = new FileText(file, "UTF-8");
 FileText fileTextCopy = new FileText(fileTextOriginal);

 Assert.assertSame(file, fileTextOriginal.getFile());
 Assert.assertSame(file, fileTextCopy.getFile());
 Assert.assertSame(fileTextOriginal.charset, fileTextCopy.charset);
 Assert.assertArrayEquals(fileTextOriginal.lines, fileTextCopy.lines);
 }

 @Test
 public void testFileTextLinesConstructor() throws IOException {
 File file = getTestFile("testFile.txt");
 List<String> lines = Arrays.asList(file.getLines());
 FileText fileText = new FileText(file, lines);

 Assert.assertEquals("testFile.txt", fileText.getFile().getName());
 Assert.assertNull(fileText.charset);
 Assert.assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.lines);
 }

 @Test
 public void testFindLineBreaks() {
 FileText fileText = new FileText(getTestFile("testFile.txt"), "UTF-8");
 int[] lineBreakPositions = fileText.findLineBreaks();

 int expectedSize = fileText.size() + 1;
 Assert.assertArrayNotNull(lineBreakPositions);
 Assert.assertEquals(expectedSize, lineBreakPositions.length);
 }

 @Test
 public void testLineColumn() {
 FileText fileText = new FileText(getTestFile("testFile.txt"), "UTF-8");
 int[] lineBreakPositions = fileText.findLineBreaks();

 int pos = 42;
 LineColumn lineColumn = fileText.lineColumn(pos);

 int expectedLineNo = 2;
 int expectedColumnNo = 6;

 Assert.assertEquals(expectedLineNo, lineColumn.lineNo);
 Assert.assertEquals(expectedColumnNo, lineColumn.columnNo);
 }

 @Test
 public void testGet() {
 FileText fileText = new FileText(getTestFile("testFile.txt"), "UTF-8");
 String expectedLine1 = "This is the first line.";
 String expectedLine2 = "This is the second line.";

 String actualLine1 = fileText.get(0);
 String actualLine2 = fileText.get(1);

 Assert.assertEquals(expectedLine1, actualLine1);
 Assert.assertEquals(expectedLine2, actualLine2);
 }

 @Test
 public void testSize() {
 FileText fileText = new FileText(getTestFile("testFile.txt"), "UTF-8");
 int expectedSize = 2;

 int actualSize = fileText.size();

 Assert.assertEquals(expectedSize, actualSize);
 }
}