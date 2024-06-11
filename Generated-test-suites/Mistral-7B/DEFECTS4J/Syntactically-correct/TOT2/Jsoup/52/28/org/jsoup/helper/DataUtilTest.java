import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

 private DataUtil dataUtil;
 private Document document;
 private InputStream inputStream;
 private OutputStream outputStream;
 private byte[] fileContent;

 @Before
 public void setup() {
 dataUtil = new DataUtil();
 document = Mockito.mock(Document.class);
 inputStream = mock(InputStream.class);
 outputStream = mock(OutputStream.class);
 fileContent = "some content".getBytes(StandardCharsets.UTF_8);
 }

 @Test
 public void testLoadFile() throws IOException {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("testFile.html"));
 document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamUTF8() throws IOException {
 inputStream = new ByteArrayInputStream(fileContent);
 document = DataUtil.load(inputStream, "UTF-8", null);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamUnknownCharset() throws IOException {
 inputStream = new ByteArrayInputStream(fileContent);
 document = DataUtil.load(inputStream, "ISO-8859-1", null); // unknown charset
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithParserXml() throws IOException {
 inputStream = new ByteArrayInputStream(fileContent);
 document = DataUtil.load(inputStream, "UTF-8", null, Parser.xmlParser());
 assertNotNull(document);
 }

 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(inputStream, outputStream);
 byte[] output = outputStream.toByteArray();
 assertEquals(fileContent.length, output.length);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 byte[] byteData = {(byte) 0xFF, (byte) 0xFE, 0, 1, 2}; // BOM + random bytes
 document = DataUtil.parseByteData(ByteBuffer.wrap(byteData), null, null, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test(expected = IOException.class)
 public void testParseByteDataWithInvalidCharset() {
 byte[] byteData = {(byte) 0xFF, (byte) 0xFE, 0, 1, 2}; // BOM + random bytes
 document = DataUtil.parseByteData(ByteBuffer.wrap(byteData), "invalidCharset", null, Parser.htmlParser());
 }
}