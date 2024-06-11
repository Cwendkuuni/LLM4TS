import org.jsoup.helper.DataUtil;
import org.jsoup.helper.ConstrainableInputStream;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private Document document;
 @Mock
 private InputStream inputStream;
 @Mock
 private OutputStream outputStream;

 @Before
 public void setUp() throws Exception {
 Parser parser = Parser.htmlParser();
 document = new Document();
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";

 when(inputStream.readAllBytes()).thenReturn(getTestFileContent());

 Document result = DataUtil.load(inputStream, charsetName, baseUri);

 assertEquals(document, result);
 }

 @Test
 public void testLoadInputStreamWithUTF8Charset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(getTestFileContent());
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";

 Document result = DataUtil.load(inputStream, charsetName, baseUri);

 assertEquals(document, result);
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(getTestFileContent());
 String charsetName = "invalidCharset";
 String baseUri = "http://example.com";

 Document result = null;
 try {
 result = DataUtil.load(inputStream, charsetName, baseUri);
 } catch (Exception e) {
 // expected
 }

 assertEquals(document, result);
 }

 @Test
 public void testParseInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(getTestFileContent());
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = Parser.htmlParser();

 Document result = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

 assertEquals(document, result);
 }

 @Test
 public void testCrossStreams() throws IOException {
 InputStream inputStream = mock(InputStream.class);
 OutputStream outputStream = mock(OutputStream.class);

 byte[] testData = "testData".getBytes(StandardCharsets.UTF_8);
 ByteArrayOutputStream outputStreamMock = new ByteArrayOutputStream();

 when(inputStream.read(testData)).thenReturn(testData.length);
 when(inputStream.read(testData)).thenReturn(-1);
 when(outputStream.write(testData)).thenReturn(testData.length);
 when(outputStream.flush()).thenReturn(true);

 DataUtil.crossStreams(inputStream, outputStream);

 byte[] result = outputStreamMock.toByteArray();
 assert ArrayEquals(testData, result);
 }

 @Test
 public void testParseInputStreamWithValidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(getTestFileContentWithMetaCharset());
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = Parser.htmlParser();

 Document result = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

 assertEquals(document, result);
 }

 @Test
 public void testParseInputStreamWithInvalidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(getTestFileContentWithMetaCharset());
 String charsetName = "invalidCharset";
 String baseUri = "http://example.com";
 Parser parser = Parser.htmlParser();

 Document result = null;
 try {
 result = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
 } catch (Exception e) {
 // expected
 }

 assertEquals(document, result);
 }

 @Test
 public void testParseInputStreamWithXmlDeclaration() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(getTestFileContentWithXMLDeclaration());
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = Parser.xmlParser();

 Document result = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

 assertEquals(document, result);
 }

 @Test
 public void testParseInputStreamWithBOM() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(getTestFileContentWithBOM());
 String charsetName = "UTF-16";
 String baseUri = "http://example.com";
 Parser parser = Parser.htmlParser();

 Document result = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

 assertEquals(document, result);
 }

 @Test
 public void testParseInputStreamWithBOMAndValidCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(getTestFileContentWithBOMAndUTF8());
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = Parser.htmlParser();

 Document result = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);

 assertEquals(document, result);
 }

 private byte[] getTestFileContent() {
 return "testContent".getBytes(StandardCharsets.UTF_8);
 }

 private byte[] getTestFileContentWithMetaCharset() {
 return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>".getBytes(StandardCharsets.UTF_8);
 }

 private byte[] getTestFileContentWithXMLDeclaration() {
 return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><head></head><body></body></html>".getBytes(StandardCharsets.UTF_8);
 }

 private byte[] getTestFileContentWithBOM() {
 return (new String(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00}) + "testContent").getBytes(StandardCharsets.UTF_16LE);
 }

 private byte[] getTestFileContentWithBOMAndUTF8() {
 return (new String(new byte[]{(byte) 0xFF, (byte) 0xFE}) + "testContent").getBytes(StandardCharsets.UTF_8);
 }
}