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
import java.io.File;
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
 private Document doc;
 private Parser parser;
 private InputStream inputStream;
 private File file;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 dataUtil = new DataUtil();
 parser = mock(Parser.class);
 }

 @Test
 public void testLoadFile() throws IOException {
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 file = new File("src/test/resources/test.html");
 doc = new Document();
 when(parser.parseInput(doc, baseUri)).thenReturn(doc);

 Document actual = dataUtil.load(file, charsetName, baseUri);
 assertEquals(doc, actual);
 }

 @Test
 public void testLoadInputStream() throws IOException {
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("test".getBytes());
 inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 Document expected = new Document();
 when(parser.parseByteData(byteData, charsetName, baseUri, parser)).thenReturn(expected);

 Document actual = dataUtil.load(inputStream, charsetName, baseUri, parser);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseByteData() throws IOException {
 String charsetName = null;
 String baseUri = "http://example.com";
 Parser parser = mock(Parser.class);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("test".getBytes());
 ByteBuffer byteData = ByteBuffer.wrap(outputStream.toByteArray());
 String docData = "test";
 when(parser.parseInput(docData, baseUri)).thenReturn(new Document());

 Document actual = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(actual);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=EUC-JP";
 String expected = "EUC-JP";
 String actual = dataUtil.getCharsetFromContentType(contentType);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = null;
 String actual = dataUtil.getCharsetFromContentType(contentType);
 assertNull(actual);
 }
}