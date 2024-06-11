package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 @Mock
 private File inputFile;
 @Mock
 private InputStream inputStream;
 @Mock
 private Parser parser;

 @Before
 public void setup() {
 dataUtil = new DataUtil();
 when(inputFile.exists()).thenReturn(true);
 when(inputFile.toInputStream()).thenReturn(inputStream);
 when(inputStream.readAllBytes()).thenReturn(new byte[0]);
 when(parser.parseInput(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(mock(Document.class));
 }

 @Test
 public void testLoadFile() throws IOException {
 File file = new File("test.html");
 Document doc = dataUtil.load(file, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithCharset() throws IOException {
 File file = new File("test.html");
 String charset = "UTF-8";
 Document doc = dataUtil.load(file, charset, null);
 assertNotNull(doc);
 assertEquals(charset, doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[0]);
 Document doc = dataUtil.load(input, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithCharset() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[0]);
 String charset = "UTF-8";
 Document doc = dataUtil.load(input, charset, null);
 assertNotNull(doc);
 assertEquals(charset, doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream input = new ByteArrayInputStream(new byte[0]);
 Parser parser = mock(Parser.class);
 Document doc = dataUtil.load(input, null, null, parser);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
 String charsetName = null;
 String baseUri = null;
 Parser parser = mock(Parser.class);
 Document doc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[0]);
 String charsetName = "UTF-8";
 String baseUri = null;
 Parser parser = mock(Parser.class);
 Document doc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(doc);
 assertEquals(charsetName, doc.outputSettings().charset());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=EUC-JP";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("EUC-JP", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeInvalid() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }
}