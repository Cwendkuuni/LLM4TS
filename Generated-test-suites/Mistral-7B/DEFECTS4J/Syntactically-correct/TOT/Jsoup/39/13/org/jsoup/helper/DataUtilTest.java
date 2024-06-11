import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private static final String HTML_CONTENT_TYPE = "text/html; charset=UTF-8";
 private static final String CHARSET_META_TAG = "Content-Type: text/html; charset=ISO-8859-1";
 private static final String CHARSET_HTML5_META_TAG = "<meta charset='UTF-16'>";
 private static final String CHARSET_GB2312_META_TAG = "Content-Type: text/html; charset=gb2312";
 private static final String CHARSET_INVALID_META_TAG = "Content-Type: text/html; charset=invalid_charset";
 private static final String BOM_UTF8_DATA = "\ufeffhello world";
 private static final String HTML_DATA = "<!DOCTYPE html>\n<html>\n<head>\n<meta charset='UTF-8'>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
 private static final String GB2312_DATA = "\u7b80\u4e16\u754c\u9762\n<!DOCTYPE html>\n<html>\n<head>\n<meta charset='gb2312'>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
 private Document htmlDocument;
 private Document gb2312Document;
 private InputStream htmlInputStream;
 private InputStream gb2312InputStream;
 private InputStream charsetInvalidInputStream;
 private InputStream bomInputStream;
 private Random random;

 @Before
 public void setUp() {
 try {
 htmlDocument = DataUtil.parseString(HTML_DATA);
 gb2312Document = DataUtil.parseString(GB2312_DATA);
 htmlInputStream = new ByteArrayInputStream(HTML_DATA.getBytes(StandardCharsets.UTF_8));
 gb2312InputStream = new ByteArrayInputStream(GB2312_DATA.getBytes(StandardCharsets.UTF_8));
 charsetInvalidInputStream = new ByteArrayInputStream(CHARSET_INVALID_META_TAG.getBytes(StandardCharsets.UTF_8));
 bomInputStream = new ByteArrayInputStream(BOM_UTF8_DATA.getBytes(StandardCharsets.UTF_8));
 } catch (IOException e) {
 throw new RuntimeException(e);
 }
 MockitoAnnotations.initMocks(this);
 random = new Random();
 }

 @Test
 public void testLoadFileWithCharsetName() throws IOException {
 File file = new File("test.html");
 assertEquals(htmlDocument, DataUtil.load(file, HTML_CONTENT_TYPE, null));
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File file = new File("test.html");
 assertEquals(htmlDocument, DataUtil.load(file, null, null));
 }

 @Test
 public void testLoadInputStreamWithCharsetName() throws IOException {
 InputStream inputStream = htmlInputStream;
 assertEquals(htmlDocument, DataUtil.load(inputStream, HTML_CONTENT_TYPE, null));
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 InputStream inputStream = htmlInputStream;
 assertEquals(htmlDocument, DataUtil.load(inputStream, null, null));
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream inputStream = htmlInputStream;
 assertEquals(htmlDocument, DataUtil.load(inputStream, HTML_CONTENT_TYPE, null, Parser.htmlParser()));
 }

 @Test
 public void testLoadInputStreamWithInvalidCharsetName() {
 assertThrows(IllegalArgumentException.class, () -> DataUtil.load(charsetInvalidInputStream, null, null));
 }

 @Test
 public void testLoadByteBufferWithCharsetName() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(htmlInputStream);
 assertEquals(htmlDocument, DataUtil.parseByteData(byteData, HTML_CONTENT_TYPE, null, Parser.htmlParser()));
 }

 @Test
 public void testLoadByteBufferWithDefaultCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(htmlInputStream);
 assertEquals(htmlDocument, DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()));
 }

 @Test
 public void testLoadByteBufferWithMetaTagCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(gb2312InputStream);
 assertEquals(gb2312Document, DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()));
 }

 @Test
 public void testLoadByteBufferWithHtml5MetaTagCharset() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(CHARSET_HTML5_META_TAG.getBytes(StandardCharsets.UTF_8)));
 assertEquals(gb2312Document, DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()));
 }

 @Test
 public void testLoadByteBufferWithBomUtf8() {
 ByteBuffer byteData = DataUtil.readToByteBuffer(bomInputStream);
 assertEquals(htmlDocument, DataUtil.parseByteData(byteData, null, null, Parser.htmlParser()));
 }
}