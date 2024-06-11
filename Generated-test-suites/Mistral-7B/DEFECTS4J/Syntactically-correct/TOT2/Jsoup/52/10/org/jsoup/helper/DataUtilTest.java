import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class DataUtilTest {

 private static Document document;
 private static InputStream inputStream;
 private static File file;

 @BeforeClass
 public static void setup() throws IOException {
 // Basic functionality test for load(File) method
 document = DataUtil.load(new File("test.html"), "UTF-8", null);
 assertNotNull(document);

 // Basic functionality test for load(InputStream) method
 inputStream = new ByteArrayInputStream("<html><head></head><body></body></html>".getBytes("UTF-8"));
 document = DataUtil.load(inputStream, "UTF-8", null);
 assertNotNull(document);
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 // Test for loading a file with a default charset
 document = DataUtil.load(new File("testDefaultCharset.html"));
 assertNotNull(document);
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws IOException {
 // Test for loading a file with an invalid charset
 document = DataUtil.load(new File("testInvalidCharset.html"), "ISO-8859-1");
 assertNotNull(document);
 // Edge case: document should have a default charset if charset is invalid
 assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithNullCharset() throws IOException {
 // Test for loading an input stream with a null charset
 document = DataUtil.load(new ByteArrayInputStream("test".getBytes()), null);
 assertNotNull(document);
 // Edge case: document should have a default charset if charset is null
 assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 // Test for loading an input stream with an invalid charset
 document = DataUtil.load(new ByteArrayInputStream("test".getBytes()), "ISO-8859-1");
 assertNotNull(document);
 // Edge case: document should have a default charset if charset is invalid
 assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 // Test for loading an input stream with a specified charset
 document = DataUtil.load(new ByteArrayInputStream("test".getBytes()), "UTF-8");
 assertNotNull(document);
 // Edge case: document should have the specified charset if provided
 assertEquals("UTF-8", document.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithBOM() throws IOException {
 // Test for loading an input stream with a BOM
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write(0xFF);
 outputStream.write(0xFE);
 outputStream.write(0x00);
 outputStream.write(0x00);
 inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 document = DataUtil.load(inputStream, null);
 // Edge case: document should have a UTF-16LE charset if BOM is detected
 assertEquals("UTF-16LE", document.outputSettings().charset());
 }

 // Test for parseByteData method
 @Test
 public void testParseByteDataWithUTF8() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><head></head><body></body></html>".getBytes("UTF-8"));
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
 document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithUTF16() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write((byte) 0xFF);
 outputStream.write((byte) 0xFE);
 outputStream.write("<html><head></head><body></body></html>".getBytes("UTF-16LE"));
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
 document = DataUtil.parseByteData(byteData, "UTF-16LE", null, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithUTF32() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write((byte) 0x00);
 outputStream.write((byte) 0x00);
 outputStream.write((byte) 0xFE);
 outputStream.write((byte) 0xFF);
 outputStream.write("<html><head></head><body></body></html>".getBytes("UTF-32BE"));
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(outputStream.toByteArray()));
 document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertNotNull(document);
 // Edge case: document should have a UTF-32BE charset if BOM is detected
 assertEquals("UTF-32BE", document.outputSettings().charset());
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream("test".getBytes("UTF-8")));
 document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertNotNull(document);
 // Edge case: document should have a default charset if none is provided
 assertEquals(DataUtil.defaultCharset, document.outputSettings().charset());
 }

 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 File tempFile = File.createTempFile("test", null);
 OutputStream output = new FileOutputStream(tempFile);
 InputStream input = new ByteArrayInputStream("test".getBytes("UTF-8"));
 DataUtil.crossStreams(input, output);
 byte[] tempFileContent = new byte[tempFile.length()];
 FileInputStream fileInput = new FileInputStream(tempFile);
 fileInput.read(tempFileContent);
 assertEquals("test".getBytes("UTF-8"), tempFileContent);
 }
}