package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser parser;

 @Before
 public void setUp() {
 dataUtil = new DataUtil();
 parser = mock(Parser.class);
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("testFile.html");
 InputStream inputStream = new FileInputStream(file);
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Document expectedDoc = createTestDocument();
 when(parser.parseInput(any(String.class), eq(baseUri))).thenReturn(expectedDoc);
 Document actualDoc = dataUtil.load(file, charsetName, baseUri);
 assertEquals(expectedDoc, actualDoc);
 inputStream.close();
 }

 @Test
 public void testLoadFileWithInvalidCharsetName() throws IOException {
 File file = new File("testFile.html");
 InputStream inputStream = new FileInputStream(file);
 String charsetName = "";
 String baseUri = "http://test.com";
 Document expectedDoc = createTestDocument();
 when(parser.parseInput(any(String.class), eq(baseUri))).thenReturn(expectedDoc);
 Document actualDoc = dataUtil.load(file, charsetName, baseUri);
 assertEquals(expectedDoc, actualDoc);
 inputStream.close();
 }

 @Test
 public void testLoadFileWithDetectedCharset() throws IOException {
 File file = new File("testFile.html");
 InputStream inputStream = new FileInputStream(file);
 String charsetName = null;
 String baseUri = "http://test.com";
 Document expectedDoc = createTestDocument();
 when(parser.parseInput(any(String.class), eq(baseUri))).thenReturn(expectedDoc);
 when(parser.parseInput(any(String.class), any(String.class))).thenReturn(expectedDoc); // for decoding with detected charset
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[bufferSize];
 String docData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"EUC-JP\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
 outputStream.write(docData.getBytes());
 InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 when(parser.parseInput(testInputStream)).thenReturn(expectedDoc);
 ByteBuffer byteData = dataUtil.readToByteBuffer(inputStream);
 Document actualDoc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals(expectedDoc, actualDoc);
 inputStream.close();
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>".getBytes());
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Document expectedDoc = createTestDocument();
 when(parser.parseInput(any(String.class), eq(baseUri))).thenReturn(expectedDoc);
 Document actualDoc = dataUtil.load(inputStream, charsetName, baseUri);
 assertEquals(expectedDoc, actualDoc);
 }

 @Test
 public void testLoadInputStreamWithInvalidCharsetName() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>".getBytes());
 String charsetName = "";
 String baseUri = "http://test.com";
 Document expectedDoc = createTestDocument();
 when(parser.parseInput(any(String.class), eq(baseUri))).thenReturn(expectedDoc);
 Document actualDoc = dataUtil.load(inputStream, charsetName, baseUri);
 assertEquals(expectedDoc, actualDoc);
 }

 @Test
 public void testLoadInputStreamWithDetectedCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"EUC-JP\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>".getBytes());
 String charsetName = null;
 String baseUri = "http://test.com";
 Document expectedDoc = createTestDocument();
 when(parser.parseInput(any(String.class), eq(baseUri))).thenReturn(expectedDoc);
 when(parser.parseInput(any(InputStream.class), any(String.class))).thenReturn(expectedDoc); // for decoding with detected charset
 Document actualDoc = dataUtil.load(inputStream, charsetName, baseUri);
 assertEquals(expectedDoc, actualDoc);
 }

 @Test
 public void testLoadInputStreamWithDifferentParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>".getBytes());
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Parser parserXml = mock(Parser.Parser.class);
 Document expectedDoc = createTestDocument();
 when(parserXml.parseInput(any(String.class), eq(baseUri))).thenReturn(expectedDoc);
 when(parser.parseInput(any(InputStream.class), any(String.class))).thenReturn(parserXml);
 Document actualDoc = dataUtil.load(inputStream, charsetName, baseUri, parserXml);
 assertEquals(expectedDoc, actualDoc);
 }

 @Test
 public void testParseByteDataWithCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[bufferSize];
 String docData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
 outputStream.write(docData.getBytes());
 InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 String charsetName = "UTF-8";
 String baseUri = "http://test.com";
 Document expectedDoc = createTestDocument();
 when(parser.parseInput(any(String.class), eq(baseUri))).thenReturn(expectedDoc);
 Document actualDoc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals(expectedDoc, actualDoc);
 }

 @Test
 public void testParseByteDataWithInvalidCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[bufferSize];
 String docData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
 outputStream.write(docData.getBytes());
 InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 String charsetName = "";
 String baseUri = "http://test.com";
 Document expectedDoc = createTestDocument();
 when(parser.parseInput(any(String.class), eq(baseUri))).thenReturn(expectedDoc);
 Document actualDoc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals(expectedDoc, actualDoc);
 }

 @Test
 public void testParseByteDataWithDetectedCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[bufferSize];
 String docData = "<html>\n<head>\n<meta charset=\"EUC-JP\">\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
 outputStream.write(docData.getBytes());
 InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 String charsetName = null;
 String baseUri = "http://test.com";
 Document expectedDoc = createTestDocument();
 when(parser.parseInput(any(String.class), eq(baseUri))).thenReturn(expectedDoc);
 when(parser.parseInput(any(InputStream.class), any(String.class))).thenReturn(expectedDoc); // for decoding with detected charset
 Document actualDoc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertEquals(expectedDoc, actualDoc);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
 ByteBuffer byteData = dataUtil.readToByteBuffer(inputStream);
 byte[] expectedData = "Test".getBytes();
 byte[] actualData = new byte[byteData.limit()];
 byteData.get(actualData);
 assertEquals(expectedData, actualData);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=EUC-JP";
 String expectedCharset = "EUC-JP";
 String actualCharset = dataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, actualCharset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithNullContentType() {
 String contentType = null;
 String expectedCharset = null;
 String actualCharset = dataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, actualCharset);
 }

 private Document createTestDocument() {
 return parser.parseInput("<html><body><p>Test</p></body></html>");
 }
}