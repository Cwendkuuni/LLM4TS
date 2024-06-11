
import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class FileTextTest {

    @Test
    public void testFileTextConstructionWithValidFileAndCharset() throws IOException {
        File file = new File("test/validFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals("UTF-8", fileText.getCharset().name());
        // Add assertions for lines, size, and other attributes
    }

    @Test(expected = IllegalStateException.class)
    public void testFileTextConstructionWithInvalidCharset() {
        File file = new File("test/validFile.txt");
        try {
            new FileText(file, "InvalidCharset");
        } catch (IllegalStateException ex) {
            assertEquals("Unsupported charset: InvalidCharset", ex.getMessage());
        }
    }

    @Test
    public void testFileTextConstructionWithEmptyFile() {
        File file = new File("test/emptyFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(0, fileText.size());
    }

    @Test(expected = NullPointerException.class)
    public void testFileTextConstructionWithNullFile() {
        try {
            new FileText(null, "UTF-8");
        } catch (NullPointerException ex) {
            // Additional assertions for exceptions
        }
    }

    @Test(expected = NullPointerException.class)
    public void testFileTextConstructionWithValidFileAndNullCharset() {
        File file = new File("test/validFile.txt");
        try {
            new FileText(file, null);
        } catch (NullPointerException ex) {
            // Additional assertions for exceptions
        }
    }

    @Test
    public void testGetLineColumn() {
        File file = new File("test/validFile.txt");
        FileText fileText = new FileText(file, "UTF-8");

        int lineNo = 5;
        int columnNo = 10;
        LineColumn lineColumn = fileText.lineColumn(lineNo * fileText.size() + columnNo);

        assertEquals(lineNo, lineColumn.getLineNumber());
        assertEquals(columnNo, lineColumn.getColumnNumber());
    }

    // Add test cases for other methods, such as toLinesArray and findLineBreaks
}