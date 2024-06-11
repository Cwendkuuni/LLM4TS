import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class DataUtilTest {

 private static Document doc;
 private static final String TEST_FILE_NAME = "testFile.html";
 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_BASE_URI = "http://test.com";
 private static final String TEST_MIME_BOUNDARY = DataUtil.mimeBoundary();

 @BeforeClass
 public static void setup() throws IOException {
 File testFile = new File(TEST_FILE_NAME);
 DataUtil.save(new ByteArrayOutputStream(), TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser(), "<html><body>Test Content</body></html>");
 testFile.createNewFile();
 InputStream testInputStream = new FileInputStream(testFile);
 doc = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI);
 }

 @Test
 public void testLoadFile() throws IOException {
 assertNotNull(doc);
 assertEquals("<html><body>Test Content</body></html>", doc.html());
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream testByteOutputStream = new ByteArrayOutputStream();
 DataUtil.save(testByteOutputStream, null, TEST_BASE_URI, Parser.htmlParser(), "<html><body>Test Content</body></html>");
 ByteArrayInputStream testByteInputStream = new ByteArrayInputStream(testByteOutputStream.toByteArray());
 Document docWithDefaultCharset = DataUtil.load(testByteInputStream, null, TEST_BASE_URI);
 assertEquals("<html><body>Test Content</body></html>", docWithDefaultCharset.html());
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream testByteOutputStream = new ByteArrayOutputStream();
 DataUtil.save(testByteOutputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser(), "<html><body>Test Content</body></html>");
 ByteArrayInputStream testByteInputStream = new ByteArrayInputStream(testByteOutputStream.toByteArray());
 Document docWithSpecifiedCharset = DataUtil.load(testByteInputStream, TEST_CHARSET, TEST_BASE_URI);
 assertEquals("<html><body>Test Content</body></html>", docWithSpecifiedCharset.html());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 ByteArrayOutputStream testByteOutputStream = new ByteArrayOutputStream();
 DataUtil.save(testByteOutputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser(), "<html><body>Test Content</body></html>");
 ByteArrayInputStream testByteInputStream = new ByteArrayInputStream(testByteOutputStream.toByteArray());
 Document docWithParser = DataUtil.load(testByteInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());
 assertEquals("<html><body>Test Content</body></html>", docWithParser.html()); // This test case may fail due to parser mismatch
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream in = new FileInputStream(new File("testFile.html"));
 OutputStream out = new ByteArrayOutputStream();
 DataUtil.crossStreams(in, out);
 Document docFromCrossStreams = DataUtil.parseByteData(ByteBuffer.wrap(out.toByteArray()), TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 assertEquals("<html><body>Test Content</body></html>", docFromCrossStreams.html());
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidCharset() throws IOException {
 File testFile = new File(TEST_FILE_NAME);
 DataUtil.load(new FileInputStream(testFile), "InvalidCharset", TEST_BASE_URI);
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidBaseUri() throws IOException {
 File testFile = new File(TEST_FILE_NAME);
 DataUtil.load(new FileInputStream(testFile), TEST_CHARSET, "InvalidBaseUri");
 }

 @Test
 public void testParseByteDataWithEmptyByteBuffer() {
 ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
 assertEquals(0, emptyByteBuffer.limit());
 Document docFromEmptyByteBuffer = DataUtil.parseByteData(emptyByteBuffer, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 assertNull(docFromEmptyByteBuffer);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_MIME_BOUNDARY.getBytes(StandardCharsets.UTF_8));
 byteData.put(UNICODE_BOM);
 String docData = "Test Content";
 byte[] docBytes = docData.getBytes(StandardCharsets.UTF_8);
 byteData.put(docBytes);
 Document docFromDefaultCharset = DataUtil.parseByteData(byteData, null, TEST_BASE_URI, Parser.htmlParser());
 assertEquals("<html><body>Test Content</body></html>", docFromDefaultCharset.html());
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_MIME_BOM.getBytes(StandardCharsets.UTF_8));
 byteData.put(UNICODE_BOM);
 String docData = "Test Content";
 byte[] docBytes = docData.getBytes(StandardCharsets.UTF_8);
 byteData.put(docBytes);
 Document docFromSpecifiedCharset = DataUtil.parseByteData(byteData, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 assertEquals("<html><body>Test Content</body></html>", docFromSpecifiedCharset.html());
 }

 @Test
 public void testParseByteDataWithInvalidCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_MIME_BOUNDARY.getBytes(StandardCharsets.UTF_8));
 byteData.put(UNICODE_BOM);
 String docData = "Test Content";
 byte[] docBytes = docData.getBytes(StandardCharsets.UTF_8);
 byteData.put(docBytes);
 Document docFromInvalidCharset = null;
 try {
 docFromInvalidCharset = DataUtil.parseByteData(byteData, "InvalidCharset", TEST_BASE_URI, Parser.htmlParser());
 } catch (IOException e) {
 // Expected
 }
 assertNull(docFromInvalidCharset);
 }

 @Test
 public void testParseByteDataWithMimeBoundary() {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_MIME_BOUNDARY.getBytes(StandardCharsets.UTF_8));
 byteData.put(UNICODE_BOM);
 String docData = "Test Content";
 byte[] docBytes = docData.getBytes(StandardCharsets.UTF_8);
 byteData.put(docBytes);
 byteData.put(TEST_MIME_BOUNDARY.getBytes(StandardCharsets.UTF_8));
 Document docFromMimeBoundary = DataUtil.parseByteData(byteData, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
 assertEquals("<html><body>Test Content</body></html>", docFromMimeBoundary.html());
 }

 @Test
 public void testParseByteDataWithMimeBoundaryAndInvalidCharset() {
 ByteBuffer byteData = ByteBuffer.wrap(TEST_MIME_BOUNDARY.getBytes(StandardCharsets.UTF_8));
 byteData.put(UNICODE_BOM);
 String docData = "Test Content";
 byte[] docBytes = docData.getBytes(StandardCharsets.UTF_8);
 byteData.put(docBytes);
 byteData.put(TEST_MIME_BOUNDARY.getBytes(StandardCharsets.UTF_8));
 Document docFromMimeBoundaryAndInvalidCharset = null;
 try {
 docFromMimeBoundaryAndInvalidCharset = DataUtil.parseByteData(byteData, "InvalidCharset", TEST_BASE_URI, Parser.htmlParser());
 } catch (IOException e) {
 // Expected
 }
 assertNull(docFromMimeBoundaryAndInvalidCharset);
 }

 @Test
 public void testGetCharsetFromContentType() {
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8; anotherHeader=value"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8\n anotherHeader=value"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8\n anotherHeader=value\n"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8\n anotherHeader=value\n anotherHeader=value"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8\n anotherHeader=value\n anotherHeader=value\n charset=ISO-8859-1"));
 assertEquals("ISO-8859-1", DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1"));
 assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=ISO-8859-1\n anotherHeader=value"));
 assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
 assertEquals(null, DataUtil.getCharsetFromContentType("text/html\n anotherHeader=value"));
 assertEquals(null, DataUtil.getCharsetFromContentType("text/html\n anotherHeader=value\n"));
 assertEquals(null, DataUtil.getCharsetFromContentType("text/html\n anotherHeader=value\n anotherHeader=value"));
 assertEquals(null, DataUtil.getCharsetFromContentType("text/html\n anotherHeader=value\n anotherHeader=value\n charset="));
 assertEquals(null, DataUtil.getCharsetFromContentType("text/html\n anotherHeader=value\n anotherHeader=value\n charset=".toUpperCase(Locale.ENGLISH)));
 }

 @Test
 public void testMimeBoundary() {
 assertEquals(boundaryLength, DataUtil.mimeBoundary().length());
 }
}