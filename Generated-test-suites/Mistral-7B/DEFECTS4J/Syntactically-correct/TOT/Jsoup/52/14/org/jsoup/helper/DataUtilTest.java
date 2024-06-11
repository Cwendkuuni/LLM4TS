package org.jsoup.helper.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

 private static final String TEST_HTML_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n" +
         "<head>\n" +
         "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
         "<title>Test</title>\n" +
         "</head>\n" +
         "<body>\n" +
         "<p>Hello World!</p>\n" +
         "</body>\n" +
         "</html>";

 @Rule
 public Random random = new Random();

 @Before
 public void setUp() {
 }

 @After
 public void tearDown() {
 }

 /**
 * Test method for {@link DataUtil#load(File, String, String)}
 */
 @Test
 public void testLoadFile() throws IOException {
 Document doc = DataUtil.load(new File("src/test/java/org/jsoup/helper/test/testFile.html"), "UTF-8", null);
 assertNotNull(doc);
 assertEquals(TEST_HTML_CONTENT, doc.html());
 }

 /**
 * Test method for {@link DataUtil#load(InputStream, String, String)}
 */
 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(TEST_HTML_CONTENT.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, "UTF-8", null);
 assertNotNull(doc);
 assertEquals(TEST_HTML_CONTENT, doc.html());
 }

 /**
 * Test method for {@link DataUtil#load(InputStream, String, String, Parser)}
 */
 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(TEST_HTML_CONTENT.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.htmlParser());
 assertNotNull(doc);
 assertEquals(TEST_HTML_CONTENT, doc.html());
 }

 /**
 * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} when charset is not specified
 */
 @Test
 public void testParseByteDataNoCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_HTML_CONTENT.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.parseByteData(byteData, null, null, DataUtil.Parser.htmlParser());
 assertNotNull(doc);
 assertEquals(TEST_HTML_CONTENT, doc.html());
 }

 /**
 * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} when charset is specified
 */
 @Test
 public void testParseByteDataWithCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_HTML_CONTENT.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, DataUtil.Parser.htmlParser());
 assertNotNull(doc);
 assertEquals(TEST_HTML_CONTENT, doc.html());
 }

 /**
 * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} when charset is invalid
 */
 @Test(expected = IllegalArgumentException.class)
 public void testParseByteDataInvalidCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_HTML_CONTENT.getBytes(StandardCharsets.UTF_8));
 Document doc = DataUtil.parseByteData(byteData, "UTF-16BE", null, DataUtil.Parser.htmlParser());
 }

 /**
 * Test method for {@link DataUtil#parseByteData(ByteBuffer, String, String, Parser)} when document is empty
 */
 @Test
 public void testParseByteDataEmpty() {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, DataUtil.Parser.htmlParser());
 assertNull(doc);
 }

 /**
 * Test method for {@link DataUtil#readToByteBuffer(InputStream)}
 */
 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(TEST_HTML_CONTENT.getBytes(StandardCharsets.UTF_8));
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
 assertEquals(TEST_HTML_CONTENT.length(), byteBuffer.remaining());
 }

 /**
 * Test method for {@link DataUtil#readToByteBuffer(File)}
 */
 @Test
 public void testReadToByteBufferFile() throws IOException {
 File file = new File("src/test/java/org/jsoup/helper/test/testFile.html");
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(file);
 assertEquals(TEST_HTML_CONTENT.length(), byteBuffer.remaining());
 }

 /**
 * Test method for {@link DataUtil#crossStreams(InputStream, OutputStream)}
 */
 @Test
 public void testCrossStreams() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(TEST_HTML_CONTENT.getBytes(StandardCharsets.UTF_8));
 OutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(inputStream, outputStream);
 assertEquals(TEST_HTML_CONTENT, outputStream.toString(StandardCharsets.UTF_8.name()));
 }
}