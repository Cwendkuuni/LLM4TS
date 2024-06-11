import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private static final String HTML_CONTENT_TYPE = "text/html";
 private static final String XML_CONTENT_TYPE = "application/xml";
 private static final String UTF8_CHARSET = "UTF-8";
 private static final String GB2312_CHARSET = "gb2312";
 private static final String ISO_8859_1_CHARSET = "ISO-8859-1";
 private static final String CHARSET_HEADER = "Content-Type: " + HTML_CONTENT_TYPE + "; charset=" + UTF8_CHARSET;
 private static final String CHARSET_XML_HEADER = "Content-Type: " + XML_CONTENT_TYPE;
 private static final String META_CHARSET_HTML = "<meta http-equiv='Content-Type' content='text/html; charset=" + UTF8_CHARSET + "'>";
 private static final String META_CHARSET_XML = "<meta charset='" + UTF8_CHARSET + "'>";
 private static final String XML_DECLARATION = "<?xml encoding='" + ISO_8859_1_CHARSET + "'?>";
 private static final String CHUNK_SIZE = "1024";
 private Document document;
 private Parser parser;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 parser = new Parser();
 }

 @Test
 public void testLoadFileWithUtf8Charset() throws IOException {
 InputStream inputStream = new FileInputStream(new File("test.html"));
 String charsetName = UTF8_CHARSET;
 String baseUri = "/test";
 Document expectedDocument = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
 assertEquals(expectedDocument, actualDocument);
 inputStream.close();
 }

 @Test
 public void testLoadFileWithNullCharset() throws IOException {
 InputStream inputStream = new FileInputStream(new File("test.html"));
 String charsetName = null;
 String baseUri = "/test";
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
 Document expectedDocument = new Document(baseUri); // empty document
 assertEquals(expectedDocument, actualDocument);
 inputStream.close();
 }

 @Test
 public void testLoadFileWithUnknownCharset() throws IOException {
 InputStream inputStream = new FileInputStream(new File("test.html"));
 String charsetName = "unknownCharset";
 String baseUri = "/test";
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
 Document expectedDocument = new Document(baseUri); // empty document
 assertEquals(expectedDocument, actualDocument);
 inputStream.close();
 }

 @Test
 public void testLoadInputStreamWithUtf8Charset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(META_CHARSET_HTML.getBytes(StandardCharsets.UTF_8));
 String charsetName = UTF8_CHARSET;
 String baseUri = "/test";
 Document expectedDocument = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testLoadInputStreamWithCharsetFromContentType() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(CHARSET_HEADER.getBytes(StandardCharsets.UTF_8));
 String charsetName = null;
 String baseUri = "/test";
 Document expectedDocument = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testLoadInputStreamWithUTF8CharsetDetected() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(HTML_CONTENT_TYPE.getBytes(StandardCharsets.UTF_8));
 String charsetName = null;
 String baseUri = "/test";
 Document expectedDocument = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testLoadInputStreamWithXmlCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(CHARSET_XML_HEADER.getBytes(StandardCharsets.UTF_8));
 String charsetName = null;
 String baseUri = "/test";
 Document expectedDocument = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testLoadInputStreamWithXmlDeclaration() throws IOException {
 InputStream inputStream = new ByteArrayInputStream((XML_DECLARATION + META_CHARSET_XML).getBytes(StandardCharsets.UTF_8));
 String charsetName = null;
 String baseUri = "/test";
 Document expectedDocument = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testLoadInputStreamWithGb2312Charset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(META_CHARSET_HTML.getBytes(StandardCharsets.UTF_8));
 String charsetName = GB2312_CHARSET;
 String baseUri = "/test";
 Document expectedDocument = new Document(baseUri); // empty document
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertNull(actualDocument);
 }

 @Test
 public void testLoadInputStreamWithIso88591Charset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(META_CHARSET_HTML.getBytes(StandardCharsets.ISO_8859_1));
 String charsetName = ISO_8859_1_CHARSET;
 String baseUri = "/test";
 Document expectedDocument = new Document(baseUri); // empty document
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertNull(actualDocument);
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(META_CHARSET_HTML.getBytes(StandardCharsets.UTF_8));
 String charsetName = "invalidCharset";
 String baseUri = "/test";
 Document expectedDocument = new Document(baseUri); // empty document
 Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri, parser);
 assertNull(actualDocument);
 }

 @Test
 public void testParseInputStreamWithCharsetFromContentType() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(CHARSET_HEADER.getBytes(StandardCharsets.UTF_8));
 String charsetName = null;
 String baseUri = "/test";
 Document expectedDocument = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
 Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testParseInputStreamWithUtf8Charset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(META_CHARSET_HTML.getBytes(StandardCharsets.UTF_8));
 String charsetName = UTF8_CHARSET;
 String baseUri = "/test";
 Document expectedDocument = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
 Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testParseInputStreamWithXmlCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(CHARSET_XML_HEADER.getBytes(StandardCharsets.UTF_8));
 String charsetName = null;
 String baseUri = "/test";
 Document expectedDocument = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
 Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testParseInputStreamWithXmlDeclaration() throws IOException {
 InputStream inputStream = new ByteArrayInputStream((XML_DECLARATION + META_CHARSET_XML).getBytes(StandardCharsets.UTF_8));
 String charsetName = null;
 String baseUri = "/test";
 Document expectedDocument = parser.parse(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
 Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testParseInputStreamWithUnknownCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(META_CHARSET_HTML.getBytes(StandardCharsets.UTF_8));
 String charsetName = "unknownCharset";
 String baseUri = "/test";
 Document expectedDocument = new Document(baseUri); // empty document
 Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
 assertNull(actualDocument);
 }

 @Test
 public void testParseInputStreamWithGb2312Charset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(META_CHARSET_HTML.getBytes(StandardCharsets.UTF_8));
 String charsetName = GB2312_CHARSET;
 String baseUri = "/test";
 Document expectedDocument = new Document(baseUri); // empty document
 Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
 assertNull(actualDocument);
 }

 @Test
 public void testParseInputStreamWithIso88591Charset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(META_CHARSET_HTML.getBytes(StandardCharsets.ISO_8859_1));
 String charsetName = ISO_8859_1_CHARSET;
 String baseUri = "/test";
 Document expectedDocument = new Document(baseUri); // empty document
 Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
 assertNull(actualDocument);
 }

 @Test
 public void testParseInputStreamWithInvalidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(META_CHARSET_HTML.getBytes(StandardCharsets.UTF_8));
 String charsetName = "invalidCharset";
 String baseUri = "/test";
 Document expectedDocument = new Document(baseUri); // empty document
 Document actualDocument = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
 assertNull(actualDocument);
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(HTML_CONTENT_TYPE.getBytes(StandardCharsets.UTF_8));
 OutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(inputStream, outputStream);
 String actualContentType = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 String expectedContentType = HTML_CONTENT_TYPE;
 assertEquals(expectedContentType, actualContentType);
 }
}