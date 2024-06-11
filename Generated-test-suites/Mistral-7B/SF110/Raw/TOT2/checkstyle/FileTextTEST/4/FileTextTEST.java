import static org.junit.Assert.*;
import com.google.common.io.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;
import org.junit.Test;

public class FileTextTest {

  @Test
  public void testFileTextConstructionFromFileAndCharset() {
    File file = new File("test.txt");
    FileText fileText = new FileText(file, "UTF-8");
    assertEquals(file, fileText.getFile());
    assertEquals("UTF-8", fileText.getCharset().name());
    String expectedFullText = Files.readFirstLine(file, Charsets.UTF_8);
    String actualFullText = fileText.getFullText();
    assertEquals(expectedFullText, actualFullText);
  }

  // Test other constructors, getters, and methods here

  // Test edge cases and exceptions

  @Test(expected = FileNotFoundException.class)
  public void testFileTextConstructionFromNonExistingFile() {
    File file = new File("non_existing_file.txt");
    new FileText(file, "UTF-8");
  }

  @Test(expected = UnsupportedCharsetException.class)
  public void testFileTextConstructionFromUnsupportedCharset() {
    File file = new File("test.txt");
    new FileText(file, "XYZ");
  }

  @Test
  public void testFileTextCloning() {
    FileText fileText1 = new FileText("test line 1\n test line 2");
    FileText fileText2 = new FileText(fileText1);
    assertEquals(fileText1, fileText2);
  }

  @Test
  public void testLineColumnNegativeIndex() {
    FileText fileText = new FileText("test line 1\n test line 2");
    LineColumn lineColumn = fileText.lineColumn(-1);
    assertNull(lineColumn);
  }

  @Test
  public void testLineColumnEndOfLine() {
    FileText fileText = new FileText("test line 1\n test line 2");
    LineColumn lineColumn = fileText.lineColumn(fileText.size());
    assertEquals(2, lineColumn.getLineNumber());
    assertEquals(1, lineColumn.getColumnNumber());
  }

  @Test
  public void testSizeWithLF() {
    File file = new File("test_lf.txt");
    List<String> lines = Files.readLines(file, Charsets.UTF_8);
    FileText fileText = new FileText(file, lines);
    assertEquals(lines.size(), fileText.size());
  }

  @Test
  public void testSizeWithCRLF() {
    File file = new File("test_crlf.txt");
    List<String> lines = Files.readLines(file, Charsets.UTF_8);
    FileText fileText = new FileText(file, lines);
    assertEquals(lines.size(), fileText.size());
  }

  @Test
  public void testGetFromInvalidIndex() {
    FileText fileText = new FileText("test line 1\n test line 2");
    assertNull(fileText.get(-1));
  }

  @Test
  public void testGetFromValidIndex() {
    FileText fileText = new FileText("test line 1\n test line 2");
    String expectedLine = "test line 1";
    String actualLine = fileText.get(0);
    assertEquals(expectedLine, actualLine);
  }

  @Test
  public void testGetFromLastIndex() {
    FileText fileText = new FileText("test line 1\n test line 2");
    String expectedLine = "test line 2";
    String actualLine = fileText.get(fileText.size() - 1);
    assertEquals(expectedLine, actualLine);
  }
}