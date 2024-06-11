import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        // Given
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document document = DataUtil.load(inputFile, charsetName, baseUri);

        // Then
        assertNotNull(document);
        assertEquals("HTML Document", document.tagName(), "html");
    }

    @Test(expected = IOException.class)
    public void testLoadFileInvalidCharset() throws IOException {
        // Given
        File inputFile = new File("test.html");
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        // When and Then
        DataUtil.load(inputFile, charsetName, baseUri); // Exception should be thrown
    }

    @Test
    public void testLoadFileNoCharset() throws IOException {
        // Given
        File inputFile = new File("test.html");
        String charsetName = null;
        String baseUri = "http://example.com";

        // When
        Document document = DataUtil.load(inputFile, charsetName, baseUri);

        // Then
        assertNotNull(document);
        assertEquals("HTML Document", document.tagName(), "html");
        // Charset should be detected automatically
    }
}