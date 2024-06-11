import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

 @Test
 public void testLoadFileWithValidCharset() throws IOException {
 File inputFile = new File("validFile.html"); // Replace with actual valid file path
 String charsetName = "UTF-8"; // Replace with actual charset if known
 String baseUri = "http://example.com"; // Replace with actual base URI if needed
 Document doc = DataUtil.load(inputFile, charsetName, baseUri);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithInvalidCharsetHeader() throws IOException {
 File inputFile = new File("invalidCharsetFile.html"); // Replace with actual invalid charset file path
 String charsetName = null; // Let the method detect charset
 String baseUri = "http://example.com"; // Replace with actual base URI if needed
 Document doc = DataUtil.load(inputFile, charsetName, baseUri);
 assertNotNull(doc);
 // Add assertions to check if charset was correctly detected and decoded
 }

 @Test
 public void testLoadFileWithNoCharsetHeader() throws IOException {
 File inputFile = new File("noCharsetFile.html"); // Replace with actual file path with no charset header
 String charsetName = "UTF-8"; // Default charset
 String baseUri = "http://example.com"; // Replace with actual base URI if needed
 Document doc = DataUtil.load(inputFile, charsetName, baseUri);
 assertNotNull(doc);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithIOException() throws IOException {
 File inputFile = new File("invalidFile.txt"); // Replace with actual invalid file path
 String charsetName = null; // Let the method detect charset
 String baseUri = "http://example.com"; // Replace with actual base URI if needed
 Document doc = null;
 try {
 doc = DataUtil.load(inputFile, charsetName, baseUri);
 } catch (IllegalArgumentException e) {
 // Handle the IllegalArgumentException if the file is not a valid HTML or XML file
 throw new IOException(e.getMessage());
 }
 assertNull(doc);
 }

 @Test
 public void testLoadInputStreamWithValidCharset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/validFile.html"); // Replace with actual valid file path
 String charsetName = "UTF-8"; // Replace with actual charset if known
 String baseUri = "http://example.com"; // Replace with actual base URI if needed
 Document doc = DataUtil.load(inputStream, charsetName, baseUri);
 assertNotNull(doc);
 }

 // Add more test cases for other methods like 'parseInputStream()', 'load() with different baseUri', 'crossStreams()', 'parseInputStream() with unsupported charset' etc.

}