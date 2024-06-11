import org.junit.Test;
import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testFileTextConstruction() throws IOException {
        // Given
        File file = new File("src/test/resources/testfile.txt");
        String charsetName = "UTF-8";

        // When
        FileText fileText = new FileText(file, charsetName);

        // Then
        assertEquals(file, fileText.getFile());
        assertEquals(charsetName, fileText.getCharset().name());
        assertArrayEquals(new String[]{"Line 1.", "Line 2.", "Line 3."}, fileText.toLinesArray());
    }
}