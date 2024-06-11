import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {

 private InputStream inputStream;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() throws IOException {
 inputStream = new FileInputStream(new File("path/to/your/test/file.html"));;
 outputStream = new ByteArrayOutputStream();
 }

 @After
 public void tearDown() throws IOException {
 inputStream.close();
 outputStream.close();
 }

 @Test
 public void testLoad() throws IOException {
 Document document = DataUtil.load(new File("path/to/your/test/file.html"), null, null);
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testLoadWithInvalidFile() throws IOException {
 DataUtil.load(new File("path/to/your/test/invalid/file"), null, null);
 }

 @Test
 public void testLoadWithCharset() throws IOException {
 String charsetName = "UTF-16";
 Document document = DataUtil.load(inputStream, charsetName, null);
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testLoadWithInvalidCharset() throws IOException {
 String charsetName = "INVALID_CHARSET";
 DataUtil.load(inputStream, charsetName, null);
 }

 @Test(expected = IOException.class)
 public void testLoadWithInputStream() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream("test data".getBytes(StandardCharsets.UTF_8));
 DataUtil.load(inputStream, null, null);
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
 Document document = DataUtil.parseByteData(byteBuffer, null, null, DataUtil.Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testCrossStreams() throws IOException {
 ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
 DataUtil.crossStreams(inputStream, outputStream);
 byte[] bytes = outputStream.toByteArray();
 Document document = DataUtil.parseByteData(ByteBuffer.wrap(bytes), null, null, DataUtil.Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(charset, "UTF-8");
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals(boundary.length(), 32);
 }
}