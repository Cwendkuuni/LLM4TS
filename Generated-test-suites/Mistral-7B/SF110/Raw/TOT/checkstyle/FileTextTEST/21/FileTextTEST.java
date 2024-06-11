import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.FileText;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class FileTextTest {
  @Test
  public void testFileTextCreation() throws IOException {
    File file = new File("test_file.txt"); // Replace with a valid file path
    FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
    String expectedText = readFile("test_file.txt", StandardCharsets.UTF_8); // Replace with a method to read file content
    assertEquals(expectedText, fileText.getFullText());

    int[] lineBreaks = fileText.findLineBreaks();
    String[] lines = fileText.toLinesArray();
    int numLines = lines.length;

    // Check line breaks and number of lines
    for (int i = 0; i < numLines; i++) {
      int lineBreakPosition = lineBreaks[i + 1];
      String line = lines[i];
      int lineLength = line.length();
      int expectedLineBreakPosition = lineLength + i * System.lineSeparator().length().intValue();
      assertEquals(expectedLineBreakPosition, lineBreakPosition);
    }
  }
}