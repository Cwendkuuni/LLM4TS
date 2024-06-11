import org.junit.Test;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.*;

public class DataUtilTest {

 // Test case for load method with valid file, default charset, and baseUri as null
 @Test
 public void testLoadValidFile() throws IOException {
 File file = new File("path/to/valid/file.html");
 Document document = DataUtil.load(file, null, null);
 assertNotNull(document);
 // Add assertions for other expected properties of the Document object
 }

 // Test case for load method with valid file, specified charset, and baseUri as null
 @Test
 public void testLoadValidFileWithCharset() throws IOException {
 File file = new File("path/to/valid/file.html");
 Document document = DataUtil.load(file, "UTF-16", null);
 assertNotNull(document);
 // Add assertions for other expected properties of the Document object
 }

 // Test case for load method with valid file, specified charset, and baseUri as "http://example.com"
 @Test
 public void testLoadValidFileWithBaseUri() throws IOException {
 File file = new File("path/to/valid/file.html");
 Document document = DataUtil.load(file, "UTF-16", "http://example.com");
 assertNotNull(document);
 // Add assertions for other expected properties of the Document object
 }

 // Test case for load method with null file
 @Test(expected = IOException.class)
 public void testLoadNullFile() {
 DataUtil.load(null, null, null);
 }

 // Test case for load method with empty file
 @Test
 public void testLoadEmptyFile() throws IOException {
 File file = new File("path/to/empty/file.html");
 Document document = DataUtil.load(file, null, null);
 assertEquals(0, document.childNodeSize());
 }

 // Test case for loadInputStream method with valid InputStream, default charset, and baseUri as null
 @Test
 public void testLoadInputStreamValid() throws IOException {
 InputStream inputStream = new FileInputStream("path/to/valid/file.html");
 Document document = DataUtil.load(inputStream, null, null);
 assertNotNull(document);
 // Add assertions for other expected properties of the Document object
 inputStream.close();
 }

 // Test case for loadInputStream method with valid InputStream, specified charset, and baseUri as null
 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 InputStream inputStream = new FileInputStream("path/to/valid/file.html");
 Document document = DataUtil.load(inputStream, "UTF-16", null);
 assertNotNull(document);
 // Add assertions for other expected properties of the Document object
 inputStream.close();
 }

 // Test case for loadInputStream method with valid InputStream, specified charset, and baseUri as "http://example.com"
 @Test
 public void testLoadInputStreamWithBaseUri() throws IOException {
 InputStream inputStream = new FileInputStream("path/to/valid/file.html");
 Document document = DataUtil.load(inputStream, "UTF-16", "http://example.com");
 assertNotNull(document);
 // Add assertions for other expected properties of the Document object
 inputStream.close();
 }

 // Test case for loadInputStream method with null InputStream
 @Test(expected = IOException.class)
 public void testLoadInputStreamNull() throws IOException {
 DataUtil.load(null, null, null);
 }

 // Test case for loadInputStream method with empty InputStream
 @Test
 public void testLoadInputStreamEmpty() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 Document document = DataUtil.load(inputStream, null, null);
 assertEquals(0, document.childNodeSize());
 inputStream.close();
 }

 // Test case for parseInputStream method with valid InputStream, default charset, and baseUri as null
 @Test
 public void testParseInputStreamValid() throws IOException {
 InputStream inputStream = new FileInputStream("path/to/valid/file.html");
 Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 assertNotNull(document);
 // Add assertions for other expected properties of the Document object
 inputStream.close();
 }

 // Test case for parseInputStream method with valid InputStream, specified charset, and baseUri as null
 @Test
 public void testParseInputStreamWithCharset() throws IOException {
 InputStream inputStream = new FileInputStream("path/to/valid/file.html");
 Document document = DataUtil.parseInputStream(inputStream, "UTF-16", null, Parser.htmlParser());
 assertNotNull(document);
 // Add assertions for other expected properties of the Document object
 inputStream.close();
 }

 // Test case for parseInputStream method with valid InputStream, specified charset, and baseUri as "http://example.com"
 @Test
 public void testParseInputStreamWithBaseUri() throws IOException {
 InputStream inputStream = new FileInputStream("path/to/valid/file.html");
 Document document = DataUtil.parseInputStream(inputStream, "UTF-16", "http://example.com", Parser.htmlParser());
 assertNotNull(document);
 // Add assertions for other expected properties of the Document object
 inputStream.close();
 }

 // Test case for parseInputStream method with null InputStream
 @Test(expected = IOException.class)
 public void testParseInputStreamNull() throws IOException {
 DataUtil.parseInputStream(null, null, null, Parser.htmlParser());
 }

 // Test case for parseInputStream method with empty InputStream
 @Test
 public void testParseInputStreamEmpty() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[0]);
 Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 assertEquals(0, document.childNodeSize());
 inputStream.close();
 }

 // Test case for crossStreams method with valid InputStream and OutputStream
 @Test
 public void testCrossStreamsValid() throws IOException {
 InputStream inputStream = new FileInputStream("path/to/valid/file.html");
 OutputStream outputStream = new FileOutputStream("path/to/output/file.html");
 DataUtil.crossStreams(inputStream, outputStream);
 // Add assertions for the contents of the output file
 inputStream.close();
 outputStream.close();
 }

 // Test case for crossStreams method with null InputStream
 @Test(expected = IOException.class)
 public void testCrossStreamsNullInput() throws IOException {
 DataUtil.crossStreams(null, new FileOutputStream("path/to/output/file.html"));
 }

 // Test case for crossStreams method with null OutputStream
 @Test(expected = IOException.class)
 public void testCrossStreamsNullOutput() throws IOException {
 DataUtil.crossStreams(new FileInputStream("path/to/input/file.html"), null);
 }

 // Test case for parseInputStream method with invalid InputStream
 @Test(expected = IOException.class)
 public void testParseInputStreamInvalid() throws IOException {
 InputStream inputStream = new FileInputStream("path/to/invalid/file");
 Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 fail("Should have thrown an IOException");
 inputStream.close();
 }
}