import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

public class FileTextTest {
    @Test
    public void testFileTextConstruction() throws IOException {
        String filePath = "test/resources/sample.txt";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        FileText fileText = new FileText(file, charsetName);
        // Check the instance variables
        assertNotNull(fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotSame(file, fileText.getFile());
        // Check the size and content of the FileText object
        assertEquals(10, fileText.size());
        String expectedContent = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10";
        assertEquals(expectedContent, fileText.getFullText());
        // Check the lines array
        assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3", "Line 4", "Line 5", "Line 6", "Line 7", "Line 8", "Line 9", "Line 10"}, fileText.toLinesArray());
    }
}