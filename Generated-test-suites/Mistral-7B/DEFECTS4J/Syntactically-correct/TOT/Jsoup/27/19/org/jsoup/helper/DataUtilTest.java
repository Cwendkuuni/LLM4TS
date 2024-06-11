import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class DataUtilTest {

 private Document doc;
 private String charsetName = "UTF-8";
 private String baseUri = "http://example.com";
 private Parser parser = Parser.htmlParser();

 @Before
 public void setUp() {
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 doc = DataUtil.load(file, charsetName, baseUri);
 assertTrue(doc != null);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8'></head><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 doc = DataUtil.load(inputStream, null, baseUri);
 assertTrue(doc != null);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<?xml version='1.0' encoding='ISO-8859-1'?>\n<html><head><meta charset='ISO-8859-1'></head><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 doc = DataUtil.load(inputStream, "ISO-8859-1", baseUri);
 assertTrue(doc != null);
 assertEquals("ISO-8859-1", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithMismatchedContentType() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("text/plain; charset=UTF-8\n<html><head><meta charset='ISO-8859-1'></head><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 try {
 DataUtil.load(inputStream, null, baseUri);
 fail("Expected IOException when content type does not match document.");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='UTF-8'></head><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), charsetName, baseUri, parser);
 assertTrue(doc != null);
 assertEquals("UTF-8", doc.outputSettings().charset());
 assertEquals("Test", doc.title());
 }

 @Test
 public void testParseByteDataWithDetectedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><head><meta charset='ISO-8859-1'></head><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, baseUri, parser);
 assertTrue(doc != null);
 assertEquals("ISO-8859-1", doc.outputSettings().charset());
 assertEquals("Test", doc.title());
 }

 @Test
 public void testParseByteDataWithMismatchedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<?xml version='1.0' encoding='UTF-8'?>\n<html><head><meta charset='ISO-8859-1'></head><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 try {
 DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), "UTF-8", baseUri, parser);
 fail("Expected IOException when charset does not match document.");
 } catch (IOException e) {
 // Expected
 }
 }
}