import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

 private static final String HTML_WITH_UTF8_CHARSET =
         "<html>\n" +
         "<head>\n" +
         "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
         "</head>\n" +
         "<body>\n" +
         "<p>Some text in UTF-8</p>\n" +
         "</body>\n" +
         "</html>";

 private static final String HTML_WITH_GB2312_CHARSET =
         "<html>\n" +
         "<head>\n" +
         "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GB2312\">\n" +
         "</head>\n" +
         "<body>\n" +
         "<p>Some text in GB2312</p>\n" +
         "</body>\n" +
         "</html>";

 @BeforeClass
 public static void init() {
     System.setProperty("jsoup.parseHtmlFiles", "true");
 }

 @Test
 public void testLoadFileWithUtf8Charset() throws IOException {
     File file = new File("test.html");
     file.getParentFile().mkdirs();
     file.createNewFile();
     try (FileWriter writer = new FileWriter(file)) {
         writer.write(HTML_WITH_UTF8_CHARSET, StandardCharsets.UTF_8);
     }

     Document doc = DataUtil.load(file, "UTF-8", "");
     assertNotNull(doc);
     assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadFileWithoutCharset() throws IOException {
     File file = new File("test.html");
     file.getParentFile().mkdirs();
     file.createNewFile();
     try (FileWriter writer = new FileWriter(file)) {
         writer.write(HTML_WITH_UTF8_CHARSET, StandardCharsets.UTF_8);
     }

     Document doc = DataUtil.load(file, null, "");
     assertNotNull(doc);
     assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidCharset() throws IOException {
     File file = new File("test.html");
     file.getParentFile().mkdirs();
     file.createNewFile();
     try (FileWriter writer = new FileWriter(file)) {
         writer.write(HTML_WITH_UTF8_CHARSET, StandardCharsets.UTF_8);
     }

     try (ByteArrayInputStream inputStream = new ByteArrayInputStream(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8))) {
         InputStream in = new ByteArrayInputStream(new byte[1]); // Empty input stream to simulate closed file
         Document doc = DataUtil.load(in, "INVALID_CHARSET", "");
     }
 }

 @Test
 public void testLoadInputStreamWithUtf8Charset() throws IOException {
     InputStream inputStream = new ByteArrayInputStream(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
     Document doc = DataUtil.load(inputStream, "UTF-8", "");
     assertNotNull(doc);
     assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithNoCharset() throws IOException {
     InputStream inputStream = new ByteArrayInputStream(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
     Document doc = DataUtil.load(inputStream, null, "");
     assertNotNull(doc);
     assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
     InputStream inputStream = new ByteArrayInputStream(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
     try (ByteArrayInputStream in = new ByteArrayInputStream(new byte[1])) {
         Document doc = DataUtil.load(in, "INVALID_CHARSET", "");
     }
 }

 @Test
 public void testLoadInputStreamWithDetectedCharset() throws IOException {
     InputStream inputStream = new ByteArrayInputStream(HTML_WITH_GB2312_CHARSET.getBytes(StandardCharsets.UTF_8));
     Document doc = DataUtil.load(inputStream, null, "");
     assertEquals("GB2312", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharsetAndUtf8BOM() throws IOException {
     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     outputStream.write(65279); // UTF-8 BOM
     outputStream.write(HTML_WITH_UTF8_CHARSET.getBytes(StandardCharsets.UTF_8));
     InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

     Document doc = DataUtil.load(inputStream, "UTF-8", "");
     assertNotNull(doc);
     assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testDetectCharsetFromContentType() {
     String contentType = "text/html; charset=UTF-8";
     assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

     contentType = "text/html; charset=GB2312";
     assertEquals("GB2312", DataUtil.getCharsetFromContentType(contentType));

     contentType = "text/html";
     assertNull(DataUtil.getCharsetFromContentType(contentType));
 }

 @Test
 public void testDecodeByteBufferWithUtf8Charset() throws IOException {
     String html = "<html>\n" +
             "<head>\n" +
             "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
             "</head>\n" +
             "<body>\n" +
             "<p>Some text in UTF-8</p>\n" +
             "</body>\n" +
             "</html>";
     ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));

     Document doc = DataUtil.parseByteData(byteData, "UTF-8", "", Parser.htmlParser());
     assertNotNull(doc);
     assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testDecodeByteBufferWithDetectedCharset() throws IOException {
     String html = "<html>\n" +
             "<head>\n" +
             "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GB2312\">\n" +
             "</head>\n" +
             "<body>\n" +
             "<p>Some text in GB2312</p>\n" +
             "</body>\n" +
             "</html>";
     ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));

     Document doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
     assertEquals("GB2312", doc.outputSettings().charset());
 }

 @Test(expected = IOException.class)
 public void testDecodeByteBufferWithInvalidCharset() throws IOException {
     String html = "<html>\n" +
             "<head>\n" +
             "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=INVALID_CHARSET\">\n" +
             "</head>\n" +
             "<body>\n" +
             "<p>Some text in UTF-8</p>\n" +
             "</body>\n" +
             "</html>";
     ByteBuffer byteData = ByteBuffer.wrap(html.getBytes(StandardCharsets.UTF_8));

     Document doc = null;
     try {
         doc = DataUtil.parseByteData(byteData, null, "", Parser.htmlParser());
     } catch (IOException ex) {
         // Expected
     }
     assertNull(doc);
 }
}