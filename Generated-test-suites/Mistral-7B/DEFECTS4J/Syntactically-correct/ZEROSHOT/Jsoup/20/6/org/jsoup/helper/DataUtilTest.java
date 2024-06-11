package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 @Mock private Parser parser;
 @Mock private Element element;

 @Before
 public void setUp() {
 dataUtil = new DataUtil();
 when(parser.parseInput(anyString(), anyString())).thenReturn(mock(Document.class));
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("path/to/file.html");
 String charsetName = "UTF-8";
 String baseUri = "http://example.com/";
 Document doc = dataUtil.load(file, charsetName, baseUri);
 // Verify call to load method with File input, charsetName and baseUri
 // and check returned Document
 // ...
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes());
 String charsetName = "UTF-8";
 String baseUri = "http://example.com/";
 Document doc = dataUtil.load(inputStream, charsetName, baseUri);
 // Verify call to load method with InputStream input, charsetName and baseUri
 // and check returned Document
 // ...
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html></html>".getBytes());
 String charsetName = "UTF-8";
 String baseUri = "http://example.com/";
 Parser parser = mock(Parser.class);
 Document doc = dataUtil.load(inputStream, charsetName, baseUri, parser);
 // Verify call to load method with InputStream input, charsetName, baseUri and parser
 // and check returned Document
 // ...
 }

 @Test
 public void testParseByteData() throws IOException {
 String docData = "<html></html>";
 ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
 String charsetName = "UTF-8";
 String baseUri = "http://example.com/";
 Document doc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 // Verify call to parseByteData method with ByteBuffer byteData, charsetName, baseUri and parser
 // and check returned Document
 // ...
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("test".getBytes());
 ByteBuffer byteData = dataUtil.readToByteBuffer(inputStream);
 // Verify call to readToByteBuffer method with InputStream inputStream
 // and check returned ByteBuffer
 // ...
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=EUC-JP";
 String charset = dataUtil.getCharsetFromContentType(contentType);
 // Check returned charset
 // ...
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = null;
 String charset = dataUtil.getCharsetFromContentType(contentType);
 // Check returned charset is null
 // ...
 }
}