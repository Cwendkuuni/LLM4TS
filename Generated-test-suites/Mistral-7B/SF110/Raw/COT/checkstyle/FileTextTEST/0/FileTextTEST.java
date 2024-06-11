import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

 private FileText fileText;
 private static final String FILE_PATH = "src/test/resources/FileTextTest.java";

 @Before
 public void setUp() throws IOException {
 File file = new File(FILE_PATH);
 fileText = new FileText(file, StandardCharsets.UTF_8);
 }

 @Test
 public void testConstructorWithFileAndCharset() throws IOException {
 File file = new File(FILE_PATH);
 Charset charset = StandardCharsets.UTF_8;
 FileText expectedFileText = new FileText(file, charset.name());
 Assert.assertArrayEquals(expectedFileText.toLinesArray(), fileText.toLinesArray());
 }

 @Test(expected = FileNotFoundException.class)
 public void testConstructorWithInvalidFile() throws IOException {
 File file = new File("non_existent_file.txt");
 FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
 }

 @Test
 public void testConstructorWithFileText() {
 FileText fileText1 = new FileText(this.fileText);
 Assert.assertArrayEquals(fileText.toLinesArray(), fileText1.toLinesArray());
 }

 @Test
 public void testFromLines() {
 List<String> lines = Arrays.asList(this.fileText.toLinesArray());
 FileText fileTextFromLines = FileText.fromLines(new File(FILE_PATH), lines);
 Assert.assertArrayEquals(fileTextFromLines.toLinesArray(), fileText.toLinesArray());
 }

 @Test
 public void testGetFile() {
 File expectedFile = new File(FILE_PATH);
 Assert.assertEquals(expectedFile, fileText.getFile());
 }

 @Test
 public void testGetCharset() {
 Charset expectedCharset = StandardCharsets.UTF_8;
 Assert.assertEquals(expectedCharset, fileText.getCharset());
 }

 @Test
 public void testGetFullText() {
 String expectedFullText = Files.toString(new File(FILE_PATH), StandardCharsets.UTF_8);
 Assert.assertEquals(expectedFullText, fileText.getFullText());
 }

 @Test
 public void testToLinesArray() {
 String[] expectedLines = this.fileText.toLinesArray();
 String[] actualLines = fileText.toLinesArray();
 Assert.assertArrayEquals(expectedLines, actualLines);
 }

 @Test
 public void testFindLineBreaks() {
 int[] expectedLineBreaks = fileText.findLineBreaks();
 int[] actualLineBreaks = fileText.lineBreaks;
 Assert.assertArrayEquals(expectedLineBreaks, actualLineBreaks);
 }

 @Test
 public void testLineColumn() {
 int pos = 38;
 LineColumn expectedLineColumn = new LineColumn(1, 4);
 LineColumn actualLineColumn = fileText.lineColumn(pos);
 Assert.assertEquals(expectedLineColumn, actualLineColumn);
 }

 @Test
 public void testGetWithValidIndex() {
 String expectedLine = "public class FileTextTest {\n";
 String actualLine = fileText.get(0);
 Assert.assertEquals(expectedLine, actualLine);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithInvalidIndex() {
 int invalidIndex = fileText.size() + 1;
 String actualLine = fileText.get(invalidIndex);
 }

 @Test
 public void testSize() {
 int expectedSize = this.fileText.size();
 int actualSize = fileText.size();
 Assert.assertEquals(expectedSize, actualSize);
 }

}