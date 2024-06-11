import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(PowerMockRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Document document;
 private InputStream inputStream;
 private OutputStream outputStream;
 private static final String CHARSET_NAME = "UTF-8";
 private static final String BASE_URI = "https://example.com";
 private static final String HTML_CONTENT = "<html><head><title>Test Page</title></head><body><p>Test content.</p></body></html>";

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 PowerMockito.mockStatic(DataUtil.class);
 dataUtil = new DataUtil();
 document = DataUtil.parseString(HTML_CONTENT, CHARSET_NAME, BASE_URI);
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 inputStream = new FileInputStream(file);
 document = dataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
 assertEquals(HTML_CONTENT, document.body().text());
 inputStream.close();
 }

 @Test
 public void testLoadInputStream() throws IOException {
 byte[] htmlBytes = HTML_CONTENT.getBytes(StandardCharsets.UTF_8);
 inputStream = new ByteArrayInputStream(htmlBytes);
 document = dataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
 assertEquals(HTML_CONTENT, document.body().text());
 inputStream.close();
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 byte[] htmlBytes = HTML_CONTENT.getBytes(StandardCharsets.UTF_8);
 inputStream = new ByteArrayInputStream(htmlBytes);
 assertThrows(IOException.class, () -> dataUtil.load(inputStream, null, BASE_URI));
 inputStream.close();
 }

 @Test
 public void testLoadInputStreamWithNullDocument() throws IOException {
 byte[] htmlBytes = HTML_CONTENT.getBytes(StandardCharsets.UTF_8);
 inputStream = new ByteArrayInputStream(htmlBytes);
 PowerMockito.when(DataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, DataUtil.Parser.htmlParser())).thenReturn(null);
 document = dataUtil.load(inputStream, CHARSET_NAME, BASE_URI);
 assertNull(document);
 inputStream.close();
 }

 @Test
 public void testParseInputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 dataUtil.crossStreams(inputStream, outputStream);
 inputStream.close();
 String parsedHtml = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
 document = DataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, DataUtil.Parser.htmlParser());
 assertEquals(HTML_CONTENT, document.body().text());
 }

 @Test
 public void testParseInputStreamWithNullDocument() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 PowerMockito.when(DataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, DataUtil.Parser.htmlParser()))
 .thenReturn(null);
 document = dataUtil.parseInputStream(inputStream, CHARSET_NAME, BASE_URI, DataUtil.Parser.htmlParser());
 assertNull(document);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 assertEquals(expectedCharset, DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = null;
 assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testValidateCharset() {
 String charset = "UTF-8";
 assertEquals(charset, DataUtil.validateCharset(charset));
 }

 @Test
 public void testValidateCharsetInvalid() {
 String charset = "InvalidCharset";
 assertNull(DataUtil.validateCharset(charset));
 }
}