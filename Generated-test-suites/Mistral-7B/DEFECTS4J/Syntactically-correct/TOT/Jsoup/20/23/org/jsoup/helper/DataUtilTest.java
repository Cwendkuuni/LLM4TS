import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private static final String TEST_HTML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " +
         "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
         "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
         "<head>\n" +
         "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
         "<meta charset=\"UTF-8\">\n" +
         "</head>\n" +
         "<body>\n" +
         "<p>Test</p>\n" +
         "</body>\n" +
         "</html>";

 private static final String TEST_GB2312_HTML = "<?xml version=\"1.0\" encoding=\"gb2312\"?>\n" +
         "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " +
         "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
         "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
         "<head>\n" +
         "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\n" +
         "<meta charset=\"gb2312\">\n" +
         "</head>\n" +
         "<body>\n" +
         "<p>Test</p>\n" +
         "</body>\n" +
         "</html>";

 private static Document testHtmlDocument;
 private static Document gb2312HtmlDocument;
 private static InputStream testInputStream;
 private static InputStream gb2312InputStream;
 private static Parser parser;

 @BeforeClass
 public static void setup() {
 MockitoAnnotations.initMocks(DataUtilTest.class);
 parser = DataUtil.Parser.htmlParser();
 testHtmlDocument = parser.parseInput(TEST_HTML, "http://example.com");
 gb2312HtmlDocument = parser.parseInput(TEST_GB2312_HTML, "http://example.com");
 testInputStream = new ByteArrayInputStream(TEST_HTML.getBytes(Charset.forName("UTF-8")));
 gb2312InputStream = new ByteArrayInputStream(TEST_GB2312_HTML.getBytes());
 }

 @Test
 public void testLoadFileWithUTF8Charset() throws IOException {
 Document document = DataUtil.load(new File("test.html"), "UTF-8", "http://example.com");
 assertEquals(testHtmlDocument, document);
 }

 @Test
 public void testLoadFileWithoutCharset() throws IOException {
 Document document = DataUtil.load(new File("test.html"), null, "http://example.com");
 assertEquals(testHtmlDocument, document);
 }

 @Test
 public void testLoadFileWithGB2312Charset() throws IOException {
 Document document = DataUtil.load(new File("test.gb2312.html"), "gb2312", "http://example.com");
 assertEquals(gb2312HtmlDocument, document);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidCharset() throws IOException {
 DataUtil.load(new File("test.html"), "ISO-8859-1", "http://example.com");
 }

 @Test
 public void testLoadInputStreamWithUTF8Charset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(TEST_HTML.getBytes(Charset.forName("UTF-8")));
 Document document = DataUtil.load(inputStream, "UTF-8", "http://example.com");
 assertEquals(testHtmlDocument, document);
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws IOException {
 Document document = DataUtil.load(testInputStream, null, "http://example.com");
 assertEquals(testHtmlDocument, document);
 }

 @Test
 public void testLoadInputStreamWithGB2312Charset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(TEST_GB2312_HTML.getBytes(Charset.forName("gb2312")));
 Document document = DataUtil.load(inputStream, "gb2312", "http://example.com");
 assertEquals(gb2312HtmlDocument, document);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 DataUtil.load(testInputStream, "ISO-8859-1", "http://example.com");
 }

 @Test
 public void testLoadInputStreamWithDifferentCharsets() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(TEST_HTML.getBytes(Charset.forName("UTF-8")));
 Document document = DataUtil.load(inputStream, "gb2312", "http://example.com");
 assertNull(document);
 }

 @Test
 public void testParseByteDataWithUTF8Charset() {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_HTML.getBytes(Charset.forName("UTF-8")));
 Document document = DataUtil.parseByteData(byteData, "UTF-8", "http://example.com", parser);
 assertEquals(testHtmlDocument, document);
 }

 @Test
 public void testParseByteDataWithNullCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_HTML.getBytes(Charset.forName("UTF-8")));
 Document document = DataUtil.parseByteData(byteData, null, "http://example.com", parser);
 assertEquals(testHtmlDocument, document);
 }

 @Test
 public void testParseByteDataWithGB2312Charset() {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_GB2312_HTML.getBytes(Charset.forName("gb2312")));
 Document document = DataUtil.parseByteData(byteData, "gb2312", "http://example.com", parser);
 assertEquals(gb2312HtmlDocument, document);
 }

 @Test
 public void testParseByteDataWithUnknownCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_HTML.getBytes(Charset.forName("UTF-8")));
 Document document = DataUtil.parseByteData(byteData, "ISO-8859-1", "http://example.com", parser);
 assertNull(document);
 }

 @Test
 public void testParseByteDataWithMidStreamCharsetChange() throws IOException {
 ByteArrayOutputStream byteDataStream = new ByteArrayOutputStream();
 byteDataStream.write(TEST_HTML.getBytes(Charset.forName("UTF-8")));
 byteDataStream.write(TEST_GB2312_HTML.getBytes(Charset.forName("gb2312"))[0]);
 ByteBuffer byteData = ByteBuffer.wrap(byteDataStream.toByteArray());
 Document document = DataUtil.parseByteData(byteData, null, null, parser);
 assertEquals(gb2312HtmlDocument, document);
 }
}