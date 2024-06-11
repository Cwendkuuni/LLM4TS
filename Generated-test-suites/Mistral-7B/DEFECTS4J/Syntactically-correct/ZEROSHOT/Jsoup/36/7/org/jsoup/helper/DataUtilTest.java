package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 @Mock private Parser parser;

 @Before
 public void setUp() {
 dataUtil = new DataUtil();
 }

 @Test
 public void testLoadFile() throws IOException {
 File inputFile = new File("test.html");
 String charsetName = "UTF-8";
 String baseUri = "http://test.com/";

 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>".getBytes());
 File tempFile = new File(outputStream.toByteArray());
 try (InputStream inputStream = new FileInputStream(tempFile)) {
 Document doc = dataUtil.load(inputFile, charsetName, baseUri);
 assertNotNull(doc);
 assertEquals(2, doc.select("h1").size());
 }

 // verify that load method was called with correct arguments
 verify(parser).parseInput(anyString(), anyString());
 }

 @Test
 public void testLoadInputStream() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>".getBytes());
 String charsetName = "UTF-8";
 String baseUri = "http://test.com/";

 Document doc = dataUtil.load(inputStream, charsetName, baseUri);
 assertNotNull(doc);
 assertEquals(2, doc.select("h1").size());

 // verify that load method was called with correct arguments
 verify(parser).parseInput(anyString(), anyString());
 }

 @Test
 public void testParseByteDataWithDefaultCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>".getBytes());
 String charsetName = null;
 String baseUri = "http://test.com/";
 Parser parser = Mockito.mock(Parser.class);

 Document doc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(doc);
 assertEquals(2, doc.select("h1").size());

 // verify that parseByteData method was called with correct arguments
 verify(parser).parseInput("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>", baseUri);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>".getBytes());
 String charsetName = "UTF-8";
 String baseUri = "http://test.com/";
 Parser parser = Mockito.mock(Parser.class);

 Document doc = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
 assertNotNull(doc);
 assertEquals(2, doc.select("h1").size());

 // verify that parseByteData method was called with correct arguments
 verify(parser).parseInput("<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>", baseUri);
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
 ByteBuffer byteData = dataUtil.readToByteBuffer(inputStream);
 assertEquals(6, byteData.limit());
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 assertEquals("UTF-8", dataUtil.getCharsetFromContentType(contentType));
 }
}