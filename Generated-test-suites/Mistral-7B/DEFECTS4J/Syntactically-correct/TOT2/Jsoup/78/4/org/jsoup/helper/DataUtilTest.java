import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Document document;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataUtil = new DataUtil();
 }

 @Test
 public void testLoadFileDocument() throws IOException {
 String xmlContent = "<xml><tag>Test</tag></xml>";
 File file = new File("test.xml");
 file.createNewFile();
 try (OutputStream outputStream = new FileOutputStream(file)) {
 outputStream.write(xmlContent.getBytes(StandardCharsets.UTF_8));
 }

 document = dataUtil.load(file, "UTF-8", null);
 assertNotNull(document);
 assertEquals("<xml><tag>Test</tag></xml>", document.html());
 }

 @Test
 public void testLoadInputStreamDocument() throws IOException {
 String xmlContent = "<xml><tag>Test</tag></xml>";
 InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
 document = dataUtil.load(inputStream, "UTF-8", null);
 assertNotNull(document);
 assertEquals("<xml><tag>Test</tag></xml>", document.html());
 }

 @Test
 public void testParseInputStreamDocument() throws IOException {
 String xmlContent = "<xml><tag>Test</tag></xml>";
 InputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
 document = dataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
 assertNotNull(document);
 assertEquals("<xml><tag>Test</tag></xml>", document.html());
 }

 @Test(expected = IOException.class)
 public void testLoadFileDocumentWithInvalidCharset() throws IOException {
 String xmlContent = "<xml><tag>Test</tag></xml>";
 File file = new File("test.xml");
 file.createNewFile();
 try (OutputStream outputStream = new FileOutputStream(file)) {
 outputStream.write(xmlContent.getBytes(StandardCharsets.UTF_8));
 }

 try {
 document = dataUtil.load(file, "invalidCharset", null);
 } finally {
 file.delete();
 }
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test".getBytes(StandardCharsets.UTF_8));
 OutputStream outputStream = new ByteArrayOutputStream();
 dataUtil.crossStreams(inputStream, outputStream);
 assertEquals("Test", outputStream.toString(StandardCharsets.UTF_8));
 }
}