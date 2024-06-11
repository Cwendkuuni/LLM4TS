import static org.junit.Assert.*;
import static com.google.common.io.Files.*;

import org.junit.Test;

public class FileTextTest {
  @Test
  public void testFileTextConstruction() throws IOException {
    String fileContent = "Line 1\nLine 2\nLine 3";
    File tempFile = createTempFile("test", "txt");
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"))) {
      writer.write(fileContent);
    }

    FileText fileText = new FileText(tempFile, "UTF-8");
    assertEquals(3, fileText.size());
    assertEquals("Line 1", fileText.get(0));
    assertEquals("Line 2", fileText.get(1));
    assertEquals("Line 3", fileText.get(2));

    String expectedLine = "Expected line";
    FileText fileTextWithInvalidCharset = new FileText(tempFile, "INVALID_CHARSET");
    try {
      assertEquals(expectedLine, fileTextWithInvalidCharset.get(0));
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }

    Files.delete(tempFile.toPath());
  }
}