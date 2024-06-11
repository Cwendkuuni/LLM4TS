package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Parser parser;
 private Document document;
 private InputStream inputStream;
 private File file;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataUtil = new DataUtil();
 parser = mock(Parser.class);
 document = mock(Document.class);
 inputStream = mock(InputStream.class);
 file = mock(File.class);
 }

 @Test
 public void testLoadFromFile() throws IOException {
 when(parser.parseInput(anyString(), anyString())).thenReturn(document);
 when(file.exists()).thenReturn(true);
 when(file.length()).thenReturn(10);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Your content here".getBytes());
 FileInputStream fileInputStream = new FileInputStream(file);
 fileInputStream.transferTo(outputStream);

 ByteBuffer expectedByteData = ByteBuffer.wrap(outputStream.toByteArray());
 when(file.getAbsolutePath()).thenReturn("path/to/file");

 ByteBuffer actualByteData = dataUtil.load(file, null, "").getByteData();
 assertEquals(expectedByteData, actualByteData);
 }

 @Test(expected = IOException.class)
 public void testLoadFromFileWithException() throws IOException {
 when(parser.parseInput(anyString(), anyString())).thenReturn(document);
 when(file.exists()).thenReturn(false);

 throw new IOException("Expected exception");
 }

 @Test
 public void testLoadFromInputStream() throws IOException {
 when(parser.parseInput(anyString(), anyString())).thenReturn(document);

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Your content here".getBytes());
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

 ByteBuffer expectedByteData = ByteBuffer.wrap(outputStream.toByteArray());
 when(parser.parseByteData(any(ByteBuffer.class), anyString(), anyString(), eq(parser))).thenReturn(document);

 ByteBuffer actualByteData = dataUtil.load(inputStream, null, "").getByteData();
 assertEquals(expectedByteData, actualByteData);
 }

 @Test(expected = IOException.class)
 public void testLoadFromInputStreamWithException() throws IOException {
 when(parser.parseInput(anyString(), anyString())).thenReturn(document);

 throw new IOException("Expected exception");
 }

 @Test
 public void testParseByteData() throws IOException {
 String charsetName = "UTF-8";
 String baseUri = "";
 String docData = "<html><head></head><body></body></html>";

 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
 when(parser.parseByteData(byteData, charsetName, baseUri, parser())).thenReturn(document);

 Document actualDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, parser());
 assertEquals(document, actualDocument);
 }

 @Test
 public void testDetectCharset() throws IOException {
 String docData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"></head><body></body></html>";

 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
 when(parser.parseByteData(byteData, null, "", parser())).thenReturn(document);
when(document.select("meta[http-equiv=content-type]").first()).thenReturn(mock(Element.class));

 Document actualDocument = dataUtil.parseByteData(byteData, null, "", parser());
 assertEquals(document, actualDocument);

 String actualCharset = actualDocument.outputSettings().charset();
 assertEquals("gb2312", actualCharset);
 }

 @Test
 public void testDetectCharsetFail() throws IOException {
 String docData = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html\"></head><body></body></html>";

 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
 when(parser.parseByteData(byteData, null, "", parser())).thenReturn(document);
when(document.select("meta[http-equiv=content-type]").first()).thenReturn(null);

 Document actualDocument = dataUtil.parseByteData(byteData, null, "", parser());
 assertEquals(document, actualDocument);

 String actualCharset = actualDocument.outputSettings().charset();
 assertEquals("UTF-8", actualCharset);
 }
}