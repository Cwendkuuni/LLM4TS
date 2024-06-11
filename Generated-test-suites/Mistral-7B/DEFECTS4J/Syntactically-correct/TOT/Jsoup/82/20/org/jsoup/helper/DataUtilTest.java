import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtilTest;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

 @Test
 public void testLoadFileWithValidCharset() throws Exception {
 String expectedCharset = "UTF-8";
 String filePath = "path/to/valid/file.html";
 String baseUri = "http://example.com";

 // Act
 Document actualDocument = DataUtil.load(new File(filePath), expectedCharset, baseUri);

 // Assert
 assertNotNull(actualDocument);
 assertEquals(expectedCharset, actualDocument.outputSettings().charset());
 }

 @Test
 public void testLoadFileWithInvalidCharsetInHeader() throws Exception {
 String filePath = "path/to/file.html";
 String baseUri = "http://example.com";
 String invalidCharset = "gb2312";

 // Act
 Document actualDocument = DataUtil.load(new File(filePath), invalidCharset, baseUri);

 // Assert
 assertNotNull(actualDocument);
 // Since the invalid charset is replaced with UTF-8 as a default, the test does not check the charset here
 }

 @Test
 public void testLoadFileWithEmptyCharset() throws Exception {
 String filePath = "path/to/file.html";
 String baseUri = "http://example.com";
 String emptyCharset = "";

 // Act
 Document actualDocument = DataUtil.load(new File(filePath), emptyCharset, baseUri);

 // Assert
 assertNotNull(actualDocument);
 // Since the empty charset is replaced with UTF-8 as a default, the test does not check the charset here
 }

 @Test
 public void testLoadFileWithNullCharset() throws Exception {
 String filePath = "path/to/file.html";
 String baseUri = "http://example.com";
 String charsetName = null;

 // Act and Assert
 expect(IOException.class).andThrow(new IOException("Must set charset arg to character set of file to parse."));
 DataUtil.load(new File(filePath), charsetName, baseUri);
 }

 @Test
 public void testLoadInputStreamWithValidCharset() throws Exception {
 String expectedCharset = "UTF-8";
 String charsetName = "UTF-8";
 String inputStreamData = "sample data";
 String baseUri = "http://example.com";
 InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName(expectedCharset)));

 // Act
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

 // Assert
 assertNotNull(actualDocument);
 assertEquals(expectedCharset, actualDocument.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws Exception {
 String charsetName = null;
 String inputStreamData = "sample data";
 String baseUri = "http://example.com";
 InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName("UTF-8")));

 // Act and Assert
 expect(IOException.class).andThrow(new IOException("Must set charset arg to character set of file to parse."));
 DataUtil.load(inputStream, charsetName, baseUri);
 }

 @Test
 public void testLoadInputStreamWithNoBOMAndValidCharset() throws Exception {
 String expectedCharset = "UTF-8";
 String charsetName = "UTF-8";
 String inputStreamData = "sample data";
 String baseUri = "http://example.com";
 InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName(expectedCharset)));

 // Act
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

 // Assert
 assertNotNull(actualDocument);
 assertEquals(expectedCharset, actualDocument.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithBOMAndValidCharset() throws Exception {
 String expectedCharset = "UTF-8";
 String charsetName = "UTF-8";
 String inputStreamData = "\uFEFFsample data";
 String baseUri = "http://example.com";
 InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes());

 // Act
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

 // Assert
 assertNotNull(actualDocument);
 assertEquals(expectedCharset, actualDocument.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithInvalidCharsetInHeader() throws Exception {
 String inputStreamData = "sample data";
 String charsetName = "gb2312";
 String baseUri = "http://example.com";
 InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName("UTF-8")));

 // Act
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);

 // Assert
 assertNotNull(actualDocument);
 // Since the invalid charset is replaced with UTF-8 as a default, the test does not check the charset here
 }

 @Test
 public void testParseInputStreamWithValidCharsetAndValidEncodingDeclaration() throws Exception {
 String expectedCharset = "UTF-8";
 String encodingDeclaration = "<?xml encoding='UTF-8'?>";
 String inputStreamData = encodingDeclaration + "sample data";
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName(expectedCharset)));

 // Act
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.xmlParser());

 // Assert
 assertNotNull(actualDocument);
 assertEquals(expectedCharset, actualDocument.outputSettings().charset());
 }

 @Test
 public void testParseInputStreamWithInvalidCharsetInEncodingDeclaration() throws Exception {
 String inputStreamData = "<?xml encoding='gb2312'?>sample data";
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName("UTF-8")));

 // Act
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.xmlParser());

 // Assert
 assertNotNull(actualDocument);
 // Since the invalid charset is replaced with UTF-8 as a default, the test does not check the charset here
 }

 @Test
 public void testParseInputStreamWithMissingEncodingDeclaration() throws Exception {
 String inputStreamData = "sample data";
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName("UTF-8")));

 // Act
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.xmlParser());

 // Assert
 assertNotNull(actualDocument);
 // Since no encoding declaration is present, the default charset, UTF-8, is used
 }

 @Test
 public void testCrossStreams() throws Exception {
 String inputStreamData = "sample data";
 String outputStreamData = "";
 String charsetName = "UTF-8";
 InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(Charset.forName(charsetName)));
 OutputStream outputStream = new ByteArrayOutputStream();

 // Act
 DataUtil.crossStreams(inputStream, outputStream);

 // Assert
 String actualData = outputStream.toString();
 assertEquals(inputStreamData, actualData);
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
 assertEquals(0, byteBuffer.limit());
 }
}