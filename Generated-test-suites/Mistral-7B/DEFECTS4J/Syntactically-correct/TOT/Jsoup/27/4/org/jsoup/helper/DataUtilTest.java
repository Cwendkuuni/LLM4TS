import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

/**
 * Tests for the DataUtil class.
 */
public class DataUtilTest {

 private static String htmlWithCharsetMetaTag;
 private static String htmlWithContentTypeHeader;
 private static String htmlWithoutCharsetInfo;

 @BeforeClass
 public static void setup() throws IOException {
 htmlWithCharsetMetaTag = "" +
 "<!doctype html>\n" +
 "<html lang=\"en\">\n" +
 "<head>\n" +
 "<meta charset=\"UTF-8\">\n" +
 "</head>\n" +
 "<body>\n" +
 "</body>\n" +
 "</html>";

 htmlWithContentTypeHeader = "" +
 "<!doctype html>\n" +
 "<html lang=\"en\">\n" +
 "<head>\n" +
 "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
 "</head>\n" +
 "<body>\n" +
 "</body>\n" +
 "</html>";

 htmlWithoutCharsetInfo = "" +
 "<!doctype html>\n" +
 "<html lang=\"en\">\n" +
 "<head>\n" +
 "</head>\n" +
 "<body>\n" +
 "</body>\n" +
 "</html>";
 }

 /**
 * Test loading a file with a valid charset name.
 * @throws IOException
 */
 @Test
 public void testLoadFileValidCharset() throws IOException {
 File testFile = new File("testFile.html");
 testFile.createNewFile();
 try (PrintWriter writer = new PrintWriter(testFile)) {
 writer.write(htmlWithCharsetMetaTag);
 }
 Document doc = DataUtil.load(testFile, "UTF-8", "");
 assertEquals("UTF-8", doc.outputSettings().charset());
 testFile.delete();
 }

 /**
 * Test loading a file with no charset info.
 * @throws IOException
 */
 @Test
 public void testLoadFileNoCharset() throws IOException {
 File testFile = new File("testFile.html");
 testFile.createNewFile();
 try (PrintWriter writer = new PrintWriter(testFile)) {
 writer.write(htmlWithoutCharsetInfo);
 }
 Document doc = DataUtil.load(testFile, null, "");
 assertEquals("UTF-8", doc.outputSettings().charset());
 testFile.delete();
 }

 /**
 * Test loading a file with an invalid charset name.
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testLoadFileInvalidCharset() throws IOException {
 File testFile = new File("testFile.html");
 testFile.createNewFile();
 try (PrintWriter writer = new PrintWriter(testFile)) {
 writer.write(htmlWithCharsetMetaTag);
 }
 Document doc = null;
 try {
 doc = DataUtil.load(testFile, "invalidCharset", "");
 } catch (IOException e) {
 // expected
 }
 testFile.delete();
 assertNull(doc);
 }

 /**
 * Test parsing a stream with a valid charset name.
 * @throws IOException
 */
 @Test
 public void testLoadStreamValidCharset() throws IOException {
 InputStream testStream = new ByteArrayInputStream(htmlWithCharsetMetaTag.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(testStream, "UTF-8", "");
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 /**
 * Test parsing a stream with no charset info.
 * @throws IOException
 */
 @Test
 public void testLoadStreamNoCharset() throws IOException {
 InputStream testStream = new ByteArrayInputStream(htmlWithoutCharsetInfo.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(testStream, null, "");
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 /**
 * Test parsing a stream with an invalid charset name.
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testLoadStreamInvalidCharset() throws IOException {
 InputStream testStream = new ByteArrayInputStream(htmlWithCharsetMetaTag.getBytes(StandardCharsets.UTF_8));
 Document doc = null;
 try {
 doc = DataUtil.load(testStream, "invalidCharset", "");
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }

 /**
 * Test parsing a stream with a valid charset name specified in the content-type header.
 * @throws IOException
 */
 @Test
 public void testLoadStreamValidCharsetFromContentType() throws IOException {
 String contentType = "text/html; charset=UTF-8";
 InputStream testStream = new ByteArrayInputStream(htmlWithCharsetMetaTag.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(testStream, contentType, "");
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 /**
 * Test parsing a stream with no charset information in the content-type header.
 * @throws IOException
 */
 @Test
 public void testLoadStreamNoCharsetFromContentType() throws IOException {
 String contentType = "text/html";
 InputStream testStream = new ByteArrayInputStream(htmlWithCharsetMetaTag.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(testStream, contentType, "");
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 /**
 * Test parsing a stream with an invalid charset name in the content-type header.
 * @throws IOException
 */
 @Test(expected = IOException.class)
 public void testLoadStreamInvalidCharsetFromContentType() throws IOException {
 String contentType = "text/html; charset=invalidCharset";
 InputStream testStream = new ByteArrayInputStream(htmlWithCharsetMetaTag.getBytes(StandardCharsets.UTF_8));
 Document doc = null;
 try {
 doc = DataUtil.load(testStream, contentType, "");
 } catch (IOException e) {
 // expected
 }
 assertNull(doc);
 }
}