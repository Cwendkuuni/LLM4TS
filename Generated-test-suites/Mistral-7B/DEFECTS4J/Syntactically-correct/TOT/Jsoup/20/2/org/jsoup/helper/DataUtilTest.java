package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class DataUtilTest {

 @BeforeEach
 public void setUp() {
 }

 @AfterEach
 public void tearDown() {
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File inputFile = new File("test.html");
 String baseUri = "http://example.com/";
 Document document = DataUtil.load(inputFile, null, baseUri);
 assertNotNull(document);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 File inputFile = new File("test.html");
 String charsetName = "ISO-8859-1";
 String baseUri = "http://example.com/";
 Document document = DataUtil.load(inputFile, charsetName, baseUri);
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 String testData = "<html><head></head><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
 String baseUri = "http://example.com/";
 Document document = DataUtil.load(inputStream, null, baseUri, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 String testData = "<html><head></head><body></body></html>";
 String charsetName = "ISO-8859-1";
 InputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
 String baseUri = "http://example.com/";
 Document document = DataUtil.load(inputStream, charsetName, baseUri, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 String testData = "<html><head></head><body></body></html>";
 ByteBuffer byteData = DataUtil.readToByteBuffer(testData.getBytes(StandardCharsets.UTF_8));
 String baseUri = "http://example.com/";
 Document document = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() {
 String testData = "<html><head></head><body></body></html>";
 ByteBuffer byteData = DataUtil.readToByteBuffer(testData.getBytes(StandardCharsets.UTF_8));
 String charsetName = "ISO-8859-1";
 String baseUri = "http://example.com/";
 Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
 assertNotNull(document);
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String expectedCharset = "UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(expectedCharset, charset);
 }

 @Test
 public void testGetCharsetFromContentTypeNull() {
 String contentType = null;
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNull(charset);
 }
}