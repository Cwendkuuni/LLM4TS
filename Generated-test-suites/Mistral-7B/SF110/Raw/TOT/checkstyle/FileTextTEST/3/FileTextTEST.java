import static org.junit.Assert.*;
import static com.google.common.io.Files.*;

import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.FileText;

public class FileTextTest {
    @Test
    public void testFileTextConstruction() throws IOException {
        String filePath = "path/to/your/file.txt";
        String charsetName = "UTF-8";

        File file = new File(filePath);
        FileText fileText = new FileText(file, charsetName);

        // Check file properties
        assertEquals(file, fileText.getFile());
        assertEquals(charsetName, fileText.getCharset().name());

        // Check file content
        String expectedContent = readLinesFromFile(filePath, charsetName).toString();
        String actualContent = fileText.getFullText();
        assertEquals(expectedContent, actualContent);

        // Check line count
        int expectedLineCount = readLinesFromFile(filePath, charsetName).size();
        assertEquals(expectedLineCount, fileText.size());
    }

    private List<String> readLinesFromFile(String filePath, String charsetName) throws IOException {
        return readLinesFromFile(new File(filePath), charsetName);
    }

    private List<String> readLinesFromFile(File file, String charsetName) throws IOException {
        return Files.readLines(file, Charsets.forName(charsetName));
    }
}